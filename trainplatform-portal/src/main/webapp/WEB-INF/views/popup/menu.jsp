<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<script type="text/javascript">var appPath = "<%=path %>";var resPath = "<%=resourcePath %>";var basePath = "<%=basePath %>";</script>
<div class="header_top">
    <div class="area">
        <div class="clearfix" >
            <a href="<%=basePath%>student">
                <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" class="logo">
            </a>
            <div class="clearfix pull-right">
            </div>
        </div>
    </div>
</div>
<div class="nav">
    <div class="area clearfix">
        <ul class="clearfix pull-left">
            <li><a href="<%=basePath%>popup/studentModify?id=${sessionScope.bossien_user.id}" class="<c:if test="${param.menu=='stuinfo'}">active</c:if>" >个人信息</a></li>
            <shiro:hasRole name="company_admin">
                <li><a href="<%=basePath%>admin/message" class="<c:if test="${param.menu=='message'}">active</c:if>" >我的消息</a></li>
            </shiro:hasRole>
            <shiro:hasRole name="company_user">
                <c:if test="${sessionScope.company_id == '-1'}">
                <li><a href="<%=basePath%>popup/companyChange" class="<c:if test="${param.menu=='companyChange'}">active</c:if>" >单位更换</a></li>
                </c:if>
                <li><a href="<%=basePath%>student/message" class="<c:if test="${param.menu=='message'}">active</c:if>" >我的消息</a></li>
            </shiro:hasRole>
        </ul>
    </div>
</div>
<!-- input hidden -->
<input type="hidden" id="user-role-name" value="${sessionScope.bossien_role.roleName}">
<input type="hidden" id="user-id" value="${sessionScope.bossien_user.id}">
<%--<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>--%>
<%--<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>--%>
<%--<script src="<%=resourcePath%>static/global/js/sso/setting.js"></script>--%>

