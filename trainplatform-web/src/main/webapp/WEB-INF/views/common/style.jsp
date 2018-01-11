<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>

<meta charset="utf-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta content="" name="description"/>
<meta content="" name="author"/>
<link rel="stylesheet" href="<%=resourcePath%>assets/bootstrap-3.3.7-dist/css/bootstrap.min.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>assets/css/style-index.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>assets/js/zTree/css/zTreeStyle/metro.css" type="text/css">
<link rel="stylesheet" href="<%=resourcePath%>assets/My97DatePicker/skin/WdatePicker.css" type="text/css">