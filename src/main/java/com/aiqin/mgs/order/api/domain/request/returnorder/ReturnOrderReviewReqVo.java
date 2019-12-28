package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
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

    @NotBlank(message = "退货单编码不能为空")
    @ApiModelProperty("退货单编码")
    private String returnOrderCode;

    @ApiModelProperty("是否通过，true-通过，false-不通过")
    private Boolean passStatus;

    @ApiModelProperty("审核备注")
    private String reviewNote;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "操作人", hidden = true)
    private String operator;

    @NotBlank(message = "审核操作状态")
    @ApiModelProperty("审核操作状态(erp内部使用)：1--通过 2--挂账 3--不通过（驳回）99-已取消")
    private Integer operateStatus;

    @NotBlank(message = "申请人姓名")
    @ApiModelProperty(value="申请人姓名")
    private String userName;

    @NotBlank(message = "申请人编码")
    @ApiModelProperty(value="申请人编码")
    private String applier;

    @ApiModelProperty(value="审批人所属部门")
    private String deptCode;

    @NotBlank(message = "加盟商id")
    @ApiModelProperty("加盟商id")
    private String franchiseeId;

    @ApiModelProperty(value="A品券发放审批申请详情")
    private CouponApprovalDetail approvalDetail;

}
