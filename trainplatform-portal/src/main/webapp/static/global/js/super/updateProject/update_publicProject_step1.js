/**
 * Created by Administrator on 2017-07-31.
 */
function UpdatePublicProject_Step1() {
    var _this = this;
    var page;
    var permissionExam,permissionTrain,permissionExercise;
    var projectId = $("#projectId").val();           // 项目编号
    var projectTypeNo = $("#projectTypeNo").val();   // 项目类型编号
    var projectStatus = $("#projectStatus").val();   // 项目状态
    _this.courses  = null;
    _this.page_size = 10;
    _this.index = -1;

    // init
    _this.init = function(_page,options) {
        page = _page;
        //含有培训的
        permissionExam = options.permissionExam;
        //含有考试的
        permissionTrain = options.permissionTrain;
        //含有练习的
        permissionExercise = options.permissionExercise;
        _this.initEvent();

        if("1" != projectStatus){
            _this.initTimeBtn();
        }
    }

    _this.initEvent = function() {
        // 点击返回到首页
        $("#btn_back").click(function() {
            window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
        });

        // 点击下一步,保存项目
        $("#step_2").click(function() {
            _this.projectUpdateSave();
        });
    };


    // 修改保存
    _this.projectUpdateSave = function() {
       // if(!_this.verify())return;
        var trainBeginTime='',trainEndTime='',project_train_Time='',
            exerciseBeginTime='',exerciseEndTime='',project_exercise_Time='',
            examBeginTime='',examEndTime='',project_exam_Time='';
        var intRetestTime = '';
        // 含培训
        if(_this.isContains('1457',projectTypeNo)){
            trainBeginTime = $("#trainBeginTime").val()+" 00:00:00";
            trainEndTime = $("#trainEndTime").val()+" 23:59:59";
            project_train_Time = trainBeginTime+" 至 "+trainEndTime;
        }
        // 含练习
        if(_this.isContains('2467',projectTypeNo)){
            exerciseBeginTime = $("#exerciseBeginTime").val()+" 00:00:00";
            exerciseEndTime = $("#exerciseEndTime").val()+" 23:59:59";
            project_exercise_Time = exerciseBeginTime+" 至 "+exerciseEndTime;
        }
        // 含考试
        if(_this.isContains('3567',projectTypeNo)){
            examBeginTime = $("#examBeginTime").val()+":00";
            examEndTime = $("#examEndTime").val()+":00";
            intRetestTime = $("#intRetestTime").val();
            project_exam_Time = examBeginTime+" 至 "+examEndTime;
        }
        var params = {
            'projectId' : $("#projectId").val(),              // 项目id
            'trainBeginTime': trainBeginTime,                 // 培训开始时间
            'trainEndTime': trainEndTime,                     // 培训结束时间
            'project_train_Time' : project_train_Time,        // 培训时间
            'exerciseBeginTime': exerciseBeginTime,           // 练习开始时间
            'exerciseEndTime': exerciseEndTime,               // 练习结束时间
            'project_exercise_Time' : project_exercise_Time,  // 练习时间
            'examBeginTime': examBeginTime,                   // 考试开始时间
            'examEndTime': examEndTime,                       // 考试结束时间
            'project_exam_Time' : project_exam_Time,          // 考试时间
            'intRetestTime':intRetestTime                     // 考试次数
            }
        $.ajax({
            url : appPath + '/super/project_create/public/update_public_project',
            dataType : 'json',
            async : false,
            type : 'post',
            data : params,
            success : function(data) {
                var result = data.code;
                if (10000 == result) {
                    window.location.href = appPath+"/super/project_create/public/create_project?projectId="+projectId+"&projectTypeNo="+projectTypeNo+"&step="+'2';
                } else {
                    layer.alert('信息有误', {icon : 2,skin : 'layer-ext-moon'});
                }
            }
        });
    };

    // 时间验证校验
    _this.verify = function() {
        // 错误信息
        var msg = "";
        // 含培训
        if(_this.permissionTrain){
            // 项目开始时间
            var datBeginTime = $.trim($("#trainBeginTime").val());
            if(!datBeginTime){
                msg = "请选择项目开始时间";
                layer.msg(msg);
                return false;
            }
            // 项目结束时间
            var datEndTime = $.trim($("#trainEndTime").val());
            if(!datEndTime){
                msg = "请选择项目结束时间";
                layer.msg(msg);
                return false;
            }
        }
        // 含练习
        if(_this.permissionExercise){
            // 项目开始时间
            var datBeginTime = $.trim($("#exerciseBeginTime").val());
            if(!datBeginTime){
                msg = "请选择项目开始时间";
                layer.msg(msg);
                return false;
            }
            // 项目结束时间
            var datEndTime = $.trim($("#exerciseEndTime").val());
            if(!datEndTime){
                msg = "请选择项目结束时间";
                layer.msg(msg);
                return false;
            }
        }
        // 含考试
        if(_this.permissionExercise){
            // 考试开始时间
            var examBeginTime = $.trim($("#examBeginTime").val());
            if(!examBeginTime){
                msg = "请选择考试开始时间";
                layer.msg(msg);
                return false;
            }
            // 考试结束时间
            var examEndTime = $.trim($("#examEndTime").val());
            if(!examEndTime){
                msg = "请选择考试结束时间";
                layer.msg(msg);
                return false;
            }
            // 考试次数
            var intRetestTime = $.trim($("#intRetestTime").val());
            if(!intRetestTime){
                msg = "请填写考试次数";
                layer.msg(msg);
                return false;
            }
        }
        return true;
    };

    //判断字符串是否包含某字符
    _this.isContains = function(str,substr){
        return new RegExp(substr).test(str);
    }


    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }



    //验证考试次数为整数
    _this.verifyIntRetestTime = function(value) {
        var value_ = $.trim(value);
        //value= value_.replace(/\D/g, '');
        value= Math.round(value_);
        $("#intRetestTime").val( value);
    }

    /**
     * 判断时间是否可更改(未开始未发布的项目可更改时间开始时间和结束时间，进行中的项目只可改结束时间，已结束项目时间不可更改)
     *
     */
    _this.initTimeBtn = function () {
        //当前系统时间
        var nowTime = new Date().getTime();
        $(".Wdate").each(function (i) {
            var $this = $(this);
            var val = $this.val();
            if("" != val && null != val){
                var old = new Date(val.replace(/-/g, "/")).getTime();
                if(old < nowTime){
                    $this.removeAttr('onfocus').attr('readOnly',true).css('background-color','#ddd');
                }
            }
        });
    }

}

var updatePublicProject_step1 = new UpdatePublicProject_Step1();