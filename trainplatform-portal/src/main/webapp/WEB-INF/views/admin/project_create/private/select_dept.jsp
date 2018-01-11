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
<title>博晟 | 培训平台-管理员-人员管理</title>
    <style>
        .tree_list{height:auto;}
        .tree_list .tree{width:300px;padding-right:0;}
        .tree_list .list{margin-left:310px;}
        .index{display: none;}
        .box{margin-top:20px;margin-bottom:20px;}
        .page{margin-bottom:0;}
        .btn-group .btn{height: 36px;line-height: 34px;width: 85px;color: #fff;background: #2487de;border-color:#2487de}
        .btn-group .btn.unclick{background: #ccc;border: 1px solid #ccc;}
        .btn-info {
            color: #fff;
            /* background-color: #00b8c7; */
            /* border-color: #00b8c7; */
        }
        .nav{display: none}
    </style>
<jsp:include page="${path}/common/style" />
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script src="<%=resourcePath%>static/global/js/zTree/js/jquery.ztree.all.min.js"></script>
<script type="text/javascript">

    $(document).ready(function(){
        tree();
    });
</script>
</head>
<body style="min-width: initial;">
<div class="btn-group pull-right" style="margin-right: 5px;">
    <a href="javascript:;" class="btn" id="next"  onclick="selectDept()" style="color: #fff;"> 确定</a>
</div>
<div class="tree" style="background: #f7f8f9;">
    <ul id="group_tree" class="ztree"></ul>
</div>

<!-- 隐藏字段 -->
<input type="hidden" id="companyId" name="companyId" value="${companyId}">
<input type="hidden" id="userIds" name="userIds" value="${userIds}">
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/common/groupTree.js"></script>
<script type="text/javascript">
    window.groupTree_search = transDepartment;
    groupTree.init();
</script>
</body>
</html>