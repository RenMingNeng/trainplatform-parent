 //课程分类
function course_type(){
    var _this = this;

    var course_type_options = {
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
        var zTree = $.fn.zTree.getZTreeObj("course_treeId");

            $("#intTypeId").val(treeNode.id);
        if($("#courseManager").val()=='courseManager'){
            course_list.search();
        }else {
            private_selectCourse.search();
        }
    };

//初始化树
    _this.iniTree = function(){
        //_this.tree_search();
        var url = appPath + '/courseType/courseType_tree';
        var result = _this.ajax(url);
        course_type_options.zNodes = eval(result);
        $.fn.zTree.init($("#course_treeId"), _this.setting, course_type_options.zNodes);
        //默认选中根节点
        var treeObj = $.fn.zTree.getZTreeObj("course_treeId");
        var rootNodes = treeObj.getNodesByFilter(function(node){return node.level==0;});
        var rootNode = rootNodes[0];
        // 默认选中根节点
        treeObj.selectNode(rootNode);
    };


   /* //树搜索
    _this.tree_search = function(){
        $("#search_key").bind("propertychange", _this.searchNode).bind("input", _this.searchNode);
    };

    _this.searchNode = function(e) {
        var zTree = $.fn.zTree.getZTreeObj("course_treeId");
        var value = $.trim($("#search_key").val());
        var	nodeList = zTree.getNodesByParamFuzzy("name", value);
        if(value){
            _this.highlightNodes(nodeList, true);
        }else{
            _this.highlightNodes(nodeList, false);
        }
    };

    _this.highlightNodes = function(nodeList, highlight) {
        var zTree = $.fn.zTree.getZTreeObj("course_treeId");
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

var course_type = new course_type();

//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}
