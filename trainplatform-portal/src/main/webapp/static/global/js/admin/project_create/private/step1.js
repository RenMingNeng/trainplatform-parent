
var step1=new Object();
step1.roles="";
step1.deptIds=[];
step1.projectTypeNo = $("#projectTypeNo").val();
step1.projectId=$("#projectId").val();
step1.projectStatus = "";
    step1.init=function(basePath,options){
       step1.basePath=basePath;
        //含有培训的
        step1.permissionTrain=options.permissionTrain;
        //含有考试的
        step1.permissionExam=options.permissionExam;
        //含有练习的
        step1.permissionExercise=options.permissionExercise;
        //是否有操作下一步的权限
        //step1.step2(false);
        //选择单位
        step1.initDepartment();
        //选择受训角色
        //step1.initRole();
        //保存基本信息
        step1.saveProjectBasic();
        //发布项目
        //step1.releaseProjectBasic();
        //对于课程和人员的操作
        //step1.handleCourseAndUser();
        window.groupTreeCheckbox_search = step1.groupTreeChecked;
    }

    step1.dis_node = function (treeObj,node) {
        treeObj.checkNode(node, false, false, true);
        var children = node.children;
        if(children){
            for(var j=0; j< children.length; j++) {
                step1.dis_node(treeObj,children[j]);
            }
        }
    };
    
    step1.groupTreeChecked  = function(treeObj,treeNode){
        if(!$("#isChecked").is(":checked")){
            var nodes = treeNode.children;
            if(nodes){
                for(var i=0; i< nodes.length; i++) {
                    if(nodes[i] && nodes[i].type == 2){
                        step1.dis_node(treeObj,nodes[i]);
                    }
                }
            }
        }
    }

   /*//初始化受训角色
    step1.initRole=function() {
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

     }*/
