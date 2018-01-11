<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html class="select">
<head>
    <title>选择培训课程</title>
    <jsp:include page="${resourcePath}/common/style" />
</head>
<body>
<div class="select-left no_select">

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
        <ul id="course_type_tree" class="ztree">

        </ul>
    </div>
</div>
<div class="select-right">
    <!-- 选择课程 -->
    <div class="box">
        <div class="box-body">
            <div class="clearfix top" >
                <form id="select_course_form">
                    <div class="pull-left">
                        <label >课程表</label>
                    </div>
                    <div class="pull-right search-group">
                        <span class="search">
                            <input type="text" id="course_name" name="course_name" placeholder="请输入课程名称">
                            <a type="button" title="搜索"  class="btn" id="submit_search">
                                <span class="fa fa-search"></span> 搜索
                            </a>
                        </span>
                        <span>
                            <a href="javascript:;" id="course_all" class="all btn"> <span class="fa fa-th"></span> 全部</a>
                        </span>

                    </div>
                    <input type="hidden" id="course_type" name="course_type">
                </form>
            </div>
            <!-- 表格 -->
            <div class="table_container">
                <table class="table table_first">
                    <thead>
                    <tr>
                        <th width="50">序号</th>
                        <th width="50"><input type="checkbox" name="checkAll" id="checkAll" onclick="selectCourse.checkAll()"></th>
                        <th>课程编号</th>
                        <th width="240">课程名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                    <table class="table">
                        <%--列表--%>
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
            <div class="clearfix top">
                <form>
                <div class="pull-left">
                    <label>已选课程</label>
                </div>
                <div class="pull-right  search-group">
                    <span>
                        <button class="btn btn-info" id="btn_sure"  type="button"><span class="fa fa-check"></span> 确定</button>
                    </span>
                    <span>
                        <button class="btn btn-info" id="btn_cancel"  type="button"><span class="fa fa-remove"></span> 取消</button>
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
                        <th>课程编号</th>
                        <th width="240">课程名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                    <table class="table">
                        <%--列表--%>
                        <tbody id="select_course_list_copy">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<input type="hidden" id="projectId" name="projectId" value="${projectId}">
<input type="hidden" id="type" name="type" value="${type}">
<jsp:include page="${resourcePath}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/dialog/course_type.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/treeSearch.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/dialog/select_course.js"></script>
<script type="text/javascript">
    var page = new page();
    courseType.init();
    treeSearch.init("course_type_tree");
    selectCourse.init(page);
    $('.table_inner').niceScroll();
</script>
</body>
</html>


