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
<jsp:include page="${path}/common/menu" ><jsp:param name="menu" value="feedback"/></jsp:include>
<div class="lk-sub-nav">
    <ul class="sub-nav-ul">
        <li>
            <a href="${path}feedback" class="sub-nav-items<c:if test="${empty param.personType}"> sub-nav-hover</c:if>">全部</a>
        </li>
        <li>
            <a href="${path}feedback?problemType=1" class="sub-nav-items<c:if test="${param.problemType == '1'}"> sub-nav-hover</c:if>">视频播放问题</a>
        </li>
        <li>
            <a href="${path}feedback?problemType=2" class="sub-nav-items<c:if test="${param.problemType == '2'}"> sub-nav-hover</c:if>">题目问题</a>
        </li>
        <li>
            <a href="${path}feedback?problemType=3" class="sub-nav-items<c:if test="${param.problemType == '3'}"> sub-nav-hover</c:if>">系统错误</a>
        </li>
        <li>
            <a href="${path}feedback?problemType=4" class="sub-nav-items<c:if test="${param.problemType == '4'}"> sub-nav-hover</c:if>">界面问题</a>
        </li>
        <li>
            <a href="${path}feedback?problemType=5" class="sub-nav-items<c:if test="${param.problemType == '5'}"> sub-nav-hover</c:if>">功能要求</a>
        </li>
        <li>
            <a href="${path}feedback?problemType=6" class="sub-nav-items<c:if test="${param.problemType == '6'}"> sub-nav-hover</c:if>">其他建议</a>
        </li>
    </ul>
</div>
<div class="lk-pannel-main aia-content sms-content">
    <div class="aia-btn-bar clearfix">
        <div class="left">
        </div>
        <div class="right clearfix">
            <form id="feedbackForm">
                <input type="hidden" value="${param.problemType}" name="problemType">
                <div class="input-group right" style="width: 250px;">
                    <input type="text" placeholder="搜索问题描述、提交人" name="search" class="form-control" style="height: 32px;border-radius: 0;border-color: #d3dae5;">

                    <div class="input-group-btn">
                        <button type="button" class="btn btn-default icon_search" onclick="feedback.goPage(1);" tabindex="-1" style="width: 36px;height: 32px;border-radius: 0;border-color: #d3dae5;">
                            <!--<i class="icon-search"></i>-->
                        </button>
                    </div>
                </div>
                <div class="input-group right" style="width: 200px;margin-right: 10px;">
                    <input type="text"class="Wdate form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'startTime\')}'})" placeholder="结束时间" name="endTime" id="endTime"
                           style="height: 32px;border-radius: 0;border-color: #d3dae5;">
                </div>
                <div class="input-group right" style="width: 200px;margin-right: 10px;">
                    <input type="text" class="Wdate form-control" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,maxDate:'#F{$dp.$D(\'endTime\')}'})" placeholder="开始时间" name="startTime" id="startTime"
                           style="height: 32px;border-radius: 0;border-color: #d3dae5;">
                </div>
            </form>
        </div>
    </div>
    <div class="sms-table" id="feedback-table">
        <div class="title clearfix" style="border: 1px solid #eaeaea;">
            <div style="line-height: 30px;">
                <span class="lk-checkbox select-all" style="vertical-align: -3px;"></span>
                <span style="font-weight: bold;">全选</span>
            </div>
            <div style="font-weight:bold;">操作</div>
        </div>
    </div>
    <div class="clearfix pageBox" id="page-footer" style="margin-top: 50px;margin-right: 20px;float: right;">

    </div>

</div>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" charset="utf-8" src="<%=resourcePath%>/assets/js/feedback/feedback.js"></script>
<script type="text/javascript">
    $(function () {

        ///选择效果
        var num;
        var tr_num;
        num = $('.tr .lk-checkbox-active').length;
        tr_num = $('.tr').length;
//        console.log(num + '-' + tr_num);
        $('.select-all').click(function () {

            if($(this).hasClass('lk-checkbox-active')){
                $('.sms-table .tr .lk-checkbox').removeClass('lk-checkbox-active');
                $(this).toggleClass('lk-checkbox-active');
//                $('.sms-table .tr .lk-checkbox').toggleClass('lk-checkbox-active');
            }else{
                $('.sms-table .tr .lk-checkbox').addClass('lk-checkbox-active');
                $(this).toggleClass('lk-checkbox-active');
//                $('.sms-table .tr .lk-checkbox').toggleClass('lk-checkbox-active');
            }
            num = $('.tr .lk-checkbox-active').length;
//            console.log(num);

        });

        $('.sms-table').on("click", ".tr .lk-checkbox",function () {
            if($(this).hasClass('lk-checkbox-active')){
                $(this).toggleClass('lk-checkbox-active');
            }else{
                $(this).toggleClass('lk-checkbox-active');
            }
            num = $('.tr .lk-checkbox-active').length;
//            console.log(num);
            if(num == tr_num){
                $('.select-all').addClass('lk-checkbox-active');
            }else {
                $('.select-all').removeClass('lk-checkbox-active');
            }

        });

        feedback.goPage();
    });
</script>
</body>
</html>

