<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<html>
<head>
    <title>博晟 | 培训平台 反馈</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/feedback.css" rel="stylesheet" type="text/css">
    <style>
        #up_uploader_container{
            padding:0;
        }
        #up_uploader_filelist{
            overflow-y: auto;
            border-width:0 1px 0 1px;
            border-style:solid;
            border-color:#DFDFDF;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="logo">
        <img src="<%=resourcePath%>static/global/images/feedback_logo.png" alt="" class="pull-left">

    </div>
    <div class="main" style="margin-bottom:50px;">
        <h2><span>问题处理中心</span></h2>
        <form id="feedback-form" method="post" enctype="multipart/form-data">
            <!-- input hidden -->
            <input type="hidden" name="feedBack_url" id="feedBack_url" value="${url}">
            <input type="hidden" name="user_id" id="user_id" value="${sessionScope.bossien_user.id}">
            <input type="hidden" name="user_name" id="user_name" value="${sessionScope.bossien_user.userName}">
            <input type="hidden" name="fileList" id="fileList">
            <div class="head">
                尊敬的用户 <br>
                您可以将使用该系统时产生的问题反馈给我们，帮助我们改进用户体验。非常感谢您的参与！
            </div>
            <div class="item">
                <div class="label"><em>*</em>问题类别</div>
                <div>
                    <label><input type="radio" name="problem_type" value="1">&nbsp;视频播放问题</label>
                    <label><input type="radio" name="problem_type" value="2">&nbsp;题目问题</label>
                    <label><input type="radio" name="problem_type" value="3">&nbsp;系统错误</label>
                    <label><input type="radio" name="problem_type" value="4">&nbsp;界面问题</label>
                    <label><input type="radio" name="problem_type" value="5">&nbsp;功能要求</label>
                    <label><input type="radio" name="problem_type" value="6">&nbsp;其他建议</label>
                </div>
            </div>
            <div class="item">
                <div class="label"><em>*</em>问题描述</div>
                <div>
                    <textarea name="content" id="content" ></textarea>
                </div>
            </div>
            <div class="item">
                <div class="label">上传截图&nbsp;&nbsp; <span style="display: inline-block;color:#333;font-size:12px;font-weight:bold;"> 支持格式包括:jpg, png, txt, zip, doc, docx, ppt, pdf, xls, xlsx  单文件不超过2M</span></div>
                <div style="line-height: 24px;">
                    <%--<button type="button" id="up" class="btn btn-info" style="vertical-align: top;">上传</button>--%>
                    <%--<input type="file" name="files" multiple="multiple" class="file_input" onchange="fileChange(this);">--%>

                </div>
                <div id="up_uploader_container">

                </div>
            </div>
            <div class="item">
                <div class="label">再次感谢您的反馈！您可以留下常用的联系方式，以便进一步联络。</div>
                <div>
                    <input type="text" name="contact_way" id="contact_way" placeholder="手机号、姓名等">
                </div>
            </div>
            <div  class="item">
                <a href="javascript:;" id="btn-submit">提交</a>
                <a href="javascript:;" id="btn-close">关闭</a>
            </div>
        </form>
    </div>
</div>

<jsp:include page="${path}/common/script" />
<!-- 上传 下载js -->
<link rel="stylesheet" href="<%=basePath%>/static/global/plupload/jquery.plupload.queue/css/jquery.plupload.queue.css" type="text/css" media="screen" />
<script type="text/javascript" src="<%=basePath%>/static/global/plupload/upload/plupload.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/global/plupload/plupload.full.min.js"></script>
<script type="text/javascript" src="<%=basePath%>/static/global/plupload/jquery.plupload.queue/jquery.plupload.queue.js"></script>

<script>
    $(function(){
        $("#content").focus();
        iniUploadFile("up",appPath + '/feedback/upload');


        // feedback form ajax form submit
        $("#btn-submit").click(function (){
            $("#feedback-form").ajaxSubmit({
                type: 'post',
                dataType: 'json',
                url: appPath + '/feedback/insert',
                beforeSubmit: function(){

                    return verify();
                },
                success: function(data){
                    console.log(data);
                    var code = data.code;
                    if("10000" == code){
                        layer.open({
                            type:1,
                            title: false, // 不显示标题栏
                            closeBtn: false,
                            area: "600px",
                            shade: 0.3,
                            id: "layer-session-timeout", // 设置id, 防止重复弹出
                            resize: false,
                            btn:["知道了"],
                            btnAlign: "c",
                            moveType: 1,
                            content: "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300'>反馈成功,我们将尽快处理</div>",
                            yes: function(layero) {

                                layer.close(layero);

                                $("#feedback-form")[0].reset();

                                //重置上传插件
                                plupload_data_array = new Array();
                                $("#up_uploader").remove();
                                iniUploadFile("up",appPath + '/feedback/upload');

                                window.close();
                            }
                        });
                    }
                },
                error: function(XmlHttpRequest, textStatus, errorThrown){

                },
                complete: function(){

                }
            });
        });

        $("#btn-close").click(function () {
            window.open('', '_self', '');
            window.opener = null;
            window.close();
        });

        $('.file_input').change(function(){
            var val = $(this).val();
            $('.file_abbr').text( val );
        })
    })

    function verify() {
        var problem_types = [];
        $("input[name='problem_type']:checked").each(function () {
            problem_types.push($(this).val());
        });
        if(problem_types.length == 0) {
            alert("请选择一项问题类别"); return false;
        }
        if(problem_types.length > 1) {
            alert("只能选择一项问题类别"); return false;
        }

        var content = $.trim($("#content").val());
        if(!content) {
            alert("请填写问题描述"); return false;
        }

//        var contact_way = $.trim($("#contact_way").val());
//        if(!contact_way) {
//            alert("请填写联系方式"); return false;
//        }

        return true;
    }

    var isIE = /msie/i.test(navigator.userAgent) && !window.opera;
    function fileChange(target, id){
        var fileSize = 0;
        var fileTypes = [".jpg", ".png", ".bmp", ".txt", ".zip", ".doc", ".docx", ".ppt", ".pdf", ".xls", ".xlsx"];
        var filePath = target.value;
        var fileMaxSize = 1024 * 2; // 2M
        if(filePath) {
            var isNext = false;
            var fileEnd = filePath.substring(filePath.indexOf(".")).toLowerCase();
            for(var i=0; i< fileTypes.length; i++) {
                if(fileTypes[i] == fileEnd){
                    isNext = true;break;
                }
            }
            if(!isNext) {
                alert("不接受此文件类型");
                target.value = "";
                return false;
            }
        } else {
            return false;
        }

        if(isIE && !target.files) {
            var filePath = target.value;
            var fileSystem = new ActiveXObject("Scripting.FileSystemObject");
            if(!fileSystem.FileExists(filePath)) {
                alert('附件不存在,请重新选择');
                return false;
            }
            var file = fileSystem.GetFile(filePath);
            fileSize = file.Size;
        } else {
            fileSize = target.files[0].size;
        }

        var size = fileSize / 1024;
        if(size > fileMaxSize) {
            alert("附件大小不能大于" + fileMaxSize / 1024 + "M");
            target.value = "";
            return false;
        }
        if(size <= 0) {
            alert('附件大小不能为0M');
            target.value = "";
            return false;
        }

    }


</script>
</body>
</html>
