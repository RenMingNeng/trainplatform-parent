function train_subject() {
    var _this = this;
    var page;
    _this.index = -1;
    var items;
    _this.init = function (page_person) {
        page = page_person;
        _this.initTable();
        _this.initEvent();
    };
    //按钮事件
    _this.initEvent = function () {
        //删除按钮
        $("#btn_del").click(function () {
            var id = '';
            var ids = '';
            $(":checkbox[name='ckbox']:checked").each(function () {
                ids += $(this).val() + ',';
                id = $(this).val();
            });
            if (id == '') {
                layer.alert("请选择要删除的主题");
                return;
            }

            _this.index = layer.alert('是否确定删除', {
                btn: ['确定', '取消'],
                btn1: function () {
                    ajax(appPath + "/admin/trainSubject/delete", {idStr: ids}, "post");
                },
                btn2: function () {
                }
            })
        });

        // 新增按钮
        $("#btn_add").click(function () {
            window.location.href = appPath + "/admin/trainSubject/trainsubjectAdd";
            /*var url = appPath + "/admin/trainSubject/add";

             var editLayer = "<div class='box'' id='addLayer'>" +
             "<form  id='addForm' type='POST'  action = " + url + " enctype='multipart/form-data' method='post'> " +
             "<table class='table'>" +
             "<tbody id='trainsubject_table'>" +
             "<tr>" +
             "<td class='th'>主题名称</td>" +
             "<td>" +
             "<input type='text' id='addName' name='addName' required='required' placeholder='主题名称，限64个字符' maxlength='64'/>" +
             "<span id='nameMsg'></span>" +
             "</td>" +
             "</tr>" +
             "<tr style='display: none;'>" +
             "<td class='th'>是否有效</td>" +
             "<td>" +
             "<select id='addValid'  disabled='disabled'>" +
             "<option value='1'>有效</option>" +
             "</select>" +
             "</td>" +
             "</tr>" +
             "<tr>" +
             "<td style='padding-bottom:0;' class='th'>主题描述</td>" +
             "<td style='padding-bottom:0;'>" +
             "<textarea id='addDesc' name='addDesc' rows='7' cols='5' maxlength='256' placeholder='主题描述，限256个字符'></textarea> " +
             "</td>" +
             "</tr>";
             editLayer += "<td style='padding-bottom:0;' class='th'>上传图片</td>" +
             "<td style='padding-bottom:0;' class='text-left'>" +
             '<div id="divPreiew" style="height:100px;width:100px;line-height:100px;background:#ddd;color:#fff;text-align:center;position: relative;">暂无图片<img width="100" height="100" style="position: absolute;left:0;top:0;" alt="" id="imHeadPhoto"></div>' +
             '<input type="file" id="fileName" name="fileName" onchange="PreviewImage(this,\'imHeadPhoto\',\'divPreiew\')"/>' +
             "</td>" +
             "</tr>";
             editLayer += "<tr>" +
             "<td style='padding-top:0;'>" +
             "&nbsp;" +
             "</td>" +
             "<td style='padding-top:0;' class='text-right'>" +
             /!*       "<input type='submit'  class='btn btn-info' value='提交'>" +*!/
             "<a href='javascript:void(0)'  class='btn btn-info' onclick='onSubmit(0)'>提交</a> " +
             " <a href='javascript:void(0)' id='reset' class='btn btn-info' onclick='reset(0)'>重置</a>" +
             "</td>" +
             "</tr>" +
             "</tbody>" +
             "</table>" +
             "</form>" +
             "</div>";
             _this.index = layer.open({
             type: 1,
             title: '新增培训主题',
             shadeClose: false, // true点击遮罩层关闭
             shade: 0.3, // 遮罩层背景
             area: ['600px', '450px'], // 弹出层大小
             scrollbar: false, // false隐藏滑动块
             // content : [appPath+ '/admin/basicinfo/train_subject_edit.jsp','yes' ]
             content: editLayer
             });
             */
        });

        // 修改按钮
        $("#btn_edit").click(function () {
            //1 检查选中的复选框是否只有一个
            var size = $(":checkbox[name='ckbox']:checked").length;
            if (size == 0) {
                layer.alert("请选择要修改的主题");
                return;
            } else if (size > 1) {
                layer.alert("修改条目只可选择一个");
                return;
            }

            //2 获取选中复选框的值
            var id;
            $(":checkbox[name='ckbox']:checked").each(function () {
                id = $(this).val();
            });
            var name, valid, desc, source;
            for (var i = 0; i < items.length; i++) {
                if (id == items[i].varId) {
                    name = items[i].subjectName;
                    valid = items[i].isValid;
                    desc = items[i].subjectDesc;
                    source = items[i].source;
                    logo = items[i].logo;
                    if (source == '1') {
                        layer.alert("系统自带的主题不可修改");
                        return;
                    }
                    break;
                }
            }

            var editLayer = "<div class='box' id='addLayer'>" +
                "<form  id='editForm' type='POST' enctype='multipart/form-data' >" +
                "<input type='hidden' id='varId'  name='id' value='" + id + "'/>" +
                "<input type='hidden' id='source' name='source' value='" + source + "'/>" +
                "<table class='table'>" +
                "<tbody id='trainsubject_table'>" +
                "<tr><td class='th'>主题名称</td><td><input type='text' id='editName'  name='editName' value='" + name + "'/><span id='editNameMsg'></span></td><tr>" +
                "<tr style='display: none;'><td class='th'>是否有效</td><td><select id='editValid'>";
            if (valid == 1) {
                editLayer += "<option value='1' selected='selected'>有效</option>";
                editLayer += "<option value='2' >无效</option>";
            } else if (valid == 2) {
                editLayer += "<option value='1'>有效</option>";
                editLayer += "<option value='2' selected='selected'>无效</option>";
            }
            editLayer += "</select></td><tr>" +
                "<tr><td class='th' style='padding-bottom:0;'>主题描述</td>";

            if (null != desc) {

                editLayer += "<td style='padding-bottom:0;'><textarea id='editDesc' name='editDesc' rows='7' cols='5' maxlength='256'  placeholder='主题描述，限256字符'>" + desc + "</textarea> </td><tr>";
            } else {
                editLayer += "<td style='padding-bottom:0;'><textarea id='editDesc' name='editDesc' rows='7' cols='5' maxlength='256'  placeholder='主题描述，限256字符'></textarea> </td><tr>";
            }
            editLayer += "<td style='padding-bottom:0;' class='th'>上传图片</td>" +
                "<td style='padding-bottom:0;' class='text-left'>" +
                '<div id="divPreiew"><img width="100" height="100"  alt=""  id="imHeadPhoto" src="' + logo + '"><input type="hidden" name="imgName" value="' + logo + '"/></div>' +
                '<input type="file" id="fileName" name="fileName" onchange="PreviewImage(this,\'imHeadPhoto\',\'divPreiew\')"/>' +
                "</td>" +
                "</tr>";
            //editLayer += '<tr><td>&nbsp;</td><td class="text-left"><div id="divPreiew"><img width="100" height="100"  alt=""  id="imHeadPhoto" src="\' + logo + \'"><input type="hidden" name="imgName" value="\' + logo + \'"/></div></td></tr>'
            editLayer += "<tr><td style='padding-top:0;'></td><td style='padding-top:0;' class='text-right'><a href='javascript:;' class='btn btn-info' onclick='onSubmit(1)'>提交</a> " +
                " <a href='javascript:void(0)' id='reset' class='btn btn-info' onclick='reset(1)'>重置</a>" +
                "</td></tr>" +
                "</tbody>" +
                "</table>" +
                "</form>" +
                "</div>";
            _this.index = layer.open({
                type: 1,
                title: '修改培训主题',
                shadeClose: false, // true点击遮罩层关闭
                shade: 0.3, // 遮罩层背景
                area: ['600px', '450px'], // 弹出层大小
                scrollbar: false, // false隐藏滑动块
                // content : [appPath+ '/admin/basicinfo/train_subject_edit.jsp','yes' ]
                content: editLayer
            });
        });
        // 详情按钮
        $("#btn_all").click(function () {
            //1 检查选中的复选框是否只有一个
            var size = $(":checkbox[name='ckbox']:checked").length;
            if (size == 0) {
                layer.alert("请选择要查看的主题");
                return;
            } else if (size > 1) {
                layer.alert("查看条目只可选择一个");
                return;
            }
            //2 获取选中复选框的值
            var id;
            $(":checkbox[name='ckbox']:checked").each(function () {
                id = $(this).val();
            });
            var name, valid, desc, source;
            for (var i = 0; i < items.length; i++) {
                if (id == items[i].varId) {
                    name = items[i].subjectName;
                    valid = items[i].isValid;
                    desc = items[i].subjectDesc;
                    source = items[i].source;
                    logo = items[i].logo;
                    break;
                }
            }

            var editLayer = "<div class='box' id='addLayer'>" +
                "<form  id='editForm' type='POST'>" +
                "<input type='hidden' id='varId' value='" + id + "'/>" +
                "<input type='hidden' id='source' value='" + source + "'/>" +
                "<table class='table'>" +
                "<tbody id='trainsubject_table'>" +
                "<tr><td class='th'>主题名称</td><td><input type='text' id='editName' readonly='readonly' value='" + name + "'/><span id='editNameMsg'></span></td><tr>" +
                "<tr><td class='th'>主题描述</td>";
            if (desc == null) {
                editLayer += "<td><textarea id='editDesc' rows='7' cols='5' maxlength='256' readonly='readonly' placeholder='主题描述，限256字符'></textarea> </td><tr>";

            } else {
                editLayer += "<td><textarea id='editDesc' rows='7' cols='5' maxlength='256' readonly='readonly' placeholder='主题描述，限256字符'>" + desc + "</textarea> </td><tr>";

            }
            editLayer += "<td style='padding-bottom:0;' class='th'>主题照片</td>" +
                "<td style='padding-bottom:0;' class='text-left'>" +
                '<div id="divPreiew"><img width="100" height="100"  alt=""  id="imHeadPhoto" src="' + logo + '"></div>' +
                "</td>" +
                "</tr>";
            editLayer += "</tbody>" +
                "</table>" +
                "</form>" +
                "</div>";
            _this.index = layer.open({
                type: 1,
                title: '培训主题详情',
                shadeClose: true, // true点击遮罩层关闭
                shade: 0.3, // 遮罩层背景
                area: ['600px', '450px'], // 弹出层大小
                scrollbar: false, // false隐藏滑动块
                // content : [appPath+ '/admin/basicinfo/train_subject_edit.jsp','yes' ]
                content: editLayer
            });
        });
        //全选与非全选
        $("#all").click(function () {
            if (this.checked) {
                $("#trainsubject_table :checkbox:not(:disabled)").prop("checked", true);
            } else {
                $("#trainsubject_table :checkbox:not(:disabled)").prop("checked", false);
            }
        });
    }

    _this.initTable = function () {
        var list_url = appPath + "/admin/trainSubject/paging";
        page.init("trainsubject_form", list_url, "trainsubject_table", "trainsubject_page", 1, 10);
        page.goPage(1);
        page.list = function (dataList) {
            $("#all").attr("checked", false);
            var len = dataList.length;
            items = dataList;
            var inner = "", item;
            for (var i = 0; i < len; i++) {
                // 组装数据
                item = dataList[i];
                inner += '<tr>';
                if (item.source == '1') {
                    inner += '<td><input disabled type="checkbox" name="ckbox"  value="' + item.varId + '" /></td>';
                } else if (item.source == '2') {
                    inner += '<td><input type="checkbox" name="ckbox"  value="' + item.varId + '" /></td>';
                }
                inner += '<td>' + (i + 1) + '</td>';
                inner += '<td>';
                inner += '<span>' + item.subjectName + '</span>';
                inner += '</td>';
                var temp_logo = appPath + "/static/global/images/subject_image/subject_image_" + parseInt(Math.round(Math.random())) + ".png";
                if ("" == item.logo) {
                    inner += '<td><img width="100" height="100" src="' + temp_logo + '" alt=""></td>';
                } else {
                    inner += '<td><img data-original="'+temp_logo+'" width="100" height="100" src=' + item.logo + ' alt=""></td>';
                }
                if (item.source == '1') {
                    inner += '<td><span class="text-gray">系统自带</span></td>';
                } else if (item.source == '2') {
                    inner += '<td><span class="text-green">企业自制</span></td>';
                }
                inner += '<td>' + item.courseCount + '</td>';
                inner += '<td>' + parseFullDate(item.operDate) + '</td>';

                if (item.source == '1') {
                    inner += '<td><a href="javascript:train_subject.info(\'' + item.varId + '\');" id="btn_all" class="a a-view">详情</a> ';
                    inner += '<a href="javascript:;" class="a" id="">修改</a></td>';
                } else if (item.source == '2') {
                    inner += '<td><a href="javascript:train_subject.info(\'' + item.varId + '\');"  id="btn_all" class="a a-info">详情</a> ';
                    inner += '<a href="javascript:train_subject.modify(\'' + item.varId + '\');" id="" class="a a-view">修改</a></td>';
                }
                inner += '</tr>';
            }
            return inner;
            console.log(inner)
        }
    };


    _this.search = function () {
        _this.initTable();
    };

    _this.searchAll = function () {
        $("#trainsubject_form").find("input[id=abc]").val("");
        _this.initTable();
    };

    _this.bind_click = function (t) {
        var value = $(t).attr("data-value");
        $("#person_form").find("input[name=role_id]").val(value);
        _this.initTable();
    };

    _this.info = function (varId) {
        window.location.href = appPath + "/admin/trainSubject/trainSubjectInfo?varId=" + varId;
    }

    _this.modify = function (varId) {
        window.location.href = appPath + "/admin/trainSubject/trainsubjectModify?varId=" + varId;
    }


    _this.ajax = function (url, param, type) {
        var result;
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function (data) {
                result = data;

            }
        });
        return result;
    };
}

