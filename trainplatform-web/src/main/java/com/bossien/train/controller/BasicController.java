package com.bossien.train.controller;

import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by luocc on 2017/7/20.
 */
public class BasicController {
    private static final Logger logger = LoggerFactory.getLogger(BasicController.class);

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
        return (User)request.getSession().getAttribute("sessionUser");
    }

    /**
     * 获取当前用户角色
     * @param request
     * @return Role
     */
    protected Role getCurrRole(HttpServletRequest request){
        return (Role)request.getSession().getAttribute("sessionRole");
    }

    protected void addParam(HttpServletRequest request, String key, Object value){
        if(null == request) {
            return;
        }
        if(StringUtils.isEmpty(key)) {
            return;
        }
        request.setAttribute(key, value);
    }

    protected Map newMap(){
        return Maps.newConcurrentMap();
    }
}
