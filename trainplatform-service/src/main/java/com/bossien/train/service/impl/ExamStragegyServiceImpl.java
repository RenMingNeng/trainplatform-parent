package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ExamStrategyMapper;
import com.bossien.train.dao.tp.ProjectCourseMapper;
import com.bossien.train.domain.*;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 组卷策略表
 * Created by Administrator on 2017/7/27.
 */
@Service
public class ExamStragegyServiceImpl implements IExamStrategyService {
    @Autowired
    private IBaseService<ExamStrategy> baseService;
    @Autowired
    private IQuestionService questionService;
    @Autowired
    private IProjectRoleService projectRoleService;
    @Autowired
    ISequenceService sequenceService;
    @Autowired
    private ExamStrategyMapper examStrategyMapper;
    @Autowired
    private ProjectCourseMapper projectCourseMapper;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectBasicService projectBasicService;
    /**
     * 组卷策略中的题目类型： 单选题
     */
    public   String EXAMSTRATEGY_SINGLE_TYPE="单选题";
    /**
     * 组卷策略中的题目类型： 多选题
     */
    public   String EXAMSTRATEGY_Many_TYPE="多选题";
    /**
     * 组卷策略中的题目类型： 判断题
     */
    public  String EXAMSTRATEGY_JUDGE_TYPE="判断题";
    /**
     * 组卷策略中的题目类型： 填空题
     */
    public String EXAMSTRATEGY_FILLOUT_TYPE="填空题";
    /**
     * 组卷策略中的题目类型： 问答题
     */
    public String EXAMSTRATEGY_QUESTION_ANSWER_TYPE="问答题";

    @Override
    public ExamStrategy selectByProjectIdAndRoleId(Map<String, Object> params) {

        return examStrategyMapper.selectByProjectIdAndRoleId(params);
    }

    /**
     * 通过projectId和roleId查询组卷策略
     * @param projectId
     * @param roleId
     * @return
     */
    @Cacheable(value = "examStrategyCache#(60 * 60)", key = "'selectByProjectIdAndRoleId'.concat('_').concat(#projectId).concat('_').concat(#roleId)")
    @Override
    public ExamStrategy selectByProjectIdAndRoleId(String projectId, String roleId) {
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        params.put("roleId", roleId);
        return examStrategyMapper.selectByProjectIdAndRoleId(params);
    }
    /**
     * 通过projectId和roleId查询组卷策略(用于缓存)
     * @param
     * @return
     */
    @Cacheable(value = "examStrategyCache#(60 * 60)", key = "'selectOneByProjectIdAndRoleId'.concat('_').concat(#projectId).concat('_').concat(#roleId)")
    @Override
    public ExamStrategy selectOneByProjectIdAndRoleId(String projectId, String roleId){
        return examStrategyMapper.selectOneByProjectIdAndRoleId(projectId,roleId);
    }

    @Override
    public ExamStrategy selectProjectIdAndUserId(String projectId, String userId) {
        ProjectUser projectUser = projectUserService.selectByProjectIdAndUserId(projectId, userId);
        if(null != projectUser && null != projectUser.getRoleId()){

            return examStrategyMapper.selectOneByProjectIdAndRoleId(projectId,projectUser.getRoleId());
        }
        return null;
    }

    /**
     *组装组卷策略中的多种题型数据
     * @param examStrategy
     * @return
     */
    @Override
    public List<Map<String,Object>> assembleExamStrategy(ExamStrategy examStrategy){
        List<Map<String,Object>>   list=new ArrayList<Map<String,Object>>();
        if(null!=examStrategy.getSingleCount()) {         //单选题
            list.add(assembleExamStrategySingle(examStrategy));
        }
        if(null!=examStrategy.getManyCount()){              //多选题
            list.add(assembleExamStrategyMany(examStrategy));
        }
        if(null!=examStrategy.getJudgeCount()){            //判断题
            list.add(assembleExamStrategyJudge(examStrategy));
        }
        return list;

    }

