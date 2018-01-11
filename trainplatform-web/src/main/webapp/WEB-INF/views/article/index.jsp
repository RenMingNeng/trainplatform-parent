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
    <jsp:include page="${path}/common/script" />
</head>

<body class="main-bg">
<jsp:include page="${path}/common/menu" ><jsp:param name="menu" value="article"/></jsp:include>
<div class="lk-sub-nav">
    <ul class="sub-nav-ul">
        <li>
            <a href="${path}article" class="sub-nav-items<c:if test="${empty param.personType}"> sub-nav-hover</c:if>">全部</a>
        </li>
        <li>
            <a href="${path}article?personType=1" class="sub-nav-items<c:if test="${param.personType == '1'}"> sub-nav-hover</c:if>">不限</a>
        </li>
        <li>
            <a href="${path}article?personType=2" class="sub-nav-items<c:if test="${param.personType == '2'}"> sub-nav-hover</c:if>">学员</a>
        </li>
        <li>
            <a href="${path}article?personType=3" class="sub-nav-items<c:if test="${param.personType == '3'}"> sub-nav-hover</c:if>">管理员</a>
        </li>
    </ul>
</div>
<div class="lk-pannel-main aia-content sms-content ">
    <div class="aia-btn-bar clearfix">
        <div class="left">
            <button class="aia-btn aia-btn-left aia-btn-right aia-btn-active mr20R" onclick="window.location.href = '${path}article/add';" type="button">添加文章</button>
            <button class="aia-btn aia-btn-left aia-btn-right aia-btn-active mr20R" onclick="article.clear();" type="button">刷新前台数据</button>
        </div>
        <div class="right clearfix" style="width: 450px;">
            <div class="input-group right" style="width: 250px;">
                <form id="articleForm">
                    <!-- input hidden -->
                    <input type="hidden" name="personType" value="${param.personType}">
                    <input type="text" placeholder="搜索标题、作者、内容" name="search" class="form-control" style="height: 32px;border-radius: 0;border-color: #d3dae5;">
                </form>
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default icon_search" onclick="article.goPage(1);" tabindex="-1" style="width: 36px;height: 32px;border-radius: 0;border-color: #d3dae5;">
                        <!--<i class="icon-search"></i>-->
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="sms-table" id="article-table">
        <div class="title clearfix" style="border: 1px solid #eaeaea;">
            <div style="line-height: 30px;">
                <span class="lk-checkbox select-all" style="vertical-align: -3px;"></span>
                <span style="font-weight: bold;">全选</span>
            </div>
            <div style="font-weight:bold;">操作</div>
        </div>
        <!--
        <div class="title tr clearfix">
            <div style="line-height: 30px;">
                <span class="lk-checkbox" style="vertical-align: -3px;"></span>
                <span style="color: #4b586a;"><span style="font-weight: 600;">18925251835(1人）</span>已发送1人失败0人  </span>
                <span class="send-time">发送时间:2017-06-15 16时24分</span>
                <p>尊敬的旺铺助手用户，截止到今天18时，您本周共有1条通话，其中1条为呼入（1条未接）、0条为呼出，发送短信20条。感谢使用旺铺助手业务！</p>
            </div>
            <div>
                <a href="" class="sms-section5-link">详情</a>
                <a href="" class="sms-section5-link">编辑</a>
            </div>
        </div>
        -->
    </div>
    <div class="clearfix pageBox" id="page-footer" style="margin-top: 50px;margin-right: 20px;float: right;">

    </div>

</div>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" charset="utf-8" src="<%=resourcePath%>/assets/js/article/article.js"></script>
<script type="text/javascript">
    article.goPage();
</script>
</body>
</html>

