
function select_company(){
    var _this = this;
    _this.nodeId;
    _this.table_id;
    _this.page;

    _this.rMenu = '';
    _this.setting = {
        /*check: {
         enable: true,
         chkStyle: "radio"
         },*/
        data: {
            simpleData: {
                enable: true
            }
        },

        view: {
            dblClickExpand: false,
            fontCss: _this.getFontCss
        },
        callback: {
            onClick: function(e,treeId, treeNode){
                _this.click(treeNode.id);
            }
        }
    };
    _this.zTree;
    _this.tree_url = appPath + '/popup/select_company/tree';
    _this.tree_id = 'company_tree_id';
    _this.zNodes;

    _this.initTree = function(){
        var data = _this.ajax(_this.tree_url);
        var result = data['result'];
        _this.zNodes = JSON.parse(result);

        // 加载树
        zTree = $.fn.zTree.init($("#" + _this.tree_id), _this.setting, _this.zNodes);
        // var nodes = zTree.getNodes();
        // if(nodes){
        //     zTree.selectNode(nodes[0]);
        //     _this.click(nodes[0].id);
        // }
    };

    _this.click = function(id){
       $("#companyId").val(id);
        _this.search();
    }

    _this.initMethod = function(nodeId, table_id, page){
        _this.nodeId = nodeId;
        _this.table_id = table_id;
        _this.page = page;

        _this.initTree();

        _this.initBody();

        $("input[name=varName]").keyup(function () {
            _this.search();
        });
    };

    _this.search = function () {
        _this.page.goPage(1);
    }
    
    _this.initBody = function () {
        $('#checkAll').prop("checked", false);
        var params = $("#companyForm").serialize();
        var url = appPath + '/popup/select_company/search';

        _this.page.init("companyForm", url, _this.table_id, "select_company_page", 1, 10);
        _this.page.goPage(1);
        _this.page.list = function(dataList){
            _this.obj = dataList;
            if(dataList == null){
                return;
            }
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                if(_this.isContain(item['intId'])){
                    inner += '<tr title="该节点已经被监管">';
                    inner += '<td width="60">' + (i+1) + '</td>';
                    inner += '<td width="60"><input title="该节点已经被监管" disabled type="checkbox"></td>';
                    inner += '<td>' + item['varName'] + '</td>';
                    inner += '</tr>';
                }else{
                    inner += '<tr>';
                    inner += '<td width="60">' + (i+1) + '</td>';
                    inner += '<td width="60"><input type="checkbox" value="' + item['intId'] +'"></td>';
                    inner += '<td>' + item['varName'] + '</td>';
                    inner += '</tr>';
                }
            }
            return inner;
        };
        $('.table_inner').getNiceScroll().resize();
    };

    _this.pNodes = parent.supervise.zNodes;
    _this.isContain = function (id) {
        console.log(_this.pNodes);
        for(var i=0; i< _this.pNodes.length; i++) {
            var treeId = _this.pNodes[i].id;
            if(treeId == id){
                return true;
            }
        }
        return false;
    }

    //父页面重现saveCompany(pid, nodes)方法
    _this.saveCompany = function(){
        var companys = '';
        $("#" + _this.table_id).find("input[type=checkbox]:checked").each(function () {
            var value = $(this).val();
            companys += companys == '' ? value : ',' + value;
        });
        if(companys == ''){
            layer.msg("最少选一个节点");
            return;
        }
        if(companys != ''){
            var url = appPath + '/supervise/sendMessage';
            var params = {"companyIds" : companys, "pid" : _this.nodeId};
            var data = _this.ajax(url, params);
            if(data && data['code'] == '50503'){
                layer.msg("登录超时!");
                return;
            }
            _this.close();
            parent.layer.msg("监管申请已发送，请等待审核");
        }
    };


    //搜索高亮
    _this.getFontCss = function(treeId, treeNode) {
        return (!!treeNode.highlight) ? {'color': "#A60000", 'font-weight': "bold"} : {
            'color': "#333",
            'font-weight': "normal"
        };
    };

    //行业
    _this.business_zTree;
    _this.initBusinessTree = function (id, vals) {
        // 加载树
        _this.business_zTree = $.fn.zTree.init($("." + id), {
            check: {
                enable: true,
                chkboxType :  { "Y" : "", "N" : "" }
                //nocheckInherit: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            view: {
                dblClickExpand: false
            },
            callback: {
                onCheck: function (event, treeId, treeNode) {
                    var data = [];
                    var nodes = _this.business_zTree.getCheckedNodes(true);
                    for(var i = 0; i < nodes.length; i++){
                        var n = nodes[i];
                        if(n.pId == null){
                            data = [];
                            data.push(n.id);
                            $("#business_select").val(data).trigger("change");
                            if(nodes.length > 1){
                                _this.cancelNode(_this.business_zTree, n);
                            }
                            return;
                        }
                        data.push(n.id);
                    }

                    //组装参数
                    var rsBusines = [];
                    if(data != null && data.length > 0){
                        for(var i = 0; i < data.length; i++){
                            rsBusines.push(getAllChildrens(_this.business_zTree, data[i]));
                        }
                    }
                    console.log(rsBusines.join(","));
                    $("#varBusinessId").val(rsBusines.join(","));

                    //前台显示并查询
                    $("#business_select").val(data).trigger("change");
                }
            }
        }, business_tree);

        if(vals != null){
            for(var i = 0; i < vals.length; i++){
                var val = vals[i];
                var nodes = _this.business_zTree.getNodesByParam("id", val, null);
                if(nodes.length > 0){
                    _this.business_zTree.checkNode(nodes[0], true, true);
                }
            }
        }
    };

    _this.cancelNode = function (treeObj, node) {
        treeObj.checkAllNodes(false);
        treeObj.checkNode(node, true, true);
    };

    //地区
    _this.region_zTree;
    _this.initRegionTree = function (id, vals) {
        // 加载树
        _this.region_zTree = $.fn.zTree.init($("." + id), {
            check: {
                enable: true,
                chkboxType :  { "Y" : "", "N" : "" }
                //nocheckInherit: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            },
            view: {
                dblClickExpand: false
            },
            callback: {
                onCheck: function (event, treeId, treeNode) {
                    var nodes = _this.region_zTree.getCheckedNodes(true);
                    var data = [];
                    for(var i = 0; i < nodes.length; i++){
                        var n = nodes[i];
                        if(n.pId == null){
                            data = [];
                            data.push(n.id);
                            $("#region_select").val(data).trigger("change");
                            if(nodes.length > 1){
                                _this.cancelNode(_this.region_zTree, n);
                            }
                            return;
                        }
                        data.push(n.id);
                    }

                    //解析参数
                    var rsRegion = [];
                    if(data != null && data.length > 0){
                        for(var i = 0; i < data.length; i++){
                            rsRegion.push(getAllChildrens(_this.region_zTree, data[i]));
                        }
                    }
                    console.log(rsRegion.join(","));
                    $("#intRegionId").val(rsRegion.join(","));

                    //前台显示并查询
                    $("#region_select").val(data).trigger("change");
                }
            }
        }, resion_tree);

        if(vals != null){
            for(var i = 0; i < vals.length; i++){
                var val = vals[i];
                var nodes = _this.region_zTree.getNodesByParam("id", val, null);
                if(nodes.length > 0){
                    _this.region_zTree.checkNode(nodes[0], true, true);
                }
            }
        }
    };

    _this.close = function(){
        parent.layer.close(parent.layer.getFrameIndex(window.name));
    };

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

var select_company = new select_company();