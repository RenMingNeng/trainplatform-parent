/**
 * Created by Administrator on 2017/7/24.
 */


 var pro_info = new Object();


pro_info.init = function(basePath,  projectId,roleId,courseName,projectMode,projectType,page,page1,tbody_id,page_id){
    pro_info.page=page;
    pro_info.page1=page1;
    pro_info.basePath = basePath;
    pro_info.projectid=projectId;
    pro_info.roleId=roleId;
    pro_info.courseName=courseName;
    pro_info.projectMode=projectMode;
    pro_info.projectType=projectType;
    pro_info.tbody_id=tbody_id;
    pro_info.page_id=page_id;

    pro_info.project_course();
    pro_info.project_user();
}

//刷新项目人员
pro_info.project_course=function () {
    var page_size=10
    var user_list_url = appPath + "/admin/project_create/private/project_course_list";
    pro_info.page.init("select_course_form", user_list_url, "courseInfo_tbody", "courseInfo_page", 1, page_size);
    pro_info.page.goPage(1);
    pro_info.page.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        if(dataList.length<1){
            var length = $("#"+pro_info.tbody_id).parent().find("th").length;
            $("#"+pro_info.tbody_id).empty().html(pro_info.noList(length));$("#"+pro_info.page_id).empty();return
        }
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];
            inner += "<tr>";
            inner += "<td>"+(parseInt(i)+1)+"</td>";
            inner += '<td>'+item['course_no']+'</td>';
            inner += "<td><span class=\"text-orange\" title=\"+item['course_name']+\"></span>"+item['course_name']+"</td>";
            /*if(pro_info.projectMode == '0'){
                inner += '<td>'+item['role_name']+'</td>';
            }*/
            if(pro_info.isContains('1457',pro_info.projectType)) {
                inner += "<td>" + TimeUtil.getHouAndMinAndSec(item['class_hour']*60) + "</td>";
                inner += "<td>" + TimeUtil.getHouAndMinAndSec(item['requirement']*60) + "</td>";
            }
            if(pro_info.isContains('234567',pro_info.projectType)) {
                inner += "<td>" + item['question_count'] + "</td>";
            }
           /* if(pro_info.isContains('3567',pro_info.projectType)) {
                inner += "<td>" + item['select_count'] + "</td>";
            }*/
            inner += "</tr>";
        }
        return inner;
    }

}


   //通过课程名字搜索
   pro_info.search=function(){
       pro_info.courseName=$.trim($("#courseName").val());
       pro_info.project_course();
   }
   //搜索
   pro_info.searchAll=function(){
       $("#courseName").val("");
       pro_info.courseName="";
       pro_info.project_course();
   }
   pro_info.isContains=function(str,substr){
       return new RegExp(substr).test(str);
   }

pro_info.noList = function(length){
  var inner = "";
  inner += "<tr>";
  inner += '<td colspan=\''+length+'\' align=\"center\" class=\"green\" style=\"text-align: center\">对不起，没有找到任何相关记录...</td>'
  inner += "</tr>";
  return inner;
}
//刷新项目人员
pro_info.project_user=function () {
    var page_size=10
    var user_list_url = appPath + "/admin/project_create/private/project_user_list";
    pro_info.page1.init("select_user_form", user_list_url, "select_user_list", "select_user_page", 1, page_size);
    pro_info.page1.goPage(1);
    pro_info.page1.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];
            inner += '<tr >';
            inner += '<td>' + (i+1) + '</td>';
            inner +='<td>'+item['user_name']+'</td>';
           /* inner +='<td><span class="text-orange">'+item['role_name']+'</span></td>';*/
            inner +='<td><span class="text-orange">'+item['department_name']+'</span></td>';
            inner += '</tr>';
        }
        return inner;
    }

}

//通过课程名字搜索
pro_info.userSearch=function(){
    pro_info.project_user();
}
//搜索
pro_info.userSearchAll=function(){
    $("#userName").val("");
    pro_info.project_user();
}