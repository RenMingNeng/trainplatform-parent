/**
 * 组卷策略
 * @type {StragegySelect}
 */
var stragegySelect = new Object();

stragegySelect.projectId = null;

/*组卷策略*/
stragegySelect.stragegy_select_default = function(){
    var stragegy_title = "";
    stragegy_title += '<li>总分：<input type="text" class="totalScore" readonly="true" name="totalScore" value="0" style="width: 35px;">&nbsp;分</li>';
    stragegy_title += '<li>合格分：';
    stragegy_title += '<input type="text" data-max="0" data-value="0" name="passScore" value="0" style="width: 35px;">';
    stragegy_title += '&nbsp;分</li>';
    stragegy_title += '<li>考试学时：';
    stragegy_title += '<input type="text" name="necessaryHour" value="0" style="width: 35px;">';
    stragegy_title += '&nbsp;min</li>';
    stragegy_title += '<li>考试时长：';
    stragegy_title += '<input type="text" name="examDuration" value="120" style="width: 35px;">';
    stragegy_title += '&nbsp;min</li>';
    $("#stragegy_title").html(stragegy_title);

    var stragegy_body = "";
    stragegy_body += '<tr>';
    stragegy_body += '<td>1</td>';
    stragegy_body += '<td>单选题</td>';
    stragegy_body += '<td class="allCount">0</td>';
    stragegy_body += '<td>';
    stragegy_body += '<input type="text" data-max="0" name="singleCount" value="0" style="width: 35px;">';
    stragegy_body += '</td><td>';
    stragegy_body += '<input type="text" name="singleScore" value="0" style="width: 35px;">';
    stragegy_body += '</td>';
    stragegy_body += '<td style="border-left:1px solid #ddd" rowspan="3" class="totalScore">0</td>';
    stragegy_body += '</tr>';

    stragegy_body += '<tr>';
    stragegy_body += '<td>2</td>';
    stragegy_body += '<td>多选题</td>';
    stragegy_body += '<td class="allCount">0</td>';
    stragegy_body += '<td>';
    stragegy_body += '<input type="text" data-max="0" name="manyCount" value="0" style="width: 35px;">';
    stragegy_body += '</td><td>';
    stragegy_body += '<input type="text" name="manyScore" value="0" style="width: 35px;">';
    stragegy_body += '</td></tr>';

    stragegy_body += '<tr>';
    stragegy_body += '<td>3</td>';
    stragegy_body += '<td>判断题</td>';
    stragegy_body += '<td class="allCount">0</td>';
    stragegy_body += '<td>';
    stragegy_body += '<input type="text" data-max="0" name="judgeCount" value="0" style="width: 35px;">';
    stragegy_body += '</td><td>';
    stragegy_body += '<input type="text" name="judgeScore" value="0" style="width: 35px;">';
    stragegy_body += '</td></tr>';
    $("#stragegy_body").html(stragegy_body);
};

stragegySelect.stragegy_select = function (projectId_,isTrain, projectStatus, examPermission, num) {
    if(num == null || num == undefined){
        num = "";
    }
    stragegySelect.projectId = projectId_;
    var params = {"project_id": projectId_};
    var url = appPath + "/popup/project_create/step6/minInfo";
    var data = stragegySelect.ajax(url, params);
    var code = data['code'];
    var result = data['result'];
    if (code == '10000' && result != undefined && result != '') {
        var examStrategy = result['examStrategy'];
        var list = result['list'];
        stragegySelect.loadHTML(examStrategy, list, isTrain, num);

        // //角色名称
        // var strategyNames = result['strategyNames'];
        // var strategyNames_html = '';
        // for (var i = 0; i < strategyNames.length; i++) {
        //     strategyNames_html += '<div>' + strategyNames[i] + '</div>';
        // }
        // $("#roleNames").html(strategyNames_html);

        /*未开始、未发布、考试未考试的项目才可以修改*/
        if("1" == projectStatus || "2" == projectStatus || !examPermission){
            stragegySelect.loadEditable();
        }
    } else {
        stragegySelect.stragegy_select_default();
    }

    stragegySelect.stragegy_check();
};

//初始化插件
stragegySelect.loadEditable = function () {
    $("#stragegyForm").find("input:not([type=hidden])").each(function (index) {
        $.fn.editable.defaults.mode = 'popup';
        $(this).editable({
            validate:function (value) {
                if(isNaN(value)){
                    return  '输入不合法';
                }

                if(value == ""){
                    value = 0 ;
                }

                if(Number(value) < 0){
                    return  '不能输入负数';
                }

                if(Number(value) != parseInt(value)){
                    return  '不能输入小数';
                }

                var max = $(this).attr("data-max");
                var min = $(this).attr("data-min");//最小值
                if(Number(value) > Number(max)){
                    return  '最大值为' + max;
                }
                if(Number(value) < Number(min)){
                    return  '最小值为' + min;
                }

            },
            display:function (value) {
                if(value == ""){
                    $(this).val(0);
                }else{
                    $(this).val(parseInt(value));
                }
                $(this).trigger("keyup");
            }
        })
    });
};


