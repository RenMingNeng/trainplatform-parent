<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html class="select full">
<head>
    <title>创建项目</title>
    <jsp:include page="${path}/common/style" />
</head>
<body style="min-width:100px;">
    <div class="box" >
        <div class="box-header clearfix">
            <div class="pull-left search-group">
                <span>
                    <a href="javascript:;" class="btn btn-info" id="btn_close"><span class="fa fa-reply"></span> 关闭</a>
                </span>
            </div>
            <form id="select_user_form" class="clearfix" >
            <div class="pull-right search-group">
                <input type="hidden" id="pro_user_id" name="projectId" value="${projectId}">          <%-- 项目id--%>
                <span>
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
            <div class="container_table">
                <table class="table first_table">
                    <thead>
                    <tr>
                        <th width="80">序号</th>
                        <th width="80"><input type="checkbox" name="all" id="usercheckAll"  ></th>
                        <th width="200">姓名</th>
                        <th >部门信息</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                    <%--授权课程列表--%>
                    <table class="table">
                        <tbody id="select_user_list">
                            <tr>
                             <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <%--分页--%>
                <div class="page text-right  bg_page" style="margin-top:0px;">
                    <ul id="select_user_page"></ul>
                </div>
            </div>
        </div>
    </div>
<!--隐藏字段-->
<input type="hidden" id="projectTypeNo" name="projectTypeNo" value="${projectTypeNo}">  <%--项目类型编号--%>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">              <%-- 主题编号--%>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=path%>/static/global/js/page.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/userSignUp/user_manager.js"></script>
<script>
    var page = new page();
    userManager.init(page);
    $('.table_inner').niceScroll();

</script>

</body>
</html>
