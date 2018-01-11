<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<html>
<head>
    <title>反馈页面</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/feedback.css" rel="stylesheet" type="text/css">
</head>
<body>
<div class="container">
    <div class="logo">
        <img src="<%=resourcePath%>static/global/images/feedback_logo.png" alt="" class="pull-left">
        <div class="pull-left text">问题处理中心</div>
    </div>
    <div class="main">
        <div class="head">
            尊敬的用户 <br>
            您可以将使用该系统时产生的问题反馈给我们，帮助我们改进用户体验。非常感谢您的参与！
        </div>
        <div class="item">
            <div class="label"><em>*</em>问题类别</div>
            <div>
                <label><input type="checkbox">&nbsp;无法播放</label>
                <label><input type="checkbox">&nbsp;播放卡顿</label>
                <label><input type="checkbox">&nbsp;播放中断</label>
                <label><input type="checkbox">&nbsp;无法显示</label>
                <label><input type="checkbox">&nbsp;播放黑屏</label>
                <label><input type="checkbox">&nbsp;画面、声音不同步</label>
                <label><input type="checkbox">&nbsp;其他</label>
            </div>
        </div>
        <div class="item">
            <div class="label"><em>*</em>问题描述</div>
            <div>
                <textarea></textarea>
            </div>
        </div>
        <div class="item">
            <a href="javascript:;" class="uplaod" title="支持jpg、jpeg、gif、png、bmp格式，文件需小于4M">
                上传截图
                <input type="file">
            </a>
        </div>
        <div class="item">
            <div class="label">再次感谢您的反馈！您可以留下常用的联系方式，以便进一步联络。</div>
            <div>
                <input type="text">
            </div>
        </div>
        <div  class="item">
            <button class="btn btn-info">提交</button>
        </div>
    </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script>
    $(function(){
        var winH = $(window).height();
        var footH = $('#footer').height();
        console.log( winH );
        $('.container').css('min-height',winH - footH - 30);
    })
</script>
</body>
</html>
