<%@ page import="java.util.Map" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
    Map<String, String> companys = (Map<String, String>) request.getAttribute("companys");
    System.out.println(companys);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8"/>
    <title>博晟 | 管理平台</title>
    <jsp:include page="${path}/common/style" />
    <link type="text/css" rel="stylesheet" href="<%=path%>/assets/js/select2/css/select2.min.css"/>
    <style>
        html,body{
            font-family: "Microsoft YaHei", "Helvetica Neue", Helvetica, sans-serif;
        }

        .sms-send-blue {
            color: #007fda;
            font-size: 16px;
            font-weight: bold;
            padding: 0 3px;
        }

        /*去掉input、button、textarea默认样式-开始*/
        .lk-pannel-main input,.lk-pannel-main textarea,.lk-pannel-main button{
            border: 1px solid #e4e4e4!important;
            -webkit-box-shadow: none!important;
            -moz-box-shadow: none!important    ;
            box-shadow: none!important    ;
            outline: none!important;
        }

        .lk-pannel-main input:focus,.lk-pannel-main textarea:focus{
            border: 1px solid #e4e4e4!important;
            -webkit-box-shadow: none!important;
            -moz-box-shadow: none!important    ;
            box-shadow: none!important    ;
        }
        /*去掉input、button、textarea默认样式-结束*/
    </style>
</head>

<body class="main-bg">
<jsp:include page="${path}/common/menu" ><jsp:param name="menu" value="permission"/></jsp:include>
<div class="lk-pannel-main aia-content sms-content sms-send-content">
    <div class="sms-send-content-title">权限分配</div>
    <p style="margin-left: 20px;line-height: 26px;color: #8f99a8;">温馨提示：权限分配单次最多支持1000个单位,若数量较多请分批次操作。</p>
    <div class="send-tel">分配单位
        <div id="select_companys">
        <c:forEach var="company" items="${companys}">
            <span style="color:#8f99a8;" data-company="${company.key}"><c:out value="${company.value}"/></span>
        </c:forEach>
        </div>
    </div>
    <div class="grp01" style="margin-top:12px;">
        <div class="clearfix row01" style="overflow: hidden">
                <span style="float: left">
                   <span style="margin-right: 31px;">管理员</span>
                </span>
        </div>
        <select id="textarea_admin" name="textarea_admin" rows="4" class="form-control" placeholder=""></select>
    </div>

    <div class="grp01 grp02" style="margin-top:20px;">
        <div class="clearfix row01" style="overflow: hidden">
                <span style="float: left">
                   <span style="margin-right: 31px;">学员</span>
                </span>
        </div>
        <select id="textarea_student" name="textarea_student" rows="4" class="form-control" placeholder=""></select>
    </div>

    <div class="grp01 grp02" style="margin-top:20px;">
        <div class="clearfix row01" style="overflow: hidden">
                <span style="float: left">
                   <span style="margin-right: 31px;">监管</span>
                </span>
        </div>
        <select id="textarea_supervise" name="textarea_supervise" rows="4" class="form-control" placeholder=""></select>
    </div>

    <div class="sms-send-btn-grp clearfix" style="margin-top:20px;overflow: hidden">
        <button class="aia-btn aia-btn-left aia-btn-right aia-btn-active" onclick="companyPermission.save();" type="button" style="margin-left:20px;">保存</button>
        <button class="aia-btn aia-btn-left aia-btn-right" type="button" style="margin-left:20px;" onclick="history.go(-1);">取消</button>
    </div>

</div>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>/assets/js/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=resourcePath%>assets/js/permission/index.js"></script>
<script type="text/javascript">
    companyPermission.init();
</script>
</body>
</html>