    @Override
    public int insertBatch(Map<String, Object> params){
        String projectId = params.get("projectId").toString();
        String userName = params.get("userName").toString();
        int count = 0;
        //获取角色集合
        List<Map> roleList = (List<Map>)params.get("roleList_");
       /* //获取课程Id集合
        List<String> intCourseIds = (List<String>)params.get("courseList");
        Map<String, Object> param_ = new HashedMap();
        param_.put("intCourseIds", intCourseIds);
        //获取试题id集合
        List<String> ids = courseQuestionService.selectQuestionIdList(param_);
        //获取不同试题类型数量
        param_.put("ids", ids);
        List<Integer> typeList = questionService.selectQuestionsTypeCount(param_);*/
        //创建考试组卷策略集合
        List<ExamStrategy> item = new ArrayList<>();
        for (Map roleMap: roleList) {
            String roleId = roleMap.get("id").toString();
            Map<String, Object> param = new HashedMap();
            param.put("roleId",roleId);
            param.put("projectId", projectId);
            //初始化组卷策略
            ExamStrategy examStrategy = getExamStrategy(projectId, userName);
            examStrategy.setRoleId(roleId);
            examStrategy.setRoleName(roleMap.get("text").toString());
            examStrategy.setNecessaryHour(0);
            /*examStrategy.setSingleAllCount(typeList.get(0));
            examStrategy.setManyAllCount(typeList.get(1));
            examStrategy.setJudgeAllCount(typeList.get(2));*/
            item.add(examStrategy);
        }
        //根据角色不同批量插入组卷策略信息
        if(item !=null && item.size()>0){
            count = examStrategyMapper.insertBatch(item);
        }
        return count;
    }

    @Override
    public void update(ExamStrategy examStrategy, User user) {
        if(null == examStrategy.getRoleId()){
            List<ProjectRole> projectRoles = projectRoleService.selectByProjectId(examStrategy.getProjectId());
            if(null == projectRoles || projectRoles.size() < 1){
                return;
            }
            for(ProjectRole projectRole : projectRoles){
                examStrategy.setRoleId(projectRole.getRoleId());
                examStrategy.setRoleName(projectRole.getRoleName());
                examStrategyMapper.update(baseService.updateBuild(user.getUserName(), examStrategy));
            }
        }else{
            examStrategyMapper.update(baseService.updateBuild(user.getUserName(), examStrategy));
        }
    }

    @Override
    public int insert(Map<String, Object> params) {
        String projectId = params.get("projectId").toString();
        String userName = params.get("userName").toString();
        String roleId = params.get("roleId").toString();
        String roleName = params.get("roleName").toString();
        ExamStrategy examStrategy = getExamStrategy(projectId, userName);
        examStrategy.setRoleId(roleId);
        examStrategy.setRoleName(roleName);
        examStrategy.setNecessaryHour(Integer.parseInt(params.get("necessaryHour").toString()));
        examStrategy.setSingleAllCount(Integer.parseInt(params.get("singleAllCount").toString()));
        examStrategy.setManyAllCount(Integer.parseInt(params.get("manyAllCount").toString()));
        examStrategy.setJudgeAllCount(Integer.parseInt(params.get("judgeAllCount").toString()));
        return examStrategyMapper.insert(examStrategy);
    }

    @Override
    public List<ExamStrategy> selectList(Map<String, Object> params) {

        return examStrategyMapper.selectList(params);
    }

    /**
     * 初始化组卷策略
     * @return
     */
    public ExamStrategy getExamStrategy(String projectId, String userName){
        ExamStrategy examStrategy = new ExamStrategy();
        examStrategy.setId(sequenceService.generator());
        examStrategy.setProjectId(projectId);
        examStrategy.setTotalScore(100);
        examStrategy.setExamDuration(120);
        examStrategy.setPassScore(60);
        examStrategy.setSingleCount(60);
        examStrategy.setSingleScore(1);
        examStrategy.setSingleAllCount(0);
        examStrategy.setManyCount(20);
        examStrategy.setManyScore(1);
        examStrategy.setManyAllCount(0);
        examStrategy.setJudgeCount(20);
        examStrategy.setJudgeScore(1);
        examStrategy.setJudgeAllCount(0);
        examStrategy.setFilloutCount(0);
        examStrategy.setFilloutScore(0);
        examStrategy.setQuesAnsCount(0);
        examStrategy.setQuesAnsScore(0);
        examStrategy.setCreateUser(userName);
        examStrategy.setCreateTime(DateUtils.formatDateTime(new Date()));
        examStrategy.setOperUser(userName);
        examStrategy.setOperTime(DateUtils.formatDateTime(new Date()));
        return examStrategy;
    }

