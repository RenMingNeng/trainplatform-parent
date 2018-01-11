package com.bossien.train.controller;

import com.bossien.train.domain.Page;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IUserService;
import com.bossien.train.util.ParamsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private IUserService userService;

    @RequestMapping("")
    public String index(
            HttpServletRequest request,
            @RequestParam(value="userType", required = false, defaultValue = "") String userType
        ) {

        addParam(request, "userType", userType);
        return "user/index";

    }

    @RequestMapping("/list")
    @ResponseBody
    public Object list(HttpServletRequest request,
                    @RequestParam(value="pageSize", required = true, defaultValue = "20") Integer pageSize,
                    @RequestParam(value="pageNum", required = true, defaultValue = "1") Integer pageNum,
                    @RequestParam(value="search", required = false, defaultValue = "") String search,
                    @RequestParam(value="userType", required = false, defaultValue = "") String userType
        ) throws ServiceException {

        Response<Object> resp = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());

        Map<String, Object> params = newMap();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);
        if(!StringUtils.isEmpty(search)) {
            params.put("search", ParamsUtil.joinLike(search));
        }
        params.put("userType", userType);
        Integer count = userService.selectCount(params);

        Page<User> page = new Page<User>(count, pageNum, pageSize);
        params.put("startNum", page.getStartNum());
        params.put("pageSize", page.getPageSize());
        List<User> list = userService.selectList(params);

        page.setDataList(list);
        resp.setResult(page);

        return resp;
    }

    @RequestMapping("/update_valid")
    @ResponseBody
    public Object update_company_valid(
            @RequestParam(value="isValid", required = false, defaultValue = "") String isValid,
            @RequestParam(value="userId", required = false, defaultValue = "") String userId) throws ServiceException {

        if(!StringUtils.isEmpty(isValid) && !StringUtils.isEmpty(userId)) {
            userService.updateUserValid(isValid, userId);
        }

        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

}
