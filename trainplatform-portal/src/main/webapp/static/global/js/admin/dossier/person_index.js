

function person_dossier(){
    var _this = this;
    var page;
    var companyIds = [];
    var deptIds = [];
    var comPIds = [];

    _this.init = function(page_person){
        page = page_person;
        groupTree.init();
        _this.addDefaultNode();
        var rootNode = getRoots()[0];
        _this.getAllchildrenNodes(comPIds,rootNode,rootNode.type);
        window.groupTree_search = _this.search;
        _this.initListTable();

        $('#click_role_type').on("click", "span", function(){
            $('#click_role_type').find("span").removeClass("active");
            $(this).addClass("active");
            _this.bind_click(this);
        });

        //全选与非全选
        $("#checkAll").click(function(){
            if(this.checked){
                $("#person_table :checkbox").prop("checked", true);
            }else{
                $("#person_table :checkbox").prop("checked", false);
            }

            _this.getCheckedRows_ids();
        });
    };

    _this.initListTable = function () {
        var companyId = $("#companyId").val();
        $("#companyIds").val("");
        $("#deptIds").val(companyId);
        _this.initTable();
    }

    _this.initTable = function(){
        var list_url = appPath + "/admin/dossier/person/list";
        var project_url = appPath + "/admin/dossier/person/project?user_id=";
        var study_self_url= appPath + "/admin/dossier/person/studySelf?user_id=";
        page.init("person_form", list_url, "person_table", "person_page", 1, 10);
        page.goPage(1);
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td width="60">' + (i+1) + '</td>';
                inner += "<td width='40'><input type='checkbox' id='person_"+i+"' name='checkPerson' value='" + item['user_id'] + "'/></td>";
                inner += '<td width="140">';
                inner += '<span class="text-orange">' + item['user_name'] + '</span>';
                inner += '</td>';
                inner += '<td>'+ item['role_name'] + '</td>';
                inner += '<td width="160">'+ item['dept_name'] + '</td>';
                inner += '<td width="80">'+ TimeUtil.getHouAndMinAndSec(item['year_studytime']) + '</td>';
                inner += '<td width="80">'+ TimeUtil.getHouAndMinAndSec(item['total_studytime']) + '</td>';
                inner += '<td width="240">';
                inner += '<a href="' + project_url + item['user_id'] + '" class="a a-info">项目记录</a>'
                inner += '<a href="' + study_self_url + item['user_id'] + '" class="a a-info">自学记录</a>'
                inner += '</td>';
                inner += '</tr>';
            }
            return inner;
        }
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


    _this.getRole = function(){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        var rootNode = this.getRoots()[0];
        treeObj.selectNode(rootNode);
        treeObj.setting.callback.onClick(null, tree_options.id, rootNode);
    };
    /*_this.zTree = null;
    _this.rMenu = null;
    // 初始化树
    _this.tree = function(){
        $.ajax({
            url: tree_options.url,
            async: false,
            type: 'post',
            data: {

            },
            success: function(data){
                var zNodes = JSON.parse(data.result);
                // 加载树
                $.fn.zTree.init($("#"+tree_options.id), tree_options.setting, zNodes);
                // 根节点下添加退厂节点
                _this.zTree = $.fn.zTree.getZTreeObj(tree_options.id);
                var rootNode =getRoots()[0];
                var treeNode = new Object();
                treeNode.id = 'quit';
                treeNode.pid = rootNode.id;
                treeNode.name = '离职人员';
                treeNode.companyId = rootNode.id;
                treeNode.departmentId = "-1";
                treeNode.doCheck = false;
                _this.zTree.addNodes(rootNode, treeNode, true);
                setTimeout(function(){
                }, 200);
                _this.zTree.expandAll(true);

                //增加本部节点
               /!* var nodes = _this.zTree.getNodes();
                _this.addYourSelfNode(nodes);*!/
            }
        });
    };*/

   /* _this.addYourSelfNode = function (nodes) {
        if(nodes && nodes.length > 0){
            for(var i = 0; i < nodes.length; i++){
                var node = nodes[i];
                if(node && node.isParent){
                    var departmentId = node.departmentId == "" ? "" : node.departmentId;
                    _this.addDefaultNode(node, 0, "yourself_" + node.id, node.id, node.name + "(本部)", node.companyId, departmentId, false);

                    var childs = node.children;
                    _this.addYourSelfNode(childs);
                }
            }
        }
    };*/

    _this.addDefaultNode = function () {
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

    _this.getCheckedRows_ids = function(){
        // var length = $("#person_table").find("input[type=checkbox]:checked").length;
        // alert(length);
        var userIds = "";
        $("#person_table").find("input[type=checkbox]:checked").each(function(){
            userIds += userIds == '' ? $(this).val() : "," + $(this).val();
        });
        return userIds;
    };

    _this.search = function(selectedNode){
        var selectedNodes = $.fn.zTree.getZTreeObj("group_tree").getSelectedNodes();
        var treeNode = selectedNodes[0];
        // 获取左侧选中节点
        // 获取左侧选中节点
        var companyId = (treeNode.companyId)?treeNode.companyId:"";
        var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
        var departmentName = (treeNode.name)?treeNode.name:"";
        var type = treeNode.type;
        var id = treeNode.id.toString();
        $("#departmentId").attr("value",departmentId);
        $("#departmentName").attr("value",departmentName);
        var array = [];
        _this.getAllchildrenNodes(array,treeNode,type);
        debugger;
        if("1"==type ||"2"==type){
            if($("#recursion").prop("checked")){
                companyIds = array;
                $("#deptIds").val('');
                $("#companyIds").val(companyIds);
            }else{
                $("#companyIds").val("");
                $("#deptIds").val(companyId);
            }
        }
        if("3"==type){
            $("#companyIds").val('');
            if($("#recursion").prop("checked")){
                if(departmentId == '-1'){
                    $("#companyIds").val(comPIds);
                }
                deptIds = array;
                $("#deptIds").val(deptIds);
            }else{
                $("#deptIds").val(departmentId);
            }

        }
        _this.initTable();
    };

    //获取某一节点下的所有子节点
    _this.getAllchildrenNodes = function (array,treeNode,type) {
            array.push(treeNode.id);
            if(treeNode.isParent){
                var childrenNodes = treeNode.children;
                if(childrenNodes){
                    var len = childrenNodes.length;
                    if(type == '3'){
                        for(var i = 0; i<len; i++){
                            _this.getAllchildrenNodes(array,childrenNodes[i],type);
                        }
                    }else{
                        for(var j = 0; j<len; j++){
                            if(childrenNodes[j].type == '3'){
                                continue;
                            }
                            _this.getAllchildrenNodes(array,childrenNodes[j],type);
                        }
                    }
                }
            }
            return array;
        };

    _this.searchAll = function(){
        $("#person_form").find("input[name=user_name]").val("");
        $("#person_form").find("input[name=role_name]").val("");
        _this.initTable();
    };

    _this.bind_click = function(t){
        var value = $(t).attr("data-value");
        $("#person_form").find("input[name=role_id]").val(value);
        _this.initTable();
    };

    _this.export = function(){
        if(page.count != 0){
            _this.to_export();
        }else{
            layer.msg("没有可以导出的数据！");
        }
    };

    _this.to_export = function(){
        var getCheckedRows_ids = _this.getCheckedRows_ids();
        if(getCheckedRows_ids == ''){
            //询问框
            layer.confirm('您要导出列表中全部人员？', {
                btn: ['确定','取消'], //按钮
                icon : 3
            }, function(index){
                var params = $("#person_form").serialize();
                window.open(appPath + '/admin/dossier/person/export?' + params);
                layer.close(index);
            });
        }else {
            window.open(appPath + '/admin/dossier/person/export?userIds=' + getCheckedRows_ids);
        }
    };

    _this.ajax = function(url, param, type) {
        var result;
        $.ajax({
            url : url,
            async : false,
            type : 'post',
            data : param,
            success : function(data) {
                result = data;
            }
        });
        return result;
    };
}

var person_dossier = new person_dossier();