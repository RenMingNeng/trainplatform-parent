/**
 * Created by Administrator on 2017-07-25.
 */
var exercise_order = new Object();

exercise_order.init = function(basePath, page1){
  exercise_order.basePath = basePath;
  exercise_order.page = page1;
  //练习排行个人排行
  exercise_order.user_order_info();
  //练习排行信息列表
  exercise_order.exercise_order_info();
    // 初始化
  exercise_order.click();
}

  /*练习排行个人排行*/
 exercise_order.user_order_info = function() {
  var params = {
      'projectId': $("#projectId").val(),
      'orderType': $("#orderType").val()
   };
   $.ajax({
     url: exercise_order.basePath + "/exercise/user_exercise",
     data: params,
     type: "post",
     dataType:"json",
     success:function(data){
       var dataList = data.result.dataList;
       if(dataList.length == 0){
         return;
       }
       var inner = "", item;
           item = dataList[0];
           inner += "<tr style='background: #f4f4f4;'>";
           inner += "<td>"+item['rownum']+"</td>";
           inner += "<td><span class=\"text-orange\">"+item['userName']+"</span></td>";
           /*inner += "<td class=\"role_name\">"+item['roleName']+"</td>";*/
           inner += "<td>"+item['deptName']+"</td>";
           inner += "<td>"+item['totalQuestion']+"</td>";
           inner += "<td>"+item['yetAnswered']+"</td>";
           inner += "<td>"+(item['totalQuestion'] - item['yetAnswered'])+"</td>";
           inner += "<td>"+item['correctAnswered']+"</td>";
           inner += "<td>"+item['failAnswered']+"</td>";
           inner += "<td>"+ item['correctRate']+"%</td>";
           inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['answerStudyTime'])+"</td>";
           inner += "</tr>";
       // 渲染表单
       $("#user_exercise").empty().html(inner);
     }
   });
 }

/*练习排行信息列表*/
exercise_order.exercise_order_info = function() {
  var page_size=10;
  var exercise_order_url = appPath + "/exercise/showProjectExerciseOrder";
  exercise_order.page.init("exercise_order_form", exercise_order_url, "exercise_order", "project_exercise_page", 1, page_size);
  exercise_order.page.goPage(1);
  exercise_order.page.list = function(dataList){
    $("#user_exercise").removeClass("nav");
    var len = dataList.length;
    if(len == 1){
    $("#user_exercise").addClass("nav");
    }
    var inner = "", item;
    // 组装数据
    for(var i=0; i< len; i++) {
      item = dataList[i];
      inner += "<tr>";
      inner += "<td>"+item['rownum']+"</td>";
      inner += "<td><span class=\"text-orange\">"+item['userName']+"</span></td>";
      /*inner += "<td class=\"role_name\">"+item['roleName']+"</td>";*/
      inner += "<td>"+item['deptName']+"</td>";
      inner += "<td>"+item['totalQuestion']+"</td>";
      inner += "<td>"+item['yetAnswered']+"</td>";
      inner += "<td>"+(item['totalQuestion'] - item['yetAnswered'])+"</td>";
      inner += "<td>"+item['correctAnswered']+"</td>";
      inner += "<td>"+item['failAnswered']+"</td>";
      inner += "<td>"+ item['correctRate']+"%</td>";
      inner += "<td>"+TimeUtil.getHouAndMinAndSec(item['answerStudyTime'])+"</td>";
      inner += "</tr>";
    }
    return inner;
  }
}

/*点击事件*/
exercise_order.click = function(){
  //搜索
  $("#exercise_search").click(function(){
    $("#userName").val($.trim($("#userName").val()));
    exercise_order.exercise_order_info();
  });
  //全部
  $("#exercise_all").click(function(){
    //清空搜索框
    $("#userName").val('');
    exercise_order.exercise_order_info();
  });
  $('.status_flag span').click(function () {

    $(this).addClass('active').siblings('span').removeClass('active');
    exercise_order.user_order_info();
    exercise_order.exercise_order_info();
  })
}

exercise_order.index = function(){
  window.location.href = exercise_order.basePath + "/student";
}

exercise_order.record = function(){
  window.location.href = exercise_order.basePath + "/exercise/project_record?projectId=" + $("#projectId").val() + "&projectType=" + $("#projectType").val();
}

/* 练习排行排名 */
exercise_order.sort = function (type) {
  if(type == '1'){
      $("#orderType").val("1");
  }else if(type == '2'){
    $("#orderType").val("2");
  }else{
    $("#orderType").val("3");
  }
}
