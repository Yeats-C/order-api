package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: AfterReturnOrderSearchVo
 * date: 2019/12/26 17:22
 * author: hantao
 * version: 1.0
 */

@Data
@ApiModel("售后管理--退货单列表搜索项")
public class AfterReturnOrderSearchVo implements Serializable {

    @ApiModelProperty("退货单号")
    private String ReturnOrderCode;

    @ApiModelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("申请开始时间")
    private String applyStartTime;

    @ApiModelProperty("申请结束时间")
    private String applyEndTime;

    @ApiModelProperty("退货单状态 ")
    private Integer returnOrderStatus;

    @ApiModelProperty("地区层级编码对象")
    private AreaReq areaReq;

    @ApiModelProperty("所有门店id")
    private List<String> storeIds;

    @ApiModelProperty(value = "订单类型 0直送、1配送、2辅采")
    private Integer orderType;

    @ApiModelProperty(value = "退货原因编码 14:质量问题退货 15:一般退货")
    private String returnReasonCode;


}
