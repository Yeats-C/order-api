package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2019/11/6 10:06
 * Description:
 */
@ApiModel("预存订单提货详情")
@Data
public class PrestorageOrderSupplyDetail {

    @ApiModelProperty(value="预存订单提货明细id")
    @JsonProperty("prestorage_order_supply_detail_id")
    private String prestorageOrderSupplyDetailId="";


    @ApiModelProperty(value="预存订单提货id")
    @JsonProperty("prestorage_order_supply_id")
    private String prestorageOrderSupplyId="";

    @ApiModelProperty(value="订单id")
    @JsonProperty("order_id")
    private String orderId="";

    @ApiModelProperty(value="订单明细id")
    @JsonProperty("order_detail_id")
    private String orderDetailId="";

    @ApiModelProperty(value="商品名称")
    @JsonProperty("product_name")
    private String productName="";

    @ApiModelProperty(value="条形码")
    @JsonProperty("sku_code")
    private String skuCode="";

    @ApiModelProperty(value="条形码")
    @JsonProperty("bar_code")
    private String barCode="";

    @ApiModelProperty(value="购买数量")
    @JsonProperty("amount")
    private Integer amount=0;

    @ApiModelProperty(value="已提货的退货数量")
    @JsonProperty("return_amount")
    private Integer returnAmount=0;

    @ApiModelProperty(value="未提货的退货数量")
    @JsonProperty("return_prestorage_amount")
    private Integer returnPrestorageAmount=0;

    @ApiModelProperty(value="提货数量")
    @JsonProperty("supply_amount")
    private Integer supplyAmount=0;

    @ApiModelProperty(value="提货状态，0待提货，1已完成")
    @JsonProperty("prestorage_order_supply_status")
    private Integer prestorageOrderSupplyStatus=0;

    @ApiModelProperty(value="下单时间",example = "2001-01-01 01:01:01")
    @JsonProperty("create_time")
    private Date createTime;


    @ApiModelProperty(value="更改时间",example = "2001-01-01 01:01:01")
    @JsonProperty("update_time")
    private Date updateTime;


    @ApiModelProperty(value="操作员")
    @JsonProperty("create_by")
    private String createBy="";


    @ApiModelProperty(value="修改员")
    @JsonProperty("update_by")
    private String updateBy="";


    @ApiModelProperty(value="操作员名称")
    @JsonProperty("`create_by_name`")
    private String createByName="";


    @ApiModelProperty(value="修改员名称")
    @JsonProperty("`update_by_name`")
    private String updateByName="";

}
