package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
 * 自学答题
 *
 * @author DF
 *
 */
@Component("queueStudentStudyExerciseListener")
public class QueueStudentStudyExerciseListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueStudentStudyExerciseListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_class_hours = "class_hours";
    private final String collectionName_exercise_answers = "exercise_answers";
    private final String collectionName_question_wrong_answers = "question_wrong_answers";
    private final String collectionName_study_self = "study_self";

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IBaseService<ExerciseAnswers> baseService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private ICourseInfoService courseInfoService;

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

        for(String ans : ansAry){
            if(null == ans || ans.equals("") || ans.indexOf("undefined") != -1){
                continue;
            }

            String[] params = ans.split(",");
            if(params.length < 5){
                return;
            }

            String projectId = params[0];
            String questionId = params[1];
            String answer = params[2];
            String isTrue = params[3];
            String courseId = params[4];

            //是否添加答题记录
            boolean isInsert = insertClassHourse(projectId, courseId, questionId, isTrue, userId, userName, companyId);
            if(isInsert){
                ExerciseAnswers exerciseAnswers = new ExerciseAnswers(
                        projectId,
                        courseId,
                        questionId,
                        userId,
                        answer,
                        isTrue,
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
                                  String isTrue, String userId, String userName, String companyId){
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
        }else{//答错
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

            //错题个数小于3时，添加
            if(wrongCount < 3){
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

        //更新个人档案
        update_person_dossier(userId, userName, companyId, studyTime);

        //更新统计表
        updateCompanyTj(studyTime, companyId, userName);

        //保存自学学时
        updateStudySelf(userId, userName, courseId, studyTime);

        return result;
    }

    /**
     * 更新个人档案
     * @param userId
     * @param userName
     * @param companyId
     * @param studyTime
     */
    public void update_person_dossier(String userId, String userName, String companyId, Long studyTime){
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("userId", userId);
        Map<String, Object>  personDossierMap = personDossierService.selectOne(params);

        if(null != personDossierMap) {
            personDossierMap.put("userId", userId);
            personDossierMap.put("companyId", companyId);
            personDossierMap.put("yearStudytime", Long.parseLong(personDossierMap.get("year_studytime").toString()) + studyTime);
            personDossierMap.put("totalStudytime", Long.parseLong(personDossierMap.get("total_studytime").toString()) + studyTime);
            personDossierMap.put("yearStudySelf", Long.parseLong(personDossierMap.get("year_study_self").toString()) + studyTime);
            personDossierMap.put("totalStudySelf", Long.parseLong(personDossierMap.get("total_study_self").toString()) + studyTime);

            personDossierMap.put("operUser", userName);
            personDossierMap.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            personDossierService.update(personDossierMap);
        } else {
            logger.error("QueueExerciseListener-----------personDossierMap not exist with args:"+new Gson().toJson(params));
        }
    }

    /**
     * 更新统计表
     * @param studyTime
     * @param companyId
     * @param userName
     */
    public void updateCompanyTj(Long studyTime, String companyId, String userName){
        //公司统计表
        CompanyTj companyTj = companyTjService.selectOne(companyId, userName);
        //更新统计信息
        Double total = (Double.parseDouble(companyTj.getTotalClassHour()) * 3600 + studyTime)/3600;
        Double total_year = (Double.parseDouble(companyTj.getTotalYearClassHour()) * 3600 + studyTime)/3600;
        companyTj.setTotalClassHour(total.toString());
        companyTj.setTotalYearClassHour(total_year.toString());

        int count_user = Integer.parseInt(companyTj.getCountUser());

        if(count_user != 0){
            //总学时、年度学时
            Double average_person_class_hour = Double.parseDouble(new DecimalFormat("0.00")
                    .format(total * 1.00 / count_user));
            Double average_year_class_hour = Double.parseDouble(new DecimalFormat("0.00")
                    .format(total_year * 1.00 / count_user));
            companyTj.setAverageYearClassHour(average_person_class_hour.toString());
            companyTj.setAverageYearClassHour(average_year_class_hour.toString());
        }

        //最后操作时间、人员
        companyTj.setOperTime(DateUtils.formatDateTime(new Date()));
        companyTj.setOperUser(userName);

        Map<String, Object> companyTjMap = new HashMap<String, Object>();
        MapUtils.putAll(companyTjMap, companyTj);
        companyTjService.update(companyTjMap);
    }

    /**
     * 新增、修改自学学习记录
     * @param user_id
     * @param user_name
     * @param course_id
     * @param study_time
     */
    public void updateStudySelf(String user_id, String user_name, String course_id, long study_time){
        Query query = new Query(new Criteria().andOperator(
                Criteria.where("user_id").is(user_id),
                Criteria.where("course_id").is(course_id))
        );
        //查询单条记录
        StudySelf studySelf = mongoTemplate.findOne(query,StudySelf.class,collectionName_study_self);

        if(null == studySelf){
            CourseInfo courseInfo = courseInfoService.selectOne(course_id);
            // 插入自学记录表
            studySelf = new StudySelf(
                    sequenceService.generator(),
                    user_id,
                    user_name,
                    course_id,
                    courseInfo.getCourseNo(),
                    courseInfo.getCourseName(),
                    courseInfo.getClassHour(),
                    study_time,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );
            mongoTemplate.insert(studySelf, collectionName_study_self);
        }else{
            Update update =new Update();
            update.set("study_time",studySelf.getStudy_time()+study_time);
            update.set("oper_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //更新自学学时信息
            mongoTemplate.updateFirst(query,update,collectionName_study_self);
        }
     }
}
