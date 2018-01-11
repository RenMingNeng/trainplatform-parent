var train_subject_modify = new Object();
train_subject_modify.courses = [];
train_subject_modify.courseIds = [];
train_subject_modify.init = function (basePath, page, options) {
    //对于课程和人员的操作
    train_subject_modify.handle();
    //展示已经有的数据
    train_subject_modify.initTable();

}

//查询主题下的所有课程
train_subject_modify.initCourse = function () {
    var varId = $("#varId").val();
    /**/
    $.ajax({
        url: appPath + "/admin/trainSubject/allCourse",
        type: 'post',
        data: {"varId": varId},
        success: function (data) {
            if (10000 == data.code) {
                train_subject_modify.courseIds = data.result;
                console.log("==已有的课程id=" + data.result);
            }
        }
    });
}
train_subject_modify.initTable = function () {

    $("#all").attr("checked", false);
    var list_url = appPath + "/admin/trainSubject/courseList";
    page.init("select_course_form", list_url, "select_course_list", "select_course_page", 1, 10);
    page.goPage(1);
    page.list = function (dataList) {

        var len = dataList.length;
        items = dataList;
        var inner = "", item;
        for (var i = 0; i < len; i++) {
            // 组装数据
            item = dataList[i];
            inner += '<tr>';
            inner += '<td>' + (i + 1) + '</td>';
            inner += '<td><input type="checkbox"  name="ckbox" data-courseId="' + item.course_id + '"  value="' + item.course_id + '" /></td>';
            inner += '<td>';
            inner += '<span>' + item.course_no + '</span>';
            inner += '</td>';
            inner += '<td>' + item.course_name + '</td>';
            inner += '<td>' + item.class_hour + '</td>';
            inner += '<td><a href="javascript:;" onclick=\"train_subject_modify.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
            inner += ' <a href="javascript:;" onclick=\"train_subject_modify.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a></td>';
            inner += '</tr>';
        }
        return inner;
    }
    //查询所有课程
    train_subject_modify.initCourse()
};


//受训课程和人员处理
train_subject_modify.handle = function () {
    //弹窗-选择课程
    $("#btn_dialog_course").click(function () {
        // 调用选择培训课程的弹窗
        layer.open({
            type: 2,
            title: '选择课程',
            area: ['1200px', '80%'],		//弹出层大小
            scrollbar: false,				//false隐藏滑动块
            content: [appPath + '/popup/enterSelectCourse', 'yes']
        });
    });

    $("#all").click(function () {
        if (this.checked) {
            $("#select_course_list :checkbox").prop("checked", true);
        } else {
            $("#select_course_list :checkbox").prop("checked", false);
        }
    });
    $("#btn_search_course").click(function () {
        train_subject_modify.initTable();
    });
    $("#btn_search_all_course").click(function () {
        $("#courseName").val("");
        train_subject_modify.initTable();
    });

    train_subject_modify.search = function () {
        train_subject_modify.initTable();
    };


    //保存
    $("#update").click(function () {
        var subjectName = $("#subjectName").val();
        var id = $("#varId").val();
        if ("" == subjectName) {
            layer.msg("请填写主题名称");
            return;
        }
        if (subjectName.length > 15) {
            layer.msg("主题名称请少于15个字");
            return;
        }
        var url = appPath + "/admin/trainSubject/update";
        var opt = {
            url: url,
            type: "post",
            dataType: "json",
            success: function (data) {
                result = data.code;
                if ("success" == data.message && "10000" == result) {
                    window.location.href = appPath + "/admin/trainSubject"
                } else if ("fail"== data.message && "10000" == result) {
                    layer.msg("上传照片失败");
                    window.setTimeout(function () {
                        window.location.href = appPath + "/admin/trainSubject";
                    }, 1500);
                }
            }
        };
        $("#trainsubject_form").ajaxSubmit(opt);

    });


    //删除课程
    $("#btn_delete_batch_course").click(function () {
        train_subject_modify.delete_train_subject_course();
    })
}

// 批量删除课程
train_subject_modify.delete_train_subject_course = function () {

    var courseIds = []
    $('#select_course_list input:checked').each(function (i, e) {
        courseIds.push($(this).attr("data-courseId"));
    })

    if (courseIds.length == 0) {
        layer.msg("未选择课程");
        return;
    }

    layer.confirm("确定执行此操作?", {
        icon: 3,
        btn: ["确认", "取消"]
    }, function () {
        train_subject_modify.remove(courseIds);

    });
};


// 选择培训课程的回调
train_subject_modify.submitCourse = function (rows) {
    var varId = $("#varId").val();
    if (rows.length < 1) {
        return;
    }
    for (i = 0; i < rows.length; i++) {
        //train_subject_modify.courseIds.push(rows[i].course_id);
        train_subject_modify.removeRepart(train_subject_modify.courseIds, rows[i].course_id);
    }

    console.log("==选择id=" + train_subject_modify.courseIds);
    //train_subject_modify.a = [];
    //train_subject_modify.a = train_subject_modify.removeRepart(train_subject_modify.courses, train_subject_modify.courseIds);
    //console.log("==整合后的id=" + train_subject_modify.a);
    /*修改*/
    $.ajax({
        url: appPath + "/admin/trainSubject/updateCourse",
        type: 'post',
        data: {"varId": varId, "courseIds": train_subject_modify.courseIds.join(',')},
        success: function (data) {
            if (10000 == data.code) {
                console.log("=== ok 修改成功=======");
                /* train_subject_modify.courses = [];
                 train_subject_modify.courseIds = [];*/
                //train_subject_modify.a = []
                train_subject_modify.initTable();
            }
        }
    });

}


