package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ExamAnswersMapper;
import com.bossien.train.dao.tp.ExamPaperInfoMapper;
import com.bossien.train.domain.ExamAnswers;
import com.bossien.train.domain.ExamPaperInfo;
import com.bossien.train.domain.Question;
import com.bossien.train.domain.eum.QuestionsTypeEmun;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IExamAnswersService;
import com.bossien.train.service.IExamQuestionService;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class ExamAnswersServiceImpl implements IExamAnswersService {
    @Autowired
    private ExamAnswersMapper examAnswersMapper;
    @Autowired
    private IBaseService<ExamAnswers> baseService;
    @Autowired
    private ExamPaperInfoMapper examPaperInfoMapper;
    @Autowired
    private IExamQuestionService examQuestionService;

    @Override
    public Map<String, Object> insert(String detail, String exam_no, String user_name, ExamPaperInfo paperInfo){
        int point = 0;
        List<ExamAnswers> answersList = new ArrayList<>();
        //试卷详情
        if(!detail.equals("")){//没有选择答案
            String[] ansAry = detail.split(";");
            //考试题库

            Map<String, Object> params = new HashedMap();
            params.put("examNo", exam_no);
            List<Question> questions = examQuestionService.selectQuestionList(params);
            Map<String, Question> questionMap = listToMap(questions);
            ExamAnswers examAnswers;
            int score = 0;
            String IsCorrect;
            String projectId;
            String questionId;
            String answer;
            for(String ans : ansAry){
                if(null == ans || ans.equals("") || ans.indexOf("undefined") != -1){
                    continue;
                }
                // project_id + "," + question_id + "," + select;
                String[] datas = ans.split(",");
                if(datas.length < 3){
                    continue;
                }
                projectId = datas[0];questionId = datas[1];answer = datas[2];
                Question question = questionMap.get(questionId);
                if(null == question){
                    continue;
                }
                //组装答题记录、计算分数
                examAnswers = new ExamAnswers();
                if(null != question.getVarAnswer() && question.getVarAnswer().equals(answer)){//答对
                    score = getScore(question, paperInfo);
                    IsCorrect = "1";
                    //统计
                    point += score;
                }else{//答错
                    IsCorrect = "2";
                }
                examAnswers.setIs_correct(IsCorrect);
                examAnswers.setScore(score);
                examAnswers.setExam_no(exam_no);
                examAnswers.setQuestion_id(questionId);
                examAnswers.setProject_id(projectId);
                examAnswers.setAnswer(answer);
                answersList.add(baseService.build(user_name, examAnswers));
            }
        }

        Map<String, Object> result = new HashedMap();
        Integer passScore = Integer.parseInt(String.valueOf(paperInfo.getPassScore()));
        result.put("pass_score", passScore);
        result.put("my_score", point);
        if(point >= passScore){//1:合格 2:不合格
            result.put("isPassed", "1");
        }else{
            result.put("isPassed", "2");
        }

        //controller发送消息队列，将需要的数据返回
        Map<String, Object> data = new HashedMap();
        data.put("result", result);
        data.put("answers", answersList);
        return data;
    }

    /**
     * 判断对错
     * @param question
     * @param paperInfo
     * @return
     */
    public Integer getScore(Question question, ExamPaperInfo paperInfo){
        String chrType = question.getChrType();
        if(chrType.equals(QuestionsTypeEmun.QUESTIONTYPE_SINGLE.getValue())){
            return Integer.parseInt(String.valueOf(paperInfo.getSingleScore()));
        }else if(chrType.equals(QuestionsTypeEmun.QUESTIONTYPE_MANY.getValue())){
            return Integer.parseInt(String.valueOf(paperInfo.getManyScore()));
        }else if(chrType.equals(QuestionsTypeEmun.QUESTIONTYPE_JUDGE.getValue())){
            return Integer.parseInt(String.valueOf(paperInfo.getJudgeScore()));
        }
        return 0;
    }

    /**
     * list转换成Map
     * @param questions
     * @return
     */
    public Map<String, Question> listToMap(List<Question> questions){
        Map<String, Question> result = new HashedMap();
        for(Question question : questions){
            result.put(question.getIntId(), question);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return examAnswersMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return examAnswersMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {

        return examAnswersMapper.selectOne(params);
    }
}
