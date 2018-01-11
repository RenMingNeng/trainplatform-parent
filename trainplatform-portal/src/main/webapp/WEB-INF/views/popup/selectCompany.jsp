<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN" class="select full">
<head>
    <base href="<%=basePath%>"/>
    <meta charset="utf-8"/>
    <title>博晟 | 培训平台-管理员-单位树</title>

    <jsp:include page="${path}/common/style"/>

    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
    <script src="<%=resourcePath%>static/global/js/zTree/js/jquery.ztree.all.min.js"></script>
    <link type="text/css" rel="stylesheet" href="<%=resourcePath%>static/global/js/select2/css/select2.min.css"/>
    <style>
        body{min-width:100px;padding:10px;}
        .mg_b_10{margin-bottom:10px;}
        .select2-container{z-index:10000;}
        .select2.select2-container .select2-selection--multiple{height: 36px;overflow: auto;}
        .box .box-header{line-height:1;}
        .select2.select2-container .select2-selection--multiple,
        .select2-container--default.select2-container--focus .select2-selection--multiple,
        .select2.select2-container--default.select2-container--focus .select2-selection--multiple{border-color:#e1e1e1;}
        .table th{padding:12px 5px;}
        .select #first, .select #weiye {
            display: inline-block;
        }
    </style>
</head>
<body style="padding:0;">
<div class="box">
    <div class="box-header clearfix mg_b_10">
        <div class="search-group  ">
            <div class="pull-left" style="">
                <select id="business_select" name="varBusinessId" class="form-control" placeholder="" style="width: 260px;"></select>
                <select id="region_select" name="intRegionId" class="form-control" placeholder="" style="width: 260px;"></select>
            </div>
            <form id="companyForm">
                <input type="hidden" name="varBusinessId" id="varBusinessId">
                <input type="hidden" name="intRegionId" id="intRegionId">
                <div class="pull-right">
                    <span class="search">
                            <input type="hidden" name="companyId" id="companyId">
                            <input type="text" name="varName" placeholder="请输入单位名称" style="width: 300px;">
                        </span>
                    <span><a href="javascript:;" class="btn btn-info" onclick="select_company.saveCompany();">确定</a></span>
                </div>
            </form>

        </div>
    </div>
    <div class="box-body clear" style="padding-left:300px;">
        <div class="pull-left" style="margin-left:-300px;width:290px;overflow: auto;height: 100%;}">
            <ul id="company_tree_id" class="ztree"></ul>
        </div>
        <div class="container_table pull-left" style="padding-bottom:50px;padding-top:40px;width:100%;">
            <table class="table first_table">
                <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th width="60"><input type="checkbox" id="checkAll"></th>
                        <th>公司名称</th>
                    </tr>
                </thead>
            </table>
            <div class="table_inner">
                <table class="table">
                    <tbody id="company_table_id">

                    </tbody>
                </table>
            </div>
            <div class="page text-right bg_page">
                <ul id="select_company_page"></ul>
            </div>
        </div>
    </div>
</div>

<!-- 隐藏字段 -->
<jsp:include page="${path}/common/script"/>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/business.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/region.js"></script>
<script src="<%=resourcePath%>static/global/js/popup/select_company.js"></script>
<script type="text/javascript">
    var page = new page();
    select_company.initMethod('${param.nodeId}', 'company_table_id', page);

    $(function(){
        select_company.company_id = '${param.company_id}';
        $('.table_inner').niceScroll();

        $("#checkAll").click(function () {
            if($(this).prop("checked")){
                $("#company_table_id").find("input[type=checkbox]").prop("checked", true);
            }else{
                $("#company_table_id").find("input[type=checkbox]").prop("checked", false);
            }
        });

        $("#company_table_id").on("click","input[type=checkbox]",function () {
            var tr_num = $("#company_table_id").find("input[type=checkbox]").length;
            var num = $("#company_table_id").find("input[type=checkbox]:checked").length;
            if(num == tr_num){
                $('#checkAll').prop("checked", true);
            }else {
                $('#checkAll').prop("checked", false);
            }
        });

        $("#business_select").select2({
            tags:true,
            data:business_json,
            multiple:true,
            placeholder:"请选择行业",
            language:"zh-CN"
        });
        $("#region_select").select2({
            tags:true,
            data:resion_json,
            multiple:true,
            placeholder:"请选择行政区域",
            language:"zh-CN"
        });

        $(".form-control").on("select2:open",function(e){
            var name = $(this).attr("name");
            var id = name + "_tree";
            $(".select2-dropdown").html('<div><ul id="' + id + '" class="ztree ' + id + '" style="height: 380px;overflow: scroll;"></ul></div>');

            if(name == "varBusinessId"){
                select_company.initBusinessTree(id, $("#business_select").val());
            }else{
                select_company.initRegionTree(id, $("#region_select").val());
            }
        });

        $(".form-control").on("change",function(e){
            $(".select2-selection__choice__remove").remove();
            select_company.search();
        });
    });
    
    function getAllChildrens(tree, id) {
        var nodes = tree.getNodesByParam("id", id, null);
        var array = [];
        if(nodes.length > 0 && nodes[0].isParent){
            var childs = nodes[0].children;
            if(childs.length > 0){
                for(var i = 0; i < childs.length; i++){
                    array.push(getAllChildrens(tree, childs[i].id));
                }
            }
        }
        array.push(nodes[0].id);
        return array;
    }

</script>
</body>
</html>