package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * description: DLReturnOrderReqVo
 * date: 2019/12/19 17:50
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel
public class DLReturnOrderReqVo{


    @NotBlank(message = "请求方法不能为空")
    private String method;

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型(1直送  2配送)")
    private Integer orderType;

    @NotBlank(message = "门店编号不能为空")
    @ApiModelProperty(value = "门店编号")
    private String customerCode;

    @NotBlank(message = "退货单编号不能为空")
    @ApiModelProperty(value = "退货单编号(双方判断唯一标识)")
    private String returnOrderCode;

    @ApiModelProperty(value = "供货单位编号，直送必传")
    private String supplierCode;

    @ApiModelProperty(value = "仓库编号，配送必传")
    private String transportCenterCode;

    @ApiModelProperty(value = "原销售单id(DL推送的发货单id)")
    private String order_store_id;

    @ApiModelProperty(value = "明细")
    private List<DLReturnOrderDetail> details;

}
