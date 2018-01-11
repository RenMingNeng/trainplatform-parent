
var createPrivateProject=new Object();
createPrivateProject.roles="";
createPrivateProject.projectTypeNo = $("#projectTypeNo").val();
    createPrivateProject.init=function(basePath,page,page1,options){
       createPrivateProject.basePath=basePath;
        createPrivateProject.page=page;
        createPrivateProject.page1=page1;
        //含有培训的
        createPrivateProject.permissionTrain=options.permissionTrain;
        //含有考试的
        createPrivateProject.permissionExam=options.permissionExam;
        //含有练习的
        createPrivateProject.permissionExercise=options.permissionExercise;
        //是否有操作下一步的权限
        createPrivateProject.step2(false);
        //选择单位
        createPrivateProject.initDepartment();
        //选择受训角色
        createPrivateProject.initRole();
        //保存基本信息
        createPrivateProject.saveProjectBasic();
        //发布项目
        createPrivateProject.releaseProjectBasic();
        //对于课程和人员的操作
        createPrivateProject.handleCourseAndUser();
    }

   //初始化受训角色
    createPrivateProject.initRole=function() {
        $.ajax({
            url: appPath + '/admin/project_create/private/trainRole',
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
            }

        })

     }



     //初始化受训单位列表
      createPrivateProject.initDepartment=function() {
          $.ajax({
              url: appPath+ '/admin/project_create/private/trainDepartment',
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
              }

          })

    }


    // 保存项目基本信息
    createPrivateProject.saveProjectBasic=function(){
        $("#btn_project_base_info_save").click(function(){
            var $this = $(this);
            if( !$this.hasClass('unclick') ) {
                var projectStatus = "1";                                       //项目状态
                var trainRoles = $("#role_select2").select2("data");           //受训角色数组
                if(!createPrivateProject.verifyTrainRole(trainRoles)){
                    layer.msg('必须选择受训角色');
                    return;
                }

                trainRoles = createPrivateProject.assembleDate(trainRoles)
                createPrivateProject.roles=trainRoles;

                var departments = $("#department_select2").select2("data");
                departments = createPrivateProject.assembleDate(departments)   //受训单位数组
                var id = $("#projectId").val();                                //项目主键
                var projectTypeNo = $("#projectTypeNo").val();                 //项目类型
                var project_train_info = "";                                   //培训项目详情json(培训时间)
                var project_exercise_info = "";                                //练习项目详情json(练习时间)
                var project_exam_info = "";                                    //考试项目详情json(考试时间、考试次数)
                var beginTime = $("#projectBeginTime").val()+" 00:00:00";                  //项目开始时间
                var endTime = $("#projectEndTime").val()+" 23:59:59";                      //项目结束时间
                var trainBeginTime = "";
                var trainEndTime = "";
                var exerciseBeginTime = "";
                var exerciseEndTime = "";
                var examBeginTime = "";
                var examEndTime = "";
                var intRetestTime = "";
                //包含培训项目
                if (createPrivateProject.permissionTrain) {
                    project_train_info1 = {
                        "beginTime": beginTime,
                        "endTime": endTime
                    },
                    project_train_info = JSON.stringify(project_train_info1);
                    trainBeginTime = beginTime;
                    trainEndTime = endTime;
                }
                //包含练习项目
                if (createPrivateProject.permissionExercise) {
                    project_exercise_info1 = {
                        "beginTime": beginTime,
                        "endTime": endTime
                    };
                    project_exercise_info = JSON.stringify(project_exercise_info1)
                    exerciseBeginTime = beginTime;
                    exerciseEndTime = endTime;
                }
                //包含考试项目
                if (createPrivateProject.permissionExam) {
                    project_exam_info1 = {
                        "beginTime": $("#examBeginTime").val()+":00",
                        "endTime": $("#examEndTime").val()+":00",
                        "count": $("#intRetestTime").val()
                    };
                    project_exam_info = JSON.stringify(project_exam_info1);
                    examBeginTime = $("#examBeginTime").val()+":00";
                    examEndTime = $("#examEndTime").val()+":00";
                    intRetestTime = $("#intRetestTime").val();

                    stragegySelect.stragegy_listener();
                }

                var projectName = $("#projectName").val();                        //项目名称
                var subjectId = $("#subjectId").val();                            //主题编号
                var subjectName = $("#subjectName").val();                        //主题名称

                // 自动计算学员项目周期 = 项目结束时间-项目开始时间
                var start = Date.parse($("#projectBeginTime").val().replace(/-/g, '/')) / 86400000;
                var end = Date.parse($("#projectEndTime").val().replace(/-/g, '/')) / 86400000;
                var trainPeriod = Math.abs(start - end);                                                   //项目周期
                     trainPeriod = Math.ceil(trainPeriod);
                var id = $("#projectId").val();                                                            //项目主键
                var createUser = $("#createUser").val();                                                   //创建用户
                var createTime = $("#createTime").val();                                                   //创建时间
                var operUser = $("#operUser").val();                                                       //操作用户
                var operTime = $("#operTime").val();                                                       //操作时间


                var params = {
                    'id': id,                                               //主键
                    'projectStatus': projectStatus,                        //项目状态
                    'projectName': projectName, 							// 项目名称
                    'subjectId': subjectId, 								// 主题id
                    'subjectName': subjectName,                             // 主题名称
                    'projectType': projectTypeNo, 							// 项目类型
                    'trainPeriod': trainPeriod, 							// 项目开始时间
                    'projectTrainInfo': project_train_info,               // 培训项目时间
                    'projectExerciseInfo': project_exercise_info,		      // 练习项目时间
                    'projectExamInfo': project_exam_info,                 // 考试项目信息
                    'trainBeginTime': trainBeginTime,                       // 培训开始时间
                    'trainEndTime': trainEndTime,                           // 培训结束时间
                    'exerciseBeginTime': exerciseBeginTime,                 // 练习开始时间
                    'exerciseEndTime': exerciseEndTime,                     // 练习结束时间
                    'examBeginTime': examBeginTime,                         // 考试开始时间
                    'examEndTime': examEndTime,                             // 考试结束时间
                    'intRetestTime': intRetestTime,                         // 考试次数
                    'createUser': createUser,                              // 创建用户
                    'createTime': createTime,                              // 创建时间
                    'projectOpen':'1',                                     // 项目开启状态
                    'operUser': operUser,                                  // 操作用户
                    'operTime': operTime,                                  // 操作时间
                    'roles': trainRoles,                                    // 受训角色
                    'departments': departments                              // 受训单位
                };
                //保存项目信息
                createPrivateProject.saveProject(params);
            }
        });
    }
       //发送异步保存项目信息
      createPrivateProject.saveProject=function (params) {
         $.ajax({
            dataType: 'json',
            url: appPath + '/admin/project_create/private/save',
            async: false,
            type: 'post',
            data:params,
            success:function (data) {
                 var code=data.code;
                if(10000==code){

                     layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon',closeBtn:0}, function(index){
                         layer.close(index);
                         // 重复保存限制
                         $("#btn_project_base_info_save").addClass("unclick");

                             //进行下一步的操作（基础信息不允许更改）
                             createPrivateProject.step2(true);
                             //加载受训角色树
                             createPrivateProject.load_role_tree(params.roles);
                         });
                     }
                }
          })
      }


    //验证受训角色
    createPrivateProject.verifyTrainRole=function(trainRoles) {
        if(trainRoles.length>0){
            return true;
        }else{
            return false;
        }
    }

      // 是否有下一步操作的权限
     createPrivateProject.step2 = function(flag_){
         if(flag_){
             var $div_2 = $('.index_2');
             var $div_3 = $('.index_3');
             var $div_4 = $('.index_4');
             if( $div_2  ){
                 $div_2.show();
             }
             if( $div_3  ){
                 $div_3.show();
             }
             if( $div_4  ){
                 $div_4.show();
             }
            // 禁用-项目基本信息
            /*$(".project-details input").attr("disabled",true);
            $(".project-details select").attr("disabled",true);
             $("#role_select2").prop('disabled',true);
             $("#department_select2").prop('disabled',true);
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
            $("#btn_search_all_user").removeClass("unclick");*/

            return;
        }
        // 启用-项目基本信息
        /*$(".project-details input").attr("readonly",false);
        // 禁用-选择培训课程
        $("#btn_dialog_course").removeClass("unclick").addClass("unclick");
        // 启用-选择培训课程-批量删除
        $("#btn_delete_batch_course").removeClass("unclick").addClass("unclick");
        // 启用-选择培训课程-搜索
        $("#btn_search_course").removeClass("unclick").addClass("unclick");
        // 启用-选择培训课程-全部
        $("#btn_search_all_course").removeClass("unclick").addClass("unclick");

        // 启用-选择其他人员
        $("#btn_dialog_user").addClass("unclick");
        // 启用-选择其他人员-批量删除
        $("#btn_delete_batch_user").removeClass("unclick").addClass("unclick");
        // 启用-选择其他人员-搜索
        $("#btn_search_user").removeClass("unclick").addClass("unclick");
        // 启用-选择其他人员-全部
        $("#btn_search_all_user").removeClass("unclick").addClass("unclick");*/
    };

       //发布项目
       createPrivateProject.releaseProjectBasic=function(){
           // 保存并发布
           $("#btn_project_save_publish").click(function(){
               var val=$("#btn_project_base_info_save").hasClass("unclick");
               if(!val){
                   layer.msg('请先填写项目基本信息并保存');
                   return;
               }
               var projectStatus = "2";

               if(!createPrivateProject.verify())return;
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
                           layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                               layer.close(index);
                               // 返回首页
                              window.location.href=appPath+ "/admin";
                           });
                       }else{
                           layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                       }
                   }
               });
           });
      };

     //验证考试次数为整数
     createPrivateProject.verifyIntRetestTime=function(value) {
         var value_ = $.trim(value);
         //value= value_.replace(/\D/g, '');
         value= Math.round(value_);
         $("#intRetestTime").val( value);
     }

    // 表单校验：不含考试的项目，【项目名称】【项目时间】为必填项，其他都是非必填项。
    //          含考试的项目，【项目名称】【项目时间】【考试时间】【考试次数为必填项】
    createPrivateProject.verify = function(){
        // 错误信息
        var msg = "";
        // 项目名称
        var projectName = $.trim($("#projectName").val());
        if(!projectName){
            msg = "请填写项目名称";
            layer.msg(msg);
            return false;
        }
        // 项目开始时间
        var projectBeginTime = $.trim($("#projectBeginTime").val());
        if(!projectBeginTime){
            msg = "请选择项目开始时间";
            layer.msg(msg);
            return false;
        }
        // 项目结束时间
        var projectEndTime = $.trim($("#projectEndTime").val());
        if(!projectEndTime){
            msg = "请选择项目结束时间";
            layer.msg(msg);
            return false;
        }
        // 1：培训/2：练习/3：考试/4：培训，练习/5：培训，考试/6：练习，考试/7：培训，练习，考试
        var chrProjectType = createPrivateProject.projectTypeNo;
        if(createPrivateProject.permissionExam){
            // 考试开始时间
            var examBeginTime = $.trim($("#examBeginTime").val());
            if(!examBeginTime){
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
                  onRightClick: function(event, treeId, treeNode){

                },
                onClick: function(e, treeId, treeNode){
                    $("#btn_search_course").attr("roleId", treeNode.roleId); // 记住当前点击的受训角色,搜索用
                    $("#roleId").val(treeNode.roleId);                       //赋值便于搜索项目课程信息
                    createPrivateProject.project_role_tree_node_course();
                },
                onRename: function(event, treeId, treeNode, isCancel){

                }
            }
        },
        zNodes : '',
        rMenu : ''
    };

    //加载受训角色树
    createPrivateProject.load_role_tree=function(roles){
        $.ajax({
            url: appPath + '/admin/project_create/private/trainRole/tree',
            async: false,
            type: 'post',
            data: {'roles':roles},
            success: function(data){
                var datalist=data.result
                role_tree_options.zNodes = eval(datalist);
                $.fn.zTree.init($("#"+role_tree_options.id), role_tree_options.setting, role_tree_options.zNodes);
                // 设置根节点不可选
                var treeObj = $.fn.zTree.getZTreeObj(role_tree_options.id);
                var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
                var rootNode = rootNodes[0];
                rootNode.nocheck = true;
                treeObj.updateNode(rootNode);
                // 默认选中根节点
                treeObj.selectNode(rootNode);
                treeObj.setting.callback.onClick(null, role_tree_options.id, rootNode);
            }
        });

    }
    // 项目-受训角色-课程信息
    createPrivateProject.project_role_tree_node_course = function(){
        var page_size=10

        var course_list_url = appPath + "/admin/project_create/private/project_course_list";
        createPrivateProject.page.init("select_course_form", course_list_url, "select_course_list", "select_course_page", 1, page_size);
        createPrivateProject.page.goPage(1);
        createPrivateProject.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                    item = dataList[i];
                    inner += '<tr >';
                    inner += '<td>' + (i+1) + '</td>';
                    inner += '<td><input type=\"checkbox\" name="courseBox" data-courseId="' + item['course_id'] + '" data-id="' + item['id'] + '"></td>';
                    inner +='<td>'+item['course_no']+'</td>';
                    inner +='<td><span class="text-orange tooltip" data-length="8">'+item['course_name']+'</span></td>';
                    inner += '<td><span class="text-orange tooltip" data-length="8">'+item['role_name']+'</span></td>';
                if (createPrivateProject.permissionTrain) {
                    inner += '<td>'+item['class_hour']+'</td>';
                    inner += '<td>';
                    inner += '<a href="javascript:;" onmouseover="createPrivateProject.editRequirement(this,\''+ item.id +'\')" class="editable editable-click" >' + item['requirement'] + '</a>'; //学时要求
                    inner += '</td>';
                }
                if (createPrivateProject.permissionExercise ||createPrivateProject.permissionExam) {
                    inner += '<td>'+item['question_count']+'</td>';
                }
                if (createPrivateProject.permissionExam) {
                    inner += '<td>';
                    inner += '<a href="javascript:;" onmouseover="createPrivateProject.editSelectCount(this,\''+ item.id +'\','+ item.question_count+')" class="editable editable-click" >' + item['select_count'] + '</a>'; //必选题量
                    inner += '</td>';
                }
                    inner +='<td>';
                  if (createPrivateProject.permissionTrain) {
                    inner +='<a href="javascript:;" onclick=\"createPrivateProject.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
                  }
                  if (createPrivateProject.permissionExercise || createPrivateProject.permissionExam) {
                    inner +=' <a href="javascript:;" onclick=\"createPrivateProject.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
                  }
                    inner +='</td>';
                    inner += '</tr>';
            }
            return inner;
        }

        //包含考试项目
        if (createPrivateProject.permissionExam) {
            //组卷策略
            stragegySelect.stragegy_select($("#projectId").val(),createPrivateProject.permissionTrain);
        }
    };
   //修改应修学时
    createPrivateProject.editRequirement = function (obj,id,roleId) {
        var projectId = $("#projectId").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
            validate:function (value) {
                var param={
                    "projectId": projectId,
                    "id": id,
                    "requirement": value
                }
                createPrivateProject.requirementOrSelectCount_ajax (param);
            }
        })
    }
   //修改必选题量
    createPrivateProject.editSelectCount = function (obj,id,questionCount) {
        var projectId = $("#projectId").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
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
                    "selectCount": value
                }
                createPrivateProject.requirementOrSelectCount_ajax (param);
            },
            display:function (value) {
            if(value>questionCount || value < 0 ){
                $(this).text(questionCount);
            }else {
                $(this).text(value);
            }
        }
        })
    }

    createPrivateProject.requirementOrSelectCount_ajax= function (param) {
        $.ajax({
            url: appPath + '/admin/proManager/updateRequirementAndSelectCount',
            dataType: 'json',
            async: false,
            type: 'post',
            data: param,
            success: function (data) {
                var result = data.code;
                if ('10000' == result) {
                    layer.msg("设置成功");
                } else {
                    layer.msg("设置失败");
                }
            }
        });
    }

    /*createPrivateProject.requirement_ajax = function (value,id,projectId) {
        $.ajax({
            url: appPath + '/admin/project_create/private/updateRequirement',
            dataType: 'json',
            async: false,
            type: 'post',
            data: {
                "projectId": projectId,
                "id": id,
                "requirement": value
            },
            success: function (data) {
                var result = data.code;
                if ('10000' == result) {
                    layer.alert("设置成功");
                } else {
                    layer.alert("设置失败");
                }
            }
        });
    }
*/
     //受训课程和人员处理
    createPrivateProject.handleCourseAndUser=function(){
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
                    title : '选择培训课程',
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
                createPrivateProject.project_delete_batch_course();
            }
        });
        // 批量删除人员
        $("#btn_delete_batch_user").click(function(){
            if(!$(this).hasClass('unclick')){
                createPrivateProject.project_delete_batch_user();
            }
        });
        // 根据课程名称查询
        $("#btn_search_course").click(function(){
            if(!$(this).hasClass("unclick")){
              $("#courseName").val($.trim($("#courseName").val()));
                createPrivateProject.project_role_tree_node_course();
            }
        });

        //查询所有
        $("#btn_search_all_course").click(function(){
            if(!$(this).hasClass("unclick")){
                $("#courseName").val('');
                createPrivateProject.project_role_tree_node_course();
            }

        });

        // 根据用户名称查询
        $("#btn_search_user").click(function(){
            if(!$(this).hasClass("unclick")){
              $("#userName").val($.trim($("#userName").val()));
                createPrivateProject.project_user();
            }
        });

        //查询所有
        $("#btn_search_all_user").click(function(){
            if(!$(this).hasClass("unclick")){
                $("#userName").val('');
                createPrivateProject.project_user();
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
      createPrivateProject.project_delete_batch_course = function(){

        var couseInfoIds=[]
        $('#select_course_list input:checked').each(function(i,e){
            couseInfoIds.push($(this).attr("data-id"));
        })

          var courseIds=[]
          $('#select_course_list input:checked').each(function(i,e){
              courseIds.push($(this).attr("data-courseId"));
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
                    'ids': couseInfoIds.join(","), 	   // proejctCourse主键id
                    'roleIds': arr_.join(","),	       // 受训角色ids
                    'courseIds':courseIds.join(","),   //课程id
                    'projectType':createPrivateProject.projectTypeNo, //项目类型
                    'projectId': $("#projectId").val()
                },
                success: function(data){
                    var code=data['code']
                    if(code==10000){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            $("#coursecheckAll").prop("checked",false);
                            // 刷新左侧受训角色树
                            createPrivateProject.load_role_tree(createPrivateProject.roles);
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

     // 批量删除人员
     createPrivateProject.project_delete_batch_user = function(){
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
                    'projectId': $("#projectId").val(), 	            // 项目总览id
                    'ids': userIds.join(","), 		                    // 人员ids
                    'projectType':createPrivateProject.projectTypeNo    //项目类型
                },
                success: function(data){
                    var code = data['code'];
                    if(code){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            $("#usercheckAll").prop("checked",false);
                            // 刷新项目人员
                            createPrivateProject.project_user();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

      //判断字符串是否包含某字符
      createPrivateProject.isContains=function(str,substr){
          return new RegExp(substr).test(str);
      }
      //封装角色和受训单位数据
     createPrivateProject.assembleDate=function(datas){
          var sbingDatas=[];
              for(var i = 0;i<datas.length;i++){
                var object = new Object();
                object.id = datas[i].id;
                object.text = datas[i].text;
                sbingDatas.push(object)
              }
          return JSON.stringify(sbingDatas);
     }


    // 选择培训课程的回调
    createPrivateProject.submitCourse=function(rows){

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
                'projectId': $("#projectId").val(),                                  // 项目id
                'roles': roles,                                                      // 受训角色ids
                'courseIds': courseIds.join(",")   ,                                  // 培训课程ids
                'projectTypeNo':createPrivateProject.projectTypeNo                    //项目类型
            },
            success: function(data){
                var code = data['code'];
                if(code==10000){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        // 刷新左侧受训角色树
                        createPrivateProject.load_role_tree(createPrivateProject.roles);

                        //加载课程列表信息
                        createPrivateProject.project_role_tree_node_course();

                    });
                }else{
                    layer.alert('操作失败', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    }

    //课程预览
    createPrivateProject.course_view = function(course_id){
      common.goto_student_course_view(course_id);
    }

    //试题预览
    createPrivateProject.question_view = function(course_id){
      common.goto_student_course_question_view(course_id);
    }

    // 选择人员的回调
    createPrivateProject.train_user_callback=function(rows){
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
            inner += '<input type=\"radio\" onchange="createPrivateProject.inputChange(this)" name="courseBox" data-id="' + item.roleId + '" value="' + item.name + '">';
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

                var roleName=createPrivateProject.roleName;
                var roleId=createPrivateProject.roleId;

                //var rows = $('#tt_user_role').datagrid("getSelections"); // console.debug(rows);
                if(roleId==""){
                    layer.msg("未选择受训角色");return;
                }
              //var varRoleId = rows[0].varRoleId;
                $("#tt_user_role").html("");
                layer.close(layero);
                // 更改项目的其他人员
                createPrivateProject.changeProjectUserOther(roleId, roleName,userIds);
            },
            btn2: function(){
                $("#tt_user_role").html("");
            },
            cancel:function(){
                $("#tt_user_role").html("");
            }
        });

    }


        createPrivateProject.changeProjectUserOther=function(roleId,rolename, users){
            // 更改项目的其他人员
            $.ajax({
                url: appPath + '/admin/project_create/private/save_project_user',
                async: false,
                type: 'post',
                data: {
                    'id': $("#projectId").val(),     // 项目id
                    'roleId': roleId,                // 受训角色id
                    'roleName': rolename,            // 受训人员ids
                    'users': users,
                    'projectType': createPrivateProject.projectTypeNo
                },
                success: function(data){
                    var code = data['code'];
                    if(code==10000){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            // 刷新项目人员
                            createPrivateProject.project_user();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        }
    //刷新项目人员
    createPrivateProject.project_user=function () {
        var page_size=10
        var user_list_url = appPath + "/admin/project_create/private/project_user_list";
        createPrivateProject.page1.init("select_user_form", user_list_url, "select_user_list", "select_user_page", 1, page_size);
        createPrivateProject.page1.goPage(1);
        createPrivateProject.page1.list = function(dataList){
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

    createPrivateProject.roleName  = "";
    createPrivateProject.roleId  = "";
    createPrivateProject.inputChange=function(obj){
        var $obj = $(obj);
        var roleId=$obj.attr('data-id');
        var roleName = $obj.next().text();
            createPrivateProject.roleName= roleName;
            createPrivateProject.roleId=roleId;
    }