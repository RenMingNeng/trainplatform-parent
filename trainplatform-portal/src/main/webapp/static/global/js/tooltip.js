/**
 * Created by Administrator on 2017/8/14.
 */
/*    $(function(){
        setTimeout(function(){
            $('.tooltip').attr('title',$('.tooltip').text());
        },50)

    })*/

    //模拟title事件
function tooptip(){

}
    /*function tooptip(){
        'use strict';
            var dataLength = 20;
          $(document).delegate('.tooltip','mouseover',function(){
            var $this = $(this);
            var newHtml = $.trim($this.html());
            if( !$this.find('.tooptip_inner')[0] ){
              var $children = $('<div class="tooptip_inner"></div>').html( newHtml );
              $children.append('<span class="tooptip-icon"></span><span class="tooptip-icon-conver"></span>');
              dataLength = $this.attr('data-length') || dataLength;
              newHtml = newHtml.substring(0,dataLength);
              $this.html( newHtml );
              $this.append( $children );
              var h = $this.find($('.tooptip_inner')).outerHeight();
              $children.css('top', -h - 20 );
            }

            $this.find($('.tooptip_inner')).stop().fadeIn();
          });
            $(document).delegate('.tooltip','mouseout',function(){
                var $this = $(this);
              $this .find($('.tooptip_inner')).stop().fadeOut(300);
            })
       /!* $('.tooltip').each(function(i,n){
            var dataLength = 20;
            var $this = $(this);
            var newHtml = $.trim($this.html());
            var $children = $('<div class="tooptip_inner"></div>').html( newHtml );
            $children.append('<span class="tooptip-icon"></span><span class="tooptip-icon-conver"></span>');
            dataLength = $this.attr('data-length') || dataLength;
            newHtml = newHtml.substring(0,dataLength);
            $this.html( newHtml );
            $this.append( $children );
            alert();
            $(document).delegate($this,'mouseover',function(){
              alert();
              var h = $this.find($('.tooptip_inner')).outerHeight();

              $children.css('top', -h - 20 );
              $this.find($('.tooptip_inner')).stop().fadeIn();
            })

           /!* $this.hover(function(){


            },function(){
                $this .find($('.tooptip_inner')).stop().fadeOut(300);

            })*!/
        })*!/
    }

    $(function(){
      tooptip();
    })*/

/*
;(function(dialog_box_info,$,obj){

    dialog_box_info.default = {
        addr : 'bottom',
        text : '显示消息'
    };
    dialog_box_info.init = function(){
        dialog_box_info.create();
        dialog_box_info.close();
    };
    dialog_box_info.create = function(){
        var options = {
            addr : $(obj).data('addr'),
            text : $(obj).data('text')
        };
        $.extend(this.default, options );
        var oDiv = '<div class="dialog_box_info_containner ' + this.default.addr + '">' +
                        '<div class="dialog_box_info_box">' +
                            '<div class="dialog_box_info_content">' +
                                this.default.text +
                            '</div>'+
                        '</div>'+
                        '<span class="dialog_box_info_close">×</span>'+
                    '</div>';
        $(obj).css('position','relative');
        $(obj).append($(oDiv));
    };
    dialog_box_info.close = function(){
        $(document).delegate('.dialog_box_info_close','click',function(e){
            e.stopPropagation();
            $('.dialog_box_info_containner').fadeOut(function(){
                $(this).remove();
            });
        })
    };
    dialog_box_info.show = function(){
        $('.dialog_box_info_containner').fadeIn();
    }
    window.dialog_box_info = dialog_box_info;

})(window.dialog_box_info || {},jQuery, '.dialog_box_info')*/
