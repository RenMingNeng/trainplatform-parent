<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<shiro:hasRole name="company_user">
 <c:redirect url="../student"></c:redirect>
</shiro:hasRole>

<shiro:hasRole name="company_admin">
    <c:redirect url="../admin"></c:redirect>
</shiro:hasRole>

<shiro:hasRole name="super_vise">
    <c:redirect url="../supervise"></c:redirect>
</shiro:hasRole>

<shiro:hasRole name="super_admin">
    <c:redirect url="../super"></c:redirect>
</shiro:hasRole>

平台未匹配到您的角色, 跳转失败
