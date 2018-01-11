    function train_role() {
    var _this = this;
    var page;
    _this.index = -1;
    var items;
    _this.init = function (page_person) {
        page = page_person;
        _this.initTable();
        _this.initEvent();
    };
    _this.initEvent = function () {
        // 新增按钮
        $("#btn_add").click(function () {
            window.location.href = appPath + "/admin/trainRole/trainRoleAdd";
        });
        /*新增结束*/
        // 详情按钮
        $("#btn_all").click(function () {
            //1 检查选中的复选框是否只有一个
            var size = $(":checkbox[name='ckbox']:checked").length;
            if (size == 0) {
                layer.alert("请选择要查看的角色");
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
                    name = items[i].roleName;
                    valid = items[i].isValid;
                    desc = items[i].roleDesc;
                    source = items[i].source;
                    break;
                }
            }

            var editLayer = "<div class='box' id='addLayer'>" +
                "<form  id='editForm' type='POST'>" +
                "<input type='hidden' id='varId' value='" + id + "'/>" +
                "<input type='hidden' id='source' value='" + source + "'/>" +
                "<table class='table'>" +
                "<tbody id='trainrole_table'>" +
                "<tr><td class='th'>角色名称</td><td><input type='text' id='editName'  readonly='readonly' value='" + name + "'/>" +
                "<span id='editNameMsg'></span></td><tr>" +
                /* "<tr><td class='th'>是否有效</td><td><select id='editValid' disabled='disabled'>";
                 if (valid == 1) {
                 editLayer += "<option value='1' selected='selected' >有效</option>";
                 editLayer += "<option value='2' >无效</option>";
                 } else if (valid == 2) {
                 editLayer += "<option value='1'>有效</option>";
                 editLayer += "<option value='2' selected='selected'>无效</option>";
                 }
                 editLayer += "</select></td><tr>" +*/
                "<tr><td class='th'>角色描述</td>" +
                "<td>";
            if (desc == null) {
                editLayer += "<textarea id='editDesc' rows='7' cols='5' maxlength='256'  readonly='readonly' placeholder='角色描述，限256字符'></textarea> </td><tr>";
            } else {
                editLayer += "<textarea id='editDesc' rows='7' cols='5' maxlength='256' readonly='readonly' placeholder='角色描述，限256字符'>" + desc + "</textarea> </td><tr>";
            }
            editLayer += "</tbody>" +
                "</table>" +
                "</form>" +
                "</div>";
            _this.index = layer.open({
                type: 1,
                title: '培训角色详情',
                shadeClose: true, // true点击遮罩层关闭
                area: ['600px', '280px'], // 弹出层大小
                scrollbar: false, // false隐藏滑动块
                // content : [appPath+ '/admin/basicinfo/train_role_edit.jsp','yes' ]
                content: editLayer
            });
        });
        // 修改按钮
        $("#btn_edit").click(function () {
            //1 检查选中的复选框是否只有一个
            var size = $(":checkbox[name='ckbox']:checked").length;
            if (size == 0) {
                layer.alert("请选择要修改的角色");
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
                    name = items[i].roleName;
                    valid = items[i].isValid;
                    desc = items[i].roleDesc;
                    source = items[i].source;
                    if (source == '1') {
                        layer.alert("系统自带的主题不可修改");
                        return;
                    }
                    break;
                }
            }
            var editLayer = "<div class='box' id='addLayer'>" +
                "<form  id='editForm' type='POST'>" +
                "<input type='hidden' id='varId' value='" + id + "'/>" +
                "<input type='hidden' id='source' value='" + source + "'/>" +
                "<table class='table'>" +
                "<tbody id='trainrole_table'>" +
                "<tr><td class='th'><b class='text-red'>*</b>角色名称</td><td><input type='text' id='editName' value='" + name + "'/>" +
                "<span id='editNameMsg'></span></td><tr>" +
                "<tr style='display: none;'><td class='th'>是否有效</td><td><select id='editValid'>";
            if (valid == 1) {
                editLayer += "<option value='1' selected='selected'>有效</option>";
                editLayer += "<option value='2' >无效</option>";
            } else if (valid == 2) {
                editLayer += "<option value='1'>有效</option>";
                editLayer += "<option value='2' selected='selected'>无效</option>";
            }
            editLayer += "</select></td><tr>" +
                "<tr><td class='th  style='padding-bottom:0;''>角色描述</td>" +
                "<td  style='padding-bottom:0;'>";
            if (desc == null) {
                editLayer += "<textarea id='editDesc' rows='7' cols='5' maxlength='256' placeholder='角色描述，限256字符'></textarea> </td><tr>";
            } else {
                editLayer += "<textarea id='editDesc' rows='7' cols='5' maxlength='256' placeholder='角色描述，限256字符'>" + desc + "</textarea> </td></tr>";
            }
            editLayer += "<tr><td style='padding-top:0;'>&nbsp;</td><td style='padding-top:0;' class='text-right'><a href='javascript:;' class='btn btn-info' onclick='onSubmit(1)'>提交</a> " +
                " <a href='javascript:void(0)' id='reset' class='btn btn-info' onclick='reset(1)'>重置</a>" +
                "</td></tr>" +
                "</tbody>" +
                "</table>" +
                "</form>" +
                "</div>";
            _this.index = layer.open({
                type: 1,
                title: '修改培训角色',
                shadeClose: false, // true点击遮罩层关闭
                shade: 0.3, // 遮罩层背景
                area: ['600px', '320px'], // 弹出层大小
                scrollbar: false, // false隐藏滑动块
                // content : [appPath+ '/admin/basicinfo/train_role_edit.jsp','yes' ]
                content: editLayer
            });
        });
        // 修改结束
        //删除按钮
        $("#btn_del").click(function () {
            var id = '';
            var ids = '';
            $(":checkbox[name='ckbox']:checked").each(function () {
                id = $(this).val();
                ids += $(this).val() + ',';
            });
            if (id == '') {
                layer.alert("请选择要删除的角色");
                return;
            }
            _this.index = layer.alert('是否确定删除', {
                btn: ['确定', '取消'],
                btn1: function () {
                    ajax(appPath + "/admin/trainRole/delete", {idStr: ids}, "post");
                }
            })
        });
        /*导入*/
        $("#btn_im").click(function () {
            // 导入模板名称
            var tempType = "2";
            var params = new Object();
            layer.open({
                type: 2,
                title: '导入excel',
                shadeClose: false,				//true点击遮罩层关闭
                shade: 0.3,					//遮罩层背景
                maxmin: true, 					// 开启最大化最小化按钮
                area: ['1000px', '80%'],		//弹出层大小
                scrollbar: false,				//false隐藏滑动块
                content: [appPath + '/admin/excelImport/to_import?tempType=' + tempType + '&exportTye=2' + '&params=' + encodeURIComponent(JSON.stringify(params)), 'yes'],
                cancel: function () {
                    layer.close(train_role.index);
                    _this.initTable();
                }
            })
        });
        /*导出*/
        $("#btn_ex").click(function () {
            // 导出模板名称
            var ids = '';
            $(":checkbox[name='ckbox']:checked").each(function () {
                ids += $(this).val() + ',';
            });
            _this.index = layer.alert('是否确定导出', {
                btn: ['确定', '取消'],
                btn1: function () {
                    var fileName = new Date().getTime() + ".xlsx";
                    var roleName = $("#all").val();

                    window.location.href = appPath + '/admin/trainRole/excel_export?fileName=' + fileName + '&idStr=' + ids + '&roleName=' + roleName + '&params=role&exportTye=2';
                    layer.close(train_role.index);
                }
            })


        });
        //全选与非全选
        $("#send").click(function () {
            if (this.checked) {
                $("#trainrole_table :checkbox:not(:disabled)").prop("checked", true);
            } else {
                $("#trainrole_table :checkbox:not(:disabled)").prop("checked", false);
            }
        });
    }
    _this.initTable = function () {
        var list_url = appPath + "/admin/trainRole/paging";
        page.init("trainrole_form", list_url, "trainrole_table", "trainrole_page", 1, 10);
        page.goPage(1);
        page.list = function (dataList) {
            $("#all").attr("checked", false);
            var len = dataList.length;
            items = dataList;
            var inner = "", item;
            // 组装数据
            for (var i = 0; i < len; i++) {
                item = dataList[i];
                inner += '<tr>';
                if (item.source == '1') {
                    inner += '<td><input type="checkbox" name="ckbox" disabled value="' + item.varId + '" /></td>';
                } else if (item.source == '2') {
                    inner += '<td><input type="checkbox" name="ckbox" value="' + item.varId + '" /></td>';
                }
                inner += '<td>' + (i + 1) + '</td>';
                inner += '<td><span>' + item.roleName + '</span></td>';
                if (item.source == '1') {
                    inner += '<td><span class="text-gray">系统自带</span></td>';
                } else if (item.source == '2') {
                    inner += '<td><span class="text-green">企业自制</span></td>';
                }
                inner += '<td>' +parseFullDate(item.operDate) + '</td>';
                if (item.source == '1') {
                    inner += '<td><a href="javascript:train_role.info(\'' + item.varId + '\');" id="btn_all" class="a a-view">详情</a> ';
                    inner += '<a href="javascript:;" id="btn_edit" class="a">修改</a></td>';
                } else if (item.source == '2') {
                    inner += '<td><a href="javascript:train_role.info(\'' + item.varId + '\');"  id="btn_all"  class="a a-info">详情</a> ';
                    inner += '<a href="javascript:train_role.modify(\'' + item.varId + '\');"  id="btn_edit" class="a a-view">修改</a></td>';
                }
                inner += '</tr>';
            }
            return inner;
        }
    };
    _this.info = function (varId) {
        window.location.href = appPath + "/admin/trainRole/trainRoleInfo?varId=" + varId;
    }

    _this.modify = function (varId) {
        window.location.href = appPath + "/admin/trainRole/trainRoleModify?varId=" + varId;
    }
    _this.search = function () {
        _this.initTable();
    };
    _this.searchAll = function () {
        $("#all").val("");
        _this.initTable();
    };
    _this.bind_click = function (t) {
        var value = $(t).attr("data-value");
        $("#person_form").find("input[name=role_id]").val(value);
        _this.initTable();
    };
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
    function parseFullDate(str) {
        if(!str) return "";
        if(str.length<=10) return "";
        return str.substring(0,10);
    }

//表单重置
function reset(type) {
    if (type == 0)
        $("#addForm")[0].reset();
    else if (type == 1)
        $("#editName").val("");
    $("#editDesc").val("");
}

//提交
function onSubmit(op) {
    if (op == 1) {
        //修改
        var name = document.getElementById("editName").value;
        if (name == '') {
            $("#editNameMsg").html("<font color='red'> 请填写角色名称</font>");
            return;
        }
        var id = document.getElementById("varId").value;
        var valid = document.getElementById("editValid").value;
        var desc = document.getElementById("editDesc").value;
        var source = document.getElementById("source").value;
        ajax(appPath + "/admin/trainRole/update", {
            varId: id,
            source: source,
            roleName: name,
            isValid: valid,
            roleDesc: desc
        }, "post");
    } else if (op == 0) {
        //新增
        var name = document.getElementById("addName").value;
        if (name == '') {
            $("#nameMsg").html("<font color='red'> 请填写角色名称</font>");
            return;
        }
        var valid = document.getElementById("addValid").value;
        var desc = document.getElementById("addDesc").value;
        ajax(appPath + "/admin/trainRole/add", {roleName: name, isValid: valid, roleDesc: desc}, "post");
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
            train_role.initTable();
            if (train_role.index != -1) {
                layer.close(train_role.index);
            }
        }
    });
}

var train_role = new train_role();