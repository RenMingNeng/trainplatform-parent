/**
 * Created by Administrator on 2017/9/28.
 */
$(function(){
    var winH = $(window).height();
    $('.main').css('min-height',winH - 94);
    var len = $('.main_right > .questions').length;
    var flag = false;
    var current = 0;
    var aLi = '';


    $(window).resize(setLeftH).trigger('resize');
    /*当文档发生scroll事件是，改变答题卡版块的布局*/
    $(document).scroll(isFiexd).trigger('scroll');
    $(document).scroll(setLeftH);

    for( var i = 0; i < len; i++ ){
        aLi += '<li>' + (i+1) + '</li>';
    }
    //加载答题卡
    $('#card_list').html(aLi);
    //答题卡部分模拟滚动条
    $('#card_list').niceScroll();

    /*显示/隐藏答题卡*/
    $('.card dt').click(function(){
        var $this = $(this);
        var $icon = $this.find('.fa');
        $icon.toggleClass('fa-angle-double-down');
        $this.next('dd').toggle();
    })

    /*点击收藏*/
    $('.collect').on('click',function(e){
        e.stopPropagation();
        var href = window.location.href;
        var $this = $(this);
        var $e = $this.find('.fa');
        var $span = $(this).find('span');
        var question_id = $this.parents(".questions").attr("data-question");
        var project_id = $this.parents(".questions").attr("data-project");
        if( $e.hasClass('fa-star-o') ){//没选中状态
            $e.attr('class','fa fa-star on');
            $span.text('取消收藏');
            question_basic.addCollect(question_id, project_id);
        }else{
            if( href.indexOf('collection') > -1 ){
                layer.alert('是否取消收藏本题?',{
                    btn : ['是','否'],
                    yes: function(index){
                        $e.attr('class','fa fa-star-o');
                        $span.text('收藏试题');
                        question_basic.deleteCollect(question_id, project_id);
                        // window.location.reload();

                        question_exercise_ajax.refresh();
                        layer.close(index);
                    }
                })
            }else{
                $e.attr('class','fa fa-star-o');
                $span.text('收藏试题');
                question_basic.deleteCollect(question_id, project_id);
            }
        }

    })

    //设置main_left高度
    function setLeftH(){
        var winH = $(window).outerHeight(true);
        var $this = $('.main_left');
        var $other = $('.down_time');
        var headH = $('.header_top').outerHeight(true) + 10;
        var flag = $this.hasClass('pof');
       if( flag ){
            $this.css( 'height',winH - 20);
        }else{
            $this.css( 'height',winH - headH);
        }
        $this.css('padding-top',$other .height() + 20 );
        $('.card,.card_info').fadeIn();

    }

    //图片懒加载
    $("img.lazy").lazyload({
        effect: "fadeIn",
        threshold : 20000
    });

    //头部定位
    function isFiexd(){
        var scrollTop = $(document).scrollTop();
        var $e = $('.main_left');
        if( scrollTop > 144  ){
            $e.addClass('pof');
        }else{
            $e.removeClass('pof');
        }
        $('#card_list').getNiceScroll().resize();

    }

    //更改底部内容
    /*$('#footer').html('<div class="area">Copyright ©武汉博晟安全技术股份有限公司 中国·武汉 版权所有 All Rights Reserved.</div>')
    .css({
        'background':'#333',
        'lineHeight':'40px',
        'paddingTop':'0',
        'color':'#fff',
        'text-align':'center'
    });*/

    //图片预览功能
    $(".questions img").hover(function(){
        var $this = $(this);
        var src = $this.attr('data-original');
        $('body').append("<p class='pic'><img style='width:100%;' src='"+src+"'></p>");
        $this.mousemove(function(e){

            $(".pic").css({
                "max-width" : '300px',
                "top":e.pageY + 20 + "px",
                "left":e.pageX  + 20 + "px"
            }).fadeIn(1000);

        })
    },function(){
        $(".pic").remove();
    });

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
    });

    //单体模式切换
    $('#toggle').click(function(){
        flag = !flag;
        if( flag ){
            $('.questions').hide();
            $('.questions').eq(current).show();
            $('#btn-group').show();
            $('html,body').animate({scrollTop:0});
        }else{
            $('#btn-group').hide();
            $('.questions').show();
            $('html,body').stop().animate({
                scrollTop : $('.main_right>.questions').eq(current).offset().top
            },{duration:500,easing:'swing'});

        }
    });

    /*选择答案*/
    $('.questions').delegate('.option','click',function(e){
        var $this = $(this);
        var $p = $this.parents('.questions');
        var $aCheck = $p.find('.option');
        var id = $p.attr('id');
        var index = id.substring(3) - 1;
        var flag = true;
        if($this.hasClass ('checkbox')){
            flag = false;
            $this.toggleClass('on');
            $.each($aCheck,function(){
                if( $aCheck.hasClass('on') ){
                    flag = true;
                    return false;
                }
            })
        }else{
            $aCheck.removeClass('on');
            $this.addClass('on');
        }
        if( flag ){
            $("#card_list li:eq(" + index + ")").addClass('finish');
        }else{
            $("#card_list li:eq(" + index + ")").removeClass('finish');
        }
    });

    $('.main_right').delegate('.questions','click',function(){
        var $this = $(this);
        var id = $this.attr('id');
        var index = id.substring(3) - 1;
        current = index;
        if( !flag ){
            $('#card_list li:eq('+ index +')').addClass('current').siblings('li').removeClass('current');
            $('html,body').stop().animate({
                scrollTop : $('.main_right>.questions').eq(index).offset().top
            },{duration:500,easing:'swing'});
        }
    })

    $('#next').click(function(){
        if( current < len - 1 ){
            current++;
        }
        $('#card_list li').eq(current).addClass('current').siblings().removeClass('current');
        $('.questions').eq(current).show().siblings('.questions').hide();
    })
    $('#pre').click(function(){
        if( current > 0 ){
            current--;
        }
        $('#card_list li').eq(current).addClass('current').siblings().removeClass('current');
        $('.questions').eq(current).show().siblings('.questions').hide();
    })

    //点击答题卡事件
    $('#card_list li').click(function(){
        var $this = $(this);
        var index = $this.index();
        current = index;
        $this.addClass('current').siblings('li').removeClass('current');
        if( !flag ){
            $('html,body').stop().animate({
                scrollTop : $('.main_right>.questions').eq(index).offset().top
            },{duration:500,easing:'swing'});
        }else{
            $('.questions').eq(index).show().siblings('.questions').hide();
        }
    })
    $('#card_list li:eq(0)').addClass('current');

    //标记题目
    $(document).delegate('.btn-mark','click',function(){
        var $this = $(this);
        var text = $(this).text();
        if( text == '标记' ){
            $(this).text('取消标记');
        }else{
            $(this).text('标记');
        }
        var $p = $this.parents('.questions');
        var id = $p.attr('id');
        var index = parseInt( id.substring(3) ) - 1;
        $('#card_list li').eq(index).toggleClass('mark');
    })

})
function showAnswer(){
    var questions = $(".is_correct");
    for (var i=0;i<questions.length;i++){
        if(questions[i].innerText == '回答正确'){
            $("#card_list").find("li:eq(" + i + ")").addClass("success");
        }else{
            $("#card_list").find("li:eq(" + i + ")").addClass("error");
        }
    }
}