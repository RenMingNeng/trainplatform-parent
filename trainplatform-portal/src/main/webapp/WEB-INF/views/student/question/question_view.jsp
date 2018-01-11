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
    <div class="main_right">
        <h2>
            <c:if test="${!empty title}">
                《${title}》
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
                        <span class="index">${i.index + 1}</span><span class="text-main">（单选题）</span>${question.varQuestionsContent}
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
                    <div class="option_select clearfix" data-type="${question.chrType}">
                        <div class="pull-left">
                            <%--<c:forEach items="${question.options}" var="q">
                                <label>
                                    <input type="radio" name="redio${i.index}" value="${q.oType}"> ${q.oType}
                                </label>
                            </c:forEach>--%>
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
                        <span class="index">${i.index + 1}</span><span class="text-main">（多选题）</span>${question.varQuestionsContent}
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
                    <div class="option_select clearfix" data-type="${question.chrType}">
                        <div class="pull-left">
                            <%--<c:forEach items="${question.options}" var="q">
                                <label>
                                    <input type="checkbox" name="checkbox${i.index}" value="${q.oType}"> ${q.oType}
                                </label>
                            </c:forEach>--%>
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
                        <span class="index">${i.index + 1}</span><span class="text-main">（判断题）</span>${question.varQuestionsContent}
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
                    <div class="option_select clearfix" data-type="${question.chrType}">
                        <div class="pull-left">
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
    })
</script>
</body>