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
<jsp:include page="${path}/student/menu">
  <jsp:param name="menu" value="stuarchive"></jsp:param>
</jsp:include>

<div id="main">
  <div class="area">

    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">自学记录</h3>
        <form id="study_self_info_form">
          <div class="pull-right search-group">
          <span class="search">
                <input id="courseName" type="text" placeholder="请输入课程名称" name="course_name">
                <input type="hidden" name="userId" value="${userId}">
                <button type="button" title="搜索" class="btn" onclick="student_info.searchSelf();">
                  <span class="fa fa-search"></span> 搜索
                </button>
          </span>
            <span>
              <a href="javascript:;" class="btn all" onclick="student_info.searchAllSelf();"><span class="fa fa-th"></span> 全部</a>
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

      <!-- 题库练习列表 -->
      <div class="box">
        <div class="box-header">
          <h3 class="box-title pull-left">项目记录</h3>
          <form id="person_info_form">
            <div class="pull-right search-group">
              <span>
                <label>开始时间</label>
                <input type="text" name="project_start_time" id="project_start_time" class="Wdate"
                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,maxDate:'#F{$dp.$D(\'project_end_time\')}'})">
              </span>
              <span>
                <label>结束时间</label>
                <input type="text" name="project_end_time" id="project_end_time" class="Wdate"
                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'project_start_time\')}'})">
              </span>
              <span class="search">
                <input type="text" placeholder="请输入项目名称" name="project_name">
                <button type="button" title="搜索" onclick="student_info.search();" class="btn">
                  <span class="fa fa-search"></span> 搜索
                </button>
              </span>
              <span>
                <a href="javascript:;" class="btn all" onclick="student_info.searchAll();"><span class="fa fa-search"></span> 全部</a>
              </span>
              <span>
           <%-- <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>--%>
          </span>
            </div>
            <input type="hidden" name="user_id" value="${userId}">
            <input type="hidden" name="userType" value="${userType}">
            <input type="hidden" name="train_status" value="">
          </form>
        </div>
        <div class="box-body">
          <div class="status_flag">
            <dl>
             <%-- <dt>培训状态:</dt>--%>
              <dd class="clearfix" id="click-train_status">
                <span class="active">全部</span>
                <span data-value="1">未完成</span>
                <span data-value="2">完成</span>
              </dd>
            </dl>
          </div>
          <!-- 表格 -->
          <table class="table pro_msg admin_index_table">
            <thead>
            <tr>
              <th width="60">序号</th>
              <th class="text-left">项目名称</th>
              <th width="160">项目时间</th>
              <th width="60">应修<br>学时</th>
              <th width="60">已修<br>学时</th>
              <th width="60">完成<br>状态</th>
              <th width="60">总题量</th>
              <th width="60">已答<br>题量</th>
              <th width="60">答对<br>题量</th>
              <th width="60">答题<br>正确率</th>
              <th width="60">答题<br>学时</th>
              <th width="60">考试<br>成绩</th>
              <th width="60">考试<br>状态</th>
              <th width="180">操作</th>
            </tr>
            </thead>

          <tbody id="person_info_table">

          </tbody>
        </table>



      </div>
        <%-- 分页--%>
        <div class="page text-right" >
          <ul id="person_info_page"></ul>
        </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script src="<%=resourcePath%>static/global/js/student/stuDossier/student_info.js"></script>
<script type="text/javascript">
    var page1 = new page1();
    var page = new page();
    student_info.init(page1,page);
</script>
</body>

</html>

