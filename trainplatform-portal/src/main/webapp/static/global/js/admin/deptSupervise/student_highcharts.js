//数据
var data = [{
    name: 'Jane',
    data: [["一",50.5], ["二",40.8], ["三",64.5],["四",100],["五",10],["六",88],["七",75]]
}];

//绘图数据
var option={
    chart:{
        renderTo:'container',
        type:'column',
        zoomType:"xy",			//用鼠标选中一段区域，可对该区域进行
        backgroundColor: '#fafafa'
    },
    title: {
        text: ""
    },
    xAxis: {
        type: 'category'
    },
    yAxis: {
        title: {
            text: '人均学时(小时)'
        }
    },
    credits:{
        enabled:false
    },
    plotOptions:{
        column:{
            pointPadding:0,
            borderWidth:0,
            pointWidth : 30,
            borderRadius: 5
        },
        pie: {
            allowPointSelect: true, //选中某块区域是否允许分离
            cursor: 'pointer',
            dataLabels: {
                enabled: false //是否直接呈现数据 也就是外围显示数据与否
            },
            depth: 35,
            showInLegend: true
        }
    },
    tooltip:{
        enabled:true
    },
    series: data,
    dataLabels: {
        enabled: true,
        formatter: function () {
            return Highcharts.numberFormat(this.y / 1000, 0, ',') + 'k';
        },
        style: {
            color: '#FFFFFF',
            fontWeight: 'bold',
            textShadow: '0px 0px 3px black'
        }
    },
    pointPadding: 0
}

//图片对象
var chartObj;

/*
 *	隐藏所有内容区
 */
var hideContent = function(){
    $(".content").each(function(index){
        $(this).hide("slow");
    });
}
var bindTitle = function(){
    $(".title").each(function(){
        $(this).click(function(){
            hideContent();
            $(this).next().show("slow");
        });
    });
}

//报表
var highcharts_init = function(companyId){
    var params = {"companyId" : companyId};
    var data_url = appPath + '/supervise/highcharts';
    var dt = ajax(data_url, params);
    var result = dt['result'];
    data = JSON.parse(result);
    option.xAxis.categories = ['1', '2', '3'];
    option.series = data;

    drawImage();

    //隐藏设置区内容
    hideContent();
    //给标题绑定事件
    bindTitle();

    $("g.highcharts-button").remove();
};

var result_list;
var highcharts_data = function(result, companyAry){
    // data = JSON.parse(result);
    // option.series = data;
    result_list = result;
    option.series[0] = result_list[0];

    // option.xAxis.categories = companyAry;

    drawImage();

    //隐藏设置区内容
    hideContent();
    //给标题绑定事件
    bindTitle();

    changeType();

    $("g.highcharts-button").remove();
    $("g.highcharts-legend").remove();
};

var ajax = function(url, param){
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

//绘制图表方法，每次绘制完成后，将绘制的图标存入变量chartObj
var drawImage = function(){
    chartObj = new Highcharts.Chart(option);
}

//更换图表类型，通过修改json对象option的值来修改图表，修改之后绘制图表
var changeType = function(){
    var valueName = $("input[name=valueName]:checked").val();
    // option.chart.type = typeName;
    if( valueName == 0 ){
        var colors = ['#509bfd','#58befd'];
        Highcharts.getOptions().colors = Highcharts.map(colors,function(color){
            return {
                radialGradient :{cx:0,cy:-0.8,r:2.3},
                stops:[[0,color],[2,Highcharts.Color(color).brighten(0.2).get('rgb')]]
            }
        });
    }else if(valueName == 1){
        var colors = ['#4bd128','#70e351'];
        Highcharts.getOptions().colors = Highcharts.map(colors,function(color){
            return {
                radialGradient :{cx:0,cy:-0.8,r:2.3},
                stops:[[0,color],[2,Highcharts.Color(color).brighten(0.2).get('rgb')]]
            }
        });
    }else if( valueName == 2 ){
        var colors = ['#fed547','#fee079'];
        Highcharts.getOptions().colors = Highcharts.map(colors,function(color){
            return {
                radialGradient :{cx:0,cy:-0.8,r:2.3},
                stops:[[0,color],[2,Highcharts.Color(color).brighten(0.2).get('rgb')]]
            }
        });
    }
    option.series[0] = result_list[valueName];
    option.yAxis.title.text = $("input[name=valueName]:checked").attr("title");

    drawImage();
    $("g.highcharts-button").remove();
    $("g.highcharts-legend").remove();
}

//更换图表的标题，可以通过修改json对象option来修改，也可以通过highcharts自带的方法修改
var changeTitle = function(){
    var titleTemp = $("#changeTitle").val();
    var subtitleTemp = $("#changeSubTitle").val();
    if(titleTemp != "" && titleTemp != null && subtitleTemp != "" && subtitleTemp != null){
        chartObj.setTitle({
            text:titleTemp
        },{
            text:subtitleTemp
        });
    }else {
        if(titleTemp != "" && titleTemp != null){
            option.title.text = titleTemp;
        }else if(subtitleTemp != "" && subtitleTemp != null){
            option.subtitle.text = subtitleTemp;
        }
        drawImage();
    }
}

//数据的格式可以参考data对象，在数据中，可以设置各种数据的属性
var changeData = function(){
    option.series = data;
    drawImage();
}

//控制标签显隐
var legendFlag = true;
var showOrHideLegend = function(){
    if(!option.legend){
        option.legend = {};
    }
    if(legendFlag){
        option.legend.enabled = false;
        legendFlag = false;
        $("#legend").html("显示标签");
    }else{
        option.legend.enabled = true;
        legendFlag = true;
        $("#legend").html("隐藏标签");
    }
    drawImage();
}

/*
 * 改变标签的位置
 */
var changeLegend = function(position){
    if(!option.legend){
        option.legend = {};
    }
    option.legend.align = position;
    drawImage();
}

/*
 * 控制版本信息显隐
 */
var creditFlag = false;
var showOrHideCredits = function(){
    if(!option.credits){
        option.credits = {}
    }
    if (creditFlag) {
        option.credits.enabled = false;
        creditFlag = false;
        $("#credits").html("显示版本信息");
    }else{
        option.credits.enabled = true;
        creditFlag = true;
        $("#credits").html("隐藏版本信息");
    }
    drawImage();
}

/*
 *	控制版本设置
 */
var changeCredits = function(){
    if(!option.credits){
        option.credits = {}
    }
    var creditsText = $("#creditsText").val();
    var creditsHref = $("#creditsHref").val();
    if(creditsText != "" && creditsText != null){
        option.credits.text = creditsText;
    }else if(creditsHref != "" && creditsHref != null){
        if (creditsHref.indexOf("www")>=0) {
            creditsHrefTemp = "http://" +　creditsHref;
        }
        option.credits.href = creditsHrefTemp;
    }
    drawImage();
}

/*
 *	修改X轴坐标值
 */
var changeCategories = function(){
    var categories = $("#categories").val();
    // console.log(categories);
    var categoriesArray = categories.split(" ");
    // console.log(categoriesArray);
    option.xAxis.categories = categoriesArray;
    drawImage();
}