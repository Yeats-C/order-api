package com.aiqin.mgs.order.api.component;

import io.swagger.annotations.ApiModelProperty;

import java.lang.reflect.Method;

/**
 * 描述:
 *
 * @author zhujunchao
 * @create 2018-12-27 14:44
 */
public class ParamUnit {
    /**
     * 传属性数组判断是否为空
     * @param strings 属性可变长参数
     * @param o
     * @throws Exception
     */
    public  static  void  isNotNull(Object o,String...strings) {
        for (String param : strings) {
            //获取方法名
            String MethodName=new StringBuilder("get").append(Character.toUpperCase(param.charAt(0))).append(param.substring(1)).toString();
            Class  c=  o.getClass();
            //获取方法
            Method  method= null;
            Object re=null;
            try {
                method = c.getMethod(MethodName);
                re= method.invoke(o);
            } catch (Exception e) {
                throw new IllegalArgumentException( "获取Get方法获取异常");
            }
            //判断值是否为空
            if (re==null ||re.toString().trim().length()==0){
                ApiModelProperty apiModelProperty= null;
                try {
                    apiModelProperty = c.getDeclaredField(param).getAnnotation(ApiModelProperty.class);
                } catch (NoSuchFieldException e) {
                    throw new IllegalArgumentException( "注解ApiModelProperty获取异常");
                }
                String zjValue=apiModelProperty.value();
                throw new IllegalArgumentException( zjValue+ "不能为空");
            }
        }
        return;
    }
}
