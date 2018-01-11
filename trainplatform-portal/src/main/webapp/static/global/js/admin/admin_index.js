
var admin_index = new Object();

admin_index.page;
admin_index.page1;
admin_index.page_size=10;
admin_index.page_size1=11
admin_index.obj;
admin_index.pageNum = 1;
admin_index.project_status  = $("#project_status").val();

admin_index.init=function(page_,page1_){
  admin_index.page = page_;
  admin_index.page1 = page1_;
  admin_index.initTable();
  admin_index.initTable_public();
  //显示项目状态
  admin_index.show_project_status();
  //初始化数据统计信息
  admin_index.getSubjects(admin_index.pageNum);
  // 初始化
  admin_index.click();
}

admin_index.show_project_status = function(){
  $("span[project_status='"+admin_index.project_status+"']").siblings().removeClass("active");
  $("span[project_status='"+admin_index.project_status+"']").addClass("active");
}

//主题展示
admin_index.getSubjects = function (pageNum) {
  var url = appPath + "/admin/train_subject/subject_list";
    if(pageNum){
        admin_index.pageNum = pageNum;
    }
    var params = {
        'pageSize': admin_index.page_size1,
        'pageNum': admin_index.pageNum
    };
    $.ajax({
        url: url,
        data: params,
        type: "post",
        dataType:"json",
        success:function(rs){
            var data = rs['result'];
            var dataList = data['dataList'];
            //var length = $("#subjects").parent().find("th").length;
            /*if(dataList.length<1){
                $("#"+_this.tbody_id).empty().html(_this.noList(length));$("#"+_this.page_id).empty();return
            }*/
            var inner = admin_index.list(dataList);

            // 计算分页
            var page = data['page']; // 当前页数
            var pageSize = data['pageSize']; // 每页数量
            var count = data['count']; // 总条数
            var totalPages = data['totalPages']; // 总页数
            var haveNext = data['haveNext']; // 是否有下一页
            var nextPage = data['nextPage']; // 下一页页数
            var havePre = data['havePre']; // 是否有上一页
            var prePage = data['prePage']; // 上一页页数

            if(page == 1){
                inner += "<a href='javascript:;' class='left icon_btn' style='display: none;'>左按钮</a>";
            }else{
                inner += "<a href='javascript:;' class='left icon_btn'>左按钮</a>";
            }
            if(havePre == 0){
                inner += "<a href='javascript:;' class='left icon_btn' style='display: none;'>左按钮 </a>";
            }else{
                inner += "<a href=\"javascript:admin_index.getSubjects("+(page-1)+");\" class='left icon_btn'>左按钮 </a>";
            }
            if(haveNext==1){
                inner += "<a href=\"javascript:admin_index.getSubjects("+(page+1)+");\" class='right icon_btn'>右按钮 </a>";
            }else{
                inner += "<a href=\"javascript:;\" class='right icon_btn' style='display: none;'>右按钮  </a>";
            }
            //console.log(inner)
           // 渲染表单
            $(".list_container").empty().html(inner);

        }
    });
}

