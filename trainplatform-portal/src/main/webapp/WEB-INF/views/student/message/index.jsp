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
  <title>博晟 | 培训平台-管理员-人员管理</title>
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
</head>

<body>
<jsp:include page="${path}/popup/menu" >
  <jsp:param name="menu" value="message"></jsp:param>
</jsp:include>

<div id="main">

  <div class="area">
    <div class="box">
        <form id="message_form">
          <div class="box-header clearfix">
            <h3 class="box-title pull-left">我的消息</h3>
            <div class="pull-right search-group">
              <a href="javascript:back();" class="btn btn-info"  id="btnback"><span class="fa fa-reply"></span> 返回</a>
            </div>
          </div>
          <div class="box-body" style="padding:0;">
            <div class="status_flag">
              <dl>
                <%--<dt>项目类型:</dt>--%>
                <dd class="clearfix" id="message_status">
                  <span data-value="" class="active">全部</span>
                  <span data-value="1">未读</span>
                  <span data-value="2">已读</span>
                </dd>
              </dl>
            </div>
            <%--公开标识--%>
            <input type="hidden" name="messageStatus">
            <table class="table pro_msg admin_index_table">
              <thead>
              <tr>
                <th width="46">序号</th>
                <th width="160">消息标题</th>
                <th width="260">消息内容</th>
                <th width="140">发送人</th>
                <th width="140">发送时间</th>
                <th width="140">状态</th>
                <th width="140">操作</th>
              </tr>
              </thead>
              <tbody id="message_table">
                  <tr>
                    <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                  </tr>
              </tbody>
            </table>
          </div>
        </form>

        <%--分页--%>
        <div class="page text-right" >
          <ul id="message_page" class="clearfix"></ul>
        </div>
    </div>

    <div class="box">
      <form id="feedback_form">
        <div class="box-header clearfix">
          <h3 class="box-title pull-left">我的反馈</h3>
        </div>
        <div class="box-body" style="padding:0;">
          <div class="status_flag">
            <dl>
              <%--<dt>项目类型:</dt>--%>
              <dd class="clearfix" id="problem_type">
                <span data-value="" class="active">全部</span>
                <c:forEach items="${problemType}" var="t">
                  <span data-value="${t.value}">${t.name}</span>
                </c:forEach>
              </dd>
            </dl>
          </div>
          <%--公开标识--%>
          <input type="hidden" name="problemType">
          <table class="table pro_msg admin_index_table" style="table-layout: fixed;">
            <thead>
            <tr>
              <th width="5%">序号</th>
              <th>问题描述</th>
              <th width="10%">问题类型</th>
              <th width="20%">创建时间</th>
              <th width="20%">文件</th>
            </tr>
            </thead>
            <tbody id="feedback_table">
            <tr>
              <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
            </tr>
            </tbody>
          </table>
        </div>
      </form>

      <%--分页--%>
      <div class="page text-right" >
        <ul id="feedback_page" class="clearfix"></ul>
      </div>
    </div>

  </div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/message/message.js"></script>
</body>
<script>
    message.init(new page());
    feedback.init(new page());
    
    function refresh_cur_page() {
        message.refresh_cur_page();
    }
    //返回
    function back() {
        window.close();
    }
</script>


</html>

