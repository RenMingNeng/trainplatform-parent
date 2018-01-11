package com.bossien.train.listener;

import com.bossien.framework.mq.listener.AbstractListener;
import com.bossien.train.domain.*;
import com.bossien.train.domain.eum.QuestionsTypeEmun;
import com.bossien.train.service.*;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.*;
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
@Component("queuePaperListener")
public class QueuePaperListener extends AbstractListener {

    public static final Logger logger = LoggerFactory.getLogger(QueuePaperListener.class);

    @Autowired
    private ICourseQuestionService courseQuestionService;
    @Autowired
    private IExamPaperInfoService examPaperInfoService;
    @Autowired
    private IProjectStatisticsInfoService projectStatisticsInfoService;
    @Autowired
    private IBaseService<ExamQuestion> baseService;
    @Autowired
    private IExamStrategyService examStrategyService;
    @Autowired
    private IProjectUserService projectUserService;
    @Autowired
    private IProjectCourseService projectCourseService;
    @Autowired
    private IExamQuestionService examQuestionService;

    @Override
    public <V> void mapMessageHander(Map<String, V> map) throws ParseException {
        super.mapMessageHander(map);
        String userId = String.valueOf(map.get("userId"));
        String userName = String.valueOf(map.get("userName"));
        String examNo = String.valueOf(map.get("examNo"));
        String projectId = String.valueOf(map.get("projectId"));
        String examType = String.valueOf(map.get("examType"));

        //判断是否已经保存了考卷
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("examNo", examNo);
        ExamPaperInfo examPaperInfo = examPaperInfoService.selectOne(params);
        if(null != examPaperInfo){
            return;
        }

        //获取角色
        ProjectUser projectUser = projectUserService.selectByProjectIdAndUserId(projectId, userId);
        if(null == projectUser){
            return;
        }

        //根据examId查询组卷策略
        ExamStrategy examStrategy = examStrategyService.selectByProjectIdAndRoleId(projectId, projectUser.getRoleId());
        if(null == examStrategy){
            return;
        }

        //判断必选题量跟组卷数量的合法性
        params = new HashedMap();
        params.put("projectId", projectId);
        params.put("roleId", examStrategy.getRoleId());
        List<ProjectCourse> projectCourseList = projectCourseService.selectByProjectIdAndRoleId(params);

        //组卷
        Integer totalCount = examStrategy.getSingleCount() + examStrategy.getManyCount() + examStrategy.getJudgeCount();
        List<ExamQuestion> questions = checkDataLawful(projectId, examStrategy, projectCourseList, userId, userName);
        if(null != questions && questions.size() > 0){
            ExamQuestion examQuestion = questions.get(0);
            examQuestion = baseService.build(userName,examQuestion);
            String questionIds = "";
            for (ExamQuestion e : questions) {
                questionIds += questionIds == "" ? e.getQuestionsId() : "," + e.getQuestionsId();
            }
            examQuestion.setQuestionsId(questionIds);
            examQuestion.setExamNo(examNo);
            examQuestionService.insert(examQuestion);

            //添加考试详情
            examPaperInfoService.insert(examNo, examType, examStrategy, userId, userName);
        }
    }

    /**
     * 生成考试试卷
     */
    public List<ExamQuestion> makeExamPaper(Map<String, List<Question>> selectQuestionsByCourseId,
                                            Map<String, ProjectCourse> selectCourseByCourseId, ExamStrategy examStrategy,
                                            String project_id, String userId, String userName){
        List<ExamQuestion> select = new ArrayList<>();

        //根据组卷策略、必选题数量生成必选题
        select.addAll(makeMustPaper(selectQuestionsByCourseId, examStrategy, selectCourseByCourseId, project_id, userId, userName));

        //去掉list中重复的考试题目
        Map<String, ExamQuestion> mlist = new HashMap<>();
        for (int i=0; i < select.size(); i++) {
            ExamQuestion examQuestion = select.get(i);
            String questions_id = examQuestion.getQuestionsId();
            if (null != questions_id) {
                if (null != mlist.get(questions_id)) {
                    select.remove(i);
                    i--;
                }
                mlist.put(questions_id, examQuestion);
            }
        }
        return select;
    }

