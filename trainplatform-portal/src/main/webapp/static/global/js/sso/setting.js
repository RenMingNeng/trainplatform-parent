/**
 * Created by Administrator on 2017/9/4.
 */

var sso_setting = new Object();
sso_setting.init = function () {
    
    // 账户设置的移入移出
    $("#account-setting").mouseenter(function () {
        $(this).addClass('hover');
    }).mouseleave(function () {
        $(this).removeClass('hover');
    });

    $('.user-role-layer ul>li').hover(function(){
        var $this = $(this);
        $this.addClass('hover');
        var $ol = $this.find('ol');
        if( $ol !== null ){
            $ol.show();
        }
    },function(){
        var $this = $(this);
        $this.removeClass('hover');
        $('.user-role-layer ol').hide();
    })

    $("#account-setting-layer-content a").each(function () {
        var $a = $(this);
            $a.click(function(){
                var current_role = $("#user-role-name").val();
                var role = $a.attr("role");
                if(role == current_role) {
                    alert('已经是当前角色, 无须切换');return;
                }
                sso_setting.switch_role(role);
            });
    });

    // 解绑账号-指的是统一帐通的账号与培训平台账户解绑
    $("#account-setting-unbind").click(function () {
        layer.confirm(
            '请谨慎操作此功能！如解除绑定，当前账号将与本系统平台产生的数据解除关联，再次登陆时，将以游客身份进入，或重新绑定系统平台账号。',
            {title:'温馨提示:'}
            ,function (index) {
                sso_setting.account_unbind();
                layer.close(index);
            });

    });

    /*// 变更单位
    $("#account-setting-companyChange").click(function () {
       var url = basePath + "popup/companyChange" ;
       window.open(url)
    });*/
};

sso_setting.switch_role = function (role) {
    if(!role) {
        alert('参数role丢失'); return;
    }
    var params = {
        'role': role
    };

    $.ajax({
        url: basePath + "switch/role",
        data: params,
        type: "post",
        dataType:"json",
        beforeSend: function (){
        },
        success: function(data) {
            var code = data.code;
            var message = data.message;
            if("10000" == code){
                window.location.replace(basePath + "sso/redirect");
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

sso_setting.account_unbind = function () {
    var user_id = $("#user-id").val();
    if(!user_id) {
        alert('参数user_id丢失'); return;
    }
    var params = {
        'user_id': user_id
    };

    $.ajax({
        url: basePath + "account/unbind",
        data: params,
        type: "post",
        dataType:"json",
        beforeSend: function (){
        },
        success: function(data) {
            var code = data.code;
            var message = data.message;
            if("10000" == code){
                layer.alert('解绑成功,需要您重新登录');
                window.location.replace(basePath + "logout");
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

function enter_stbms(){
    var name = "stbms_url";
    var mycookie = document.cookie;
    var idx = mycookie.indexOf(name+'=');
    var url_value = null;
    if(idx == -1){
        url_value = null;
    }else {
        var start = mycookie.indexOf('=', idx) + 2;
        var end = mycookie.indexOf(';', start);
        if (end == -1) {
            end = mycookie.length-1;
        }
        url_value = unescape(mycookie.substring(start, end));
    }
    console.log(url_value)
    window.open(url_value);

}
