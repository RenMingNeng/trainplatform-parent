var exerciseInfo=new Object();

exerciseInfo.init=function(basePath,page){
  exerciseInfo.basePath=basePath;
  exerciseInfo.page=page;

    // 练习详情
  exerciseInfo.project_exercise_info();
  //点击事件
  exerciseInfo.click();
}

    // 查看学员练习详情
    exerciseInfo.project_exercise_info = function(){
        var page_size=10
        var exercise_info_url = appPath + "/admin/projectCourseInfo/project_course_list";
     exerciseInfo.page.init("select_exerciseInfo_form", exercise_info_url, "exercise_Info", "exercise_Info_page", 1, page_size);
     exerciseInfo.page.goPage(1);
     exerciseInfo.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
              item = dataList[i];
              inner += '<tr >';
              inner += '<td>' + (i+1) + '</td>';
              inner +='<td>'+item['course_name']+'</td>';
              inner += '<td>'+item['total_question']+'</td>';
              inner += '<td>'+item['yet_answered']+'</td>';
              inner += '<td>'+item['correct_answered']+'</td>';
              inner += '<td>'+item['correct_rate']+'%</td>';
              inner += '<td>'+TimeUtil.getHouAndMinAndSec(item['answer_studytime'])+'</td>';
              inner += '</tr>';
            }
            return inner;
        }
    };

   //点击事件
   exerciseInfo.click = function(){
     //搜索
     $("#exercise_search").click(function(){
       $("#courseName").val($.trim($("#courseName").val()));
       exerciseInfo.project_exercise_info();
     });
     //全部
     $("#exercise_all").click(function(){
       $("#courseName").val("");
       exerciseInfo.project_exercise_info();
     });
   }













