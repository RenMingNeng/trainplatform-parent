/**
 * 创建公开型项目第二步
 */
function Step_2() {
    var _this = this;
    var page;
    var projectId = $("#id").val();                 // 项目编号
    var projectTypeNo = $("#projectTypeNo").val();  // 项目类型编号
    _this.page_size = 10;
    _this.index = -1;
    _this.maxSelectQuestion = common.getMaxSelectQuestion();

    _this.init = function (_page) {
        page = _page;
        _this.initEvent();
        _this.initTable_course();
    }

    _this.initEvent = function() {
        //选择培训课程弹窗
        $("#btn_select_course").click(function() {
            _this.index = layer.open({
                type : 2,
                title : '选择培训课程',
                shadeClose : false, // true点击遮罩层关闭
                shade : 0.3, // 遮罩层背景
                area : [ '1200px', '80%' ], // 弹出层大小
                scrollbar : false, // false隐藏滑动块
                content : [appPath+ '/popup/selectTrainCourse?projectId='+projectId+"&type=1",'yes' ]
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
                            'projectId' : $("#id").val(),
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

        // 搜索课程
        $("#btn_search_course").click(function() {
            _this.initTable_course();
        });

        // 全部课程
        $("#btn_all_course").click(function() {
            $("#course_name").val("");
            _this.initTable_course();
        });
        // 回到首页
        $("#index").click(function() {
            window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
        });

        // 点击上一步
        $("#step_1").click(function() {
            var param = "?projectId="+projectId+"&projectType="+projectTypeNo+"&projectStatus="+$("#projectStatus").val();
            window.location.href = appPath+"/super/project_create/public/project_update"+param;
        });

        // 点击下一步
        $("#step_3").click(function() {
            var param = "?projectId="+projectId+"&projectTypeNo="+projectTypeNo+"&step="+'3';
            window.location.href = appPath+"/super/project_create/public/create_project"+param;
        });
    }

    //课程列表
    _this.initTable_course = function() {
        var list_url = appPath + "/super/project_create/public/course_list";
        page.init("course_form", list_url, "course_list", "course_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
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
                inner += '<td>';
                inner += '<a href="javascript:;" onclick=\"step_2.course_view(\'' + item.course_id + '\');\" class="a a-info">课程预览</a>';
                inner += '<a href="javascript:;" onclick=\"step_2.question_view(\'' + item.course_id + '\');\" class="a a-view">题库预览</a>';
                inner += '</td>';
                inner += '</tr>';
            }
            return inner;
        }
    };

    //判断字符串是否包含某字符
    _this.isContains = function(str,substr){
        return new RegExp(substr).test(str);
    }

    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
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

var step_2 = new Step_2();