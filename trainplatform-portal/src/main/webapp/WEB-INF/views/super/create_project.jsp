<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.bossien.train.util.PropertiesUtils" %>
<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";

    //含有考试项目的
    String containExamType= PropertiesUtils.getValue("contain_exam_type");
    //含有培训项目的
    String containTrainType=PropertiesUtils.getValue("contain_train_type");
    //含有练习项目的
    String containExerciseType=PropertiesUtils.getValue("contain_exercise_type");
    //单独考试项目
    String examType= ProjectTypeEnum.QuestionType_3.getValue();
%>
<!DOCTYPE html>
<html>
<head>
    <title>创建公开型项目</title>
    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />

    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
</head>
<body onbeforeunload="stragegySelect.stragegy_save();">
<jsp:include page="${path}/admin/super_menu" >
    <jsp:param name="super_menu" value="index"></jsp:param>
</jsp:include>

<c:set var="containExamType" value="<%=containExamType%>" ></c:set>
<c:set var="containTrainType" value="<%=containTrainType%>" ></c:set>
<c:set var="containExerciseType" value="<%=containExerciseType%>" ></c:set>
<c:set var="examType" value="<%=examType%>" ></c:set>

<div id="main" style="min-height:auto;">
    <div class="area" >
        <div class="box" style="padding-bottom:10px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                    <span>
                        <a class="btn btn-info" id="btn_save"><span class="fa fa-save"></span> 保存</a>
                        <a class="btn btn-info" id="btn_project_publish"><span class="fa fa-paper-plane "></span> 发布</a>
                        <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span>返回</a>
                    </span>
                </div>
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
                    <div class="col-7 info">
                        <input type="text" id="datBeginTime" name="datBeginTime" value="${projectBeginTime}" class="Wdate" style="width: 186px"
                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'datBeginTime\')}'})">
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" id="datEndTime" name="datEndTime" value="${projectEndTime}" class="Wdate" style="width: 186px"
                               onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'datEndTime\')}'})">
                    </div>
                </div>


                <c:if test="${fn:contains(containExamType, projectTypeNo)}">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间</label>
                        <div class="col-7 info">
                            <input type="text" id="examBeginTime" name="examBeginTime" value="${examBeginTime}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${examEndTime}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'examBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试次数</label>
                        <div class="col-7 info">
                            <input type="text"  id="intRetestTime" name="intRetestTime" value="1" style="width: 160px" onchange="createPrivateProject.verifyIntRetestTime(this.value)">
                            &nbsp;&nbsp;次&nbsp;&nbsp;
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="box index" style="display: none;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训课程信息列表</h3>
                <form id="course_form">
                <div class="pull-right search-group">
                    <span>
                        <button  type="button"  class="btn btn-info" id="btn_select_course"><span class="fa fa-mouse-pointer"></span> 选择课程</button>
                        <button  type="button"  class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
                        <input type="text" id="course_name" name="course_name" placehodler="请输入课程名称">
                        <button type="button" title="搜索" class="btn" id="btn_search_course">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                    </span>
                    <span>
                        <a href="javascript:;" id="btn_search_all_course" class="btn all">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </span>
                </div>
                    <input type="hidden" id="course_project_id" name="course_project_id" value="${projectBasic.id}">
                </form>
            </div>

            <div class="box-body">

                        <table class="table">
                            <thead>
                            <tr >
                                <th>序号</th>
                                <th><input type="checkbox" name="all" id="coursecheckAll"  ></th>
                                <th>课程编号</th>
                                <th>课程名称</th>
                                <th>受训角色</th>
                                <c:if test="${fn:contains(containTrainType, projectTypeNo)}">
                                    <th>课时</th>
                                    <th>学时要求</th>
                                </c:if>
                                <c:if test="${fn:contains(containExamType, projectTypeNo) || fn:contains(containExerciseType, projectTypeNo) }">
                                    <th>题库总量</th>
                                </c:if>
                                <c:if test="${fn:contains(containExamType, projectTypeNo) }">
                                    <th>必选题量</th>
                                </c:if>
                                <th style="text-align: center">查看</th>
                            </tr>
                            </thead>
                            <%--课程列表--%>
                            <tbody id="course_list">

                            </tbody>
                        </table>
                        <div class="page text-right"  style="margin-top:0px;">
                            <ul id="course_page"></ul>
                        </div>
                </div>
            </div>

            <%--组卷策略--%>
            <c:if test="${fn:contains(containExamType, projectTypeNo)}">
                <div class="box index" style="display: none;">
                    <div class="box-header">
                        <h3 class="box-title">组卷策略信息</h3>
                    </div>
                    <div class="box-body">
                        <form id="stragegyForm">
                            <div>
                                <ul class="clearfix combination" id="stragegy_title" style="border:none;">

                                </ul>
                            </div>
                            <table  class="table">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>题型</th>
                                    <th>考试题量</th>
                                    <th>分值</th>
                                    <th>总分</th>
                                </tr>
                                </thead>
                                <tbody id="stragegy_body">

                                </tbody>
                            </table>
                            <input type="hidden" id="roleId" name="roleId" value="-1">
                            <input type="hidden" id="roleName" name="roleName" value="默认角色">
                        </form>
                    </div>
                </div>
            </c:if>

            <div class="box index" style="display: none;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训单位信息列表</h3>
                <form id="company_form">
                    <input type="hidden" id="company_project_id" name="company_project_id" value="${projectBasic.id}">
                <div class="pull-right search-group">
                    <span>
                        <button type="button" class="btn btn-info" id="btn_select_company"><span class="fa fa-mouse-pointer"></span> 选择单位</button>
                        <button type="button" class="btn btn-info" id="btn_delete_batch_company"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
                        <%--<label>单位名称</label>--%>
                        <input type="text" id="company_name" name="company_name" placeholder="请输入单位名称">
                        <button type="button" title="搜索" class="btn" id="btn_company_search">
                            <span class="fa fa-search"></span> 搜索
                        </button>

                    </span>
                    <span>
                        <a href="javascript:;" id="btn_company_all" class="btn all">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </span>
                </div>
                </form>
            </div>

            <div class="box-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th><input type="checkbox" name="all"></th>
                        <th>单位名称</th>
                    </tr>
                    </thead>
                    <%--单位列表--%>
                    <tbody id="company_list">

                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:0px;">
                    <ul id="company_page"></ul>
                </div>
                </div>
            </div>
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
<script type="text/javascript" src="<%=resourcePath%>static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/stragegy_public_select.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/create_publicProject.js"></script>
<script type="text/javascript">
    var page1 = new page1();
    var page2 = new page();
    var options={
        'containExamType':"<%=containExamType%>",
        'containTrainType':"<%=containTrainType%>",
        'containExerciseType':"<%=containExerciseType%>",
        'examType':"<%=examType%>"
    }
    createPublicProject.init(page1,page2,options);
</script>
</body>
</html>
