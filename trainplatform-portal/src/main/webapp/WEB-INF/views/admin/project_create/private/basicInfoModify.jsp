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
    <%--select2--%>
    <link type="text/css" rel="stylesheet" href="<%=resourcePath%>static/global/js/select2/css/select2.min.css"/>
    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <style>
        .tree_list{height:auto;}
        .tree_list .tree>div{background: #f7f8f9;}
        .combination{border: none;}
        .btn-group .btn{height: 36px;line-height: 34px;width: 85px;color: #fff;background: #2487de;border-color:#2487de}
        .nav{display: none}
        .select2-dropdown{display:none;}
        .por{position: relative;}
        .w_700{width:700px!important;}
        .tree_containenr{display: none;padding:10px;}
        .select2-selection.select2-selection--multiple{padding-right:30px;}
        .tree_containenr .btn{width:60px;height:30px;line-height: 30px;margin-right:0;}
        .pos{position: absolute;width:100%;background: #fff;z-index:10000;border-width:0 2px 2px 2px;border-style:solid;border-color:#b9dfff;margin-top:-2px;background: #f6f6f6;}
        .icon{position: absolute;width:16px;height:22px;background: url("<%=resourcePath%>static/global/js/My97DatePicker/skin/datePicker.gif");right:4px;top:50%;margin-top:-11px;}
        .select2-container--default .select2-selection--multiple .select2-selection__clear{display: none;}
        .select2-container .select2-search--inline .select2-search__field{margin-top:12px;}
        .por.show .select2.select2-container .select2-selection--multiple{border-width: 2px; border-color: #b9dfff; }
        .por.show .tree_containenr{display: block}
        .select2.select2-container--default.select2-container--disabled.select2-container--focus .select2-selection--multiple{border-width: 1px;border-style: solid;border-color: #e1e1e1 transparent transparent #e1e1e1;}
    </style>
</head>


<body>
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area" >
        <%--<div id="addr" class="clearfix">
            <div class="pull-left">
                <a href="<%=resourcePath%>admin/proManager/proManagerList">项目管理</a><span class="fa fa-angle-right"></span><a href="javascript:;">修改项目</a>
            </div>
        </div>--%>

       <%-- <div class="box">
            <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
            <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span>返回</a>
        </div>--%>

        <div class="box pd_b_10">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                    <a href="javascript:;" class="btn btn-info"  id="save"><span class="fa fa-paper-plane"></span> 保存</a>

                    <a href="<%=path%>/admin/project/projectList" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>
            <div class="box-body project-details">
                <div class="row form-group" <c:if test="${empty fn:trim(subjectName)}">style="display: none"</c:if>>
                    <label class="col-1"><b class="text-red">*</b>项目主题：</label>
                    <div class="col-7">
                        <input type="text" class="block" value="${subjectName}" disabled="disabled" >
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1"><b class="text-red">*</b>项目名称：</label>
                    <div class="col-7">
                        <input type="text" class="block" data-class="ing" id="projectName" value="${projectBasic.projectName}">
                    </div>
                </div>
                <input type="hidden" id="systemTime" name="systemTime" value="${systemTime}"/>
                <%--含有培训项目--%>
             <c:if test="${permissionTrain }">
                <div class="row form-group" >
                    <label class="col-1"><b class="text-red">*</b>培训时间：</label>
                    <div class="col-7">
                           <input type="text" id="trainStartTime" name="trainStartTime" value="${fn:substring(trainStartTime,0 , 10)}" style="width: 160px"
                               class="Wdate" <c:if test="${trainPermission}">data-class="ing"</c:if>  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'trainEndTime\')}',readOnly:true})" >
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="hidden" id="trainStartTime_" name="trainStartTime_" value="${fn:substring(trainStartTime,0 , 10)}" style="width: 160px"
                               class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'trainEndTime\')}',readOnly:true})" >
                           <input type="text"  id="trainEndTime" name="trainEndTime" value="${fn:substring(trainEndTime,0 , 10)}" style="width: 160px"
                               class="Wdate endTime"  <c:if test="${projectInfo.projectStatus == '3'}">onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'trainStartTime_\')}',readOnly:true})"</c:if>
                               <c:if test="${projectInfo.projectStatus != '3'}">onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'trainStartTime\')}',readOnly:true})"</c:if>>
                    </div>
                </div>
             </c:if>
             <%--含有练习项目--%>
            <c:if test="${permissionExercise }">
                <div class="row form-group" >
                    <label class="col-1"><b class="text-red">*</b>练习时间：</label>
                    <div class="col-7">
                        <input type="text" id="exerciseStartTime" name="ExerciseStartTime" value="${fn:substring(exerciseStartTime,0 , 10)}" style="width: 160px"
                               class="Wdate" <c:if test="${exercisePermission}">data-class="ing"</c:if>  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'exerciseEndTime\')}',readOnly:true})" >
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="hidden" id="exerciseStartTime_" name="ExerciseStartTime_" value="${fn:substring(exerciseStartTime,0 , 10)}" style="width: 160px"
                               class="Wdate" data-class="ing"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'exerciseEndTime\')}',readOnly:true})" >
                        <input type="text" id="exerciseEndTime" name="ExerciseEndTime" value="${fn:substring(exerciseEndTime,0 , 10)}" style="width: 160px"
                               class="Wdate  endTime"  <c:if test="${projectInfo.projectStatus == '3'}">onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'exerciseStartTime_\')}',readOnly:true})"</c:if>
                               <c:if test="${projectInfo.projectStatus != '3'}">onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'exerciseStartTime\')}',readOnly:true})"</c:if>>
                    </div>
                </div>
            </c:if>

                <%--含有考试项目--%>
            <c:if test="${permissionExam }">
                    <div class="row form-group">
                        <label class="col-1"><b class="text-red">*</b>考试时间：</label>
                        <div class="col-7">
                            <input type="text" id="examStartTime" name="examStartTime" value="${fn:substring(examStartTime,0 , 16)}" style="width: 160px"
                                   class="Wdate" <c:if test="${examPermission }">data-class="ing"</c:if>  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'systemTime\')}',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="hidden" id="examEndTime_" name="examEndTime_" value="${fn:substring(examEndTime,0 , 16)}" style="width: 160px"
                                   class="Wdate  endTime"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'examStartTime\')}',readOnly:true})">
                            <input type="text" id="examEndTime" name="examEndTime" value="${fn:substring(examEndTime,0 , 16)}" style="width: 160px"
                                   class="Wdate  endTime"  <c:if test="${projectInfo.projectStatus == '3'}">onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'examEndTime_\')}',readOnly:true})"</c:if>
                                   <c:if test="${projectInfo.projectStatus != '3'}">onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'examStartTime\')}',readOnly:true})"</c:if>>
                        </div>
                    </div>
                    <div class="row form-group">
                        <label class="col-1"><b class="text-red">*</b>考试次数：</label>
                        <div class="col-7">
                            <input type="text" <c:if test="${examPermission }">data-class="ing"</c:if> onchange="basicInfoModify.verifyIntRetestTime(this.value)"  id="intRetestTime" name="intRetestTime" value="${intRetestTime}" style="width: 160px" onchange="basicInfoModify.verifyIntRetestTime(this.value)">
                            &nbsp;&nbsp;次&nbsp;&nbsp;
                        </div>
                    </div>
                </c:if>




                <%--<div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>受训角色</label>
                    <div class="col-7 info">
                        <div  >
                          <select  class="select_box" id="role_select2" style="width:100%;" onchange="proModify.load_role_tree()">
                              <option value="-1">默认角色</option>
                          </select>
                        </div>
                    </div>
                    &lt;%&ndash;<div class="col-2">
                        <button class="btn btn-info">选择受训角色</button>
                    </div>&ndash;%&gt;
                </div>--%>


                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red"></b>受训单位：</label>
                    <div class="col-11">
                        <div  class="por w_700">
                            <select  onclick="basicInfoModify.selectDept(this)" class="select_box" style="width:100%;" id="department_select2">
                            </select>
                            <div class="pos tree_containenr">
                                <div class="search-group">
                                    <button class="btn btn-info" onclick="basicInfoModify.finish()">确定</button>
                                    <button class="btn btn-info" onclick="basicInfoModify.close();">取消</button>
                                    <label style="line-height: 30px;"><input type="checkbox" id="isChecked" style="vertical-align: middle;margin: -2px 0 0 20px;"/>选中子节点人员</label>
                                </div>
                                <ul id="group_tree" class="ztree"></ul>

                            </div>
                            <span class="icon"></span>
                        </div>
                    </div>
                    <%--<div class="col-2">
                        <button class="btn btn-info">选择受训角色</button>
                    </div>--%>
                </div>
           <c:if test="${permissionExam || permissionExercise }">
                <div class="row form-group" style="display: none">
                    <label for="" class="col-1"><b class="text-red">*</b>题目总量：</label>
                    <div class="col-7 info">
                        <input type="text" class="block" id="trainPeriod" value="">
                    </div>
                </div>
            </c:if>
            </div>
        </div>

        <div class="clearfix" style="margin-top:-10px;margin-bottom:20px;">
            <div class="btn-group" style="float:right;">
                <a href="javascript:;" class="btn btn-info" id="next"><span class="fa fa-long-arrow-right"></span> 下一步</a>
            </div>
        </div>
   </div>
  </div>

