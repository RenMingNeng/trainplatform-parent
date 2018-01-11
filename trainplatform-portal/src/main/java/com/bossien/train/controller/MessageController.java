package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.Message;
import com.bossien.train.domain.Page;
import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IMessageService;
import com.bossien.train.util.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("message")
public class MessageController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);
    @Autowired
    private IMessageService messageService;

    /**
     * 项目档案异步
     * @param request
     * @param pageSize
     * @param pageNum
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                       @RequestParam(value="messageStatus", required = false, defaultValue = "") String messageStatus,
                       @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                       @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum) throws ServiceException {
        Map<String, Object> params = new HashMap<String, Object>();
        Response<Object> resp = new Response<Object>();

        if(pageSize > 100) {
            throw new ServiceException(Code.BAD_REQUEST, "pageSize 参数过大");
        }

        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<Object>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        params.put("userId", user.getId());

        if(!StringUtil.isEmpty(messageStatus)){
            params.put("messageStatus", messageStatus);
        }

        Role role = this.getCurrRole(request);
        if(role.getRoleName().equals("company_admin")){
            params.put("companyId", user.getCompanyId());
        }
        Page<Message> page = messageService.queryForPagination(params, pageNum, pageSize, user);
        resp.setCode(Code.SUCCESS.getCode());
        resp.setResult(page);
        return resp;
    }

    /**
     * 项目档案异步
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/updateStatus")
    @ResponseBody
    public Object updateStatus(HttpServletRequest request,
                               @RequestParam(value="id", required = true, defaultValue = "") String id) throws ServiceException {
        User user = this.getCurrUser(request);
        if(null == user){
            return new Response<Object>(Code.USER_NOT_LOGIN.getCode(), Code.USER_NOT_LOGIN.getMessage());
        }
        Map<String, Object> params = new HashedMap();
        params.put("id", id);
        params.put("messageStatus", "2");
        messageService.update(params);
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }
}
