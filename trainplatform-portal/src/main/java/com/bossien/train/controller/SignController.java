package com.bossien.train.controller;

import com.bossien.framework.annotation.NeedSign;
import com.bossien.train.domain.Page;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.ISignService;
import com.bossien.train.service.fuck.impl.IFuckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("sign")
public class SignController {

    @Value("${sign}")
    private String sign;

    @Autowired
    private ISignService signService;

    /**
     * 获取 sign 签名
     * @param request
     * @return
     * @throws ServiceException
     */
    @RequestMapping("/get")
    @ResponseBody
    public Object getSign(HttpServletRequest request) throws Exception{

        Response<Object> resp = new Response<Object>(Code.SUCCESS);

        // 请求参数
        Map<String, String> sortMap = getRequestParams(request);
        // sign本身不参与签名
        sortMap.remove(sign);

        resp.setResult(signService.getSign(sortMap));

        return resp;
    }

    /**
     * 测试 sign 签名
     * @param request
     * @return
     * @throws ServiceException
     */
    @NeedSign
    @RequestMapping("/needSign")
    @ResponseBody
    public Object needSign(HttpServletRequest request) throws ServiceException{

        Response<Object> resp = new Response<Object>(Code.SUCCESS);

        return resp;
    }

    /**
     * 获取所有请求参数
     *
     * @param request
     * @return
     */
    private static Map<String, String> getRequestParams(HttpServletRequest request) {

        Map<String, String> map = new TreeMap<String, String>();
        Enumeration paramNames = request.getParameterNames();
        if (paramNames != null) {
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length == 1) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        map.put(paramName, paramValue);
                    }
                }
            }
        }
        return map;
    }

}
