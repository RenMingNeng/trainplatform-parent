<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>

<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<link rel="shortcut icon" href="<%=resourcePath%>favicon.ico" />
<link rel="stylesheet" href="<%=resourcePath%>static/global/js/icheck/square/green.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/js/layer/skin/default/layer.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/js/My97DatePicker/skin/WdatePicker.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/Font-Awesome-master/css/font-awesome.min.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/js/zTree/css/zTreeStyle/metro.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/js/msg/css/jquery.my-message.1.1.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/css/base.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/css/style.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/css/gotop.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>static/global/css/app.css" type="text/css">