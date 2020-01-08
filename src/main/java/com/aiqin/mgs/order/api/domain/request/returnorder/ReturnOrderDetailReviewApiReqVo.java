package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class ReturnOrderDetailReviewApiReqVo implements Serializable {

    private static final long serialVersionUID = 731706668149842489L;

    @ApiModelProperty(value = "行号")
    private Integer lineCode;

    @ApiModelProperty(value = "实退数量")
    private Long actualReturnProductCount;

}
