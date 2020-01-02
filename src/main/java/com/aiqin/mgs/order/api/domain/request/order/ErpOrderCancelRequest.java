package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 供应链返回订单是否可以取消参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/4 16:38
 */
@Data
public class ErpOrderCancelRequest {

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "结果 1可以取消 0不可取消  供应链回调时候使用")
    @JsonProperty("status")
    private Integer status;

    @ApiModelProperty(value = "不可取消原因（选填） 供应链回调时候使用")
    @JsonProperty("reason")
    private String reason;

    @ApiModelProperty(value = "操作人id")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "操作人姓名")
    @JsonProperty("person_name")
    private String personName;
}
