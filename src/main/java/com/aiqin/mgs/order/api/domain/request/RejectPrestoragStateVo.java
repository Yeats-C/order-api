package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2019/11/25 16:16
 * Description:
 */

@Data
@ApiModel(value = "pos预存订单修改状态Vo")
public class RejectPrestoragStateVo {
    @JsonProperty("order_id")
    @ApiModelProperty("订单id")
    private String orderId;

    /**
     * 商品状态
     */
    @JsonProperty("product_status")
    @ApiModelProperty("商品状态")
    private Integer productStatus;
    @ApiModelProperty("预存订单提货id")
    @JsonProperty("prestorage_order_supply_id")
    private String prestorageOrderSupplyId;
}
