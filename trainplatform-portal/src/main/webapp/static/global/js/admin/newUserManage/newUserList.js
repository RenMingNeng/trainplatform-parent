/**
 * Created by Administrator on 2017/7/24.
 */
var newUserList = new Object();
var page;
var company_name;
var companyIds = [];
var deptIds = [];
var comPIds = [];
var userObject = [];

    // 初始化
newUserList.init = function(page){
        page=page;
    window.groupTree_search = newUserList.search;
    window.groupTree_rightClick = newUserList.onRightClick;
    groupTree.init();
    newUserList.addDefaultNode();
    this.initTable();
 };

newUserList.initTable = function () {
    var companyId = $("#companyId").val();
    $("#deptIds").val(companyId);
    var rootNode = getRoots()[0];
    newUserList.getAllchildrenNodes(comPIds,rootNode,rootNode.type);
   var url = appPath + "/popup/deptUserList";
    newUserList.initListTable(url);
};

newUserList.initListTable= function(url){
    var list_url = url;

    /*
     * 分页
     * @param param_form 参数form id
     * @param list_url 请求链接
     * @param tbody_id 列表div id
     * @param page_id 分页id
     * @param pageNum 页数
     * @param pageSize 每页条数
     */
    page.init("userListForm", list_url, "user_tbody", "user_page", 1, 10);

    //初始化
    page.goPage(1);

    //重写组装方法
    page.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        for(var i=0; i< len; i++) {
            item = dataList[i];
            // 组装数据
            inner += "<tr>";
            inner += "<td width='50'>"+(parseInt(i)+1)+"</td>";
            inner += "<td width='50' ><input type='checkbox' id='userId_"+i+"' title='"+item.userType+"' name='checkUser' value='"+item.id+"' companyId='"+item.companyId+"' userName='"+item.userName+"' /></td>";
            inner += "<td width='100'><span class=\"text-orange\">"+item.userName+"</span></td>";
            inner += "<td width='120'>"+item.userAccount+"</td>";
            inner += "<td width='150'>"+item.companyName+"</td>";
            var dept_name = item.departmentName;
            if(dept_name == '' || dept_name==undefined || dept_name==null){
                inner += "<td width='107'></td>";
            }else{
                inner += "<td width='107'>"+item.departmentName+"</td>";
            }
            inner += "<td width='92'>"+TimeUtil.longMsTimeConvertToDateTime(item.registDate)+"</td>";
            inner += "<td width='220'><a href=javascript:userInfo('"+item.id+"') class='a a-view'>详情</a>&nbsp;";
            var isable = item.isValid;
            var uType=item.userType;
            if("1"==isable){
                if(uType.indexOf('1')!=-1 || uType.indexOf('2')!=-1){
                    inner += "<a href=javascript:void(0) class='a'>修改</a>&nbsp;";
                }else{
                    inner += "<a href=javascript:modifyPassWord('"+item.id+"') class='a a-info'>修改</a>&nbsp;";
                }
            }else{
                inner += "<a href=javascript:; class='a'>修改</a>&nbsp;";
            }

            if(uType.indexOf('1')!=-1 || uType.indexOf('2')!=-1){
                inner += "<a href=javascript:void(0) class='a'>删除</a>&nbsp;";
            }else{
                inner += "<a href=javascript:deleteUser('"+item.id+"') class='a a-close'>删除</a>&nbsp;";
            }
            inner += "</td>";
            inner += "</tr>";
        }
        return inner;
    }

};
newUserList.search = function (treeNode){
    var selectedNodes = $.fn.zTree.getZTreeObj("group_tree").getSelectedNodes();
    var selectedNode = selectedNodes[0];
    newUserList.treeSearch(selectedNode);
};

    //模糊查询
