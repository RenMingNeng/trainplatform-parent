<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>个人信息</title>
    <jsp:include page="${path}/common/style" />

</head>
<body>
<jsp:include page="${path}/student/menu"></jsp:include>

<div id="main">
    <div class="area">

        <div class="box pd_b_10">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">人员基本信息</h3>
                <div class="pull-right search-group">
                    <a href="javascript:;" class="btn btn-info">保存</a>
                    <a href="javascript:;" class="btn btn-info">返回</a>
                </div>
            </div>
            <div class="box-body">
                <div class="row form-group">
                    <label class="col-1">用户名</label>
                    <div class="col-7 info">
                        42011719465464613112
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1"><b class="text-red">*</b>姓名：</label>
                    <div class="col-7 info">
                        <input type="text">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1">性别：</label>
                    <div class="col-7 info">
                        <label for="man">
                            <input type="radio" id="man"> 男
                        </label>
                        &nbsp;&nbsp;
                        <label for="woman">
                            <input type="radio" id="woman"> 女
                        </label>
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1">身份证号：</label>
                    <div class="col-7 info">
                        <input type="text">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1">手机号：</label>
                    <div class="col-7 info">
                        <input type="text">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1">受训角色：</label>
                    <div class="col-7 info">
                        默认角色
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1">部门信息：</label>
                    <div class="col-7 info">
                        默认角色
                    </div>
                </div>
            </div>
        </div>

        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">项目记录</h3>
            </div>
            <div class="box-body">
                <table class="table">
                    <thead>
                        <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>人员变动信息</th>
                            <th>部门信息</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>1</td>
                            <td>2017-08-02 16:10:10</td>
                            <td>人员登记</td>
                            <td>性能测试人员</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>

<jsp:include page="${path}/common/footer" />


</body>
</html>
