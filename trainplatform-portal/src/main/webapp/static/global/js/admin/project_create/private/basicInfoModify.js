var basicInfoModify=new Object();
basicInfoModify.data = "";
basicInfoModify.roles="";
basicInfoModify.deptIds=[];
basicInfoModify.projectTypeNo = $("#projectTypeNo").val();
basicInfoModify.projectId=$("#projectId").val();
basicInfoModify.projectStatus=$("#projectStatus").val();
basicInfoModify.examPermission=$("#examPermission").val();


    basicInfoModify.init=function(basePath,projectStatus,options){
       basicInfoModify.basePath=basePath;

        basicInfoModify.projectStatus=projectStatus;
        //含有培训的
        basicInfoModify.permissionTrain=options.permissionTrain;
        //含有考试的
        basicInfoModify.permissionExam=options.permissionExam;
        //含有练习的
        basicInfoModify.permissionExercise=options.permissionExercise;
        //选择单位
        basicInfoModify.initDepartment();

        //basicInfoModify.initTree();
       //根据项目状态来判断操作权限（）
        basicInfoModify.permission(projectStatus);

        /*//保存基本信息
        basicInfoModify.updateProject();*/
        //按钮事件
        basicInfoModify.handle();

        window.groupTreeCheckbox_search = basicInfoModify.groupTreeChecked;

}
    basicInfoModify.dis_node = function (treeObj,node) {
        treeObj.checkNode(node, false, false, true);
        var children = node.children;
        if(children){
            for(var j=0; j< children.length; j++) {
                basicInfoModify.dis_node(treeObj,children[j]);
            }
        }
    };

    basicInfoModify.groupTreeChecked  = function(treeObj,treeNode){
        if(!$("#isChecked").is(":checked")){
            var nodes = treeNode.children;
            if(nodes){
                for(var i=0; i< nodes.length; i++) {
                    if(nodes[i] && nodes[i].type == 2){
                        basicInfoModify.dis_node(treeObj,nodes[i]);
                    }
                }
            }
        }
    }
   /*//初始化受训角色
    basicInfoModify.initRole=function() {
        $("#role_select2").select2();
        $.ajax({
            url: basicInfoModify.basePath + 'admin/project_create/private/trainRole',
            dataType: 'json',
            type:'get',
            success:function (data) {
                var list=data.result;
                $("#role_select2").select2({
                    tags:true,
                    multiple:true,
                    data:list,
                    allowClear:true,
                    language:"zh-CN"
                })
                //发送异步请求项目下已有的角色
                $.ajax({
                    url: basicInfoModify.basePath + 'admin/proManager/projectRole',
                    dataType: 'json',
                    type:'get',
                    data:{"projectId":basicInfoModify.projectId},
                    success:function (data) {
                        var list=data.result;
                        var len=list.length;
                        var inner = "", item;
                        var ids=[];
                        // 组装数据
                        for(var i=0; i< len; i++) {
                            item = list[i];
                            ids.push(item.roleId)
                        }
                        $("#role_select2").val(ids).trigger("change");
                        //刷新受训角色树

                    }

                })

            }

        })

     }
*/

        // 是否有下一步操作的权限
         basicInfoModify.permission = function(projectStatus){
            //进行中的项目只能增不能减
            if('3'==projectStatus){
                $("#role_select2").prop('disabled',true);
                $("#department_select2").prop('disabled',true);
                // 启用-项目基本信息
                $(".project-details input[data-class='ing']").attr("disabled",true);
                return;
            }
        };

     //初始化受训单位列表
      basicInfoModify.initDepartment=function() {
          //console.log( $("#department_select2") );
          $("#department_select2").select2();
          $.ajax({
              url: basicInfoModify.basePath + 'admin/project_create/private/trainDepartment',
              dataType: 'json',
              type:'get',
              success:function (data) {
                  var list=data.result;
                  $("#department_select2").select2({
                      tags:true,
                      multiple:true,
                      data:list,
                      allowClear:true,
                      placeholder:"请选择受训单位",
                      language:"zh-CN"
                  })
                  //发送异步请求项目下已有的单位
                  $.ajax({
                      url: basicInfoModify.basePath + 'admin/project_create/private/projectDepartment',
                      dataType: 'json',
                      type:'get',
                      data:{"projectId":basicInfoModify.projectId},
                      success:function (data) {
                          basicInfoModify.data = data;
                          var list=data.result;
                          var len=list.length;
                          var inner = "", item;
                          // 组装数据
                          for(var i=0; i< len; i++) {
                              item = list[i];
                              basicInfoModify.deptIds.push(item.deptId)
                          }
                          $("#department_select2").val(basicInfoModify.deptIds).trigger("change");
                          basicInfoModify.initData(data);
                          //点击小X触发，取消树中节点的勾选状态
                          basicInfoModify.deleteDept();

                      }

                  })

              }

          })

    }

