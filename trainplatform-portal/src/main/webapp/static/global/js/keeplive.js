/**
 * 保活 keeplive
 */
var keeplive = function (){
    $.ajax({
        url: appPath + "/keeplive",
        data: {},
        type: "post",
        dataType:"json",
        success:function(data) {
            console.log(data);
        }
    });
};

self.setInterval("keeplive()", 1000 * 60 *5);