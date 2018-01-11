package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserExtra;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IUserExtraService;
import com.bossien.train.service.IVideoPositionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("userExtra")
public class UserExtraController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);
    // 操作mongodb的集合名,等同于mysql中的表
    private final String collectionName_user_extra = "user_extra";

    @Autowired
    private MongoOperations mongoTemplate;
    @Autowired
    private IUserExtraService userExtraService;

   /**
     * 添加数据
     * @param request
     * @param guide_mark
     */
    @RequestMapping("/insert")
    public void insert(HttpServletRequest request,
            @RequestParam(value = "guide_mark", required = true, defaultValue = "") String guide_mark             //引导页标示
    ){
        User user = getCurrUser(request);
        UserExtra userExtra = new UserExtra(
              user.getId(),
                guide_mark,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        ) ;
        userExtraService.insert(userExtra);
    }

    /**
     * 修改
     * @param request
     * @param guide_mark
     */
    @RequestMapping("/update")
    @ResponseBody
    public void update(HttpServletRequest request,
                       @RequestParam(value = "guide_mark", required = true, defaultValue = "") String guide_mark             //引导页标示
    ){
        User user = getCurrUser(request);
        UserExtra userExtra = new UserExtra(
                user.getId(),
                guide_mark,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        ) ;

        //修改数据
        userExtraService.update(userExtra);
    }

}
