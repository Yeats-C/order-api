package com.aiqin.mgs.order.api.domain.request.returnorder;

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

    @NotBlank(message = "退货单编码不能为空")
    @ApiModelProperty("退货单编码")
    private String returnOrderId;

    @ApiModelProperty(value = "操作人", hidden = true)
    private String operator;

    @NotBlank(message = "退货单状态不能为空")
    @ApiModelProperty("退货单状态(供应链使用):4-等待退货验收，5-等待退货入库 11-退货完成")
    private Integer operateStatus;

//    @NotBlank(message = "加盟商id")
//    @ApiModelProperty("加盟商id")
//    private String franchiseeId;

//    @ApiModelProperty(value="退货单详情")
//    private List<ReturnOrderDetail> details;

    @ApiModelProperty(value="修改退货单详情实退数量")
    private ReturnOrderDetailReviewApiReqVo returnOrderDetailReviewApiReqVo;

}
