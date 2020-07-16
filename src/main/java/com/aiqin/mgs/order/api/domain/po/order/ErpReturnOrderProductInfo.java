package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
//@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpReturnOrderProductInfo{
	
	@ApiModelProperty(value="退货单号")
	@JsonProperty("return_order_code")
	private String returnOrderCode;

	
	@ApiModelProperty(value="商品编号")
	@JsonProperty("sku_code")
	private String skuCode;


	@ApiModelProperty(value="商品名称")
	@JsonProperty("sku_name")
	private String skuName;


	@ApiModelProperty(value="商品品类编码")
	@JsonProperty("product_category_code")
	private String productCategoryCode;


	@ApiModelProperty(value="商品品类名称")
	@JsonProperty("product_category_name")
	private String productCategoryName;


	@ApiModelProperty(value="商品品牌编码")
	@JsonProperty("product_brand_code")
	private String productBrandCode;


	@ApiModelProperty(value="商品品牌名称")
	@JsonProperty("product_brand_name")
	private String productBrandName;


	@ApiModelProperty(value="商品属性编码")
	@JsonProperty("product_property_code")
	private String productPropertyCode;


	@ApiModelProperty(value="商品属性名称")
	@JsonProperty("product_property_name")
	private String productPropertyName;


	@ApiModelProperty(value="商品类型  0商品 1赠品")
	@JsonProperty("product_type")
	private Integer productType;


	@ApiModelProperty(value="熙耘采购价")
	@JsonProperty("scmp_purchase_amount")
	private BigDecimal scmpPurchaseAmount;


	@ApiModelProperty(value="渠道采购价")
	@JsonProperty("purchase_amount")
	private BigDecimal purchaseAmount;


	@ApiModelProperty(value="退货数量")
	@JsonProperty("product_count")
	private Integer productCount;
	

	@ApiModelProperty(value="退货金额")
	@JsonProperty("total_product_amount")
	private BigDecimal totalProductAmount;

	
	@ApiModelProperty(value="退货单价")
	@JsonProperty("preferential_amount")
	private BigDecimal preferentialAmount;


	@ApiModelProperty(value="退还A品券")
	@JsonProperty("top_coupon_money")
	private BigDecimal topCouponMoney;


	@ApiModelProperty(value="退还服纺金")
	@JsonProperty("suit_coupon_money")
	private BigDecimal suitCouponMoney;
	
	
	@ApiModelProperty(value="批次列表")
	@JsonProperty("batch_list")
	private List<ErpBatchInfo> batchList;
}
