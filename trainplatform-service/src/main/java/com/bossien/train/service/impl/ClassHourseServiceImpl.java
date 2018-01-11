package com.bossien.train.service.impl;

import com.bossien.train.domain.ClassHours;
import com.bossien.train.domain.CompanyTj;
import com.bossien.train.domain.User;
import com.bossien.train.service.*;
import com.bossien.train.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2017/7/25.
 */

@Service
public class ClassHourseServiceImpl implements IClassHoursService {
    private final String collectionName_class_hours = "class_hours";

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IUserService userService;
    @Autowired
    private IProjectInfoService projectInfoService;
    @Autowired
    private ICompanyProjectService companyProjectService;
    @Autowired
    private IExamScoreService examScoreService;

    @Override
    public CompanyTj selectSuperviceByTime(String companyId, String companyName, String startTime, String endTime) {
        Criteria criteriaTime = Criteria.where("create_time");
        if(!StringUtil.isEmpty(startTime)){
            criteriaTime.gt(startTime);
        }

        Map<String, Object>  newParams = new HashMap<String, Object>();
        newParams.put("companyId", companyId);
        newParams.put("userType", User.UserType.TYPE_3.getValue());
        newParams.put("isValid", User.IsValid.TYPE_1.getValue());

        //结束时间
        if(!StringUtil.isEmpty(endTime)){
            criteriaTime.lt(endTime);
            newParams.put("endTime", endTime);
        }

        //学员数量
        List<String> userIds = userService.selectUserIds(newParams);
        int countUser = userIds.size();

        //查询条件
        Criteria criteria = new Criteria().andOperator(
                Criteria.where("company_id").is(companyId),
                criteriaTime
        );

        //考试人数
        int countTrainUser_exam = 0;
        //培训人次
        int countTrain_exam = 0;
        List<String> projectIds = companyProjectService.selectProjectIdsByCompanyId(companyId);
        if(null != projectIds && projectIds.size() > 0){
            newParams = new HashMap<String, Object>();
            newParams.put("projectIds", projectIds);
            newParams.put("projectTypes", Arrays.asList("3"));
            List<String> projectIds_exam = projectInfoService.selectProjectIds(newParams);
            if(null != projectIds_exam && projectIds_exam.size() > 0){
                //查询出考试项目
                newParams = new HashMap<String, Object>();
                newParams.put("projectIds", projectIds_exam);
                newParams.put("groupBy", "user_id");
                //培训人数
                countTrainUser_exam = examScoreService.selectCountGroupByProjectIdOrUserId(newParams);

                //培训人次
                newParams.put("groupBy", "project_id,user_id");
                countTrain_exam = examScoreService.selectCountGroupByProjectIdOrUserId(newParams);
            }
        }

        //培训人数
        int countTrainUser = mongoTemplate.group(criteria,
                collectionName_class_hours,
                new GroupBy("user_id")
                        .initialDocument("{requestCount:0}")
                        .reduceFunction("function(curr,result){result.requestCount += curr.requestCount;}"),
                ClassHours.class).getKeys();

        //培训人次
        int countTrain = mongoTemplate.group(criteria,
                collectionName_class_hours,
                new GroupBy("user_id", "project_id")
                        .initialDocument("{requestCount:0}")
                        .reduceFunction("function(curr,result){result.requestCount += curr.requestCount;}"),
                ClassHours.class).getKeys();

        //总学时
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("company_id").sum("study_time").as("study_time"),
                Aggregation.limit(1)
        );
        AggregationResults<ClassHours> aggregationResults = mongoTemplate.aggregate(aggregation, collectionName_class_hours, ClassHours.class);
        List<ClassHours> classHourss = aggregationResults.getMappedResults();
        Long totalClassHour = 0L;
        if(classHourss.size() > 0){
            totalClassHour += classHourss.get(0).getStudy_time();
        }
        System.out.println(totalClassHour);

        return new CompanyTj(companyId, companyName, countUser, countTrain, countTrainUser, totalClassHour);
    }
}