admin_index.list = function(dataList){
    var len = dataList.length;
    var inner = "<ul class='clearfix'>", item;
    inner += '<li class="first">' +
                '<div class="show_img">' +
                    '<img src="'+appPath+'/static/global/images/v2/no-project.png" alt="">' +
                    /*'<span class="enter">点击进入</span>'+*/
                    /*'<div class="show_div">' +
                        '<div class="show_div_bg"></div>' +
                    '</div>'+*/

                    /*'<p>无主题<span class="show_line"></span></p>' +*/
                '</div>'+
                '<div class="title">' +
                    '<img src="'+appPath+'/static/global/images/v2/title_info_icon.png" alt="">' +
                    '<div class="title_1"><b>从业人员<i>自定义培训</i></b></div>' +
                    '<div class="title_2">(您可以在此自定义培训内容)</div>' +
                '</div>'+
                    '<div class="create">'+
                        '<div class="options">'+
                        '<div class="option" data-projectdId="1" onclick="admin_index.select(this)"><span>TRAINING</span>培训</div>'+
                        '<div class="option" data-projectdId="2" onclick="admin_index.select(this)"><span>EXERCISE</span>练习</div>'+
                        '<div class="option" data-projectdId="3" onclick="admin_index.select(this)"><span>EXAM</span>考试</div>'+
                    '</div>' +
                    '<button class="create_btn" id="btn_project_create" onclick="admin_index.createProject(this)">创建</button>' +
                '</div>'+
            "</li>" ;


    // 组装数据
    for(var i=0; i< len; i++) {
        item = dataList[i];
        if(null ==item.subjectDesc){item.subjectDesc =""}
        var temp_logo = appPath + "/static/global/images/subject_image/subject_image_"+ parseInt(Math.round(Math.random())) +".png";
        /*if("" == item.logo){
            item.logo = appPath + "/static/global/images/subject_image/subject_image_"+ parseInt(Math.round(Math.random())) +".png";
        }*/
        inner += '<li>' +
                    '<div class="show_img">' +
                      '<img alt="" class="lazy lazy_img" src="'+ item.logo + '" onerror="admin_index.imgError(this)" >' +

                      /*'<div class="show_div">' +
                        '<div class="show_div_bg"></div>' +

                          '<p>'+item.subjectDesc+'<span class="show_line"></span></p></div>'+*/

                        '</div>'+
                        '<div class="title">'+item.subjectName+'</div>'+
                        '<div class="create">'+
                          '<div class="options">'+
                            '<div class="option" data-projectdId="1" onclick="admin_index.select(this)"><span>TRAINING</span>培训</div>'+
                            '<div class="option" data-projectdId="2" onclick="admin_index.select(this)"><span>EXERCISE</span>练习</div>'+
                            '<div class="option" data-projectdId="3" onclick="admin_index.select(this)"><span>EXAM</span>考试</div>'+
                          '</div>' +
                          '<button class="create_btn" id="btn_project_create" data-subjectId="'+item.varId+'" data-subjectName="'+item.subjectName+'" onclick="admin_index.createProject(this)" >创建</button>' +
                        '</div>'+
                  "</li>";
    }
    inner += '<script>' +
            '$("img.lazy").lazyload({effect: "fadeIn"});' +
        '</script>'
    inner += "</ul>"
    //console.log(inner)
    return inner;

}

admin_index.select=function(obj){
    var $this = $(obj);
    $this.toggleClass("active");
}
admin_index.imgError = function(obj){
    var temp_logo = appPath + "/static/global/images/subject_image/subject_image_"+ parseInt(Math.round(Math.random())) +".png";
    $(obj).attr( 'src', temp_logo );
}

    //首页统计信息
admin_index.statistics = function () {
          $.ajax({
            url: appPath + "/admin/tongJi",
            type: 'post',
            success: function (data) {
                $("#countUser").text(data.countUser);
                $("#countTrainUser").text(data.countTrainUser);
                $("#percentTrainComplete").text(data.percentTrainComplete+"%");
                $("#countCourse").text(data.countCourse);
                $("#countQuestion").text(data.countQuestion);
                $("#countProject").text(data.countProject);
                $("#totalClassHour").text(data.totalClassHour);
                $("#averagePersonClassHour").text(data.averagePersonClassHour);
                $("#totalYearClassHour").text(data.totalYearClassHour);
                $("#averageYearClassHour").text(data.averageYearClassHour);
                $("#countTrain").text(data.countTrain);
                $("#countTrainCompleteYes").text(data.countTrain);
                $("#countExam").text(data.countTrain);
                $("#countExamPassYes").text(data.countTrain);
            }

          })
}

//创建项目 (取项目类型和主题编号)
admin_index.createProject=function (obj) {
    var $div= $(obj).prev().find(".active")
    var projectTypeNo="";
    var array= [];
    $($div).each(function(index,element){
            var str=$(this).attr("data-projectdId");
            array.push(str);

    })

    if(array.length!=0){
        //项目类型 1：培训/2：练习/3：考试/4：培训-练习/5：培训-考试/6：练习，考试/7：培训，练习，考试

        if(array.length==1){
            for(var i=0;i<array.length;i++){
                if(array[i]==1){
                    projectTypeNo="1";
                }else if(array[i]==2){
                    projectTypeNo="2";
                }else{
                    projectTypeNo="3";
                }
            }
        }

        if(array.length==2){
            if(array[0]==1&&array[1]==2){
                projectTypeNo="4";
            }else if(array[0]==1&&array[1]==3){
                projectTypeNo="5";
            }else{
                projectTypeNo="6";
            }
        }
        if(array.length==3){
            projectTypeNo="7";
        }

        // 项目主题编号
        var subjectId = $(obj).attr("data-subjectId");
        if(subjectId =="" || subjectId==undefined ){
            subjectId="";
        }
        var subjectName = $(obj).attr("data-subjectName");
        if(subjectName =="" || subjectName==undefined ){
            subjectName="";
        }
        window.location.href = appPath +"/admin/project_create/private/create_private_project?projectTypeNo="+projectTypeNo+"&subjectId="+subjectId+"&subjectName="+subjectName;
    }else{
        layer.msg("请选择项目类型")
    }

  //})

};

