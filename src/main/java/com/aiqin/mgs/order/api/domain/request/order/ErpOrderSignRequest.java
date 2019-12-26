package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 订单签收请求参数
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/14 11:02
 */
@Data
public class ErpOrderSignRequest {

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "签收订单明细")
    @JsonProperty("item_list")
    private List<ErpOrderProductItemRequest> itemList;

    @ApiModelProperty(value = "用户id")
    @JsonProperty("person_id")
    private String personId;

    @ApiModelProperty(value = "用户名称")
    @JsonProperty("person_name")
    private String personName;

}
