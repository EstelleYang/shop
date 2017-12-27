package com.o2o.Util.util.converter;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 实体类转换
 *
 * @author     tangyuhan 349910
 * @version    v1.0
 * @date       2017/1/9 14:09 下午
 */
public class BeanUtilConverter extends BeanUtils {
    static {
        ConvertUtils.register(new BigDecimalConverter(),java.math.BigDecimal.class);
    }

    public static void copyProperties(Object target, Object source)
            throws InvocationTargetException, IllegalAccessException {
        // 支持对BigDecimal为null转换
        org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
    }
}
