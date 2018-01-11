package com.bossien.framework.filter;

import com.bossien.framework.interceptor.SignInterceptor;
import com.bossien.train.domain.User;
import com.bossien.train.exception.Code;
import com.bossien.train.exception.SsoException;
import com.bossien.train.listener.SystemContextLoaderListener;
import com.bossien.train.service.IUserService;
import com.bossien.train.service.impl.UserServiceImpl;
import com.bossien.train.sso.SsoUser;
import com.bossien.train.util.*;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

public class SsoAuthFilter implements Filter {

    private Set<String> prefixIignores = Sets.newConcurrentHashSet();

    public static final Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private static final String app_key = PropertiesUtils.getValue("app_key");
    private static final String app_code = PropertiesUtils.getValue("app_code");
    private static final String allow_urls = PropertiesUtils.getValue("allow_urls");
    private static final String sso_switch = PropertiesUtils.getValue("sso_switch");
    private static final String sso_user = PropertiesUtils.getValue("sso_user");
    private static final String session_user = PropertiesUtils.getValue("session_user");
    private static final String sso_version = PropertiesUtils.getValue("sso_version");
    private static final String sso_login_url = PropertiesUtils.getValue("sso_login_url");
    private static final String sso_ticket_url = PropertiesUtils.getValue("sso_ticket_url");

//    @Autowired SystemContextLoaderListener辅助解决filter中无法注入service的bug
    private IUserService userService;

    // 初始化工作
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String[] allows = allow_urls.split(",");
        for (String url : allows) {
            prefixIignores.add(url);
        }

