package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单流程确认请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/3 16:51
 */
@Data
public class ErpOrderCarryOutNextStepRequest {

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "操作人id")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "操作人名称")
    @JsonProperty("person_name")
    private String personName;
}
