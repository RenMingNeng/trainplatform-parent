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
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="renderer" content="webkit|ie-comp|ie-stand">

  <title>博晟 | 培训平台-管理员-首页</title>

  <!-- BEGIN GLOBAL STYLES -->
  <jsp:include page="${path}/common/style" />
  <link href="<%=resourcePath%>static/global/css/app.css" rel="stylesheet" type="text/css">
  <style type="text/css">
    .no{
      display: none;
    }
  </style>
  <!-- END GLOBAL STYLES -->
</head>

<body class="home">
<jsp:include page="${path}/admin/menu" >
  <jsp:param name="menu" value="index"></jsp:param>
</jsp:include>
<div class="banner admin_banner"></div>

<div id="main">

  <div class="area">
    <div class="box box_1">
      <h4>
        <div>热门主题</div>
        <img src="<%=resourcePath%>static/global/images/v2/title_1.png" alt="">
      </h4>
      <div class="title_info text-center">
        <img src="<%=resourcePath%>static/global/images/v2/title_info_icon.png" alt=""> 请点击下方主题<b>创建项目</b>
      </div>

      <div class="list_container no_select">

      </div>

    </div>
    <div class="box box_2">
      <h4>
        <div>信息统计</div>
        <img src="<%=resourcePath%>static/global/images/v2/title_2.png" alt=""></h4>
      <div class="clearfix">
        <div class="pull-left w_490">
          <ul class="clearfix">
            <li class="active" data-content-id="1">人员信息</li>
            <li data-content-id="2">学时信息</li>
            <li data-content-id="3">培训信息</li>
          </ul>
          <div style="display: block" id="container_1"></div>
          <div  id="container_2">学时信息</div>
          <div  id="container_3">培训信息</div>
        </div>
        <div class="pull-right w_510">
          <div class="title">
            基本信息
          </div>
          <div class="r_1 clearfix">
            <div class="r_1_1">
              <span class="num"></span>
            </div>
            <div class="r_1_2">
              <span class="num"></span>
            </div>
            <div class="r_1_3">
              <span class="num"></span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div id = "public_box" class="box" style="padding:0;">
      <form id="public_project_form">
        <div class="box-header clearfix" style="padding:20px;">
          <h3 class="box-title pull-left">
            <img src="<%=resourcePath%>static/global/images/v3/public_icon.png" alt="">
          </h3>
          <div class="pull-right search-group">
          <span>
            <label>开始时间</label>
            <input type="text" id="public_datBeginTime" name="datBeginTime"
                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'datEndTime\')}',readOnly:true})" >
          </span>
            <span>
            <label>结束时间</label>
            <input type="text" id="public_datEndTime" name="datEndTime"
                   class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'datBeginTime\')}',readOnly:true})">
          </span>
            <span class="search clearfix">
            <input id="public_project_name" name="project_name" type="text" placeholder="请输入关键字">
            <button type="button" id="public_search" title="搜索" class="btn"> <span class="fa fa-search"></span> 搜 索</button>
          </span>
            <span>
              <a href="javascript:;" id="public_search_all" class="btn all"> <span class="fa fa-th"></span> 全 部</a>
          </span>
          </div>
        </div>
        <div class="box-body" style="padding:0;">
          <div class="status_flag" id="public">
            <dl>
              <dd class="clearfix">
                <span public_project_status="" class="active status public" id="public_clickAll">全部</span>
                <span public_project_status="2" class="status public">未开始</span>
                <span public_project_status="3" class="status public">进行中</span>
                <span public_project_status="4" class="status public">已结束</span>
              </dd>
            </dl>
          </div>
          <%--隐藏域--%>
          <input type="hidden" id="public_project_status" name="project_status">
          <input type="hidden" id="public_company_id" name="company_id" value="${company_id}">
          <%--公开标识--%>
          <input type="hidden" id="public_project_mode" name="project_mode" value="1">
          <table class="table pro_msg admin_index_table">
            <thead>
            <tr>
              <th width="46">序号</th>
              <th class="text-left">项目名称</th>
              <th width="160">项目类别</th>
              <th width="140">项目时间</th>
              <th width="80">人数</th>
              <th width="80">状态</th>
              <th width="100">创建人员</th>
              <th width="100">创建日期</th>
              <th width="240">操作</th>
            </tr>
            </thead>
            <tbody id="public_project_list" class="no">
            <tr>
              <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
            </tr>
            </tbody>
          </table>
        </div>
      </form>

      <%--分页--%>
      <div class="page text-right" >
        <ul id="public_project_page" class="clearfix"></ul>
      </div>
    </div>

    <div class="box" style="padding:0;">
      <form id="private_project_form">
      <div class="box-header clearfix" style="padding:20px; ">
        <h3 class="box-title pull-left">
          <img src="<%=resourcePath%>static/global/images/v3/company_icon.png" alt="">
        </h3>
        <div class="pull-right search-group">
          <span>
            <label>开始时间</label>
            <input type="text" id="datBeginTime" name="datBeginTime"
              class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'datEndTime\')}',readOnly:true})" >
          </span>
          <span>
            <label>结束时间</label>
            <input type="text" id="datEndTime" name="datEndTime"
              class="Wdate"  onfocus="WdatePicker({dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'datBeginTime\')}',readOnly:true})">
          </span>
          <span class="search clearfix">
            <input id="project_name" name="project_name" type="text" placeholder="请输入关键字">
            <button type="button" id="search" title="搜索" class="btn"> <span class="fa fa-search"></span> 搜 索</button>
          </span>
          <span>
              <a href="javascript:;" id="search_all" class="btn all"> <span class="fa fa-th"></span> 全 部</a>
          </span>
        </div>
      </div>
      <div class="box-body" style="padding:0;">
          <div class="status_flag" id="private">
            <dl>
              <dd class="clearfix">
                <span project_status="" class="active status" id="private_clickAll">全部</span>
                <span project_status="1" class="status">未发布</span>
                <span project_status="2" class="status">未开始</span>
                <span project_status="3" class="status">进行中</span>
                <span project_status="4" class="status">已结束</span>
              </dd>
            </dl>
          </div>
          <%--隐藏域--%>
          <input type="hidden" id="project_status" name="project_status">
          <input type="hidden" id="company_id" name="company_id" value="${company_id}">
          <%--公开标识--%>
          <input type="hidden" id="project_mode" name="project_mode" value="0">
        <table class="table pro_msg admin_index_table">
          <thead>
          <tr>
            <th width="46">序号</th>
            <th class="text-left" width="300">项目名称</th>
            <th width="140">项目类别</th>
            <th width="160">项目时间</th>
            <th width="60">人数</th>
            <th width="80">状态</th>
            <th >创建人员</th>
            <th width="100">创建日期</th>
            <th width="170">操作</th>
          </tr>
          </thead>
          <tbody id="private_project_list">
            <tr>
              <td colspan="10" class="table_load"><span>正在加载数据，请稍等。。。</span></td>
            </tr>
          </tbody>
        </table>
      </div>
      </form>

      <%--分页--%>
      <div class="page text-right" >
        <ul id="private_project_page" class="clearfix"></ul>
      </div>
    </div>
  </div>
