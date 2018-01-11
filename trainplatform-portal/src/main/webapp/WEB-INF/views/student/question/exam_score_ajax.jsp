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
    <title>考试预览</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/exercise.css" rel="stylesheet" type="text/css">
</head>
<body>
<body onbeforeunload="question.checkLeave();" class="no_select">
<jsp:include page="${path}/student/menu" />

<div class="main area clearfix">
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
        <dl class="card_info">
            <dt>选框说明：</dt>
            <%--<dd class="current"><i></i>当前</dd>
            <dd class="finish"><i></i>已答</dd>--%>
            <dd class="success"><i></i>答对</dd>
            <dd class="error"><i></i>答错</dd>
            <%--<dd class="mark"><i></i>标记</dd>--%>
        </dl>
    </div>
    <div class="main_right" id="question_body">
        <h2>
            <c:if test="${!empty title}">
                《${title}-答题记录》
            </c:if>
        </h2>

    </div>
</div>
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/question/question_score_ajax.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question.js"></script>
<script>
    $(function () {
        //初始化
        question.init();
        question_basic.init();

        question_score_ajax.initMethod('question_body', '${param.exam_no}');

        var answer_status = '${answer_status}';
        if(answer_status && answer_status == 'false'){
            var message = alertMessage("正在保存答题记录，请稍等...");
            layer.open({
                type:1,
                title: false, // 不显示标题栏
                closeBtn: false,
                area: "400px",
                shade: 0.3,
                id: "layer-exam-paper-fail", // 设置id, 防止重复弹出
                resize: false,
                btn:["确定","关闭"],
                btnAlign: "c",
                moveType: 1,
                content: message,
                btn1: function(index) {
                    layer.close(index);
                },
                btn2: function(layero) {
                    window.close();
                }
            });
        }
    });

    function alertMessage(message){
        var url = "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300;font-size: 15px;'>";
        url += message;
        url += "&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' style='color:#69e146;' onclick='window.location.reload();'>重试</a></div>";
        return url;
    };
</script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
<script>
    $(function () {
        showAnswer();
    });
</script>
</body>