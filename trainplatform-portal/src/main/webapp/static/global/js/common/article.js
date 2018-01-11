/**
 * Created by Administrator on 2017/9/4.
 */

    var article = new Object();

    article.init = function () {

        var params = {};
        $.ajax({
            url: appPath + "/article/top",
            data: params,
            type: "post",
            dataType:"json",
            success: function(data) {
                var code = data.code;
                if("10000" == code){
                    var articles = data.result;
                    if(!articles) {
                        $("#article-dl").append("<dd><a href='javascript:;'>暂无公告</a></dd>");
                        return
                    };
                    var len = articles.length;
                    var item = null;
                    var inner = "";
                    var href = "";
                    for(var index = 0; index < len; index++) {
                        item = articles[index];
                        href = appPath + "/article/" + item.id;
                        inner += "<dd><a target='_blank' href=\"" + href + "\">" + (index+1) + "、" + item.title + "<span class='hot'></span></a></dd>";
                    }
                    // 渲染数据
                    $("#article-dl").append(inner);
                    // 轮播效果
                    var num = 0;
                    var len = $('dd').length;
                    setInterval(function(){
                        num++;
                        if( num >= len ){
                            num = 0;
                        }
                        $('dd').hide();
                        $('dd').eq(num).show();
                    },3000)
                }
            }
        });
    };
