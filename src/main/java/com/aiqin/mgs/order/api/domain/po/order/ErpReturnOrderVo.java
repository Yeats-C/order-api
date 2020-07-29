package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 退货实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
//@JsonInclude(JsonInclude.Include.ALWAYS)
//extends PagesRequest
public class ErpReturnOrderVo {
	
	@ApiModelProperty(value="退货单号")
	@JsonProperty("return_order_code")
	private String returnOrderCode;


	@ApiModelProperty(value="子订单号")
	@JsonProperty("order_code")
	private String orderCode;


	@ApiModelProperty(value="退货状态 1:已验收")
	@JsonProperty("order_status")
	private Integer orderStatus;


	@ApiModelProperty(value="sap传输状态  0:未传输 1:已传输")
	@JsonProperty("push_status")
	private Integer pushStatus;


	@ApiModelProperty(value="客户编码")
	@JsonProperty("franchisee_code")
	private String franchiseeCode;


	@ApiModelProperty(value="客户名称")
	@JsonProperty("franchisee_name")
	private String franchiseeName;
	
	@ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

	@ApiModelProperty(value="退货类型编码 1:售后退货 2:缺货取消 3:客户拒收")
	@JsonProperty("order_type_code")
	private String orderTypeCode;


	@ApiModelProperty(value="退货类型名称")
	@JsonProperty("order_type_name")
	private String orderTypeName;


	@ApiModelProperty(value="退货仓库编码")
	@JsonProperty("transport_center_code")
	private String transportCenterCode;


	@ApiModelProperty(value="退货仓库名称")
	@JsonProperty("transport_center_name")
	private String transportCenterName;


	@ApiModelProperty(value="退货库房编码")
	@JsonProperty("warehouse_code")
	private String warehouseCode;


	@ApiModelProperty(value="退货库房名称")
	@JsonProperty("warehouse_name")
	private String warehouseName;


	@ApiModelProperty(value="退货总额")
	@JsonProperty("total_product_amount")
	private BigDecimal totalProductAmount;


	@ApiModelProperty(value="退A品券总额")
	@JsonProperty("top_coupon_money")
	private BigDecimal topCouponMoney;


	@ApiModelProperty(value="退服纺券总额")
	@JsonProperty("suit_coupon_money")
	private BigDecimal suitCouponMoney;


	@ApiModelProperty(value="熙耘成本总额")
	@JsonProperty("scmp_cost")
	private BigDecimal scmpCost;


	@ApiModelProperty(value="爱亲成本总额")
	@JsonProperty("aiqin_cost")
	private BigDecimal aiqinCost;


	@ApiModelProperty(value="入库时间")
	@JsonProperty("input_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date inputTime;


	@ApiModelProperty(value="sap传输时间")
	@JsonProperty("push_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date pushTime;


	@ApiModelProperty(value="所属渠道")
	@JsonProperty("company_code")
	private String companyCode;


	@ApiModelProperty(value="所属渠道名称")
	@JsonProperty("company_name")
	private String companyName;

	
	@ApiModelProperty(value="商品列表")
	@JsonProperty("prodcut_list")
	private List<ErpReturnOrderProductInfo> prodcutList;
}