// 课程信息列表
train_subject_modify.train_subject_course = function () {
    var len = train_subject_modify.courses.length;
    var length = $("#select_course_list").parent().find("th").length;
    if (len < 1) {
        $("#select_course_list").empty().html(train_subject_modify.noList(length));
        return
    }
    var inner = "", item;
    // 组装数据
    for (var i = 0; i < len; i++) {
        item = train_subject_modify.courses[i];
        inner += '<tr >';
        inner += '<td>' + (i + 1) + '</td>';
        inner += '<td><input type=\"checkbox\" name="courseBox" data-courseId="' + item['course_id'] + '" data-id="' + item['id'] + '"></td>';
        inner += '<td>' + item['course_no'] + '</td>';
        inner += '<td><span class="text-orange">' + item['course_name'] + '</span></td>';
        inner += '<td>' + item['class_hour'] + '</td>';
        inner += '<td>';
        inner += '<a href="javascript:;" onclick=\"train_subject_modify.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
        inner += ' <a href="javascript:;" onclick=\"train_subject_modify.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
        inner += '</td>';
        inner += '</tr>';
    }

    // 渲染表单
    $("#select_course_list").empty().html(inner);

};


//课程预览
train_subject_modify.course_view = function (course_id) {
    common.goto_student_course_view(course_id);
}

//试题预览
train_subject_modify.question_view = function (course_id) {
    common.goto_student_course_question_view(course_id);
}

//课程数组合并去重
train_subject_modify.removeRepart = function (courseIds, courseId) {
    var flag = true;
    for (var i = 0; i < courseIds.length; i++) {
        if (courseIds[i] == courseId) {
            flag = false;
        }
    }
    if (flag) {
        train_subject_modify.courseIds.push(courseId);
    }
}

//删除课程
train_subject_modify.remove = function (courseId) {

    var varId = $("#varId").val();
    $.ajax({
        url: appPath + "/admin/trainSubject/deleteCourse",
        type: 'post',
        data: {"varId": varId, "courseIds": courseId.join(','),},
        success: function (data) {
            if (10000 == data.code) {
                console.log("=== ok=======");
                layer.msg("删除成功")
                //展示课程
                train_subject_modify.initTable();


            }
        }
    });
}

train_subject_modify.noList = function (length) {
    var inner = "";
    inner += "<tr>";
    inner += '<td colspan=\'' + length + '\' class=\"table_empty\"><img src=\'' + empty_img_src + '\'></img></td>'
    inner += "</tr>";
    return inner;
}

/*
 *
 * 预览照片*/

//js本地图片预览，兼容ie[6-9]、火狐、Chrome17+、Opera11+、Maxthon3
train_subject_modify.previewImage = function (fileObj, imgPreviewId, divPreviewId) {
    var allowExtention = ".jpg,.bmp,.gif,.png";//允许上传文件的后缀名document.getElementById("hfAllowPicSuffix").value;
    var extention = fileObj.value.substring(fileObj.value.lastIndexOf(".") + 1).toLowerCase();
    var browserVersion = window.navigator.userAgent.toUpperCase();
    if (allowExtention.indexOf(extention) > -1) {
        if (fileObj.files) {//HTML5实现预览，兼容chrome、火狐7+等
            if (window.FileReader) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    document.getElementById(imgPreviewId).setAttribute("src", e.target.result);
                }
                reader.readAsDataURL(fileObj.files[0]);
            } else if (browserVersion.indexOf("SAFARI") > -1) {
                alert("不支持Safari6.0以下浏览器的图片预览!");
            }
        } else if (browserVersion.indexOf("MSIE") > -1) {
            if (browserVersion.indexOf("MSIE 6") > -1) {//ie6
                document.getElementById(imgPreviewId).setAttribute("src", fileObj.value);
            } else {//ie[7-9]
                fileObj.select();
                if (browserVersion.indexOf("MSIE 9") > -1)
                    fileObj.blur();//不加上document.selection.createRange().text在ie9会拒绝访问
                var newPreview = document.getElementById(divPreviewId + "New");
                if (newPreview == null) {
                    newPreview = document.createElement("div");
                    newPreview.setAttribute("id", divPreviewId + "New");
                    newPreview.style.width = document.getElementById(imgPreviewId).width + "px";
                    newPreview.style.height = document.getElementById(imgPreviewId).height + "px";
                    newPreview.style.border = "solid 1px #d2e2e2";
                }
                newPreview.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod='scale',src='" + document.selection.createRange().text + "')";
                var tempDivPreview = document.getElementById(divPreviewId);
                tempDivPreview.parentNode.insertBefore(newPreview, tempDivPreview);
                tempDivPreview.style.display = "none";
            }
        } else if (browserVersion.indexOf("FIREFOX") > -1) {//firefox
            var firefoxVersion = parseFloat(browserVersion.toLowerCase().match(/firefox\/([\d.]+)/)[1]);
            if (firefoxVersion < 7) {//firefox7以下版本
                document.getElementById(imgPreviewId).setAttribute("src", fileObj.files[0].getAsDataURL());
            } else {//firefox7.0+
                document.getElementById(imgPreviewId).setAttribute("src", window.URL.createObjectURL(fileObj.files[0]));
            }
        } else {
            document.getElementById(imgPreviewId).setAttribute("src", fileObj.value);
        }
    } else {
        alert("仅支持" + allowExtention + "为后缀名的文件!");
        fileObj.value = "";//清空选中文件
        if (browserVersion.indexOf("MSIE") > -1) {
            fileObj.select();
            document.selection.clear();
        }
        fileObj.outerHTML = fileObj.outerHTML;
    }
}