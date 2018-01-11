<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="course"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area">
       <%-- <div id="addr" class="clearfix">
            <div class="pull-left">
                <a href="<%=resourcePath%>admin/courseManager/courseManagerList">课程管理</a><span class="fa fa-angle-right"></span><a href="javascript:;">课程详情</a>
            </div>
            <%--<div class="pull-right">
                <a href="javascript:;" class="btn btn-info"  id="refresh"><span class="fa fa-refresh"></span>刷新</a>
            </div>--%>

       <%-- <div class="box">
            <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span>返回</a>
        </div>--%>
        <!-- 项目基础信息 -->
        <div class="box pd_b_10">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">课程信息</h3>
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">课程编号：</label>
                        <div class="col-11 info" >${courseInfo.courseNo}</div>
                    </div>
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">课程名称：</label>
                        <div class="col-11 info">${courseInfo.courseName}</div>
                    </div>
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">课程类别：</label>
                        <div class="col-11 info">${courseTypeName}</div>
                    </div>
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">课时：</label>
                        <div class="col-11 info">${courseInfo.classHour}分钟</div>
                    </div>
                    <%--<div class="col-12 form-group">
                        <label for="" class="col-1">培训主题：</label>
                        <div class="col-11 info">
                            <c:forEach items="${trainSubjects}" var="trainSubject" varStatus="index" >
                                <c:if test="${index.index ne 0}">
                                    /
                                </c:if>
                                ${trainSubject.subjectName}
                            </c:forEach>
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label for="" class="col-1">受训角色：</label>
                        <div class="col-11 info">
                            <c:forEach items="${trainRoles}" var="trainRole" varStatus="index" >
                                <c:if test="${index.index ne 0}">
                                    /
                                </c:if>
                                ${trainRole.roleName}
                            </c:forEach>
                        </div>
                    </div>--%>
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">课程介绍：</label>
                        <div class="col-11 info">${courseInfo.courseDesc}</div>
                    </div>
                </div>
            </div>
        </div>
        <!-- 课程信息 -->
</div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
</body>
</html>