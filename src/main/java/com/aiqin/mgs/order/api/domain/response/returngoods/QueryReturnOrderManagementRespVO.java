package com.aiqin.mgs.order.api.domain.response.returngoods;

import com.aiqin.mgs.order.api.base.ReturnOrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("退货管理返回vo")
public class QueryReturnOrderManagementRespVO {

    @ApiModelProperty("退货订单编码")
    private String returnOrderCode;

    @ApiModelProperty("订单编码(订单号)")
    private String orderCode;

    @ApiModelProperty("退货单状态")
    private String orderStatus;

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = ReturnOrderStatus.getAllStatus().get(orderStatus).getBackgroundOrderStatus();
    }

    @ApiModelProperty("退货类型：客户退货、缺货退货、售后退货")
    private String returnOrderType;

    @ApiModelProperty("物流中心名称")
    private String transportCenterName;

    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("商品数量")
    private Long productNum;

    @ApiModelProperty("分销总金额")
    private Long productTotalAmount;

    @ApiModelProperty("退货金额")
    private Long returnOrderAmount;

    @ApiModelProperty("实际退货数量")
    private Long actualProductNum;

    @ApiModelProperty("实际渠道总金额")
    private Long actualProductChannelTotalAmount;

    @ApiModelProperty("实际分销总金额")
    private Long actualProductTotalAmount;

    @ApiModelProperty("渠道总金额")
    private Long productChannelTotalAmount;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty("修改人名称")
    private String updateByName;

    @ApiModelProperty("修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;


}
