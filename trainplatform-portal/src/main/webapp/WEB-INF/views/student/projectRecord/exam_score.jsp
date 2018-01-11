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
    <title>博晟 | 培训平台-学员-考试记录</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <div id="addr">
            <a href="javascript:;" onclick="exam_score.index()">首页</a> &gt;
            <c:if test="${projectTypeName != '3'}">
                <a href="javascript:;" onclick="exam_score.record()" >查看记录</a>&gt;
            </c:if>
            <a href="javascript:;">考试记录</a>
        </div>
        <!-- 考试信息列表 -->
        <div class="box">
            <div class="box-body">
                <form id="exam_score_form">
                    <div class="clearfix search-group">
                        <div class="pull-left">
                            <label>考试类型：</label>
                            <select id="examType" name="examType">
                                <option value="">全部</option>
                                <option value="1">模拟考试</option>
                                <option value="2">正式考试</option>
                            </select>
                            <label>状态：</label>
                            <select id="isPassed" name="isPassed">
                                <option value="">全部</option>
                                <option value="1">合格</option>
                                <option value="2">不合格</option>
                            </select>
                        </div>
                        <div class="pull-right">
                            <div class="search">
                                <input type="hidden" id="projectId" name="projectId" value="${projectId}">   <!--项目id-->
                                <input type="hidden" id="userId" name="userId" value="${userId}">     <!--用户id-->
                                <button type="button" title="搜索" >
                                    <span class="fa fa-search" id="exam_search"></span>
                                </button>
                            </div>
                            <a href="javascript:;" id="exam_all">全部</a>
                        </div>
                    </div>
                </form>
                <!-- 表格 -->
                <table class="table pro_msg project_enter_table">
                    <thead>
                    <tr>
                        <th class="exam_num">序号</th>
                        <th class="exam_score">考试成绩</th>
                        <th class="exam_state">考试状态</th>
                        <th class="exam_type">考试类型</th>
                        <th class="exam_time">考试开始时间</th>
                        <th class="exam_duration">考试时长</th>
                        <th>操作</th>
                    </tr>
                    </thead>
                    <tbody id="exam_score">
                    </tbody>
                </table>
                <%--
                    text-left、text-center、text-right
                    分别让分页版块左、中、右
                  --%>
                <div class="page text-right" >
                    <ul id="exam_score_page"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!--隐藏字段-->
<input type="hidden" id="projectType" name="projectTypeName" value="${projectTypeName}">     <!--项目类型名称-->

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/projectRecord/exam_score.js"></script>
<script>
    var page = new page();
  $(function(){
    var basePath = "<%=basePath%>";
    exam_score.init(basePath, page);
  })
</script>
</body>
</html>
