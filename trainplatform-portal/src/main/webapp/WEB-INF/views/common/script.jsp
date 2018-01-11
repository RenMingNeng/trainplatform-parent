<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>

<script type="text/javascript">
    var appPath = "<%=path %>";
    var resourcePath = "<%=resourcePath %>";
    var empty_img_src = "<%=resourcePath %>"+ "static/global/images/empty.png";
    var loading_img_src = "<%=resourcePath %>"+ "static/global/images/loading.gif";
</script>
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/zTree/js/jquery.ztree.core-3.5.js"></script>
<script src="<%=resourcePath%>static/global/js/jquery.form.js"></script>
<script src="<%=resourcePath%>static/global/js/msg/js/jquery.my-message.1.1.js"></script>
<script src="<%=resourcePath%>static/global/js/scrollbar/nicescroll.min.js"></script>
<script src="<%=resourcePath%>static/global/js/layer/layer.js"></script>
<script src="<%=resourcePath%>static/global/js/zTree/js/jquery.ztree.all.min.js"></script>
<script src="<%=resourcePath%>static/global/js/My97DatePicker/WdatePicker.js"></script>
<script src="<%=resourcePath%>static/global/js/lazyload/jquery.lazyload.min.js"></script>
<script src="<%=resourcePath%>static/global/js/icheck/icheck.min.js"></script>
<script src="<%=resourcePath%>static/global/js/mSlider.js"></script>
<script src="<%=resourcePath%>static/global/js/cookie.js"></script>
<script src="<%=resourcePath%>static/global/js/common/base.js"></script>
<script src="<%=resourcePath%>static/global/js/page.js"></script>
<script src="<%=resourcePath%>static/global/js/common/date.js"></script>
<script src="<%=resourcePath%>static/global/js/common.js"></script>
<script src="<%=resourcePath%>static/global/js/tooltip.js"></script>
<script src="<%=resourcePath%>static/global/js/sso/setting.js"></script>
<script>
    // 重写浏览器console日志输出, 避免浏览器因不支持或未启用console.log导致js不往下执行的问题
    if(typeof window.console == "undefined") {
        // 浏览器不支持或者未开启console, 则禁用日志
        window.console = {
            log: function () {}
        };
    } else {
        // 增加日志输出手动开关
        var url = window.top.location.href;
        if(url.indexOf("127.0.0.1") != -1 || url.indexOf("localhost") != -1 || url.indexOf("openlog") != -1) {
            // 如果是本机, 或者开启了日志, 则正常使用console

        } else {
            // 非本机, 也未开启, 则禁用console
            window.console = {
                log: function () {}
            };
        }
    }
</script>
<script>

    var message;
    $(document).ready(function(){

        message = new MyMessage.message({
            /*默认参数，下面为默认项*/
            iconFontSize: "20px", //图标大小,默认为20px
            messageFontSize: "12px", //信息字体大小,默认为12px
            showTime: 3000, //消失时间,默认为3000
            align: "center", //显示的位置类型center,right,left
            positions: { //放置信息距离周边的距离,默认为10px
                top: "10px",
                bottom: "10px",
                right: "10px",
                left: "10px"
            },
            message: "这是一条消息", //消息内容,默认为"这是一条消息"
            type: "normal", //消息的类型，还有success,error,warning等，默认为normal
        });
        /*两种不同的设置方式*/
        message.setting({
            align: "bottom" //会覆盖前面的所有设置,可以创建不同的对象去避免覆盖
        });
        message.setting("showTime", "5000");


        // go top
        $('#gotop').click(function(){
           $('body,html').animate({
               scrollTop : 0
            },500);
        });
        $('#tbox>div').hover(function(){
            var $this = $(this);
            $this.addClass('hover');
            $this.children('div').show().stop().animate({
                right : '80px'
            },300,'swing');
        },function(){
            var $this = $(this);
            $this.removeClass('hover');
            $this.children('div').hide().stop().animate({
                right : '100px'
            },300);
        });


        $(window).scroll(toggleTopBtn).trigger('scroll');
        $(window).resize(windowResize).trigger('resize');

        //解决ajax请求得到的数据，nicescorll插件不兼容的问题
        $(document).delegate('#pageSize,input[name="all"]','change',function(){
            if( $('.table_inner')[0]  !== null ){
                setTimeout(function(){
                    $('.table_inner').getNiceScroll().resize();
                },500)
            }
        });

        $('.hand-hover').hover(function(){
            $(this).addClass('hover');
        },function(){
            $(this).removeClass('hover');
        })
    });

    function windowResize(){
        var winW = $(window).width();
        if( winW < 1200 ){
            $('body').addClass('sm-body');
        }else{
            $('body').removeClass('sm-body');
        }
    }


    /*function a(){

        var $area = $('.area:eq(0)');

        if( $area[0] ){
            var offset_L = $area.offset().left;
            var area_w = $area.width();
            $('#tbox').css('left',(offset_L + area_w +  10)).css('bottom',100).show();
            //$('#tbox').css('bottom',y + 'px');

        }

    };*/

    //切换显示侧边栏按钮---回到顶部的显示、隐藏
    function toggleTopBtn(){
        var t = $(document).scrollTop();
        if(t > 120){
            $('#gotop').fadeIn();
        }else{
            $('#gotop').fadeOut();
        }
    };



    /**
     * 课程预览
     * @param id layer弹出id
     * @param url 预览url
     */
    function popupWindow(url){
        if($("#popupWindow").length == 0) {
            $("body").append("<div class=\"wrap\"><div id=\"popupWindow\" style=\"opacity: 0;padding: 2px;\" class=\"layer-right\"><iframe frameborder=\"0\" width=\"100%\" height=\"100%\"></iframe></div></div>");
        } else {
            $("#popupWindow iframe").remove();
        }
        var slider = new mSlider({dom: ".layer-right", direction: "right"});
        slider.open();
        setTimeout(function(){
            $("#popupWindow iframe").attr("src", url);
        }, 3000);
    }

    formatSeconds = function (a){
        if(0 >=a)
            return "00时" + "00分" + "00秒";
        var hh = parseInt(a/3600);
        if(hh < 10) hh = "0" + hh;
        var mm = parseInt((a - hh*3600)/60);
        if(mm < 10) mm = "0" + mm;
        var ss = parseInt((a - hh*3600)%60);
        if(ss < 10) ss = "0" + ss;
        var result = hh + "时" + mm + "分" + ss + "秒";
        if(a > 0)
            return result;
        return "00时" + "00分" + "00秒";
    }

    function flashChecker () {
        var hasFlash = false; // 是否安装了flash
        var flashVersion = 0; // flash 版本
        if(document.all) {
            var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
            if(swf) {
                hasFlash = true;
            }
        } else {
            if(navigator.plugins && navigator.plugins.length !=0) {
                var swf = navigator.plugins["Shockwave Flash"];
                if(swf) {
                    hasFlash = true;
                }
            }
        }
        return hasFlash;
    }
    function clearCheched(){
        $('table input[type="checkbox"]').prop('checked',false);
        $('table input[type="radio"]').prop('checked',false);
    }
</script>
<script type="text/javascript">

    sso_setting.init();

    function modifyPass(userId){
        var url="<%=path%>/modifyPass?userId="+userId;
        layer.open({
            type: 2,
            title: '修改密码',
            area: ['450px', '380px'],
            shade: 0.3,
            closeBtn: 1,
            shadeClose: false,
            content: url
        });
    }

</script>