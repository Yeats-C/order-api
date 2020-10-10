package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("退货单表")
public class DLReturnOrderDetail {


    @NotBlank(message = "原销售单明细id不能为空")
    @ApiModelProperty(value = "原销售单明细id(DL推动的发货单明细id)")
    private String orderDetailId;

    @NotBlank(message = "sku编号不能为空")
    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @NotNull(message = "退货数量不能为空")
    @ApiModelProperty(value = "退货数量")
    private Long returnProductCount;

    @NotNull(message = "行号不能为空")
    @ApiModelProperty(value = "行号")
    private Long lineCode;

    @NotNull(message = "退货单价不能为空")
    @ApiModelProperty(value = "退货单价")
    private BigDecimal productAmount;

    @NotNull(message = "税率不能为空")
    @ApiModelProperty(value = "税率")
    private BigDecimal outputTaxRate;

    @NotNull(message = "商品类型不能为空")
    @ApiModelProperty(value = "商品类型 0商品 1赠品 2:兑换赠品")
    private Integer productType;

}