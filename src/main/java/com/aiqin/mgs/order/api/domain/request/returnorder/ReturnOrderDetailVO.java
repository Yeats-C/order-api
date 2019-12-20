package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: 修改退货单详情model
 * date: 2019/12/18 14:10
 * author: hantao
 * version: 1.0
 */
@ApiModel
@Data
public class ReturnOrderDetailVO implements Serializable {

    @ApiModelProperty("售后单ID")
    @JsonProperty("return_order_code")
    private String returnOrderCode;

    @ApiModelProperty(value = "退货申请人id")
    @JsonProperty("create_by_id")
    private String createId;

    @ApiModelProperty(value = "退货申请人姓名")
    @JsonProperty("create_by_name")
    private String creator;

    @ApiModelProperty(value = "退货单详情list")
    @JsonProperty("details")
    private List<ReturnOrderDetail> details;

}
