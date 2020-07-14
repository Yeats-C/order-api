package com.aiqin.mgs.order.api.domain.po.gift;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 兑换赠品积分账户使用明细查询参数
 *
 * @author: csf
 * @version: v1.0.0
 * @date 2020/04/03 17:18
 */
@Data
@ApiModel("兑换赠品积分账户使用明细查询参数")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GiftQuotasUseDetailPageRequest extends PagesRequest {

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "类型 1 赠品划单 2订单使用 3订单赠送 4过期")
    @JsonProperty("type")
    private Integer type;

}
