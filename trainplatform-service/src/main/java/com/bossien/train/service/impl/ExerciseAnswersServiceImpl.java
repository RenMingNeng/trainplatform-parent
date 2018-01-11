package com.bossien.train.service.impl;

import com.bossien.train.dao.tp.ExerciseAnswersMapper;
import com.bossien.train.domain.ExerciseAnswers;
import com.bossien.train.domain.User;
import com.bossien.train.service.IBaseService;
import com.bossien.train.service.IExerciseAnswersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhaoyong on 2017/7/25.
 */

@Service
public class ExerciseAnswersServiceImpl implements IExerciseAnswersService {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    @Autowired
    private ExerciseAnswersMapper exerciseAnswersMapper;
    @Autowired
    private IBaseService<ExerciseAnswers> baseService;

    @Override
    public void insert(String detail, User user) throws Exception{
//        List<ExerciseAnswers> answersList = new ArrayList<>();
//        String[] ansAry = detail.split(";");
//        if(ansAry.length < 1){
//            return;
//        }
//        for(String ans : ansAry){
//            ExerciseAnswers exerciseAnswers = new ExerciseAnswers();
//            if(null == ans || ans.equals("") || ans.indexOf("undefined") != -1){
//                continue;
//            }
//            //question_id + "," + project_id + "," + answer + "," + isTrue;
//            String[] params = ans.split(",");
//            exerciseAnswers.setQuestion_id(params[0]);
//            exerciseAnswers.setProject_id(params[1]);
//            exerciseAnswers.setAnswer(params[2]);
//            exerciseAnswers.setIs_correct(params[3]);
//            answersList.add(baseService.build(user, exerciseAnswers));
//        }

        // 发送到activemq的study_time.queue
//        Map<String,Object> queueMap = new ConcurrentHashMap<String, Object>();
//        queueMap.put("project_id", project_id);
//        queueMap.put("course_id", course_id);
//        queueMap.put("user_id", user_id);
//        queueMap.put("source", source);
//        queueMap.put("study_time", study_time);
//        EXECUTOR.execute(new StudentStudyController.AnalysDeal(queueMap));
        //添加
    }

    @Override
    public List<Map<String, Object>> selectList(Map<String, Object> params) {

        return exerciseAnswersMapper.selectList(params);
    }

    @Override
    public Integer selectCount(Map<String, Object> params) {

        return exerciseAnswersMapper.selectCount(params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {

        return exerciseAnswersMapper.selectOne(params);
    }
}
