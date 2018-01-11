function StudentIndex() {
    var _this = this;
    var page;
    var page_size=10;
    var obj;

    //初始化
    _this.init = function (page_) {
        page = page_;
        _this.student_tj();
        _this.initTable();
        _this.click();
    }

    //点击事件
    _this.click = function () {
        //搜索
        $("#search").click(function(){
          $("#projectName").val($.trim($("#projectName").val()));
            _this.initTable();
        })
        //全部
        $("#search_all").click(function(){
            $("#projectName").val("");
            $("#startTime").val("");
            $("#endTime").val("");
            $(".status:eq(0)").trigger("click");
        })
        //状态搜索
        $(".status").each(function(){
            var $this = this;
            $($this).click(function(){
                $("#projectStatus").val($($this).attr("project_status"));
                $(".status").removeClass("active");
                $($this).addClass("active");
                _this.initTable();
            })
        });
    }

      //学员统计
      _this.student_tj = function () {
         var url = appPath + "/student/student_tj";
        $.ajax({
          url: url,
          async: false,
          type: "post",
          dataType:"json",
          success:function(data){
            var result = data['result'];
            if(result){
              $("#ndxs").html(TimeUtil.getHour(result['year_studytime'])+"小时");
              $("#ljxs").html(TimeUtil.getHour(result['total_studytime'])+"小时");
              $("#train_count").html(result['train_count']+"次");
              if(result['pentName']){
                var inner1 = '<div class="cell">'+result['pentName']+'</div>';
                inner1+='<div class="cell text-center">'+parseInt(result['pentRank'])+'</div>'
                $("#pent_name").html(inner1);
              }
              if(result['prevName']){
                var inner2 = '<div class="cell">'+result['prevName']+'</div>';
                inner2+='<div class="cell text-center">'+parseInt(result['prevRank'])+'</div>'
                $("#prev_name").html(inner2);
              }
              var inner = '<div class="cell">'+result['company_name']+'</div>';
              inner+='<div class="cell text-center">'+result['rownum']+'</div>'
              $("#company_name").html(inner);
            }
          }
        });
      }

    //项目列表
    _this.initTable = function () {
        var list_url = appPath + "/student/pu/list";
        page.init("pu_form", list_url, "pu_table", "pu_page", 1, page_size);
        page.goPage(1);
        page.list = function(dataList){console.log(dataList)
            obj = dataList;
            // 组装数据
            var len = dataList.length;
            var inner = "", item;
            for(var i=0; i< len; i++) {
                item = dataList[i];
                /*if('1' == item['project_status']){
                    inner += "<tr style='display: none'>";
                }else{
                    inner += "<tr>";
                }*/
                inner += "<tr>";
                inner += "<td>" + (i+1) + "</td>";
                inner += "<td><span class=\"text-orange tooltip\" data-length='30'>" + item['project_name'] + "</span></td>";
                inner += "<td>" + Enum.projectType(item['project_type']) + "</td>";

                var projectType = item['project_type'];
                /*if(projectType == '3'){
                    inner += '<td>'+ TimeUtil.longMsTimeToDateTime3(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeToDateTime3(item['project_end_time']) + '</td>';
                }else{
                    inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + '</td>';
                }*/
                inner += "<td>" + TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + "<br>" + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + "</td>";

                inner += "<td>" + Enum.projectStatus(item['project_status']) + "</td>";
                inner += "<td>" + item['create_user'] + "</td>";
                inner += "<td>" + TimeUtil.longMsTimeConvertToDateTime(item['create_time']) + "</td>";
                inner += "<td>";
                inner += '<a href=\"javascript:;\" onclick="studentIndex.projectInfo(\'' + item.id + '\',\'' + item.project_mode + '\');" class="a a-info">详情</a>';
                if(item['project_status'] == '2' || item['project_status'] == '3'){
                    inner += '&nbsp;<a href=\"javascript:;\" onclick="studentIndex.enterProject(\'' + item.id + '\',\''+ item.project_status + '\',\''+ item.project_type + '\');" class="a a-view">进入项目</a>';
                }else{
                    inner += '&nbsp;<a href=\"javascript:;\"  class="a">进入项目</a>';
                }
                inner += '&nbsp;<a href=\"javascript:;\" onclick="studentIndex.projectRecord(\'' + item.id + '\',\''+ item.project_type + '\',\''+ item.project_status + '\');" class="a a-publish">查看记录</a>';
                inner += "</td>";
                inner += "</tr>";
            }
            return inner;
        }
    }

    //查看项目详情
    _this.projectInfo = function(projectId,projectMode) {
        window.location.href=appPath+"/student/ProjectInfo/student_info?projectId="+projectId+"&projectMode="+projectMode;
    }
    //进入项目
    _this.enterProject = function(projectId,projectStatus,projectType){
        if(projectStatus == '3'){
            if('3' == projectType){  // 进入单独的考试项目
              window.location.href=appPath+"/student/enterProject/student_enter_exam?projectId="+projectId;
            }else{                  // 进入非单独的考试项目
              window.location.href=appPath+"/student/enterProject/student_enter?projectId="+projectId;
            }
        }else if (projectStatus == '2'){
          layer.msg("对不起，未开始的项目不能进入项目",{icon:5,time:1500});
        }else{
          layer.msg("对不起，已结束的项目不能进入项目",{icon:5,time:1500});
        }
    }
    //查看记录
    _this.projectRecord = function(projectId,projectType,projectStatus) {
      if(projectStatus == '3' || projectStatus == '4'){
        window.location.href=appPath+"/student/studentProjectRecord/project_record?projectId="+projectId+"&projectType="+projectType;
      }else{
        layer.msg("对不起，未开始的项目不能查看记录",{icon:5,time:1500});
      }
    }


}

var studentIndex = new StudentIndex();