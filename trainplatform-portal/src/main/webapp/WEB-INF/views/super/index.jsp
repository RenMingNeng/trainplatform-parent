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

  <title>博晟 | 培训平台-管理员-首页</title>

  <!-- BEGIN GLOBAL STYLES -->
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">

  <!-- END GLOBAL STYLES -->
</head>

<body class="home">
<jsp:include page="${path}/super/menu" >
  <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>
<div class="banner admin_banner"></div>
<div id="main">

  <div class="area">
    <div class="clearfix statistics">
      <div class="box box_1">
        <h3><span>热门主题</span></h3>
        <h6 class="text-center">十大热门主题，您创建的培训主题在这里 <span class="text-red">（请选择一个培训主题，非必选）</span></h6>
        <div class="list_container">
      </div>
    </div>

    <div class="box">
      <form id="public_project_form">
      <div class="box-header clearfix pd-20" >
        <h3 class="box-title pull-left">公开项目</h3>

        <div class="pull-right search-group">
          <span>
              <label>项目开始时间</label>
              <input type="text" id="datBeginTime" name="datBeginTime" class="Wdate" style="width: 186px"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'datBeginTime\')}'})">
          </span>
          <span>
              <label>项目结束时间</label>
              <input type="text" id="datEndTime" name="datEndTime" class="Wdate" style="width: 186px"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'datEndTime\')}'})">
          </span>
          <span class="search">
                <input type="text" id="project_name" name="project_name" placeholder="请输入项目名称">
                <button type="button" class="btn" id="btn_search" title="搜索">
                  <span class="fa fa-search"></span> 搜索
                </button>
          </span>
          <span>
            <a href="javascript:;" id="btn_all"  class="btn all" ><span class="fa fa-th"></span> 全部</a>
          </span>
        </div>
      </div>
      <div class="box-body">
        <!-- 表格 -->
          <div class="status_flag">
            <dl>
              <dd class="clearfix">
                <span class="active status" finish_status="" id="btn_a">全部</span>
                <span class="status" finish_status="1">未发布</span>
                <span class="status" finish_status="2">未开始</span>
                <span class="status" finish_status="3">进行中</span>
                <span class="status" finish_status="4">已结束</span>
              </dd>
            </dl>
          </div>
          <input type="hidden" id="project_status" name="project_status">
        <table class="table pro_msg admin_index_table">
          <thead>
          <tr>
            <th width="46">序号</th>
            <th>项目名称</th>
            <th width="160">项目类别</th>
            <th width="130">项目时间</th>
            <th width="80">人数</th>
            <th width="80">状态</th>
            <th width="100">创建人员</th>
            <th width="130">创建日期</th>
            <th width="300">操作</th>
          </tr>
          </thead>
          <%--项目列表--%>
          <tbody id="public_project_list">
          <tr>
            <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
          </tr>
          </tbody>
        </table>

      </div>
      </form>
      <div class="page text-right" >
        <ul id="public_project_page"></ul>
      </div>
    </div>

  </div>
</div>
</div>
<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/superAdmin_index.js"></script>
<script type="text/javascript">

      var page = new page();
      superAdmin_index.init(page);

      //项目类别选择
      $('.type-class>div>div').on('click',function(){
          $(this).toggleClass('active');
          var flag=false;
          $('.type-class .col-4 div').each(function(index,element){
              if($(element).hasClass('active')){
                  flag=true;
                  return;
              }
          });
          if(flag){
              $("#btn_project_create").addClass('active').css("background-color","");
          }else{
              $("#btn_project_create").removeClass('active').css("background-color","#ccc");
          }
      })

      //项目主题选择
      $('#hot_subject_list').delegate(' .subject','click',function () {
          if($(this).hasClass('active')){
              $(this).removeClass('active');
          }else{
              $('.type-class_sub>div>div').removeClass('active');
              $(this).addClass('active');
          }

      });
      /*
       * 创建项目
       * */
      //显示\隐藏 创建框面板
      var li_timer = null;
      $('.list_container').delegate('li','mouseover',function(){
        var $this = $(this);
        $this.addClass('hover');

      })
      $('.list_container').delegate('li','mouseleave',function(){
        var $this = $(this);
        $this.removeClass('hover');
        $this.find('.option').removeClass('active');
      })
      //选择创建类型
      $('.options').delegate('.option','click',function(){
        $(this).toggleClass('active');
        return false;
      })
</script>
</body>

</html>

