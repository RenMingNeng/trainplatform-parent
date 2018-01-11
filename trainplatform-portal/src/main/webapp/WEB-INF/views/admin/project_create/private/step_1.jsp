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
    <title>创建项目</title>
    <jsp:include page="${path}/common/style" />
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <%--select2--%>
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <style>
        .tree_list{height:auto;}
        .tree_list .tree{width:300px;padding-right:0;}
        .tree_list .list{margin-left:310px;}
        .index{display: none;}
        .box{margin-top:20px;margin-bottom:20px;}
        .page{margin-bottom:0;}
        .btn-group .btn{height: 36px;line-height: 34px;width: 85px;color: #fff;background: #2487de;border-color:#2487de}
        .btn-group .btn.unclick{background: #ccc;border: 1px solid #ccc;}
        .nav{display: none}
        .select2-dropdown{display:none;}
        .por{position: relative;}
        .tree_containenr{display: none;padding:10px;}
        .select2-selection.select2-selection--multiple{padding-right:30px;}
        .tree_containenr .btn{width:60px;height:30px;line-height: 30px;margin-right:0;}
        .pos{position: absolute;width:100%;background: #fff;z-index:10000;border-width:0 2px 2px 2px;border-style:solid;border-color:#b9dfff;margin-top:-2px;background: #f6f6f6;}
        .icon{position: absolute;width:16px;height:22px;background: url("<%=resourcePath%>static/global/js/My97DatePicker/skin/datePicker.gif");right:4px;top:50%;margin-top:-11px;}
        .select2-container--default .select2-selection--multiple .select2-selection__clear{display: none;}
        .select2-container .select2-search--inline .select2-search__field{margin-top:12px;}
        .por.show .select2.select2-container .select2-selection--multiple{border-width: 2px; border-color: #b9dfff; }
        .por.show .tree_containenr{display: block}
    </style>
</head>
<body>
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>



<div id="main">
    <div class="area">
        <%--<div id="addr" class="clearfix">
            <div class="pull-left">
                <a href="project_index.html">首页</a><span class="fa fa-angle-right"></span><a href="javascript:;">创建项目</a>
            </div>
        </div>--%>
        <%--<div class="back-btn" style="margin-bottom: 10px ">
            <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
            <a href="javascript:;" class="btn btn-info" id="btn_project_save_publish"><span class="fa fa-paper-plane"></span> 发布</a>
        </div>--%>




        <div class="box " style="padding-bottom:10px;" >
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                        <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>

            <div class="box-body project-details" >

                <div class="row form-group" <c:if test="${empty subjectName}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>项目主题：</label>
                    <div class="col-7">
                        <input type="text" id="subjectName" class="block" value="${subjectName}" disabled="disabled" >
                    </div>
                </div>

                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>项目名称：</label>
                    <div class="col-7">
                        <input type="text" class="block" id="projectName" value="${projectName}">
                    </div>
                </div>



                <div class="row form-group" <c:if test="${projectTypeNo==examType}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>项目时间：</label>
                    <input type="hidden" id="systemTime" name="systemTime" value="${systemTime}"/>
                    <div class="col-7">
                        <input type="text" id="projectBeginTime" name="projectBeginTime" value="${fn:substring(projectBeginTime,0 , 10)}" style="width: 160px"
                               class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'projectEndTime\')}',readOnly:true})" >
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" id="projectEndTime" name="projectEndTime" value="${fn:substring(projectEndTime,0 , 10)}" style="width: 160px"
                               class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'projectBeginTime\')}',readOnly:true})">
                    </div>
                </div>

                <c:if test="${permissionExam}">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间：</label>
                        <div class="col-7">
                            <input type="text" id="examBeginTime" name="examBeginTime" value="${fn:substring(examBeginTime,0 , 16)}" style="width: 160px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${fn:substring(examEndTime,0 , 16)}" style="width: 160px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'examBeginTime\')}',readOnly:true})">
                        </div>
                    </div>
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试次数：</label>
                        <div class="col-7">
                            <input type="text"  id="intRetestTime" name="intRetestTime" value="${intRetestTime}" style="width: 160px" onchange="step1.verifyIntRetestTime(this.value)">
                            &nbsp;&nbsp;次&nbsp;&nbsp;
                        </div>
                    </div>
                </c:if>


             <%--   <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>受训角色</label>
                    <div class="col-7">
                        <div>
                            <select  class="select_box" id="role_select2" style="width:100%;" >
                                <option value="-1" >默认角色</option>
                            </select>
                        </div>
                    </div>
                </div>--%>


                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red"></b>受训单位：</label>
                    <div class="col-7">
                        <div  class="por">
                            <select  onclick="step1.selectDept(this)" class="select_box" style="width:100%;" id="department_select2">
                            </select>
                            <div class="pos tree_containenr">
                                <div class="search-group">
                                    <button class="btn btn-info" onclick="step1.finish()">确定</button>
                                    <button class="btn btn-info" onclick="step1.close();">取消</button>
                                    <label style="line-height: 30px;"><input type="checkbox" id="isChecked" style="vertical-align: middle;margin: -2px 0 0 20px;"/>选中子节点人员</label>
                                </div>
                                <ul id="group_tree" class="ztree"></ul>

                            </div>
                            <span class="icon"></span>
                        </div>
                    </div>
                </div>

                <div class="row form-group" style="display: none">
                    <label for="" class="col-1"><b class="text-red">*</b>项目周期：</label>
                    <div class="col-7">
                        <input type="text" class="block" id="trainPeriod" value="">
                    </div>
                </div>
            </div>
        </div>

        <div class="btn-group text-right mg_b_20">
            <a href="javascript:;" class="btn btn-info" id="next" style="margin-right: 10px;"><span class="fa fa-long-arrow-right"></span> 下一步</a>
        </div>

    </div>
  </div>
