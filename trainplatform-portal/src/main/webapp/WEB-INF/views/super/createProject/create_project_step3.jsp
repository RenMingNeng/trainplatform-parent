<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.bossien.train.util.PropertiesUtils" %>
<%@ page import="com.bossien.train.domain.eum.ProjectTypeEnum" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";

%>
<!DOCTYPE html>
<html>
<head>
    <title>创建公开型项目</title>
    <jsp:include page="${path}/common/style" />

</head>
<body>
<jsp:include page="${path}/super/menu" >
    <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <div class="box index">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训单位信息列表</h3>
                <form id="company_form">
                    <input type="hidden" id="company_project_id" name="company_project_id" value="${projectId}">
                <div class="pull-right search-group">
                    <span>
                        <button type="button" class="btn btn-info" id="btn_select_company"><span class="fa fa-mouse-pointer"></span> 选择单位</button>
                        <button type="button" class="btn btn-info" id="btn_delete_batch_company"><span class="fa fa-trash-o"></span> 批量删除</button>
                    </span>
                    <span class="search">
                        <%--<label>单位名称</label>--%>
                        <input type="text" id="company_name" name="company_name" placeholder="请输入单位名称">
                        <a type="button" title="搜索" class="btn" id="btn_company_search">
                            <span class="fa fa-search"></span> 搜索
                        </a>

                    </span>
                    <span>
                        <a href="javascript:;" id="btn_company_all" class="btn all">
                            <span class="fa fa-th"></span> 全部
                        </a>
                    </span>
                </div>
                </form>
            </div>

            <div class="box-body">
                <table class="table">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th><input type="checkbox" name="all"></th>
                        <th>单位名称</th>
                    </tr>
                    </thead>
                    <%--单位列表--%>
                    <tbody id="company_list">
                    <tr>
                        <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                    </tbody>
                </table>
                <div class="page text-right" style="margin-top:0px;">
                    <ul id="company_page"></ul>
                </div>

            </div>
        </div>
        <div class="search-group clearfix" style="margin-top:-10px;margin-bottom:20px;">
            <span style="float: right;">
                <a href="javascript:;" class="btn btn-info " id="index"><span class="fa fa-home"></span> 回到首页</a>&nbsp;&nbsp;
                <a href="javascript:;" class="btn btn-info" id="step_2"><span class="fa fa-long-arrow-left"></span> 上一步</a>&nbsp;&nbsp;
                <a href="javascript:;" class="btn btn-info" id="step_4"><span class="fa fa-long-arrow-right"></span> 下一步</a>
            </span>
        </div>
    </div>
</div>
<%--隐藏域--%>
<input type="hidden" id="id" name="id" value="${projectId}">
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/createProject/step_3.js"></script>
<script type="text/javascript">
    var page = new page();
    step_3.init(page);
</script>
</body>
</html>
