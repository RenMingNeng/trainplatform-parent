<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>博晟 | 公告</title>
    <jsp:include page="${path}/common/style" />
    <link rel="stylesheet" href="<%=resourcePath%>static/global/css/detail.css">
    <style>
        .article img{
            max-width: 90%;
            height: auto;
            text-align: left;
            float: left;
        }
    </style>
</head>
<body class=" bg_fa">
<div class="header_top">
    <div class="area">
        <div class="clearfix">
            <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" class="logo">
        </div>
    </div>
</div>
<div class="container">
    <div class="area">
        <div class="clearfix">
            <div class="pull-left w_890 pd_20 bg_f mg_t_20 mg_b_20">
                <div class="h">
                    <h2>${article.title}</h2>
                    <div class="clearfix">
                        <div class="pull-left">${article.createTime}</div>
                        <div class="pull-right">${article.author}</div>
                    </div>
                </div>
                <div class="b article">
                    ${article.content}
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/script" />
</body>
</html>
