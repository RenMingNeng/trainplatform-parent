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
  <title>博晟 | 培训平台-管理员-fuck</title>
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="${path}/console/menu" >
  <jsp:param name="menu" value="onlineuser"></jsp:param>
</jsp:include>




<div id="main">
  <div class="area">
    <div id="addr"><a href="project_index.html">在线用户</a></div>
    <div class="box">
    <div class="box-body">
      <table class="table">
        <thead>
        <tr>
          <th nowrap="nowrap">序号</th>
          <th nowrap="nowrap">主机</th>
          <th nowrap="nowrap">在线用户数</th>
          <th nowrap="nowrap">操作</th>
        </tr>
        </thead>
        <tbody>
          <c:forEach var="server" items="${servers}" varStatus="status">
          <tr>
            <td>${status.index + 1}</td>
            <td>${server}</td>
            <td>12</td>
            <td><a class="link">详情</a></td>
          </tr>
          </c:forEach>
        </tbody>
      </table>
      <form class="clearfix">
        <div>
          <div class="clearfix search-group">
            <div class="pull-right">
              <label>用户名称</label>
              <div class="search">
                <input type="text" placehodler="请输入关键字">
                <button type="button" title="搜索">
                  <span class="fa fa-search"></span>
                </button>
              </div>
              <a href="javascript:;">全部</a>
            </div>
          </div>
        </div>
      </form>
      <table class="table pro_msg index_table">
        <thead>
        <tr>
          <th nowrap="nowrap">序号</th>
          <th nowrap="nowrap">用户id</th>
          <th nowrap="nowrap">用户名称</th>
          <th nowrap="nowrap">会话id</th>
          <th nowrap="nowrap">主机-ip</th>
          <th nowrap="nowrap">环境</th>
          <th nowrap="nowrap">位置</th>
          <th nowrap="nowrap">代理信息</th>
          <th nowrap="nowrap">登录时间</th>
          <th nowrap="nowrap">操作</th>
        </tr>
        </thead>
        <tbody id="fuck_tbody">
          <!--
          browser:"Chrome"
          city:null
          country:null
          hostname:"0:0:0:0:0:0:0:1"
          ip:"0:0:0:0:0:0:0:1"
          loginTime:1503627458624
          opersystem:"windows"
          province:null
          sessionId:"F9290B7449C7C36203414D29A59AE011"
          userId:"2a64fd67-066d-428a-bb46-b93a25255937"
          userName:"大唐管理员"
          useragent:"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.89 Safari/537.36"
          -->
        </tbody>
      </table>
      <%--
        text-left、text-center、text-right
        分别让分页版块左、中、右
      --%>
      <div class="page text-right" >
        <ul id="fuck_page"></ul>
      </div>
    </div>
  </div>
  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/console/onlineuser/index.js"></script>
<script>
  $(function(){
      var basePath = "<%=basePath%>";
      fuck.init(basePath, "fuck_tbody", "fuck_page", 1, 2);
      fuck.goPage(1);
  })

</script>
</body>

</html>


