<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>

    <!-- BEGIN GLOBAL STYLES -->

    <base href="<%=basePath%>"/>
    <jsp:include page="${path}/common/style"/>
    <title>博晟 | 培训平台-管理员-首页</title>
    <style>
        .layui-layer .table td {
            border-right: 0 none;
            border-bottom: 0 none;
            padding-right: 20px;
            text-align: left;
        }

        .layui-layer .table .th {
            font-weight: bold;
            text-align: right;
            padding-right: 10px;
            vertical-align: top;
            width: 120px;
        }

        .layui-layer input[type="text"] {
            width: 100%;
        }

        .layui-layer input[type="text"][readonly] {
            color: #555;
            font-size: 14px;
            font-family: '微软雅黑';
        }

        .layui-layer textarea {
            width: 100%;
            border: 1px solid #ccc;
            outline: none;
            padding: 4px;
            color: #555;
            font-size: 14px;
            font-family: '微软雅黑';
        }

        .layui-layer textarea:focus {
            border: 1px solid #31b0d5;
        }

        .layui-layer textarea[readonly] {
            border: 1px solid #ddd;
            background: #ddd;
        }

        .layui-layer .box {
            margin-bottom: 0;
        }
    </style>


    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
    <script src="<%=resourcePath%>static/global/js/page.js"></script>
    <!-- END GLOBAL STYLES -->
</head>

<body>
<jsp:include page="${path}/admin/menu">
    <jsp:param name="menu" value="basicinfo"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area">
        <!-- 题库练习列表 -->
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">受训角色</h3>
                <form id="trainrole_form">
                    <div class="pull-right search-group">
                    <span>
                         <a href="javascript:;" id="btn_add" class="btn btn-info" onclick=""><span
                                 class="fa fa-plus"></span> 新增</a>
                            <a href="javascript:;" id="btn_del" class="btn btn-info"><span class="fa fa-trash-o"></span>
                                删除</a>
                            <a href="javascript:;" id="btn_im" class="btn btn-info"><span class="fa fa-sign-in"></span>
                                导入</a>
                            <a href="javascript:;" id="btn_ex" class="btn btn-info"><span class="fa fa-sign-out"></span>
                                导出</a>
                    </span>
                        <span class="search">
                        <input type="text" placeholder="请输入角色名称" name="roleName" id="all">
                        <button type="button" class="btn" title="搜索" onclick="train_role.search();">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                    </span>
                        <span>
                        <a href="javascript:;" class="btn all" onclick="train_role.searchAll();"><span
                                class="fa fa-th"></span>  全部</a>
                    </span>
                        <input type="hidden" name="role_id">
                    </div>
                </form>
            </div>
            <div class="box-body">
                <!-- 表格 -->
                <table class="table pro_msg admin_index_table">
                    <thead>
                    <tr>
                        <th width="150"><input type="checkbox" id="send" value=""></th>
                        <th width="150">序号</th>
                        <th width="300">受训角色</th>
                        <th>受训角色来源</th>
                        <th width="300">更新日期</th>
                        <th>操作</th>
                    </tr>
                    </thead>

                    <%--列表--%>
                    <tbody id="trainrole_table">
                    <tr>
                        <td colspan="5" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                    </tbody>
                </table>


            </div>
            <%-- 分页--%>
            <div class="page text-right">
                <ul id="trainrole_page"></ul>
            </div>
        </div>

    </div>
</div>

<jsp:include page="${path}/common/footer"/>
<jsp:include page="${path}/common/script"/>

<script src="<%=resourcePath%>static/global/js/admin/basicinfo/train_role_index.js"></script>
<script>
    var page = new page();
    train_role.init(page);
</script>
</body>

</html>

