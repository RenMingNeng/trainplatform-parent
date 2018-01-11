/**
 * 用户档案里面项目列表
 */

function person_info(){
    var containTrainType = $("#containTrainType").val();
    var containExerciseType = $("#containExerciseType").val();
    var containExamType = $("#containExamType").val();
    var _this = this;
    var page;
    _this.page_size = 10;

    _this.init = function(page_person){
        page = page_person;
        _this.initTable();

        $('#click-train_status').on("click", "span", function(){
            $('#click-train_status').find("span").removeClass("active");
            $(this).addClass("active");
            _this.bind_click(this);
        });
    };

    _this.initTable = function(){
        var list_url = appPath + "/admin/dossier/person/project_list";
        var user_id = $("#person_info_form").find("input[name=user_id]").val();
        var userType = $("#person_info_form").find("input[name=userType]").val();
        var project_course_url = appPath + "/admin/dossier/person/course?user_id=" + user_id + "&project_id=";

        page.init("person_info_form", list_url, "person_info_table", "person_info_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item, projectType;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                projectType = item['project_type'];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td>'+ item['project_name'] + '</td>';
                inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + '</td>';
                if(~containTrainType.indexOf(projectType)){
                    inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60) + '</td>';
                }else{
                    inner += '<td>\\</td>';
                }
                if(~containTrainType.indexOf(projectType) || ~containExerciseType.indexOf(projectType)){
                    inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['total_studytime']) + '</td>';
                    var train_status = item['train_status'];
                    if(train_status == 1){
                        inner += '<td>未完成</td>';
                    }else{
                        inner += '<td>已完成</td>';
                    }
                }else{
                    inner += '<td>\\</td>';
                    inner += '<td>\\</td>';
                }
                if(~containExerciseType.indexOf(projectType)){
                    inner += '<td>'+ item['total_question'] + '</td>';
                    inner += '<td>'+ item['yet_answered'] + '</td>';
                    inner += '<td>'+ item['correct_answered'] + '</td>';
                    inner += '<td>'+ item['correct_rate'] + '%</td>';
                    inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['answer_studytime']) + '</td>';
                }else{
                    inner += '<td>\\</td>';
                    inner += '<td>\\</td>';
                    inner += '<td>\\</td>';
                    inner += '<td>\\</td>';
                    inner += '<td>\\</td>';
                }
                if(~containExamType.indexOf(projectType)){
                    var exam_score = item['exam_score'];
                    if(exam_score == '' || exam_score == undefined){
                        exam_score = '/';
                    }
                    inner += '<td>'+ exam_score + '</td>';
                    var exam_status = item['exam_status'];
                    if(exam_status == 1){//考试状态:1未考试2合格3不合格
                        inner += '<td>未考试</td>';
                    }else if(exam_status == 2){
                        inner += '<td>合格</td>';
                    }else{
                        inner += '<td>不合格</td>';
                    }
                }else{
                    inner += '<td>\\</td>';
                    inner += '<td>\\</td>';
                }
                inner += '<td>';
                inner += '<a href="' + project_course_url + item['project_id'] + '" class="a a-info">详情</a> ';
                var exam_no = item['exam_no'];
                if(exam_no != '' && exam_no != undefined){
                    inner += '<a href="javascript:void(0);" onclick="common.goto_student_exam_answer_view(\'' + item['exam_no'] + '\');" class="a a-view">答题记录</a>';
                }else{
                    inner += '<a href="javascript:void(0);" onclick="layer.msg(\'没有考试记录\');" class="a">答题记录</a>';
                }
                inner += '</td>';
                inner += '</tr>';
            }
            return inner;
        }
    };

    _this.search = function(){
        _this.initTable();
    };

    _this.searchAll = function(){
        $("#person_info_form").find("input[name=project_name]").val("");
        $("#person_info_form").find("input[name=project_start_time]").val("");
        $("#person_info_form").find("input[name=project_end_time]").val("");
        _this.initTable();
    };

    _this.bind_click = function(t){
        var value = $(t).attr("data-value");
        $("#person_info_form").find("input[name=train_status]").val(value);
        _this.initTable();
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

var person_info = new person_info();