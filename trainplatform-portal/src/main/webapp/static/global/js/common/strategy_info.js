/**
 * 获取组卷策略详细值
 */
function strategy_info() {
    var _this = this;
    _this.ajax_url = appPath + "/popup/strategy/info";

    _this.getStrategyInfo = function (projectId, id) {
        var params = {"projectId": projectId};
        var data = _this.ajax(_this.ajax_url, params);
        var code = data['code'];
        if (code == '10000') {
            var result = data['result'];
            var html = '本次考试共';
            html += Number(result['singleCount']) + Number(result['manyCount']) + Number(result['judgeCount']);
            html += '题，满分';
            html += result['totalScore'];
            html += '分，其中单选题';
            html += result['singleCount'];
            html += '道，每题';
            html += result['singleScore'];
            html += '分，多选题';
            html += result['manyCount'];
            html += '道，每题';
            html += result['manyScore'];
            html += '分，判断题';
            html += result['judgeCount'];
            html += '道，每题';
            html += result['judgeScore'];
            html += '分';
            $("#" + id).html(html);
        }else{
            layer.alert(data['message']);
        }
    };

    _this.ajax = function (url, param) {
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

var strategy_info = new strategy_info();