    /**
     * 根据组卷策略、必选题数量生成必选题
     */
    public List<ExamQuestion> makeMustPaper(Map<String, List<Question>> selectQuestionsByCourseId, ExamStrategy examStrategy,
                                            Map<String, ProjectCourse> selectCourseByCourseId, String project_id,
                                            String userId, String userName){
        List<ExamQuestion> select = new ArrayList<>();
        List<Question> selectList = new ArrayList<>();
        List<Question> loseList = new ArrayList<>();

        //、courseAndQuestionMap，根据必选题量将一部分数据保存到考试表中，将已经保存的数据再courseAndQuestionMap中values中删除
        //获取role下面的 课程-考试课程表
        Iterator<String> itCourse = selectQuestionsByCourseId.keySet().iterator();
        while(itCourse.hasNext()){
            String courseId = itCourse.next();
            List<Question> questions = selectQuestionsByCourseId.get(courseId);

            ProjectCourse course = selectCourseByCourseId.get(courseId);
            int intSelectCount = 0;
            if(null != course){
                intSelectCount = course.getSelectCount();//必选题数量---判断是否为0
            }
            if(intSelectCount != 0){
                Collections.shuffle(questions);//洗牌
                //如果无法满足必选题量
                if(questions.size() < intSelectCount){
                    return new ArrayList<>();
                }
                //根据必选题量获取题目------如果课程中包含相同的课程，数量照样
                List<Question> must = questions.subList(0, intSelectCount);
                select.addAll(mapToExamQuestion(must, project_id, userId, userName));
                selectList.addAll(must);
            }
            //将selectQuestionsByCourseId的每个角色中剩余values的数据集合lostQuestions
            loseList.addAll(questions.subList(intSelectCount, questions.size()));
        }

        Map<String, Integer> typeCount = getTypeCount(selectList);//统计上面必选题的 类型--数量
//        //根据组卷策略生产考卷(减去必选题的数量，然后随机抽取)
        select.addAll(makePaperByStrategy(loseList, examStrategy, typeCount, project_id, userId, userName));
        return select;
    }

    /**
     * 统计必选题的类型--数量
     * @param select
     * @return
     */
    public Map<String, Integer> getTypeCount(List<Question> select){
        Map<String, Integer> typeCount = new HashMap<>();
        for (Question question : select) {
            String intQuestionsType = question.getChrType();
            if(null != intQuestionsType){
                int count = 1;
                if(null != typeCount.get(intQuestionsType)){
                    count = typeCount.get(intQuestionsType);
                }
                typeCount.put(intQuestionsType, count);
            }
        }
        return typeCount;
    }

    /**
     * 根据组卷策略生产考卷(减去必选题的数量，然后随机抽取)
     * @return
     */
    public List<ExamQuestion> makePaperByStrategy(List<Question> questionList, ExamStrategy examStrategy,
                                                  Map<String, Integer> typeCount, String project_id, String userId, String userName){
        List<ExamQuestion> select = new ArrayList<>();
        //根据角色，将单选、多选、判断题分开成map<role, map<type,list>> typeList
        Map<String, List<Question>> typeQuestionMap = getTypeQuestionMap(questionList);

        //在根据每个角色的组卷策略随机获取数据并保存
        Iterator<String> itType = typeQuestionMap.keySet().iterator();
        while(itType.hasNext()){
            String type = itType.next();
            List<Question> result = typeQuestionMap.get(type);
            int length = getSubLengthByStrategy(type, examStrategy, typeCount);
            //必选题量设置不合格
            if(length < 0){
                return new ArrayList<>();
            }
            if(null != result && length > 0){
                if(result.size() < length){
//                    throw new RuntimeException("\"" + QuestionsTypeEmun.getName(type) + "\"数量不合法：" + length + "大于" + result.size());
                    return new ArrayList<>();
                }
                Collections.shuffle(result);//洗牌
                select.addAll(mapToExamQuestion(result.subList(0, length), project_id, userId, userName));
            }
        }
        return select;
    }

    /**
     * 根据组卷策略获取 type 的题目要求数量
     * @param type
     * @param examStrategy
     * @param typeCount 已选的题量类型统计
     * @return
     */
    public int getSubLengthByStrategy(String type, ExamStrategy examStrategy, Map<String, Integer> typeCount){
        //根据类型获取组卷数量
        if(null == examStrategy) {
            return 0;
        }
        String questiontype_single = QuestionsTypeEmun.QUESTIONTYPE_SINGLE.getValue();
        String questiontype_many = QuestionsTypeEmun.QUESTIONTYPE_MANY.getValue();
        String questiontype_judge = QuestionsTypeEmun.QUESTIONTYPE_JUDGE.getValue();
        if(type.equals(questiontype_single)) {
            return examStrategy.getSingleCount() - getMustQuestionCount(typeCount,
                questiontype_single);
        }
        if(type.equals(questiontype_many)) {
            return examStrategy.getManyCount() - getMustQuestionCount(typeCount, questiontype_many);
        }
        if(type.equals(questiontype_judge)) {
            return examStrategy.getJudgeCount() - getMustQuestionCount(typeCount,
                questiontype_judge);
        }
        return 0;
    }

