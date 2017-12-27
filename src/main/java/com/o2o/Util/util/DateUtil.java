package com.o2o.Util.util;

import com.deppon.cubc.bill.client.utils.MagicNumber;
import com.deppon.cubc.web.common.constant.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 日期转换类
 *
 * @author tangyuhan
 * @version v1.0 2016-11-11 19:17:45
 */
public abstract class DateUtil {

	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);

	private static final Object lockObj = new Object();

	/** 存放不同的日期模板格式的sdf的Map */
	private static Map<String, ThreadLocal<SimpleDateFormat>> dateFormatMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();

	private static final String FORMAT = Constant.YYMMDD;
	//999毫秒
	public static final int timeHouSecond=999;
	/**
	 *<pre>
	 * 方法体说明：(方法详细描述说明、方法参数的具体涵义)
	 * 作者：405859
	 * 日期： 2017年2月10日 下午5:18:36
	 * @param date
	 * @param flag
	 * 0 返回yyyy-MM-dd 00:00:00日期<br>
	 * 1 返回yyyy-MM-dd 23:59:59日期 
	 * @return
	 *</pre>
	 */
	public static String getinputDate(Date date, int flag) {
		SimpleDateFormat myFmt2 = new SimpleDateFormat(Constant.YYMMDDHMS);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		long hour = cal.get(Calendar.HOUR_OF_DAY);
		long minute = cal.get(Calendar.MINUTE);
		long second = cal.get(Calendar.SECOND);
		// 时分秒（毫秒数）
		long millisecond = hour * MagicNumber.int_60 * MagicNumber.int_60 * MagicNumber.int_1000 + minute * MagicNumber.int_60 * MagicNumber.int_1000 + second * MagicNumber.int_1000;
		// 凌晨00:00:00
		cal.setTimeInMillis(cal.getTimeInMillis() - millisecond);
		if (flag == 0) {
			return myFmt2.format(cal.getTime());
		} else if (flag == 1) {
			// 凌晨23:59:59
			cal.setTimeInMillis(cal.getTimeInMillis() + MagicNumber.int_23 * MagicNumber.int_60 * MagicNumber.int_60 * MagicNumber.int_1000 + MagicNumber.int_59 * MagicNumber.int_60 * MagicNumber.int_1000 + MagicNumber.int_59 * MagicNumber.int_1000);
		}
		return myFmt2.format(cal.getTime());
	}
	/**
	 * Method formatDate
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static String formatDate(Date date, String format) {
		if (null == date) {
			return null;
		}
		return getDefaultDateFormat(format).format(date);
	}

	/**
	 * Method formatDate
	 *
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date) {
		return formatDate(date, FORMAT);
	}

	/**
	 * Method pareseDate
	 *
	 * @param date
	 * @param format
	 * @return
	 */
	public static Date pareseDate(String date, String format) {
		Date result = null;

		try {
			if (null == date) {
				return null;
			}
			result = getDefaultDateFormat(format).parse(date);
		} catch (ParseException e) {
			logger.error("Can't parse {} to Date", date);
		}

		return result;
	}


	public static final Date paraseStringToDate(String dateStr1){
		Date result = null;
		try {
			if (null == dateStr1) {
				return null;
			}
			result = getDefaultDateFormat(Constant.YYMMDDHMS).parse(dateStr1);
		} catch (ParseException e) {
			logger.error("Can't parse {} to Date", dateStr1);
		}
		return result;
	}

	/**
	 * Method pareseDate
	 *
	 * @param date
	 * @return
	 */
	public static Date pareseDate(String date) {
		return pareseDate(date, FORMAT);
	}

	/**
	 * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
	 *
	 * @param pattern
	 * @return
	 */
	private static SimpleDateFormat getDefaultDateFormat(final String pattern) {
		ThreadLocal<SimpleDateFormat> threadSimple = dateFormatMap.get(pattern);

		// 此处的双重判断和同步是为了防止dateFormatMap这个单例被多次put重复的sdf
		if (threadSimple == null) {
			synchronized (lockObj) {
				threadSimple = dateFormatMap.get(pattern);
				if (threadSimple == null) {
					// 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
					logger.debug("put new sdf of pattern " + pattern + " to map");
					// 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
					threadSimple = new ThreadLocal<SimpleDateFormat>() {

						@Override
						protected SimpleDateFormat initialValue() {
							logger.debug("thread: " + Thread.currentThread() + " init pattern: " + pattern);
							return new SimpleDateFormat(pattern);
						}
					};
					dateFormatMap.put(pattern, threadSimple);
				}
			}
		}
		return threadSimple.get();
	}

	/**
	 * 获取指定时间的那天 00:00:00.000 的时间
	 *
	 * @param date
	 * @return
	 */
	public static final Date dayBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 获取指定时间的那天 23:59:59.999 的时间
	 *
	 * @param date
	 * @return
	 */
	public static final Date dayEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, MagicNumber.int_23);
		c.set(Calendar.MINUTE, MagicNumber.int_59);
		c.set(Calendar.SECOND, MagicNumber.int_59);
		c.set(Calendar.MILLISECOND, timeHouSecond);
		return c.getTime();
	}

	/**
	 * 月首 零点00:00:00.000
	 *
	 * @param date
	 * @return
	 */
	public static final Date monthBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, 1);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/**
	 * 月末23:59:59.999
	 *
	 * @param date
	 * @return
	 */
	public static final Date monthEnd(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		c.set(Calendar.HOUR_OF_DAY, MagicNumber.int_23);
		c.set(Calendar.MINUTE, MagicNumber.int_59);
		c.set(Calendar.SECOND, MagicNumber.int_59);
		c.set(Calendar.MILLISECOND, timeHouSecond);
		return c.getTime();
	}
}