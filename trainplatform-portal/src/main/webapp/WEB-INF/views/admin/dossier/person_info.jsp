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
          <a href="<%=resourcePath%>admin/personDossier">人员档案</a><span class="fa fa-angle-right"></span>
          <a href="javascript:;">${userName}的项目记录</a>
        </div>
        <div class="pull-right">
          <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
        </div>
      </div>--%>

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
                <button type="button" title="搜索" onclick="person_info.search();" class="btn">
                  <span class="fa fa-search"></span> 搜索
                </button>
              </span>
              <span>
                <a href="javascript:;" class="btn all" onclick="person_info.searchAll();"><span class="fa fa-search"></span> 全部</a>
              </span>
              <span>
            <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
          </span>
            </div>
            <input type="hidden" name="user_id" value="${param.user_id}">
            <input type="hidden" name="userType" value="${userType}">
            <input type="hidden" name="train_status">
            <%--<div class="status_flag">
              <dl>
                <dt>培训状态:</dt>
                <dd class="clearfix" id="click-train_status">
                  <span class="active">全部</span>
                  <span data-value="1">未完成</span>
                  <span data-value="2">完成</span>
                </dd>
              </dl>
            </div>--%>
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
              <th>项目名称</th>
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

            <%--列表--%>
      <%--<div class="box-header clearfix">
                <h3 class="box-title pull-left">项目记录</h3>
                <form id="person_info_form">
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
                       <input type="text" placehodler="请输入关键字" name="project_name" >
                        <button type="button" class="btn" title="搜索" onclick="person_info.search();">
                          <span class="fa fa-search"></span> 搜索
                        </button>
                  </span>
                  <span>
                    <a href="javascript:;" onclick="person_info.searchAll();" class="btn all"><span class="fa fa-th"></span> 全部</a>
                  </span>
                </div>
                  <input type="hidden" name="user_id" value="${param.user_id}">
                  <input type="hidden" name="train_status">
                </form>
                 <div class="status_flag">
                   <dl>
                     <dd class="clearfix" id="click-train_status">
                       <span class="active">全部</span>
                       <span data-value="1">未完成</span>
                       <span data-value="2">完成</span>
                     </dd>
                   </dl>
                 </div>
                    <th width="200">操作</th>--%>
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
<input type="hidden" id="containTrainType" value="${containTrainType}">
<input type="hidden" id="containExerciseType" value="${containExerciseType}">
<input type="hidden" id="containExamType" value="${containExamType}">
<script src="<%=resourcePath%>static/global/js/admin/dossier/person_info.js"></script>
<script>
    var page = new page();
    person_info.init(page);
</script>
</body>

</html>

