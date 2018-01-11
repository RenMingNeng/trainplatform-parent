function iniUploadFile(id,upload_url, mime_types){
	if(mime_types == undefined){
		mime_types = [{title : "Image files", extensions : "jpg,png,txt,zip,doc,docx,ppt,pptx,pdf,xls,xlsx"}];
	}

    if(!$("#" + id + "_uploader").length){
        var box = $("<div>").attr("id", id + "_uploader");
        $("#" + id + "_uploader_container").append(box);
    }

    $("#" + id + "_uploader").pluploadQueue({
        // General settings
        runtimes : "html4",
        url : upload_url,
        unique_names : true,
        multi_selection : true,
        filters : {
            mime_types: mime_types,
			max_file_size: '2048kb'
        }
    });
}

// function my_plupload_delete(id) {
//     $('#' + id).on('click', '#old_file.plupload_delete a', function(e) {
//         var $this = $(this).parents("div[id$='_uploader']");
//         var _this = $(this).parents("li#old_file");
//         var name = _this.attr("filename");
//
//         layer.confirm('您确认想要删除附件吗？', {
//             icon: 3,  skin: 'layer-ext-moon'
//         }, function(index){
//             for(var i=0; i<plupload_data_array.length; i++){
//                 var obj = plupload_data_array[i];
//                 if(obj.name == name){
//                     plupload_data_array.splice(i,1);
//                     _this.remove();
//                     layer.close(index);
//                     return;
// 				}
//             }
//         });
//
//     });
// }

function init_plupload_data(id){
	var html = "";
	if(plupload_data_array.length < 1){
		return html;
	}

	for(var i=0; i<plupload_data_array.length; i++){
		var obj = plupload_data_array[i];
        html += '<li id="old_file" fileName="' + obj.name + '" class="plupload_delete">' +
            '<div class="plupload_file_name"><span>' + obj.name + '</span></div>' +
            '<div class="plupload_file_action"><a href="#" style="display: block;"></a></div>' +
            '<div class="plupload_file_status">100%</div>' +
            '<div class="plupload_file_size">' + plupload.formatSize(obj.size) + '</div>' +
            '<div class="plupload_clearer">&nbsp;</div>' +
        '</li>';
	}
	return html;
}

var plupload_data_array = new Array();
function add_plupload_data(id, files){
    plupload_data_array = new Array();

    for(var i=0; i<files.length; i++){
        plupload_data_array.push(files[i]);
        // remove_upload_data_array(files[i].name);
    }
}

function my_plupload_cancel(index, id){
	var status = $("#" + id + "_uploader").parents("div[id^=layui-layer]").attr("upload_status");
	if(status == 'true' || status == true){
		return true;
	}
	layer.confirm('<div style="text-align:center;line-height:60px;">文件未上传完，关闭会在后台继续上传</div>', {
		btn: ['确定','取消'] //按钮
	}, function(i){
//		var maxmin = $("#" + id + "_uploader").parents("div[id^=layui-layer]").find(".layui-layer-maxmin").length;
//		if(maxmin < 1){
//			$("#" + id + "_uploader").parents("div[id^=layui-layer]").find(".layui-layer-min").trigger("click");
//		}
		$("#" + id + "_uploader").parents("div[id^=layui-layer]").remove();
		layer.close(i);
	});
	return false;
}

//开始上传
function my_plupload_start(id){
//	console.log("开始上传！");
//	$("#" + id).parents("div[id^=layui-layer]").find(".layui-layer-min").trigger("click");
	$("#" + id).parents("div[id^=layui-layer]").attr("upload_status", "false");
}

//上传成功
function my_plupload_success(id){
//	console.log("上传成功！");
	$("#" + id).parents("div[id^=layui-layer]").attr("upload_status", "true");
}

var file_id_path = {};
function backUpCallBack(id, response_, fileId) {
    var data = eval("(" + response_ + ")");
	var result = data['result'];
	var value = $("#fileList").val();
	var upload = result.gridFSFileId + ";" + result.name;
    value += value == '' ? upload : "," + upload;
    $("#fileList").val(value)

    file_id_path[fileId] = upload;
}

// function remove_upload_data_array(name) {
// 	var array = plupload_upload_data_array;
//     plupload_upload_data_array = new Array();
//     for(var i=0; i<array.length; i++){
//         if(array[i].name == name){
//             plupload_upload_data_array.push(array[i]);
//         }
//     }
// }

//id 按钮id
function delCallback(id, rm_files){
    console.log(rm_files.id);
    var result = '';
    for(var i=0; i<plupload_data_array.length; i++){
        var upload = file_id_path[plupload_data_array[i].id];
        result += result == '' ? upload : "," + upload;
    }
    $("#fileList").val(result)
}
