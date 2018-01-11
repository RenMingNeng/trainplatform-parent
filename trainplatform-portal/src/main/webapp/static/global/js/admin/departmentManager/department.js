//课程分类
function Department(){
    var _this = this;

    var department_options = {
        zNodes: '',
        rMenu: ''
    };



    _this.setting = {
        view: {
            dblClickExpand: false,
            fontCss: getFontCss
        },
        callback: {
            onClick: function(e,treeId, treeNode){
                _this.onClick(e,treeId, treeNode);
            }
        }
    };

    //点击
    _this.onClick = function(e,treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("department_treeId");
//		zTree.expandNode(treeNode);
        //$("#varTypeId").val(treeNode.id);

    };

//初始化树
    _this.iniTree = function(){
       // _this.tree_search();
        var url = appPath + '/admin/department/deptTree';
        var result = _this.ajax(url);
        department_options.zNodes = eval(result);
        $.fn.zTree.init($("#department_treeId"), _this.setting, department_options.zNodes);
    };


  /*  //树搜索
    _this.tree_search = function(){
        $("#search_key").bind("propertychange", _this.searchNode).bind("input", _this.searchNode);
    };

    _this.searchNode = function(e) {
        var zTree = $.fn.zTree.getZTreeObj("department_treeId");
        var value = $.trim($("#search_key").val());
        var	nodeList = zTree.getNodesByParamFuzzy("name", value);
        if(value){
            _this.highlightNodes(nodeList, true);
        }else{
            _this.highlightNodes(nodeList, false);
        }
    };

    _this.highlightNodes = function(nodeList, highlight) {
        var zTree = $.fn.zTree.getZTreeObj("department_treeId");
        var len = nodeList.length;
        for( var i=0; i<len; i++) {
            nodeList[i].highlight = highlight;
            zTree.updateNode(nodeList[i]);
        }
    };*/

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
};

var department = new Department();

/*//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}*/
