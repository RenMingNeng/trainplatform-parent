package com.bossien.train.controller;

import com.bossien.train.domain.User;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-08-03.
 */
@Controller
@RequestMapping("companyType")
public class CompanyTypeController extends BasicController{

    @Autowired
    private ICompanyTypeService companyTypeService;

    /**
     * 加载单位树
     * @param
     * @return
     */
    @RequestMapping(value = "tree")
    @ResponseBody
    public Object companyType_tree(HttpServletRequest request){
        Response<Object> response=new Response <Object>();
        User user = getCurrUser(request);
        List<Map <String, Object>> companyTypeZTree = companyTypeService.treeNodes(user);
        //转成 数组
        Object[] companyTypeZTrees = companyTypeZTree.toArray();
        response.setResult(companyTypeZTrees);
        return response;
    }
}
