package com.bossien.train.controller;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.framework.mq.sender.QueueTJSender;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserExtra;
import com.bossien.train.domain.eum.GuideMarkEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.IUserExtraService;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.CookieGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;

@Controller
public class PageController extends BasicController {

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    @Value("${user_id}")
    private String user_id;
    @Value("${duration}")
    private String duration;
    @Value("${login_count}")
    private String login_count;
    @Autowired
    private QueueTJSender queueTJSender;
    @Autowired
    private IUserExtraService userExtraService;

    @RequestMapping("")
    public String login()
    {
        return "login";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/unauth")
    public String unauth(HttpServletRequest request, HttpServletResponse response) {
        if(RequestUtils.isAjax(request)) {
            try {
                writeMessageUft8(response, new Response<Object>(Code.AUTH_ERROR.getCode(), Code.AUTH_ERROR.getMessage()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        return "unauth";
    }

    @RequestMapping("/redirect")
    public String redirect(HttpServletRequest request,HttpServletResponse response) {
        User user = getCurrUser(request);
        /*//查询mongodb中的userExtra记录
        UserExtra userExtra = userExtraService.selectOne(user.getId());
        String guide_mark = GuideMarkEnum.GuideMark_2.getValue();

        //mongdb有数据表明不要显示引导页
        Boolean permission = true;
        if(userExtra != null && userExtra.getGuide_mark() != null){
            if((userExtra.getGuide_mark()).equals(guide_mark)){
                permission = false;
            }
        }

        request.getSession().setAttribute("guide_mark",permission);*/

        return "redirect";
    }

    @RequestMapping("/404")
    public String not_found() { return "404"; }

    @RequestMapping("/keeplive")
    @ResponseBody
    public Object keeplive() {
        return new Response<Object>(Code.SUCCESS.getCode(), "keeplive");
    }

    @RequestMapping("/login")
    public String login(
            ModelMap modelMap,HttpServletResponse response,
            @RequestParam(value="msg", required = false, defaultValue = "") String msg,
            @RequestParam(value="redirect", required = false, defaultValue = "") String redirect
        ) {

        modelMap.addAttribute("msg", msg);
        modelMap.addAttribute("redirect", redirect);
        return "login";
    }


    @RequestMapping("/admin")
    public String admin_index(
            ModelMap modelMap,
            HttpServletRequest request)throws Exception {

        User user = getCurrUser(request);
        modelMap.put("user_id", user.getId());
        modelMap.put("company_id", user.getCompanyId());

        return "admin/index";
    }

    @RequestMapping("/super")
    public String superAdmin_index(HttpServletRequest request) {
        return "super/index";
    }



    @RequestMapping("/common")
    public String common(HttpServletRequest request) {
        return "common";
    }








    /*公共的javascript文件、style文件、footer版块引入*/
    @RequestMapping("/common/script")
    public String common_script() {
        return "common/script";
    }
    @RequestMapping("/common/style")
    public String common_style() {
        return "common/style";
    }
    @RequestMapping("/common/footer")
    public String common_footer() {
        return "common/footer";
    }

    /*前端模板*/
    @RequestMapping("/template/create_project")
    public String template_create_project() { return "template/create_project"; }
    /*@RequestMapping("/template/student_info")
    public String template_student_info() { return "template/student_info"; }*/
    @RequestMapping("/template/student_enter")
    public String template_student_enter() { return "template/student_enter"; }
    @RequestMapping("/template/student_study")
    public String template_student_study() { return "template/student_study"; }
    @RequestMapping("/template/student_exercise")
    public String template_student_exercise() { return "template/student_exercise"; }
    @RequestMapping("/template/error_feedback")
    public String template_error_feedback() {
        return "template/error_feedback";
    }
    @RequestMapping("/template/list_detail")
    public String template_list_detail() {
        return "template/list_detail";
    }
    @RequestMapping("/template/enter_after")
    public String template_enter_after() {
        return "template/enter_after";
    }
    @RequestMapping("/template/user_info")
    public String template_user_info() {
        return "template/user_info";
    }
    @RequestMapping("/template/my_message")
    public String template_my_message() {
        return "template/my_message";
    }

}
