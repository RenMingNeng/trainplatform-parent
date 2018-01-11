<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="cn" >
<head>
    <title>考试</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/exercise.css" rel="stylesheet" type="text/css">
    <style>
        .nav{display: none;}
        #time{text-align: left;}
    </style>
</head>
<body onbeforeunload="isClose();" class="no_select">
<jsp:include page="${path}/student/menu" />
<div class="main area clearfix" style="min-height:700px;">
    <div class="main_left">
        <dl class="down_time">
            <dt style="text-align: left;padding:8px;font-size:18px;"><i class="fa fa-clock-o"></i>&nbsp;<span id="time">正在计算时间...</span></dt>
            <dd><label for="toggle"><input type="checkbox" id="toggle">单题模式</label></dd>
            <dd style="padding:10px;"><a href="javascript:;" class="hand_exam_btn" id="subPaper" onclick="question.submitExam(this);"><span></span>我要交卷</a></dd>
            <dd class="project_info" id="project_info">
                本次考试共100题，满分100分，其中单选题60道，每题1分，多选题20道，每题1分，判断题20道，每题1分
            </dd>
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
        <input type="hidden" id="exam_no" value="${param.exam_no}">
        <input type="hidden" id="project_id" value="${param.project_id}">
        <input type="hidden" id="role_id" value="${param.role_id}">

        <%--题量为0时--%>


    </div>
</div>

<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/messenger.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
<script src="<%=resourcePath%>static/global/js/common/storage.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>

<script src="<%=resourcePath%>static/global/js/common/store_exam.js"></script>
<script src="<%=resourcePath%>static/global/js/common/strategy_info.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_exam_ajax.js"></script>
<script src="<%=resourcePath%>static/global/js/keeplive.js"></script>

<script>
    $(function () {
        common.question_messenger_init();

        question_exam_ajax.initMethod('question_body', '${param.exam_no}', '${project_id}');

        var intExamDuration = '${time}';
        if(intExamDuration == ''){
            layer.open({
                type:1,
                title: false, // 不显示标题栏
                closeBtn: false,
                area: "400px",
                shade: 0.3,
                id: "layer-exam-paper-fail", // 设置id, 防止重复弹出
                resize: false,
                btn:["关闭"],
                btnAlign: "c",
                moveType: 1,
                content: "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300;font-size: 15px;'>正在生成考卷中，请稍等...&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' style='color:#69e146;' onclick='window.location.reload();'>重试</a></div>",
                yes: function(layero) {
                    window.close();
                }
            });
            return ;
        }
        //初始化
//        var allTime = Number(intExamDuration)*60 - 1;

        var exam_no = '${param.exam_no}';
        //判断考试时间是否在cookie中
        if(getCookie(exam_no + "_time") == undefined){
            question.examTime = 0;
        }else{
            var examTime = getCookie(exam_no + "_time");
            question.examTime = examTime;
        }
        question.allTime = Number(intExamDuration)*60;
        //初始化
        question.clearInterval = window.setInterval(question.setInterval, 1000);
        question.exam_no = exam_no;
        question.init();
        question_basic.init();

        strategy_info.getStrategyInfo('${project_id}', "project_info");
    });

    function isClose() {
        setCookie('messenger_parent_msg', 'question_refresh');
//        return "isClose";
    }

</script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
<script>
    $(function () {
        store_exam.init('${param.exam_no}');
    });
</script>
</body>
</html>