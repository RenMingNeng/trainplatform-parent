//单位分类
function CompanyType(){
    var _this = this;

    _this.init = function () {
        _this.initTree();
    }

    var tree_options = {
        id: 'company_type_tree',
        url: appPath + '/companyType/tree',
        setting: {
            view: {
                dblClickExpand: false
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
                // 默认选中根节点
                var rootNode = _this.getRoots()[0];
                var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
                treeObj.selectNode(rootNode);
                _this.onClick("", "", rootNode);
                // $("#intTypeId").val(rootNode.id);
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
        $("#intTypeId").val(treeNode.id);
        company.goPage(1);
    };

};

var companyType = new CompanyType();