/**
 * 企业项目列表
 */
admin_index.initTable = function (){
  var list_url = appPath + "/admin/project_private/list";
  admin_index.page.init("private_project_form", list_url, "private_project_list", "private_project_page", 1, admin_index.page_size);
  admin_index.page.goPage(1);
  admin_index.page.list = function(dataList){//console.log(dataList)
    var inner = "", item;
    admin_index.obj = dataList;
    var len = dataList.length;
    // 组装数据
    for(var i=0; i< len; i++) {
      item = dataList[i];
      inner += "<tr>";
      inner += "<td>" +(i+1)+ "</td>";
      inner += "<td class='text-left' id=\'pan_varProjectName + item['id']\'><span class=\"tooltip\" data-length='20'>" + item['project_name'] + "</span></td>";
      inner += "<td>" + Enum.projectType(item['project_type']) + "</td>";

        var projectType = item['project_type'];
        /*if(projectType == '3'){
            inner += '<td>'+ TimeUtil.longMsTimeToDateTime3(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeToDateTime3(item['project_end_time']) + '</td>';
        }else{
            inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + '</td>';
        }*/
      inner += "<td>" + TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + "<br>" + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + "</td>";

      inner += "<td>" + item['person_count'] + "</td>";
      inner += "<td>" + Enum.projectStatus(item['project_status']) + "</td>";
      inner += "<td>" + item['create_user'] + "</td>";
      inner += "<td>" + TimeUtil.longMsTimeConvertToDateTime(item['create_time']) + "</td>";
      inner += "<td>";
      inner += '<a href=\"javascript:;\" onclick=\"admin_index.show_info(\'' + item.id + '\',\'' + item.project_mode + '\');\" class=\"a a-info\">详情</a>';
      if(item['project_mode'] == '0'){
        if(item['project_status'] == '1'){
          inner += '<a href=\"javascript:;\" onclick=\"admin_index.project_is_publish(\'' + item.id + '\',\'' + item.project_type + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_start_time) + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_end_time) + '\',1);\" class=\"a a-publish\">发布项目</a>';
        }
        if(item['project_status'] == '2'){
          inner += '<a href=\"javascript:;\" onclick=\"admin_index.project_is_publish(\'' + item.id + '\',\'' + item.project_type + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_start_time) + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_end_time) + '\',2);\" class=\"a  a-close\">取消发布</a>';
        }
      }
      if(item['project_status'] == '3' || item['project_status'] == '4'){
        inner += '<a href=\"javascript:;\" onclick=\"admin_index.show_process(\'' + item.id + '\', \'' + item.project_type + '\');\" class=\"a  a-view\">查看进度</a>';
      }
      inner += "</td>";
      inner += "</tr>";
    }
    admin_index.handlerPublish();
    return inner;
  }
};

/**
 * 公开项目列表
 */
admin_index.initTable_public = function (){
  var list_url = appPath + "/admin/project_private/list";
  admin_index.page1.init("public_project_form", list_url, "public_project_list", "public_project_page", 1, admin_index.page_size);
  admin_index.page1.goPage(1);
  admin_index.page1.list = function(dataList){//console.log(dataList)
    $("#public_project_list").removeClass("no");
    var inner = "", item;
    admin_index.obj = dataList;
    var len = dataList.length;
    // 组装数据
    for(var i=0; i< len; i++) {
      item = dataList[i];
      inner += "<tr>";
      inner += "<td>" +(i+1)+ "</td>";
      inner += "<td class='text-left' id=\'pan_varProjectName + item['id']\'><span class=\"tooltip\" data-length='30'>" + item['project_name'] + "</span></td>";
      inner += "<td>" + Enum.projectType(item['project_type']) + "</td>";

        var projectType = item['project_type'];
        // if(projectType == '3'){
        //     inner += '<td>'+ TimeUtil.longMsTimeToDateTime3(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeToDateTime3(item['project_end_time']) + '</td>';
        // }else{
        // }
        inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + '</td>';
        // inner += "<td>" + project_time_format(item['project_start_time']) + "<br>" + project_time_format(item['project_end_time']) + "</td>";

      inner += "<td>" + item['person_count'] + "</td>";
      inner += "<td>" + Enum.projectStatus(item['project_status']) + "</td>";
      inner += "<td>" + item['create_user'] + "</td>";
      inner += "<td>" + TimeUtil.longMsTimeConvertToDateTime(item['create_time']) + "</td>";
      inner += "<td>";
      inner += '<a href=\"javascript:;\" onclick=\"admin_index.show_info(\'' + item.id + '\',\'' + item.project_mode + '\');\" class=\"a a-info\">详情</a>';
        inner += '<a href=\"javascript:;\" onclick=\"admin_index.user_manager(\'' + item.id + '\');\" class=\"a a-view\">人员管理</a>';
      inner += '<a href=\"javascript:;\" onclick=\"admin_index.sign_up(\'' + item.id + '\',\'' + item.project_type + '\');\" class=\"a a-publish\">报名</a>';
      inner += "</td>";
      inner += "</tr>";
    }
    return inner;
  }
};

  // 七天之内,需要发布的提示
