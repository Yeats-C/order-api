package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2019/11/4 10:51
 * Description:
 */
@Data
@ApiModel("封装-预存订单提货明细")
public class PrestorageResponse {
    @ApiModelProperty("预存订单提货明细id")
    @JsonProperty("prestorage_order_supply_detail_id")
    private String prestorageOrderSupplyDetailId;

    @ApiModelProperty("商品")
    @JsonProperty("product_name")
    private String productName;

    @ApiModelProperty("购买数量")
    @JsonProperty("amount")
    private Integer amount;

    @ApiModelProperty("已提货的退货数量")
    @JsonProperty("return_amount")
    private Integer returnAmount;


    @ApiModelProperty("未提货的退货数量")
    @JsonProperty("return_prestorage_amount")
    private Integer returnPrestorageAmount;


    @ApiModelProperty("提货数量")
    @JsonProperty("supply_amount")
    private Integer supplyAmount;


    @ApiModelProperty("可售数量")
    @JsonProperty("goods_amount")
    private Integer goodsAmount;

    @ApiModelProperty("skuCode")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("预存订单提货id")
    @JsonProperty("prestorage_order_supply_id")
    private String prestorageOrderSupplyId;

    @ApiModelProperty("分销机构编码")
    @JsonProperty("distributor_code")
    private String distributorCode;

    @ApiModelProperty("分销机构编码")
    @JsonProperty("distributor_id")
    private String distributorId;

    @ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;
}
