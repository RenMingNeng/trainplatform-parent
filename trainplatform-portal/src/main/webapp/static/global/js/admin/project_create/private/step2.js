
var step2=new Object();
step2.roles="";
step2.projectTypeNo = $("#projectTypeNo").val();
step2.projectId = $("#projectId").val();
step2.projectStatus = $("#projectStatus").val();
step2.examPermission=$("#examPermission").val();

    step2.init=function(basePath,page,options){
        step2.basePath=basePath;
        step2.page=page;
        //含有培训的
        step2.permissionTrain=options.permissionTrain;
        //含有考试的
        step2.permissionExam=options.permissionExam;
        //含有练习的
        step2.permissionExercise=options.permissionExercise;
        //是否有非默认角色的课程
        step2.flag = options.flag;
        //初始化课程
        step2.project_role_tree_node_course();
        //未开始的项目不能删除课程，但可以点击
        if('2' == step2.projectStatus){
            $("#btn_delete_batch_course").removeClass("not-create").addClass("not-create");
        }
        //进行中的项目不能删除课程
        if('3' == step2.projectStatus){
          $("#btn_delete_batch_course").removeClass("unclick").addClass("unclick");
        }
        //对于课程和人员的操作
        step2.handleCourseAndUser();
    }



     //受训课程和人员处理
    step2.handleCourseAndUser=function(){
        //弹窗-选择课程
         $("#btn_dialog_course").click(function(){
                // 调用选择培训课程的弹窗
                layer.open({
                    type : 2,
                    title : '选择课程',
                    area : [ '1200px', '80%' ],		//弹出层大小
                    scrollbar : false,				//false隐藏滑动块
                    content : [ appPath + '/popup/enterSelectCourse', 'yes' ]
                });

         });

        // 批量删除课程
        $("#btn_delete_batch_course").click(function(){
                if(!$(this).hasClass("unclick")){
                  step2.project_delete_batch_course();
                }
        });

        // 根据课程名称查询
        $("#btn_search_course").click(function(){
              $("#courseName").val($.trim($("#courseName").val()));
                step2.project_role_tree_node_course();
        });

        //查询所有
        $("#btn_search_all_course").click(function(){
                $("#courseName").val('');
                step2.project_role_tree_node_course();
        });

        //全选课程
        $("#checkAll").click(function () {
            var flag=$("#checkAll").prop("checked");
            $('#select_course_list input[type="checkbox"]').prop("checked",flag);
        })

        //回到首页
        $("#index").click(function () {
            window.location.href = appPath + "/admin";
        })

       //上一步操作
        $("#pev").click(function () {
            window.location.href = appPath + "/admin/project_create/private/basicInfoModify?projectTypeNo="+step2.projectTypeNo+"&projectId="+step2.projectId;
        })


        //下一步操作
        $("#next").click(function () {
            window.location.href = appPath + "/admin/project_create/private/step3?projectTypeNo="+step2.projectTypeNo+"&projectId="+step2.projectId+"&projectStatus="+step2.projectStatus+"&examPermission="+step2.examPermission;
        })

    }

       // 批量删除课程
      step2.project_delete_batch_course = function(){
        if(step2.projectStatus == '2'){
            layer.msg("若要删除课程，请先在首页取消发布本项目！");return;
        }
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
        arr_.push("-1");
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
                    'projectType':step2.projectTypeNo, //项目类型
                    'projectId': $("#projectId").val()
                },
                success: function(data){
                    var code=data['code']
                    if(code==10000){
                        //layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.closeAll();
                            $("#checkAll").prop("checked",false);
                            step2.project_role_tree_node_course();
                       // });

                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

      //判断字符串是否包含某字符
      step2.isContains=function(str,substr){
          return new RegExp(substr).test(str);
      }
      //封装角色和受训单位数据
     step2.assembleDate=function(datas){
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
    step2.submitCourse=function(rows){
        var courseIds = [];
        for(var i=0; i<rows.length; i++){
            courseIds.push(rows[i].course_id);
        }
                var roles = [];
                var obj=new Object();
                obj.roleId="-1";
                obj.roleName="默认角色";
                roles.push(obj);


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
                'projectTypeNo':step2.projectTypeNo                    //项目类型
            },
            success: function(data){
                var code = data['code'];
                if(code==10000){
                    /*layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);*/
                        //加载课程列表信息
                        step2.project_role_tree_node_course();

                   // });
                }else{
                    layer.alert('操作失败', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    }

// 项目-受训角色-课程信息
step2.project_role_tree_node_course = function(){
    var page_size=10

    var course_list_url = appPath + "/admin/project_create/private/project_course_list";
    step2.page.init("select_course_form", course_list_url, "select_course_list", "select_course_page", 1, page_size);
    step2.page.goPage(1);
    step2.page.list = function(dataList){
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
            if (step2.permissionTrain) {//console.log(step2.projectTypeNo)
                inner += '<td>'+item['class_hour']+'</td>';
                if(step2.flag ){
                inner += '<td>';
                  if('3' == step2.projectStatus){
                    inner += '<a href="javascript:;" class="editable" >' + item['requirement'] + '</a>'; //学时要求
                  }else{
                    inner += '<a href="javascript:;" onmouseover="step2.editRequirement(this,\''+ item.id +'\',' + item.course_id + ',' + item.role_id + ')" class="editable edit" >' + item['requirement'] + '</a>'; //学时要求
                  }
                inner += '</td>';
                }
            }
            if (step2.permissionExercise ||step2.permissionExam) {
                inner += '<td>'+item['question_count']+'</td>';
            }
            // if (step2.permissionExam && step2.flag) {
            //     inner += '<td>';
            //     if('3' == step2.projectStatus){
            //       inner += '<a href="javascript:;" class="editable" >' + item['select_count'] + '</a>'; //必选题量
            //     }else{
            //       inner += '<a href="javascript:;" onmouseover="step2.editSelectCount(this,\'' + item.id + '\',' + item.question_count + ',' + item.course_id + ')" class="editable edit" >' + item['select_count'] + '</a>'; //必选题量
            //     }
            //     inner += '</td>';
            // }
            inner +='<td>';
            if (step2.permissionTrain) {
                inner +='<a href="javascript:;" onclick=\"step2.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
            }
            if (step2.permissionExercise || step2.permissionExam) {
                inner +=' <a href="javascript:;" onclick=\"step2.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
            }
            inner +='</td>';
            inner += '</tr>';
        }
        return inner;
    }

};
        //修改应修学时
        step2.editRequirement = function (obj,id,courseId,roleId) {
            var projectId = $("#projectId").val();
            $.fn.editable.defaults.mode = 'popup';
            $(obj).editable({
                validate:function (value) {
                    if(value.length>1 && value.substring(0,1)==0){
                        return  '输入不合法';
                    }
                    if(value == ""){
                        value = 0 ;
                    }
                    if(isNaN(value) || Number(value) != parseInt(value)){
                      return  '输入不合法';
                    }
                    var param={
                        "projectId": projectId,
                        "id": id,
                        'roleId':roleId,
                        'courseId': courseId,
                        "requirement": value
                    }
                    step2.requirementOrSelectCount_ajax (param);
                },
                display:function (value) {
                if(value == ""){
                    $(this).text(0);
                }else {
                    $(this).text(value);
                }
            }
            })
        }
        //修改必选题量
        step2.editSelectCount = function (obj,id,questionCount,courseId) {
            var projectId = $("#projectId").val();
            $.fn.editable.defaults.mode = 'popup';
            $(obj).editable({
                validate:function (value) {
                    if(value.length>1 && value.substring(0,1)==0){
                        return  '输入不合法';
                    }
                    if(value == ""){
                        value = 0 ;
                    }
                    if(isNaN(value) ){
                      return  '输入不合法';
                    }
                    var param={
                        "projectId": projectId,
                        "id": id,
                        'courseId': courseId,
                        "selectCount": value
                    }
                    step2.requirementOrSelectCount_ajax (param);
                },
                display:function (value) {
                    if(value>questionCount ){
                        $(this).text(questionCount);
                    }else if(value == ""|| value <0){
                        $(this).text(0);
                    }else{
                        $(this).text(value);
                    }
                }
            })
        }

         step2.requirementOrSelectCount_ajax= function (param) {
            $.ajax({
                url: appPath + '/admin/project/updateRequirementAndSelectCount',
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

    //课程预览
    step2.course_view = function(course_id){
      common.goto_student_course_view(course_id);
    }

    //试题预览
    step2.question_view = function(course_id){
      common.goto_student_course_question_view(course_id);
    }
