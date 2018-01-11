var superAdmin_index=new Object();
superAdmin_index.page;
superAdmin_index.pageNum = 1;
superAdmin_index.page_size=10;
superAdmin_index.page_size1=11;
superAdmin_index.obj;
//init
superAdmin_index.init=function(_page){
    superAdmin_index.page = _page;
    superAdmin_index.initTable();
    superAdmin_index.click();
    superAdmin_index.getSubjects(1);
}


/**
 * 创建项目
 */
superAdmin_index.createProject=function (obj) {
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
                    window.location.href = appPath+"/super/project_create/public/create_project?projectTypeNo="+projectTypeNo+"&subjectId="+subjectId+"&subjectName="+subjectName+"&step="+'1';
                }else{
                    layer.alert("请至少选则一个项目类别",{icon:7})
                }

}

/**
 * 项目列表
 */
superAdmin_index.initTable = function () {
    var list_url = appPath + "/super/project_public/list";
    superAdmin_index.page.init("public_project_form", list_url, "public_project_list", "public_project_page", 1, superAdmin_index.page_size);
    superAdmin_index.page.goPage(1);
    superAdmin_index.page.list = function(dataList){
        var inner = "", item;
        superAdmin_index.obj = dataList;
        var len = dataList.length;
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];
            inner += '<tr >';
            inner += '<td>' + (i+1) + '</td>';
            inner += "<td id=\'pan_varProjectName + item['id']\'><span class=\"text-orange tooltip\" data-length='10'>" + item['project_name'] + "</span></td>";
            inner += '<td>'+ Enum.projectType(item['project_type']) + '</td>';
            if(item['project_type'] == 3){
                inner += '<td>'+ TimeUtil.longMsTimeToDateTime3(item['project_start_time']) +'<br>'+ TimeUtil.longMsTimeToDateTime3(item['project_end_time']) +'</td>';
            }else{
                inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) +'<br>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) +'</td>';
            }
            inner += '<td>'+ item['person_count'] + '</td>';
            inner += '<td>'+ Enum.projectStatus(item['project_status']) + '</td>';
            inner += '<td>'+ item['create_user'] + '</td>';
            inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['create_time']) + '</td>';
            inner += '<td>';
            inner += '<a href="javascript:;" class="a a-publish" onclick="superAdmin_index.project_info(\''+ item.id  + '\')"> 详情 </a>';
            inner += '<a href="javascript:;" class="a a-info" onclick="superAdmin_index.project_update(\''+ item.id +'\',\''+ item.project_type +'\',\''+ item.project_status +'\')"> 修改 </a>';
            if(item['project_status'] == '1'){
                inner += '<a href=\"javascript:;\" onclick=\"superAdmin_index.project_publish(\'' + item.id + '\',\'' + item.project_type + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_start_time) + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_end_time) + '\',1);\" class=\"a a-publish\">发布项目</a>';
            }
            if(item['project_status'] == '2'){
                inner += '<a href=\"javascript:;\" onclick=\"superAdmin_index.project_publish(\'' + item.id + '\',\'' + item.project_type + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_start_time) + '\',\'' + TimeUtil.longMsTimeToDateTime(item.project_end_time) + '\',2);\" class=\"a  a-close\">取消发布</a>';
            }
            inner += '<a href="javascript:;" class="a a-close " onclick="superAdmin_index.project_delete(\''+ item.id +'\')"> 删除 </a>';
            inner += '</td>';
            inner += '</tr>';
        }
        return inner;
    }
}

/**
 * 详情
 */
superAdmin_index.project_info = function (projectId) {

    window.location.href = appPath + "/super/project_create/public/project_info?projectId="+projectId;
}

/**
 * 修改
 */
superAdmin_index.project_update = function (projectId,projectType,projectStatus) {

    window.location.href = appPath + "/super/project_create/public/project_update?projectId="+projectId+"&projectType="+projectType+"&projectStatus="+projectStatus;
}

/**
 * 发布项目
 */
superAdmin_index.project_publish = function (projectId,projectType, startTime, endTime, operateType) {
    var url = appPath + '/super/project_create/public/project_publish';
    var param = {
        'project_id' : projectId,
        'project_type' : projectType,
        'startTime' : startTime,
        'endTime' : endTime,
        'type': operateType
    }

    if(operateType == 1){
        layer.confirm("确定要发布该项目吗?",{icon:3},function(){
            $.ajax({
                url:url,
                type:"POST",
                cache:false,
                async:false,
                dataType: "json",
                data:param,
                success:function(result) {
                    if (result.code == '10000' && result.result == null) {
                        superAdmin_index.initTable();
                        layer.msg("发布成功",{time:1500});
                    } else {
                        layer.alert(result.result);
                    }
                }
            });
        });
    }
    if(operateType == 2){
        layer.confirm("确定要取消发布该项目吗?",{icon:3},function(){
            $.ajax({
                url:url,
                type:"POST",
                cache:false,
                async:false,
                dataType: "json",
                data:param,
                success:function(result) {
                    if (result.code == '10000') {
                        superAdmin_index.initTable();
                        layer.msg("取消发布成功",{time:1500});
                    } else {
                        layer.msg("取消发布失败");
                    }
                }
            });
        });
    }

}

