/**
 * Created by Administrator on 2017-07-25.
 */
var project_record = new Object();

project_record.init = function(basePath, page, projectType, permissionTrain, permissionExercise, containTrainAndExercise){
  project_record.basePath = basePath;
  project_record.page = page;
  project_record.projectType = projectType;
  project_record.permissionTrain = permissionTrain;
  project_record.permissionExercise = permissionExercise;
  project_record.containTrainAndExercise = containTrainAndExercise;
  //培训练习项目课程信息列表
  project_record.project_record_info();
  // 初始化
  project_record.click();
}

/*培训练习项目课程信息列表*/
project_record.project_record_info = function() {
  var page_size=10;
  var project_record_url = appPath + "/student/enterProject/list";
  project_record.page.init("project_record_form", project_record_url, "project_record", "project_course_page", 1, page_size);
  project_record.page.goPage(1);
  project_record.page.list = function(dataList){
    var len = dataList.length;
    var inner = "", item, num;
    // 组装数据
    for(var i=0; i< len; i++) {
      item = dataList[i];
      num = i+1;
      inner += "<tr>";
      inner += "<td>"+ num +"</td>";
      inner += "<td><span class=\"text-orange\">"+item['course_name']+"</span></td>";
      if (project_record.permissionTrain){
        inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['class_hour']*60)+"</td>";
        inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60)+"</td>";
        if(project_record.isContains(project_record.containTrainAndExercise,project_record.projectType)){
          inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['total_studytime'])+"</td>";
        }
        inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['train_studytime'])+"</td>";
      }
      if (project_record.permissionExercise){
        inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['answer_studytime'])+"</td>";
        inner += "<td>"+item['total_question']+"</td>";
        inner += "<td>"+item['yet_answered']+"</td>";
        inner += "<td>"+item['correct_answered']+"</td>";
        inner += "<td>"+item['correct_rate']+"%</td>";
      }
      if(item['finish_status'] == '1'){
        inner += "<td><span class=\"text-gray\">已完成</span></td>";
      }else{
        inner += "<td><span class=\"text-red\">未完成</span></td>";
      }
      inner += "</tr>";
    }
    return inner;
  }
}

/*点击事件*/
project_record.click = function(){
    //搜索
    $("#course_search").click(function(){
      $("#name_search").val($.trim($("#name_search").val()));
      project_record.project_record_info();
    });
    $("#status_1").click(function(){
      $(this).addClass("active");
      $("#status_2,#status_3").removeClass("active");
      $("#finish_status").val("");
      project_record.project_record_info();
    });
    $("#status_2").click(function(){
      $(this).addClass("active");
      $("#status_1,#status_3").removeClass("active");
      $("#finish_status").val("-1");
      project_record.project_record_info();
    });
    $("#status_3").click(function(){
      $(this).addClass("active");
      $("#status_1,#status_2").removeClass("active");
      $("#finish_status").val("1");
      project_record.project_record_info();
  });

 /* //练习记录
  $("#sort2").click(function(){
    window.location.href= project_record.basePath + "studentProjectRecord/exercise_order?projectId=" + $("#project_basicId").val() + "&userId=" + $("#project_userId").val() + "&projectTypeName=" + $("#projectType").val();
  });

  //考试记录
  $("#sort1").click(function(){
    window.location.href= project_record.basePath + "studentProjectRecord/exam_score?projectId=" + $("#project_basicId").val() + "&userId=" + $("#project_userId").val() + "&projectTypeName=" + $("#projectType").val();
  });*/

  //全部
  $("#course_all").click(function(){
    $("#name_search").val("");  //清空搜索框
    $("#status_1").trigger("click");
  });
}


/*学员在项目中的数据统计*/
project_record.tongji = function(project_basicId,project_userId){
    var _this =  this;
    var params = {
        'project_basicId': project_basicId,
        'project_userId': project_userId
    }
    $.ajax({
        url: appPath + "/statistics/studentStatistics",
        data: params,
        async: false,
        type: "post",
        dataType:"json",
        success:function(data){
            if(data.result){
                for( var attr in data.result ) {
                    if(attr == 'correctRate'){
                        var correctRateT = data.result[attr] + "%";
                        $('#' + attr).html(  '<span class="time" data-time="'+ correctRateT  +'">' + correctRateT  + '</span>' );
                    }else if(attr == 'requirementStudyTime'){
                        var time = data.result[attr] * 60;
                        var html = _this.formatTime(time);
                        var requirementStudyTimeT = TimeUtil.getHouAndMinAndSec(time);

                        $('#' + attr).html( '<span  class="time" data-time="'+ requirementStudyTimeT +'">' + html + '</span>' );
                    }else if( attr == 'totalStudyTime' || attr == 'trainStudyTime' || attr == 'answerStudyTime'){
                        var StudyTime = data.result[attr];
                        var html = _this.formatTime(StudyTime);
                        var StudyTimeT = TimeUtil.getHouAndMinAndSec(StudyTime);
                        $('#' + attr).html( '<span  class="time" data-time="'+ StudyTimeT +'">' +  html  + '</span>' );
                    }else if( attr === 'totalQuestion' || attr === 'yetAnswered' || attr === 'correctAnswered'  ){
                        $('#' + attr).html(  '<span class="time" data-time="'+ data.result[attr]  +'">' + data.result[attr]  + '</span>' );
                    }
                }
            }
        }
    })
}
/*格式时间*/
project_record.formatTime = function(time){

    var requirementStudyTimeT = ''; //获取学时总秒数
    if( time <= 0 ){
        return requirementStudyTimeT += '<b>' + 0  + '</b>秒';
    }
    var s1 = parseInt( time % 3600 );   //获取不足小时的秒数
    var h = parseInt( time / 3600 );    //获取小时数
    var m = parseInt(  s1 / 60 );       //获取分钟数
    var s = parseInt( s1 % 60 );        //获取秒数
    if( h > 0){
        requirementStudyTimeT += '<b>' + h  + '</b>时';
    }
    if( m > 0 ){
        requirementStudyTimeT += '<b>' + m  + '</b>分';
    }
    if( s > 0 ){
        requirementStudyTimeT += '<b>' + s  + '</b>秒';
    }
    return requirementStudyTimeT;
}

project_record.index = function(){
  window.location.href = project_record.basePath + "/student";
}

//一个字符串是否包含另一个字符串
project_record.isContains=function(str,substr){
  return new RegExp(substr).test(str);
}

$(function(){
    $('.tooltip').on('mouseover','span',function(){
        var $this = $(this);
        var $p = $this.parents('.form-group');
        var $tooltip_inner = $p.find('.tooltip_inner');
        var info = $p.data('info');

        if( $tooltip_inner.length !== 0 ){
            $tooltip_inner.stop().fadeIn();
        }else{
            $tooltip_inner = $('<div class="tooltip_inner">' + info  + '<span></span></div>');
            $p.append($tooltip_inner).stop().fadeIn();
        }
    });
    $('.tooltip').on('mouseout','span',function(){
        var $this = $(this);
        var $p = $this.parents('.form-group');
        var $tooltip_inner = $p.find('.tooltip_inner');
        $tooltip_inner.stop().fadeOut();
    });
})