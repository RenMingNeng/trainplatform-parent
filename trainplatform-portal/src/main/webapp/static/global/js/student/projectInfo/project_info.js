/**
 * Created by Administrator on 2017/7/24.
 */


 var projectinfo = new Object();

projectinfo.init = function(basePath, projectId,roleId,courseName,projectType,page,tbody_id,page_id){
    projectinfo.basePath = basePath;
    projectinfo.page = page;
    projectinfo.projectid=projectId;
    projectinfo.roleId=roleId;
    projectinfo.courseName=courseName;
    projectinfo.projectType=projectType;
    projectinfo.tbody_id=tbody_id;
    projectinfo.page_id=page_id;
    projectinfo.project_course();

}
//刷新项目人员
projectinfo.project_course=function () {
    var page_size=10
    var user_list_url = appPath + "/student/ProjectInfo/courseInfo_list";
    projectinfo.page.init("select_course_form", user_list_url, "courseInfo_tbody", "courseInfo_page", 1, page_size);
    projectinfo.page.goPage(1);
    projectinfo.page.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        if(dataList.length<1){
            var length = $("#"+projectinfo.tbody_id).parent().find("th").length;
            $("#"+projectinfo.tbody_id).empty().html(projectinfo.noList(length));$("#"+projectinfo.page_id).empty();return
        }
        console.log(len);
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];
            console.log(item);
            inner += "<tr>";
            inner += "<td>"+(parseInt(i)+1)+"</td>";
            inner += '<td>'+item['course_no']+'</td>';
            inner += "<td><span class=\"text-orange\" title=\"+item['course_name']+\"></span>"+item['course_name']+"</td>";
            if(projectinfo.isContains('1457',projectinfo.projectType)) {
                inner += "<td>" + TimeUtil.getHouAndMinAndSec(item['class_hour']*60) + "</td>";
                inner += "<td>" + TimeUtil.getHouAndMinAndSec(item['requirement']*60) + "</td>";
            }
            if(projectinfo.isContains('234567',projectinfo.projectType)) {
                inner += "<td>" + item['question_count'] + "</td>";
            }
            // if(projectinfo.isContains('3567',projectinfo.projectType)) {
            //     inner += "<td>" + item['select_count'] + "</td>";
            // }
            inner += "</tr>";
        }
        return inner;
    }

}


   //通过课程名字搜索
   projectinfo.search=function(){
       projectinfo.courseName=$.trim($("#courseName").val());
       projectinfo.project_course();
   }
   //搜索
   projectinfo.searchAll=function(){
       $("#courseName").val("");
       projectinfo.courseName="";
       projectinfo.project_course();
   }
   projectinfo.isContains=function(str,substr){
       return new RegExp(substr).test(str);
   }

  projectinfo.noList = function(length){
      var inner = "";
    inner += "<tr>";
    inner += '<td colspan=\''+length+'\'>对不起，没有找到任何相关记录...</td>'
    inner += "</tr>";
    return inner;
  }