//初始化内容
stragegySelect.loadHTML = function (examStrategy, list, isTrain, num) {
    var stragegy_title = "";
    stragegy_title += '';
    stragegy_title += '<li>总分：<span class="totalScore">' + examStrategy.totalScore + '</span>&nbsp;分</li>';
    if(examStrategy.roleId){
        stragegy_title += '<input type="hidden" _name="roleId" value="' + examStrategy.roleId + '">';
    }
    stragegy_title += '<input type="hidden" readonly data-value="' + examStrategy.totalScore + '" class="totalScore" _name="totalScore" value="' + examStrategy.totalScore + '">';
    stragegy_title += '<li>合格分：';
    stragegy_title += '<input type="text" title="点击修改" readonly data-value="' + examStrategy.passScore + '" data-max="' + examStrategy.totalScore + '" _name="passScore" value="' + examStrategy.passScore + '" style="width: 35px;">';
    stragegy_title += '&nbsp;分</li>';
    if(isTrain){
        stragegy_title += '<li>考试学时：';
        stragegy_title += '<input type="text" title="点击修改" readonly data-value="' + examStrategy.necessaryHour + '" _name="necessaryHour" value="' + examStrategy.necessaryHour + '" style="width: 35px;">';
        stragegy_title += '&nbsp;min</li>';
    }
    stragegy_title += '<li>考试时长：';
    stragegy_title += '<input type="text" title="点击修改" readonly data-value="' + examStrategy.examDuration + '" _name="examDuration" value="' + examStrategy.examDuration + '" style="width: 35px;">';
    stragegy_title += '&nbsp;min</li>';
    $("#stragegy_title" + num).html(stragegy_title);

    var stragegy_body = "";
    stragegy_body += '';
    for (var i = 0; i < list.length; i++) {
        if (i == 0) {
            stragegy_body += '<tr>';
            stragegy_body += '<td>' + (Number(i) + 1) + '</td>';
            stragegy_body += '<td>' + list[i]['type'] + '</td>';
            stragegy_body += '<td class="allCount">' + list[i]['allCount'] + '</td>';
            stragegy_body += '<td>';
            stragegy_body += '<input type="text" title="点击修改" readonly data-min="' + examStrategy[list[i]['typeName'] + 'Count'] + '" data-value="' + list[i]['intCount'] + '" data-max="' + list[i]['allCount'] + '" _name="' + list[i]['typeName'] + 'Count" value="' + list[i]['intCount'] + '">';
            stragegy_body += '</td><td>';
            stragegy_body += '<input type="text" title="点击修改" readonly data-value="' + list[i]['intScore'] + '" _name="' + list[i]['typeName'] + 'Score" value="' + list[i]['intScore'] + '" style="width: 35px;">';
            stragegy_body += '</td>';
            stragegy_body += '<td style="border-left:1px solid #ddd" rowspan="3" class="totalScore">' + list[i]['allScore'] + '</td>';
            stragegy_body += '</tr>';
        } else {
            stragegy_body += '<tr>';
            stragegy_body += '<td>' + (Number(i) + 1) + '</td>';
            stragegy_body += '<td>' + list[i]['type'] + '</td>';
            stragegy_body += '<td class="allCount">' + list[i]['allCount'] + '</td>';
            stragegy_body += '<td>';
            stragegy_body += '<input type="text" title="点击修改" readonly data-min="' + examStrategy[list[i]['typeName'] + 'Count'] + '" data-value="' + list[i]['intCount'] + '" data-max="' + list[i]['allCount'] + '" _name="' + list[i]['typeName'] + 'Count" value="' + list[i]['intCount'] + '">';
            stragegy_body += '</td><td>';
            stragegy_body += '<input type="text" title="点击修改" readonly data-value="' + list[i]['intScore'] + '" _name="' + list[i]['typeName'] + 'Score" value="' + list[i]['intScore'] + '" style="width: 35px;">';
            stragegy_body += '</td></tr>';
        }
    }
    $("#stragegy_body" + num).html(stragegy_body);
};

stragegySelect.stragegy_select_list = function (projectId_,isTrain) {
    stragegySelect.projectId = projectId_;
    var params = {"projectId": projectId_};
    var url = appPath + "/popup/project_create/step6/info";
    var data = stragegySelect.ajax(url, params);
    var code = data['code'];
    var result = data['result'];
    if (code == '10000' && result != undefined && result != '') {
        var examStrategies = result['examStrategies'];
        var lists = result['list'];

        for (var i = 0; i < lists.length; i++) {
            stragegySelect.loadHTML(examStrategies[i], lists[i], isTrain, i);

            //角色名称
            var strategyNames_html = '<td>' + examStrategies[i].roleName + '</td>';
            $("#roleNames" + i).html(strategyNames_html);
        }

        stragegySelect.stragegy_check();
    }
};

