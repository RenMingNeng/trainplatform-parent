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
<jsp:include page="${path}/common/menu" ><jsp:param name="menu" value="company"/></jsp:include>
<div class="lk-sub-nav">
    <p class="sub-nav-title">类型</p>
    <ul class="sub-nav-ul">
        <li>
            <a href="" class="sub-nav-items sub-nav-hover">树结构</a>
        </li>
    </ul>
    <ul id="company_type_tree" class="ztree"></ul>
</div>
<div class="lk-pannel-main aia-content sms-content ">
    <div class="aia-btn-bar clearfix">
        <div class="left">
            <button class="aia-btn aia-btn-left aia-btn-right aia-btn-active mr20R" type="button" id="btn-permission">分配权限</button>
            <%--<button class="aia-btn aia-btn-left aia-btn-right aia-btn-active mr20R" type="button" id="btn-permission-all">批量分配权限</button>--%>
        </div>
        <div class="right clearfix" style="width: 450px;">
            <div class="input-group right" style="width: 250px;">
                <input type="text" placeholder="搜索节点名称、节点编号" id="search_value" class="form-control" style="height: 32px;border-radius: 0;border-color: #d3dae5;">
                <div class="input-group-btn">
                    <button type="button" class="btn btn-default icon_search" onclick="company.search();" tabindex="-1" style="width: 36px;height: 32px;border-radius: 0;border-color: #d3dae5;">
                        <!--<i class="icon-search"></i>-->
                    </button>
                </div>
            </div>
        </div>
    </div>
    <div class="sms-table" id="user-table">
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
        <!--
        <span class="left" style="line-height: 32px;margin-right: 20px;">共<span style="color: #007fda;">500</span>条记录</span>
        <nav aria-label="Page navigation " class="left" style="height:32px;">
            <ul class="pagination" style="margin:0;">
                <li class="active"><a href="#">首页</a></li>
                <li>
                    <a href="#" aria-label="Previous">上一页</a>
                </li>
                <li><a href="#">下一页</a></li>
                <li>
                    <a href="#" aria-label="Next">尾页</a>
                </li>
            </ul>
        </nav>
        <span class="control-label left" style="vertical-align: 10px;line-height: 32px;margin: 0 10px 0 30px;">跳至</span>
        <input type="" name="" id="" value="" class="form-control left" style=" width: 40px;">
        <span class="control-label left " style="line-height: 32px;margin: 0 10px;">页</span>
        <button type="button " class="btn btn-default left" style="color: #818082;">跳转</button>
        -->
    </div>
    <div class="sms-content-right">
        <%--<div class="sms-content-right-item">--%>
            <%--<i></i>--%>
            <%--<p>本月共发送了<span>23</span>条短信，今日发送了<span>2</span>条短信</p>--%>
        <%--</div>--%>
        <%--<div class="sms-content-right-item">--%>
            <%--<i></i>--%>
            <%--<p>国家及通信运营商对短信发送量有一定的限制，为提高您的短信发送成功率，请点击：<a href="#" class="sms-section5-link">查看详情</a></p>--%>
        <%--</div>--%>
        <%--<div class="sms-content-right-item">--%>
            <%--<i></i>--%>
            <%--<p>按照国家相关规定，企业所发短信必须签名</p>--%>
        <%--</div>--%>
    </div>
</div>
<!-- input hidden -->
<input type="hidden" id="intTypeId">
<!-- form hidden -->
<form id="company-form" action="${path}permission" method="post">
    <input type="hidden" id="companyIdNameArr" name="companys">
</form>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>assets/js/companyType/index.js"></script>
<script type="text/javascript" src="<%=resourcePath%>assets/js/company/index.js"></script>
<script type="text/javascript">
    companyType.init();
    company.init();
</script>
</body>
</html>

