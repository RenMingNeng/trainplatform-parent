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
    </style>
</head>
<body onbeforeunload="setCookie('messenger_parent_msg', 'question_refresh');" class="no_select">
<jsp:include page="${path}/student/menu" />
<div class="main area clearfix" style="min-height:700px;">
    <div class="main_left">
        <dl class="down_time">
            <dt><i class="fa fa-clock-o"></i>&nbsp;<span id="time"></span></dt>
            <dd><label for="toggle"><input type="checkbox" id="toggle">单题模式</label></dd>
            <dd><a href="javascript:;" id="subPaper" onclick="question.submitExam(this);" class="warn_info">我要交卷</a></dd>
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
            <%--<dd class="success"><i></i>答对</dd>
            <dd class="error"><i></i>答错</dd>--%>
            <dd class="mark"><i></i>标记</dd>
        </dl>
    </div>
    <div class="main_right">
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
        <c:if test="${fn:length(questions) < 1}">
            <div><div class="no-content"></div></div>
        </c:if>

        <c:forEach items="${questions}" var="question" varStatus="i">
            <c:if test="${question.chrType == '01'}">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${project_id}">
                    <div class="subject">
                        <span class="index">${i.index + 1}</span><span class="title_bg">（单选题）</span>${question.varQuestionsContent}
                        <div class="show_img clearfix">
                            <c:forEach items="${question.questionTitleFiles}" var="file">
                                <%-- 图片--%>
                                <c:if test="${file.mimeType == 'img'}">
                                    <img class="lazy" data-original="${file.mimeUrl}" alt="">
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="options"><%--选项--%>
                        <c:forEach items="${question.options}" var="q">
                            <div>
                                <div class="option" data-type="${q.oType}">
                                    <span></span>
                                    <div>${q.oType}、${q.optionContent}</div>
                                </div>
                                <div class="show_img clearfix">
                                    <c:if test="${q.mimeType == 'img'}">
                                        <img class="lazy" data-original="${q.mimeUrl}" alt="">
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="option_select clearfix" data-type="${question.chrType}">
                       <%-- <div class="pull-left">
                            <c:forEach items="${question.options}" var="q">
                                <label>
                                    <input type="radio" name="redio${i.index}" value="${q.oType}"> ${q.oType}
                                </label>
                            </c:forEach>
                        </div>--%>
                        <div class="pull-left">
                            <button type="button" class="btn btn-mark">标记</button>
                        </div>
                        <div class="pull-right">
                            <c:if test="${question.chrIsCollect == true || question.chrIsCollect == 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star on"></i> 收藏试题</a>
                            </c:if>
                            <c:if test="${question.chrIsCollect != true && question.chrIsCollect != 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> 收藏试题</a>
                            </c:if>
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${question.chrType == '02'}">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${project_id}">
                    <div class="subject">
                        <span class="index">${i.index + 1}</span><span class="title_bg">（多选题）</span>${question.varQuestionsContent}
                        <div class="show_img clearfix">
                            <c:forEach items="${question.questionTitleFiles}" var="file">
                                <%-- 图片--%>
                                <c:if test="${file.mimeType == 'img'}">
                                    <img class="lazy" data-original="${file.mimeUrl}" alt="">
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="options">
                        <c:forEach items="${question.options}" var="q">
                            <div>
                                <div class="option checkbox" data-type="${q.oType}">
                                    <span></span>
                                    <div>${q.oType}、${q.optionContent}</div>
                                </div>
                                <div class="show_img clearfix">
                                    <c:if test="${q.mimeType == 'img'}">
                                        <img class="lazy" data-original="${q.mimeUrl}" alt="">
                                    </c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="option_select clearfix" data-type="${question.chrType}">
                        <%--<div class="pull-left">
                            <c:forEach items="${question.options}" var="q">
                                <label>
                                    <input type="checkbox" name="checkbox${i.index}" value="${q.oType}"> ${q.oType}
                                </label>
                            </c:forEach>
                        </div>--%>
                        <div class="pull-left">
                            <button type="button" class="btn btn-mark">标记</button>
                        </div>
                        <div class="pull-right">
                            <c:if test="${question.chrIsCollect == true || question.chrIsCollect == 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star" style="color: #2487de"></i> 收藏试题</a>
                            </c:if>
                            <c:if test="${question.chrIsCollect != true && question.chrIsCollect != 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star-o" style="color: #666"></i> 收藏试题</a>
                            </c:if>
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                        </div>
                    </div>
                </div>
            </c:if>

            <c:if test="${question.chrType == '03'}">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${project_id}">
                    <div class="subject">
                        <span class="index">${i.index + 1}</span><span class="title_bg">（判断题）</span>${question.varQuestionsContent}
                        <div class="show_img clearfix">
                            <c:forEach items="${question.questionTitleFiles}" var="file">
                                <%-- 图片--%>
                                <c:if test="${file.mimeType == 'img'}">
                                    <img class="lazy" data-original="${file.mimeUrl}" alt="">
                                </c:if>
                            </c:forEach>
                        </div>
                    </div>
                    <div class="options">
                        <div>
                            <div class="option" data-type="A">
                                <span></span>
                                <div>A、正确</div>
                            </div>
                        </div>
                        <div>
                            <div class="option" data-type="B">
                                <span></span>
                                <div>B、错误</div>
                            </div>
                        </div>
                    </div>
                    <div class="option_select clearfix" data-type="${question.chrType}">
                        <%--<div class="pull-left">
                            <label>
                                <input type="radio" name="radio${i.index}" value="A"> A
                            </label>
                            <label>
                                <input type="radio" name="radio${i.index}" value="B"> B
                            </label>
                        </div>--%>
                        <div class="pull-left">
                            <button type="button" class="btn btn-mark">标记</button>
                        </div>
                        <div class="pull-right">
                            <c:if test="${question.chrIsCollect == true || question.chrIsCollect == 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star on"></i> 收藏试题</a>
                            </c:if>
                            <c:if test="${question.chrIsCollect != true && question.chrIsCollect != 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> 收藏试题</a>
                            </c:if>
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>

    </div>
</div>

<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/messenger.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
<script src="<%=resourcePath%>static/global/js/keeplive.js"></script>

<script>
    $(function () {
        common.question_messenger_init();
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
                content: "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300;font-size: 15px;'>考卷正在生成中，请稍等...&nbsp;&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' style='color:#69e146;' onclick='window.location.reload();'>重试</a></div>",
                yes: function(layero) {
                    window.close();
                }
            });
            return ;
        }
        //初始化
        var allTime = Number(intExamDuration)*60 - 1;
        $("#time").html(parseInt(Number(allTime)/60/60) + " 小时" +
            parseInt(Number(allTime)/60%60) + " 分钟" +
            parseInt(Number(allTime)%60) + " 秒");
        question.allTime = new Date().getTime()/1000 + Number(intExamDuration)*60;
        question.clearInterval = window.setInterval(question.setInterval, 1000);
        question.startTime = '${startTime}';
        question.init();
        question_basic.init();
    });
</script>
</body>
</html>