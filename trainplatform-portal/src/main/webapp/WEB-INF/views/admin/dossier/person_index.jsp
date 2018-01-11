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

    <!-- 题库练习列表 -->
    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">人员档案</h3>
        <form id="person_form">
          <input type="hidden" name="role_id">
          <input type="hidden" id="companyId" name="companyId" value="${companyId}">
          <input type="hidden" id="departmentId" name="departmentId" value="">
          <input type="hidden" id="companyIds" name="companyIds" value="">
          <input type="hidden" id="deptIds" name="deptIds" value="">
          <input type="hidden" id="departmentName" name="departmentName" value="">
        <div class="pull-right search-group">
          <span>
            <a href="javascript:person_dossier.export()" class="btn btn-info"><span class="fa fa-sign-out"></span> 导出</a>
          </span>
          <span class="search">
            <%--<input type="text" placeholder="请输入受训角色名称" name="role_name" style="margin-right:10px;">--%>
            <input type="text" placeholder="请输入人员名称" name="user_name">
            <button type="button" title="搜索" class="btn" onclick="person_dossier.search();">
              <span class="fa fa-search"></span> 搜索
            </button>
          </span>
          <span>
             <a href="javascript:;" class="btn all" onclick="person_dossier.searchAll();"><span class="fa fa-th"></span> 全部</a>
          </span>
        </div>
        </form>
      </div>

      <div>
        <input type="checkbox" id="recursion" onclick="person_dossier.search()" style="vertical-align: middle;margin-top:-2px;margin-left:10px;">显示子节点人员
      </div>

      <div class="box-body tree_list">
        <!--左侧树-->
        <div class="tree" style="background: #f7f8f9;">
            <div class="tree" style="background: #f7f8f9;">
              <ul id="group_tree" class="ztree"></ul>
            </div>
        </div>
        <div class="list">
          <table class="table first_table">
            <thead>
            <tr>
              <th width="60">序号</th>
              <th width="40"><input type="checkbox"  id="checkAll" value=""></th>
              <th width="140">姓名</th>
              <th>受训角色</th>
              <th width="160">部门信息</th>
              <th width="80">年度学时</th>
              <th width="80">累计学时</th>
              <th width="240">操作</th>
            </tr>
            </thead>
          </table>
          <div class="table_inner">
            <table class="table">
            <%--列表--%>
            <tbody id="person_table">

            </tbody>
          </table>
          </div>
          <%-- 分页--%>
          <div class="page text-right bg_page">
            <ul id="person_page"></ul>
          </div>
        </div>
        <!-- 表格 -->
      </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/common/groupTree.js"></script>
<script src="<%=resourcePath%>static/global/js/admin/dossier/person_index.js"></script>
<script>
    $("#recursion").attr("checked",false);
    var page = new page();
    person_dossier.init(page);
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
</body>

</html>

