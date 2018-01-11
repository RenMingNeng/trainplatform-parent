/**
 * 用户档案里面项目列表
 */

function project_course_info(){
    var _this = this;
    var page;
    _this.page_size = 10;
    _this.type;

    _this.init = function(page_person, type){
        page = page_person;
        _this.initTable();
        _this.type = type;
    };

    _this.initTable = function(){
        var list_url = appPath + "/admin/dossier/person/course_list";
        page.init("project_course_info_form", list_url, "project_course_info_table", "project_course_info_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td>'+ item['course_name'] + '</td>';
                inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60) + '</td>';
                inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['total_studytime']) + '</td>';
                inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['answer_studytime']) + '</td>';
                inner += '<td>'+ TimeUtil.getHouAndMinAndSec(item['train_studytime']) + '</td>';
                inner += '<td>'+ item['total_question'] + '</td>';
                inner += '<td>'+ item['yet_answered'] + '</td>';
                inner += '<td>'+ item['correct_answered'] + '</td>';
                inner += '<td>'+ item['correct_rate'] + '%</td>';
                var finish_status = item['finish_status'];
                if(finish_status == -1){
                    inner += '<td>未完成</td>';
                }else{
                    inner += '<td>完成</td>';
                }
                inner += '</tr>';
            }

            var iObj = $(inner);
            iObj.each(function () {
                /*培训*/
                if(_this.type == 1 || _this.type == 5){
                    $(this).find("td").eq(4).html("\\");
                    $(this).find("td").eq(6).html("\\");
                    $(this).find("td").eq(7).html("\\");
                    $(this).find("td").eq(8).html("\\");
                    $(this).find("td").eq(9).html("\\");
                }
                /*练习*/
                if(_this.type == 2 || _this.type == 6){
                    $(this).find("td").eq(2).html("\\");
                    $(this).find("td").eq(5).html("\\");
                    $(this).find("td").eq(10).html("\\");
                }
                /*考试*/
                if(_this.type == 3){
                    $(this).find("td").eq(2).html("\\");
                    $(this).find("td").eq(3).html("\\");
                    $(this).find("td").eq(4).html("\\");
                    $(this).find("td").eq(5).html("\\");
                    $(this).find("td").eq(6).html("\\");
                    $(this).find("td").eq(7).html("\\");
                    $(this).find("td").eq(8).html("\\");
                    $(this).find("td").eq(9).html("\\");
                    $(this).find("td").eq(10).html("\\");
                }
            });

            return iObj;
        }
    };

    _this.search = function(){
        _this.initTable();
    };

    _this.searchAll = function(){
        $("#project_course_info_form").find("input[name=course_name]").val("");
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

var project_course_info = new project_course_info();