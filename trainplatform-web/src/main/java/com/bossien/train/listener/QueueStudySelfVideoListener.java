package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.ClassHours;
import com.bossien.train.domain.CompanyTj;
import com.bossien.train.domain.StudySelf;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.bossien.train.util.MapUtils;
import com.bossien.train.util.MathUtil;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 按需复写父类方法，真实业务请直接继承AbstractListener
 * que 监听器
 * @author DF
 *
 */
@Component("queueStudySelfVideoListener")
public class QueueStudySelfVideoListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueueStudySelfVideoListener.class);

    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_study_self = "study_self";
    private final String collectionName_class_hours = "class_hours";


    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IPersonDossierService personDossierService;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private IProjectCourseInfoService projectCourseInfoService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);

        String source = (String) map.get("source");
        String course_id = (String) map.get("course_id");
        String course_no = (String) map.get("course_no");
        String course_name = (String) map.get("course_name");
        String user_id = (String) map.get("user_id");
        String user_name = (String) map.get("user_name");
        Integer class_hour = Integer.valueOf(map.get("class_hour").toString());
        Long study_time = (Long) map.get("study_time");
        String companyId = (String) map.get("companyId");


        // 插入学时记录
        ClassHours classHours = new ClassHours(
                "-9",
                course_id,
                user_id,
                source,
                study_time,
                companyId,
                user_name,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );
        mongoTemplate.insert(classHours, collectionName_class_hours);

        Query query = new Query(new Criteria().andOperator(
                Criteria.where("user_id").is(user_id),
                Criteria.where("course_id").is(course_id))
                );
        //查询单条记录
        StudySelf studySelf = mongoTemplate.findOne(query,StudySelf.class,collectionName_study_self);

        if(null == studySelf){
            // 插入自学记录表
              studySelf = new StudySelf(
                    sequenceService.generator(),
                    user_id,
                    user_name,
                    course_id,
                    course_no,
                    course_name,
                    class_hour,
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


        // 个人档案信息 person_dossier_info
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.clear();
        params.put("userId", user_id);
        Map<String, Object>  personDossierMap = personDossierService.selectOne(params);
        if(null != personDossierMap) {
            personDossierMap.put("id", personDossierMap.get("id"));
            personDossierMap.put("userId", personDossierMap.get("user_id"));
            personDossierMap.put("userName", personDossierMap.get("user_name"));
            personDossierMap.put("roleId", personDossierMap.get("role_id"));
            personDossierMap.put("roleName", personDossierMap.get("role_name"));
            personDossierMap.put("companyId", personDossierMap.get("company_id"));
            personDossierMap.put("companyName", personDossierMap.get("company_name"));
            personDossierMap.put("yearStudytime", (Long)personDossierMap.get("year_studytime")+ study_time);
            personDossierMap.put("totalStudytime", (Long)personDossierMap.get("total_studytime")+ study_time);
            personDossierMap.put("yearStudySelf", (Long)personDossierMap.get("year_study_self") + study_time);
            personDossierMap.put("totalStudySelf", (Long)personDossierMap.get("total_study_self") + study_time);
            //判断是否是该项目第一次添加学时
            /*if(null != projectStatisticsInfo_totalStudyTime && projectStatisticsInfo_totalStudyTime == 0){
                personDossierMap.put("trainCount", Integer.parseInt(personDossierMap.get("train_count").toString()) + 1);
            }*/
            personDossierMap.put("createUser", personDossierMap.get("create_user"));
            personDossierMap.put("createTime", personDossierMap.get("create_time"));
            personDossierMap.put("operUser", user_name);
            personDossierMap.put("operTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            personDossierService.update(personDossierMap);
        } else {
            logger.error("QueueVideoListener-----------personDossierMap not exist with args:"+new Gson().toJson(params));
        }


       //更新公司统计表company_tj数据
        params.clear();
        params.put("companyId", companyId);

        //根据companyId查询公司
        CompanyTj companyTj = companyTjService.selectOne(companyId,user_name);
        //学员数量
        int countUser = Integer.parseInt(companyTj.getCountUser());
        //累计学时
        Double totalClassHour = Double.parseDouble(companyTj.getTotalClassHour())* 3600 + Double.parseDouble(study_time.toString());
        //年度累计学时
        Double totalYearClassHour = Double.parseDouble(companyTj.getTotalYearClassHour()) * 3600+ Double.parseDouble(study_time.toString());

        //人均学时
        Double averagePersonClassHour = 0.00;
        //年度人均学时
        Double averageYearClassHour = 0.00;
        if (0 != countUser) {
            averagePersonClassHour = Double.valueOf(MathUtil.getHour(totalClassHour / countUser)) ;
            averageYearClassHour = Double.valueOf(MathUtil.getHour(totalYearClassHour / countUser)) ;
        }
        //换算成时
        totalClassHour = Double.parseDouble(new DecimalFormat("0.00").format((totalClassHour * 1.00 / 3600)));
        totalYearClassHour = Double.parseDouble(new DecimalFormat("0.00").format((totalYearClassHour * 1.00 / 3600)));

        companyTj.setTotalClassHour(totalClassHour.toString());
        companyTj.setTotalYearClassHour(totalYearClassHour.toString());
        companyTj.setAveragePersonClassHour(averagePersonClassHour.toString());
        companyTj.setAverageYearClassHour(averageYearClassHour.toString());
        companyTj.setOperTime(DateUtils.formatDateTime(new Date()));
        companyTj.setOperUser(user_name);

        Map<String, Object> companyTjMap = new HashMap<String, Object>();
        MapUtils.putAll(companyTjMap, companyTj);

        //更新数据
        companyTjService.update(companyTjMap);

        }




}
