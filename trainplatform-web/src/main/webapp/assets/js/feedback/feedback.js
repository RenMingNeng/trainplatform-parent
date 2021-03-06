

function feedback(){
    var _this = this;
    _this.pageNum = 1;
    _this.pageSize = 5;
    _this.currPage = 1;

    _this.goPage = function(currPage){
        if(currPage){
            _this.pageNum = currPage;
        }
        var params = {
            'pageSize': _this.pageSize,
            'pageNum': _this.pageNum
        };
        var url = appPath + "/feedback/list?" + $("#feedbackForm").serialize();
        var data = _this.ajax(url, params);
        var result = data['result'];
        if(!result) {
            alert("<tr><td>暂无数据</td></tr>"); return;
        }
        var dataList = result['dataList'];
        if(!dataList){
            alert("<tr><td>暂无数据</td></tr>"); return;
        }
        var len = dataList.length;
        var inner = "", item;
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];

            // inner += '';
            // inner += '<div class="title tr clearfix">';
            // inner += '<div style="line-height: 30px;">';
            // inner += '<span class="lk-checkbox" style="vertical-align: -3px;"></span>';
            // inner += '<span style="">' + item['content'] + '</span>';
            // inner += '</div>';
            // var problemStatus = item['problemStatus'];
            // var problemStatusName = problemStatus == "1" ? "待处理" : ( problemStatus == "2" ? "已处理" : "不处理");
            // inner += '<div>' + item['problemType'] + "，" + problemStatusName + '</div>';
            // inner += '<div>' + item['createUser'] + '</div>';
            // inner += '<div>' + item['createTime'] + '</div>';
            // inner += '<div>';
            // var attchments = item['attchments'];
            // inner += "<a target='_blank' href=\"" + _this.handler_2_attachments(attchments) + "\" class=\"sms-section5-link\">预览图片</a>";
            // if(problemStatus == 1){
            //     inner += "<a href=\"javascript:void(0)\" onclick='feedback.update_status(\"" + item['id'] + "\",2)' class=\"sms-section5-link\">&nbsp;&nbsp;已处理</a>";
            //     inner += "<a href=\"javascript:void(0)\" onclick='feedback.update_status(\"" + item['id'] + "\",3)' class=\"sms-section5-link\">&nbsp;&nbsp;不处理</a>";
            // }
            // inner += '</div></div>';

            inner += "<div class=\"title tr clearfix\">";
            inner += "<div style=\"line-height: 30px;\">";
            inner += "<span class=\"lk-checkbox\" style=\"vertical-align: -3px;\"></span>";
            inner += "<span style=\"color: #4b586a;\"><span style=\"font-weight: 600;\">"+item['createUser']+"</span></span>";
            inner += "<span class=\"send-time\">最后更新时间:"+item['createTime']+"</span>";
            inner += "<p style='word-break: break-all;' class='json'>"+_this.toJSON(item)+"</p>";
            inner += "</div>";
            inner += "<div>";

            var problemStatus = item['problemStatus'];
            if(problemStatus == '1'){
                inner += "<a href=\"javascript:void(0)\" onclick='feedback.update_status(\"" + item['id'] + "\",2)' class=\"sms-section5-link\">&nbsp;&nbsp;已处理</a>";
                inner += "<a href=\"javascript:void(0)\" onclick='feedback.update_status(\"" + item['id'] + "\",3)' class=\"sms-section5-link\">&nbsp;&nbsp;不处理</a><br>";
            }
            // var attchments = item['attchments'];
            // inner += _this.handler_2_attachments(attchments);
            var attachmentLMap = item['attachmentLMap'];
            for(var j=0; j< attachmentLMap.length; j++) {
                var id = attachmentLMap[j]['gridFSFileId'];
                var name = attachmentLMap[j]['originalFilename'];
                var attachment_url = appPath + "/article/attachment/" + id;
                inner += '<a target="_blank" href="' + attachment_url + '" class="a a-attachment">' + name + '</a>';
            }
            inner += "</div>";
            inner += "</div>";
        }
        // 渲染表单
        $("#feedback-table > div:not(':first')").remove();
        $("#feedback-table").append(inner);

        //分页
        _this.feedback_page(result);
    };

    _this.update_status = function(id, status){
        var params = {"id" : id, "problemStatus" : status};
        var url = appPath + '/feedback/update';
        var data = _this.ajax(url, params);
        alert('操作成功');
        _this.goPage(1);
    };

    _this.feedback_page = function(result){
        // 计算分页
        var page_inner = "";
        var page = result['page']; // 当前页数
        var pageSize = result['pageSize']; // 每页数量
        var count = result['count']; // 总条数
        var totalPages = result['totalPages']; // 总页数
        var haveNext = result['haveNext']; // 是否有下一页
        var nextPage = result['nextPage']; // 下一页页数
        var havePre = result['havePre']; // 是否有上一页
        var prePage = result['prePage']; // 上一页页数

        page_inner += "<span class=\"left\" style=\"line-height: 32px;margin-right: 20px;\">共<span style=\"color: #007fda;\">"+count+"</span>条&nbsp;第<span style=\"color: #007fda;\">"+page+"</span>页</span>";
        page_inner += "<nav aria-label=\"Page navigation \" class=\"left\" style=\"height:32px;\">";
        page_inner += "<ul class=\"pagination\" style=\"margin:0;\">";

        if(page == 1){
            page_inner += "<li class=\"active\"><a href=\"javascript:;\">首页</a></li>";
        }else{
            page_inner += "<li><a href=\"javascript:feedback.goPage(1);\">首页</a></li>";
        }
        if(havePre == 0){
            page_inner += "<li><a href=\"javascript:alert('已经是首页');\" aria-label=\"Previous\">上一页</a></li>";
        }else{
            page_inner += "<li><a href=\"javascript:feedback.goPage("+(page-1)+");\" aria-label=\"Previous\">上一页</a></li>";
        }
        if(haveNext==1){
            page_inner += "<li><a href=\"javascript:feedback.goPage("+(page+1)+");\" aria-label=\"Next\">下一页</a></li>";
        }else{
            page_inner += "<li><a href=\"javascript:alert('已经是尾页');\" aria-label=\"Next\">下一页</a></li>";
        }
        if(totalPages == page){
            page_inner += "<li class=\"active\"><a href=\"javascript:void(0);\" onclick=\"javascript:;\">尾页</a></li>";
        }else{
            page_inner += "<li><a href=\"javascript:void(0);\" onclick=\"javascript:feedback.goPage("+totalPages+");\">尾页</a></li>";
        }

        page_inner += "</nav>";
        page_inner += "<span class=\"control-label left\" style=\"vertical-align: 10px;line-height: 32px;margin: 0 10px 0 30px;\">跳至</span>";
        page_inner += "<input id=\"\" class=\"form-control left page-input\" style=\" width: 40px;\">";
        page_inner += "<span class=\"control-label left \" style=\"line-height: 32px;margin: 0 10px;\">页</span>";
        page_inner += "<button type=\"button \" class=\"btn btn-default left page-button\" style=\"color: #818082;\">跳转</button>";

        $("#page-footer").empty().html(page_inner);
        $("#page-footer").find(".page-button").bind("click", function(){
            var pageTxt = $("#page-footer").find(".page-input").val();
            if(isNaN(Number(pageTxt)) || typeof Number(pageTxt) != 'number'){alert('页数不合法');return;}
            if(pageTxt == page){alert('已经是当前页');return;}
            if(pageTxt > totalPages){alert('页数不合法');return;}
            if(pageTxt < 1){alert('页数不合法');return;}
            feedback.goPage(pageTxt);
        });
    };

    _this.handler_2_problem_type = function (problem_type) {
        if(!problem_type) return "";
        switch (problem_type) {
            case '1': return '培训'; break;
            case '2': return '练习'; break;
            case '3': return '考试'; break;
            case '4': return '培训、练习'; break;
            case '5': return '培训、考试'; break;
            case '6': return '练习、考试'; break;
            case '7': return '培训、练习、考试'; break;
            default : '';
        }
    };

    _this.handler_2_attachments = function (attachments) {
        if(!attachments) return "";
        var attachments_arr = attachments.split(",");
        var inner = "", attachment_url = "";
        for(var i=0; i<attachments_arr.length; i++) {
            attachment_url = appPath + "/article/attachment/" + attachments_arr[i];
            inner += "<a target='_blank' href=\""+attachment_url+"\">" + attachments_arr[i] +"</a><br>";
            // inner += "<a target='_blank' href=\""+attachment_url+"\">图" + (i+1) +"</a>";
        }
        return inner;
    };

    _this.toJSON = function (obj){
        return JSON.stringify(obj);
    };

    _this.clear = function () {
        var url = appPath + '/feedback/clearCache';
        _this.ajax(url);
        alert('操作成功');
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