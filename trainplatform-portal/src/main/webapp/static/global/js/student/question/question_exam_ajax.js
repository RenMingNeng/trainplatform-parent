/**
 * 试题异步
 */

function question_exam_ajax(){
    var _this = this;
    _this.body_id;
    this.project_id;

    _this.initMethod = function(body_id, exam_no, project_id){
        _this.body_id = body_id;
        _this.project_id = project_id;
        _this.intTable(exam_no);
    };

    _this.intTable = function (exam_no) {
        var url = appPath + "/student/question/exam_ajax";
        var param = {"exam_no" : exam_no};
        var data = _this.ajax(url, param);
        var code = data['code'];
        if(code == '10000'){
            var questions = data['result'];
            var html = '';
            if(questions.length < 1){
                html = '<div><div class="no-content"></div></div>';
            }

            for(var i=0; i<questions.length; i++){
               var question = questions[i];
                var chrType = question.chrType;
                if(chrType == '01' || chrType == '02'){
                    html += _this.single_many_html(question, i);
                }else if(chrType == '03'){
                    html += _this.judge_html(question, i);
                }
            }

            $("#" + _this.body_id).append(html);
        }
    };

    _this.single_many_html = function (question, i) {
        var chrType = question.chrType;
        var html = '';
        html += '';
        html += '<div id="div' + (i + 1) + '" class="questions" data-question="' + question.intId + '" data-project="' + _this.project_id + '">';
        html += '<div class="subject"><div class="question_content clearfix">';
        html += '<span class="index">' + (i + 1) + '</span><span class="title_bg">（';
        if(chrType == '02'){
            html += '多选题';
        }else{
            html += '单选题';
        }
        html += '）</span><div>'+ question.varQuestionsContent + '</div></div>';
        html += '<div class="show_img clearfix">';
        var questionTitleFiles = question.questionTitleFiles;
        if(questionTitleFiles != undefined){
            for(var i=0; i<questionTitleFiles.length; i++){
                var file = questionTitleFiles[i];
                if(file.mimeType == 'img'){
                    html += '<img class="lazy" data-original="' + file.mimeUrl + '" alt="">';
                }
            }
        }
        html += ' </div>';
        html += '</div>';
        html += '<div class="options">';
        var options = question.options;
        if(options != undefined){
            for(var i=0; i<options.length; i++){
                var q = options[i];
                html += '<div>';
                if(chrType == '02'){
                    html += '<div class="option checkbox" data-type="' + q.oType + '">';
                }else{
                    html += '<div class="option" data-type="' + q.oType + '">';
                }
                html += '   <span></span>';
                html += '<div><span>' + q.oType + '、</span><div>' + q.optionContent + '</div></div>';
                html += '</div>';
                html += '<div class="show_img clearfix">';
                if(q.mimeType == 'img'){
                    html += '<img class="lazy" data-original="' + q.mimeUrl + '" alt="">';
                }
                html += '</div>';
                html += '</div>';
            }
        }
        html += '</div>';
        html += '<div class="option_select clearfix" data-type="' + question.chrType + '">';
        html += '<div class="pull-left">';
        html += '<button type="button" class="btn btn-mark">标记</button>';
        html += '</div>';
        html += '<div class="pull-right">';
        var chrIsCollect = question.chrIsCollect;
        if(chrIsCollect == true || chrIsCollect == 'true'){
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star on"></i> <span>取消试题</span></a>';
        }else{
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> <span>收藏试题</span></a>';
        }
        html += '</div>';
        html += '</div>';
        html += '</div>';
        return html;
    };

    _this.judge_html = function (question, i) {
        var html = '';
        html += '';
        html += '<div id="div' + (i + 1) + '" class="questions" data-question="' + question.intId + '" data-project="' + _this.project_id + '">';
        html += '<div class="subject"><div class="question_content clearfix">';
        html += '<span class="index">' + (i + 1) + '</span><span class="title_bg">（判断题）</span><div>' + question.varQuestionsContent + '</div></div>';
        html += '<div class="show_img clearfix">';
        var questionTitleFiles = question.questionTitleFiles;
        if(questionTitleFiles != undefined){
            for(var i=0; i<questionTitleFiles.length; i++){
                var file = questionTitleFiles[i];
                if(file.mimeType == 'img'){
                    html += '<img class="lazy" data-original="' + file.mimeUrl + '" alt="">';
                }
            }
        }
        html += '</div>';
        html += '</div>';
        html += '<div class="options">';
        html += '<div>';
        html += '<div class="option" data-type="A">';
        html += '<span></span>';
        html += '<div><span>A、</span><div>正确</div></div>';
        html += '</div>';
        html += '</div>';
        html += '<div>';
        html += '<div class="option" data-type="B">';
        html += '<span></span>';
        html += '<div><span>B、</span><div>错误</div></div>';
        html += '</div>';
        html += '</div>';
        html += '</div>';
        html += '<div class="option_select clearfix" data-type="' + question.chrType + '">';
        html += '<div class="pull-left">';
        html += '<button type="button" class="btn btn-mark">标记</button>';
        html += '</div>';
        html += '<div class="pull-right">';
        var chrIsCollect = question.chrIsCollect;
        if(chrIsCollect == true || chrIsCollect == 'true'){
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star on"></i> <span>收藏试题</span></a>';
        }else{
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> <span>收藏试题</span></a>';
        }
        html += '</div>';
        html += '</div>';
        html += '</div>';
        return html;
    };

    _this.ajax = function(url, param, type) {
        var result;
        $.ajax({
            url : url,
            async : false,
            type : 'post',
            data : param,
            success : function(data) {
                result = data;
            }
        });
        return result;
    };
}

var question_exam_ajax = new question_exam_ajax();