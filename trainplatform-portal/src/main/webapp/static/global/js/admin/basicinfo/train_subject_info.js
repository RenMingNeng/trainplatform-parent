function trainSubjectInfo() {
    var _this = this;
    var page;
    _this.index = -1;
    var items;
    _this.init = function (page_person) {
        page = page_person;
        _this.initTable();

    };
    _this.initTable = function () {
        var list_url = appPath + "/admin/trainSubject/courseList";
        page.init("select_course_form", list_url, "select_course_list", "select_course_page", 1, 10);
        page.goPage(1);
        page.list = function (dataList) {
            $("#all").attr("checked", false);
            var len = dataList.length;
            items = dataList;
            var inner = "", item;
            for (var i = 0; i < len; i++) {
                // 组装数据
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i + 1) + '</td>';
                inner += '<td><input type="checkbox" name="ckbox"  value="' + item.varId + '" /></td>';
                inner += '<td>';
                inner += '<span>' + item.course_no + '</span>';
                inner += '</td>';
                inner += '<td>' + item.course_name + '</td>';
                inner += '<td>' + item.class_hour + '</td>';
                inner += '<td><a href="javascript:;" onclick=\"trainSubjectInfo.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
                inner += ' <a href="javascript:;" onclick=\"trainSubjectInfo.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a></td>';
                inner += '</tr>';
            }
            return inner;
        }
    };
    $("#btn_search_course").click(function () {
        trainSubjectInfo.initTable();
    });
    $("#btn_search_all_course").click(function () {
        $("#courseName").val("");
        trainSubjectInfo.initTable();
    });
    //课程预览
    _this.course_view = function (course_id) {
        common.goto_student_course_view(course_id);
    }

    //试题预览
    _this.question_view = function (course_id) {
        common.goto_student_course_question_view(course_id);
    }
}


var trainSubjectInfo = new trainSubjectInfo();