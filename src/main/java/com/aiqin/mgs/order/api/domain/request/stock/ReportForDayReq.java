package com.aiqin.mgs.order.api.domain.request.stock;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author wanghao
 */
@Data
@ApiModel("日结报表请求实体")
public class ReportForDayReq {

    private String startTime;

    private String endTime;

    @ApiModelProperty("收银员idArray")
    private List<String> cashierIdList;

    private String distributorId;

}
