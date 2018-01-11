/**
 * 树节点搜索公用
 * @constructor
 */
function TreeSearch() {
    var _this = this;
    var nodeList = [], clickCount = 0, len = -1;
    var zTree, key;

    _this.init = function(treeId) {
        zTree = $.fn.zTree.getZTreeObj(treeId);
        key = $("#searchKey");
        _this.tree_search();
    };


    //树搜索
    _this.tree_search = function(){
        $("#showInfo").hide();
        key.bind("propertychange", _this.searchNode).bind("input", _this.searchNode);
    };

    _this.searchNode = function(e) {
        $("#showInfo").show();
        _this.highlightNodes(false);
        var value = $.trim(key.get(0).value);
        if(value === "") return;
        nodeList = zTree.getNodesByParamFuzzy("name", value);
        _this.highlightNodes(true);
    };

    _this.highlightNodes = function(highlight) {
        for( var i=0, l=nodeList.length; i<l; i++) {
            nodeList[i].highlight = highlight;
            zTree.expandNode(nodeList[i].getParentNode(), true, false, false); //将搜索到的节点的父节点展开
            zTree.updateNode(nodeList[i]);
        }
    };

    _this.callNumber = function () {
        if (nodeList.length) {
            //让结果集里边的第一个获取焦点（主要为了设置背景色），再把焦点返回给搜索框
            zTree.selectNode(nodeList[clickCount], false);
            $("#searchKey").focus();

            //clickCount = 1; //防止重新输入的搜索信息的时候，没有清空上一次记录
            len = nodeList.length

            //显示当前所在的是第一条
            $("#number")[0].innerHTML = "[" + (clickCount + 1) + "/" + len + "]";
        } else if (nodeList.length == 0) {
            $("#number")[0].innerHTML = "[0/0]";
            zTree.cancelSelectedNode(); //取消焦点
        }
        //如果输入框中没有搜索内容，则清空隐藏标签
        if ($("#searchKey").val() == "") {
            $("#number")[0].innerHTML = "";
            $("#showInfo").hide();
            zTree.cancelSelectedNode();
        }
    }

    //点击向上按钮时，将焦点移向上一条数据
    _this.clickUp = function () {
        //如果焦点已经移动到了第一条数据上，就返回到最后一条重新开始，否则继续移动到上一条
        if( clickCount === 0 ){
            clickCount = len ;
        }
        clickCount--;
        zTree.selectNode(nodeList[clickCount], false);
        //$("#searchKey").val(nodeList[clickCount].name)
        //显示当前所在的是条数
        $("#number")[0].innerHTML = "[" + (clickCount+1) + "/" + len + "]";
    };

    //点击向下按钮时，将焦点移向下一条数据
    _this.clickDown = function () {
        //如果焦点已经移动到了最后一条数据上，就返回到第一条重新开始，否则继续移动到下一条
        if( clickCount === len - 1 ){
            clickCount = -1 ;
        }
        clickCount++;
        zTree.selectNode(nodeList[clickCount], false);
        //$("#searchKey").val(nodeList[clickCount].name)
        //显示当前所在的条数
        $("#number")[0].innerHTML = "[" + (clickCount+1)  + "/" + len + "]";
    }
}

var treeSearch = new TreeSearch();


//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}
