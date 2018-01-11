package com.bossien.train.util;

import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * <p>日期,时间工具类</p>
 * 
 * @author mo.xf
 *
 */
public class DateUtils {
	private static  Logger logger = Logger.getLogger(DateUtils.class);
	public static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String TIME_FORMAT = "HH:mm:ss";
	public static String DATETIMEFORMAT = "yyyyMMddHHmmss";

	/** 日期 */
	//public final static String DEFAILT_DATE_PATTERN = "yyyy-MM-dd";
	/** 日期时间 */
	//public final static String DEFAILT_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 时间 */
	//public final static String DEFAULT_TIME_PATTERN = "HH:mm:ss";
	/**
	 * 每天的毫秒数
	 */
	public final static long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

	/**
	 * Get the previous time, from how many days to now.
	 * 
	 * @param days
	 *            How many days.
	 * @return The new previous time.
	 */
	public static Date previous(int days) {
		return new Date(System.currentTimeMillis() - days * MILLIS_IN_DAY);
	}
	
	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(Date d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}

	public static String formatDateTime() {
		return new SimpleDateFormat(DATETIMEFORMAT).format(new Date());
	}

	/**
	 * Convert date and time to string like "yyyy-MM-dd HH:mm".
	 */
	public static String formatDateTime(long d) {
		return new SimpleDateFormat(DATETIME_FORMAT).format(d);
	}
	
	public static String formatDateTime(long d , String format) {
		return new SimpleDateFormat(format).format(d);
	}