    /**
     * 组卷策略中的单选题数据组合
     * @param examStrategy
     * @return
     */
    public  Map<String, Object> assembleExamStrategySingle(ExamStrategy examStrategy){
        Map<String, Object> single = new HashMap<String, Object>();
        single.put("type", EXAMSTRATEGY_SINGLE_TYPE);          //单选题型
        single.put("typeName", "single");
        if(null != examStrategy){
            single.put("intCount", examStrategy.getSingleCount());
            single.put("intScore", examStrategy.getSingleScore());
            single.put("allScore", examStrategy.getTotalScore());
            single.put("allCount", examStrategy.getSingleAllCount());
        }
        return single;
    }

    /**
     * 组卷策略中的多选题数据组合
     * @param examStrategy
     * @return
     */
    public  Map<String, Object> assembleExamStrategyMany(ExamStrategy examStrategy){
        Map<String, Object>  many = new HashMap<String, Object>();
        many.put("type", EXAMSTRATEGY_Many_TYPE);            //多选题型
        many.put("typeName", "many");
        if(null != examStrategy){
            many.put("intCount", examStrategy.getManyCount());
            many.put("intScore", examStrategy.getManyScore());
            many.put("allScore", examStrategy.getTotalScore());
            many.put("allCount", examStrategy.getManyAllCount());
        }
        return many;
    }

    /**
     * 组卷策略中的判断题数据组合
     * @param examStrategy
     * @return
     */
    public  Map<String, Object> assembleExamStrategyJudge(ExamStrategy examStrategy){
        Map<String, Object> judge = new HashMap<String, Object>();
        judge.put("type", EXAMSTRATEGY_JUDGE_TYPE);        //判断题型
        judge.put("typeName", "judge");
        if(null != examStrategy){
            judge.put("intCount", examStrategy.getJudgeCount());
            judge.put("intScore", examStrategy.getJudgeScore());
            judge.put("allScore", examStrategy.getTotalScore());
            judge.put("allCount", examStrategy.getJudgeAllCount());
        }
        return judge;
    }

    /**
     * 组卷策略中的填空题数据组合
     * @param examStrategy
     * @return
     */
    public  Map<String, Object> assembleExamStrategyFillout(ExamStrategy examStrategy){
        Map<String, Object> fillout = new HashMap<String, Object>();
        fillout.put("type", EXAMSTRATEGY_FILLOUT_TYPE);     //填空题型
        if(null != examStrategy){
            fillout.put("intCount", examStrategy.getFilloutCount());
            fillout.put("intScore", examStrategy.getFilloutScore());
            fillout.put("allScore", examStrategy.getTotalScore());
        }
        return fillout;
    }

    /**
     * 组卷策略中的问答题数据组合
     * @param examStrategy
     * @return
     */
    public  Map<String, Object> assembleExamStrategyQuestionAnswer(ExamStrategy examStrategy){
        Map<String, Object> questionAnswer = new HashMap<String, Object>();
        questionAnswer.put("type", EXAMSTRATEGY_QUESTION_ANSWER_TYPE);    //问答题型
        if(null != examStrategy){
            questionAnswer.put("intCount", examStrategy.getQuesAnsCount());
            questionAnswer.put("intScore", examStrategy.getQuesAnsScore());
            questionAnswer.put("allScore", examStrategy.getTotalScore());
        }
        return questionAnswer;
    }
    /**
     * 根据projectId删除
     * @param map
     * @return
     */
    @Override
    public int deleteByProjectId(Map<String,Object> map){
       return  examStrategyMapper.deleteByProjectId(map);
    }
    /**
     * 根据projectId和roleIds确认数据是否最新
     * @param params
     */
    @Override
    public void confirmExamStrategy(Map<String, Object> params){
        List<String> ids=examStrategyMapper.selectIds(params);
        if(ids!=null && ids.size()>0){
            for (String id:ids) {
                examStrategyMapper.delete(id);
            }
        }
    }

