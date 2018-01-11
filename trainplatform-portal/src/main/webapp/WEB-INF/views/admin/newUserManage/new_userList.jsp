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
                    <input type="hidden" id="companyIds" name="companyIds" value="">
                    <input type="hidden" id="deptIds" name="deptIds" value="">
                    <input type="hidden" id="companyId" name="companyId" value="${companyId}">
                    <input type="hidden" id="departmentId" name="departmentId" value="">
                    <input type="hidden" id="departmentName" name="departmentName" value="">
                    <input type="hidden" id="createDeptUrl" name="createDeptUrl" value="${createDeptUrl}">
                    <input type="hidden" id="createUserUrl" name="createUserUrl" value="${createUserUrl}">
                    <input type="hidden" id="updateDeptUrl" name="updateDeptUrl" value="${updateDeptUrl}">
                    <div class="pull-right search-group">
                        <span>
                            <a href="javascript:newUserList.importUser()" class="btn btn-info"><span class="fa fa-sign-in"></span> 导入</a>
                            <a href="javascript:newUserList.exportUser()" class="btn btn-info"><span class="fa fa-sign-out"></span> 导出</a>
                            <a href="javascript:newUserList.moveUser()" class="btn btn-info"><span class="fa fa-exchange"></span> 转移</a>
                            <a href="javascript:newUserList.quitUser()" class="btn btn-info"><span class="fa fa-share"></span> 离职</a>
                            <a href="javascript:newUserList.batchDelUser()" class="btn btn-info"><span class="fa fa-remove"></span> 批量删除</a>
                        </span>

                        <span class="search">
                            <select class="pull-left" id="searchType" name="searchType" onchange="onChangeStatus()">
                                <option value="1">&nbsp; &nbsp;&nbsp;姓名</option>
                                <option value="2">用户名</option>
                            </select>
                            <input type="text" id="userName" name="userName" value="" placeholder="请输入姓名" style="width:150px;">
                             <input type="text" id="userAccount" name="userAccount" value="" placeholder="请输入用户名" style="width:150px;">
                            <button type="button" title="搜索" onclick="newUserList.search()" class="btn">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>

                        <span>
                            <a href="javascript:newUserList.searchAll()" class="all btn"> <span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                </form>

            </div>
            <div class="box-body tree_list" style="padding:0;">


                    <div>
                        <input type="checkbox" id="recursion" onclick="newUserList.search()"  style="vertical-align: middle;margin-top:-2px;margin-left:10px;"/>显示子节点人员
                    </div>
                    <div class="tree" style="background: #f7f8f9;">
                        <ul id="group_tree" class="ztree"></ul>
                    </div>
                    <div class="list">
                        <table class="table first_table">
                            <thead>
                                <tr>
                                    <th width="50">序号</th>
                                    <th width="50" ><input type="checkbox"  id="checkAll" value=""></th>
                                    <th width="100" >姓名</th>
                                    <th width="120" >用户名</th>
                                    <th width='150'>单位名称</th>
                                    <th width="107">部门</th>
                                    <th width="92">注册日期</th>
                                    <th width="220">操作</th>
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
        <li id="m_add_company"><a href="javascript:newUserList.addCompany()">新增单位</a></li>
        <li id="m_create_user" onclick="newUserList.createUser()">新增管理员</li>
        <li id="m_update_dept" onclick="newUserList.updataDeptUrl()">修改单位</li>
        <li id="m_add_dept" onclick="newUserList.addDept()">新增部门</li>
        <li id="m_del" onclick="newUserList.removeTreeNode()">删除节点</li>
        <li id="m_reset" onclick="newUserList.renameTreeNode()">重命名</li>
        <li id="m_moveup" onclick="newUserList.moveUp();">上移</li>
        <li id="m_movedown" onclick="newUserList.moveDown();">下移</li>
    </ul>
</div>
<!-- 隐藏字段 -->
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/common/groupTree.js"></script>
<script src="<%=resourcePath%>static/global/js/admin/newUserManage/newUserList.js"></script>

</body>
<script type="text/javascript">

//    document.domain =bosafe.com;
    //新建对象
    var page = new page();
    var rMenu;

    $(document).ready(function(){
        $("#recursion").attr("checked",false);
        newUserList.init(page);
        rMenu = $("#rMenu");
        $('.table_inner').niceScroll();
        setH();
        onChangeStatus();
    });

    function onChangeStatus(){
        var val = $("#searchType").val();
        if (val == 1) {
            $("#userName").show();
            $("#userAccount").hide();
        }else {
            $("#userName").hide();
            $("#userAccount").show();
        }
    }

    $(window).resize(function(){
        setTimeout(function(){
            setH();
        },100);

    });


    function setH(){
        var windowH = $(window).height();
        var navH = $('.nav').height(),
            topH = $('.header_top').height();
        $('.tree_list').css('height',windowH - navH - topH - 116 );
    }

</script>
</html>

