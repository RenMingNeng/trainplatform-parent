var train_subject_add = new Object();
train_subject_add.courses = [];

train_subject_add.init = function (basePath, page, options) {

    //对于课程和人员的操作
    train_subject_add.handle();
    //展示课程列表
    train_subject_add.train_subject_course()
}


//受训课程和人员处理
train_subject_add.handle = function () {
    //取消图片的选择
    $("#cancel").click(function () {
        $("#imHeadPhoto").attr("src", "");
        $("#file").val("")
    })
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

    // 批量删除课程
    $("#btn_delete_batch_course").click(function () {
        if (!$(this).hasClass("unclick")) {
            train_subject_add.delete_train_subject_course();
        }
    });

    //全选课程
    $("#coursecheckAll").click(function () {
        var flag = $("#coursecheckAll").prop("checked");
        $('#select_course_list input[type="checkbox"]').prop("checked", flag);
    })

    //保存
    $("#save").click(function () {
        debugger;
        var subjectName = $.trim($("#subjectName").val());
        if ("" == subjectName) {
            layer.msg("请填写主题名称")
            return;
        }
        if (subjectName.length > 15) {
            layer.msg("主题名称请少于15个字");
            return;
        }
        $.ajax({
            url: appPath + "/admin/trainSubject/verify",
            type: 'post',
            data: {"subjectName": subjectName},
            success: function (data) {
                if ("1" == data.code) {
                    layer.msg("主题名称重复，请重置");
                    return;

                } else {
                    var courseIds = [];
                    for (var i = 0; i < train_subject_add.courses.length; i++) {
                        courseIds.push(train_subject_add.courses[i].course_id)
                    }
                    $("#courseIds").val(courseIds);
                    var url = appPath + "/admin/trainSubject/add"
                    //$("#trainsubject_form").attr("action",url);
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
                }
            }
        });
    })

};

// 批量删除课程
train_subject_add.delete_train_subject_course = function () {

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
        for (var i = 0; i < courseIds.length; i++) {
            train_subject_add.remove(courseIds[i]);
        }
        $("#coursecheckAll").prop("checked", false);
        //展示课程
        train_subject_add.train_subject_course();
        layer.msg("删除成功")
    });
};


// 选择培训课程的回调
train_subject_add.submitCourse = function (rows) {
    for (var i = 0; i < rows.length; i++) {
        train_subject_add.removeRepart(rows[i]);
    }
    console.log("===" + train_subject_add.courses);
    train_subject_add.train_subject_course();
}

// 课程信息列表
train_subject_add.train_subject_course = function () {
    var len = train_subject_add.courses.length;
    var length = $("#select_course_list").parent().find("th").length;
    if (len < 1) {
        $("#select_course_list").empty().html(train_subject_add.noList(length));
        return
    }
    var inner = "", item;
    // 组装数据
    for (var i = 0; i < len; i++) {
        item = train_subject_add.courses[i];
        inner += '<tr >';
        inner += '<td>' + (i + 1) + '</td>';
        inner += '<td><input type=\"checkbox\" name="courseBox" data-courseId="' + item['course_id'] + '" data-id="' + item['id'] + '"></td>';
        inner += '<td>' + item['course_no'] + '</td>';
        inner += '<td><span class="text-orange">' + item['course_name'] + '</span></td>';
        inner += '<td>' + item['class_hour'] + '</td>';
        inner += '<td>';
        inner += '<a href="javascript:;" onclick=\"train_subject_add.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
        inner += ' <a href="javascript:;" onclick=\"train_subject_add.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
        inner += '</td>';
        inner += '</tr>';
    }

    // 渲染表单
    $("#select_course_list").empty().html(inner);

};


//课程预览
train_subject_add.course_view = function (course_id) {
    common.goto_student_course_view(course_id);
}

//试题预览
train_subject_add.question_view = function (course_id) {
    common.goto_student_course_question_view(course_id);
}

//课程去重
train_subject_add.removeRepart = function (course) {
    var flag = true;
    for (var i = 0; i < train_subject_add.courses.length; i++) {
        if (train_subject_add.courses[i].course_id == course.course_id) {
            flag = false;
        }
    }
    if (flag) {
        train_subject_add.courses.push(course);
    }
}

//删除课程
train_subject_add.remove = function (courseId) {
    for (var i = 0; i < train_subject_add.courses.length; i++) {
        if (train_subject_add.courses[i].course_id == courseId) {
            train_subject_add.courses.splice(i, 1);
        }
    }
}

train_subject_add.noList = function (length) {
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
train_subject_add.previewImage = function (fileObj, imgPreviewId, divPreviewId) {
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