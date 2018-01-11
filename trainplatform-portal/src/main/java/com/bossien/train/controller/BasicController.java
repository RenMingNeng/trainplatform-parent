package com.bossien.train.controller;

import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.domain.eum.ProjectTypeEnum;
import com.bossien.train.model.view.Response;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.StringUtil;
import com.bossien.train.util.TokenUtil;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * Created by luocc on 2017/7/20.
 */
public class BasicController {

    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);

    private static final String session_user = PropertiesUtils.getValue("session_user");
    private static final String session_role = PropertiesUtils.getValue("session_role");
    private static final String login_time = PropertiesUtils.getValue("login_time");
    private static final String user_id = PropertiesUtils.getValue("user_id");
    private static final String company_id = PropertiesUtils.getValue("company_id");


    /**
     * 请求成功返回数据
     * @return
     */
    protected Map<String, Object> success(){
        return success(null);
    }

    /**
     * 请求成功返回数据
     * @param obj
     * @return
     */
    protected Map<String, Object> success(Object obj){
        Map<String, Object> params = Maps.newHashMap();
        params.put("code", 0);
        params.put("success", true);
        params.put("data", obj);
        return params;
    }

    /**
     * 请求失败返回数据
     * @param msg
     * @return
     */
    protected Map<String, Object> fail(Object msg){
        Map<String, Object> params = Maps.newHashMap();
        params.put("code", 1);
        params.put("success", false);
        params.put("msg", msg);
        return params;
    }

    /**
     * 获取当前登陆用户
     * @param request
     * @return User
     */
    protected User getCurrUser(HttpServletRequest request){
        return (User)request.getSession().getAttribute(session_user);
    }

    /**
     * 变更用户单位需重写session
     * @param request
     * @return User
     */
    protected void setCurrUser(HttpServletRequest request,Map<String ,Object> param){
        User user = (User)param.get(session_user);

        request.getSession().setAttribute(session_user,user);
    }

    /**
     * 获取当前用户角色
     * @param request
     * @return Role
     */
    protected Role getCurrRole(HttpServletRequest request){
        return (Role)request.getSession().getAttribute(session_role);
    }

    /**
     * 注销
     */
    protected void logout(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        session.removeAttribute(session_user);
        session.removeAttribute(session_role);
//        session.invalidate();

    }

    /**
     * 登陆
     * @param request
     * @param user
     * @param role
     */
    protected void login(HttpServletRequest request, HttpServletResponse response, User user, Role role) {

        // 存储 session
        HttpSession session = request.getSession();
        session.setAttribute(session_user, user);
        session.setAttribute(session_role, role);

        // 存储 user_id
        CookieGenerator cookieGenerator = new CookieGenerator();
        cookieGenerator.setCookiePath("/");
        cookieGenerator.setCookieName(user_id);
        cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
        cookieGenerator.addCookie(response, user.getId());

        // 存储 company_id
        cookieGenerator.setCookiePath("/");
        cookieGenerator.setCookieName(company_id);
        cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
        cookieGenerator.addCookie(response, user.getCompanyId());

        // 存储 login_time
        cookieGenerator.setCookiePath("/");
        cookieGenerator.setCookieName(login_time);
        cookieGenerator.setCookieMaxAge(Integer.MAX_VALUE);
        cookieGenerator.addCookie(response, System.currentTimeMillis() + "");
    }

    /**
     * 是否有进入答题功能
     * @param projectType
     * @return
     */
    protected boolean hasPermissionQuestion(String projectType){
        //单独培训的项目类型编号
       String containExerciseType= PropertiesUtils.getValue("contain_exercise_type");
       if(StringUtils.contains(containExerciseType,projectType)){
           return true;
       }
       return false;
    }

    /***
     * 是否有学习的功能
     * @param projectType
     * @return
     */
    protected boolean hasPermissionStudy(String projectType){
        //含有培训的
        String containTrainType=PropertiesUtils.getValue("contain_train_type");
        if(StringUtils.contains(containTrainType,projectType)){
            return true;
        }
        return false;
    }
    /***
     * 是否含有考试
     * @param projectType
     * @return
     */
    protected boolean hasPermissionExam(String projectType){
        //含有考试的
        String containExamType=PropertiesUtils.getValue("contain_exam_type");
        if(StringUtils.contains(containExamType,projectType)){
            return true;
        }
        return false;
    }

    protected String getWebPath(HttpServletRequest request) {
        return
                request.getScheme() + "://" +
                request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath() + "/";
    }

    protected void writeMessageUft8(HttpServletResponse response, Response<Object> resp) throws Exception{
        try{
            Gson gson = new Gson();
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(gson.toJson(resp));
        }finally{
            response.getWriter().close();
        }
    }

    protected void writeStrUft8(HttpServletResponse response, String str) throws Exception{
        try{
            Gson gson = new Gson();
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().print(str);
        }finally{
            response.getWriter().close();
        }
    }
}
