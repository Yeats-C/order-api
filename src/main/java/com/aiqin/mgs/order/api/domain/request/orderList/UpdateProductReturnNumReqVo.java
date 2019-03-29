package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * UpdateProductReturnNumReqVo
 *
 * @author zhangtao
 * @createTime 2019-03-29
 * @description
 */
@Data
public class UpdateProductReturnNumReqVo {

    @JsonProperty("order_code")
    private String orderCode;

    private List<UpdateProductReturnNumItemReqVo> items;
}
