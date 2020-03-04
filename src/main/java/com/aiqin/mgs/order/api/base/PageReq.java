package com.aiqin.mgs.order.api.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @功能说明:
 * @author wangxu
 * @date 2018/12/3 0003 19:54
 */
@Data
@ApiModel("分页传输对象")
public class PageReq {
    @ApiModelProperty("当前页")
    private Integer pageNo = 1;

    @ApiModelProperty("每页条数")
    private Integer pageSize = 20;
}
