<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>"/>
    <meta charset="utf-8"/>
    <title>博晟 | 培训平台-学员-项目详情</title>
    <jsp:include page="${path}/common/style"/>
</head>
<body>
<jsp:include page="${path}/admin/menu">
    <jsp:param name="menu" value="basicinfo"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area" style="overflow: hidden;">
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">受训角色详情</h3>
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:window.location.href = appPath + '/admin/trainRole';" class="btn btn-info" id="btn_back"><span
                                class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <form id="trainsubject_form">
                        <div class="col-12 form-group">
                            <label for="" class="col-1">角色名称：</label>
                            <div class="col-11">
                                <input class="w_700" type='text' id='editName' value='${trainRole.roleName}' readonly/>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label for="" class="col-1">角色描述：</label>
                            <div class="col-11 ">
                                <textarea id='editDesc' rows='7' cols='5' maxlength='256' readonly='readonly' class="w_700"
                                          placeholder='主题描述，限256字符'>${trainRole.roleDesc}</textarea>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/footer"/>
<jsp:include page="${path}/common/script"/>


<script src="<%=resourcePath%>static/global/js/admin/basicinfo/train_subject_info.js"></script>
<script>
    var page = new page();
    trainSubjectInfo.init(page)
</script>
</body>
</html>