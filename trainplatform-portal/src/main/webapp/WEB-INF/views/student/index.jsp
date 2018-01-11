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
  <title>博晟 | 培训平台-学员-首页</title>
  <jsp:include page="${path}/common/style" />
  <style>

    .w_700{width:600px;background: url("<%=resourcePath%>static/global/images/v2/student_index_2.png") no-repeat left center #fff;}
    .w_480{width:560px;background: url("<%=resourcePath%>static/global/images/v2/student_index_1.png") no-repeat left center #fff;}
    .w_700,.w_480{height:160px;padding-left:130px;}
    .w_480 .box-body{padding:50px 20px;}
    .w_480 .box-body ul{margin-top:5px;}
    .w_480 .box-body li{float: left;font-size:16px;text-align:center;width:33.3333%;color:#161616;line-height: 26px;}
    .w_480 .box-body li b{font-size:18px;}
    .w_480 .box-body li.middle{border-width:1px;border-style:solid;border-color :transparent #e8e8e8 transparent #e8e8e8; }
    .w_700 .box-body{padding:8px;}
    .table{display: table;border:none;}
    .row{display: table-row;}
    .font_14{font-size:14px;}
    .cell{display: table-cell;padding:5px 8px;color:#0c0c0c;}
    .th{font-weight:bold;background: #f5f5f5;color:#0c0c0c;}
    .mg_t_30 {margin-top:30px;}
    .mg_b_30{margin-bottom:30px;}
    .mg_b_10{margin-bottom:10px;}
  </style>
</head>

<body>
<jsp:include page="${path}/student/menu">
  <jsp:param name="menu" value="student"></jsp:param>
</jsp:include>
<div class="banner student_banner"></div>
<div id="main">
  <div class="area">
   <%-- <div id="addr" class="clearfix">
      <div class="pull-right">
        <div class="box-toolbar"><a href="javascript:window.location.reload();" title="刷新"><i class="fa fa-refresh"></i></a><a href="javascript:history.go(-1);" title="返回"><i class="fa fa-arrow-left"></i></a></div>
      </div>
    </div>--%>
    <div class="clearfix">
     <div class="box w_480 pull-left mg_t_30 mg_b_10">
       <div class="box-body">
         <%--<div>年度学时：0.23h</div>
         <div>累计学时：0.23h</div>
         <div>培训次数：3次</div>--%>
             <div class="ul">
                 <ul class="clearfix">
                     <li><b id="ndxs">0小时</b><br>年度学时</li>
                     <li class="middle"><b id="ljxs">0小时</b><br>累计学时</li>
                     <li><b id="train_count">0次</b><br>培训次数</li>
                 </ul>
             </div>
       </div>
     </div>
     <div class="box w_700 pull-right mg_t_30 mg_b_10">
       <div class="box-body  pd_r_10 pd_l_10 pb_b_10 font_14 l_h_30">
          <div class="table">
            <div class="row">
              <div class="cell th">单位名称</div>
              <div class="cell th text-center">排行情况</div>
            </div>

              <div class="row" id="pent_name">
              </div>
              <div class="row" id="prev_name">
              </div>
              <div class="row" id="company_name">
              </div>
          </div>
       </div>
     </div>
    </div>
    <div class="box">
      <div class="box-header clearfix">
        <h3 class="box-title pull-left">我的项目</h3>
        <form id = "pu_form">
        <div class="pull-right search-group">
          <span>
             <label>开始时间</label>
                <input class="Wdate" type="text" name="startTime" id="startTime" style="width: 186px"
                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,maxDate:'#F{$dp.$D(\'endTime\')}'})">
          </span>
          <span>
            <label>结束时间</label>
                <input class="Wdate"  type="text" name="endTime" id="endTime" style="width: 186px"
                       onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',readOnly:true,minDate:'#F{$dp.$D(\'startTime\')}'})">
          </span>
          <span class="search">
                <input type="hidden" id="projectStatus" name="projectStatus" value="">
                <input type="text" id="projectName" name="projectName" placeholder="请输入项目名称">
                <button type="button" id="search" title="搜索" class="btn">
                  <span class="fa fa-search"></span> 搜索
                </button>
          </span>
          <span>
              <a href="javascript:;" id="search_all" class="btn all"><span class="fa fa-th"></span> 全部</a>
          </span>
        </div>
        </form>
      </div>
      <div class="box-body">
          <div class="status_flag">
            <dl>
              <%--<dt>状态:</dt>--%>
              <dd class="clearfix">
                <span class="active status" project_status="">全部</span>
                <span class="status" project_status="2">未开始</span>
                <span class="status" project_status="3">进行中</span>
                <span class="status" project_status="4">已结束</span>
              </dd>
            </dl>
          </div>
        <table class="table pro_msg index_table" id="tab">
          <thead>
          <tr>
            <th width="70">序号</th>
            <th>项目名称</th>
            <th width="140">项目类别</th>
            <th width="160">项目时间</th>
            <th width="90">状态</th>
            <th width="120">创建人员</th>
            <th width="120">创建日期</th>
            <th width="270">操作</th>
          </tr>
          </thead>
          <tbody id="pu_table">

          </tbody>
        </table>



      </div>
        <div class="page text-right" >
            <ul id="pu_page"></ul>
        </div>
    </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/student/student_index.js"></script>
<script type="text/javascript">
    var page = new page();
    studentIndex.init(page);

</script>
</body>
</html>

