<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<%--<link rel="stylesheet" href="<%=basePath%>/css/style.css">&ndash;%&gt;--%>
<%--<jsp:include page="${path}/common/script" />--%>
<style type="text/css">
	body{
		margin:0;
		padding:20px;
	}
	#a_excel_temp_download{
		font-size: 14px;color: #4f9fe4;
	}
	#btn_select_file,#btn_upload_file{
		min-width: 80px;
		background: #4f9fe4;
		color: #fff;
		height: 34px;
		border-radius: 3px;
	}
	#ipt_file{
		width:100%;
		border:0 none;
	}
	#ipt_file_h{
		position: absolute;height: 34px;opacity: 0;filter: alpha(opacity=0);cursor: pointer;margin-left:-73px;width:153px;
	}
	button{
		cursor: pointer;
		border:none;
	}
	/* 表格样式 */
	table,.table{
		font-size:12px;
		width:100%;
		border-collapse: collapse;
		border-spacing: 0;
		border-bottom: 1px solid #ddd;
		border-left: 1px solid #ddd;
	}

	.table td,
	.table th{
		border-top: 1px solid #ddd;
		border-right: 1px solid #ddd;
	}
	.table{
		margin-top:10px;
	}
	.table td{
		padding:5px;
		word-break:break-all;
	}
	.table .vertical{
		vertical-align:top;
		line-height:28px;
	}
	.table .th{
		font-weight:bold;
		width:130px;
		text-align:right;
	}
	.table table{
		width:100%;
		border-left:1px solid #d5d5d5;
		border-top:1px solid #d5d5d5;
		margin-top:-18px;
	}
	.table table thead th{
		border-right:1px solid #d5d5d5;
		border-bottom:1px solid #d5d5d5;
		text-align:center;
		line-height:30px;
	}
	.table table tbody td{
		border-right:1px solid #d5d5d5;
		border-bottom:1px solid #d5d5d5;
		text-align:center;
	}
	.display_table{
		display: table;
		width:100%;
		margin-top:10px;
	}
	.input_warp{
		padding: 0 10px;
		border-radius: 3px;
		border:1px solid #d5d5d5;
	}
	.display_table>div{
		display: table-cell;
	}
</style>
<jsp:include page="${path}/common/script" />
</head>
<body>
	<span style="color: #000;font-size: 12px;font-weight: 900;">请选择要导入的文件（必须是符合指定格式的Microsoft Excel文档）： 
		<a target="_blank" id="a_excel_temp_download"  href="<%=path%>/admin/excelImport/template_download?tempName=${tempName}">模板下载</a>
	</span>
	<form enctype="multipart/form-data" method="post" id="ff_file">
		<div class="display_table">
			<div class="input_warp">
				<input id="ipt_file" type="text" placeholder="未选择文件" readonly="readonly"/> 
			</div>
			
			<div style="width:170px;text-align:right;">
				<input type="file" id="ipt_file_h" name="file">
				<button type="button" id="btn_select_file">浏览</button>
				<button type="button" id="btn_upload_file">确认</button>
			</div>
		</div>
		<input type="hidden" id="tempName" name="tempName" value="${tempName}">
		<input type="hidden" id="params" name="params" value='${params}'>
		<input type="hidden" id="exportTye" name="exportTye" value='${exportTye}'>
	</form>
	<span id="span_01" style="color: #000;font-size: 12px;font-weight: 900;display: none">
		共导入
		<a id="a_total" style="color: #0068b7;font-size: 18px;">10</a>条  ,
		成功
		<a id="a_success" style="color: #1c8e4a;font-size: 18px;">10</a>条  ,
		失败
		<a id="a_error" style="color: #D53434;font-size: 18px;">10</a>条
		<a id="a_download" target="_blank"  style="font-size: 13px;color: rgb(37, 116, 40);" fileName="">下载</a>
	</span>
	<table class="table" id="tt_error">
	</table>
	<br>
	<span id="span_02" style="color: #000;font-size: 12px;font-weight: 900;display: none">
		<a target="_blank"  style="font-size: 13px;color: #EB661B;position: relative;">
			<img alt="" src="<%=path%>/static/global/images/loading.gif" style="position: absolute;top: 0;left: 0">
			<span style="width: 150px;margin-left: 20px;">正在处理,请勿关闭此页面...</span>
		</a>
	</span>
</body>
<script type="text/javascript">

    $("#btn_upload_file").click(function(){
        excelUpload();
    });

    // file change事件
    $("#ipt_file_h").change(function(){
        $("#ipt_file").val($(this).val());
        $("#span_01").hide();
        $("#span_02").hide();
        $("#a_total").html("0");
        $("#a_success").html("0");
        $("#a_error").html("0");
        $("#a_download").attr("href", "javascript:;");
        $("#tt_error").empty();
    });
    // excel上传
    function excelUpload(){
        $("#ff_file").ajaxSubmit({
            type: 'post',
            dataType: 'json',
            url: appPath + '/admin/excelImport/import',
            beforeSubmit: function(){
                var result = verify();
                if(result){
                    $("#span_02").show();
                }
                return result;
            },
            success: function(data){
                //console.debug(data.result);
                var code = data.code;
                if(code=="10000"){
                    var total = data.result.total;
                    //console.debug(data.result.total);
                    var success = data.result.success;
                    var error = data.result.error;
                    var errorLMap = data.result.errorLMap;
                    var fileName = data.result.fileName;
                    layer.alert("处理完成", {icon: 1,  skin: 'layer-ext-moon'});
                    $("#span_01").show();
                    $("#a_total").html(total);
                    $("#a_success").html(success);
                    $("#a_error").html(error);
                    var href = appPath + "/admin/excelImport/result_download?fileName="+fileName;
                    $("#a_download").attr("href", href);

                    // 错误list
                    if(errorLMap){
                        var innerhtml = "";
                        for(var i=0; i<errorLMap.length; i++){
                            innerhtml += "<tr>";
                            for(var j in errorLMap[i]){
                                innerhtml += "<td>";
                                innerhtml += errorLMap[i][j];
                                innerhtml += "</td>";
                            }
                            innerhtml += "</tr>";
                        }
                        $("#tt_error").empty().append(innerhtml);
                    }
                    return;
                }
                layer.alert("处理失败", {icon: 2,  skin: 'layer-ext-moon'});

            },
            error: function(XmlHttpRequest, textStatus, errorThrown){

            },
            complete: function(){
                $("#span_02").hide();
            }
        });
    }

    // 表单校验
    function verify(){
        // 错误信息
        var msg = "";
        var fileVal = $("#ipt_file_h").val();
        var fileExt = fileVal.substring(fileVal.lastIndexOf(".")+1, fileVal.length);
        // 校验是否选择了文件
        if(!fileVal){
            msg = '请选择excel文件';
            layer.alert(msg, {icon: 2,  skin: 'layer-ext-moon'});return false;
        }
        // 校验文件格式
        if("xls"!=fileExt && "xlsx"!=fileExt){
            msg = '文件格式错误';
            layer.alert(msg, {icon: 2,  skin: 'layer-ext-moon'});
            // 表单重置
            $("#ff_file")[0].reset();$("#ipt_file_h").val("");
            return false;
        }
        return true;
    }

</script>
</html>
