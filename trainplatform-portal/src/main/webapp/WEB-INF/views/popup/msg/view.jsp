<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>博晟 | 公告</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<shiro:hasRole name="company_admin">
    <jsp:include page="${path}/admin/menu" >
        <jsp:param name="menu" value="msg"></jsp:param>
    </jsp:include>
</shiro:hasRole>
<shiro:hasRole name="company_user">
    <jsp:include page="${path}/student/menu" >
        <jsp:param name="menu" value="msg"></jsp:param>
    </jsp:include>
</shiro:hasRole>

<div id="main" class="container">
    <div class="area">
        <div class="clearfix">
            <div class="msg_view">
                <div class="addr" style="margin:0 -200px;font-size:14px;padding-bottom:20px;color:#8e9193;">
                    <span style="color:#50d652">当前位置：</span> 首页 > 通知公告 > <span style="text-decoration: underline">正文</span>
                </div>
                <div class="h" style="margin-bottom:10px;">
                    <h2 class="text-center" style="font-size:24px;">${msgText.msgTitle}</h2>
                    <div class="text-center" style="color:#ccc;line-height: 30px;">${msgText.publishTime}</div>
                </div>
                <div class="b article">
                    ${msgText.msgContent}
                        <div class="clearfix msg_view_b">
                            <c:if test="${!empty files}">
                                <c:forEach items="${files}" var="file">
                                    <div class="clearfix item" style="margin-bottom:15px;">
                                        <div class="word-icon pull-left">
                                            <img style="display: block;vertical-align: middle;" src="<%=resourcePath%>static/global/images/v2/word_icon.png" alt="">
                                        </div>
                                        <div class="pull-left b" style="line-height: 19px;">
                                            <a href="${path}download/${file.url}?fileName=${file.name}" >点击下载：${file.name}</a>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:if>
                        </div>
                </div>
            </div>

        </div>

    </div>
</div>
<jsp:include page="${path}/common/script" />
<jsp:include page="${path}/common/footer" />
</body>
</html>
