package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.component.enums.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
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
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderInfo extends ErpOrderBase {

    /***订单id*/
    private String orderId;
    /***订单编号*/
    private String orderCode;
    /***订单状态 枚举 ErpOrderStatusEnum*/
    private Integer orderStatus;
    /***订单状态描述 枚举 ErpOrderStatusEnum*/
    private String orderStatusDesc;
    /***订单支付状态 ErpPayStatusEnum*/
    private Integer payStatus;
    /***订单支付状态描述 ErpPayStatusEnum*/
    private String payStatusDesc;
    /***订单类型 枚举 ErpOrderTypeEnum*/
    private Integer orderType;
    /***订单类型描述 枚举 ErpOrderTypeEnum*/
    private String orderTypeDesc;
    /***订单来源 ErpOrderOriginTypeEnum*/
    private Integer orderOriginType;
    /***订单来源描述 ErpOrderOriginTypeEnum*/
    private String orderOriginTypeDesc;
    /***订单销售渠道标识 ErpOrderChannelTypeEnum*/
    private Integer orderChannelType;
    /***订单销售渠道标识描述 ErpOrderChannelTypeEnum*/
    private String orderChannelTypeDesc;
    /***订单级别 枚举 ErpOrderLevelEnum*/
    private Integer orderLevel;
    /***订单级别描述 枚举 ErpOrderLevelEnum*/
    private String orderLevelDesc;
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
    private BigDecimal payMoney;
    /***物流券*/
    private BigDecimal goodsCoupon;

    /***订单取消之前的订单状态*/
    private Integer beforeCancelStatus;
    /***关联物流单id*/
    private String logisticsId;
    /***费用id*/
    private String feeId;

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

    /***查询开始时间 yyyy-MM-dd*/
    private String createTimeStart;
    /***查询结束时间 yyyy-MM-dd*/
    private String createTimeEnd;
    @ApiModelProperty(value = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(value = "当前页")
    private Integer pageNo;
    /***是否支持校验支付 对应列表上的确认收款按钮 1有按钮 0没有按钮*/
    @ApiModelProperty(value = "是否支持校验支付")
    private Integer repayOperation;
    /***能否新增赠品行  1可以 0不能 */
    @ApiModelProperty(value = "是否支持校验支付")
    private Integer addProductGiftOperation;

    /***上级订单*/
    private ErpOrderInfo primaryOrder;
    /***关联子订单*/
    private List<ErpOrderInfo> secondaryOrderList;

    /***订单商品明细行*/
    private List<ErpOrderItem> orderItemList;
    /***订单费用*/
    private ErpOrderFee orderFee;
    /***订单物流信息*/
    private ErpOrderLogistics orderLogistics;
    /***订单收货人信息*/
    private ErpOrderConsignee orderConsignee;
    /***订单操作日志*/
    private List<ErpOrderOperationLog> orderOperationLogList;

    public String getOrderStatusDesc() {
        return ErpOrderStatusEnum.getEnumDesc(orderStatus);
    }

    public String getPayStatusDesc() {
        return ErpPayStatusEnum.getEnumDesc(payStatus);
    }

    public String getOrderTypeDesc() {
        return ErpOrderTypeEnum.getEnumDesc(orderType);
    }

    public String getOrderOriginTypeDesc() {
        return ErpOrderOriginTypeEnum.getEnumDesc(orderOriginType);
    }

    public String getOrderChannelTypeDesc() {
        return ErpOrderChannelTypeEnum.getEnumDesc(orderChannelType);
    }

    public String getOrderLevelDesc() {
        return ErpOrderLevelEnum.getEnumDesc(orderLevel);
    }
}
