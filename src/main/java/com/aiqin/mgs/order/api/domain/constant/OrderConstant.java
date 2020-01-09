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
    public static final int MAX_PAY_POLLING_TIMES = 10;
    /***支付结果轮询初始延迟 毫秒*/
    public static final long MAX_PAY_POLLING_INITIALDELAY = 1000L * 5;
    /***支付结果轮询间隔时间 毫秒*/
    public static final long MAX_PAY_POLLING_PERIOD = 1000L * 5;
    /***订单未支付取消订单最大时间（毫秒）*/
    public static final long MAX_PAY_TIME_OUT_TIME = 1000L * 60 * 60 * 12;

    /***默认公司编码*/
    public static final String DEFAULT_COMPANY_CODE = "01";
    /***默认公司名称*/
    public static final String DEFAULT_COMPANY_NAME = "北京爱亲科技股份有限公司";

    /***门店营业状态 待配架*/
    public static final String OPEN_STATUS_CODE_17 = "010104";
    /***门店营业状态 待首单*/
    public static final String OPEN_STATUS_CODE_18 = "010105";

    /***模拟序列名称 订单号*/
    public static final String SEQUENCE_NAME_ORDER_STORE_CODE = "order_store_code";

}
