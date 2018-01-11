/**
 * Created by Administrator on 2017/7/24.
 */

    var studentStudySelf = new Object();
    studentStudySelf.study_time = 0;
    studentStudySelf.video_id = "";
    studentStudySelf.courseNo = "";
    studentStudySelf.courseName = "";
    studentStudySelf.classHour = "";
    studentStudySelf.video_time = 0;


    // 初始化
    studentStudySelf.init = function(basePath, courseId,  userId, source){
        //console.log("basePath="+basePath);console.log("projectId="+projectId);console.log("courseId="+courseId);console.log("userId="+userId);console.log("source="+source);
        studentStudySelf.basePath = basePath;
        studentStudySelf.courseId = courseId;
        studentStudySelf.userId = userId;
        studentStudySelf.source = source;


        // 初始化事件
        studentStudySelf.initEvent();
        // 获取课程基本信息
        studentStudySelf.getCourse();
    };

    studentStudySelf.initEvent = function () {
        // studentStudySelf.messenger.addTarget(window.opener, 'parent1');

        // 点击返回主页
        $(".back").bind("click", function () {
            // send to server for mark time
            window.open('', '_self', '');
            window.opener = null;
            window.close();
        });

        studentStudySelf.timer = null;
        studentStudySelf.speed = 250;
        studentStudySelf.time = 0;
        studentStudySelf.options = {
            'techOrder':['html5'],
            controlBar : {
                volumeMenuButton : {
                    inline :false,
                    vertical : true
                }
            }
        };
        studentStudySelf.player = videojs("video", studentStudySelf.options, function(){
           videojs.log('your player is ready !!!');
            this.on("loadstart", function () {
                videojs.log('your player is loadstart !!!');
            });

            this.on("seeking", function () {
                videojs.log('your player is seeking !!!');
            });

            this.on("seeked", function () {
                videojs.log('your player is seeked !!!');
            });
            this.on('loadedalldate',function(){
                videojs.log( 'loadedalldate' );
            });

            this.on("waiting", function () {
                videojs.log('your player is waiting !!!');
            });

            this.on("timeupdate", function () {
                 videojs.log('your player is timeupdate !!!');
               /* studentStudySelf.time += 0.25;
                $("#study_time").html(studentStudySelf.formatSeconds(parseInt(studentStudySelf.time)));*/

                // var currentTime = this.currentTime(); // 获取播放进度
            });
            this.on('progress',function(){
                //videojs.log('your player is progress !!!');
            });

            this.on("ended", function () {
                videojs.log('your player is over !!!');
                // 切换到下一个视频
                message.add("温馨提示:即将自动播放下一个视频", "normal");

                var len = $("#show_list_video li a[class='active']").parent().next().length;

                if(len >0 ){
                    $("#show_list_video li a[class='active']").parent().next().find("a:eq(0)").trigger("click");
                }else{
                    $("#show_list_video li a[class='active']").parent().parent().find("li:eq(0)").find("a").trigger("click");
                }
            });




            this.on("play", function () {
                videojs.log('your player is play !!!');

            });

            this.on("pause", function () {
                videojs.log('your player is pause !!!');
            });
        });


    }

    studentStudySelf.plusTime = function () {
        if(studentStudySelf.time && studentStudySelf.time != 0){
            //关闭页面记录视频播放位置
            studentStudySelf.savePosition();
            setCookie('messenger_parent_msg', "study_refresh");
            var params = {
                'course_id': studentStudySelf.courseId,
                'course_no': studentStudySelf.courseNo,
                'course_name': studentStudySelf.courseName,
                'class_hour': studentStudySelf.classHour,
                'user_id': studentStudySelf.userId,
                'source': studentStudySelf.source,
                'study_time': parseInt(studentStudySelf.time)
            };
            $.ajax({
                url: studentStudySelf.basePath + "student/studySelf/plusTime",
                data: params,
                type: "post",
                async : false,
                dataType:"json",
                success:function(data) {
                    //console.log(data);
                },
                complete:function(){
                    // studentStudySelf.messenger.targets['parent1'].send("我特么学完了");

                    window.open('', '_self', '');
                    window.opener = null;
                    window.close();
                }
            });
        }else{
            common.study_messenger_send("study_check_is_close");
        }
    };

    studentStudySelf.getCourse = function(){
        var params = {
            'courseId': studentStudySelf.courseId
        };
        $.ajax({
            url: studentStudySelf.basePath + "popup/getCourse",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var course = data['result'];
                    if(null == course || typeof (course) == 'undefined') {
                        message.add("查询课程信息失败", "error");
                        $("#show_list_video").empty().html("<li class=\"empty\">暂无视频</li>");
                        $("#show_list_doc").empty().html("<li class=\"empty\">暂无文档</li>");
                        return;
                    }
                    var courseName = course['varName'];
                    var courseDesc = course['varDesc'];
                    var courseNo = course['varCode'];
                    var classHour = course['intClassHour'];
                    if(courseName) {
                        studentStudySelf.courseName = courseName;
                        $("#courseName").empty().html(courseName);
                    }
                    if(courseDesc) {
                        $("#courseDesc").empty().html(courseDesc);
                    }
                    if(courseNo) {
                        studentStudySelf.courseNo = courseNo;
                        $("#courseNo").empty().html(courseNo);
                    }
                    if(classHour) {
                        studentStudySelf.classHour = classHour;
                        $("#classHour").empty().html(classHour);
                    }

                    // 加载附件
                    studentStudySelf.getAttachment();
                }
            }
        });
    }

    studentStudySelf.getAttachment = function(){
        var params = {
            'courseId': studentStudySelf.courseId
        };
        $.ajax({
            url: studentStudySelf.basePath + "popup/getAttachment",
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
                    studentStudySelf.handlerAttachment(attachments);
                }
            }
        });
    }

    studentStudySelf.handlerAttachment = function (attachments){
        var len = attachments.length;
        var attachment = new Object(), varExt = "";
        var attachment_video = [];var attachment_doc = [];
        for(var i=0; i<len; i++) {
            attachment = attachments[i];
            varExt = attachment['varExt'];
            if(varExt == 'doc' || varExt == 'docx' || varExt == 'xlsx' || varExt == 'xls' || varExt == 'ppt' || varExt == 'pptx'|| varExt == 'pdf') {
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
                inner_video += "<li><a href=\"javascript:;\" videoId=\""+attachment_video[j]['intId']+"\" fId=\""+attachment_video[j]['varFid']+"\">"+attachment_video[j]['varLocalName']+"</a></li>";
            }
            $("#show_list_video").empty().html(inner_video);
        }

        if(0 == attachment_doc_length) {
            $("#show_list_doc").empty().html("<li class=\"empty\">暂无文档</li>");
        } else {
            for(var k=0; k<attachment_doc_length; k++) {
                inner_doc += "<li><a href=\"javascript:;\" videoId=\""+attachment_doc[k]['intId']+"\" fId=\""+attachment_doc[k]['varFid']+"\">"+attachment_doc[k]['varLocalName']+"</a></li>";
            }
            $("#show_list_doc").empty().html(inner_doc);
        }

        // 视频或者文档点击播放事件处理
        $('#show_list_video li a').each(function (){
            var $a = $(this);

            $a.bind("click", function (){
                //记录视频播放位置
                studentStudySelf.savePosition();
                $('#show_list_video li a').removeClass("active"); $a.addClass("active");
                var $fId = $a.attr("fId");
                var $videoId = $a.attr("videoId");
                studentStudySelf.video_id = $videoId;

                setTimeout(function() {
                    studentStudySelf.playVideo($fId);
                },300);

            })
        });
        $('#show_list_doc li a').each(function (){
            var $a = $(this);
                $a.bind("click", function (){
                    $('#show_list_doc li a').removeClass("active"); $a.addClass("active");
                    var $fId = $a.attr("fId");
                    var $videoId = $a.attr("videoId");
                    studentStudySelf.video_id = $videoId;
                    studentStudySelf.playDoc($fId);
                })
        });

        // 默认播放第一个视频
        $('#show_list_video li a:eq(0)').trigger("click");
    }


    studentStudySelf.playVideo = function (fId){
        // m3u8
        var params = {
            'fId': fId,
            'course_id': studentStudySelf.courseId,
            'user_id': studentStudySelf.userId,
            'video_id': studentStudySelf.video_id
        };
        $.ajax({
            url: studentStudySelf.basePath + "popup/m3u8",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){

                    var url = data.result['url'];
                    var position = data.result['lastPosition'];
                    url = encodeURI(url);
                    if(!url)return;

                   studentStudySelf.player.src({
                        src: url,
                        type: 'application/X-mpegURL'
                    });
                    studentStudySelf.player.play();
                    //视频从上次位置开始播放
                    if(position != 0){
                        setTimeout(function(){
                            studentStudySelf.player.currentTime(parseInt(position));
                        },300);

                    }
                    setTimeout(function(){
                        studentStudySelf.video_time = parseInt(studentStudySelf.player.duration());
                    },300);
                }
            }
        });
    }

    studentStudySelf.playDoc = function (fId){
        // m3u8
        var params = {
            'fId': fId
        };
        $.ajax({
            url: studentStudySelf.basePath + "popup/m3u8",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var url = data.result['url'];
                    url = encodeURI(url);
                    if(!url)return;
                    $("#doc").html('<iframe id="iframe-doc" name="pdfView" src="'+ url +'" frameborder="0" style="width: 100%;height: 100%;" scrolling="no"></iframe>');
                }
            }
        });
    }

    studentStudySelf.formatSeconds = function (a){
        if(0 >=a)
            return "00时" + "00分" + "00秒";
        var hh = parseInt(a/3600);
        if(hh < 10) hh = "0" + hh;
        var mm = parseInt((a - hh*3600)/60);
        if(mm < 10) mm = "0" + mm;
        var ss = parseInt((a - hh*3600)%60);
        if(ss < 10) ss = "0" + ss;
        var result = hh + "时" + mm + "分" + ss + "秒";
        if(a > 0)
            return result;
        return "00时" + "00分" + "00秒";
    }

    // 檢測瀏覽器是否支持hls播放
    window.hlsSupportCallback = function (flag){
        if(!flag) { // 不支持
            studentStudySelf.player.pause();
            clearInterval(studentStudySelf.timer);
            $("#study_time").html(studentStudySelf.formatSeconds(0));
            studentStudySelf.time = 0;
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
/*
* 通过video。js库来做计时功能
* */

window.studentStudyTime = function (time) {
    studentStudySelf.time = time;
    if( time !== 0 && (time % 300) === 0 ){
        studentStudySelf.player.pause();
        var index = layer.alert('您已经五分钟没有操作了，请确定是否继续学习 :-D',{title:'温馨提示:'},function(){
            studentStudySelf.player.play();
            layer.close( index );
        })
    }
    $("#study_time").html(studentStudySelf.formatSeconds(parseInt(time)));
}

//记录视频播放位置
studentStudySelf.savePosition = function () {
    $('#show_list_video li a').each(function (){
        if($(this).hasClass('active')){
            //视频播放进度
            var last_position = studentStudySelf.player.currentTime();
            var $last_position = studentStudySelf.video_time - last_position;

            if($last_position <= 1){
                last_position = 0;
            }
            var param = {
                'course_id': studentStudySelf.courseId,
                'user_id': studentStudySelf.userId,
                'video_id': $(this).attr("videoId"),
                'study_time': parseInt(last_position)
            };

            saveVideoPosition(param);
        }
    })
}


//获取视频播放位置
studentStudySelf.getPosition = function (fid) {
    var param = {
        'course_id': studentStudySelf.courseId,
        'user_id': studentStudySelf.userId,
        'video_id': studentStudySelf.video_id,
        'fid': fid
    };
    getVideoPosition(param);
}

function feed_back() {
    var url = encodeURI(window.location.href);
    var feed_back_url =appPath + "/feedback?url=" + url;
    window.open(feed_back_url)
}