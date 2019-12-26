package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @ApiModelProperty(value = "支付id")
    @JsonProperty("pay_id")
    private String payId;

    @ApiModelProperty(value = "支付流水号")
    @JsonProperty("pay_code")
    private String payCode;
}
