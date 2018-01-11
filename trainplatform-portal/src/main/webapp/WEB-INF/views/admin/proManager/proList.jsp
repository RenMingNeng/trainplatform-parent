<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
  String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>

<html lang="zh-CN">
<head>
  <base href="<%=basePath%>" />
  <meta charset="utf-8"/>
  <title>博晟 | 培训平台-管理员</title>
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
  <style>
    .search-group select {
      font-family: '微软雅黑';
    }
  </style>
</head>
<body>
<jsp:include page="${path}/admin/menu" >
  <jsp:param name="menu" value="proManager"></jsp:param>
</jsp:include>




<div id="main">
  <div class="area" style="overflow: hidden;">
    <%--<div id="addr">
      <div id="menu_li">
        <ul>
          <li>项目管理</li>
        </ul>
      </div>
      <a href="<%=resourcePath%>admin/proManager/proManagerList">项目管理</a>
    </div>--%>
    <div class="box">
    <div class="box-header clearfix">
      <h3 class="box-title pull-left">项目管理</h3>
      <form class="clearfix" id="projectId" action="<%=path%>admin/proManager/queryProList">
      <div class="pull-right search-group">
        <span>
          <label>开始时间</label>
              <input type="text" id="projectStartTime" name="projectStartTime" onclick="WdatePicker()" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'projectEndTime\')}',readOnly:true})">
        </span>
        <span>
          <label>结束时间</label>
              <input type="text" id="projectEndTime" name="projectEndTime" onclick="WdatePicker()" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'projectStartTime\')}',readOnly:true})">
        </span>

        <span>
          <select name="" id="select_search" style="color: #000;">
              <option value="1">项目名称</option>
              <option value="2">项目状态</option>
              <%--<option value="3">项目类型</option>--%>
          </select>
        </span>

        <span class="search">
            <input type="text" id="projectName" name="projectName" placeholder="请输入项目名称">
           <input type="text" id="projectStatus" name="projectStatus" placeholder="请输入项目状态"  style="display: none;">
           <input type="text" id="projectTypeName" name="projectTypeName" placeholder="请输入项目类型" style="display: none;">
             <input type="hidden" id="projectType" name="projectType" >
          <button type="button" title="搜索" onclick="search()" class="btn">
              <span class="fa fa-search"></span> 搜索
            </button>
        </span>
        <span>
          <a href="javascript:searchAll()" class="all btn"><span class="fa fa-th"></span> 全部</a>
        </span>
      </div>
      </form>
    </div>
    <div class="box-body">

        <%--<div>
          <div class="clearfix search-group">
            <div class="pull-left">
              <label>开始时间</label>
              <input type="text" id="projectStartTime" name="projectStartTime" onclick="WdatePicker()" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'projectEndTime\')}',readOnly:true})">
              &nbsp;&nbsp;&nbsp;&nbsp;
              <label>结束时间</label>
              <input type="text" id="projectEndTime" name="projectEndTime" onclick="WdatePicker()" class="Wdate"
                     onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'projectStartTime\')}',readOnly:true})">
            </div>
            <div class="pull-right">
              <label>项目名称：</label>
              <div class="search">
                <input type="text" id="projectName" name="projectName" placeholder="请输入项目名称">
                <button type="button" title="搜索" onclick="search()">
                  <span class="fa fa-search"></span>
                </button>
              </div>
              <a href="javascript:searchAll()">全部</a>
            </div>
          </div>
          <%--<div class="status_flag">
            <dl>
              <dt>状态:</dt>
              <dd class="clearfix">
                <span class="active">全部</span>
                <span>未开始</span>
                <span>进行中</span>
                <span>已结束</span>
              </dd>
            </dl>
          </div>
        </div>--%>
        <div class="status_flag">
            <dl>
                <%--<dt>项目类型:</dt>--%>
                <dd class="clearfix" id="click_project_type">
                    <span class="active">全部</span>
                    <c:forEach items="${project_type}" var="t">
                        <span data-value="${t.value}">${t.name}</span>
                    </c:forEach>
                </dd>
            </dl>
        </div>
      <table class="table pro_msg index_table">
        <thead>
        <tr>
          <th width="60">序号</th>
          <th>项目名称</th>
          <th width="150">项目时间</th>
          <th width="60">人数</th>
          <th width="140">类型</th>
         <%-- <th width="50">来源</th>--%>
          <th width="65">状态</th>
          <th width="165">创建人员</th>
          <th width="100">创建日期</th>
          <th width="220">操作</th>
        </tr>
        </thead>
        <tbody id="pro_tbody"></tbody>
      </table>
      <%--
        text-left、text-center、text-right
        分别让分页版块左、中、右
      --%>

    </div>
      <div class="page text-right" >
        <ul id="pro_page"></ul>
      </div>
  </div>

  </div>
