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
     * 修改状态
     */
    String MODIFY_TYPE_0 = "0";  //添加
    String MODIFY_TYPE_1 = "1";  //修改
    
    //是否计算数据量
    Integer ICOUNT_0 = 0;  //计算数据量
    
    /**
     * 订单来源
     */
    Integer ORIGIN_TYPE_0 = 0;  //pos
    Integer ORIGIN_TYPE_1 = 1;  //微商城
    Integer ORIGIN_TYPE_2 = 2;  //全部 作为查询条件
    Integer ORIGIN_TYPE_3 = 3;  //web
    
    /**
     * 生成订单号专用字典项 - 销售渠道标识、订单来源(避免与老系统重复)
     */
    Integer ORDERID_CHANNEL_3 = 3;  //总部向门店销售
    Integer ORDERID_CHANNEL_4 = 4;  //门店像会员销售
    String  ORIGIN_COME_3 = "3";  //pos
    String  ORIGIN_COME_4 = "4";  //微商城
    String  ORIGIN_COME_5 = "5";  //web
    
    
    /**
     * 收货方式
     */
    Integer RECEIVE_TYPE_0 = 0;  //自提
    Integer RECEIVE_TYPE_1 = 1;  //快递
    Integer RECEIVE_TYPE_2 = 2;  //全部 作为查询条件
    
    
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
     * 是否发生退货
     */
    Integer IS_RETURN_0 = 0;  //否
    Integer IS_RETURN_1 = 1;  //是
    
    
    /**
     * 优惠券状态
     */
    Integer COUPON_STATUS_0 = 0;  //使用
    Integer COUPON_STATUS_1 = 1;  //退
    
    
    /**
     * 日志表-状态
     */
    String STATUS_0 = "订单状态";  //订单状态
    String STATUS_1 = "支付状态";  //支付状态
    String STATUS_2 = "退货状态";  //退货状态
    
    
    /**
     * 状态描述
     */
    String STATUS_CONTENT_10 = "未付款";
    String STATUS_CONTENT_1 = "待发货";
    String STATUS_CONTENT_2 = "待提货";
    String STATUS_CONTENT_3 = "已发货";
    String STATUS_CONTENT_4 = "已取消";
    String STATUS_CONTENT_5 = "已完成";
    String STATUS_CONTENT_6 = "已提货";
    String STATUS_CONTENT_100 = "审批中";
    String STATUS_CONTENT_11 = "处理中";
    String STATUS_CONTENT_22 = "退款中";
    String STATUS_CONTENT_33 = "已完成";
    String STATUS_CONTENT_44 = "已取消";
    String STATUS_CONTENT_55 = "已关闭";
    String STATUS_CONTENT_66 = "未发生退货";
    String STATUS_CONTENT_1000 = "待支付";
    String STATUS_CONTENT_111 = "支付完成";
    
    Integer STATUS_TYPE_10 = 10;
    Integer STATUS_TYPE_1 = 1;
    Integer STATUS_TYPE_2 = 2;
    Integer STATUS_TYPE_3 = 3;
    Integer STATUS_TYPE_4 = 4;
    Integer STATUS_TYPE_5 = 5;
    Integer STATUS_TYPE_6 = 6;
    Integer STATUS_TYPE_100 = 100;
    Integer STATUS_TYPE_11 = 11;
    Integer STATUS_TYPE_22 = 22;
    Integer STATUS_TYPE_33 = 33;
    Integer STATUS_TYPE_44 = 44;
    Integer STATUS_TYPE_55 = 55;
    Integer STATUS_TYPE_66 = 66;
    Integer STATUS_TYPE_1000 = 1000;
    Integer STATUS_TYPE_111 = 111;
    


    /**
     * 退货状态
     */
    Integer AFTER_SALE_STATUS_0 = 0;  //"审批中"
    Integer AFTER_SALE_STATUS_1 = 1;  //处理中
    Integer AFTER_SALE_STATUS_2 = 2;  //退款中
    Integer AFTER_SALE_STATUS_3 = 3;  //已完成
    Integer AFTER_SALE_STATUS_4 = 4;  //已取消
    Integer AFTER_SALE_STATUS_5 = 5;  //已关闭
    
    
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
    
    //公司编码
    //爱亲
    String COMPANY_01 = "1";    
    
    //日周月.
    Integer DATE_TYPE_1 = 1;  //日
    Integer DATE_TYPE_2 = 2;  //周
    Integer DATE_TYPE_3 = 3;  //月
}
