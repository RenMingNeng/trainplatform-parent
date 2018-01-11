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
  <title>博晟 | 培训平台-学员-我的课程</title>
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
  <style>

  </style>
  <!-- END GLOBAL STYLES -->
</head>

<body>
<jsp:include page="${path}/student/menu" >
  <jsp:param name="menu" value="selfcourse"></jsp:param>
</jsp:include>

<div id="main">

  <div class="area">

    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">我的课程</h3>
        <form id="select_course_form">
        <div class="pull-right search-group">

          <span>
            <%--<a href="javascript:;" class="btn btn-info" id="course_move"><span class="fa fa-exchange"></span> 转移</a>--%>
          </span>
          <span class="search">
            <input type="hidden" id="intTypeId" name="intTypeId" value="">
            <input type="text" placeholder="请输入课程名称" name="courseName" id="courseName">
            <button type="button" title="搜索" id="courseName_search" class="btn">
              <span class="fa fa-search"></span> 搜索
            </button>
          </span>
          <span>
            <a href="javascript:;" id="course_all" class="btn all"> <span class="fa fa-th"></span> 全部</a>
          </span>

        </div>
        </form>
      </div>
      <div class="box-body  tree_list">
          <div class="tree tree-search" style="background: #f7f8f9;">
              <div class="search">
                  <span class="fa fa-search"></span>
                  <input type="text" id="searchKey" class="empty" onkeyup="treeSearch.callNumber()" placeholder="按节点名称检索"/>
                  <div id="showInfo">
                      <a id="clickUp" class="up search_btn" onclick="treeSearch.clickUp()">
                          <span class="fa  fa-angle-up"></span>
                      </a>
                      <label id="number" class="number"></label>
                      <a id="clickDown" class="down search_btn" onclick="treeSearch.clickDown()">
                          <span class="fa  fa-angle-down"></span>
                      </a>
                  </div>
              </div>
              <div>
                <ul id="course_treeId" class="ztree">
                </ul>
              </div>
          </div>
          <div class="list">

            <table class="table first_table">
              <thead>
              <tr>
                <th width="50">序号</th>
                <th width="100">课程编号</th>
                <th >课程名称</th>
               <%-- <th>受训角色</th>--%>
                <th width="80">课时/分</th>
               <%-- <th>学时要求</th>--%>
                <th width="80">题库总量</th>
                  <th width="100">自学学时/分</th>
                <th width="160">操作</th>
              </tr>
              </thead>
            </table>
              <div class="table_inner">
                <table class="table">
                <%--授权课程列表--%>
                <tbody id="select_course_list">
                   <tr>
                   <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                  </tr>
                </tbody>
              </table>
              </div>
              <div class="page text-right bg_page">
                <ul id="select_course_page"></ul>
              </div>
          </div>


      </div>

    </div>


  </div>
</div>
<input type="hidden" value="courseManager" id="courseManager">
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/student/study_self/stu_course_type.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/student/study_self/stu_course_list.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/treeSearch.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
</body>
<script>
      var page = new page();
      //加载课程分类
      course_type.iniTree();
      course_list.init(page);
      treeSearch.init("course_treeId");
      //页面刷新
      common.messenger_student_self_init();
      $(function(){
          $('.table_inner').niceScroll();
          setH();
          $(window).resize(function(){
              setTimeout(function(){
                  setH();
              },100);

          })
      });

      function setH(){
          var windowH = $(window).height();
          var navH = $('.nav').height(),
              topH = $('.header_top').height();
          $('.tree_list').css('height',windowH - navH - topH - 116 );
      }
</script>
</html>

