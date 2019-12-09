package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
public class ErpOrderInfo extends ErpOrderBase {

    /***订单id*/
    private String orderId;
    /***订单编号*/
    private String orderCode;
    /***订单状态 枚举 ErpOrderStatusEnum*/
    private Integer orderStatus;
    /***订单类型 枚举 ErpOrderTypeEnum*/
    private Integer orderType;
    /***订单来源 ErpOrderOriginTypeEnum*/
    private Integer orderOriginType;
    /***订单销售渠道标识 ErpOrderChannelTypeEnum*/
    private Integer orderChannelType;
    /***订单级别 枚举 ErpOrderLevelEnum*/
    private Integer orderLevel;
    /***关联主订单编码*/
    private String primaryCode;
    /***是否被拆分 YesOrNoEnum */
    private Integer splitStatus;
    /***是否发生退货 YesOrNoEnum */
    private Integer returnStatus;

    /***订单总额*/
    private BigDecimal totalMoney;
    /***活动优惠金额*/
    private BigDecimal activityMoney;
    /***服纺券优惠金额*/
    private BigDecimal suitCouponMoney;
    /***A品券优惠金额*/
    private BigDecimal topCouponMoney;
    /***实付金额*/
    private BigDecimal actualMoney;

    /***物流券*/
    private BigDecimal goodsCoupon;
    /***订单取消之前的订单状态*/
    private Integer beforeCancelStatus;
    /***关联物流单id*/
    private String sendingId;

    /***供应商编码*/
    private String supplierCode;
    /***供应商名称*/
    private String supplierName;
    /***仓库编码*/
    private String repertoryCode;
    /***仓库名称*/
    private String repertoryName;
    /***加盟商id*/
    private String franchiseeId;
    /***加盟商编码*/
    private String franchiseeCode;
    /***加盟商名称*/
    private String franchiseeName;
    /***门店id*/
    private String storeId;
    /***门店编码*/
    private String storeCode;
    /***门店名称*/
    private String storeName;

    private List<ErpOrderItem> itemList;
}