    @Override
    public Map<String,Object> selectMinStrategyByProject(String projectId) {
        Map<String,Object> result = new HashedMap();
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategies = examStrategyMapper.selectList(params);
        List<String> strategyNames = new ArrayList<String>();
        ExamStrategy examStrategyMin = null;
        Map<String, Object> SelectAllCountMap = new HashedMap();
        //一个不做处理,直接查出
        if(examStrategies.size() == 1){
            examStrategyMin = examStrategies.get(0);
            strategyNames.add(examStrategyMin.getRoleName());
            SelectAllCountMap = getTotalSelectAllCount(examStrategyMin);
        }else{
            //多个时取最小值selectAllCount、manyAllCount、judgeAllCount
            for(ExamStrategy examStrategy : examStrategies){
                if(null == examStrategyMin){
                    examStrategyMin = examStrategy;
                    continue;
                }
                //取最小值
                if(examStrategyMin.getSingleAllCount() > examStrategy.getSingleAllCount()){
                    examStrategyMin.setSingleAllCount(examStrategy.getSingleAllCount());
                }
                if(examStrategyMin.getManyAllCount() > examStrategy.getManyAllCount()){
                    examStrategyMin.setManyAllCount(examStrategy.getManyAllCount());
                }
                if(examStrategyMin.getJudgeAllCount() > examStrategy.getJudgeAllCount()){
                    examStrategyMin.setJudgeAllCount(examStrategy.getJudgeAllCount());
                }
                strategyNames.add(examStrategy.getRoleName());

                checkTotalSelectAllCount(SelectAllCountMap, getTotalSelectAllCount(examStrategy));
            }
        }
        List<Map<String,Object>> list = assembleExamStrategy(examStrategyMin);
        result.put("list", list);
        result.put("examStrategy", setTotalSelectAllCount(examStrategyMin, SelectAllCountMap));
        result.put("strategyNames", strategyNames);
        return result;
    }

    //必选题量
    public int getTotalSelectAllCount(Map<String, Object> params){
        List<ProjectCourse> projectCourseList = projectCourseMapper.selectByProjectIdAndRoleId(params);
        int count = 0;
        for(ProjectCourse projectCourse : projectCourseList){
            count += projectCourse.getSelectCount();
        }
        return count;
    }

    /**
     * 将最终的值放入strategy中
     * @param strategy
     * @param map
     * @return
     */
    public ExamStrategy setTotalSelectAllCount(ExamStrategy strategy, Map<String,Object> map){
        if(map.size() == 0){
            return strategy;
        }
        Object singAllCount = map.get("singAllCount");
        Object manyAllCount = map.get("manyAllCount");
        Object judgeAllCount = map.get("judgeAllCount");
        if(null != singAllCount){
            strategy.setSingleCount(Integer.parseInt(singAllCount.toString()));
        }
        if(null != manyAllCount){
            strategy.setManyCount(Integer.parseInt(manyAllCount.toString()));
        }
        if(null != judgeAllCount){
            strategy.setJudgeCount(Integer.parseInt(judgeAllCount.toString()));
        }
        return strategy;
    }

