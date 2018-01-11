package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
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
 * 题库监听器
 *
 * @author DF
 *
 */
@Component("queueExerciseListener")
public class QueueExerciseListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueExerciseListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_class_hours = "class_hours";
    private final String collectionName_exercise_answers = "exercise_answers";
    private final String collectionName_question_wrong_answers = "question_wrong_answers";

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IBaseService<ExerciseAnswers> baseService;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IProjectExerciseOrderService projectExerciseOrderService;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private IProjectInfoService projectInfoService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String userId = (String) map.get("userId");
        String userName = (String) map.get("userName");
        String detail = (String) map.get("detail");
        String companyId = (String) map.get("companyId");

        String[] ansAry = detail.split(";");
        if(ansAry.length < 1){
            logger.error("QueueQuestionListener-----------projectCourseInfo not exist with args:"+new Gson().toJson(map));
            return;
        }

        //公司统计表
        CompanyTj companyTj = companyTjService.selectOne(companyId, userName);

        //查询项目中所有题跟课程的关联关系
        Map<String, String> questionToCourse = new HashedMap();
        ProjectInfo projectInfo = null;
        for(String ans : ansAry){

            if(null == ans || ans.equals("") || ans.indexOf("undefined") != -1){
                continue;
            }
//            project_id + "," + question_id + "," + answer + "," + isTrue;
            String[] params = ans.split(",");

            //求课程id
            String courseId = "";
            if(params.length < 5){
                if(questionToCourse.size() < 1){
                    questionToCourse = courseQuestionService.selectCourseIdByQuestionId(params[0], userId);
                }
                courseId = questionToCourse.get(params[1]);
            }else{
                //            project_id + "," + question_id + "," + answer + "," + isTrue + "," + course_id;
                courseId = params[4];
            }

            //求项目信息
            if(null == projectInfo){
                projectInfo = projectInfoService.selectProjectInfoById(params[0]);
            }

            //是否添加答题记录
            boolean isInsert = insertClassHourse(params[0], courseId, params[1], params[3], userId, userName, companyId, projectInfo, companyTj);
            if(isInsert){
                ExerciseAnswers exerciseAnswers = new ExerciseAnswers(
                        params[0],
                        courseId,
                        params[1],
                        userId,
                        params[2],
                        params[3],
                        userName,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                );
                mongoTemplate.insert(baseService.build(userName, exerciseAnswers), collectionName_exercise_answers);
            }
        }
    }

    /**
     * 添加学时
     * @param projectId
     * @param courseId
     * @param questionId
     * @param isTrue
     * @param userId
     * @param userName
     * @param companyId
     * @return
     */
    public boolean insertClassHourse(String projectId, String courseId, String questionId,
                                  String isTrue, String userId, String userName, String companyId,
                                     ProjectInfo projectInfo, CompanyTj companyTj){
        //学时表：答对存1条，答错存1条；
        //答题表：答对存3条，答错存1条
        //错题表：答错存3条
        boolean result = true;
        String source = "";
        Long studyTime = 0L;
        // 统计错题总学时  累加
        Long allStudyTime = 0L;
        //答对数量
        long answerCorrectCount = mongoTemplate.count(new Query(new Criteria().andOperator(
                Criteria.where("project_id").is(projectId),
                Criteria.where("question_id").is(questionId),
                Criteria.where("user_id").is(userId),
                Criteria.where("is_correct").is("1")
        )), collectionName_exercise_answers);
        //答错数量
        long answerWrongCount = mongoTemplate.count(new Query(new Criteria().andOperator(
                Criteria.where("project_id").is(projectId),
                Criteria.where("question_id").is(questionId),
                Criteria.where("user_id").is(userId),
                Criteria.where("is_correct").is("2")
        )), collectionName_exercise_answers);

        //答对
        if(isTrue.equals("1")){
            source = "1";
            studyTime = 30L;
            allStudyTime = 30L;

            //判断答对个数
            if(answerCorrectCount > 2){
                return false;
            }
        }else{
            //答错
            source = "2";
            studyTime = 3L;
            allStudyTime = 3L;

            //答错3次就不添加练习记录
            if(answerWrongCount > 0){
                //不添加练习记录
                result = false;

                Query query = new Query(new Criteria().andOperator(
                        Criteria.where("course_id").is(courseId),
                        Criteria.where("project_id").is(projectId),
                        Criteria.where("user_id").is(userId),
                        Criteria.where("source").is("2")
                ));
                List<ClassHours> classHourss = mongoTemplate.find(query, ClassHours.class, collectionName_class_hours);
                //先删除后添加
                if(null != classHourss && classHourss.size() > 0){
                    allStudyTime = 3 + classHourss.get(0).getStudy_time();

                    /*错题只记录一次，删除之前的一次*/
                    mongoTemplate.remove(query, collectionName_class_hours);
                }
            }

            //答错记录表
            long wrongCount = mongoTemplate.count(new Query(new Criteria().andOperator(
                    Criteria.where("question_id").is(questionId),
                    Criteria.where("project_id").is(projectId),
                    Criteria.where("user_id").is(userId)
            )), collectionName_question_wrong_answers);

            if(wrongCount < 3){//错题个数小于3时，添加
                QuestionWrongAnswers questionWrongAnswers = new QuestionWrongAnswers(
                        projectId,
                        questionId,
                        userId,
                        userName,
                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                );
                mongoTemplate.insert(questionWrongAnswers, collectionName_question_wrong_answers);
            }
        }
        // 插入学时记录
        ClassHours classHours = new ClassHours(
                projectId,
                courseId,
                userId,
                source,
                allStudyTime,
                companyId,
                userName,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
        mongoTemplate.insert(classHours, collectionName_class_hours);

        //计算 已答题量、答对题量 未答题量 答错题量
        Integer yetAnswered = 0;
        Integer correctAnswered = 0;
        Integer notAnswered = 0;
        Integer failAnswered = 0;
        if(answerCorrectCount == 0 && isTrue.equals("1")){
            correctAnswered += 1;
        }

        if(answerWrongCount == 0 && answerCorrectCount == 0 && isTrue.equals("2")){
            failAnswered += 1;
        }

        if(answerWrongCount > 0 && answerCorrectCount == 0 && isTrue.equals("1")){
            failAnswered -= 1;
        }

        if(answerCorrectCount == 0 && answerWrongCount == 0){
            yetAnswered += 1;
            notAnswered -=1;
        }

        //更新项目课程信息
        update_project_course_info(userId, userName, courseId, projectId, yetAnswered, correctAnswered, studyTime, projectInfo);

        //更新个人统计信息
        boolean isUpdateTrainCount = update_project_statistics_info(userId, userName, projectId, yetAnswered, correctAnswered, studyTime, companyTj);

        //更新个人档案
        update_person_dossier(userId, userName, companyId, projectId, studyTime, isUpdateTrainCount, companyTj);

        //更新练习排行信息
        update_project_exercise_order(userId, userName, projectId, yetAnswered, correctAnswered, notAnswered, failAnswered, studyTime);

        //更新公司统计表
        updateCompanyTj(studyTime, companyTj, userName);

        return result;
    }

    /**
     * 更新统计表
     * @param studyTime
     * @param companyTj
     * @param userName
     */
    public void updateCompanyTj(Long studyTime, CompanyTj companyTj, String userName){
        //更新统计信息
        Double total = Double.parseDouble(new DecimalFormat("0.00").format(
                (new Double(companyTj.getTotalClassHour()) * 3600.00 + studyTime) / 3600));
        Double total_year = Double.parseDouble(new DecimalFormat("0.00").format(
                (new Double(companyTj.getTotalYearClassHour()) * 3600.00 + studyTime) / 3600));
        companyTj.setTotalClassHour(total.toString());
        companyTj.setTotalYearClassHour(total_year.toString());

        int count_user = Integer.parseInt(companyTj.getCountUser());

        if(count_user != 0){
            //总学时、年度学时
            Double average_person_class_hour = Double.parseDouble(new DecimalFormat("0.00")
                    .format(total * 1.00 / count_user));
            Double average_year_class_hour = Double.parseDouble(new DecimalFormat("0.00")
                    .format(total_year * 1.00 / count_user));
            companyTj.setAveragePersonClassHour(average_person_class_hour.toString());
            companyTj.setAverageYearClassHour(average_year_class_hour.toString());

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

    /**
     * 更新个人档案
     * @param userId
     * @param userName
     * @param companyId
     * @param projectId
     * @param studyTime
     */
    public void update_person_dossier(String userId, String userName, String companyId, String projectId,
                                      Long studyTime, boolean isUpdateTrainCount, CompanyTj companyTj){
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("userId", userId);
        params.put("projectId", projectId);
        Map<String, Object>  personDossierMap = personDossierService.selectOne(params);

        if(null != personDossierMap) {
            Long total_studytime = Long.parseLong(personDossierMap.get("total_studytime").toString());
            Long trainCount = Long.parseLong(personDossierMap.get("train_count").toString());
            personDossierMap.put("userId", userId);
            personDossierMap.put("companyId", companyId);
            personDossierMap.put("yearStudytime", Long.parseLong(personDossierMap.get("year_studytime").toString()) + studyTime);
            personDossierMap.put("totalStudytime", total_studytime + studyTime);
            //更新培训人次
            if(isUpdateTrainCount){
                //判断是否考试
                params = new ConcurrentHashMap<String, Object>();
                params.put("userId", userId);
                params.put("examStatus", 2);
                params.put("projectId", projectId);
                int count = examPaperInfoService.selectCount(params);
                if(count < 1){
                    //更新统计表 -- 培训人次
                    personDossierMap.put("trainCount", Integer.parseInt(personDossierMap.get("train_count").toString()) + 1);
                    companyTj.setCountTrain(Integer.parseInt(companyTj.getCountTrain()) + 1 + "");
                }
            }

            //培训人数
            if(null != trainCount && trainCount == 0){
                companyTj.setCountTrainUser(Integer.parseInt(companyTj.getCountTrainUser()) + 1 + "");
            }

            personDossierMap.put("operUser", userName);
            personDossierMap.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            personDossierService.update(personDossierMap);
        } else {
            logger.error("QueueExerciseListener-----------personDossierMap not exist with args:"+new Gson().toJson(params));
        }
    }

    /**
     * 更新项目课程信息
     * @param userId
     * @param userName
     * @param courseId
     * @param projectId
     * @param studyTime
     */
    public void update_project_course_info(String userId, String userName, String courseId, String projectId,
                                           Integer yetAnswered, Integer correctAnswered, Long studyTime, ProjectInfo projectInfo){
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("courseId", courseId);
        params.put("userId", userId);
        ProjectCourseInfo projectCourseInfo = projectCourseInfoService.selectOne(params);

        if(null != projectCourseInfo) {
            correctAnswered = projectCourseInfo.getCorrectAnswered() + correctAnswered;
            yetAnswered = projectCourseInfo.getYetAnswered() + yetAnswered;

            //总答正确率：答对总题量/已答题量*100
            Double correctRate = 0.00;
            if(yetAnswered != 0){
                correctRate = Double.parseDouble(new DecimalFormat("0.0").
                        format(correctAnswered * 100 / yetAnswered));
            }

            //判断应修学时和已修学时，来更新finishStatus培训状态（-1未完成1完成）
            Long requirement_studytime = projectCourseInfo.getRequirementStudyTime();
            Long totalStudyTime = projectCourseInfo.getTotalStudyTime() + studyTime;
            String finishStatus = projectCourseInfo.getFinishStatus();

            //培训-练习、培训-练习-考试 项目根据学时判断完成状态
            boolean updateFinishStatusByTime = projectInfo.getProjectType().equals(ProjectTypeEnum.QuestionType_4.getValue()) ||
                    projectInfo.getProjectType().equals(ProjectTypeEnum.QuestionType_7.getValue());
            if(updateFinishStatusByTime && totalStudyTime >= requirement_studytime*60){
                finishStatus = "1";
            }

            // 练习项目、练习考试项目 根据题量修改完成状态
            boolean updateFinishStatusByQuestionCount = projectInfo.getProjectType().equals(ProjectTypeEnum.QuestionType_2.getValue()) ||
                    projectInfo.getProjectType().equals(ProjectTypeEnum.QuestionType_6.getValue());
            if(updateFinishStatusByQuestionCount && null != yetAnswered &&
                    yetAnswered.intValue() == projectCourseInfo.getTotalQuestion()){
                finishStatus = "1";
            }

            projectCourseInfo = new ProjectCourseInfo(
                    projectCourseInfo.getId(),
                    projectCourseInfo.getProjectId(),
                    projectCourseInfo.getCourseId(),
                    projectCourseInfo.getCourseName(),
                    projectCourseInfo.getClassHour(),
                    projectCourseInfo.getUserId(),
                    requirement_studytime,
                    totalStudyTime,
                    projectCourseInfo.getAnswerStudyTime() + studyTime,
                    projectCourseInfo.getTrainStudyTime(),
                    projectCourseInfo.getTotalQuestion(),
                    yetAnswered,
                    correctAnswered,
                    correctRate,
                    finishStatus,
                    projectCourseInfo.getCreateUser(),
                    projectCourseInfo.getCreateTime(),
                    userName,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );

            projectCourseInfoService.update(projectCourseInfo);
        } else {
            logger.error("QueueExerciseListener-----------projectCourseInfo not exist with args:"+new Gson().toJson(params));
        }
    }

    /**
     * 更新个人统计信息
     * @param userId
     * @param userName
     * @param projectId
     * @param studyTime
     */
    public boolean update_project_statistics_info(String userId, String userName, String projectId,
                       Integer yetAnswered, Integer correctAnswered, Long studyTime, CompanyTj companyTj){
        boolean result = false;
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("userId", userId);
        ProjectStatisticsInfo projectStatisticsInfo = projectStatisticsInfoService.selectOne(params);


        if(null != projectStatisticsInfo) {
            correctAnswered = projectStatisticsInfo.getCorrectAnswered() + correctAnswered;
            yetAnswered = projectStatisticsInfo.getYetAnswered() + yetAnswered;

            //总答正确率：答对总题量/已答题量*100
            Double correctRate = 0.00;
            if(yetAnswered != 0){
                correctRate = Double.parseDouble(new DecimalFormat("0.0").
                        format(correctAnswered * 100 / yetAnswered));
            }

            //判断是否是第一次添加学时
            Long totalStudyTime = projectStatisticsInfo.getTotalStudyTime();
            if(totalStudyTime == 0){
                result = true;
            }

            //检查培训状态
            String trainStatus = projectStatisticsInfo.getTrainStatus();
            if( totalStudyTime < (projectStatisticsInfo.getRequirementStudyTime()*60) && trainStatus.equals("1") && (totalStudyTime + studyTime) >= projectStatisticsInfo.getRequirementStudyTime()*60){
                trainStatus = "2";

                //更新统计信息--完成培训人次
                companyTj.setCountTrainCompleteYes(Integer.parseInt(companyTj.getCountTrainCompleteYes()) + 1 + "");
            }

            //判断应修学时和已修学时，来更新train_status培训状态（1未完成2完成）
            projectStatisticsInfo = new ProjectStatisticsInfo(
                    projectStatisticsInfo.getId(),
                    projectStatisticsInfo.getProjectId(),
                    projectStatisticsInfo.getProjectStartTime(),
                    projectStatisticsInfo.getProjectEndTime(),
                    projectStatisticsInfo.getUserId(),
                    projectStatisticsInfo.getRoleId(),
                    projectStatisticsInfo.getRoleName(),
                    projectStatisticsInfo.getDeptName(),
                    projectStatisticsInfo.getRequirementStudyTime(),
                    totalStudyTime + studyTime,
                    projectStatisticsInfo.getAnswerStudyTime() + studyTime,
                    projectStatisticsInfo.getTrainStudyTime(),
                    projectStatisticsInfo.getTotalQuestion(),
                    yetAnswered,
                    correctAnswered,
                    correctRate,
                    trainStatus,
                    projectStatisticsInfo.getExamNo(),
                    projectStatisticsInfo.getExamTimeInfo(),
                    projectStatisticsInfo.getExamScore(),
                    projectStatisticsInfo.getExamStatus(),
                    projectStatisticsInfo.getCreateUser(),
                    projectStatisticsInfo.getCreateTime(),
                    userName,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );

            projectStatisticsInfoService.update(projectStatisticsInfo);
        } else {
            logger.error("QueueExerciseListener-----------projectStatisticsInfo not exist with args:"+new Gson().toJson(params));
        }
        return result;
    }

    /**
     * 更新练习排行信息
     * @param userId
     * @param userName
     * @param projectId
     * @param yetAnswered
     * @param correctAnswered
     * @param notAnswered
     * @param failAnswered
     * @param studyTime
     */
    public void update_project_exercise_order(String userId, String userName, String projectId,
                  Integer yetAnswered, Integer correctAnswered, Integer notAnswered, Integer failAnswered,
                                              Long studyTime){
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("projectId", projectId);
        params.put("userId", userId);
        ProjectExerciseOrder projectExerciseOrder = projectExerciseOrderService.selectOne(params);

        if(null != projectExerciseOrder) {
            correctAnswered = projectExerciseOrder.getCorrectAnswered() + correctAnswered;
            yetAnswered = projectExerciseOrder.getYetAnswered() + yetAnswered;

            //总答正确率：答对总题量/已答题量*100
            Double correctRate = 0.00;
            if(yetAnswered != 0){
                correctRate = Double.parseDouble(new DecimalFormat("0.0").
                        format(correctAnswered * 100 / yetAnswered));
            }

            projectExerciseOrder = new ProjectExerciseOrder(
                    projectId,
                    userId,
                    projectExerciseOrder.getTotalQuestion(),
                    yetAnswered,
                    projectExerciseOrder.getNotAnswered() + notAnswered,
                    correctAnswered,
                    projectExerciseOrder.getFailAnswered() + failAnswered,
                    correctRate,
                    projectExerciseOrder.getAnswerStudyTime() + studyTime,
                    userName,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );
            projectExerciseOrderService.update(projectExerciseOrder);
        } else {
            logger.error("QueueExerciseListener-----------projectExerciseOrder not exist with args:"+new Gson().toJson(params));
        }
    }
}