basicInfoModify.initData = function (data) {
    var result = data.result;
    var treeObj = $.fn.zTree.getZTreeObj("group_tree");
    //初始化默认所选单位选中状态
    for(var i = 0; i< result.length ;i++){
        var companyId = result[i].companyId
        var deptId = result[i].deptId;
        var node = treeObj.getNodeByParam("id", deptId, null);
        if(node!=null){
            node.checked = true ;
            treeObj.updateNode(node);
        }

    }
}

basicInfoModify.check =function (responseData) {

    var result = basicInfoModify.data.result;
    for(var i =0; i < responseData.length; i++) {
         for(var j = 0;j<result.length;j++){
             if(responseData[i].departmentId == result[j].deptId){
                 responseData[i].checked = true;
                 //responseData[i].getParentNode.open =  true;
                 break;
             }
         }

    }
    return responseData;
}
        //基础信息中选择部门弹窗
        basicInfoModify.selectDept=function(){


            //得到最新的受训单位数据
            var departments = $("#department_select2").select2("data");
            basicInfoModify.getNewDate(departments);

            $('.por').addClass('show');
        };

        //隐藏树下拉框
        basicInfoModify.close = function(){
            $('.por').removeClass('show');
        }
        // 确定受训角色
        basicInfoModify.finish = function(){
            var arr_ = [];
            var treeObj = $.fn.zTree.getZTreeObj("group_tree");
            var checkedNodes = treeObj.getCheckedNodes(true);
            /*if(checkedNodes.length==0){
                layer.msg("请选择受训单位",{time : 1000});
                return;
            }*/
            for(var i=0; i<checkedNodes.length; i++){
                if(checkedNodes[i].departmentId == undefined || '' == checkedNodes[i].departmentId){
                    arr_.push(checkedNodes[i].companyId);
                }else{
                    arr_.push(checkedNodes[i].departmentId);
                }
            }
            $("#department_select2").val(arr_).trigger("change");
            basicInfoModify.close();
        }

        //点击小X触发，取消树中节点的勾选状态
        basicInfoModify.deleteDept = function () {
            $(document).delegate('.select2-selection__choice__remove','click',function(){
                var id = $(this).parent().attr('id');
                var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                var node = treeObj.getNodeByParam("id", id, null);
                node.checked = false ;
                treeObj.updateNode(node);
            })
        }


     //验证考试次数为整数
     basicInfoModify.verifyIntRetestTime=function(value) {
         var value_ = $.trim(value);
         value= value_.replace(/\D/g, '');
         //value= Math.round(value_);
         $("#intRetestTime").val( value);
     }

    // 表单校验：不含考试的项目，【项目名称】【项目时间】为必填项，其他都是非必填项。
    //          含考试的项目，【项目名称】【项目时间】【考试时间】【考试次数为必填项】
    // 1：培训/2：练习/3：考试/4：培训，练习/5：培训，考试/6：练习，考试/7：培训，练习，考试
    basicInfoModify.verify = function(){
        // 错误信息
        var msg = "";
        // 项目名称
        var projectName = $.trim($("#projectName").val());
        if(!projectName){
            msg = "请填写项目名称";
            layer.msg(msg);
            return false;
        }
        // 培训项目项目时间校验
      if (basicInfoModify.permissionTrain) {
          var trainStartTime = $.trim($("#trainStartTime").val());
          if (!trainStartTime) {
              msg = "请选择培训项目开始时间";
              layer.msg(msg);
              return false;
          }
          // 项目结束时间
          var trainEndTime = $.trim($("#trainEndTime").val());
          if (!trainEndTime) {
              msg = "请选择培训项目结束时间";
              layer.msg(msg);
              return false;
          }
      }

        // 练习项目时间校验
      if (basicInfoModify.permissionExercise) {
            var exerciseStartTime = $.trim($("#exerciseStartTime").val());
            if (!exerciseStartTime) {
                msg = "请选择练习项目开始时间";
                layer.msg(msg);
                return false;
            }
            // 项目结束时间
            var exerciseEndTime = $.trim($("#exerciseEndTime").val());
            if (!exerciseEndTime) {
                msg = "请选择练习项目结束时间";
                layer.msg(msg);
                return false;
            }
        }
        //考试项目时间校验
        if(basicInfoModify.permissionExam){
            // 考试开始时间
            var examStartTime = $.trim($("#examStartTime").val());
            if(!examStartTime){
                msg = "请选择考试开始时间";
                layer.msg(msg);
                return false;
            }
            // 考试结束时间
            var examEndTime = $.trim($("#examEndTime").val());
            if(!examEndTime){
                msg = "请选择考试结束时间";
                layer.msg(msg);
                return false;
            }
            // 考试次数
            var intRetestTime = $.trim($("#intRetestTime").val());
            if(!intRetestTime){
                msg = "请填写考试次数";
                layer.msg(msg);
                return false;
            }
        }
        return true;
    };

        //按钮事件
        basicInfoModify.handle = function () {
            //下一步按钮事件
            $("#next").click(function(){
                var value = 'next';
                basicInfoModify.updateProject(value);
            });

            //保存按钮事件
            $("#save").click(function () {
                var value = 'save';
                basicInfoModify.updateProject(value);
            })
        }



    // 保存修改的项目基本信息
     basicInfoModify.updateProject=function(value){

             var projectName = $("#projectName").val();                        //项目名称

             var project_train_info = "";                                 //培训项目详情json(培训时间)
             var project_exercise_info = "";                              //练习项目详情json(练习时间)
             var project_exam_info = "";                                  //考试项目详情json(考试时间、考试次数)
             var project_train_Time = "";                                 //培训项目详情json(培训时间)
             var project_exercise_Time = "";                              //练习项目详情json(练习时间)
             var project_exam_Time = "";                                  //考试项目详情json(考试时间、考试次数)
             var trainBeginTime = "";
             var trainEndTime = "";
             var exerciseBeginTime = "";
             var exerciseEndTime = "";
             var examBeginTime = "";
             var examEndTime = "";
             var intRetestTime = "";
             //包含培训项目
             if (basicInfoModify.permissionTrain) {
                 project_train_info = {
                     "beginTime": $("#trainStartTime").val()+" 00:00:00",
                     "endTime": $("#trainEndTime").val()+" 23:59:59"
                 },
                 project_train_info = JSON.stringify(project_train_info);
                 trainBeginTime=$("#trainStartTime").val()+" 00:00:00";
                 trainEndTime=$("#trainEndTime").val()+" 23:59:59";
                 project_train_Time=trainBeginTime+" 至 "+trainEndTime;
             }
             //包含练习项目
             if (basicInfoModify.permissionExercise) {
                 project_exercise_info = {
                     "beginTime": $("#exerciseStartTime").val()+" 00:00:00",
                     "endTime": $("#exerciseEndTime").val()+" 23:59:59"
                 };
                 project_exercise_info = JSON.stringify(project_exercise_info);
                 exerciseBeginTime=$("#exerciseStartTime").val()+" 00:00:00";
                 exerciseEndTime=$("#exerciseEndTime").val()+" 23:59:59";
                 project_exercise_Time= exerciseBeginTime+" 至 "+exerciseEndTime;
             }
             //包含考试项目
             if (basicInfoModify.permissionExam) {
                 project_exam_info = {
                     "beginTime": $("#examStartTime").val()+":00",
                     "endTime": $("#examEndTime").val()+":00",
                     "count": $("#intRetestTime").val()
                 };
                 project_exam_info = JSON.stringify(project_exam_info);
                 examBeginTime=$("#examStartTime").val()+":00";
                 examEndTime=$("#examEndTime").val()+":00";
                 intRetestTime = $("#intRetestTime").val();
                 project_exam_Time=examBeginTime+" 至 "+examEndTime;
             }
             /*var trainRoles = $("#role_select2").select2("data");              //受训角色数组
             if(!basicInfoModify.verifyTrainRole(trainRoles)){
                 layer.msg('必须选择受训角色');
                 return;
             }
             var roleNames=basicInfoModify.getRoleName(trainRoles);
             trainRoles = basicInfoModify.assembleDate(trainRoles);*/
             var departments = $("#department_select2").select2("data");
             departments = basicInfoModify.assembleDate(departments)                 //受训单位数组

             var params = {
                 'projectId': basicInfoModify.projectId,                                           //主键
                 'projectType':basicInfoModify.projectTypeNo,                                      //项目类型
                 'projectName': projectName, 												 // 项目名称
                 'project_train_info': project_train_info,                                   // 培训项目时间
                 'project_exercise_info': project_exercise_info,		                     // 练习项目时间
                 'project_exam_info':  project_exam_info,                                     // 考试项目信息
                 'projectStatus':basicInfoModify.projectStatus,                                    //项目状态
                 'project_train_Time':project_train_Time,
                 'project_exercise_Time':project_exercise_Time,
                 'project_exam_Time':project_exam_Time,
                 //'roles': trainRoles,                                                        //受训角色
                 'departments': departments ,                                                //受训单位
                 //'roleNames':roleNames,                                                      //角色名称字符串
                 'trainBeginTime': trainBeginTime,                                           // 培训开始时间
                 'trainEndTime': trainEndTime,                                               // 培训结束时间
                 'exerciseBeginTime': exerciseBeginTime,                                     // 练习开始时间
                 'exerciseEndTime': exerciseEndTime,                                         // 练习结束时间
                 'examBeginTime': examBeginTime,                                             // 考试开始时间
                 'examEndTime': examEndTime,                                                 // 考试结束时间
                 'intRetestTime': intRetestTime,                                              // 考试次数
                 'value': value
             };
             basicInfoModify.saveProject(params);

     }
     //发送异步保存项目信息
     basicInfoModify.saveProject=function (params) {
         if(!basicInfoModify.verify())return;
         $.ajax({
         dataType: 'json',
         url: appPath + '/admin/project_create/private/updateProject',
         async: false,
         type: 'post',
         data: params,
         success:function (data) {
         var code=data.code;
         if(10000==code){

         /*layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
         layer.close(index);*/
         if('save' == params.value){
             window.location.href=appPath+"/admin/project/projectList";
         }else if('next' == params.value){
             window.location.href=appPath+"/admin/project_create/private/step2?projectTypeNo="+basicInfoModify.projectTypeNo+"&projectId="+basicInfoModify.projectId+"&projectStatus="+basicInfoModify.projectStatus+"&examPermission="+basicInfoModify.examPermission;
         }
             // });
         }

     }


     })
     }

    /*//验证受训角色
      basicInfoModify.verifyTrainRole=function(trainRoles) {
        if(trainRoles.length>0){
            return true;
        }else{
            return false;
        }
    }*/


      //判断字符串是否包含某字符
      basicInfoModify.isContains=function(str,substr){
          return new RegExp(substr).test(str);
      }
      //封装角色和受训单位数据
     basicInfoModify.assembleDate=function(datas){
           var sbingDatas = [];
          for(var i = 0;i<datas.length;i++){
            var object = new Object();
            object.id = datas[i].id;
            object.text = datas[i].text;
            object.type = datas[i].type;
            sbingDatas.push(object)
          }
          return JSON.stringify(sbingDatas);
     }
    //得到最新的受训单位数据
    basicInfoModify.getNewDate=function(datas){
        var deptDatas = [];
        for(var i = 0;i<datas.length;i++){
            deptDatas.push(datas[i].id);
        }
        basicInfoModify.deptIds = deptDatas;
    }

     //得到角色名称组合字符串
     basicInfoModify.getRoleName=function(datas){
         var roleNames=null;
         for(var i = 0;i<datas.length;i++){
             if(i!=0){
                 roleNames+=","+ datas[i].text;
             }else{
                 roleNames=datas[i].text;
             }
         }
         return roleNames;
     }
    //去除重复的受训角色
    basicInfoModify.repeatDept=function(id,depatements){
        var flag=true;
        for (var i=0;i<depatements.length;i++){
            if(depatements[i]==id){
                flag=false;
            }
        }
        return flag;
    }

