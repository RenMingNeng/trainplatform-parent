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
    <title>学员_学习</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/study.css" rel="stylesheet" type="text/css">
</head>
<body>

<div class="container">
    <div class="header clearfix">
        <h1 class="pull-left">水利安全生产标准化达标评审</h1>
        <small class="pull-left">
            学时进度：<span>00分00秒</span>
        </small>
        <div class="pull-right msg">
            查看简介>>
            <div class="intro">
                本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读
                本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读
                本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读
                本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读
                本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读
            </div>
        </div>
    </div>
    <div class="body">
        <div id="video" class="video_box"></div>
        <div class="play_list">
            <%--隐藏--%>
            <div class="addr">
                <span class="fa fa-angle-right"></span>
            </div>
            <ul class="clearfix">
                <li class="active">视频</li>
                <li class="last">文档</li>
            </ul>
            <div class="show_list" id="show_list">
                <div class="active">
                    <ul>
                        <li><a href="javascript:;" data-href="1.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">1.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">11.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="3.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="1.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                        <li><a href="javascript:;" data-href="2.mp4">21.本课程对水利水电施工企业安全生产标准化评审标准试行中的要素作业安全进行了详细解读</a></li>
                    </ul>
                </div>
                <div>
                    <ul><li><a href="javascript:;">1.文档文档文档文档文档文档文档文档文档文档文档文档文档文档</a></li></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/ckplayer/ckplayer.js"></script>

<script type="text/javascript">

    $(function(){


        //显示/隐藏播放列表
        $('.addr').click(function(){
            var $span = $(this).find('span');
            if( $span.hasClass('fa-angle-left') ){
                $('.video_box').css('right',300);
                $('.play_list').css('right',0);
                $span[0].className = 'fa fa-angle-right'
            }else if( $span.hasClass('fa-angle-right') ){
                $('.video_box').css('right',0);
                $('.play_list').css('right',-300);
                $span[0].className = 'fa fa-angle-left'
            }
        })

        //查看简介
        $('.msg').hover(function(){
            $(this).find('div').show();
        },function(){
            $(this).find('div').hide();
        })


        //模拟滑动块
        $('.show_list').niceScroll();

        //切换播放列表
        $('.play_list > ul ').on('click','li',function(){
            var $this = $(this);
            var index = $this.index();
            $this.addClass('active').siblings('li').removeClass('active');
            $('.show_list div').eq(index).addClass('active').siblings('div').removeClass('active');
        })


        /*视频切换*/
        $('.show_list li').on('click','a',function(){
            var $this = $(this);
            $('.show_list li a').removeClass('active');
            $this.addClass('active');
            var url = $this.attr('data-href');
            var flashvars={
                f:'<%=resourcePath%>static/global/telp/' + url,
                c:0,
                b:1,
                p:1
            };
            var params={bgcolor:'#292929',allowFullScreen:true,allowScriptAccess:'always',wmode:'transparent'};
            CKobject.embedSWF('<%=resourcePath%>static/global/js/ckplayer/ckplayer.swf','video','ckplayer_video','100%','100%',flashvars,params);
        })
        /*页面初始化，模拟点击事件*/
        $('.show_list li a:eq(0)').trigger('click');
    })
</script>
</body>
</html>
