<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8"/>
    <title>博晟 | 管理平台</title>
    <jsp:include page="${path}/common/style" />
</head>

<body class="main-bg">
<jsp:include page="${path}/common/menu" />
<div class="w1300 lk-main clearfix">
</div>

<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/student/student_index.js"></script>
<script type="text/javascript">
    var page = new page();
    studentIndex.init(page);
</script>
</body>
</html>