</div>

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script src="<%=resourcePath%>static/global/js/common/date.js"></script>
<script type="text/javascript">
  $(function () {
      $('#select_search').change(function() {
          var val = $(this).val();
          if (val == 2) {
              $('#projectName').val("");
              $('#projectTypeName').val("");
              $('.search input[type="text"]').hide();
              $('.search #projectStatus').show();
          } else if (val == 3) {
              $('#projectName').val("");
              $('#projectStatus').val("");
              $('.search input[type="text"]').hide();
              $('.search #projectTypeName').show();
          } else {
              $('#projectStatus').val("");
              $('#projectTypeName').val("");
              $('.search input[type="text"]').hide();
              $('.search #projectName').show();
          }
      })

      $('#click_project_type').on("click", "span", function(){
          $('#click_project_type').find("span").removeClass("active");
          $(this).addClass("active");
          bind_click(this);
      });
  })

    function formatPrjStatus(value) {
        switch ( value ){
            case '1':{
                return '<span class="text-red">未发布</span>';
            }
            case '2':{
                return '<span class="text-gray">未开始</span>';
            }
            case '3':{
                return '<span class="text-green">进行中</span>';
            }
            case '4':{
                return '<span class="text-gray">已结束</span>';
            }
        }
    }

    //新建对象
    var page = new page();

    initListTable(page);
    function initListTable(page){
        var basePath = "<%=basePath%>";
        var list_url = "<%=basePath%>admin/project/queryProList";

        /*
         * 分页
         * @param param_form 参数form id
         * @param list_url 请求链接
         * @param tbody_id 列表div id
         * @param page_id 分页id
         * @param pageNum 页数
         * @param pageSize 每页条数
         */
        page.init("projectId", list_url, "pro_tbody", "pro_page", 1, 10);

        //初始化
        page.goPage(1);

        //重写组装方法
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            for(var i=0; i< len; i++) {
                item = dataList[i];
                // 组装数据
                inner += "<tr>";
                inner += "<td>"+(parseInt(i)+1)+"</td>";
                inner += "<td><span class=\"text-orange tooltip\" data-length='30'>"+item.project_name+"</span></td>";

                var projectType = item['project_type'];
                /*if(projectType == '3'){
                    inner += '<td>'+ TimeUtil.longMsTimeToDateTime3(item['projectStartTime']) + '<br/>' + TimeUtil.longMsTimeToDateTime3(item['projectEndTime']) + '</td>';
                }else{
                    inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['projectStartTime']) + '<br/>' + TimeUtil.longMsTimeConvertToDateTime(item['projectEndTime']) + '</td>';
                }*/
               inner += "<td>"+(TimeUtil.longMsTimeConvertToDateTime(item['project_start_time'])+"<br>"+TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']))+"</td>";

                inner += "<td>"+item.person_count+"</td>";
                inner += "<td>"+Enum.projectType(item.project_type)+"</td>";
               /* inner += "<td>"+Enum.projectModel(item.projectMode)+"</td>";*/
                inner += "<td><span class=\"text-blue\">" +Enum.projectStatus(item.project_status)+ "</span></td>";
                inner += "<td>"+item.create_user+"</td>";
                inner += "<td>"+TimeUtil.longMsTimeConvertToDateTime(item.create_time)+"</td>";
                inner += '<td><a href="javascript:info(\''+item.id+'\');" class="a a-view">详情</a> ';
                //已结束项目不允许修改
            if("4"!=item.project_status && "0"==item.project_mode){
                inner +=  '<a href="javascript:;" onclick="modify(\'' + item.id + '\',\''+ item.project_type + '\',\''+ item.project_status + '\');" class="a a-info">修改</a>';
            }else{
                inner +=  '<a href="javascript:;"  class="a">修改</a>';
            }
                // 【进行中】【已结束】项目不可删除,【未发布】【未开始】项目可删除
            if("1"==item.project_status || "2"==item.project_status && "0"==item.project_mode){
                inner += '<a href="javascript:del(\''+item.id+'\');" class="a a-close">删除</a>';
            }else{
                inner += '<a href="javascript:;" class="a">删除</a>';
            }
                inner += "</td>";
                inner += "</tr>";
            }
          return inner;
        }


    }

    //模糊查询
    function search(){
        var startTime = $("#projectStartTime").val();
        var endtime = $("#projectEndTime").val();
        if (startTime != "" && endtime != "") {
            if (startTime > endtime) {
                alert("项目开始时间必须小于项目结束时间！");
                return;
            }
        }
        var projectTypeName = $('#projectTypeName').val();
        if("" != projectTypeName){
           $("#projectType").val(getProjectTypeCode(projectTypeName));
        }
      $("#projectName").val($.trim($("#projectName").val()));
        initListTable(page);
    }

    //查询全部
    function searchAll(){
        $('#click_project_type').find("span").removeClass("active");
        $('#click_project_type span:eq(0)').addClass("active");
        $("#projectStartTime").val("");
        $("#projectEndTime").val("");
        $("#projectName").val("");
        $('#projectStatus').val("");
        $('#projectTypeName').val("");
        $("#projectType").val("")
        initListTable(page);
    }
    //查看详情
    function info(id){
       location.href="<%=basePath%>admin/project/proManagerInfo?projectId="+id+"&type=proManager";
    }
    //修改项目
  function modify(projectId,projectType,projectStatus) {
      location.href="<%=basePath%>admin/project_create/private/basicInfoModify?projectId="+projectId+"&projectTypeNo="+projectType+"&projectStatus="+projectStatus;
  }
  //删除项目
  function del(projectId) {
      layer.confirm("确定删除吗?", {
          icon : 3,
          btn : [ "确认", "取消" ]
      }, function() {
          $.ajax({
              url: appPath + '/admin/project/proManagerDelete',
              dataType: 'json',
              async: false,
              type: 'post',
              data: {'projectId': projectId},
              success: function (data) {
               window.location.reload(true);
              }
          });
      });
  }

  bind_click = function(t){
      var value = $(t).attr("data-value");
      $("#projectType").val(value);
      initListTable(page);
  };

  function getProjectTypeCode(projectTypeName) {
        var projectType = "-1";
     if("培训" == projectTypeName ) {
         projectType = "1";
     }else if("练习" == projectTypeName ) {
         projectType = "2";
     }else if("考试" == projectTypeName ) {
         projectType = "3";
     }else if("培训练习" == projectTypeName ) {
         projectType = "4";
     }else if("培训考试" == projectTypeName ) {
         projectType = "5";
     }else if("练习考试" == projectTypeName ) {
         projectType = "6";
     }else if("培训练习考试" == projectTypeName ) {
         projectType = "7";
     }
     return projectType;
  }
</script>
</body>
</html>


