package com.aiqin.mgs.order.api.domain.request.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付中心回调接口请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 16:45
 */
@Data
public class ErpOrderPayCallbackRequest {

    /***支付id*/
    @ApiModelProperty(value = "支付id")
    private String payId;
    /***支付流水号*/
    @ApiModelProperty(value = "支付流水号")
    private String payCode;
}
