package com.bossien.train.controller;

import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IDeptSuperviseService;
import com.bossien.train.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by A on 2017/11/27.
 * 部门监管
 */
@Controller
@RequestMapping("admin/deptSupervise")
public class DeptSuperviseController extends BasicController {
    @Autowired
    private IDeptSuperviseService deptSuperviseService;

    @RequestMapping("")
    public String index() {
        return "admin/deptSupervise/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="company_id", required = true, defaultValue = "") String companyId,
                       @RequestParam(value="dept_id", required = true, defaultValue = "") String deptId,
                       @RequestParam(value="dept_name", required = true, defaultValue = "") String deptName,
                       @RequestParam(value="search", required = true, defaultValue = "") String search,
                       @RequestParam(value="startTime", required = true, defaultValue = "") String startTime,
                       @RequestParam(value="endTime", required = true, defaultValue = "") String endTime,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        //开始时间补充00:00:00
        if(!StringUtils.isEmpty(startTime)){
            startTime = startTime+" 00:00:00";
        }
        //结束时间补充23:59:59
        if(!StringUtils.isEmpty(endTime)){
            endTime = endTime+" 23:59:59";
        }

        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> result;
        //时间都为空或者开始时间为空、结束时间大于当前时间
        boolean noTimeBtn = (StringUtils.isEmpty(startTime) && StringUtils.isEmpty(endTime)) ||
                (StringUtils.isEmpty(startTime) && StringUtils.isNotEmpty(endTime) && DateUtils.parseDateTime(endTime).getTime() > System.currentTimeMillis());
        if(noTimeBtn){
            result = deptSuperviseService.queryForPagination(companyId, deptId, deptName, search, pageNum, pageSize);
        }else{
            result = deptSuperviseService.queryForPagination(companyId, deptId, deptName, search, startTime, endTime, pageNum, pageSize);
        }

        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(result);
        return resp;
    }
}