newUserList.treeSearch = function (treeNode){
    // 获取左侧选中节点
    var companyId = (treeNode.companyId)?treeNode.companyId:"";
    var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
    var departmentName = (treeNode.name)?treeNode.name:"";
    var type = treeNode.type;
    var id = treeNode.id.toString();
    $("#departmentId").attr("value",departmentId);
    $("#departmentName").attr("value",departmentName);
    var url ="";
    var array = [];
    newUserList.getAllchildrenNodes(array,treeNode,type);
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
        url = appPath + "/popup/deptUserList";
        if($("#recursion").prop("checked")){
            if(departmentId == '-1'){
                $("#companyIds").val(comPIds);
                url = appPath + "/popup/userList";
            }else{
                deptIds = array;
                $("#deptIds").val(deptIds);
            }
        }else{
            $("#deptIds").val(departmentId);
        }
    }
    newUserList.initListTable(url);
    };

    //获取某一节点下的所有子节点
    newUserList.getAllchildrenNodes = function (array,treeNode,type) {
        array.push(treeNode.id);
        if(treeNode.isParent){
            var childrenNodes = treeNode.children;
            if(childrenNodes){
                var len = childrenNodes.length;
                if(type == '3'){
                    for(var i = 0; i<len; i++){
                        newUserList.getAllchildrenNodes(array,childrenNodes[i],type);
                    }
                }else{
                    for(var j = 0; j<len; j++){
                        if(childrenNodes[j].type == '3'){
                            continue;
                        }
                        newUserList.getAllchildrenNodes(array,childrenNodes[j],type);
                    }
                }
            }
        }
        return array;
    };

    //查询全部
    newUserList.searchAll=function(){
        $("#userName").val("");
        $("#userAccount").val("");
        newUserList.search();
    };


    //搜索高亮
    function getFontCss(treeId, treeNode){
        return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
    }

    // 获取根节点集合
    function getRoots(){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        return treeObj.getNodesByFilter(function(node){return node.level==0;});
    }


newUserList.getRole = function(){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        var rootNode = this.getRoots()[0];
        treeObj.selectNode(rootNode);
        treeObj.setting.callback.onClick(null, tree_options.id, rootNode);
    };


/**
 * 导入
 */
newUserList.importUser=function(){
    // 获取部门id
    var departmentId ="";
    var companyId = "";
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length!=0){
        companyId = selectedNodes[0].companyId;
        if(selectedNodes[0].departmentId){
            departmentId = selectedNodes[0].departmentId;
        }

    }
     if(departmentId=="-1"){
     layer.msg('离职人员暂不支持人员导入！');
     return;
     }
    // 导入模板类型 1：学员模板  2：角色模板
    //exportTye 1调用学员导出方法 2调用角色导出方法
    var tempType ="1";
    var params = new Object();
    params.departmentId = departmentId;
    params.companyId = companyId;
    layer.open({
        type : 2,
        title : '导入excel',
        shadeClose : false,				//true点击遮罩层关闭
        shade : 0.3,					//遮罩层背景
        maxmin : true, 					// 开启最大化最小化按钮
        area : [ '1000px', '80%' ],		//弹出层大小
        scrollbar : false,				//false隐藏滑动块
        content : [ appPath + '/admin/excelImport/to_import?tempType='+tempType+'&exportTye=1'+'&params='+encodeURIComponent(JSON.stringify(params)), 'yes' ],
        cancel: function(){
            //刷新数据
            returnUrl();
        }
    })
};

//全选与非全选
$("#checkAll").click(function(){
    if(this.checked){
        $("#user_tbody :checkbox").prop("checked", true);
    }else{
        $("#user_tbody :checkbox").prop("checked", false);
    }
});


/*userList.exportUser = function(){
 if(page.count != 0){
 userList.to_exportUser();
 }else{
 layer.msg("没有可以导出的数据！");
 }
 };*/

/**
 * 导出
 */
