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
    <title>博晟 | 培训平台-课程预览</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/study.css" rel="stylesheet" type="text/css">
    <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
    <link href="<%=resourcePath%>static/global/js/videojs/css/video.css" rel="stylesheet" type="text/css">
    <style>
        .foot.mark{background:#3f3f3f;}
        .video_box{z-index:100000;}
        .vjs-has-started.vjs-user-inactive.vjs-playing .vjs-control-bar{opacity: 1!important;}
        .vjs-button>.vjs-icon-placeholder:before{font-size: 2.2em;}
        .vjs-paused .vjs-big-play-button{display: block;}
        .vjs-current-time.vjs-time-control.vjs-control,.vjs-time-control,.vjs-duration.vjs-time-control.vjs-control{display: block;}
        .video-js .vjs-duration{padding-left: 0;}
        .vjs-remaining-time.vjs-time-control.vjs-control{display: none;}
        .video-js .vjs-current-time{padding-right: 0;}
    </style>
</head>
<body>

<div class="container" style="z-index:10000;">
    <div class="study_border"></div>
    <a href="javascript:;" class="toggle prev"></a>
    <a href="javascript:;" class="toggle next"></a>
    <a href="javascript:;" class="back">
        <span class="fa fa-angle-left"></span>关闭
    </a>
    <div class="body">

        <div class="head">
            <span id="courseName">暂无课程信息</span>
        </div>
        <div class="video_box " >
            <video id="video" class="video-js vjs-default-skin" style="width:100%;height:100%;" controls preload="auto"></video>
            <div id="doc" style="width:100%;height:100%;"></div>
        </div>

        <div class="foot">
            <ul class="clearfix">
                <li class="msg">
                    <a href="javascript:;"><span class="fa fa-info-circle"></span> 课程介绍</a>
                    <div class="info" id="courseDesc">
                        课程简介
                    </div>
                </li>
                <li><a href="javascript:feed_back();" target="_blank"><span class="fa fa-edit"></span> 报告问题</a></li>
            </ul>
        </div>
    </div>
    <div class="play_list">
        <%--隐藏--%>
        <div class="addr">
            <span class="fa fa-angle-right"></span>
        </div>
        <ul class="clearfix">
            <li class="active"><span>视频</span></li>
            <li><span>文档</span></li>
        </ul>
        <div class="show_list" id="show_list">
            <div class="active">
                <ul id="show_list_video">
                    <!--<li class="empty">暂无视频</li>-->
                    <%--<li class="active"><a href="javascript:;" data-href="http://storage-sit.bosafe.com/api/v1/file/download/b0452f46371f414c9be508abc853084f.m3u8">1.课程视频0001</a></li>--%>
                </ul>
            </div>
            <div>
                <ul id="show_list_doc">
                    <!--<li class="empty">暂无文档</li>-->
                    <%--<li><a href="javascript:;">1.课程文档0001</a></li>--%>
                </ul>
            </div>
        </div>
    </div>
    <!-- hidden -->
    <input type="hidden" id="courseId" value="${courseId}">
</div>
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/jquery.min.js"></script>
<script src="<%=resourcePath%>static/global/js/scrollbar/nicescroll.min.js"></script>
<script src="<%=resourcePath%>static/global/js/jquery.base64.js"></script>

<!--
<script src="<%=resourcePath%>static/global/js/ckplayer/ckplayer.js"></script>
-->
<script src="<%=resourcePath%>static/global/js/videojs/js/video.min.js"></script>
<script src="<%=resourcePath%>static/global/js/videojs/js/videojs-contrib-hls.js"></script>
<script src="<%=resourcePath%>static/global/js/videojs/js/videojs-ie8.min.js"></script>
<script src="<%=resourcePath%>static/global/js/jquery.timer.js"></script>
<script src="<%=resourcePath%>static/global/js/jquery.blockui.min.js"></script>
<script src="<%=resourcePath%>static/global/js/admin/courseManager/course_view.js"></script>
<script type="text/javascript">

    $(function(){

      courseView.init("<%=basePath%>", $("#courseId").val());

      //显示/隐藏播放列表
      $('.addr').click(function(){
        var width = 320;
        var speed = 200;
        var $span = $(this).find('span');
        if( $span.hasClass('fa-angle-left') ){
          $('.play_list').stop().animate({
            'right':0
          },speed);
          $('.body').stop().animate({
            'right':width
          },speed);
          $span[0].className = 'fa fa-angle-right'
        }else if( $span.hasClass('fa-angle-right') ){
          $('.play_list').stop().animate({
            'right':-width
          },speed);
          $('.body').stop().animate({
            'right':0
          },speed);
          $span[0].className = 'fa fa-angle-left'
        }
      });

      //查看简介
      $('.msg').hover(function(){
        $(this).find('div').show();
      },function(){
        $(this).find('div').hide();
      });

      //模拟滑动块
      $('.show_list').niceScroll();

      //切换播放列表
      $('.play_list > ul ').on('click','li',function(){
        //clearInterval( studentStudy.timer);
        var $this = $(this);
        var index = $this.index();
        $this.addClass('active').siblings('li').removeClass('active');
        $('.show_list div').eq(index).addClass('active').siblings('div').removeClass('active');
        if(0 == index) {
          $("#video").show();$("#doc").hide();
          $('.foot').removeClass('mark');
        } else {
          $("#video").hide();
          $("#doc").show();
          $('.foot').addClass('mark');
          // 没有播放任何文档则播放第一个

          if(0 == $("#show_list_doc li a[class='active']").length) {
            $('#show_list_doc li a:eq(0)').trigger("click");
          }

          // 有播放视频则暂停
          if(0 != $("#show_list_video li a[class='active']").length) {
            courseView.player.pause();
          }
        }
      });
        /*页面初始化，模拟点击事件*/
      $('.show_list li a:eq(0)').trigger('click');


        /*上一个视频*/
      $('.prev').click(function(){
        var winH = $(window).height();
        var speed = 500;
        $('.body').stop().animate({
          top: -winH,
          bottom: winH
        },speed,function(){
          $('.body').css({top: winH, bottom: -winH});
          $('.body').stop().animate({top: 0,bottom: 0},speed);
        })
      });
        /*下一个视频*/
      $('.next').click(function(){
        var winH = $(window).height();
        var speed = 500;
        $('.body').stop().animate({
          top: winH,
          bottom: -winH
        },speed,function(){
          $('.body').css({top: -winH, bottom: winH});
          $('.body').stop().animate({top: 0,bottom: 0},speed);
        })
      });
    })
</script>
</body>
</html>
