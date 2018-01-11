package com.bossien.train.controller;

import com.bossien.train.domain.Feedback;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.eum.ProblemTypeEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IFeedbackService;
import com.bossien.train.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("/feedback")
public class FeedbackController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private IFeedbackService feedbackService;

    @RequestMapping("")
    public String feedback(
            HttpServletRequest request, ModelMap modelMap
        ) {

        return "feedback/index";
    }

    @RequestMapping("list")
    @ResponseBody
    public Object list(@RequestParam(value="search", required = false, defaultValue = "") String search,
                       @RequestParam(value="startTime", required = false, defaultValue = "") String startTime,
                       @RequestParam(value="endTime", required = false, defaultValue = "") String endTime,
                       @RequestParam(value="problemType", required = false, defaultValue = "") String problemType,
                       @RequestParam(value="pageSize", required = false, defaultValue = "20") Integer pageSize,
                       @RequestParam(value="pageNum", required = false, defaultValue = "1") Integer pageNum
        ) throws ServiceException{
        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        //开始时间补充00:00:00
        if(!StringUtils.isEmpty(startTime)){
            startTime = startTime+" 00:00:00";
        }
        //结束时间补充23:59:59
        if(!org.apache.commons.lang.StringUtils.isEmpty(endTime)){
            endTime = endTime+" 23:59:59";
        }

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("problemType", problemType);
        if(!StringUtils.isEmpty(search)) {
            params.put("search", ParamsUtil.joinLike(search));
        }

        Integer count = feedbackService.selectCount(params);

        Page page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("pageSize", page.getPageSize());

        List<Feedback> feedbacks = feedbackService.selectList(params);
        for(Feedback feedback: feedbacks){
            feedback.setProblemType(ProblemTypeEnum.get(feedback.getProblemType()).getName());
        }

        page.setDataList(feedbacks);
        resp.setResult(page);

        return resp;
    }

    @ResponseBody
    @RequestMapping("update")
    public Object update_save(HttpServletRequest request, Feedback feedback){
        String resourcePath = request.getServletContext().getInitParameter("resource_path");

        if(null != feedback.getId() && null != feedback.getProblemStatus()){
            feedbackService.update(feedback);
        }
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

}
