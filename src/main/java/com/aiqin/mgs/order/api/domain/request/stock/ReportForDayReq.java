package com.aiqin.mgs.order.api.domain.request.stock;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/1/3 17:16
 * Description:
 */
@Data
@ApiModel("日结报表请求实体")
public class ReportForDayReq {


    @ApiModelProperty("查询时间")
    @JsonProperty("selectTime")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String selectTime;

    private String startTime;

    private String endTime;

    @ApiModelProperty("收银员idArray")
    private List<String> cashierIdList;

}
