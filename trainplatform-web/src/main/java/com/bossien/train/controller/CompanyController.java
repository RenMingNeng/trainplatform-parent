package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ICompanyService;
import com.bossien.train.service.ICompanyTypeService;
import com.bossien.train.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/company")
public class CompanyController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(CompanyController.class);

    @Autowired
    private ICompanyService companyService;
    @Autowired
    private ICompanyTypeService companyTypeService;

    @RequestMapping("")
    public String index() {
        return "company/index";
    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(
                @RequestParam(value="intTypeId", required = false, defaultValue = "") String intTypeId,
                @RequestParam(value="search", required = false, defaultValue = "") String search,
                @RequestParam(value="varName", required = false, defaultValue = "") String varName,
                @RequestParam(value="pageSize", required = true, defaultValue = "10") Integer pageSize,
                @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum
        ) throws ServiceException {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("intTypeId", intTypeId);
        params.put("varName", varName);
        // 根据类型id获取所有子节点
        String varCTChiList = companyTypeService.getCTChiList(params);
        List<String> varCTList = Arrays.asList(varCTChiList.split(","));
        params.put("varCTList", varCTList);

        if(!StringUtils.isEmpty(search)) {
            params.put("search", ParamsUtil.joinLike(search));
        }
        Integer count = companyService.selectCount(params);

        Page<Map<String, Object>> page = new Page(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("endNum", page.getPageSize());
        List<Map<String, Object>> listMap = companyService.selectList(params);

        page.setDataList(listMap);
        resp.setResult(page);

        return resp;
    }

    @RequestMapping("/update_company_valid")
    @ResponseBody
    public Object update_company_valid(
            @RequestParam(value="chrIsValid", required = false, defaultValue = "") String chrIsValid,
            @RequestParam(value="companyId", required = false, defaultValue = "") String companyId) throws ServiceException {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("chrIsValid", chrIsValid);
        params.put("companyId", companyId);
        companyService.updateCompanyValid(params);

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

}
