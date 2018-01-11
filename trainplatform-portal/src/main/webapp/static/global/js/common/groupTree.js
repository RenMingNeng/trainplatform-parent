/**
 * 单位 部门树
 * @type {Object}
 */
var groupTree = new Object();

    // 初始化
    groupTree.init = function(){
            this.tree();
    };

    //常量设置
    var tree_options = {
        id: 'group_tree',
        url: appPath + '/admin/user/groupTree', //查询单位节点请求url
        zNodes : ''
    };

    var setting = {
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
            onClick: function(e,treeId, treeNode){
                // 获取左侧选中节点
               // newUserList.search(treeNode);
                window.groupTree_search(treeNode);
            },
            //beforeExpand: beforeExpand,
            onRightClick: function(event, treeId, treeNode){
                window.groupTree_rightClick(event, treeId, treeNode);
            },
            onCheck: function (event, treeId, treeNode) {
                window.groupTree_check(treeNode);
            }
            //onAsyncSuccess: onAsyncSuccess,
            //onAsyncError: onAsyncError
        }
    };

    //点击事件
    function groupTree_search(treeNode) {

    }
    //选中时间
    function groupTree_check(treeNode) {

    }

    //右键操作
    function groupTree_rightClick(event, treeId, treeNode){

    }

    //异步请求后数据返回值过滤
    function filter(treeId, parentNode, responseData) {
        if (responseData) {
            responseData=JSON.parse(responseData.result);
            for(var i =0; i < responseData.length; i++) {
                responseData[i].name = responseData[i].name.replace(/\.n/g, '.');
            }
        }
        return responseData;
    }

   /* function getUrl(treeId, treeNode) {
        var param = "id="+treeNode.id+"&type="+treeNode.type;
        alert(param);
        return appPath + "/admin/user/groupTree?" + param;
    }*/

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

   groupTree.getRoot = function(){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
        var rootNode = rootNodes[0];
        treeObj.selectNode(rootNode);
        treeObj.setting.callback.onClick(null, tree_options.id, rootNode);
    };

    // 初始化树
    groupTree.tree = function(){
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
                groupTree.zTree = $.fn.zTree.getZTreeObj(tree_options.id);
                //离职
                //groupTree.addDefaultNode(rootNode, null, 'quit', rootNode.id, '离职人员', rootNode.id, "-1", false);
                //默认选中根节点
                var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
                // 默认选中根节点
                treeObj.selectNode(rootNodes[0]);

                //初始化方法
                window.groupTree_init(rootNodes[0]);
            }
        });
     };

    groupTree.expandNode = function(pid){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
      //  var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
        var node=treeObj.getNodeByParam("id",pid);
        treeObj.expandNode(node, true, true, true);
        // var nodes = treeObj.getSelectedNodes(0);
        // if (nodes.length>0) {
        //     treeObj.expandNode(nodes[0], true, true, true);
        // }
    };

//初始化事件
function groupTree_init(treeNode){

}

//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}

// 获取根节点集合
function getRoots(){
    var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
    return treeObj.getNodesByFilter(function(node){return node.level==0;});
}
