<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
    //单独考试项目
    String examType=ProjectTypeEnum.QuestionType_3.getValue();
%>
<!DOCTYPE html>
<html>
<head>
    <title>修改项目</title>
    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />
    <%--select2--%>
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <script type="text/javascript" src="<%=path%>/static/global/js/select2/js/select2.full.js"></script>
    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
    <style>
        .tree_list{height:auto;}
        .tree_list .tree>div{background: #f7f8f9;}
        .combination{border: none;}
    </style>
</head>


<body onbeforeunload="stragegy_update.stragegy_save();">
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="proManager"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area" style="overflow: hidden;">
        <%--<div id="addr" class="clearfix">
            <div class="pull-left">
                <a href="<%=resourcePath%>admin/proManager/proManagerList">项目管理</a><span class="fa fa-angle-right"></span><a href="javascript:;">修改项目</a>
            </div>
        </div>--%>

       <%-- <div class="box">
            <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
            <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span>返回</a>
        </div>--%>

        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                    <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
                    <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>
            <div class="box-body project-details" style="padding-bottom:10px;">

                <%--没有主题时隐藏--%>
                <div class="row form-group" <c:if test="${empty fn:trim(subjectName)}">style="display: none"</c:if> >
                    <label for="" class="col-1"><b class="text-red">*</b>项目主题</label>
                    <div class="col-7 info">
                        <input type="text" class="block" value="${subjectName}" disabled="disabled" >
                    </div>
                </div>
                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>项目名称</label>
                    <div class="col-7 info">
                        <input type="text" class="block" data-class="ing" id="projectName" value="${projectBasic.projectName}">
                    </div>
                </div>
                <%--含有培训项目--%>
             <c:if test="${permissionTrain }">
                <div class="row form-group" >
                    <label for="" class="col-1"><b class="text-red">*</b>培训时间</label>
                    <div class="col-7 info">
                           <input type="text" id="trainStartTime" name="trainStartTime" value="${fn:substring(trainStartTime,0 , 10)}" style="width: 160px"
                               class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'trainEndTime\')}',readOnly:true})" >
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                           <input type="text"  id="trainEndTime" name="trainEndTime" value="${fn:substring(trainEndTime,0 , 10)}" style="width: 160px"
                               class="Wdate endTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'trainStartTime\')}',readOnly:true})">
                    </div>
                </div>
             </c:if>
             <%--含有练习项目--%>
            <c:if test="${permissionExercise }">
                <div class="row form-group" >
                    <label for="" class="col-1"><b class="text-red">*</b>练习时间</label>
                    <div class="col-7 info">
                        <input type="text" id="exerciseStartTime" name="ExerciseStartTime" value="${fn:substring(exerciseStartTime,0 , 10)}" style="width: 160px"
                               class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'exerciseEndTime\')}',readOnly:true})" >
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" id="exerciseEndTime" name="ExerciseEndTime" value="${fn:substring(exerciseEndTime,0 , 10)}" style="width: 160px"
                               class="Wdate  endTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'exerciseStartTime\')}',readOnly:true})">
                    </div>
                </div>
            </c:if>

                <%--含有考试项目--%>
            <c:if test="${permissionExam }">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间</label>
                        <div class="col-7 info">
                            <input type="text" id="examStartTime" name="examStartTime" value="${fn:substring(examStartTime,0 , 16)}" style="width: 160px"
                                   class="Wdate" <c:if test="${examPermission }">data-class="ing"</c:if>  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${fn:substring(examEndTime,0 , 16)}" style="width: 160px"
                                   class="Wdate  endTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'examStartTime\')}',readOnly:true})">
                        </div>
                    </div>
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试次数</label>
                        <div class="col-7 info">
                            <input type="text" data-class="ing" onchange="proModify.verifyIntRetestTime(this.value)"  id="intRetestTime" name="intRetestTime" value="${intRetestTime}" style="width: 160px" onchange="createPrivateProject.verifyIntRetestTime(this.value)">
                            &nbsp;&nbsp;次&nbsp;&nbsp;
                        </div>
                    </div>
                </c:if>




                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>受训角色</label>
                    <div class="col-7 info">
                        <div  >
                          <select  class="select_box" id="role_select2" style="width:100%;" onchange="proModify.load_role_tree()">
                              <option value="-1">默认角色</option>
                          </select>
                        </div>
                    </div>
                    <%--<div class="col-2">
                        <button class="btn btn-info">选择受训角色</button>
                    </div>--%>
                </div>


                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>受训单位</label>
                    <div class="col-7 info">
                        <div  >
                            <select  class="select_box" style="width:100%;" id="department_select2">
                            </select>
                        </div>
                    </div>
                    <%--<div class="col-2">
                        <button class="btn btn-info">选择受训角色</button>
                    </div>--%>
                </div>
           <c:if test="${permissionExam || permissionExercise }">
                <div class="row form-group" style="display: none">
                    <label for="" class="col-1"><b class="text-red">*</b>题目总量</label>
                    <div class="col-7 info">
                        <input type="text" class="block" id="trainPeriod" value="">
                    </div>
                </div>
            </c:if>
            </div>
        </div>

        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">课程课程信息</h3>
                <form id="select_course_form"  class="clearfix">
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:;" class="btn btn-info" id="btn_dialog_course" style="width:120px;"><span class="fa fa-mouse-pointer"></span> 选择培训课程</a>
                        <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</a>
                    </span>
                    <span class="search">
                        <input type="hidden" id="proid" name="projectId" value="${projectBasic.id}">          <%-- 项目id--%>
                        <input type="hidden" id="roleId" name="roleId" value="">
                        <input type="text" id="courseName" placeholder="请输入课程名称" name="courseName">
                        <button class="btn" id="btn_search_course" type="button"><span class="fa fa-search"></span> 搜索</button>
                    </span>
                    <span>
                        <a href="javascript:;" class="btn all" id="btn_search_all_course"><span class="fa fa-th"></span> 全部</a>
                    </span>
                </div>
                </form>
            </div>
            <div class="box-body" >
                <%--<div style="padding: 5px 0;">

                        <div class="pull-left">
                            <a href="javascript:;" class="btn btn-info" id="btn_dialog_course"><span class="fa fa-mouse-pointer"></span> 选择培训课程</a>
                            <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</a>
                        </div>

                        <div class="pull-right">
                            <input type="hidden" id="proid" name="projectId" value="${projectBasic.id}">          &lt;%&ndash; 项目id&ndash;%&gt;
                            <input type="hidden" id="roleId" name="roleId" value="">
                            <input type="text" id="courseName" placehodler="请输入课程名称" name="courseName">
                            <a href="javascript:;" class="btn btn-info" id="btn_search_course"><span class="fa fa-search"></span> 搜索</a>
                            <a href="javascript:;" class="btn btn-info" id="btn_search_all_course"><span class="fa fa-th"></span> 全部</a>
                        </div>
                </div>--%>


                <div class="clearfix tree_list">
                    <div class="col-3 tree">
                        <div>
                            <ul id="role_tree" class="ztree"></ul>
                        </div>
                    </div>
                    <div class="col-9 list">
                        <table class="table" id="table">
                            <thead>
                            <tr >
                                <th width="50">序号</th>
                                <th width="50"><input type="checkbox" name="all" id="coursecheckAll"  ></th>
                                <th width="80">课程编号</th>
                                <th >课程名称</th>
                                <th  width="90">受训角色</th>
                                <c:if test="${permissionTrain}">
                                    <th width="60">课时</th>
                                    <th width="80">学时要求</th>
                                </c:if>
                                <c:if test="${permissionExam || permissionExercise }">
                                    <th width="80">题库总量</th>
                                </c:if>
                                <c:if test="${permissionExam }">
                                    <th width="80">必选题量</th>
                                </c:if>
                                <th width="200" style="text-align: center">查看</th>
                            </tr>
                            </thead>
                            <%--授权课程列表--%>
                            <tbody id="select_course_list">

                            </tbody>
                        </table>

                    </div>
                </div>
                <%--分页--%>

            </div>
        </div>
        <div class="page text-right"  style="margin-top:-30px;margin-bottom:30px;">
            <ul id="select_course_page"></ul>
        </div>