	/**
	 * Parse date like "yyyy-MM-dd".
	 */
	public static Date parseDate(String d) {
		try {
			return new SimpleDateFormat(DATE_FORMAT).parse(d);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Parse date and time like "yyyy-MM-dd hh:mm".
	 */
	public static Date parseDateTime(String dt) {
		try {
			return new SimpleDateFormat(DATETIME_FORMAT).parse(dt);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return null;
	}

	public static String second2HHmmss_ZH_CN(Long second) {
		if(null == second || 0 >= second.longValue()) {
            return "00时00分00秒";
        }
		try {
			long temp = 0;
			StringBuilder sb = new StringBuilder();
			temp = second / 3600;
			sb.append((temp < 10) ? "0" + temp + "时":"" + temp + "时");
			temp = second % 3600 / 60;
			sb.append((temp < 10) ? "0" + temp + "分":"" + temp + "分");
			temp = second % 3600 % 60;
			sb.append((temp < 10) ? "0" + temp + "秒": "" + temp + "秒");
			return sb.toString();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return "00时00分00秒";
	}

	/**
	 * 转换日期字符串得到指定格式的日期类型
	 * 
	 * @param formatString
	 *            需要转换的格式字符串
	 * @param targetDate
	 *            需要转换的时间
	 * @return
	 * @throws ParseException
	 */
	public static final Date convertString2Date(String formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isEmpty(targetDate)) {
            return null;
        }
		SimpleDateFormat format = null;
		Date result = null;
		format = new SimpleDateFormat(formatString);
		try {
			result = format.parse(targetDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	public static final Date convertString2Date(String[] formatString,
			String targetDate) throws ParseException {
		if (StringUtils.isEmpty(targetDate)) {
			return null;
		}
		SimpleDateFormat format = null;
		Date result = null;
		String errorMessage = null;
		Integer errorOffset = null;
		for (String dateFormat : formatString) {
			try {
				format = new SimpleDateFormat(dateFormat);
				result = format.parse(targetDate);
			} catch (ParseException pe) {
				result = null;
				errorMessage = pe.getMessage();
				errorOffset = pe.getErrorOffset();
			} finally {
				if (result != null && result.getTime() > 1) {
					break;
				}
			}
		}
		if (result == null) {
			throw new ParseException(errorMessage, errorOffset);
		}
		return result;
	}

	/**
	 * 转换字符串得到默认格式的日期类型
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date convertString2Date(String strDate) throws ParseException {
		Date result = null;
		try {
			result = convertString2Date(DATE_FORMAT, strDate);
		} catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return result;
	}

	/**
	 * 转换日期得到指定格式的日期字符串
	 * 
	 * @param formatString
	 *            需要把目标日期格式化什么样子的格式。例如,yyyy-MM-dd HH:mm:ss
	 * @param targetDate
	 *            目标日期
	 * @return
	 */
	public static String convertDate2String(String formatString, Date targetDate) {
		SimpleDateFormat format = null;
		String result = null;
		if (targetDate != null) {
			format = new SimpleDateFormat(formatString);
			result = format.format(targetDate);
		} else {
			return null;
		}
		return result;
	}

	/**
	 * 转换日期,得到默认日期格式字符串
	 * 
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2String(Date targetDate) {
		return convertDate2String(DATE_FORMAT, targetDate);
	}

	/**
	 * 转换日期,得到当前日期格式字符串
	 *
	 * @return
	 */
	public static String currentDateString() {
		return convertDate2String(DATE_FORMAT, new Date());
	}

	/**
	 * 转换日期,得到默认日期格式字符串
	 *
	 * @param targetDate
	 * @return
	 */
	public static String convertDate2StringTime(Date targetDate) {
		return convertDate2String(DATETIME_FORMAT, targetDate);
	}

	/**
	 * 比较日期大小
	 * 
	 * @param src
	 * @param src
	 * @return int; 1:DATE1>DATE2;
	 */
	public static int compare_date(Date src, Date src1) {

		String date1 = convertDate2String(DATETIME_FORMAT, src);
		String date2 = convertDate2String(DATETIME_FORMAT, src1);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date dt1 = df.parse(date1);
			Date dt2 = df.parse(date2);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return 0;
	}

	/**
	 * 日期比较
	 * 
	 * 判断时间date1是否在时间date2之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @param
	 * @return
	 */
	public static boolean isDateBefore(String date1, String date2) {
		try {
			DateFormat df = DateFormat.getDateTimeInstance();
			return df.parse(date1).before(df.parse(date2));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 日期比较
	 * 
	 * 判断当前时间是否在时间date2之前 <br/>
	 * 时间格式 2005-4-21 16:16:34 <br/>
	 * 添加人：胡建国
	 * 
	 * @param
	 * @return
	 */
	public static boolean isDateBefore(String date2) {
		if (date2 == null) {
			return false;
		}
		try {
			Date date1 = new Date();
			DateFormat df = DateFormat.getDateTimeInstance();
			return date1.before(df.parse(date2));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 比较当前时间与时间date2的天相等 时间格式 2008-11-25 16:30:10 如:当前时间是2008-11-25
	 * 16:30:10与传入时间2008-11-25 15:31:20 相比较,返回true即相等
	 * 
	 * @param
	 * @param date2
	 * @return boolean; true:相等
	 * @author zhangjl
	 */
	public static boolean equalDate(String date2) {
		try {
			String date1 = convertDate2String(DATETIME_FORMAT,
					new Date());
			date1.equals(date2);
			Date d1 = convertString2Date(DATE_FORMAT, date1);
			Date d2 = convertString2Date(DATE_FORMAT, date2);
			return d1.equals(d2);
		} catch (ParseException e) {
			logger.error(e.getLocalizedMessage());
			return false;
		}
	}

	/**
	 * 比较时间date1与时间date2的天相等 时间格式 2008-11-25 16:30:10
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:相等
	 * @author zhangjl
	 */
	public static boolean equalDate(String date1, String date2) {
		try {

			Date d1 = convertString2Date(DATE_FORMAT, date1);
			Date d2 = convertString2Date(DATE_FORMAT, date2);

			return d1.equals(d2);
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 比较时间date1是否在时间date2之前 时间格式 2008-11-25 16:30:10
	 * 
	 * @param date1
	 * @param date2
	 * @return boolean; true:在date2之前
	 * @author 胡建国
	 */
	public static boolean beforeDate(String date1, String date2) {
		try {
			Date d1 = convertString2Date(DATE_FORMAT, date1);
			Date d2 = convertString2Date(DATE_FORMAT, date2);
			return d1.before(d2);
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 获取上个月开始时间
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return 上个月的第一天
	 */
	public static Date getBoferBeginDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), (currentDate
				.get(Calendar.MONTH)) - 1, result
				.getActualMinimum(Calendar.DATE), 0, 0, 0);
		return result.getTime();
	}

	/**
	 * 获取指定月份的第一天
	 * 
	 * @param currentDate
	 * @return
	 */
	public static Date getBeginDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), (currentDate
				.get(Calendar.MONTH)), result.getActualMinimum(Calendar.DATE));
		return result.getTime();
	}


	/**
	 * 获取上个月结束时间
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return 上个月最后一天
	 */
	public static Date getBoferEndDate(Calendar currentDate) {
		Calendar result = currentDate;
		// result.set(currentDate.get(Calendar.YEAR), currentDate
		// .get(Calendar.MONTH) - 1);
		result.set(Calendar.DATE, 1);
		result.add(Calendar.DATE, -1);
		return result.getTime();
	}

	/**
	 * 获取两个时间的时间间隔
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getDaysBetween(Calendar beginDate, Calendar endDate) {
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int days = endDate.get(Calendar.DAY_OF_YEAR)
				- beginDate.get(Calendar.DAY_OF_YEAR) + 1;
		int year = endDate.get(Calendar.YEAR);
		if (beginDate.get(Calendar.YEAR) != year) {
			beginDate = (Calendar) beginDate.clone();
			do {
				days += beginDate.getActualMaximum(Calendar.DAY_OF_YEAR);
				beginDate.add(Calendar.YEAR, 1);
			} while (beginDate.get(Calendar.YEAR) != year);
		}
		return days;
	}

	/**
	 * 获取两个时间的时间间隔(月份)
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getMonthsBetween(Date beginDate, Date endDate) {
		if (beginDate.after(endDate)) {
			Date swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int months = endDate.getMonth() - beginDate.getMonth();
		int years = endDate.getYear() - beginDate.getYear();

		months += years * 12;

		return months;
	}

	/**
	 * 获取两个时间内的工作日
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getWorkingDay(Calendar beginDate, Calendar endDate) {
		int result = -1;
		if (beginDate.after(endDate)) {
			Calendar swap = beginDate;
			beginDate = endDate;
			endDate = swap;
		}
		int charge_start_date = 0;
		int charge_end_date = 0;
		int stmp;
		int etmp;
		stmp = 7 - beginDate.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - endDate.get(Calendar.DAY_OF_WEEK);
		if (stmp != 0 && stmp != 6) {
			charge_start_date = stmp - 1;
		}
		if (etmp != 0 && etmp != 6) {
			charge_end_date = etmp - 1;
		}
		result = (getDaysBetween(getNextMonday(beginDate),
				getNextMonday(endDate)) / 7)
				* 5 + charge_start_date - charge_end_date;
		return result;
	}

	/**
	 * 根据当前给定的日期获取当前天是星期几(中国版的)
	 * 
	 * @param date
	 *            任意时间
	 * @return
	 */
	public static String getChineseWeek(Calendar date) {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五",
				"星期六" };
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];

	}

	/**
	 * 获得日期的下一个星期一的日期
	 * 
	 * @param date
	 *            任意时间
	 * @return
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result = null;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * 获取两个日期之间的休息时间
	 * 
	 * @param beginDate
	 *            开始时间
	 * @param endDate
	 *            结束时间
	 * @return
	 */
	public static int getHolidays(Calendar beginDate, Calendar endDate) {
		return getDaysBetween(beginDate, endDate)
				- getWorkingDay(beginDate, endDate);

	}

	public static boolean isDateEnable(Date beginDate, Date endDate,
			Date currentDate) {
		// 开始日期
		long beginDateLong = beginDate.getTime();
		// 结束日期
		long endDateLong = endDate.getTime();
		// 当前日期
		long currentDateLong = currentDate.getTime();
		if (currentDateLong >= beginDateLong && currentDateLong <= endDateLong) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 获取当前月份的第一天
	 * 
	 * @param
	 *
	 * @return
	 */
	public static Date getMinDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), currentDate
				.get(Calendar.MONTH), currentDate
				.getActualMinimum(Calendar.DATE));
		return result.getTime();
	}

	public static Calendar getDate(int year, int month, int date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month, date);
		return calendar;
	}

	public static Calendar getDate(int year, int month) {
		return getDate(year, month, 0);
	}

	public static Date getCountMinDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, calendar.getActualMinimum(Calendar.DATE));
		return calendar.getTime();
	}

	public static Date getCountMaxDate(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.set(calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), 0);
		return calendar2.getTime();
	}

