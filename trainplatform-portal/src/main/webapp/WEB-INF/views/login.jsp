<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="com.bossien.train.util.PropertiesUtils" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
    String version = PropertiesUtils.getValue("version");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <base href="<%=basePath%>">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="renderer" content="webkit">
    <title>博晟 | 培训平台-登录</title>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/layer/skin/default/layer.css">
    <link rel="stylesheet" href="<%=resourcePath%>static/global/css/login.css">
</head>
<body>
<div class="header">
    <div class="area clear">
        <div class="pull-left"><img src="<%=resourcePath%>static/global/images/login/icon_home.png"/>	您好，欢迎您来到博晟在线培训云平台！</div>
        <div class="pull-right">
            <ul>
                <li><img src="<%=resourcePath%>static/global/images/login/phone.png"/> 服务热线：<b>400-027-0009转5</b></li>
                <li><a href="javascript:;"><img src="<%=resourcePath%>static/global/images/login/wenhao.png"/> 帮助中心</a></li>
                <li class="icon_block"><img src="<%=resourcePath%>static/global/images/login/edit.png"/>&nbsp;</li>
                <li class="icon_block"><img src="<%=resourcePath%>static/global/images/login/user.png"/>&nbsp;</li>
            </ul>

        </div>
    </div>
</div>
<div class="logo">
    <div class="area" style="padding:0;">
        <div class="clear">
            <div class="pull-left">
                <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" style="margin:20px 0;">
            </div>
            <div class="pull-right">
                <dl id="article-dl">
                    <dt></dt>
                </dl>
            </div>
        </div>
    </div>
</div>
<!-- main -->
<div class="main">
    <div class="area">
        <div class="clear">
            <div class="pull-left">
                <img src="<%=resourcePath%>static/global/images/login/login_bg.png"/>
            </div>
            <div class="pull-right right" style="height:420px;">
                <h2>登 录</h2>
                <div class="line"></div>
                <div class="tabs">
                    <ul class="clear">
                        <li userType="5b9e9b3e-9ca5-4dfd-91f0-de584d439121" class="active" >学员</li>
                        <li userType="d685f6c9-8a25-4fc1-9dbd-8b684ec70e3a" >培训管理员</li>
                        <li userType="ffefa04c-d274-4694-ba49-904f790c234b" >培训监管员</li>
                    </ul>
                </div>
                <ol style="padding-bottom:20px;bottom:0;">
                    <h4 style="color: #555">建议使用最新版本360或者chrome浏览器访问应用以避免不必要的浏览器兼容问题</h4>
                </ol>
                    <p class="user-error msg"></p>
                    <div class="input">
                        <span class="icon user_form"></span>
                        <input type="text" placeholder="请输入用户名"  id="userAccount" name="userAccount"/>
                    </div>
                    <div class="input inp">
                        <span class="icon user_password"></span>
                        <input type="password" placeholder="请输入密码"  id="userPasswd" name="userPasswd" />
                    </div>
                    <div class="input">
                        <div class="clear">
                            <label class="pull-left checkbox f_14" style="display: none">
                                <span></span>
                                <input type="checkbox" id="remeberMe" name="remeberMe"/>&nbsp;&nbsp;&nbsp;是否记住我
                            </label>
                            <a href="javascript:alert('请联系客服,帮助找回密码!');" class="pull-right f_14">忘记密码？</a>
                        </div>
                    </div>
                    <div class="input lh_50">
                        <div class="img-loading"></div>
                        <button class="lh_50 submit" onclick="login()" type="button">登 录</button>
                    </div>
                <div class="input last" style="display:none;">
                    <div class="clear">
                        <div class="pull-left">
                            还没有用户名？
                        </div>
                        <div class="pull-right">
                            <a href="javascript:;" class="register">立即注册</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!-- 底部 -->
<div class="footer">
    <ul class="clear">
        <li><a href="<%=basePath%>static/global/tlmp/intro.html#1" target="_blank">公司介绍</a></li>
        <li class="split">|</li>
        <li><a href="<%=basePath%>static/global/tlmp/intro.html#2" target="_blank">诚聘精英</a></li>
        <li class="split">|</li>
        <li><a href="<%=basePath%>static/global/tlmp/intro.html#3" target="_blank">联系我们</a></li>
    </ul>
    <p>Copyright ©武汉博晟安全技术股份有限公司 中国·武汉 版权所有 All Rights Reserved. V<span id="version"></span></p>
</div>

<div class="browser-tip">
    <div class="dialog_bg"></div>
    <div class="browser-tip-content">
        <div class="area">
            <div class="pull-left info">
                <div >抱歉, 系统检测到您的浏览器版本过低，可能导致网站不能正常访问。</div>
                <div>如果您使用的是<span style="color:red;">360浏览器</span>,推荐升级切换至【极速模式】。切换方式如图所示。</div>
            </div>
            <div class="pull-left">
                <a href="http://chrome.360.cn" target="_blank">下载360浏览器</a>
            </div>
        </div>
        <img src="<%=resourcePath%>static/global/images/tem.png" alt="">
    </div>
</div>

<!-- input hidden value -->
<input type="hidden" id="redirect" value="${redirect}">
<input type="hidden" id="userType" value="5b9e9b3e-9ca5-4dfd-91f0-de584d439121">
<script type="text/javascript">var appPath = "<%=path %>";var resPath = "<%=resourcePath %>";var basePath = "<%=basePath %>";</script>
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
<script src="<%=resourcePath%>static/global/js/sha256.min.js"></script>
<script src="<%=resourcePath%>static/global/js/check.browser.js"></script>
<script src="<%=resourcePath%>static/global/js/common/article.js"></script>
<script src="<%=resourcePath%>static/global/js/md5.js"></script>
<script src="<%=resourcePath%>static/global/js/login.js"></script>
<script type="text/javascript">
    $(function () {
        var name = "ver";
        var mycookie=document.cookie;
        var idx=mycookie.indexOf(name+'=');
        var value=null;
        if(idx==-1){
            value = null;
        }else {
            var start = mycookie.indexOf('=', idx) + 1;
            var end = mycookie.indexOf(';', start);
            if (end == -1) {
                end = mycookie.length;
            }
            value = unescape(mycookie.substring(start, end));
        }
        document.getElementById("version").innerHTML = value;
    })
</script>
</body>
</html>