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
    <title>博晟 | 培训平台-管理员-人员管理</title>

    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">

    <!-- END GLOBAL STYLES -->
</head>

<body style="position: relative;">
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="user"></jsp:param>
</jsp:include>

<div id="main">

    <div class="area">
        <div class="box" style="padding:0;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">人员信息</h3>
                <form id="userListForm">
                    <input type="hidden" id="companyId" name="companyId" value="${companyId}">
                    <input type="hidden" id="departmentId" name="departmentId" value="">
                    <input type="hidden" id="departmentName" name="departmentName" value="">
                    <div class="pull-right search-group">
                        <span>
                            <a href="javascript:userList.importUser()" class="btn btn-info"><span class="fa fa-sign-in"></span> 导入</a>
                            <a href="javascript:userList.exportUser()" class="btn btn-info"><span class="fa fa-sign-out"></span> 导出</a>
                            <a href="javascript:userList.moveUser()" class="btn btn-info"><span class="fa fa-exchange"></span> 转移</a>
                            <a href="javascript:userList.quitUser()" class="btn btn-info"><span class="fa fa-share"></span> 离职</a>
                            <a href="javascript:userList.batchDelUser()" class="btn btn-info"><span class="fa fa-remove"></span> 批量删除</a>
                        </span>

                        <span class="search">
                            <select class="pull-left" id="searchType" name="searchType">
                                <option value="1">&nbsp; &nbsp;&nbsp;姓名</option>
                                <option value="2">用户名</option>
                            </select>
                            <input type="text" id="userName" name="userName" placeholder="请输入姓名或用户名" style="width:150px;">
                            <button type="button" title="搜索" onclick="userList.search()" class="btn">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>

                        <span>
                            <a href="javascript:userList.searchAll()" class="all btn"> <span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                </form>

            </div>
            <div class="box-body tree_list" style="padding:0;">
                    <div class="tree" style="background: #f7f8f9;">
                        <ul id="deparent_tree" class="ztree"></ul>
                    </div>
                    <div class="list">
                        <table class="table first_table">
                            <thead>
                                <tr>
                                    <th width="50">序号</th>
                                    <th width="50" ><input type="checkbox"  id="checkAll" value=""></th>
                                    <th width="130" >姓名</th>
                                    <th width="120" >用户名</th>
                                    <th>部门</th>
                                    <th width="92">注册日期</th>
                                    <th width="320">操作</th>
                                </tr>
                            </thead>
                        </table>
                        <div class="table_inner">
                            <table class="table" >
                                <tbody id="user_tbody">

                                </tbody>
                            </table>
                        </div>
                        <%--分页在这里--%>
                        <div class="page text-right bg_page">
                            <ul id="user_page"></ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>
<!-- 右键菜单 -->
<div id="rMenu" class="ztree_sub">
    <ul>
        <li id="m_add" onclick="userList.addTreeNode()">添加子节点</li>
        <li id="m_del" onclick="userList.removeTreeNode()">删除节点</li>
        <li id="m_reset" onclick="userList.renameTreeNode()">重命名</li>
        <li id="m_moveup" onclick="userList.moveUp();">上移</li>
        <li id="m_movedown" onclick="userList.moveDown();">下移</li>
    </ul>
</div>
<!-- 隐藏字段 -->
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/user_manager/user_list.js"></script>

</body>
<script type="text/javascript">

    //新建对象
    var page = new page();
    var rMenu;

    $(document).ready(function(){
       userList.init(page);
        rMenu = $("#rMenu");
        $('.table_inner').niceScroll();
        setH();
    });

    $(window).resize(function(){
        setTimeout(function(){
            setH();
        },100);

    })


    function setH(){
        var windowH = $(window).height();
        var navH = $('.nav').height(),
            topH = $('.header_top').height();
        $('.tree_list').css('height',windowH - navH - topH - 116 );
    }

</script>
</html>

