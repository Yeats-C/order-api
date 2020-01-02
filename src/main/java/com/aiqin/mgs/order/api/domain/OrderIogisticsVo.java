package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderIogisticsVo implements Serializable {
    //订单
    @ApiModelProperty(value = "发运状态")
    private Integer transport_status;
    @ApiModelProperty(value = "配送方式编码")
    private String distributionModeCode;
    @ApiModelProperty(value = "发货时间")
    private Date deliveryTime;
    @ApiModelProperty(value = "发运时间")
    private Date transportTime;
    @ApiModelProperty(value = "订单号")
    private String orderStoreCode;
    @ApiModelProperty(value = "签收时间（数据库两个发运时间）")
    private Date receiveTime;
    @ApiModelProperty(value = "实际销售数量")
    private Long actualTotalCount;
    @ApiModelProperty(value = "发货人id")
    private String deliveryPersionId;
    @ApiModelProperty(value = "操作人id")
    private String personId;
    @ApiModelProperty(value = "操作人姓名")
    private String personName;

    //订单明细
    @ApiModelProperty(value = "订单商品明细行")
    private List<OrderStoreDelete> orderStoreDelete;

    //订单批次明细（仓卡）
    @ApiModelProperty(value = "行号")
    private Long lineCode;
    @ApiModelProperty(value = "SKU编码")
    private String skuCode;
    @ApiModelProperty(value = "SKU名称")
    private String skuName;
    @ApiModelProperty(value = "批次编号")
    private String batchCode;
    @ApiModelProperty(value = "生产日期")
    private Date productDate;
    @ApiModelProperty(value = "实际销售数量")
    private Long actualToalCount;

}
