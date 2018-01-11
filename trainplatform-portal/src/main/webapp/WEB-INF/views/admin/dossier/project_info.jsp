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
  <style>
    /*.nav{display: none;}*/
  </style>
</head>

<body>
<jsp:include page="${path}/admin/menu" >
  <jsp:param name="menu" value="dossier"></jsp:param>
</jsp:include>

<div id="main">
  <div class="area">

  <%--  <div id="addr" class="clearfix">
      <div class="pull-left">
        <a href="<%=resourcePath%>/admin/dossier/project">项目档案</a><span class="fa fa-angle-right"></span>
        <a href="javascript:;">${projectName}的人员记录</a>
      </div>
      <div class="pull-right">
        <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
      </div>
    </div>--%>

    <!-- 题库练习列表 -->
    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">人员记录</h3>

        <form id="project_info_form">
        <div class="pull-right search-group">
          <span class="search">
           <%-- <input type="text" placeholder="请输入受训角色名称" name="role_name" style="margin-left:10px;">--%>
            <input type="text" placeholder="请输入单位名称" name="dept_name" style="margin-left:10px;">
            <input type="text" placeholder="请输入人员名称" name="user_name" style="margin-left:10px;">
            <button type="button" title="搜索" onclick="project_info.search();" class="btn">
                  <span class="fa fa-search"></span> 搜索
            </button>
          </span>
          <span>
            <a href="javascript:;" onclick="project_info.searchAll();" class="btn all"><span class="fa fa-th"></span> 全部</a>
          </span>
          <span>
            <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
          </span>
        </div>
          <input type="hidden" name="project_id" value="${param.project_id}">
          <input type="hidden" name="role_id">
        </form>
      </div>
      <div class="box-body">
        <%--<form id="project_info_form">
          <div class="clearfix search-group">
            <div class="pull-left">

            </div>
            <div class="pull-right">
              <label>受训角色名称</label>
              <div class="search clear">
                <input type="text" placehodler="请输入关键字" name="role_name">
              </div>

              <label>单位名称</label>
              <div class="search clear">
                <input type="text" placehodler="请输入关键字" name="dept_name">
              </div>

              <label>人员名称</label>
              <div class="search clear">
                <input type="text" placehodler="请输入关键字" name="user_name">
                <button type="button" title="搜索" onclick="project_info.search();">
                  <span class="fa fa-search"></span>
                </button>
              </div>
              <a href="javascript:;" onclick="project_info.searchAll();">全部</a>
            </div>
          </div>
          <input type="hidden" name="project_id" value="${param.project_id}">
          <input type="hidden" name="role_id">
          <div class="status_flag">
            &lt;%&ndash;<dl>&ndash;%&gt;
              &lt;%&ndash;<dt>受训角色:</dt>&ndash;%&gt;
              &lt;%&ndash;<dd class="clearfix" id="click_role">&ndash;%&gt;
                &lt;%&ndash;<span class="active">全部</span>&ndash;%&gt;
                &lt;%&ndash;<c:forEach items="${roles}" var="r">&ndash;%&gt;
                  &lt;%&ndash;<span data-value="${r.varId}">${r.roleName}</span>&ndash;%&gt;
                &lt;%&ndash;</c:forEach>&ndash;%&gt;
              &lt;%&ndash;</dd>&ndash;%&gt;
            &lt;%&ndash;</dl>&ndash;%&gt;
          </div>
        </form>--%>
        <!-- 表格 -->
        <table class="table pro_msg admin_index_table">
          <thead>
          <tr>
            <th width="50">序号</th>
            <th>姓名</th>
            <th>受训角色</th>
            <th width="150">部门信息</th>
            <th width="80">应修学时</th>
            <th width="80">已修学时</th>
            <th width="80">总题量</th>
            <th width="80">已答题量</th>
            <th width="80">答题正确率</th>
            <th width="80">学时排名</th>
            <th width="80">考试成绩</th>
            <th width="80">考试状态</th>
            <th width="180">操作</th>
          </tr>
          </thead>

          <%--列表--%>
          <tbody id="project_info_table">

          </tbody>
        </table>



      </div>
      <%-- 分页--%>
      <div class="page text-right" >
        <ul id="project_info_page"></ul>
      </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/dossier/project_info.js"></script>
<script>
    var page = new page();
    project_info.init(page, '${projectType}');
</script>
</body>

</html>

