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
    <title>博晟 | 培训平台 错误页</title>
    <style>
        *{margin:0;padding:0;}
        html,body,.bg{height:100%;width:100%;position: relative;}
        .bg{background:url('<%=resourcePath%>/static/global/images/v2/error_bg.png') no-repeat center center;}
        .content{color:#fff;width:360px;height:360px;position: absolute;left:50%;top:20%;margin-left:-180px; text-align: center;}
        .content .content_text{margin:50px 0 30px;}
        .content p{line-height: 30px;}
        .content button{cursor: pointer;background: #ff603b;outline: none;border:none;width:110px;height:32px;border-radius: 2px;color:#fff;}
    </style>
    <script type="text/javascript">
        var appPath = "<%=path %>";
    </script>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/layer/skin/default/layer.css" type="text/css">
</head>
<body>


            <c:choose>
                <c:when  test="${errorCode == 50512}">
                <div class="bg" style="background-image: url('<%=resourcePath%>static/global/images/v2/leave_bg.png')">
                        <div class="content" style="color:#bddcff;margin-top: 100px;font-size: 24px;">
                        抱歉，您已是离职人员！！！<br><br>
                        无权限进入我们系统
                        </div>
                </div>
                </c:when>
                <c:otherwise>

            <div class="bg">
                    <div class="content" style="color:#bddcff;">
                        <img src="<%=resourcePath%>static/global/images/v2/error_icon.png" style="margin-left:-55px;" alt="">
                        <p class="content_text">
                            错误代码：${errorCode}<br>
                            错误描述：${errorMsg}<br>
                            如果该资源对你很重要，请联系客服 <br>
                        </p>
                        <p id="content-detail" style="display: none;color: black;text-align: left">
                            请求地址：${requUrl}<br>
                            请求参数：${requQueryStr}<br>
                            错误信息：${extroMsg}
                        </p>
                        <button style="background: #616161;" id="btn-detail"> 详细信息</button>
                        <button id="btn-goindex"> 返回首页</button>
                    </div>
            </div>
                </c:otherwise>
            </c:choose>

        </div>
    </div>
    <script>
        $(function(){
            $("#btn-detail").click(function () {
                layer.open({
                    title: '错误信息',
                    area: ['1000px', '600px'],
                    content: $("#content-detail")
                });
            });
            $("#btn-goindex").click(function () {
                window.location.href = appPath + "/logout";
            });
        })
    </script>
</body>
</html>
