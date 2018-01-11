package com.bossien.train.service.impl;

import com.bossien.train.dao.ex.QuestionMapper;
import com.bossien.train.dao.tp.ExamQuestionMapper;
import com.bossien.train.dao.tp.ProjectCourseMapper;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.ExamTypeEnum;
import com.bossien.train.service.*;
import com.bossien.train.util.DateUtils;
import com.google.gson.Gson;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class ExamQuestionServiceImpl implements IExamQuestionService {

    @Autowired
    private ExamQuestionMapper examQuestionMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectBasicService projectBasicService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private IBaseService<ExamQuestion> baseService;
    @Autowired
    private ProjectCourseMapper projectCourseMapper;
    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;

    @Override
    public void insert(ExamQuestion examQuestion) {

        examQuestionMapper.insert(examQuestion);
    }

    public int insertBatch(List<ExamQuestion> examQuestions) {

        return examQuestionMapper.insertBatch(examQuestions);
    }

    @Override
    public List<String> selectList(Map<String, Object> params) {
        List<String> questions = examQuestionMapper.selectList(params);
        if(null != questions && questions.size() > 0){
            String questionIds = questions.get(0);
            return Arrays.asList(questionIds.split(","));
        }
        return new ArrayList<String>();
    }

    @Override
    public List<Question> selectQuestionList(Map<String, Object> params) {
        List<String> questionIds = examQuestionMapper.selectList(params);
        if(null != questionIds && questionIds.size() > 0){
            params = new HashedMap();
            params.put("intIds", Arrays.asList(questionIds.get(0).split(",")));
            params.put("chrValid", "1");
            return questionMapper.selectList(params);
        }
        return new ArrayList<>();
    }

    
    public Integer selectCount(Map<String, Object> params) {

        return examQuestionMapper.selectCount(params);
    }

    @Override
    public String createPaper(Map<String, Object> params, User user) {
        String project_id = String.valueOf(params.get("projectId"));
        String exam_type = String.valueOf(params.get("examType"));
        String exam_no = String.valueOf(params.get("examNo"));

        //判断字符串为"null"的数据
        if("null".equals(project_id) || "null".equals(exam_type)) {
            return "";
        }

        //检查考试时间
        ProjectBasic projectBasic = projectBasicService.selectById(project_id);
        if(!checkExamTime(projectBasic)){
            return "不在考试时间范围之内不能考试!";
        }

        // 检查是否有未考试试卷
        params.put("examStatus", "1");
        params.put("userId", user.getId());
        params.remove("examNo");
        ExamPaperInfo examPaperInfo = examPaperInfoService.selectOne(params);
        if (null != examPaperInfo && null != examPaperInfo.getExamNo()) {
            return examPaperInfo.getExamNo();
        }

        params.put("examNo", exam_no);
        ProjectUser projectUser = projectUserService.selectByProjectIdAndUserId(project_id, user.getId());
        if(null == projectUser){
            return "您不在该考试项目中不能考试!";
        }

        //根据examId查询组卷策略
        params.put("roleId", projectUser.getRoleId());
        ExamStrategy examStrategy = examStrategyService.selectByProjectIdAndRoleId(project_id, projectUser.getRoleId());
        if(null == examStrategy){
            return "组卷策略不合法，无法组卷!";
        }

        //检测学时要求是否符合要求
        if(ExamTypeEnum.ExamType_2.getValue().equals(exam_type)){
            //考试次数检查
            Gson gson = new Gson();
            Map<String, Object> project_exam_info = gson.fromJson(projectBasic.getProjectExamInfo(), Map.class);
            if(!checkExamCount(project_id, exam_type, String.valueOf(project_exam_info.get("count")), user)){
                return "您超过最大考试次数不能考试!";
            }

            if(!"0".equals(examStrategy.getNecessaryHour())){
                //totalQuestion 培训学时
                ProjectStatisticsInfo projectStatisticsInfo = projectStatisticsInfoService.selectOne(params);
                //判断用户学时是否大于组卷策略中的必修学时要求
                if(examStrategy.getNecessaryHour() * 60 > projectStatisticsInfo.getTotalStudyTime()){
                    return "没有达到学时要求，不能进行考试!";
                }
            }
        }
        return "";
    }

    @Override
    public void deleteBatch(Map<String, Object> params) {
        examQuestionMapper.deleteBatch(params);
    }

    /**
     * //查询考试对象，获取正式考试次数限制
     * @param project_id
     * @param exam_type
     * @param exam_count
     * @param user
     * @return
     */
    public boolean checkExamCount(String project_id, String exam_type, String exam_count, User user){
        // 判断正式考试次数合法
        Map<String, Object> params = new HashedMap();
        params.put("projectId", project_id);
        params.put("examType", exam_type);
        params.put("userId", user.getId());
        int count = examPaperInfoService.selectCount(params);
        if(count >= Integer.parseInt(exam_count)) {
            return false;
        }
        return true;
    }

    /**
     * //查询考试对象，获取正式考试次数限制
     * @param projectBasic
     * @return
     */
    public boolean checkExamTime(ProjectBasic projectBasic){
        if(null == projectBasic){
            return false;
        }
        String exam_time = projectBasic.getProjectExamInfo();
        if(null == exam_time){
            return false;
        }
        Gson gson = new Gson();
        Map<String, Object> time = gson.fromJson(exam_time, Map.class);
        String beginTime = String.valueOf(time.get("beginTime"));
        String endTime = String.valueOf(time.get("endTime"));
        if(DateUtils.parseDateTime(beginTime).getTime() <= System.currentTimeMillis() &&
                DateUtils.parseDateTime(endTime).getTime() >= System.currentTimeMillis()){
            return true;
        }
        return false;
    }
}
