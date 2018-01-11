<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>课程题库预览</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/exercise.css" rel="stylesheet" type="text/css">
</head>
<body onbeforeunload="question.checkLeave();" class="no_select">
<jsp:include page="${path}/student/menu" />

<div class="main area clearfix" >
    <div class="main_left">
        <dl class="down_time">
            <dt><i class="fa"></i>操作</dt>
            <dd><a href="javascript:;" onclick="window.close();" class="warn_info">关闭</a></dd>
        </dl>
        <dl class="card">
            <dt class="text-16">答题卡 <i class="fa fa-angle-double-up"></i></dt>
            <dd>
                <ul class="clearfix" id="card_list">
                </ul>
            </dd>
        </dl>
        <%--<dl class="card_info">--%>
            <%--<dt>选框说明：</dt>--%>
            <%--<dd class="current"><i></i>当前</dd>--%>
            <%--<dd class="finish"><i></i>已答</dd>--%>
        <%--</dl>--%>
    </div>
    <div class="main_right" id="question_body">
        <h2>
            <c:if test="${!empty title}">
                《${title}》
            </c:if>
        </h2>

    </div>
</div>
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/question/question_view_ajax.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question.js"></script>
<script>
    $(function () {
        //初始化
        question.init();
        question_basic.init();

        question_view_ajax.initMethod('question_body', '${param.course_id}');
    })
</script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
</body>