<%--含有考试项目--%>
<c:if test="${permissionExam }">
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">组卷策略信息</h3>
            </div>
            <div class="box-body">
                <div style="padding: 5px 0;">
                    <form id="select_examstrategy_form"  class="clearfix">
                        <div class="pull-left">
                            <%--<a href="javascript:;" class="btn btn-info" id="btn_batch_examstrategy"><span>批量设置</span></a>--%>
                        </div>
                    </form>
                </div>


                <div class="clearfix tree_list">
                    <div class="col-3 tree">
                        <div>
                            <ul id="stragegy_role_tree" class="ztree"></ul>
                        </div>
                    </div>
                    <div class="col-9 list">
                        <div class="box-body">
                            <form id="stragegyForm">
                                <div>
                                    <ul class="clearfix combination" id="stragegy_title">

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
                                        <th>总分</th>
                                    </tr>
                                    </thead>
                                    <tbody id="stragegy_body">

                                    </tbody>
                                </table>
                            </form>
                        </div>

                    </div>
                </div>

            </div>
        </div>
</c:if>

    <div class="box">
        <div class="box-header clearfix">
            <h3 class="box-title pull-left">培训人员信息</h3>
            <form id="select_user_form" class="clearfix" >
            <div class="pull-right search-group">
                <span>
                    <a href="javascript:;" class="btn btn-info" id="btn_dialog_user" style="width:120px;"><span class="fa fa-mouse-pointer"></span> 选择其他人员</a>
                    <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_user"><span class="fa fa-trash-o"></span> 批量删除</a>
                </span>
                <span class="search">
                    <input type="hidden" id="pro_user_id" name="projectId" value="${projectBasic.id}">          <%-- 项目id--%>
                    <input type="text" id="userName" placeholder="请输入用户名称" name="userName">
                    <button type="button" class="btn" id="btn_search_user"><span class="fa fa-search"></span> 搜索</button>

                </span>
                <span>
                    <a href="javascript:;" class="btn all" id="btn_search_all_user"><span class="fa fa-th"></span> 全部</a>
                </span>
            </div>
            </form>
        </div>
        <div class="box-body">
            <%--<div style="padding: 5px 0;">

                <div class="pull-left">

                </div>

                <div class="pull-right">
                    <input type="hidden" id="pro_user_id" name="projectId" value="${projectBasic.id}">          &lt;%&ndash; 项目id&ndash;%&gt;
                    <input type="text" id="userName" placehodler="请输入用户名称" name="userName">
                    <a href="javascript:;" class="btn btn-info" id="btn_search_user"><span class="fa fa-search"></span> 搜索</a>
                    <a href="javascript:;" class="btn btn-info" id="btn_search_all_user"><span class="fa fa-th"></span> 全部</a>
                </div>
            </div>--%>
            <div class="list">
                <table class="table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th><input type="checkbox" name="all" id="usercheckAll"  ></th>
                        <th>姓名</th>
                        <th>受训角色</th>
                        <th>部门信息</th>
                    </tr>
                    </thead>
                    <%--授权课程列表--%>
                    <tbody id="select_user_list">

                    </tbody>
                </table>
                <%--分页--%>
                <div class="page text-right" style="margin-top:5px;">
                    <ul id="select_user_page"></ul>
                </div>

            </div>
        </div>
    </div>

   </div>
  </div>

</div>
<!--隐藏字段-->
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">  <%--项目类型编号--%>
<input type="hidden" id="subjectId" name="subjectId" value="${subjectid}">              <%-- 主题编号--%>


<input type="hidden" id="projectId" name="projectId" value="${projectBasic.id}">                               <%--项目主键--%>
<input type="hidden" id="parentSource" name="parentSource" value="projectModify">
    <jsp:include page="${path}/common/footer" />


<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/admin/proManager/proModify.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/stragegy_update.js"></script>


<script>
    var page = new page();
    var page1 = new page1();


    $(function(){
        var basePath = "<%=basePath%>";
     var options={
        'permissionExam':${permissionExam},
        'permissionTrain':${permissionTrain},
        'permissionExercise':${permissionExercise}
      }
        proModify.init(basePath,page,page1,'${projectInfo.projectStatus}',options);

        stragegy_update.initMethod('${projectInfo.projectStatus}','${examPermission}');

    })


</script>





</body>
</html>
