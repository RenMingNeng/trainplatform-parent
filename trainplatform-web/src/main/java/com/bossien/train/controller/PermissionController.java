package com.bossien.train.controller;

import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(PermissionController.class);
    @Autowired
    private ICompanyPermissionService companyPermissionService;

    @RequestMapping("")
    public String index(
            HttpServletRequest request,
            @RequestParam(value="companys", required = false, defaultValue = "") String companys2
        ) {

        if(StringUtils.isEmpty(companys2)) {
            new ServiceException(Code.BAD_REQUEST, "参数companys2丢失");
        }
        List<String> list = Arrays.asList(companys2.split(","));
        if(null == list || list.isEmpty()) {
            new ServiceException(Code.BAD_REQUEST, "参数companys2丢失");
        }
        Map<String, String> companys = newMap();
        String[] arr;
        for(String str : list) {
            arr = str.split("@@");
            if(null == arr || arr.length !=2) {
                continue;
            }
            companys.put(arr[0], arr[1]);
        }
        addParam(request, "companys", companys);
        return "permission/index";
    }

    @ResponseBody
    @RequestMapping("/save")
    public Object save(HttpServletRequest request,
            @RequestParam(value="companyIds", required = false, defaultValue = "") String companysIds,
            @RequestParam(value="permissionIds", required = false, defaultValue = "") String permissionIds) {

        if(StringUtils.isEmpty(companysIds)) {
            new ServiceException(Code.BAD_REQUEST, "参数companysIds丢失");
        }
        if(StringUtils.isEmpty(permissionIds)) {
            new ServiceException(Code.BAD_REQUEST, "参数permissionIds丢失");
        }
        List<String> companys = Arrays.asList(companysIds.split(","));
        if(null == companys || companys.isEmpty()) {
            new ServiceException(Code.BAD_REQUEST, "参数companysIds丢失");
        }

        companyPermissionService.insert(companys, permissionIds);
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }
}
