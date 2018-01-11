var train_role_add = new Object();
train_role_add.courses = [];

train_role_add.init = function (basePath, page, options) {

    //对于课程和人员的操作
    train_role_add.handle();

}
train_role_add.handle = function () {
//保存
    $("#save").click(function () {
        var roleName = $.trim($("#roleName").val())
        if ("" == roleName) {
            layer.msg("请填写角色名称")
            return;
        }
        if (roleName.length>15) {
            layer.msg("角色名称请少于15字")
            return;
        }
        $.ajax({
            url: appPath + "/admin/trainRole/verify",
            type: 'post',
            data: {"roleName": roleName},
            success: function (data) {
                if ("1" == data.code) {
                    layer.msg("角色名称重复，请重置");
                    return;
                } else {
                    var url = appPath + "/admin/trainRole/add";
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
                    };
                    $("#trainrole_form").ajaxSubmit(opt);
                }
            }
        });
    })
};