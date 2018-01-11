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
    <base href="<%=basePath%>"/>

    <title>博晟 | 培训平台-管理员-首页</title>


    <!-- BEGIN GLOBAL STYLES -->
    <jsp:include page="${path}/common/style"/>
    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
    <script src="<%=resourcePath%>static/global/js/page.js"></script>
    <style>
        #trainsubject_table input[type="text"] {
            width: 100%;
        }

        #trainsubject_table .th {
            width: 100px;
            vertical-align: top;
            padding-right: 10px;
            text-align: right;
        }

        #trainsubject_table select {
            min-width: 80px;
        }

        #trainsubject_table textarea {
            padding: 4px;
            outline: none;
            border-radius: 3px;
            border: 1px solid #ccc;
        }

        #trainsubject_table textarea:focus {
            border: 1px solid #31b0d5;
        }
    </style>
    <!-- END GLOBAL STYLES -->
</head>

<body>
<jsp:include page="${path}/admin/menu">
    <jsp:param name="menu" value="basicinfo"/>
</jsp:include>


<%--<div id="addr">
    <div class="area">
        <a href="<%=resourcePath%>admin/trainSubject">培训主题 </a>
    </div>
</div>--%>

<div id="main">
    <div class="area">

        <!-- 题库练习列表 -->
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训主题</h3>
                <form id="trainsubject_form">

                    <div class="pull-right search-group">

                        <span>

                            <a href="javascript:;" id="btn_add" class="btn btn-info" onclick=""><span
                                    class="fa fa-plus"></span> 新增</a>

                            <a href="javascript:;" id="btn_del" class="btn btn-info"><span class="fa fa-trash-o"></span>
                                删除</a>
                    </span>
                        <span class="search">
                        <input type="text" placeholder="请输入培训主题" name="subjectName" id="abc">
                        <button type="button" class="btn" title="搜索" onclick="train_subject.search();">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                    </span>
                        <span>
                        <a href="javascript:;" class="btn all" onclick="train_subject.searchAll();"><span
                                class="fa fa-th"></span>  全部</a>
                    </span>
                    </div>
                </form>
            </div>

            <div class="box-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th width="60"><input type="checkbox" id="all" value=""></th>
                        <th width="60">序号</th>
                        <%--<th class="pro_prosen">操作</th>--%>
                        <th>培训主题</th>
                        <th width="140">主题图片预览</th>
                        <th width="120">培训主题来源</th>
                        <th width="100">课程数量</th>
                        <th  width="100">更新日期</th>

                        <th  width="160">操作</th>
                    </tr>
                    </thead>


                    <%--列表--%>
                    <tbody id="trainsubject_table">
                    <tr>
                        <td colspan="6" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                    </tbody>
                </table>


            </div>
            <%-- 分页--%>
            <div class="page text-right">
                <ul id="trainsubject_page"></ul>
            </div>
        </div>

    </div>
</div>

<jsp:include page="${path}/common/footer"/>
<jsp:include page="${path}/common/script"/>

<script src="<%=resourcePath%>static/global/js/admin/basicinfo/train_subject_index.js"></script>
<script>
    var page = new page();
    train_subject.init(page);
</script>
</body>

</html>

