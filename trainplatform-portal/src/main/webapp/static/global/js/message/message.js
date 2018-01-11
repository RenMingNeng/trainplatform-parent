
function message() {
    var _this = this;
    _this.page;
    _this.page_size = 10;

    _this.init = function(page){
        _this.page = page;
        _this.initTable();

        $("#message_status span").click(function () {
            $(this).siblings("span").removeClass("active");
            $(this).addClass("active");
            var value = $(this).attr("data-value");
            $("input[name=messageStatus]").val(value);
            _this.page.goPage(1);
        });

        $("#message_table").on("click", ".agree_yes", function () {
            if($(this).attr("href")){
                var id = $(this).attr("messageid");
                var pid = $(this).attr("pid");
                var params = {"id": id, "pid": pid, "isTrue": "true"};
                var url = appPath + "/admin/dispose_message";
                var data = _this.ajax(url, params);
                if(data['code'] == 10000){
                    layer.msg("处理成功");
                    _this.page.refresh_cur_page();
                }
            }
        });
        $("#message_table").on("click", ".agree_no", function () {
            if($(this).attr("href")){
                var id = $(this).attr("messageid");
                var params = {"id": id};
                var url = appPath + "/admin/dispose_message";
                var data = _this.ajax(url, params);
                if(data['code'] == 10000){
                    layer.msg("处理成功");
                    _this.page.refresh_cur_page();
                }
            }
        });

        //同意变更公司
        $("#message_table").on("click", ".move_yes", function () {
            if($(this).attr("href")){
                var messageId = $(this).attr("messageid");
                var companyId = $(this).attr("companyId");
                var userId = $(this).attr("userId");
                var userName = $(this).attr("userName");
                var params = {"messageId": messageId, "companyId": companyId, "userId": userId,'userName':userName};
                var url = appPath + "/move_to_new_company";
                var data = _this.ajax(url, params);
                if(data['code'] == 10000){
                    layer.msg("处理成功");
                    _this.page.refresh_cur_page();
                }
            }
        });
        $("#message_table").on("click", ".move_no", function () {
            if($(this).attr("href")){
                var messageId = $(this).attr("messageid");
                var params = {"messageId": messageId};
                var url = appPath + "/move_to_new_company";
                var data = _this.ajax(url, params);
                if(data['code'] == 10000){
                    layer.msg("处理成功");
                    _this.page.refresh_cur_page();
                }
            }
        });

        $("#message_table").on("click", ".a-publish", function () {
            var id = $(this).attr("messageid");
            var params = {"id": id};
            var url = appPath + "/message/updateStatus";
            var data = _this.ajax(url, params);
            if(data['code'] == 10000){
                layer.msg("处理成功");
                _this.page.refresh_cur_page();
            }
        });
    };

    _this.initTable = function(){
        var list_url = appPath + "/message/list";
        _this.page.init("message_form", list_url, "message_table", "message_page", 1, _this.page_size);
        _this.page.goPage(1);
        _this.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td>'+ item['messageTitle'] + '</td>';
                inner += '<td>'+ item['messageContent'] + '</td>';
                inner += '<td>'+ item['sendName'] + '</td>';
                inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['createTime']) + '</td>';
                var messageStatus = item['messageStatus'] == "1" ? "未读" : "已读";
                inner += '<td>'+ messageStatus + '</td>';

                inner += '<td><a href="javascript:void(0);" class="a';
                if(item['messageStatus'] == "1"){
                    inner += ' a-publish" messageid="'+ item['id'] + '">标为已读</a></td>';
                }else{
                    inner += '">已预览</a></td>';
                }
                inner += '</tr>';
            }
            return inner;
        }
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
}
var message = new message();


function feedback() {
    var _this = this;
    _this.page;
    _this.page_size = 10;

    _this.init = function(page){
        _this.page = page;
        _this.initTable();

        $("#problem_type span").click(function () {
            $(this).siblings("span").removeClass("active");
            $(this).addClass("active");
            var value = $(this).attr("data-value");
            $("input[name=problemType]").val(value);
            _this.page.goPage(1);
        });
    };

    _this.initTable = function(){
        var list_url = appPath + "/feedback/list";
        _this.page.init("feedback_form", list_url, "feedback_table", "feedback_page", 1, _this.page_size);
        _this.page.goPage(1);
        _this.page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td><div style="word-break: break-all;">'+ item['content'] + '</div></td>';
                inner += '<td>'+ item['problemType'] + '</td>';
                inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['createTime']) + '</td>';
                inner += '<td>';
                var attachmentLMap = item['attachmentLMap'];
                for(var j=0; j< attachmentLMap.length; j++) {
                    var id = attachmentLMap[j]['gridFSFileId'];
                    var name = attachmentLMap[j]['originalFilename'];
                    inner += '<a target="_blank" href="' + appPath + '/attachment/' + id + '" class="a a-attachment">' + name + '</a>';
                }
                inner += '</td>';
                inner += '</tr>';
            }
            return inner;
        }
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
}
var feedback = new feedback();