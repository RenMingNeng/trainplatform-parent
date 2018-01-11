<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>进入考试</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .box{height:800px;margin:30px 0;}
        .bs_10{box-shadow: 10px 10px 30px rgba(0,0,0,.1);}
        .box p{font-size:18px;color:#0077f1;}
        .mg_b_17{margin-bottom:17px;}
        .mg_b_70{margin-bottom:70px;}
        .mg_t_51{margin-top:51px;}
        .w_540{width:540px;background:#c3edfe;height:100%;padding:34px;}
        .w_660{width:660px;height:100%;background: url("<%=resourcePath%>static/global/images/v2/enter_after.png") center center no-repeat }
        .a{height:56px;width:100%;opacity: 1;border:none;font-size:16px;color:#fff;}
        .a img{display: inline-block;margin-right:14px;line-height: 56px;}

        p{line-height: 33px;}

        .a_1{background:#f56a6a;}
        .a_2{background:#31d288;}
        .a_3{background:#01a2ff;}
        .a_4{background:#007eff;}
        .a:hover{opacity:0.9;}
        .a-group{padding-top:151px;background: url("<%=resourcePath%>static/global/images/v2/enter_after_1.png") center top no-repeat; }
    </style>
</head>
<body>
<jsp:include page="${path}/student/menu"></jsp:include>

<div id="main">
    <div class="area">

        <div class="box bs_10 clearfix">
            <div class="pull-left w_660"></div>
            <div class="pull-right w_540">

                <button class="a mg_b_17 a_1"><img src="<%=resourcePath%>static/global/images/v2/enter_after_2.png" alt="">我的错题</button>
                <button class="a mg_b_70 a_2"><img src="<%=resourcePath%>static/global/images/v2/star.png" alt="">我的收藏</button>
                <p class="text-center">
                    总分：100分&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;合格分：60分
                </p>
                <p class="text-center">考试时间：120分钟</p>
                <div class="a-group mg_t_51">
                    <button class="a mg_b_17 a_3">模拟考试</button>
                    <button class="a a_4">正式考试</button>
                </div>
            </div>
        </div>

    </div>
</div>

<jsp:include page="${path}/common/footer" />


</body>
</html>
