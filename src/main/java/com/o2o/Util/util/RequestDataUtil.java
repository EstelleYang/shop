package com.o2o.Util.util;

import com.deppon.cubc.web.common.constant.Constant;
import com.deppon.cubc.web.common.constant.NumberConstants;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * 请求数据处理
 * @ClassName: RequestData
 * @author 349910-foss-tangyuhan
 * @date 2016年9月12日 下午3:31:23
 *
 */
public class RequestDataUtil {

	private static final String SETTER_PREFIX = "set";

	private static final String GETTER_PREFIX = "get";

	private static final String GETRIDOF_SERIAUID = "SerialVersionUID";

	/**
	 * 校验必填字段  notField(排除不校验属性)
	 * @Title: ExpressPartSalesDeptProcessor.java
	 * @author 349910-foss-tangyuhan
	 * @date 2016年9月12日 下午3:11:14
	 * @version
	 */
	public static String checkRequData(Object obj,String...notFields) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
		StringBuilder builder = new StringBuilder();
		/**
		 * 获取对象里所有的属性，不包括父类
		 */
		Field[] fies = obj.getClass().getDeclaredFields();
		for (int i = NumberConstants.NUMERAL_ZORE; i < fies.length; i++) {
			/**
			 * 获取属性名称并首字母大写
			 */
			String name = fies[i].getName().substring(NumberConstants.NUMERAL_ZORE,NumberConstants.NUMBER_1000).toUpperCase()+fies[i].getName().substring(NumberConstants.NUMBER_1000);

			/**
			 * 默认不校验的属性 (首字母大写)
			 */
			String [] notFinds =
				{"SerialVersionUID","CreateTime","ModifyTime","VersionNo","Active"};

			/**
			 * 排序某些属性(首字母大写)  SerialVersionUID 默认排除
			 */
			if (!Arrays.asList(notFields).contains(name) && !Arrays.asList(notFinds).contains(name)) {
				/**
				 * 调用getter方法
				 */
				Method  m = obj.getClass().getMethod(GETTER_PREFIX+name);
				Object value = m.invoke(obj);
				/**
				 * 判断String-->null     Integer -- > NumberConstants.NUMERAL_ZORE
				 */
				if (null == value || "NumberConstants.NUMERAL_ZORE".equals(value)) {
					builder.append(name + ",");
				}
			}
		}
		/**
		 *  非空验证
		 */
		if (StringUtils.isNotBlank(builder.toString())) {
			/**
			 *  声明StringBuilder对象
			 */
			StringBuilder msg = new StringBuilder();
			/**
			 *  返回错误信息
			 */
			msg.append("10008")
					.append(",必填字段：").append(builder);
			/**
			 *  把消息类型转换成String
			 */
			return msg.toString();
		}
		// 返回为空
		return null;
	}

	/**
	 * turnInto(转成对象) -- > return, beforeTheTurn --> 转前对象，     notField 不需要赋值的属性
	 * @Title: RequestData.java
	 * @author 349910-foss-tangyuhan
	 * @date 2016年9月12日 下午4:39:01
	 * @version
	 */
	public static Object changeDataToObject(Object turnInto,Object beforeTheTurn,String...notFields) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException{
		if (null == beforeTheTurn) {
			return null;
		}
		//需要转成的对象属性
		Field[] turnFies = turnInto.getClass().getDeclaredFields();
		//转前对象属性
		Field[] beforeFies = beforeTheTurn.getClass().getDeclaredFields();
		//遍历转成对象属性
		for (int i = NumberConstants.NUMERAL_ZORE; i < turnFies.length; i++) {
			/**
			 * 获取转成对象属性名称并首字母大写
			 */
			String turnName = turnFies[i].getName().substring(NumberConstants.NUMERAL_ZORE,NumberConstants.NUMBER_1000).toUpperCase()+turnFies[i].getName().substring(NumberConstants.NUMBER_1000);

			/**
			 * 排序某些属性(首字母大写)  SerialVersionUID 默认排除
			 */
			if (Arrays.asList(notFields).contains(turnName) || GETRIDOF_SERIAUID.equals(turnName)) {
				continue;
			}
			//获取转成对象属性类型
			String turnType = turnFies[i].getGenericType().toString();
			//遍历转前对象属性
			for (int j = NumberConstants.NUMERAL_ZORE; j < beforeFies.length; j++) {
				/**
				 * 获取转前对象属性名称并首字母大写
				 */
				String beforeName = beforeFies[j].getName().substring(NumberConstants.NUMERAL_ZORE,NumberConstants.NUMBER_1000).toUpperCase()+beforeFies[j].getName().substring(NumberConstants.NUMBER_1000);
				/**
				 * 获取转前对象属性类型
				 */
				String beforeType = beforeFies[j].getGenericType().toString();
				/**
				 * 判断属性名以及类型是否相等
				 */
				if (turnName.equals(beforeName) && turnType.equals(beforeType)) {
					/**
					 * 调用getter方法
					 */
					Method beforeMethod = beforeTheTurn.getClass().getMethod(GETTER_PREFIX+beforeName);
					/**
					 * getter获取值
					 */
					Object value = beforeMethod.invoke(beforeTheTurn);
					/**
					 * 判断String-->null     Integer -- > NumberConstants.NUMERAL_ZORE，值校验
					 */
					if (ObjectUtils.notEqual(null, value) && ObjectUtils.notEqual(NumberConstants.NUMERAL_ZORE, value)) {
						Method turnMethod = null;
						/**
						 * 判断属性的类型，并对应赋值
						 */
						if ("class java.lang.String".equals(beforeType)) {
							turnMethod = turnInto.getClass().getMethod(SETTER_PREFIX+turnName,String.class);
							turnMethod.invoke(turnInto, value);
							break;
						}
						if ("class java.lang.Integer".equals(beforeType) || "int".equals(beforeType)) {
							turnMethod = turnInto.getClass().getMethod(SETTER_PREFIX+turnName,int.class);
							turnMethod.invoke(turnInto, value);
							break;
						}
						if (Constant.TYPE_CLASS_LONG.equals(beforeType)) {
							turnMethod = turnInto.getClass().getMethod(SETTER_PREFIX+turnName,Long.class);
							turnMethod.invoke(turnInto, value);
							break;
						}
						if ("class java.util.Date".equals(beforeType)) {
							turnMethod = turnInto.getClass().getMethod(SETTER_PREFIX+turnName,Date.class);
							turnMethod.invoke(turnInto, value);
							break;
						}
					}
				}
			}
		}
		return turnInto;
	}

	/**
	 * 动态随机生成测试数据(可拓展)
	 * @Title: ApplicationConfigUtil.java
	 * @author 349910-foss-tangyuhan
	 * @date 2016年9月7日 下午6:33:53
	 * @version
	 */
	public static void automaticProduction(Object model){
		/**
		 *  获取实体类的所有属性，返回Field数组
		 */
		Field[] field = model.getClass().getDeclaredFields();

		/**
		 * 随机生成10000以内的数据    基于数据库有类型是char(NumberConstants.NUMBER_1000)而实体为String,则不使用
		 */
		Random random = new Random();
		/**
		 * 测试数据默认为1
		 */
//		String testDate = NumberConstants.NUMERAL_S_ONE;
		try {
			/**
			 *  遍历所有属性
			 */
			for (int j = NumberConstants.NUMERAL_ZORE; j < field.length; j++) {
				/**
				 *  获取属性的名字
				 */
				String name = field[j].getName();
				/**
				 *  将属性的首字符大写，方便构造get，set方法
				 */
				name = StringUtils.capitalize(name);
				/**
				 *  获取属性的类型
				 */
				String type = field[j].getGenericType().toString();
				/**
				 *  如果type是类类型，则前面包含"class "，后面跟类名
				 */
				Method m = null;
				if (type.equals("class java.lang.String")) {
					m = model.getClass().getMethod(GETTER_PREFIX + name);
					/**
					 *  调用getter方法获取属性值
					 */
					String value = (String) m.invoke(model);
					/**
					 * value为空就调用setter方法赋值
					 */
					if (value == null) {
						m = model.getClass().getMethod(SETTER_PREFIX+name,String.class);
						m.invoke(model, name+random.nextInt()*NumberConstants.NUMBER_1000);
					}
				}
				if (type.equals("class java.lang.Integer") || type.equals("int")) {
					m = model.getClass().getMethod(GETTER_PREFIX + name);
					Integer value = (Integer) m.invoke(model);
					if (value == null) {
						m = model.getClass().getMethod(SETTER_PREFIX+name,Integer.class);
						m.invoke(model, random.nextInt()*NumberConstants.NUMBER_1000);
					}
				}
				if (type.equals("class java.math.BigDecimal")) {
					m = model.getClass().getMethod(GETTER_PREFIX + name);
					Boolean value = (Boolean) m.invoke(model);
					if (value == null) {
						m = model.getClass().getMethod(SETTER_PREFIX+name,BigDecimal.class);
						m.invoke(model, new BigDecimal(random.nextInt()*NumberConstants.NUMBER_1000));
					}
				}
				if (type.equals("class java.lang.Boolean")) {
					m = model.getClass().getMethod(GETTER_PREFIX + name);
					Boolean value = (Boolean) m.invoke(model);
					if (value == null) {
						m = model.getClass().getMethod(SETTER_PREFIX+name,Boolean.class);
						m.invoke(model, random.nextInt(NumberConstants.NUMBER_2)==NumberConstants.NUMERAL_ZORE?true:false);
					}
				}
				if (type.equals("class java.util.Date")) {
					m = model.getClass().getMethod(GETTER_PREFIX + name);
					Date value = (Date) m.invoke(model);
					if (value == null) {
						m = model.getClass().getMethod(SETTER_PREFIX+name,Date.class);
						m.invoke(model, new Date());
					}
				}
				if (type.equals(Constant.TYPE_CLASS_LONG)) {
					m = model.getClass().getMethod(GETTER_PREFIX + name);
					Date value = (Date) m.invoke(model);
					if (value == null) {
						m = model.getClass().getMethod(SETTER_PREFIX+name,Long.class);
						m.invoke(model, random.nextLong()*NumberConstants.NUMBER_1000);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将实体类空串转成NULL
	 * @param object
	 * @param getRidIs
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
     */
	public static void convertEmptyToNull(Object object,Boolean...getRidIs) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		String getRidOfField = "Is";
		for(Field field : object.getClass().getDeclaredFields()){
			String name = StringUtils.capitalize(field.getName());
			if(GETRIDOF_SERIAUID.equals(name)){
				continue;
			}
			if(getRidIs.length != NumberConstants.NUMERAL_ZORE && getRidIs[NumberConstants.NUMERAL_ZORE]){
				if(name.startsWith(getRidOfField)){
					name = name.substring(NumberConstants.NUMBER_2).substring(NumberConstants.NUMERAL_ZORE, NumberConstants.NUMBER_1).toUpperCase() + name.substring(NumberConstants.NUMBER_3);
				}
			}
			Method m = object.getClass().getMethod(GETTER_PREFIX+name);
			Object value = m.invoke(object);
			if(null != value){
				if("".equals(value)){
					m = object.getClass().getMethod(SETTER_PREFIX+name,field.getType());
					m.invoke(object,new Object[]{null});
				}
			}
		}
		//获取父类的属性
		Field [] superField = object.getClass().getSuperclass().getDeclaredFields();
		//判断父类属性的长度是否大于0，是否执行
		if(superField.length>NumberConstants.NUMERAL_ZORE) {
			for (Field field : object.getClass().getSuperclass().getDeclaredFields()) {
				String name = StringUtils.capitalize(field.getName());
				if (GETRIDOF_SERIAUID.equals(name)) {
					continue;
				}

				if(getRidIs.length != NumberConstants.NUMERAL_ZORE && getRidIs[NumberConstants.NUMERAL_ZORE]){
					if (name.startsWith(getRidOfField)) {
						name = name.substring(NumberConstants.NUMBER_2).substring(
								NumberConstants.NUMERAL_ZORE, NumberConstants.NUMBER_1).toUpperCase() +
								name.substring(NumberConstants.NUMBER_3);
					}
				}
				Method m = object.getClass().getSuperclass().getMethod(GETTER_PREFIX + name);
				Object value = m.invoke(object);
				if (null != value) {
					if ("".equals(value)) {
						m = object.getClass().getSuperclass().getMethod(SETTER_PREFIX + name, field.getType());
						m.invoke(object, new Object[]{null});
					}
				}
			}
		}
	}
}
