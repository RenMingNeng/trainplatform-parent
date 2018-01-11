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
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
  <style>
    .ex_course_type_flush{
      display: inline-block;
      width: 80px;
      height: 32px;
      line-height: 32px;
      background: rgb(0, 173, 255);
      border-radius: 3px;
      color: white;
      cursor: pointer;
      -webkit-border-radius: 3px;
    }
  </style>
</head>

<body>
<jsp:include page="${path}/console/menu" >
  <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>


<div id="main">
  <div class="area pd_10" style="padding:10px 0">
    <div class="box" style="padding:0;">
      <form id="public_project_form">
        <div class="box-header clearfix" style="padding:20px;">
          <h3 class="box-title pull-left">重要操作</h3>
        </div>
        <div class="box-body" style="padding:0;">
          <table class="table pro_msg admin_index_table">
            <thead>
              <tr>
                <th width="20%">名称</th>
                <th width="60%">描述</th>
                <th width="20%">操作</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td><span style="color: #000">ex_course_type刷新数据</span></td>
                <td><span style="color: #177E09">发送消息给资源平台-请求刷新ex_course_type的数据-应答模式完成</span></td>
                <td><a id="ex_course_type_flush" class="ex_course_type_flush">刷新数据</a></td>
              </tr>
              <tr>
                <td><span style="color: #000">course_info刷新数据课程数量</span></td>
                <td><span style="color: #177E09">效验课程表中题库数量</span></td>
                <td><a id="quession" class="ex_course_type_flush">刷新数据</a></td>
              </tr>
              <tr>
                <td><span style="color: #000">web.properties数据维护</span></td>
                <td><span style="color: #177E09">更新web.properties中版本号</span></td>
                <td><a id="webProperties" class="ex_course_type_flush">维护</a></td>
              </tr>
              <tr>
                <td><span style="color: #000">project_course、project_course_info表字段更新</span></td>
                <td><span style="color: #177E09">project_course、project_course_info表中total_question字段更新</span></td>
                <td><input id="project_id" type="text" placeholder="请输入项目编号" style="width: 120px;">
                  <a id="updateQuestionCount" class="ex_course_type_flush">更新</a></td>
              </tr>
              <tr>
                <td><span style="color: #000">project_exercise_order、project_statistics_info表中total_question字段更新</span></td>
                <td><span style="color: #177E09">project_exercise_order、project_statistics_info表中total_question字段更新</span></td>
                <td><input id="projectId" type="text" placeholder="请输入项目编号" style="width: 120px;">
                  <a id="updateTotalQuestion" class="ex_course_type_flush">更新</a>
                </td>
              </tr>
              <tr>
                <td><span style="color: #000">project_basic、project_info表中project_status字段更新</span></td>
                <td><span style="color: #177E09">project_basic、project_info表中project_status字段更新</span></td>
                <td><input id="id" type="text" placeholder="请输入项目编号" style="width: 120px;">
                  <a id="updateProjectStatus" class="ex_course_type_flush">更新</a>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </form>
    </div>
  </div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/console/index.js"></script>
<script type="text/javascript">
  $(function () {
      console_index.init();
  })
</script>
</body>

</html>

