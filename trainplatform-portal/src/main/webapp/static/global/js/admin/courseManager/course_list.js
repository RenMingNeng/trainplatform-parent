function Course_list() {
    var _this = this;
    var page;
    _this.obj = null;
    _this.templateArr = [];
    _this.page_size = 10;

    // init
    _this.init = function(_page) {
        page = _page;
        _this.initTable();
        _this.initEvent();
    }

    _this.initTable = function() {
        var list_url = appPath + "/admin/course/course_list";
        page.init("select_course_form", list_url, "select_course_list", "select_course_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            _this.obj = dataList;
            if(dataList == null){
                return;
            }
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td width="50">' + (i+1) + '</td>';
                inner += '<td width="50"><input type=\"checkbox\" name="courseBox" data-id="' + item['course_id'] + '"></td>';
                inner += '<td  width="100">'+ item['course_no'] + '</td>';
                inner += '<td ><span class="text-orange tooltip" data-length="15">'+ item['course_name'] + '</span></td>';
                inner += '<td width="100">'+ item['class_hour'] + '</td>';
              //inner += '<td width="100">'+ TimeUtil.getHouAndMinAndSec(item['class_hour']*60) + '</td>';
                inner += '<td width="100">'+ item['question_count'] + '</td>';
                inner += '<td width="260"><a href="javascript:course_list.info(\''+ item['course_id'] +'\');" class="a a-view">详情</a> ';
                inner += '<a href="javascript:course_list.courseView(\''+ item['course_id'] +'\');" class="a a-info">课程预览</a> ';
                inner += '<a href="javascript:course_list.questionView(\''+ item['course_id'] +'\');" class="a a-publish">题库预览</a></td>';
                inner += '</tr>';
            }
            return inner;
        }

    };



    _this.initEvent = function () {
        // 确定
        $("#btn_sure").click(function () {

            if(_this.templateArr.length>0){

               parent.createPrivateProject.submitCourse(_this.templateArr);
            }
            _this.closeDialog();
        });

        //取消
        $("#btn_cancel").click(function () {
            _this.closeDialog();
        });

        //搜索
        $("#courseName_search").click(function () {
            _this.search();
        });
       //全部
        $("#course_all").click(function () {
            _this.all();
        });
       //课程详情
        $("#course_info").click(function () {
            _this.info();
        })

       //课程移动
        $("#course_move").click(function () {
            _this.courseMove();
        });

       //课程预览
        $("#course_view").click(function () {
            _this.courseView();
        });
        //题库预览
        $("#question_view").click(function () {
            _this.questionView();
        });

        //全选
        $("#checkAll").click(function () {
            _this.courseCheckAll();
        })

    }

    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }
    //获取id索引
    _this.getIndex = function(obj, attr){
        var index = -1;
        $.each(obj,function(i, n){
            if( this.intId == attr ){
                index = i;
                return false;
            }
        })
        return index;
    }

    //搜索
    _this.search=function(){
      $("#courseName").val($.trim($("#courseName").val()));
        _this.initTable();
    }
    //全部
    _this.all = function() {
        var treeObj = $.fn.zTree.getZTreeObj("course_treeId");
        treeObj.cancelSelectedNode();
        $("#intTypeId").val("");
        $("#courseName").val("");
        _this.initTable();
    };
    //详情
    _this.info=function (courseId) {
        /*//1 检查选中的复选框是否只有一个
        var size = $("#select_course_list input:checked").length;
        if (size == 0) {
            layer.alert('请至少选择一条记录!', {icon: 2});
            return;
        } else if (size > 1) {
            layer.alert("查看条目只可选择一个", {icon: 2});
            return;
        }
       var courseId=$("#select_course_list input:checked").attr("data-id");*/
        window.location.href=appPath+"/admin/course/course_info?courseId="+courseId;

    }
    //课程预览
    _this.courseView=function (courseId) {
        /*//1 检查选中的复选框是否只有一个
        var size = $("#select_course_list input:checked").length;
        if (size == 0) {
            layer.alert('请至少选择一条记录!', {icon: 2});
            return;
        } else if (size > 1) {
            layer.alert("查看条目只可选择一个", {icon: 2});
            return;
        }
        var courseId=$("#select_course_list input:checked").attr("data-id");*/
        common.goto_student_course_view(courseId);

    }
    //题库预览
    _this.questionView=function (courseId) {
        /*//1 检查选中的复选框是否只有一个
        var size = $("#select_course_list input:checked").length;
        if (size == 0) {
            layer.alert('请至少选择一条记录!', {icon: 2});
            return;
        } else if (size > 1) {
            layer.alert("查看条目只可选择一个", {icon: 2});
            return;
        }
        var courseId=$("#select_course_list input:checked").attr("data-id");*/
        common.goto_student_course_question_view(courseId);

    }
    //课程移动
    _this.courseMove=function () {
        var courseIds=[]
        $("#select_course_list input:checked").each(function (i,e) {
            courseIds.push($(this).attr("data-id"))
        })
        if(courseIds.length==0){
            layer.alert('请至少选择一条记录!', {icon: 2});
            return;
        }
        var html = '';
        html += '<div style="padding:0px 30px;">';
        html += '<div style="padding: 10px 0px;"><input id="searchbox" class="easyui-searchbox"  placeholder="请输入名称自动检索" style="width:270px;height:30px;"/></div>';
        html += '<div style="padding: 0px 10px;"><ul id="courseType_tree" class="ztree"></ul></div>';
        html += '</div>';
        layer.open({
            type: 1,
            title: '移动课程',
            shadeClose: true,
            scrollbar: false,
            area: ['350px', '80%'],
            content: html
        });
        _this.Movetree.iniTree();
    }

      //课程全选
    _this.courseCheckAll=function(){
        var flag=$("#checkAll").prop("checked");
        $('#select_course_list input[type="checkbox"]').prop("checked",flag);
    }

    // 移动树
        _this.Movetree = {
            setting : {
                view : {
                    dblClickExpand : false,
                    fontCss : getFontCss
                },
                callback : {
                    onClick : function(e, treeId, treeNode) {
                        _this.Movetree.onClick(treeNode.id);
                    }
                }
            },
            onClick : function(courseTypeId) {
                layer.confirm('您确定要移动吗？', {
                    icon : 3,
                    btn : [ '移动', '取消' ]
                    // 按钮
                }, function() {
                    var courseIds=[]
                    $("#select_course_list input:checked").each(function (i,e) {
                        courseIds.push($(this).attr("data-id"))
                    })
                    course_list.move(courseIds, courseTypeId);
                });
            },
            iniTree : function() {
                var url = appPath + '/courseType/courseType_tree';
                var result = _this.ajax(url);
                var zNodes = eval(result);
                $.fn.zTree.init($("#courseType_tree"), _this.Movetree.setting,
                    zNodes);
            }
        };

