<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<div id="#lk-nav-block" class="lk-menu">
    <a class="lk-logo left" style="visibility: hidden">
        <img class="lk-logo-img" src="<%=resourcePath%>/assets/images/hearer_logo.png" alt="">
    </a>
    <div class="left relative">
        <ul>
            <%--<li class="lk-menu-items">--%>
                <%--<c:if test="${param.menu=='permission'}">--%>
                    <%--<a href="${path}permission/admin" class="lk-menu-link lk-menu-active">权限</a><div class="lk-menu-line"></div>--%>
                <%--</c:if>--%>
                <%--<c:if test="${param.menu!='permission'}">--%>
                    <%--<a href="${path}permission/admin" class="lk-menu-link">权限</a>--%>
                <%--</c:if>--%>
            <%--</li>--%>
            <li class="lk-menu-items">
                <c:if test="${param.menu=='company'}">
                    <a href="${path}company" class="lk-menu-link lk-menu-active">单位</a><div class="lk-menu-line"></div>
                </c:if>
                <c:if test="${param.menu!='company'}">
                    <a href="${path}company" class="lk-menu-link">单位</a>
                </c:if>
            </li>
            <li class="lk-menu-items">
                <c:if test="${param.menu=='user'}">
                    <a href="${path}user" class="lk-menu-link lk-menu-active">账户</a><div class="lk-menu-line"></div>
                </c:if>
                <c:if test="${param.menu!='user'}">
                    <a href="${path}user" class="lk-menu-link">账户</a>
                </c:if>
            </li>
            <%--<li class="lk-menu-items">--%>
                <%--<c:if test="${param.menu=='session'}">--%>
                    <%--<a href="${path}session" class="lk-menu-link lk-menu-active">会话</a><div class="lk-menu-line"></div>--%>
                <%--</c:if>--%>
                <%--<c:if test="${param.menu!='session'}">--%>
                    <%--<a href="${path}session" class="lk-menu-link">会话</a>--%>
                <%--</c:if>--%>
            <%--</li>--%>
            <li class="lk-menu-items">
                <c:if test="${param.menu=='article'}">
                    <a href="${path}article" class="lk-menu-link lk-menu-active">新闻公告</a><div class="lk-menu-line"></div>
                </c:if>
                <c:if test="${param.menu!='article'}">
                    <a href="${path}article" class="lk-menu-link">新闻公告</a>
                </c:if>
            </li>
            <li class="lk-menu-items">
                <c:if test="${param.menu=='feedback'}">
                    <a href="${path}feedback" class="lk-menu-link lk-menu-active">反馈处理</a><div class="lk-menu-line"></div>
                </c:if>
                <c:if test="${param.menu!='feedback'}">
                    <a href="${path}feedback" class="lk-menu-link">反馈处理</a>
                </c:if>
            </li>
        </ul>
    </div>
</div>