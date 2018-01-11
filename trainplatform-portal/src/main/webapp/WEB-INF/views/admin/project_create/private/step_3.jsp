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
        .box1{margin-top:10px;padding:0 10px 10px 0; font-size: 14px; display: none;}
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
<body>
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


    <div class="box" >
        <div class="box-header clearfix">
            <h3 class="box-title pull-left">培训人员信息</h3>
            <form id="select_user_form" class="clearfix" >
            <div class="pull-right search-group">
                <input type="hidden" id="pro_user_id" name="projectId" value="${projectId}">          <%-- 项目id--%>
                <span>
                    <a href="javascript:;" class="btn btn-info" id="btn_dialog_user"><span class="fa fa-mouse-pointer"></span> 选择人员</a>
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
                        <th width="80"><input type="checkbox" name="all" id="checkAll"  ></th>
                        <th width="200">姓名</th>
                        <th >部门信息</th>
                    </tr>
                    </thead>
                    <%--授权课程列表--%>
                    <tbody id="select_user_list">
                        <tr>
                         <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                        </tr>
                    </tbody>
                </table>
                <%--分页--%>
                <div class="page text-right  bg_page" style="margin-top:0px;">
                    <ul id="select_user_page"></ul>
                </div>
                <div id="allot" class=" box1 text-right index">
                    <input type="checkbox" id="select"/>
                    <span>是否为该项目分配相应的角色</span>
                </div>
            </div>
        </div>
    </div>
    <div class="btn-group text-right" style="margin-bottom:20px;">
        <a href="javascript:;" class="btn btn-info " id="index"><span class="fa fa-home"></span> 回到首页</a>
        <a href="javascript:;" class="btn btn-info " id="pev"><span class="fa  fa-long-arrow-left"></span> 上一步</a>
        <a href="javascript:;" class="btn btn-info" id="next" operate-type="1"><span class="fa fa-long-arrow-right"></span> 下一步</a>
        <a href="javascript:;" class="btn btn-info" id="save" style="display: none"><span class="fa fa-paper-plane"></span> 保存</a>
    </div>
    </div>
  </div>
<!--隐藏字段-->
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">  <%--项目类型编号--%>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">              <%-- 主题编号--%>
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectStatus}">              <%-- 项目状态--%>
<input type="hidden" id="examPermission" name="examPermission" value="${examPermission}">
<input type="hidden" id="parentSource" name="parentSource" value="createProject">

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step2.js"></script>

<script type="text/javascript" src="<%=path%>/static/global/js/page1.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step3.js"></script>
<script>
    var page = new page();
    $(function(){
        options={
            'permissionExam':${permissionExam},
            'permissionTrain':${permissionTrain},
            'permissionExercise':${permissionExercise},
            'permissionRole':${flag}
        }
        var winH = $(window).height();
        $('#main').css('min-height',winH - 190);
        var basePath = "<%=basePath%>";
        step3.init(basePath,page,options);
    })
</script>

</body>
</html>