/**
 * 删除项目
 */
superAdmin_index.project_delete = function (projectId) {
    layer.confirm('确定执行操作？', {
        icon: 3,
        btn: ['确定','取消']      //按钮
    }, function(index){  // 执行删除操作
        $.ajax({
            url : appPath + '/super/project_create/public/project_delete',
            dataType : 'json',
            type : 'post',
            async : false,
            data : {"project_id" : projectId},
            success : function (data) {
                var result = data.code;
                if('10000' == result){
                    layer.alert("操作成功",{icon: 1});
                    layer.close(index);
                    setTimeout(function(){
                        superAdmin_index.initTable();
                    },500);
                }else{
                    layer.alert("操作失败",{icon: 2});
                }
            }

        });
    });
}

/**
 * 点击事件
 */
superAdmin_index.click = function () {
    //搜索
    $("#btn_search").click(function () {
        superAdmin_index.initTable();
    })
    //全部
    $("#btn_all").click(function () {
        $("#datBeginTime").val("");
        $("#datEndTime").val("");
        $("#project_name").val("")   //清空条件
        $("#btn_a").trigger("click")
    })


    //根据状态搜索
    $(".status").each(function(){
        var $this = this;
        $($this).click(function(){
            $("#project_status").val($($this).attr("finish_status"));
            $(".status").removeClass("active");
            $($this).addClass("active");
            superAdmin_index.initTable();
        })
    });

}

/**
 * 首页系统主题展示
 * @param pageNum
 */
superAdmin_index.getSubjects = function (pageNum) {
  var url = appPath + "/super/train_subject/subject_list";
  if(pageNum){
    superAdmin_index.pageNum = pageNum;
  }
  var params = {
    'pageSize': superAdmin_index.page_size1,
    'pageNum': superAdmin_index.pageNum
  };
  $.ajax({
    url: url,
    data: params,
    type: "post",
    dataType:"json",
    success:function(rs){
      var data = rs['result'];
      var dataList = data['dataList'];
      var inner = superAdmin_index.list(dataList);

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
        inner += "<a href='javascript:;' class='left icon_btn' style='display: none;'>左按钮</a>";
      }else{
        inner += "<a href=\"javascript:superAdmin_index.getSubjects("+(page-1)+");\" class='left icon_btn'>左按钮</a>";
      }
      if(haveNext==1){
        inner += "<a href=\"javascript:superAdmin_index.getSubjects("+(page+1)+");\" class='right icon_btn'>右按钮</a>";
      }else{
        inner += "<a href=\"javascript:;\" class='right icon_btn' style='display: none;'>右按钮</a>";
      }
      //console.log(inner)
      // 渲染表单
      $(".list_container").empty().html(inner);

    }
  });
}

superAdmin_index.list = function(dataList){
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
      '<div class="option" data-projectdId="1" onclick="superAdmin_index.select(this)"><span>TRAINING</span>培训</div>'+
      '<div class="option" data-projectdId="2" onclick="superAdmin_index.select(this)"><span>EXERCISE</span>练习</div>'+
      '<div class="option" data-projectdId="3" onclick="superAdmin_index.select(this)"><span>EXAM</span>考试</div>'+
      '</div>' +
      '<button class="create_btn" id="btn_project_create" onclick="superAdmin_index.createProject(this)">创建</button>' +
      '</div>'+
      "</li>";

  // 组装数据
  for(var i=0; i< len; i++) {
    item = dataList[i];
    inner += '<li>' +
        '<div class="show_img">' +
        '<img data-original="'+item.logo+'" alt=""  class="lazy lazy_img" src="'+ appPath + '/static/global/images/v2/no_photo.png">' +

        /* '<div class="show_div">' +
       '<div class="show_div_bg"></div>' +
        '<p>'+item.subjectDesc+'<span class="show_line"></span></p></div>'+*/
        '</div>'+
        '<div class="title">'+item.subjectName+'</div>'+

        '<div class="create">'+
        '<div class="options">'+
        '<div class="option" data-projectdId="1" onclick="superAdmin_index.select(this)"><span>TRAINING</span>培训</div>'+
        '<div class="option" data-projectdId="2" onclick="superAdmin_index.select(this)"><span>EXERCISE</span>练习</div>'+
        '<div class="option" data-projectdId="3" onclick="superAdmin_index.select(this)"><span>EXAM</span>考试</div>'+
        '</div>' +
        '<button class="create_btn" id="btn_project_create" data-subjectId="'+item.varId+'" data-subjectName="'+item.subjectName+'" onclick="superAdmin_index.createProject(this)" >创建</button>' +
        '</div>'+
        "</li>";
  }
    inner += '<script>' +
        '$("img.lazy").lazyload({effect: "fadeIn"});' +
        '</script>'
  inner += "</ul>"
  return inner;
}

superAdmin_index.select=function(obj){
  $(obj).toggleClass("active");
}
