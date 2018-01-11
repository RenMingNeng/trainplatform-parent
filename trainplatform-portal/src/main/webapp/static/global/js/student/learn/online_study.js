/**
 * Created by Administrator on 2017/7/24.
 */

    var onlineStudy = new Object();
    onlineStudy.study_time = 0;
    onlineStudy.video_id = "";
    onlineStudy.video_time = 0;
    // 初始化
    onlineStudy.init = function(basePath, projectId,  userId, source){
        //console.log("basePath="+basePath);console.log("projectId="+projectId);console.log("courseId="+courseId);console.log("userId="+userId);console.log("source="+source);
        onlineStudy.basePath = basePath;
        onlineStudy.projectId = projectId;
        onlineStudy.userId = userId;
        onlineStudy.source = source;


        // 初始化事件
        onlineStudy.initEvent();
        // 获取项目中的所有课程
        onlineStudy.getCourses();
        // 获取课程基本信息
        //onlineStudy.getCourse();
    };

    onlineStudy.initEvent = function () {
        // onlineStudy.messenger.addTarget(window.opener, 'parent1');

        // 点击返回主页
        $(".back").bind("click", function () {


            // send to server for mark time
            window.open('', '_self', '');
            window.opener = null;
            window.close();
        });

        onlineStudy.timer = null;
        onlineStudy.speed = 250;
        onlineStudy.time = 0;
        onlineStudy.options = {
            'techOrder':['html5'],
            controlBar : {
                volumeMenuButton : {
                    inline :false,
                    vertical : true
                }
            }
        };
        onlineStudy.player = videojs("video", onlineStudy.options, function(){
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
               /* onlineStudy.time += 0.25;
                $("#study_time").html(onlineStudy.formatSeconds(parseInt(onlineStudy.time)));*/

                // var currentTime = this.currentTime(); // 获取播放进度
            });
            this.on('progress',function(){
                //videojs.log('your player is progress !!!');
            });

            this.on("ended", function () {
                videojs.log('your player is over !!!');
                // 切换到下一个视频
                message.add("温馨提示:即将自动播放下一个视频", "normal");


                $('#show_list_course li a i').each(function (index,element) {
                    var flag = $(this).hasClass('fa-minus');
                    if(flag){

                        var $li = $(this).parent().parent();
                        var $a = $(this).parent();

                        var $file_a = $li.find("div ul li a[class='active']");
                        var len = $file_a.parent().next().length;
                        //该课程下有其他视频直接播放
                        if(len>0){
                            $file_a.parent().next().find("a").trigger("click");
                        }else{//没有其他视频，直接切到下一个课程

                             var $li_len_next = $li.next().length;
                             var $li_len_prev = $li.prev().length;

                             if($li_len_next >0){//有课程直接播放
                            $li.next().find("a").trigger("click");
                             }else if($li_len_next == 0 && $li_len_prev > 0){
                                 $li.parent().find("li:eq(0)").find("a:eq(0)").trigger("click");
                             }else{
                                 //没有其他课程，重复播放当前视频
                                 $file_a.trigger("click");
                             }
                        }
                        return false;

                    }
                })

            });




            this.on("play", function () {
                videojs.log('your player is play !!!');

            });

            this.on("pause", function () {
                videojs.log('your player is pause !!!');
            });
        });


    }

    onlineStudy.plusTime = function (oper) {

        if(onlineStudy.time && onlineStudy.time != 0){
            //关闭页面记录视频播放位置
            onlineStudy.savePosition();

            setCookie('messenger_parent_msg', "study_refresh");
            var  courseId = $("#courseId").val();
            var params = {
                'project_id': onlineStudy.projectId,
                'course_id': courseId,
                'user_id': onlineStudy.userId,
                'source': onlineStudy.source,
                'study_time':parseInt(onlineStudy.time)- parseInt(onlineStudy.study_time)
            };
            $.ajax({
                url: onlineStudy.basePath + "student/study/plusTime",
                data: params,
                type: "post",
                async : false,
                dataType:"json",
                success:function(data) {
                    onlineStudy.study_time = onlineStudy.time;
                    //console.log(data);
                },
                complete:function(){

                    // onlineStudy.messenger.targets['parent1'].send("我特么学完了");
                if(oper != "add"){
                    window.open('', '_self', '');
                    window.opener = null;
                    window.close();
                }

                }
            });
        }else{
            if(oper != "add") {
                common.study_messenger_send("study_check_is_close");
            }
        }
    };

    //获取项目中的所有课程
    onlineStudy.getCourses = function(){
        var params = {
            'projectId': onlineStudy.projectId,
            'userId' : onlineStudy.userId
        };
        $.ajax({
            url: appPath + "/student/learn/getCourses",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                   var result = data.result;
                   var inner = ""
                   if(null == result){
                       inner =  "<li class=\"empty\">暂无课程</li>"
                   }else{
                       var len = result.length;
                       for(var i = 0; i<len ;i++){
                           var item = result[i];
                          inner += ' <li><a href="javascript:;" data-courseName="'+ item.courseName +'" data-courseId = "'+ item.courseId +'" data-varDesc = "'+ item.varDesc +'"><i class="fa fa-plus"></i>'+(i+1) + ". " +item.courseName +'</a></li>';
                       }

                   }
                   $("#show_list_course").empty().html(inner);
                    // 默认点击第一个课程
                    $('#show_list_course > li > a:eq(0)').trigger("click");


                }
            }
        });
    }

 /*   //获取单个课程的信息
    onlineStudy.getCourse = function(){
        var params = {
            'courseId': onlineStudy.courseId
        };
        $.ajax({
            url: onlineStudy.basePath + "popup/getCourse",
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
                    if(courseName) $("#courseName").empty().html(courseName);
                    if(courseDesc) {}$("#courseDesc").empty().html(courseDesc);

                    // 加载附件
                    //onlineStudy.getAttachment();
                }
            }
        });
    }*/

    //获取课程附件
    onlineStudy.getAttachment = function(obj,courseId){
        var params = {
            'courseId': courseId
        };
        $.ajax({
            url: onlineStudy.basePath + "popup/getAttachment",
            data: params,
            type: "post",
            dataType:"json",
            success:function(data) {
                //console.log(data);
                var code = data.code;
                if("10000" == code){
                    var attachments = data['result'];
                    if(null == attachments || typeof (attachments) == 'undefined'|| attachments.length == 0) {
                        var inner = "<div style='display: block;'><ul><li class=\"empty\">暂无视频或文档</li></ul></div>";
                        $(obj).after(inner);
                        return;
                    }
                    onlineStudy.handlerAttachment(obj,attachments);
                }
            }
        });
    }

    onlineStudy.handlerAttachment = function (obj,attachments){
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

        var inner = "<div style='display: block;'><ul>", inner_doc = "";
        var attachment_video_length = attachment_video.length;
        var attachment_doc_length = attachment_doc.length;

        if(0 == attachment_video_length && 0 == attachment_doc_length) {
            inner += "<div><ul><li class=\"empty\">暂无视频</li>"
        } else {
            for(var j=0; j<attachment_video_length; j++) {
                inner += "<li><a href=\"javascript:;\" type='video' videoId=\""+attachment_video[j]['intId']+"\" fId=\""+attachment_video[j]['varFid']+"\">"+attachment_video[j]['varLocalName']+"</a></li>";
            }
            for(var k=0; k<attachment_doc_length; k++) {
                inner += "<li><a href=\"javascript:;\" type='doc' videoId=\""+attachment_doc[k]['intId']+"\" fId=\""+attachment_doc[k]['varFid']+"\">"+attachment_doc[k]['varLocalName']+"</a></li>";
            }


        }
        inner += "</ul></div>";
        $(obj).next("div").remove();
        $(obj).after(inner);

        var $as = $(obj).next().find("ul li a");

        // 视频或者文档点击播放事件处理
        $($as ).each(function (){
            var $a = $(this);
            $a.bind("click", function (){

                var $fId = $a.attr("fId");
                var type = $a.attr("type")
                var $videoId = $a.attr("videoId");
                onlineStudy.video_id = $videoId;
                 //记录视频播放位置
                onlineStudy.savePosition($as);
                if(type == 'video'){

                    $('#show_list_course>li>div>ul>li>a').removeClass("active");

                    $a.addClass("active");
                    $("#doc").addClass("nav");
                    $("#video").removeClass("nav");
                  setTimeout(function () {
                      onlineStudy.playVideo($fId);
                  },300);

                }else if(type == 'doc'){
                    $('#show_list_course>li>div>ul>li>a').removeClass("active");
                    onlineStudy.player.pause();
                    $a.addClass("active");
                    $("#video").addClass("nav");
                    $("#doc").removeClass("nav");
                    onlineStudy.playDoc($fId);
                }
            })
        });

        /*$('#show_list_doc li a').each(function (){
            var $a = $(this);
                $a.bind("click", function (){
                    $('#show_list_doc li a').removeClass("active"); $a.addClass("active");
                    var $fId = $a.attr("fId");
                    onlineStudy.playDoc($fId);
                })
        });*/
        var $afirst = $(obj).next().find("ul li a:eq(0)");
        console.log($afirst)
        // 默认播放第一个视频
       $($afirst).trigger("click");
    }

    onlineStudy.playVideo = function (fId){
        $("#doc").addClass("nav");
        var  courseId = $("#courseId").val();
        // m3u8
        var params = {
            'fId': fId,
            'course_id': courseId,
            'user_id':  onlineStudy.userId,
            'video_id': onlineStudy.video_id
        };
        $.ajax({
            url: onlineStudy.basePath + "popup/m3u8",
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

                    onlineStudy.player.src({
                        src: url,
                        type: 'application/X-mpegURL'
                    });
                    onlineStudy.player.play();
                    //视频从上次位置开始播放
                    if(position != 0){
                        setTimeout(function(){
                            onlineStudy.player.currentTime(parseInt(position));
                        },300);

                    }

                    setTimeout(function(){
                        onlineStudy.video_time = parseInt(onlineStudy.player.duration());
                    },300);
                }
            }
        });
    }

    onlineStudy.playDoc = function (fId){

        // m3u8
        var params = {
            'fId': fId
        };
        $.ajax({
            url: onlineStudy.basePath + "popup/m3u8",
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

    onlineStudy.formatSeconds = function (a){
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
            onlineStudy.player.pause();
            clearInterval(onlineStudy.timer);
            $("#study_time").html(onlineStudy.formatSeconds(0));
            onlineStudy.time = 0;
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

window.studentStudyTime = function (time) {
    onlineStudy.time = time;

    if( time !== 0 && (time % 300) === 0 ){
        onlineStudy.player.pause();
        var index = layer.alert('您已经五分钟没有操作了，请确定是否继续学习 :-D',{title:'温馨提示:'},function(){
            onlineStudy.player.play();
            layer.close( index );
        })
    }
    $("#study_time").html(onlineStudy.formatSeconds(parseInt(time)));
}

/*
onlineStudy.courseHandle = function (obj) {
    var $ul = $(obj).parent().parent();
    var $a  = $ul.find('i');
    console.log( $a[0] )
    var $li = $(obj).parent();
    var flag = $li.find("div").hasClass("nav");
    if(flag){

        $a.addClass('fa-minus');
        $ul.find("li").each(function (index,element) {
            $(this).find("div").addClass("nav");
        })
        $li.find("div").removeClass("nav");
    }else{
        $a.removeClass('fa-minus');
        $li.find("div").addClass("nav");
        $ul.find("li").each(function (index,element) {
            $(this).find("div").addClass("nav");
        })
    }
}*/
$('#show_list_course').delegate('>li>a','click',function(e){






    var $this = $(this);
    var $i = $this.find('i');
    var $p = $this.parent();
    var $sib = $p.find('div');
    var $sibHtml = $sib.html();
    var courseId = $this.attr("data-courseId");
    var courseName = $this.attr("data-courseName");
    var courseDesc = $this.attr("data-vardesc");

    var element = e.target.nodeName;
    if(element.toLowerCase() == 'i'){  //i标签直接展开
        var flag = $i.hasClass('fa-minus');
        if(flag){
            $sib.hide();
            $i.removeClass('fa-minus');
        }else{
            $i.addClass('fa-minus');
            $sib.slideUp();
            //获取附件
            onlineStudy.getAttachment1(this,courseId);
        }
    }else{  //a标签直接点击

        //切换到下一个课程之前把当前课程学时添加数据库
        $('#show_list_course li a ').each(function (index,element) {
            if($(this).hasClass('plusTime')){
                var oper = "add";
                onlineStudy.plusTime(oper);

            }
        })
        //将课程编号放到隐藏域方便以便pulsTime方法取值
        $("#courseId").val($(this).attr("data-courseId"));

        if(courseName) $("#courseName").empty().html(courseName);
        if(courseDesc=="undefined" || courseDesc =="") {
            courseDesc = "暂无简介";
        }
        $("#courseDesc").empty().html(courseDesc);


        $('#show_list_course>li>div').slideUp();
    $('#show_list_course>li>a>i').removeClass('fa-minus');
    var flag = $i.hasClass('fa-minus');
    $(this).addClass("plusTime");

    if( !flag ){
      $i.addClass('fa-minus');

        if( $sibHtml === '' || $sibHtml === undefined ){

            //获取附件
            onlineStudy.getAttachment(this,courseId);
        }else{
            $sib.slideDown();
            //$p.find("ul li a:eq(0)").trigger("click");
            $sib.find("ul li a:eq(0)").trigger("click");
        }
    }
        //$sib.find("ul li a:eq(0)").trigger("click");



    console.log(flag)

    }
})

/*$('#show_list_course').delegate('>li>a>i','click',function(){
    var flag = $(this).hasClass('fa-minus');
    var $a = $(this).parent();
    var $sib = $(this).parent().find("div");
    if(flag){
        $sib.slideDown();
    }else{
        $(this).addClass('fa-minus');
        var courseId = $a.attr("data-courseId");
        $sib.slideUp();
        //获取附件
        onlineStudy.getAttachment($a,courseId);
    }
});*/



//记录视频播放位置
onlineStudy.savePosition = function () {
    $('#show_list_course>li>div>ul>li>a').each(function (){
        if($(this).hasClass('active')){
            var type = $(this).attr("type");
            if(type == "doc"){
                return;
            }
            //视频播放进度
            var last_position = onlineStudy.player.currentTime();
            var $last_position = onlineStudy.video_time - last_position;

            if($last_position <= 1){
                last_position = 0;
            }
            var courseId = $(this).parent().parent().parent().prev().attr("data-courseId");
            var videoId= $(this).attr("videoId");

            var param = {
                'course_id': courseId,
                'video_id': videoId,
                'study_time': parseInt(last_position)
            };
           //保存视频播放位置
            saveVideoPosition(param);
        }
    })
}
function feed_back() {
    var url = encodeURI(window.location.href);
    var feed_back_url = appPath + "/feedback?url=" + url;
    window.open(feed_back_url)
}



//获取课程附件
onlineStudy.getAttachment1 = function(obj,courseId){
    var params = {
        'courseId': courseId
    };
    $.ajax({
        url: onlineStudy.basePath + "popup/getAttachment",
        data: params,
        type: "post",
        dataType:"json",
        success:function(data) {
            //console.log(data);
            var code = data.code;
            if("10000" == code){
                var attachments = data['result'];
                if(null == attachments || typeof (attachments) == 'undefined'|| attachments.length == 0) {
                    var inner = "<div style='display: block;'><ul><li class=\"empty\">暂无视频或文档</li></ul></div>";
                    $(obj).after(inner);
                    return;
                }
                onlineStudy.handlerAttachment1(obj,attachments);
            }
        }
    });
}

onlineStudy.handlerAttachment1 = function (obj,attachments){
    var len = attachments.length;
    var attachment = new Object(), varExt = "";
    var attachment_video = [];var attachment_doc = [];
    for(var i=0; i<len; i++) {
        attachment = attachments[i];
        varExt = attachment['varExt'];
        if(varExt == 'doc' || varExt == 'docx' || varExt == 'xlsx' || varExt == 'xls' || varExt == 'ppt' || varExt == 'pptx' || varExt == 'pdf') {
            attachment_doc.push(attachment);continue;
        }
        if(varExt == 'flv' || varExt == 'mp4')  {
            attachment_video.push(attachment);continue;
        }
    }

    var inner = "<div style='display: block;'><ul>", inner_doc = "";
    var attachment_video_length = attachment_video.length;
    var attachment_doc_length = attachment_doc.length;

    if(0 == attachment_video_length && 0 == attachment_doc_length) {
        inner += "<div><ul><li class=\"empty\">暂无视频</li>"
    } else {
        for(var j=0; j<attachment_video_length; j++) {
            inner += "<li><a href=\"javascript:;\" type='video' videoId=\""+attachment_video[j]['intId']+"\" fId=\""+attachment_video[j]['varFid']+"\">"+attachment_video[j]['varLocalName']+"</a></li>";
        }
        for(var k=0; k<attachment_doc_length; k++) {
            inner += "<li><a href=\"javascript:;\" type='doc' videoId=\""+attachment_doc[k]['intId']+"\" fId=\""+attachment_doc[k]['varFid']+"\">"+attachment_doc[k]['varLocalName']+"</a></li>";
        }


    }
    inner += "</ul></div>";
    $(obj).next("div").remove();
    $(obj).after(inner);

    var $as = $(obj).next().find("ul li a");

    // 视频或者文档点击播放事件处理
    $($as ).each(function (){
        var $a = $(this);
        $a.bind("click", function (){

            var $fId = $a.attr("fId");
            var type = $a.attr("type")
            var $videoId = $a.attr("videoId");
            onlineStudy.video_id = $videoId;
            //记录视频播放位置
            onlineStudy.savePosition($as);
            if(type == 'video'){

                $('#show_list_course>li>div>ul>li>a').removeClass("active");

                $a.addClass("active");
                $("#doc").addClass("nav");
                $("#video").removeClass("nav");
                setTimeout(function () {
                    onlineStudy.playVideo($fId);
                },300);

            }else if(type == 'doc'){
                $('#show_list_course>li>div>ul>li>a').removeClass("active");
                onlineStudy.player.pause();
                $a.addClass("active");
                $("#video").addClass("nav");
                $("#doc").removeClass("nav");
                onlineStudy.playDoc($fId);
            }
        })
    });

    /*$('#show_list_doc li a').each(function (){
     var $a = $(this);
     $a.bind("click", function (){
     $('#show_list_doc li a').removeClass("active"); $a.addClass("active");
     var $fId = $a.attr("fId");
     onlineStudy.playDoc($fId);
     })
     });*/
}
