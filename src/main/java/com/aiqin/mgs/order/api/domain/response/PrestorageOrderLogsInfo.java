package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2019/11/7 16:51
 * Description:
 */
@Data
@ApiModel("预存订单提货日志")
public class PrestorageOrderLogsInfo {
    /**
     * 会员名称
     */
    @JsonProperty("member_name")
    @ApiModelProperty("会员名称")
    private String memberName;

    /**
     * 会员手机号
     */
    @JsonProperty("member_phone")
    @ApiModelProperty("会员手机号")
    private String memberPhone;

    /**
     * 商品名称
     */
    @JsonProperty("product_name")
    @ApiModelProperty("商品名称")
    private String productName;

    /**
     * sku_code
     */
    @JsonProperty("sku_code")
    @ApiModelProperty("sku_code")
    private String skuCode;
    /**
     * spu_code
     */
    @JsonProperty("spu_code")
    @ApiModelProperty("spu_code")
    private String spuCode;
    /**
     * 条形码
     */
    @JsonProperty("bar_code")
    @ApiModelProperty("条形码")
    private String barCode;

    @JsonProperty("supply_amount")
    @ApiModelProperty("提货数量")
    private Integer supplyAmount;


    @JsonProperty("surplus_amount")
    @ApiModelProperty("剩余数量")
    private Integer surplusAmount;
    /**
     * 订单id
     */
    @JsonProperty("order_id")
    @ApiModelProperty("订单id")
    private String orderId;
    /**
     * 订单orderCode
     */
    @JsonProperty("order_code")
    @ApiModelProperty("订单orderCode")
    private String orderCode;

    @JsonProperty("create_time")
    @ApiModelProperty(value = "提货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonProperty("store_name")
    private String storeName;
}
