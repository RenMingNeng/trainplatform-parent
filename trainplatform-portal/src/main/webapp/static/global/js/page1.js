/**
 * Created by Administrator on 2017/7/24.
 */

function page1(){
    var _this = this;
    _this.pageSizeAry;

    /**
     * 分页
     * @param param_form 参数form id
     * @param list_url 请求链接
     * @param tbody_id 列表div id
     * @param page_id 分页id
     * @param pageNum
     * @param pageSize
     */
    _this.init = function(param_form_id, list_url, tbody_id, page_id, pageNum, pageSize, pageSizeAry){
        _this.param_form_id = param_form_id;
        _this.list_url = list_url;
        _this.tbody_id = tbody_id;
        _this.page_id = page_id;
        _this.pageNum = pageNum;
        if(!_this.pageSize){
            _this.pageSize = pageSize;
        }
        if(!_this.pageNum){
            _this.pageNum = pageNum;
        }

        if(pageSizeAry){
            _this.pageSize = pageSizeAry[0];
            _this.pageSizeAry = pageSizeAry;
        }else{
            _this.pageSizeAry = new Array();
            _this.pageSizeAry.push(10);
            _this.pageSizeAry.push(20);
            _this.pageSizeAry.push(30);
            _this.pageSizeAry.push(50);
        }

       var length = $("#"+_this.tbody_id).parent().find("th").length;
       $("#"+_this.tbody_id).empty().html(_this.loadData(length));

        $("#" + page_id).on("change", "#pageSize", function(){
            _this.pageSize = $(this).val();
            _this.goPage(1);
        });

        var num;
        $("#"+_this.tbody_id).on("click", "input[type=checkbox]",function () {
            var tr_num = $("#"+_this.tbody_id).find("input[type=checkbox]").length;
            num = $("#"+_this.tbody_id).find("input[type=checkbox]:checked").length;
            if(num == tr_num){
                $('#checkAll').prop("checked", true);
            }else {
                $('#checkAll').prop("checked", false);
            }
        });
    };

    //返回新的分页参数
    _this.initPage = function (data) {
        _this.count = data['count'];

        var totalPages = data['totalPages']; // 总页数
        if(totalPages < _this.pageNum){
            _this.pageNum = 1;
        }
    };

    _this.getPageByNum = function (pageNum) {
        _this.pageNum = pageNum;
        _this.goPage();
    };

    _this.goPage = function(pageNum) {
        var url = _this.list_url + "?" + $("#" + _this.param_form_id).serialize();
        var params = {
            'pageSize': _this.pageSize,
            'pageNum': _this.pageNum
        };
        $.ajax({
            url: url,
            data: params,
            type: "post",
            dataType:"json",
            success:function(rs){
                var data = rs['result'];
                var dataList = data['dataList'];
                if(dataList.length<1){
                  var length = $("#"+_this.tbody_id).parent().find("th").length;
                  $("#"+_this.tbody_id).empty().html(_this.noList(length));$("#"+_this.page_id).empty();return
                }

                //初始化参数
                _this.initPage(data);
                var inner = _this.list(dataList);
                // 渲染表单
                $("#"+_this.tbody_id).empty().html(inner);
                $("#"+_this.tbody_id).parent("table").find("input").attr("checked", false);
                // 计算分页
                var page_inner = "";
                var page = data['page']; // 当前页数
                var pageSize = data['pageSize']; // 每页数量
                var count = data['count']; // 总条数
                var totalPages = data['totalPages']; // 总页数
                var haveNext = data['haveNext']; // 是否有下一页
                var nextPage = data['nextPage']; // 下一页页数
                var havePre = data['havePre']; // 是否有上一页
                var prePage = data['prePage']; // 上一页页数

                // page_inner += "<li class=\"first_total\"><span>共&nbsp;"+count+"&nbsp;条&nbsp;&nbsp;";
                page_inner += '<li><select name="" id="pageSize">' + _this.pageSizeList() + '</select></li>'
                if(page == 1){
                    page_inner += "<li id=\"first\" class=\"p-prev disabled first\"><a href='javascript:page1.getPageByNum(1);'>首 页</a></li>";
                }else{
                    page_inner += "<li id=\"first\" class=\"p-prev first\"><a href='javascript:page1.getPageByNum(1);'>首 页</a></li>";
                }
                if(havePre == 0){
                    page_inner += "<li id=\"shangyiye\" class=\"p-prev disabled\" ><a href=\"javascript:;\"><i></i>上一页</a></li>";
                }else{
                    page_inner += "<li id=\"shangyiye\" class=\"p-prev\" ><a href=\"javascript:page1.getPageByNum("+(page-1)+");\"><i></i>上一页</a></li>";
                }
                if(haveNext==1){
                    page_inner += "<li class=\"p-next\"><a href=\"javascript:page1.getPageByNum("+(page+1)+");\">下一页<i></i></a></li>";
                }else{
                    page_inner += "<li class=\"p-next disabled\"><a href=\"javascript:;\">下一页<i></i></a></li>";
                }
                if(totalPages == page){
                    page_inner += "<li id=\"weiye\" class=\"p-next disabled\"><a href=\"javascript:void(0);\" onclick=\"javascript:;\">尾 页</a></li>";
                }else{
                    page_inner += "<li id=\"weiye\" class=\"p-next\"><a href=\"javascript:void(0);\" onclick=\"javascript:page1.getPageByNum("+totalPages+");\">尾 页</a></li>";
                }
                page_inner += "<li class=\"total\"><span id=\"span_number\">第&nbsp;"+page+"&nbsp;页&nbsp;&nbsp;共&nbsp;"+totalPages+"&nbsp;页&nbsp;&nbsp;共&nbsp;"+count+"&nbsp;条&nbsp;";
                page_inner += "到第<input type=\"text\" id=\"input_number\" class=\"page-txtbox\" />页&nbsp;";
                page_inner += "<input name=\"\" value=\"确定\" type=\"button\" class=\"page-btn\"/></span></li>";

                $("#"+_this.page_id).empty().html(page_inner);
                $("#"+_this.page_id).find(".page-btn").bind("click", function(){

                    var pageTxt = $("#"+_this.page_id).find(".page-txtbox").val();
                    pageTxt = $.trim(pageTxt);
                    if(pageTxt == ''){layer.msg('请输入页数');return;}
                    if(isNaN(pageTxt)){layer.msg('页数不合法');return;}
                    if(pageTxt == page){layer.msg('已经是当前页');return;}
                    if(pageTxt > totalPages){layer.msg('页数不合法');return;}
                    if(pageTxt < 1){layer.msg('页数不合法');return;}
                    _this.getPageByNum(pageTxt);
                });

            }
        });
    }

    _this.pageSizeList = function () {
        var html = '';
        for(var i = 0; i < _this.pageSizeAry.length; i++){

            if(_this.pageSize == _this.pageSizeAry[i]){
                html += '<option value="' + _this.pageSizeAry[i] + '" selected>' + _this.pageSizeAry[i] + '</option>';
            }else{
                html += '<option value="' + _this.pageSizeAry[i] + '">' + _this.pageSizeAry[i] + '</option>';
            }
        }
        return html;
    };

    _this.list = function(dataList){
        var len = dataList.length;
        var inner = "", item;
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];
            inner += "<tr>";
            inner += "<td class=\"pro_num\">"+item['page_id']+"</td>";
            inner += "<td class=\"pro_name\"><span class=\"text-orange\">"+item['page_name']+"</span></td>";
            inner += "<td class=\"pro_type\">培训，练习，考试</td>";
            inner += "<td class=\"pro_time\">2017-06-01 至 2017-12-31</td>";
            inner += "<td class=\"pro_states\"><span class=\"text-blue\">进行中</span></td>";
            inner += "<td class=\"pro_person\">admin</td>";
            inner += "<td class=\"pro_create\">2017-05-31</td>";
            inner += "<td class=\"pro_handle\">";
            inner += "<a href=\"project_info.html\" class=\"link\">详情</a>";
            inner += "<a href=\"project_enter.html\" class=\"link\">进入项目</a>";
            inner += "<a href=\"javascript:;\" class=\"link\">查看详情</a>";
            inner += "</td>";
            inner += "</tr>";
        }
        return inner;
  }
  _this.noList = function(length){
    var inner = "";
    inner += "<tr>";
    inner += '<td colspan=\''+length+'\' class=\"table_empty\"><img src=\''+empty_img_src+'\'></img></td>'
    inner += "</tr>";
    return inner;
  }

  _this.loadData = function(length){
    var inner = "";
    inner += "<tr>";
    inner += '<td colspan=\''+length+'\' class=\"table_load\"><img src=\''+loading_img_src+'\'></img><span>正在加载数据，请稍等。。。</span></td>'
    inner += "</tr>";
    return inner;
  }
};


