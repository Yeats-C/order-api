package com.aiqin.mgs.order.api.domain.request.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单编辑请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 11:52
 */
@Data
public class ErpOrderEditRequest {

    /***订单编号*/
    @ApiModelProperty(value = "订单编号")
    private String orderCode;
}
