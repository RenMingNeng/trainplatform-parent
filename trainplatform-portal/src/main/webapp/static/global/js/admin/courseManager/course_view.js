/**
 * Created by Administrator on 2017/7/24.
 */

    var courseView = new Object();

    // 初始化
    courseView.init = function(basePath, courseId){
        //console.log("basePath="+basePath);console.log("projectId="+projectId);console.log("courseId="+courseId);console.log("userId="+userId);console.log("source="+source);
      courseView.basePath = basePath;
      courseView.courseId = courseId;
      // 初始化事件
      courseView.initEvent();
      // 获取课程基本信息
      courseView.getCourse();

    };

courseView.initEvent = function () {

  // 点击返回主页
  $(".back").bind("click", function () {
    window.close();
  });

  courseView.timer = null;
  courseView.speed = 1000;
  courseView.time = 0;
  courseView.options = {'techOrder':['html5']};
  courseView.player = videojs("video", courseView.options, function onPlayReady(){
    videojs.log('your player is ready !!!');
    this.play();

    this.on("loadstart", function () {
      videojs.log('your player is loadstart !!!');
    });

    this.on("play", function () {
      videojs.log('your player is play !!!');
    });

    this.on("pause", function () {
      videojs.log('your player is pause !!!');
    });

    this.on("seeking", function () {
      videojs.log('your player is seeking !!!');
    });

    this.on("seeked", function () {
      videojs.log('your player is seeked !!!');
    });

    this.on("timeupdate", function () {
      // videojs.log('your player is timeupdate !!!');
    });

    this.on("ended", function () {
      videojs.log('your player is over !!!');
    });
  });

}


courseView.getCourse = function(){
        var params = {
            'courseId': courseView.courseId
        };
        $.ajax({
            url: courseView.basePath + "popup/getCourse",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var course = data['result'];
                    if(null == course || typeof (course) == 'undefined') {
                        //message.add("查询课程信息失败", "warning");
                        $("#show_list_video").empty().html("<li class=\"empty\">暂无视频</li>");
                        $("#show_list_doc").empty().html("<li class=\"empty\">暂无文档</li>");
                        return;
                    }
                    var courseName = course['varName'];
                    var courseDesc = course['varDesc'];
                    if(courseName) $("#courseName").empty().html(courseName);
                    if(courseDesc) {}$("#courseDesc").empty().html(courseDesc);

                    // 加载附件
                  courseView.getAttachment();
                }
            }
        });
    }

courseView.getAttachment = function(){
        var params = {
            'courseId': courseView.courseId
        };
        $.ajax({
            url: courseView.basePath + "popup/getAttachment",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var attachments = data['result'];
                    if(null == attachments || typeof (attachments) == 'undefined'|| attachments.length == 0) {
                        $("#show_list_video").empty().html("<li class=\"empty\">暂无视频</li>");
                        $("#show_list_doc").empty().html("<li class=\"empty\">暂无文档</li>");
                        return;
                    }
                  courseView.handlerAttachment(attachments);
                }
            }
        });
    }

courseView.handlerAttachment = function (attachments){
        var len = attachments.length;
        var attachment = new Object(), varExt = "";
        var attachment_video = [];var attachment_doc = [];
        for(var i=0; i<len; i++) {
            attachment = attachments[i];
            varExt = attachment['varExt'];
            if(varExt == 'doc' || varExt == 'docx' || varExt == 'xlsx' || varExt == 'xls' || varExt == 'ppt' || varExt == 'pptx'||varExt == 'pdf') {
                attachment_doc.push(attachment);continue;
            }
            if(varExt == 'flv' || varExt == 'mp4')  {
                attachment_video.push(attachment);continue;
            }
        }

        var inner_video = "", inner_doc = "";
        var attachment_video_length = attachment_video.length;
        var attachment_doc_length = attachment_doc.length;

        if(0 == attachment_video_length) {
            $("#show_list_video").empty().html("<li class=\"empty\">暂无视频</li>");
        } else {
            for(var j=0; j<attachment_video_length; j++) {
                inner_video += "<li><a href=\"javascript:;\" fId=\""+attachment_video[j]['varFid']+"\">"+attachment_video[j]['varLocalName']+"</a></li>";
            }
            $("#show_list_video").empty().html(inner_video);
        }

        if(0 == attachment_doc_length) {
            $("#show_list_doc").empty().html("<li class=\"empty\">暂无文档</li>");
        } else {
            for(var k=0; k<attachment_doc_length; k++) {
                inner_doc += "<li><a href=\"javascript:;\" fId=\""+attachment_doc[k]['varFid']+"\">"+attachment_doc[k]['varLocalName']+"</a></li>";
            }
            $("#show_list_doc").empty().html(inner_doc);
        }

        // 视频或者文档点击播放事件处理
        $('#show_list_video li a').each(function (){
            var $a = $(this);
            $a.bind("click", function (){
                $('#show_list_video li a').removeClass("active"); $a.addClass("active");
                var $fId = $a.attr("fId");
                courseView.playVideo($fId);
            })
        });
        $('#show_list_doc li a').each(function (){
            var $a = $(this);
                $a.bind("click", function (){
                    $('#show_list_doc li a').removeClass("active"); $a.addClass("active");
                    var $fId = $a.attr("fId");
                    courseView.playDoc($fId);
                })
        });

        // 默认播放第一个视频
        $('#show_list_video li a:eq(0)').trigger("click");
    }

courseView.playVideo = function (fId){
        // m3u8
        var params = {
            'fId': fId
        };
        $.ajax({
            url: courseView.basePath + "popup/m3u8",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var url = data.result.url;
                    url = encodeURI(url);
                    if(!url)return;
                    courseView.player.src({
                        src: url,
                        type: 'application/X-mpegURL'
                    });
                    courseView.player.play();
                }
            }
        });
    }

courseView.playDoc = function (fId){
        // m3u8
        var params = {
            'fId': fId
        };
        $.ajax({
            url: courseView.basePath + "popup/m3u8",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var url = data.result.url;
                    if(!url)return;
                    $("#doc").html('<iframe id="iframe-doc" name="pdfView" src="'+ url +'" frameborder="0" style="width: 100%;height: 100%;" scrolling="no"></iframe>');
                }
            }
        });
    }

// 檢測瀏覽器是否支持hls播放
window.hlsSupportCallback = function (flag){
  if(!flag) { // 不支持
      layer.open({
          type:1,
          title: false, // 不显示标题栏
          closeBtn: false,
          area: "600px",
          shade: 0.3,
          id: "layer-hls-support", // 设置id, 防止重复弹出
          resize: false,
          btn:["知道了"],
          btnAlign: "c",
          moveType: 1,
          content: "<div style='padding:50px;line-height:22px;background-color:#393D49;color:#fff;font-weight:300'>经系统检测,您的浏览器版本过低,无法完美支持视频播放<br>推荐您安装360安全浏览器&nbsp;&nbsp;&nbsp;<a href='//se.360.cn' target='_blank' style='color: rgb(0, 156, 75);'>点击下载</a></div>",
          yes: function(layero) {
              layer.close(layero);
          }
      });
  }
};


function feed_back() {
    var url = encodeURI(window.location.href);
    var feed_back_url =appPath + "/feedback?url=" + url;
    window.open(feed_back_url)
}

