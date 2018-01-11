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
    <title>新闻公告-列表</title>
    <jsp:include page="${path}/common/style" />
    <link rel="stylesheet" href="<%=resourcePath%>static/global/css/detail.css">

</head>
<body class=" bg_fa">
<jsp:include page="${path}/student/menu"></jsp:include>
<div class="container">
    <div class="area">
        <div class="clearfix">
            <div class="pull-left w_890 pd_20 bg_f mg_t_20 mg_b_20">
                <div class="h">
                    <h2>雅思阅读能力提高有方法 文章精读可以这样做</h2>
                    <div class="clearfix">
                        <div class="pull-left">2016-02-26 10:31</div>
                        <div class="pull-right">作者什么的</div>
                    </div>
                </div>
                <div class="b">
                    <p>关于雅思(课程)精读，首先，认真选择精读文章，只需10篇剑桥文章，你的成绩就可以在7.5以上。但这一前提是你不是流于形式，而是认真走心的读。</p>
                    <p>我一直认为雅思精读最大的目的在于四点：</p>
                    <p>雅思考生公认精读来扫清阅读单词死角是再合适不过的了，尤其精读了几篇生物类文章，再答生物类全都认识了。比如C7蚂蚁智能里面的forage/scout/bearing/odour等词，精读过少量生物类文章，再去做OG上的swarm之类的文章就非常easy了，通篇可以快速读懂，准确定位，正确率超高。</p>
                    <p>粗定位一个定位词，全文可能会出现30多处，俗话说：两点定一线，你的关键词/定位词，至少要画两个以上还要全都找到。我一般建议学生“抓三点”“抓四点”“抓五点”，题配句，词换词，细定位就是要找至少两三个换的词。</p>
                    <p>段落信息配对题，因为无序且恶心，同义替换幅度较大，有时候需要通读全文。我却始终坚信“预测乃解决断子绝孙题的直通车”。只要精读了，你就会发现，原来文章各个部位都有暗示你过，那么下次如果你没读原文直接做MATCHING你要怎么“蒙题”，精读多了你就懂了</p>
                </div>
            </div>
            <div class="pull-right w_300 pd_20 bg_f mg_t_20 mg_b_20">
                <div class="b">
                    <dl>
                        <dt><h2>新闻列表</h2></dt>
                        <dd><span>1</span><a href="javascript:;">雅思阅读能力提高有方法 文章精读可以这样做</a></dd>
                        <dd><span>2</span><a href="javascript:;">中国军舰协助英、德等10国从也门撤侨225人</a></dd>
                        <dd><span>3</span><a href="javascript:;">MBA过来人亲述：联考英语复习经验</a></dd>
                        <dd><span>4</span><a href="javascript:;">MBA过来人亲述：联考英语复习经验</a></dd>
                        <dd><span>5</span><a href="javascript:;">渠道之殇：三星手机中国没落背后</a></dd>
                        <dd><span>6</span><a href="javascript:;">陈年重回T恤大战：凡客归来</a></dd>
                        <dd><span>7</span><a href="javascript:;">陈年重回T恤大战：凡客归来</a></dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
</body>
</html>