admin_index.handlerPublish = function(){
  var data = admin_index.obj;
    var len = data.length;
    if(len==0)
      return;
    var row = null;
    // 项目开始时间
    var datBeginTime = null;
    var datEndTime = null;
    var chrProjectStatus = null;
    var chrProjectShare = null;
    var id = null;
    var arr = [];
    for(var i=0; i<len; i++){
      row = data[i];
      id = row.id;
      datBeginTime = TimeUtil.longMsTimeToDateTime(row.project_start_time);
      datEndTime = TimeUtil.longMsTimeToDateTime(row.project_end_time);
      chrProjectStatus = row.project_status;
      chrProjectShare = row.project_mode;
      if(!datBeginTime){
        continue;
      }
      var dateP = new Date(datBeginTime.replace(/-/g,'/'));
      var dateE = new Date(datEndTime.replace(/-/g,'/'));
      var date1 = new Date();
      var date2 = new Date(date1);
      date2.setDate(date1.getDate()+7);
      /*if(("2"==chrProjectStatus && dateP.getTime()<=date1.getTime()) || ("3"==chrProjectStatus && dateE.getTime()<date1.getTime())){
        admin_index.project_is_publish(id,'','','',3,chrProjectStatus);
      }*/
      // 已发布项目和公开性项目不提醒
      if("3"==chrProjectStatus || "4"==chrProjectStatus){
          continue;
      }
      // 提前七天提醒：当前时间<项目开始时间<当前时间+7天
      if("1"==chrProjectStatus){
        arr.push(row);
      }
    }
    // 如果培训时间已到，管理员仍未发布，处理方式如下： 默认到项目开始时间自动发布， 平台提前7天开始提示，每次进入平台都要求完成发布操作
    if("1"==getCookie("publishFlag"))return;
    if(arr.length==0)return;
    layer.open({
      title:'提示',
      closeBtn:0,
      content: '<span style=\"font-size: 15px;font-weight: 700;color: rgb(221, 25, 25);\">尊敬的管理员,系统检测到近期(一周内)有需要发布的项目,请您完成发布操作!</span>',
      scrollbar: false,
      btn: ['知道了'],
      yes: function(index){
        layer.close(index);
        //location.href="#private_project_list";  //兼容ie跳转
        //$("html,body").animate({scrollTop: $("#div_wjs_project").offset().top}, 1000);
      }
    });
    setCookie("publishFlag", "1");

  };

/*详情*/
admin_index.show_info = function(projectId,projectMode){
  window.location.href = appPath + "/admin/project/proManagerInfo?projectId=" + projectId + "&projectMode="+ projectMode +"&type=index";
}

/*查看进度*/
admin_index.show_process = function(projectId, projectType){
  window.location.href = appPath + "/admin/project/proManagerProcess?projectId=" + projectId + "&projectType=" + projectType;
}

/**
 * 报名弹窗
 * @param projectId
 * @param projectType
 */
admin_index.sign_up = function(projectId,projectType){
    // 调用选择培训人员弹窗
    layer.open({
        type : 2,
        title : '选择报名人员',
        area : [ '1200px', '80%' ],		//弹出层大小
        scrollbar : false,				//false隐藏滑动块
        content : [ appPath + '/popup/userSignUp?projectId='+projectId+"&projectType="+projectType+"&companyId="+$("#company_id").val(), 'yes' ]
    });
}

