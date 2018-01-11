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
    <title>博晟 | 完善账户</title>
    <style>
        *{margin:0;padding:0;}
        body{min-width:1200px;background: #E4E4E4}
        .container {
            width: 1100px;
            margin: 0 auto;
        }
        .clearfix{
            overflow: hidden;
        }
        .pull-left {
            float: left;
        }
        .pull-right {
            float: right;
        }
        .hidden{display: none!important;}
        #header{
            background: #fff;
        }
        #header a{
            text-decoration: none;
            color:#333;
        }
        #header h1 {
            padding-bottom: 20px;
            padding-top: 20px;
        }
        #header h1 .title {
            color: #46413d;
            font-size: 20px;
            border-left: 2px solid #eee;
            margin-top: 15px;
            margin-left: 20px;
            padding: 5px 20px;
        }

        .bind-type{
            width: 1000px;
            -webkit-border-radius: 5px;
            -moz-border-radius: 5px;
            -ms-border-radius: 5px;
            -o-border-radius: 5px;
            margin: 0 auto ;
            margin-top: 62px;
            vertical-align: middle;
            background: white;
            padding: 20px;
        }
        .bind-form-fields{
            overflow: hidden;
            line-height: 22px;
            margin-top:10px;
        }
        .bind-form-fields label{
            float: left;
            width:66px;
            text-align:right;
            padding-right:6px;
        }
        .bind-form-fields input[type="text"],
        .bind-form-fields input[type="password"]{
            height: 22px;
            float: left;
            padding:0 5px;
            width:200px;
        }
        .item h6{
            font-size:15px;
        }
        .item{
            margin-bottom:20px;
            display: none;
        }
        .item_1{
            display: block;
        }
        .item .body{
            padding:10px;
            font-size:14px;
            line-height: 22px;
        }
        .item .btn-group-current .btn{
            margin-left:8px;
        }
        .item .btn{
            height:22px;
            border:none;
            cursor: pointer;
            width:50px;
            border-radius: 3px;
            background: #31a2ee;
            color:#fff;
            line-height: 21px;
        }
        .item .btn-lg{
            width:80px;
        }
        .item .btn:hover{
            opacity:0.9;
        }
        .alertMsg{padding:50px;line-height:22px;background-color:#E0E0E0;color:#FF7000}
        .alertMsg i{width: 22px;
            height: 22px;
            background: #FF7000;
            line-height: 22px;
            display: block;
            float: left;
            text-align: center;
            border-radius: 9px;
            color: #fff;
            font-weight: 900;
            margin-right: 10px;
        }
        .layui-layer-btn .layui-layer-btn0 {
            border-color: #CCCCCC ! important;
            background-color: #7C7C7C ! important;
            color: #fff ! important;
            border: none ! important;
        }
    </style>
    <script type="text/javascript">
        var basePath = "<%=basePath %>";
    </script>
    <script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
    <script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
    <script src="<%=resourcePath%>static/global/js/md5.js"></script>
</head>
<body>
<div id="header" class="register_hearder">
    <div class="container">
        <div class="clearfix">
            <h1 class="pull-left">
                <a href="javascript:;" title="博晟安全">
                    <img class="pull-left" src="<%=resourcePath%>static/global/images/login-logo.png" alt="博晟安全">
                    <div class="pull-left title">完善个人信息</div>
                </a>
            </h1>
            <a class="pull-right" href="<%=basePath %>logout;" style="line-height: 106px;">退出</a>
        </div>
    </div>
</div>
<div class="bind-type">
    <div class="item item_1">
        <h6>步骤一：您是否有培训平台账号？</h6>
        <div class="body clearfix">
            <div class="pull-left btn-group btn-group-current">
                <button class="btn" onclick="next(this,1)">有</button>
                <button class="btn" onclick="next(this,2)">没有</button>
            </div>
        </div>
    </div>
    <div class="item item_3">
        <h6>步骤二：绑定账户</h6>
        <div class="body clearfix">
            <form class="bind-form" action="#" method="post">
                <div class="bind-form-fields">
                    <label for="user_account_1">账户:</label>
                    <input id="user_account_1" type="text" placeholder="请输入账户">
                </div>
                <div class="bind-form-fields">
                    <label for="user_passwd_1">密码:</label>
                    <input id="user_passwd_1" type="password" placeholder="请输入密码">
                </div>
                <div class="bind-form-fields">
                    <label style="visibility: hidden">占位</label>
                    <button class="btn btn-lg"  type="button" onclick="back();">上一步</button>
                    <input type="button" class="btn" value="提交" id="bind-btn-1">
                </div>
            </form>
        </div>
    </div>

    <div class="item item_2">
        <h6>步骤二： 此规则适用于：没有培训平台账户, 属于第一次登录培训平台, 直接使用博晟统一账户快捷登录</h6>
        <div class="body clearfix">
            <div class="pull-left btn-group btn-group-current">
                <button class="btn btn-lg"  type="button" onclick="back();">上一步</button>
                <button class="btn btn-lg" id="btn-bind-two">快捷登录</button>
            </div>
        </div>
    </div>

</div>

<!-- 外部系统唯一授权码（UUID生成，唯一） -->
<input id="sso_user_open_id" name="sso_user_open_id" type="hidden" value="${sso_user_open_id}">
<!-- 账号 -->
<input id="sso_user_user_account" name="sso_user_user_account" type="hidden" value="${sso_user_user_account}">
<!-- 密码 -->
<input id="sso_user_user_passwd" name="sso_user_user_passwd" type="hidden" value="${sso_user_user_passwd}">
<!-- 用户姓名 -->
<input id="sso_user_user_name" name="sso_user_user_name" type="hidden" value="${sso_user_user_name}">
<!-- 手机号 -->
<input id="sso_user_mobile_no" name="sso_user_mobile_no" type="hidden" value="${sso_user_mobile_no}">
<!-- 用户性别： 0：女 1：男 -->
<input id="sso_user_sex" name="sso_user_sex" type="hidden" value="${sso_user_sex}">
<!-- 证件类型: 0: 身份证 1: 居住证 2: 签证 3: 护照 4: 军人证 5: 港澳通行证 6: 台胞证 -->
<input id="sso_user_id_type" name="sso_user_id_type" type="hidden" value="${sso_user_id_type}">
<!-- 证件号 -->
<input id="sso_user_id_number" name="sso_user_id_number" type="hidden" value="${sso_user_id_number}">
<!-- 邮箱 -->
<input id="sso_user_email" name="sso_user_email" type="hidden" value="${sso_user_email}">
<!-- service -->
<input id="sso_user_service" name="sso_user_service" type="hidden" value="${sso_user_service}">

<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script>

    window.alert = function alertMsg (msg) {
        layer.open({
            type:1,
            title: false, // 不显示标题栏
            closeBtn: false,
            area: "600px",
            shade: 0.3,
            id: "layer-session-timeout", // 设置id, 防止重复弹出
            resize: false,
            btn:["知道了"],
            btnAlign: "c",
            moveType: 1,
            content: "<div class='alertMsg'><i>!</i>"+msg+"</div>",
            yes: function(layero) {
                layer.close(layero);
            }
        });
    }
    var layer_index_1;
    $(function(){
        $("#btn-bind-one").click(function () {
            layer_index_1 = layer.open({
                type:1,
                title: false, // 不显示标题栏
                closeBtn: false,
                area: ['680px','350px'],
                shade: 0.3,
                resize: false,
                btnAlign: "c",
                moveType: 1,
                content: $("#layer-content-1")
            });
        });

        $("#btn-bind-two").click(function () {

            // 进行快速绑定前先去检测该账户是否在子系统已存在
            ssoCheckAccount();
        });
        $("#layer-close-1").click(function () {
            layer.close(layer_index_1)
        });

        $("#bind-btn-1").click(function () {
            var user_account_1 = $.trim($("#user_account_1").val());
            var user_passwd_1 = $.trim($("#user_passwd_1").val());
            var open_id = $.trim($("#sso_user_open_id").val());

            if(!user_account_1) {
                alert('请输入账户');return;
            }
            if(!user_passwd_1) {
                alert('请输入密码');return;
            }
            if(!open_id) {
                alert('open_id丢失');return;
            }
            var params = {
                'user_account': user_account_1,
                'user_passwd': md5(user_passwd_1),
                'open_id':open_id
            };

            $.ajax({
                url: basePath + "sso/bind/one",
                data: params,
                type: "post",
                dataType:"json",
                beforeSend: function (){
                },
                success: function(data) {
                    var code = data.code;
                    var message = data.message;
                    if("10000" == code){
                        window.location.href = basePath + "redirect";return;
                    }
                    if(message){
                        alert(message);
                    };
                },
                complete: function (XMLHttpRequest, textStatus) {
                }
            });
        });



    })

    function ssoCheckAccount() {
        var params = {
            'user_account': $.trim($("#sso_user_user_account").val())
        };

        $.ajax({
            url: basePath + "sso/bind/two/check",
            data: params,
            type: "post",
            dataType:"json",
            beforeSend: function (){
            },
            success: function(data) {
                var code = data.code;
                var message = data.message;
                var result = data.result;
                if("10000" == code){
                    if(result){
                        alert('账户已存在于子系统, 建议您选择绑定');
                    } else {
                        // 一键登陆, 根据统一账户信息自动创建子账户（默认分配学员角色）
                        yijianLogin();
                    }
                } else {
                    if(message){
                        alert(message);
                    }
                }
            },
            complete: function (XMLHttpRequest, textStatus) {
            }
        });
    }

    function yijianLogin() {

        var params = {
            'user_account': $.trim($("#sso_user_user_account").val()),
            'user_name': $.trim($("#sso_user_user_name").val()),
            'user_passwd': $.trim($("#sso_user_user_passwd").val()),
            'mobile_no': $.trim($("#sso_user_mobile_no").val()),
            'sex': $.trim($("#sso_user_sex").val()),
            'id_type': $.trim($("#sso_user_id_type").val()),
            'id_number': $.trim($("#sso_user_id_number").val()),
            'email': $.trim($("#sso_user_email").val()),
            'open_id': $.trim($("#sso_user_open_id").val())
        };

        $.ajax({
            url: basePath + "sso/bind/two",
            data: params,
            type: "post",
            dataType:"json",
            beforeSend: function (){
            },
            success: function(data) {
                var code = data.code;
                var message = data.message;
                if("10000" == code){
                    window.location.href = basePath + "redirect";return;
                } else {
                    if(message){
                        alert(message);
                    }
                }
            },
            complete: function (XMLHttpRequest, textStatus) {
            }
        });
    }

    function next(obj,index){
        var $parent = $(obj).parent();
        var $parents = $(obj).parents('.item');
        $parent.addClass('hidden');
        $('.item:eq('+ index +') ').show();
    }
    function back(){
        $('.item').hide();
        $('.item_1').show().find('.btn-group').removeClass('hidden');
    }

</script>
</body>
</html>
