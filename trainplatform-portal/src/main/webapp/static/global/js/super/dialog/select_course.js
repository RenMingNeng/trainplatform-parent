function SelectCourse() {
    var _this = this;
    var page;
    var courseIds = [];
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
        var list_url = appPath + "/super/selectCourse/public_course_list";
        page.init("select_course_form", list_url, "select_course_list", "select_course_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
        var inner = "", item;
            _this.obj = dataList;
            var len = dataList.length;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td  width="50">' + (i+1) + '</td>';
                inner += '<td  width="50"><input type=\"checkbox\" name="courseBox" data-id="' + item['course_id'] + '"></td>';
                inner += '<td>'+ item['course_no'] + '</td>';
                inner += '<td width="240">'+ item['course_name'] + '</td>';
                inner += '</tr>';
            }
        return inner;
        }

    };

    _this.initEvent = function () {
        // 确定(保存项目课程)
        $("#btn_sure").click(function () {
            var courses = _this.templateArr;
            for ( var i = 0; i < courses.length; i++) {
                courseIds.push(courses[i].course_id)
            }
            // 点击确定完成课程添加
            $.ajax({
                url : appPath + '/super/project_create/public/save_public_course',
                dataType : 'json',
                async : false,
                type : 'post',
                data : {
                    "course_project_id" : $("#projectId").val(),
                    "course_ids" : courseIds.toString()
                },
                success : function (data) {
                    var result = data.code;
                    if (10000 == result) {
                        parent.layer.close(parent.layer.getFrameIndex(window.name));
                        layer.msg('操作成功',{time:1000});
                        if('1' == $("#type").val()){
                            parent.step_2.initTable_course();
                        }else{
                            parent.updatePublicProject.initTable_course();
                        }
                    } else {
                        layer.alert('操作失敗');
                    }
                }
            });
        });

        //取消
        $("#btn_cancel").click(function () {
            _this.closeDialog();
        });

        //搜索
        $("#submit_search").click(function () {
            _this.initTable();
        });

        //全部
        $("#course_all").click(function () {
            $("#course_name").val("");
            _this.initTable();
        });

        //选择课程
        $('#select_course_list').on('change','input',function(){
            var $this = $(this);
            var flag = $this.prop('checked');
            var id = $this.attr('data-id');
            var index = _this.getIndex(_this.obj, id);
            var template_index = _this.getIndex(_this.templateArr, id);
            if( flag && index != -1  ){
                _this.templateArr.push(_this.obj[index]);

            }else if( !flag && template_index != -1 ){
                _this.templateArr.splice( template_index,1 );
            }
            _this.select_after();
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
            if( this.course_id == attr ){
                index = i;
                return false;
            }
        })
        return index;
    }
    //选择课程后执行
    _this.select_after = function(){
        var tbody = '';
        $.each(_this.templateArr,function(i, n){
            tbody += '<tr >';
            tbody += '<td   width="50">' + (i+1) + '</td>';
            tbody += '<td>'+ this.course_no + '</td>';
            tbody += '<td   width="240">'+ this.course_name + '</td>';
            tbody += '</tr>';
        })
        $('#select_course_list_copy').html( tbody );
    }


    //全选
    _this.checkAll=function () {
        var flag=$("#checkAll").prop("checked");
        $('tbody input[name="courseBox"]').prop("checked",flag);
        if(flag && _this.obj.length>0){
            $.each(_this.obj,function(i, n){
                _this.removeRepart(this);
            })
        }else if( !flag ){
            $.each(_this.obj,function(i, n){
                var id = this.course_id;
                var index = _this.getIndex(_this.obj, id);
                var template_index = _this.getIndex(_this.templateArr, id);
                _this.templateArr.splice( template_index,1 );
            })

        }
        _this.select_after();
    }

    //去重
    _this.removeRepart=function (courseData) {
        var flag=true;
        for (var i=0;i<_this.templateArr.length;i++){
            if(_this.templateArr[i].course_id==courseData.course_id){
                flag=false;
            }
        }
        if(flag){
            _this.templateArr.push(courseData) ;
        }
    }
    //关闭对话框
    _this.closeDialog =function () {
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    }

}

var selectCourse = new SelectCourse();
