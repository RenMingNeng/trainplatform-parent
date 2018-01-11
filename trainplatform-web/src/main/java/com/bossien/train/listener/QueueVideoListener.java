package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.ClassHours;
import com.bossien.train.domain.CompanyTj;
import com.bossien.train.domain.ProjectCourseInfo;
import com.bossien.train.domain.ProjectStatisticsInfo;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 * @author DF
 *
 */
@Component("queueVideoListener")
public class QueueVideoListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueVideoListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_class_hours = "class_hours";
    private final String collectionName_person_dossier = "person_dossier";

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String project_id = (String) map.get("project_id");
        String course_id = (String) map.get("course_id");
        String user_id = (String) map.get("user_id");
        String user_name = (String) map.get("user_name");
        String source = (String) map.get("source");
        Long study_time = (Long) map.get("study_time");
        String companyId = (String) map.get("companyId");

        //公司统计表
        CompanyTj companyTj = companyTjService.selectOne(companyId, user_name);

        // 插入学时记录
        ClassHours classHours = new ClassHours(
                project_id,
                course_id,
                user_id,
                source,
                study_time,
                companyId,
                user_name,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
        mongoTemplate.insert(classHours, collectionName_class_hours);

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        // 项目课程信息 project_course_info
        params.clear();
        params.put("projectId", project_id);
        params.put("courseId", course_id);
        params.put("userId", user_id);
        ProjectCourseInfo projectCourseInfo = projectCourseInfoService.selectOne(params);
        if(null != projectCourseInfo) {
            //判断应修学时和已修学时，来更新finishStatus培训状态（-1未完成1完成）
            Long requirement_studytime = projectCourseInfo.getRequirementStudyTime();
            Long totalStudyTime = projectCourseInfo.getTotalStudyTime() + study_time;
            String finishStatus = projectCourseInfo.getFinishStatus();
            if(totalStudyTime >= requirement_studytime*60){
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
                    projectCourseInfo.getAnswerStudyTime(),
                    projectCourseInfo.getTrainStudyTime() + study_time,
                    projectCourseInfo.getTotalQuestion(),
                    projectCourseInfo.getYetAnswered(),
                    projectCourseInfo.getCorrectAnswered(),
                    projectCourseInfo.getCorrectRate(),
                    finishStatus,
                    projectCourseInfo.getCreateUser(),
                    projectCourseInfo.getCreateTime(),
                    user_name,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );
            projectCourseInfoService.update(projectCourseInfo);
        } else {
            logger.error("QueueVideoListener-----------projectCourseInfo not exist with args:"+new Gson().toJson(params));
        }

        // 项目个人学时统计 project_statistics_info
        params.clear();
        params.put("projectId", project_id);
        params.put("userId", user_id);
        ProjectStatisticsInfo projectStatisticsInfo = projectStatisticsInfoService.selectOne(params);
        Long projectStatisticsInfo_totalStudyTime = null;

        if(null != projectStatisticsInfo) {
            projectStatisticsInfo_totalStudyTime = projectStatisticsInfo.getTotalStudyTime();
            //判断应修学时和已修学时，来更新train_status培训状态（1未完成2完成）
            Long requirement_studytime = projectStatisticsInfo.getRequirementStudyTime();
            Long totalStudyTime = projectStatisticsInfo_totalStudyTime + study_time;
            String train_status = projectStatisticsInfo.getTrainStatus();
            if((projectStatisticsInfo_totalStudyTime < (requirement_studytime*60)) && (totalStudyTime >= (requirement_studytime*60)) ){
                train_status = "2";
                //更新统计信息--完成培训人次
                companyTj.setCountTrainCompleteYes(Integer.parseInt(companyTj.getCountTrainCompleteYes()) + 1 + "");
            }
            projectStatisticsInfo = new ProjectStatisticsInfo(
                    projectStatisticsInfo.getId(),
                    projectStatisticsInfo.getProjectId(),
                    projectStatisticsInfo.getProjectStartTime(),
                    projectStatisticsInfo.getProjectEndTime(),
                    projectStatisticsInfo.getUserId(),
                    projectStatisticsInfo.getRoleId(),
                    projectStatisticsInfo.getRoleName(),
                    projectStatisticsInfo.getDeptName(),
                    requirement_studytime,
                    totalStudyTime,
                    projectStatisticsInfo.getAnswerStudyTime(),
                    projectStatisticsInfo.getTrainStudyTime() + study_time,
                    projectStatisticsInfo.getTotalQuestion(),
                    projectStatisticsInfo.getYetAnswered(),
                    projectStatisticsInfo.getCorrectAnswered(),
                    projectStatisticsInfo.getCorrectRate(),
                    train_status,
                    projectStatisticsInfo.getExamNo(),
                    projectStatisticsInfo.getExamTimeInfo(),
                    projectStatisticsInfo.getExamScore(),
                    projectStatisticsInfo.getExamStatus(),
                    projectStatisticsInfo.getCreateUser(),
                    projectStatisticsInfo.getCreateTime(),
                    user_name,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
            );
            projectStatisticsInfoService.update(projectStatisticsInfo);
        } else {
            logger.error("QueueVideoListener-----------projectStatisticsInfo not exist with args:"+new Gson().toJson(params));
        }

        // 个人档案信息 person_dossier_info
        params.clear();
        params.put("userId", user_id);
        Map<String, Object>  personDossierMap = personDossierService.selectOne(params);
        if(null != personDossierMap) {
            Long total_studytime = Long.parseLong(personDossierMap.get("total_studytime").toString());
            Long total_study_self = Long.parseLong(personDossierMap.get("total_study_self").toString());
            //培训次数
            Object trainCount = personDossierMap.get("train_count");
            if(null == trainCount){
                //默认值
                trainCount = 0;
            }
            personDossierMap.put("id", personDossierMap.get("id"));
            personDossierMap.put("userId", personDossierMap.get("user_id"));
            personDossierMap.put("userName", personDossierMap.get("user_name"));
            personDossierMap.put("roleId", personDossierMap.get("role_id"));
            personDossierMap.put("roleName", personDossierMap.get("role_name"));
            personDossierMap.put("companyId", personDossierMap.get("company_id"));
            personDossierMap.put("companyName", personDossierMap.get("company_name"));
            personDossierMap.put("yearStudytime", (Long)personDossierMap.get("year_studytime") + study_time);
            personDossierMap.put("totalStudytime", (Long)personDossierMap.get("total_studytime") + study_time);
            //判断是否是该项目第一次添加学时
            if(null != projectStatisticsInfo_totalStudyTime && projectStatisticsInfo_totalStudyTime == 0){
                //判断是否考试
                params = MapUtils.newHashMap();
                params.put("userId", user_id);
                params.put("examStatus", 2);
                params.put("projectId", project_id);
                int count = examPaperInfoService.selectCount(params);
                if(count < 1){
                    //更新‘参与培训人次’
                    personDossierMap.put("trainCount", Integer.parseInt(personDossierMap.get("train_count").toString()) + 1);
                    companyTj.setCountTrain(Integer.parseInt(companyTj.getCountTrain()) + 1 + "");
                }
            }

            //培训人数
            if(0 == Integer.parseInt(trainCount.toString())){
                //更新统计表‘培训人数’
                companyTj.setCountTrainUser(Integer.parseInt(companyTj.getCountTrainUser()) + 1 + "");
            }

            personDossierMap.put("createUser", personDossierMap.get("create_user"));
            personDossierMap.put("createTime", personDossierMap.get("create_time"));
            personDossierMap.put("operUser", user_name);
            personDossierMap.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            personDossierService.update(personDossierMap);
        } else {
            logger.error("QueueVideoListener-----------personDossierMap not exist with args:"+new Gson().toJson(params));
        }

        //更新统计表 company_tj
        Double total = (Double.parseDouble(companyTj.getTotalClassHour()) * 3600 + study_time)/3600;
        Double total_year = (Double.parseDouble(companyTj.getTotalYearClassHour()) * 3600 + study_time)/3600;
        companyTj.setTotalClassHour(total.toString());
        companyTj.setTotalYearClassHour(total_year.toString());

        int count_user = Integer.parseInt(companyTj.getCountUser());

        //总学时、年度学时
        if(count_user != 0){
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
        companyTj.setOperUser(user_name);

        Map<String, Object> companyTjMap = new HashMap<String, Object>();
        MapUtils.putAll(companyTjMap, companyTj);
        //更新数据
        companyTjService.update(companyTjMap);

    }
}
