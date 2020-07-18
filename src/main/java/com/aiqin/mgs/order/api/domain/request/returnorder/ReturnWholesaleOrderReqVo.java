package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * description: ReturnOrderReqVo
 * date: 2019/12/19 17:50
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel
public class ReturnWholesaleOrderReqVo implements Serializable {

    private static final long serialVersionUID = -4250407967683503902L;

    @ApiModelProperty(value = "订单号 ---传")
    private String orderStoreCode;

//    @ApiModelProperty(value = "订单状态)")
//    private Integer orderStatus;

    @ApiModelProperty(value = "(支付状态0已支付  1未支付) --传")
    private Integer paymentStatus;

    @ApiModelProperty(value = "客户编码 --传")
    private String customerCode;

    @ApiModelProperty(value = "客户名称 --传")
    private String customerName;

    //展示无法确认订单总额字段
//    @ApiModelProperty(value = "订单总额")
//    private BigDecimal returnOrderAmount;

    //实付金额字段

    @ApiModelProperty(value="省编码 --传")
//    @JsonProperty("province_id")
    private String provinceId;

    @ApiModelProperty(value="省名称 --传")
//    @JsonProperty("province_name")
    private String provinceName;

    @ApiModelProperty(value="市编码 --传")
//    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="市名称 --传")
//    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value="区编码 --传")
//    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value="区名称--传")
//    @JsonProperty("district_name")
    private String districtName;

    //新加加盟商和合伙人
    @ApiModelProperty("加盟商编码 ---不用传")
    private String franchiseeCode;

    @ApiModelProperty("加盟商名称 ---不用传")
    private String franchiseeName;

    @ApiModelProperty("合伙人编码 ---不用传")
    private String copartnerAreaId;

    @ApiModelProperty("合伙人名称 ---不用传")
    private String copartnerAreaName;

    @ApiModelProperty("业务形式 0门店退货 1批发退货 --传")
    private Integer businessForm;

//    @ApiModelProperty(value = "订单类型(订单类型 0直送、1配送、2辅采 ) --传")
     @ApiModelProperty(value = "订单类型 1直送 2配送 3货架  --传")
    private Integer orderType;

    @ApiModelProperty(value = "订单类别：1：收单配送 2：首单赠送 3：配送补货 4：首单直送 5：直送补货 51：批发普通订货  17.货架补货  147.采购直送 --传")
    private Integer orderCategory;

    @NotBlank(message = "公司编码不能为空")
    @ApiModelProperty(value = "公司编码--传") //需
    private String companyCode;

    @ApiModelProperty(value = "公司名称--传") //需
    private String companyName;

    @NotBlank(message = "售后类型不能为空 --- 不用传") //需
    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货、3冲减单 6质量问题")
    private Integer returnOrderType;

    @NotBlank(message = "临时售后类型不能为空 --- 传") //需
    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货、3冲减单 6质量问题")
    private Integer returnOrderTypeTemporary;

    @ApiModelProperty(value = "退货金额--前端不用传")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty(value = "收货地址--传")
    private String receiveAddress;

    @ApiModelProperty(value = "收货人--传")
    private String receivePerson;

    @ApiModelProperty(value = "收货人电话--传")
    private String receiveMobile;

    @ApiModelProperty(value = "是否需要发票 0是 1否 --传")
    private Integer whetherInvoice;

    @ApiModelProperty(value = "处理办法 1--退货退款(通过) --传")
    private Integer treatmentMethod;

    @ApiModelProperty(value = "商品明细")
    private List<ReturnWholesaleOrderDetail> details;

    @ApiModelProperty(value = "创建人编码")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "供应商编码 -----传")
//    @JsonProperty("supplier_code")
    private String supplierCode;

    @ApiModelProperty(value = "供应商名称 -----传")
//    @JsonProperty("supplier_name")
    private String supplierName;


    @ApiModelProperty(value = "门店id ---传")
//    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店名称  ----传")
//    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店编码 ---传")
//    @JsonProperty("store_code")
    private String storeCode;

}