// 执行移动
    _this.move = function(courseIds, courseTypeId) {
        var param = {
            "courseIds" : courseIds.join(","),
            "courseTypeId" : courseTypeId
        };
        var url = appPath+"/admin/course/course_move";
        var result = _this.ajax(url, param);
        if ("10000"==result) {
            layer.alert('操作成功', {
                icon : 1
            }, function(index) {
                layer.close(index);
                course_list.search();
                layer.close(1);
                $("#checkAll").prop("checked",false);
                return true;
            });
        } else {
            layer.alert('操作失败', {
                icon : 2
            });
        }
    };
    //全选
    _this.checkAll=function () {
        var flag=$("#checkAll").prop("checked");
        $('input[name="courseBox"]').attr("checked",flag);
        if(flag && _this.obj.length>0){

            $.each(_this.obj,function(i, n){
                _this.removeRepart(this);
            })
            /*for(var i=0; i<_this.obj;i++){
               _this.removeRepart(_this.obj[i]);
            }*/

        }else if( !flag ){

            $.each(_this.obj,function(i, n){
                var id = this.intId;
                var index = _this.getIndex(_this.obj, id);
                var template_index = _this.getIndex(_this.templateArr, id);
                _this.templateArr.splice( template_index,1 );
            })

        }
        _this.select_after();
    }

    _this.ajax = function(url, param, type){
        var result;
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function(data){
                result = data.result;
            }
        });
        return result;
    };

}

var course_list = new Course_list();


//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}