        this.userService = (UserServiceImpl) SystemContextLoaderListener.getBean("userService");
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
    }

    // 检测是否开启sso
    private boolean isSupportSso(String ssoSwitch) {

        return "1".equals(ssoSwitch) || "true".equalsIgnoreCase(ssoSwitch) || "on".equalsIgnoreCase(ssoSwitch);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 未开启sso
        if(!isSupportSso(sso_switch)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 允许路径直接放行
        if(canIgnore(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        // 获取当前会话信息
        Session session = SecurityUtils.getSubject().getSession();
        User user = (User) session.getAttribute(session_user);
        if (null != user) {
            String url = request.getRequestURI();
            if(url.indexOf("/sso/login") == -1){
                filterChain.doFilter(request, response);
                return;
            }
        }
        // 获取ticket信息
        String ticket = request.getParameter("ticket");
        if (StringUtils.isEmpty(ticket)) {
            this.sendLogin(request, response);
            return;
        }
        // 根据ticket获取用户信息
        Map<String,String> ticketMap = this.getUserByTicket(ticket);
        if(null == ticketMap){
            throw new SsoException(Code.SSO_ERROR_C000001); // sso系统异常 errorCode: C000001
        }
        // 判断响应结果
        String code = ticketMap.get("code");
        // ticket过期或者无效, 重新调用平台退出
        if("C000004".equalsIgnoreCase(code)){
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            response.sendRedirect(basePath + "logout");
            return;
        }
        if(!"000000".equals(code)){
            throw new SsoException(Code.SSO_ERROR_C000004); // 获取用户信息异常 errorCode: C000004
        }
        // 验证签名
        boolean verifySign = SignUtils.verifySign(ticketMap, app_key);
        if(!verifySign){
            throw new SsoException(Code.SSO_ERROR_C000002); // sign verify 失败 errorCode: C000002
        }
        // 解密用户数据
        String jsonData = AESUtils.decrypt(ticketMap.get("data"), app_key);
        SsoUser ssoUser = null;
        try {
            ssoUser = JsonUtils.toObject(jsonData, SsoUser.class);
        } catch (Exception e) {
            throw new SsoException(Code.SSO_ERROR_C000004); // 获取用户信息异常 errorCode: C000004
        }
        // 检查统一用户和子系统用户是否有关联
        User openIdUser = userService.selectByOpenId(ssoUser.getOpenId());
        if (null == openIdUser) {
            // 子系统拿到用户中心返回的用户信息，具体怎么处理，自己发挥，以下处理方式只是建议
            doBinding(request, response, ssoUser);
            return;
        }
        // 统一账户存在子账户, 直接采用子账户登陆
        RequestUtils.addParam(request, "user_by_open_id", openIdUser);
        session.setAttribute(sso_user, ssoUser);
        filterChain.doFilter(request, response);
        return;
    }

    private boolean canIgnore(HttpServletRequest request) {
        String url = request.getRequestURI();
        if(url.indexOf("/sso/login") != -1) {
            return false;
        }
        for(String ignore : prefixIignores) {
            if(url.indexOf(ignore) != -1) {
                return true;
            }
        }
        return false;
    }


    /**
     * 发送SSO登录请求
     *
     * @param request
     * @param response
     * @throws IOException
     */
    private void sendLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

        //  封装请求参数
        Map<String,String> params = Maps.newTreeMap();
        params.put("version", sso_version);
        params.put("appCode", app_code);
        params.put("returnUrl", /*URLEncoder.encode(*/basePath+"sso/login"/*, "UTF-8")*/);
        // SSO登录跳转URL
        StringBuilder redirectUrl = new StringBuilder(sso_login_url).append("?");
        String key = "";
        String value = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            redirectUrl.append(key).append("=").append(null == value ? "" : value).append("&");
        }
        // 计算签名
        String signValue = SignUtils.getSignValue(params, app_key);
        redirectUrl.append("signValue").append("=").append(signValue);
        response.sendRedirect(redirectUrl.toString());
    }

    /**
     * 根据ticket获取用户信息
     *
     * @param ticket
     * @throws UnsupportedEncodingException
     */
    private Map<String,String> getUserByTicket(String ticket) throws UnsupportedEncodingException {

        // 封装请求参数
        Map<String,String> params = Maps.newTreeMap();
        params.put("version", sso_version);
        params.put("appCode", app_code);
        params.put("ticket", ticket);
        // ticket URL
        StringBuilder ticketUrl = new StringBuilder(sso_ticket_url).append("?");
        String key = "";
        String value = "";
        for (Map.Entry<String, String> entry : params.entrySet()) {
            key = entry.getKey();
            value = entry.getValue();
            ticketUrl.append(key).append("=").append(null == value ? "" : value).append("&");
        }
        // 计算签名
        String signValue = SignUtils.getSignValue(params, app_key);
        ticketUrl.append("signValue").append("=").append(signValue);
        // 发送请求
        String respBody = HttpClientUtils.doGet(ticketUrl.toString());
        Map<String,String> result = null;
        try {
            result = JsonUtils.toObject(respBody, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new SsoException(Code.SSO_ERROR_TICKET_JSON_CONVERT); // 用户信息转换异常
        }
        return result;

    }

    /**
     * 建立统一账号和子系统账号关系（通过openId关联）,两钟方式：
     * 1、有子系统账号，绑定原子系统账号（需提供绑定页面）；
     * 2、没有子系统账号，后台默认初始化一个子系统账号和统一账号关联（这种方式需要考虑用户权限问题）。
     *
     * @param request
     * @param response
     * @param ssoUser
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private void doBinding(HttpServletRequest request, HttpServletResponse response, SsoUser ssoUser) throws ServletException, IOException {

        // 外部系统唯一授权码（UUID生成，唯一）
        request.setAttribute("sso_user_open_id", ssoUser.getOpenId());
        // 账号
        request.setAttribute("sso_user_user_account", ssoUser.getAccount());
        // 密码
        request.setAttribute("sso_user_user_passwd", ssoUser.getPassword());
        // 用户姓名
        request.setAttribute("sso_user_user_name", ssoUser.getName());
        // 手机号
        request.setAttribute("sso_user_mobile_no", ssoUser.getMobileNo());
        // 用户性别： 0：女 1：男
        request.setAttribute("sso_user_sex", ssoUser.getSex());
        // 证件类型: 0: 身份证 1: 居住证 2: 签证 3: 护照 4: 军人证 5: 港澳通行证 6: 台胞证
        request.setAttribute("sso_user_id_type", ssoUser.getIdType());
        // 证件号
        request.setAttribute("sso_user_id_number", ssoUser.getIdNumber());
        // 邮箱
        request.setAttribute("sso_user_email", ssoUser.getEmail());
        // service
        request.setAttribute("sso_user_service", request.getRequestURL());

        request.getRequestDispatcher("/sso/bind").forward(request, response);
    }

}
