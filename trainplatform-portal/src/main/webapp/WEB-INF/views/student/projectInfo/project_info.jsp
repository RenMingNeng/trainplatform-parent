<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8"/>
    <title>博晟 | 培训平台-学员-项目详情</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .nav{display: none;}
    </style>
</head>
<body>
<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <%--<div id="addr" class="clearfix">
            <div class="pull-left">
                <div class="box-title"><span><a href="<%=resourcePath%>/student">我的项目&nbsp;<i class="fa fa-angle-right"></i></a><a href="javascript:;">项目详情&nbsp;</a></span></div>
            </div>
        </div>--%>
        <!-- 项目基础信息 -->
        <div class="box pd_b_10">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目详情</h3>
                <div class="pull-right search-group">
                    <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <div class="col-12 form-group mg_b_5" <c:if test="${empty fn:trim(projectInfo.subjectName)}">style="display: none"</c:if>>
                        <label for="" class="col-1">培训主题：</label>
                        <div class="col-11 info">${projectInfo.subjectName}</div>
                    </div>
                    <div class="col-12 form-group mg_b_5">
                        <label for="" class="col-1">项目名称：</label>
                        <div class="col-11 info">
                            ${projectInfo.projectName}
                        </div>
                    </div>
                    <%--<c:if test="${! empty projectInfo.roleName and projectInfo.roleName != '默认角色' }">

                    <div class="col-12 form-group mg_b_5" >
                        <label for="" class="col-1">受训角色：</label>
                        <div class="col-11 info">${projectInfo.roleName}</div>
                    </div>
                    </c:if>--%>
                    <c:if test="${fn:contains('1457',projectInfo.projectType)}">
                    <div class="col-12 form-group mg_b_5">
                        <label for="" class="col-1">培训时间：</label>
                        <div class="col-11 info">${projectInfo.projectTrainTime}</div>
                    </div>
                    </c:if>
                    <c:if test="${fn:contains('2467',projectInfo.projectType)}">
                    <div class="col-12 form-group mg_b_5">
                        <label for="" class="col-1">练习时间：</label>
                        <div class="col-11 info">${projectInfo.projectExerciseTime}</div>
                    </div>
                    </c:if>
                    <c:if test="${fn:contains('3567',projectInfo.projectType )}">
                    <div class="col-12 form-group mg_b_5">
                        <label for="" class="col-1">考试时间：</label>
                        <div class="col-11 info">${projectInfo.projectExamTime}</div>
                    </div>
                    </c:if>

                    <c:if test="${! empty projectDepartments}">
                    <c:if test="${projectMode == 0}">
                    <div class="col-12 form-group mg_b_5">
                        <label for="" class="col-1">受训单位：</label>
                        <div class="col-11 info">
                            <c:forEach items="${projectDepartments}" var="projectDepartment" varStatus="index" >
                                <c:if test="${index.index ne 0}">
                                    /
                                </c:if>
                                ${projectDepartment.deptName}
                            </c:forEach>
                        </div>
                    </div>
                    </c:if>
                    </c:if>
                </div>
            </div>
        </div>
        <!-- 课程信息 -->
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">课程信息</h3>
                <form id="select_course_form">
                    <input type="hidden" name="projectId" value="${projectInfo.id}">   <%--项目id--%>
                    <div class="pull-right search-group">
                        <span class="search">
                            <input type="text" id="courseName" placeholder="请输入课程名称" name="courseName">
                            <button type="button" class="btn" title="搜索" onclick="projectinfo.search();">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" class="btn all"  onclick="projectinfo.searchAll();"><span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                </form>
            </div>
            <div class="box-body">
                <table class="table pro_msg project_info_table">
                    <thead>
                    <tr>
                            <th width="100">序号</th>
                            <th width="180">课程编号</th>
                            <th >课程名称</th>
                        <c:if test="${fn:contains('1457',projectInfo.projectType)}">
                            <th width="100">课时</th>
                            <th width="150">学时要求</th>
                        </c:if>
                        <c:if test="${fn:contains('2467',projectInfo.projectType) || fn:contains('3567',projectInfo.projectType )}">
                            <th width="150">题库数量</th>
                        </c:if>
                       <%-- <c:if test="${fn:contains('3567',projectInfo.projectType )}">
                            <th width="150">必选题量</th>
                        </c:if>--%>
                    </tr>
                    </thead>

                    <%--课程信息开始--%>
                    <tbody id="courseInfo_tbody">
                       <tr>
                        <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                       </tr>
                    </tbody>
                    <%--课程信息结束--%>

                </table>
                 <%--分页开始--%>
                <div class="page text-right"  style="margin-top:0;margin-bottom:30px;">
                    <ul id="courseInfo_page"></ul>
                </div>
            </div>
        </div>

        <c:if test="${fn:contains('3567',projectInfo.projectType ) and null != examStrategy}">
        <!-- 确定组卷策略开始 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">组卷策略信息</h3>
            </div>
            <div class="box-body">
                <div>
                    <ul class="clearfix combination" style="border:none;">
                        <li>总分：${examStrategy.totalScore}&nbsp;分</li>
                        <li>合格分：${examStrategy.passScore}&nbsp;分</li>
                        <li>考试学时：${examStrategy.necessaryHour}&nbsp;min</li>
                      <%--  <li>学时要求： ${examStrategy.necessaryHour*60}&nbsp;分&nbsp;0&nbsp;秒</li>--%>
                        <li>考试时长：${examStrategy.examDuration}&nbsp;min</li>
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
                    <c:forEach  items="${examStrategyRows}"   var="row" varStatus="i"  end="0">
                        <tr>
                            <td>${i.index+1}</td>
                            <td>${row.type}</td>
                            <td>${row.allCount}</td>
                            <td>${row.intCount}</td>
                            <td>${row.intScore}</td>
                            <td style="border-left:1px solid #ddd" rowspan="${fn:length(examStrategyRows)}">${row.allScore}</td>
                        </tr>
                    </c:forEach>
                    <c:forEach  items="${examStrategyRows}"   var="row" varStatus="i" begin="1" >
                        <tr>
                            <td>${i.index+1}</td>
                            <td>${row.type}</td>
                            <td>${row.allCount}</td>
                            <td>${row.intCount}</td>
                            <td>${row.intScore}</td>

                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        <!-- 确定组卷策略结束 -->
        </c:if>


    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" name="projectId" value="${projectInfo.id}">   <%--项目id--%>
<input type="hidden" id="roleId" name="roleId" value="${roleId}">                <%-- 角色id--%>
<input type="hidden" id="projectType" name="projectTypeName" value="${projectInfo.projectType}">  <%--项目类型--%>



<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/student/projectInfo/project_info.js"></script>
<script>
  var page = new page();
    $(function(){

        var basePath = "<%=basePath%>";
        var projectId=$("#projectId").val();
        var roleId=$("#roleId").val();
        var projectType=$("#projectType").val();
        var courseName=$("#courseName").val();
        projectinfo.init(basePath,
                          projectId,             //项目id
                          roleId,                //学员角色id
                          courseName,            //课程名称
                          projectType,        //项目类型
                          page,
                         'courseInfo_tbody',
                         'courseInfo_page'
                        );
    })

</script>
</html>