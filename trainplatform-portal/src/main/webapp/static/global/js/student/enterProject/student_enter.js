/**
 * Created by Administrator on 2017-07-25.
 */
var student_enter = new Object();
student_enter.page;
student_enter.page_size=10;
student_enter.obj;

student_enter.trainPer = false ;
student_enter.exercisePer = false ;
student_enter.examPer = false ;

student_enter.init = function(page_,permission_){
    student_enter.page = page_;
    student_enter.permission = permission_;
    // 项目是否进行中的权限处理
    student_enter.initPermission();
    // 初始化课程列表
    student_enter.initTable();
    // 初始化事件
    student_enter.initEvent();
    // 上方统计信息
    student_enter.tongji();

    common.update_wrong_collect();

    common.goto_student_exam_paper_no($("#project_basicId").val());
}

// 项目是否进行中的权限处理
student_enter.initPermission = function(){
    debugger;
    var projectTrainInfo = student_enter.permission.projectTrainInfo;
    var projectExerciseInfo = student_enter.permission.projectExerciseInfo;
    var projectExamInfo = student_enter.permission.projectExamInfo;

    var currentDate = new Date().Format("yyyy-MM-dd HH:mm:ss");

    if("" != projectTrainInfo && undefined != projectTrainInfo) {
        var projectTrainInfo_Obj = JSON.parse(projectTrainInfo);
        //培训是否进行中
        if(projectTrainInfo_Obj.beginTime < currentDate) {
            student_enter.trainPer = true ;
        }
    }
    if("" != projectExerciseInfo && undefined != projectExerciseInfo) {
        var projectExerciseInfo_Obj = JSON.parse(projectExerciseInfo);
        //练习是否进行中
        if(projectExerciseInfo_Obj.beginTime < currentDate) {
            student_enter.exercisePer = true ;
        }
    }
    if("" != projectExamInfo && undefined != projectExamInfo) {
        var projectExamInfo_Obj = JSON.parse(projectExamInfo);
        //考试是否进行中
        if(projectExamInfo_Obj.beginTime < currentDate) {
            student_enter.examPer = true ;
        }
    }
    if(student_enter.trainPer){
        $("#online_study .exam_3").removeClass("disabled");
    }else{
        $("#online_study .exam_3").addClass("disabled");
    }


    if(student_enter.exercisePer ){
        $("#box").removeClass("permission");
        $("#enter_exercise_1").removeClass("nav");
        $("#enter_exercise_2").removeClass("nav");
        $("#enter_exercise_3").addClass("nav");
        $("#enter_exercise_4").addClass("nav");
    }else{
        $("#box").addClass("permission");
        $("#enter_exercise_3").removeClass("nav");
        $("#enter_exercise_4").removeClass("nav");
        $("#enter_exercise_1").addClass("nav");
        $("#enter_exercise_2").addClass("nav");

    }

    if(student_enter.examPer  ){
        $(".exam_1,.exam_2").removeClass("disabled");
        $("#enter_exam_1").removeClass("nav");
        $("#enter_exam_2").addClass("nav");
    }else{
        $(".exam_1,.exam_2").addClass("disabled");
        $("#enter_exam_2").removeClass("nav");
        $("#enter_exam_1").addClass("nav");
    }


};

/*培训练习项目课程信息列表*/
student_enter.initTable = function () {
    var list_url = appPath + "/student/enterProject/list";
    student_enter.page.init("project_course_form", list_url, "project_course_list", "project_course_page", 1, student_enter.page_size);
    student_enter.page.goPage(1);
    student_enter.page.list = function(dataList){
        var inner = "", item;
        student_enter.obj = dataList;
        var len = dataList.length;
        for(var i=0; i< len; i++) {
            item = dataList[i];
            inner += "<tr data-course-id='" + item['course_id'] + "'>";
            inner += '<td>' + (i+1) + '</td>';
            inner += "<td class='text-left'><span>"+item['course_name']+"</span></td>";
            if(student_enter.permission['permissionTrain']) {
                inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['class_hour']*60)+"</td>";
                inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60)+"</td>";
                inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['total_studytime'])+"</td>";
                if (student_enter.permission['trainType'] != student_enter.permission['projectType']){
                    inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['train_studytime'])+"</td>";
                }
            }
            if(student_enter.permission['permissionExercise']) {
                inner += "<td>" + item['total_question'] + "</td>";
                inner += "<td>" + item['yet_answered'] + "</td>";
                inner += "<td>" + item['correct_answered'] + "</td>";
                if(!student_enter.permission['permissionTrain']){
                    inner += "<td>" + TimeUtil.getHouAndMinAndSec(item['answer_studytime']) + "</td>";
                }
                inner += "<td>" + item['correct_rate'] + "%</td>";
            }
            if (student_enter.permission['trainExamType'] == student_enter.permission['projectType']){
                if(item['requirement_studytime']==0){
                    inner += "<td>0%</td>";
                }else if(item['train_studytime'] > item['requirement_studytime'] *60  ){
                    inner += "<td>100%</td>";
                }else{
                    inner += "<td>" + Math.round(parseFloat(item['train_studytime'])/parseFloat(item['requirement_studytime']*60)*10000)/100 + "%</td>";
                }
            }
            if(item['finish_status'] == '1'){
                inner += "<td class=\"pro_states\"><span class=\"text-gray\">已完成</span></td>";
            }else{
                inner += "<td class=\"pro_states\"><span class=\"text-red\">未完成</span></td>";
            }
            inner += "<td>";
            if(student_enter.permission['permissionTrain']) {
                if( student_enter.trainPer  ){
                    inner += "<a href='javascript:' class='a a-info' onclick=\"student_enter.enterStudy('" + item.course_id + "')\">进入学习</a>";
                }else{
                    inner += "<a href='javascript:' class='a'>进入学习</a>";
                }

            }
            if(student_enter.permission['permissionExercise'] ) {
                if( student_enter.exercisePer && item['total_question'] != 0){
                    inner += "<a href='javascript:' class='a a-view'  onclick=\"student_enter.enterQuestion('" + item.course_id + "')\"> 进入答题</a>";
                }else{
                    inner += "<a href='javascript:' class='a'>进入答题</a>";
                }

            }
            inner += "</td>";
            inner += "</tr>";
        }
        return inner;
    }
}

/*初始化事件*/
student_enter.initEvent = function(){
    //搜索
    $("#course_search").click(function(){
        student_enter.initTable();
    });
    //全部
    $("#course_all").click(function(){
        $("#course_name").val('');  //清空搜索框
        $("#btn_all").trigger('click')
    });

    //根据状态搜索
    $(".status").each(function(){
        var $this = this;
        $($this).click(function(){
            $("#finish_status").val($($this).attr("finish_status"));
            $(".status").removeClass("active");
            $($this).addClass("active");
            student_enter.initTable();
        })
    });
}


//进入学习
student_enter.enterStudy = function (courseId) {
    if(!student_enter.trainPer){
        layer.alert('不在时间范围之内！');
        return;
    }
    var projectId = $("#project_basicId").val();
    common.goto_student_study(projectId, courseId);
}


//进入答题
student_enter.enterQuestion = function (courseId) {
    var projectId = $("#project_basicId").val();
    common.goto_student_course_study(projectId, courseId);
}


/*学员在项目中的数据统计*/
student_enter.tongji = function(){
    var _this = this;
    var params = {
        'project_basicId': $("#project_basicId").val(),
        'project_userId': $("#project_userId").val()
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
student_enter.formatTime = function(time){

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
