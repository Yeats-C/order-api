package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@ApiModel("订单主表")
@Data
public class OrderList extends PagesRequest {
    private String id;

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "订单类型(1:配送补货、2:直送补货、3:首单、4:首单赠送)")
    @JsonProperty("order_type")
    private Integer orderType;

    @ApiModelProperty(value = "订单状态")
    @JsonProperty("order_status")
    private Integer orderStatus = 1;

    @ApiModelProperty(value = "订单状态显示")
    @JsonProperty("order_status_show")
    private String orderStatusShow;

    @ApiModelProperty(value = "支付状态(0:未支付 1:已支付 2:已退款)")
    @JsonProperty("payment_status")
    private Integer paymentStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "支付日期")
    @JsonProperty("payment_time")
    private Date paymentTime;

    @ApiModelProperty(value = "支付方式描述")
    @JsonProperty("payment_type")
    private String paymentType;

    @ApiModelProperty(value = "支付方式编码")
    @JsonProperty("payment_typeCode")
    private String paymentTypeCode;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店code")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店类型")
    @JsonProperty("store_type")
    private String storeType;

    @ApiModelProperty(value = "订单总额")
    @JsonProperty("total_orders")
    private Long totalOrders;

    @ApiModelProperty(value = "实付金额")
    @JsonProperty("actual_amount_paid")
    private Long actualAmountPaid;

    @ApiModelProperty(value = "活动金额")
    @JsonProperty("activity_amount")
    private Long activityAmount;

    @ApiModelProperty(value = "优惠额度")
    @JsonProperty("preferential_quota")
    private Long preferentialQuota;

    @ApiModelProperty(value = "物流减免比例")
    @JsonProperty("logistics_remission_ratio")
    private Integer logisticsRemissionRatio;

    @ApiModelProperty(value = "物流减免类型(0:送货上门,1:送货市级市,2:无减免)")
    @JsonProperty("logistics_remission_type")
    private Integer logisticsRemissionType;

    @ApiModelProperty(value = "下单人编码")
    @JsonProperty("place_order_code")
    private String placeOrderCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "下单时间")
    @JsonProperty("place_order_time")
    private Date placeOrderTime;

    @ApiModelProperty(value = "账户余额")
    @JsonProperty("account_balance")
    private Long accountBalance;

    @ApiModelProperty(value = "授信额度")
    @JsonProperty("line_credit")
    private Long lineCredit;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receiving_address")
    private String receivingAddress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "到货时间")
    @JsonProperty("arrival_time")
    private Date arrivalTime;

    @ApiModelProperty(value = "收货人")
    @JsonProperty("consignee")
    private String consignee;

    @ApiModelProperty(value = "收货人电话")
    @JsonProperty("consignee_phone")
    private String consigneePhone;

    @ApiModelProperty(value = "所属公司")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value = "公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value = "原单标识code")
    @JsonProperty("original")
    private String original;

    @ApiModelProperty(value = "省编码")
    @JsonProperty("province_code")
    private String provinceCode;

    @ApiModelProperty(value = "省名称")
    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value = "市编码")
    @JsonProperty("city_code")
    private String cityCode;

    @ApiModelProperty(value = "市名称")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value = "区编码")
    @JsonProperty("district_code")
    private String districtCode;

    @ApiModelProperty(value = "区名称")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value = "发票抬头")
    @JsonProperty("invoice_title")
    private String invoiceTitle;

    @ApiModelProperty(value = "发票抬头类型 0不开、1增普、2增专")
    @JsonProperty("invoice_type")
    private Integer invoiceType;

    @ApiModelProperty(value = "订单来源")
    @JsonProperty("order_original")
    private String orderOriginal;

    @ApiModelProperty(value = "订单商品数量")
    @JsonProperty("product_num")
    private Integer productNum;


    @ApiModelProperty(value = "供应商编码")
    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称")
    @JsonProperty("supplier_name")
    private String supplierName;

    @ApiModelProperty(value = "物流中心编码")
    @JsonProperty("transport_center_code")
    private String transportCenterCode;

    @ApiModelProperty(value = "物流中心名称")
    @JsonProperty("transport_center_name")
    private String transportCenterName;

    @ApiModelProperty(value = "仓库编码")
    @JsonProperty("warehouse_code")
    private String warehouseCode;

    @ApiModelProperty(value = "仓库名称")
    @JsonProperty("warehouse_name")
    private String warehouseName;

    @ApiModelProperty(value = "重量")
    @JsonProperty("weight")
    private Integer weight;

    @ApiModelProperty(value = "邮编")
    @JsonProperty("zip_code")
    private String zipCode;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remake")
    private String remake;

    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "修改时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "是否申请赠品(0是1否)")
    @JsonProperty("application_gifts")
    private Integer applicationGifts;

    @ApiModelProperty(value = "是否申请优惠券(0是1否)")
    @JsonProperty("apply_coupons")
    private Integer applyCoupons;

    @ApiModelProperty(value = "商品List")
    @JsonProperty("order_list_product_list")
    private List<OrderListProduct> orderListProductList;
}