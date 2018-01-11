<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <title>博晟 | 培训平台-管理员-查看练习详情</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .nav{display: none;}
    </style>
</head>
<body>
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area" style="overflow: hidden">
        <%--<div id="addr" class="clearfix">
            <div class="pull-right">
                <a href="javascript:;" class="btn btn-info"  id="refresh"><span class="fa fa-refresh"></span>刷新</a>
                <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span>返回</a>
            </div>
        </div>--%>
        <!-- 查看练习详情 -->
        <c:if test="${fn:contains('2467',projectType )}">
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">练习详情</h3>
                    <form id="select_exerciseInfo_form">
                    <div class="pull-right search-group">
                        <input type="hidden" id="projectId" name="projectId" value="${projectId}">
                        <input type="hidden" id="userId" name="userId" value="${userId}">
                        <span class="search">
                            <input type="text" id="courseName" name="courseName" placeholder="请输入课程名称">
                            <button type="button" id="exercise_search" title="搜索" class="btn">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" class="btn all" id="exercise_all"><span class="fa fa-th"></span>  全部</a>
                        </span>
                        <span>
                              <button onclick="javascript:history.go(-1);" class="btn btn-info"  id="btn_back" >
                                <span class="fa fa-reply"></span>
                                返回
                            </button>
                        </span>
                    </div>
                    </form>
                </div>
                <%--<form id="select_exerciseInfo_form">
                    <input type="hidden" id="projectId" name="projectId" value="${projectId}">
                    <input type="hidden" id="userId" name="userId" value="${userId}">
                    <div class="clearfix search-group">
                        <div class="pull-right">
                            <label>课程名称：</label>
                            <div class="search">
                                <input type="text" id="courseName" name="courseName" placehodler="请输入课程名称">
                                <button type="button" title="搜索" >
                                    <span class="fa fa-search" id="exercise_search"></span>
                                </button>
                            </div>
                            <a href="javascript:;" id="exercise_all">全部</a>
                        </div>
                    </div>
                </form>--%>
                <!-- 表格 -->
                <table class="table pro_msg project_exercise_table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>课程名称</th>
                        <th>总题量</th>
                        <th>已答题量</th>
                        <th>答对题量</th>
                        <th>答题正确率</th>
                        <th>答题学时</th>
                    </tr>
                    </thead>
                    <tbody id="exercise_Info">

                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:0;">
                    <ul id="exercise_Info_page"></ul>
                </div>
            </div>
        </c:if>

    </div>
</div>
<%--隐藏域--%>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/proManager/exerciseInfo.js"></script>
<script>
  var page = new page();
    $(function(){
        var basePath = "<%=basePath%>";
      exerciseInfo.init(basePath, page);
    });
</script>
</body>
</html>