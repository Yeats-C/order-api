package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UpdateProductReturnNumItemReqVo
 *
 * @author zhangtao
 * @createTime 2019-03-29
 * @description
 */
@Data
public class UpdateProductReturnNumItemReqVo {

    @ApiModelProperty("退货数量")
    private Integer num;

    @ApiModelProperty("商品行项目号")
    @JsonProperty("order_product_id")
    private String orderProductId;
}
