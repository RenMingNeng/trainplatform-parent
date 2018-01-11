package com.bossien.train.controller;

import com.bossien.framework.db.DataSource;
import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Feedback;
import com.bossien.train.domain.dto.CompanyCourseMessage;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyCourseService;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.IFeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class TestController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private IFeedbackService feedbackService;
    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanyCourseService companyCourseService;

    /**
     * 此方法没有被注解@Transactional作用,不会回滚导致产生脏数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/exception/no/transactional")
    public Response exception_no_transactional() {
        CompanyCourseMessage companyCourseMessage = new CompanyCourseMessage();
        companyCourseMessage.setCompanyId("123");
        companyCourseMessage.setCourseId("456");
        companyCourseMessage.setCompanyCode("789");
        companyCourseMessage.setCompanyCourseTypeId("1111111111");
        companyCourseService.insertSelective(companyCourseMessage);

        Feedback feedback01 = new Feedback("123","test10", "test1", "", "", "", "", "", "2017-09-13 14:21:00");
        feedbackService.insert(feedback01);

        Feedback feedback02 = new Feedback("","test20", "test2", "", "", "", "", "", "");
        feedbackService.insert(feedback02);
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 此方法被注解@Transactional作用, 发生错误产生回滚导致产生脏数据
     * @return
     */
    @Transactional(DataSource.RES)
    @ResponseBody
    @RequestMapping("/exception/yes/transactional")
    public Response exception_yes_transactional() {
        CompanyCourseMessage companyCourseMessage = new CompanyCourseMessage();
        companyCourseMessage.setCompanyId("123");
        companyCourseMessage.setCourseId("456");
        companyCourseMessage.setCompanyCode("789");
        companyCourseMessage.setCompanyCourseTypeId("1111111111");
        companyCourseService.insertSelective(companyCourseMessage);

        Feedback feedback03 = new Feedback("123","test30", "test3", "", "", "", "", "", "2017-09-13 14:23:00");
        feedbackService.insert(feedback03);

        Feedback feedback04 = new Feedback("","test40", "test4", "", "", "", "", "", "");
        feedbackService.insert(feedback04);
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 此方法没有被注解@Transactional作用,不会回滚导致产生脏数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/exception/no/transactional/select")
    public Response exception_no_transactional_select() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", "test3");
        params.put("startNum", 0);
        params.put("pageSize", 99999);
        List<Feedback> feedbacks = feedbackService.selectList(params);
        Response rs = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        rs.setResult(feedbacks);
        return rs;
    }

    /**
     * 此方法被注解@Transactional作用, 发生错误产生回滚导致产生脏数据
     * @return
     */
    @Transactional(DataSource.RES)
    @ResponseBody
    @RequestMapping("/exception/yes/transactional/select")
    public Response exception_yes_transactional_select() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", "test3");
        params.put("startNum", 0);
        params.put("pageSize", 99999);
        List<Feedback> feedbacks = feedbackService.selectList(params);
        Response rs = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        rs.setResult(feedbacks);
        return rs;
    }
}
