<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://www.springframework.org/tags" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<jsp:include page="${path}/common/style" />
<jsp:include page="${path}/common/script" />
<html>
<body>
    <h2>欢迎来到新版培训平台!</h2>

    <h5>自动登录到<a href="${path}admin">管理员</a></h5>

    <h5>自动登录到<a href="${path}superAdmin">超级管理员</a></h5>

    <h5>自动登录到<a href="${path}student">学员</a></h5>

    <h5>进入学习界面<a href="${path}student/study?projectId=1&courseId=1">${path}student/study?projectId=1&courseId=1</a></h5>

    <h5>进入学习界面<a id="a_course_preview" href="javascript:void(0);">弹出课程预览界面</a></h5>

    <button id="btn1">普通的</button>
    <button id="btn2">成功的</button>
    <button id="btn3">警告的</button>
    <button id="btn4">错误的</button>

</body>
<script>
    $(document).ready(function(){


        $("#a_course_preview").click(function () {
            popupWindow("<%=basePath%>/popup/course/preview");
        });

        $('#btn1').click(function() {
            message.add("普通的消息");
        });
        $('#btn2').click(function() {
            message.add("成功的消息", "success");
        });
        $('#btn3').click(function() {
            message.add("警告的消息", "warning");
        });
        $('#btn4').click(function() {
            message.add("错误的消息", "error");
        });

    });
</script>
</html>
