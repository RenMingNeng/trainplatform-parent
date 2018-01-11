/**
 * 考试+练习+预览 基本信息
 */
function question(){
    var _this = this;
    _this.allTime = 0;
    _this.examTime = "";
    _this.clearInterval;
    _this.exam_no;

    _this.init = function(){
        _this.submitQuestion();
    };

    //考试时间
    _this.setInterval = function(){
        var time = parseInt(Number(question.allTime) - Number(_this.examTime));
        setCookie(_this.exam_no + "_time", _this.examTime++);

        var html = parseInt(Number(time)/60/60) + "小时" + parseInt(Number(time)/60%60) + "分钟" + parseInt(Number(time)%60) + "秒";
        $("#time").html(html);
        if(time < 1){//自动提交
            _this.save_paper($("#subPaper"));
            window.clearInterval(_this.clearInterval);
            return;
        }
    };

    //单个题提交
    _this.submitQuestion = function(){
        $("[id^=div]").on("click", ".sure", function(){
            var $this = $(this);
            var $parents = $this.parents('.questions');
            var index = $parents.attr('data-index');
            var type = $(this).parents(".option_select").attr("data-type");
            var select = '';
            var answer = $(".questions:eq(" + index + ") .answer").text();

            $(".questions:eq(" + index + ") .option.on").each(function(){
                select += $(this).attr('data-type');
            });

            if(select == ''){
                layer.msg("请选择");
                return;
            }

            //点击后显示
            $(this).parents(".option_select").find(".analy").show();
            if(select == answer){
                $(this).attr("data-true", '1');
                $("#card_list").find("li:eq(" + Number(index) + ")").addClass("success");
                layer.msg("回答正确");
            }else{
                $(this).attr("data-true", '2');
                $(this).parents(".option_select").find(".analy").trigger("click");
                $("#card_list").find("li:eq(" + Number(index) + ")").addClass("error");
                layer.msg("回答错误");
            }
            $(this).attr("data-answer", select);
            $(this).removeClass("sure").addClass("_sure unclick");
            $(".questions:eq(" + index + ") .option").addClass('unclick');

            $("#card_list").find("li:eq(" + (Number(index)+1) + ")").trigger("click");
        });
    };

    _this.submitExam = function(t){
        var message = '';
        var unselect = 0;
        $("[id^=div]").each(function(){
            if($(this).find('.option.on').hasClass("on")){
                return;
            }
            unselect++;
        });
        if(unselect > 0){
            message += '您未答题量' + unselect + '题，';
        }

        var examTime = getCookie(question.exam_no + "_time");
        var date = (Number(question.allTime) - Number(examTime));
        var newDate = parseInt(Number(date)/60/60) + "时/" + parseInt(Number(date)/60%60) + "分/" + parseInt(Number(date)%60) + '秒';
        message += '距离考试结束时间还有' + newDate + '，确定要提交试卷吗？';

        var content = _this.alertMessage(message);
        layer.open({
            type:1,
            title: false, // 不显示标题栏
            closeBtn: false,
            area: "400px",
            shade: 0.3,
            id: "layer-submit-exam-paper", // 设置id, 防止重复弹出
            resize: false,
            btn:['确定','取消'],
            btnAlign: "c",
            moveType: 1,
            content: content,
            btn1: function(index) {
                layer.close(index);
                _this.save_paper(t);
            },
            btn2: function(index) {
                layer.close(index);
            }
        });
    };

    _this.save_paper = function (t) {
        //禁止再次考试
        $(t).attr("_onclick", $(t).attr("onclick")).removeAttr("onclick");

        var detail = '';
        $("[id^=div]").find(".option_select").each(function(){
            var project_id = $(this).parents(".questions").attr("data-project");
            var question_id = $(this).parents(".questions").attr("data-question");
            var type = $(this).attr("data-type");
            var select = '';
            $(this).parents(".questions").find(".option.on").each(function(){
                select += $(this).attr('data-type');
            });

            //判断是否都已经答题完成

            if(select != ''){
                var dt = project_id + "," + question_id + "," + select;
                detail += detail == '' ? dt : ";" + dt;
            }
        });
        var url = appPath + '/student/question/save_exam.do';
        var exam_no = $("#exam_no").val();
        var param = {"detail" : detail, "exam_no" : exam_no, "examTime" : _this.examTime};
        var data = _this.ajax(url, param);
        var code = data['code'];
        if(code == '10000'){
            $(t).parent("dd").remove();
            window.clearInterval(_this.clearInterval);

            var isPassed = data['result']['isPassed'] == "1" ? "合格" : "不合格";
            var message = _this.alertMessage("您的成绩是" + data['result']['my_score'] + "&nbsp;(&nbsp;" + isPassed + "&nbsp;)");
            layer.open({
                type:1,
                title: false, // 不显示标题栏
                closeBtn: false,
                area: "400px",
                shade: 0.3,
                id: "layer-exam-paper-success", // 设置id, 防止重复弹出
                resize: false,
                btn:['确定','查看答题记录'],
                btnAlign: "c",
                moveType: 1,
                content: message,
                btn1: function(index) {
                    window.close();
                },
                btn2: function(layero) {
                    common.goto_student_exam_answer_view(exam_no);
                    window.close();
                }
            });

            //删除答题记录
            store_exam.remove(exam_no);
        }else if(code == '50518'){
            layer.alert(data['message'])
        }else{
            layer.alert('操作失败');
            $(t).attr("onclick", $(t).attr("_onclick")).removeAttr("_onclick");
        }
    };

    _this.alertMessage = function(message){
        var url = "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300;font-size: 15px;'>";
        url += message;
        url += "&nbsp;&nbsp;&nbsp;&nbsp;</div>";
        return url;
    };

    //关闭页面是调用保存练习记录
    _this.checkLeave = function(){
        var detail = '';
        $("[id^=div]").find("._sure").each(function(){
            var $parents = $(this).parents('.questions');
            var question_id = $parents.attr("data-question");
            var project_id = $parents.attr("data-project");
            var course_id = $parents.attr("data-course");
            var answer = $(this).attr("data-answer");
            var isTrue = $(this).attr("data-true");
            var dt = project_id + "," + question_id + "," + answer + "," + isTrue;
            if(course_id != '' && course_id != undefined){
                dt += ',' + course_id;
            }
            detail += detail == '' ? dt : ";" + dt;
        });
        if(detail != ''){
            var url = appPath + '/student/question/save_exercise.do';
            var param = {"detail" : detail};
            var data = _this.ajax(url, param);
        }
        //提示父页面刷新
        setCookie('messenger_parent_msg', "question_exercise_refresh");
    };

    _this.ajax = function(url, param){
        var result = '';
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function(data){
                result = data;
            }
        });
        return result;
    };
}

var question = new question();