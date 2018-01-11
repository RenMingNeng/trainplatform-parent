//单位分类
function CompanyPermission(companys){
    var _this = this;
    _this.init = function () {
        _this.initSelect();
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

    // 初始化select
    _this.initSelect = function(){

        var student_json = appPath + "/assets/json/student.json";
        var admin_json = appPath + "/assets/json/admin.json";
        var supervise_json = appPath + "/assets/json/supervise.json";

        $.getJSON(admin_json, function (data) {
            $("#textarea_admin").select2({
                tags: true,
                multiple: true,
                data: data,
                allowClear: true,
                language:"zh-CN"
            }).val(['admin:index']).trigger("change");
        });
        $.getJSON(student_json, function (data) {
            $("#textarea_student").select2({
                tags: true,
                multiple: true,
                data: data,
                allowClear: true,
                language:"zh-CN"
            }).val(['student:index']).trigger("change");
        });
        $.getJSON(supervise_json, function (data) {
            $("#textarea_supervise").select2({
                tags: true,
                multiple: true,
                data: data,
                allowClear: true,
                language:"zh-CN"
            }).val(['super:tj']).trigger("change");
        });

        $("#textarea_admin").on("select2:unselect",function(e){
            var permissionIds_admin = $("#textarea_admin").select2("data");
            var ary = [];
            var permissionIds = false;
            for(var i = 0; i < permissionIds_admin.length ; i++){
                ary.push(permissionIds_admin[i].id);
                if(permissionIds_admin[i].id == 'admin:index'){
                    permissionIds = true;
                }
            }
            if(!permissionIds){
                ary.push("admin:index");
                $("#textarea_admin").select2().val(ary).trigger("change");
            }
        });

        $("#textarea_student").on("select2:unselect",function(e){
            var permissionIds_student = $("#textarea_student").select2("data");           //学员
            var ary = [];
            var permissionIds = false;
            for(var i = 0; i < permissionIds_student.length ; i++){
                ary.push(permissionIds_student[i].id);
                if(permissionIds_student[i].id == 'student:index'){
                    permissionIds = true;
                }
            }
            if(!permissionIds){
                ary.push("student:index");
                $("#textarea_student").select2().val(ary).trigger("change");
            }
        });

        $("#textarea_supervise").on("select2:unselect",function(e){
            var permissionIds_supervise = $("#textarea_supervise").select2("data");           //监管
            var ary = [];
            var permissionIds = false;
            for(var i = 0; i < permissionIds_supervise.length ; i++){
                ary.push(permissionIds_supervise[i].id);
                if(permissionIds_supervise[i].id == 'super:tj'){
                    permissionIds = true;
                }
            }
            if(!permissionIds){
                ary.push("super:tj");
                $("#textarea_supervise").select2().val(ary).trigger("change");
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
        $("#company_type").val(treeNode.id);
        selectCompany.initTable();
    };

    _this.save = function(){
        var permissionIds_admin = $("#textarea_admin").select2("data");           //管理员
        var permissionIds = '';
        for(var i = 0; i < permissionIds_admin.length ; i++){
            permissionIds += permissionIds == '' ? permissionIds_admin[i].id : ',' + permissionIds_admin[i].id;
        }

        var permissionIds_student = $("#textarea_student").select2("data");           //学员
        for(var i = 0; i < permissionIds_student.length ; i++){
            permissionIds += permissionIds == '' ? permissionIds_student[i].id : ',' + permissionIds_student[i].id;
        }

        var permissionIds_supervise = $("#textarea_supervise").select2("data");           //监管
        for(var i = 0; i < permissionIds_supervise.length ; i++){
            permissionIds += permissionIds == '' ? permissionIds_supervise[i].id : ',' + permissionIds_supervise[i].id;
        }

        if(permissionIds == ''){
            alert("未选择权限项");
            return;
        }

        //公司
        var companyIds = '';
        $("#select_companys").find("span").each(function(){
            companyIds += companyIds == '' ? $(this).attr("data-company") : ',' + $(this).attr("data-company");
        });

        if(companyIds == ''){
            alert("未选择公司");
            return;
        }

        var params = {"companyIds" : companyIds, "permissionIds" : permissionIds};
        var url = appPath + '/permission/save';
        var data = _this.ajax(url, params);
        var result = data['code'];
        if(result == '10000'){
            alert('操作成功');
            history.go(-1);
        }
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

var companyPermission = new CompanyPermission();
