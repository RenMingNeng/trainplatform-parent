/**
 * 试题异步
 */

function question_exercise_ajax(){
    var _this = this;
    _this.url;
    _this.project_id;
    _this.course_id = '';

    _this.initMethod = function(body_id, url, project_id, course_id){
        _this.body_id = body_id;
        _this.url = url;
        _this.project_id = project_id;
        if(course_id){
            _this.course_id = course_id;
        }
        _this.intTable();
    };

    _this.intTable = function () {
        var data = _this.ajax(_this.url);
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
            $("#" + _this.body_id).find('[id^=div]').remove().end().append(html);
        }
    };

    //刷新
    _this.refresh = function () {
        question_exercise_ajax.initMethod(_this.body_id, _this.url, _this.project_id, _this.course_id);
        $.getScript(appPath + '/static/global/js/student/question/question_public.js');
    };

    _this.single_many_html = function (question, i) {
        var chrType = question.chrType;
        var html = '';
        html += '';
        html += '<div id="div' + (i + 1) + '" class="questions" data-course="' + _this.course_id + '" data-question="' + question.intId + '" data-project="' + _this.project_id + '" data-index="' + i + '">';
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
                html += '<span></span>';
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
        html += '<button type="button" class="btn btn-info sure">确定</button>&nbsp;&nbsp;';
        html += '<button type="button" class="btn btn-mark">标记</button>';
        html += '</div>';
        html += '<div class="pull-right">';
        html += '<a href="javascript:;" class="remove_error" style="display: none;"><i class="fa fa-remove"></i> 移除错题</a>&nbsp;&nbsp;';
        var chrIsCollect = question.chrIsCollect;
        if(chrIsCollect == true || chrIsCollect == 'true'){
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star on"></i> <span>取消收藏</span></a>&nbsp;&nbsp;';
        }else{
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star-o" ></i> <span>收藏试题</span></a>&nbsp;&nbsp;';
        }
        html += '<a href="javascript:;" class="analy" style="display: none;">展开解析 <i class="fa fa-angle-down"></i></a>';
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
        html += '<div id="div' + (i + 1) + '" class="questions" data-course="' + _this.course_id + '" data-question="' + question.intId + '" data-project="' + _this.project_id + '" data-index="' + i + '">';
        html += '<div class="subject"><div class="question_content clearfix">';
        html += '<span class="index">' + (i + 1) + '</span><span class="title_bg">（判断题）</span>' + '<div>' + question.varQuestionsContent + '</div></div>';
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
        html += '<button type="button" class="btn btn-info sure">确定</button>&nbsp;&nbsp;';
        html += '<button type="button" class="btn btn-mark">标记</button>';
        html += '</div>';
        html += '<div class="pull-right">';
        html += '<a href="javascript:;" class="remove_error" style="display: none;"><i class="fa fa-remove"></i> 移除错题</a>&nbsp;&nbsp;';
        var chrIsCollect = question.chrIsCollect;
        if(chrIsCollect == true || chrIsCollect == 'true'){
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star on"></i> <span>取消收藏</span></a>&nbsp;&nbsp;';
        }else{
            html += '<a href="javascript:;" class="collect"><i class="fa fa-star-o" ></i> <span>收藏试题</span></a>&nbsp;&nbsp;';
        }
        html += '<a href="javascript:;" class="analy" style="display: none;">展开解析 <i class="fa fa-angle-down"></i></a>';
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

var question_exercise_ajax = new question_exercise_ajax();