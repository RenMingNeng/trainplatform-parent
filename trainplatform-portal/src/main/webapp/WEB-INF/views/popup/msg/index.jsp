<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
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
  <title>博晟 | 培训平台-管理员-通知公告</title>
  <jsp:include page="${path}/common/style" />
</head>

<body>
<shiro:hasRole name="company_admin">
  <jsp:include page="${path}/admin/menu" >
    <jsp:param name="menu" value="msg"></jsp:param>
  </jsp:include>
</shiro:hasRole>
<shiro:hasRole name="company_user">
  <jsp:include page="${path}/student/menu" >
    <jsp:param name="menu" value="msg"></jsp:param>
  </jsp:include>
</shiro:hasRole>

<div id="main" class="msg_main">

  <div class="area">
    <div class="box">
        <form id="msg_form">
          <div class="box-body" style="padding:0">

              <div class="box-header clearfix">
                <h3 class="box-title pull-left">通知公告</h3>
                <form>

                  <div class="pull-right search-group">
                        <span>
                            <shiro:hasRole name="company_admin">
                                <a href="<%=path%>/popup/msg/add" id="btn_add" class="btn btn-info"><span class="fa fa-plus"></span> 新增</a>
                            </shiro:hasRole>
                        </span>
                        <span class="search">
                            <input type="text" id="msgTitle" placeholder="请输入公告标题" name="msgTitle" >
                            <button type="button" id="search" class="btn" title="搜索" style="background: #67717f;border:1px solid #67717f;">
                                <span class="fa fa-search"></span> 搜索
                            </button>
                        </span>
                        <span>
                            <a href="javascript:;" class="btn all" id="searchAll" ><span class="fa fa-th"></span>  全部</a>
                        </span>
                  </div>
                </form>
              </div>

            <%--公开标识--%>
            <div style="padding:0 20px;">
              <table class="table pro_msg admin_index_table" style="table-layout: fixed;">
                <%--<thead>
                <tr>
                  <th width="160">公告标题</th>
                  <th width="140">发送时间</th>
                </tr>
                </thead>--%>
                <tbody id="msg_table">
                    <tr>
                      <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                    </tr>
                </tbody>
              </table>
            </div>
          </div>
        </form>

        <%--分页--%>
        <div class="page text-right" >
          <ul id="msg_page" class="clearfix"></ul>
        </div>
    </div>

  </div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/popup/msg.js"></script>
</body>
<script>
    var page = new page();
    msg.init(page);
</script>


</html>

