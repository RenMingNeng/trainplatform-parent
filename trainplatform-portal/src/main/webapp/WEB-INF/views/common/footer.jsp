<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<div id="tbox">
    <div>
        <shiro:hasRole name="company_admin">
            <a href="<%=basePath%>admin/message" target="_blank"><i class="fa fa-commenting"></i></a>
        </shiro:hasRole>
        <shiro:hasRole name="company_user">
            <a href="<%=basePath%>student/message" target="_blank"><i class="fa fa-commenting"></i></a>
        </shiro:hasRole>

        <%--<span class="index"></span>--%>
        <div>
            <p>我的消息</p>
            <span></span>
        </div>
    </div>

    <div>
        <a href="javascript:feed_back();" target="_blank" ><i class="fa fa-edit"></i></a>
        <div>
            意见反馈
            <span></span>
        </div>
    </div>

    <div>
        <a href="javascript:;"><i class="fa fa-qq"></i></a>
        <div style="width:140px;">
            <p style="text-align:left;">在线客服：</p>
            <p>QQ：3533813028</p>
            <p>QQ：1134686735</p>
            <span></span>
        </div>
    </div>
    <div>
        <a href="javascript:;"><i class="fa fa-phone"></i></a>
        <div style="width:140px;">
            <p style="text-align:left;">客服电话：</p>
            <p>400-027-0009转5</p>
            <span></span>
        </div>
    </div>
    <div>
        <a href="javascript:;"><i class="fa fa-qrcode"></i></a>
        <div class="shadow_1" style="width:120px;background: #fff;">
            <img src="<%=resourcePath%>static/global/images/app.png" alt="">
            <p style="color:#333;">下载APP</p>
            <span style="border-color:transparent transparent transparent #fff;"></span>
        </div>
    </div>

    <div id="gotop">
        <a href="javascript:;"><i class="fa fa-angle-up"></i></a>
        <div>
            返回顶部
            <span></span>
        </div>
    </div>
    <%--<a href="javascript:;">
        <div>
            <img src="<%=resourcePath%>static/global/images/QQ_icon.png" alt="">
        </div>
        <div style="font-size:14px;">
            在线客服
        </div>
    </a>
    <a id="jianyi" target="_blank" href="javascript:feed_back();" >
        <div>
            <img src="<%=resourcePath%>static/global/images/jianyi_icon.png" alt="">
        </div>
        <div style="font-size:20px;margin:10px 0;">
            建议
        </div>
        <div style="font-size:14px;">
            请在此提出您的宝贵意见建议
        </div>
    </a>

    <a id="gotop" href="javascript:void(0)">
        <div>
            <i class="fa fa-sort-asc"></i>
        </div>
        <div style="margin-bottom: 7px;">
            top
        </div>
        <div>
            返回顶部
        </div>
    </a>--%>
</div>
<div id="footer">
    <div class="area">
        <div class="clearfix t">
            <div class="l pull-left">
                <div class="logo"></div>
                <ul class="clearfix">
                    <li><a href="<%=basePath%>static/global/tlmp/intro.html#1" target="_blank">公司简介</a></li>
                    <li class="split">|</li>
                    <li><a href="<%=basePath%>static/global/tlmp/intro.html#2" target="_blank">招贤纳士</a></li>
                    <li class="split">|</li>
                    <li><a href="<%=basePath%>static/global/tlmp/intro.html#3" target="_blank">联系我们</a></li>
                </ul>
            </div>
            <div class="c pull-left">
                <%--<div class="time">周一至周五：8:30-18:00</div>--%>
                <div class="phone">400-027-0009转5</div>
                <div class="qq">3533813028 </div>
                <div class="email">3533813028@qq.com  </div>
            </div>
            <div class="r pull-right">
                <ul class="clearfix img">
                    <li>
                        <img src="<%=resourcePath%>static/global/images/v2/qc_4.png" alt="">
                        <p>水利安全</p>
                    </li>
                    <li>
                        <img src="<%=resourcePath%>static/global/images/v2/qc_3.png" alt="">
                        <p>建筑安全生产</p>
                    </li>
                    <li>
                        <img src="<%=resourcePath%>static/global/images/v2/qc_2.png" alt="">
                        <p>电力微安全</p>
                    </li>
                    <li>
                        <img src="<%=resourcePath%>static/global/images/v2/qc_1.png" alt="">
                        <p>博晟安全</p>
                    </li>
                </ul>
            </div>
        </div>
    </div>
    <div class="b">
        <div class="area">

            <p class="copyright">Copyright ©武汉博晟安全技术股份有限公司    中国·武汉 版权所有 All Rights Reserved. V<span id="version"></span></p>
        </div>
    </div>
</div>
<script>
    var name = "ver";
    var mycookie = document.cookie;
    var idx = mycookie.indexOf(name+'=');
    var value = null;
    if(idx == -1){
        value = null;
    }else {
        var start = mycookie.indexOf('=', idx) + 1;
        var end = mycookie.indexOf(';', start);
        if (end == -1) {
            end = mycookie.length;
        }
        value = unescape(mycookie.substring(start, end));
    }
    //写入版本号
    document.getElementById("version").innerHTML = value;

    function feed_back() {
        var url = encodeURI(window.location.href);
        var feed_back_url = "<%=basePath%>feedback?url=" + url;
        window.open(feed_back_url)
    }


</script>

