package com.aiqin.mgs.order.api.domain.po.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
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
public class ErpOrderVo{
	
	@ApiModelProperty(value="子订单编码")
	@JsonProperty("order_code")
	private String orderCode;


	@ApiModelProperty(value="所属主订单编码")
	@JsonProperty("main_order_code")
	private String mainOrderCode;


	@ApiModelProperty(value="订单状态  1:已支付 2：已发货")
	@JsonProperty("order_status")
	private Integer orderStatus;


	@ApiModelProperty(value="sap传输状态 0:未传输 1:已传输")
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

	@ApiModelProperty(value="订单类型编码 1直送 2配送 3辅采直送")
	@JsonProperty("order_type_code")
	private String orderTypeCode;


	@ApiModelProperty(value="订单类型名称")
	@JsonProperty("order_type_name")
	private String orderTypeName;


	@ApiModelProperty(value="订单类别编码 1:首单配送 2:首单赠送 3:首单货架 4:货架补货 5:配送补货 6:游乐设备 7:首单直送 8直送补货")
	@JsonProperty("order_category_code")
	private String orderCategoryCode;


	@ApiModelProperty(value="订单类别名称")
	@JsonProperty("order_category_name")
	private String orderCategoryName;


	@ApiModelProperty(value="订单总额")
	@JsonProperty("total_product_amount")
	private BigDecimal totalProductAmount;


	@ApiModelProperty(value="实付金额")
	@JsonProperty("actual_total_product_amount")
	private BigDecimal actualTotalProductAmount;


	@ApiModelProperty(value="订单商品总数量")
	@JsonProperty("total_product_count")
	private Integer totalProductCount;


	@ApiModelProperty(value="实发商品总数量")
	@JsonProperty("actual_total_product_count")
	private Integer actualTotalProductCount;


	@ApiModelProperty(value="总物流费")
	@JsonProperty("deliver_amount")
	private BigDecimal deliverAmount;


	@ApiModelProperty(value="物流券抵减金额")
	@JsonProperty("goods_coupon")
	private BigDecimal goodsCoupon;


	@ApiModelProperty(value="账户抵减物流费")
	@JsonProperty("account_goods_coupon")
	private BigDecimal accountGoodsCoupon;


	@ApiModelProperty(value="活动抵减")
	@JsonProperty("activity_money")
	private BigDecimal activityMoney;


	@ApiModelProperty(value="A品券抵减")
	@JsonProperty("top_coupon_money")
	private BigDecimal topCouponMoney;


	@ApiModelProperty(value="服纺券抵减")
	@JsonProperty("suit_coupon_money")
	private BigDecimal suitCouponMoney;


	@ApiModelProperty(value="仓库编码")
	@JsonProperty("transport_center_code")
	private String transportCenterCode;


	@ApiModelProperty(value="仓库名称")
	@JsonProperty("transport_center_name")
	private String transportCenterName;


	@ApiModelProperty(value="库房编码")
	@JsonProperty("warehouse_code")
	private String warehouseCode;


	@ApiModelProperty(value="库房名称")
	@JsonProperty("warehouse_name")
	private String warehouseName;


	@ApiModelProperty(value="下单时间")
	@JsonProperty("order_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date orderTime;


	@ApiModelProperty(value="出库时间")
	@JsonProperty("out_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date outTime;


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


	@ApiModelProperty(value="物流单号")
	@JsonProperty("transport_code")
	private String transportCode;


	@ApiModelProperty(value="物流公司编码")
	@JsonProperty("transport_company_code")
	private String transportCompanyCode;


	@ApiModelProperty(value="物流公司名称")
	@JsonProperty("transport_company_name")
	private String transportCompanyName;


	@ApiModelProperty(value="使用赠品额度")
	@JsonProperty("complimentary_amount")
	private BigDecimal complimentaryAmount;


	@ApiModelProperty(value="A品券作废金额")
	@JsonProperty("nullify_top_coupon_money")
	private BigDecimal nullifyTopCouponMoney;

	@ApiModelProperty(value="商品列表")
	@JsonProperty("prodcut_list")
	private List<ErpOrderProductInfo> prodcutList;

	@ApiModelProperty(value="收货时间")
	@JsonProperty("take_time")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date takeTime;
}
