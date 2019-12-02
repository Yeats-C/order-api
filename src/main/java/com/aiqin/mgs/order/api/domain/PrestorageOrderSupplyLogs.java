package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2019/11/7 16:11
 * Description: 预存订单取货流水表
 */

@ApiModel("预存订单取货流水表")
@Data
public class PrestorageOrderSupplyLogs {
    @ApiModelProperty(value="预存订单提货明细id")
    @JsonProperty("prestorage_order_supply_detail_id")
    private String prestorageOrderSupplyDetailId="";


    @ApiModelProperty(value="预存订单提货id")
    @JsonProperty("prestorage_order_supply_id")
    private String prestorageOrderSupplyId="";
    @ApiModelProperty(value="条形码")
    @JsonProperty("sku_code")
    private String skuCode="";
    @ApiModelProperty(value="skp_code")
    @JsonProperty("skp_code")
    private String skpCode="";
    @ApiModelProperty(value="条形码")
    @JsonProperty("bar_code")
    private String barCode="";

    @ApiModelProperty(value="提货数量")
    @JsonProperty("supply_amount")
    private Integer supplyAmount=0;

    @ApiModelProperty(value="剩余数量")
    @JsonProperty("surplus_amount")
    private Integer surplusAmount=0;

    @ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;

}