newUserList.exportUser = function () {
    // 获取部门id
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    var treeNode = selectedNodes[0];
    var companyId = (treeNode.companyId)?treeNode.companyId:"";
    var depId = (treeNode.departmentId)?treeNode.departmentId:"";
    var departmentName = (treeNode.name)?treeNode.name:"";
    var type = treeNode.type;
    var array = new Array;
    var pkidObj = document.getElementsByName("checkUser");
    for (var j = 0; j < pkidObj.length; j++) {
        if (pkidObj.item(j).checked == true) {
            array.push(pkidObj.item(j).value);
        }
    }
    var arr = array.join(",");
    var fileName = new Date().getTime() + ".xlsx";

    var flag= $("#recursion").prop("checked");

    companyIds = [];
    if(null != arr && '' != arr){
        window.location.href = appPath + '/admin/excelExport/export?fileName=' + fileName + '&params=' + arr + "&exportTye=1"+"&flag="+flag;
    }else if( "3" == type){  //导出部门人员
        if(flag && '-1' == depId){
            companyIds.push(comPIds);
        }else{
            companyIds.push(companyId);
        }
        //询问框
        layer.confirm('您想要导出'+ '<span style="color: blue;font-weight: bold">'+departmentName+'</span>' +'部门下的人员？', {
            btn: ['确定', '取消'], //按钮
            icon : 3
        }, function (index) {
            window.location.href = appPath + '/admin/excelExport/export?fileName=' + fileName + '&depId=' + depId + '&companyIds=' + companyIds + "&exportTye=1"+"&flag="+flag;
            layer.close(index);
        }, function () {
            //取消
        });
    }else{

        if(flag){
            companyIds = newUserList.getAllchildrenNodes(companyIds,treeNode,type);
        }else{
            companyIds.push(companyId);
        }


        //询问框
        layer.confirm('您想要导出全公司人员？', {
            btn: ['确定', '取消'], //按钮
            icon : 3
        }, function (index) {
            window.location.href = appPath + '/admin/excelExport/export?fileName=' + fileName + "&exportTye=1" + "&companyIds=" + companyIds+"&flag="+flag;
            layer.close(index);
        }, function () {
            //取消
        });
    }
};

/**
 * 转移
 */
newUserList.moveUser=function(){
    // 获取选择人员
    var array = new Array;
    var obj;
    var pkidObj = document.getElementsByName("checkUser");
    for ( var j = 0; j < pkidObj.length; j++) {
        if (pkidObj.item(j).checked == true) {
            obj = new Object();
            obj.id = pkidObj.item(j).value;
            obj.companyId = pkidObj.item(j).getAttribute("companyId");
            obj.userType = pkidObj.item(j).getAttribute("title");
            obj.userName = pkidObj.item(j).getAttribute("userName");
            array.push(obj);
        }
    }
    userObject = array;
    if(array.length==0){
        layer.msg("未勾选人员");
        return;
    }
    var userIds = encodeURI(JSON.stringify(array));
    // 弹窗-选择部门
    layer.open({
        type : 2,
        title : '选择部门',
        shadeClose : false,				//true点击遮罩层关闭
        shade : 0.3,					//遮罩层背景
        maxmin : true, 					// 开启最大化最小化按钮
        area : [ '  px', '60%' ],		//弹出层大小
        scrollbar : false,				//false隐藏滑动块
        content : [ appPath + '/admin/user/selectDept?companyId='+$("#companyId").val(), 'yes' ]
    });

};


