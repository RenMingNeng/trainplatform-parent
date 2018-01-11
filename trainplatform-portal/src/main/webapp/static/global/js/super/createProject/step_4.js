/**
 * 创建公开型项目第四步
 */
function Step_4() {
    var _this = this;
    var containTrainType, containExamType, containExerciseType;
    var projectId = $("#id").val();                 // 项目编号
    var projectTypeNo = $("#projectTypeNo").val();  // 项目类型编号
    var projectStatus = $("#projectStatus").val();  // 项目状态
    _this.init = function () {
        //含有培训的
        containTrainType = options.containTrainType;
        //含有考试的
        containExamType = options.containExamType;
        //含有练习的
        containExerciseType = options.containExerciseType;

        _this.initEvent();
        _this.initTable_course();
    }

    _this.initEvent = function () {
        // 回到首页
        $("#index").click(function() {
            window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
        });
        // 点击上一步
        $("#step_3").click(function () {
            var param = "?projectId=" + projectId + "&projectTypeNo=" + projectTypeNo + "&step=" + '3';
            window.location.href = appPath + "/super/project_create/public/create_project" + param;
        });
        // 点击下一步
        $("#step_5").click(function () {
            var param = "?projectId=" + projectId + "&projectTypeNo=" + projectTypeNo + "&step=" + '5';
            window.location.href = appPath + "/super/project_create/public/create_project" + param;
        });

        // 发布
        $("#publish_1").click(function () {
            _this.publishProject();
        });

        // 保存
        $("#btn_save").click(function() {
          if("1" == projectStatus){
            layer.confirm('保存成功，是否发布？', {
              icon: 3,
              btn: ['是','否'] //按钮
            }, function(){
              // 异步发布项目
              $.ajax({
                url : appPath + '/super/project_create/public/project_publish',
                dataType : 'json',
                async : false,
                type : 'post',
                data : {
                    "project_id" : projectId,
                    "project_type" : projectTypeNo,
                    'type': '1'
                },
                success : function (data) {
                    var code = data.code;
                    var result = data.result
                    if('10000' == code && result == null){
                        layer.msg("操作成功",{icon: 1},function () {
                            // 返回首页列表
                            window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
                        });
                    }else{
                        layer.alert(result,{icon: 2});
                    }
                }
              });
            }, function(){
              // 返回首页列表
              window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
            });
          }else{
            layer.alert("保存成功", {icon: 1}, function () {
              window.location.href = appPath
                  + "/super/project_create/public/create_project?step=" + '0';
            })
          }

        });

        // 搜索课程
        $("#btn_search_course").click(function () {
            _this.initTable_course();
        });

        // 全部课程
        $("#btn_all_course").click(function () {
            $("#course_name").val("");
            _this.initTable_course();
        });
    }

    //课程列表
    _this.initTable_course = function () {
        var list_url = appPath + "/super/project_create/public/course_list";
        page.init("course_form", list_url, "course_list", "course_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function (dataList) {
            var inner = "", item;
            _this.obj = dataList;
            var len = dataList.length;
            // 组装数据
            for (var i = 0; i < len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td>' + (i + 1) + '</td>';
                inner += '<td>' + item['course_name'] + '</td>';
                inner += '<td>' + item['course_no'] + '</td>';
                if (_this.isContains('1457', projectTypeNo)) {
                    inner += '<td>' + item['class_hour'] + '</td>';
                    inner += '<td id="editRequirement">';
                    if (_this.isContains('12',projectStatus)) {
                        inner += '<a href="javascript:;" style="font-weight: bolder" class="editable editable-click edit" onmouseover="step_4.editRequirement(this,\'' + item.id + '\')" >' + item['requirement'] + '</a>'; //学时要求
                    } else {
                        inner += '<a href="javascript:;" data-type="text" data-pk="1">' + item['requirement'] + '</a>'; //学时要求
                    }
                    inner += '</td>';
                }
                if (_this.isContains('234567', projectTypeNo)) {
                    inner += '<td>' + item['question_count'] + '</td>';
                }
                // if (_this.isContains('3567', projectTypeNo)) {
                //     inner += '<td id="edit">';
                //     if (_this.isContains('12',projectStatus)){
                //         inner += '<a href="javascript:;" style="font-weight: bolder" class="editable editable-click edit" onmouseover="step_4.editSelectCount(this,\'' + item.id + '\',' + item.question_count + ')" >' + item['select_count'] + '</a>'; //必选题量
                //     } else {
                //         inner += '<a href="javascript:;" data-type="text" data-pk="1">' + item['select_count'] + '</a>'; //必选题量
                //     }
                //     inner += '</td>';
                // }
                inner += '</tr>';
            }
            return inner;
        }
    };

    //设置学时要求
    _this.editRequirement = function (obj, id) {
        var projectId = $("#course_project_id").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
            validate: function (value) {
                var param = {
                    "projectId": projectId,
                    "id": id,
                    "requirement": value
                }
                // console.log(param)
                _this.edit_ajax(param, obj);
            }
        })
    };

    //设置必选题量
    _this.editSelectCount = function (obj, id, questionCount) {
        var projectId = $("#course_project_id").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
            editBy: 'click',
            validate: function (value) {
                if (value > questionCount) {
                    value = questionCount;
                    return "题量不合法";
                }
                var param = {
                    "projectId": projectId,
                    "id": id,
                    "selectCount": value
                }
                // console.log(param)
                _this.edit_ajax(param, obj);
                return false;
            },
            display: function (value) {
                if (value > questionCount || value < 0) {
                    $(this).text(questionCount);
                } else {
                    $(this).text(value);
                }
            }
        })
    };

    _this.edit_ajax = function (param, obj) {
        console.log(param)
        $.ajax({
            url: appPath + '/super/project_create/public/edit_ajax',
            dataType: 'json',
            async: false,
            type: 'post',
            data: param,
            success: function (data) {
                var result = data.code;
                if ('10000' == result) {
                    layer.msg("设置成功");
                } else {
                    layer.msg("设置失败");
                    $(obj).text()
                }
            }
        });
    };

    // 保存（返回首页）
    _this.publishProject = function () {
        layer.alert("发布项目");
    }

    //判断字符串是否包含某字符
    _this.isContains = function (str, substr) {
        return new RegExp(substr).test(str);
    }

    //关闭对话框
    _this.closeDialog = function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }

}

var step_4 = new Step_4();