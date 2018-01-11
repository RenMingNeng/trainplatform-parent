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
    <title>博晟 | 培训平台-管理员-人员修改</title>

    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <script type="text/javascript" src="<%=resourcePath%>/static/global/js/select2/js/select2.full.js"></script>
    <style>
        .nav{display: none;}
        input[type="text"],
        .select2.select2-container{width:700px!important;}
        .col-11{padding-left:20px;}

    </style>
    <script type="text/javascript">
        $(document).ready(function(){
            initRole();

        });
    </script>
</head>
<body>
<jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="user"></jsp:param>
</jsp:include>
<div id="main">
    <form id="ff_user">
    <div class="area" >
        <!-- 项目基础信息 -->
        <div class="box pd_b_10">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">人员修改</h3>
                <div class="pull-right search-group">
                    <a href="javascript:save();" class="btn btn-info " id="btn_project_base_info_save"><span class="fa fa-save"></span> 保存</a>
                    <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
                </div>
            </div>
            <div class="box-body project_info">
                <div class="row">

                        <div class="col-12 form-group">
                            <label class="col-1">用户名：</label>
                            <div class="col-11">
                                <input type="text" value="${userInfo.userAccount}" readonly></div>
                        </div>

                    <div class="col-12 form-group">
                        <label class="col-1">姓名：</label>
                        <div class="col-11">
                            <input type="text" id="userName" name="userName" value="${userInfo.userName}"/>
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-1">性别：</label>
                        <div class="col-11 info">
                            <input type="radio" name="sex" value="1" <c:if test="${userInfo.sex eq '1'}">checked="checked"</c:if>>女
                            <input type="radio" name="sex" value="2" <c:if test="${userInfo.sex eq '2'}">checked="checked"</c:if>>男
                        </div>
                    </div>

                    <div class="col-12 form-group">
                        <label class="col-1">身份证号：</label>
                        <div class="col-11">
                            <input type="text" id="idNumber" name="idNumber" value="${userInfo.idNumber}"/>
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-1">手机号：</label>
                        <div class="col-11">
                            <input type="text" id="mobileNo" name="mobileNo" value="${userInfo.mobileNo}"/>
                        </div>
                    </div>

                    <div class="row form-group" id="role">
                        <label class="col-1">受训角色：</label>
                        <div class="col-11">
                            <div>
                                <select  class="select_box" name="troleName" id="role_select2" style="width:100%;" >
                                    <%--<option value="-1">默认角色</option>--%>
                                   <%-- <c:forEach var="ur" items="${userRoles}">
                                        <option value="${ur.trainRoleId}" >${ur.roleName}</option>
                                    </c:forEach>--%>
                                </select>
                            </div>
                        </div>
                    </div>
               <c:if test="${! empty userInfo.departmentName}">
                    <div class="col-12 form-group">
                        <label class="col-1">部门信息：</label>
                        <div class="col-11">
                            <input type="text" id="departmentName" name="departmentName" value=" ${userInfo.departmentName=="" ? "":userInfo.departmentName}" readonly/>
                        </div>
                    </div>
               </c:if>
                </div>
            </div>
        </div>
        <%--隐藏域--%>
        <input type="hidden" id="id" name="id" value="${userInfo.id}">   <%--人员id--%>
        <input type="hidden" id="companyId" name="companyId" value="${userInfo.companyId}">   <%--人员id--%>
        <input type="hidden" id="departmentId" name="departmentId" value="${userInfo.departmentId}">   <%--人员id--%>
        <!-- 人员变动信息 -->
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">人员变动信息</h3>
            </div>
            <div class="box-body">

                <table class="table pro_msg project_info_table">
                    <thead>
                    <tr>
                            <th>序号</th>
                            <th>日期</th>
                            <th>人员变动信息</th>
                            <th>部门信息</th>
                    </tr>
                    </thead>

                    <%--人员变动信息开始--%>
                    <tbody id="userInfo_tbody">
                    <!-- 循环输出带过来的list数组 的值-->
                    <c:choose>
                        <c:when test="${not empty workList}">
                            <c:forEach items="${workList}" var="uwork" varStatus="vs">
                                <tr>
                                    <td>${vs.index+1}</td>
                                    <td>${uwork.operTime.substring(0,19)}</td>
                                    <td>
                                        <c:if test="${uwork.operater==1}">
                                            人员登记
                                        </c:if>
                                        <c:if test="${uwork.operater==2}">
                                            人员转移
                                        </c:if>
                                        <c:if test="${uwork.operater==3}">
                                            人员离职
                                        </c:if>
                                        <c:if test="${uwork.operater==4}">
                                            人员修改
                                        </c:if>
                                    </td>
                                    <td>${uwork.deptName}</td>

                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td align="center">
                                    <span>对不起，没有找到任何相关记录...</span>
                                </td>
                            </tr>
                        </c:otherwise>
                      </c:choose>
                    </tbody>
                    <%--人员变动信息结束--%>

                </table>
                 <%--分页开始--%>
               <%-- <div class="page text-right" >
                    <ul id="courseInfo_page"></ul>
                </div>--%>
            </div>
        </div>


    </div>
    </form>
