<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.bossien.train.util.PropertiesUtils" %>
<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";

    //含有考试项目的
    String containExamType = PropertiesUtils.getValue("contain_exam_type");
    //含有培训项目的
    String containTrainType = PropertiesUtils.getValue("contain_train_type");
    //含有练习项目的
    String containExerciseType = PropertiesUtils.getValue("contain_exercise_type");
    //单独考试项目
    String examType = ProjectTypeEnum.QuestionType_3.getValue();
%>
<!DOCTYPE html>
<html>
<head>
    <title>创建公开型项目</title>
    <jsp:include page="${path}/common/style"/>
    <jsp:include page="${path}/common/script"/>

    <%--poshytip--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/poshytip/css/tip-yellowsimple.css">
    <script type="text/javascript"
            src="<%=resourcePath%>static/global/js/jquery-editable/poshytip/js/jquery.poshytip.js"></script>
    <%--x-editable--%>
    <link rel="stylesheet" href="<%=resourcePath%>static/global/js/jquery-editable/css/jquery-editable.css">
    <script type="text/javascript"
            src="<%=resourcePath%>static/global/js/jquery-editable/js/jquery-editable-poshytip.js"></script>

    <style>
        #stragegyForm input[type="text"][readonly] {
            width: 65px !important;
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

        #stragegyForm input[type="text"][readonly]:hover {
            color: #2487de
        }
        .no-width li{
            width:auto;
            margin-left:50px;
            padding:0;
        }
    </style>
</head>
<body onbeforeunload="stragegySelect_public.stragegy_save();">
<jsp:include page="${path}/super/menu">
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>

<c:set var="containExamType" value="<%=containExamType%>"></c:set>
<c:set var="containTrainType" value="<%=containTrainType%>"></c:set>
<c:set var="containExerciseType" value="<%=containExerciseType%>"></c:set>
<c:set var="examType" value="<%=examType%>"></c:set>

<div id="main" style="min-height:100px;">
    <div class="area">
        <div class="box index">
            <%--组卷策略--%>
            <div class="box-header clearfix">
                <div class="box-header clearfix">
                    <h3 class="box-title pull-left">高级设置</h3>
                </div>
                <form id="stragegyForm">
                    <div class="box-body clearfix">
                        <div class="clearfix">
                            <h4 class="pull-left" style="margin-left:10px;">组卷策略</h4>


                            <div class="pull-right no-width">
                                <ul class="clearfix combination" id="stragegy_title" style="border:none;">

                                </ul>
                            </div>
                        </div>
                    </div>
                    <table class="table">
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
                        <tr>
                            <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                        </tr>
                        </tbody>
                    </table>
                    <input type="hidden" id="roleId" name="roleId" value="-1">
                    <input type="hidden" id="roleName" name="roleName" value="默认角色">
                </form>
            </div>

        </div>
        <div class="search-group clearfix" style="margin-top:-10px;margin-bottom:20px;">
                <span style="float: right;">
                    <a href="javascript:;" class="btn btn-info " id="index"><span class="fa fa-home"></span> 回到首页</a>&nbsp;&nbsp;
                    <a href="javascript:;" class="btn btn-info" id="step_4"><span class="fa  fa-long-arrow-left"></span> 上一步</a>&nbsp;&nbsp;
                    <a href="javascript:;" class="btn btn-info" id="btn_save"><span
                            class="fa fa fa-save"></span> 保 存</a>
                </span>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">
<input type="hidden" id="projectStatus" name="projectStatus" value="${projectStatus}">
<input type="hidden" id="examStatus" name="examStatus" value="${examStatus}">
<jsp:include page="${path}/common/footer"/>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/stragegy_public_select.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/createProject/step_5.js"></script>
<script type="text/javascript">
    var options = {
        'containExamType': "<%=containExamType%>",
        'containTrainType': "<%=containTrainType%>",
        'containExerciseType': "<%=containExerciseType%>",
        'examType': "<%=examType%>"
    }
    step_5.init(options);

</script>
</body>
</html>
