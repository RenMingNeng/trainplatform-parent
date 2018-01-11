/**
 * Created by Administrator on 2017/7/24.
 */

   //保存视频播放位置
   function saveVideoPosition(param) {

    $.ajax({
        url: appPath + "/student/videoPosition/savePosition",
        data: param,
        type: "post",
        dataType:"json",
        success:function(data) {
            //console.log(data);
        },
        complete:function(){
            // studentStudy.messenger.targets['parent1'].send("我特么学完了");
        }
    });
   }



   function getVideoPosition(param) {
       var last_position = 0;
       $.ajax({
           url: appPath + "/student/videoPosition/getPosition",
           data: param,
           type: "post",
           dataType:"json",
           success:function(data) {

               //console.log(data);

                   last_position = data;


           },
           complete:function(){
               // studentStudy.messenger.targets['parent1'].send("我特么学完了");
           }
       });
       return last_position;
   }



