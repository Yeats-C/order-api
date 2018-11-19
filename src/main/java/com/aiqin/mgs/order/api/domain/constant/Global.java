package com.aiqin.mgs.order.api.domain.constant;


public interface Global {

    /**
     * 禁用状态
     */
    String USER_OFF = "0";
    /**
     * 启用状态
     */
    String USER_ON = "1";
    
    /**
     * 订单来源
     */
    Integer ORIGIN_TYPE_3 = 3;  //pos
    Integer ORIGIN_TYPE_4 = 4;  //微商城
    
    /**
     * 销售渠道标识-生成订单号
     */
    Integer ORDERID_channel_3 = 3;  //总部向门店销售
    Integer ORDERID_channel_4 = 4;  //门店像会员销售
    
    
    /**
     * 收货方式
     */
    Integer RECEIVE_TYPE_0 = 0;  //自提
    Integer RECEIVE_TYPE_1 = 1;  //快递
    
    
    /**
     * 订单状态
     */
    Integer ORDER_STATUS_0 = 0;  //未付款
    Integer ORDER_STATUS_1 = 1;  //待发货
    Integer ORDER_STATUS_2 = 2;  //待提货
    Integer ORDER_STATUS_3 = 3;  //已发货
    Integer ORDER_STATUS_4 = 4;  //已取消
    Integer ORDER_STATUS_5 = 5;  //已完成
    Integer ORDER_STATUS_6 = 6;  //已提货
    
    
    /**
     * 退货状态
     */
    Integer RETURN_STATUS_0 = 0;  //审批中
    Integer RETURN_STATUS_1 = 1;  //处理中
    Integer RETURN_STATUS_2 = 2;  //退款中
    Integer RETURN_STATUS_3 = 3;  //已完成
    Integer RETURN_STATUS_4 = 4;  //未操作
    
    
    /**
     * 支付状态
     */
    Integer PAY_STATUS_0 = 0;  //待支付
    Integer PAY_STATUS_1 = 1;  //支付完成
    
    
    
    /**
     * 订单-支付方式
     */
    Integer PAY_TYPE_0 = 0;  //微信
    Integer PAY_TYPE_1 = 1;  //支付宝
    Integer PAY_TYPE_2 = 2;  //现金
    Integer PAY_TYPE_3 = 3;  //银联
    
    
    /**
     * 订单支付-支付类型
     */
    Integer P_TYPE_0 = 0;  //在线支付-微信
    Integer P_TYPE_1 = 1;  //在线支付-支付宝
    Integer P_TYPE_2 = 2;  //在线支付-银联
    Integer P_TYPE_3 = 3;  //到店支付-现金支付
    Integer P_TYPE_4 = 4;  //到店支付-微信
    Integer P_TYPE_5 = 5;  //到店支付-支付宝
    Integer P_TYPE_6 = 6;  //到店支付-银联
    
    
    /**
     * 发货方状态
     */
    Integer DELIVERY_STATUS_0 = 0;  //未发货
    Integer DELIVERY_STATUS_1 = 1;  //已发货
    
    
    /**
     * 是否
     */
    Integer YES = 0;  //是
    Integer NO = 1;  //否
    
    
    /**
     * 优惠券状态
     */
    Integer COUPON_STATUS_0 = 0;  //使用
    Integer COUPON_STATUS_1 = 1;  //退
    
    
    /**
     * 状态
     */
    Integer STATUS_0 = 0;  //订单状态
    Integer STATUS_1 = 1;  //支付状态
    Integer STATUS_2 = 2;  //退货状态
    
    
    /**
     * 状态描述
     */
    String STATUS_CONTENT_0 = "未付款";
    String STATUS_CONTENT_1 = "待发货";
    String STATUS_CONTENT_2 = "待提货";
    String STATUS_CONTENT_3 = "已发货";
    String STATUS_CONTENT_4 = "已取消";
    String STATUS_CONTENT_5 = "已完成";
    String STATUS_CONTENT_6 = "已提货";
    String STATUS_CONTENT_7 = "审批中";
    String STATUS_CONTENT_8 = "处理中";
    String STATUS_CONTENT_9 = "退款中";
    String STATUS_CONTENT_10 = "已完成";
    String STATUS_CONTENT_11 = "已付款";
    String STATUS_CONTENT_12 = "未付款";
    String STATUS_CONTENT_13 = "已退款";


    /**
     * 退货状态
     */
//    Integer AFTER_SALE_STATUS_0 = 0;  //"审批中"
//    Integer AFTER_SALE_STATUS_1 = 1;  //处理中
//    Integer AFTER_SALE_STATUS_2 = 2;  //退款中
    Integer AFTER_SALE_STATUS_3 = 3;  //已完成
//    Integer AFTER_SALE_STATUS_4 = 4;  //已取消
//    Integer AFTER_SALE_STATUS_5 = 5;  //已关闭
    
    
    /**
     * 售后类型
     */
    Integer AFTER_SALE_TYPE_0 = 0;  //赔偿
    Integer AFTER_SALE_TYPE_1 = 1;  //退货
    Integer AFTER_SALE_TYPE_2 = 2;  //换货
    
    
    /**
     * 处理方式
     */
    Integer PROCESS_TYPE_0 = 0;  //整单退
    Integer PROCESS_TYPE_1 = 0;  //部分退
    Integer PROCESS_TYPE_2 = 0;  //补差价
    Integer PROCESS_TYPE_3 = 0;  //折价退
    
    /**
     * 折扣方式
     */
    String AGIOTYPE_0 = "0"; //无优惠
    String AGIOTYPE_1 = "1"; //限时折扣
    String AGIOTYPE_2 = "2"; //优惠券
    String AGIOTYPE_3 = "3"; //满减
    String AGIOTYPE_4 = "4"; //积分
    
}
