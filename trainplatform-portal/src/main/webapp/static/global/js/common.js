
function common(){
    var _this = this;

    //callback是回调函数
    //parent端增加初始化
    _this.messenger_parent_init = function(callback){
        var messenger = new Messenger("parent", "messenger");
        messenger.listen(function (msg) {
            callback(msg);
        });
    };

    //客户端发送消息
    _this.messenger_child_send = function(msg){
        var messenger = new Messenger("child", "messenger");
        messenger.addTarget(window.opener, "child");
        messenger.targets["child"].send(msg);
    };

    var outerWindow_study_status = false;
    //进入学习
    _this.goto_student_study = function(project_id, course_id){
        var study_url = "";
        //学员自学
        if(project_id == null){
            study_url =  appPath + "/student/studySelf?courseId=" + course_id
        }
        // 在线学习
        if(course_id == null){
            study_url = appPath + "/student/learn?projectId=" + project_id
        }
        // 各个课程进入学习
        if(course_id != null && project_id != null){
            study_url = appPath + "/student/study?projectId=" + project_id + "&courseId=" + course_id;
        }

        if(outerWindow_study_status) {
            //study_url = appPath + "/student/study?projectId=" + project_id + "&courseId=" + course_id;
            var winOpn = window.open("", "student_study");
            if(winOpn.location.href.indexOf(study_url) == -1){
                window.open(study_url, "student_study","",true);
            }else{
                window.open("", "student_study","",true);
            }
        } else {
            window.open(study_url, "student_study");
            outerWindow_study_status = true;
        }
    };

    _this.messenger_student_enter_init = function () {
        setCookie('messenger_study_msg', "is_open_study");
        setCookie('messenger_question_msg', "is_open_question");
        window.setInterval(function () {
            var msg = getCookie("messenger_parent_msg");
            if(msg != '' && msg != undefined){
                console.log("messenger_parent_msg:" + msg);
                if(msg == 'question_true'){
                    outerWindow_question_status = true;
                }else if(msg == 'study_true'){
                    outerWindow_study_status = true;
                }else if(msg == 'study_refresh'){
                    //监听子页面的返回参数，刷新列表
                    window.setTimeout(function (){
                        // message.add("学时刷新", "success");
                        common.update_wrong_collect();
                        student_enter.tongji();
                        student_enter.initTable();
                    }, 1500);
                    outerWindow_question_status = false;
                    setCookie('messenger_study_msg', "is_open_study");
                }else if(msg == 'question_refresh'){
                    outerWindow_question_status = false;
                    setCookie('messenger_question_msg', "is_open_question");
                    common.goto_student_exam_paper_no($("#projectId").val());
                    common.update_wrong_collect();
                }else if(msg == 'question_exercise_refresh'){
                    //监听子页面的返回参数，刷新列表
                    window.setTimeout(function (){
                        common.update_wrong_collect();
                        //增加培训判断
                        if(student_enter.permission['permissionTrain'] || student_enter.permission['permissionExercise']){
                            // message.add("学时刷新", "success");
                            student_enter.tongji();
                            student_enter.initTable();
                        }
                    }, 1500);
                }
                //重置cookie
                delCookie("messenger_parent_msg");
            }
        }, 1000);
    };

    _this.messenger_student_self_init = function () {
        setCookie('messenger_study_msg', "is_open_study");
        setCookie('messenger_question_msg', "is_open_question");
        window.setInterval(function () {
            var msg = getCookie("messenger_parent_msg");
            if(msg != '' && msg != undefined){
                console.log("messenger_parent_msg:" + msg);
                if(msg == 'question_true'){
                    outerWindow_question_status = true;
                }else if(msg == 'study_true'){
                    outerWindow_study_status = true;
                }else if(msg == 'study_refresh'){
                    //监听子页面的返回参数，刷新列表
                    window.setTimeout(function (){
                        // message.add("学时刷新", "success");
                        course_list.initTable();
                    }, 1500);
                    outerWindow_question_status = false;
                    setCookie('messenger_study_msg', "is_open_study");
                }else if(msg == 'question_exercise_refresh'){
                    //监听子页面的返回参数，刷新列表
                    window.setTimeout(function (){
                            // message.add("学时刷新", "success");
                            course_list.initTable();

                    }, 1500);
                }
                //重置cookie
                delCookie("messenger_parent_msg");
            }
        }, 1000);
    };

    //看视频
    _this.study_messenger_init = function () {
        setCookie('messenger_parent_msg', "study_true");
        window.setInterval(function () {
            var msg = getCookie("messenger_study_msg");
            if(msg != '' && msg != undefined){
                console.log("messenger_study_msg:" + msg);
                if(msg == 'is_open_study'){
                    setCookie('messenger_parent_msg', "study_true");
                }
                delCookie("messenger_study_msg");
            }
        }, 1000);
    };

    //答题
    var question_messenger;
    _this.question_messenger_init = function () {
        setCookie('messenger_parent_msg', "question_true");
        window.setInterval(function () {
            var msg = getCookie("messenger_question_msg");
            if(msg != '' && msg != undefined){
                console.log("messenger_question_msg:" + msg);
                if(msg == 'is_open_question'){
                    setCookie('messenger_parent_msg', "question_true");
                }
                delCookie("messenger_question_msg");
            }
        }, 1000);
    };

      //课程预览
    _this.goto_student_course_view = function(course_id){
        window.open(appPath + "/popup/course/preview?courseId=" + course_id);
    };

    //课程题库预览
    _this.goto_student_course_question_view = function(course_id){
        window.open(appPath + "/popup/course_question_view?course_id=" + course_id);
    };

    //成绩预览
    _this.goto_student_exam_answer_view = function(exam_no){
        window.open(appPath + "/popup/question/exam_score?exam_no=" + exam_no);
    };

    _this.plusTime =function () {
        _this.study_messenger_send("study_refresh");
    }

    //获取考试编号
    _this.goto_student_exam_paper_no = function(project_id){
        if(permission['permissionExam']){
            var url = appPath + '/student/question/paper_no.do';
            var param = {"project_id" : project_id};
            var data = _this.ajax(url, param);
            var code = data['code'];
            var result = data['result'];
            if(code == '10000' && null != result && result != ''){
                $(".exam_1").attr("exam_no", result['examNo_mn']);
                $(".exam_2").attr("exam_no", result['examNo_zs']);
                $(".exam_2").find("i:eq(0)").html(result['las_exam_count']);
                $(".exam_2").find("i:eq(1)").html(result['necessaryHour']);
            }else{
                window.location.reload();
            }
        }
    };

    _this.update_wrong_collect = function(){
        var params = {
            'project_id': $("#project_basicId").val(),
            'project_userId': $("#project_userId").val()
        }
        $.ajax({
            url: appPath + "/student/enterProject/update_wrong_collect",
            data: params,
            async: false,
            type: "post",
            dataType:"json",
            success:function(data){
                var rs = data.result;
                if(rs){
                    $("#wrongCount").find("span").html(rs['wrongCount']);
                    $("#collectCount").find("span").html(rs['collectCount']);
                }
            }
        })
    }

    //进入考试 1模拟2考试
    var outerWindow_question_status = false;
    _this.goto_student_exam = function(project_id, exam_type){
        if($(".exam_1").parent().hasClass("permission")){
            return;
        }
        if(outerWindow_question_status){
            outerWindow_question_status = false;
            setCookie('messenger_question_msg', "is_open_question");
            window.open("", "student_question");
            setTimeout(function () {
                if(!outerWindow_question_status){
                    common.goto_student_exam(project_id, exam_type);
                }
            }, 1000);
        }else{
            if(exam_type == 2 && $(".exam_" + exam_type).find("i").html() == 0){
                layer.alert("没有剩余考试次数");
                return;
            }
            var exam_no = $(".exam_" + exam_type).attr("exam_no");
            var url = appPath + '/student/question/create_paper.do';
            var param = {"project_id" : project_id, "exam_type" : exam_type, "exam_no" : exam_no};
            var data = _this.ajax(url, param);
            var code = data['code'];
            var result = data['result'];
            if(code == '50514'){//检查未通过
                layer.alert(result);
            }else if(code == '50515'){//数据库中有未考试的题
                window.open(appPath + "/student/question/exam?exam_no=" + result, "student_question");
                outerWindow_question_status = true;
            }else if(code == '10000'){
                if(result && result != ''){//如果原来exam_no时，重新生成一个
                    exam_no = result;
                }
                var winopn = window.open("", "student_question");
                //如果ajax响应时间过长，就给个提示
                winopn.document.write('<style>body{margin:0;padding:0;background: #e8edf0;overflow: hidden;}</style><img src="' + resourcePath + 'static/global/images/create_paper.png" style="width:100%;height:auto;margin-top:9%;"/>');

                window.setTimeout(function () {
                    var url = appPath + "/student/question/exam?exam_no=" + exam_no;
                    winopn.location = url;
                }, 2500);
            }else{
                layer.alert("网络异常");
            }
        }
    };

    //我的错题
    _this.goto_student_wrong = function(project_id){
        window.open(appPath + "/student/question/wrongs?project_id=" + project_id);
    };

    //我的收藏
    _this.goto_student_collection = function(project_id){
        window.open(appPath + "/student/question/collection?project_id=" + project_id);
    };

    //专项练习：单选
    _this.goto_student_single = function(project_id){
        window.open(appPath + "/student/question/exercise/single?project_id=" + project_id);
    };

    //专项练习：多选
    _this.goto_student_many = function(project_id){
        window.open(appPath + "/student/question/exercise/many?project_id=" + project_id);
    };

    //专项练习：判断
    _this.goto_student_judge = function(project_id){
        window.open(appPath + "/student/question/exercise/judge?project_id=" + project_id);
    };

    //专项练习：我的易错题
    _this.goto_student_easyWrongs = function(project_id){
        window.open(appPath + "/student/question/easyWrongs?project_id=" + project_id);
    };

    //随机练习
    _this.goto_student_random = function(project_id){
        window.open(appPath + "/student/question/exercise/courseList?project_id=" + project_id);
    };

    //课程练习
    _this.goto_student_course_study = function(project_id, course_id){
        window.open(appPath + "/student/question/courseExercise?project_id=" + project_id + "&course_id=" + course_id);
    };

    /**
     * 根据项目id查询组卷策略中必选题量最大值
     * @param projectId
     * @returns {number}
     */
    _this.getMaxSelectQuestion = function(projectId){
        var url = appPath + '/popup/project_create/getSelectAllCount.do';
        var param = {"projectId" : projectId};
        var data = _this.ajax(url, param);
        var result = data['result'];
        var code = data['code'];
        if(code == '10000' && result){
            return result;
        }
        return 0;
    };

    _this.ajax = function(url, param){
        var result = '';
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function(data){
                result = data;
            }
        });
        return result;
    };



}

var common = new common();