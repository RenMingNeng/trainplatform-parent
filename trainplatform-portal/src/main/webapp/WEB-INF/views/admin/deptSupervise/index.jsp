<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN" class="loading">
<head>
    <base href="<%=basePath%>"/>
    <meta charset="utf-8"/>
    <title>博晟 | 培训平台-数据统计</title>
    <jsp:include page="${path}/common/style"/>
    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="<%=resourcePath%>static/global/css/supervise.css">
    <!-- END GLOBAL STYLES -->
</head>

<body style="position: relative;">
<jsp:include page="${path}/admin/menu">
    <jsp:param name="menu" value="deptSupervise"></jsp:param>
</jsp:include>

<div id="main">

    <div class="area">
        <div class="box" style="padding:0;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">部门信息</h3>
                <form id="superviseForm">
                    <input type="hidden" id="company_id" name="company_id">
                    <input type="hidden" id="dept_id" name="dept_id">
                    <input type="hidden" id="dept_name" name="dept_name">
                    <div class="pull-right search-group">
                        <span>
                        <label>开始时间</label>
                          <input type="text" name="startTime" id="startTime" class="Wdate"
                                 onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,maxDate:'#F{$dp.$D(\'endTime\')}'})">
                        </span>
                                    <span>
                        <label>结束时间</label>
                          <input type="text" name="endTime" id="endTime" class="Wdate"
                                 onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'startTime\')}'})">
                        </span>
                        <span class="search">
                            <input type="text" id="search" name="search" placeholder="请输入部门名称">
                            <button type="button" title="搜索" onclick="dept_supervise.search()" class="btn">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:dept_supervise.searchAll()" class="all btn"> <span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                </form>

            </div>
            <div class="box-body" style="padding:0;">
                <div class="clearfix tree_list">
                    <div class="pull-left tree" style="background: #f7f8f9; height:637px;overflow: auto;">
                        <div class="tree" style="background: #f7f8f9;">
                            <ul id="group_tree" class="ztree">

                            </ul>
                        </div>
                    </div>
                    <div class="list" style="padding-top:0;">
                        <table class="table">
                            <thead>
                            <tr>
                                <th width="180">单位名称</th>
                                <th width="80">学员数量</th>
                                <th width="80">培训人数</th>
                                <th width="120">培训率</th>
                                <%--<th width="90">学时达标率</th>--%>
                                <th width="100">累计学时(小时)</th>
                                <th width="100">人均学时(小时)</th>
                                <th width="80">培训人次</th>
                                <th width="100">人均培训人次</th>
                            </tr>
                            </thead>
                            <tbody id="supervise_table">

                            </tbody>

                        </table>
                        <%--分页在这里--%>
                        <div class="page text-right" style="margin-bottom:10px;position: relative;">
                            <ul id="supervise_page"></ul>
                        </div>
                        <div class="radio-group text-center">
                            <%--<label for="c"><input type="radio" name="typeName" value="column" id="c" checked>柱状图</label>--%>
                            <%--<label for="a"><input type="radio" name="typeName" value="line" id="a"> 折线图</label>--%>
                            <%--<label for="b"><input type="radio" name="typeName" value="spline" id="b"> 曲线图</label>--%>
                            <%--<label for="d"><input type="radio" name="typeName" value="bar" id="d"> 条形图</label>--%>
                            <%--<label for="e"><input type="radio" name="typeName" value="scatter" id="e"> 点状图</label>--%>
                            <%--&lt;%&ndash;<label for="f"><input type="radio" name="typeName" value="pie" id="f"> 饼状图</label>&ndash;%&gt;--%>
                            <%--<label for="g"><input type="radio" name="typeName" value="area" id="g"> 面积图</label>--%>

                            <label class="active" for="a"><span></span><input type="radio" name="valueName" title="人均学时(小时)" value="0" id="a" checked>人均学时</label>
                            <label for="b"><span class="mark-1"></span><input type="radio" name="valueName" title="累计学时(小时)" value="1" id="b">累计学时</label>
                            <label for="c"><span class="mark-2"></span><input type="radio" name="valueName" title="培训人次(人次)" value="2" id="c">培训人次</label>
                        </div>
                        <div style="margin-top:10px;" id="container">

                        </div>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- 右键菜单 -->
<%--<div id="rMenu" class="ztree_sub">
    <ul>
        <li id="m_add" onclick="supervise.addNode()">添加监管单位</li>
        <li id="m_del" onclick="supervise.removeNode()">解除监管关系</li>
        <li id="m_moveup" onclick="supervise.moveUp();">上移</li>
        <li id="m_movedown" onclick="supervise.moveDown();">下移</li>
        <li id="m_upgrade" onclick="supervise.upgrade();">升级</li>
        <li id="m_degrade" onclick="supervise.degrade();">降级</li>
    </ul>
</div>--%>
<div class="load"></div>
<!-- 隐藏字段 -->
<jsp:include page="${path}/common/footer"/>
<jsp:include page="${path}/common/script"/>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts/highcharts-3d.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts/exporting.js"></script>
<script src="<%=resourcePath%>static/global/js/admin/deptSupervise/student_highcharts.js"></script>
<script src="<%=resourcePath%>static/global/js/page.js"></script>
<script src="<%=resourcePath%>static/global/js/admin/deptSupervise/student_supervise.js"></script>
<script src="<%=resourcePath%>static/global/js/common/groupTree.js"></script>

</body>
<script type="text/javascript">

    groupTree.tree();

//    var rMenu = $("#rMenu");
    dept_supervise.initMethod();

    function groupTree_search(treeNode) {
        dept_supervise.click(treeNode);
    }

    function groupTree_init(treeNode){
        dept_supervise.click(treeNode);
    }

    function setH() {
        var mainH = $('.main').height() - 80;
        $('#height').css('height', mainH);
    }
</script>
</html>

