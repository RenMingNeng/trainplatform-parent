package com.bossien.train.controller;

import com.bossien.train.domain.Role;
import com.bossien.train.domain.User;
import com.bossien.train.domain.UserExtra;
import com.bossien.train.domain.UserRole;
import com.bossien.train.domain.eum.GuideMarkEnum;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.IncorrectPasswordException;
import com.bossien.train.exception.ServiceException;
import com.bossien.train.exception.SsoException;
import com.bossien.train.model.view.Response;
import com.bossien.train.service.ISequenceService;
import com.bossien.train.service.IUserExtraService;
import com.bossien.train.service.IUserRoleService;
import com.bossien.train.service.IUserService;
import com.bossien.train.shiro.CustomizedToken;
import com.bossien.train.sso.SsoUser;
import com.bossien.train.util.MD5Utils;
import com.bossien.train.util.PropertiesUtils;
import com.bossien.train.util.RequestUtils;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@RequestMapping("sso")
public class SsoController extends BasicController{

    public static final Logger logger = LoggerFactory.getLogger(SsoController.class);


    @Value("${app_key}")
    private String app_key;
    @Value("${session_user}")
    private String session_user;
    @Value("${sso_user}")
    private String sso_user;

    @Autowired
    private Role company_user;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserRoleService userRoleService;
    @Autowired
    private ISequenceService sequenceService;
    @Autowired
    private IUserExtraService userExtraService;

    @RequestMapping("/redirect")
    public String redirect(
            HttpServletRequest request
    ) {

        User user = (User) request.getSession().getAttribute(session_user);

        //mongdb取出引导页标示比对
        Boolean permission = this.guidePermission(user.getId());

        RequestUtils.addParam(request, "guide_mark", permission);
        return "sso/redirect";
    }

    @RequestMapping("/login")
    public String login(
            HttpServletRequest request,
            @RequestParam(value="ticket", required = true, defaultValue = "") String ticket
    ) {

        SsoUser ssoUser = (SsoUser) request.getSession().getAttribute(sso_user);
        User user = (User) RequestUtils.getParam(request, "user_by_open_id");
        // 对比ssoUser和user 做用户信息的更新
        if(null != ssoUser && null != user) {
            String ssoUserName = ssoUser.getName();
            String userName = user.getUserName();
            if(StringUtils.isNotEmpty(ssoUserName) && !StringUtils.equals(ssoUserName, userName)) {
                // 更新user_name 和 mobile_no
                userService.updateUserBySso(user.getId(), ssoUserName, ssoUser.getPassword(), ssoUser.getMobileNo(), this.convertSsoSex(ssoUser.getSex()));
                // 临时更新密码方便下方登陆
                user.setUserPasswd(ssoUser.getPassword());
            }
        }

        // 绑定成功 调用shiro登陆
        Subject subject = SecurityUtils.getSubject();

        CustomizedToken token = new CustomizedToken(
                user.getUserAccount(),
                user.getUserPasswd(),
                false,
                request.getRemoteAddr(),
                company_user.getId(),
                true
        );
        subject.login(token);

        return "redirect:/sso/redirect";
    }

    /**
     * 引导页的标示比对，用于辨别是否进入引导页
     * @param userId
     * @return
     */
    private Boolean guidePermission(String userId) {

        //查询mongodb中的userExtra记录
        UserExtra userExtra = userExtraService.selectOne(userId);
        String guide_mark = GuideMarkEnum.GuideMark_2.getValue();

        //mongdb有数据表明不要显示引导页
        Boolean permission = true;
        if(userExtra != null && userExtra.getGuide_mark() != null){
            if((userExtra.getGuide_mark()).equals(guide_mark)){
                permission = false;
            }
        }
        return permission;
    }


//    @ResponseBody
//    @RequestMapping("/signin")
//    public Object signin(
//            HttpServletRequest request,
//            @RequestParam(value="ticket", required = true, defaultValue = "") String ticket
//    ) {
//        // 解密ticket信息
//        String openId = AESUtils.decrypt(ticket, app_key);
//        if (StringUtils.isEmpty(openId)) {
//            throw new SsoException(Code.SSO_ERROR_C000003); // 数据解密异常 errorCode: C000003
//        }
//        // 检查统一用户和子系统用户是否有关联
//        User openIdUser = userService.selectByOpenId(openId);
//        if(null == openIdUser) {
//            throw new SsoException(Code.SSO_ERROR_C000004); // 获取用户信息异常 errorCode: C000004
//        }
//        // 绑定成功 调用shiro登陆
//        Subject subject = SecurityUtils.getSubject();
//        UsernamePasswordUsertypeToken token = new UsernamePasswordUsertypeToken(
//                openIdUser.getUserAccount(),
//                openIdUser.getUserPasswd(),
//                false,
//                request.getRemoteAddr(),
//                company_user.getId()
//        );
//        subject.login(token);
//        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
//    }

    @RequestMapping(value = "/bind")
    public ModelAndView bind() { return new ModelAndView("sso/bind"); }

