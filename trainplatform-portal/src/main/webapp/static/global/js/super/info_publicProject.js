/**
 * Created by Administrator on 2017-07-31.
 */
function InfoPublicProject() {
    var _this = this;
    var page1,page2;
    var projectStatus = $("#projectStatus").val();  // 项目状态
    var projectTypeNo = $("#projectTypeNo").val();  // 项目类型编号
    // init
    _this.courses  = null;
    _this.page_size = 10;
    _this.index = -1;
    _this.init = function(_page1,_page2) {
        page1 = _page1; page2 = _page2;
        _this.initEvent();
        _this.initTable_course(1);
        _this.initTable_company();
        _this.init_btn();
    }

    _this.initEvent = function() {

        // 搜索课程
        $("#btn_search_course").click(function() {
            _this.initTable_course();
        });

        // 全部课程
        $("#btn_search_all_course").click(function() {
            $("#course_name").val("");
            _this.initTable_course();
        });

        // 搜索单位
        $("#btn_company_search").click(function() {
            _this.initTable_company();
        });

        // 全部单位
        $("#btn_company_all").click(function() {
            $("#company_name").val("");
            _this.initTable_company();
        });

    };

    //课程列表
    _this.initTable_course = function() {
        var list_url = appPath + "/super/project_create/public/course_list";
        page1.init("course_form", list_url, "course_list", "course_page", 1, _this.page_size);
        page1.goPage(1);
        page1.list = function(dataList){
        var inner = "", item;
            _this.obj = dataList;
            var len = dataList.length;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td>'+ item['course_name'] +'</td>';
                inner += '<td>'+ item['course_no'] +'</td>';
                if (_this.isContains('1457', projectTypeNo)) {
                    inner += '<td>'+item['class_hour']+'</td>';
                    inner += '<td>'+ item['requirement'] + '</td>';
                }
                if (_this.isContains('234567', projectTypeNo)) {
                    inner += '<td>'+item['question_count']+'</td>';
                }
                if (_this.isContains('3567', projectTypeNo)) {
                    inner += '<td>'+ item['select_count'] + '</td>';
                }
                inner += '<td>';
                inner += '<a href="javascript:;"  onclick=\"infoPublicProject.course_view(\'' + item.course_id + '\');\"   class="a a-info">课程预览</a>';
                inner += ' <a href="javascript:;" onclick=\"infoPublicProject.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
                inner +='</td>';
                inner += '</tr>';
            }
        return inner;
        }
    };

    //单位列表
    _this.initTable_company = function() {
        var list_url = appPath + "/super/project_create/public/company_list";
        page2.init("company_form", list_url, "company_list", "company_page", 1, _this.page_size);
        page2.goPage(1);
        page2.list = function(dataList){
        var inner = "", item;
            _this.obj = dataList;
            var len = dataList.length;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td class="pro_num">' + (i+1) + '</td>';
                inner += '<td><input type=\"checkbox\" name="companyBox" data-id="' + item['intId'] + '"></td>';
                inner += '<td class="pro_name">'+ item['varName'] + '</td>';
                inner += '</tr>';
            }
        return inner;
        }

    };

    //判断字符串是否包含某字符
    _this.isContains = function (str, substr) {
        return new RegExp(substr).test(str);
    }

    //按钮初始化
    _this.init_btn = function () {
        $("#btn_select_course").addClass("unclick");
        $("#btn_delete_batch_course").addClass("unclick");
        $("#btn_select_company").addClass("unclick");
        $("#btn_delete_batch_company").addClass("unclick");
    }

    //课程预览
    _this.course_view = function(course_id){
        common.goto_student_course_view(course_id);
    }

    //试题预览
    _this.question_view = function(course_id){
        common.goto_student_course_question_view(course_id);
    }

}

var infoPublicProject = new InfoPublicProject();