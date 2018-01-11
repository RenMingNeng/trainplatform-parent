package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueVideoSender;
import com.bossien.train.domain.User;
import com.bossien.train.domain.VideoPosition;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IVideoPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("student/videoPosition")
public class videoPositionController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);
    @Autowired
    private IVideoPositionService videoPositionService;

    @RequestMapping("savePosition")
    @ResponseBody
    public Object saveVideoPosition(
            @RequestParam(value = "course_id", required = true, defaultValue = "") String course_id,             //课程id
            @RequestParam(value = "video_id", required = true, defaultValue = "") String video_id,        //视频或文档id
            @RequestParam(value = "study_time", required = true, defaultValue = "0") Integer lastPosition,       //学习位置
            HttpServletRequest request
    ){
        User user = getCurrUser(request);
        Response<Object> response = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId",user.getId());
        params.put("courseId",course_id);
        params.put("videoId",video_id);
        params.put("lastPosition",Integer.valueOf(lastPosition));

        videoPositionService.saveLastPosition(params);
        response.setCode(Code.SUCCESS.getCode());
        return response;
    }

    @RequestMapping("getPosition")
    @ResponseBody
    public Object getPosition(
            @RequestParam(value = "course_id", required = true, defaultValue = "") String course_id,             //课程id
            @RequestParam(value = "video_id", required = true, defaultValue = "") String video_id,        //视频或文档id
            HttpServletRequest request
    ){
        User user = getCurrUser(request);
        Response<Object> response = new Response<Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId",user.getId());
        params.put("courseId",course_id);
        params.put("videoId",video_id);

        /*//VideoPosition videoPosition = videoPositionService.selectOne(params);
        if(videoPosition == null){
            response.setResult(0);
        }
        response.setResult(videoPosition.getLastPosition());*/
        return response;
    }
}
