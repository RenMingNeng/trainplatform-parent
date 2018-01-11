/**
 * Created by Administrator on 2017-07-31.
 */
function UpdatePublicProject() {
    var _this = this;
    var page1,page2;
    var containTrainType,containExamType,containExerciseType;
    var projectTypeNo,projectStatus;
    _this.courses  = null;
    _this.page_size = 10;
    _this.index = -1;

    // init
    _this.init = function(_page1,_page2,options) {
        page1 = _page1; page2 = _page2;
        //含有培训的
        containTrainType = options.containTrainType;
        //含有考试的
        containExamType = options.containExamType;
        //含有练习的
        containExerciseType = options.containExerciseType;
        projectTypeNo = $("#projectTypeNo").val();  //项目类型
        projectStatus = $("#projectStatus").val();  //项目状态
        _this.initEvent();
        _this.initTable_course(1);
        _this.initTable_company();
        _this.initTimeBtn();
    }

    _this.initEvent = function() {

        // 弹窗-选择培训课程
        $("#btn_select_course").click(function() {
            _this.index = layer.open({
                type : 2,
                title : '选择培训课程',
                shadeClose : false, // true点击遮罩层关闭
                shade : 0.3, // 遮罩层背景
                area : [ '1200px', '80%' ], // 弹出层大小
                scrollbar : false, // false隐藏滑动块
                content : [appPath+ '/popup/selectTrainCourse?projectId='+$("#projectId").val()+"&type=2",'yes' ]
            });
        });

        // 弹窗-选择培训单位
        $("#btn_select_company").click(function() {
            layer.open({
                type : 2,
                title : '选择培训单位',
                shadeClose : false, // true点击遮罩层关闭
                shade : 0.3, // 遮罩层背景
                area : [ '1200px', '80%' ], // 弹出层大小
                scrollbar : false, // false隐藏滑动块
                content : [appPath+ '/popup/selectTrainCompany?projectId='+$("#projectId").val()+"&type=2",'yes' ]
            });
        });


        // 批量删除课程
        $("#btn_delete_batch_course").click(function() {
            var $checkboxs = $('#course_list input:checked');
            var course_ids = [];
            $.each($checkboxs,function(i,n){
                var $this = $(this);
                course_ids.push( $this.data('id') );
            })
            if(course_ids.length < 1){
                layer.alert("至少选择一条数据",{icon:7})
            }else{
                layer.confirm('确定删除？', {
                    icon : 3,
                    btn: ['确定','取消']        //按钮
                }, function(){
                    $.ajax({
                        url : appPath + '/super/project_create/public/delete_project_course',
                        dataType : 'json',
                        async : false,
                        type : 'post',
                        data : {
                            'projectId' : $("#projectId").val(),
                            'courseIds' : course_ids.join(',') 		    //要删除的课程id
                        },
                        success : function(data) {
                            var result = data.code;
                            if (10000 == result) {
                                layer.alert('操作成功', {icon : 1},function (index) {
                                    layer.close(index);
                                    //刷新课程列表
                                    _this.initTable_course();
                                })
                            } else {
                                layer.alert('操作失敗', {icon : 2});
                            }
                        }
                    });
                }, function(){
                   //取消
                });
            }

        });

        // 批量删除单位
        $("#btn_delete_batch_company").click(function() {
            var $checkboxs = $('#company_list input:checked');
            var company_ids = [];
            $.each($checkboxs,function(i,n){
                var $this = $(this);
                company_ids.push( $this.data('id') );
            })
            console.log(company_ids)
            if(company_ids.length < 1){
                layer.alert("至少选择一条数据",{icon:7})
            }else{
                layer.confirm('确定删除？', {
                    icon : 3,
                    btn: ['确定','取消']        //按钮
                }, function(){
                    $.ajax({
                        url : appPath + '/super/project_create/public/delete_project_company',
                        dataType : 'json',
                        async : false,
                        type : 'post',
                        data : {
                            'projectId' : $("#projectId").val(),
                            'companyIds' : company_ids.join(',') 		    //要删除的单位id
                        },
                        success : function(data) {
                            var result = data.code;
                            if (10000 == result) {
                                layer.alert('操作成功', {icon : 1},function (index) {
                                    layer.close(index);
                                    //刷新课程列表
                                    _this.initTable_company();
                                })
                            } else {
                                layer.alert('操作失敗', {icon : 2});
                            }
                        }
                    });
                }, function(){
                    //取消
                });
            }

        });

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

        // 保存项目
        $("#btn_save").click(function() {
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
            trainBeginTime = $("#trainBeginTime").val();
            trainEndTime = $("#trainEndTime").val();
            project_train_Time = trainBeginTime+" 至 "+trainEndTime;
        }
        // 含练习
        if(_this.isContains('2467',projectTypeNo)){
            exerciseBeginTime = $("#exerciseBeginTime").val();
            exerciseEndTime = $("#exerciseEndTime").val();
            project_exercise_Time = exerciseBeginTime+" 至 "+exerciseEndTime;
        }
        // 含考试
        if(_this.isContains('3567',projectTypeNo)){
            examBeginTime = $("#examBeginTime").val();
            examEndTime = $("#examEndTime").val();
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
                    layer.alert('操作成功', {closeBtn:false,icon : 1,skin : 'layer-ext-moon'},function (index) {
                        layer.close(index);
                    })
                } else {
                    layer.alert('操作失敗', {icon : 2,skin : 'layer-ext-moon'});
                }
            }
        });
    };

    // 时间验证校验
    _this.verify = function() {
        // 错误信息
        var msg = "";
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

    //判断字符串是否包含某字符
    _this.isContains = function(str,substr){
        return new RegExp(substr).test(str);
    }


    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }

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
                inner += '<td><input type=\"checkbox\" data-id=\"'+item.course_id+'\" name="courseBox" ></td>';
                inner += '<td>'+ item['course_no'] +'</td>';
                inner += '<td>'+ item['course_name'] +'</td>';
                inner += '<td>'+ item['role_name'] + '</td>';
                if (_this.isContains('1457', projectTypeNo)) {
                    inner += '<td>'+item['class_hour']+'</td>';
                    inner += '<td id="editRequirement">';
                    if("1" == projectStatus || "2" == projectStatus ) {
                        inner += '<a href="javascript:;" class="editable editable-click" onmouseover="updatePublicProject.editRequirement(this,\''+ item.id +'\')" >' + item['requirement'] + '</a>'; //学时要求
                    }else{
                        inner += '<a href="javascript:;" data-type="text" data-pk="1"  >' + item['requirement'] + '</a>'; //学时要求
                    }
                    inner += '</td>';
                }
                if (_this.isContains('234567', projectTypeNo)) {
                    inner += '<td>'+item['question_count']+'</td>';
                }
                if (_this.isContains('3567', projectTypeNo)) {
                    inner += '<td id="edit">';
                    if("1" == projectStatus || "2" == projectStatus ) {
                        inner += '<a href="javascript:;" class="editable editable-click"  onmouseover="updatePublicProject.editSelectCount(this,\''+ item.id +'\','+ item.question_count+')" >' + item['select_count'] + '</a>'; //必选题量
                    }else{
                        inner += '<a href="javascript:;" data-type="text" data-pk="1"  >' + item['select_count'] + '</a>'; //必选题量
                    }
                    inner += '</td>';
                }
                inner += '<td>';
                inner += '<a href="javascript:;" class="a a-info">课程预览</a>';
                inner += ' <a href="javascript:;" class="a a-view">题库预览</a>';
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

    //验证考试次数为整数
    _this.verifyIntRetestTime = function(value) {
        var value_ = $.trim(value);
        //value= value_.replace(/\D/g, '');
        value= Math.round(value_);
        $("#intRetestTime").val( value);
    }

    //设置学时要求
    _this.editRequirement = function (obj,id) {
        var projectId = $("#course_project_id").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
            validate:function (value) {
                var param={
                    "projectId": projectId,
                    "id": id,
                    "requirement": value
                }
               // console.log(param)
                _this.edit_ajax(param,obj);
            }
        })
    };

    //设置必选题量
    _this.editSelectCount = function (obj,id,questionCount) {
        var projectId = $("#course_project_id").val();
        $.fn.editable.defaults.mode = 'popup';
        $(obj).editable({
            editBy:'click',
            validate:function (value) {
                if(value>questionCount){
                    value=questionCount;
                    return "题量不合法";
                }
                var param={
                    "projectId": projectId,
                    "id": id,
                    "selectCount": value
                }
               // console.log(param)
                _this.edit_ajax(param,obj);
                return false;
            },
            display:function (value) {
                if(value > questionCount || value < 0 ){
                    $(this).text(questionCount);
                }else {
                    $(this).text(value);
                }
            }
        })
    };

    _this.edit_ajax = function(param,obj){
        console.log(param)
        $.ajax({
            url : appPath + '/super/project_create/public/edit_ajax',
            dataType : 'json',
            async : false,
            type : 'post',
            data : param,
            success : function (data) {
                var result = data.code;
                if('10000'==result){
                    layer.alert("设置成功",{icon:1});
                }else{
                    layer.alert("设置失败",{icon:2});
                    $(obj).text()
                }
            }
        });
    };

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

var updatePublicProject = new UpdatePublicProject();