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

<div class="main area clearfix">
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
        <div class="questions">
            <div class="no-content"></div>
        </div>
        <div id="div0" class="questions" data-course="182" data-question="17352" data-project="0epafp1p4qji" data-index="0">
            <div class="subject">
                <span class="index">1</span>
                <span class="title_bg">（单选题）</span>
                下列关于班组的描述中，不正确的是（     ）。
                <div class="show_img clearfix"></div>
            </div>
            <div class="options">
                <div>
                    <div class="option" data-type="A">
                        <span></span>
                        <div>A、90%的事故与班组无关</div>
                    </div>
                    <div class="show_img clearfix">
                    </div>
                </div>
                <div>
                    <div class="option" data-type="B">
                        <span></span>
                        <div>B、班组是企业最基层的单位</div>
                    </div>
                    <div class="show_img clearfix">
                    </div>
                </div>
                <div>
                    <div class="option" data-type="C">
                        <span></span>
                        <div>C、班组安全管理是企业安全管理的基础</div>
                    </div>
                    <div class="show_img clearfix">
                    </div>
                </div>
                <div>
                    <div class="option" data-type="D">
                        <span></span>
                        <div>D、班组安全管理是一切安全管理工作的出发点和落脚点</div>
                    </div>
                    <div class="show_img clearfix">
                    </div>
                </div>
            </div>
            <div class="option_select clearfix" data-type="01">
                <div class="pull-left">
                    <button type="button" class="btn btn-info sure">确定</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-mark">标记</button>
                </div>
                <div class="pull-right">
                    <a href="javascript:;" class="remove_error" style="display: none;">
                        <i class="fa fa-remove"></i> 移除错题
                    </a>
                    <a href="javascript:;" class="collect">
                        <i class="fa fa-star-o"></i> 收藏试题
                    </a>
                    <a href="javascript:;" class="analy" style="display: none;">
                        展开解析 <i class="fa fa-angle-down"></i>
                    </a>
                </div>
            </div>
            <div class="subject_foot">
                <div>
                    <span class="label">正确答案：</span><i class="answer">A</i>
                </div>
                <div class="clearfix">
                    <span class="label">试题解析：</span>
                    <div class="pull-left analysis">暂无解析</div>
                </div>
            </div>
        </div>
        <div id="div1" class="questions">
            <div class="subject">
                <span class="index">2</span>
                <span class="title_bg">（填空题）</span>
                班组安全管理是一切安全管理工作的出发点和落脚点班组安全管理是一切安全管理工作的出发点和落脚点 （&nbsp;&nbsp;）
                班组安全管理是一切安全管理工作的出发点和落脚点班组安全管理是一切安全管理工作的出发点和落脚点
                班组安全管理是一切安全管理工作的出发点和落脚点班组安全管理是一切安全管理工作的出发点和落脚点
                班组安全管理是一切安全管理工作的出发点和落脚点班组安全管理是一切安全管理工作的出发点和落脚点
                <div class="show_img clearfix"></div>
            </div>
            <div class="options">
                <div>
                    <b class="text_info">答：</b>
                    <textarea></textarea>
                </div>
            </div>
            <div class="option_select clearfix" data-type="01">
                <div class="pull-left">
                    <button type="button" class="btn btn-info sure">确定</button>&nbsp;&nbsp;
                    <button type="button" class="btn btn-mark">标记</button>
                </div>
                <div class="pull-right">
                    <a href="javascript:;" class="remove_error" style="display: none;">
                        <i class="fa fa-remove"></i> 移除错题
                    </a>
                    <a href="javascript:;" class="collect">
                        <i class="fa fa-star-o"></i> 收藏试题
                    </a>
                    <a href="javascript:;" class="analy" style="display: none;">
                        展开解析 <i class="fa fa-angle-down"></i>
                    </a>
                </div>
            </div>
            <div class="subject_foot">
                <div>
                    <span class="label">正确答案：</span><i class="answer">A</i>
                </div>
                <div class="clearfix">
                    <span class="label">试题解析：</span>
                    <div class="pull-left analysis">暂无解析</div>
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
        var len = $('.main_right > div').length;
        var aLi = '';
        for( var i = 0; i < len; i++ ){
            aLi += '<li><a href="javascript:;">' + (i+1) + '</a></li>';
        }
        $('#card_list').html(aLi);
        //点击答题卡事件
        $('#card_list li').click(function(){
            var $this = $(this);
            var id = $this.find('a').attr('href');
            $this.addClass('current').siblings('li').removeClass('current');
            $('html,body').animate({
                scrollTop : $(id).offset().top
            },{duration:500,easing:'swing'});
        })


        $('#card_list').niceScroll({cursoropacitymax:0});



        //设置main_left高度
        $('.main_left').css( 'height',winH - 251);
        $('.main_left').css('padding-top', $('.down_time').height() + 10 );

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

        //解析切换显示
        $('.analy').click(function(){
            var $this = $(this);
            var text = $this.text();
            var $parents = $this.parents('.questions');
            if( text === '展开解析 ' ){
                $this.html('收起解析 <i class="fa fa-angle-up"></i>');
            }else{
                $this.html('展开解析 <i class="fa fa-angle-down"></i>');
        }
            $parents.find('.subject_foot').toggle();
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
                    'top' : 0
                });
            }
        })
        //图片放大
        $(".questions img").hover(function(){
            var $this = $(this);
            var src = $this.attr('data-original');
            $('body').append("<p class='pic'><img src='"+src+"'></p>");
            $this.mousemove(function(e){

                $(".pic").css({
                    "top":e.pageY + 20 + "px",
                    "left":e.pageX  + 20 + "px"
                }).fadeIn(1000);

            })
        },function(){
            $(".pic").remove();
        });
    })
</script>
</body>