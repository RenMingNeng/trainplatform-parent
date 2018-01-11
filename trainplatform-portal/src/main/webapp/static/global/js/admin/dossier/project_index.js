

function project_dossier(){
    var _this = this;
    var page;

    _this.init = function(page_project){
        page = page_project;
        _this.initTable();

        $('#click_project_type').on("click", "span", function(){
            $('#click_project_type').find("span").removeClass("active");
            $(this).addClass("active");
            _this.bind_click(this);
        });
    };

    _this.initTable = function(){
        var list_url = appPath + "/admin/dossier/project/list";
        var project_url = appPath + "/admin/dossier/project/user?project_id=";
        page.init("project_form", list_url, "project_table", "project_page", 1, 10);
        page.goPage(1);
        page.list = function(dataList){
            var len = dataList.length;
            var inner = "", item;
            // 组装数据
            for(var i=0; i< len; i++) {
                item = dataList[i];
                inner += '<tr>';
                inner += '<td>' + (i+1) + '</td>';
                inner += '<td>';
                inner += '<span class="text-orange">'+ item['project_name'] + '</span>';
                inner += '</td>';
                inner += '<td>'+ item['project_type_name'] + '</td>';
                var projectType = item['project_type'];
                // if(projectType == '3'){
                //     inner += '<td>'+ TimeUtil.longMsTimeToDateTime3(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeToDateTime3(item['project_end_time']) + '</td>';
                // }else{
                // }
                inner += '<td>'+ TimeUtil.longMsTimeConvertToDateTime(item['project_start_time']) + '<br/>' + TimeUtil.longMsTimeConvertToDateTime(item['project_end_time']) + '</td>';
                inner += '<td>'+ item['person_count'] + '</td>';
                // var projectMode = item['project_Mode'];
                // if(projectMode == "0"){
                //     inner += '<td>私有</td>';
                // }else if(projectMode == "1"){
                //     inner += '<td>公开</td>';
                // }
                inner += '<td>';
                inner += item['create_user'];
                inner += '</td>';
                inner += '<td>';
                inner += TimeUtil.longMsTimeConvertToDateTime(item['create_time']);
                inner += '</td>';
                inner += '<td>';
                inner += '<a href="' + project_url + item['project_id'] + '" class="a a-info">详情</a> ';
                inner += '<a href="' +　appPath + '/admin/dossier/project/export?project_id=' + item['project_id'] + '&project_type=' + item['project_type'] + '" class="a a-view">导出</a>';
                inner += '</td>';
                inner += '</tr>';
            }
            return inner;
        }
    };

    _this.search = function(){
        _this.initTable();
    };

    _this.searchAll = function(){
        $('#click_project_type').find("span").removeClass("active");
        $('#click_project_type span:eq(0)').addClass("active");
        $("#project_form").find("input[name=project_name]").val("");
        $("#project_form").find("input[name=project_start_time]").val("");
        $("#project_form").find("input[name=project_end_time]").val("");
        $("#project_form").find("input[name=project_type]").val("");
        _this.initTable();
    };

    _this.bind_click = function(t){
        var value = $(t).attr("data-value");
        $("#project_form").find("input[name=project_type]").val(value);
        _this.initTable();
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

var project_dossier = new project_dossier();