</div>
<!-- hidden input -->
<input id="user_id" type="hidden" value="${user_id}">

<jsp:include page="${path}/common/footer" />
<jsp:include page="${path}/common/script" />
<script type="text/javascript" src="<%=resourcePath%>static/global/js/admin/admin_index.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/highcharts.js"></script>
<script type="text/javascript" src="<%=resourcePath%>static/global/js/page1.js"></script>
<script>
    //首页统计信息
    $.ajax({
        url: appPath + "/admin/tongJi",
        type: 'post',
        success: function (companyTj) {
            //人员信息统计图
            var unTrainCOunt = companyTj.countUser - companyTj.countTrainUser;  //未培训人数
            var countTrainUser = parseInt(companyTj.countTrainUser);
            var data1 = [{
                type: 'pie',
                innerSize : '70%',
                name : '人员信息',
                data : [
                    {name : '未培训人数',y: unTrainCOunt},
                    {name : '已培训人数',y: countTrainUser},
                ]
            }];

            //学时统计信息图
            var data2 = {
                'totalClassHour':companyTj.totalClassHour*100/100 ,
                'averagePersonClassHour':companyTj.averagePersonClassHour*100/100,
                'totalYearClassHour':companyTj.totalYearClassHour*100/100,
                'averageYearClassHour':companyTj.averageYearClassHour*100/100,
                'countUser': companyTj.countUser
            }



            //培训信息统计图
            var data3 = {
                'countTrain': parseInt(companyTj.countTrain) ,
                'countTrainCompleteYes': parseInt(companyTj.countTrainCompleteYes),
                'countExam': parseInt(companyTj.countExam),
                'countExamPassYes': parseInt(companyTj.countExamPassYes)
            }

            //课程数量，题库数量，项目数量
            $(".r_1_1 .num").text(companyTj.countCourse);
            $(".r_1_2 .num").text(companyTj.countQuestion);
            $(".r_1_3 .num").text(companyTj.countProject);


          /*
           * 统计信息切换
           * */
            //左边切换
            $('.w_490').delegate('li','click',function(){
                var $this = $(this);
                var index = $this.index();
                $this.addClass('active').siblings('li').removeClass('active');
                $('.w_490>div').eq(index).show().siblings('div').hide();
                var dataContentId = $(this).attr("data-content-id");
                if("1" == dataContentId){
                    drawImage('container_1',data1);
                }else if("2" == dataContentId){
                    classHourStatistic(data2);
                }else if("3" == dataContentId){
                    trainStatistic(data3);
                }
            })
            $('.w_490 li:eq(0)').trigger('click');
        }

    })




    //人员信息统计图
    function drawImage(container,data){
        Highcharts.chart(container, {
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            credits:{
                enabled:false
            },
            title: {
                floating : true,
                text :'<span style="color:#acadc0;font-size:16px;">学员数量</span><br><span style="font-size:28px;font-weight:bold;color:#29b0fd;">' + (data[0].data[0].y + data[0].data[1].y) + '</span>'
            },
            tooltip:{
                enabled : true,
                pointFormat: '{point.percentage:.1f} %'
            },
            colors:['#4df0f0','#2db7fb','#ff3e75','#ffac32','#f3e24f'],
            plotOptions: {
                pie: {
                    allowPointSelect: false, //选中某块区域是否允许分离
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true, //是否直接呈现数据 也就是外围显示数据与否
                        format : '{point.name}:{point.percentage:.1f} %'
                    },
                    point:{
                      events : {
                          mouseOver : function(e){
                              chart.setTitle({
                                  text : '<span style="color:#acadc0;font-size:16px;">' + e.target.name + '</span><br><span style="font-size:28px;font-weight:bold;color:#29b0fd;">' + e.target.y + '</span>'
                              })
                          },
                          mouseOut : function(e){
                              chart.setTitle({
                                  text : '<span style="color:#acadc0;font-size:16px;">学员数量</span><br><span style="font-size:28px;font-weight:bold;color:#29b0fd;">' +  (data[0].data[0].y + data[0].data[1].y)  + '</span>'
                              })
                          }
                      }
                    },
                    showInLegend: true
                }
            },
            series: data
        },function(c){
            var centerY = c.series[0].center[1],
                titleHeight = parseInt( c.title.styles.fontSize );
            c.setTitle({
                y : centerY + titleHeight / 2 - 15
            });
            chart = c;
        });
    }
    data1 = [{
        type: 'pie',
        innerSize : '70%',
        name : '人员信息',
        data : [
            {name : '未培训人数',y: 100},
            {name : '已培训人数',y: 1000},
        ]
    }];


  //学时信息统计图
  function classHourStatistic(data2) {
      var totalClassHour = data2.totalClassHour;                   // 累计学时
      var averagePersonClassHour = data2.averagePersonClassHour;   //人均学时
      var totalYearClassHour = data2.totalYearClassHour;           //年度累计学时
      var averageYearClassHour = data2.averageYearClassHour;       //年度人均学时
      var countUser = data2.countUser;
      //var categories = ["0"];
      var data1 = [[0,parseFloat(averageYearClassHour)]];
      var data3 = [[0,parseFloat(averagePersonClassHour)]];
      if(countUser != 0 ){
          var data_1 =[parseInt(countUser),parseFloat(averageYearClassHour)] ;      //年度人均学时
          //var data_1 =[parseInt(countUser),20.02] ;
          data1.push(data_1);
          var data_3 = [parseInt(countUser),parseFloat(averagePersonClassHour)]     //人均学时

          //var data_3 = [parseInt(countUser),18]
          data3.push(data_3);
      }
      Highcharts.chart('container_2', {

          chart: {
              type: 'area'
          },
          credits:{
              enabled:false
          },
          title: {
              text: ''
          },
          subtitle: {
              text: ''
          },
          xAxis: {
              title: {
                  text: '人数/人'
              },
          },
          yAxis: {
              title: {
                  text: '人均学时/小时'
              },
              labels: {
                  formatter: function () {
                      return this.value;
                  }
              }
          },
          tooltip: {
              //headerFormat: '<span style="font-size:11px">人数: '+ countUser +'</span><br>',
              pointFormat:  '人均学时: <b>{point.y}</b> '
          },
          plotOptions: {
              area: {
                  pointStart: 0,
                  /*dataLabels: {
                      enabled: true, //是否直接呈现数据 也就是外围显示数据与否
                      format : '{series.name}:{point.y}'
                  },*/
                  marker: {
                      enabled: true,
                      symbol: 'circle',
                      radius: 1,
                      states: {
                          hover: {
                              enabled: true
                          }
                      }
                  }
              }
          },
          series: [{
              name: '年度累计学时 '+totalYearClassHour+' 小时',
              data: data1
          }, {
              name: '累计学时 '+totalClassHour+' 小时',
              data: data3
          }]


      });



  }

  //培训信息统计图
  function trainStatistic(data3) {
      var countTrain = data3.countTrain;                              //培训人次
      var countTrainCompleteYes = data3.countTrainCompleteYes;        //完成培训人次
      var countExam = data3.countExam;                                //考试人次
      var countExamPassYes = data3.countExamPassYes;                  //完成考试人次
      Highcharts.chart('container_3', {
          chart: {
              type: 'column'
          },
          credits:{
              enabled:false
          },
          title: {
              text: ''
          },
          subtitle: {
              text: ''
          },
          colors:['#4df0f0','#2db7fb','#ff3e75','#ffac32'],
          xAxis: {
              type: 'category'
          },
          yAxis: {
              title: {
                  text: '单位/人次'
              }

          },
          legend: {
              enabled: false
          },
          plotOptions: {
              series: {
                  borderWidth: 0,
                  dataLabels: {
                      enabled: true,
                      format: '{point.y}'
                  }
              }
          },

          tooltip: {
              headerFormat: '<span style="font-size:11px"></span><br>',
              pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b> <br/>'
          },

          series: [{
              name: 'Brands',
              colorByPoint: true,
              data: [{
                  name: '培训人次',
                  y: countTrain,
                  drilldown: '培训人次'
              }, {
                  name: '完成培训人次',
                  y: countTrainCompleteYes,
                  drilldown: '完成培训人次'
              }, {
                  name: '考试人次',
                  y: countExam,
                  drilldown: '考试人次'
              }, {
                  name: '考试合格人次',
                  y: countExamPassYes,
                  drilldown: '考试合格人次'
              }]
          }]
      });

  }
