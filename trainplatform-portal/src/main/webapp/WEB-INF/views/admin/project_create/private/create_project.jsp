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
    <style>
        .tree_list{height:auto;}
        .tree_list .tree{width:300px;padding-right:0;}
        .tree_list .list{margin-left:310px;}
        .index{display: none;}
        .box{margin-top:20px;margin-bottom:20px;}
        .page{margin-bottom:0;}
        .btn-group .btn{height: 36px;line-height: 34px;width: 85px;color: #fff;background: #2487de;border-color:#2487de}
        .btn-group .btn.unclick{background: #ccc;border: 1px solid #ccc;}
        .w_300{width:300px;}
        .w_900{width:900px;}
        .pd_10{padding:10px;}
        .pr_10{padding-right:10px;}
        .pd_t_10{padding-top:10px;}
        .bd_t{border-top:1px solid #eee;}
        .mg_t_10{margin-top:10px;}
    </style>
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
</head>
<body  onbeforeunload="stragegySelect.stragegy_save();">
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>



<div id="main">
    <div class="area" style="overflow: hidden;">
        <%--<div id="addr" class="clearfix">
            <div class="pull-left">
                <a href="project_index.html">首页</a><span class="fa fa-angle-right"></span><a href="javascript:;">创建项目</a>
            </div>
        </div>--%>
        <%--<div class="back-btn" style="margin-bottom: 10px ">
            <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
            <a href="javascript:;" class="btn btn-info" id="btn_project_save_publish"><span class="fa fa-paper-plane"></span> 发布</a>
        </div>--%>




        <div class="box index_1 index" style="padding-bottom:10px;" >
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目基础信息</h3>
                <div class="pull-right search-group">
                    <%--<a href="javascript:;" class="btn btn-info " id="pev"><span class="fa  fa-long-arrow-left"></span> 上一步</a>
                    <a href="javascript:;" class="btn btn-info" id="next"><span class="fa fa-long-arrow-right"></span> 下一步</a>
                    <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
                    <a href="javascript:;" class="btn btn-info" id="btn_project_save_publish"><span class="fa fa-paper-plane"></span> 发布</a>--%>
                </div>
            </div>

            <div class="box-body project-details" >

                <div class="row form-group" <c:if test="${empty subjectName}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>项目主题</label>
                    <div class="col-7 info">
                        <input type="text" id="subjectName" class="block" value="${subjectName}" disabled="disabled" >
                    </div>
                </div>

                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>项目名称</label>
                    <div class="col-7 info">
                        <input type="text" class="block" id="projectName" value="${projectName}">
                    </div>
                </div>



                <div class="row form-group" <c:if test="${projectTypeNo==examType}">style="display: none"</c:if>>
                    <label for="" class="col-1"><b class="text-red">*</b>项目时间</label>
                    <div class="col-7 info">
                        <input type="text" id="projectBeginTime" name="projectBeginTime" value="${fn:substring(projectBeginTime,0 , 10)}" style="width: 160px"
                               class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'projectEndTime\')}',readOnly:true})" >
                        &nbsp;&nbsp;至&nbsp;&nbsp;
                        <input type="text" id="projectEndTime" name="projectEndTime" value="${fn:substring(projectEndTime,0 , 10)}" style="width: 160px"
                               class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'projectBeginTime\')}',readOnly:true})">
                    </div>
                </div>

                <c:if test="${permissionExam}">
                    <div class="row form-group">
                        <label for="" class="col-1"><b class="text-red">*</b>考试时间</label>
                        <div class="col-7 info">
                            <input type="text" id="examBeginTime" name="examBeginTime" value="${fn:substring(examBeginTime,0 , 16)}" style="width: 160px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',maxDate:'#F{$dp.$D(\'examEndTime\')}',readOnly:true})">
                            &nbsp;&nbsp;至&nbsp;&nbsp;
                            <input type="text" id="examEndTime" name="examEndTime" value="${fn:substring(examEndTime,0 , 16)}" style="width: 160px"
                                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm',minDate:'#F{$dp.$D(\'examBeginTime\')}',readOnly:true})">
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


                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red">*</b>受训角色</label>
                    <div class="col-7 info">
                        <div>
                            <select  class="select_box" id="role_select2" style="width:100%;" >
                                <option value="-1" >默认角色</option>
                            </select>
                        </div>
                    </div>
                </div>


                <div class="row form-group">
                    <label for="" class="col-1"><b class="text-red"></b>受训单位</label>
                    <div class="col-7 info">
                        <div  >
                            <select  class="select_box" style="width:100%;" id="department_select2">
                            </select>
                        </div>
                    </div>
                </div>

                <div class="row form-group" style="display: none">
                    <label for="" class="col-1"><b class="text-red">*</b>项目周期</label>
                    <div class="col-7 info">
                        <input type="text" class="block" id="trainPeriod" value="">
                    </div>
                </div>
            </div>
        </div>

        <div class="box  index_2 index" style="display: none;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">课程信息</h3>
                <form id="select_course_form"  class="clearfix">
                <input type="hidden" id="proid" name="projectId" value="${projectBasic.id}">          <%-- 项目id--%>
                <input type="hidden" id="roleId" name="roleId" value="">
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:;" class="btn btn-info" id="btn_dialog_course" style="width:120px;"><span class="fa fa-mouse-pointer"></span> 选择培训课程</a>
                        <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_course"><span class="fa fa-trash-o"></span> 批量删除</a>
                    </span>
                    <span class="search">
                        <input type="text" id="courseName" placeholder="请输入课程名称" name="courseName">
                        <button type="button" href="javascript:;" class="btn" id="btn_search_course"><span class="fa fa-search"></span> 搜索</button>
                    </span>
                    <span>
                         <a href="javascript:;" class="btn all" id="btn_search_all_course"><span class="fa fa-th"></span> 全部</a>
                    </span>
                </div>
                </form>
            </div>
            <div class="box-body">
                <%--<div style="padding: 5px 0;">
                    <form id="select_course_form"  class="clearfix">
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
                    </form>
                </div>--%>


                <div class="clearfix tree_list">
                    <div class="pull-left tree">
                        <div>
                            <ul id="role_tree" class="ztree"></ul>
                        </div>
                    </div>
                    <div class="list">
                        <table class="table">
                            <thead>
                            <tr>
                                <th width="40">序号</th>
                                <th width="40"><input type="checkbox" name="all" id="coursecheckAll"  ></th>
                                <th  width="100">课程编号</th>
                                <th>课程名称</th>
                                <th  width="80">受训角色</th>
                    <c:if test="${permissionTrain}">
                                <th  width="60">课时</th>
                                <th  width="80">学时要求</th>
                    </c:if>
                   <c:if test="${permissionExercise || permissionExam}">
                               <th width="80">题库总量</th>
                   </c:if>
                   <c:if test="${permissionExam}">
                                    <th width="80">必选题量</th>
                    </c:if>
                                <th  width="200">查看</th>
                            </tr>
                            </thead>
                            <%--授权课程列表--%>
                            <tbody id="select_course_list">

                            </tbody>
                        </table>

                    </div>
                </div>
                <%--分页--%>
                <div class="page text-right"  style="margin-top:5px;">
                    <ul id="select_course_page"></ul>
                </div>
            </div>
        </div>


    <div class="box  index_3 index" style="display: none;">
        <div class="box-header clearfix">
            <h3 class="box-title pull-left">培训人员信息</h3>
            <form id="select_user_form" class="clearfix" >
            <div class="pull-right search-group">
                <input type="hidden" id="pro_user_id" name="projectId" value="${projectBasic.id}">          <%-- 项目id--%>
                <span>
                    <a href="javascript:;" style="width:120px;" class="btn btn-info" id="btn_dialog_user"><span class="fa fa-mouse-pointer"></span> 选择其他人员</a>
                    <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_user"><span class="fa fa-trash-o"></span> 批量删除</a>
                </span>
                <span class="search">
                    <input type="text" id="userName" placeholder="请输入用户名称" name="userName">
                    <button type="button" href="javascript:;" class="btn" id="btn_search_user"><span class="fa fa-search"></span> 搜索</button>
                </span>
                <span>
                    <a href="javascript:;" class="btn all" id="btn_search_all_user"><span class="fa fa-th"></span> 全部</a>
                </span>
            </div>
            </form>
        </div>
        <div class="box-body">
            <%--<div style="padding: 5px 0;">
                <form id="select_user_form" class="clearfix" >
                <div class="pull-left">
                    <a href="javascript:;" class="btn btn-info" id="btn_dialog_user"><span class="fa fa-mouse-pointer"></span> 选择其他人员</a>
                    <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_user"><span class="fa fa-trash-o"></span> 批量删除</a>
                </div>

                <div class="pull-right">
                    <input type="hidden" id="pro_user_id" name="projectId" value="${projectBasic.id}">          &lt;%&ndash; 项目id&ndash;%&gt;
                    <input type="text" id="userName" placehodler="请输入用户名称" name="userName">
                    <a href="javascript:;" class="btn btn-info" id="btn_search_user"><span class="fa fa-search"></span> 搜索</a>
                    <a href="javascript:;" class="btn btn-info" id="btn_search_all_user"><span class="fa fa-th"></span> 全部</a>
                </div>
                </form>
            </div>--%>
            <div class="list">
                <table class="table">
                    <thead>
                    <tr>
                        <th width="80">序号</th>
                        <th width="80"><input type="checkbox" name="all" id="usercheckAll"  ></th>
                        <th width="200">姓名</th>
                        <th width="150">受训角色</th>
                        <th >部门信息</th>
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

    <c:if test="${permissionExam}">
        <div class="box  index_4 index" style="display: none;margin-bottom:70px;">
            <div class="box-header">
                <h3 class="box-title">组卷策略信息</h3>
            </div>
            <div class="box-body">
                <form id="stragegyForm">
                <div>
                    <ul class="clearfix combination" id="stragegy_title" style="border:none;">

                    </ul>
                </div>
                <div class="clearfix">
                    <div class="pull-left w_300 pr_10">
                        <table  class="table">
                            <thead>
                                <tr>
                                    <th>受训角色</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr height="125">
                                    <td>受训角色1</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="pull-right w_900">
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
                    </div>
                </div>

                <div class="clearfix pd_t_10 bd_t mg_t_10">
                    <div class="pull-left w_300 pd_10">
                        受训角色
                    </div>
                    <div class="pull-right w_900">
                            <div>
                                <ul class="clearfix combination" style="border:none;">

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
                                <tbody>

                                </tbody>
                            </table>
                    </div>
                </div>
                </form>
            </div>
        </div>
    </c:if>
        <%--<div class="box index">
            <div class="box-header">
                <h3 class="box-title">高级设置</h3>
            </div>
            <div class="box-body">
                <table  class="table">
                    <thead>
                    <tr>
                        <th>学员信息</th>
                        <th>受训角色1 <div class="switch"><span></span></div></th>
                        <th>受训角色2 <div class="switch"><span></span></div></th>
                        <th>受训角色1 <div class="switch"><span></span></div></th>
                        <th>受训角色2 <div class="switch"><span></span></div></th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>陶杨</td>
                            <td>默认受训  <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                            <td>默认受训  <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                        </tr>
                        <tr>
                            <td>陶杨</td>
                            <td>默认受 色 <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                            <td>默认 角色 <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                        </tr>
                        <tr>
                            <td>陶杨</td>
                            <td>默认 角色 <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                            <td>默认角色  <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                        </tr>
                        <tr>
                            <td>陶杨</td>
                            <td>默认受训  <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                            <td>默认 角色 <div class="switch"><span></span></div></td>
                            <td>受训角色2 <div class="switch"><span></span></div></td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>--%>

        <div class="btn-group text-right">
            <a href="javascript:;" class="btn btn-info " id="pev"><span class="fa  fa-long-arrow-left"></span> 上一步</a>
            <a href="javascript:;" class="btn btn-info" id="next"><span class="fa fa-long-arrow-right"></span> 下一步</a>
            <a href="javascript:;" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
            <a href="javascript:;" class="btn btn-info" id="btn_project_save_publish"><span class="fa fa-paper-plane"></span> 发布</a>
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


