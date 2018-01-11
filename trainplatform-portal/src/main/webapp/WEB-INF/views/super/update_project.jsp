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
    <title>公开型项目修改</title>
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
<body>
<jsp:include page="${path}/admin/super_menu" >
    <jsp:param name="super_menu" value="index"></jsp:param>
</jsp:include>

<c:set var="containExamType" value="<%=containExamType%>" ></c:set>
<c:set var="containTrainType" value="<%=containTrainType%>" ></c:set>
<c:set var="containExerciseType" value="<%=containExerciseType%>" ></c:set>
<c:set var="examType" value="<%=examType%>" ></c:set>
<div id="main">
    <div class="area">
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                    <span>
                        <a class="btn btn-info" id="btn_save"><span class="fa fa-save"></span> 保存</a>
                        <a class="btn btn-info" id="btn_save_public"> <span class="fa fa-paper-plane "></span> 发布</a>
                        <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
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
                <c:if test="${fn:contains(containTrainType, projectTypeNo)}">
                    <div class="row form-group" >
                        <label for="" class="col-1"><b class="text-red">*</b>培训时间</label>
                        <div class="col-7 info">
                            <input type="text" id="trainBeginTime" name="trainBeginTime" value="${trainStartTime}" style="width: 186px"
                                   class="Wdate block" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'trainEndTime\')}',readOnly:true})" >
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text"  id="trainEndTime" name="trainEndTime" value="${trainEndTime}" style="width: 186px"
                                   class="Wdate endTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'trainBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                </c:if>
                <%--含有练习项目--%>
                <c:if test="${fn:contains(containExerciseType, projectTypeNo)}">
                    <div class="row form-group" >
                        <label for="" class="col-1"><b class="text-red">*</b>练习时间</label>
                        <div class="col-7 info">
                            <input type="text" id="exerciseBeginTime" name="ExerciseBeginTime" value="${exerciseStartTime}" style="width: 186px"
                                   class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'exerciseEndTime\')}',readOnly:true})" >
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="exerciseEndTime" name="ExerciseEndTime" value="${exerciseEndTime}" style="width: 186px"
                                   class="Wdate  endTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'exerciseBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                </c:if>
                <%--含有考试项目--%>
                <c:if test="${fn:contains(containExamType, projectTypeNo)}">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间</label>
                        <div class="col-7 info">
                            <input type="text" id="examBeginTime" name="examBeginTime" value="${examStartTime}" style="width: 186px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${examEndTime}" style="width: 186px"
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

        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训课程信息列表</h3>
                <form id="course_form">
                <div class="pull-right search-group">
                    <input type="hidden" id="course_project_id" name="course_project_id" value="${projectInfo.id}">
                    <span>
                        <button type="button" class="btn btn-info" id="btn_select_course"><span class="fa fa-mouse-pointer"></span> 选择课程</button>
                        <button type="button" class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
                         <input type="text" id="course_name" name="course_name" placeholder="请输入课程名称">
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
                </form>
            </div>
            <%--<div class="pull-left">
                <button class="btn btn-info" id="btn_select_course"><span class="fa fa-mouse-pointer"></span> 选择课程</button>
                <button class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</button>
            </div>--%>
            <%--<form id="course_form">
                <div class="pull-right">
                    <div class="search">
                    <label>课程名称</label>
                        <input type="text" id="course_name" name="course_name" placehodler="请输入课程名称">
                        <button type="button" title="搜索" class="btn btn-info" id="btn_search_course">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                        <a href="javascript:;" id="btn_search_all_course" class="btn btn-info">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </div>
                </div>
                <input type="hidden" id="course_project_id" name="course_project_id" value="${projectInfo.id}">
            </form>--%>
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
                        <th>查看</th>
                    </tr>
                    </thead>
                    <%--课程列表--%>
                    <tbody id="course_list">

                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:0;">
                    <ul id="course_page"></ul>
                </div>
            </div>
        </div>

        <%--组卷策略--%>
        <c:if test="${fn:contains(containExamType, projectTypeNo)}">
            <div class="box">
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
                                <th>总题量</th>
                                <th>考试题量</th>
                                <th>分值</th>
                                <th style="text-align: center">总分</th>
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

        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训单位信息列表</h3>
                <form id="company_form">
                <div class="pull-right search-group">
                    <span>
                        <button type="button" class="btn btn-info" id="btn_select_company"><span class="fa fa-mouse-pointer"></span> 选择单位</button>
                        <button type="button" class="btn btn-info" id="btn_delete_batch_company"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
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
                    <input type="hidden" id="company_project_id" name="company_project_id" value="${projectInfo.id}">
                </form>
            </div>
            <%--<div class="pull-left">
                <button class="btn btn-info" id="btn_select_company"><span class="fa fa-mouse-pointer"></span> 选择单位</button>
                <button class="btn btn-info" id="btn_delete_batch_company"><span class="fa fa-trash-o"></span> 批量删除</button>
            </div>--%>
            <%--<form id="company_form">
                <div class="pull-right">
                    <div class="search">
                        <label>单位名称</label>
                        <input type="text" id="company_name" name="company_name" placehodler="请输入单位名称">
                        <button type="button" title="搜索" class="btn btn-info" id="btn_company_search">
                            <span class="fa fa-search"></span> 搜索
                        </button>
                        <a href="javascript:;" id="btn_company_all" class="btn btn-info">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </div>
                </div>
                <input type="hidden" id="company_project_id" name="company_project_id" value="${projectInfo.id}">
            </form>--%>
            <div class="box-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th width="60"><input type="checkbox" name="all"></th>
                        <th>单位名称</th>
                    </tr>
                    </thead>
                    <%--单位列表--%>
                    <tbody id="company_list">

                    </tbody>
                </table>
                <div class="page text-right" style="border:none;">
                    <ul id="company_page"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" name="projectId" value="${projectInfo.id}">
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectInfo.projectType}">
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectInfo.projectStatus}">
<jsp:include page="${path}/common/footer" />
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/common/stragegy_update.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/super/update_publicProject.js"></script>
<script type="text/javascript">
    var page1 = new page1();
    var page2 = new page();
    var options={
        'containExamType':"<%=containExamType%>",
        'containTrainType':"<%=containTrainType%>",
        'containExerciseType':"<%=containExerciseType%>",
        'examType':"<%=examType%>"
    }
    updatePublicProject.init(page1,page2,options);
    // 含考试项目查询组卷策略
    if(updatePublicProject.isContains('3567',$("#projectTypeNo").val())){
        stragegy_update.initPublicMethod('${projectInfo.projectStatus}');
    }
</script>
</body>
</html>
