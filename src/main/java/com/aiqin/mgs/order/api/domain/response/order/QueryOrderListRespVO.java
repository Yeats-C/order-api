package com.aiqin.mgs.order.api.domain.response.order;

import com.aiqin.mgs.order.api.base.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@ApiModel("订单列表展示vo")
@Data
public class QueryOrderListRespVO {

    @ApiModelProperty("订单编码(订单号)")
    private String orderCode;

    @ApiModelProperty("类型：直送、配送、首单、首单赠送.辅采直送")
    private String orderType;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("物流中心名称")
    private String transportCenterName;

    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("订单状态(状态有点多，后面补)")
    private String orderStatus;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @ApiModelProperty("订单金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("商品分销价总金额")
    private BigDecimal productTotalAmount;

    @ApiModelProperty("商品渠道价总金额")
    private BigDecimal productChannelTotalAmount;

    @ApiModelProperty("商品数量")
    private Long productNum;

    @ApiModelProperty("实际发货数量")
    private Long actualProductNum;

    @ApiModelProperty("实际渠道总价")
    private BigDecimal actualProductChannelTotalAmount;

    @ApiModelProperty("实际分销总价")
    private BigDecimal actualProductTotalAmount;

    @ApiModelProperty("创建人")
    private String createByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String updateByName;

    @ApiModelProperty("修改时间")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = OrderStatus.getAllStatus().get(orderStatus).getBackgroundOrderStatus();
    }
}
