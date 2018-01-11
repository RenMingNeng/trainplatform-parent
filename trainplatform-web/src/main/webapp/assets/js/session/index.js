function Session(){
    var _this = this;
    _this.init = function () {
        _this.list();
        _this.initEvent();
    }

    _this.initEvent = function (){
        $(".sub-nav-ul li").each(function (){
            var $li = $(this);
                $li.unbind("click").bind("click", function () {
                    $(".sub-nav-ul li a").removeClass("sub-nav-hover");
                    $li.find(".sub-nav-items").addClass("sub-nav-hover");
                    var type = $li.find(".sub-nav-items").attr("type");
                    if("1" == type) {
                        $("#session-table-1").show();
                        $("#session-table-2").hide();
                        $("#session-table-3").hide();
                    } else if("2" == type) {
                        $("#session-table-1").hide();
                        $("#session-table-2").show();
                        $("#session-table-3").hide();
                    } else if("3" == type) {
                        $("#session-table-1").hide();
                        $("#session-table-2").hide();
                        $("#session-table-3").show();
                    }
                });
        });
        $("#session-table-1").show();
        $("#session-table-2").hide();
        $("#session-table-3").hide();
    };

    _this.list = function(){
        var params = {};
        var url = appPath + "/session/list";
        $.ajax({
            url: url,
            data: params,
            type: "post",
            dataType:"json",
            success:function(data){
                console.log(data);
                var result = data['result'];
                if(!result) {
                    alert("<tr><td>暂无数据</td></tr>"); return;
                }
                var sessions_1 = result['sessions_1'];
                var sessions_2 = result['sessions_2'];
                var sessions_3 = result['sessions_3'];
                var len_sessions_1 = sessions_1.length;
                var len_sessions_2 = sessions_2.length;
                var len_sessions_3 = sessions_3.length;
                $(".sub-nav-ul li a[type='1']").next().text(len_sessions_1);
                $(".sub-nav-ul li a[type='2']").next().text(len_sessions_2);
                $(".sub-nav-ul li a[type='3']").next().text(len_sessions_3);
                var inner = "";
                var item = "";
                // 组装数据
                for(var i=0; i< len_sessions_1; i++) {
                    item = sessions_1[i];
                    inner += "<div class=\"title tr clearfix\">";
                    inner += "<div style=\"line-height: 30px;\">";
                    inner += "<span class=\"lk-checkbox\" style=\"vertical-align: -3px;\"></span>";
                    inner += "<span style=\"color: #4b586a;\"><span style=\"font-weight: 600;\">"+item['id']+"</span></span>";
                    inner += "<span style=\"margin-left: 20px;\">最初访问时间 :"+TimeUtil.formatterDateTime(new Date(item['startTimestamp']))+"</span>";
                    inner += "<span style=\"margin-left: 20px;\">最后访问时间 :"+TimeUtil.formatterDateTime(new Date(item['lastAccessTime']))+"</span>";
                    inner += "<p style='word-break: break-all;'>"+_this.toJSON(item)+"</p>";
                    inner += "</div>";
                    inner += "<div>";
                    inner += "<a href=\"javascript:void(0);\" class=\"sms-section5-link\" onclick=\"user.update_valid('1','" + item['id'] + "');\">启用</a>";
                    inner += "</div>";
                    inner += "</div>";
                }
                // 渲染表单
                $("#session-table-1 > div:not(':first')").remove();
                $("#session-table-1").append(inner);

                inner = "";
                for(var i=0; i< len_sessions_2; i++) {
                    item = sessions_2[i];
                    inner += "<div class=\"title tr clearfix\">";
                    inner += "<div style=\"line-height: 30px;\">";
                    inner += "<span class=\"lk-checkbox\" style=\"vertical-align: -3px;\"></span>";
                    inner += "<span style=\"color: #4b586a;\"><span style=\"font-weight: 600;\">"+item['id']+"</span></span>";
                    inner += "<span style=\"margin-left: 20px;\">最初访问时间 :"+TimeUtil.formatterDateTime(new Date(item['startTimestamp']))+"</span>";
                    inner += "<span style=\"margin-left: 20px;\">最后访问时间 :"+TimeUtil.formatterDateTime(new Date(item['lastAccessTime']))+"</span>";
                    inner += "<p style='word-break: break-all;'>"+_this.toJSON(item['attributes']['shiro_session_attr_online_user'])+"</p>";
                    inner += "</div>";
                    inner += "<div>";
                    inner += "<a href=\"javascript:void(0);\" class=\"sms-section5-link\" onclick=\"user.update_valid('1','" + item['id'] + "');\">启用</a>";
                    inner += "</div>";
                    inner += "</div>";
                }
                // 渲染表单
                $("#session-table-2 > div:not(':first')").remove();
                $("#session-table-2").append(inner);

                inner = "";
                for(var i=0; i< len_sessions_3; i++) {
                    item = sessions_3[i];
                    inner += "<div class=\"title tr clearfix\">";
                    inner += "<div style=\"line-height: 30px;\">";
                    inner += "<span class=\"lk-checkbox\" style=\"vertical-align: -3px;\"></span>";
                    inner += "<span style=\"color: #4b586a;\"><span style=\"font-weight: 600;\">"+item['id']+"</span></span>";
                    inner += "<span style=\"margin-left: 20px;\">最初访问时间 :"+TimeUtil.formatterDateTime(new Date(item['startTimestamp']))+"</span>";
                    inner += "<span style=\"margin-left: 20px;\">最后访问时间 :"+TimeUtil.formatterDateTime(new Date(item['lastAccessTime']))+"</span>";
                    inner += "<p style='word-break: break-all;'>"+_this.toJSON(item)+"</p>";
                    inner += "</div>";
                    inner += "<div>";
                    inner += "<a href=\"javascript:void(0);\" class=\"sms-section5-link\" onclick=\"user.update_valid('1','" + item['id'] + "');\">启用</a>";
                    inner += "</div>";
                    inner += "</div>";
                }
                // 渲染表单
                $("#session-table-3 > div:not(':first')").remove();
                $("#session-table-3").append(inner);
            }
        });
    };

    //修改状态
    _this.update_valid = function(isValid, userId){
        var params = {"isValid" : isValid, "userId" : userId};
        var url = appPath + '/user/update_valid';
        var data = _this.ajax(url, params);
        var result = data['code'];
        if(result == '10000'){
            alert('操作成功');
            _this.goPage(1);
        }
    };

    _this.toJSON = function (obj){
        return JSON.stringify(obj);
    };

    _this.ajax = function(url, param){
        var result = '';
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function(data){
                result = data;
            }
        });
        return result;
    };
};

var session = new Session();
