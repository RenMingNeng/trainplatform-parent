function SelectUser_SignUp() {
    var _this = this;
    var page;
    _this.obj = null;
    _this.templateArr = [];
    _this.page_size = 10;
    var companyIds = [];
    var deptIds = [];

    // init
    _this.init = function(_page) {
        page = _page;
        window.groupTree_search = selectUser_SignUp.search;
        groupTree.init();
        _this.treeSearch();
        _this.initListTable();
        _this.initEvent();
    }

    _this.initListTable = function () {
        var companyId = $("#companyId").val();
        companyIds.splice(0,companyIds.length);
        companyIds.push(companyId);
        $("#companyIds").val(companyIds);
        var url = appPath + "/popup/userList";
        _this.initTable(url);
    }

    // 点击刷新人员列表
    _this.search = function (){
        // 获取选中的树节点
        var selectedNodes = $.fn.zTree.getZTreeObj("group_tree").getSelectedNodes();
        var selectedNode = selectedNodes[0];
        selectUser_SignUp.treeSearch(selectedNode);
    }

    _this.initEvent = function () {
        // 确定
        $("#btn_sure").click(function () {
            var userIds = [];
            for(var i=0; i<_this.templateArr.length; i++){
                if(_this.templateArr[i].id){
                    var obj=new Object();
                    obj.id=_this.templateArr[i].id;
                    obj.userName=_this.templateArr[i].userName;
                    obj.deptName=_this.templateArr[i].departmentName==null?"":_this.templateArr[i].departmentName;
                    userIds.push(obj);
                }
            }
            userIds =JSON.stringify(userIds);
            var roleId = "-1";
            var roleName="默认角色"
            $.ajax({
                url: appPath + '/admin/project_create/private/save_project_user',
                async: false,
                type: 'post',
                data: {
                    'id': $("#projectId").val(),     // 项目id
                    'roleId': roleId,                // 受训角色id
                    'roleName': roleName,            // 受训人员ids
                    'users': userIds,
                    'projectType': $("#projectType").val()
                },
                success: function(data){
                    var code = data['code'];
                    if(code==10000){
                        layer.msg('报名成功', {time:1000},function () {
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                            // 刷新项目列表
                            parent.admin_index.initTable_public();
                        });
                    }else{
                        layer.msg('操作失敗', {time:1500},function () {
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                        });
                    }
                }
            });

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

    //模糊查询
    _this.treeSearch=function (){
        var selectedNodes = $.fn.zTree.getZTreeObj("group_tree").getSelectedNodes();
        var treeNode = selectedNodes[0];
        // 获取左侧选中节点
        var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
        var type = treeNode.type;
        var id = treeNode.id.toString();
        companyIds.splice(0,companyIds.length);
        companyIds = _this.getAllchildrenNodes(treeNode);
        $("#departmentId").attr("value",departmentId);
        $("#companyIds").val(companyIds);
        var url ="";
        if("1"==type ||"2"==type){
            url = appPath + "/popup/userList";
        }
        if("3"==type){
            deptIds.splice(0,deptIds.length);
            deptIds = _this.getAllchildrenNodes(treeNode);
            $("#deptIds").val(deptIds);
            url = appPath + "/popup/deptUserList";
        }
        _this.initTable(url);
    };

    //获取么一节点下的所有子节点
    _this.getAllchildrenNodes = function (treeNode) {
        var array = [];
        array.push(treeNode.id);
        if(treeNode.isParent){
            var childrenNodes = treeNode.children;
            if(childrenNodes){
                for(var i = 0; i<childrenNodes.length; i++){
                    array.push(childrenNodes[i].id);
                }
            }
        }
        return array;
    }

    // 人员列表
    _this.initTable = function(list_url) {
        _this.obj = null;
        $("#checkAll").prop("checked",false);
        var pageArr=[10,50,100,500];
        page.init("select_user_form", list_url, "select_user_list", "select_user_page", 1, _this.page_size,pageArr);
        page.goPage(1);
        page.list = function(dataList){
            _this.obj = dataList;
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td width="50">' + (i+1) + '</td>';
                inner += '<td width="50"><input type=\"checkbox\" name="userBox" data-deptname="' + item['departmentName'] + '" data-id="' + item['id'] + '"></td>';
                inner += '<td>'+ item['userName'] + '</td>';
                inner += '</tr>';
            }
            return inner;
        }
        _this.tableInit();
    };


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

    //初始化选择后的表格
    _this.tableInit = function(){
        $('#select_user_list_copy').html('<tr><td colspan="3" class="empty">暂无数据</td></tr>')
    }
    //全部
    _this.searchAll = function() {
        $("#userName").val("");
        selectUser_SignUp.initListTable(appPath + "/popup/userList");
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

}

var selectUser_SignUp = new SelectUser_SignUp();

// 获取根节点集合
function getRoots(){
    var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
    return treeObj.getNodesByFilter(function(node){return node.level==0;});
}

//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}