// 人员离职
newUserList.quitUser = function(){
    // 获取选择人员
    var array = new Array;
    var pkidObj = document.getElementsByName("checkUser");
    for ( var j = 0; j < pkidObj.length; j++) {
        if (pkidObj.item(j).checked == true) {
            if(pkidObj.item(j).title.indexOf('1')!=-1){
                layer.msg("勾选的人员中"+pkidObj.item(j).getAttribute("userName")+"是管理员账号，请重新选择");
                return;
            }
            array.push(pkidObj.item(j).value);
        }
    }

    if(array.length==0){
        layer.msg("未勾选人员");
        return;
    }
    layer.confirm("确定执行此操作?", {
        icon : 3,
        btn : [ "确认", "取消" ]
    }, function() {
        $.ajax({
            url: appPath + '/admin/user/changeUserQuit',
            async: false,
            type: 'post',
            data: {
                'departmentId': "-1",
                'uids': array.join(',')
            },
            success: function(data){
                var result = data.result;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        $("#checkAll").prop("checked",false);
                        // 刷新
                        returnUrl();
                    });
                }else{
                    layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    });
};

/**
 * 批量删除
 */
newUserList.batchDelUser=function(){
    // 获取选择人员
    var array = new Array;
    var pkidObj = document.getElementsByName("checkUser");
    for ( var j = 0; j < pkidObj.length; j++) {
        if (pkidObj.item(j).checked == true) {
            if(pkidObj.item(j).title.indexOf('1')!=-1){
                layer.msg("勾选的人员中"+pkidObj.item(j).getAttribute("userName")+"是管理员，请重新选择");
                return;
            }
            array.push(pkidObj.item(j).value);
        }
    }
    if(array.length==0){
        layer.msg("未勾选人员");
        return;
    }
    if(checkHours(array.join(','))){
        layer.msg("所选学员中有学员存在学时数据，请选择没有学时的学员");
        return;
    }

    layer.confirm("确定删除吗?", {
        icon : 3,
        btn : [ "确认", "取消" ]
    }, function() {

        $.ajax({
            url: appPath + '/admin/user/batchDelUser',
            async: false,
            type: 'post',
            data: {'userIds': array.join(',')},
            success: function (data) {
                var code = data['code'];
                var message;
                if (code == '10000') {
                    layer.alert('批量删除成功', {icon: 1, skin: 'layer-ext-moon'}, function (index) {
                        layer.close(index);
                        // 刷新
                        returnUrl();
                    });
                } else {
                    layer.alert((data['message']) ? data['message'] : '批量删除失敗', {icon: 2, skin: 'layer-ext-moon'});
                }
            }
        });

    });
};

/**
 * 删除
 */
function deleteUser(userId){
    if(userId==null || userId==''){
        layer.alert('删除失败', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    if(checkHours(userId)){
        layer.msg("该学员已经有学时数据存在，不能删除");
        return;
    }
    layer.confirm("确定删除吗?", {
        icon : 3,
        btn : [ "确认", "取消" ]
    }, function() {
        $.ajax({
            url: appPath + '/admin/user/delUser',
            async: false,
            type: 'post',
            data: {
                'userId': userId	// 人员id
            },
            success: function (data) {
                var code = data['code'];
                if (code == '10000') {
                    layer.msg('删除成功');
                    // 刷新
                    returnUrl();
                } else {
                    layer.alert((data['message']) ? data['message'] : '删除失敗', {icon: 2, skin: 'layer-ext-moon'});
                }
            }
        });
    })
}

//查询是否有学时
function checkHours(userId){
    var flag = false;
    $.ajax({
        url: appPath + '/admin/user/checkHours',
        async: false,
        type: 'post',
        data: {'userId':userId},
        success: function(data){
            var result = data['result'];
            flag = result;
        }
    });
    return flag;
}

function returnUrl(){
    newUserList.search();
}

//详情
function userInfo(userId){
    location.href=appPath + '/admin/user/userInfo?id='+userId;
}

//密码重置
function restPassWord(userId){
    layer.confirm("确定重置密码?", {
        icon : 3,
        btn : [ "确定", "取消" ]
    }, function() {
        // 用户重置密码
        userList.user_pwd_reset(userId);
    }, function() {
        // 取消
    });
}

// 用户重置密码
newUserList.user_pwd_reset = function(userId){
    $.ajax({
        url: appPath + '/admin/user/resetPassWord',
        async: false,
        type: 'post',
        data: {
            'userId': userId	// 人员id
        },
        success: function(data){
            var code = data.code;
            if(code=='10000'){
                var pwd_new = data.result;
                layer.alert('密码重置成功,学员密码为'+pwd_new, {icon: 1,  skin: 'layer-ext-moon'});
            }else{
                layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
            }
        }
    });
};

newUserList.addDefaultNode = function () {
    var rootNode = getRoots()[0];
    var treeNode = new Object();
    treeNode.id = '-1';
    treeNode.pid = rootNode.id;
    treeNode.name = '离职人员';
    treeNode.companyId = rootNode.id;
    treeNode.departmentId = "-1";
    treeNode.type = '3';
    treeNode.icon = appPath + "/static/global/js/zTree/css/zTreeStyle/img/department.png"
    groupTree.zTree.addNodes(rootNode, treeNode, false);
};

//修改
function modifyPassWord(userId){
    location.href=appPath + '/admin/user/userModify?id='+userId;
}

//删除节点
newUserList.removeTreeNode = function(e){
    hideRMenu();
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    var params = {
        'companyId': selectedNode.companyId,
        'departmentId': selectedNode.departmentId
    };
    if (selectedNode.children && selectedNode.children.length > 0) {
        layer.confirm('提示：您要删除的节点包含子节点，如果删除将连同子节点一起删掉', {
            btn: ['删除','取消'] //按钮
        }, function(){
            newUserList.remove(params,selectedNode);
        }, function(){
            // 取消删除
        });
    }else{
        layer.confirm('确定删除吗？', {
            btn: ['删除','取消'] //按钮
        }, function(){
            newUserList.remove(params,selectedNode);
        });
    }
};

// 删除节点
newUserList.remove = function(params,selectedNode){
    $.ajax({
        url: appPath + '/admin/department/delDepartment',
        async: false,
        type: 'post',
        data: params,
        success: function(data){
            var result = data['result'];
            var message;
            if(result){
                layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                    layer.close(index);
                    // 刷新树
                    groupTree.init();
                    //展开当前节点以下的所有子节点
                    groupTree.expandNode(selectedNode.pId);
                    newUserList.addDefaultNode();
                });
            }else{
                layer.alert("操作失敗,部门下存在学员不能删除", {icon: 2,  skin: 'layer-ext-moon'});
            }
        }
    });
};

//修改名称
newUserList.renameTreeNode = function(e){
    hideRMenu();
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    var node='';
    if (selectedNodes.length > 0) {
        node = selectedNodes[0].getParentNode();
    }
    // 请输入节点名称
    var promptIndex = layer.prompt({
        title : '输入新的节点名称，并确认',
        formType : 2,
        maxlength:40,
        value: selectedNode.name
    }, function(text) {
        layer.close(promptIndex);
        var selectedNode = selectedNodes[0];
        var params = {
            'companyId': selectedNode.companyId,
            'departmentId': selectedNode.departmentId,
            'departmentName': $.trim(text),
            'departmentNames': node.pId == null?'':node.name
        };
        if(checkName($.trim(text),selectedNode.companyId,selectedNode.id,'edit')){
            layer.msg("部门名称已存在");
            return;
        }
        $.ajax({
            url: appPath + '/admin/department/updateDepartment',
            async: false,
            type: 'post',
            data: params,
            success: function(data){
                var result = data['result'];
                var message;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);

                        // 获取左侧选中节点
                        var companyId = (selectedNode.companyId)?selectedNode.companyId:"";
                        var departmentId = (selectedNode.departmentId)?selectedNode.departmentId:"";
                        var name = selectedNode.name;
                        $("#departmentId").attr("value",departmentId);
                        $("#companyId").attr("value",companyId);

                        newUserList.treeSearch(selectedNode);
                        //userList.initListTable();
                        // 刷新树
                        groupTree.init();
                        //展开当前节点以下的所有子节点
                        groupTree.expandNode(node.id);
                        newUserList.addDefaultNode();
                        /*selectedNode.name = value;
                        alert(name)
                        selectedNode.checked = true;
                        $.fn.zTree.getZTreeObj(tree_options.id).updateNode(selectedNode);*/
                    });
                }else{
                    layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    });
};

//新增单位
newUserList.addCompany = function(e){
    hideRMenu();
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    var url=$("#createDeptUrl").val();
    var companyId=selectedNode.id;
    window.open(url+"/"+companyId);
    //location.href = appPath +"/admin/user/to_redirect?companyId="+companyId;
};

//新增部门
newUserList.addDept = function(e){
    hideRMenu();
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    // 请输入节点名称
    var promptIndex = layer.prompt({
        title : '输入要添加的节点名称，并确认',
        formType : 2,
        maxlength:40
    }, function(text) {
        var params = {
            'companyId': selectedNode.companyId,
            'departmentId': selectedNode.departmentId,
            'departmentName': $.trim(text)
        };
        if(text.length>40){
            layer.msg("部门名称超出长度限制");
            return;
        }
        if(checkName($.trim(text),selectedNode.companyId,selectedNode.id,null)){
            layer.msg("部门名称已存在");
            return;
        }
        $.ajax({
            url: appPath + '/admin/department/addDepartment',
            async: false,
            type: 'post',
            data: params,
            success: function(data){
                var result = data['result'];
                var message;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        layer.close(promptIndex);
                        // 刷新树
                        groupTree.init();
                        //展开当前节点以下的所有子节点
                        groupTree.expandNode(selectedNode.id);
                        newUserList.addDefaultNode();
                    });
                }else{
                    layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    });
};

// 新增管理员
newUserList.createUser = function(){
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    if(selectedNode.type=='1' || selectedNode.type=='2') {
        var url = $("#createUserUrl").val();
        var companyId=selectedNode.id;
        window.open(url+"/"+companyId);
    }

};

// 修改单位
newUserList.updataDeptUrl = function(){
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    if(selectedNode.type=='1' || selectedNode.type=='2') {
        var url = $("#updateDeptUrl").val();
        var companyId=selectedNode.id;
        window.open(url+"/"+companyId);
    }

};

function checkName(departmentName,companyId,parentId,type){
    var flag = false;
    $.ajax({
        url: appPath + '/admin/department/checkDepartmentName',
        async: false,
        type: 'post',
        data: {'departmentName':departmentName,
            'companyId':companyId,
            'parentId':parentId,
            'operType':type
        },
        success: function(data){
            var result = data['result'];
            flag = result;
        }
    });
    return flag;
}

newUserList.onRightClick = function(event, treeId, treeNode){
    var scrollTop = $(window).scrollTop();
    var pageX = event.clientX;
    var pageY = event.clientY + scrollTop;
    zTree = $.fn.zTree.getZTreeObj(tree_options.id);
    zTree.selectNode(treeNode);
    showRMenu(pageX ,pageY , treeNode);
};

function showRMenu(x, y,treeNode) {
    if("1"==treeNode.type || "2"==treeNode.type){//单位节点
        $("#m_add_company").hide();
        $("#m_update_dept").hide();
        $("#m_create_user").hide();
        $("#m_add_dept").show();
        $("#m_del").hide();
        $("#m_reset").hide();
        $("#m_moveup").hide();
        $("#m_movedown").hide();
    }else{
        // 部门节点
        $("#m_add_dept").show();
        $("#m_del").show();
        $("#m_reset").show();
        $("#m_moveup").show();
        $("#m_movedown").show();
        $("#m_create_user").hide();
        $("#m_update_dept").hide();
        $("#m_add_company").hide();
    }
    $("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"});

    $("body").bind("mousedown", onBodyMouseDown);
}

function onBodyMouseDown(event){
    if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
        rMenu.css({"visibility" : "hidden"});
    }
}

