$(function() {
    /*顶部一级菜单点击切换*/
    $('.lk-menu-items').on('click', function(e) {
        var $active = $(this).siblings('.lk-menu-items').find('.lk-menu-active').parent('.lk-menu-items');
        $active.find('.lk-menu-active').removeClass('lk-menu-active');
        $active.find('.lk-menu-line').remove();
        $(this).find('.lk-menu-link').addClass('lk-menu-active');
        $(this).append('<div class="lk-menu-line"></div>');
    });
    /*左侧二级菜单单机切换*/
    $('.sub-nav-items').on('click', function(e) {
        $('.sub-nav-hover').removeClass('sub-nav-hover');
        $(this).addClass('sub-nav-hover');
    })
})