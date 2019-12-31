package com.aiqin.mgs.order.api.domain.request.returnorder;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * description: LogisticsVo
 * date: 2019/12/28 13:48
 * author: hantao
 * version: 1.0
 */
@ApiModel("更新物流单信息（向爱掌柜提供接口）实体类")
@Data
public class LogisticsVo implements Serializable {

    @ApiModelProperty(value="退货编码")
    private String returnOrderCode;

    @ApiModelProperty(value="物流公司编码")
    private String logisticsCompanyCode;

    @ApiModelProperty(value="物流公司名称")
    private String logisticsCompanyName;

    @ApiModelProperty(value="物流单号")
    private String logisticsCode;

}
