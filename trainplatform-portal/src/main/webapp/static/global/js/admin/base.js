$(function() {
    var _this = this;
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
            return "培训、练习"
        }else if('5'==type){
            return "培训、考试"
        }else if('6'==type){
            return "练习、考试"
        }else{
            return "培训、练习、考试"
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
      var timeFormat = projectTime.substring(0,10);
      return timeFormat;
    }

