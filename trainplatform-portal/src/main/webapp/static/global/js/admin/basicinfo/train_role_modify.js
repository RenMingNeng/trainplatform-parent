var train_role_modify = new Object();
train_role_modify.courses = [];
train_role_modify.courseIds = [];
train_role_modify.init = function (basePath, page, options) {

    //人员的操作
    train_role_modify.handle();
   
}
train_role_modify.handle = function () {
//保存
    $("#save").click(function () {
        var roleName = $("#roleName").val()
        if ("" == roleName) {
            layer.msg("请填写角色名称")
            return;
        }
        if (roleName.length>15) {
            layer.msg("角色名称请少于15字")
            return;
        }

        var url = appPath + "/admin/trainRole/update"
        //$("#trainsubject_form").attr("action",url);
        var opt = {
            url: url,
            type: "post",
            dataType: "json",
            success: function (data) {
                result = data.code;
                if ("10000" == result) {
                    window.location.href = appPath + "/admin/trainRole"
                }
            }
        }
        $("#trainrole_form").ajaxSubmit(opt);
    })
}