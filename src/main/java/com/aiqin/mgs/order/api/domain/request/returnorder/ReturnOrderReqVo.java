package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * description: 退货单添加实体类
 * date: 2019/12/19 17:50
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel
public class ReturnOrderReqVo implements Serializable {

    private static final long serialVersionUID = -4250407967683503902L;

    @NotBlank(message = "订单编号不能为空")
    @ApiModelProperty(value = "订单号")
    private String orderStoreCode;

    @NotNull(message = "订单类型不能为空")
    @ApiModelProperty(value = "订单类型(订单类型 0直送、1配送、2辅采)")
    private Integer orderType;

    @NotBlank(message = "公司编码不能为空")
    @ApiModelProperty(value = "公司编码")
    private String companyCode;

    @NotBlank(message = "门店id不能为空")
    @ApiModelProperty(value = "门店id")
    private String storeId;

    @NotBlank(message = "门店编码不能为空")
    @ApiModelProperty(value = "门店编码")
    private String storeCode;

    @NotBlank(message = "门店类型编码不能为空")
    @ApiModelProperty(value = "门店类型编码")
    private String natureCode;

    @NotBlank(message = "门店类型名称不能为空")
    @ApiModelProperty(value = "门店类型名称")
    private String natureName;

    @NotBlank(message = "仓库编码不能为空")
    @ApiModelProperty("仓库编码")
    private String warehouseCode;

    @NotBlank(message = "仓库名称不能为空")
    @ApiModelProperty("仓库名称")
    private String warehouseName;

    @NotBlank(message = "售后类型不能为空")
    @ApiModelProperty(value = "退货类型  0客户退货、1缺货退货、2售后退货")
    private Integer returnOrderType;

    @ApiModelProperty(value = "公司名称")
    private String companyName;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty("配送中心编码")
    private String transportCenterCode;

    @ApiModelProperty("配送中心名称")
    private String transportCenterName;

    @ApiModelProperty(value = "售后备注信息")
    private String remark;

    @ApiModelProperty(value = "处理方式，0-整单退、1-部分退")
    private Integer processType;

    @ApiModelProperty(value = "收货地址")
    private String receiveAddress;

    @ApiModelProperty(value = "邮编")
    private String zipCode;

    @ApiModelProperty(value = "收货人")
    private String receivePerson;

    @ApiModelProperty(value = "收货人电话")
    private String receiveMobile;

    @ApiModelProperty(value = "物流公司编码")
    private String logisticsCompanyCode;

    @ApiModelProperty(value = "物流公司名称")
    private String logisticsCompanyName;

    @ApiModelProperty(value = "物流单号")
    private String logisticsCode;

    @ApiModelProperty(value = "创建人编码")
    private String createById;

    @ApiModelProperty(value = "创建人名称")
    private String createByName;

    @ApiModelProperty(value = "商品数量")
    private Long productCount;

    @ApiModelProperty(value = "退货金额")
    private BigDecimal returnOrderAmount;

    @ApiModelProperty(value = "退货原因编码")
    private String returnReasonCode;

    @ApiModelProperty(value = "退货原因描述")
    private String returnReasonContent;

    @ApiModelProperty(value = "来源类型:1-web收银台 2-安卓收银台 3-微信公众号")
    private Integer sourceType;

    @ApiModelProperty(value = "订单类别：1：收单配送 2：首单赠送 3：配送补货 4：首单直送 5：直送补货")
    private Integer orderCategory;

    @ApiModelProperty(value = "明细")
    private List<ReturnOrderDetail> details;

}
