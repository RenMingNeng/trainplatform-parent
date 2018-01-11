
var selectRole=new Object();
selectRole.roles='';
selectRole.projectTypeNo = $("#projectTypeNo").val();
selectRole.projectId = $("#projectId").val();
selectRole.projectStatus = $("#projectStatus").val();
selectRole.examPermission=$("#examPermission").val();

    selectRole.init=function(basePath,page,options){
       selectRole.basePath=basePath;
        selectRole.page=page;
        //含有培训的
        selectRole.permissionTrain=options.permissionTrain;
        //含有考试的
        selectRole.permissionExam=options.permissionExam;
        //含有练习的
        selectRole.permissionExercise=options.permissionExercise;
        selectRole.select_role();
        //开关相关事件
          selectRole.hoddleSwitch();
    }


    // 项目-受训角色-课程信息
    selectRole.select_role = function(){
        var page_size=10
        var course_list_url = appPath + "/admin/project_create/private/trainRole";
        selectRole.page.init("select_role_form", course_list_url, "select_role", "select_role_page", 1, page_size);
        selectRole.page.goPage(1);
        selectRole.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item,role;
            var flag=true;
            // 组装数据
            for(var i=0; i< len; i++) {
                    item = dataList[i];
                    inner += '<tr >';
                    inner += '<td><span class="text-orange">'+item['varRoleName']+'</span>';
                    inner += '</td>'
                    if(item['selected'] == 'selected'){
                      if("3" == selectRole.projectStatus){
                        inner += '<td><div class="on switch_btn switch_index switch_'+ i +'" data-roleId="'+ item['varId'] +'"  data-roleName="'+ item['varRoleName'] +'" data-index="'+ i +'"><span></span>';
                      }else{
                        inner += '<td><div class="on switch_btn switch_index switch_'+ i +'" data-roleId="'+ item['varId'] +'"  data-roleName="'+ item['varRoleName'] +'" data-index="'+ i +'" onclick="selectRole.switch(this);"><span></span>';
                      }
                    }else{
                      flag = false;
                      inner += '<td><div class="switch_btn switch_index switch_'+ i +'" data-roleId="'+ item['varId'] +'"  data-roleName="'+ item['varRoleName'] +'" data-index="'+ i +'" onclick="selectRole.switch(this);"><span></span>';
                    }
                    inner += '</td>'
                    inner += '</tr>';
            }
          if(flag){
            $(".switchAll").addClass("on");
          }else{
            $(".switchAll").removeClass("on");
          }
            return inner;
        }

    };

    //开关事件
    selectRole.switch=function (obj) {
      var num=$("#select_role .on").length;;
      if( num < 5 ){
        $(obj).toggleClass('on');
      }else{
        if( $(obj).hasClass('on') ){
          $(obj).removeClass('on');
        }else{
          layer.msg("每个项目最多可选五个角色",{time:1000});return;
        }
      }
      //$(obj).toggleClass('on');
       /* var num=$("#select_role .on").length;
        if(num>4){
          layer.msg("每个项目最多可选五个角色",{time:1000});return;
        }else{
          $(obj).toggleClass('on');
        }*/

        //一列内没有选中，表头选中要去掉
        var flag=true;
        $.each($("#select_role .switch_btn"),function (i,e) {

            if(!$(this).hasClass("on")){
                flag=false;
            }

        })

      if(flag){
            $(".switchAll").addClass("on");
        }else{
          $(".switchAll").removeClass("on");
        }
    }

    selectRole.hoddleSwitch=function () {
        //全开全关
        $('.switchAll').on('click',function(){
          var num1=$("#select_role .switch_btn").length;
          if(num1>5){
            layer.msg("每个项目最多可选五个角色,无法全选",{time:1500});return;
          }
          if("3" == selectRole.projectStatus){
            $(this).addClass('on');
          }else{
            $(this).toggleClass('on');
          }
            if($(this).hasClass("on")){
                $(".switch_btn").addClass("on");
            }else{
                $(".switch_btn").removeClass("on");
            }
        })

        //回到首页
        $("#index").click(function () {
            window.location.href = appPath + "/admin";
        })

        //上一步操作
        $("#pev").click(function () {
            window.location.href = appPath + "/admin/project_create/private/step3?projectTypeNo="+selectRole.projectTypeNo+"&projectId="+selectRole.projectId+"&projectStatus="+selectRole.projectStatus+"&examPermission="+selectRole.examPermission;
        })

        //下一步操作
        $("#next").click(function () {
            selectRole.next();
        })

    }
    //下一步取值
    selectRole.next=function(){
        var roles=[];
        $.each($("#select_role .on"),function (index,element) {

            var role=new Object();
            role.roleId=$(this).attr('data-roleId');
            role.roleName=$(this).attr('data-roleName');
            roles.push(role)
            console.log(role);


        })

        if(roles.length==0){
            /*if(selectRole.permissionExam){
                //含有考试直接进入组卷策略页面
                window.location.href=appPath + "/popup/project_create/step6?projectTypeNo="+selectRole.projectTypeNo+"&projectId="+selectRole.projectId+"&source=selectRole";
            }else{
                /!*layer.confirm('是否发布', {btn: ['发布','取消']}, function(index){
                    //发布项目
                    selectRole.releaseProject();
                },function(){
                    window.location.href = appPath + "/admin";
                });*!/
              window.location.href = appPath + "/admin";
            }*/
          layer.msg("请选择要分配的角色",{time:1000});
          return;
        }else if (roles.length>5){
          return;
        }

        var params={
            "roles":JSON.stringify(roles),
            "projectId":selectRole.projectId,
            "projectType":selectRole.projectTypeNo
        }

        //保存数据
        selectRole.save(params);
    }
    //发送异步保存项目信息
    selectRole.save=function (params) {
        $.ajax({
            dataType: 'json',
            url: appPath + '/admin/project_create/private/saveProjectRole',
            async: false,
            type: 'post',
            data:params,
            success:function (data) {
                var code=data.code;
                if(10000==code){

                    window.location.href = appPath + "/admin/project/advanceSetting/step4?projectTypeNo="+selectRole.projectTypeNo+"&projectId="+selectRole.projectId+"&projectStatus="+selectRole.projectStatus+"&examPermission="+selectRole.examPermission;
                }
            }
        })
    }


