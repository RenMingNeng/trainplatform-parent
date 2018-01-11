<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.bossien.train.util.PropertiesUtils" %>
<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";

    //含有考试项目的
    String containExamType= PropertiesUtils.getValue("contain_exam_type");
    //含有培训项目的
    String containTrainType=PropertiesUtils.getValue("contain_train_type");
    //含有练习项目的
    String containExerciseType=PropertiesUtils.getValue("contain_exercise_type");
    //单独考试项目
    String examType= ProjectTypeEnum.QuestionType_3.getValue();
%>
<!DOCTYPE html>
<html>
<head>
    <title>创建公开型项目</title>
    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />

    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
</head>
<body>
<jsp:include page="${path}/super/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>

<c:set var="containExamType" value="<%=containExamType%>" ></c:set>
<c:set var="containTrainType" value="<%=containTrainType%>" ></c:set>
<c:set var="containExerciseType" value="<%=containExerciseType%>" ></c:set>
<c:set var="examType" value="<%=examType%>" ></c:set>

<div id="main">
    <div class="area">
        <div class="box index">
            <div class="box-header clearfix">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">高级设置</h3>
                </div>
                <form id="course_form">
                    <h3 class="pull-left">学时要求<%--/必选题量--%></h3>
                    <div class="pull-right search-group">
                        <span class="search">
                        <input type="text" id="course_name" name="course_name" placehodler="请输入课程名称">
                        <button type="button" title="搜索" class="btn" id="btn_search_course">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                    </span>
                        <span>
                        <a href="javascript:;" id="btn_all_course" class="btn all">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </span>
                    </div>
                    <input type="hidden" id="course_project_id" name="course_project_id" value="${projectId}">
                </form>
            </div>

            <div class="box-body">
                <table class="table">
                    <thead>
                    <tr >
                        <th>序号</th>
                        <th>课程名称</th>
                        <th>课程编号</th>
                        <c:if test="${fn:contains(containTrainType, projectTypeNo)}">
                            <th>课时</th>
                            <th>学时要求</th>
                        </c:if>
                        <c:if test="${fn:contains(containExamType, projectTypeNo) || fn:contains(containExerciseType, projectTypeNo) }">
                            <th>题库总量</th>
                        </c:if>
                        <%--<c:if test="${fn:contains(containExamType, projectTypeNo) }">--%>
                            <%--<th>必选题量</th>--%>
                        <%--</c:if>--%>
                    </tr>
                    </thead>
                    <%--课程列表--%>
                    <tbody id="course_list">
                    <tr>
                        <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                    </tbody>
                </table>
                <div class="page text-right"  style="margin-top:0px;">
                    <ul id="course_page"></ul>
                </div>

            </div>
        </div>
        <div class="search-group clearfix" style="margin-top:-10px;margin-bottom:20px;">
            <span style="float: right;">
                <a href="javascript:;" class="btn btn-info " id="index"><span class="fa fa-home"></span> 回到首页</a>&nbsp;&nbsp;
                <a href="javascript:;" class="btn btn-info" id="step_3"><span class="fa  fa-long-arrow-left"></span> 上一步</a>&nbsp;&nbsp;
                <c:choose>
                    <c:when test="${fn:contains(containExamType, projectTypeNo) }">
                        <a href="javascript:;" class="btn btn-info" id="step_5"><span class="fa fa-long-arrow-right"></span> 下一步</a>
                    </c:when>
                    <c:otherwise>
                        <%--<a class="btn btn-info" id="publish_1"><span class="fa fa-paper-plane"></span> 发布</a>--%>
                        <a href="javascript:;" class="btn btn-info" id="btn_save"><span class="fa fa fa-save"></span> 保 存</a>
                    </c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="id" name="id" value="${projectId}">
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectStatus}">
<jsp:include page="${path}/common/footer" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/createProject/step_4.js"></script>
<script type="text/javascript">
    var page = new page();
    var options={
        'containExamType':"<%=containExamType%>",
        'containTrainType':"<%=containTrainType%>",
        'containExerciseType':"<%=containExerciseType%>",
        'examType':"<%=examType%>"
    }
    step_4.init(page,options);
</script>
</body>
</html>
