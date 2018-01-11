<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>博晟 | 培训平台-管理员-人员管理</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
    <link type="text/css" rel="stylesheet" href="<%=resourcePath%>static/global/js/select2/css/select2.min.css"/>
    <style>
        .select2-dropdown.select2-dropdown--below{border: 2px solid #b9dfff;border-top: aliceblue;}
        .tree_list {
            height: auto;
        }

        .tree_list .tree {
            width: 300px;
            padding-right: 0;
        }

        .tree_list .list {
            margin-left: 310px;
            overflow: hidden;
        }

        .radio-group label {
            margin-right: 15px;
            font-size: 14px;
        }
        .loading,.loading body{
            height:100%;
        }
        .loading .load{
            display: block;;
        }
        .load{
            position: fixed;
            left:0;top:0;height:100%;width:100%;
            display: none;z-index:10000000;
            background:url("<%=resourcePath%>static/global/js/layer/skin/default/loading-0.gif") no-repeat center center rgba(0,0,0,.3);
        }
    </style>
</head>

<body>
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="msg"></jsp:param>
</jsp:include>
<div id="main">
    <div class="area">
        <div class="box">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">新增通知公告</h3>
                <div class="pull-right search-group">
                    <span>
                        <a href="javascript:;" class="btn btn-info" onclick="saveForm();"><span class="fa fa-paper-plane">
                        </span> 保存</a>
                        <a href="javascript:window.location.href = appPath + '/popup/msg';"
                           class="btn btn-info" id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                    </span>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">
                    <form id="msg_form" enctype='multipart/form-data' method='post'>
                        <input type="hidden" id="courseIds" name="courseIds" value="">
                        <div class="col-12 form-group">
                            <label class="col-1"><b class="text-red">*</b>通知标题：</label>
                            <div class="col-11">
                                <input class="w_700" type='text' id='msgTitle' name="msgTitle" value=''/>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1"><b class="text-red">*</b>发布范围：</label>
                            <div class="col-8 pull-left">
                                <input type="hidden" name="companys" id="companys">
                                <input type="hidden" name="depts" id="depts">
                                <select id="area_select" class="form-control"style="width: 100%;"></select>
                            </div>
                            <div class="col-2 form-group">
                                <div class="col-2"></div>
                                <label class="col-8" style="line-height: 41px;">
                                    <input type='checkbox' id="isSelect" value="2" checked/>
                                    选中子节点人员
                                </label>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <div class="col-6 form-group">
                                <label class="col-2">是否置顶：</label>
                                <div class="col-8">
                                    <label class="col-2" style="line-height: 41px;">
                                        <input type='radio' name="isTop" value="1"/>
                                        是
                                    </label>
                                    <label class="col-6" style="line-height: 41px;">
                                        <input type='radio' name="isTop" value="2" checked/>
                                        否
                                    </label>
                                </div>
                            </div>
                            <div class="col-6 form-group">
                                <label class="col-2">是否HOT：</label>
                                <div class="col-8">
                                    <label class="col-2" style="line-height: 41px;">
                                        <input type='radio' name="isHot" value="1"/>
                                        是
                                    </label>
                                    <label class="col-6" style="line-height: 41px;">
                                        <input type='radio' name="isHot" value="2" checked/>
                                        否
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <div class="col-6 form-group">
                                <label class="col-2">发布时间：</label>
                                <div class="col-8">
                                    <input style="width: 60%;" type='text' id='publishTime' name="publishTime"  value='${curDate}' readonly/>
                                    <%--<input style="width: 60%;" type='text' name="publishTime" class="Wdate"--%>
                                           <%--onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'%y-%M-%d',readOnly:true})"/>--%>
                                </div>
                            </div>
                            <div class="col-6 form-group">
                                <label class="col-2">发件人：</label>
                                <div class="col-8">
                                    <input style="width: 60%;" type='text' id='3' value='${userName}' readOnly/>
                                </div>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1"><b class="text-red">*</b>通知描述：</label>
                            <div class="col-11">
                                <textarea id="content" name="msgContent" placeholder="公告内容"></textarea>
                            </div>
                        </div>
                        <div class="col-12 form-group">
                            <label class="col-1">上传附件：</label>
                            <div class="item col-10">
                                <div class="label"><span style="display: inline-block;color:#333;font-size:12px;font-weight:bold;"> 支持格式包括:jpg, png, txt, zip, doc, docx, ppt, pdf, xls, xlsx  单文件不超过2M</span></div>
                                <div style="line-height: 24px;">
                                    <input type="hidden" name="msgFile" id="fileList">
                                </div>
                                <div id="up_uploader_container">

                                </div>
                            </div>
                        </div>
                    </form>

                </div>

            </div>
        </div>
    </div>
