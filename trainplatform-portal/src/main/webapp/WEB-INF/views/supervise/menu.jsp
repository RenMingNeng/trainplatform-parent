<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
            <c:if test="${!sessionScope.is_support_sso}">
            <ul class="clearfix pull-right">
                <li>用户：<span class="user_info">${sessionScope.bossien_user.userName}</span></li>
                <li class="split">|</li>
                <li style="position: relative">
                    角色：<span class="user_info">${sessionScope.bossien_role.roleDesc}</span>
                </li>

                    <li class="split">|</li>
                    <li><a href="javascript:modifyPass('${sessionScope.bossien_user.id}');" >修改密码</a></li>
                    <li class="split">|</li>
                    <li ><a href="<%=basePath%>logout" class="signout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></li>
                    <li class="split" style="margin-right:0;">|</li>
            </ul>
            </c:if>

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
                            <dd><a target="_blank" href="<%=basePath%>popup/studentModify?id=${sessionScope.bossien_user.id}">个人信息</a></dd>
                            <c:if test="${sessionScope.company_id == '-1'}">
                            <dd><a href="<%=basePath%>popup/companyChange"  id="account-setting-companyChange" target="_blank">单位变更</a></dd>
                            </c:if>
                            <dd><a href="javascript:enter_stbms();" target="_blank">准入管理平台</a></dd>
                            <dd><a href="<%=basePath%>logout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></dd>
                        </dl>
                    </div>
                </li>
                <li class="split">|</li>
                <li class="hand-hover">
                    帮助<i class="arrows"></i>
                    <div>
                        <dl>
                            <dd><a href="javascript:feed_back();" target="_blank">建议反馈</a></dd>
                            <dd><a href="<%=basePath%>static/guide/admin/step_0.html" target="_blank">操作指导</a></dd>
                        </dl>
                    </div>
                </li>
                <%--<li>
                    <div id="account-setting" style="position: relative;cursor: pointer;">
                        账户设置
                        <i class="fa fa-sort-down" style="vertical-align: text-top;"></i>
                        <div id="account-setting-layer" class="user-role-layer" >
                            <ul class="box-shadow">
                                <li class="b-b"><a target="_blank" href="<%=basePath%>popup/studentModify?id=${sessionScope.bossien_user.id}">个人信息</a></li>
                                <li>
                                    <a href="javascript:;">
                                        角色切换
                                        <span class="fa fa-caret-right" style="margin-left:10px;"></span>
                                    </a>
                                    <ol class="box-shadow" id="account-setting-layer-content">
                                        <c:forEach items="${sessionScope.bossien_roles}" var="role">
                                            <li>
                                                <a role="${role.roleName}" <c:if test="${role.roleName == sessionScope.bossien_role.roleName}">class="active"</c:if>>
                                                    <span>${role.roleDesc}</span>
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ol>
                                </li>
                            <c:if test="${sessionScope.company_id == '-1'}">
                                <li><a href="<%=basePath%>popup/companyChange"  id="account-setting-companyChange" target="_blank">单位变更</a></li>
                            </c:if>
                                <li style="display: none"><a href="javascript:;" id="account-setting-unbind">解绑账号</a></li>
                                <li><a href="javascript:enter_stbms();" target="_blank">准入管理平台</a></li>
                                <li><a href="javascript:;" target="_blank">操作指引</a></li>
                                <li class="b-b"><a href="javascript:feed_back();" target="_blank">意见反馈</a></li>
                                <li><a href="<%=basePath%>logout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></li>
                            </ul>
                        </div>--%>
                               <%-- <div id="account-setting-layer" class="user-role-layer">
                                    <div class="user-role-layer-title">
                                        <h2>我的角色</h2>
                                        <h5>切换角色不需要重新登录</h5>
                                    </div>
                                    <div id="account-setting-layer-content" class="user-role-layer-content">
                                        <c:forEach items="${sessionScope.bossien_roles}" var="role">
                                            <a role="${role.roleName}" <c:if test="${role.roleName == sessionScope.bossien_role.roleName}">class="active"</c:if>><span>${role.roleDesc}</span></a>
                                        </c:forEach>
                                    </div>
                                    <div class="user-role-layer-footer">

                                        <a id="account-setting-unbind" class="user-role-layer-footer-r user-role-layer-footer-un" ><span >解绑账号</span></a>
                                        <a id="account-setting-companyChange" class="user-role-layer-footer-r user-role-layer-footer-un"><span >单位变更</span></a>

                                        <a class="user-role-layer-footer-l">
                                            <span>统一登录账号：${sessionScope.sso_user.account}</span>
                                            <span>培训平台账号：${sessionScope.bossien_user.userAccount}</span>
                                        </a>


                                    </div>
                                </div>
                            </span>--%>
                 <%--   </div>
                </li>
                <li class="split">|</li>--%>

            </ul>
                </c:if>

        </div>
    </div>
</div>
<div class="nav">
    <div class="area clearfix">
        <ul class="clearfix pull-left">
            <li><a href="<%=basePath%>supervise" class="first <c:if test="${param.menu=='index'}">active</c:if>">数据统计</a></li>
        </ul>
    </div>
</div>
<!-- input hidden -->
<input type="hidden" id="user-role-name" value="${sessionScope.bossien_role.roleName}">
<input type="hidden" id="user-id" value="${sessionScope.bossien_user.id}">
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script src="<%=resourcePath%>static/global/js/sso/setting.js"></script>
<script type="text/javascript">

    sso_setting.init();

    function modifyPass(userId){
        var url="<%=path%>/modifyPass?userId="+userId;
        layer.open({
            type: 2,
            title: '修改密码',
            area: ['450px', '380px'],
            shade: 0.3,
            closeBtn: 1,
            shadeClose: false,
            content: url
        });
    }

</script>
