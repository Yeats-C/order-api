package com.aiqin.mgs.order.api.domain.response.purchase;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseApplyProductInfoResponse {

    @ApiModelProperty(value="普通商品件数")
    @JsonProperty("product_piece_sum")
    private Integer productPieceSum;

    @ApiModelProperty(value="普通商品单品数")
    @JsonProperty("product_single_sum")
    private Integer productSingleSum;

    @ApiModelProperty(value="普通商品含税总金额")
    @JsonProperty("product_tax_sum")
    private BigDecimal productTaxSum;

    @ApiModelProperty(value="实物采购件数")
    @JsonProperty("matter_piece_sum")
    private Integer matterPieceSum;

    @ApiModelProperty(value="实物返单品总数")
    @JsonProperty("matter_single_sum")
    private Integer matterSingleSum;

    @ApiModelProperty(value="实物返含税总金额")
    @JsonProperty("matter_tax_sum")
    private BigDecimal matterTaxSum;

    @ApiModelProperty(value="赠品采购件数")
    @JsonProperty("gift_piece_sum")
    private Integer giftPieceSum;

    @ApiModelProperty(value="赠品单品总数")
    @JsonProperty("gift_single_sum")
    private Integer giftSingleSum;

    @ApiModelProperty(value="赠品含税总金额")
    @JsonProperty("gift_tax_sum")
    private BigDecimal giftTaxSum;

    @ApiModelProperty(value="总件数")
    @JsonProperty("piece_sum")
    private Integer pieceSum;

    @ApiModelProperty(value="单品总数")
    @JsonProperty("single_sum")
    private Integer singleSum;

//    @ApiModelProperty(value="含税总金额")
//    @JsonProperty("tax_sum")
//    private Integer taxSum;

    @ApiModelProperty(value="实际普通商品件数")
    @JsonProperty("actual_product_piece_sum")
    private Integer actualProductPieceSum;

    @ApiModelProperty(value="实际普通商品单品数")
    @JsonProperty("actual_product_single_sum")
    private Integer actualProductSingleSum;

    @ApiModelProperty(value="实际普通商品含税总金额")
    @JsonProperty("actual_product_tax_sum")
    private BigDecimal actualProductTaxSum;

    @ApiModelProperty(value="实际实物采购件数")
    @JsonProperty("actual_matter_piece_sum")
    private Integer actualMatterPieceSum;

    @ApiModelProperty(value="实际实物返单品总数")
    @JsonProperty("actual_matter_single_sum")
    private Integer actualMatterSingleSum;

    @ApiModelProperty(value="实际实物返含税总金额")
    @JsonProperty("actual_matter_tax_sum")
    private BigDecimal actualMatterTaxSum;

    @ApiModelProperty(value="实际赠品采购件数")
    @JsonProperty("actual_gift_piece_sum")
    private Integer actualGiftPieceSum;

    @ApiModelProperty(value="实际赠品单品总数")
    @JsonProperty("actual_gift_single_sum")
    private Integer actualGiftSingleSum;

    @ApiModelProperty(value="实际赠品含税总金额")
    @JsonProperty("actual_gift_tax_sum")
    private BigDecimal actualGiftTaxSum;

    @ApiModelProperty(value="实际总件数")
    @JsonProperty("actual_piece_sum")
    private Integer actualPieceSum;

    @ApiModelProperty(value="实际单品总数")
    @JsonProperty("actual_single_sum")
    private Integer actualSingleSum;

//    @ApiModelProperty(value="实际含税总金额")
//    @JsonProperty("actual_tax_sum")
//    private Integer actualTaxSum;
}