<input type="hidden" id="createUser" name="projectId" value="${projectBasic.createUser}">
<input type="hidden" id="createTime" name="projectId" value="${projectBasic.createTime}">
<input type="hidden" id="operUser" name="projectId" value="${projectBasic.operUser}">
<input type="hidden" id="operTime" name="projectId" value="${projectBasic.operTime}">
<input type="hidden" id="parentSource" name="parentSource" value="createProject">

<jsp:include page="${path}/common/footer" />
<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/create_project.js"></script>
<script>
    var page = new page();
    var page1 = new page1()
    $(function(){
        options={
            'permissionExam':${permissionExam},
            'permissionTrain':${permissionTrain},
            'permissionExercise':${permissionExercise},
            'examType':"${examType}"
        }
        var winH = $(window).height();
        $('#main').css('min-height',winH - 190);
        var basePath = "<%=basePath%>";
        createPrivateProject.init(basePath,page,page1,options);
        var current = 0;
       toggleBox(current);
        $('#next').click(function(){
            if( current === 0 ){
                layer.msg('保存',{time : 1000});
            }
            current++;
            toggleBox(current);
        });
        $('#pev').click(function(){
            current--;
            toggleBox(current);
        });
        function toggleBox(index){
            var len = $('.index').length;
            $('#btn_project_save_publish').hide();
            if( index === 0 ){
                $('#pev').hide();
            }
            if( index > 0 && index != len ){
                $('#pev').show();
                $('#next').show();
            }
            if( index === len - 1  ){
                $('#next').hide();
                $('#btn_project_save_publish').show();
            }
            $('.index').eq(index).show().siblings('.index').hide();
        };
        $('.switch').click(function(){
            $(this).toggleClass('on');
        });
        /*$('thead .switch').click(function(){
            var $this = $(this);
            var $table = $this.parents('table');
            var checked = $this.hasClass('on');
            var $p = $this.parent();
            var index = $p.index();
            var switchs = $table.find('tbody tr td').eq(index).find('.switch');
            console.log( switchs.length );
            checked ? switchs.addClass('on') : switchs.removeClass('on');
        })*/
    })
</script>

</body>
</html>
