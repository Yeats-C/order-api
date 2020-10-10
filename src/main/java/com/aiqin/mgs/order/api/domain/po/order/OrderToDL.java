package com.aiqin.mgs.order.api.domain.po.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class OrderToDL {
    @ApiModelProperty(value = "订单类型编码 1配送 2直送")
    private String orderType;

    @ApiModelProperty(value = "订单编号")
    @NotBlank(message = "订单编号不能为空")
    private String orderStoreCode;

    @ApiModelProperty(value = "请求方法")
    private  String method;

    @ApiModelProperty(value = "入库集合")
    private List<ImplData> impData;
}
