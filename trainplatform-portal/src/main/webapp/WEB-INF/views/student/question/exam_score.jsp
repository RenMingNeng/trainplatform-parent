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
    <div class="main_right">
        <h2>
            <c:if test="${!empty title}">
                《${title}-答题记录》
            </c:if>
        </h2>

        <%--题量为0时--%>
        <c:if test="${fn:length(questions) < 1}">
            <div><div class="no-content"></div></div>
        </c:if>

        <c:forEach items="${questions}" var="question" varStatus="i">
            <%--单选题--%>
            <c:if test="${question.chrType == '01'}">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${param.project_id}" data-index="${i.index}">
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
                    <div class="option_select clearfix" data-correct="${question.isCorrect}">
                        <div class="pull-left">
                            我的回答：${question.myAnswer}（
                            <c:if test="${question.isCorrect == 1 || question.isCorrect == '1'}">
                                <span class="is_correct" style="color: #00b8c7">回答正确</span>
                            </c:if>
                            <c:if test="${question.isCorrect != 1 && question.isCorrect != '1'}">
                                <span class="is_correct" style="color: red;">回答错误</span>
                            </c:if>
                            ）
                        </div>
                        <div class="pull-right">
                            <a href="javascript:;" class="analy">展开解析 <i class="fa fa-angle-down"></i></a>
                        </div>
                    </div>
                    <div class="subject_foot">
                        <div><span class="label">正确答案：</span><i class="answer">${question.varAnswer}</i></div>
                        <div class="clearfix">
                            <span class="label">试题解析：</span>
                            <div class="pull-left analysis">
                                <c:if test="${empty question.analysisContent}">
                                    暂无解析
                                </c:if>
                                <c:if test="${!empty question.analysisContent}">
                                    ${question.analysisContent}
                                    <div class="show_img clearfix">
                                        <c:forEach items="${analysisFiles}" var="file">
                                            <c:if test="${file.mimeType == 'img'}">
                                                <img class="lazy" data-original="${file.mimeUrl}" alt="">
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <%--多选题--%>
            <c:if test="${question.chrType == '02'}">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${param.project_id}" data-index="${i.index}">
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
                                <div class="option" data-type="${q.oType}">
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
                    <div class="option_select clearfix" data-correct="${question.isCorrect}">
                        <div class="pull-left">
                            我的回答：${question.myAnswer}（
                            <c:if test="${question.isCorrect == 1 || question.isCorrect == '1'}">
                                <span class="is_correct" style="color: #00b8c7">回答正确</span>
                            </c:if>
                            <c:if test="${question.isCorrect != 1 && question.isCorrect != '1'}">
                                <span class="is_correct" style="color: red;">回答错误</span>
                            </c:if>）
                        </div>
                        <div class="pull-right">
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                            <a href="javascript:;" class="analy">展开解析 <i class="fa fa-angle-down"></i></a>
                        </div>
                    </div>
                    <div class="subject_foot">
                        <div><span class="label">正确答案：</span><i class="answer">${question.varAnswer}</i></div>
                        <div class="clearfix"><span class="label">试题解析：</span>
                            <div class="pull-left analysis">
                                <c:if test="${empty question.analysisContent}">
                                    暂无解析
                                </c:if>
                                <c:if test="${!empty question.analysisContent}">
                                    ${question.analysisContent}
                                    <div class="show_img clearfix">
                                        <c:forEach items="${analysisFiles}" var="file">
                                            <c:if test="${file.mimeType == 'img'}">
                                                <img class="lazy" data-original="${file.mimeUrl}" alt="">
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <%--判断题--%>
            <c:if test="${question.chrType == '03'}">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${param.project_id}"  data-index="${i.index}">
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
                                <div>A、正确</div>
                            </div>
                        </div>
                        <div>
                            <div class="option" data-type="B">
                                <div>B、错误</div>
                            </div>
                        </div>
                    </div>
                    <div class="option_select clearfix" data-correct="${question.isCorrect}">
                        <div class="pull-left">
                            我的回答：${question.myAnswer}（
                            <c:if test="${question.isCorrect == 1 || question.isCorrect == '1'}">
                                <span class="is_correct" style="color: #00b8c7">回答正确</span>
                            </c:if>
                            <c:if test="${question.isCorrect != 1 && question.isCorrect != '1'}">
                                <span class="is_correct" style="color: red;">回答错误</span>
                            </c:if>）
                        </div>
                        <div class="pull-right">
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                            <a href="javascript:;" class="analy">展开解析 <i class="fa fa-angle-down"></i></a>
                        </div>
                    </div>
                    <div class="subject_foot">
                        <div><span class="label">正确答案：</span><i class="answer">${question.varAnswer}</i></div>
                        <div class="clearfix"><span class="label">试题解析：</span>
                            <div class="pull-left analysis">
                                <c:if test="${empty question.analysisContent}">
                                    暂无解析
                                </c:if>
                                <c:if test="${!empty question.analysisContent}">
                                    ${question.analysisContent}
                                    <div class="show_img clearfix">
                                        <c:forEach items="${analysisFiles}" var="file">
                                            <c:if test="${file.mimeType == 'img'}">
                                                <img class="lazy" data-original="${file.mimeUrl}" alt="">
                                            </c:if>
                                        </c:forEach>
                                    </div>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>

    </div>
</div>
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
<script>
    $(function () {
        //初始化
        question.init();
        question_basic.init();
        showAnswer();

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
</body>