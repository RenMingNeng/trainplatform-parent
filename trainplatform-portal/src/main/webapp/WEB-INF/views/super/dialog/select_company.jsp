<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    String resourcePath = request.getServletContext().getInitParameter("resource_path") + "/";
%>
<!DOCTYPE html>
<html class="select">
<head>
    <title>选择培训单位</title>
    <jsp:include page="${path}/common/style" />
    <style>
        .w_560{width:560px!important;}
        .w_400{width:400px!important;}
        .search{position: relative;}
        .search .tree_box{position: absolute;background:#fff;width:100%;top:36px;display: none;border:1px solid #ccc;padding:5px;z-index:100;height:300px;overflow: auto;}
    </style>
</head>
<body>
<!-- 选择单位 -->
<div class="select-left no_select">

    <div class="search">
        <span class="fa fa-search"></span>
        <input type="text" id="searchKey" class="empty" onkeyup="treeSearch.callNumber()" placeholder="按节点名称检索"/>
        <div id="showInfo">
            <a id="clickUp" class="up search_btn" onclick="treeSearch.clickUp()">
                <span class="fa  fa-angle-up"></span>
            </a>
            <label id="number" class="number"></label>
            <a id="clickDown" class="down search_btn" onclick="treeSearch.clickDown()">
                <span class="fa  fa-angle-down"></span>
            </a>
        </div>
    </div>

    <div>
        <ul id="company_type_tree" class="ztree">

        </ul>
    </div>
</div>
<div class="select-right">
    <!-- 选择课程 -->
    <div class="box w_560">
        <div class="box-body">
            <div class="clearfix top" >
                <form id="select_company_form">
                    <div class="pull-left">
                        <label>单位列表</label>
                    </div>
                    <div class="pull-right search-group">
                        <span>
                            <select name="" id="select_search">
                                <option value="1">单位名称</option>
                                <option value="2">行业类别</option>
                                <option value="3">单位类型</option>
                            </select>
                        </span>
                        <span class="search" id="searchType">
                            <input type="text" id="company_name" name="company_name" placeholder="请输入单位名称">
                            <input type="text" id="company_business" name="company_business" placeholder="请选择单位行业" readonly="readonly" style="display: none;">
                            <input type="text" id="company_category" name="company_category" placeholder="请选择单位类型" readonly="readonly" style="display: none;">
                            <input type="hidden" id="businessId" name="businessId">
                            <input type="hidden" id="categoryId" name="categoryId">
                            <button type="button" title="搜索" class="btn" id="submit_search">
                                <span class="fa fa-search" ></span> 搜索
                            </button>
                            <div class="tree_box">
                                <ul class="ztree" id="tree">
                                </ul>
                            </div>

                        </span>
                        <span>
                            <a href="javascript:;" class="btn all" id="company_all"><span class="fa fa-th"></span> 全部</a>
                        </span>
                    </div>
                    <input type="hidden" id="company_type" name="company_type" value="">
                </form>
            </div>
            <!-- 表格 -->
            <div class="table_container">
                <table class="table table_first">
                    <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th width="60"><input type="checkbox" name="checkAll" id="checkAll" onclick="selectCompany.checkAll()"></th>
                        <th>单位名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                    <table class="table">
                        <%--列表--%>
                        <tbody id="select_company_list">
                            <tr>
                                <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="page text-right" >
                <ul id="select_company_page"></ul>
            </div>
        </div>
    </div>

    <div class="box w_400" style="padding-left:5px;">
        <div class="box-body">
            <div class="clearfix top">
                <form>
                    <div class="pull-left">
                        <label>已选单位</label>
                    </div>
                    <div class="pull-right search-group">
                        <span>
                            <button class="btn btn-info" id="btn_sure"><span class="fa fa-check"></span> 确定</button>
                        </span>
                        <span>
                            <button class="btn btn-info" id="btn_cancel"><span class="fa fa-remove"></span> 取消</button>
                        </span>
                    </div>
                </form>
            </div>
            <!-- 表格 -->
            <div class="table_container">
                <table class="table table_first">
                    <thead>
                    <tr>
                        <th width="60">序号</th>
                        <th>单位名称</th>
                    </tr>
                    </thead>
                </table>
                <div class="table_inner">
                    <table class="table">
                        <%--列表--%>
                        <tbody id="select_company_list_copy">

                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="${path}/common/script" />
<input type="hidden" id="projectId" name="projectId" value="${projectId}">
<input type="hidden" id="type" name="type" value="${type}">
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/dialog/company_type.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/common/treeSearch.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/dialog/select_company.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/super/dialog/business_category_type.js"></script>
<script type="text/javascript">
    var page = new page();
    companyType.init();
    treeSearch.init("company_type_tree");
    selectCompany.init(page);
    $('.table_inner').niceScroll();
</script>
<script>
  $(document).ready(function(){
    $('#select_search').change(function(){
      var val = $(this).val();
      $('#searchType input[type="text"]').hide();
      if( val == 2 ){
        $('#company_name').val("");
        $('#company_category').val("");
        $('#categoryId').val("");
        // 加载单位行业树
        businessType.init();
        $('#company_business').show();
      }else if( val == 3 ){
        $('#company_name').val("");
        $('#company_business').val("");
        $('#businessId').val("");
        // 加载单位类型树
        categoryType.init();

        $('.search #company_category').show();
      }else{
        $('#company_business').val("");
        $('#businessId').val("");
        $('#company_category').val("");
        $('#categoryId').val("");
        $('#company_name').show();
      }
    })
    $('#company_business').focus(function(){
      $(".tree_box").show();
      $(".tree_box").fadeIn();
      $(".tree_box span").removeClass('switch');
    })
    $('#company_category').focus(function(){
      $(".tree_box").show();
      $(".tree_box").fadeIn();
      $(".tree_box span").removeClass('switch');
    })
    $('.tree_box').mouseleave(function(){
      $(".tree_box").hide();
    })

    //搜索
    $("#submit_search").click(function () {
      selectCompany.initTable();
    });
    $('#company_all').click(function () {
      $("#company_name").val("");
      $("#businessId").val("");
      $("#company_business").val("");
      $("#categoryId").val("");
      $("#company_category").val("");
      selectCompany.initTable();
    });
  });
</script>
</body>

</html>
