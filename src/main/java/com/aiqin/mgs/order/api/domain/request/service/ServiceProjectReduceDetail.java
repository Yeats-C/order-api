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
import java.util.List;

@Data
@ApiModel("服务项目")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ServiceProjectReduceDetail {
    @ApiModelProperty(value = "自增主键")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value = "服务商品资产id")
    @JsonProperty("asset_id")
    @NotBlank
    private String assetId;

    @ApiModelProperty(value = "扣减id")
    @JsonProperty("order_id")
    private String orderId;

    @ApiModelProperty(value = "扣减编号")
    @JsonProperty("order_code")
    private String orderCode;

    @ApiModelProperty(value = "扣减类型，，0为扣减,1为购买,2为退次，3赠次，4延期，5退次")
    @JsonProperty("order_type")
    private Integer orderType;

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

    @ApiModelProperty(value = "扣减次数或者退次次数")
    @JsonProperty("reduce_count")
    private Integer reduceCount=0;

    @ApiModelProperty(value = "退还金额")
    @JsonProperty("return_amount")
    private Long returnAmount;

    @ApiModelProperty(value = "剩余次数")
    @JsonProperty("remain_count")
    private Integer remainCount;

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

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value="支付方式")
    @JsonProperty("pay_type")
    private String payType;

    @ApiModelProperty(value = "购买订单的商品信息")
    @JsonProperty("project_list")
    private List<ServiceProjectAsset> serviceProjectAssetList;

    @ApiModelProperty(value="有效期截止时间")
    @JsonProperty("finish_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date finishTime;

}
