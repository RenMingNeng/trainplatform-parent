var proProcess=new Object();

proProcess.projectId=$("#projectId").val();

proProcess.init=function(basePath,page,page1,page2,projectType){
  proProcess.basePath=basePath;
  proProcess.page=page;
  proProcess.page1=page1;
  proProcess.page2=page2;
  proProcess.projectType=projectType;
  if(projectType == '1' || projectType == '4' || projectType == '5' || projectType == '7'){
    // 每个学员的培训进度
    proProcess.project_train_process()
  }
  if(projectType == '2' || projectType == '4' || projectType == '6' || projectType == '7'){
    // 练习排行记录
    proProcess.project_exercise_process()
  }
  if(projectType == '3' || projectType == '5' || projectType == '6' || projectType == '7'){
    // 考试成绩列表
    proProcess.project_exam_process()
  }
  //点击事件
  proProcess.click();
}

    // 每个学员的培训进度
    proProcess.project_train_process = function(){
        var page_size=10
        var train_process_url = appPath + "/statistics/project_statistics_list";
        proProcess.page.init("select_train_form", train_process_url, "project_train", "project_train_page", 1, page_size);
        proProcess.page.goPage(1);
        proProcess.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td>' + (i+1) + '</td>';
                inner +='<td>'+item['user_name']+'</td>';
                if(item['mobile_no'] == null || item['mobile_no'] == ''){
                  inner += '<td></td>';
                }else{
                  inner += '<td>'+item['mobile_no']+'</td>';
                }
                inner += '<td>'+item['dept_name']+'</td>';
                inner += '<td>'+TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60)+'</td>';
                inner += '<td>'+TimeUtil.getHouAndMinAndSec(item['total_studytime'])+'</td>';
                inner += '<td>'+item['train_process']+'</td>';
                inner +='<td>';
                inner +='<a href=\"javascript:;\" onclick=\"proProcess.train_record(\'' + item.project_id+ '\',\'' + item.user_id+ '\');\" class=\"a a-info\">培训详情</a>';
                inner +='</td>';
                inner += '</tr>';
            }
            return inner;
        }
    };

    //练习排行情况
    proProcess.project_exercise_process=function () {
      var page_size=10
      var exercise_process_url = appPath + "/exercise/showProjectExerciseOrder";
      proProcess.page1.init("select_exercise_form", exercise_process_url, "project_exercise", "project_exercise_page", 1, page_size);
      proProcess.page1.goPage(1);
      proProcess.page1.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
              inner +='<td>'+item['rownum']+'</td>';
                inner +='<td>'+item['userName']+'</td>';
                inner +='<td>'+item['deptName']+'</td>';
                inner +='<td>'+item['totalQuestion']+'</td>';
                inner +='<td>'+item['yetAnswered']+'</td>';
                inner +='<td>'+item['correctAnswered']+'</td>';
                inner +='<td>'+item['failAnswered']+'</td>';
                inner +='<td>'+item['correctRate']+'%</td>';
                inner +='<td>'+TimeUtil.getHouAndMinAndSec(item['answerStudyTime'])+'</td>';
                inner +='<td>';
                inner +='<a href=\"javascript:;\" onclick=\"proProcess.exercise_record(\'' + item.projectId+ '\',\'' + item.userId+ '\');\" class=\"a a-publish\">练习详情</a>';
                inner +='</td>';
                inner += '</tr>';
            }
            return inner;
        }
    }

    //考试成绩列表
    proProcess.project_exam_process=function () {
      var page_size=10
      var exam_process_url = appPath + "/statistics/project_statistics_list";
      proProcess.page2.init("select_exam_form", exam_process_url, "project_exam", "project_exam_page", 1, page_size);
      proProcess.page2.goPage(1);
      proProcess.page2.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        // 组装数据
        for(var i=0; i< len; i++) {
          item = dataList[i];
          inner += '<tr >';
          inner += '<td>' + (i+1) + '</td>';
          inner +='<td>'+item['user_name']+'</td>';
          inner +='<td>'+item['dept_name']+'</td>';
          inner +='<td>'+item['exam_score']+'</td>';
          if(item['exam_status'] == '1'){
            inner +='<td><span class="text-gray">未考试</span></td>';
          }
          if(item['exam_status'] == '2'){
            inner +='<td><span class="text-green">合格</span></td>';
          }
          if(item['exam_status'] == '3'){
            inner +='<td><span class="text-red">不合格</span></td>';
          }
          inner +='<td>';
          if(item['exam_status'] == '1'){
            inner += '<a href=\"javascript:;\" class=\"a\">答题记录</a>';
          }else{
            inner += '<a href=\"javascript:;\" onclick=\"proProcess.exam_record(\'' + item.exam_no+ '\');\" class=\"a a-view\">答题记录</a>';
          }
          inner +='</td>';
          inner += '</tr>';
        }
        return inner;
      }
    }
  /* 查看培训记录*/
  proProcess.train_record = function(projectId,userId) {
    window.location.href = appPath + "/admin/projectCourseInfo/project_course_entry?projectId=" + projectId + "&userId=" + userId + "&type=1";
  }
  /* 查看练习记录*/
  proProcess.exercise_record = function(projectId,userId) {
    window.location.href = appPath + "/admin/projectCourseInfo/project_course_entry?projectId=" + projectId + "&userId=" + userId + "&type=2";
  }
  /* 查看答题记录*/
  proProcess.exam_record = function(examNo){
    common.goto_student_exam_answer_view(examNo);
  }

  //点击事件
  proProcess.click = function(){
    //搜索
    $("#train_search").click(function(){
      $("#userName_train").val($.trim($("#userName_train").val()));
      proProcess.project_train_process();
    });
    //全部
    $("#train_all").click(function(){
      $("#userName_train").val("");
      $("#roleId").val("");
      proProcess.project_train_process();
    });
    //搜索
    $("#exercise_search").click(function(){
      $("#userName_exercise").val($.trim($("#userName_exercise").val()));
      proProcess.project_exercise_process();
    });
    //全部
    $("#exercise_all").click(function(){
      $("#userName_exercise").val("");
      proProcess.project_exercise_process();
    });
    //搜索
    $("#exam_search").click(function(){
      $("#userName_exam").val($.trim($("#userName_exam").val()));
      proProcess.project_exam_process();
    });
    //全部
    $("#exam_all").click(function(){
      $("#userName_exam").val("");
      proProcess.project_exam_process();
    });

    proProcess.sort = function(type){
      if(type == '1'){
        $("#sort1").addClass("active");
        $("#sort2,#sort3").removeClass("active");
        $("#orderType").val("1");
      }
      if(type == '2'){
        $("#sort2").addClass("active");
        $("#sort1,#sort3").removeClass("active");
        $("#orderType").val("2");
      }
      if(type == '3'){
        $("#sort3").addClass("active");
        $("#sort1,#sort2").removeClass("active");
        $("#orderType").val("3");
      }
      proProcess.project_exercise_process();
    }
}













