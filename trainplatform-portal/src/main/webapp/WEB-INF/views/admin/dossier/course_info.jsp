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
  <style>
    .nav{display: none;}
  </style>
  <!-- END GLOBAL STYLES -->
</head>

<body>
<jsp:include page="${path}/admin/menu" >
  <jsp:param name="menu" value="dossier"></jsp:param>
</jsp:include>




<div id="main">
  <div class="area">
    <%--<div id="addr" class="clearfix">
      <div class="pull-left">
        <a href="<%=resourcePath%>/admin/dossier/project">项目档案</a><span class="fa fa-angle-right"></span><a href="<%=resourcePath%>admin/personDossier">人员档案</a>
      </div>
      <div class="pull-right">
        <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
      </div>
    </div>--%>

    <!-- 题库练习列表 -->
    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">项目课程详情</h3>
        <form id="project_course_info_form">
        <div class="pull-right search-group">
          <span class="search">
                <input type="text" placeholder="请输入课程名称" name="course_name">
                <button type="button" title="搜索" class="btn" onclick="project_course_info.search();">
                  <span class="fa fa-search"></span> 搜索
                </button>
          </span>
          <span>
              <a href="javascript:;" class="btn all" onclick="project_course_info.searchAll();"><span class="fa fa-th"></span> 全部</a>
          </span>
          <span>
            <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
          </span>
        </div>
          <input type="hidden" name="user_id" value="${param.user_id}">
          <input type="hidden" name="project_id" value="${param.project_id}">
        </form>
      </div>
      <div class="box-body">
       <%-- <form id="project_course_info_form">
          <div class="clearfix search-group">
            <div class="pull-left">

            </div>
            <div class="pull-right">
              <label>课程名称</label>
              <div class="search clear">
                <input type="text" placehodler="请输入关键字" name="course_name">
                <button type="button" title="搜索" onclick="project_course_info.search();">
                  <span class="fa fa-search"></span>
                </button>
              </div>
              <a href="javascript:;" onclick="project_course_info.searchAll();">全部</a>
            </div>
          </div>
          <input type="hidden" name="user_id" value="${param.user_id}">
          <input type="hidden" name="project_id" value="${param.project_id}">
        </form>--%>
        <!-- 表格 -->
        <table class="table pro_msg admin_index_table">
          <thead>
          <tr>
            <th width="50">序号</th>
            <th>课程名称</th>
            <th width="120">应修学时</th>
            <th width="120">已修学时</th>
            <th width="120">答题学时</th>
            <th width="120">培训学时</th>
            <th width="70">总题量</th>
            <th width="70">已答题量</th>
            <th width="70">答对题量</th>
            <th width="85">答题正确率</th>
            <th width="70">完成状态</th>
          </tr>
          </thead>

          <%--列表--%>
          <tbody id="project_course_info_table">

          </tbody>
        </table>



      </div>
      <%-- 分页--%>
      <div class="page text-right" >
        <ul id="project_course_info_page"></ul>
      </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/dossier/course_info.js"></script>
<script>
    var page = new page();
    project_course_info.init(page, "${projectType}");
</script>
</body>

</html>