var train_subject = new train_subject();

//表单重置
function reset(type) {
    if (type == 0)
        $("#addForm")[0].reset();
    else if (type == 1)
        $("#editName").val("");
    $("#editDesc").val("");
    $("#imHeadPhoto").attr("src", "");


}

function parseFullDate(str) {
    if (!str) return "";
    if (str.length <= 10) return "";
    return str.substring(0, 10);
}

/*
 *
 * 预览照片*/

//js本地图片预览，兼容ie[6-9]、火狐、Chrome17+、Opera11+、Maxthon3
function PreviewImage(fileObj, imgPreviewId, divPreviewId) {
    var allowExtention = ".jpg,.bmp,.gif,.png";//允许上传文件的后缀名document.getElementById("hfAllowPicSuffix").value;
    var extention = fileObj.value.substring(fileObj.value.lastIndexOf(".") + 1).toLowerCase();
    var browserVersion = window.navigator.userAgent.toUpperCase();
    if (allowExtention.indexOf(extention) > -1) {
        if (fileObj.files) {//HTML5实现预览，兼容chrome、火狐7+等
            if (window.FileReader) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    document.getElementById(imgPreviewId).setAttribute("src", e.target.result);
                }
                reader.readAsDataURL(fileObj.files[0]);
            } else if (browserVersion.indexOf("SAFARI") > -1) {
                alert("不支持Safari6.0以下浏览器的图片预览!");
            }
        } else if (browserVersion.indexOf("MSIE") > -1) {
            if (browserVersion.indexOf("MSIE 6") > -1) {//ie6
                document.getElementById(imgPreviewId).setAttribute("src", fileObj.value);
            } else {//ie[7-9]
                fileObj.select();
                if (browserVersion.indexOf("MSIE 9") > -1)
                    fileObj.blur();//不加上document.selection.createRange().text在ie9会拒绝访问
                var newPreview = document.getElementById(divPreviewId + "New");
                if (newPreview == null) {
                    newPreview = document.createElement("div");
                    newPreview.setAttribute("id", divPreviewId + "New");
                    newPreview.style.width = document.getElementById(imgPreviewId).width + "px";
                    newPreview.style.height = document.getElementById(imgPreviewId).height + "px";
                    newPreview.style.border = "solid 1px #d2e2e2";
                }
                newPreview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + document.selection.createRange().text + "')";
                var tempDivPreview = document.getElementById(divPreviewId);
                tempDivPreview.parentNode.insertBefore(newPreview, tempDivPreview);
                tempDivPreview.style.display = "none";
            }
        } else if (browserVersion.indexOf("FIREFOX") > -1) {//firefox
            var firefoxVersion = parseFloat(browserVersion.toLowerCase().match(/firefox\/([\d.]+)/)[1]);
            if (firefoxVersion < 7) {//firefox7以下版本
                document.getElementById(imgPreviewId).setAttribute("src", fileObj.files[0].getAsDataURL());
            } else {//firefox7.0+
                document.getElementById(imgPreviewId).setAttribute("src", window.URL.createObjectURL(fileObj.files[0]));
            }
        } else {
            document.getElementById(imgPreviewId).setAttribute("src", fileObj.value);
        }
    } else {
        alert("仅支持" + allowExtention + "为后缀名的文件!");
        fileObj.value = "";//清空选中文件
        if (browserVersion.indexOf("MSIE") > -1) {
            fileObj.select();
            document.selection.clear();
        }
        fileObj.outerHTML = fileObj.outerHTML;
    }
}