function hideRMenu() {
    if (rMenu) rMenu.css({"visibility": "hidden"});
    $("body").unbind("mousedown", onBodyMouseDown);
}


// 上移
newUserList.moveUp = function(){
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    var params = {
        'companyId': selectedNode.companyId,
        'departmentId': selectedNode.departmentId
    };
    $.ajax({
        url: appPath + '/admin/department/moveUp',
        async: false,
        type: 'post',
        data: params,
        success: function(data){
            var result = data['result'];
            var message;
            if(result){
                layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                    layer.close(index);
                    // 刷新树
                    groupTree.init();
                    newUserList.addDefaultNode();
                });
            }else{
                layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
            }
        }
    });
};

// 下移
newUserList.moveDown = function(){
    var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
    if(selectedNodes.length==0){
        layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
        return;
    }
    var selectedNode = selectedNodes[0];
    var params = {
        'companyId': selectedNode.companyId,
        'departmentId': selectedNode.departmentId
    };
    $.ajax({
        url: appPath + '/admin/department/moveDown',
        async: false,
        type: 'post',
        data: params,
        success: function(data){
            var result = data['result'];
            var message;
            if(result){
                layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                    layer.close(index);
                    // 刷新树
                    groupTree.init();
                    newUserList.addDefaultNode();
                });
            }else{
                layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
            }
        }
    });
};
