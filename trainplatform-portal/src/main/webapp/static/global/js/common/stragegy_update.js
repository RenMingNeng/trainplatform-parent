/*组卷策略*/

function stragegy_update() {
    var _this = this;
    _this.projectId = $("#projectId").val();
    _this.projectStatus = '';

    _this.initMethod = function(projectStatus,examPermission){
        _this.projectStatus = projectStatus;
        _this.examPermission = examPermission;
        console.log("examPermission==="+examPermission);
        stragegy_update.stragegy_listener();
    };

    //公开性项目初始化
    _this.initPublicMethod = function(projectStatus){
        _this.projectStatus = projectStatus;


        _this.stragegy_listener();

        //初始化默认的一个
        _this.stragegy_select("-1", "默认受训角色");
    };

    _this.stragegy_select = function (role_id, role_name, isTrain) {
        //检查提交
        _this.stragegy_save()

        var params = {"project_id": _this.projectId, "role_id": role_id};
        var url = appPath + "/popup/proManager/examStrategy";
        var data = _this.ajax(url, params);
        var code = data['code'];
        if (code == '10000') {
            var result = data['result'];
            var examStrategy = result['examStrategy'];
            var selectAllCount = result['selectAllCount'];
            var stragegy_title = "";
            stragegy_title += '';
            stragegy_title += '<li>总分：<span class="totalScore">' + examStrategy.totalScore + '</span>&nbsp;分</li>';
            stragegy_title += '<input type="hidden" data-value="' + examStrategy.totalScore + '" class="totalScore" _name="totalScore" value="' + examStrategy.totalScore + '">';
            stragegy_title += '<input type="hidden" _name="roleId" value="' + role_id + '">';
            stragegy_title += '<input type="hidden" _name="roleName" value="' + role_name + '">';
            stragegy_title += '<li>合格分：';
            stragegy_title += '<input type="text" data-value="' + examStrategy.passScore + '" data-max="' + examStrategy.totalScore + '" _name="passScore" value="' + examStrategy.passScore + '" style="width: 35px;">';
            stragegy_title += '&nbsp;分</li>';
            if(isTrain){
                stragegy_title += '<li>考试学时：';
                stragegy_title += '<input type="text" data-value="' + examStrategy.necessaryHour + '" _name="necessaryHour" value="' + examStrategy.necessaryHour + '" style="width: 35px;">';
                stragegy_title += '&nbsp;min</li>';
            }
            stragegy_title += '<li>考试时长：';
            stragegy_title += '<input type="text" data-value="' + examStrategy.examDuration + '" _name="examDuration" value="' + examStrategy.examDuration + '" style="width: 35px;">';
            stragegy_title += '&nbsp;min</li>';
            $("#stragegy_title").html(stragegy_title);

            var list = result['list'];
            var stragegy_body = "";
            stragegy_body += '';
            for (var i = 0; i < list.length; i++) {
                if (i == 0) {
                    stragegy_body += '<tr>';
                    stragegy_body += '<td>' + (Number(i) + 1) + '</td>';
                    stragegy_body += '<td>' + list[i]['type'] + '</td>';
                    stragegy_body += '<td class="allCount">' + list[i]['allCount'] + '</td>';
                    stragegy_body += '<td>';
                    stragegy_body += '<input type="text" data-min="' + selectAllCount + '" data-value="' + list[i]['intCount'] + '" data-max="' + list[i]['allCount'] + '" _name="' + list[i]['typeName'] + 'Count" value="' + list[i]['intCount'] + '" style="width: 35px;">';
                    stragegy_body += '</td><td>';
                    stragegy_body += '<input type="text" data-value="' + list[i]['intScore'] + '" _name="' + list[i]['typeName'] + 'Score" value="' + list[i]['intScore'] + '" style="width: 35px;">';
                    stragegy_body += '</td>';
                    stragegy_body += '<td style="border-left:1px solid #ddd" rowspan="3" class="totalScore">' + list[i]['allScore'] + '</td>';
                    stragegy_body += '</tr>';
                } else {
                    stragegy_body += '<tr>';
                    stragegy_body += '<td>' + (Number(i) + 1) + '</td>';
                    stragegy_body += '<td>' + list[i]['type'] + '</td>';
                    stragegy_body += '<td class="allCount">' + list[i]['allCount'] + '</td>';
                    stragegy_body += '<td>';
                    stragegy_body += '<input type="text" data-min="' + selectAllCount + '" data-value="' + list[i]['intCount'] + '" data-max="' + list[i]['allCount'] + '" _name="' + list[i]['typeName'] + 'Count" value="' + list[i]['intCount'] + '" style="width: 35px;">';
                    stragegy_body += '</td><td>';
                    stragegy_body += '<input type="text" data-value="' + list[i]['intScore'] + '" _name="' + list[i]['typeName'] + 'Score" value="' + list[i]['intScore'] + '" style="width: 35px;">';
                    stragegy_body += '</td></tr>';
                }
            }
            $("#stragegy_body").html(stragegy_body);

            //组卷策略
            _this.stragegy_check();
        } else {
            alert('查询失败');
        }
    };

//增加监听
    _this.stragegy_listener = function () {
        $("#stragegyForm").find("#stragegy_body").on("keyup", "input", function () {
            _this.stragegy_compare(this);
            var totalScore = _this.totalScore();
            $(".totalScore").html(totalScore);
            $(".totalScore").val(totalScore);

            $("#stragegyForm").find("input[_name=passScore]").attr("data-max", totalScore);
            $("#stragegyForm").find("input[_name=passScore]").trigger("keyup");
            $("#stragegyForm").find("input[name=passScore]").attr("data-max", totalScore);
            $("#stragegyForm").find("input[name=passScore]").trigger("keyup");

            //隐藏域
            var curTotalScore = $("#stragegyForm").find("input[_name=totalScore]").attr("data-value");
            if (curTotalScore != undefined && curTotalScore != totalScore) {
                $("#stragegyForm").find("input[_name=totalScore]").attr("name",
                    $("#stragegyForm").find("input[_name=totalScore]").attr("_name"));
                $("#stragegyForm").find("input[_name=totalScore]").removeAttr("_name");
            }
        });

        $("#stragegyForm").find("#stragegy_title").on("keyup", "input", function () {
            _this.stragegy_compare(this);
        });
    };

    _this.stragegy_check = function () {
        console.log('3' == _this.projectStatus && _this.examPermission=='true')
        //权限
        if ('3' == _this.projectStatus && _this.examPermission=='true') {
            //组卷策略禁用
            $("#stragegyForm").find("input").attr("disabled", true);

            return;
        }

        //检查数据合法性
        $("#stragegyForm").find("input").each(function () {
            _this.stragegy_compare(this);

            $(this).trigger("keyup");
        });
    };

    _this.stragegy_save = function () {
        var length = $("#stragegyForm").find("input[name]").length;
        if (length > 0) {
            $("#stragegyForm").find("input[_name=roleId]").attr("name",
                $("#stragegyForm").find("input[_name=roleId]").attr("_name")).removeAttr("_name");
            $("#stragegyForm").find("input[_name=roleName]").attr("name",
                $("#stragegyForm").find("input[_name=roleName]").attr("_name")).removeAttr("_name");

            var params = $("#stragegyForm").serialize();
            params += "&projectId=" + _this.projectId;
            var url = appPath + "/popup/proManager/examStrategy/save" + "?" + params;
            _this.ajax(url);
        }
    };

//比较，超过最大值时取最大值
    _this.stragegy_compare = function (t) {
        var max = $(t).attr("data-max");
        var value = $(t).val();
        var curValue = $(t).attr("data-value");//原值
        var dataMin = $(t).attr("data-min");//最小值
        if (max != undefined && Number(value) > Number(max)) {
            $(t).val(max);
            value = max;
        }
        if(dataMin != undefined && curValue != undefined && Number(value) < Number(dataMin)){
            $(t).val(dataMin);
            value = dataMin;
        }
        if (curValue != undefined && curValue != value && $(t).attr("_name") != undefined) {
            $(t).attr("name", $(t).attr("_name"));
            $(t).removeAttr("_name");
            $(t).trigger("keyup");
        }
    };

//计算总分数
    _this.totalScore = function () {
        var length = $("#stragegyForm").find("#stragegy_body").find("input[name]").length;
        var totalScore = $("#stragegyForm").find("#stragegy_title").find(".totalScore").html();
        // if(length > 0){
        totalScore = 0;
        $("#stragegyForm").find("#stragegy_body tr").each(function () {
            if ($(this).find("input").length > 1) {
                var count = $(this).find("input:eq(0)").val();
                var score = $(this).find("input:eq(1)").val();
                score = Number(count) * Number(score);
                totalScore = Number(totalScore) + Number(score);
            }
        });
        // }
        return totalScore;
    };

    _this.ajax = function (url, param, type) {
        var result;
        $.ajax({
            url: url,
            async: false,
            type: 'post',
            data: param,
            success: function (data) {
                result = data;
            }
        });
        return result;
    };
}

var stragegy_update = new stragegy_update();


