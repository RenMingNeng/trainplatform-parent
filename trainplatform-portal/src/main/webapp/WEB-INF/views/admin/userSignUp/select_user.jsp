<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html class="select" >
<head>
    <title>选择报名人员</title>
    <jsp:include page="${path}/common/style" />
</head>
<body>
<div class="select-left">
    <div class="search">
        <span class="fa fa-search"></span>
        <input type="text" id="searchKey" class="empty" onkeyup="treeSearch.callNumber()" placeholder="按节点名称检索"/>
        <div id="showInfo">
            <a id="clickUp" class="up search_btn" onclick="treeSearch.clickUp()">
                <span class="fa  fa-angle-up"></span>
            </a>
            <label id="number" class="number"></label>
            <a id="clickDown" class="down search_btn" onclick="treeSearch.clickDown()">
                <span class="fa  fa-angle-down"></span>
            </a>
        </div>
    </div>
    <div>
        <ul id="group_tree" class="ztree">
        </ul>
    </div>
</div>

<div class="select-right">
    <!-- 选择人员 -->
    <div class="box">
        <div class="box-body">
            <div class="clearfix top">
                <form id="select_user_form">
                    <div class="pull-left">
                        <label>人员表</label>
                    </div>
                    <div class="pull-right search-group">
                            <input type="hidden" id="companyIds" name="companyIds" value="">
                            <input type="hidden" id="deptIds" name="deptIds" value="">
                            <input type="hidden" id="companyId" name="companyId" value="${companyId}">
                            <input type="hidden" id="departmentId" name="departmentId" value="">
                            <input type="hidden" id="departmentName" name="departmentName" value="">
                            <input type="hidden" id="searchType" name="searchType" value="1">
                            <input type="text" id="userName" name="userName" placeholder="请输入人员姓名">
                            <button type="button" title="搜索"  class="btn" onclick="selectUser_SignUp.search()">
                                <span class="fa fa-search" id="subjext_search"></span> 搜索
                            </button>

                        <span>
                            <a href="javascript:;" id="user_all" onclick="selectUser_SignUp.searchAll()" class="btn all">
                                <span class="fa fa-th"></span> 全部
                            </a>
                        </span>
                    </div>
                </form>
            </div>
            <!-- 表格 -->
            <div class="table_container">
                <table class="table table_first">
                    <thead>
                    <tr>
                        <th width="50">序号</th>
                        <th width="50"><input type="checkbox" name="all" id="checkAll" onclick="selectUser_SignUp.checkAll()" ></th>
                        <th>人员名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                <%--列表--%>
                    <table class="table">
                        <tbody id="select_user_list">
                            <tr>
                                <td colspan="3" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="page text-right" >
                <ul id="select_user_page"></ul>
            </div>
        </div>

    </div>

    <div class="box" style="padding-left:5px;">
        <div class="box-body">
            <div class="clearfix top">
                <form>
                <div class="pull-left">
                    <label>报名人员</label>
                </div>
                <div class="pull-right search-group">
                    <button type="button" class="btn btn-info" id="btn_sure">确定</button>
                    <button type="button" class="btn btn-info" id="btn_cancel">取消</button>
                </div>
                </form>
            </div>
            <!-- 表格 -->
            <div class="table_container">
                <table class="table table_first">
                    <thead>
                    <tr>
                        <th width="80">序号</th>
                        <th>用户姓名</th>
                    </tr>
                    </thead>
                </table>

                <div class="table_inner">
                    <%--列表--%>
                    <table class="table">
                        <%--列表--%>
                        <tbody id="select_user_list_copy">

                        </tbody>
                    </table>
                </div>

            </table>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">
<input type="hidden" id="projectType" name="projectType" value="${projectType}">
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/common/groupTree.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/userSignUp/select_user.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/treeSearch.js"></script>
<script type="text/javascript">
    //新建对象
    var page = new page();
    $(document).ready(function(){
        selectUser_SignUp.init(page);
        treeSearch.init("group_tree");
         $('.table_inner').niceScroll();
    });
</script>
</body>
</html>


