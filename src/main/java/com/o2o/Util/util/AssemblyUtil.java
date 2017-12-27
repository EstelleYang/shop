package com.o2o.Util.util;

import com.deppon.cubc.asset.client.paycenter.model.PayInfoDetailDO;
import net.sourceforge.pinyin4j.PinyinHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 排序、字符转换类
 *
 * @author tangyuhan 349910
 * @version v1.0
 * @date Date:2016/12/8 22:53 下午
 */
public class AssemblyUtil {

	private final static Logger logger = LoggerFactory.getLogger(AssemblyUtil.class);

	private AssemblyUtil() {
	}

	/**
	 * 驼峰写法转换成 "_"小写
	 *
	 * @param fieldName
	 * @return
	 */
	public static String converHump(String fieldName) {
		if (StringUtils.isBlank(fieldName)) {
			return "";
		}
		fieldName = String.valueOf(fieldName.charAt(0)).toUpperCase().concat(fieldName.substring(1));
		StringBuilder sb = new StringBuilder();
		Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
		Matcher matcher = pattern.matcher(fieldName);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(word.toLowerCase());
			sb.append(matcher.end() == fieldName.length() ? "" : "_");
		}
		return sb.toString();
	}

	/**
	 * 汉字、字母(首字母ASCII)，数字(大小)排序
	 *
	 * @param list
	 * @param sortField
	 * @param sortType
	 */
	public static final void comparatorField(Collection<?> list, String sortField, final String sortType) {
		if (null == list || list.size() < 1) {
			throw new IllegalArgumentException("the list must not be null Or the number is less than 1");
		}
		if (StringUtils.isBlank(sortField) || StringUtils.isBlank(sortType)) {
			return;
		}
		final String fieldName = StringUtils.capitalize(sortField);
		// 使根据指定比较器产生的顺序对指定对象数组进行排序。
		Collections.sort((List<Object>) list, new Comparator<Object>() {
			@Override
			public int compare(Object o1, Object o2) {
				try {
					Method m1 = o1.getClass().getMethod("get" + fieldName);

					Object obj1 = m1.invoke(o1);
					Object obj2 = m1.invoke(o2);
					if (m1.getReturnType() == Date.class) {
						Date d1 = (Date) obj1;
						Date d2 = (Date) obj2;
						if (d1 == null) {
							return 1;
						}
						if (d2 == null) {
							return -1;
						}
						if ("desc".equals(sortType)) {
							return d2.compareTo(d1);
						}
						return d1.compareTo(d2);
					}


					String v1 = String.valueOf(obj1);
					if(isChineseByName(v1)){
						v1 = getPinYinHeadChar((v1.length() > 1 ? v1.substring(0,1) : v1));
					}
					if (StringUtils.isBlank(v1)) {
						return 1;
					}
					String v2 = String.valueOf(obj2);
					if(isChineseByName(v2)){
						v2 = getPinYinHeadChar((v2.length() > 1 ? v2.substring(0,1) : v2));
					}
//					v2 = v2.length() > 1 ? v2.substring(0,1) : v2;
					if (StringUtils.isBlank(v2)) {
						return -1;
					}

					if (m1.getReturnType() == int.class || m1.getReturnType() == BigDecimal.class || m1.getReturnType() == Integer.class
							|| m1.getReturnType() == long.class) {

						BigDecimal i1 = new BigDecimal(v1);
						BigDecimal i2 = new BigDecimal(v2);
						if ("desc".equals(sortType)) {
							return i2.compareTo(i1);
						}
						return i1.compareTo(i2);
					}

					if ("desc".equals(sortType)) {
						return v2.compareTo(v1);
					}
					return v1.compareTo(v2);
				} catch (Exception e) {
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				return 0;
			}
		});
	}

	// 只能判断部分CJK字符（CJK统一汉字）
	public static boolean isChineseByName(String str) {
		if (str == null) {
			return false;
		}
		Pattern pattern = Pattern.compile("[\\u4E00-\\u9FBF]+");
		return pattern.matcher(str.trim()).find();
	}

	// 返回中文的首字母
	public static String getPinYinHeadChar(String str) {
		String convert = null;
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}


	public static void main(String[] args) {
		List<PayInfoDetailDO> list = new ArrayList<PayInfoDetailDO>();
		PayInfoDetailDO p1 = new PayInfoDetailDO();
		RequestDataUtil.automaticProduction(p1);
		p1.setPayCode("储");
		PayInfoDetailDO p2 = new PayInfoDetailDO();
		RequestDataUtil.automaticProduction(p2);
		p2.setPayCode("有");

		PayInfoDetailDO p3 = new PayInfoDetailDO();
		RequestDataUtil.automaticProduction(p3);
		p3.setPayCode("啊");

		PayInfoDetailDO p4 = new PayInfoDetailDO();
		RequestDataUtil.automaticProduction(p4);
		p4.setPayCode(null);

		list.add(p2);
		list.add(p4);
		list.add(p1);
		list.add(p3);
		for (PayInfoDetailDO p : list) {
			System.out.println(p.getPayCode());
		}
		comparatorField(list, "PayCode", "1");
		System.out.println();

		for (PayInfoDetailDO p : list) {
			System.out.println(p.getPayCode());
		}

	}
}
