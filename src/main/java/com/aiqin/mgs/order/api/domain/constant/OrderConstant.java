package com.aiqin.mgs.order.api.domain.constant;

/**
 * 订单功能常量类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 9:54
 */
public class OrderConstant {

    /***订单支付状态轮询最大次数*/
    public static final int MAX_PAY_POLLING_TIMES = 5;
    /***订单未支付取消订单最大时间（毫秒）*/
    public static final long MAX_PAY_TIME_OUT_TIME = 1000 * 60 * 60 * 12;

}
