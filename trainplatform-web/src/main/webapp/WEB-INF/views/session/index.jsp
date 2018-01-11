<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<style>
    .sub-nav-ul li {
        position: relative;
    }
    .sub-nav-ul li i {
        position: absolute;
        top: 12px;
        right: 30px;
        color: #188855;
        font-size: 14px;
        font-weight: 600;
    }

</style>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8"/>
    <title>博晟 | 管理平台</title>
    <jsp:include page="${path}/common/style" />
</head>

<body class="main-bg">
<jsp:include page="${path}/common/menu" ><jsp:param name="menu" value="session"/></jsp:include>
<div class="lk-sub-nav">
    <ul class="sub-nav-ul">
        <li>
            <a href="javascript:;" type="1" class="sub-nav-items sub-nav-hover">所有会话</a>
            <i>0</i>
        </li>
        <li>
            <a href="javascript:;" type="2" class="sub-nav-items">在线</a>
            <i>0</i>
        </li>
        <li>
            <a href="javascript:;" type="3" class="sub-nav-items">游客</a>
            <i>0</i>
        </li>
    </ul>
</div>
<div class="lk-pannel-main aia-content sms-content ">
    <div class="aia-btn-bar clearfix">
        <div class="left">
            <%--<button class="aia-btn aia-btn-left aia-btn-right aia-btn-active mr20R" type="button">分配权限</button>--%>
        </div>
        <div class="right clearfix" style="width: 450px;">
            <div class="input-group right" style="width: 250px;">
                <form id="userFormId">
                    <!-- input hidden -->
                    <input type="hidden" id="userType" value="${userType}">
                    <input type="text" placeholder="搜索编号、用户名、姓名" name="search" class="form-control" style="height: 32px;border-radius: 0;border-color: #d3dae5;">
                </form>
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default icon_search" onclick="user.goPage(1);" tabindex="-1" style="width: 36px;height: 32px;border-radius: 0;border-color: #d3dae5;">
                        <!--<i class="icon-search"></i>-->
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="sms-table" id="session-table-1">
        <div class="title clearfix" style="border: 1px solid #eaeaea;">
            <div style="line-height: 30px;">
                <span class="lk-checkbox select-all" style="vertical-align: -3px;"></span>
                <span style="font-weight: bold;">全选</span>
            </div>
            <div style="font-weight:bold;">操作</div>
        </div>
    </div>
    <div class="sms-table" id="session-table-2">
        <div class="title clearfix" style="border: 1px solid #eaeaea;">
            <div style="line-height: 30px;">
                <span class="lk-checkbox select-all" style="vertical-align: -3px;"></span>
                <span style="font-weight: bold;">全选</span>
            </div>
            <div style="font-weight:bold;">操作</div>
        </div>
    </div>
    <div class="sms-table" id="session-table-3">
        <div class="title clearfix" style="border: 1px solid #eaeaea;">
            <div style="line-height: 30px;">
                <span class="lk-checkbox select-all" style="vertical-align: -3px;"></span>
                <span style="font-weight: bold;">全选</span>
            </div>
            <div style="font-weight:bold;">操作</div>
        </div>
    </div>

</div>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>assets/js/session/index.js"></script>
<script type="text/javascript">
    session.init();
</script>
</body>
</html>

