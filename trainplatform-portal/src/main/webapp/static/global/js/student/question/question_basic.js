/**
 * 考试+练习+预览 基本信息
 */
function question_basic(){
    var _this = this;

    _this.init = function(){
        _this.removeBtn();
    };

    _this.removeBtn = function(){
        //移除我的错题
        var url = window.location.href;
        if(url.indexOf("wrong") != -1){
            $(".fa-remove").parent().show();
            _this.deleteFaultQuestion();
        }
    }

    //添加收藏
    _this.addCollect = function(questionId,projectId){
        var param = {
            "question_id" : questionId,
            "project_id" : projectId
        };
        var url = appPath + '/student/question/save_collect.do';
        _this.ajax(url, param);
    };
    //取消收藏
    _this.deleteCollect = function(questionId,projectId){
        var param = {
            "question_id" : questionId,
            "project_id" : projectId
        };
        var url = appPath + '/student/question/delete_collect.do';
        _this.ajax(url, param);
    };

    //移除错题
    _this.deleteFaultQuestion = function(){
        $("[id^=div]").find("i.fa-remove").parent("a").click(function(){
            var url = window.location.href;
            if(url.indexOf("wrong") != -1){
                var param = {
                    "question_id" : $(this).parents(".questions").attr("data-question"),
                    "project_id" : $(this).parents(".questions").attr("data-project")
                };
                var url = appPath + '/student/question/delete_wrong.do';
                var data = _this.ajax(url, param);
                var result = data['code'];
                if(result == '10000'){
                    $(this).remove();
                    layer.msg('操作成功');
                }else{
                    layer.msg('操作失败');
                }
            }
        });
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
}

var question_basic = new question_basic();