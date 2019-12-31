package com.aiqin.mgs.order.api.domain.request.returnorder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * description: 冲减单入参实体类
 * date: 2019/12/31 10:32
 * author: hantao
 * version: 1.0
 */
@ApiModel("冲减单入参实体类")
@Data
public class WriteDownOrder implements Serializable {

    @ApiModelProperty(value="订单编码")
    private String orderCode;

    @ApiModelProperty(value="冲减单详情")
    private List<WriteDownOrderDetail> details;


}