    // 子账户与统一账户绑定
    @ResponseBody
    @RequestMapping("/bind/one")
    public Object bind_one(
            HttpServletRequest request,
            @RequestParam(value="user_account", required = true, defaultValue = "") String user_account,
            @RequestParam(value="user_passwd", required = true, defaultValue = "") String user_passwd,
            @RequestParam(value="open_id", required = true, defaultValue = "") String open_id
    ) throws ServiceException {

        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("user_account", user_account);
        // 账户是否存在
        User user = userService.selectOne(params);
        if(null == user) {
            throw new UnknownAccountException();        // 未找到账号
        }
        // 密码是否正确
        if(!user_passwd.equals(user.getUserPasswd())) {
            throw new IncorrectPasswordException();     // 密码错误
        }
        // 账户是否被绑定open_id
        if(StringUtils.isNotEmpty(user.getOpenId())) {
            throw new SsoException(Code.SSO_ERROR_ALREADY_BIND_OPEN_ID);        // 已绑定openid
        }
        // 账户是否锁定
        if(!User.IsValid.TYPE_1.getValue().equals(user.getIsValid())) {
            throw new LockedAccountException();         // 账号被锁定
        }
        // 密码是否正确
        if(!user_passwd.equals(user.getUserPasswd())) {
            throw new IncorrectPasswordException();     // 密码错误
        }

        // open_id 绑定
        userService.updateOpenId(user.getId(), open_id);

        // resp
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    // 一键绑定统一账号 (前端先排除统一账户已经存在于子系统的情况)
    @RequestMapping("/bind/two")
    @ResponseBody
    public Object bind_two(
            @RequestParam(value="user_account", required = true, defaultValue = "") String user_account,
            @RequestParam(value="user_passwd", required = true, defaultValue = "") String user_passwd,
            @RequestParam(value="user_name", required = true, defaultValue = "") String user_name,
            @RequestParam(value="mobile_no", required = true, defaultValue = "") String mobile_no,
            @RequestParam(value="sex", required = true, defaultValue = "") String sex,
            @RequestParam(value="id_type", required = true, defaultValue = "") String id_type,
            @RequestParam(value="id_number", required = true, defaultValue = "") String id_number,
            @RequestParam(value="email", required = true, defaultValue = "") String email,
            @RequestParam(value="open_id", required = true, defaultValue = "") String open_id
    ) throws Exception{

        // 在统一登录后完善信息页面(密码、账户名、邮箱等) 如果跳过此步骤,子系统需要自己填充账户和密码
        if(StringUtils.isEmpty(user_name)) {
            user_name = user_account;
        }
        if(StringUtils.isEmpty(user_passwd)) {
            user_passwd = MD5Utils.getMD5("123456").toLowerCase();
        }

        String current = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // 构造user
        User user = new User(
                sequenceService.generator(),
                user_account,
                user_passwd,
                user_name,
                this.convertSsoSex(sex),
                User.IdType.TYPE_1.getValue(),
                id_number,
                User.Supporter.TYPE_1.getValue(),
                User.UserType.TYPE_3.getValue(),
                mobile_no,
                "-1",
                "",
                "",
                User.RegistType.TYPE_1.getValue(),
                current,
                "",
                User.IsValid.TYPE_1.getValue(),
                open_id,
                "sso",
                current,
                "sso",
                current
        );

        // 构造userRole
        UserRole userRole = new UserRole(
                sequenceService.generator(),
                user.getId(),
                company_user.getId(),
                "sso",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                "sso",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // 自动创建用户,角色默认学员
        userService.insert(user);
        userRoleService.insert(userRole);
        // resp
        return new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
    }

    /**
     * 此方法用于转换统一登录的性别到培训平台的性别
     * 统一登录 性别 0：女 1：男
     * 培训平台 性别 1：女 2：男
     * @param sex
     */
    private String convertSsoSex(String sex) {
        if(org.springframework.util.StringUtils.isEmpty(sex)) {
            // 默认为男性
            return User.Sex.TYPE_2.getValue();
        }
        if("0".equals(sex)) {
            return User.Sex.TYPE_1.getValue();
        }
        if("1".equals(sex)) {
            return User.Sex.TYPE_2.getValue();
        }
        // 默认为男性
        return User.Sex.TYPE_2.getValue();
    }

    // 进行快速绑定前先去检测该账户是否在子系统已存在
    @RequestMapping("/bind/two/check")
    @ResponseBody
    public Object bind_two_check(
            @RequestParam(value="user_account", required = true, defaultValue = "") String user_account
    ) throws Exception{

        Response response = new Response<Object>(Code.SUCCESS.getCode(), Code.SUCCESS.getMessage());
        Map<String, Object> params = new ConcurrentHashMap<String, Object>();
        params.put("user_account", user_account);
        // 账户是否存在
        User user = userService.selectOne(params);
        if(null != user) {
            response.setResult(true);
        } else {
            response.setResult(false);
        }
        // resp
        return response;
    }

    // 统一注册之后的客户端应答接收地址
    @RequestMapping(value = "/register")
    public void register(
            HttpServletResponse response,
            SsoUser ssoUser) throws Exception {

        logger.debug("sso register callback ssoUser = " + new Gson().toJson(ssoUser));

        try {
            super.writeStrUft8(response, "{\"code\":\"000000\",\"msg\":\"操作成功\"}");
        } catch (Exception e) {
            e.printStackTrace();
            super.writeStrUft8(response, "{\"code\":\"111111\",\"msg\":\"操作失败\"}");
        }
    }

    // 统一退出之后的客户端回调接收地址
    @RequestMapping(value = "/logout")
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Subject subject = SecurityUtils.getSubject();
        // 退出
        try{
            subject.logout(); // 调用logout 从session中删除内容 下一次访问的时候就必须重新登录了
        } catch (SessionException ex) {
            logger.error("sso logout error", ex);
        }
    }



}
