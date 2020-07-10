package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("批发商品明细表")
public class ReturnWholesaleOrderDetail {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "业务id")
    private String returnOrderDetailId;

    @ApiModelProperty(value = "退货单号")
    private String returnOrderCode;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "商品类型 0商品 1赠品 2:兑换赠品")
    private Integer productType;

    @ApiModelProperty(value = "单位编码")
    private String unitCode;

    @ApiModelProperty(value = "单位名称")
    private String unitName;

    @ApiModelProperty(value = "批次编码")
    private String batchInfoCode;

    @ApiModelProperty(value = "批次号时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date batchDate;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    @ApiModelProperty(value = "(分摊后金额)均摊后单价")
    private BigDecimal preferentialAmount;

    @ApiModelProperty(value = "本行A品券优惠单品额度")
    private BigDecimal topCouponAmount;

    @ApiModelProperty(value = "使用赠品额度")
    private BigDecimal complimentaryAmount;

    @ApiModelProperty(value = "服纺券单品金额")
    private BigDecimal returnClothingSpinning;

    @ApiModelProperty(value = "客户实收数量")
    private Long actualInboundCount;

//    @ApiModelProperty(value = "(申请退货数量)退货数量")
//    private Long returnProductCount;

    @ApiModelProperty(value = "(申请退货数量)实退数量 ----传")
    private Long actualReturnProductCount;


    @ApiModelProperty(value = "商品状态0正品1残品")
    private Integer productStatus;

    @ApiModelProperty(value = "颜色编码")
    private String colorCode;

    @ApiModelProperty(value = "颜色名称")
    private String colorName;

    @ApiModelProperty(value = "规格")
    private String productSpec;

    @ApiModelProperty(value = "型号")
    private String modelCode;

    @ApiModelProperty(value = "创建人编码")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "修改人编码")
    private String updateById;

    @ApiModelProperty(value = "修改人名称")
    private String updateByName;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "商品数量")
    private Long productCount;

    @ApiModelProperty(value = "退款金额 ----传")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "退供金额 ----传")
    private BigDecimal withdrawAmount;

    //未确定已退货数量字段
//    @ApiModelProperty(value = "(已退货数量)实退数量")
//    private Long actualReturnProductCount;

     @ApiModelProperty(value = "(已退货数量)退货数量 ----传")
     private Long returnProductCount;

    @ApiModelProperty(value = "行号  ----传")
    private Long lineCode;

    @NotBlank(message = "仓库编码不能为空 ----传")
    @ApiModelProperty("仓库编码")
    private String warehouseCode;

    @NotBlank(message = "仓库名称不能为空 ----传")
    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @ApiModelProperty("配送中心编码 ---传" )
    private String transportCenterCode;

    @ApiModelProperty("配送中心名称 ---传")
    private String transportCenterName;







}