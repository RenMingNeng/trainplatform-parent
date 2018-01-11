package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ExamTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * 考试 监听器
 *
 * @author DF
 *
 */
@Component("queueExamListener")
public class QueueExamListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueExamListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_class_hours = "class_hours";
    private final String collectionName_exam_answers = "exam_answers";
    private final String collectionName_question_wrong_answers = "question_wrong_answers";

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private IExamAnswersService examAnswersService;
    @Autowired
    private IExamScoreService examScoreService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IBaseService<ExamScore> baseService;
    @Autowired
    private IExamDossierService examDossierService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private ICompanyTjService companyTjService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String userId = String.valueOf(map.get("userId"));
        String userName = String.valueOf(map.get("userName"));
        String detail = String.valueOf(map.get("detail"));
        String examNo = String.valueOf(map.get("examNo"));
        String examEndTime = String.valueOf(map.get("examEndTime"));
        String examTimeStr = String.valueOf(map.get("examTime"));
        String companyId = String.valueOf(map.get("companyId"));
        //examTimeStr 设置默认值0（排除小于0的情况）
        if(StringUtils.isEmpty(examTimeStr) ||
                (NumberUtils.isNumber(examTimeStr) && Integer.parseInt(examTimeStr) < 0)){
            examTimeStr = "0";
        }

        //判断值的合法性
        if(!verify(userId, examNo, examEndTime, companyId)){
            logger.error("QueueExamListener-----------verify:"+new Gson().toJson(map));
            return;
        }
        Double examTime = Double.parseDouble(examTimeStr);

        //查询考试编号
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("examNo", examNo);
        ExamPaperInfo examPaperInfo = examPaperInfoService.selectOne(params);

        if(null == examPaperInfo
                //1未提交
                || examPaperInfo.getExamStatus().equals("1")
                //比较试卷用户id是否跟提交用户一直
                || !examPaperInfo.getUserId().equals(userId)){
            return;
        }

        //获取解析信息
        Map<String, Object> data = examAnswersService.insert(detail, examNo, userName, examPaperInfo);
        if(null == data.get("answers") || !(data.get("answers") instanceof List)){
            return;
        }

        if(null == data.get("result") || !(data.get("result") instanceof Map)){
            return;
        }

        //公司统计表
        CompanyTj companyTj = companyTjService.selectOne(companyId, userName);

        Map<String, Object> result = (Map<String, Object>)data.get("result");
        List<ExamAnswers>  answersList = (List<ExamAnswers>)data.get("answers");
        //查询项目中所有题跟课程的关联关系
        Map<String, String> questionToCourse = courseQuestionService.selectCourseIdByQuestionId(examPaperInfo.getProjectId(), userId);
        for(ExamAnswers examAnswers : answersList){

            insertClassHourse(examAnswers, questionToCourse.get(examAnswers.getQuestion_id()), userId, userName, companyId);

            //保存答题记录
            examAnswers.setUser_id(userId);
            mongoTemplate.insert(examAnswers, collectionName_exam_answers);
        }

        //保存考试成绩
        save_paper_score(examTime, examEndTime, examPaperInfo, examNo, Integer.valueOf(result.get("my_score").toString()),
                String.valueOf(result.get("isPassed")), userId, userName);

        //跟新人员档案培训次数
        update_person_dossier(userId, userName, examPaperInfo.getProjectId(), companyTj);

        //正式考试
        if(ExamTypeEnum.ExamType_2.getValue().equals(examPaperInfo.getExamType())){
            String examStatus = String.valueOf(result.get("isPassed")) == "1" ? "2" : "3";
            String examTimeInfo = examPaperInfo.getCreateTime() + "至" + examEndTime;

            params = new ConcurrentHashMap<String, Object>();
            params.put("projectId", examPaperInfo.getProjectId());
            params.put("userId", userId);
            ProjectStatisticsInfo projectStatisticsInfo = projectStatisticsInfoService.selectOne(params);

            if(null == projectStatisticsInfo) {
                logger.error("QueueExamListener-----------update_project_statistics_info not exist with args:"+new Gson().toJson(params));
                return;
            }

            //修改考试信息表
            updateExamDossierInfo(examPaperInfo.getProjectId(), examStatus, projectStatisticsInfo, companyTj);

            //修改个人学时统计表
            update_project_statistics_info(userName, examNo, examTimeInfo,
                    result.get("my_score").toString(), examStatus, projectStatisticsInfo);
        }

        //统计表更新
        updateCompanyTj(companyTj, userName);
    }

    /**
     * 判断参数
     * @return
     */
    public boolean verify(String userId, String examNo, String examEndTime, String companyId){
        if(StringUtils.isEmpty(userId)){
            logger.info("QueueExamListener-----------userId为空");
            return false;
        }

        if(StringUtils.isEmpty(examNo)){
            logger.info("QueueExamListener-----------examNo为空");
            return false;
        }

        if(StringUtils.isEmpty(examEndTime)){
            logger.info("QueueExamListener-----------examEndTime为空");
            return false;
        }

        if(StringUtils.isEmpty(companyId)){
            logger.info("QueueExamListener-----------companyId为空");
            return false;
        }
        return true;
    }

    //保存考试成绩
    public void save_paper_score(Double examTime, String examEndTime, ExamPaperInfo examPaperInfo,
                           String examNo, Integer score, String isPassed, String userId, String userName){
//        Double examTime = Double.parseDouble((DateUtils.parseDateTime(examEndTime).getTime() -
//                DateUtils.parseDateTime(startTime).getTime() + ".00"));
        String examDuration = new DecimalFormat("0.00").format(examTime / 60);
        ExamScore examScore = new ExamScore(
                examPaperInfo.getProjectId(),
                examNo,
                userId,
                examPaperInfo.getExamType(),
                score,
                examPaperInfo.getCreateTime(),
                isPassed,
                Double.parseDouble(examDuration)
        );
        examScoreService.insert(baseService.build(userName, examScore));
    }

    /**
     * 更新个人统计信息
     * @param userName
     * @param examNo //试卷编号
     * @param examTimeInfo //用户考试时间：开始时间+结束时间
     * @param examScore //考试成绩
     * @param examStatus //考试状态
     * @param projectStatisticsInfo
     */
    public void update_project_statistics_info(String userName, String examNo, String examTimeInfo,
                String examScore, String examStatus, ProjectStatisticsInfo projectStatisticsInfo){
        //成绩没有记录的高时不修改
        if(null != projectStatisticsInfo.getExamScore() && !projectStatisticsInfo.getExamScore().equals("")
                && !projectStatisticsInfo.getExamScore().equals("\\") &&
                Integer.parseInt(projectStatisticsInfo.getExamScore()) > Integer.parseInt(examScore)){
            return;
        }

        //执行修改
        projectStatisticsInfo.setExamNo(examNo);
        projectStatisticsInfo.setExamTimeInfo(examTimeInfo);
        projectStatisticsInfo.setExamScore(examScore);
        projectStatisticsInfo.setExamStatus(examStatus);
        projectStatisticsInfo.setOperUser(userName);
        projectStatisticsInfo.setOperTime(DateUtils.formatDateTime(new Date()));
        projectStatisticsInfoService.update(projectStatisticsInfo);
    }

    /**
     * 修改考试信息表
     * @param projectId
     * @param examStatus
     * @param projectStatisticsInfo
     */
    public void updateExamDossierInfo(String projectId, String examStatus,
                                      ProjectStatisticsInfo projectStatisticsInfo, CompanyTj companyTj){
        //原本合格状态，不需要继续
        if(projectStatisticsInfo.getExamStatus().equals("2") ||
                (projectStatisticsInfo.getExamStatus().equals("3") && examStatus.equals("3"))){
            return;
        }
        //更新考试信息表
        ExamDossierInfo examDossierInfoOld = examDossierService.selectOne(projectId);
        if(null == examDossierInfoOld){
            ProjectInfo projectInfo = projectInfoService.selectProjectInfoById(projectId);
            examDossierInfoOld = new ExamDossierInfo(projectId, projectInfo.getPersonCount());
            examDossierService.insert(examDossierInfoOld);
        }
        Integer yetExamCount = examDossierInfoOld.getYetExamCount();    //已考人数
        Integer notExamCount = examDossierInfoOld.getNotExamCount();    //未考人数
        Integer qualifiedCount = examDossierInfoOld.getQualifiedCount();  //合格人数
        Integer unqualifiedCount = examDossierInfoOld.getUnqualifiedCount(); //不合格人数
        //当为考试时
        if(null == projectStatisticsInfo.getExamScore() || projectStatisticsInfo.getExamScore().equals("")
                || projectStatisticsInfo.getExamScore().equals("\\")){
            ++yetExamCount;
            --notExamCount;
        }
        //当考试状态是未考试（1）和不合格（3）时，且当前状态是合格状态时
        //之前
        if(projectStatisticsInfo.getExamStatus().equals("1") && examStatus.equals("2")){
            ++qualifiedCount;
        }
        //之前考试未考试，当前考试不合格
        if(projectStatisticsInfo.getExamStatus().equals("1") && examStatus.equals("3")){
            ++unqualifiedCount;
        }
        //之前未合格，当前考试合格
        if(projectStatisticsInfo.getExamStatus().equals("3") && examStatus.equals("2")){
            ++qualifiedCount;
            --unqualifiedCount;
        }
        //之前考试未合格，当前考试合格时修改 考试合格人次----考试合格人次
        if(!projectStatisticsInfo.getExamStatus().equals("2") && examStatus.equals("2")){
            companyTj.setCountExamPassYes(Integer.parseInt(companyTj.getCountExamPassYes()) + 1 + "");
        }
        examDossierService.update(new ExamDossierInfo(projectId, yetExamCount, notExamCount, qualifiedCount, unqualifiedCount));
    }

    /**
     * 添加学时
     * @param examAnswers
     * @param user_id user_name
     */
    public void insertClassHourse(ExamAnswers examAnswers, String course_id, String user_id, String user_name, String companyId){
        String source = "";
        Long study_time = 0L;
        String is_correct = examAnswers.getIs_correct();
        if(is_correct.equals("1")){//答对
            source = "3";
            study_time = 30L;
        }else{//答错
            source = "4";
            study_time = 3L;

            //答错记录表
            long wrongCount = mongoTemplate.count(new Query(new Criteria().andOperator(
                        Criteria.where("question_id").is(examAnswers.getQuestion_id()),
                        Criteria.where("project_id").is(examAnswers.getProject_id()),
                        Criteria.where("user_id").is(user_id)
                    )),
                    collectionName_question_wrong_answers
            );
            if(wrongCount < 3){
                QuestionWrongAnswers questionWrongAnswers = new QuestionWrongAnswers(
                        examAnswers.getProject_id(),
                        examAnswers.getQuestion_id(),
                        user_id,
                        user_name,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                );
                mongoTemplate.insert(questionWrongAnswers, collectionName_question_wrong_answers);
            }
        }
        // 插入学时记录
        ClassHours classHours = new ClassHours(
                examAnswers.getProject_id(),
                course_id,
                user_id,
                source,
                study_time,
                companyId,
                user_name,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
        mongoTemplate.insert(classHours, collectionName_class_hours);
    }

    /**
     * 更新个人档案中培训次数
     * @param userId
     */
    public void update_person_dossier(String userId, String userName, String projectId, CompanyTj companyTj){
        //只检查考试项目
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("userId", userId);
        Map<String, Object>  personDossierMap = personDossierService.selectOne(params);
        if(null == personDossierMap){
            return;
        }

        //培训人次
        Object train_count = personDossierMap.get("train_count");
        if(null == train_count){
            //默认值
            train_count = 0;
        }

        //更新公司统计表 ---培训人数
        if(0 == Integer.parseInt(train_count.toString())){
            companyTj.setCountTrainUser(Integer.parseInt(companyTj.getCountTrainUser()) + 1 + "");
        }

        //更新公司统计表、人员档案 ---培训人次
        params = new ConcurrentHashMap<String, Object>();
        params.put("userId", userId);
        params.put("examStatus", 2);
        params.put("projectId", projectId);
        int count = examPaperInfoService.selectCount(params);

        //有除本次以外的考试记录时不增加培训人次
        if(count > 1){
            return;
        }

        //判断是否有学时记录
        params = new ConcurrentHashMap<String, Object>();
        params.put("userId", userId);
        params.put("projectId", projectId);
        ProjectStatisticsInfo projectStatisticsInfo = projectStatisticsInfoService.selectOne(params);
        if(null != projectStatisticsInfo && 0 != projectStatisticsInfo.getTotalStudyTime()){
            return;
        }

        //更新人员档案表---培训人次
        personDossierMap.put("userId", userId);
        personDossierMap.put("companyId", personDossierMap.get("company_id"));
        personDossierMap.put("trainCount", Long.parseLong(train_count.toString()) + 1);
        personDossierMap.put("operUser", userName);
        personDossierMap.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        personDossierService.update(personDossierMap);

        //更新统计表--培训人次
        companyTj.setCountTrain(Integer.parseInt(companyTj.getCountTrain()) + 1 + "");
        //更新统计表--考试人次
        companyTj.setCountExam(Integer.parseInt(companyTj.getCountExam()) + 1 + "");
    }

    /**
     * 更新统计表
     * @param companyTj
     * @param userName
     */
    public void updateCompanyTj(CompanyTj companyTj, String userName){
        int count_user = Integer.parseInt(companyTj.getCountUser());

        if(count_user > 0){
            //培训率
            Double percentTrainComplete = Double.parseDouble(new DecimalFormat("0.00").format(
                    new Double(companyTj.getCountTrainUser()) / new Double(count_user) * 100.00));
            companyTj.setPercentTrainComplete(percentTrainComplete.toString());
        }

        //最后操作时间、人员
        companyTj.setOperTime(DateUtils.formatDateTime(new Date()));
        companyTj.setOperUser(userName);

        Map<String, Object> companyTjMap = new HashMap<String, Object>();
        MapUtils.putAll(companyTjMap, companyTj);
        companyTjService.update(companyTjMap);
    }
}
