/*****************************************************************
 * 模块名称：計算类
 * 开发人员: csf
 * 开发时间: 2020-04-21
 * ****************************************************************************/
package com.aiqin.mgs.order.api.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public class MathUtil {

    public static BigDecimal getBigDecimal(Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }
}

