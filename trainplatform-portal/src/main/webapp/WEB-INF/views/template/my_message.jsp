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
    <title>我的信息</title>
    <jsp:include page="${path}/common/style" />
    <link rel="stylesheet" href="<%=resourcePath%>static/global/css/detail.css">
    <style>
        .nav{display: none;}
        .header_top{background:#fff;}
    </style>
</head>
<body style="background:#f4f4f4;">
<jsp:include page="${path}/student/menu"></jsp:include>
<div class="main" id="main">
    <div class="area">
        <div class="box">
            <div class="box-header">
                <h3 class="box-title">我的消息列表</h3>
            </div>
            <div class="box-body pd_10" style="padding-top:0;">
                <div class="message_list">
                    <ul>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                        <li>
                            <div class="clearfix title">
                                <h5 class="pull-left">我是标题</h5>
                                <div class="pull-right r on">未读</div>
                            </div>
                            <div class="context">
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                                建立项目时受训角色屏蔽然后合格分默认为总分的考试题量默单选道判断多选
                                总共分当考试题量比总题量多的时候应将考试题量标红设置提醒管理员修改黄召勇
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/script" />

<script>
    $(function(){
        $('.message_list li').click(function(){
            var $this = $(this);
            $this.data('on',1);
            var flag = $this.data('on');
            if( flag === 1 ){
                $this.find('.title').find('.r').removeClass('on').text('已读');
            }
            if( $this.hasClass('show') ){
                $this.find('.context').slideUp()
                    .end().removeClass('show');
            }else{
                $this.find('.context').slideDown();
                $this.addClass('show').siblings('li').removeClass('show').find('.context').slideUp();
            }
        })
    })
</script>
</body>
</html>