</script>

<script type="text/javascript">
      var page = new page();
      var page1 = new page1();
      admin_index.init(page,page1);
      //项目类别选择
      $('.type-class .type-item').on('click',function(){
          $(this).toggleClass('active');
          var flag=false;
          $('.type-class .type-item').each(function(index,element){
              if($(this).hasClass('active')){
                  flag=true;
                  return;
              }
          });
          if(flag){
              $("#btn_project_create").addClass('active');
          }else{
              $("#btn_project_create").removeClass('active');
          }
      })
      //


      //项目主题选择
      $('#hot_subject_list ').delegate('.subject','click',function () {
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
      $('.list_container').delegate('li','mouseover',function(){
          var $this = $(this);
          if( !$this.hasClass('no_project') ){
              $this.addClass('hover');
              console.log( 1 );
          }
      })
      $('.list_container').delegate('li','mouseleave',function(){
          var $this = $(this);
          $this.removeClass('hover');
          $this.find('.option').removeClass('active');
      })
      //选择创建类型
      /*$('.options').delegate('.option','click',function(){
          alert();
          $(this).toggleClass('active');
          var $parent = $(this).parent('.options');
          var $btn = $parent.next();
          var len = $parent.find('.option.active').length;
          console.log( len );
          len > 0 ? $btn.addClass('selected') : $btn.removeClass('selected');

          return false;
      })*/

</script>
</body>

</html>

