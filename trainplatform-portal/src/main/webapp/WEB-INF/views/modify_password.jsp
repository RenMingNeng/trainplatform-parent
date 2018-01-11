<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html>
<head>
    <title>修改密码</title>
    <jsp:include page="${path}/common/style" />
    <style>

        .col-1{width:140px;}
        .col-7{width:290px;padding:0 20px;}
        input[type="password"],input[type="text"]{
            width:100%;
        }
    </style>
</head>
<body style="min-width:100px;">
<%--<jsp:include page="${path}/student/menu">
    <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>--%>
<div>
    <div class="area" style="width:auto;">

        <div class="box pd_b_10" style="margin-bottom:0;">
            <div class="box-body">
                <div class="row form-group">
                    <label class="col-1">用户名：</label>
                    <div class="col-7">
                        <input readonly type="text" id="userAccount" name="userAccount" value="${user.userAccount}">
                        <input type="hidden" id="userId" name="userId" value="${user.id}">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1"><b class="text-red">*</b>请输入旧密码：</label>
                    <div class="col-7">
                        <input type="password" id="userOldPasswd" name="userOldPasswd" value="" onblur="verfyOldPass()">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1"><b class="text-red">*</b>请输入新密码：</label>
                    <div class="col-7">
                        <input type="password" id="userPasswd" name="userPasswd" value="" onblur="verfyPass()">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1"><b class="text-red">*</b>请确认新密码：</label>
                    <div class="col-7">
                        <input type="password" id="userNewPasswd" name="userNewPasswd" value="" onblur="verfyNewPass()">
                    </div>
                </div>
                <div class="row form-group">
                    <label class="col-1" style="visibility: hidden;">按钮</label>
                    <div class="col-7 search-group">

                        <a href="javascript:save();" class="btn btn-info pull-right" >保存</a>
                        <%--<a href="javascript:back();" class="btn btn-info">返回</a>--%>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<%--<jsp:include page="${path}/common/footer" />--%>
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
<script src="<%=resourcePath%>static/global/js/md5.js"></script>
<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script type="text/javascript">
      function verfyOldPass(){
          var old_pass = $("#userOldPasswd").val();
          if (old_pass == "") {
              layer.msg("请输入旧密码!");
              $("#userOldPasswd").focus();
              return false;
          }
          if(md5(old_pass)!='${user.userPasswd}'){
              layer.msg("输入的旧密码不正确!");
              $("#userOldPasswd").focus();
              return false;
          }
          return true;
      }

      function verfyPass(){
          var pass = $("#userPasswd").val();
          if(pass.length<6){
              layer.msg("新密码长度必须大于6位!");
              $("#userPasswd").focus();
              return false;
          }
          if(pass==""){
              layer.msg("请输入新密码!");
              $("#userPasswd").focus();
              return false;
          }
          return true;
      }

      function verfyNewPass(){
          var old_pass = $("#userOldPasswd").val();
          var pass = $("#userPasswd").val();
          var new_pass = $("#userNewPasswd").val();
          if(new_pass==""){
              layer.msg("请输入确认密码!");
              $("#userNewPasswd").focus();
              return false;
          }
          if(pass != new_pass){
              layer.msg("新密码与确认密码不一致!");
              $("#userNewPasswd").focus();
              return false;
          }
          if(old_pass==pass){
              layer.msg("新密码不能与旧密码相同！");
              return false;
          }
        return true;
      }

      function save(){
          if(verfyOldPass() && verfyPass() && verfyNewPass()){
              $.ajax({
                  url:"<%=path %>/updatePassWord",
                  async:false,
                  type: "POST",
                  data:{
                      userId:$("#userId").val(),
                      userPasswd:md5($.trim($("#userPasswd").val()))

                  },
                  success:function(data){
                      var result=data.result;
                      if(result){
                          layer.close(0);
                          layer.confirm('密码修改成功, 请重新登录！', {
                              btn: ['确定'] //按钮
                          }, function(){
                              parent.location.href="<%=basePath%>logout";
                          }, function(){
                              // 取消
                          });

                      }else{
                          layer.alert('修改失敗', {icon: 2,  skin: 'layer-ext-moon'});
                      }
                  }
              });
          }

      }

</script>
</body>
</html>
