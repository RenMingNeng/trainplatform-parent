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
        <form id="study_self_info_form">
          <div class="pull-left search-group">

            <span style="padding-left: 40px;font-size: 18px;">年度自学学时：${yearStudySelf}h&nbsp;&nbsp;&nbsp;&nbsp;累计自学学时：${totalStudySelf}h</span>
          </div>
        <div class="pull-right search-group">
          <span class="search">
                <input id="courseName" type="text" placeholder="请输入课程名称" name="course_name">
                <input type="hidden" name="userId" value="${userId}">
                <button type="button" title="搜索" class="btn" onclick="study_self_info.search();">
                  <span class="fa fa-search"></span> 搜索
                </button>
          </span>
          <span>
              <a href="javascript:;" class="btn all" onclick="study_self_info.searchAll();"><span class="fa fa-th"></span> 全部</a>
          </span>
          <span>
             <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
             </span>
        </div>
        </form>
      </div>
      <div class="box-body">
        <!-- 表格 -->
        <table class="table pro_msg admin_index_table">
          <thead>
          <tr>
            <th>序号</th>
            <th>课程名称</th>
            <th>课时/分</th>
            <th>已学学时/时</th>
          </tr>
          </thead>

          <%--列表--%>
          <tbody id="study_self_info_table">

          </tbody>
        </table>



      </div>
      <%-- 分页--%>
      <div class="page text-right" >
        <ul id="study_self_info_page"></ul>
      </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/dossier/study_self_info.js"></script>
<script>
    var page = new page();
    study_self_info.init(page);
</script>
</body>

</html>