</div>
<div class="load"></div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/select2/js/select2.full.js"></script>
<%--ue编辑器--%>
<script type="text/javascript" charset="utf-8" src="<%=resourcePath%>static/global/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=resourcePath%>static/global/js/ueditor/ueditor.all.js"></script>
<script type="text/javascript" charset="utf-8" src="<%=resourcePath%>static/global/js/ueditor/webutils.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/global/js/popup/select_company_dept.js"></script>
<!-- 上传 下载js -->
<link rel="stylesheet" href="<%=basePath%>/static/global/plupload/jquery.plupload.queue/css/jquery.plupload.queue.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=basePath%>/static/global/plupload/upload/plupload.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/global/plupload/plupload.full.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/global/plupload/jquery.plupload.queue/jquery.plupload.queue.js"></script>
</body>
<script type="text/javascript">
    $(function() {
        //实例化编辑器 UE编辑器
        initUEEditor("content",'90%','500');

        select_company_dept.initTree('area_select', '');

        //上传文件
        iniUploadFile("up",appPath + '/popup/fastUpload');

    });

    var fileList = {};
    function backUpCallBack(id, response_, fileId) {
        var data = eval("(" + response_ + ")");
        var result = data['result'];
        fileList[fileId] = result;
        //更新页面数据
        var data = $("#fileList").val();
        result += data == '' ? "" : ";" + data;
        $("#fileList").val(result);
    }

    //删除
    function delCallback(id, file){
        var delFileId = file['id'];

        var result = '';
        var array = Object.keys(fileList);
        for(var i = 0; i < array.length; i++){
            if(array[i] == delFileId){
                //后台删除fastdfs
                delFastDfsFile(fileList[delFileId]);
                //删除集合
                delete fileList[delFileId];
                continue;
            }
            if(fileList[array[i]] != undefined){
                result += result == ''? fileList[array[i]] : ";" + fileList[array[i]];
            }
        }
        $("#fileList").val(result);
    }

    function delFastDfsFile(newFileName) {
        var url = appPath + '/popup/fastDelete';
        var params = {'fileName': newFileName};
        ajax(url, params);
    }

    function saveForm() {

        if(!verify()){
            return;
        }
        $("html").addClass("loading");

        var params = $("#msg_form").serialize();
        var url = appPath + '/popup/msg/saveAdd';
        $.ajax({
            url: url,
            async: true,
            type: 'post',
            data: params,
            success: function(data){
                var result = data['code'];
                if(result == '10000'){
                    layer.alert('操作成功',function () {
                        window.location.href = appPath + '/popup/msg';
                    });
                }
            }
        });
    }

    //重写
    function selectCompanyDeptCheck(nodes) {
        var companys = '';
        var depts = '';
        var data = [];
        for(var i=0; i< nodes.length; i++) {
            data.push(nodes[i].id);
            //组装数据
            var type = nodes[i].type;
            if(type == 1 || type == 2){
                companys += companys == '' ? nodes[i].id : "," + nodes[i].id;
                continue;
            }
            depts += depts == '' ? nodes[i].id : "," + nodes[i].id;
        }
        $("#companys").val(companys);
        $("#depts").val(depts);
    }

    function verify() {
        if($("#msgTitle").val() == ''){
            layer.alert("通知公告标题必填", function (index) {
                $("#msgTitle").focus();
                layer.close(index);
            });
            return false;
        }

        if($("#companys").val() == '' && $("#depts").val() == ''){
            layer.alert("通知公告发布范围必填", function (index) {
                $("#companys").focus();
                layer.close(index);
            });
            return false;
        }

        if(UE.getEditor('content').getContent() == ''){
            layer.alert("通知公告内容必填", function (index) {
                $("#msgContent").focus();
                layer.close(index);
            });
            return false;
        }
        return true;
    }

    function ajax(url, params) {
        var result;
        $.ajax({
            url: url,
            async: true,
            type: 'post',
            data: params,
            success: function(data){
                result = data;
            }
        });
        return result;
    }
</script>

</html>

