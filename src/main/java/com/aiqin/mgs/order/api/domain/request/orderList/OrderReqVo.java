package com.aiqin.mgs.order.api.domain.request.orderList;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * OrderReqVo
 *
 * @author zhangtao
 * @createTime 2019-01-16
 * @description
 */
@Data
@ApiModel("订单保存请求数据")
public class OrderReqVo {

    @ApiModelProperty(value = "订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "订单类型(1:配送补货、2:直送补货、3:首单、4:首单赠送)")
    @JsonProperty("order_type")
    private Integer orderType;

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
    private Long totalOrders = 0L;

    @ApiModelProperty(value = "实付金额")
    @JsonProperty("actual_amount_paid")
    private Long actualAmountPaid = 0L;

    @ApiModelProperty(value = "活动金额")
    @JsonProperty("activity_amount")
    private Long activityAmount = 0L;

    @ApiModelProperty(value = "优惠额度")
    @JsonProperty("preferential_quota")
    private Long preferentialQuota = 0L;

    @ApiModelProperty(value = "物流减免比例")
    @JsonProperty("logistics_remission_ratio")
    private Integer logisticsRemissionRatio = 0;

    @ApiModelProperty(value = "物流减免类型(0:送货上门,1:送货市级市,2:无减免)")
    @JsonProperty("logistics_remission_type")
    private Integer logisticsRemissionType = 2;

    @ApiModelProperty(value = "下单人编码")
    @JsonProperty("place_order_code")
    private String placeOrderCode;

    @ApiModelProperty(value = "收货地址")
    @JsonProperty("receiving_address")
    private String receivingAddress;

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

    @ApiModelProperty(value = "订单来源,0-门店网页下单，1-运营中心下单，")
    @JsonProperty("order_original")
    private String orderOriginal;

    @ApiModelProperty(value = "订单商品数量")
    @JsonProperty("product_num")
    private Integer productNum = 0;

//    @ApiModelProperty(value = "重量")
//    @JsonProperty("weight")
//    private Integer weight;

    @ApiModelProperty(value = "邮编")
    @JsonProperty("zip_code")
    private String zipCode;

    @ApiModelProperty(value = "备注")
    @JsonProperty("remake")
    private String remake;

    @ApiModelProperty(value = "下单人用户名")
    @JsonProperty("create_by")
    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "创建（下单）时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "是否包含赠品(0是1否)")
    @JsonProperty("application_gifts")
    private Integer applicationGifts = 1;

    @ApiModelProperty(value = "是否使用优惠券(0是1否)")
    @JsonProperty("apply_coupons")
    private Integer applyCoupons = 1;

    @ApiModelProperty(value = "发票抬头类型 0不开、1开")
    @JsonProperty("invoice_type")
    private Integer invoiceType = 0;

    @ApiModelProperty(value = "订单商品")
    private List<OrderProductReqVo> products;

    @ApiModelProperty(value = "订单是否需要支付")
    private Boolean needPaid = true;

    @ApiModelProperty(value = "订单参与活动编号")
    @JsonProperty("order_list_activity_code")
    private String orderListActivityCode;

    @ApiModelProperty(value = "订单参与活动名称")
    @JsonProperty("order_list_activity_name")
    private String orderListActivityName;

    @ApiModelProperty(value = "订单参与活动类型 (6-条件类型满减，7-条件类型满赠，8-条件类型折扣，9-条件类型特价)")
    @JsonProperty("order_list_activity_type")
    private Integer orderListActivityType;
}
