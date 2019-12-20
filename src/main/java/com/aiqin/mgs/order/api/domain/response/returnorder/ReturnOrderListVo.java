package com.aiqin.mgs.order.api.domain.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description: ReturnOrderListVo
 * date: 2019/12/20 10:13
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("退货单返回数据")
public class ReturnOrderListVo implements Serializable {

    private static final long serialVersionUID = -4250407967683503902L;

    @ApiModelProperty(value = "业务id")
    private String returnOrderId;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;

    @ApiModelProperty(value = "订单号")
    private String orderStoreCode;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "售后类型 0退货退款  1仅退款")
    private Integer treatmentMethod;

    @ApiModelProperty(value = "处理方式，0-整单退、1-部分退")
    private Integer processType;

    @ApiModelProperty(value = "退款状态，0-未退款、1-已退款")
    private Integer refundStatus;

    @ApiModelProperty(value = "退货状态：1-待审核，2-审核通过，3-订单同步中，4-等待退货验收，5-等待退货入库，6-等待审批，11-退货完成，12-退款完成，97-退货终止，98-审核不通过，99-已取消")
    private Integer returnOrderStatus;

    @ApiModelProperty(value = "渠道来源")
    private Integer sourceType;

    @ApiModelProperty(value = "退款方式 1:现金 2:微信 3:支付宝 4:银联")
    private Integer returnMoneyType;

    @ApiModelProperty(value = "实退商品总金额")
    private BigDecimal actualReturnOrderAmount;

    @ApiModelProperty(value = "下单人（操作人）")
    private String createByName;

    @ApiModelProperty(value = "门店id")
    private String storeId;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

}