/**
 * 人员管理弹窗
 * @param projectId
 * @param projectType
 */
admin_index.user_manager = function(projectId){
    layer.open({
        type : 2,
        title : '选择报名人员',
        area : [ '1000px', '80%' ],		//弹出层大小
        scrollbar : false,				//false隐藏滑动块
        content : [ appPath + '/popup/userManager?projectId='+projectId, 'yes' ]
    });
}

/*取消或者发布项目*/
admin_index.project_is_publish = function(id, projectType, startTime, endTime, createType, projectStatus){
  var url = "/admin/project/proManagerPublish";
  var param = {
    'id':id,
    'projectType': projectType,
    'startTime' : startTime,
    'endTime' : endTime,
    'type':createType
  }
  if(createType == 1){
    layer.confirm("确定要发布该项目吗?",{icon:3},function(){
      $.ajax({
        url:appPath + url,
        type:"POST",
        cache:false,
        async:false,
        dataType: "json",
        data:param,
        success:function(result) {
          if (result.code == '10000' && result.result == null) {
            admin_index.initTable();
            layer.msg("发布成功",{time:1000});
          } else {
            layer.alert(result.result);
          }
        }
      });
    });
  }
  if(createType == 2){
    layer.confirm("确定要取消发布该项目吗?",{icon:3},function(){
      $.ajax({
        url:appPath + url,
        type:"POST",
        cache:false,
        async:false,
        dataType: "json",
        data:param,
        success:function(result) {
          if (result.code == '10000') {
            admin_index.initTable();
            layer.msg("取消发布成功",{time:1000});
          } else {
            layer.msg("取消发布失败");
          }
        }
      });
    });
  }
  if(createType == 3) {
    $.ajax({
      url: appPath + "/admin/project/proManagerPublish",
      type: "POST",
      cache: false,
      async: false,
      dataType: "json",
      data: {'id': id, 'type': createType, 'projectStatus':projectStatus},
      success: function (result) {

      }
    });
  }
}

/*点击事件*/
admin_index.click = function() {
  //企业项目搜索
  $("#search").click(function () {
    $("#project_name").val($.trim($("#project_name").val()));
    admin_index.initTable();
  });
  //企业项目全部
  $("#search_all").click(function(){
    $("#datBeginTime").val("");
    $("#datEndTime").val("");
    $("#project_name").val("");
    $("#projectStatus").val("");
    //$(".status").removeClass("active");
    $("#private_clickAll").trigger("click");
  });

  //公开项目搜索
  $("#public_search").click(function () {
    $("#project_name").val($.trim($("#project_name").val()));
    admin_index.initTable_public();
  });
  //公开项目全部
  $("#public_search_all").click(function(){
    $("#public_datBeginTime").val("");
    $("#public_datEndTime").val("");
    $("#public_project_name").val("");
    $("#public_projectStatus").val("");
    //$(".status public").removeClass("active");
    $("#public_clickAll").trigger("click");
  });

  $(".status").click(function(){
    var $this = $(this);
    var target = $this.parents('.status_flag');
    var id = target.attr('id');
    if( id === 'public' ){
      $("#public_project_status").val($($this).attr("public_project_status"));
      $(this).addClass("active").siblings('.status').removeClass('active');
      admin_index.initTable_public()
    }else{
      $("#project_status").val($($this).attr("project_status"));
      $(this).addClass("active").siblings('.status').removeClass('active');
      admin_index.initTable();
    }
  });


}

admin_index.project_type_2_str = function (project_type) {
  if(!project_type) return "";
  switch (project_type) {
    case '1': return '培训'; break;
    case '2': return '练习'; break;
    case '3': return '考试'; break;
    case '4': return '培训、练习'; break;
    case '5': return '培训、考试'; break;
    case '6': return '练习、考试'; break;
    case '7': return '培训、练习、考试'; break;
    default : '';
  }
};

admin_index.project_status_2_str = function (project_status) {
  if(!project_status) return "";
  switch (project_status) {
    case '1': return "<span class=\"text-red\">" + "未发布" + "</span>"; break;
    case '2': return "<span class=\"text-gray\">" + "未开始" + "</span>"; break;
    case '3': return "<span class=\"text-green\">" + "进行中" + "</span>"; break;
    case '4': return "<span class=\"text-gray\">" + "已结束" + "</span>"; break;
    default : '';
  }

};
