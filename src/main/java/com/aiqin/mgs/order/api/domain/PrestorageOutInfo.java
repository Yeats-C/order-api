package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2019/11/5 19:03
 * Description:
 */
@Data
@ApiModel("预存商品提货VO")
public class PrestorageOutInfo {
    /**
     * 分销机构id
     */
    @JsonProperty("distributor_id")
    @ApiModelProperty("分销机构id")
    private String distributorId;

    /**
     * 提货数量
     */
    @JsonProperty("amount")
    @ApiModelProperty("提货数量")
    private Integer amount;
    /**
     * 提货详情表ID
     */
    @JsonProperty("prestorage_order_supply_detail_id")
    @ApiModelProperty("提货详情表ID")
    private String prestorageOrderSupplyDetailId;
}
