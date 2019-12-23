package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 供应链调用实体类
 *
 * @author hantao
 * @createTime 2019-12-23
 * @description
 */
@Data
@ApiModel
public class ReturnOrderReviewApiReqVo implements Serializable {

    private static final long serialVersionUID = 731706668149842489L;

    @NotBlank(message = "售后类型不能为空")
    @ApiModelProperty("售后单ID")
    private String returnOrderId;

    @JSONField(serialize = false)
    @ApiModelProperty(value = "操作人", hidden = true)
    private String operator;

    @NotBlank(message = "售后类型不能为空")
    @ApiModelProperty("供应链使用:4-等待退货验收，5-等待退货入库 11-退货完成")
    private Integer operateStatus;

    @NotBlank(message = "售后类型不能为空")
    @ApiModelProperty("加盟商id")
    private String franchiseeId;

    @ApiModelProperty(value="A品券发放审批申请详情")
    private ReturnOrderDetail returnOrderDetail;

}