<!--隐藏字段-->
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">  <%--项目类型编号--%>
<input type="hidden" id="subjectId" name="subjectId" value="${subjectId}">              <%-- 主题编号--%>
<input type="hidden" id="projectId" name="projectId" value="${projectBasic.id}">                               <%--项目主键--%>
<input type="hidden" id="project_name" name="project_name" value="${projectBasic.projectName}">              <%-- 项目名称--%>
<input type="hidden" id="subject_id" name="subject_id" value="${projectBasic.subjectId}">
<input type="hidden" id="project_type" name="project_type" value="${projectBasic.projectType}">
<input type="hidden" id="train_period" name="train_period" value="${projectBasic.trainPeriod}">
<input type="hidden" id="project_mode" name="project_mode" value="${projectBasic.projectMode}">
<input type="hidden" id="project_status" name="project_status" value="${projectBasic.projectStatus}">
<input type="hidden" id="project_train_info" name="project_train_info" value="${projectBasic.projectTrainInfo}">
<input type="hidden" id="project_exercise_info" name="project_exercise_info" value="${projectBasic.projectExerciseInfo}">
<input type="hidden" id="project_exam_info" name="project_exam_info" value="${projectBasic.projectExamInfo}">
<input type="hidden" id="project_open" name="project_open" value="${projectBasic.projectOpen}">
<input type="hidden" id="createUser" name="createUser" value="${projectBasic.createUser}">
<input type="hidden" id="createTime" name="createTime" value="${projectBasic.createTime}">
<input type="hidden" id="operUser" name="operUser" value="${projectBasic.operUser}">
<input type="hidden" id="operTime" name="operTime" value="${projectBasic.operTime}">
<input type="hidden" id="parentSource" name="parentSource" value="step1">

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/select2/js/select2.full.js"></script>
<%--poshytip--%>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
<%--x-editable--%>

<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step1.js"></script>
<script src="<%=resourcePath%>static/global/js/common/groupTreeCheckbox.js"></script>
<script>

    $(function(){
        options={
            'permissionExam':${permissionExam},
            'permissionTrain':${permissionTrain},
            'permissionExercise':${permissionExercise},
            'examType':"${examType}"
        }
        var winH = $(window).height();
        $('#main').css('min-height',winH - 440);
        var basePath = "<%=basePath%>";
        step1.init(basePath,options);

       /* $(document).delegate('.select2-container--default','click',function(){
            if( $(this).hasClass('select2-container--open') ){
               step1.selectDept();
            }
        })*/
       $(document).delegate('.select2','click',function(){
           var $this = $(this);
           var $pro = $this.parents('.pro');
           $pro.addClass('show');
           step1.selectDept();
       })
        $(document).click(function(e){
            var target = e.target;
            if( $(target).parents('.por').length ===0 ){
                $('.por').removeClass('show');
            }
        })

    })
</script>
<script type="text/javascript">

    groupTreeCheckbox.init();

</script>

</body>
</html>
