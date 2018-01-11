$(function() {
    var _this = this
/*    var winH = $(window).height();
    var headH = $('#header').outerHeight(true);
    var head_topH = $('.header_top').outerHeight(true);
    var footH = $('#footer').outerHeight(true);
    var timer = null;
    //当窗口发生变化时，触发
    $(window).resize(function(){
        clearTimeout( timer );
        timer = setTimeout(function(){
            setMainH();
        },500);
    })
    setMainH();*/
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
                // alert("参数错误：" + respCode);return;
                message.add("成功的消息", "error");return;
            }
            if("50500" == respCode) {
                // alert("服务异常：" + respCode);return;
                message.add("服务异常：" + respCode, "error");return;
            }
            if("50503" == respCode) {
                // alert("用户未登录：" + respCode);return;
                message.add("用户未登录：" + respCode, "error");return;
            }
            if("50505" == respCode) {
                // alert("签名错误：" + respCode);return;
                message.add("签名错误：" + respCode, "error");return;
            }
            if("50506" == respCode) {
                // alert("请求无效 会话已过期：" + respCode);
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


    /*首页菜单*/
	var timer = null;
	$('.nav li').hover(function(){
		if( $(this).find('dl') !== null ){
			var _this = $(this);
			_this.find('dl').stop().fadeIn();
		}
	},function(){
		if( $(this).find('dl') !== null ){
			var _this = $(this);
			_this.find('dl').stop().fadeOut();
		}
	})
    function setMainH(){
        $('#main').css('min-height',winH - footH - headH - head_topH);
    }
});

var Enum= new Object();
    //项目类型
    Enum.projectType = function (type) {
        if('1'==type){
            return "培训"
        }else if('2'==type){
            return "练习"
        }else if('3'==type){
            return "考试"
        }else if('4'==type){
            return "培训-练习"
        }else if('5'==type){
            return "培训-考试"
        }else if('6'==type){
            return "练习-考试"
        }else{
            return "培训-练习-考试"
        }
    }

    //项目状态
    Enum.projectStatus = function (status) {
        if('1'==status){
            return "<span class=\"text-red\">" + "未发布" + "</span>";
        }else if('2'==status){
            return "<span class=\"text-gray\">" + "未开始" + "</span>";
        }else if('3'==status){
            return "<span class=\"text-green\">" + "进行中" + "</span>";
        }else{
            return "<span class=\"text-gray\">" + "已结束" + "</span>";
        }
    }
  Enum.projectModel = function (model) {
     if("0"==model){
         return "私有";
     }else if("1"==model){
        return "公开";
     }
  }


    project_time_format = function(projectTime){
      if(!projectTime) return "";
      var date = new Date(projectTime);
      date = date.Format("yyyy-MM-dd HH:mm:ss");
      var timeFormat = date.substring(0,10);
      return timeFormat;
    }

    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }

