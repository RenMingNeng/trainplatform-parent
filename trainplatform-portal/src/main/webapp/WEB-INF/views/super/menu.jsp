<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<script type="text/javascript">var appPath = "<%=path %>";var resPath = "<%=resourcePath %>";var basePath = "<%=basePath %>";</script>
<div class="header_top"  id="header">
    <div class="area"  id="sub_header_wrapper" >
        <div class="clearfix">
            <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" class="logo">
                <c:if test="${sessionScope.is_support_sso}">
                <ul class="clearfix pull-right sso_nav">
                    <li><span class="user_info">${sessionScope.bossien_user.userName}</span></li>
                    <li class="split">|</li>
                    <li class="hand-hover role">
                        <span class="user_info">${sessionScope.bossien_role.roleDesc}</span><i class="arrows"></i>
                        <div>
                            <dl  id="account-setting-layer-content">
                                <c:forEach items="${sessionScope.bossien_roles}" var="role">
                                    <dd>
                                        <a role="${role.roleName}" <c:if test="${role.roleName == sessionScope.bossien_role.roleName}">class="active"</c:if>>
                                            <span>${role.roleDesc}</span>
                                        </a>
                                    </dd>
                                </c:forEach>
                            </dl>
                        </div>
                    </li>
                    <li class="split">|</li>
                    <li class="hand-hover set" >
                        设置<i class="arrows"></i>
                        <div>
                            <dl>
                                <dd><a href="<%=basePath%>logout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></dd>
                            </dl>
                        </div>
                        <%--<div id="account-setting" style="position: relative;cursor: pointer;">
                            账户设置
                            <i class="fa fa-sort-down" style="vertical-align: text-top;"></i>
                            <div id="account-setting-layer" class="user-role-layer" >
                                <ul class="box-shadow">
                                    <li class="b-b" style="display: none"><a href="javascript:;" id="account-setting-unbind">解绑账号</a></li>
                                    <li><a href="javascript:;" target="_blank">操作指引</a></li>
                                    <li class="b-b"><a href="javascript:feed_back();" target="_blank">意见反馈</a></li>
                                    <li><a href="<%=basePath%>logout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></li>
                                </ul>
                            </div>
                        </div>--%>
                    </li>
                    <li class="split">|</li>
                    <li class="hand-hover">
                        帮助<i class="arrows"></i>
                        <div>
                            <dl>
                                <dd><a href="javascript:feed_back();" target="_blank">建议反馈</a></dd>
                                <%--<dd><a  href="javascript:;" target="_blank">操作指导</a></dd>--%>
                            </dl>
                        </div>
                    </li>
                </ul>
                </c:if>
                <c:if test="${!sessionScope.is_support_sso}">
                <ul class="clearfix pull-right">
                    <li>用户：<span class="user_info">${sessionScope.bossien_user.userName}</span></li>
                    <li class="split">|</li>
                    <li style="position: relative">
                        角色：<span class="user_info">${sessionScope.bossien_role.roleDesc}</span>
                    </li>
                    <li class="split">|</li>
                    <li ><a href="<%=basePath%>logout" class="signout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></li>
                    <li class="split" style="margin-right:0;">|</li>
                </ul>
                </c:if>
        </div>
    </div>
</div>
<!-- input hidden -->
<input type="hidden" id="user-role-name" value="${sessionScope.bossien_role.roleName}">
<input type="hidden" id="user-id" value="${sessionScope.bossien_user.id}">
<%--<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script src="<%=resourcePath%>static/global/js/sso/setting.js"></script>--%>
