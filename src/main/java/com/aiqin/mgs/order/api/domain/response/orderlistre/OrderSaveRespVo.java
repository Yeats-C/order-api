package com.aiqin.mgs.order.api.domain.response.orderlistre;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * OrderSaveRespVo
 *
 * @author zhangtao
 * @createTime 2019-01-18
 * @description
 */
@Data
public class OrderSaveRespVo {

    @ApiModelProperty("主订单编号")
    private String orderCode;

    @ApiModelProperty("是否进行了拆单")
    private Boolean splitOrder;

    @ApiModelProperty("拆单后子订单号")
    private List<String> childOrderCode;
}
