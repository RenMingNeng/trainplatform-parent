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
    <title>创建公开型项目</title>
    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />
</head>
<body>
<jsp:include page="${path}/super/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area">
        <div class="box index" style="padding-bottom:10px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
            </div>
            <div class="box-body">
                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>项目名称</label>
                    <div class="col-7 info">
                        <input type="text" id="projectName" name="projectName" value="${projectName}" class="block">
                    </div>
                </div>

                <div class="row form-group" <c:if test="${empty subjectName}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>培训主题</label>
                    <div class="col-7 info">
                        <input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">
                        <input type="hidden" id="subjectId" value="${subjectId}" >
                        <input type="text" class="block" id="subjectName" value="${subjectName}" readonly="readonly">
                    </div>
                </div>

                <div class="row form-group" <c:if test="${projectTypeNo==examType}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>项目时间</label>
                    <input type="hidden" id="systemTime" name="systemTime" value="${systemTime}"/>
                    <div class="col-7 info">
                        <input type="text" id="datBeginTime" name="datBeginTime" value="${fn:substring(datBeginTime,0 , 10)}" style="width: 186px"
                               class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'datEndTime\')}',readOnly:true})">
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" id="datEndTime" name="datEndTime" value="${fn:substring(datEndTime,0 , 10)}" style="width: 186px"
                               class="Wdate" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'datBeginTime\')}',readOnly:true})">
                    </div>
                </div>

                <c:if test="${permissionExam}">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间</label>
                        <div class="col-7 info">
                            <input type="text" id="examBeginTime" name="examBeginTime" value="${fn:substring(examBeginTime,0,16)}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${fn:substring(examEndTime,0,16)}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'examBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试次数</label>
                        <div class="col-7 info">
                            <input type="text"  id="intRetestTime" name="intRetestTime" value="1" style="width: 160px" onchange="step_1.verifyIntRetestTime(this.value)">
                            &nbsp;&nbsp;次&nbsp;&nbsp;
                        </div>
                    </div>
                </c:if>

            </div>

        </div>
        <div class="search-group clearfix">
            <span style="float:right;margin-top:-10px;">
                <a class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                &nbsp;&nbsp;
                <a class="btn btn-info" id="step_2"><span class="fa fa-long-arrow-right"></span> 下一步</a>
            </span>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="id" name="id" value="${projectBasic.id}">
<input type="hidden" id="trainPeriod" name="trainPeriod" value="${projectBasic.trainPeriod}">
<input type="hidden" id="projectMode" name="projectMode" value="${projectBasic.projectMode}">

<input type="hidden" id="createUser" name="createUser" value="${projectBasic.createUser}">
<input type="hidden" id="createTime" name="createTime" value="${projectBasic.createTime}">
<input type="hidden" id="operUser" name="operUser" value="${projectBasic.operUser}">
<input type="hidden" id="operTime" name="operTime" value="${projectBasic.operTime}">
<jsp:include page="${path}/common/footer" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/createProject/step_1.js"></script>
<script type="text/javascript">
    var options={
        'containExamType':"${containExamType}",
        'containTrainType':"${containTrainType}",
        'containExerciseType':"${containExerciseType}",
        'examType':"${examType}"
    }
    step_1.init(options);
</script>
</body>
</html>
