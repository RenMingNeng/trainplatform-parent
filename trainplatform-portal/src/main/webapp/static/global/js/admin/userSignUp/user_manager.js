function UserManager() {
    var _this = this;
    var page;


    _this.init = function (page_) {
        page = page_;
        _this.initEvent();
        _this.initTable();
    }


    _this.initEvent = function () {
        // 批量删除人员
        $("#btn_delete_batch_user").click(function(){
            if(!$(this).hasClass("unclick")){
                _this.project_delete_batch_user();
            }
        });

        // 根据用户名称查询
        $("#btn_search_user").click(function(){
            $("#userName").val($.trim($("#userName").val()));
            _this.initTable();
        });

        //查询所有
        $("#btn_search_all_user").click(function(){
            if(!$(this).hasClass("unclick")){
                $("#userName").val('');
                _this.initTable();
            }

        });

        //全选人员
        $("#usercheckAll").click(function () {
            var flag=$("#usercheckAll").prop("checked");
            $('#select_user_list input[type="checkbox"]').prop("checked",flag);
        })

        // 关闭弹窗
        $("#btn_close").click(function () {
            // 刷新首页公开项目列表
            parent.layer.close(parent.layer.getFrameIndex(window.name));
            top.admin_index.initTable_public();
        })
    }

    // 人员列表
    _this.initTable = function () {
        var pageArray = [10,20,30,40,50]
        var user_list_url = appPath + "/admin/project_create/private/project_user_list";
        page.init("select_user_form", user_list_url, "select_user_list", "select_user_page", 1, 10, pageArray);
        page.goPage(1);
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr >';
                inner += '<td width="80">' + (i+1) + '</td>';
                inner += '<td width="80"><input type=\"checkbox\" name="courseBox" data-id="' + item['user_id'] + '"></td>';
                inner += '<td width="200">'+item['user_name']+'</td>';
                inner += '<td><span class="text-orange">'+item['department_name']+'</span></td>';
                inner += '</tr>';
            }
            return inner;
        }
    }

    // 批量删除人员
    _this.project_delete_batch_user = function(){
        var userIds=[]
        $('#select_user_list input:checked').each(function(i,e){
            userIds.push($(this).attr("data-id"));
        })

        if(userIds.length==0){
            layer.msg("未选择人员");return;
        }
        layer.confirm("与人员相关的项目记录会全部删除（<span style='color: red;'>无法恢复</span>）确定此操作吗?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {
            $.ajax({
                url: appPath + '/admin/project_create/private/delete_project_user',
                async: false,
                type: 'post',
                data: {
                    'projectId': $("#projectId").val(), 	            // 项目总览id
                    'projectStatus': _this.projectStatus, 	          // 项目状态
                    'ids': userIds.join(","), 		                    // 人员ids
                    'projectType':_this.projectTypeNo    //项目类型
                },
                success: function(data){
                    var code = data['code'];
                    if(code){
                        layer.msg('操作成功', {time: 1000,}, function(){
                            $("#usercheckAll").prop("checked",false);
                            // 刷新项目人员
                            _this.initTable();
                        });
                    }else{
                        layer.msg('操作失敗', {icon: 1500});
                    }
                }
            });
        });
    };

}

var userManager = new UserManager();
