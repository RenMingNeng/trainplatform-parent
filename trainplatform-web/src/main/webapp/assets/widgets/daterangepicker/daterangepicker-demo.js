/**
 * Simplified Chinese translation for bootstrap-datetimepicker
 * Yuan Cheung <advanimal@gmail.com>
 */
;
//(function($) {
//	$.fn.datetimepicker.dates['zh-CN'] = {
//		days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
//		daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
//		daysMin: ["日", "一", "二", "三", "四", "五", "六", "日"],
//		months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
//		monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
//		today: "今日",
//		suffix: [],
//		meridiem: ["上午", "下午"],
//		format: "yyyy-mm-dd" /*控制显示格式,默认为空，显示小时分钟*/
//	};
//}(jQuery));

$(function() {
    "use strict";
    $("#daterangepicker-example").daterangepicker({
        separator: ' 至 ',
        format: "yyyy:mm:dd",
        locale: {
            applyLabel: '确定',
            cancelLabel: '取消',
            fromLabel: '起始时间',
            toLabel: '结束时间',
            customRangeLabel: '自定义',
            daysOfWeek: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            firstDay: 1
        }
    });
}), $(function() {
    "use strict";

    var options = {};

    //	options.showDropdowns = true;
    //显示第几周
    //	options.showWeekNumbers = true;

    options.showISOWeekNumbers = true;

    //	options.timePicker = true;
    //
    //	options.timePicker24Hour = true;

    //	options.timePickerIncrement = parseInt($('#timePickerIncrement').val(), 10);
    //	options.timePickerSeconds = true;

    options.autoApply = true;

    options.dateLimit = {
        days: 7
    };

    //	options.ranges = {
    //		'今天': [moment(), moment()],
    //		'昨天': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
    //		'最近7天': [moment().subtract(6, 'days'), moment()],
    //		'最近30天': [moment().subtract(29, 'days'), moment()],
    //		'This Month': [moment().startOf('month'), moment().endOf('month')],
    //		'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
    //	};
    //	
    options.ranges = {
        //'最近1小时': [moment().subtract('hours',1), moment()],  
        '今日': [moment().startOf('day'), moment()],
        '昨日': [moment().subtract('days', 1).startOf('day'), moment().subtract('days', 1).endOf('day')],
        '最近7日': [moment().subtract('days', 6), moment()],
        '最近30日': [moment().subtract('days', 29), moment()]
    };

    options.locale = {
        format: "yyyy-mm-dd",
        separator: ' - ',
        applyLabel: '确定',
        cancelLabel: '取消',
        fromLabel: '起始时间',
        toLabel: '结束时间',
        customRangeLabel: '自定义',
        daysOfWeek: ['日', '一', '二', '三', '四', '五', '六'],
        monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
        firstDay: 1
    };

    options.linkedCalendars = false;

    options.autoUpdateInput = false;

    options.alwaysShowCalendars = true;

    $("#daterangepicker-time").daterangepicker(options);
    //	$("#daterangepicker-time").daterangepicker({
    //		timePicker: !0,
    //		timePickerIncrement: 30,
    //		format: "MM/DD/YYYY h:mm A"
    //	})
    //	

}), $(function() {
    "use strict";
    $("#daterangepicker-custom").daterangepicker({
        startDate: moment().subtract("days", 29),
        endDate: moment(),
        minDate: "01/01/2012",
        maxDate: "12/31/2014",
        dateLimit: {
            days: 60
        },
        showDropdowns: !0,
        showWeekNumbers: !0,
        timePicker: !1,
        timePickerIncrement: 1,
        timePicker12Hour: !0,
        ranges: {
            Today: [moment(), moment()],
            Yesterday: [moment().subtract("days", 1), moment().subtract("days", 1)],
            "Last 7 Days": [moment().subtract("days", 6), moment()],
            "Last 30 Days": [moment().subtract("days", 29), moment()],
            "This Month": [moment().startOf("month"), moment().endOf("month")],
            "Last Month": [moment().subtract("month", 1).startOf("month"), moment().subtract("month", 1).endOf("month")]
        },
        opens: "left",
        buttonClasses: ["btn btn-default"],
        applyClass: "small bg-green",
        cancelClass: "small ui-state-default",
        format: "MM/DD/YYYY",
        separator: " to ",
        locale: {
            applyLabel: "Apply",
            fromLabel: "From",
            toLabel: "To",
            customRangeLabel: "Custom Range",
            daysOfWeek: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
            monthNames: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            firstDay: 1
        }
    }, function(a, b) {
        console.log("Callback has been called!"), $("#daterangepicker-custom span").html(a.format("MMMM D, YYYY") + " - " + b.format("MMMM D, YYYY"))
    }), $("#daterangepicker-custom span").html(moment().subtract("days", 29).format("MMMM D, YYYY") + " - " + moment().format("MMMM D, YYYY"))
}), $(function() {
    "use strict";
    $("#daterangepicker-custom-2").daterangepicker({
        startDate: moment().subtract("days", 29),
        endDate: moment(),
        minDate: "01/01/2012",
        maxDate: "12/31/2014",
        dateLimit: {
            days: 60
        },
        showDropdowns: !0,
        showWeekNumbers: !0,
        timePicker: !1,
        timePickerIncrement: 1,
        timePicker12Hour: !0,
        ranges: {
            Today: [moment(), moment()],
            Yesterday: [moment().subtract("days", 1), moment().subtract("days", 1)],
            "Last 7 Days": [moment().subtract("days", 6), moment()],
            "Last 30 Days": [moment().subtract("days", 29), moment()],
            "This Month": [moment().startOf("month"), moment().endOf("month")],
            "Last Month": [moment().subtract("month", 1).startOf("month"), moment().subtract("month", 1).endOf("month")]
        },
        opens: "left",
        buttonClasses: ["btn btn-default"],
        applyClass: "small bg-green",
        cancelClass: "small ui-state-default",
        format: "MM/DD/YYYY",
        separator: " to ",
        locale: {
            applyLabel: "Apply",
            fromLabel: "From",
            toLabel: "To",
            customRangeLabel: "Custom Range",
            daysOfWeek: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
            monthNames: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            firstDay: 1
        }
    }, function(a, b) {
        console.log("Callback has been called!"), $("#daterangepicker-custom-2 span").html(a.format("MMMM D, YYYY") + " - " + b.format("MMMM D, YYYY"))
    }), $("#daterangepicker-custom-2 span").html(moment().subtract("days", 29).format("MMMM D, YYYY") + " - " + moment().format("MMMM D, YYYY"))
}), $(function() {
    "use strict";
    $("#daterangepicker-custom-1").daterangepicker({
        startDate: moment().subtract("days", 29),
        endDate: moment(),
        minDate: "01/01/2012",
        maxDate: "12/31/2014",
        dateLimit: {
            days: 60
        },
        showDropdowns: !0,
        showWeekNumbers: !0,
        timePicker: !1,
        timePickerIncrement: 1,
        timePicker12Hour: !0,
        ranges: {
            Today: [moment(), moment()],
            Yesterday: [moment().subtract("days", 1), moment().subtract("days", 1)],
            "Last 7 Days": [moment().subtract("days", 6), moment()],
            "Last 30 Days": [moment().subtract("days", 29), moment()],
            "This Month": [moment().startOf("month"), moment().endOf("month")],
            "Last Month": [moment().subtract("month", 1).startOf("month"), moment().subtract("month", 1).endOf("month")]
        },
        opens: "left",
        buttonClasses: ["btn btn-default"],
        applyClass: "small bg-green",
        cancelClass: "small ui-state-default",
        format: "MM/DD/YYYY",
        separator: " to ",
        locale: {
            applyLabel: "Apply",
            fromLabel: "From",
            toLabel: "To",
            customRangeLabel: "Custom Range",
            daysOfWeek: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
            monthNames: ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
            firstDay: 1
        }
    }, function(a, b) {
        console.log("Callback has been called!"), $("#daterangepicker-custom-1 span").html(a.format("MMMM D, YYYY") + " - " + b.format("MMMM D, YYYY"))
    }), $("#daterangepicker-custom-1 span").html(moment().subtract("days", 29).format("MMMM D, YYYY") + " - " + moment().format("MMMM D, YYYY"))
});