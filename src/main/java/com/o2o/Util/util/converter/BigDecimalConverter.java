package com.o2o.Util.util.converter;

import org.apache.commons.beanutils.Converter;

import java.math.BigDecimal;


/**
 * 金额转换器
 *
 * @author     tangyuhan 349910
 * @version    v1.0
 * @date       2017/1/9 13:50 下午
 */
public class BigDecimalConverter implements Converter {
    @Override
    public Object convert(Class class1, Object value) {
        try {
            if(null == value){
                return BigDecimal.ZERO;
            }
            if(value instanceof BigDecimal){
                return value;
            }
        }catch (Exception e){
            return BigDecimal.ZERO;
        }
        return value;
    }
}
