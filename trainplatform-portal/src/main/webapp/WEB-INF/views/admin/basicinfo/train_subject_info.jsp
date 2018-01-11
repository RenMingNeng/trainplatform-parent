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
    <div class="area">
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">培训主题详情</h3>
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:history.go(-1);" class="btn btn-info" id="btn_back"><span
                                class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <form id="trainsubject_form">
                        <div class="col-12 form-group">
                            <label class="col-1">主题名称：</label>
                            <div class="col-11">
                                <input class="w_700" type='text' id='editName' value='${trainSubject.subjectName}' readonly="readonly"/>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1">主题描述：</label>
                            <div class="col-11">
                                <textarea class="w_700" id='editDesc' rows='7' cols='5' maxlength='256' readonly='readonly'
                                          placeholder='主题描述，限256字符'>${trainSubject.subjectDesc}</textarea>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1">主题照片：</label>
                            <div class="col-11">
                                <c:if test="${'' == trainSubject.logo}">
                                    <img width="100" height="100" alt="" id="imHeadPhoto" src="<%=resourcePath%>/static/global/images/subject_image/subject_image_${num}.png">
                                </c:if>
                                <c:if test="${'' != trainSubject.logo}">
                                <img width="100"  height="100" alt="" id="imHeadPhoto" src="${trainSubject.logo}">
                               </c:if>
                            </div>
                        </div>
                    </form>

                </div>

            </div>
        </div>
        <!-- 课程信息 -->
        <div class="box">
            <div class="box-header clearfix">

                <h3 class="box-title pull-left">培训主题列表</h3>
                <form id="select_course_form" class="clearfix">
                    <input type="hidden" id="proid" name="varId" value="${trainSubject.varId}"> <%-- 项目id--%>
                    <div class="pull-right search-group">
                        <span class="search">
                        <input type="text" id="courseName" placeholder="请输入课程名称" name="courseName">
                        <button type="button" href="javascript:;" class="btn" id="btn_search_course"><span
                                class="fa fa-search"></span> 搜索</button>
                    </span>
                        <span>
                         <a href="javascript:;" class="btn all" id="btn_search_all_course"><span
                                 class="fa fa-th"></span> 全部</a>
                    </span>
                    </div>
                </form>

            </div>
            <div class="box-body">


                    <table class="table">
                        <thead>
                        <tr>
                            <th width="60">序号</th>
                            <th width="40"><input type="checkbox" name="all" id="coursecheckAll"></th>
                            <th width="100">课程编号</th>
                            <th width="100">课程名称</th>
                            <th width="60">课时/分</th>
                            <th width="200">查看</th>
                        </tr>
                        </thead>
                        <%--授权课程列表--%>
                        <tbody id="select_course_list">
                        <tr>
                            <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                        </tr>
                        </tbody>
                    </table>
                    <%--分页--%>
                    <div class="page text-right">
                        <ul id="select_course_page"></ul>
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