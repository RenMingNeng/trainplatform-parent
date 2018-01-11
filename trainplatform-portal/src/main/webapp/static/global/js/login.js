/**
 * Created by Administrator on 2017/10/13.
 */
$(function() {

    // 获取公告
    article.init();

    var $user = $('#userAccount');
    var $pass = $("#userPasswd");

    /*输入框获得焦点*/
    $('input[type="text"],input[type="password"]').focus(function(){
        $(this).siblings('.icon').addClass('click')
    })
    $('input[type="text"],input[type="password"]').blur(function(){
        $(this).siblings('.icon').removeClass('click')
    })

    $("#userPasswd,#userAccount").focus(function(){
        $('.right').removeClass('error');
    });

    $user.blur(function(){
        var $this = $(this);
        if($this.val() == ''){
            $(".msg").html('用户名不能为空！');
            $('.right').addClass('error');
            return;
        }else{
            $('.right').removeClass('error');
        }
    });

    $pass.blur(function(){
        var $this = $(this);
        if($this.val().length < 6 && $this.val() !== ''){
            $(".msg").html('密码不能小于6位！');
            $('.right').addClass('error');
            return;
        }else if( $this.val() == ''){
            $(".msg").html('密码不能为空！');
            $('.right').addClass('error');
            return;
        }else{
            $('.right').removeClass('error');
        }
    });
    $('#userAccount,#userPasswd').keydown(function(e){
        var code = e.keyCode;
        if( code === 13 ){
            login();
        }
    });
    $('.tabs li').click(function(){
        var $this = $(this);
        $this.addClass('active').siblings('li').removeClass('active');
        $("#userType").val($.trim($this.attr("userType")));
    })
});

function login(){
    var $user = $('#userAccount');
    var $pass = $("#userPasswd");

    if($user.val() == ''){
        $(".msg").html('用户名不能为空！');
        $('.right').addClass('error');
        return;
    }else{
        $('.right').removeClass('error');
    }

    if($pass.val().length < 6 && $pass.val() !== ''){
        $(".msg").html('密码不能小于6位！');
        $('.right').addClass('error');
        return;
    }else if( $pass.val() == ''){
        $(".msg").html('密码不能为空！');
        $('.right').addClass('error');
        return;
    }else{
        //$pass.val(hex_sha256($pass.val()));
        $('.right').removeClass('error');
    }

    if( $('.right').hasClass('error') ){
        return;
    }

    var userAccount = $.trim($("#userAccount").val());
    var userPasswd = md5($.trim($("#userPasswd").val()));
    var userType = $.trim($("#userType").val());
    //var remeberMe = $("#remeberMe").prop('checked');alert(remeberMe);
    var params = {
        'userAccount': userAccount,
        'userPasswd': userPasswd,
        'userType': userType
    };

    $.ajax({
        url: basePath + "signin",
        data: params,
        type: "post",
        dataType:"json",
        beforeSend: function (){
            $(".img-loading").css("display", 'block');
        },
        success: function(data) {
            var code = data.code;
            var message = data.message;
            if("10000" == code){
                window.location.href = basePath + "redirect";return;
            }
            if(message){
                $('.right').addClass('error');
                $(".msg").html(message);
            };
            $(".img-loading").css("display", 'none');
        },
        complete: function (XMLHttpRequest, textStatus) {
            $(".img-loading").css("display", 'none');
        }
    });
}
