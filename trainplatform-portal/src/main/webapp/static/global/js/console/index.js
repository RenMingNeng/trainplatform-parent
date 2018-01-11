var console_index = new Object();

console_index.init = function () {
    // 初始化事件
    console_index.initEvent();
}

console_index.initEvent = function () {

    // ex_course_type 刷新数据
    $("#ex_course_type_flush").bind("click", function () {
        $.ajax({
            url: appPath + "/console/getCourseType",
            type: 'post',
            success: function (data) {
                if (10000 == data.code) {
                    console.log("==消息队列发送成功=");
                }
            }
        });

        alert('正在请求后台做刷新'); // 写在ConsoleController里面
    });
    // ex_course_type 刷新数据
    $("#quession").bind("click", function () {
        $.ajax({
            url: appPath + "/console/getQuessionCount",
            type: 'post',
            success: function (data) {
                if (10000 == data.code) {
                    alert("课程题目数量更新成功");
                }
            }
        });

    });


    // web.properties 版本号维护
    $("#webProperties").bind("click", function () {
        window.location.href = appPath +"/console/maintainWebProperties"

    });

    //project_course、project_course_info表中question_count字段更新
    $("#updateQuestionCount").bind("click", function () {
        var projectId = $("#project_id").val();
        if("" == projectId){
            layer.msg("请输入项目编号");
            return;
        }
        $.ajax({
            url: appPath + "/console/updateProjectCourse",
            type: 'post',
            data:{"projectId":projectId},
            success: function (data) {
                if (10000 == data.code) {
                    layer.msg("更新成功");
                }else{
                    layer.msg("更新失败");
                }
            }
        });

    });

    //project_exercise_order、project_statistics_info表中total_question字段更新
    $("#updateTotalQuestion").bind("click", function () {
         var projectId = $("#projectId").val();
         if("" == projectId){
             layer.msg("请输入项目编号");
             return;
         }
        $.ajax({
            url: appPath + "/console/updateTotalQuestion",
            type: 'post',
            data:{"projectId":projectId},
            success: function (data) {
                var result = data.result;
                if (10000 == data.result) {
                    layer.msg("更新成功");
                }else{
                    layer.msg(result);
                }
            }
        });

    });

    //project_basic、project_info表中project_status字段更新
    $("#updateProjectStatus").bind("click", function () {
        var projectId = $("#id").val();
        if("" == projectId){
            layer.msg("请输入项目编号");
            return;
        }
        $.ajax({
            url: appPath + "/console/updateProjectStatus",
            type: 'post',
            data:{"projectId":projectId},
            success: function (data) {
                var result = data.result;
                if (10000 == data.result) {
                    layer.msg("更新成功");
                }else{
                    layer.msg(result);
                }
            }
        });

    });
};

