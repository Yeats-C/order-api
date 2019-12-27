package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 供应链调用实体类
 *
 * @author hantao
 * @createTime 2019-12-23
 * @description
 */
@Data
@ApiModel
public class ReturnOrderDetailReviewApiReqVo implements Serializable {

    private static final long serialVersionUID = 731706668149842489L;

    @ApiModelProperty(value = "业务id")
    private String returnOrderDetailId;

    @ApiModelProperty(value = "实退数量")
    private Long actualReturnProductCount;

//    @ApiModelProperty(value = "修改人姓名")
//    private String updateByName;
//
//    @ApiModelProperty(value = "修改人编码")
//    private String updateById;

    @ApiModelProperty(value = "修改人编码")
    List<ReturnOrderDetailReviewApiReqVo> list;

}