	/**
	 * 获取当前月份的第一天
	 * 
	 * @param
	 *
	 * @return
	 */
	public static Date getMinDate() {
		Calendar currentDate = Calendar.getInstance();
		return getMinDate(currentDate);
	}

	/**
	 * 获取当前月分的最大天数
	 * 
	 * @param currentDate
	 *            当前时间
	 * @return
	 */
	public static Date getMaxDate(Calendar currentDate) {
		Calendar result = Calendar.getInstance();
		result.set(currentDate.get(Calendar.YEAR), currentDate
				.get(Calendar.MONTH), currentDate
				.getActualMaximum(Calendar.DATE));
		return result.getTime();
	}

	/**
	 * 获取当前月分的最大天数
	 * 
	 * @param
	 *
	 * @return
	 */
	public static Date getMaxDate() {
		Calendar currentDate = Calendar.getInstance();
		return getMaxDate(currentDate);
	}

	/**
	 * 获取今天最大的时间
	 * 
	 * @return
	 */
	public static String getMaxDateTimeForToDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		return convertDate2String(DATETIME_FORMAT, calendar.getTime());
	}

	/**
	 * 获取日期最大的时间
	 * 
	 * @return
	 */
	public static Date getMaxDateTimeForToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMaximum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * 获取今天最小时间
	 * 
	 * @return
	 */
	public static String getMinDateTimeForToDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		return convertDate2String(DATETIME_FORMAT, calendar.getTime());
	}

	/**
	 * 获取 date 最小时间
	 * 
	 * @return
	 */
	public static Date getMinDateTimeForToDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, calendar
				.getMinimum(Calendar.HOUR_OF_DAY));
		calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
		calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
		return calendar.getTime();
	}

	/**
	 * 获取发生日期的结束时间 根据用户设置的日期天数来判定这这个日期是什么(例如 (getHappenMinDate = 2008-10-1) 的话
	 * 那么 (getHappenMaxDate = 2008-11-1) 号)
	 * 
	 * @return
	 */
	public static Date getHappenMaxDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		return calendar.getTime();
	}

	/**
	 * 加减天数
	 * 
	 * @param num
	 * @param Date
	 * @return
	 */
	public static Date addDay(int num, Date Date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Date);
		calendar.add(Calendar.DATE, num);// 把日期往后增加 num 天.整数往后推,负数往前移动
		return calendar.getTime(); // 这个时间就是日期往后推一天的结果
	}
	
	/**
	 * 加减天数
	 * 
	 * @param num
	 * @param
	 * @return
	 */
	public static String addDay(int num, String date) {
		Date d = DateUtils.addDay(num, DateUtils.parseDate(date)) ;
		return DateUtils.formatDateTime(d);
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(getMaxDateTimeForToDay());
	 * System.out.println(getMinDateTimeForToDay()); }
	 */

	/**
	 * 计算两端时间的小时差
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getHour(Date begin, Date end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(begin);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		Long millisecond = c2.getTimeInMillis() - c1.getTimeInMillis();
		Long hour = millisecond / 1000 / 60 / 60;
		Long minute = (millisecond / 1000 / 60) % 60;
		if (minute >= 30) {
			hour++;
		}

		return hour.intValue();
	}
	
	/**
	 * 计算两端时间的分钟差
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int getMinute(Date begin, Date end) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(begin);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(end);
		Long millisecond = c2.getTimeInMillis() - c1.getTimeInMillis();
		Long minute = millisecond / 1000 / 60;
		
		return minute.intValue();
	}

	/**
	 * 格式化日期
	 */
	public static String dateFormat(Date date) {
		if (date == null) {
			return null;
		}
		return DateFormat.getDateInstance().format(date);
	}

	/**
	 * @see
	 * @return String
	 * @throws ParseException
	 */
	public static String setDateFormat(Date myDate, String strFormat)
			throws ParseException {
		if (myDate == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);
		return sDate;
	}

	public static String setDateFormat(String myDate, String strFormat)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(strFormat);
		String sDate = sdf.format(myDate);

		return sDate;
	}

	/*****************************************
	 * @功能 计算某年某月的结束日期
	 * @return interger
	 * @throws ParseException
	 ****************************************/
	public static String getYearMonthEndDay(int yearNum, int monthNum)
			throws ParseException {

		// 分别取得当前日期的年、月、日
		String tempYear = Integer.toString(yearNum);
		String tempMonth = Integer.toString(monthNum);
		String tempDay = "31";
		if (tempMonth.equals("1") || tempMonth.equals("3")
				|| tempMonth.equals("5") || tempMonth.equals("7")
				|| tempMonth.equals("8") || tempMonth.equals("10")
				|| tempMonth.equals("12")) {
			tempDay = "31";
		}
		if (tempMonth.equals("4") || tempMonth.equals("6")
				|| tempMonth.equals("9") || tempMonth.equals("11")) {
			tempDay = "30";
		}
		if (tempMonth.equals("2")) {
			if (isLeapYear(yearNum)) {
				tempDay = "29";
			} else {
				tempDay = "28";
			}
		}
		String tempDate = tempYear + "-" + tempMonth + "-" + tempDay;
		return tempDate;// setDateFormat(tempDate,"yyyy-MM-dd");
	}

	/*****************************************
	 * @功能 判断某年是否为闰年
	 * @return boolean
	 * @throws ParseException
	 ****************************************/
	public static boolean isLeapYear(int yearNum) {
		boolean isLeep = false;
		/** 判断是否为闰年，赋值给一标识符flag */
		if ((yearNum % 4 == 0) && (yearNum % 100 != 0)) {
			isLeep = true;
		} else if (yearNum % 400 == 0) {
			isLeep = true;
		} else {
			isLeep = false;
		}
		return isLeep;
	}

	/**
	 * 格式化日期
	 * 
	 * @throws ParseException
	 * 
	 *             例: DateUtils.formatDate("yyyy-MM-dd HH",new Date())
	 *             "yyyy-MM-dd HH:00:00"
	 */
	public static Date formatDate(String formatString, Date date)
			throws ParseException {
		if (date == null) {
			date = new Date();
		}
		if (StringUtils.isEmpty(formatString)) {
            formatString = DateUtils.DATE_FORMAT;
        }

		date = DateUtils.convertString2Date(formatString, DateUtils
				.convertDate2String(formatString, date));

		return date;
	}

	/**
	 * 格式化日期 yyyy-MM-dd
	 * 
	 * @throws ParseException
	 *             例： DateUtils.formatDate(new Date()) "yyyy-MM-dd 00:00:00"
	 */
	public static Date formatDate(Date date) throws ParseException {
		date = formatDate(DateUtils.DATE_FORMAT, date);
		return date;
	}

	/**
	 * @throws ParseException
	 *             根据日期获得 星期一的日期
	 * 
	 */
	public static Date getMonDay(Date date) throws ParseException {

		Calendar cal = Calendar.getInstance();
		if (date == null) {
            date = new Date();
        }
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            cal.add(Calendar.WEEK_OF_YEAR, -1);
        }

		cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

		date = formatDate(cal.getTime());

		return date;
	}

	/**
	 * @throws ParseException
	 *             根据日期获得 星期日 的日期
	 * 
	 */
	public static Date getSunDay(Date date) throws ParseException {

		Calendar cal = Calendar.getInstance();
		if (date == null) {
            date = new Date();
        }
		cal.setTime(date);
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            cal.add(Calendar.WEEK_OF_YEAR, 1);
        }

		cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

		date = formatDate(cal.getTime());
		return date;
	}

	/**
	 * 获得 下个月的第一天
	 * 
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date getNextDay(Date date) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, 1);
		cal.set(Calendar.DATE, 1);
		return formatDate(cal.getTime());
	}

	/**
	 * 测试
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
//		Date date = DateUtils.parseDate("2014-1-7") ;
//		System.out.println(DateUtils.formatDateTime(date));
//		System.out.println(DateUtils.formatDateTime(new Date().getTime(),DateUtils.DATE_FORMAT) );

//		Long second = 407L;

//		System.out.println(DateUtils.isDateBefore("2017-09-07 23:59:59","2017-09-08 00:00:00"));

		// 有效的userId集合
		List<String> valids = Lists.newArrayList();
		// 构造数据 1 2 3 5 7 9
		valids.add("1");valids.add("2");valids.add("3");valids.add("5");valids.add("7");valids.add("9");
		// project user
		List<String> valids2 = Lists.newArrayList();
		// 构造数据 2 4 5 8 9
		valids2.add("2");valids2.add("4");valids2.add("5");valids2.add("8");valids2.add("9");

		Integer len1 = valids2.size();
//			valids2.removeAll(valids);
		// 4 8
//		System.out.println(valids2);
		Integer len2 = valids2.size();

//		System.out.println(len1 - len2);

		List<String> validsNew = Lists.newCopyOnWriteArrayList(valids);
		System.out.println("valids=======" + valids);
		System.out.println("validsNew=======" + validsNew);
		System.out.println("valids2=======" + valids2);

		System.out.println("------------------------------------------------");
		validsNew.removeAll(valids2);

		System.out.println("validsNew.removeAll(valids2)=======" + validsNew);

		valids.removeAll(validsNew);

//		valids.retainAll(validsNew);
		System.out.println("valids.removeAll(validsNew)=======" + valids);

		System.out.println(UUID.randomUUID());
	}


	// 获取下个月
	public static Date getNextMouthDay(Date date) throws ParseException{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +31);
		return calendar.getTime();
	}

	// 获取下一天
	public static Date getNextDay1(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1);
		return calendar.getTime();
	}


	/**
	 * 三个日期相比取最近
	 * @param d1
	 * @param d2
	 * @param d3
	 * @return 最近日期
	 */
	public static String getBigDate(String d1,String d2,String d3) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date a =null ;Date b = null;Date c = null;
		if(org.apache.commons.lang.StringUtils.isNotEmpty(d1)){a = sdf.parse(d1);}
		if(org.apache.commons.lang.StringUtils.isNotEmpty(d2)){b = sdf.parse(d2);}
		if(org.apache.commons.lang.StringUtils.isNotEmpty(d3)){c = sdf.parse(d3);}
		Date res = null;
		if(null != a && null != b && null != c){
			res = a.after(b)?(a.after(c)?a:c):(b.after(c)?b:c);
		}else if(null == a && null != b && null != c){
			res = b.after(c)?b:c;
		}else if(null != a && null == b && null != c){
			res = a.after(c)?a:c;
		}else if(null != a && null != b && null == c){
			res = a.after(b)?a:b;
		}else if(null != a && null == b && null == c){
			res = a;
		}else if(null == a && null != b && null == c){
			res = b;
		}else if(null == a && null == b && null != c){
			res = c;
		}
		return sdf.format(res);
	}

	/**
	 * 三个日期相比取最远
	 * @param d1
	 * @param d2
	 * @param d3
	 * @return 最远日期
	 */
	public static String getMinDate(String d1,String d2,String d3) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date a =null ;Date b = null;Date c = null;
		if(org.apache.commons.lang.StringUtils.isNotEmpty(d1)){a = sdf.parse(d1);}
		if(org.apache.commons.lang.StringUtils.isNotEmpty(d2)){b = sdf.parse(d2);}
		if(org.apache.commons.lang.StringUtils.isNotEmpty(d3)){c = sdf.parse(d3);}
		Date res = null;
		if(null != a && null != b && null != c){
			res = a.before(b)?(a.before(c)?a:c):(b.before(c)?b:c);
		}else if(null == a && null != b && null != c){
			res = b.before(c)?b:c;
		}else if(null != a && null == b && null != c){
			res = a.before(c)?a:c;
		}else if(null != a && null != b && null == c){
			res = a.before(b)?a:b;
		}else if(null != a && null == b && null == c){
			res = a;
		}else if(null == a && null != b && null == c){
			res = b;
		}else if(null == a && null == b && null != c){
			res = c;
		}
		return sdf.format(res);
	}

	/**
	 * 获取项目状态值
	 * @param d2
	 * @param projectStatus
	 * @return
	 * @throws ParseException
	 */
	public static String getProjectStatus(String d1, String d2, String projectStatus) throws ParseException{
		String projectStatus_ = projectStatus;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date beginTime = sdf.parse(d1);
		Date endTime = sdf.parse(d2);
		Date date = new Date();
		Date date2 = beginTime.before(date)?beginTime:date;
		Date date1 = endTime.after(date)?endTime:date;
		if("1".equals(projectStatus) && beginTime== date2 && endTime == date1){
			projectStatus_="3";
		}
		if("2".equals(projectStatus) && beginTime== date2 && endTime == date1){
			projectStatus_="3";
		}
		if("2".equals(projectStatus) && date == date1){
			projectStatus_="4";
		}
		if("3".equals(projectStatus) && date == date1){
			projectStatus_="4";
		}
		return projectStatus_;
	}

	/**
	 * 转换成分、秒
	 * @param time 秒为单位
	 * @return
	 */
	public static String getMinStr(long time){
		long min = time/60%60;
		long second = time%60;

		return min + "分" + second + "秒";
	}

	/**
	 * 转换成时、分、秒
	 * @param time 秒为单位
	 * @return
	 */
	public static String getHourStr(long time){
		long hour = time/60/60;
		long min = time/60%60;
		long second = time%60;

		return hour + "时" + min + "分" + second + "秒";
	}

	/**
	 * 转换成分、秒
	 * @param time 秒为单位
	 * @return
	 */
	public static Double getMinDouble(long time){

		return Double.parseDouble(new DecimalFormat("0.00").
				format(time / 60.0));
	}

	/**
	 * 当前时间-毫秒格式
	 * @return Long
	 */
    public static Long getCurrTimeInMillis() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		return calendar.getTimeInMillis();
	}

	/**
	 * 获取毫秒数时间
	 * @param date
	 * @return Long
	 */
	public static Long getTimeInMillis(Date date) {
		if(null == date) {
            return 0L;
        }
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getTimeInMillis();
	}

}