</div>
<!--隐藏字段-->
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">  <%--项目类型编号--%>
<input type="hidden" id="subjectId" name="subjectId" value="${projectBasic.subjectId}">              <%-- 主题编号--%>

<input type="hidden" id="projectStatus" name="projectStatus" value="${projectInfo.projectStatus}">
<input type="hidden" id="examPermission" name="examPermission" value="${examPermission}">
<input type="hidden" id="projectId" name="projectId" value="${projectBasic.id}">                               <%--项目主键--%>
<input type="hidden" id="parentSource" name="parentSource" value="basicInfoModify">

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step2.js"></script>

<script src="<%=resourcePath%>static/global/js/common/groupTreeCheckbox.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=path%>/static/global/js/admin/project_create/private/basicInfoModify.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/stragegy_update.js"></script>

<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step1.js"></script>

<script>
    window.groupTreeCheckbox.initData = basicInfoModify.initData;
    window.groupTreeCheckbox.check = basicInfoModify.check;
    groupTreeCheckbox.init();
    $(function(){
        var basePath = "<%=basePath%>";
     var options={
        'permissionExam':${permissionExam},
        'permissionTrain':${permissionTrain},
        'permissionExercise':${permissionExercise}
      }
        basicInfoModify.init(basePath,'${projectInfo.projectStatus}',options);
        $(document).delegate('.select2','click',function(){
            var $this = $(this);
            var $pro = $this.parents('.pro');
            if( !$this.hasClass('select2-container--disabled') ){
                $pro.addClass('show');
                step1.selectDept();
            }
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
    /*var setting = {
        check: {
            enable: true,
            chkboxType :  { "Y" : "s", "N" : "s" }
            //nocheckInherit: true
        },
        async: {
            enable: true,
            url:appPath + "/admin/user/groupTree", //异步查询部门节点请求url
            autoParam:["id", "type"],
            otherParam:{"otherParam":"zTreeAsyncTest"},
            type:"post",
            dataType:"json",
            dataFilter: filter
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        view: {
            selectedMulti: false
        },
        callback: {
            onClick: function(e,treeId, treeNode){console.log(treeNode)
                // 获取左侧选中节点
                // newUserList.search(treeNode);
                window.groupTree_search(treeNode);
            },
            beforeExpand: beforeExpand,
            onRightClick: function(event, treeId, treeNode){
                onRightClick(event, treeId, treeNode);
            },
            onCheck: function (event, treeId, treeNode) {
                window.groupTree_check(treeNode);
            }
            //onAsyncSuccess: onAsyncSuccess,
            //onAsyncError: onAsyncError
        }
    };*/



</script>




</body>
</html>
