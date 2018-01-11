/**
 * Created by Administrator on 2017-07-31.
 */
function CreatePublicProject() {
    var _this = this;
    var page1,page2;
    var containTrainType,containExamType,containExerciseType;
    var projectTypeNo = $("#projectTypeNo").val();  //项目类型
    _this.courses  = null;
    _this.page_size = 10;
    _this.index = -1;

    // init
    _this.init = function(_page1,_page2,options) {
        page1 = _page1;page2 = _page2;
        //含有培训的
        containTrainType = options.containTrainType;
        //含有考试的
        containExamType = options.containExamType;
        //含有练习的
        containExerciseType = options.containExerciseType;

        _this.initEvent();
        _this.initTable_course(1);
        _this.initTable_company();
        //_this.init_btn();
    }

    _this.initEvent = function() {
        $("#btn_project_publish").addClass('unclick');
        // 弹窗-选择培训课程
        $("#btn_select_course").click(function() {
            _this.index = layer.open({
                type : 2,
                title : '选择培训课程',
                shadeClose : false, // true点击遮罩层关闭
                shade : 0.3, // 遮罩层背景
                area : [ '1200px', '80%' ], // 弹出层大小
                scrollbar : false, // false隐藏滑动块
                content : [appPath+ '/popup/selectTrainCourse?projectId='+$("#id").val()+"&type=1",'yes' ]
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
                content : [appPath+ '/popup/selectTrainCompany?projectId='+$("#id").val()+"&type=1",'yes' ]
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
        $("#btn_all_course").click(function() {
            $("#course_name").val("");
            _this.initTable_course();
        });

        // 搜索单位
        $("#btn_search_company").click(function() {
            _this.initTable_company();
        });

        // 全部单位
        $("#btn_all_company").click(function() {
            $("#company_name").val("");
            _this.initTable_company();
        });

        // 保存项目
        $("#btn_save").click(function() {
            var $this = $(this);
            if( !$this.hasClass('unclick') ){
                $this.addClass('unclick');
                $("#btn_project_publish").removeClass('unclick');
                projectStatus = '1';
                _this.projectSave(projectStatus);
                //考试项目有组卷策略
                var projectId = $("#id").val();
                if(_this.isContains(containExamType,projectTypeNo)){
                    stragegySelect.stragegy_listener();
                }
            }
        });
    }

    //发布
    $("#btn_project_publish").click(function(){
        var $this = $(this);
        if( !$this.hasClass('unclick') ){
            layer.confirm('确定执行操作？',{
                icon: 3,
                btn: ['确定','取消']      //按钮
            }, function(index){
                $.ajax({
                    url : appPath + '/super/project_create/public/project_publish',
                    dataType : 'json',
                    async : false,
                    type : 'post',
                    data : {"project_id" : $("#id").val()},
                    success : function (data) {
                        var result = data.code;
                        if('10000' == result){
                            layer.alert("操作成功",{icon: 1});
                            layer.close(index);
                            $this.addClass('unclick')
                        }else{
                            layer.alert("操作失败",{icon: 2});
                        }
                    }

                });
            });
        }
    });

    // 保存项目
    _this.projectSave = function(projectStatus) {
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
            'id' : $("#id").val(),
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
                    layer.alert('操作成功', {closeBtn:false,icon : 1,skin : 'layer-ext-moon'},function (index) {
                        layer.close(index);
                       if( $('.index') ){
                           $('.index').show();
                       }

                    })
                } else {
                    layer.alert('操作失敗', {icon : 2,skin : 'layer-ext-moon'});
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
                inner += '<tr >';
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
                        inner += '<a href="javascript:;" data-type="text" data-pk="1">' + item['requirement'] + '</a>'; //学时要求
                    }
                    inner += '</td>';
                }
                if (_this.isContains('234567', projectTypeNo)) {
                    inner += '<td>'+item['question_count']+'</td>';
                }
                if (_this.isContains('3567', projectTypeNo)) {
                    inner += '<td id="edit">';
                    if("1" == projectStatus || "2" == projectStatus ) {
                        inner += '<a href="javascript:;" class="editable editable-click" onmouseover="updatePublicProject.editSelectCount(this,\''+ item.id +'\','+ item.question_count+')" >' + item['select_count'] + '</a>'; //必选题量
                    }else{
                        inner += '<a href="javascript:;" data-type="text" data-pk="1">' + item['select_count'] + '</a>'; //必选题量
                    }
                    inner += '</td>';
                }
                inner +=  '<td>';
                inner += '<a href="javascript:;" class="link">课程预览</a>';
                inner += ' <a href="javascript:;" class="link">题库预览</a>';
                inner += '</td>';
                inner += '</tr>';
            }
        return inner;
        }

        if(_this.isContains(containExamType,projectTypeNo)){
            stragegySelect.stragegy_select($("#id").val(), _this.isContains(containTrainType,projectTypeNo));
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

    //验证考试次数为整数
    _this.verifyIntRetestTime = function(value) {
        var value_ = $.trim(value);
        //value= value_.replace(/\D/g, '');
        value= Math.round(value_);
        $("#intRetestTime").val( value);
    }

    //按钮初始化
    _this.init_btn = function () {
        $("#btn_select_course").addClass("unclick");
        $("#btn_delete_batch_course").addClass("unclick");
        $("#btn_select_company").addClass("unclick");
        $("#btn_delete_batch_company").addClass("unclick");
    }


}

var createPublicProject = new CreatePublicProject();