/**
 * 试题异步
 */

function question_view_ajax(){
    var _this = this;
    _this.body_id;

    _this.initMethod = function(body_id, course_id){
        _this.body_id = body_id;
        _this.intTable(course_id);
    };

    _this.intTable = function (course_id) {
        var url = appPath + "/popup/course_question_view_ajax";
        var param = {"course_id" : course_id};
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
        html += '<div id="div' + (i + 1) + '" class="questions"  data-question="' + question.intId + '"  data-index="' + i + '">';
        html += '<div class="subject"><div class="question_content clearfix">';
        html += '<span class="index">' + (i + 1) + '</span><span class="title_bg">（';
        if(chrType == '02'){
            html += '多选题';
        }else{
            html += '单选题';
        }
        html += '）</span><div>' + question.varQuestionsContent + '</div></div>';
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
        html += '<div class="pull-left"></div>';
        html += '<div class="pull-right">';
        html += '<a href="javascript:;" class="analy">展开解析 <i class="fa fa-angle-down"></i></a>';
        html += '</div>';
        html += '</div>';
        html += '<div class="subject_foot">';
        html += '<div><span class="label">正确答案：</span><i class="answer">' + question.varAnswer + '</i></div>';
        html += '<div class="clearfix">';
        html += '<span class="label">试题解析：</span>';
        html += '<div class="pull-left analysis">';
        var analysisContent = question.analysisContent;
        if(analysisContent == '' || analysisContent == undefined){
            html += '暂无解析';
        }else{
            html += question.analysisContent;
            html += '<div class="show_img clearfix">';
            var analysisFiles = question.analysisFiles;
            if(analysisFiles == undefined && analysisFiles.length > 0){
                for(var i=0; i<analysisFiles.length; i++){
                    var file = analysisFiles[i];
                    if(q.mimeType == 'img'){
                        html += '<img class="lazy" data-original="' + q.mimeUrl + '" alt="">';
                    }
                }
            }
            html += '</div>';
        }
        html += '</div>';
        html += '</div>';
        html += '</div>';
        html += '</div>';
        return html;
    };

    _this.judge_html = function (question, i) {
        var chrType = question.chrType;
        var html = '';
        html += '';
        html += '<div id="div' + (i + 1) + '" class="questions"  data-question="' + question.intId + '" data-index="' + i + '">';
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
        html += '<div><span>A、</span><div>正确</div></div>';
        html += '</div>';
        html += '</div>';
        html += '<div>';
        html += '<div class="option" data-type="B">';
        html += '<div><span>B、</span><div>错误</div></div>';
        html += '</div>';
        html += '</div>';
        html += '</div>';

        html += '<div class="option_select clearfix" data-type="' + question.chrType + '">';
        html += '<div class="pull-left"></div>';
        html += '<div class="pull-right">';
        html += '<a href="javascript:;" class="analy">展开解析 <i class="fa fa-angle-down"></i></a>';
        html += '</div>';
        html += '</div>';
        html += '<div class="subject_foot">';
        html += '<div><span class="label">正确答案：</span><i class="answer">' + question.varAnswer + '</i></div>';
        html += '<div class="clearfix">';
        html += '<span class="label">试题解析：</span>';
        html += '<div class="pull-left analysis">';
        var analysisContent = question.analysisContent;
        if(analysisContent == '' || analysisContent == undefined){
            html += '暂无解析';
        }else{
            html += question.analysisContent;
            html += '<div class="show_img clearfix">';
            var analysisFiles = question.analysisFiles;
            if(analysisFiles == undefined && analysisFiles.length > 0){
                for(var i=0; i<analysisFiles.length; i++){
                    var file = analysisFiles[i];
                    if(q.mimeType == 'img'){
                        html += '<img class="lazy" data-original="' + q.mimeUrl + '" alt="">';
                    }
                }
            }
            html += '</div>';
        }
        html += '</div>';
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

var question_view_ajax = new question_view_ajax();