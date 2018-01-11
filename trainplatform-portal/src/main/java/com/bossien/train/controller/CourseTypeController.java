package com.bossien.train.controller;

import com.alibaba.fastjson.JSONObject;
import com.bossien.framework.mq.sender.QueueCourseTypeSender;
import com.bossien.train.dao.ap.CompanyCourseTypeMapper;
import com.bossien.train.domain.CourseType;
import com.bossien.train.domain.User;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyCourseTypeService;
import com.bossien.train.service.ICourseTypeService;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2017/8/2.
 */
@Controller
@RequestMapping(value = "/courseType")
public class CourseTypeController extends BasicController {
    @Autowired
    private ICompanyCourseTypeService companyCourseTypeService;

    /**
     * 加载课程类别树（授权课程）
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/courseType_tree")
    @ResponseBody
    public Object courseType_tree(HttpServletRequest request) {
        User user = getCurrUser(request);
        String intCompanyId = user.getCompanyId();
        Response<Object> response = new Response<Object>();
        Map<String, Object> params = new HashMap();
        params.put("intCompanyId", intCompanyId);
        //查询某公司下的课程类别集合
        List<Map<String, Object>> companyCourseTypeIds = companyCourseTypeService.selectByCompanyId(params);
       /*if(null!=intCourseTypeIds){
       List<String> intIds=new ArrayList <String>();
           for (Map map:intCourseTypeIds) {
               intIds.add(map.get("intCourseTypeId").toString());
           }
        //根据intIds查询课程类别集合
       List<CourseType> courseTypes=courseTypeService.selectByIntIds(intIds);*/
        //组装课程分类树数据
        List<Map<String, Object>> courseTypeTrees = companyCourseTypeService.assemblecourseTypeTree(companyCourseTypeIds);
        //转成数组
        Object[] courseTypeZTrees = courseTypeTrees.toArray();
        response.setResult(courseTypeZTrees);

        return response;
    }



}
