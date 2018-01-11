/**
 * 单位 部门树
 * @type {Object}
 */
var groupTreeCheckbox = new Object();

    // 初始化
    groupTreeCheckbox.init = function(){
        groupTreeCheckbox.tree();
    };

    //常量设置
    var tree_options = {
        id: 'group_tree',
        url: appPath + '/admin/user/groupTree', //查询单位节点请求url
        zNodes : ''
    };

    var setting = {
        check: {
            enable: true,
            chkboxType :  { "Y" : "s", "N" : "s" }
            //nocheckInherit: true
        },
        async: {
            enable: true,
            url:appPath + "/admin/user/groupTree", //异步查询部门节点请求url
            autoParam:["id", "type"],
            otherParam:{"otherParam":"zTreeAsyncTest"},
            type:"post",
            dataType:"json",
            dataFilter: filter
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        view: {
            selectedMulti: false
        },
        callback: {
            onClick: function(e,treeId, treeNode){console.log(treeNode)
                // 获取左侧选中节点
               // newUserList.search(treeNode);
                window.groupTreeCheckbox_search(treeNode);
            },
            onCheck: function (event, treeId, treeNode) {
                var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                window.groupTreeCheckbox_search(treeObj,treeNode);
            }
            //onAsyncSuccess: onAsyncSuccess,
            //onAsyncError: onAsyncError
        }
    };

    //点击事件
    function groupTreeCheckbox_search(treeNode) {

    }
    //选中时间
    function groupTreeCheckbox_check(treeObj,treeNode) {

    }

    //异步请求后数据返回值过滤
    function filter(treeId, parentNode, responseData) {
        if (responseData) {
            responseData=JSON.parse(responseData.result);
            for(var i =0; i < responseData.length; i++) {
                responseData[i].name = responseData[i].name.replace(/\.n/g, '.');
                //responseData[i].checked = true;
            }
        }
        responseData =  window.groupTreeCheckbox.check(responseData);
        return responseData;
    }
//选中时间
groupTreeCheckbox.check = function(treeNode) {
return treeNode;
}

   var reloadType;
    function beforeExpand(treeId, treeNode) {
        if (!treeNode.isAjaxing) {
            var zTree = $.fn.zTree.getZTreeObj(tree_options.id);
            if (reloadType == "refresh") {
                treeNode.icon = appPath +"/js/zTree/css/zTreeStyle/img/loading.gif";
                zTree.updateNode(treeNode);
            }
            zTree.reAsyncChildNodes(treeNode, reloadType, true);
            return true;
        } else {
            alert("zTree 正在下载数据中，请稍后展开节点。。。");
            return false;
        }
    }

   groupTreeCheckbox.getRoot = function(){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        var rootNode = this.getRoots()[0];
        treeObj.selectNode(rootNode);
        treeObj.setting.callback.onClick(null, tree_options.id, rootNode);
    };

    // 初始化树
    groupTreeCheckbox.tree = function(){
        $.ajax({
            url: tree_options.url,
            async: false,
            type: 'post',
            data: {},
            success: function(data){
                var zNodes = JSON.parse(data.result);
                // 加载树
                $.fn.zTree.init($("#"+tree_options.id),setting, zNodes);
                // 根节点下添加退厂节点
                groupTreeCheckbox.zTree = $.fn.zTree.getZTreeObj(tree_options.id);
                var rootNode =getRoots()[0];

                var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                //离职
                //groupTreeCheckbox.addDefaultNode(rootNode, null, 'quit', rootNode.id, '离职人员', rootNode.id, "-1", false);
               /* //默认选中根节点
                var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
                // 默认选中根节点
                treeObj.selectNode(rootNode);*/
            }
        });
     };

// 获取根节点集合
function getRoots(){
    var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
    return treeObj.getNodesByFilter(function(node){return node.level==0;});
}
