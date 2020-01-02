package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("新增退货申请单商品数据req")
public class OrderReturnProductReq {

    @ApiModelProperty(value = "申请退货单id")
    @JsonProperty("order_return_id")
    private String orderReturnId;

    @ApiModelProperty(value = "申请退货单编号")
    @JsonProperty("order_return_code")
    private String orderReturnCode;

    @ApiModelProperty(value = "商品编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "实收数量")
    @JsonProperty("actual_quantity")
    private Long actualQuantity;

    @ApiModelProperty(value = "申请退货数量")
    @JsonProperty("return_quantity")
    private Long returnQuantity;

    @ApiModelProperty(value = "退货仓往陈列仓回退数量")
    @JsonProperty("display_stock")
    private Long displayStock;

    @ApiModelProperty(value = "锁定的库存数")
    @JsonProperty("lock_stock")
    private Long lockStock;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "更新人id")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "更新人名称")
    @JsonProperty("update_by_name")
    private String updateByName;

}
