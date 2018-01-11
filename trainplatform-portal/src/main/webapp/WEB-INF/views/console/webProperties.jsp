<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.bossien.train.util.PropertiesUtils" language="java" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
  String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <base href="<%=basePath%>" />
  <title>博晟 | 培训平台-管理员-首页</title>
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
  <style>
    .ex_course_type_flush{
      display: inline-block;
      width: 80px;
      height: 32px;
      line-height: 32px;
      background: rgb(0, 173, 255);
      border-radius: 3px;
      color: white;
      cursor: pointer;
      -webkit-border-radius: 3px;
    }
    table td input{
      width: 300px;
    }
  </style>
</head>

<body>
<jsp:include page="${path}/console/menu" >
  <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>


<div id="main">
  <div class="area pd_10" style="padding:10px 0">
    <div class="box" style="padding:0;">
      <form id="p_version" >
        <input type="hidden" name="ver" value="ver" readonly="readonly" >
        <div class="box-header clearfix" style="padding:20px;">
          <h3 class="box-title pull-left">web.properties维护</h3>
        </div>
        <div class="pull-right search-group">
          <a href="javascript:history.go(-1);" class="btn btn-info"  id="btn_back"><span class="fa fa-reply"></span> 返回</a>
          <a href="javascript:;" class="btn btn-info"  id="save"><span class="fa fa-paper-plane"></span> 保存</a>
        </div>
        <div class="box-body" style="padding:0;">
          <table class="table pro_msg admin_index_table">
            <thead>
              <tr>
                <th width="20%">键</th>
                <th width="60%">值</th>
              </tr>
            </thead>
            <tbody>
            <tr>
              <td><input type="text" name="version" value="version" readonly="readonly" ></td>
              <td><input type="text" id="versionValue" name="versionValue" value="${version}" style="color: #177E09;width: 300px;"></td>
            </tr>
            <c:forEach items="${properties}" var="item">
            <tr>
              <c:if test="${item[0]!='version'}">
                <c:forEach items="${item}" var="pro">
                  <td><input type="text"  value="${pro}" readonly="readonly" ></td>
                </c:forEach>
              </c:if>
            </tr>
            </c:forEach>
            </tbody>
          </table>
        </div>
      </form>
    </div>
  </div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript">
  $(function () {
      $("#save").bind("click", function () {
          var url = appPath + "/console/updateWebProperties";
          var opt = {
              url: url,
              type: "post",
              dataType: "json",
              success: function (data) {
                var  result = data.result;
                  if ("10000" == result) {
                      //删除cookie中原本的
                    delCookie("ver");
                    setCookie("ver",$("#versionValue").val())
                   layer.msg("更新成功");
                   setInterval(function () {
                      window.location.href =  appPath + "/console";
                   },500)
                  } else  {
                      layer.msg(result);
                  }
              }
          };
          $("#p_version").ajaxSubmit(opt);

      });
  })
</script>
</body>

</html>

