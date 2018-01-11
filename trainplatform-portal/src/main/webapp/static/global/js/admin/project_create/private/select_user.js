function Private_selectUser() {
    var _this = this;
    var page;
    _this.obj = null;
    _this.deptId;
    _this.templateArr = [];
    _this.page_size = 10;
    var companyIds = [];
    var deptIds = [];

    // init
    _this.init = function(_page) {
        page = _page;
        //_this.initDepartmentTree();
        _this.initListTable();
          /*setTimeout(function(){
            $('.checked input[type="checkbox"]').prop('checked',true);
            $('.table_inner').getNiceScroll().resize();

          },3000)*/
        _this.initEvent();
    }

    _this.initListTable = function () {
        var companyId = $("#companyId").val();
        $("#deptIds").val(companyId);
        var url = appPath + "/popup/deptUserList";
        _this.initTable(url);
    }

    //模糊查询
    _this.treeSearch=function (){

        var selectedNodes = $.fn.zTree.getZTreeObj("group_tree").getSelectedNodes();
        var treeNode = selectedNodes[0];
        // 获取左侧选中节点
        var companyId = (treeNode.companyId)?treeNode.companyId:"";
        var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
        var type = treeNode.type;
        var id = treeNode.id.toString();
        $("#departmentId").attr("value",departmentId);
        var url ="";
        var array = [];
        _this.getAllchildrenNodes(array,treeNode,type);
        if("1"==type ||"2"==type){
            if($("#recursion").prop("checked")){
                companyIds = array;
                $("#companyIds").val(companyIds);
                url = appPath + "/popup/userList";
            }else{
                $("#deptIds").val(companyId);
                url = appPath + "/popup/deptUserList";
            }
        }
        if("3"==type){
            if($("#recursion").prop("checked")){
                deptIds = array;
                $("#deptIds").val(deptIds);
            }else{
                $("#deptIds").val(departmentId);

            }
            //deptIds = array;
            // $("#deptIds").val(departmentId);
            url = appPath + "/popup/deptUserList";
        }
        _this.initTable(url);
        _this.select_after();
    };

    //获取么一节点下的所有子节点
    _this.getAllchildrenNodes = function (array,treeNode,type) {
        array.push(treeNode.id);
        if(treeNode.isParent){
            var childrenNodes = treeNode.children;
            if(childrenNodes){
                var len = childrenNodes.length;
                if(type == '3'){
                    for(var i = 0; i<len; i++){
                        _this.getAllchildrenNodes(array,childrenNodes[i],type);
                    }
                }else{
                    for(var j = 0; j<len; j++){
                        if(childrenNodes[j].type == '3'){
                            continue;
                        }
                        _this.getAllchildrenNodes(array,childrenNodes[j],type);
                    }
                }
            }
        }
        return array;
    }

    _this.initTable = function(list_url) {
        _this.obj = null;
        var pageArr=[10,50,100,500];
        page.init("select_user_form", list_url, "select_user_list", "select_user_page", 1, _this.page_size,pageArr);
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
                if(_this.selected(item.id)){
                  inner += '<td width="50" class="checked"><input type=\"checkbox\" name="userBox" data-deptname="' + item['departmentName'] + '" data-id="' + item['id'] + '"/></td>';

                }else{
                  inner += '<td width="50"><input type=\"checkbox\" name="userBox" data-deptname="' + item['departmentName'] + '" data-id="' + item['id'] + '"></td>';
                }
                inner += '<td>'+ item['userName'] + '</td>';
                inner += '</tr>';
            }

            return inner;
        }
        _this.tableInit();
    };

    _this.initEvent = function () {
        // 确定
        $("#btn_sure").click(function () {
            if(_this.templateArr.length>0){
                var value=$("#parentSource",window.parent.document).val()
                if("projectModify"==value){
                    parent.proModify.train_user_callback(_this.templateArr);
                }else if("createProject"==value){
                parent.step3.train_user_callback(_this.templateArr);
                }
                _this.closeDialog();
            }

        });

        //取消
        $("#btn_cancel").click(function () {
            _this.closeDialog();
        });
        $('#select_user_list').on('change','input',function(){
            var $this = $(this);
            var id = $this.attr('data-id');
        })


        //选择人员
        $('#select_user_list').on('change','input',function(){

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
            if( this.id == attr ){
                index = i;
                return false;
            }
        })
        return index;
    }
    //选择人员后执行
    _this.select_after = function(){
        var tbody = '';
        $.each(_this.templateArr,function(i, n){
            tbody += '<tr >';
            tbody += '<td width="80">' + (i+1) + '</td>';
            tbody += '<td>'+ this.userName + '</td>';
            tbody += '</tr>';
        })
        $('#select_user_list_copy').html( tbody );
    }
    //搜索人员
    _this.search = function() {


        //$("#checkAll").prop("checked",false);
        var _this = private_selectUser;
      $("#userName").val($.trim($("#userName").val()));
        $("#companyIds").val("");
        _this.initTable(appPath + "/popup/userList");
      //展示选中人员
        _this.select_after();
    };
    //初始化选择后的表格
    _this.tableInit = function(){
        $('#select_user_list_copy').html('<tr><td colspan="3" class="empty">暂无数据</td></tr>')
    }
    //全部
    _this.all = function() {
        var treeObj = $.fn.zTree.getZTreeObj("user_treeId");
        //treeObj.cancelSelectedNode();
        $("#departmentId").val("");
        $("#userName").val("");
        _this.initTable(appPath + "/popup/userList");
        //展示选中人员
        _this.select_after();
    };

    //全选
    _this.checkAll=function () {
        var flag=$("#checkAll").prop("checked");
        if(flag){
          $('tbody input[type="checkbox"]').prop("checked",true);
        }else{
          $('tbody input[type="checkbox"]').prop("checked",false);
        }
        if(flag && _this.obj.length>0){

            $.each(_this.obj,function(i, n){
                _this.removeRepart(this);
            })

        }else if( !flag ){

            $.each(_this.obj,function(i, n){
                var id = this.id;
                var index = _this.getIndex(_this.obj, id);
                var template_index = _this.getIndex(_this.templateArr, id);
                _this.templateArr.splice( template_index,1 );
            })

        }
        _this.select_after();
    }

    //是否选中
  _this.selected = function(courseId){
    for(var m=0; m<_this.templateArr.length; m++){
        if(_this.templateArr[m].id == courseId){
            return true;
        }
    }
    return false;
  }
    //去重
    _this.removeRepart=function (userData) {
        var flag=true;
        for (var i=0;i<_this.templateArr.length;i++){
            if(_this.templateArr[i].id==userData.id){
                flag=false;
            }
        }
        if(flag){
            _this.templateArr.push(userData) ;
        }
    }



    // tree options
    _this.tree_options = {
        id: 'department_treeId',
        url: appPath + '/admin/department/deptTree',
        setting: {
            data: {
                simpleData: {
                    enable: true
                }
            },
            view: {
                dblClickExpand: false,
                fontCss: getFontCss
            },
            callback: {
                onRightClick: function(event, treeId, treeNode){
                    this.onRightClick(event, treeId, treeNode);
                },
                onClick: function(e,treeId, treeNode){
                    // 获取左侧选中节点
                    var companyId = (treeNode.companyId)?treeNode.companyId:"";
                    var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
                    $("#departmentId").attr("value",departmentId);
                    $("#companyId").attr("value",companyId);
                    private_selectUser.initTable();
                    private_selectUser.select_after();
                },
                onRename: function(event, treeId, treeNode, isCancel){
                    this.zTreeOnRename(event, treeId, treeNode, isCancel);
                }
            }
        },
        zNodes : '',
        rMenu : ''
    };

     // 初始化树
    _this.initDepartmentTree = function(){
        $.ajax({
            url: _this.tree_options.url,
            async: false,
            type: 'post',
            data: {

            },
            success: function(data){
                var zNodes = eval(data.result);
                // 加载树
                $.fn.zTree.init($("#"+_this.tree_options.id), _this.tree_options.setting, zNodes);

            }
        });
    };






}

var private_selectUser = new Private_selectUser;

//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}