//提交
function onSubmit(op) {
    if (op == 1) {
        //修改
        var name = document.getElementById("editName").value;
        if (name == '') {
            $("#editNameMsg").html("<font color='red'> 请填写主题名称</font>");
            return;
        }


        var opt = {
            url: appPath + "/admin/trainSubject/update",
            type: "post",
            dataType: "json",
            success: function (data) {
                result = data;
                train_subject.initTable();
                if (train_subject.index != -1) {
                    layer.close(train_subject.index);
                }
            }
        }
        $("#editForm").ajaxSubmit(opt);

    } else if (op == 0) {
        //新增
        var name = $('#addName').val();
        if (name == '') {
            $("#nameMsg").html("<font color='red'> 请填写主题名称</font>");
            return;
        }
        var options = {
            url: appPath + "/admin/trainSubject/add",
            type: "post",
            dataType: "json",
            success: function (data) {
                result = data;
                train_subject.initTable();
                if (train_subject.index != -1) {
                    layer.close(train_subject.index);
                }
            }
        }
        $("#addForm").ajaxSubmit(options);

    }
}

//异步请求
function ajax(url, param, type) {
    $.ajax({
        url: url,
        async: true,
        type: 'post',
        data: param,
        success: function (data) {
            result = data;
            train_subject.initTable();
            if (train_subject.index != -1) {
                layer.close(train_subject.index);
            }
        }
    });
}
