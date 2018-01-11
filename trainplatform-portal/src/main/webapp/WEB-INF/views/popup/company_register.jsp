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
    <title>博晟 | 培训平台-管理员-注册新单位</title>
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=resourcePath%>/static/global/js/select2/js/select2.full.js"></script>
    <jsp:include page="${path}/common/style" />
    <style>
        input[type="text"]{width:100%;}
        input[type="text"].shrot{width:30%;float: left;}
    </style>
</head>
<body>
<script type="text/javascript">var appPath = "<%=path %>";var resPath = "<%=resourcePath %>";var basePath = "<%=basePath %>";</script>
<div class="header_top">
    <div class="area">
        <div class="clearfix" >
            <a href="<%=basePath%>student">
                <img src="<%=resourcePath%>static/global/images/v2/hearer_logo.png" alt="" class="logo">
            </a>
            <div class="clearfix pull-right">
            </div>
        </div>
    </div>
</div>
<div class="nav">
    <div class="area clearfix">
        <ul class="clearfix pull-left">
            <li><a href="<%=basePath%>popup/companyRegister" class="active" >注册新单位</a></li>
        </ul>
    </div>
</div>

<div id="main">
    <form id="ff_user">
    <div class="area" >


        <!-- 项目基础信息 -->
        <div class="box" style="padding-bottom:10px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">注册单位信息</h3>
                <div class="pull-right search-group">
                    <a href="javascript:back();" class="btn btn-info"  id="btnback"><span class="fa fa-reply"></span> 返回</a>
                    <a href="javascript:register();" class="btn btn-info"  id="btn_back" style="width: 150px;"><span class="fa fa-long-arrow-right"></span> 申请注册新单位</a>

                </div>
            </div>

            <div class="box-body project_info">
                <div class="row">

                        <div class="col-12 form-group">
                            <label class="col-2"><b class="text-red">*</b>企业名称：</label>
                            <div class="col-7">
                                <input type="text"  value="" id="companyName" ></div>
                        </div>

                    <div class="col-12 form-group">
                        <label class="col-2"><b class="text-red">*</b>行政区划：</label>
                        <div class="col-7">
                            <input class="shrot" type="text" id="province"  placeholder="请输入省" name="userName" value="" />
                            <input class="shrot" type="text" id="city" style="margin:0 5%;"  placeholder="请输入市" name="companyName" value="" />
                            <input class="shrot" type="text" id="district"  placeholder="请输入区\县" name="companyName" value="" />
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-2"><b class="text-red">*</b>行业类别：</label>
                        <div class="col-7">
                            <input type="text"  value="" id="industryType" name="userName" ></div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-2">社会信用代码：</label>
                        <div class="col-7">
                            <input type="text"  value="" id="socialCreditCode" name="" ></div>
                    </div>
                </div>
            </div>
        </div>

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

    function register(){
        var companyName = $("#companyName").val();
        var province = $("#province").val();
        var city = $("#city").val();
        var district = $("#district").val();
        var industryType = $("#industryType").val();
        var socialCreditCode = $("#socialCreditCode").val();

        if(companyName == ""){
            layer.msg("请输入企业名称");
            return;
        }
        if(province == ""){
            layer.msg("请输入省份");
            return;
        }
        if(city == ""){
            layer.msg("请输入市");
            return;
        }
        if(industryType == ""){
            layer.msg("请输入行业类别");
            return;
        }
        var regionName = province + "/" + city
        if(district != ""){
            regionName +=  "/"+ district;
        }

        var param = {
            'companyName':companyName,
            'regionName':regionName,
            'industryType':industryType,
            'socialCreditCode':socialCreditCode
        }
        $.ajax({
            url: appPath+ '/popup/register',
            type:'post',
            dataType: 'json',
            data:param,
            success:function (data) {
                if(data && data['code'] == '50503'){
                    layer.msg("登录超时!");
                    return;
                }
                layer.msg("注册新单位的申请已发送，请等待审核");

            }

        })
    }




    //返回
    function back() {
        window.close();
       /* var url = appPath +  "/popup/companyChange";
        //跳转到注册页面
        window.location.href = url;*/
    }
</script>
</html>