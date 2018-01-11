/**
 * 创建公开型项目第一步
 */
function Step_5() {
  var _this = this;
  var containTrainType, containExamType, containExerciseType;
  var projectId = $("#projectId").val();           // 项目编号
  var projectTypeNo = $("#projectTypeNo").val();  // 项目类型编号
  var projectStatus = $("#projectStatus").val();  // 项目状态

  _this.init = function () {
    //含有培训的
    containTrainType = options.containTrainType;
    //含有考试的
    containExamType = options.containExamType;
    //含有练习的
    containExerciseType = options.containExerciseType;

    _this.initEvent();

    stragegySelect_public.stragegy_listener();

    stragegySelect_public.stragegy_select(projectId, containTrainType);
  }

  _this.initEvent = function () {
      // 回到首页
      $("#index").click(function() {
          window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
      });
    // 点击上一步
    $("#step_4").click(function () {
      var param = "?projectId=" + projectId + "&projectTypeNo=" + projectTypeNo
          + "&step=" + '4';
      window.location.href = appPath
          + "/super/project_create/public/create_project" + param;
    });

    // (保存)
    $("#btn_save").click(function () {
        var isTrue = stragegySelect_public.stragegy_save();

      if ('1' == projectStatus && isTrue) {
        layer.confirm('保存成功，是否发布？', {
          icon: 3,
          btn: ['是', '否'] //按钮
        }, function () {
          // 异步发布项目
            _this.publish();
        }, function () {
          // 返回首页列表
          window.location.href = appPath
              + "/super/project_create/public/create_project?step=" + '0';
        });
      }else if(!isTrue){
          layer.alert("组卷策略数值不对，不能发布");
          return;
      } else {
          _this.publish();
      }
    });
  };

  _this.publish = function () {
      $.ajax({
          url: appPath + '/super/project_create/public/project_publish',
          dataType: 'json',
          async: false,
          type: 'post',
          data: {
              "project_id": projectId,
              "project_type": projectTypeNo,
              'type': '1'
          },
          success: function (data) {
              var code = data.code;
              var result = data.result
              if('10000' == code && result == null){
                  layer.msg("操作成功",{icon: 1},function () {
                      // 返回首页列表
                      window.location.href = appPath+"/super/project_create/public/create_project?step="+'0';
                  });
              }else{
                  layer.alert(result,{icon: 2});
              }
          }
      });
  };

  //判断字符串是否包含某字符
  _this.isContains = function (str, substr) {
    return new RegExp(substr).test(str);
  }

  //关闭对话框
  _this.closeDialog = function () {
    parent.layer.close(parent.layer.getFrameIndex(window.name));
  }

}

var step_5 = new Step_5();