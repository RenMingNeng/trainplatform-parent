<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";

    //单独考试项目
    String examType= ProjectTypeEnum.QuestionType_3.getValue();
%>
<!DOCTYPE html>
<html>
<head>
    <title>公开型项目修改</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<jsp:include page="${path}/super/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <div class="box " style="padding-bottom:10px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
            </div>
            <div class="box-body">
                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>项目名称</label>
                    <div class="col-7 info">
                        <input type="text" class="block" value="${projectInfo.projectName}" readonly="readonly">
                    </div>
                </div>
                <div class="row form-group" <c:if test="${empty subjectName}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>培训主题</label>
                    <div class="col-7 info">
                        <input type="text" class="block" id="subjectName" value="${subjectName}" readonly="readonly">
                    </div>
                </div>
                <%--含有培训项目--%>
                <c:if test="${permissionTrain}">
                    <div class="row form-group" >
                        <label for="" class="col-1"><b class="text-red">*</b>培训时间</label>
                        <div class="col-7 info">
                            <input type="text" id="trainBeginTime" name="trainBeginTime" value="${fn:substring(trainStartTime,0,10)}" style="width: 186px"
                                   class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'trainEndTime\')}',readOnly:true})" >
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text"  id="trainEndTime" name="trainEndTime" value="${fn:substring(trainEndTime,0,10)}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'trainBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                </c:if>
                <%--含有练习项目--%>
                <c:if test="${permissionExercise}">
                    <div class="row form-group" >
                        <label for="" class="col-1"><b class="text-red">*</b>练习时间</label>
                        <div class="col-7 info">
                            <input type="text" id="exerciseBeginTime" name="ExerciseBeginTime" value="${fn:substring(exerciseStartTime,0 , 10)}" style="width: 186px"
                                   class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'exerciseEndTime\')}',readOnly:true})" >
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="exerciseEndTime" name="ExerciseEndTime" value="${fn:substring(exerciseEndTime,0 , 10)}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'exerciseBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                </c:if>
                <%--含有考试项目--%>
                <c:if test="${permissionExam}">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间</label>
                        <div class="col-7 info">
                            <input type="text" id="examBeginTime" name="examBeginTime" value="${fn:substring(examStartTime, 0, 16)}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${fn:substring(examEndTime,0,16)}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'examBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试次数</label>
                        <div class="col-7 info">
                            <input type="text"  id="intRetestTime" name="intRetestTime" value="${projectInfo.intRetestTime}" style="width: 160px" onchange="updatePublicProject.verifyIntRetestTime(this.value)">
                            &nbsp;&nbsp;次&nbsp;&nbsp;
                        </div>
                    </div>
                </c:if>
            </div>

        </div>
        <div class="search-group clearfix">
            <span style="float: right;margin-top:-10px;">
                <a class="btn btn-info" id="btn_back"><span class="fa fa-reply"></span> 返 回</a>
                &nbsp; &nbsp;
                <a class="btn btn-info" id="step_2"><span class="fa fa-long-arrow-right"></span> 下一步</a>
            </span>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" name="projectId" value="${projectInfo.id}">
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectInfo.projectType}">
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectInfo.projectStatus}">
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/updateProject/update_publicProject_step1.js"></script>
<script type="text/javascript">
    var page = new page();
    var options={
        'permissionExam':"${permissionExam}>",
        'permissionTrain':"${permissionTrain}>",
        'permissionExercise':"${permissionExercise}>"
    }
    updatePublicProject_step1.init(page,options);
</script>
</body>
</html>
