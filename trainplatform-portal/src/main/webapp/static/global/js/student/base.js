
$(function() {
    var winH = $(window).height();
    var headH = $('#header').outerHeight(true);
    var footH = $('#footer').outerHeight(true);
    var timer = null;
    //当窗口发生变化时，触发
    $(window).resize(function(){
        clearTimeout( timer );
        timer = setTimeout(function(){
            setMainH();
        },500);
    })
    setMainH();

    /**
     * 全局ajax封装
     */
    $.ajaxSetup({
        contentType: "application/x-www-form-urlencoded;charset=UTF-8",
        cache: false,
        complete: function(xhr, ts){

            var respStat = xhr.status;
            if("911" == respStat) {
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
                    content: "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300'>经系统检测,存在未登录或登录超时,您需要重新登陆</div>",
                    yes: function(layero) {
                        window.location.href = appPath;
                    }
                });
                return;
            }

            var respText = xhr.responseText;
            var respJson = eval("("+respText+")");
            var respCode = respJson.code;
            if("20000" == respCode) {
                message.add("成功的消息", "error");return;
            }
            if("50500" == respCode) {
                message.add("服务异常：" + respCode, "error");return;
            }
            if("50502" == respCode) {
                message.add("授权失败：" + respCode, "error");return;
            }
            if("50503" == respCode) {
                message.add("用户未登录：" + respCode, "error");return;
            }
            if("50505" == respCode) {
                message.add("签名错误：" + respCode, "error");return;
            }
            if("50506" == respCode) {
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
                    content: "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300'>经系统检测,存在未登录或登录超时,您需要重新登陆</div>",
                    yes: function(layero) {
                        window.location.href = appPath;
                    }
                });
                return;
            }
        }
    });

	/*切换角色状态*/
	$('.select_user_type').on('click',function(){
		$('.user_type_menu').show();
	})
	$('.user_type_menu li').on('click',function(){
		var text = $(this).text();
		$('.user_type').text(text);
		$('.user_type_menu').hide();
	})

    function setMainH(){
        $('#main').css('min-height',winH - footH - headH);
    }
});
