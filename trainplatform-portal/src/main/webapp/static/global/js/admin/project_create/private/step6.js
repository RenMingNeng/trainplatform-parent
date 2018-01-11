
var step6=new Object();
step6.roles="";
step6.projectTypeNo = $("#projectTypeNo").val();
step6.projectId = $("#projectId").val();
step6.projectStatus=$("#projectStatus").val();
step6.examPermission=$("#examPermission").val();

    step6.init=function(basePath,options){
       step6.basePath=basePath;
        //含有培训的
        step6.permissionTrain=options.permissionTrain;
        //含有考试的
        step6.permissionExam=options.permissionExam;
        //含有练习的
        step6.permissionExercise=options.permissionExercise;
        //选择受训角色
        step6.initRole();
        //对于发布的操作
        step6.handle();

        stragegySelect.stragegy_listener();

        stragegySelect.stragegy_select(step6.projectId, step6.permissionTrain, options.projectStatus, options.examPermission);
    }



   //初始化受训角色
    step6.initRole=function() {
        $.ajax({
            url: appPath + '/admin/project_create/private/trainRole',
            dataType: 'json',
            type:'get',
            success:function (data) {
                var list=data.result;
                step6.roles= list;
            }

        })

     }

        step6.handle=function () {
            //回到首页
            $("#index").click(function () {
                window.location.href = appPath + "/admin";
            })
            //上一步操作
            $("#pev").click(function () {
                var source= $("#source").val()
                if(source=="step5"){
                    window.location.href = appPath + "/admin/project/advanceSetting/"+ source +"?projectTypeNo="+step6.projectTypeNo+"&projectId="+step6.projectId+"&projectStatus="+step6.projectStatus+"&examPermission="+step6.examPermission;;
                }else{
                    window.location.href = appPath + "/admin/project_create/private/"+ source +"?projectTypeNo="+step6.projectTypeNo+"&projectId="+step6.projectId+"&projectStatus="+step6.projectStatus+"&examPermission="+step6.examPermission;;
                }

            })
            //发布
            $("#btn_project_save_publish").click(function () {
                //保存组卷策略
                var status = stragegySelect.stragegy_save();
                if(!status){
                    layer.alert("组卷策略数值不对，不能发布");
                    return;
                }

              if(step6.projectStatus == '1'){
                layer.confirm('是否发布', {btn: ['是','否']}, function(index){
                  //发布项目
                  step6.releaseProject();
                },function(){
                  window.location.href = appPath + "/admin";
                });
              }else{
                  step6.releaseProject();
              }
            })

        }

        //发布项目
        step6.releaseProject=function(){
                var projectStatus = "2";
                $.ajax({
                    dataType: 'json',
                    url: appPath + '/admin/project_create/private/publish_project',
                    async: false,
                    type: 'post',
                    data: {
                        'projectId': $("#projectId").val(),
                        'projectStatus': projectStatus,
                        "permissionExam": step6.permissionExam
                    },
                    success: function(data){
                        var code = data['code'];
                        if (code == '10000' && data.result == null) {
                            layer.msg("发布成功",{time:1000});
                            window.location.href = appPath+ "/admin";
                        } else {
                            layer.alert(data.result);
                        }
                    }
                });
        };