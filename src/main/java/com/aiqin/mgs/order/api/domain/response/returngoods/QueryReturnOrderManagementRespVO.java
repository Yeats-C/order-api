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
    private String orderStoreCode;

    @ApiModelProperty("退货类型： 0客户退货、1缺货退货、2售后退货、3冲减单 、4客户取消")
    private String returnOrderType;

    @ApiModelProperty("客户名称")
    private String customerName;

    @ApiModelProperty("仓库名称")
    private String transportCenterName;

    @ApiModelProperty("库房名称")
    private String warehouseName;

    @ApiModelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("退货单状态")
    private String orderStatus;

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = ReturnOrderStatus.getAllStatus().get(orderStatus).getBackgroundOrderStatus();
    }

    @ApiModelProperty("商品数量")
    private Long productCount;

    @ApiModelProperty("退货金额")
    private Long returnOrderAmount;

    @ApiModelProperty("实际退货数量")
    private Long actualProductCount;

    @ApiModelProperty("实退商品金额")
    private Long actualProductTotalAmount;

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
