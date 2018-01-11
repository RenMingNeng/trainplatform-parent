

function store_exam(){
    var _this = this;

    _this.init = function (key) {
        //加载旧数据
        _this.load(key);

        //选中选项
        $("[id^=div]").delegate('.option','click',function(){
            var index = $("[id^=div]").index($(this).parents('[id^=div]'));
            window.LS.set(key, store_exam.getSelectsHtml(this, index));
        });

        //标记题目
        $(document).delegate('.btn-mark','click',function(){
            var index = $("[id^=div]").index($(this).parents('[id^=div]'));
            window.LS.set(key, store_exam.getSelectsHtml(this, index));
        })
    };

    /**
     * 获取选中值
     * @returns {string}
     */
    _this.getSelectsHtml = function (t, i) {
        var detail = [];
        $("[id^=div]").find(".option_select").each(function(index){
            //有选项，或者有做标记的才进行检查
            if($(this).parents(".questions").find(".option.on").length > 0 ||
                $("#card_list").find("li").eq(index).hasClass("mark")){

                var type = $(this).attr("data-type");
                //多选、不是当前选中的题时增加记录
                if(i != index || type == '02'){
                    var dt = _this.getSelect(this, index);
                    if(dt != ''){
                        detail.push(dt);
                    }
                }
            }
        });

        //添加本点击节点
        var curn = _this.getCurnSelect(t, i);
        if(curn != ''){
            detail.push(curn);
        }
        return JSON.stringify(detail, ",");
    };

    _this.getSelect = function (t, index) {
        var question_id = $(t).parents(".questions").attr("data-question");
        var type = $(t).attr("data-type");
        var select = '';
        var dt = '';
        $(t).parents(".questions").find(".option.on").each(function(){
            select += select == '' ? $(this).attr('data-type'): ',' + $(this).attr('data-type');
        });

        //判断是否都已经答题完成
        var markBtn = $("#card_list").find("li").eq(index).hasClass("mark");
        if(select != '' || markBtn){
            //1是2否
            var mark = 2;
            if(markBtn){
                mark = 1;
            }
            dt = question_id + "#" + select + "#" + mark;
        }
        return dt;
    };

    _this.getCurnSelect = function (t, index) {
        var question_id = $(t).parents(".questions").attr("data-question");
        var type = $(t).parents(".questions").find(".option_select").attr("data-type");
        var select = '';
        $(t).parents(".questions").find(".option.on").each(function(){
            select += select == '' ? $(this).attr('data-type'): ',' + $(this).attr('data-type');
        });
        //判断是否都已经答题完成
        var markBtn = $("#card_list").find("li").eq(index).hasClass("mark");
        //1是2否
        var mark = 1;
        if(!markBtn){
            mark = 2;
            //如果既没有做标记、有没有选中
            if(select == ''){
                return '';
            }
        }
        return question_id + "#" + select + "#" + mark;
    };

    _this.load = function (key) {
        var html = window.LS.get(key);
        if(html == undefined){
            //清空
            // window.LS.clear();
            return;
        }
        //添加选项
        var json = JSON.parse(html);
        for (var i=0;i<json.length;i++){
            var select = json[i];
            //选中
            _this.selectQuestion(select);
            //标记
            _this.selectMark(select);
        }
    };

    _this.selectMark = function (select) {
        var array = select.split('#');
        if(array.length > 2){
            var index = $(".questions").index($(".questions[data-question='" + array[0] + "']"));
            $("#card_list").find("li:eq(" + index+ ")").addClass("finish");
            if(array[2] == 1){
                $("#card_list").find("li:eq(" + index+ ")").addClass("mark");
                $(".questions").eq(index).find(".btn-mark").html("取消标记");
            }
        }
    };

    _this.selectQuestion = function (select) {
        var array = select.split('#');
        if(array.length > 2){
            var selectArray = array[1].split(",");
            for (var i=0;i<selectArray.length;i++){
                $(".questions[data-question='" + array[0] + "']")
                    .find("div.option[data-type*='" + selectArray[i] + "']").addClass("on");
            }
        }
    };

    _this.remove = function (key) {
        window.LS.remove(key);
    };
};

var store_exam = new store_exam();