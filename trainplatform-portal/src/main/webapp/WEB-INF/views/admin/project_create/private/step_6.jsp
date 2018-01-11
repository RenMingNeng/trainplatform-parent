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
        .combination li{width:25%;}
        .nav{display: none}
        #stragegyForm input[type="text"][readonly]{
            width:65px!important;
            cursor: pointer;
            font-style: normal;
            line-height: 24px;
            height: 24px;
            background: #f8f8f8;
            display: inline-block;
            text-align: center;
            border-width: 1px;
            border-style: solid;
            border-color: #d4e5e9 #ecf2f3 #f8f8f8 #ecf2f3;
        }
        #stragegyForm input[type="text"][readonly]:hover{
            color:#2487de
        }
    </style>
    <%--select2--%>
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
</head>
<body  onbeforeunload="stragegySelect.stragegy_save();">
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>



<div id="main">
    <div class="area" style="overflow: hidden;">
        <div class="box" >
            <div class="box-header">
                <h3 class="box-title">组卷策略信息&nbsp;&nbsp;
                    <div class="switch_btn on" title="批量设置" style="margin-top:-4px;"><span></span></div>
                </h3>

            </div>
            <div class="box-body">
                <form id="stragegyForm">
                    <div class="clearfix stragegy_list">
                        <div class="text-right">
                            <ul class="clearfix combination w_900" id="stragegy_title" style="border:none;display: inline-block">

                            </ul>
                        </div>
                        <div>

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
                </form>

                <div id="stragegyForm_list">
                    <c:if test="${count > 1}">
                        <c:forEach begin="0" end="${count-1}" varStatus="i">
                            <form id="stragegyForm_list_${i.index}" style="display: none;">
                                <div style="margin-bottom:20px;" class="stragegy_list">
                                    <div class="text-right">
                                        <ul class="clearfix combination  w_900" id="stragegy_title${i.index}" style="border:none;display: inline-block">

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
                                                <tr height="125" id="roleNames${i.index}">
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
                                                <tbody id="stragegy_body${i.index}">

                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
        </div>

        <div class="btn-group text-right" style="margin-bottom:20px;">
            <a href="javascript:;" class="btn btn-info " id="index"><span class="fa fa-home"></span> 回到首页</a>
            <a href="javascript:;" class="btn btn-info " id="pev"><span class="fa  fa-long-arrow-left"></span> 上一步</a>
            <a href="javascript:;" class="btn btn-info" id="btn_project_save_publish"><span class="fa fa-paper-plane"></span> 保存</a>
        </div>

    </div>
  </div>
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">                   <%--项目类型编号--%>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">                               <%-- 主题编号--%>
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectStatus}">
<input type="hidden" id="examPermission" name="examPermission" value="${examPermission}">
<input type="hidden" id="parentSource" name="parentSource" value="createProject">
<input type="hidden" id="source" name="source" value="${source}">
<jsp:include page="${path}/common/footer" />

<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step2.js"></script>

<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/stragegy_private_select.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/step6.js"></script>
<script>
    $(function(){
        options={
            'permissionExam':${permissionExam},
            'permissionTrain':${permissionTrain},
            'permissionExercise':${permissionExercise},
            'projectStatus':${projectStatus},
            'examPermission':${examPermission}
        }
        var winH = $(window).height();
        $('#main').css('min-height',winH - 190);
        var basePath = "<%=basePath%>";
        step6.init(basePath,options);

        var count = '${count}';
        if(count > 1){
            stragegySelect.stragegy_select_list('${projectId}', step6.permissionTrain);
        }
        $('.switch_btn').click(function(){
            $(this).toggleClass('on');

            if(count > 1){
                if($(this).hasClass("on")){
                    $("#stragegyForm").show();
                    $("#stragegyForm_list").find("form").hide();
                }else{
                    $("#stragegyForm").hide();
                    $("#stragegyForm_list").find("form").show();
                }
            }
        });

        if("3"== "${projectStatus}" && ${examPermission}){
            $(".box-body input").attr("disabled","disabled")
        }
    })
</script>

</body>
</html>
