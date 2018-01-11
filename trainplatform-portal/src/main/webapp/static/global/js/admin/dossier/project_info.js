/**
 * 用户档案里面项目列表
 */

function project_info(){
    var _this = this;
    var page;
    _this.page_size = 10;
    _this.type;

    _this.init = function(page_person, type){
        page = page_person;
        _this.initTable();
        _this.type = type;

        $('#click_role').on("click", "span", function(){
            $('#click_role').find("span").removeClass("active");
            $(this).addClass("active");
            _this.bind_click(this);
        });
    };

    _this.initTable = function(){
        var list_url = appPath + "/admin/dossier/project/user_list";
        var project_id = $("#project_info_form").find("input[name=project_id]").val();
        var project_course_url = appPath + "/admin/dossier/person/course?project_id=" + project_id + "&user_id=";
        page.init("project_info_form", list_url, "project_info_table", "project_info_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td>'+ item['user_name'] + '</td>';
                inner += '<td>';
                inner += '<span class="text-orange">'+ item['role_name'] + '</span>';
                inner += '</td>';
                inner += '<td>'+ item['dept_name'] + '</td>';
                inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60) + '</td>';
                inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['total_studytime']) + '</td>';
                inner += '<td>'+ item['total_question'] + '</td>';
                inner += '<td>'+ item['yet_answered'] + '</td>';
                inner += '<td>'+ item['correct_rate'] + '%</td>';
                inner += '<td>'+ Number((Number(page.pageNum)-1)*Number(page.pageSize) + Number((i+1))) + '</td>';
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
                inner += '<td>';
                inner += '<a href="' + project_course_url + item['user_id'] + '" class="a a-info">详情</a> ';
                var exam_no = item['exam_no'];
                if(exam_no != undefined && exam_no != ''){
                    inner += '<a href="javascript:void(0);" onclick="common.goto_student_exam_answer_view(\'' + item['exam_no'] + '\');" class="a a-view">答题记录</a>';
                }else{
                    inner += '<a href="javascript:void(0);" onclick="layer.msg(\'没有考试记录\');" class="a">答题记录</a>';
                }
                inner += '</td>';
                inner += '</tr>';
            }

            var iObj = $(inner);
            iObj.each(function () {
                /*培训练习*/
                if(_this.type == 1 || _this.type == 2 || _this.type == 4){
                    $(this).find("td").eq(10).html("\\");
                    $(this).find("td").eq(11).html("\\");
                }
                /*培训考试*/
                if(_this.type == 1 || _this.type == 3 || _this.type == 5){
                    $(this).find("td").eq(6).html("\\");
                    $(this).find("td").eq(7).html("\\");
                    $(this).find("td").eq(8).html("\\");
                }
                /*练习考试*/
                if(_this.type == 2 || _this.type == 3 || _this.type == 6){
                    $(this).find("td").eq(4).html("\\");
                }
                /*考试*/
                if(_this.type == 3){
                    $(this).find("td").eq(5).html("\\");
                    $(this).find("td").eq(9).html("\\");
                }
            });

            return iObj;
        };
    };

    _this.search = function(){
        _this.initTable();
    };

    _this.searchAll = function(){
        $("#project_info_form").find("input[name=user_name]").val("");
        $("#project_info_form").find("input[name=dept_name]").val("");
        $("#project_info_form").find("input[name=role_name]").val("");
        _this.initTable();
    };

    _this.bind_click = function(t){
        var value = $(t).attr("data-value");
        $("#project_info_form").find("input[name=role_id]").val(value);
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

var project_info = new project_info();