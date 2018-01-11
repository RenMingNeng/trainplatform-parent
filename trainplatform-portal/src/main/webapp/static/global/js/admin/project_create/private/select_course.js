function Private_selectCourse() {
    var _this = this;
    var page;
    _this.obj = null;
    _this.templateArr = [];
    _this.page_size = 10;

    // init
    _this.init = function(_page) {
        page = _page;
        _this.initTable();
        _this.initEvent(Object);
        _this.tableInit();
    }

    _this.initTable = function() {
        _this.obj = null;
        var list_url = appPath + "/admin/course/course_list";
        page.init("select_course_form", list_url, "select_course_list", "select_course_page", 1, _this.page_size);
        page.goPage(1);
        page.list = function(dataList){
            //$("#checkAll").prop("checked",false);
            _this.obj = dataList;
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td width="50">' + (i+1) + '</td>';
              if(_this.selected(item.course_id)){
                inner += '<td width="50" class="checked"><input type=\"checkbox\" name="courseBox" data-id="' + item['course_id'] + '"></td>';
              }else{
                inner += '<td width="50"><input type=\"checkbox\" name="courseBox" data-id="' + item['course_id'] + '"></td>';
              }
                inner += '<td>'+ item['course_no'] + '</td>';
                inner += '<td width="240">'+ item['course_name'] + '</td>';
                inner += '</tr>';
            }

            return inner;
        }
        _this.tableInit();
    };

    //初始化选择后的表格
    _this.tableInit = function(){
        $('#select_course_list_copy').html('<tr><td colspan="3" class="empty">暂无数据</td></tr>')
    }


    _this.initEvent = function () {

        // 确定
        $("#btn_sure").click(function () {

            if(_this.templateArr.length>0){
                var value=$("#parentSource",window.parent.document).val()
              if("projectModify"==value){
                  parent.proModify.submitCourse(_this.templateArr)  ;
              }else if("createProject"==value){         //创建项目
               parent.step2.submitCourse(_this.templateArr);
              }else if("trainSubject"==value){        //主题关联课程
               parent.train_subject_add.submitCourse(_this.templateArr);
              }else if("trainSubjectModify" == value){
                  parent.train_subject_modify.submitCourse(_this.templateArr);
              }
            }
            _this.closeDialog();
        });

        //取消ss
        $("#btn_cancel").click(function () {
            _this.closeDialog();
        });
        $('#select_course_list').on('change','input',function(){
            var $this = $(this);
            var id = $this.attr('data-id');
          // alert( _this.getIndex(_this.obj, 'intId', id) );
        })

        //选择课程
        $('#select_course_list').on('change','input',function(){
            var $this = $(this);
            var flag = $this.prop('checked');
            var id = $this.attr('data-id');
            var index = _this.getIndex(_this.obj, id);
            var template_index = _this.getIndex(_this.templateArr, id);
            if( flag && index != -1  ){

               // _this.templateArr.push(_this.obj[index]);

                _this.removeRepart(_this.obj[index])

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
            tbody += '<td  width="50">' + (i+1) + '</td>';
            /*tbody += '<td><input type=\"hidden\" data-id="' + this.course_id + '"></td>';*/
            tbody += '<td>'+ this.course_no + '</td>';
            tbody += '<td  width="240">'+ this.course_name + '</td>';
            tbody += '</tr>';
        })
        $('#select_course_list_copy').html( tbody );
    }

    //搜索课程
    _this.search = function() {
        //$("#checkAll").prop("checked",false);
        var _this = private_selectCourse;
      $("#name_search").val($.trim($("#name_search").val()));
        _this.initTable();
        //展示选中课程
        _this.select_after();
    };

    //全部
    _this.all = function() {
        var treeObj = $.fn.zTree.getZTreeObj("course_treeId");
        treeObj.cancelSelectedNode();
        $("#intTypeId").val("");
        $("#name_search").val("");
        _this.initTable();

        //展示选中课程
        _this.select_after();
    };
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
                var template_index = _this.getIndex(_this.templateArr, id);
                if(-1!=template_index){
                _this.templateArr.splice( template_index,1 );
                }
            })

        }
        _this.select_after();
    }

      //是否选中
      _this.selected = function(courseId){
        for(var m=0; m<_this.templateArr.length; m++){
          if(_this.templateArr[m].course_id == courseId){
            return true;
          }
        }
        return false;
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





}

var private_selectCourse = new Private_selectCourse();


//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}