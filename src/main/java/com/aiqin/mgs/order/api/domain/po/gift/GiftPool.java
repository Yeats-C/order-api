package com.aiqin.mgs.order.api.domain.po.gift;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 兑兑换赠品池明细
 *
 * @author: csf
 * @version: v1.0.0
 * @date 2020/04/04 17:18
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class GiftPool extends PagesRequest {

    @ApiModelProperty(value = "主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "sku编码")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    @JsonProperty("sku_name")
    private String skuName;

    @ApiModelProperty(value = "spu编码")
    @JsonProperty("spu_code")
    private String spuCode;

    @ApiModelProperty(value = "spu名称")
    @JsonProperty("spu_name")
    private String spuName;

    @ApiModelProperty(value = "爱亲分销价")
    @JsonProperty("price_tax")
    private BigDecimal priceTax;

    @ApiModelProperty(value = "厂商指导价")
    @JsonProperty("manufacturer_guide_price")
    private BigDecimal manufacturerGuidePrice;

    @ApiModelProperty(value = "0. 启用 1.禁用")
    @JsonProperty("use_status")
    private String useStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

}
