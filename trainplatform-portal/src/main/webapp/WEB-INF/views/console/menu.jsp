<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<script type="text/javascript">var appPath = "<%=path %>";var resPath = "<%=resourcePath %>";var basePath = "<%=basePath %>";</script>
<div class="header_top">
    <div class="area">
        <div class="clearfix">
            <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" class="logo">
        </div>
    </div>
</div>

<div class="nav">
    <div class="area clearfix">
        <ul class="clearfix">
            <li><a href="<%=resourcePath%>console" class="first <c:if test="${param.menu=='index'}">active</c:if>">首页</a></li>
            <li><a href="<%=resourcePath%>/druid/index.html" target="_blank">数据库监控</a></li>
        </ul>
    </div>
</div>
