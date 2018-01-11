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
    <link type="text/css" rel="stylesheet" href="<%=path%>/static/global/js/select2/css/select2.min.css"/>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=resourcePath%>/static/global/js/select2/js/select2.full.js"></script>
    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />
    <script type="text/javascript">
        $(document).ready(function(){
            initRole();
        });
    </script>
    <style>
        input[type="text"],.select_box{width:680px;}
        .select2-container--default .select2-selection--single{border-radius: 0;outline: none;border-color:#ccc;}
        .select2-container--default .select2-selection--single:focus{border-color: #31b0d5;}
        .col-11{padding-left:20px;}
    </style>
</head>
<body>
<jsp:include page="${path}/popup/menu">
<jsp:param name="menu" value="stuinfo"></jsp:param>
</jsp:include>
<div id="main">
    <form id="ff_user">
    <div class="area" >

        <!-- 项目基础信息 -->
        <div class="box" style="padding-bottom:10px;">
            <div class="box-header clearfix">
                <h3 class="box-title pull-left">个人信息</h3>
                <div class="pull-right search-group">
                    <a href="javascript:back();" class="btn btn-info"  id="btnback"><span class="fa fa-reply"></span> 返回</a>
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
                            <input type="text" id="userName" name="userName" value="${userInfo.userName}" readonly/>
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-1">性别：</label>
                        <!-- 性别只读
                        <div class="col-11 info">
                            <label>
                                <input type="radio" name="sex" value="1" <c:if test="${userInfo.sex eq '1'}">checked="checked"</c:if>> 女
                            </label>
                            &nbsp;&nbsp;
                            <label>
                                <input type="radio" name="sex" value="2" <c:if test="${userInfo.sex eq '2'}">checked="checked"</c:if>> 男
                            </label>
                        </div>
                        -->
                        <div class="col-11 info">
                            <c:if test="${userInfo.sex eq '1'}"> 女</c:if>
                            <c:if test="${userInfo.sex eq '2'}"> 男</c:if>
                        </div>
                    </div>

                    <div class="col-12 form-group">
                        <label class="col-1">身份证号：</label>
                        <div class="col-11 ">
                            <input type="text" id="idNumber" name="idNumber" value="${userInfo.idNumber}" readonly/>
                        </div>
                    </div>
                    <div class="col-12 form-group">
                        <label class="col-1">手机号：</label>
                        <div class="col-11 ">
                            <input type="text" id="mobileNo" name="mobileNo" value="${userInfo.mobileNo}" readonly/>
                        </div>
                    </div>

                    <!-- 暂时屏蔽
                    <div class="row form-group" id="trainRole">
                        <label for="" class="col-1">受训角色：</label>
                        <div class="col-11 ">
                            <div>
                                <select  class="select_box" name="troleName" id="role_select2">
                                </select>
                            </div>
                        </div>
                    </div>
                    -->

                    <div class="col-12 form-group">
                        <label class="col-1">部门信息：</label>
                        <div class="col-11 ">
                            <input type="text" id="departmentName" name="departmentName" value=" ${userInfo.departmentName}" readonly/>
                        </div>
                    </div>

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
                                <td align="center" class="table_empty" colspan='4'>
                                    <img src="<%=resourcePath %>static/global/images/empty.png">
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
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>/static/global/js/select2/js/select2.full.js"></script>
<script type="text/javascript">

    function goback(){
        location.href=appPath+"/student";
    }

        //初始化受训角色
       function initRole() {
            $("#role_select2").select2();
            $.ajax({
                url: appPath+ '/student/allTrainRole',
                dataType: 'json',
                type:'get',
                success:function (data) {
                    var list=data.result;
                    $("#role_select2").select2({
                        tags:true,
                        multiple:true,
                        data:list,
                        allowClear:true,
                        language:"zh-CN"
                    });
                    //发送异步请求项目下已有的角色
                    $.ajax({
                        url: appPath + '/student/checkedRole',
                        dataType: 'json',
                        type:'get',
                        data:{"userId":$("#id").val()},
                        success:function (data) {
                            var list=data.result;
                            var len=list.length;
                            var inner = "", item;

                            if(len ==1 && list[0].trainRoleId == "-1"){
                                $("#trainRole").hide();
                                return;
                            }
                            var ids=[];
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
         var roleIds = assembleDate(tRoles);
        $("#ff_user").ajaxSubmit({
            type: 'post',
            dataType: 'json',
            data: {
                'roleIds': roleIds	// 角色id
            },
            url: appPath + '/popup/update_stuInfo',
            beforeSubmit: function(){
                return verify();
            },
            success: function(data){
                var result = data.result;
                if(result){
                    layer.msg('操作成功', {time: 1000}, function(){
//                       location.href=appPath+"/student";
                    });
                    return;
                }
                layer.msg('操作失敗', {time: 2000});
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
    //返回
    function back() {
        window.close();
    }
</script>
</html>