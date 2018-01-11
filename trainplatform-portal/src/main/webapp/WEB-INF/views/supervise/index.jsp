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
<jsp:include page="${path}/supervise/menu">
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>

<div id="main">

    <div class="area">
        <div class="box" style="padding:0;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">单位信息</h3>
                <form id="superviseForm">
                    <input type="hidden" id="companyId" name="companyId">
                    <input type="hidden" id="companyName" name="companyName">
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
                            <input type="text" id="searchName" name="searchName" placeholder="请输入公司名称">
                            <button type="button" title="搜索" onclick="supervise.search()" class="btn">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:supervise.searchAll()" class="all btn"> <span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                </form>

            </div>
            <div class="box-body" style="padding:0;">
                <div class="clearfix tree_list">
                    <div class="pull-left tree" style="background: #f7f8f9; height:637px;overflow: auto;">
                        <div>
                            <ul id="supervise_tree_id" class="ztree">

                            </ul>
                        </div>
                    </div>
                    <div class="list" style="padding-top:0;">
                        <table class="table">
                            <thead>
                            <tr>
                                <th class="text-left" width="180">单位名称</th>
                                <th width="80">学员数量</th>
                                <th width="80">培训人数</th>
                                <th width="60">培训率</th>
                                <th width="90">学时达标率</th>
                                <th width="85">累计学时(h)</th>
                                <th width="85">人均学时(h)</th>
                                <th width="80">培训人次</th>
                                <th width="120">人均培训人次</th>
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
                            <label class="active" for="a"><input type="radio" name="valueName" title="人均学时(小时)" value="0" id="a" checked><span></span>人均学时</label>
                            <label for="b"><input type="radio" name="valueName" title="累计学时(小时)" value="1" id="b"><span class="mark-1"></span>累计学时</label>
                            <label for="c"><input type="radio" name="valueName" title="培训人次(人次)" value="2" id="c"><span class="mark-2"></span>培训人次</label>
                        </div>
                        <div style="margin-top:10px;margin:0 auto;" id="container">

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
        <li id="m_add" onclick="supervise.addNode()">添加监管单位</li>
        <li id="m_del" onclick="supervise.removeNode()">解除监管关系</li>
        <li id="m_moveup" onclick="supervise.moveUp();">上移</li>
        <li id="m_movedown" onclick="supervise.moveDown();">下移</li>
        <li id="m_upgrade" onclick="supervise.upgrade();">升级</li>
        <li id="m_degrade" onclick="supervise.degrade();">降级</li>
    </ul>
</div>
<div class="load"></div>
<!-- 隐藏字段 -->
<jsp:include page="${path}/common/footer"/>
<jsp:include page="${path}/common/script"/>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts/highcharts.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts/highcharts-3d.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts/exporting.js"></script>
<script src="<%=resourcePath%>static/global/js/supervise/highcharts.js"></script>
<script src="<%=resourcePath%>static/global/js/page.js"></script>
<script src="<%=resourcePath%>static/global/js/supervise/supervise.js"></script>

</body>
<script type="text/javascript">
    var rMenu = $("#rMenu");
    supervise.initMethod(rMenu);

    function setH() {
        var mainH = $('.main').height() - 80;
        $('#height').css('height', mainH);
    }
</script>
</html>