//增加监听
stragegySelect.stragegy_listener = function(){
    $("form[id^=stragegyForm]").on("keyup", "input", function(){
        stragegySelect.stragegy_compare(this);
    });

    $("form[id^=stragegyForm]").find("tbody[id^=stragegy_body]").on("keyup", "input", function(){
        stragegySelect.stragegy_compare(this);
        var totalScore = stragegySelect.totalScore(this);
        $(this).parents("div.stragegy_list").find(".totalScore").html(totalScore);
        $(this).parents("div.stragegy_list").find(".totalScore").val(totalScore);
        $(this).parents("div.stragegy_list").find(".totalScore").attr("value", totalScore);

        $(this).parents("div.stragegy_list").find("input[name=passScore]").attr("data-max", totalScore);
        $(this).parents("div.stragegy_list").find("input[name=passScore]").val(parseInt(totalScore * 0.8));
        $(this).parents("div.stragegy_list").find("input[name=passScore]").editable('setValue',parseInt(totalScore * 0.8));
        $(this).parents("div.stragegy_list").find("input[name=passScore]").trigger("keyup");
        $(this).parents("div.stragegy_list").find(".totalScore").trigger("keyup");

        $(this).parents("div.stragegy_list").find("input[_name=passScore]").attr("data-max", totalScore);
        $(this).parents("div.stragegy_list").find("input[_name=passScore]").val(parseInt(totalScore * 0.8));
        $(this).parents("div.stragegy_list").find("input[_name=passScore]").editable('setValue',parseInt(totalScore * 0.8));
        $(this).parents("div.stragegy_list").find("input[_name=passScore]").trigger("keyup");
    });
};

//检测合法性
stragegySelect.stragegy_check = function () {
    $("form[id^=stragegyForm]").find("tbody[id^=stragegy_body]").find("input").each(function(){
        stragegySelect.stragegy_compare(this);
    });

    $("form[id^=stragegyForm]").find("ul[id^=stragegy_title]").find("input").each(function(){
        stragegySelect.stragegy_compare(this);
    });
}

//比较，超过最大值时取最大值
stragegySelect.stragegy_compare = function(t){
    var max = $(t).attr("data-max");
    var value = $(t).val();
    var curValue = $(t).attr("data-value");//原值
    var dataMin = $(t).attr("data-min");//最小值
    if(value){
        if(isNaN(value)){
          $(t).val(max);
          value = max;
          layer.msg("不能为非数字字符，已纠正！");
        }
    }
    if(max != undefined && Number(value) > Number(max)){
        $(t).val(max);
        value = max;
        layer.msg("不能大于最大值，已纠正！");
    }
    if(dataMin != undefined && curValue != undefined && Number(value) < Number(dataMin)){
        $(t).val(dataMin);
        value = dataMin;
        layer.msg("必选题量约束，不能小于最小值，已纠正！");
    }
    if(curValue != undefined && curValue != value && $(t).attr("_name") != undefined){
        $(t).attr("name", $(t).attr("_name"));
        $(t).removeAttr("_name");
        $(t).trigger("keyup");
    }

    var scode = $(t).parent("td").next().find("input").val();
    if(value == 0 && scode != undefined && scode != 0){
        $(t).parent("td").next().find("input").val(0);
        $(t).parent("td").next().find("input").trigger("keyup");
    }else if(value != 0 && scode == 0){
        $(t).parent("td").next().find("input").val(1);
        $(t).parent("td").next().find("input").trigger("keyup");
    }
};

//计算总分数
stragegySelect.totalScore = function(t){
    var length = $(t).parents("form").find("tbody[id^=stragegy_body]").find("input[name]").length;
    var totalScore = $(t).parents("form").find("ul[id^=stragegy_title]").find(".totalScore").html();
    if(length > 0){
        totalScore = 0;
        $(t).parents("form").find("tbody[id^=stragegy_body] tr").each(function(){
            if($(this).find("input").length > 1){
                var count = $(this).find("input:eq(0)").val();
                var score = $(this).find("input:eq(1)").val();
                score = Number(count) * Number(score);
                totalScore = Number(totalScore) + Number(score);
            }
        });
    }
    return totalScore;
};

//保存组卷策略
stragegySelect.stragegy_save = function(){
    var status = true;
    $("form[id^=stragegyForm]").each(function () {
        var display = $(this).css("display");
        var length = $(this).find("input[name]").length;
        var totalScore = $(this).find("input[name=totalScore]").val();
        if(length > 0 && display != 'none' && totalScore != 0){
            if($(this).find("input[_name=roleId]")){
                $(this).find("input[_name=roleId]").attr("name", $(this).find("input[_name=roleId]").attr("_name"));
                $(this).find("input[_name=roleId]").removeAttr("_name");
            }
            var params = $(this).serialize();
            params += "&projectId=" + stragegySelect.projectId;
            var url = appPath + "/popup/project_create/step6/save" + "?" + params;
            var data = stragegySelect.ajax(url);
            var code = data['code'];
            if(code == '10000'){
                // layer.msg("保存成功");
            }
        }else if(totalScore == 0){
            status = false;
        }
    });
    return status;
};

stragegySelect.ajax = function(url, param, type) {
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