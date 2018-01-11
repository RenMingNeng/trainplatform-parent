/**
 * 创建公开型项目第一步
 */
function Step_1() {
    var _this = this;
    var containTrainType,containExamType,containExerciseType;
    var projectId = $("#id").val();                 // 项目编号
    var projectTypeNo = $("#projectTypeNo").val();  // 项目类型编号
    //init
    _this.init = function (options) {
        //含有培训的
        containTrainType = options.containTrainType;
        //含有考试的
        containExamType = options.containExamType;
        //含有练习的
        containExerciseType = options.containExerciseType;

        _this.initEvent();
    }

    _this.initEvent = function() {
        // 点击返回，跳到首页
        $("#btn_back").click(function() {
            window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
        });
        // 点击下一步，保存项目基本信息
        $("#step_2").click(function() {
            _this.projectSave();
            // 考试项目组卷策略初始化
            var projectId = $("#id").val();
            if(_this.isContains(containExamType,projectTypeNo)){
                stragegySelect.stragegy_listener();
            }
        });
    }



    // 保存项目
    _this.projectSave = function() {
        var $this = $(this);
        if(!_this.verify())return;
        var subjectId = $("#subjectName").val();
        if(null==subjectId || undefined == subjectId || 'undefined' == subjectId){
            subjectId = '';
        }
        // 自动计算学员项目周期 = 项目结束时间-项目开始时间
        var start = Date.parse($("#datBeginTime").val().replace(/-/g, '/')) / 86400000;
        var end = Date.parse($("#datEndTime").val().replace(/-/g, '/')) / 86400000;
        var trainPeriod = Math.abs(start - end);                                                   //项目周期
        var params = {
            'id' : projectId,
            'projectName' : $("#projectName").val(), 		      // 项目名称
            'subjectId' : subjectId, 			                  // 主题id
            'subjectName' : $("#subjectName").val(), 			  // 主题名称
            'datBeginTime' : $("#datBeginTime").val(), 			  // 项目开始时间
            'datEndTime' : $("#datEndTime").val(), 				  // 项目结束时间
            'examBeginTime' : $("#examBeginTime").val(), 		  // 考试开始时间
            'examEndTime' : $("#examEndTime").val(), 			  // 考试结束时间
            'trainPeriod': trainPeriod,                           // 项目周期
            'projectType' : projectTypeNo, 		                  // 项目类型
            'intRetestTime': $("#intRetestTime").val(),           // 考试次数
            'projectMode' : "1",                                  // 项目公开 0私有   1公开
            'projectOpen':'1',                                    // 项目开启状态 1开启  2未开启
            'projectStatus' : "1",                                // 项目状态 1未发布  2发布
            'createUser' : $("#createUser").val(),
            'createTime' : $("#createTime").val(),
            'operUser' : $("#operUser").val(),
            'operTime' : $("#operTime").val()
        };

        _this.ajax(params);
        //数据保存

    };

    _this.ajax = function (params) {
        $.ajax({
            url : appPath + '/super/project_create/public/save_public_project',
            dataType : 'json',
            async : false,
            type : 'post',
            data : params,
            success : function(data) {
                var result = data.code;
                if (10000 == result) {
                    var param = "?projectId="+projectId+"&projectTypeNo="+projectTypeNo+"&step="+'2';
                    window.location.href = appPath+"/super/project_create/public/create_project"+param;
                } else {
                    layer.msg('操作失敗');
                }
            }
        });
    }


    // 表单校验：1、不含考试的项目，【项目名称】【项目时间】为必填项，其他都是非必填项。
    //         2、含考试的项目，【项目名称】【项目时间】【考试时间】【考试次数为必填项】
    _this.verify = function() {
        // 错误信息
        var msg = "";
        // 项目名称
        var varProjectName = $.trim($("#projectName").val());
        if(!varProjectName){
            msg = "请填写项目名称";
            layer.msg(msg);
            return false;
        }
        if(varProjectName.length > 40){
            msg = "项目名称不得超过40个字符";
            layer.msg(msg);
            return false;
        }
        // 项目开始时间
        var datBeginTime = $.trim($("#datBeginTime").val());
        if(!datBeginTime){
            msg = "请选择项目开始时间";
            layer.msg(msg);
            return false;
        }
        // 项目结束时间
        var datEndTime = $.trim($("#datEndTime").val());
        if(!datEndTime){
            msg = "请选择项目结束时间";
            layer.msg(msg);
            return false;
        }
        if(_this.isContains(containExamType, projectTypeNo )){
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

    // 验证考试次数为整数（取整）
    _this.verifyIntRetestTime = function(value) {
        var value_ = $.trim(value);
        //value= value_.replace(/\D/g, '');
        value= Math.round(value_);
        $("#intRetestTime").val( value);
    }

    //判断字符串是否包含某字符
    _this.isContains = function(str,substr){
        return new RegExp(substr).test(str);
    }

    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }

}

var step_1 = new Step_1();