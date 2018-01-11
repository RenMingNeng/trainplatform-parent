/**
 * Created by Administrator on 2017/7/24.
 */

var userList = new Object();
var page;
var company_name;

    // 初始化
userList.init = function(page){
        page=page;
        this.tree();
        this.initListTable(page);
 };

userList.initListTable= function(){
    var list_url = appPath+"/admin/user/queryUserList";

    /*
     * 分页
     * @param param_form 参数form id
     * @param list_url 请求链接
     * @param tbody_id 列表div id
     * @param page_id 分页id
     * @param pageNum 页数
     * @param pageSize 每页条数
     */
    page.init("userListForm", list_url, "user_tbody", "user_page", 1, 10);

    //初始化
    page.goPage(1);

    //重写组装方法
    page.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        for(var i=0; i< len; i++) {
            item = dataList[i];
            // 组装数据
            inner += "<tr>";
            inner += "<td width='50'>"+(parseInt(i)+1)+"</td>";
            inner += "<td width='50' ><input type='checkbox' id='userId_"+i+"' title='"+item.userType+"' name='checkUser' value='"+item.id+"'/></td>";
            inner += "<td width='130'><span class=\"text-orange\">"+item.userName+"</span></td>";
            inner += "<td width='120'>"+item.userAccount+"</td>";
            /*inner += "<td class=\"pro_time\">"+item.troleName+"</td>";*/
            var dept_name = item.departmentName;
            if(dept_name == '' || dept_name==undefined || dept_name==null){
                inner += "<td></td>";
            }else{
                inner += "<td>"+item.departmentName+"</td>";
            }
            inner += "<td width='92'>"+TimeUtil.longMsTimeConvertToDateTime(item.registDate)+"</td>";
            inner += "<td width='320'><a href=javascript:userInfo('"+item.id+"') class='a a-view'>详情</a>&nbsp;";
            var isable = item.isValid;
            var uType=item.userType;
            if("1"==isable){
                if("1"==uType){
                    inner += "<a href=javascript:void(0) class='a'>修改</a>&nbsp;";
                }else{
                    inner += "<a href=javascript:modifyPassWord('"+item.id+"') class='a a-info'>修改</a>&nbsp;";
                }
                // inner += "<a href=javascript:restPassWord('"+item.id+"') class='a a-publish'>重置密码</a>&nbsp;";
            }else{
                inner += "<a href=javascript:; class='a'>修改</a>&nbsp;";
                // inner += "<a href=javascript:; class='a'>重置密码</a>&nbsp;";
            }

            if("1"==uType){
                inner += "<a href=javascript:void(0) class='a'>删除</a>&nbsp;";
            }else{
                inner += "<a href=javascript:deleteUser('"+item.id+"') class='a a-close'>删除</a>&nbsp;";
            }
            inner += "</td>";
            inner += "</tr>";
        }
        return inner;
    }

};

    //模糊查询
    userList.search=function (){
        this.initListTable();
    };

    //查询全部
    userList.searchAll=function(){
        $("#userName").val("");
        this.initListTable();
    };

    var zTree;
    var strId=''; //选中部门的节点
    var rMenu = $("#rMenu");
    // tree options
    var tree_options = {
        id: 'deparent_tree',
        url: appPath + '/admin/department/deptTree',
        setting: {
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
                fontCss: getFontCss
            },
            callback: {
                onRightClick: function(event, treeId, treeNode){
                    var id = treeNode.id.toString();
                    if(id.indexOf("yourself_") != -1){
                        return;
                    }
                   onRightClick(event, treeId, treeNode);
                },
                onClick: function(e,treeId, treeNode){console.log(treeNode)
                    // 获取左侧选中节点
                    var companyId = (treeNode.companyId)?treeNode.companyId:"";
                    var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
                    var departmentName = (treeNode.name)?treeNode.name:"";
                    var id = treeNode.id.toString();

                    $("#departmentId").attr("value",departmentId);
                    $("#departmentName").attr("value",departmentName);
                    $("#companyId").attr("value",companyId);
                    userList.initListTable();
                },
                onRename: function(event, treeId, treeNode, isCancel){
                    this.zTreeOnRename(event, treeId, treeNode, isCancel);
                }
              /*  onCheck: function(e,treeId, treeNode){
                    onCheck(e,treeId, treeNode);
                }*/
            }
        },
        zNodes : '',
        rMenu : ''
    };

    //搜索高亮
    function getFontCss(treeId, treeNode){
        return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
    }




    userList.getRole = function(){
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        var rootNode = this.getRoots()[0];
        treeObj.selectNode(rootNode);
        treeObj.setting.callback.onClick(null, tree_options.id, rootNode);
    };
    userList.zTree = null;
    userList.rMenu = null;
    // 初始化树
      userList.tree = function(){
        $.ajax({
            url: tree_options.url,
            async: false,
            type: 'post',
            data: {

            },
            success: function(data){
                var zNodes = JSON.parse(data.result);
                if(zNodes && zNodes.length>0){
                    var objDate = zNodes[0];
                    company_name = objDate.name ;
                }
                // 加载树
                $.fn.zTree.init($("#"+tree_options.id), tree_options.setting, zNodes);
                // 根节点下添加退厂节点
                userList.zTree = $.fn.zTree.getZTreeObj(tree_options.id);
                var rootNode =getRoots()[0];

                //本公司
                //离职
                userList.addDefaultNode(rootNode, null, 'quit', rootNode.id, '离职人员', rootNode.id, "-1", false);

                setTimeout(function(){
                }, 200);
                userList.zTree.expandAll(true);
            }
        });
     };

    userList.addDefaultNode = function (rootNode, index, id, pid,name,companyId,departmentId,doCheck) {
        var treeNode = new Object();
        treeNode.id = id;
        treeNode.pid = pid;
        treeNode.name = name;
        treeNode.companyId = companyId;
        treeNode.departmentId = departmentId;
        treeNode.doCheck = doCheck;
        if(null != index){
            userList.zTree.addNodes(rootNode, index, treeNode, false);
            return;
        }
        userList.zTree.addNodes(rootNode, treeNode, false);
    };

      //删除节点
    userList.removeTreeNode = function(e){
        hideRMenu();
        var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
        if(selectedNodes.length==0){
            layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
            return;
        }
        var selectedNode = selectedNodes[0];
        var params = {
            'companyId': selectedNode.companyId,
            'departmentId': selectedNode.departmentId
        };
        if (selectedNode.children && selectedNode.children.length > 0) {
            layer.confirm('提示：您要删除的节点包含子节点，如果删除将连同子节点一起删掉', {
                btn: ['删除','取消'] //按钮
            }, function(){
                userList.remove(params);
            }, function(){
                // 取消删除
            });
        }else{
            layer.confirm('确定删除吗？', {
                btn: ['删除','取消'] //按钮
            }, function(){
                userList.remove(params);
            });
        }
    };

    // 删除节点
    userList.remove = function(params){
        $.ajax({
            url: appPath + '/admin/department/delDepartment',
            async: false,
            type: 'post',
            data: params,
            success: function(data){
                var result = data['result'];
                var message;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        // 刷新树
                        userList.tree();
                    });
                }else{
                    layer.alert("操作失敗,部门下存在学员不能删除", {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
      };

    //修改名称
    userList.renameTreeNode = function(e){
        hideRMenu();
        var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
        if(selectedNodes.length==0){
            layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
            return;
        }
        var selectedNode = selectedNodes[0];
        var node='';
        if (selectedNodes.length > 0) {
             node = selectedNodes[0].getParentNode();
        }
        // 请输入节点名称
        var promptIndex = layer.prompt({
            title : '输入新的节点名称，并确认',
            formType : 2,
            maxlength:40,
            value: selectedNode.name
        }, function(text) {
            layer.close(promptIndex);
            var selectedNode = selectedNodes[0];
            var params = {
                'companyId': selectedNode.companyId,
                'departmentId': selectedNode.departmentId,
                'departmentName': $.trim(text),
                'departmentNames': node.pId == null?'':node.name
            };
            if(checkName($.trim(text),selectedNode.companyId,selectedNode.id,'edit')){
                layer.msg("部门名称已存在");
                return;
            }
            $.ajax({
                url: appPath + '/admin/department/updateDepartment',
                async: false,
                type: 'post',
                data: params,
                success: function(data){
                    var result = data['result'];
                    var message;
                    if(result){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);

                            // 获取左侧选中节点
                            var companyId = (selectedNode.companyId)?selectedNode.companyId:"";
                            var departmentId = (selectedNode.departmentId)?selectedNode.departmentId:"";
                            $("#departmentId").attr("value",departmentId);
                            $("#companyId").attr("value",companyId);
                            userList.initListTable();
                            // 刷新树
                           userList.tree();
                            //alert(value)
                            //selectedNode.name = value;
                            //selectedNode.checked = true;
                            //$.fn.zTree.getZTreeObj(tree_options.id).updateNode(selectedNode);
                        });
                    }else{
                        layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

    //添加节点
    userList.addTreeNode = function(e){
        hideRMenu();
        var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
        if(selectedNodes.length==0){
            layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
            return;
        }
        var selectedNode = selectedNodes[0];
        // 请输入节点名称
        var promptIndex = layer.prompt({
            title : '输入要添加的节点名称，并确认',
            formType : 2,
            maxlength:40
        }, function(text) {
            var params = {
                'companyId': selectedNode.companyId,
                'departmentId': selectedNode.departmentId,
                'departmentName': $.trim(text)
            };
            if(text.length>40){
                layer.msg("部门名称超出长度限制");
                return;
            }
            if(checkName($.trim(text),selectedNode.companyId,selectedNode.id,null)){
                layer.msg("部门名称已存在");
                return;
            }
            $.ajax({
                url: appPath + '/admin/department/addDepartment',
                async: false,
                type: 'post',
                data: params,
                success: function(data){
                    var result = data['result'];
                    var message;
                    if(result){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            layer.close(promptIndex);
                            // 刷新树
                            userList.tree();
                        });
                    }else{
                        layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
    };

  function checkName(departmentName,companyId,parentId,type){
    var flag = false;
    $.ajax({
        url: appPath + '/admin/department/checkDepartmentName',
        async: false,
        type: 'post',
        data: {'departmentName':departmentName,
            'companyId':companyId,
            'parentId':parentId,
            'operType':type
        },
        success: function(data){
            var result = data['result'];
            flag = result;
        }
    });
    return flag;
  }

    function onRightClick(event, treeId, treeNode) {
        var scrollTop = $(window).scrollTop();
        var pageX = event.clientX;
        var pageY = event.clientY + scrollTop;
        zTree = $.fn.zTree.getZTreeObj(tree_options.id);
        zTree.selectNode(treeNode);
        showRMenu(pageX ,pageY , treeNode);
     }

    function showRMenu(x, y,treeNode) {
          if("-1"==treeNode.departmentId){
            if("quit"==treeNode.id){
                // 退厂节点
                $("#m_add").hide();
                $("#m_del").hide();
                $("#m_reset").hide();
                $("#m_moveup").hide();
                $("#m_movedown").hide();
            }
          }else if(""==treeNode.departmentId){//根节点
              $("#m_add").show();
              $("#m_del").hide();
              $("#m_reset").hide();
              $("#m_moveup").hide();
              $("#m_movedown").hide();
          }else{
            // 部门节点
            $("#m_add").show();
            $("#m_del").show();
            $("#m_reset").show();
            $("#m_moveup").show();
            $("#m_movedown").show();
        }
        $("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"});

        $("body").bind("mousedown", onBodyMouseDown);
    }

    function onBodyMouseDown(event){
        if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
            rMenu.css({"visibility" : "hidden"});
        }
    }

    function hideRMenu() {
        if (rMenu) rMenu.css({"visibility": "hidden"});
        $("body").unbind("mousedown", onBodyMouseDown);
    }

    //全选与非全选
    $("#checkAll").click(function(){
        if(this.checked){
            $("#user_tbody :checkbox").prop("checked", true);
        }else{
            $("#user_tbody :checkbox").prop("checked", false);
        }
    });

    /**
     * 导入
     */
    userList.importUser=function(){
        // 获取部门id
        var departmentId ="";
        var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
        if(selectedNodes.length!=0){
            departmentId = selectedNodes[0].departmentId;
        }
        /*  if(departmentId==""){
              layer.msg('请选择人员需要导入的部门,没有部门请先创建部门！');
              return;
          }*/
        // 导入模板类型 1：学员模板  2：角色模板
        //exportTye 1调用学员导出方法 2调用角色导出方法
        var tempType ="1";
        var params = new Object();
        params.departmentId = departmentId;
        layer.open({
            type : 2,
            title : '导入excel',
            shadeClose : false,				//true点击遮罩层关闭
            shade : 0.3,					//遮罩层背景
            maxmin : true, 					// 开启最大化最小化按钮
            area : [ '1000px', '80%' ],		//弹出层大小
            scrollbar : false,				//false隐藏滑动块
            content : [ appPath + '/admin/excelImport/to_import?tempType='+tempType+'&exportTye=1'+'&params='+encodeURIComponent(JSON.stringify(params)), 'yes' ],
            cancel: function(){
                userList.initListTable();
            }
        })
   };


    /*userList.exportUser = function(){
    if(page.count != 0){
        userList.to_exportUser();
    }else{
        layer.msg("没有可以导出的数据！");
    }
    };*/

    /**
     * 导出
     */
    userList.exportUser = function () {
        // 获取选择人员
        var depId = $("#departmentId").val();
        var array = new Array;
        var pkidObj = document.getElementsByName("checkUser");
        for (var j = 0; j < pkidObj.length; j++) {
            if (pkidObj.item(j).checked == true) {
                array.push(pkidObj.item(j).value);
            }
        }
        var arr = array.join(",");
        var fileName = new Date().getTime() + ".xlsx";
        if(null != arr && '' != arr){
            window.location.href = appPath + '/admin/excelExport/export?fileName=' + fileName + '&params=' + arr + "&exportTye=1";
        }else if(null != depId && '' != depId){
            //询问框
            layer.confirm('您想要导出'+ '<span style="color: blue;font-weight: bold">'+$("#departmentName").val()+'</span>' +'下的人员？', {
                btn: ['确定', '取消'], //按钮
                icon : 3
            }, function (index) {
                window.location.href = appPath + '/admin/excelExport/export?fileName=' + fileName + '&depId=' + depId + "&exportTye=1";
                layer.close(index);
            }, function () {
                //取消
            });
        }else{
            //询问框
            layer.confirm('您想要导出全公司人员？', {
                btn: ['确定', '取消'], //按钮
                icon : 3
            }, function (index) {
                window.location.href = appPath + '/admin/excelExport/export?fileName=' + fileName + "&exportTye=1";
                layer.close(index);
            }, function () {
                //取消
            });
        }
    };

    /**
     * 转移
     */
    userList.moveUser=function(){
        // 获取选择人员
        var array = new Array;
        var pkidObj = document.getElementsByName("checkUser");
        for ( var j = 0; j < pkidObj.length; j++) {
            if (pkidObj.item(j).checked == true) {
                array.push(pkidObj.item(j).value);
            }
        }
        if(array.length==0){
            layer.msg("未勾选人员");
            //message.add("请选择节点", "warning");
            return;
        }

        // 弹窗-选择部门
        layer.open({
            type : 2,
            title : '选择部门',
            shadeClose : false,				//true点击遮罩层关闭
            shade : 0.3,					//遮罩层背景
            maxmin : true, 					// 开启最大化最小化按钮
            area : [ '  px', '60%' ],		//弹出层大小
            scrollbar : false,				//false隐藏滑动块
            content : [ appPath + '/admin/user/selectDept?companyId='+$("#companyId").val()+"&userIds="+array.join(','), 'yes' ]
        });

    };


    // 人员退厂
    userList.quitUser = function(){
        // 获取选择人员
        var array = new Array;
        var pkidObj = document.getElementsByName("checkUser");
        for ( var j = 0; j < pkidObj.length; j++) {
            if (pkidObj.item(j).checked == true) {
                array.push(pkidObj.item(j).value);
            }
        }
        if(array.length==0){
            layer.msg("未勾选人员");
            return;
        }
        layer.confirm("确定执行此操作?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {
            $.ajax({
                url: appPath + '/admin/user/changeUserQuit',
                async: false,
                type: 'post',
                data: {
                    'departmentId': "-1",
                    'uids': array.join(',')
                },
                success: function(data){
                    var result = data.result;
                    if(result){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            $("#checkAll").prop("checked",false);
                            // 刷新
                            userList.initListTable();
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });
     };

    //详情
    function userInfo(userId){
        location.href=appPath + '/admin/user/userInfo?id='+userId;
    }

  //密码重置
   function restPassWord(userId){
       layer.confirm("确定重置密码?", {
           icon : 3,
           btn : [ "确定", "取消" ]
       }, function() {
           // 用户重置密码
           userList.user_pwd_reset(userId);
       }, function() {
           // 取消
       });
   }

    // 用户重置密码
    userList.user_pwd_reset = function(userId){
        $.ajax({
            url: appPath + '/admin/user/resetPassWord',
            async: false,
            type: 'post',
            data: {
                'userId': userId	// 人员id
            },
            success: function(data){
                var code = data.code;
                if(code=='10000'){
                    var pwd_new = data.result;
                    layer.alert('密码重置成功,学员密码为'+pwd_new, {icon: 1,  skin: 'layer-ext-moon'});
                }else{
                    layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    };

   //修改
    function modifyPassWord(userId){
        location.href=appPath + '/admin/user/userModify?id='+userId;
    }

    // 上移
    userList.moveUp = function(){
        var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
        if(selectedNodes.length==0){
            layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
            return;
        }
        var selectedNode = selectedNodes[0];
        var params = {
            'companyId': selectedNode.companyId,
            'departmentId': selectedNode.departmentId
        };
        $.ajax({
            url: appPath + '/admin/department/moveUp',
            async: false,
            type: 'post',
            data: params,
            success: function(data){
                var result = data['result'];
                var message;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        // 刷新树
                        userList.tree();
                    });
                }else{
                    layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    };

    // 下移
   userList.moveDown = function(){
        var selectedNodes = $.fn.zTree.getZTreeObj(tree_options.id).getSelectedNodes();
        if(selectedNodes.length==0){
            layer.alert('请选择节点', {icon: 2,  skin: 'layer-ext-moon'});
            return;
        }
        var selectedNode = selectedNodes[0];
        var params = {
            'companyId': selectedNode.companyId,
            'departmentId': selectedNode.departmentId
        };
        $.ajax({
            url: appPath + '/admin/department/moveDown',
            async: false,
            type: 'post',
            data: params,
            success: function(data){
                var result = data['result'];
                var message;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                        layer.close(index);
                        // 刷新树
                        userList.tree();
                    });
                }else{
                    layer.alert((data['message'])?data['message']:'操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                }
            }
        });
    };

    /**
     * 删除
     */
    function deleteUser(userId){
        if(userId==null || userId==''){
            layer.alert('删除失败', {icon: 2,  skin: 'layer-ext-moon'});
            return;
        }
        if(checkHours(userId)){
            layer.msg("该学员已经有学时数据存在，不能删除");
            return;
        }
        layer.confirm("确定删除吗?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {
            $.ajax({
                url: appPath + '/admin/user/delUser',
                async: false,
                type: 'post',
                data: {
                    'userId': userId	// 人员id
                },
                success: function (data) {
                    var code = data['code'];
                    if (code == '10000') {
                       /* layer.alert('删除成功', {icon: 1, skin: 'layer-ext-moon'}, function (index) {
                            layer.close(index);
                            // 刷新
                            userList.initListTable();
                        });*/
                       layer.msg('删除成功');
                        userList.initListTable();
                    } else {
                        layer.alert((data['message']) ? data['message'] : '删除失敗', {icon: 2, skin: 'layer-ext-moon'});
                    }
                }
            });
        })
    }

    /**
     * 批量删除
     */
    userList.batchDelUser=function(){
        // 获取选择人员
        var array = new Array;
        var pkidObj = document.getElementsByName("checkUser");
        for ( var j = 0; j < pkidObj.length; j++) {
            if (pkidObj.item(j).checked == true) {
               if(pkidObj.item(j).title=='1'){
                   layer.msg("勾选的人员中包含管理员账号，请重新选择");
                   return;
               }
                array.push(pkidObj.item(j).value);
            }
        }
        if(array.length==0){
            layer.msg("未勾选人员");
            return;
        }
        if(checkHours(array.join(','))){
            layer.msg("所选学员中有学员存在学时数据，请选择没有学时的学员");
            return;
        }

        layer.confirm("确定删除吗?", {
            icon : 3,
            btn : [ "确认", "取消" ]
        }, function() {

            $.ajax({
                url: appPath + '/admin/user/batchDelUser',
                async: false,
                type: 'post',
                data: {'userIds': array.join(',')},
                success: function (data) {
                    var code = data['code'];
                    var message;
                    if (code == '10000') {
                        layer.alert('批量删除成功', {icon: 1, skin: 'layer-ext-moon'}, function (index) {
                            layer.close(index);
                            // 刷新
                            userList.initListTable();
                        });
                    } else {
                        layer.alert((data['message']) ? data['message'] : '批量删除失敗', {icon: 2, skin: 'layer-ext-moon'});
                    }
                }
            });

        });

    };

    //查询是否有学时
    function checkHours(userId){
        var flag = false;
        $.ajax({
            url: appPath + '/admin/user/checkHours',
            async: false,
            type: 'post',
            data: {'userId':userId},
            success: function(data){
                var result = data['result'];
                flag = result;
            }
        });
        return flag;
    }