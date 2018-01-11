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
    <title>学员_练习</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/exercise.css" rel="stylesheet" type="text/css">
</head>
<body onbeforeunload="question.checkLeave();" class="no_select">
<jsp:include page="${path}/student/menu" />

<div class="main area clearfix">
    <div class="main_left">
        <dl class="down_time">
            <dt><i class="fa"></i>操作</dt>
            <dd><label for="toggle"><input type="checkbox" id="toggle">单题模式</label></dd>
            <dd><a href="javascript:;" onclick="window.close();" class="warn_info">关闭</a></dd>
        </dl>
        <dl class="card">
            <dt class="text-16">答题卡 <i class="fa fa-angle-double-up"></i></dt>
            <dd>
                <ul class="clearfix" id="card_list">
                </ul>
            </dd>
        </dl>
        <dl class="card_info">
            <dt>选框说明：</dt>
            <dd class="current"><i></i>当前</dd>
            <dd class="finish"><i></i>已答</dd>
            <dd class="success"><i></i>答对</dd>
            <dd class="error"><i></i>答错</dd>
            <dd class="mark"><i></i>标记</dd>
        </dl>
    </div>
    <div class="main_right" id="question_body">
        <h2>
            <c:if test="${!empty title}">
                《${title}》
            </c:if>
        </h2>
        <div class="btn-group" id="btn-group" style="display: none;">
            <a href="javascript:;" class="btn btn-info" id="pre">上一题</a>
            <a href="javascript:;" class="btn btn-info" id="next">下一题</a>
        </div>



    </div>
</div>

<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/messenger.js"></script>
<script src="<%=resourcePath%>static/global/js/student/study_self/exercise_ajax.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>
<script src="<%=resourcePath%>static/global/js/student/study_self/question.js"></script>
<script src="<%=resourcePath%>static/global/js/keeplive.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
<script>
    $(function () {

        exercise_ajax.initMethod('question_body', appPath + '${url}', '-9', '${param.course_id}');

        //初始化
        question.init();
        question_basic.init();
        showAnswer();
    })
</script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
</body>