
var TimeUtil;
/**
 * @title 时间工具类
 * @note 本类一律违规验证返回false
 * @date 2013-07-01
 * @formatter "2013-07-01 00:00:00" , "2013-07-01"
 */
TimeUtil = {
    makeDate : function (time) {
        if(isNaN(time) && typeof time != 'number'){
            var arr = time.split(/[- : \/]/);
            var date = new Date(arr[0], arr[1]-1, arr[2], arr[3], arr[4], arr[5])
            return date;
        }
        return new Date(time);
    },
    /**
     * 获取当前时间毫秒数
     */
    getCurrentMsTime : function() {
        var myDate = new Date();
        return myDate.getTime();
    },
    /**
     * 毫秒转时间格式(年月日)
     */
    longMsTimeConvertToDateTime : function(time) {
        var myDate = TimeUtil.makeDate(time);
        return this.formatterDate(myDate);
    },

    /**
     * 毫秒转时间格式(含时分秒)
     */
    longMsTimeToDateTime : function(time) {
        var myDate = TimeUtil.makeDate(time);
        return this.formatterDate2(myDate);
    },

    /**
     * 毫秒转时间格式(含时分)
     */
    longMsTimeToDateTime3 : function(time) {
        var myDate = TimeUtil.makeDate(time);
        return this.formatterDate3(myDate);
    },

    dateFormat : function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "H+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
 },

    /**
     * 时间格式转毫秒
     */
    dateToLongMsTime : function(date) {
        var myDate = new Date(date);
        return myDate.getTime();
    },
    /**
     * 格式化日期（不含时间）
     */
    formatterDate : function(date) {

        var datetime = date.getFullYear()
            + "-"// "年"
            + ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : "0"
            + (date.getMonth() + 1))
            + "-"// "月"
            + (date.getDate() < 10 ? "0" + date.getDate() : date
                .getDate());
        return datetime;
    },
    /**
     * 格式化日期（含时间"00:00:00"）
     */
    formatterDate2 : function(date) {
        var datetime = date.getFullYear()
            + "-"// "年"
            + ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : "0"
            + (date.getMonth() + 1))
            + "-"// "月"
            + (date.getDate() <= 10 ? "0" + date.getDate() : date
                .getDate())
            + " "
            + (date.getHours() < 10 ? "0" + date.getHours() : date
                .getHours())
            + ":"
            + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
                .getMinutes())
            + ":"
            + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
                .getSeconds());
        return datetime;
    },
    /**
     * 格式化日期（含时间"00:00"）
     */
    formatterDate3 : function(date) {
    var datetime = date.getFullYear()
        + "-"// "年"
        + ((date.getMonth() + 1) >= 10 ? (date.getMonth() + 1) : "0"
        + (date.getMonth() + 1))
        + "-"// "月"
        + (date.getDate() < 10 ? "0" + date.getDate() : date
            .getDate())
        + " "
        + (date.getHours() < 10 ? "0" + date.getHours() : date
            .getHours())
        + ":"
        + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
            .getMinutes());
    return datetime;
    },
    /**
     * 格式化去日期（含时间）
     */
    formatterDateTime : function(date) {
        var datetime = date.getFullYear()
            + "-"// "年"
            + ((date.getMonth() + 1) > 10 ? (date.getMonth() + 1) : "0"
            + (date.getMonth() + 1))
            + "-"// "月"
            + (date.getDate() < 10 ? "0" + date.getDate() : date
                .getDate())
            + " "
            + (date.getHours() < 10 ? "0" + date.getHours() : date
                .getHours())
            + ":"
            + (date.getMinutes() < 10 ? "0" + date.getMinutes() : date
                .getMinutes())
            + ":"
            + (date.getSeconds() < 10 ? "0" + date.getSeconds() : date
                .getSeconds());
        return datetime;
    },
    /**
     * 时间比较{结束时间大于开始时间}
     */
    compareDateEndTimeGTStartTime : function(startTime, endTime) {
        return ((new Date(endTime.replace(/-/g, "/"))) > (new Date(
            startTime.replace(/-/g, "/"))));
    },
    /**
     * 验证开始时间合理性{开始时间不能小于当前时间{X}个月}
     */
    compareRightStartTime : function(month, startTime) {
        var now = formatterDayAndTime(new Date());
        var sms = new Date(startTime.replace(/-/g, "/"));
        var ems = new Date(now.replace(/-/g, "/"));
        var tDayms = month * 30 * 24 * 60 * 60 * 1000;
        var dvalue = ems - sms;
        if (dvalue > tDayms) {
            return false;
        }
        return true;
    },
    /**
     * 验证开始时间合理性{结束时间不能小于当前时间{X}个月}
     */
    compareRightEndTime : function(month, endTime) {
        var now = formatterDayAndTime(new Date());
        var sms = new Date(now.replace(/-/g, "/"));
        var ems = new Date(endTime.replace(/-/g, "/"));
        var tDayms = month * 30 * 24 * 60 * 60 * 1000;
        var dvalue = sms - ems;
        if (dvalue > tDayms) {
            return false;
        }
        return true;
    },
    /**
     * 验证开始时间合理性{结束时间与开始时间的间隔不能大于{X}个月}
     */
    compareEndTimeGTStartTime : function(month, startTime, endTime) {
        var sms = new Date(startTime.replace(/-/g, "/"));
        var ems = new Date(endTime.replace(/-/g, "/"));
        var tDayms = month * 30 * 24 * 60 * 60 * 1000;
        var dvalue = ems - sms;
        if (dvalue > tDayms) {
            return false;
        }
        return true;
    },
    /**
     * 获取最近几天[开始时间和结束时间值,时间往前推算]
     */
    getRecentDaysDateTime : function(day) {
        var daymsTime = day * 24 * 60 * 60 * 1000;
        var yesterDatsmsTime = this.getCurrentMsTime() - daymsTime;
        var startTime = this.longMsTimeConvertToDateTime(yesterDatsmsTime);
        var pastDate = this.formatterDate2(new Date(startTime));
        var nowDate = this.formatterDate2(new Date());
        var obj = {
            startTime : pastDate,
            endTime : nowDate
        };
        return obj;
    },
    /**
     * 获取今天[开始时间和结束时间值]
     */
    getTodayDateTime : function() {
        var daymsTime = 24 * 60 * 60 * 1000;
        var tomorrowDatsmsTime = this.getCurrentMsTime() + daymsTime;
        var currentTime = this.longMsTimeConvertToDateTime(this.getCurrentMsTime());
        var termorrowTime = this.longMsTimeConvertToDateTime(tomorrowDatsmsTime);
        var nowDate = this.formatterDate2(new Date(currentTime));
        var tomorrowDate = this.formatterDate2(new Date(termorrowTime));
        var obj = {
            startTime : nowDate,
            endTime : tomorrowDate
        };
        return obj;
    },
    /**
     * 获取明天[开始时间和结束时间值]
     */
    getTomorrowDateTime : function() {
        var daymsTime = 24 * 60 * 60 * 1000;
        var tomorrowDatsmsTime = this.getCurrentMsTime() + daymsTime;
        var termorrowTime = this.longMsTimeConvertToDateTime(tomorrowDatsmsTime);
        var theDayAfterTomorrowDatsmsTime = this.getCurrentMsTime()+ (2 * daymsTime);
        var theDayAfterTomorrowTime = this.longMsTimeConvertToDateTime(theDayAfterTomorrowDatsmsTime);
        var pastDate = this.formatterDate2(new Date(termorrowTime));
        var nowDate = this.formatterDate2(new Date(theDayAfterTomorrowTime));
        var obj = {
            startTime : pastDate,
            endTime : nowDate
        };
        return obj;
    },

    //学时转换：秒转 分钟秒，例如 80 秒 = 1 分 20 秒
    getMinAndSec : function(intSeconds) {
      if(!intSeconds)
        return 0 + '分' + 0 + '秒';
      var intSeconds = parseInt(intSeconds);
      var intMin = Math.floor(intSeconds / 60);
      var intRemainSecond = intSeconds % 60;
      return intMin + '分' + intRemainSecond + '秒';
   },

  //学时转换：秒转 时分秒，例如 3700 秒 = 1 时 1 分 40 秒
  getHouAndMinAndSec : function(intSeconds) {
    if(!intSeconds)
      return 0 + '秒';
    var intSeconds = parseInt(intSeconds);
    var intHour = Math.floor(intSeconds/3600);
    var intRemainHour = intSeconds % 3600;
    var intMin = Math.floor(intRemainHour / 60);
    var intRemainSecond = intRemainHour % 60;
    if(intHour == 0 && intMin == 0){
      return intRemainSecond + '秒';
    }
    if(intHour == 0 && intMin != 0){
      return  intMin + '分' + intRemainSecond + '秒';
    }
    return intHour + '时' + intMin + '分' + intRemainSecond + '秒';
  },

  //学时转换：秒转 时，例如 3700 秒 = 1.03
  getHour : function(intSeconds) {
    if(!intSeconds)
      return 0;
    var intHour = intSeconds/3600;
    return intHour.toFixed(2);
  },

  getInteger : function(corectRate) {
    if(!corectRate)
      return 0 + '%';
    var remain = corectRate % 1;
    if(remain == 0){
      corectRate = parseInt(corectRate);
    }
    return corectRate + '%';
  }
};