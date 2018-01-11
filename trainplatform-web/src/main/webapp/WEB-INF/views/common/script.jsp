<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<script type="text/javascript">
    var appPath = "<%=path %>";
    var basePath = "<%=basePath %>";
</script>
<script src="<%=resourcePath%>assets/js/jquery/jquery.min.js"></script>
<script src="<%=resourcePath%>assets/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script src="<%=resourcePath%>assets/js/zTree/js/jquery.ztree.all.min.js"></script>
<script src="<%=resourcePath%>assets/js/date.js"></script>
<script src="<%=resourcePath%>assets/My97DatePicker/WdatePicker.js"></script>
<script>
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
</script>