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
        .nav{display: none;}
        .bs_10{box-shadow: 10px 10px 30px rgba(0,0,0,.1);}
        .box p{font-size:18px;color:#0077f1;}
        .w_540{width:540px;background:#c3edfe;height:100%;padding:34px;}
        .w_660{width:660px;height:100%;background: url("<%=resourcePath%>static/global/images/v2/enter_after.png") center center no-repeat }
        .a{height:56px;width:100%;opacity: 1;border:none;font-size:16px;color:#fff;}
        .mg_b_17{margin-bottom:17px;}
        .mg_b_70{margin-bottom:70px;}
        .mg_t_51{margin-top:51px;}
        p{line-height: 33px;}
        .a_1{background:#f56a6a;}
        .a_2{background:#31d288;}
        .a_3{background:#01a2ff;}
        .a_4{background:#007eff;}
        .a:hover{opacity:0.9;}
        .a-group{padding-top:151px;background: url("<%=resourcePath%>static/global/images/v2/enter_after_1.png") center top no-repeat; }
    </style>
    <jsp:include page="${path}/common/script" />

</head>
<body>
<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area">
        <%--<div class="clearfix"  style="margin-top:10px;line-height:36px;">
            <div class="pull-left project_title">
                项目名称--考试
            </div>
            <div class="pull-right search-group">
                <a href="javascript:history.go(-1);" class="btn btn-info">返回</a>
            </div>
        </div>--%>
        <div class="clearfix enter-project-title">
            <div class="pull-left project_title">项目名称：考试</div>
            <div class="pull-right search-group">
                <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
            </div>
        </div>
        <div class="box bs_10 clearfix" style="margin-top:0px;">
            <div class="pull-left w_660"></div>
            <div class="pull-right w_540">

                <button class="a mg_b_17 a_1" id="wrongCount" onclick="common.goto_student_wrong('${param.projectId}');">我的错题(<span>0</span>)</button>
                <button class="a mg_b_70 a_2" id="collectCount" onclick="common.goto_student_collection('${param.projectId}');">我的收藏(<span>0</span>)</button>
                <p class="text-center">
                    总分：${examStrategy.totalScore}分&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;合格分：${examStrategy.passScore}分
                </p>
                <p class="text-center">考试时间：${examStrategy.examDuration}分钟</p>
                <div class="a-group mg_t_51">
                    <button class="a mg_b_17 a_3 exam_1" onclick="common.goto_student_exam('${projectInfo.id}','1')">模拟考试</button>
                    <button class="a a_4 exam_2" onclick="common.goto_student_exam('${projectInfo.id}','2')">
                        正式考试<span title="剩余考试次数" style="margin-left: 6px;">( <i>0</i> )</span>
                    </button>
                </div>
            </div>
        </div>

    </div>
</div>
<input type="hidden" id="projectId" value="${param.projectId}">
<input type="hidden" id="project_basicId" name="project_basicId" value="${param.projectId}">   <!--项目id-->
<input type="hidden" id="project_userId" name="project_userId" value="${sessionScope.bossien_user.id}">     <!--用户id-->
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/jquery.blockui.min.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
<script>

    var permission={
        'permissionTrain':${permissionTrain},
        'permissionExercise':${permissionExercise},
        'permissionExam':${permissionExam},
        'trainType':"${trainType}",
        'trainExamType':"${trainExamType}",
        'projectType':"${projectInfo.projectType}"
    }
    $(function () {
        common.messenger_student_enter_init();
        common.update_wrong_collect();
        common.goto_student_exam_paper_no('${param.projectId}');
    })
</script>
</body>
</html>
