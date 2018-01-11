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
    <style>
        .nav {
            display: none;
        }

        .img_1 {
            height: 100px;
            width: 100px;
            line-height: 100px;
            background: #ddd;
            color: #fff;
            text-align: center;
            position: relative;
            display: inline-block
        }

        .img_1 img {
            border: 1px solid #ccc;
        }

        input[type="file"] {
            vertical-align: top;
            outline: none;
        }
    </style>
</head>
<body>
<jsp:include page="${path}/admin/menu">
    <jsp:param name="menu" value="basicinfo"></jsp:param>
</jsp:include>

<div id="main">
    <div class="area">
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">新增培训主题</h3>
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:;" class="btn btn-info" id="save"><span class="fa fa-paper-plane">
                        </span> 保存</a>
                        <a href="javascript:window.location.href = appPath + '/admin/trainSubject';"
                           class="btn btn-info" id="btn_back"><span
                                class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <form id="trainsubject_form" enctype='multipart/form-data' method='post'>
                        <input type="hidden" id="courseIds" name="courseIds" value="">
                        <div class="col-12 form-group">
                            <label class="col-1"><b class="text-red">*</b>主题名称：</label>
                            <div class="col-11">
                                <input class="w_700" type='text' id='subjectName' name="subjectName" value=''/>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1">主题描述：</label>
                            <div class="col-11">
                                <textarea class="w_700" id='editDesc' name="subjectDesc" rows='7' cols='5'
                                          maxlength='256'
                                          placeholder='主题描述，限256字符'></textarea>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1">主题照片：</label>
                            <%-- <div class="col-11">
                                 <div id="divPreiew" class="img_1">
                                     暂无图片<img width="100" height="100" style="position: absolute;left:0;top:0;" alt=""
                             <label for="" class="col-1">主题照片：</label>--%>
                            <div class="col-11">
                                <div id="divPreiew" class="img_1">
                                    暂无图片 <img width="100" height="100" style="position: absolute;left:0;top:0;" alt=""
                                              id="imHeadPhoto"></div>
                                <input type="hidden" name="initFileName"
                                       value="static/global/images/subject_image/subject_image_${num}.png">
                                <input class="file" id="file" name="fileName" type="file" title="选择头像"
                                       value="subject_image_${num}.png"
                                       onchange="train_subject_add.previewImage(this,'imHeadPhoto','divPreiew')"
                                <%--accept="image/*"--%>>
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
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:;" class="btn btn-info" id="btn_dialog_course"><span
                                class="fa fa-mouse-pointer"></span> 选择课程</a>
                        <a href="javascript:;" class="btn btn-info" id="btn_delete_batch_course"><span
                                class="fa fa-trash-o"></span> 批量删除</a>
                    </span>
                </div>
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
            </div>
        </div>
        <input type="hidden" id="parentSource" name="parentSource" value="trainSubject">
    </div>
</div>
<jsp:include page="${path}/common/footer"/>
<jsp:include page="${path}/common/script"/>

<script src="<%=resourcePath%>static/global/js/admin/basicinfo/train_subject_add.js"></script>
<script>
    train_subject_add.init();
</script>
</body>
</html>