    /**
     * 过滤必选题的类型数量
     * @return
     */
    public int getMustQuestionCount(Map<String, Integer> typeCount, String type){
        if(null != typeCount){
            Integer count = typeCount.get(type);
            if(null != count){
                return count;
            }
        }
        return 0;
    }

    /**
     * 根据角色，将单选、多选、判断题分开成map<role, map<type,list>> typeList
     * @return
     */
    public Map<String, List<Question>> getTypeQuestionMap(List<Question> questionList){
        Map<String, List<Question>> typeQuestionMap = new HashMap<>();
        for(Question question : questionList){
            String type = question.getChrType();
            List<Question> data = new ArrayList<>();
            if(null != typeQuestionMap.get(type)){
                data = typeQuestionMap.get(type);
            }
            data.add(question);
            typeQuestionMap.put(type, data);
        }
        return typeQuestionMap;
    }

    /**
     * 将map转换成ExamQuestion对象
     * @param list
     * @param project_id
     * @param userId
     * @param userName
     * @return
     */
    public List<ExamQuestion> mapToExamQuestion(List<Question> list, String project_id, String userId, String userName){
        List<ExamQuestion> questions = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setProjectId(project_id);
            examQuestion.setUserId(userId);
            examQuestion.setQuestionsId(list.get(i).getIntId());
            questions.add(baseService.build(userName, examQuestion));
        }
        return questions;
    }

    /**
     * 判断合法性
     * @param project_id
     * @return
     */
    public List<ExamQuestion> checkDataLawful(String project_id, ExamStrategy examStrategy,
                                              List<ProjectCourse> projectCourseList, String userId, String userName){
        //先根据varExamId查询所有的 （课程与题库的关联表）集合
        List<Question> questions = courseQuestionService.selectQuestionByProjectAndUserId(project_id, userId);

        //去掉重复的(一个题目对应多个课程)
        questions = getDistinctList(questions);

        if(null == questions || questions.size() < 1){
            return new ArrayList<>();
        }

        //将CourseAndQuestionMap修改成map<课程id,list题库集合>
        //map<question_id,course_id> courseQuestion
        Map<String, String> courseQuestion = courseQuestionService.selectCourseIdByQuestionId(project_id, userId);
        Map<String, List<Question>> selectQuestionsByCourseId = getCourseAndQuestionMap(courseQuestion, questions);

        //将roleCourseMap转换成map<课程id，ProjectCourse>（获取必选题数量）
        Map<String, ProjectCourse> selectCourseByCourseId = getCourseMap(projectCourseList);

        return makeExamPaper(selectQuestionsByCourseId, selectCourseByCourseId, examStrategy, project_id, userId, userName);
    }

    /**
     * 去掉集合中重复的
     * @return
     */
    public static List<Question> getDistinctList(List<Question> data){
        String ids = "";
        List<Question> result = new ArrayList<>();
        for (Question question : data) {
            if(ids.indexOf(question.getIntId()) != -1){
                continue;
            }
            ids = ids == "" ? question.getIntId() : "," + question.getIntId();
            result.add(question);
        }
        return result;
    }

    /**
     * 将courseAndQuestionMap修改成map<课程id,list题库集合>
     *     courseQuestion
     * @return
     */
    public Map<String, List<Question>> getCourseAndQuestionMap(Map<String, String> courseQuestion, List<Question> questions){
        Map<String, List<Question>> result = new HashMap<>();
        for (Question question : questions) {
            String questionId = question.getIntId();
            String varCourseId = courseQuestion.get(questionId);
            if(null != varCourseId){
                List<Question> questionList = new ArrayList<>();
                if(null != result.get(varCourseId)){
                    questionList = result.get(varCourseId);
                }
                questionList.add(question);

                result.put(varCourseId, questionList);
            }
        }
        return result;
    }

    public Map<String, ProjectCourse> getCourseMap(List<ProjectCourse> projectCourseList){
        Map<String, ProjectCourse> result = new HashMap<>();
        for (ProjectCourse projectCourse : projectCourseList) {
            String courseId = projectCourse.getCourseId();
            if(null != courseId){
                result.put(courseId, projectCourse);
            }
        }
        return result;
    }
}
