/**
 * 用户档案里面项目列表
 */

function study_self_info(){
    var _this = this;
    var page;
    _this.page_size = 10;

    _this.init = function(page_person){
        page = page_person;
        _this.initTable();
    };

    _this.initTable = function(){
        var list_url = appPath + "/popup/studySelfList";
        page.init("study_self_info_form", list_url, "study_self_info_table", "study_self_info_page", 1, _this.page_size);
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
                inner += '<td>'+ item['class_hour'] + '</td>';
                inner += '<td>'+ TimeUtil.getHour(item['study_time']) + '</td>';
                inner += '</tr>';
            }
            return inner;
        }
    };

    _this.search = function(){
        $("#courseName").val($.trim($("#courseName").val()));
        _this.initTable();
    };

    _this.searchAll = function(){
        $("#courseName").val("");
        _this.initTable();
    };

    _this.ajax = function(url, param) {
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

var study_self_info = new study_self_info();