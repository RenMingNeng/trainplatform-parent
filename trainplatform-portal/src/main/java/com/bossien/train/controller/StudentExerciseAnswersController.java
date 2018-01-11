package com.bossien.train.controller;

import com.bossien.framework.mq.sender.QueueQuestionSender;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IExerciseAnswersService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by huangzhaoyong on 2017/8/1.
 * 练习答题
 */
@Controller
@RequestMapping("/student/question")
public class StudentExerciseAnswersController extends BasicController {
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    public static final Logger logger = LoggerFactory.getLogger(StudentExerciseAnswersController.class);
    @Autowired
    private QueueQuestionSender queueQuestionSender;
    @Autowired
    private IExerciseAnswersService exerciseAnswersService;

    /**
     * 保存练习
     * @param request
     * @param detail
     * @return
     */
    @ResponseBody
    @RequestMapping("/save_exercise")
    public Object save_exercise(HttpServletRequest request, String detail) throws Exception {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        if (null != detail && !detail.equals("")){
            Map<String,Object> queueMap = new HashedMap();
            queueMap.put("detail", detail);
            queueMap.put("userId", user.getId());
            queueMap.put("userName", user.getUserName());
            queueMap.put("companyId", user.getCompanyId());

            EXECUTOR.execute(new StudentExerciseAnswersController.SaveExerciseThread(queueMap));
        }
        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    private class SaveExerciseThread extends Thread {

        private Map<String,Object> queueMap;

        public SaveExerciseThread(Map<String,Object> queueMap) {
            this.queueMap = queueMap;
        }

        @Override
        public void run() {
            try {
                queueQuestionSender.sendMapMessage("tp.exercise.queue", queueMap);
            }catch(Exception ex) {
                logger.error(ex.getMessage(), ex);
            }
        }
    }
}
