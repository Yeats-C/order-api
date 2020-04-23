package com.aiqin.mgs.order.api.domain.request.stock;

import com.aiqin.mgs.order.api.domain.request.product.StockBatchRespVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author knight.xie
 * @version 1.0
 * @className ProductSkuRespVo
 * @date 2019/5/14 15:05
 * @description
 */
@ApiModel("SKU基本信息返回")
@Data
public class ProductSkuStockRespVo {


    @ApiModelProperty(value = "sku编号")
    @JsonProperty("sku_code")
    private String skuCode;

    @ApiModelProperty("sku名称")
    @JsonProperty("sku_name")
    private String skuName;


    @ApiModelProperty(value = "仓库信息")
    @JsonProperty("stock_batchRespVOList")
    private  List<StockBatchRespVO> stockBatchRespVOList;


}
