/**
 * Created by Administrator on 2017-07-25.
 */
var exam_score = new Object();

exam_score.init = function(basePath, page2){
  exam_score.basePath = basePath;
  exam_score.page = page2;
  //考试成绩信息列表
  exam_score.exam_score_info();
    // 初始化
  exam_score.click();
}

/*考试成绩信息列表*/
exam_score.exam_score_info = function() {
  var page_size=10;
  var exam_score_url = appPath + "/student/studentProjectRecord/showExamScore";
  exam_score.page.init("exam_score_form", exam_score_url, "exam_score", "exam_score_page", 1, page_size);
  exam_score.page.goPage(1);
  exam_score.page.list = function(dataList){
      var len = dataList.length;
      var inner = "", item, examTime, examNo;
      // 组装数据
      for(var i=0; i< len; i++) {
        item = dataList[i];
        examTime = item['examTime'];
        examTime = examTime.substring(0,16);
        examNo = item['examNo'];
        inner += "<tr>";
        inner += "<td>"+ (i+1) +"</td>";
        inner += "<td>"+item['score']+"</td>";
        if (item['isPassed'] == '1'){
          inner += "<td>合格</td>";
        }else{
          inner += "<td><span class=\"text-red\">不合格</span></td>";
        }
        if (item['examType'] == '1'){
          inner += "<td>模拟考试</td>";
        }else{
          inner += "<td>正式考试</td>";
        }
        inner += "<td>"+examTime+"</td>";
        inner += "<td>"+item['examDuration']+"</td>";
        inner += '<td><a href=\"javascript:;\"  onclick=\"exam_score.exam_record(\''+ examNo + '\');\" class=\"a a-info\">答题记录</a>';
        inner += "</td>";
        inner += "</tr>";
      }
      return inner;
    }
}

/* 查看答题记录*/
exam_score.exam_record = function(examNo){
  common.goto_student_exam_answer_view(examNo);
}

/*exam_score.index = function(){
  window.location.href = exam_score.basePath + "/student";
}

exam_score.record = function(){
  window.location.href = exam_score.basePath + "/studentProjectRecord/project_record?projectId=" + $("#projectId").val() + "&projectType=" + $("#projectType").val();
}*/

/*点击事件*/
exam_score.click = function(){
    //下拉框改变执行事件
    $("#examType,#isPassed").change(function(){
        $("#exam_search").trigger("click");
    })
    //搜索
    $("#exam_search").click(function(){
      exam_score.exam_score_info();
    });
    //全部
    $("#exam_all").click(function(){
        //清空搜索框
        $("#examType").val('');
        $("#isPassed").val('');
      exam_score.exam_score_info();
    });
}
