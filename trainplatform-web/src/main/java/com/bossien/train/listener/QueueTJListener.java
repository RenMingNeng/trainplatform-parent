package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.service.*;

import java.text.DecimalFormat;
import java.text.ParseException;

import com.bossien.train.util.MathUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 *
 * @author DF
 */
@Component("queueTJListener")
public class QueueTJListener extends AbstractListener {

    @Autowired
    private IUserService userService;

    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICompanyCourseService companyCourseService;
    @Autowired
    private ICourseInfoService courseInfoService;
    @Autowired
    private ICompanyTjService companyTjService;


    public static final Logger logger = LoggerFactory.getLogger(QueueTJListener.class);

    @Override
    public <V> void mapMessageHander(Map <String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String companyId = (String) map.get("companyId");
        String operType = (String) map.get("operType");
        String userName = (String) map.get("userName");

        if (StringUtils.isEmpty(companyId)) {
            return;
        }
        Map <String, Object> params = new ConcurrentHashMap <String, Object>();
        params.put("companyId", companyId);
        List <String> projectStatus = new ArrayList <String>();
        projectStatus.add("3");                                   //进行中
        projectStatus.add("4");                                   //已结束
        params.put("projectStatus", projectStatus);

        DecimalFormat df = new DecimalFormat("0.00");
        // 学员数量
        Integer countUser = userService.selectUserCount(params);
        //培训人数

        Integer countTrainUser = projectStatisticsInfoService.selectTrainUserCount(params);
        String percentTrainComplete = "0.00";
        if (0 != countUser) {
            //培训率
            percentTrainComplete = df.format(new Double(countTrainUser) / new Double(countUser) * 100);
        }

        //课程数量
        Integer countCourse = companyCourseService.selectCompanyCourseCount(params);
        //题目数量
        Integer countQuestion = courseInfoService.selectCourseQuestionCount(params);
        // 项目数量
        Integer countProject = projectInfoService.selectProjectIdCount(params);


        //参与培训人次
        List <String> projectTypes = new ArrayList <String>();
        projectTypes.add(ProjectTypeEnum.QuestionType_1.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_4.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_5.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_7.getValue());
        params.put("projectTypes", projectTypes);
        Integer countTrain = projectStatisticsInfoService.selectJoinTrainOrExamUserCount(params);


        //参与考试人次
        projectTypes = new ArrayList <String>();
        projectTypes.add(ProjectTypeEnum.QuestionType_3.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_5.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_6.getValue());
        projectTypes.add(ProjectTypeEnum.QuestionType_7.getValue());
        params.put("projectTypes", projectTypes);
        Integer countExam = projectStatisticsInfoService.selectJoinTrainOrExamUserCount(params);

        //完成培训人次
        Integer countTrainCompleteYes = projectStatisticsInfoService.selectCompleteTrainUserCount(params);
        //考试合格人次
        Integer countExamPassYes = projectStatisticsInfoService.selectPassExamUserCount(params);


        projectTypes = new ArrayList <String>();
        params.put("projectTypes", projectTypes);
        //累计学时
        Double totalClassHour = projectStatisticsInfoService.selecttotalClassHour(params);
        totalClassHour =Double.valueOf(MathUtil.getHour(totalClassHour)) ;


        Double averagePersonClassHour = 0.00;
        if (0 != countUser) {
            //人均学时
            averagePersonClassHour = Double.valueOf(MathUtil.getHour(totalClassHour / countUser)) ;
        }

        //年度累计学时
        Double totalYearClassHour = projectStatisticsInfoService.selecttotalYearClassHour(params);
        totalYearClassHour =Double.valueOf(MathUtil.getHour(totalYearClassHour)) ;

        Double averageYearClassHour = 0.00;
        if (0 != countUser) {
            //年度人均学时
            averageYearClassHour =Double.valueOf(MathUtil.getHour(totalYearClassHour / countUser)) ;
        }

        Map <String, Object> param = new HashMap <String, Object>();
        param.put("companyId", companyId);
        param.put("countUser", countUser.toString());
        param.put("countTrainUser", countTrainUser.toString());
        param.put("percentTrainComplete", percentTrainComplete);
        param.put("countCourse", countCourse.toString());
        param.put("countQuestion", countQuestion.toString());
        param.put("countProject", countProject.toString());
        param.put("countTrain", countTrain.toString());
        param.put("countTrainCompleteYes", countTrainCompleteYes.toString());
        param.put("countExam", countExam.toString());
        param.put("countExamPassYes", countExamPassYes.toString());
        param.put("totalClassHour", totalClassHour.toString());
        param.put("averagePersonClassHour", averagePersonClassHour.toString());
        param.put("totalYearClassHour", totalYearClassHour.toString());
        param.put("averageYearClassHour", averageYearClassHour.toString());
        param.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        param.put("operUser", userName);

        if ("add".equals(operType)) {

            String companyName = (String) map.get("companyName");
            param.put("companyName", companyName);
            param.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            param.put("createUser", userName);
            companyTjService.insert(param);
        }
        if ("update".equals(operType)) {
            companyTjService.update(param);
        }

    }
}
