
var step4=new Object();
step4.obj = null;
step4.roles = "";
step4.roles1 = []
step4.courses = []
step4.role_length = $("#train-role-length").val();
step4.projectTypeNo = $("#projectTypeNo").val();
step4.projectId = $("#projectId").val();
step4.projectStatus = $("#projectStatus").val();
step4.examPermission=$("#examPermission").val();

    step4.init=function(basePath,page,options){
       step4.basePath=basePath;
        step4.page=page;
        step4.page1=page1;
        //含有培训的
        step4.permissionTrain=options.permissionTrain;
        //含有考试的
        step4.permissionExam=options.permissionExam;
        //含有练习的
        step4.permissionExercise=options.permissionExercise;
        //获取受训角色
        step4.initRole();
        step4.initProjectCourse();
        //开关相关事件
        step4.hoddleSwitch();
    }
    //初始化受训角色
     step4.initRole=function() {
        $.ajax({
            url: appPath + '/admin/project/advanceSetting/getProjectRole',
            dataType: 'json',
            type:'get',
            data:{"projectId":step4.projectId},
            success:function (data) {
                var list=data;
                step4.roles= list;
                console.log(step4.roles);
                //初始化课程
                step4.project_role_tree_node_course();

            }

        })

    }

        //初始化受训角色
        step4.initProjectCourse=function() {
            $.ajax({
                url: appPath + '/admin/project/advanceSetting/getProjectCourse',
                dataType: 'json',
                type:'get',
                data:{"projectId":step4.projectId},
                success:function (data) {
                    var list=data;
                    step4.courses= list;
                    console.log("====="+step4.courses);
                }

            })

        }

    // 项目-受训角色-课程信息
    step4.project_role_tree_node_course = function(){
        var page_size=10
        var course_list_url = appPath + "/admin/project/advanceSetting/course_role";
        step4.page.init("select_course_form", course_list_url, "select_course_role", "select_course_page", 1, page_size);
        step4.page.goPage(1);
        step4.page.list = function(dataList){
            $(".switchAll").addClass("on");
            step4.obj = dataList;
            var len = dataList.length;
            var inner = "", item,role;
            // 组装数据
            for(var i=0; i< len; i++) {
                    item = dataList[i];
                    inner += '<tr >';
                    inner += '<td><span class="text-orange tooltip" data-length="4">'+item['course_name']+'</span>';
                    inner +='<input type="hidden" id="id" value="'+ item.id +'">';
                    inner +='<input type="hidden" id="projectId" value="'+ item.project_id +'">';
                    inner +='<input type="hidden" id="courseId" value="'+ item.course_id +'">';
                    inner +='<input type="hidden" id="courseNo" value="'+ item.course_no +'">';
                    inner +='<input type="hidden" id="courseName" value="'+ item.course_name +'">';
                    inner +='<input type="hidden" id="classHour" value="'+ item.class_hour +'">';
                    inner +='<input type="hidden" id="questionCount" value="'+ item.question_count +'">';
                    inner +='<input type="hidden" id="operUser" value="'+ item.oper_user +'">';
                    inner += '</td>'
                    for(var j=0; j < step4.roles.length; j++){
                        role=step4.roles[j];
                       var index = step4.select(role.roleId,step4.roles,item);
                       // if(role.id == item['role_id'+j]){
                        if(index != -1){
                          if("3" == step4.projectStatus){
                            inner += '<td><div class="on switch_btn selected switch_'+ j +'" i="'+i+'" id="switch-'+j+'-'+role.roleId+'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-index="'+ j +'"><span></span>';
                            inner += '</div></td>';
                            inner +='<td>';
                            inner += '<a href="javascript:;" class="editable edit" >' + item['requirement'+index] + '</a>'; //学时要求
                            inner +='</td>'
                            inner +='<td>';
                            inner += '<a href="javascript:;">' + item['select_count'+index] + '</a>'; //必选题量
                            inner += '</td>';
                          }else{
                            inner += '<td><div class="on switch_btn switch_'+ j +'" i="'+i+'" id="switch-'+j+'-'+role.roleId+'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-index="'+ j +'" onclick="step4.switch(this);"><span></span>';
                            inner += '</div></td>';
                            inner +='<td>';
                            inner += '<a href="javascript:;" onmouseover="step4.editRequirement(this,\''+ item.course_id +'\')" class="editable  edit" >' + item['requirement'+index] + '</a>'; //学时要求
                            inner +='</td>'
                            inner +='<td>';
                            inner += '<a href="javascript:;" onmouseover="step4.editSelectCount(this,\''+ item.course_id +'\','+ item.question_count+')"  class="editable  edit">' + item['select_count'+index] + '</a>'; //必选题量
                            inner += '</td>';
                          }
                        }else{
                            inner += '<td><div class="switch_btn switch_index switch_'+ j +'" data-roleId="'+ role.roleId +'"  data-roleName="'+ role.roleName +'" data-index="'+ j +'" onclick="step4.switch(this);"><span></span>';
                            inner += '</div></td>';
                            inner +='<td>';
                            inner += '<a href="javascript:;" onmouseover="step4.editRequirement(this,\''+ item.course_id +'\')" class="editable">' + 0 + '</a>'; //学时要求
                            inner +='</td>'
                            inner +='<td>';
                            inner += '<a href="javascript:;" onmouseover="step4.editSelectCount(this,\''+ item.course_id +'\','+ item.question_count+')" class="editable" >' + 0 + '</a>'; //必选题量
                            inner += '</td>';

                            if($("#switch-train-role-" + role.roleId).hasClass("on")){
                                $("#switch-train-role-" + role.roleId).removeClass("on");
                            }
                        }
                    }
                    inner += '</tr>';
            }
            return inner;
        }
    };

   //比对roleId返回角标，用于判断是否打开开关
    step4.select =function (roleId,roles,item) {
        var index = -1;
        for(var i=0;i<=roles.length;i++){
            if(roleId == item['role_id'+i] ){
               index=i;
               break;
            }
        }
        return index;
    }

    //开关事件
    step4.switch=function (obj) {
        $(obj).toggleClass('on');
        var index = $(obj).attr('data-index');
        //一列内没有选中，表头选中要去掉
        var flag=true;
        $.each($("#select_course_role .switch[data-index="+index+"]"),function (i,e) {
            if(!$(this).hasClass("on")){
                flag=false;
            }
        })
        if(flag){
            $(".switchAll[data-index='"+index+"']").addClass("on");
        }else{
            $(".switchAll[data-index='"+index+"']").removeClass("on");
        }

       //开关打开绑定事件，关闭取消事件
        var $td = $(obj).parent().parent().find("td:eq(0)");
        var courseId = $td.children("input[id='courseId']").val();
        var questionCount = $td.children("input[id='questionCount']").val();
        var selectCount = $(obj).parent().next().next().children("a").text();
        var requirement = $(obj).parent().next().children("a").text();

        if($(obj).hasClass("on")){
            step4.saveRoleAndCourse(obj);
            var inner1 = '<a href="javascript:;" onmouseover="step4.editRequirement(this,\''+ courseId +'\')" class="editable edit" >' + requirement + '</a>'; //学时要求
            var inner2 = '<a href="javascript:;" onmouseover="step4.editSelectCount(this,\''+ courseId +'\','+ questionCount+')" class="editable edit" >' + selectCount + '</a>'; //必选题量

            $(obj).parent().next().html(inner1);
            $(obj).parent().next().next().html(inner2)
        }else{
            step4.removeRoleAndCourse(obj)
            var inner1 = '<a href="javascript:;"  class="editable" >' + requirement + '</a>'; //学时要求
            var inner2 = '<a href="javascript:;"   >' + selectCount + '</a>'; //必选题量
            $(obj).parent().next().html(inner1);
            $(obj).parent().next().next().html(inner2)

        }

    }

   //修改应修学时
    step4.editRequirement = function (obj,courseId,roleId) {
        var $div = $(obj).parent().prev().children("div");
        var roleId = $div.attr('data-roleId');
       if($div.hasClass("on")) {
           $.fn.editable.defaults.mode = 'popup';
           $(obj).editable({
               title:'请设置应修学时',
               disabled:false,
               validate: function (value) {
                   if (value.length > 1 && value.substring(0, 1) == 0) {
                       return '输入不合法';
                   }
                   step4.modify(courseId,roleId,value,"requirement");
               }
           })

       }
    }

   //修改必选题量
    step4.editSelectCount = function (obj,courseId,questionCount) {
        var $div=$(obj).parent().prev().prev().children("div");
        var roleId = $div.attr('data-roleId');
        if($div.hasClass("on")) {
           $.fn.editable.defaults.mode = 'popup';
            $(obj).editable({
                title:"请设置必选题量",
                validate: function (value) {
                    if (value.length > 1 && value.substring(0, 1) == 0) {
                        return '题量输入不合法';
                    }
                    if (value > questionCount) {
                        value = questionCount;
                        return "必选题量不能大于总题量";
                    }
                    step4.modify(courseId,roleId,value,"selectCount");
                },
                display: function (value) {
                    if (value > questionCount || value < 0) {
                        $(obj).text(questionCount);
                    } else {
                        $(obj).text(value);
                    }
                }
            })
        }
    }

    //修改学时或必选题量
    step4.modify=function (courseId,roleId,value,param) {
        for(var i=0;i < step4.courses.length ;i++){
            if(step4.courses[i].courseId == courseId && step4.courses[i].roleId == roleId){
                if(param=='requirement'){
                    step4.courses[i].requirement = value;
                }else{
                    step4.courses[i].selectCount = value;
                }
            }
        }
    }

    //开关处理事件
    step4.hoddleSwitch=function () {

        //全开全关
        $('.switchAll').on('click',function(){
            var index=$(this).attr('data-index');
            if('3' == step4.projectStatus && $("#select_course_role .switch_"+ index).hasClass("selected")){
              $(this).removeClass("on").addClass("on");
            }else{
              $(this).toggleClass("on");
            }
            if($(this).hasClass("on")){
                $(".switch_"+index).addClass("on");
            }else{
                $(".switch_"+index).removeClass("on");
            }
            $.each($("#select_course_role .switch_"+ index ),function (index,element) {
               if($(this).hasClass("on")){
                   step4.saveRoleAndCourse(this);
               }else{
                   step4.removeRoleAndCourse(this);
               }
            })

        })

        //回到首页
        $("#index").click(function () {
            window.location.href = appPath + "/admin";
        })

        //上一步操作
        $("#pev").click(function () {
            window.location.href=appPath + "/admin/project_create/private/selectRole?projectTypeNo="+step4.projectTypeNo+"&projectId="+step4.projectId+"&projectStatus="+step4.projectStatus+"&examPermission="+step4.examPermission;
        })
        //下一步操作
        $("#next").click(function () {
            step4.next();
        })

    }
    //下一步取值
    step4.next=function(){
       //获取角色
        step4.getRoles();
       console.log("角色是===="+step4.roles1)
        console.log("课程是===="+step4.courses)
        if(step4.roles1.length==0){
            layer.msg("请给课程分配角色");return;
        }

        var params={
            "roles":JSON.stringify(step4.roles1),
            "courses":JSON.stringify(step4.courses),
            "projectId":step4.projectId,
            "projectType":step4.projectTypeNo
        }

        //保存数据
        step4.save(params);
    }
    //发送异步保存项目信息
    step4.save=function (params) {
        $.ajax({
            dataType: 'json',
            url: appPath + '/admin/project/advanceSetting/course_role_save',
            async: false,
            type: 'post',
            data:params,
            success:function (data) {
                var code=data.code;
                if(10000==code){
                    window.location.href = appPath + "/admin/project/advanceSetting/step5?projectTypeNo="+step4.projectTypeNo+"&projectId="+step4.projectId+"&projectStatus="+step4.projectStatus+"&examPermission="+step4.examPermission;
                }
            }
        })
    }



    //缓存课程和角色
    step4.saveRoleAndCourse = function (obj) {
      var role = new Object();
      role.roleId = $(obj).attr('data-roleId');
      role.roleName = $(obj).attr('data-roleName');
       var course = step4.getRoleCourse(obj,role);
         //判重
       if(step4.repeatCourseRole(course.courseId,role.roleId,step4.courses)){
           //添加到数组
           step4.courses.push(course);
          }
        }

    //去除课程和角色
    step4.removeRoleAndCourse = function (obj) {
      var role = new Object();
      role.roleId = $(obj).attr('data-roleId');
      role.roleName = $(obj).attr('data-roleName');
       var course = step4.getRoleCourse(obj,role);
       for (var j=0;j<step4.courses.length;j++){
          if(step4.courses[j].courseId == course.courseId && step4.courses[j].roleId == role.roleId){
             step4.courses.splice(j,1);
          }
       }
    }

    //得到角色课程对象
    step4.getRoleCourse = function(obj,role){
      var course = new Object();
      var $td = $(obj).parent().parent().find("td:eq(0)");
      course.id = $td.children("input[id='id']").val();
      course.projectId = step4.projectId;
      course.courseId = $td.children("input[id='courseId']").val();
      course.courseNo = $td.children("input[id='courseNo']").val();
      course.courseName = $td.children("input[id='courseName']").val();
      course.roleId = role.roleId;
      course.roleName = role.roleName;
      course.questionCount = $td.children("input[id='questionCount']").val();
      course.classHour = $td.children("input[id='classHour']").val();
      course.requirement = $(obj).parent().next().children("a").text();
      course.selectCount = $(obj).parent().next().next().children("a").text();
      return course;
    }

    //去除重复的受训角色
    step4.repeatRole=function(id,roles){
        var flag=true;
        for (var i=0;i<roles.length;i++){
            if(roles[i].roleId==id){
                flag=false;
            }
        }
        return flag;
    }

    //去除重复的课程受训角色
    step4.repeatCourseRole=function(courseId,roleId,courses){
        var flag=true;
        for (var i=0;i<courses.length;i++){
            if(courses[i].courseId == courseId && courses[i].roleId == roleId){
                flag=false;
            }
        }
        return flag;
    }

    //获取角色数组
    step4.getRoles=function () {
       step4.roles1 = [];
       for(var i=0;i < step4.courses.length ;i++){
           if(step4.repeatRole(step4.courses[i].roleId,step4.roles1)){
               var role = new Object();
                   role.roleId = step4.courses[i].roleId
                   role.roleName = step4.courses[i].roleName;
               step4.roles1.push(role);
           }
       }
    }