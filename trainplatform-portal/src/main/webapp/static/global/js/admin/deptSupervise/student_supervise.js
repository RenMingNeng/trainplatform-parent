
function dept_supervise(){
    var _this = this;
    _this.rMenu = '';
    _this.page_info;
    _this.zNodes;
    _this.setting = {
        /*check: {
         enable: true,
         chkStyle: "radio"
         },*/
        data: {
            simpleData: {
                enable: true
            }
        },

        view: {
            dblClickExpand: false,
                fontCss: _this.getFontCss
        },
        callback: {
            // onRightClick: function(event, treeId, treeNode){
            //     _this.onRightClick(event, treeId, treeNode);
            // },
            onClick: function(e,treeId, treeNode){
                _this.click(treeNode);
            }
        }
    };
    _this.zTree;
    _this.tree_url = appPath + '/admin/department/deptTree',
    _this.tree_id = 'supervise_tree_id';
    _this.list_url = appPath + "/admin/deptSupervise/list";

    _this.initMethod = function(){
        // _this.rMenu = rMenu;
        // _this.initTree();

        $("#supervise_page").on("change", "#pageSize", function(){
            _this.pageSize = $(this).val();
            _this.goPage(1);
        });

        $(".radio-group").on("click", "label",function(){

            $(this).addClass('active').siblings('label').removeClass('active');
            changeType();
        });

        _this.pageSizeAry = new Array();
        _this.pageSizeAry.push(10);
        _this.pageSizeAry.push(20);
        _this.pageSizeAry.push(30);
    };

    _this.click = function(node){
        // 获取左侧选中节点
        if(node.type == 1 || node.type == 2){
            $("#company_id").val(node.id);
            $("#dept_id").val(node.id);
        }else{
            $("#company_id").val("");
            $("#dept_id").val(node.id);
        }
        $("#dept_name").val(node.name);
        _this.initTable();
    }

    _this.onRightClick = function(event, treeId, treeNode) {
        var scrollTop = $(window).scrollTop();
        var pageX = event.clientX;
        var pageY = event.clientY + scrollTop;
        _this.zTree.selectNode(treeNode);
        _this.showRMenu(pageX ,pageY , treeNode);
    }

    _this.showRMenu = function(x, y,treeNode) {
        $("#m_add").show();
        $("#m_del").show();
        $("#m_moveup").show();
        $("#m_movedown").show();

        $("#rMenu").css({"top":y+"px", "left":x+"px", "visibility":"visible"});

        $("body").bind("mousedown", _this.onBodyMouseDown);
    }

    _this.onBodyMouseDown = function(event){
        if (!(event.target.id == "rMenu" || $(event.target).parents("#rMenu").length>0)) {
            _this.rMenu.css({"visibility" : "hidden"});
        }
    }

    _this.hideRMenu = function() {
        if (_this.rMenu) _this.rMenu.css({"visibility": "hidden"});
        $("body").unbind("mousedown", _this.onBodyMouseDown);
    }

    _this.initTree = function(){
        var data = _this.ajax(_this.tree_url);
        var result = data['result'];
        _this.zNodes = JSON.parse(result);

        // 加载树
        _this.zTree = $.fn.zTree.init($("#" + _this.tree_id), _this.setting, _this.zNodes);
        var nodes = _this.zTree.getNodes();
        if(nodes){
            _this.zTree.selectNode(nodes[0]);
            _this.click(nodes[0].id);
        }
    };

    _this.initTable = function (){
        $("html").addClass("loading");
        _this.goPage(1);
    };

    _this.goPage = function(pageNum){
        var url = _this.list_url + "?" + $("#superviseForm").serialize();
        if(pageNum){
            _this.pageNum = pageNum;
        }
        var params = {
            'pageSize': _this.pageSize,
            'pageNum': _this.pageNum
        };

        // var rs = _this.ajax(url, params);
        $.ajax({
            url: url,
            async: true,
            type: 'post',
            data: params,
            success: function(rs){
                var data = rs['result'];
                var page = data['page'];
                var dataList = page['dataList'];
                if(dataList.length<1){
                    var length = $("#supervise_table").parent().find("th").length;
                    $("#supervise_table").empty().html(_this.noList(length));$("#supervise_page").empty();
                }else{
                    var inner = _this.list(dataList, page['page']);
                    // 渲染表单
                    $("#supervise_table").empty().html(inner);
                    _this.page(page);
                }
                tooptip();
                //报表
                var jsonArray = data['jsonArray'];
                var companyAry = data['companyAry'];
                highcharts_data(jsonArray, companyAry);

                $("html").removeClass("loading");
            }
        });
    };

    _this.page = function (page_info) {
        // 计算分页
        var page_inner = "";
        var page = page_info['page']; // 当前页数
        var pageSize = page_info['pageSize']; // 每页数量
        var count = page_info['count']; // 总条数
        var totalPages = page_info['totalPages']; // 总页数
        var haveNext = page_info['haveNext']; // 是否有下一页
        var nextPage = page_info['nextPage']; // 下一页页数
        var havePre = page_info['havePre']; // 是否有上一页
        var prePage = page_info['prePage']; // 上一页页数

        page_inner += '<li><select name="" id="pageSize">' + _this.pageSizeList(_this.pageSizeAry) + '</select></li>'
        if(page == 1){
            page_inner += "<li id=\"first\" class=\"p-prev disabled first\"><a href='javascript:dept_supervise.goPage(1);'>首 页</a></li>";
        }else{
            page_inner += "<li id=\"first\" class=\"p-prev first\"><a href='javascript:dept_supervise.goPage(1);'>首 页</a></li>";
        }
        if(havePre == 0){
            page_inner += "<li id=\"shangyiye\" class=\"p-prev disabled\" ><a href=\"javascript:;\"><i></i>上一页</a></li>";
        }else{
            page_inner += "<li id=\"shangyiye\" class=\"p-prev\" ><a href=\"javascript:dept_supervise.goPage("+(page-1)+");\"><i></i>上一页</a></li>";
        }
        if(haveNext==1){
            page_inner += "<li class=\"p-next\"><a href=\"javascript:dept_supervise.goPage("+(page+1)+");\">下一页<i></i></a></li>";
        }else{
            page_inner += "<li class=\"p-next disabled\"><a href=\"javascript:;\">下一页<i></i></a></li>";
        }
        if(totalPages == page){
            page_inner += "<li id=\"weiye\" class=\"p-next disabled\"><a href=\"javascript:void(0);\" onclick=\"javascript:;\">尾 页</a></li>";
        }else{
            page_inner += "<li id=\"weiye\" class=\"p-next\"><a href=\"javascript:void(0);\" onclick=\"javascript:dept_supervise.goPage("+totalPages+");\">尾 页</a></li>";
        }
        page_inner += "<li class=\"total\"><span id=\"span_number\">当前第&nbsp;"+page+"&nbsp;页&nbsp;&nbsp;共&nbsp;"+totalPages+"&nbsp;页&nbsp;";
        page_inner += "跳转至<input type=\"text\" id=\"input_number\" class=\"page-txtbox\" />页&nbsp;";
        page_inner += " <input name=\"\" value=\"确定\" type=\"button\" class=\"page-btn\"/></span></li>";

        $("#supervise_page").empty().html(page_inner);
        $("#supervise_page").find(".page-btn").bind("click", function(){
            var pageTxt = $("#supervise_page").find(".page-txtbox").val();
            pageTxt = $.trim(pageTxt);
            if(pageTxt == ''){layer.msg('请输入页数');return;}
            if(isNaN(pageTxt)){layer.msg('页数不合法');return;}
            if(pageTxt == page){layer.msg('已经是当前页');return;}
            if(pageTxt > totalPages){layer.msg('页数不合法');return;}
            if(pageTxt < 1){layer.msg('页数不合法');return;}
            _this.goPage(pageTxt);
        });
    }

    _this.list = function(dataList, index){
        var len = dataList.length;
        var inner = "", item,isTop;
        // 组装数据
        for(var i=0; i< len; i++) {
            item = dataList[i];
            isTop = item['isTop'];
            if(isTop != undefined && isTop != null && isTop == '1' && len > 1){
                inner += '<tr style="background: #f4f4f4;">';
            }else{
                inner += '<tr>';
            }
            inner += '<td>' + item['companyName'] + '</td>';
            inner += '<td>' + item['countUser'] + '</td>';
            inner += '<td>' + item['countTrainUser'] + '</td>';
            inner += '<td>' + item['percentTrain'] + '%</td>';
            /*inner += '<td>0%</td>';*/
            inner += '<td>' + item['totalClassHour'] + '</td>';
            inner += '<td>' + item['averagePersonClassHour'] + '</td>';
            inner += '<td>' + item['countTrain'] + '</td>';
            inner += '<td>' + item['averageTrainCount'] + '</td>';
            inner += '</tr>';
        }
        return inner;
    };

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

    _this.noList = function(length){
        var inner = "";
        inner += "<tr>";
        inner += '<td colspan=\''+length+'\' align=\"center\" class=\"green\" style=\"text-align: center\">对不起，没有找到任何相关记录...</td>'
        inner += "</tr>";
        return inner;
    }

    _this.search = function(){
        _this.initTable();
    };

    _this.searchAll = function(){
        $("#search").val("");
        $("#startTime").val("");
        $("#endTime").val("");
        _this.initTable();
    };

    //搜索高亮
    function getFontCss(treeId, treeNode){
        return (!!treeNode.highlight)?{'color': "#A60000", 'font-weight':"bold"}:{'color': "#333", 'font-weight':"normal"};
    }

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
}

var dept_supervise = new dept_supervise();
