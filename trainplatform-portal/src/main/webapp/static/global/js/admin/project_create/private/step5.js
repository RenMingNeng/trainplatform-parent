
var step5=new Object();
step5.roles="";
step5.roles1 = []
step5.users = []
step5.projectTypeNo = $("#projectTypeNo").val();
step5.projectId = $("#projectId").val();
step5.projectStatus = $("#projectStatus").val();
step5.examPermission = $("#examPermission").val();

    step5.init=function(basePath,page,options){
       step5.basePath=basePath;
        step5.page=page;
        //含有培训的
        step5.permissionTrain=options.permissionTrain;
        //含有考试的
        step5.permissionExam=options.permissionExam;
        //含有练习的
        step5.permissionExercise=options.permissionExercise;
        if(!step5.permissionExam){
          $("#next").html("<span class='fa fa-paper-plane'></span> 保存");
        }
        //选择受训角色
        step5.initRole();
        step5.initProjectUser();
        //对于课程和人员的操作
        step5.handleSwitch();
    }

   //初始化受训角色
    step5.initRole=function() {
        $.ajax({
            url: appPath + '/admin/project/advanceSetting/getProjectRole',
            dataType: 'json',
            type:'get',
            data:{"projectId":step5.projectId},
            success:function (data) {
                var list=data;
                step5.roles= list;
                step5.project_user()
            }

        })

     }

        //初始化受训角色
        step5.initProjectUser=function() {
            $.ajax({
                url: appPath + '/admin/project/advanceSetting/getProjectUser',
                dataType: 'json',
                type:'get',
                data:{"projectId":step5.projectId},
                success:function (data) {
                    var list=data;
                    step5.users = list;
                    console.log("元数据=="+step5.users)
                }

            })

        }

    //刷新项目人员
    step5.project_user=function () {
        var page_size=10
        var user_list_url = appPath + "/admin/project/advanceSetting/user_role";
        step5.page.init("select_user_form", user_list_url, "select_user_role", "select_user_page", 1, page_size);
        step5.page.goPage(1);
        step5.page.list = function(dataList){
            $(".switchAll").addClass("on");
            var len = dataList.length;
            var inner = "", item,role;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                var flag = step5.select_on(step5.roles,item);
                inner += '<tr >';
                inner += '<td><span class="text-orange tooltip" data-length="6">'+item['user_name']+'</span>';
                inner +='<input type="hidden" id="userId" value="'+ item['user_id'] +'"/>';
                inner += '</td>'
                for(var j=0; j < step5.roles.length; j++){
                    role=step5.roles[j];
                    var index = step5.select(role.roleId,step5.roles,item);
                    //if(item['role_id']== role.roleId){
                    if(index != "-1"){
                        if(!flag){
                          inner += '<td><div class="on switch_btn selected switch_index switch_'+ j +'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-cellIndex="'+ j +'" data-rowIndex="'+ i +'"><span></span></div></td>';
                        }else{
                          inner += '<td><div class="on switch_btn switch_index switch_'+ j +'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-cellIndex="'+ j +'" data-rowIndex="'+ i +'" onclick="step5.switch(this);"><span></span></div></td>';
                        }
                    }else {
                      if(!flag){
                        inner += '<td><div class="switch_btn switch_index switch_'+ j +'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-cellIndex="'+ j +'" data-rowIndex="'+ i +'"><span></span></div></td>';
                      }else{
                        inner += '<td><div class="switch_btn switch_index switch_'+ j +'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-cellIndex="'+ j +'" data-rowIndex="'+ i +'" onclick="step5.switch(this);"><span></span></div></td>';
                      }
                     if($("#switch-"+role.roleId).hasClass("on")){
                          $("#switch-"+role.roleId).removeClass("on");
                      }
                    }
                }

                inner += '</tr>';
            }
            if('3' == step5.projectStatus){

            }
            return inner;
        }
    }
        //比对roleId返回角标，用于判断是否打开开关
        step5.select =function (roleId,roles,item) {
            var index = -1;
            for(var i=0;i<=roles.length;i++){
                if(roleId == item['role_id'] ){
                    index=i;
                    break;
                }
            }
            return index;
        }

        //比对roleId返回角标，用于判断是否禁闭开关
        step5.select_on =function (roles,item) {
          var flag = true;
          if('3' == step5.projectStatus){
            for(var i=0;i<roles.length;i++){
              var role = roles[i];
              if(role.roleId == item['role_id'] ){
                flag = false;
                break;
              }
            }
          }
          return flag;
        }

    //开关事件
    step5.switch=function (obj) {
        $(obj).toggleClass('on');
        var rowIndex=$(obj).attr('data-rowIndex');
        var cellIndex=$(obj).attr('data-cellIndex');
        //添加用户数据到step5.users数组中
        if($(obj).hasClass("on")){
            step5.saveUserRole(obj);
        }else{
            step5.removeUserRole(obj)
        }
        //做成单选
        //一行内只能有一个选中
        var flag=false;
        $.each($(".switch[data-cellIndex!="+cellIndex+"][data-rowIndex="+rowIndex+"]"),function (i,e) {
            if($(this).hasClass("on")){
                flag=true
            }
        })
        if(flag){
            $(".switch[data-cellIndex!="+cellIndex+"][data-rowIndex="+rowIndex+"]").removeClass("on");
        }

        //一列内没有选中，表头选中要去掉
        var flag1=true;
        $.each($("#select_user_role .switch[data-cellIndex="+cellIndex+"]"),function (i,e) {
            if(!$(this).hasClass("on")){
                flag1=false;
            }
        })
        if(!flag1){
            //$(".switchAll[data-index="+cellIndex+"]").removeClass("on");
          $(".switchAll").removeClass("on");
        }else{
            $(".switchAll[data-index="+cellIndex+"]").addClass("on");
        }

    }

        //开关相关事件
        step5.handleSwitch=function () {
            //全开全关
            $('.switchAll').on('click',function(){
              var index=$(this).attr('data-index');
              var flag = $(".switch_index").hasClass("selected");
              var flag_ = $(this).hasClass("on");
              var len = step5.roles.length;
              if(flag){
                  if(len>1 && !flag_){
                    $(this).removeClass("on");
                  }else{
                    $(this).addClass("on");
                  }
              }else{
                $(this).toggleClass("on");
              }
              if($(this).hasClass("on")){
                $(".switch_"+index).addClass("on");
                $(".switch[data-cellIndex!="+index+"]").removeClass("on");
              }else{
                  if(!flag){
                    $(".switch_"+index).removeClass("on");
                  }
              }

              $.each($("#select_user_role .switch_"+ index ),function (index,element) {
                if($(this).hasClass("on")){
                  step5.saveUserRole(this);
                }else{
                  step5.removeUserRole(this);
                }
              })
            })

            $.each($("#select_user_role .switch"),function (index,element) {
                var cellIndex=$(obj).attr('data-cellIndex');

            })

            //回到首页
            $("#index").click(function () {
                window.location.href = appPath + "/admin";
            })


            //上一步操作
            $("#pev").click(function () {
                window.location.href=appPath + "/admin/project/advanceSetting/step4?projectTypeNo="+step5.projectTypeNo+"&projectId="+step5.projectId+"&projectStatus="+step5.projectStatus+"&examPermission="+step5.examPermission;
            })
            //下一步操作
            $("#next").click(function () {
                step5.next();
            })

        }


        //下一步取值
        step5.next=function(){
           /* var roles=[];
            var users=[]
            $.each($("#select_user_role .on"),function (index,element) {

                var role=new Object();
                role.roleId=$(this).attr('data-roleId');
                role.roleName=$(this).attr('data-roleName');
                console.log(role);
                //判重
                if(step5.repeat(role.roleId,roles)){
                    roles.push(role);
                }


                var user=new Object();
                var $tr = $(this).parent().parent()
                var $td = $(this).parent().parent().find("td:eq(0)");
                user.userId= $td.children("input").val();
                user.roleId = role.roleId;
                //添加到数组
                users.push(user);

            })*/
           //获取角色
            step5.getRoles();
            console.log("角色======"+step5.roles1);
            console.log("用户======"+step5.users)
            if(step5.roles1.length==0){
                layer.msg("请给人员分配角色");return;
            }
            var params={
                "roles":JSON.stringify(step5.roles1),
                "users":JSON.stringify(step5.users),
                "projectId":step5.projectId,
                "projectType":step5.projectTypeNo
            }
            //保存数据
            step5.save(params);
        }
        //发送异步保存信息
        step5.save=function (params) {
            $.ajax({
                dataType: 'json',
                url: appPath + '/admin/project/advanceSetting/user_role_update',
                async: false,
                type: 'post',
                data:params,
                success:function (data) {
                    var code=data.code;
                    if(10000==code){
                        if(step5.permissionExam){
                            window.location.href = appPath + "/popup/project_create/step6?projectTypeNo="+step5.projectTypeNo+"&projectId="+step5.projectId+"&source=step5"+"&projectStatus="+step5.projectStatus+"&examPermission="+step5.examPermission;;
                        }else{
                          if(step5.projectStatus == '1'){
                            layer.confirm('是否发布', {btn: ['是','否']}, function(index){
                              //发布项目
                              step3.releaseProject();
                            },function(){
                              window.location.href = appPath + "/admin";
                            });
                          }else{
                            window.location.href = appPath + "/admin";
                          }
                        }
                    }
                }
            })
        }

        //缓存用户角色信息
        step5.saveUserRole = function (obj) {
            var role=new Object();
            role.roleId=$(obj).attr('data-roleId');
            role.roleName=$(obj).attr('data-roleName');
            console.log(role);

            var user=new Object();
            var $td = $(obj).parent().parent().find("td:eq(0)");
            user.userId= $td.children("input").val();
            //判重并添加数据
            step5.repeatUserRole(user.userId,role.roleId,role.roleName,step5.users)


        }

        //去除用户角色信息
        step5.removeUserRole = function (obj) {

            var role=new Object();
            role.roleId=$(obj).attr('data-roleId');
            role.roleName=$(obj).attr('data-roleName');
            console.log(role);

            var user=new Object();
            var $td = $(obj).parent().parent().find("td:eq(0)");
            user.userId= $td.children("input").val();
            user.roleId = role.roleId;

            for (var j=0;j<step5.users.length;j++){
                if(step5.users[j].userId == user.userId && step5.users[j].roleId == role.roleId){
                    step5.users.splice(j,1);
                }
            }

        }


        //去除重复的受训角色
        step5.repeatRole=function(id,roles){
            var flag=true;
            for (var i=0;i<roles.length;i++){
                if(roles[i].roleId==id){
                    flag=false;
                }
            }
            return flag;
        }
        //去除重复的用户受训角色
        step5.repeatUserRole=function(userId,roleId,roleName,users){
            var flag=true;
            for (var i=0;i<users.length;i++){
                if(users[i].userId == userId ){
                    users[i].roleId = roleId;
                    users[i].roleName = roleName;
                    flag=false;
                }
            }
            if(flag){
                var user=new Object();
                user.userId= userId
                user.roleId = roleId;
                user.roleName = roleName;
                step5.users.push(user);
            }
        }

        //获取角色数组
        step5.getRoles=function () {
            step5.roles1 = [];
            for(var i=0;i < step5.users.length ;i++){
                if(step5.repeatRole(step5.users[i].roleId,step5.roles1)){
                    var role = new Object();
                    role.roleId = step5.users[i].roleId
                    role.roleName = step5.users[i].roleName;
                    step5.roles1.push(role);
                }
            }
        }

        //发布项目
        step5.releaseProject=function(){

                var projectStatus = "2";
                $.ajax({
                    dataType: 'json',
                    url: appPath + '/admin/project_create/private/publish_project',
                    async: false,
                    type: 'post',
                    data: {
                        'projectId': $("#projectId").val(),
                        'projectStatus': projectStatus
                    },
                    success: function(data){
                        var code = data['code'];
                        if(code==10000){
                                // 返回首页
                                layer.msg("发布成功");
                                window.location.href=appPath+ "/admin";
                        }else{
                            layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                        }
                    }
                });
        };