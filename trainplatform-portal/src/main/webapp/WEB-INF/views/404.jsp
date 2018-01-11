<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/24
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<html>
<head>
    <title>404</title>
    <style>
        *{margin:0;padding:0;}
        html,body,.bg{height:100%;width:100%;position: relative;}
        body{min-width:1200px;}
        .bg{background:url('<%=resourcePath%>/static/global/images/v2/error_bg.png') no-repeat center center;}
        .content{color:#fff;width:360px;height:360px;position: absolute;left:50%;top:10%;margin-left:-180px; text-align: center;}
        .content .error_code{font-size:170px;font-family: serif,'宋体';font-weight: bold;}
        .content .content_text{margin:50px 0 30px;}
        .content p{line-height: 30px;}
        .content button{cursor: pointer;background: #ff603b;outline: none;border:none;width:110px;height:32px;border-radius: 2px;color:#fff;}
    </style>
    <script type="text/javascript">
        var appPath = "<%=path %>";
    </script>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
</head>
<body>
    <div class="bg">
        <div class="content">
            <p>
                <img src="<%=resourcePath%>static/global/images/v2/404_bg.png" alt="">
            </p>
            <p class="content_text">
                似乎你所寻找的网页已移动或丢失了 <br>
                请不要担心，如果该资源对你很重要，请刷新页面 <br>
                火星不太安全，我可以免费送你回地球
            </p>
            <button id="btn-goindex"> 返回首页</button>
        </div>
    </div>
    <script>
        $(function(){
            $("#btn-goindex").click(function () {
                window.location.href = appPath;
            });
        })
    </script>
</body>
</html>
