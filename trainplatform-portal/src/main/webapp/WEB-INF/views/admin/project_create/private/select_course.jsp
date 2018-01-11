<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html class="select">
<head>
    <title>选择课程</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
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
        <ul id="course_treeId" class="ztree">
        </ul>
    </div>
</div>
<%--<div class="user-search-list" style="height:100%;">
    <ul id="course_treeId" class="ztree">
    </ul>
</div>

<div class="select-left">
    <ul id="tree" class="ztree"></ul>
</div>--%>
<div class="select-right">
    <!-- 选择课程 -->
    <div class="box">
        <div class="box-body" >
            <div class="clearfix top">
                <form id="select_course_form">
                    <div class="pull-left">
                        <label>课程表</label>
                    </div>
                    <div class="pull-right search-group">
                        <span class="search">
                            <input type="hidden" id="intTypeId" name="intTypeId">
                            <input type="text" id="name_search" name="courseName" placeholder="请输入课程名称">
                            <button type="button" title="搜索"  class="btn" onclick="private_selectCourse.search()">
                                <span class="fa fa-search" id="subjext_search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" id="course_all" class="btn all" onclick="private_selectCourse.all()">
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
                        <th width="50"><input type="checkbox" name="all" id="checkAll" onclick="private_selectCourse.checkAll()" ></th>
                        <th>课程编号</th>
                        <th width="240">课程名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                    <%--授权课程列表--%>
                    <table class="table">
                    <tbody id="select_course_list">
                            <tr>
                            <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                            </tr>
                        </tbody>
                    </table>

                </div>
            </div>
            <div class="page text-right" >
                <ul id="select_course_page"></ul>
            </div>
        </div>
    </div>

    <div class="box" style="padding-left:5px;">
        <div class="box-body">
            <div class="clearfix top" >
                <form>
                <div class="pull-left">
                    <label>已选课程</label>
                </div>
                <div class="pull-right search-group">
                    <button class="btn btn-info" id="btn_sure">确定</button>
                    <button class="btn btn-info" id="btn_cancel">取消</button>
                </div>
                </form>
            </div>
            <!-- 表格 -->
            <div class="table_container">
                <table class="table table_first">
                    <thead>
                    <tr>
                        <th width="50">序号</th>
                        <%--<th></th>--%>
                        <th>课程编号</th>
                        <th width="240">课程名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                <%--列表--%>
                    <table class="table">
                        <tbody id="select_course_list_copy">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>


<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/courseManager/course_type.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/treeSearch.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/project_create/private/select_course.js"></script>
<script type="text/javascript">
    var page = new page();
    //加载课程分类
    course_type.iniTree();
    private_selectCourse.init(page);
    treeSearch.init("course_treeId");
    $('.table_inner').niceScroll();
</script>
</body>
</html>


