/**
 * Created by Administrator on 2017/7/24.
 */


 var fuck = new Object();


 fuck.init = function(basePath, tbody_id, page_id, pageNum, pageSize){
     console.log("basePath="+basePath);console.log("tbody_id="+tbody_id);
     fuck.basePath = basePath;
     fuck.tbody_id = tbody_id;
     fuck.page_id = page_id;
     fuck.pageNum = pageNum;
     fuck.pageSize = pageSize;
 }

 fuck.goPage = function(pageNum) {
     if(pageNum){
         fuck.pageNum = pageNum;
     }
     var params = {
         'fuck_name': '',
         'pageSize': fuck.pageSize,
         'pageNum': fuck.pageNum
     };
     $.ajax({
         url: fuck.basePath + "console/onlineuser/list",
         data: params,
         type: "post",
         dataType:"json",
         success:function(data){
            console.log(data);
            var result = data['result'];
            if(!result) {
                $("#"+fuck.tbody_id).empty().html("<tr><td>暂无数据</td></tr>"); return;
            }
            var dataList = result['dataList'];
            if(!dataList){
                $("#"+fuck.tbody_id).empty().html("<tr><td>暂无数据</td></tr>"); return;
            }
            var len = dataList.length;



            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += "<tr>";
                inner += "<td>"+(i+1)+"</td>";
                inner += "<td>"+item['userId']+"</td>";
                inner += "<td><span class=\"text-orange\">"+item['userName']+"</span></td>";
                inner += "<td>"+item['sessionId']+"</td>";
                inner += "<td>"+item['serverId']+"-"+item['hostname']+"-"+item['ip']+"</td>";
                inner += "<td>"+item['opersystem']+"-"+item['browser']+"</td>";
                inner += "<td>"+"("+item['country']+")-("+item['province']+")-("+item['city']+"</td>";
                inner += "<td>"+item['useragent']+"</td>";
                inner += "<td>"+item['loginTime']+"</td>";
                inner += "<td style=\"width: 70px\">";
                inner += '<a href=\"javascript:;\" onclick=\"admin_index.show_info(\'' + item.id + '\');\" class=\"link\">下线</a>';
                inner += "</tr>";
            }
            // 渲染表单
             $("#"+fuck.tbody_id).empty().html(inner);
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
             if(page == 1){
                 page_inner += "<li id=\"first\" class=\"p-prev disabled first\"><a href='javascript:fuck.goPage(1);'>首 页</a></li>";
             }else{
                 page_inner += "<li id=\"first\" class=\"p-prev first\"><a href='javascript:fuck.goPage(1);'>首 页</a></li>";
             }
             if(havePre == 0){
                 page_inner += "<li id=\"shangyiye\" class=\"p-prev disabled\" ><a href=\"javascript:;\"><i></i>上一页</a></li>";
             }else{
                 page_inner += "<li id=\"shangyiye\" class=\"p-prev\" ><a href=\"javascript:fuck.goPage("+(page-1)+");\"><i></i>上一页</a></li>";
             }
             if(haveNext==1){
                 page_inner += "<li class=\"p-next\"><a href=\"javascript:fuck.goPage("+(page+1)+");\">下一页<i></i></a></li>";
             }else{
                 page_inner += "<li class=\"p-next disabled\"><a href=\"javascript:;\">下一页<i></i></a></li>";
             }
             if(totalPages == page){
                 page_inner += "<li id=\"weiye\" class=\"p-next disabled\"><a href=\"javascript:void(0);\" onclick=\"javascript:;\">尾 页</a>&nbsp;</li>";
             }else{
                 page_inner += "<li id=\"weiye\" class=\"p-next\"><a href=\"javascript:void(0);\" onclick=\"javascript:fuck.goPage("+totalPages+");\">尾 页</a>&nbsp;</li>";
             }
             page_inner += "<li class=\"total\"><span id=\"span_number\">当前第&nbsp;"+page+"&nbsp;页共&nbsp;"+totalPages+"&nbsp;页&nbsp;";
             page_inner += "到第<input type=\"text\" id=\"input_number\" class=\"page-txtbox\" />页&nbsp;";
             page_inner += "<input name=\"\" value=\"确定\" type=\"button\" class=\"page-btn\"/></span></li>";





            $("#"+fuck.page_id).empty().html(page_inner);
            $("#"+fuck.page_id).find(".page-btn").bind("click", function(){

                var pageTxt = $("#"+fuck.page_id).find(".page-txtbox").val();
                if(typeof Number(pageTxt) != 'number'){alert('页数不合法');return;}
                if(pageTxt == page){alert('已经是当前页');return;}
                if(pageTxt > totalPages){alert('页数不合法');return;}
                if(pageTxt < 1){alert('页数不合法');return;}
                fuck.goPage(pageTxt);
            });













         }
     });
 }