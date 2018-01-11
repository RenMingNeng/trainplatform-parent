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
    <title>博晟 | 培训平台-练习排行</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <div id="addr">
            <a href="javascript:;" onclick="exercise_order.index()" >首页</a> &gt; <a href="javascript:;" onclick="exercise_order.record()">查看记录</a>&gt; <a href="javascript:;">练习排行</a>
        </div>

        <!-- 练习排行列表 -->
        <div class="box">
            <div class="box-body">
                <form id="exercise_order_form">
                    <div class="clearfix search-group">
                        <div class="pull-left">
                            <a id="sort1" href="javascript:;" onclick="exercise_order.sort('1')"  class="btn btn-info"><span>按答题学时排行</span></a>
                            <a id="sort2" href="javascript:;" onclick="exercise_order.sort('2')"  class="btn"><span>按答题量排行</span></a>
                            <a id="sort3" href="javascript:;" onclick="exercise_order.sort('3')"  class="btn"><span>按答题正确率排行</span></a>
                        </div>
                        <div class="pull-right">
                            <label>用户名称：</label>
                            <div class="search">
                                <input type="hidden" id="projectId" name="projectId" value="${projectId}">   <!--项目id-->
                                <input type="hidden" id="userId" name="userId" value="${userId}">     <!--用户id-->
                                <input type="hidden" id="orderType" name="orderType" value="">     <!--排序类型-->
                                <input type="text" id="name_search" name="userName" placehodler="请输入人员姓名">
                                <button type="button" title="搜索" >
                                    <span class="fa fa-search" id="exercise_search"></span>
                                </button>
                            </div>
                            <a href="javascript:;" id="exercise_all">全部</a>
                        </div>
                    </div>
                </form>
                <!-- 表格 -->
                <table class="table pro_msg project_enter_table">
                    <thead>
                    <tr>
                        <th class="exercise_num">序号</th>
                        <th class="user_name">姓名</th>
                        <th class="role_name">受训角色</th>
                        <th class="dept_name">部门信息</th>
                        <th class="total_question">总题量</th>
                        <th class="yet_answered">已答题量</th>
                        <th class="not_answered">未答题量</th>
                        <th class="correct_answered">答对题量</th>
                        <th class="fail_answered">答错题量</th>
                        <th class="correct_rate">答题正确率</th>
                        <th class="answer_study_time">答题学时</th>
                    </tr>
                    </thead>
                    <tbody id = user_exercise>

                    </tbody>
                    <tbody id="exercise_order">

                    </tbody>
                </table>
                <%--
                    text-left、text-center、text-right
                    分别让分页版块左、中、右
                  --%>
                <div class="page text-right" >
                    <ul id="project_exercise_page"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!--隐藏字段-->
<input type="hidden" id="projectType" name="projectTypeName" value="${projectTypeName}">     <!--项目类型名称-->

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/projectRecord/exercise_order.js"></script>
<script>
    var page = new page();
  $(function() {
      var basePath = "<%=basePath%>";
      exercise_order.init(basePath, page);
  })
</script>
</body>
</html>
