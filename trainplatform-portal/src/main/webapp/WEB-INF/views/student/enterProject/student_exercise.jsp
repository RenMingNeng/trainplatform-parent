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
    <title>学员_练习</title>
    <jsp:include page="${path}/common/style" />
    <link href="<%=resourcePath%>static/global/css/exercise.css" rel="stylesheet" type="text/css">
</head>
<body>
<div id="header">
    <div id="sub_header_wrapper" class="clearfix area">
        <img src="<%=resourcePath%>static/global/images/logo.png" height="65" width="400" alt="" class="logo">
        <div class="user_logo">
            <ul class="clearfix">
                <li>用户：施炜低</li>
                <li class="split">|</li>
                <li>
                    角色：
                    <a href="javascript:;" class="select_user_type">
                        <span class="user_type">学员</span>
                        <span class="sanjiao"></span>
                    </a>
                </li>
                <li class="split">|</li>
                <li><a href="javascript:;">注&nbsp;&nbsp;&nbsp;&nbsp;销</a></li>
            </ul>
            <ol class="user_type_menu">
                <li class="sanjiao"></li>
                <li>学员</li>
                <li>管理员</li>
            </ol>
        </div>
        <div class="system_info">
            <div>
                <span class="news text-24">7</span>
                <span class="text-14">登录次数</span>
            </div>
            <div>
                <img src="<%=resourcePath%>static/global/images/pic_divide.png">
            </div>
            <div>
                <span class="learn_time text-24">1250h</span>
                <span class="text-14">学习时长</span>
            </div>
        </div>
        <div id="menu_li">
            <ul>
                <li>我的练习</li>
            </ul>
        </div>
    </div>
</div>
<div class="main area">
    <div class="main_left">
        <dl class="down_time">
            <dt><i class="fa fa-clock-o"></i>&nbsp;46分54秒</dt>
            <dd><a href="javascript:;">我要交卷</a></dd>
            <dd><a href="javascript:;">暂停</a></dd>
            <dd><a href="javascript:;">下次再做</a></dd>
            <dd><a href="javascript:;"><label for="id"><input type="radio" id="id"> 单题模式</label></a></dd>
        </dl>
        <dl class="card">
            <dt class="text-16">答题卡 <i class="fa fa-angle-double-up"></i></dt>
            <dd>
                <ul class="clearfix" id="card_list">
                </ul>
            </dd>
        </dl>
    </div>
    <div class="main_right">
        <h2>《水利部关于进一步加强水利安全生产应急管理提高生产安全事故应急处置能力的通知》</h2>
        <div id="div0">
            <div class="subject">
                <span class="index">1</span><span class="text-blue">（单选题）</span>《水利部关于进一步加强水利安全生产应急管理 提高生产安全事故应急处置能力的通知》规定，流域机构的应急预案需经水利部核准并抄送（     ）。
            </div>
            <div class="option">
                <p>A、国家安全生产监督管理总局</p>
                <p>B、流域内水利建设项目法人和生产经营单位</p>
                <p>C、流域内水利建设项目法人和生产经营单位</p>
                <p>D、国家安全生产监督管理总局</p>
            </div>
            <div class="option_select clearfix">
                <div class="pull-left">
                    <label for="A">
                        <input type="radio" id="A"> A
                    </label>
                    <label for="B">
                        <input type="radio" id="B"> B
                    </label>
                    <label for="C">
                        <input type="radio" id="C"> C
                    </label>
                    <label for="D">
                        <input type="radio" id="D"> D
                    </label>
                </div>
                <div class="pull-right">
                    <a href="javascript:;" class="collect"><i class="fa fa-star-o"></i> 收藏试题</a>
                    &nbsp;
                    <a href="javascript:;"><i class="fa fa-edit"></i> 错题反馈</a>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script>
    $(function () {

        var winH = $(window).height();


        //答题卡加静态选项
        /*这些数据可以删     start*/
        var aLi = '';
        var aDiv = ''
        var oDiv = $('.main_right>div').html();
        for( var i = 0; i < 40; i++ ){
            aLi += '<li><a href="#div'+ i +'">' + (i + 1)  + '<a/></li>'
            aDiv += '<div id="div' + (i + 1) + '">' + oDiv + '</div>';
        }
        $('#card_list').html( aLi );
        $('#card_list li').eq(0).addClass('current');
        $('.main_right')[0].innerHTML += aDiv;
        /*这些数据可以删     end*/


        //点击答题卡事件
        $('#card_list li').click(function(){
            var $this = $(this);
            var id = $this.find('a').attr('href');
            console.log( id );
            $this.addClass('current').siblings('li').removeClass('current');
            $('html,body').animate({
                scrollTop : $(id).offset().top
            },{duration:500,easing:'swing'});
        })


        $('#card_list').niceScroll({cursoropacitymax:0});


        //模拟input表单选择项
        $('input').iCheck({
            checkboxClass: 'icheckbox_square-green',
            radioClass: 'iradio_square-green',
        });

        //设置main_left高度
        $('.main_left').css( 'height',winH - 100 );


        /*点击收藏*/
        $('.collect').on('click',function(){
            var $this = $(this);
            var $e = $this.find('.fa');
            if( $e.hasClass('fa-star-o') ){
                $e.attr('class','fa fa-star').css('color','#00b8c7');
            }else{
                $e.attr('class','fa fa-star-o').css('color','#666');
            }
        })

        /*显示/隐藏答题卡*/
        $('.card dt').click(function(){
            var $this = $(this);
            var $icon = $this.find('.fa');
            $icon.toggleClass('fa-angle-double-down');
            $this.next('dd').toggle();
        })

        /*当文档发生scroll事件是，改变答题卡版块的布局*/
        $(document).scroll(function(e){
            var winH = $(window).height();
            var scrollTop = $('body').scrollTop();
            if( scrollTop > 144 ){
                $('.main_left').css({
                    'position':'fixed',
                    'top' : '10px'
                });
            }else{
                $('.main_left').css({
                    'position':'relative',
                    'top' : '0'
                });
            }
        })

    })
</script>
</body>