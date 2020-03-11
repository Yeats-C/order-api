package com.aiqin.mgs.order.api.domain.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 描述:采购组外部提供接口实体
 */
@Data
@ApiModel("采购组外部提供接口实体")
public class PurchaseGroupVo {
    @ApiModelProperty("主键")
    private Long id;

    @ApiModelProperty("采购组编码")
    private String purchaseGroupCode;

    @ApiModelProperty("采购组名称")
    private String purchaseGroupName;

    @ApiModelProperty("负责人编号")
    private String responsiblePersonCode;

    @ApiModelProperty("负责人名称")
    private String responsiblePersonName;

    // @ApiModelProperty("采购组人员")
    // private List<PurchaseGroupBuyerVo> list;
}