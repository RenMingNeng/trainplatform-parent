package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IClassHoursService;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.ICompanySuperviseService;
import com.bossien.train.service.ICompanyTjService;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("supervise")
public class SuperviseController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanySuperviseService companySuperviseService;
    @Autowired
    private ICompanyTjService companyTjService;
    @Autowired
    private IClassHoursService classHoursService;


    @RequestMapping("")
    public String index() {
        return "supervise/index";
    }

    @RequestMapping("menu")
    public String menu() { return "supervise/menu"; }


    @RequestMapping("init")
    @ResponseBody // 单位监管初始化
    public String init() {

        return "supervise/menu";
    }

    @RequestMapping("/tree")
    @ResponseBody
    public Object tree(HttpServletRequest request) {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        Response<Object> res = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        String tree = companySuperviseService.getSuperviseTree(user.getCompanyId());
        res.setResult(tree);
        return res;
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="companyId", required = true, defaultValue = "") String companyId,
                       @RequestParam(value="searchName", required = true, defaultValue = "") String searchName,
                       @RequestParam(value="companyName", required = true, defaultValue = "") String companyName,
                       @RequestParam(value="startTime", required = true, defaultValue = "") String startTime,
                       @RequestParam(value="endTime", required = true, defaultValue = "") String endTime,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        if(StringUtil.isEmpty(companyId)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        //开始时间补充00:00:00
        if(!StringUtils.isEmpty(startTime)){
            startTime = startTime+" 00:00:00";
        }
        //结束时间补充23:59:59
        if(!StringUtils.isEmpty(endTime)){
            endTime = endTime+" 23:59:59";
        }

        //公司名称
        if(StringUtils.isEmpty(companyName)){
            String companyName_key = PropertiesUtils.getValue("company_name");
            companyName = request.getSession().getAttribute(companyName_key).toString();
        }

        Response<Object> resp = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> result = companyTjService.queryForPagination(companyId,
                searchName, companyName, startTime, endTime, user, pageNum, pageSize);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(result);
        return resp;
    }

    @RequestMapping("/sendMessage")
    @ResponseBody
    public Object saveCompany(HttpServletRequest request,
                              @RequestParam(value="companyIds", required = true, defaultValue = "") String companyIds,
                              @RequestParam(value="pid", required = true, defaultValue = "10") String pid) {

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        if(StringUtil.isEmpty(companyIds) || StringUtil.isEmpty(pid)
                || user.getCompanyId().equals(companyIds)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        String companyName_key = PropertiesUtils.getValue("company_name");
        String companyName_value = request.getSession().getAttribute(companyName_key).toString();

        companySuperviseService.sendMessage(user, companyName_value, pid, companyIds);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    @RequestMapping("/deleteNode")
    @ResponseBody
    public Object deleteNode(HttpServletRequest request,
                              @RequestParam(value="id", required = true, defaultValue = "") String id,
                              @RequestParam(value="pid", required = true, defaultValue = "10") String pid) {

        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(pid)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        Map<String, Object> params = new HashedMap();
        params.put("companyId", user.getCompanyId());
        params.put("id", id);
        params.put("pid", pid);
        companySuperviseService.delete(params);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 上移
     * @param request
     * @param id
     * @param pid
     * @return
     */
    @RequestMapping("/upNode")
    @ResponseBody
    public Object upNode(HttpServletRequest request,
                              @RequestParam(value="id", required = true, defaultValue = "") String id,
                              @RequestParam(value="pid", required = true, defaultValue = "10") String pid) {

        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(pid)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        companySuperviseService.upNode(user.getCompanyId(), id, pid);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 下移
     * @param request
     * @param id
     * @param pid
     * @return
     */
    @RequestMapping("/downNode")
    @ResponseBody
    public Object downNode(HttpServletRequest request,
                              @RequestParam(value="id", required = true, defaultValue = "") String id,
                              @RequestParam(value="pid", required = true, defaultValue = "10") String pid) {

        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(pid)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        companySuperviseService.downNode(user.getCompanyId(), id, pid);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 升级
     * @param request
     * @param id
     * @param pid
     * @return
     */
    @RequestMapping("/upgrade")
    @ResponseBody
    public Object upgrade(HttpServletRequest request,
                         @RequestParam(value="id", required = true, defaultValue = "") String id,
                         @RequestParam(value="pid", required = true, defaultValue = "10") String pid,
                          @RequestParam(value="orderNum", required = true, defaultValue = "0") Integer orderNum) {

        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(pid)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        companySuperviseService.upgrade(user.getCompanyId(), id, pid, orderNum);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 降级
     * @param request
     * @param id
     * @param pid
     * @return
     */
    @RequestMapping("/degrade")
    @ResponseBody
    public Object degrade(HttpServletRequest request,
                           @RequestParam(value="id", required = true, defaultValue = "") String id,
                           @RequestParam(value="pid", required = true, defaultValue = "10") String pid,
                          @RequestParam(value="orderNum", required = true, defaultValue = "0") Integer orderNum) {

        if(StringUtil.isEmpty(id) || StringUtil.isEmpty(pid)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }

        companySuperviseService.degrade(user.getCompanyId(), id, pid, orderNum);

        return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    @RequestMapping("/highcharts")
    @ResponseBody
    public Object highcharts(HttpServletRequest request,
                             @RequestParam(value="companyId", required = true, defaultValue = "") String companyId) {

        if(StringUtil.isEmpty(companyId)){
            return new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        Response<Object> res = new Response<>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        String json = companySuperviseService.highchartsgetJson(companyId);
        res.setResult(json);
        return res;
    }
}
