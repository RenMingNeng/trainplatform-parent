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

  <title>博晟 | 培训平台-管理员-首页</title>

  <!-- BEGIN GLOBAL STYLES -->
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
  <!-- END GLOBAL STYLES -->
</head>

<body>
<jsp:include page="${path}/admin/menu" >
  <jsp:param name="menu" value="dossier"></jsp:param>
</jsp:include>

<div id="main">
  <div class="area">
    <%--<div id="addr">
      <div id="menu_li">
        <ul>
          <li>项目管理</li>
        </ul>
      </div>
      <div class="area">
        <a href="<%=resourcePath%>admin/dossier/project">项目档案</a>
      </div>
    </div>--%>
    <!-- 题库练习列表 -->
    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">项目档案</h3>
        <form id="project_form">
        <div class="pull-right search-group">
          <span>
            <label>项目开始时间</label>
              <input type="text" name="project_start_time" id="project_start_time" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,maxDate:'#F{$dp.$D(\'project_end_time\')}'})">
          </span>
          <span>
            <label>项目结束时间</label>
              <input type="text" name="project_end_time" id="project_end_time" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'project_start_time\')}'})">
          </span>
          <span class="search">
                <input type="text" placeholder="请输入项目名称" name="project_name">
                <button type="button" title="搜索" onclick="project_dossier.search();" class="btn">
                  <span class="fa fa-search"></span> 搜索
                </button>
          </span>
          <span>
            <a href="javascript:;" class="btn all" onclick="project_dossier.searchAll();"><span class="fa fa-th"></span> 全部</a>
          </span>
        </div>
          <input type="hidden" name="project_type">
        </form>
      </div>
      <div class="box-body">
<%--        <form id="project_form">
          <div class="clearfix search-group">
            <div class="pull-left">
              <label>项目开始时间</label>
              <input type="text" name="project_start_time" id="project_start_time" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,maxDate:'#F{$dp.$D(\'project_end_time\')}'})">
              &nbsp;&nbsp;&nbsp;&nbsp;
              <label>项目结束时间</label>
              <input type="text" name="project_end_time" id="project_end_time" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'project_start_time\')}'})">
            </div>
            <div class="pull-right">
              <label>项目名称</label>
              <div class="search clear">
                <input type="text" placehodler="请输入关键字" name="project_name">
                <button type="button" title="搜索" onclick="project_dossier.search();">
                  <span class="fa fa-search"></span>
                </button>
              </div>
              <a href="javascript:;" onclick="project_dossier.searchAll();">全部</a>
            </div>
          </div>
          <input type="hidden" name="project_type">

        </form>--%>
        <div class="status_flag">
          <dl>
            <%--<dt>项目类型:</dt>--%>
            <dd class="clearfix" id="click_project_type">
              <span class="active">全部</span>
              <c:forEach items="${project_type}" var="t">
                <span data-value="${t.value}">${t.name}</span>
              </c:forEach>
            </dd>
          </dl>
        </div>
        <!-- 表格 -->
        <table class="table pro_msg admin_index_table">
          <thead>
          <tr>
            <th width="60">序号</th>
            <th>项目名称</th>
            <th width="150">项目类别</th>
            <th width="150">项目时间</th>
            <th width="100">人数</th>
            <%--<th width="50">来源</th>--%>
            <th width="150">创建人</th>
            <th width="150">创建时间</th>
            <th width="150">操作</th>
          </tr>
          </thead>

          <%--列表--%>
          <tbody id="project_table">

          </tbody>
        </table>



      </div>
      <%-- 分页--%>
      <div class="page text-right" >
        <ul id="project_page"></ul>
      </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/dossier/project_index.js"></script>
<script>
    var page = new page();
    project_dossier.init(page);
</script>
</body>

</html>