    /**
     * 合并两个map的集合
     * @param strategy1
     * @param strategy2
     */
    public void checkTotalSelectAllCount(Map<String,Object> strategy1, Map<String,Object> strategy2){
        if(strategy2.size() == 0){
            return;
        }
        int singAllCount1 = 0;
        int manyAllCount1 = 0;
        int judgeAllCount1 = 0;
        if(strategy1.size() > 0){
            singAllCount1 = Integer.parseInt(strategy1.get("singAllCount").toString());
            manyAllCount1 = Integer.parseInt(strategy1.get("manyAllCount").toString());
            judgeAllCount1 = Integer.parseInt(strategy1.get("judgeAllCount").toString());
        }
        int singAllCount2 = Integer.parseInt(strategy2.get("singAllCount").toString());
        int manyAllCount2 = Integer.parseInt(strategy2.get("manyAllCount").toString());
        int judgeAllCount2 = Integer.parseInt(strategy2.get("judgeAllCount").toString());
        if(singAllCount1 <= singAllCount2){
            strategy1.put("singAllCount", singAllCount2);
        }
        if(manyAllCount1 <= manyAllCount2){
            strategy1.put("manyAllCount", singAllCount2);
        }
        if(judgeAllCount1 <= judgeAllCount2){
            strategy1.put("judgeAllCount", singAllCount2);
        }
    }

    /**
     * 根据必选题量分出各个题库类型的数量
     * @param examStrategy
     */
    public Map<String,Object> getTotalSelectAllCount(ExamStrategy examStrategy){
        Map<String, Object> params = new HashedMap();
        params.put("projectId", examStrategy.getProjectId());
        params.put("roleId", examStrategy.getRoleId());
        List<ProjectCourse> projectCourseList = projectCourseMapper.selectByProjectIdAndRoleId(params);
        int singAllCount = 0;
        int manyAllCount = 0;
        int judgeAllCount = 0;
        for(ProjectCourse projectCourse : projectCourseList){
            int count = projectCourse.getSelectCount();
            if(count > 0){
                params = new HashedMap();
                params.put("intCourseId", projectCourse.getCourseId());
                //获取试题id集合
                List<String> ids = courseQuestionService.selectQuestionIdList(params);
                //获取不同试题类型数量
                params.put("ids", ids);
                Map<String, Object> typeCount = questionService.selectTypeCount(params);
                int singCount = Integer.parseInt(typeCount.get("singleAllCount").toString());
                int manyCount = Integer.parseInt(typeCount.get("manyAllCount").toString());
                int judgeCount = Integer.parseInt(typeCount.get("judgeAllCount").toString());
                if(singCount > count){
                    singCount = count;
                }
                if(manyCount > count){
                    manyCount = count;
                }
                if(judgeCount > count){
                    judgeCount = count;
                }
                singAllCount += singCount;
                manyAllCount += manyCount;
                judgeAllCount += judgeCount;
            }
        }
        Map<String,Object> result = new HashedMap();
        result.put("singAllCount", singAllCount);
        result.put("manyAllCount", manyAllCount);
        result.put("judgeAllCount", judgeAllCount);
        return result;
    }

    @Override
    public void updateExamStrategy(ExamStrategy examStrategy, Map<String, Object> param, User user) {
        ExamStrategy examStrategy_ = examStrategyMapper.selectByProjectIdAndRoleId(param);
        if(null == examStrategy_){
            return;
        }
        //从项目课程表中查询课程集合
        List<String> intCourseIds = projectCourseMapper.selectCourseIds(param);
        if (intCourseIds != null && intCourseIds.size()>0) {
            Map<String, Object> params = new HashedMap();
            params.put("intCourseIds", intCourseIds);
            //获取试题id集合
            List<String> ids = courseQuestionService.selectQuestionIdList(params);
            //获取不同试题类型数量
            params.put("ids", ids);
            Map<String, Object> typeCount = questionService.selectTypeCount(params);
            examStrategy.setSingleAllCount(Integer.parseInt(typeCount.get("singleAllCount").toString()));
            examStrategy.setManyAllCount(Integer.parseInt(typeCount.get("manyAllCount").toString()));
            examStrategy.setJudgeAllCount(Integer.parseInt(typeCount.get("judgeAllCount").toString()));
        }else{
            examStrategy.setSingleAllCount(0);
            examStrategy.setManyAllCount(0);
            examStrategy.setJudgeAllCount(0);
            examStrategy.setNecessaryHour(0);
        }
        //有数据就修改组卷策略
        this.update(examStrategy, user);
    }

