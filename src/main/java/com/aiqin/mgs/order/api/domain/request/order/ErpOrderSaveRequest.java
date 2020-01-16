package com.aiqin.mgs.order.api.domain.request.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 保存订单请求参数类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/22 16:31
 */
@Data
public class ErpOrderSaveRequest {

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "订单类型")
    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty(value = "订单类别")
    @JsonProperty("order_category")
    private Integer orderCategory;

    @ApiModelProperty(value = "货架商品列表")
    @JsonProperty("item_list")
    private List<ErpOrderProductItemRequest> itemList;

    @ApiModelProperty(value = "A品券编码")
    @JsonProperty("top_coupon_code_list")
    private List<String> topCouponCodeList;
}