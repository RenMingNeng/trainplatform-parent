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
    <title>博晟 | 培训平台-管理员-查看培训进度</title>
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
    <div class="area">
        <%--<div id="addr" class="clearfix">
            <div class="pull-right">
                <a href="javascript:;" class="btn btn-info"  id="refresh"><span class="fa fa-refresh"></span>刷新</a>
                <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span>返回</a>
            </div>
        </div>--%>
            <div class="clearfix" style="line-height:36px;margin-top:20px;margin-bottom:-10px;">
                <div class="pull-left project_title">项目名称：${projectName}</div>
                <div class="pull-right search-group">
                    <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>
        <!-- 培训项目总进度 -->
        <c:if test="${permissionTrain}">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">培训项目总进度：${trainProjectProcessMap.trainProcess_percent}</h3>

                </div>
                <div class="table-outer">
                    <table class="table pro_msg train_user_table">
                        <thead>
                        <tr>
                            <th>区间</th>
                            <th>进度=0%</th>
                            <th>0%&lt;进度&lt;=50%</th>
                            <th>50%&lt;进度&lt;100%</th>
                            <th>进度=100%</th>
                            <th>合计</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>人数（人）</td>
                            <td>${trainProjectProcessMap.trainProcess_0_count}</td>
                            <td>${trainProjectProcessMap.trainProcess_0_50_count}</td>
                            <td>${trainProjectProcessMap.trainProcess_50_100_count}</td>
                            <td>${trainProjectProcessMap.trainProcess_100_count}</td>
                            <td>${trainProjectProcessMap.trainProcess_sum_count}人</td>
                        </tr>
                        <tr>
                            <td>百分比</td>
                            <td>${trainProjectProcessMap.trainProcess_0_percent}</td>
                            <td>${trainProjectProcessMap.trainProcess_0_50_percent}</td>
                            <td>${trainProjectProcessMap.trainProcess_50_100_percent}</td>
                            <td>${trainProjectProcessMap.trainProcess_100_percent}</td>
                            <td>${trainProjectProcessMap.trainProcess_sum_percent}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <!-- 每个学员培训进度 -->
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">每个学员培训进度</h3>
                    <form id="select_train_form">
                        <input type="hidden" id="trainId" name="projectId" value="${projectId}">
                        <div class="pull-right search-group">
                            <%--<span>
                                <label>培训负责人 ：</label>
                                <select id="director" name ="director">
                                    <option value="">全部</option>
                                    <c:forEach var="director" items="${directorLMap}">
                                        <c:if test="${!empty director.varDirector}">
                                            <option value="${director.varDirector}">${director.varDirector}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </span>--%>
                          <%--  <span>
                                <label>受训角色 ：</label>
                                <select id="roleId" name ="roleId">
                                    <option value="">全部</option>
                                    <c:forEach var="trainRole" items="${trainRoleLMap}">
                                        <c:if test="${!empty trainRole.roleId}">
                                            <option value="${trainRole.roleId}">${trainRole.roleName}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </span>--%>
                            <span class="search">
                                <input type="hidden" name="type" value="1">
                                <input type="text"  id="userName_train" name="trainUserName" placeholder="请输入人员姓名">
                                <button type="button" id="train_search" title="搜索"  class="btn">
                                    <span class="fa fa-search" ></span> 搜索
                                </button>
                            </span>
                            <span>
                                <a href="javascript:;" id="train_all" class="btn all"><span class="fa fa-th" ></span> 全部</a>
                            </span>
                        </div>
                    </form>
                </div>
                <%--<form id="select_train_form">
                    <input type="hidden" id="trainId" name="projectId" value="${projectId}">
                    <div class="clearfix search-group">
                        <div class="pull-left">
                           &lt;%&ndash; <label>培训负责人 ：</label>
                            <select id="director" name ="director">
                                <option value="">全部</option>
                                <c:forEach var="director" items="${directorLMap}">
                                    <c:if test="${!empty director.varDirector}">
                                        <option value="${director.varDirector}">${director.varDirector}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
                            &nbsp;&nbsp;&ndash;%&gt;
                            <label>受训角色：</label>
                            <select id="roleId" name = "roleId">
                                <option value="">全部</option>
                                <c:forEach var="trainRole" items="${trainRoleLMap}">
                                    <option value="${trainRole.roleId}">${trainRole.roleName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="pull-right">
                            <label>用户名称：</label>
                            <div class="search">
                                <input type="hidden" name="type" value="1">
                                <input type="text"  id="userName_train" name="trainUserName" placehodler="请输入人员姓名">
                                <button type="button" title="搜索" >
                                    <span class="fa fa-search" id="train_search"></span>
                                </button>
                            </div>
                            <a href="javascript:;" id="train_all">全部</a>
                        </div>
                    </div>
                </form>--%>
                <!-- 表格 -->
                <table class="table pro_msg train_user_table">
                    <thead>
                    <tr>
                        <th width="50">序号</th>
                        <th width="150">姓名</th>
                        <th width="150">手机号</th>
                        <%--<th>培训负责人</th>--%>
                        <%--<th width="120">受训角色</th>--%>
                        <th >部门信息</th>
                        <th width="100">应修学时</th>
                        <th width="100">已修学时</th>
                        <th width="100">进度</th>
                        <th width="120">操作</th>
                    </tr>
                    </thead>
                    <tbody id="project_train">

                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:0;">
                    <ul id="project_train_page"></ul>
                </div>
            </div>
        </c:if>
        <!-- 练习排行列表 -->
        <c:if test="${permissionExercise}">
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">练习排行情况</h3>
                    <form id="select_exercise_form">
                    <div class="pull-right search-group">
                        <input type="hidden" id="exerciseId" name="projectId" value="${projectId}">
                        <span class="search">
                            <input type="hidden" id="orderType" name="orderType" placehodler="1">
                            <input type="text" id="userName_exercise" name="userName" placeholder="请输入人员姓名">
                            <button type="button" id="exercise_search" title="搜索" class="btn">
                                <span class="fa fa-search" ></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" id="exercise_all" class="btn all"><span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                    </form>
                </div>
                <div class="status_flag">
                    <dl>
                        <dd class="clearfix">
                            <span id="sort1"  class="active status" onclick="proProcess.sort('1')"  >按答题学时排行</span>
                            <span id="sort2" class="status"  onclick="proProcess.sort('2')"  >按答题量排行</span>
                            <span id="sort3" class="status"  onclick="proProcess.sort('3')"  >按答题正确率排行</span>
                        </dd>
                    </dl>
                </div>
                <%--<form id="select_exercise_form">
                    <input type="hidden" id="exerciseId" name="projectId" value="${projectId}">
                    <div class="clearfix search-group">
                        <div class="pull-left">
                            <a id="sort1" href="javascript:;" onclick="proProcess.sort('1')"  class="btn btn-info"><span>按答题学时排行</span></a>
                            <a id="sort2" href="javascript:;" onclick="proProcess.sort('2')"  class="btn"><span>按答题量排行</span></a>
                            <a id="sort3" href="javascript:;" onclick="proProcess.sort('3')"  class="btn"><span>按答题正确率排行</span></a>
                        </div>
                        <div class="pull-right">
                            <label>用户名称：</label>
                            <div class="search">
                                <input type="hidden" id="orderType" name="orderType" placehodler="1">
                                <input type="text" id="userName_exercise" name="userName" placehodler="请输入人员姓名">
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
                        <th>姓名</th>
                       <%-- <th>受训角色</th>--%>
                        <th>部门信息</th>
                        <th>总题量</th>
                        <th>已答题量</th>
                        <th>答对题量</th>
                        <th>答错题量</th>
                        <th>答题正确率</th>
                        <th>答题学时</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="project_exercise">

                    </tbody>
                </table>
                <div class="page text-right" >
                    <ul id="project_exercise_page"></ul>
                </div>
            </div>
        </c:if>
        <!-- 考试情况 -->
        <c:if test="${permissionExam}">
            <div class="box">
                <div class="box-header">
                    <h3 class="box-title">考试总体情况</h3>
                </div>
                <div class="table-outer">
                    <table class="table pro_msg train_user_table">
                        <thead>
                        <tr>
                            <th>项目总人数</th>
                            <th>已考人数</th>
                            <th>参考率</th>
                            <th>考试合格人数</th>
                            <th>考试合格率</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>${examProcessMap.userCount}</td>
                            <td>${examProcessMap.examCount}</td>
                            <td>${examProcessMap.examPercent}</td>
                            <td>${examProcessMap.passCount}</td>
                            <td>${examProcessMap.passPercent}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="box">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">考试成绩列表</h3>
                    <form id="select_exam_form">
                    <div class="pull-right search-group">
                        <input type="hidden" id="examId" name="projectId" value="${projectId}">
                        <span class="search">
                            <input type="text" id="userName_exam" name="examUserName" placeholder="请输入人员姓名">
                            <button type="button" id="exam_search" class="btn" title="搜索" >
                                <span class="fa fa-search" ></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a class="btn all" href="javascript:;" id="exam_all"><span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                    </form>
                </div>
                <%--<form id="select_exam_form">
                    <input type="hidden" id="examId" name="projectId" value="${projectId}">
                    <div class="clearfix search-group">
                        <div class="pull-right">
                            <label>用户名称：</label>
                            <div class="search">
                                <input type="text" id="userName_exam" name="examUserName" placehodler="请输入人员姓名">
                                <button type="button" title="搜索" >
                                    <span class="fa fa-search" id="exam_search"></span>
                                </button>
                            </div>
                            <a href="javascript:;" id="exam_all">全部</a>
                        </div>
                    </div>
                </form>--%>
                <!-- 表格 -->
                <table class="table pro_msg project_exam_table">
                    <thead>
                    <tr>
                        <th width="50">序号</th>
                        <th>姓名</th>
                       <%-- <th>受训角色</th>--%>
                        <th>部门信息</th>
                        <th>成绩</th>
                        <th>状态</th>
                        <th width="100">操作</th>
                    </tr>
                    </thead>
                    <tbody id="project_exam">

                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:0;">
                    <ul id="project_exam_page"></ul>
                </div>
            </div>
        </c:if>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" value="${projectId}">
<input type="hidden" id="projectType" value="${projectType}">
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/page2.js"></script>
<script src="<%=resourcePath%>static/global/js/admin/proManager/proProcess.js"></script>
<script>
  var page = new page();
  var page1 = new page1()
  var page2 = new page2()
    $(function(){
        var basePath = "<%=basePath%>";
        var projectType=$("#projectType").val();
      proProcess.init(basePath, page, page1, page2, projectType);
    });
</script>
</body>
</html>