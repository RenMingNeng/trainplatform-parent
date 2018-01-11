<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8"/>
    <title>博晟 | 管理平台</title>
    <jsp:include page="${path}/common/style" />
    <jsp:include page="${path}/common/script" />
    <%--ue编辑器--%>
    <script type="text/javascript" charset="utf-8" src="<%=resourcePath%>/assets/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=resourcePath%>/assets/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=resourcePath%>/assets/ueditor/webutils.js"></script>
    <script type="text/javascript" charset="utf-8" src="<%=resourcePath%>/assets/js/article/article.js"></script>
    <script type="text/javascript">
        $(function() {
            //实例化编辑器 UE编辑器
            initUEEditor("content",'90%','500');
            //initKindEditor_addblog('content', 580, 400, 'articleContent', 'true');

        });
    </script>
</head>

<body class="main-bg">
<jsp:include page="${path}/common/menu" ><jsp:param name="menu" value="article"/></jsp:include>
<div class="lk-sub-nav">
    <div class="sub-nav-block">
        <ul class="sub-nav-ul">
            <li>
                <a href="${path}article" class="sub-nav-items<c:if test="${empty param.personType}"> sub-nav-hover</c:if>">全部</a>
            </li>
            <li>
                <a href="${path}article?personType=1" class="sub-nav-items<c:if test="${param.personType == '1'}"> sub-nav-hover</c:if>">不限</a>
            </li>
            <li>
                <a href="${path}article?personType=2" class="sub-nav-items<c:if test="${param.personType == '2'}"> sub-nav-hover</c:if>">学员</a>
            </li>
            <li>
                <a href="${path}article?personType=3" class="sub-nav-items<c:if test="${param.personType == '3'}"> sub-nav-hover</c:if>">管理员</a>
            </li>
        </ul>
    </div>
</div>
<!--内容-->
<div class="lk-pannel-main center-setting">
    <h4>设置文章信息</h4>
    <div class="center-box">
        <div class="center-section">
            <form id="article_form">
                <input type="hidden" name="id" value="${article.id}">
                <div class="center-row clearfix">
                    <label class="center-lable">文章标题:</label>
                    <input type="text" class="form-control center-store-right no-radius" name="title" placeholder="文章标题" value="${article.title}">
                </div>
                <div class="center-row clearfix">
                    <label class="center-lable">作者:</label>
                    <input type="text" class="form-control center-store-right no-radius" name="author" placeholder="作者" style="width: 120px;" value="${article.author}">
                </div>
                <div class="center-row clearfix">
                    <label class="center-lable">阅读人群:</label>
                    <div class="left" id="personType" data-value="${article.personType}">
                        <label><input type="radio" name="personType" value="1" checked="checked">&nbsp;不限</label>
                        <label><input type="radio" name="personType" value="2">&nbsp;学员</label>
                        <label><input type="radio" name="personType" value="3">&nbsp;管理员</label>
                    </div>
                </div>
                <div class="center-row clearfix">
                    <label class="center-lable">是否置顶:</label>
                    <div class="left" id="isTop" data-value="${article.isTop}">
                        <label><input type="radio" name="isTop" value="1" checked="checked">&nbsp;是</label>
                        <label><input type="radio" name="isTop" value="2">&nbsp;否</label>
                    </div>
                </div>
                <div class="center-row clearfix">
                    <label class="center-lable">状态:</label>
                    <div class="left" id="status" data-value="${article.status}">
                        <label><input type="radio" name="status" value="1" checked="checked">&nbsp;是</label>
                        <label><input type="radio" name="status" value="2">&nbsp;否</label>
                    </div>
                </div>
                <div class="center-row clearfix">
                    <label class="center-lable">序号:</label>
                    <input type="text" class="form-control center-store-right no-radius" name="orderNub" placeholder="序号" value="${article.orderNub}" style="width: 80px;">
                </div>


                <div class="center-row clearfix">
                    <label class="center-lable">文章内容:</label>
                    <div style="float: right;width: 100%;">
                        <textarea id="content" name="content" placeholder="文章内容">${article.content}</textarea>
                    </div>
                </div>

                <div class="center-section">
                    <div class="center-btn-bar">
                        <button class="lk-btn lk-btn-blue w100Per" onclick="article.update();">保存</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/script" />
</body>
</html>

