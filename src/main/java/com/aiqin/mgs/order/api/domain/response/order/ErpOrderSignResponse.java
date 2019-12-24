package com.aiqin.mgs.order.api.domain.response.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 查询门店未签收订单数量查询返回值
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/16 11:20
 */
@Data
public class ErpOrderSignResponse {

    /***未签收订单数量*/
    @ApiModelProperty(value = "门店未签收订单数量")
    private Integer needSignOrderQuantity;
}
