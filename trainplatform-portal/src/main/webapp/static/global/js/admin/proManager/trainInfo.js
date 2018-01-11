var trainInfo=new Object();

trainInfo.init=function(basePath,page){
  trainInfo.basePath=basePath;
  trainInfo.page=page;

    // 培训详情
  trainInfo.project_train_info();
  //点击事件
  trainInfo.click();
}

    // 查看学员培训详情
    trainInfo.project_train_info = function(){
        var page_size=10
        var train_info_url = appPath + "/admin/projectCourseInfo/project_course_list";
      trainInfo.page.init("select_trainInfo_form", train_info_url, "train_Info", "train_Info_page", 1, page_size);
      trainInfo.page.goPage(1);
      trainInfo.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                    item = dataList[i];
                    inner += '<tr >';
                    inner += '<td>' + (i+1) + '</td>';
                    inner +='<td>'+item['course_name']+'</td>';
                    inner += '<td>'+TimeUtil.getHouAndMinAndSec(item['class_hour']*60)+'</td>';
                    inner += '<td>'+TimeUtil.getHouAndMinAndSec(item['requirement_studytime']*60)+'</td>';
                    inner += '<td>'+TimeUtil.getHouAndMinAndSec(item['total_studytime'])+'</td>';
                    if(item['total_studytime'] >= item['requirement_studytime']*60){
                      inner += '<td><span class="text-gray">已完成</span></td>';
                    }else{
                      inner += '<td><span class="text-red">未完成</span></td>';
                    }
                    inner += '</tr>';
            }
            return inner;
        }
    };

    //点击事件
   trainInfo.click = function(){
    //搜索
    $("#train_search").click(function(){
      $("#courseName").val($.trim($("#courseName").val()));
      trainInfo.project_train_info();
    });
    //全部
    $("#train_all").click(function(){
      $("#courseName").val("");
      trainInfo.project_train_info();
    });
  }













