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
        <%--题量为0时--%>
        <c:if test="${fn:length(questions) < 1}">
            <div><div class="no-content"></div></div>
        </c:if>

        <c:forEach items="${questions}" var="question" varStatus="i">
            <%--单选题--%>
            <c:if test="${question.chrType == '01'}">
                <div id="div${i.index + 1}" class="questions" data-course="${param.course_id}" data-question="${question.intId}" data-project="${param.project_id}" data-index="${i.index}">
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
                        <div class="pull-left">
                            <%--<c:forEach items="${question.options}" var="q">
                                <label>
                                    <input type="radio" name="redio${i.index}" value="${q.oType}"> ${q.oType}
                                </label>
                            </c:forEach>--%>
                            <button type="button" class="btn btn-info sure">确定</button>
                            <button type="button" class="btn btn-mark">标记</button>
                        </div>
                        <div class="pull-right">
                            <a href="javascript:;" class="remove_error" style="display: none;"><i class="fa fa-remove"></i> 移除错题</a>
                            <c:if test="${question.chrIsCollect == true || question.chrIsCollect == 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star on"></i> 收藏试题</a>
                            </c:if>
                            <c:if test="${question.chrIsCollect != true && question.chrIsCollect != 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star-o" ></i> 收藏试题</a>
                            </c:if>
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                            <a href="javascript:;" class="analy" style="display: none;">展开解析 <i class="fa fa-angle-down"></i></a>
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
                <div id="div${i.index + 1}" class="questions" data-course="${param.course_id}" data-question="${question.intId}" data-project="${param.project_id}" data-index="${i.index}">
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
                        <div class="pull-left">
                            <%--<c:forEach items="${question.options}" var="q">
                                <label>
                                    <input type="checkbox" name="checkbox${i.index}" value="${q.oType}"> ${q.oType}
                                </label>
                            </c:forEach>--%>
                            <button type="button" class="btn btn-info sure">确定</button>
                            <button type="button" class="btn btn-mark">标记</button>
                        </div>
                        <div class="pull-right">
                            <a href="javascript:;" style="display: none;"  class="remove_error"><i class="fa fa-remove"></i> 移除错题</a>
                            <c:if test="${question.chrIsCollect == true || question.chrIsCollect == 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star on"></i> 收藏试题</a>
                            </c:if>
                            <c:if test="${question.chrIsCollect != true && question.chrIsCollect != 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> 收藏试题</a>
                            </c:if>
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                            <a href="javascript:;" class="analy" style="display: none;">展开解析 <i class="fa fa-angle-down"></i></a>
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
                <div id="div${i.index + 1}" class="questions" data-course="${param.course_id}" data-question="${question.intId}" data-project="${param.project_id}"  data-index="${i.index}">
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
                        <div class="pull-left">
                            <button type="button" class="btn btn-info sure">确定</button>
                            <button type="button" class="btn btn-mark">标记</button>
                        </div>
                        <div class="pull-right">
                            <a href="javascript:;" style="display: none;"  class="remove_error"><i class="fa fa-remove"></i> 移除错题</a>
                            <c:if test="${question.chrIsCollect == true || question.chrIsCollect == 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star on"></i> 收藏试题</a>
                            </c:if>
                            <c:if test="${question.chrIsCollect != true && question.chrIsCollect != 'true'}">
                                <a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> 收藏试题</a>
                            </c:if>
                            <%--<a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>--%>
                            <%--&nbsp;--%>
                            <a href="javascript:;" class="analy" style="display: none;">展开解析 <i class="fa fa-angle-down"></i></a>
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

            <c:if test="${question.chrType != '03' && question.chrType != '02' && question.chrType != '01' }">
                <div id="div${i.index + 1}" class="questions" data-question="${question.intId}" data-project="${param.project_id}"  data-index="${i.index}">
                                    非单选、多选、判断题
                </div>
            </c:if>
        </c:forEach>

    </div>
</div>

<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/messenger.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_basic.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question.js"></script>
<script src="<%=resourcePath%>static/global/js/student/question/question_public.js"></script>
<script src="<%=resourcePath%>static/global/js/keeplive.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
<script>
    $(function () {
        //初始化
        question.init();
        question_basic.init();
        showAnswer();
    })
</script>
</body>