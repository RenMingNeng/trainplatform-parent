
var step3=new Object();
step3.projectTypeNo = $("#projectTypeNo").val();
step3.projectId = $("#projectId").val();
step3.projectStatus = $("#projectStatus").val();
step3.examPermission=$("#examPermission").val();
    step3.init=function(basePath,page,options){
        step3.basePath=basePath;
        step3.page=page;
        //含有培训的
        step3.permissionTrain=options.permissionTrain;
        //含有考试的
        step3.permissionExam=options.permissionExam;
        //含有练习的
        step3.permissionExercise=options.permissionExercise;
        step3.permissionRole = options.permissionRole;
        //有下一步就下一步操作，没有就保存项目
        step3.next_or_save();
        //初始化人员
        step3.project_user();
        //含有非默认角色的项目角色就勾选复选框
        if(step3.permissionRole){
          $("#select").attr("checked","checked");
        }
       //进行中的项目复选框禁用
        if('3' == step3.projectStatus){
          //$("#btn_delete_batch_user").removeClass("unclick").addClass("unclick");
          $("#select").attr("disabled","disabled");
        }
        //对于课程和人员的操作
        step3.handleCourseAndUser();
        step3.toggleShow($('#select')[0]);
    }


//受训课程和人员处理
    step3.handleCourseAndUser=function(){

      //弹窗-选择人员
      $("#btn_dialog_user").click(function(){

            // 调用选择培训人员弹窗
              layer.open({
                  type : 2,
                  title : '选择培训人员',
                  area : [ '1200px', '80%' ],		//弹出层大小
                  scrollbar : false,				//false隐藏滑动块
                  content : [ appPath + '/popup/enterSelectUser', 'yes' ]
              });

      });


        // 批量删除人员
        $("#btn_delete_batch_user").click(function(){
          if(!$(this).hasClass("unclick")){
            step3.project_delete_batch_user();
          }
        });

        // 根据用户名称查询
        $("#btn_search_user").click(function(){
              $("#userName").val($.trim($("#userName").val()));
                step3.project_user();
        });

        //查询所有
        $("#btn_search_all_user").click(function(){
            if(!$(this).hasClass("unclick")){
                $("#userName").val('');
                step3.project_user();
            }

        });
        //保存按钮是否展示
        $(document).delegate('#select','change',function () {
            var _this = $(this)[0];
            step3.toggleShow(_this);
        });


        //回到首页
        $("#index").click(function () {
            window.location.href = appPath + "/admin";
        });

        //全选人员
        $("#checkAll").click(function () {
            var flag=$("#checkAll").prop("checked");
            $('#select_user_list input[type="checkbox"]').prop("checked",flag);
        })

        //上一步操作
        $("#pev").click(function () {
            window.location.href=appPath + "/admin/project_create/private/step2?projectTypeNo="+step3.projectTypeNo+"&projectId="+step3.projectId+"&projectStatus="+step3.projectStatus+"&examPermission="+step3.examPermission;
        })


        //下一步操作
        $("#next").click(function () {
            /*$.ajax({
           url: appPath + '/admin/project_create/private/trainRole',
           dataType: 'json',
           type:'get',
           success:function (data) {
           var list=data['result']['dataList'];
           console.log(step3.permissionExam)
           if(null==list){
           if(step3.permissionExam){
           //含有考试直接进入组卷策略页面
           window.location.href=appPath + "/popup/project_create/step6?projectTypeNo="+step3.projectTypeNo+"&projectId="+step3.projectId+"&source=step3";
           }else{
           /!* layer.confirm('是否发布', {btn: ['发布','取消']}, function(index){
           //发布项目
           step3.releaseProject();
           },function(){
           window.location.href = appPath + "/admin";
           });*!/
           window.location.href = appPath + "/admin";
           }
           }else{
           //进入选择角色页面
           window.location.href=appPath + "/admin/project_create/private/selectRole?projectTypeNo="+step3.projectTypeNo+"&projectId="+step3.projectId;
           //window.location.href=appPath + "/admin/project/advanceSetting/step4?projectTypeNo="+step3.projectTypeNo+"&projectId="+step3.projectId;
           }

           }

           })*/
          var operateType = $(this).attr("operate-type");
          var operUrl = appPath + "/popup/project_create/step6?projectTypeNo="+step3.projectTypeNo+"&projectId="+step3.projectId+"&source=step3"+"&projectStatus="+step3.projectStatus+"&examPermission="+step3.examPermission;
          if(operateType == '1') {
            if ($("#select").prop("checked")) {
              //进入选择角色页面
              window.location.href = appPath
                  + "/admin/project_create/private/selectRole?projectTypeNo="
                  + step3.projectTypeNo + "&projectId=" + step3.projectId+"&projectStatus="+step3.projectStatus+"&examPermission="+step3.examPermission;
              return;
            }
          }
          if(step3.permissionExam){
            //含有考试直接进入组卷策略页面
            window.location.href= operUrl;
          }else{
            window.location.href = appPath + "/admin";
          }

        })

      $("#save").click(function () {
          if("1" == step3.projectStatus){
          layer.confirm('是否发布', {btn: ['是','否']}, function(index){
              //发布项目
              step3.releaseProject();
          },function(){
              window.location.href = appPath + "/admin";
          });
          }else {
              window.location.href = appPath + "/admin";
          }
      })

    }

    step3.next_or_save = function(){
      $.ajax({
        url: appPath + '/admin/project_create/private/trainRole',
        dataType: 'json',
        type:'get',
        success:function (data) {
          var list=data['result']['dataList'];
          if(null==list){
            if(!step3.permissionExam){
              $("#next").addClass("index");
              $("#save").removeClass("index");
            }else{
              $("#next").attr({"operate-type":"2"});
            }
          }else{
            $("#allot").removeClass("index");
          }
        }
      });
    }



     // 批量删除人员
     step3.project_delete_batch_user = function(){
         var userIds=[]
         $('#select_user_list input:checked').each(function(i,e){
             userIds.push($(this).attr("data-id"));
         })

         if(userIds.length==0){
            layer.msg("未选择人员");return;
        }
        layer.confirm("与人员相关的项目记录会全部删除（<span style='color: red;'>无法恢复</span>）确定此操作吗?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {
            $.ajax({
                url: appPath + '/admin/project_create/private/delete_project_user',
                async: false,
                type: 'post',
                data: {
                    'projectId': $("#projectId").val(), 	            // 项目总览id
                    'projectStatus': step3.projectStatus, 	          // 项目状态
                    'ids': userIds.join(","), 		                    // 人员ids
                    'projectType':step3.projectTypeNo    //项目类型
                },
                success: function(data){
                    var code = data['code'];
                    if(code){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            $("#checkAll").prop("checked",false);
                            // 刷新项目人员
                            step3.project_user();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

      //判断字符串是否包含某字符
      step3.isContains=function(str,substr){
          return new RegExp(substr).test(str);
      }
      //封装角色和受训单位数据
     step3.assembleDate=function(datas){
          var sbingDatas=[];
              for(var i = 0;i<datas.length;i++){
                var object = new Object();
                object.id = datas[i].id;
                object.text = datas[i].text;
                sbingDatas.push(object)
              }
          return JSON.stringify(sbingDatas);
     }

    // 选择人员的回调
    step3.train_user_callback=function(rows){
       var userIds = [];
        for(var i=0; i<rows.length; i++){
            if(rows[i].id){
                var obj=new Object();
                obj.id=rows[i].id;
                obj.userName=rows[i].userName;
                obj.deptName=rows[i].departmentName==null?"":rows[i].departmentName;
                userIds.push(obj);
            }
        }
        userIds =JSON.stringify(userIds);
        var roleId = "-1";
        var roleName="默认角色"

       /* layer.confirm("确定执行此操作?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {*/
            step3.changeProjectUserOther(roleId,roleName,userIds);
        //})

    }

        step3.changeProjectUserOther=function(roleId,rolename, users){
            // 更改项目的其他人员
            $.ajax({
                url: appPath + '/admin/project_create/private/save_project_user',
                async: false,
                type: 'post',
                data: {
                    'id': step3.projectId,     // 项目id
                    'roleId': roleId,                // 受训角色id
                    'roleName': rolename,            // 受训人员ids
                    'users': users,
                    'projectType': step3.projectTypeNo
                },
                success: function(data){
                    var code = data['code'];
                    if(code==10000){
                       /* layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);*/
                            // 刷新项目人员
                            step3.project_user();
                       // });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        }
    //刷新项目人员
    step3.project_user=function () {
        var page_size=10
        var user_list_url = appPath + "/admin/project_create/private/project_user_list";
        step3.page.init("select_user_form", user_list_url, "select_user_list", "select_user_page", 1, page_size);
        step3.page.goPage(1);
        step3.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td><input type=\"checkbox\" name="courseBox" data-id="' + item['user_id'] + '"></td>';
                inner +='<td>'+item['user_name']+'</td>';
                inner +='<td><span class="text-orange">'+item['department_name']+'</span></td>';
                inner += '</tr>';
            }
            return inner;
        }
    }



    //发布项目
    step3.releaseProject=function(){

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
                    /*layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);*/
                        // 返回首页
                        layer.msg("发布成功");
                        window.location.href=appPath+ "/admin";
                   // });
                }else{
                    layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    };

    step3.toggleShow = function(obj){
            var checked = $(obj).prop("checked");
            if(!checked &&  !step3.permissionExam ){
                $("#next").hide();
                $('#save').show();
            }else{
                $("#next").show();
                $('#save').hide();
            }
        }