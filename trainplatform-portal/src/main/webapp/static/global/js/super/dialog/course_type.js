//课程分类
function CourseType(){
    var _this = this;

    _this.init = function () {
        _this.initTree();
    }

    var tree_options = {
        id: 'course_type_tree',
        url: appPath + '/super/courseType/ex_courseType_tree',
        setting: {
            view: {
                dblClickExpand: false,
                fontCss: getFontCss
            },
            callback: {
                onClick: function(e,treeId, treeNode){
                    _this.onClick(e,treeId, treeNode);
                }
            }
        },
        zNodes: '',
        rMenu: ''
    };


    //初始化树
    _this.initTree = function(){
        $.ajax({
            url: tree_options.url,
            async: false,
            type: 'post',
            data: {},
            success: function(data){
                tree_options.zNodes = eval(data.result);
                // 加载树
                $.fn.zTree.init($("#"+tree_options.id), tree_options.setting, tree_options.zNodes);
                //默认选中根节点
                /*if ($("#course_type").val() == '') {
                    var rootNode = _this.getRoots()[0];
                    if (null != rootNode && "" != rootNode) {
                        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                        treeObj.selectNode(rootNode);
                        $("#course_type").val(rootNode.id);
                    }
                }*/
            }
        });
    };

    // 获取根节点集合
    _this.getRoots = function() {
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        return treeObj.getNodesByFilter(function(node){return node.level==0;});
    };

    //点击
    _this.onClick = function(e,treeId, treeNode) {
        $("#course_type").val(treeNode.id);
        selectCourse.initTable();
    };

};

var courseType = new CourseType();

//搜索高亮
function getFontCss(treeId, treeNode){
    return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
}
