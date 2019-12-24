package com.aiqin.mgs.order.api.domain.request.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单签收请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/14 11:02
 */
@Data
public class ErpOrderSignRequest {

    /***订单编号*/
    @ApiModelProperty(value = "订单编号")
    private String orderCode;
    /***门店id*/
    @ApiModelProperty(value = "门店id")
    private String storeId;

}
