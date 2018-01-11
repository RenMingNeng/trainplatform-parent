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
            <a href="<%=basePath%>admin">
                <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" class="logo">
            </a>
            <c:if test="${!sessionScope.is_support_sso}">
            <ul class="clearfix pull-right">
                <li><span class="user_info" style="max-width:240px;overflow: hidden;display: block;text-overflow: ellipsis;white-space: nowrap;" title="${sessionScope.bossien_user.userName}">用户：${sessionScope.bossien_user.userName}</span></li>
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
                <li><span class="user_info " style="max-width:240px;overflow: hidden;display: block;text-overflow: ellipsis;white-space: nowrap;" title="${sessionScope.bossien_user.userName}">${sessionScope.bossien_user.userName}</span></li>
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
                            <dd><a href="javascript:enter_stbms();" target="_blank">准入管理平台</a></dd>
                            <dd><a href="<%=basePath%>logout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></dd>
                        </dl>
                    </div>
                    <%--<div id="account-setting-layer" class="user-role-layer" >
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
                            <li style="display: none"><a href="javascript:;" id="account-setting-unbind">解绑账号</a></li>
                            <li><a href="javascript:enter_stbms();" target="_blank">准入管理平台</a></li>
                            <li><a href="<%=basePath%>static/guide/admin/step_0.html" target="_blank">操作指引</a></li>
                            <li class="b-b"><a href="javascript:feed_back();" target="_blank">意见反馈</a></li>
                            <li><a href="<%=basePath%>logout">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></li>
                        </ul>
                    </div>--%>
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
            </ul>
            </c:if>
        </div>
    </div>
</div>
<div class="nav">
    <div class="area clearfix">
        <ul class="clearfix pull-left">
            <shiro:hasPermission name="admin:index">
            <li><a href="<%=basePath%>admin" class="first <c:if test="${param.menu=='index'}">active</c:if>">首页</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="admin:user">
            <li><a href="<%=basePath%>admin/user/new_userList" class="<c:if test="${param.menu=='user'}">active</c:if>">人员管理</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="admin:course">
            <li><a href="<%=basePath%>admin/course/courseList" class="<c:if test="${param.menu=='course'}">active</c:if>">课程管理</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="admin:project">
            <li><a href="<%=basePath%>admin/project/projectList" class="<c:if test="${param.menu=='proManager'}">active</c:if>">项目管理</a></li>
            </shiro:hasPermission>
            <shiro:hasPermission name="admin:dossier">
            <li>
                <a href="javascript:;" class="<c:if test="${param.menu=='dossier'}">active</c:if>">档案管理</a>
                <dl style="width:230px;">
                    <dt>档案管理</dt>
                    <dd style="width:80px;"><a href="<%=basePath%>admin/dossier/person">人员档案</a></dd>
                    <dd style="width:80px;"><a href="<%=basePath%>admin/dossier/project">项目档案</a></dd>
                </dl>
            </li>
            </shiro:hasPermission>
            <shiro:hasPermission name="admin:basic">
            <li>
                <a  href="<%=basePath%>admin/trainSubject" class="<c:if test="${param.menu=='basicinfo'}">active</c:if>">培训主题</a>
                <%--<dl>
                    <dt>基本信息</dt>
                    &lt;%&ndash;<dd><a href="<%=basePath%>admin/trainRole">受训角色</a></dd>&ndash;%&gt;
                    <dd><a href="<%=basePath%>admin/trainSubject">培训主题</a></dd>
                </dl>--%>
            </li>
            </shiro:hasPermission>
            <li><a href="<%=basePath%>popup/msg" class="<c:if test="${param.menu=='msg'}">active</c:if>">通知公告</a></li>
           <%-- <li><a href="<%=basePath%>admin/message" class="<c:if test="${param.menu=='message'}">active</c:if>">我的消息</a></li>--%>
            <li><a href="<%=basePath%>admin/deptSupervise" class="<c:if test="${param.menu=='deptSupervise'}">active</c:if>">统计查询</a></li>
        </ul>
    </div>
</div>
<!-- input hidden -->
<input type="hidden" id="user-role-name" value="${sessionScope.bossien_role.roleName}">
<input type="hidden" id="user-id" value="${sessionScope.bossien_user.id}">
<%--<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>--%>
<%--<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>--%>
<%--<script src="<%=resourcePath%>static/global/js/sso/setting.js"></script>--%>

