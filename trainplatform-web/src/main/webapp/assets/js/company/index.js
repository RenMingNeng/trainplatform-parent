function Company(){

    var _this = this;
        _this.pageNum = 1;
        _this.pageSize = 5;
        _this.currPage = 1;
        _this.list_url = appPath + "/company/list";

    _this.init = function () {
        _this.initEvent();
    };

    _this.initEvent = function (){
        $('.select-all').click(function () {
            if($(this).hasClass('lk-checkbox-active')){
                $('.sms-table .tr .lk-checkbox').removeClass('lk-checkbox-active');
                $(this).toggleClass('lk-checkbox-active');
            }else{
                $('.sms-table .tr .lk-checkbox').addClass('lk-checkbox-active');
                $(this).toggleClass('lk-checkbox-active');
            }
        });

        $('.sms-table').delegate("div.tr span.lk-checkbox", "click", function () {
            if($(this).hasClass('lk-checkbox-active')){
                $(this).toggleClass('lk-checkbox-active');
            }else{
                $(this).toggleClass('lk-checkbox-active');
            }
            var num = $('.tr .lk-checkbox-active').length;
            var tr_num = $('.tr').length;
            if(num == tr_num){
                $('.select-all').addClass('lk-checkbox-active');
            }else {
                $('.select-all').removeClass('lk-checkbox-active');
            }
        });

        $("#btn-permission").click(function () {
            var num = $('.sms-table .tr .lk-checkbox-active').length;
            if(null == num || 0 == num) {
                alert('未选择操作项'); return;
            }
            var companys = []; var company = Object();
            $('.tr .lk-checkbox-active').each(function () {
                companys.push($(this).attr("intId") + "@@" + $(this).attr("varName"));
                $("#companyIdNameArr").val(companys.join(","));
            });
            $("#company-form").submit();
        });

        // $("#btn-permission-all").click(function () {
        //     var isTrue = confirm("是否分配该单位分类的所有单位为全部权限？");
        //     if(isTrue){
        //         var intTypeId = $("#intTypeId").val();
        //         alert(intTypeId);
        //     }
        // });

        $("#page-footer").on("keyup", "#pageSizeCount", function () {
            _this.pageSize = $(this).val();
        })
        $("#page-footer").on("blur", "#pageSizeCount", function () {
            _this.search();
        })
    };

    //初始化树
    _this.goPage = function(currPage, url){
        if (!_this.pageNum || currPage != 1) {
            _this.pageNum = currPage;
        }
        if(url == undefined){
            url = _this.list_url;
            $("#search_value").val("");
        }
        var params = {
            'intTypeId': $("#intTypeId").val(),
            'varName': '',
            'pageSize': _this.pageSize,
            'pageNum': _this.pageNum
        };
        $.ajax({
            url: url,
            data: params,
            type: "post",
            dataType:"json",
            success:function(data){
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
                    var chrIsValid = item.chrIsValid;
                    inner += "<div class=\"title tr clearfix\">";
                    inner += "<div style=\"line-height: 30px;\">";
                    inner += "<span class=\"lk-checkbox\" style=\"vertical-align: -3px;\" intId=\""+item['intId']+"\" varName=\""+item['varName']+"\"></span>";
                    inner += "<span style=\"color: #4b586a;\"><span style=\"font-weight: 600;\">"+item['varName']+"</span></span>";
                    inner += "<p style='word-break: break-all;'>"+_this.toJSON(item)+"</p>";
                    inner += "</div>";
                    inner += "<div>";
                    if(chrIsValid == '0'){
                        inner += "<a href=\"javascript:void(0);\" class=\"sms-section5-link\" onclick=\"company.update_chrIsValid('1','" + item['intId'] + "');\">启用</a>";
                    }
                    if(chrIsValid == '1'){
                        inner += "<a href=\"javascript:void(0);\" class=\"sms-section5-link\" onclick=\"company.update_chrIsValid('0','" + item['intId'] + "');\">禁用</a>";
                    }
                    inner += "</div>";
                    inner += "</div>";
                }
                // 渲染表单
                $("#user-table > div:not(':first')").remove();
                $("#user-table").append(inner);
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

                page_inner += "<span class=\"control-label left\" style=\"vertical-align: 10px;line-height: 32px;margin: 0 20px 0 0px;\">显示</span>";
                page_inner += "<input id=\"pageSizeCount\" value='" + pageSize + "' class=\"form-control left page-input\" style=\" width: 80px;\">";
                page_inner += "<span class=\"control-label left \" style=\"line-height: 32px;margin: 0 10px;\">条</span>";

                if(page == 1){
                    page_inner += "<li class=\"active\"><a href=\"javascript:;\">首页</a></li>";
                }else{
                    page_inner += "<li><a href=\"javascript:company.goPage(1);\">首页</a></li>";
                }
                if(havePre == 0){
                    page_inner += "<li><a href=\"javascript:alert('已经是首页');\" aria-label=\"Previous\">上一页</a></li>";
                }else{
                    page_inner += "<li><a href=\"javascript:company.goPage("+(page-1)+");\" aria-label=\"Previous\">上一页</a></li>";
                }
                if(haveNext==1){
                    page_inner += "<li><a href=\"javascript:company.goPage("+(page+1)+");\" aria-label=\"Next\">下一页</a></li>";
                }else{
                    page_inner += "<li><a href=\"#\" aria-label=\"Next\">上一页</a></li>";
                }
                if(totalPages == page){
                    page_inner += "<li class=\"active\"><a href=\"javascript:void(0);\" onclick=\"javascript:;\">尾页</a></li>";
                }else{
                    page_inner += "<li><a href=\"javascript:void(0);\" onclick=\"javascript:company.goPage("+totalPages+");\">尾页</a></li>";
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
                    user.goPage(pageTxt);
                });
            }
        });
    };

    _this.search = function(){
        var search_value = $("#search_value").val();
        var url = _this.list_url;
        if(search_value != '' && search_value != undefined){
            url += "?search=" + search_value;
        }
        _this.goPage(1, url);
    };

    //修改状态
    _this.update_chrIsValid = function(chrIsValid, companyId){
        var params = {"chrIsValid" : chrIsValid, "companyId" : companyId};
        var url = appPath + '/company/update_company_valid';
        var data = _this.ajax(url, params);
        var result = data['code'];
        if(result == '10000'){
            alert('操作成功');
            _this.goPage(1);
        }
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

    _this.toJSON = function (obj){
        return JSON.stringify(obj);
    };
};

var company = new Company();