    @Override
    public void updateExamStrategy(ExamStrategy examStrategy, Map<String, Object> param, User user, String operateType, List<String> courseIds) {
        ExamStrategy examStrategy_ = examStrategyMapper.selectByProjectIdAndRoleId(param);
        if(null == examStrategy_ || CollectionUtils.isEmpty(courseIds)){
            return;
        }
        Map<String, Object> params = new HashedMap();
        params.put("intCourseIds", courseIds);
        //获取试题id集合
        List<String> ids = courseQuestionService.selectQuestionIdList(params);
        //获取不同试题类型数量
        params.put("ids", ids);
        Map<String, Object> typeCount = questionService.selectTypeCount(params);
        if("saveCourse".equals(operateType)){
            examStrategy.setSingleAllCount(examStrategy_.getSingleAllCount() + Integer.parseInt(typeCount.get("singleAllCount").toString()));
            examStrategy.setManyAllCount(examStrategy_.getManyAllCount() + Integer.parseInt(typeCount.get("manyAllCount").toString()));
            examStrategy.setJudgeAllCount(examStrategy_.getJudgeAllCount() + Integer.parseInt(typeCount.get("judgeAllCount").toString()));
        }
        if("deleteCourse".equals(operateType)){
            examStrategy.setSingleAllCount(examStrategy_.getSingleAllCount() - Integer.parseInt(typeCount.get("singleAllCount").toString()));
            examStrategy.setManyAllCount(examStrategy_.getManyAllCount() - Integer.parseInt(typeCount.get("manyAllCount").toString()));
            examStrategy.setJudgeAllCount(examStrategy_.getJudgeAllCount() - Integer.parseInt(typeCount.get("judgeAllCount").toString()));
        }
        //有数据就修改组卷策略
        this.update(examStrategy, user);
    }

    @Override
    public String checkStrategy(String projectId) {
        Map<String, Object> params = new HashedMap();
        params.put("projectId", projectId);
        List<ExamStrategy> examStrategies = examStrategyMapper.selectList(params);

        if(examStrategies.size() < 1){
            return "组卷策略不合格!组卷不存在";
        }
        ProjectBasic projectBasic = projectBasicService.selectById(projectId);
        String exam_time = projectBasic.getProjectExamInfo();
        Gson gson = new Gson();
        Map<String, Object> examTime = gson.fromJson(exam_time, Map.class);
        Long beginTime = DateUtils.parseDateTime(String.valueOf(examTime.get("beginTime"))).getTime();
        Long endTime = DateUtils.parseDateTime(String.valueOf(examTime.get("endTime"))).getTime();
        Long time = (endTime - beginTime)/(1000*60);
        for(ExamStrategy examStrategy: examStrategies){
            if(examStrategy.getSingleAllCount() < examStrategy.getSingleCount() ||
                    examStrategy.getManyAllCount() < examStrategy.getManyCount() ||
                    examStrategy.getJudgeAllCount() < examStrategy.getJudgeCount()){
                return "组卷策略不合格!组卷数值大于分类总量";
            }
            if((examStrategy.getSingleCount() + examStrategy.getManyCount() + examStrategy.getJudgeCount()) == 0){
                return "组卷策略不合格!组卷数值和为0";
            }
            if(examStrategy.getTotalScore() < examStrategy.getPassScore()){
                return "组卷策略不合格!合格分大于总分";
            }

            Map<String, Object> result = getTotalSelectAllCount(examStrategy);
            int singAllCount = Integer.parseInt(result.get("singAllCount").toString());
            int manyAllCount = Integer.parseInt(result.get("manyAllCount").toString());
            int judgeAllCount = Integer.parseInt(result.get("judgeAllCount").toString());
            if(singAllCount > examStrategy.getSingleCount() ||
                    manyAllCount > examStrategy.getManyCount() ||
                    judgeAllCount > examStrategy.getJudgeCount()){
                return "组卷策略不合格!组卷数值小于必选题量值";
            }

            if(time < examStrategy.getExamDuration()){
                return "组卷策略不合格!考试时长(" + examStrategy.getExamDuration() + "分)大于项目时长(" + time + "分)";
            }
        }
        return "";
    }

}
