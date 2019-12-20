package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * OrderAfterReviewReqVo
 *
 * @author zhangtao
 * @createTime 2019-01-21
 * @description
 */
@Data
@ApiModel
public class ReturnOrderReviewReqVo implements Serializable {

    private static final long serialVersionUID = 731706668149842489L;
    @ApiModelProperty("售后单ID")
    @JsonProperty("return_order_id")
    private String returnOrderId;

    @ApiModelProperty("是否通过，true-通过，false-不通过")
    @JsonProperty("pass_status")
    private Boolean passStatus;

    @ApiModelProperty("审核备注")
    @JsonProperty("review_note")
    private String reviewNote;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "操作人", hidden = true)
    private String operator;

    @ApiModelProperty("审核操作状态：1--通过 2--挂账 3--不通过（驳回）")
    @JsonProperty("operate_status")
    private Integer operateStatus;

    @ApiModelProperty(value="申请人姓名")
    private String userName;

    @ApiModelProperty(value="申请人编码")
    private String applier;

    @ApiModelProperty(value="审批人所属部门")
    private String deptCode;

    @ApiModelProperty("加盟商id")
    private String franchiseeId;

    @ApiModelProperty(value="A品券发放审批申请详情")
    private CouponApprovalDetail approvalDetail;

}
