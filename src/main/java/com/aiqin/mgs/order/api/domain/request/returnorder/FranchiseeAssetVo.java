package com.aiqin.mgs.order.api.domain.request.returnorder;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("虚拟资产")
public class FranchiseeAssetVo {

    @ApiModelProperty(value="优惠券类型，0-物流券 1-服纺券 2-A品券")
    private Integer couponType;

    @ApiModelProperty(value = "优惠券名称")
    private String couponName;

    @ApiModelProperty(value = "面值")
    private BigDecimal nominalValue;

    @ApiModelProperty(value = "有效期开始时间")
    private Date validityStartTime;

    @ApiModelProperty(value = "使用状态")
    private String activeCondition;

    @ApiModelProperty(value = "加盟商id")
    private String franchiseeId;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "有效期结束时间")
    private Date validityEndTime;

    @ApiModelProperty(value = "优惠券编码")
    private String couponCode;

    @ApiModelProperty(value = "获得时间")
    private Date createTime;

    /**
     * 添加虚拟资产使用
     */
    private List<FranchiseeAssetVo> list;

}
