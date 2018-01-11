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
        .nav{display: none}
    </style>
    <%--select2--%>
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
</head>
<body >
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


        <div class="box">
            <div class="box-header">
                <h3 class="box-title">高级设置</h3>
                <form id="select_course_form" class="clearfix" >
                    <div class="pull-right search-group">
                        <input type="hidden" id="pro_user_id" name="projectId" value="${projectId}">          <%-- 项目id--%>
                    </div>
                </form>
            </div>
            <div class="box-body">
                <table  class="table">
                    <thead>
                    <tr>
                        <th rowspan="2">课程信息</th>
                       <%-- <th colspan="3">默认角色 <div class="switch"><span></span></div></th>--%>
                        <c:forEach items="${trainRoles}" var="trainRole" varStatus="i">
                        <th colspan="3">${trainRole.roleName} <div class="switch_btn switchAll on" id="switch-train-role-${trainRole.roleId}" data-index="${i.index}"><span></span></div></th>
                        </c:forEach>
                    </tr>
                    <tr>
                     <c:forEach  begin="1" end="${fn:length(trainRoles)}">
                        <th>按钮</th>
                        <th>应修学时/分</th>
                        <th>必选题量</th>
                     </c:forEach>
                    </tr>
                    </thead>
                    <tbody id="select_course_role">
                    <tr>
                        <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>

                    </tbody>
                </table>
                <%--分页--%>
                <div class="page text-right" style="margin-top:5px;">
                    <ul id="select_course_page"></ul>
                </div>
            </div>
        </div>

        <div class="btn-group text-right" style="margin-bottom:20px;">
            <a href="javascript:;" class="btn btn-info " id="index"><span class="fa fa-home"></span> 回到首页</a>
            <a href="javascript:;" class="btn btn-info " id="pev"><span class="fa  fa-long-arrow-left"></span> 上一步</a>
            <a href="javascript:;" class="btn btn-info" id="next"><span class="fa fa-long-arrow-right"></span> 下一步</a>
        </div>

    </div>
  </div>
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">  <%--项目类型编号--%>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">              <%-- 主题编号--%>
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectStatus}">              <%-- 项目状态--%>
<input type="hidden" id="examPermission" name="examPermission" value="${examPermission}">
<input type="hidden" id="parentSource" name="parentSource" value="createProject">
<input type="hidden" id="train-role-length" value="${fn:length(trainRoles)}">

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step2.js"></script>

<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step4.js"></script>
<script>
    var page = new page();
    $(function(){
        options={
            'permissionExam':${permissionExam},
            'permissionTrain':${permissionTrain},
            'permissionExercise':${permissionExercise},
        }
        var winH = $(window).height();
        $('#main').css('min-height',winH - 190);
        var basePath = "<%=basePath%>";
        step4.init(basePath,page,options);
/*
        $('.switch').click(function(){
            $(this).toggleClass('on');
        });*/
        /*$('.switchAll').click(function(){
            if('3' == $("#projectStatus").val()){
              $(this).addClass('on');
            }else{
              $(this).toggleClass('on');
            }
      });*/
    })
</script>

</body>
</html>
