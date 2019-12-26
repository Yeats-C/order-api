package com.aiqin.mgs.order.api.domain.response.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 爱掌柜查询订单列表页Tab订单状态返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/25 19:21
 */
@Data
public class ErpStoreOrderStatusTabResponse {

    @ApiModelProperty(value = "待支付查询状态")
    @JsonProperty("unpaid_status")
    private Integer unpaidStatus;

    @ApiModelProperty(value = "待收货查询状态")
    @JsonProperty("wait_receive")
    private Integer waitReceive;

}
