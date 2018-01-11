var proModify=new Object();
proModify.roles="";
proModify.projectTypeNo = $("#projectTypeNo").val();
proModify.projectId=$("#projectId").val();
proModify.source="projectModify";

    proModify.init=function(basePath,page,page1,projectStatus,options){
       proModify.basePath=basePath;
        proModify.page=page;
        proModify.page1=page1;
        proModify.projectStatus=projectStatus;
        //含有培训的
        proModify.permissionTrain=options.permissionTrain;
        //含有考试的
        proModify.permissionExam=options.permissionExam;
        //含有练习的
        proModify.permissionExercise=options.permissionExercise;
        //根据项目状态来判断操作权限（）
        proModify.permission(projectStatus);
        //选择单位
        proModify.initDepartment();
        //选择受训角色
        proModify.initRole();
        //对于课程和人员的操作
        proModify.handleCourseAndUser();
        //加载受训角色树
        // proModify.load_role_tree();
        //加载角色课程列表
        proModify.project_role_tree_node_course();
        //加载项目人员列表
        proModify.project_user();
        //保存基本信息
        proModify.updateProject();
}

   //初始化受训角色
    proModify.initRole=function() {
        $("#role_select2").select2();
        $.ajax({
            url: proModify.basePath + 'admin/project_create/private/trainRole',
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
                    url: proModify.basePath + 'admin/proManager/projectRole',
                    dataType: 'json',
                    type:'get',
                    data:{"projectId":proModify.projectId},
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

     //初始化受训单位列表
      proModify.initDepartment=function() {
          $("#department_select2").select2();
          $.ajax({
              url: proModify.basePath + 'admin/project_create/private/trainDepartment',
              dataType: 'json',
              type:'get',
              success:function (data) {
                  var list=data.result;
                  $("#department_select2").select2({
                      tags:true,
                      multiple:true,
                      data:list,
                      allowClear:true,
                      language:"zh-CN"
                  })
                  //发送异步请求项目下已有的单位
                  $.ajax({
                      url: proModify.basePath + 'admin/proManager/projectDepartment',
                      dataType: 'json',
                      type:'get',
                      data:{"projectId":proModify.projectId},
                      success:function (data) {
                          var list=data.result;
                          var len=list.length;
                          var inner = "", item;
                          var ids=[];
                          // 组装数据
                          for(var i=0; i< len; i++) {
                              item = list[i];
                              ids.push(item.deptId)
                          }
                          $("#department_select2").val(ids).trigger("change");
                      }

                  })

              }

          })

    }



      // 是否有下一步操作的权限
     proModify.permission = function(projectStatus){
        //未发布未开始项目允许全部
         if('1'==projectStatus && '2'==projectStatus ){
            // 禁用-项目基本信息
           // $(".project-details input").attr("disabled",true);
            //$(".project-details select").attr("disabled",true);
           //  $("#role_select2").prop('disabled',true);
           //  $("#department_select2").prop('disabled',true);
            // 启用-选择培训课程
            $("#btn_dialog_course").removeClass("unclick");
            // 启用-选择培训课程-批量删除
            $("#btn_delete_batch_course").removeClass("unclick");
            // 启用-选择培训课程-搜索
            $("#btn_search_course").removeClass("unclick");
            // 启用-选择培训课程-全部
            $("#btn_search_all_course").removeClass("unclick");

            // 启用-选择其他人员
            $("#btn_dialog_user").removeClass("unclick");
            // 启用-选择其他人员-批量删除
            $("#btn_delete_batch_user").removeClass("unclick");
            // 启用-选择其他人员-搜索
            $("#btn_search_user").removeClass("unclick");
            // 启用-选择其他人员-全部
            $("#btn_search_all_user").removeClass("unclick");
            return;
        }
        //进行中的项目只能增不能减
         if('3'==projectStatus){
            $("#role_select2").prop('disabled',true);
            $("#department_select2").prop('disabled',true);
            // 启用-项目基本信息
            $(".project-details input[data-class='ing']").attr("disabled",true);
            // 禁用-选择培训课程
            $("#btn_dialog_course").removeClass("unclick");
            // 启用-选择培训课程-批量删除
            $("#btn_delete_batch_course").removeClass("unclick").addClass("unclick");
            // 启用-选择培训课程-搜索
            $("#btn_search_course").removeClass("unclick");
            // 启用-选择培训课程-全部
            $("#btn_search_all_course").removeClass("unclick");

            // 启用-选择其他人员
            $("#btn_dialog_user").removeClass("unclick");
            // 启用-选择其他人员-批量删除
            $("#btn_delete_batch_user").removeClass("unclick").addClass("unclick");
            // 启用-选择其他人员-搜索
            $("#btn_search_user").removeClass("unclick");
            // 启用-选择其他人员-全部
            $("#btn_search_all_user").removeClass("unclick");
                 return;
         }
    };



     //验证考试次数为整数
     proModify.verifyIntRetestTime=function(value) {
         var value_ = $.trim(value);
         //value= value_.replace(/\D/g, '');
         value= Math.round(value_);
         $("#intRetestTime").val( value);
     }

    // 表单校验：不含考试的项目，【项目名称】【项目时间】为必填项，其他都是非必填项。
    //          含考试的项目，【项目名称】【项目时间】【考试时间】【考试次数为必填项】
    // 1：培训/2：练习/3：考试/4：培训，练习/5：培训，考试/6：练习，考试/7：培训，练习，考试
    proModify.verify = function(){
        // 错误信息
        var msg = "";
        // 项目名称
        var projectName = $.trim($("#projectName").val());
        if(!projectName){
            msg = "请填写项目名称";
            layer.msg(msg);
            return false;
        }
        if(projectName.length > 40){
            msg = "项目名称不得超过40个字符";
            layer.msg(msg);
            return false;
        }

        // 培训项目项目时间校验
      if (proModify.permissionTrain) {
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
      if (proModify.permissionExercise) {
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
        if(proModify.permissionExam){
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




// tree options
       var role_tree_options = {
           id: 'role_tree',
           setting: {
               check: {
                   enable: true,
                   nocheckInherit: false
              },
              view: {
                   dblClickExpand: false
             },
             callback: {
                 onRightClick: function (event, treeId, treeNode) {

                 },
                 onClick: function (e, treeId, treeNode) {

                     if ("role_tree" == treeId) {
                         $("#btn_search_course").attr("roleId", treeNode.roleId);   // 记住当前点击的受训角色,搜索用
                         $("#select_course_form #roleId").val(treeNode.roleId);     //赋值便于搜索项目课程信息
                         proModify.project_role_tree_node_course();
                     }
                     //组卷策略
                     if (treeId == 'stragegy_role_tree') {
                         stragegy_update.stragegy_select(treeNode.roleId, treeNode.name, proModify.permissionTrain);
                     }
                 },
                 onRename: function (event, treeId, treeNode, isCancel) {
                 }
             }
        },
        zNodes : '',
        rMenu : ''
    };

    //加载受训角色树
    proModify.load_role_tree=function(){
        var trainRoles = $("#role_select2").select2('data');        //受训角色数组
        trainRoles = proModify.assembleDate(trainRoles);
        $.ajax({
            url: appPath + '/admin/proManager/projectRole/tree',
            async: false,
            type: 'post',
            data: {'trainRoles':trainRoles},
            success: function(data){
                var datalist=data.result;
                role_tree_options.zNodes = eval(datalist);
                //受训课程的角色树
                $.fn.zTree.init($("#role_tree"), role_tree_options.setting, role_tree_options.zNodes);
                // 设置根节点不可选
                var treeObj = $.fn.zTree.getZTreeObj("role_tree");
                var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
                var rootNode = rootNodes[0];
                rootNode.nocheck = true;
                treeObj.updateNode(rootNode);
                // 默认选中根节点
                treeObj.selectNode(rootNode);
                treeObj.setting.callback.onClick(null, "role_tree", rootNode);

                //组卷策略的角色树
                $.fn.zTree.init($("#stragegy_role_tree"), role_tree_options.setting, role_tree_options.zNodes);
                // 选择第一个节点
                var treeObj = $.fn.zTree.getZTreeObj("stragegy_role_tree");
                var nodes = treeObj.getNodes()[0].children;
                if (nodes.length>0) {
                    treeObj.selectNode(nodes[0],false,true);
                    treeObj.setting.callback.onClick(null, "stragegy_role_tree", nodes[0]);
                }

            }

        });

    }
    // 项目-受训角色-课程信息
    proModify.project_role_tree_node_course = function(){
        var page_size=10
        var course_list_url = appPath + "/admin/project_create/private/project_course_list";
        proModify.page.init("select_course_form", course_list_url, "select_course_list", "select_course_page", 1, page_size);
        proModify.page.goPage(1);
        proModify.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                    item = dataList[i];
                    inner += '<tr >';
                    inner += '<td>' + (i+1) + '</td>';
                    inner += '<td><input type=\"checkbox\" name="courseBox" data-id="' + item['id'] + '" course-id="' + item['course_id'] + '"></td>';
                    inner +='<td>'+item['course_no']+'</td>';
                    inner +='<td><span class="text-orange tooltip" data-length="8">'+item['course_name']+'</span></td>';
                    inner += '<td><span class="text-orange tooltip" data-length="8">'+item['role_name']+'</span></td>';
                if (proModify.permissionTrain) {
                        inner += '<td>'+item['class_hour']+'</td>';
                        inner += '<td id="editRequirement">';
                    if("1"==proModify.projectStatus || "2"==proModify.projectStatus ) {
                        inner += '<a href="javascript:;" data-type="text" data-pk="1" data-id="' + item.id + '" onmouseover="proModify.editRequirement(this,\''+ item.id +'\')" class="editable editable-click">' + item['requirement'] + '</a>'; //学时要求
                    }else{
                        inner += '<a href="javascript:;" data-type="text" data-pk="1"  >' + item['requirement'] + '</a>'; //学时要求
                    }
                        inner += '</td>';
                }
                if (proModify.projectTypeNo != '1') {
                    inner += '<td>'+item['question_count']+'</td>';
                }
                if (proModify.permissionExam) {
                        inner += '<td id="edit">';
                    if("1"==proModify.projectStatus || "2"==proModify.projectStatus ) {
                        inner += '<a href="javascript:;" data-type="text" data-pk="1" data-id="'  + item.id + '" class="editable editable-click" onmouseover="proModify.editSelectCount(this,\''+ item.role_id +'\','+ item.question_count+')" >' + item['select_count'] + '</a>'; //必选题量
                    }else{
                        inner += '<a href="javascript:;" data-type="text" data-pk="1"  >' + item['select_count'] + '</a>'; //必选题量
                    }
                        inner += '</td>';
                }
                    inner +='<td>';
                    if(proModify.permissionTrain){
                      inner +='<a href="javascript:;" onclick=\"proModify.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
                    }
                    if(proModify.projectTypeNo != '1'){
                      inner +='<a href="javascript:;" onclick=\"proModify.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
                    }
                    inner +='</td>';
                    inner += '</tr>';
            }
            return inner;
        }

    };
    proModify.editRequirement = function (obj,id) {
        var projectId = $("#projectId").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable( {
            validate:function (value) {
                var param={
                    "projectId": projectId,
                    "id": id,
                    "requirement": value
                }
                proModify.requirementOrSelectCount_ajax(param,obj);
            }
        })
    }
    proModify.editSelectCount = function (obj,role_id,questionCount) {
        var projectId = $("#projectId").val();
        var id = $(obj).attr("data-id");
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
            editBy:'click',
            validate:function (value) {
                if(value.length>1 && value.substring(0,1)==0){
                    return  '题量输入不合法';
                }
                if(value>questionCount){
                    value=questionCount;
                    return "必选题量不能大于总题量";
                }
                var param={
                    "projectId": projectId,
                    "id": id,
                    "roleId": role_id,
                    "selectCount": value
                }
                var data = proModify.requirementOrSelectCount_ajax(param,obj);
                var code = data.code;
                var msg = data.message;
                if(code == '10000' && msg == 'success'){
                    layer.msg("设置成功");

                    //组卷策略的角色树
                    $.fn.zTree.init($("#stragegy_role_tree"), role_tree_options.setting, role_tree_options.zNodes);
                    // 选择第一个节点
                    var treeObj = $.fn.zTree.getZTreeObj("stragegy_role_tree");
                    var nodes = treeObj.getNodes()[0].children;
                    if (nodes.length>0) {
                        treeObj.selectNode(nodes[0],false,true);
                        treeObj.setting.callback.onClick(null, "stragegy_role_tree", nodes[0]);
                    }

                }else{
                    return msg;
                }
            },
            display:function (value) {
                if(value > questionCount || value < 0 ){
                    $(this).text(questionCount);
                }else {
                    $(this).text(value);
                }
            }
        })
    }

   proModify.requirementOrSelectCount_ajax= function (param,obj) {
       var result;
        $.ajax({
            url: appPath + '/admin/proManager/updateRequirementAndSelectCount',
            dataType: 'json',
            async: false,
            type: 'post',
            data: param,
            success: function (data) {
                result = data;
            }
        });
        return result;
    };

    //刷新项目人员
    proModify.project_user=function () {
        var page_size=10
        var user_list_url = appPath + "/admin/project_create/private/project_user_list";
        proModify.page1.init("select_user_form", user_list_url, "select_user_list", "select_user_page", 1, page_size);
        proModify.page1.goPage(1);
        proModify.page1.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td><input type=\"checkbox\" name="courseBox" data-id="' + item['user_id'] + '"></td>';
                inner +='<td>'+item['user_name']+'</td>';
                inner +='<td><span class="text-orange">'+item['role_name']+'</span></td>';
                inner +='<td><span class="text-orange">'+item['department_name']+'</span></td>';
                inner += '</tr>';
            }
            return inner;
        }

    }


     //受训课程和人员处理
    proModify.handleCourseAndUser=function(){
        //弹窗-选择课程
         $("#btn_dialog_course").click(function(){
          // 获取左侧选择的受训角色ids
            if(!$(this).hasClass("unclick")){
                var treeObj = $.fn.zTree.getZTreeObj("role_tree");
                var checkedNodes = treeObj.getCheckedNodes(true);
                if(checkedNodes.length==0){
                    layer.msg("请选择受训角色");
                    return;
                }
                // 调用选择培训课程的弹窗
                layer.open({
                    type : 2,
                    title : '选择课程',
                    area : [ '1200px', '80%' ],		//弹出层大小
                    scrollbar : false,				//false隐藏滑动块
                    content : [ appPath + '/popup/enterSelectCourse', 'yes' ]
                });
            }
         });

      //弹窗-选择人员
      $("#btn_dialog_user").click(function(){
          if( !$(this).hasClass('unclick') ){
              // 调用选择培训人员弹窗
              layer.open({
                  type : 2,
                  title : '选择培训人员',
                  area : [ '1200px', '80%' ],		//弹出层大小
                  scrollbar : false,				//false隐藏滑动块
                  content : [ appPath + '/popup/enterSelectUser', 'yes' ]
              });
          }

      });

        // 批量删除课程
        $("#btn_delete_batch_course").click(function(){
            // 存在受训角色则必须选择
            if(!$(this).hasClass("unclick")){
                var treeObj = $.fn.zTree.getZTreeObj(role_tree_options.id);
                var rootNodes=treeObj.getNodes();

                var rootNode = rootNodes[0];
                if(rootNode.children.length>0){
                    var treeObj = $.fn.zTree.getZTreeObj("role_tree");
                    var checkedNodes = treeObj.getCheckedNodes(true);
                    if(checkedNodes.length==0){
                        layer.alert("请选择受训角色");
                        return;
                    }
                }
                proModify.project_delete_batch_course();
            }
        });
        // 批量删除人员
        $("#btn_delete_batch_user").click(function(){
            if(!$(this).hasClass('unclick')){
                proModify.project_delete_batch_user();
            }
        });
        // 根据课程名称查询
        $("#btn_search_course").click(function(){
            if(!$(this).hasClass("unclick")){
              $("#courseName").val($.trim($("#courseName").val()));
                proModify.project_role_tree_node_course();
            }
        });

        //查询所有
        $("#btn_search_all_course").click(function(){
            if(!$(this).hasClass("unclick")){
                $("#courseName").val('');
                proModify.project_role_tree_node_course();
            }

        });

        // 根据用户名称查询
        $("#btn_search_user").click(function(){
            if(!$(this).hasClass("unclick")){
              $("#userName").val($.trim($("#userName").val()));
                proModify.project_user();
            }
        });

        //查询所有
        $("#btn_search_all_user").click(function(){
            if(!$(this).hasClass("unclick")){
                $("#userName").val('');
                proModify.project_user();
            }

        });

        //全选课程
        $("#coursecheckAll").click(function () {
            var flag=$("#coursecheckAll").prop("checked");
            $('#select_course_list input[type="checkbox"]').prop("checked",flag);
        })

        //全选人员
        $("#usercheckAll").click(function () {
            var flag=$("#usercheckAll").prop("checked");
            $('#select_user_list input[type="checkbox"]').prop("checked",flag);
        })

    }

    // 批量删除课程
      proModify.project_delete_batch_course = function(){

        var couseInfoIds=[]
        var courseIds = [];
        $('#select_course_list input:checked').each(function(i,e){
            couseInfoIds.push($(this).attr("data-id"));
            courseIds.push($(this).attr("course-id"));
        })

        if(couseInfoIds.length==0){
            layer.msg("未选择课程");return;
        }
        var arr_ = [];
        var treeObj = $.fn.zTree.getZTreeObj("role_tree");
        var checkedNodes = treeObj.getCheckedNodes(true);
        if(checkedNodes.length==0){
            layer.alert("请选择受训角色");
            return;
        }
        for(var i=0; i<checkedNodes.length; i++){
            arr_.push(checkedNodes[i].roleId);
        }
        layer.confirm("确定执行此操作?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {
            $.ajax({
                url: appPath + '/admin/project_create/private/delete_project_course',
                async: false,
                type: 'post',
                data: {
                    'ids': couseInfoIds.join(","), 	                 // 项目总览id
                    'roleIds': arr_.join(","),	                     // 受训角色ids
                    'courseIds': courseIds.join(","),	              // 课程ids
                    'projectId': proModify.projectId,
                    'projectType': proModify.projectTypeNo
                },
                success: function(data){
                    var code=data['code']
                    if(code==10000){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            $("#coursecheckAll").prop("checked",false);
                            // 刷新左侧受训角色树
                            proModify.load_role_tree();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

     // 批量删除人员
     proModify.project_delete_batch_user = function(){
         var userIds=[]
         $('#select_user_list input:checked').each(function(i,e){
             userIds.push($(this).attr("data-id"));
         })

         if(userIds.length==0){
            layer.msg("未选择人员");return;
        }
        layer.confirm("确定执行此操作?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {
            $.ajax({
                url: appPath + '/admin/project_create/private/delete_project_user',
                async: false,
                type: 'post',
                data: {
                    'projectId': $("#projectId").val(), 	// 项目总览id
                    'ids': userIds.join(","),		// 人员ids
                    'projectType': proModify.projectTypeNo
                },
                success: function(data){
                    var code = data['code'];
                    if(code){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            $("#usercheckAll").prop("checked",false);
                            // 刷新项目人员
                            proModify.project_user();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });



    };

    // 保存修改的项目基本信息
     proModify.updateProject=function(){
         $("#btn_project_base_info_save").click(function(){
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
             if (proModify.permissionTrain) {
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
             if (proModify.permissionExercise) {
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
             if (proModify.permissionExam) {
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
             var trainRoles = $("#role_select2").select2("data");              //受训角色数组
             if(!proModify.verifyTrainRole(trainRoles)){
                 layer.msg('必须选择受训角色');
                 return;
             }
             var roleNames=proModify.getRoleName(trainRoles);
             trainRoles = proModify.assembleDate(trainRoles);
             var departments = $("#department_select2").select2("data");
             departments = proModify.assembleDate(departments)                 //受训单位数组

             var params = {
                 'projectId': proModify.projectId,                                           //主键
                 'projectType':proModify.projectTypeNo,                                      //项目类型
                 'projectName': projectName, 												 // 项目名称
                 'project_train_info': project_train_info,                                   // 培训项目时间
                 'project_exercise_info': project_exercise_info,		                     // 练习项目时间
                 'project_exam_info':  project_exam_info,                                     // 考试项目信息
                 'projectStatus':proModify.projectStatus,                                    //项目状态
                 'project_train_Time':project_train_Time,
                 'project_exercise_Time':project_exercise_Time,
                 'project_exam_Time':project_exam_Time,
                 'roles': trainRoles,                                                        //受训角色
                 'departments': departments ,                                                //受训单位
                 'roleNames':roleNames,                                                      //角色名称字符串
                 'trainBeginTime': trainBeginTime,                                           // 培训开始时间
                 'trainEndTime': trainEndTime,                                               // 培训结束时间
                 'exerciseBeginTime': exerciseBeginTime,                                     // 练习开始时间
                 'exerciseEndTime': exerciseEndTime,                                         // 练习结束时间
                 'examBeginTime': examBeginTime,                                             // 考试开始时间
                 'examEndTime': examEndTime,                                                 // 考试结束时间
                 'intRetestTime': intRetestTime                                              // 考试次数
             };
             proModify.saveProject(params);
         })

     }
     //发送异步保存项目信息
     proModify.saveProject=function (params) {
         if(!proModify.verify())return;
         $.ajax({
         dataType: 'json',
         url: appPath + '/admin/proManager/updateProject',
         async: false,
         type: 'post',
         data: params,
         success:function (data) {
         var code=data.code;
         if(10000==code){

         layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
         layer.close(index);
         // 重复保存限制
         $("#btn_project_base_info_save").addClass("unclick");
             proModify.step2();
             window.location.href=appPath+ "/admin/proManager/proManagerList";
             //加载受训角色树
            //proModify.load_role_tree(params.roles);
         });
         }

     }


     })
     }

    //验证受训角色
      proModify.verifyTrainRole=function(trainRoles) {
        if(trainRoles.length>0){
            return true;
        }else{
            return false;
        }
    }

    // 是否有下一步操作的权限
    proModify.step2 = function(){
        $(".project-details input").attr("disabled",true);
        $(".project-details select").attr("disabled",true);
        $("#role_select2").prop('disabled',true);
        $("#department_select2").prop('disabled',true);
        $("#btn_batch_examstrategy").removeClass("unclick").addClass("unclick");
        // 禁用-选择培训课程
        $("#btn_dialog_course").removeClass("unclick").addClass("unclick");
        // 启用-选择培训课程-批量删除
        $("#btn_delete_batch_course").removeClass("unclick").addClass("unclick").attr("disabled",true);
        // 启用-选择培训课程-搜索
        $("#btn_search_course").removeClass("unclick").addClass("unclick").attr("disabled",true);
        // 启用-选择培训课程-全部
        $("#btn_search_all_course").removeClass("unclick").addClass("unclick").attr("disabled",true);

        // 启用-选择其他人员
        $("#btn_dialog_user").addClass("unclick").click(false);
        // 启用-选择其他人员-批量删除
        $("#btn_delete_batch_user").removeClass("unclick").addClass("unclick").attr("disabled",true);
        // 启用-选择其他人员-搜索
        $("#btn_search_user").removeClass("unclick").addClass("unclick").attr("disabled",true);
        // 启用-选择其他人员-全部
        $("#btn_search_all_user").removeClass("unclick").addClass("unclick").attr("disabled",true);
    };


      //判断字符串是否包含某字符
      proModify.isContains=function(str,substr){
          return new RegExp(substr).test(str);
      }
      //封装角色和受训单位数据
     proModify.assembleDate=function(datas){
           var sbingDatas = [];
          for(var i = 0;i<datas.length;i++){
            var object = new Object();
            object.id = datas[i].id;
            object.text = datas[i].text;
            sbingDatas.push(object)
          }
          return JSON.stringify(sbingDatas);
     }
     //得到角色名称组合字符串
     proModify.getRoleName=function(datas){
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

    // 选择培训课程的回调
    proModify.submitCourse=function(rows){

        var courseIds = [];
        for(var i=0; i<rows.length; i++){
            courseIds.push(rows[i].course_id);
        }
        // 获取选中的培训角色
       var treeObj = $.fn.zTree.getZTreeObj("role_tree");
       var checkedNodes = treeObj.getCheckedNodes(true);
       var roles = [];
        for(var i=0; i<checkedNodes.length; i++){
            if(checkedNodes[i].roleId){
                var obj=new Object();
                obj.roleId=checkedNodes[i].roleId;
                obj.roleName=checkedNodes[i].name;
                roles.push(obj);
            }
        }

         roles =JSON.stringify(roles);

        // 选择培训课程
        $.ajax({
            url: appPath + '/admin/project_create/private/save_project_course',
            async: false,
            type: 'post',
            data: {
                'projectId': $("#projectId").val(),                                   // 项目id
                'roles': roles,                                                       // 受训角色ids
                'courseIds': courseIds.join(",")   ,                                  // 培训课程ids
                'projectTypeNo':proModify.projectTypeNo                               //项目类型
            },
            success: function(data){
                var code = data['code'];
                if(code==10000){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        // 刷新左侧受训角色树
                        proModify.load_role_tree(proModify.roles);

                        //加载课程列表信息
                        proModify.project_role_tree_node_course();

                    });
                }else{
                    layer.alert('操作失败', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    }





    // 选择人员的回调
    proModify.train_user_callback=function(rows){
       var userIds = [];
        for(var i=0; i<rows.length; i++){
            if(rows[i].id){
                var obj=new Object();
                obj.id=rows[i].id;
                obj.userName=rows[i].userName;
                obj.deptName=rows[i].deptName;
                userIds.push(obj);
            }
        }
        userIds =JSON.stringify(userIds);
        // 获取选择的培训角色
        var treeObj = $.fn.zTree.getZTreeObj("role_tree");
        var nodes=treeObj.getNodes()[0].children;
        var roles = [];
        for(var i=0; i<nodes.length; i++){
            if(nodes[i].roleId){
                var obj=new Object();
                obj.roleId=nodes[i].roleId;
                obj.name=nodes[i].name;
                roles.push(obj);
            }
        }

        //var varUserIds = userIds.join(",");//console.debug(varUserIds);
        var inner = "", item;
            inner+='<div id=\"tt_user_role\">';
        for(var i=0;i<roles.length;i++){
            item=roles[i];
            inner += '<label>';
            inner += '<input type=\"radio\" onchange="proModify.inputChange(this)" name="courseBox" data-id="' + item.roleId + '" value="' + item.name + '">';
            inner += '<span id="roleName">' + item.name + '</span>';
            inner += '</label>'
        }
        inner += '</div>';

        // 弹窗选择受训角色
        layer.open({
            type : 1,
            title : '请选择受训角色',
            shadeClose : false,				//true点击遮罩层关闭
            btn:['确定','取消'],
            shade : 0.3,					//遮罩层背景
            scrollbar : false,				//false隐藏滑动块
            area : [ '300px', '300px' ],
            content : inner,
            btn1: function(layero){

                var roleName=proModify.rolename;
                var roleId=proModify.roleid;

                //var rows = $('#tt_user_role').datagrid("getSelections"); // console.debug(rows);
                if(roleId==""){
                    layer.msg("未选择受训角色");return;
                }
              //var varRoleId = rows[0].varRoleId;
                $("#tt_user_role").html("");
                layer.close(layero);
                // 更改项目的其他人员
                proModify.changeProjectUserOther(roleId, roleName,userIds);
            },
            btn2: function(){
                $("#tt_user_role").html("");
            },
            cancel:function(){
                $("#tt_user_role").html("");
            }
        });

    }


        proModify.changeProjectUserOther=function(roleId,rolename, users){
            // 更改项目的其他人员
            $.ajax({
                url: appPath + '/admin/project_create/private/save_project_user',
                async: false,
                type: 'post',
                data: {
                    'id': $("#projectId").val(),     // 项目id
                    'roleId': roleId,                // 受训角色id
                    'roleName': rolename,            // 受训人员ids
                    'users':users
                },
                success: function(data){
                    var code = data['code'];
                    if(code==10000){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            // 刷新项目人员
                            proModify.project_user();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        }

proModify.rolename = "";
proModify.roleid = "";
proModify.inputChange = function (obj) {
    var $obj = $(obj);
    var roleid = $obj.attr('data-id');
    var rolename = $obj.next().text();
    proModify.rolename = rolename;
    proModify.roleid = roleid;
}

//课程预览
proModify.course_view = function(course_id){
  common.goto_student_course_view(course_id);
}

//试题预览
proModify.question_view = function(course_id){
  common.goto_student_course_question_view(course_id);
}