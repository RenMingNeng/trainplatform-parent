
function msg() {
    var _this = this;
    _this.page;
    _this.page_size = 10;

    _this.init = function(page){
        _this.page = page;
        _this.initEvent();
        _this.initTable();
    };

    _this.initTable = function(){
        var list_url = appPath + "/popup/msg/list";
        _this.page.init("msg_form", list_url, "msg_table", "msg_page", 1, _this.page_size);
        _this.page.goPage(1);
        _this.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td class="warp"><div class="pull-left content"><a onclick="msg.view(this,\'' + item['msgId'] + '\')" href="javascript:void(0);';
                var status = item['status'];
                if(status == 2){
                    inner += '" style="font-weight: normal;color: #3e3e3e;">'+ item['msgTitle'] + '</a></div>';
                }else{
                    inner += '">'+ item['msgTitle'] + '</a></div>';
                }
                var isHot = item['isHot'];
                if(isHot == '1'){
                    inner += '<div class="pull-left hot"></div>';
                }
                inner += '</td><td class="time">'+ TimeUtil.longMsTimeConvertToDateTime(item['createDate']) + '</td>';
                inner += '</tr>';
            }
            return inner;
        }
    };

    _this.view = function (t,msgId) {
        $(t).css("font-weight", "normal");
        $(t).css("color", "#3e3e3e");
        window.open(appPath + '/popup/msg/view?msgId=' + msgId);
    };

    _this.ajax = function(url, param, type) {
        var result;
        $.ajax({
            url : url,
            async : false,
            type : 'post',
            data : param,
            success : function(data) {
                result = data;
            }
        });
        return result;
    };

    _this.initEvent = function() {
        $("#search").click(function () {
            _this.initTable();
        })

        $("#searchAll").click(function () {
            $("#msgTitle").val("");
            _this.initTable();
        })
    }
}
var msg = new msg();