</div>

<jsp:include page="${path}/common/footer" />


<script type="text/javascript">

    function goback(){
        history.go(-1);
    }

        //初始化受训角色
       function initRole() {
            $("#role_select2").select2();
            $.ajax({
                url: appPath+ '/admin/user/allTrainRole',
                dataType: 'json',
                type:'get',
                success:function (data) {
                    var list=data.result;

                    if(list.length == 0) {
                        $("#role").addClass("nav");
                        return;
                    }
                    $("#role_select2").select2({
                        tags:true,
                        multiple:true,
                        data:list,
                        allowClear:true,
                        language:"zh-CN"
                    });
                    //发送异步请求项目下已有的角色
                    $.ajax({
                        url: appPath + '/admin/user/checkedRole',
                        dataType: 'json',
                        type:'get',
                        data:{"userId":$("#id").val()},
                        success:function (data) {
                            var list = data.result;
                            if(list == null){
                                return;
                            }
                            var len = list.length;
                            var inner = "", item;
                            var ids = [];
                            // 组装数据
                            for(var i=0; i< len; i++) {
                                item = list[i];
                                ids.push(item.trainRoleId)
                            }
                            $("#role_select2").val(ids).trigger("change");

                        }

                    })

                }

            })

        }

    // 表单校验
    function verify(){
        // 错误信息
        var msg = "";
        // 姓名
        var userName = $.trim($("#userName").val());
        if(!userName){
            msg = "请填写姓名";
            layer.msg(msg);
            return false;
        }
        // 身份证号-非必填-填写必须18位
        var idNumber = $.trim($("#idNumber").val());
        if(idNumber){
            if(idNumber.length!=18){
                msg = "身份证号必须18位";
                layer.msg(msg);
                return false;
            }
        }
        // 手机号-非必填-填写必须11位
        var mobileNo = $.trim($("#mobileNo").val());
        if(mobileNo){
            if(mobileNo.length!=11){
                msg = "手机号必须11位";
                layer.msg(msg);
                return false;
            }
        }
        return true;
    }

    function save(){
         var tRoles = $("#role_select2").select2('data');        //受训角色数组
         //console.log(tRoles);
         var roleIds = assembleDate(tRoles);
        $("#ff_user").ajaxSubmit({
            type: 'post',
            dataType: 'json',
            data: {
                'roleIds': roleIds	// 角色id
            },
            url: appPath + '/admin/user/update',
            beforeSubmit: function(){
                return verify();
            },
            success: function(data){
                //console.debug(data);
                var result = data.result;
                if(result){
                    layer.alert('操作成功', {icon: 1,  skin: 'layer-ext-moon'}, function(){
                        history.go(-1);
                    });
                    return;
                }
                layer.alert('操作失敗', {icon: 2,  skin: 'layer-ext-moon'});
            },
            error: function(XmlHttpRequest, textStatus, errorThrown){

            },
            complete: function(){

            }
        });
    }

    function assembleDate(datas){
        var roleIds=null;
        for(var i = 0;i<datas.length;i++){
            if(i!=0){
                roleIds+=","+ datas[i].id;
            }else{
                roleIds=datas[i].id;
            }
        }
        return roleIds;
    }

</script>
</html>