package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
//@JsonInclude(JsonInclude.Include.ALWAYS)
public class ReturnErpBatchInfo {
	
	@JsonProperty("batch_info_code")
    @ApiModelProperty("批次编码")
    private String batchInfoCode;
	
	@JsonProperty("batch_no")
    @ApiModelProperty("批次号")
    private String batchNo;
	
	@ApiModelProperty(value="商品数量")
	@JsonProperty("product_count")
	private Long productCount;

	@ApiModelProperty(value="订货数量")
	@JsonProperty("total_product_count")
	private Integer totalProductCount;

	@JsonProperty("sku_code")
	@ApiModelProperty("商品编码")
	private String skuCode;

	@JsonProperty("sku_name")
	@ApiModelProperty("商品名称")
	private String skuName;
}
