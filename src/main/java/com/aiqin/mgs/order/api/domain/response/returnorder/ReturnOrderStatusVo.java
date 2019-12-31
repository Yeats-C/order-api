package com.aiqin.mgs.order.api.domain.response.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * description: ReturnOrderListVo
 * date: 2019/12/20 10:13
 * author: hantao
 * version: 1.0
 */
@Data
@ApiModel("退货单状态下拉选返回数据")
public class ReturnOrderStatusVo implements Serializable {

    private static final long serialVersionUID = -4250407967683503902L;

    @ApiModelProperty(value = "状态")
    private Integer status;

    @ApiModelProperty(value = "状态名称")
    private String name;
}
