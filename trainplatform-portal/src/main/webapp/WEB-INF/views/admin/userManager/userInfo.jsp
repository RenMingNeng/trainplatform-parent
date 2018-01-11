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
    <title>博晟 | 培训平台-管理员-学员详情</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .nav{display: none;}
    </style>
</head>
<body>
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="user"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area" style="overflow: hidden;">

        <!-- 项目基础信息 -->
        <div class="box pd_b_10">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">人员基础信息</h3>
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:goback()" class="btn btn-info" class="back" id="btn_back"> <span class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">

                        <div class="col-12 form-group mg_b_5">
                            <label class="col-1">用户名：</label>
                            <div class="col-11 info">${userInfo.userAccount}</div>
                        </div>

                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">姓名：</label>
                        <div class="col-11 info">
                            ${userInfo.userName}
                        </div>
                    </div>
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">性别：</label>
                        <div class="col-11 info">
                            <c:if test="${userInfo.sex eq '1'}">女</c:if>
                            <c:if test="${userInfo.sex eq '2'}">男</c:if>
                            <c:if test="${empty userInfo.sex}"></c:if>
                        </div>
                    </div>

                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">身份证号：</label>
                        <div class="col-11 info">${userInfo.idNumber}</div>
                    </div>
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">手机号：</label>
                        <div class="col-11 info">${userInfo.mobileNo}</div>
                    </div>
                    <c:if test="${! empty userInfo.troleName }">
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">受训角色：</label>
                        <div class="col-11 info">${userInfo.troleName}</div>
                    </div>
                    </c:if>
                    <c:if test="${! empty userInfo.departmentName }">
                    <div class="col-12 form-group mg_b_5">
                        <label class="col-1">部门信息：</label>
                        <div class="col-11 info">${userInfo.departmentName=="" ? "":userInfo.departmentName}</div>
                    </div>
                    </c:if>
                </div>
            </div>
        </div>
        <!-- 人员变动信息 -->
        <div class="box" style="padding:0;">
            <div class="box-header">
                <h3 class="box-title">人员变动信息</h3>
            </div>
            <div class="box-body" style="padding:0;">

                <table class="table pro_msg project_info_table">
                    <thead>
                    <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>人员变动信息</th>
                            <th>部门信息</th>
                    </tr>
                    </thead>

                    <%--人员变动信息开始--%>
                    <tbody id="userInfo_tbody">
                    <!-- 循环输出带过来的list数组 的值-->
                    <c:choose>
                        <c:when test="${not empty workList}">
                            <c:forEach items="${workList}" var="uwork" varStatus="vs">
                                <tr>
                                    <td>${vs.index+1}</td>
                                    <td>${uwork.operTime.substring(0,19)}</td>
                                    <td>
                                        <c:if test="${uwork.operater==1}">
                                            人员登记
                                        </c:if>
                                        <c:if test="${uwork.operater==2}">
                                            人员转移
                                        </c:if>
                                        <c:if test="${uwork.operater==3}">
                                            人员离职
                                        </c:if>
                                        <c:if test="${uwork.operater==4}">
                                            人员修改
                                        </c:if>
                                    </td>
                                    <td>${uwork.deptName}</td>

                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td class="empty" style="text-align:center;" colspan="4">
                                    <span>对不起，没有找到任何相关记录...</span>
                                </td>
                            </tr>
                        </c:otherwise>
                      </c:choose>
                    </tbody>
                    <%--人员变动信息结束--%>

                </table>
                 <%--分页开始--%>
               <%-- <div class="page text-right" >
                    <ul id="courseInfo_page"></ul>
                </div>--%>
            </div>
        </div>


    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="projectId" name="projectId" value="">   <%--项目id--%>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/admin/proManager/proInfo.js"></script>
<script type="text/javascript">
    function goback(){
        history.go(-1);
    }
</script>
</html>