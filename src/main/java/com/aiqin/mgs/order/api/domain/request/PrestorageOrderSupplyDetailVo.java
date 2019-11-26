package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author jinghaibo
 * Date: 2019/11/14 20:24
 * Description:
 */
@Data
@ApiModel(value = "pos预存订单退货Vo")
public class PrestorageOrderSupplyDetailVo {
    String prestorageOrderSupplyDetailId;


    @ApiModelProperty(value="新的已提货的退货数量")
    @JsonProperty("return_amount")
    private Integer returnAmount;

    @ApiModelProperty(value="老的已提货的退货数量")
    @JsonProperty("old_return_amount")
    private Integer oldReturnAmount;

    @ApiModelProperty(value="新的未提货的退货数量")
    @JsonProperty("return_prestorage_amount")
    private Integer returnPrestorageAmount;

    @ApiModelProperty(value="老的未提货的退货数量")
    @JsonProperty("old_return_prestorage_amount")
    private Integer oldReturnPrestorageAmount;

    private String updateBy;

    @ApiModelProperty(value="提货状态，0待提货，1已完成")
    @JsonProperty("prestorage_order_supply_status")
    private Integer prestorageOrderSupplyStatus=0;
}