// eq:
// var basePath = "<%=basePath%>";
// var list_url = basePath + "/admin/personDossier/list";
// var page = new page();
// page.init("person_form", list_url, "person_table", "person_page", 1, 10);
// page.goPage(1);
// page.list = function(dataList){
//     var len = dataList.length;
//     var inner = "", item;
//     // 组装数据
//     for(var i=0; i< len; i++) {
//         item = dataList[i];
//         inner += "<tr>";
//         inner += "<td class=\"pro_num\">"+item['page_id']+"</td>";
//         inner += "<td class=\"pro_name\"><span class=\"text-orange\">"+item['page_name']+"</span></td>";
//         inner += "<td class=\"pro_type\">培训，练习，考试</td>";
//         inner += "<td class=\"pro_time\">2017-06-01 至 2017-12-31</td>";
//         inner += "<td class=\"pro_states\"><span class=\"text-blue\">进行中</span></td>";
//         inner += "<td class=\"pro_person\">admin</td>";
//         inner += "<td class=\"pro_create\">2017-05-31</td>";
//         inner += "<td class=\"pro_handle\">";
//         inner += "<a href=\"project_info.html\" class=\"link\">详情</a>";
//         inner += "<a href=\"project_enter.html\" class=\"link\">进入项目</a>";
//         inner += "<a href=\"javascript:;\" class=\"link\">查看详情</a>";
//         inner += "</td>";
//         inner += "</tr>";
//     }
//     return inner;
// }

