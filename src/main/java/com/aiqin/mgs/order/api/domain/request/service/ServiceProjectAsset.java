package com.aiqin.mgs.order.api.domain.request.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Objects;


/**
 * @author jinghaibo
 */
@Data
@ApiModel("服务项目")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ServiceProjectAsset {

    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "服务商品资产id")
    @JsonProperty("asset_id")
    @NotBlank
    private String assetId;

    @ApiModelProperty(value = "会员id")
    @JsonProperty("customer_id")
    private String customerId;

    @ApiModelProperty(value = "会员名称")
    @JsonProperty("customer_name")
    private String customerName;

    @ApiModelProperty(value = "会员手机号")
    @JsonProperty("customer_phone")
    private String customerPhone;

    @ApiModelProperty(value = "客户类型，0为会员，1为非会员")
    @JsonProperty("customer_type")
    private Integer customerType;

    @ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    @NotBlank
    private String storeId;

    @ApiModelProperty(value = "门店编号")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "服务项目id")
    @JsonProperty("project_id")
    @NotBlank
    private String projectId;

    @ApiModelProperty(value = "服务项目编号")
    @JsonProperty("project_code")
    private String projectCode;

    @ApiModelProperty(value = "服务项目名称")
    @JsonProperty("project_name")
    private String projectName;

    @ApiModelProperty(value = "服务类别id")
    @JsonProperty("type_id")
    private String typeId;

    @ApiModelProperty(value = "活动开始时间")
    @JsonProperty("begin_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    @ApiModelProperty(value = "活动结束时间")
    @JsonProperty("finish_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date finishTime;

    @ApiModelProperty(value = "消费方式，0为限次，1为不限次")
    @JsonProperty("consumption_pattern")
    private Integer consumptionPattern;

    @ApiModelProperty(value = "限制次数")
    @JsonProperty("limit_count")
    private Integer limitCount;

    @ApiModelProperty(value = "剩余次数")
    @JsonProperty("remain_count")
    private Integer remainCount;

    @ApiModelProperty(value = "实际金额")
    @JsonProperty("actual_amount")
    private Long actualAmount;

    @ApiModelProperty(value = "收银员id")
    @JsonProperty("cashier_id")
    private String cashierId;

    @ApiModelProperty(value = "收银员名称")
    @JsonProperty("cashier_name")
    private String cashierName;

    @ApiModelProperty(value = "导购id")
    @JsonProperty("guide_id")
    private String guideId;

    @ApiModelProperty(value = "导购名称")
    @JsonProperty("guide_name")
    private String guideName;

    @ApiModelProperty(value = "资产关联购买订单编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "资产关联购买订单id")
    @JsonProperty("order_id")
    private String orderId;

    @ApiModelProperty(value = "资产关联购买订单明细id")
    @JsonProperty("order_detail_id")
    private String orderDetailId;

    @ApiModelProperty(value = "是否直接消费，0为直接消费，1为否")
    @JsonProperty("is_direct_custom")
    private Integer isDirectCustom;

    @ApiModelProperty(value = "支付状态，0为未支付，1为支付完成")
    @JsonProperty("pay_status")
    private Integer payStatus;

    @ApiModelProperty(value = "使用状态，0为是，1为否")
    @JsonProperty("use_status")
    private Integer useStatus;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ServiceProjectAsset that = (ServiceProjectAsset) o;
        return Objects.equals(orderCode, that.orderCode) &&
                Objects.equals(orderId, that.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderCode, orderId);
    }
}
