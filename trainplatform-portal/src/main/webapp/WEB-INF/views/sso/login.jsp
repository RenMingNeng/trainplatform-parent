<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/8/24
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<html>
<head>
    <title>博晟 | 跳转登录</title>
    <script type="text/javascript">
        var basePath = "<%=basePath %>";
    </script>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
    <script src="<%=resourcePath%>static/global/js/md5.js"></script>
</head>
<body>
    <h5>自动登录中...</h5>

    <!-- input hidden -->
    <input id="ticket" name="ticket" type="hidden" value="${ticket}">

    <script>
        $(function(){
            sso_login();
        })

        function sso_login() {
            var ticket = $.trim($("#ticket").val());

            if(!ticket) {
                alert('ticket丢失');return;
            }
            var params = {
                'ticket': ticket
            };

            $.ajax({
                url: basePath + "sso/signin",
                data: params,
                type: "post",
                dataType:"json",
                beforeSend: function (){
                },
                success: function(data) {
                    var code = data.code;
                    var message = data.message;
                    if("10000" == code){
                        window.location.href = basePath + "sso/redirect";return;
                    }
                    if(message){
                        alert(message);
                    };
                },
                complete: function (XMLHttpRequest, textStatus) {
                }
            });
        }
    </script>
</body>
</html>
