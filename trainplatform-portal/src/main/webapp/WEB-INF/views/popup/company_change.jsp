<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>博晟 | 培训平台-管理员-单位变更</title>
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=resourcePath%>/static/global/js/select2/js/select2.full.js"></script>
    <jsp:include page="${path}/common/style" />
    <style>
        input[type="text"],.select_box{width:680px;}
        .select2-container--default .select2-selection--single{border-radius: 0;outline: none;border-color:#ccc;}
        .select2-container--default .select2-selection--single:focus{border-color: #31b0d5;}
        .col-11{padding-left:20px;}
        .search-group .a{
            border-color: #eee;
            padding: 10px 15px;
            border-radius: 2px;
            background: #eee;
            color: #fff;
            opacity: 1;
            display: inline-block;
            line-height: 12px;
            font-size: 12px;
            margin: 0 5px;
            cursor: default;
        }
    </style>
</head>
<body>
<jsp:include page="${path}/popup/menu">
<jsp:param name="menu" value="companyChange"></jsp:param>
</jsp:include>
<div id="main">
    <form id="ff_user">
    <div class="area" >


        <!-- 项目基础信息 -->
        <div class="box" style="padding-bottom:10px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">单位更换</h3>
                <div class="pull-right search-group">
                    <a href="javascript:back();" class="btn btn-info"  id="btnback"><span class="fa fa-reply"></span> 返回</a>
                    <a href="javascript:sendMessage()" class="btn btn-info" id="btn_project_base_info_save"><span class="fa fa-save"></span> 申请更换</a>
                    <%--<a href="javascript:register();" class="btn btn-info"  id="btn_back" style="width: 100px;"><span class="fa fa-long-arrow-right"></span> 注册新单位</a>--%>
                </div>
            </div>

            <div class="box-body project_info">
                <div class="row">

                        <div class="col-12 form-group">
                            <label class="col-1">原单位：</label>
                            <div class="col-11">
                                <input type="text"  value="${companyName}" readonly></div>
                        </div>

                    <div class="col-12 form-group">
                        <label class="col-1">新单位：</label>
                        <div class="col-11">
                            <input type="text" id="companyCode" placeholder="请输入单位编号" name="userName" value="" style="width: 340px;" onblur="checkCompanyCode()"/>
                            <input type="text" id="companyName"  placeholder="单位名称" name="companyName" value="" style="width: 340px;"/>
                            <span id="msg"></span>
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-1">申请人姓名：</label>
                        <div class="col-11">
                            <input type="text"  value="" id="userName" name="userName" ></div>
                    </div>
                </div>
            </div>
        </div>

        <%--<div class="pull-left search-group">
&lt;%&ndash;
            <a href="javascript:back();" class="btn btn-info"  id="btnback"><span class="fa fa-reply"></span> 返回</a>
&ndash;%&gt;
            <a href="javascript:checkCompanyCode()" class="btn btn-info" id="btn_project_base_info_save"><span class="fa fa-save"></span> 申请更换</a>
           &lt;%&ndash; &ndash;%&gt;
        </div>--%>

    </div>
    </form>
    <input type="hidden" name ="userId" id="userId" value="${userId}">
    <input type="hidden" name ="companyId" id="companyId" value="${companyId}">
    <input type="hidden" name ="company_id" id="company_id" value="${companyId}">
    <input type="hidden" name ="register_url" id="register_url" value="${register_url}">
    <input type="hidden" name ="app_code" id="app_code" value="${app_code}">
    <input type="hidden" name ="openId" id="openId" value="${openId}">
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>/static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript">

    function checkCompanyCode(){
        var companyCode = $("#companyCode").val();
        var userName = $("#userName").val();
        if(companyCode == ""){
            $("#companyName").val("");
            layer.msg("请输入单位编号");
            return;
        }
        /*if(userName == ""){
            layer.msg("请输入姓名");
            return;
        }*/
        var param = {
            'varCode':companyCode
        }
        $.ajax({
            url: appPath+ '/find_company_by_varcode',
            type:'post',
            dataType: 'json',
            data:param,
            success:function (data) {
                var company = data.result;
                if(company == null){
                    var message = "单位不存在";
                    layer.msg(message);
                    $("#companyName").val("");
                    $("#companyName").attr("data-intId","");
                    return;
                }
                var companyId =  $("#companyId").val();
                if(company.intId == companyId){
                    layer.msg("已属于该单位");
                    $("#companyName").val("");
                    $("#companyName").attr("data-intId","");
                    return;
                }
                $("#companyName").val(company.varName);
                $("#companyName").attr("data-intId",company.intId);
                $("#companyName").attr("readOnly",true);
            }

        })
    }

     //变更单位
    function sendMessage() {
        var userName = $("#userName").val();
        var company_id = $("#companyName").attr("data-intId");
        //检查单位code
       var companyId =  $("#companyId").val();
        var userId = $("#userId").val();

        if(company_id == undefined){
            layer.msg("请输入单位编号");
            return;
        }
        if(company_id == ""  ){
            layer.msg("单位不存在");
            return;
        }
        if(companyId == company_id){
            layer.msg("已属于该单位");
            return;
        }
        if(userName == ""){
            layer.msg("请输入姓名");
            return;
        }
      var param = {
            'companyId':company_id,
            'userName':userName
      }
        $.ajax({
            url: appPath+ '/sendMessage',
            type:'post',
            dataType: 'json',
            data:param,
            success:function (data) {

                if(data && data['code'] == '50503'){
                    layer.msg("登录超时!");
                    return;
                }
                layer.msg("更换单位申请已发送，请等待审核");

            }

                })


            }

     //注册新单位
    function register() {
        var url = appPath +  "/popup/companyRegister"
        //跳转到注册页面
        window.location.href = url;
    }

    //返回
    function back() {

        window.close();
    }
</script>
</html>