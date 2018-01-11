<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<base href="<%=basePath%>" />
<meta charset="utf-8"/>
<title>博晟 | 培训平台-管理员-人员管理</title>

<jsp:include page="${path}/common/style" />
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script src="<%=resourcePath%>static/global/js/zTree/js/jquery.ztree.all.min.js"></script>
</head>
<body style="min-width:100px;">
<div class="clearfix">
    <div class="pull-left tree">
        <div class="tree" style="background: #f7f8f9;">
            <ul id="group_tree" class="ztree"></ul>
        </div>
    </div>
</div>

<!-- 隐藏字段 -->
<input type="hidden" id="companyId" name="companyId" value="${companyId}">
<input type="hidden" id="userIds" name="userIds" value='${userIds}'>
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/common/groupTree.js"></script>
<script type="text/javascript">
    window.groupTree_search = transDepartment;
    groupTree.init();

    // 转移部门
    function transDepartment(){
        debugger;

        // 获取选中部门
        var treeObj = $.fn.zTree.getZTreeObj(tree_options.id);
        var selectedNodes = treeObj.getSelectedNodes(true);

        if(selectedNodes.length==0){
            layer.msg("请选择部门");
            return;
        }

        var treeNode = selectedNodes[0];
        var companyId = (treeNode.companyId)?treeNode.companyId:"";
        var departmentId = (treeNode.departmentId)?treeNode.departmentId:"";
        //var userIds = JSON.parse($("#userIds").val());

        var userIds = window.parent.userObject;


        var array = new Array;
        for ( var i = 0,len = userIds.length; i < len; i++) {
            if(userIds[i].companyId != companyId){
                layer.msg("人员禁止转移到其他公司");
                return;
            }
            array.push(userIds[i].id);
        }

        layer.confirm('您确定要移动吗？', {
            icon : 3,
            btn : [ '移动', '取消' ]
            // 按钮
        }, function() {
            $.ajax({
                url: '<%=path%>/admin/user/changeUserDepartment',
                async: false,
                type: 'post',
                data: {
                    'companyId':companyId,       //公司id
                    'departmentId': departmentId, // 部门id
                    'userIds': array.join(",") // 人员ids
                },
                success: function(data){
                    var result = data.result;
                    if(result){
                        layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(index){
                            layer.close(index);
                            parent.layer.close(parent.layer.getFrameIndex(window.name));
                            // 刷新
                            parent.newUserList.search();
                            //清楚权限按钮
                            $("#all",window.parent.document).prop("checked",false);
                        });
                    }else{
                        layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
                    }
                }
            });
        });


    }

</script>
</body>
</html>