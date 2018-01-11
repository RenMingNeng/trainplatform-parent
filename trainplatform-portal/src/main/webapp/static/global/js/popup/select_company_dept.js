
function select_company_dept(){
    var _this = this;
    _this.tree_url = appPath + '/admin/user/groupTree';
    _this.zNodes;
    _this.vals = [];
    _this.selectId;
    _this.tree_html;
    _this.div_id;

    _this.initTree = function(selectId, vals){
        if(vals != null && vals != undefined && vals != ''){
            _this.vals = vals.split(",");
        }
        _this.selectId = selectId;
        _this.div_id = _this.selectId + '_div';
        //初始化图层
        _this.init_tree_div();

        //初始化select2
        $("#" + _this.selectId).select2({
            tags:true,
            data: _this.getAllTree(_this.zNodes),
            multiple:true,
            placeholder:"请选择范围",
            language:"zh-CN"
        });

        //加载树节点
        $(".form-control").on("select2:open",function(e){
            if($(".select2-dropdown").find("#" + _this.div_id).css("display") == undefined ||
                $(".select2-dropdown").find("#" + _this.div_id).css("display") == 'none'){
                $(".select2-dropdown").html($("#" + _this.div_id).css("display", "block"));
            }
        });
    };

    _this.dis_node = function (node) {
        _this.company_dept_zTree.checkNode(node, false, false, true);
        var children = node.children;
        if(children){
            for(var j=0; j< children.length; j++) {
                _this.dis_node(children[j]);
            }
        }
    };

    _this.init_tree_div = function () {
        _this.tree_html = '<div id="' + _this.div_id + '"><ul id="' + _this.selectId + '_tree" class="ztree" style="height: 380px;overflow: scroll;"></ul></div>';

        var $treeHtml = $(_this.tree_html).css("display", "none");
        $("body").append($treeHtml);

        //加载树数据
        var data = _this.ajax(_this.tree_url);
        var result = data['result'];
        _this.zNodes = JSON.parse(result);

        _this.initcompany_deptTree();

        var vals = $("#area_select").val();
        if(vals != null){
            for(var i = 0; i < vals.length; i++){
                var val = vals[i];
                var nodes = _this.company_dept_zTree.getNodesByParam("id", val, null);
                if(nodes.length > 0){
                    _this.company_dept_zTree.checkNode(nodes[0], true, true);
                }
            }
        }
    };

    _this.company_dept_zTree;
    _this.initcompany_deptTree = function () {
        // 加载树
        _this.company_dept_zTree = $.fn.zTree.init($("#" + _this.selectId + "_tree"), {
            check: {
                enable: true,
                chkboxType :  { "Y" : "s", "N" : "s" }
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            async: {
                enable: true,
                url: _this.tree_url, //异步查询部门节点请求url
                autoParam:["id", "type"],
                otherParam:{"otherParam":"zTreeAsyncTest"},
                type:"post",
                dataType:"json",
                dataFilter: _this.filter
            },
            view: {
                dblClickExpand: false
            },
            callback: {
                onCheck: function (event, treeId, treeNode) {
                    //select2 回显示
                    var nodes = _this.company_dept_zTree.getCheckedNodes(true);
                    var data = [];
                    for(var i=0; i< nodes.length; i++) {
                        data.push(nodes[i].id);
                    }
                    $("#area_select").val(data).trigger("change");
                    //重新方法
                    window.selectCompanyDeptCheck(nodes);

                    //前台显示并查询
                    $("#" + _this.selectId).val(data).trigger("change");
                    $(".select2-selection__choice__remove").remove();

                    if(!$("#isSelect").is(":checked")){
                        var nodes = treeNode.children;
                        if(nodes){
                            for(var i=0; i< nodes.length; i++) {
                                if(nodes[i] && nodes[i].type == 2){
                                    _this.dis_node(nodes[i]);
                                }
                            }
                        }
                    }
                }
            }
        }, _this.zNodes);

        if(_this.vals != null && _this.vals != undefined && _this.vals.length > 0){
            for(var i = 0; i < _this.vals.length; i++){
                var val = _this.vals[i];
                var nodes = _this.company_dept_zTree.getNodesByParam("id", val, null);
                if(nodes.length > 0){
                    _this.company_dept_zTree.checkNode(nodes[0], true, true);
                }
            }
        }
    };

    //异步请求后数据返回值过滤
    _this.filter = function(treeId, parentNode, responseData) {
        if (responseData) {
            responseData=JSON.parse(responseData.result);
            for(var i =0; i < responseData.length; i++) {
                responseData[i].name = responseData[i].name.replace(/\.n/g, '.');
            }
        }
        return responseData;
    }

    _this.cancelNode = function (treeObj, node) {
        treeObj.checkAllNodes(false);
        treeObj.checkNode(node, true, true);
    };

    //获取所有树节点
    _this.getAllTree = function (array) {
        var oldData = array;
        var params;
        for(var i =0; i < array.length; i++) {
            if(array[i].type == 2){
                var node = _this.company_dept_zTree.getNodeByParam("id", array[i].id, null);
                if(node && node.isParent && !node.children){
                    params = {'id': array[i].id, 'type': array[i].type};
                    var data = _this.ajax(_this.tree_url, params);
                    var result = data['result'];
                    var rs = JSON.parse(result);
                    oldData = oldData.concat(rs);
                }
            }
        }
        return oldData;
    }

    _this.ajax = function(url, param){
        var result = '';
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function(data){
                result = data;
            }
        });
        return result;
    };
};

var select_company_dept = new select_company_dept();