/*var zNodes =eval(data.result);
// 加载树
$.fn.zTree.init($("#"+tree_options.id), tree_options.setting, zNodes);
var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);


//初始化默认所选单位选中状态
for(var i = 0; i< step1.deptIds.length ;i++){
    var node = treeObj.getNodeByParam("id", step1.deptIds[i], null);
    node.checked = true ;
    treeObj.updateNode(node);
}*/


     //初始化受训单位列表
      step1.initDepartment=function() {
          $("#department_select2").select2();
          $.ajax({
              url: appPath+ '/admin/project_create/private/trainDepartment',
              dataType: 'json',
              type:'get',
              success:function (data) {
                  var list=data.result;

                  $("#department_select2").select2({
                      tags:true,
                      data:list,
                      multiple:true,
                      placeholder:"请选择受训单位",
                      language:"zh-CN"
                  })

                  //发送异步请求项目下已有的单位
                  $.ajax({
                      url: appPath + '/admin/project_create/private/projectDepartment',
                      dataType: 'json',
                      type:'get',
                      data:{"projectId":step1.projectId},
                      success:function (data) {
                          var list=data.result;
                          var len=list.length;
                          var inner = "", item;

                          // 组装数据
                          for(var i=0; i< len; i++) {
                              item = list[i];
                              step1.deptIds.push(item.deptId)
                          }
                          $("#department_select2").val(step1.deptIds).trigger("change");
                          //点击小X触发，取消树中节点的勾选状态
                          step1.deleteDept();
                      }

                  })
              }

          })

    }
    step1.selectDept=function(){
        //得到最新的受训单位数据
        var departments = $("#department_select2").select2("data");
        step1.getNewDate(departments);
        $('.por').addClass('show');

    };

    //隐藏树下拉框
    step1.close = function(event){
        $('.por').removeClass('show');
    }
    // 确定受训角色
    step1.finish = function(){
        var arr_ = [];
        var treeObj = $.fn.zTree.getZTreeObj("group_tree");
        var checkedNodes = treeObj.getCheckedNodes(true);
        if(checkedNodes.length==0){
            layer.msg("请选择受训单位",{time : 1000});
            return;
        }

        for(var i=0; i<checkedNodes.length; i++){
            var companyId = checkedNodes[i].companyId;
            var departmentId = checkedNodes[i].departmentId;
            var name = checkedNodes[i].name;
            if(checkedNodes[i].departmentId == undefined || '' == checkedNodes[i].departmentId ){
                arr_.push(checkedNodes[i].companyId);
            }else{
                arr_.push(checkedNodes[i].departmentId);
            }

        }
        /*if("step1"==value){
         parent.step1.getDept(arr_);
         }*/
        $("#department_select2").val(arr_).trigger("change");
        step1.close();
    }

    //点击小X触发，取消树中节点的勾选状态
    step1.deleteDept = function () {
        $(document).delegate('.select2-selection__choice__remove','click',function(){
            var id = $(this).parent().attr('id');
            var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
            var node = treeObj.getNodeByParam("id", id, null);
            node.checked = false ;
            treeObj.updateNode(node);
        })
    }

      //弹窗选择部门后回调
      step1.getDept = function (depatements) {
        // 组装数据
        for(var i=0; i< depatements.length; i++) {
            if(step1.repeatDept(depatements[i],step1.deptIds)){
            step1.deptIds.push(depatements[i])
            }
        }

    }

    // 保存项目基本信息
    step1.saveProjectBasic=function(){
        $("#next").click(function(){
            var $this = $(this);
                var projectStatus = "1";                                       //项目状态
                step1.projectStatus = projectStatus;
                /*var trainRoles = $("#role_select2").select2("data");           //受训角色数组
                if(!step1.verifyTrainRole(trainRoles)){
                    layer.msg('必须选择受训角色');
                    return;
                }

                trainRoles = step1.assembleDate(trainRoles)
                step1.roles=trainRoles;*/

                var departments = $("#department_select2").select2("data");
                departments = step1.assembleDate(departments)   //受训单位数组
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
                if (step1.permissionTrain) {
                    project_train_info1 = {
                        "beginTime": beginTime,
                        "endTime": endTime
                    },
                    project_train_info = JSON.stringify(project_train_info1);
                    trainBeginTime = beginTime;
                    trainEndTime = endTime;
                }
                //包含练习项目
                if (step1.permissionExercise) {
                    project_exercise_info1 = {
                        "beginTime": beginTime,
                        "endTime": endTime
                    };
                    project_exercise_info = JSON.stringify(project_exercise_info1)
                    exerciseBeginTime = beginTime;
                    exerciseEndTime = endTime;
                }
                //包含考试项目
                if (step1.permissionExam) {
                    project_exam_info1 = {
                        "beginTime": $("#examBeginTime").val()+":00",
                        "endTime": $("#examEndTime").val()+":00",
                        "count": $("#intRetestTime").val()
                    };
                    project_exam_info = JSON.stringify(project_exam_info1);
                    examBeginTime = $("#examBeginTime").val()+":00";
                    examEndTime = $("#examEndTime").val()+":00";
                    intRetestTime = $("#intRetestTime").val();

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
                    //'roles': trainRoles,                                    // 受训角色
                    'departments': departments                              // 受训单位
                };
                //保存项目信息
                step1.saveProject(params);
        });
    }
       //发送异步保存项目信息
      step1.saveProject=function (params) {
          if(!step1.verify())return;
         $.ajax({
            dataType: 'json',
            url: appPath + '/admin/project_create/private/save',
            async: false,
            type: 'post',
            data:params,
            success:function (data) {
                 var code=data.code;
                if(10000==code){
                         window.location.href=appPath+"/admin/project_create/private/step2?projectTypeNo="+step1.projectTypeNo+"&projectId="+step1.projectId+"&projectStatus="+step1.projectStatus;
                     }
                }
          })
      }


   /* //验证受训角色
    step1.verifyTrainRole=function(trainRoles) {
        if(trainRoles.length>0){
            return true;
        }else{
            return false;
        }
    }*/

     //验证考试次数为整数
     step1.verifyIntRetestTime=function(value) {
         var value_ = $.trim(value);
         value= value_.replace(/\D/g, '');
        // value= Math.round(value_);
         $("#intRetestTime").val( value);
     }

    // 表单校验：不含考试的项目，【项目名称】【项目时间】为必填项，其他都是非必填项。
    //          含考试的项目，【项目名称】【项目时间】【考试时间】【考试次数为必填项】
    step1.verify = function(){
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
        var chrProjectType = step1.projectTypeNo;
        if(step1.permissionExam){
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



          //判断字符串是否包含某字符
          step1.isContains=function(str,substr){
              return new RegExp(substr).test(str);
          }
          //封装角色和受训单位数据
         step1.assembleDate=function(datas){
              var sbingDatas=[];
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
        step1.getNewDate=function(datas){
            var deptDatas = [];
            for(var i = 0;i<datas.length;i++){
                deptDatas.push(datas[i].id);
            }
            step1.deptIds = deptDatas;
        }
        //去除重复的受训角色
        step1.repeatDept=function(id,depatements){
            var flag=true;
            for (var i=0;i<depatements.length;i++){
                if(depatements[i]==id){
                    flag=false;
                }
            }
            return flag;
        }