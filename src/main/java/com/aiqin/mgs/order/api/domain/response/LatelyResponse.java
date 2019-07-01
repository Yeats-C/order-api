/*****************************************************************

* 模块名称：封装-收银员交班收银情况统计
* 开发人员: 黄祉壹
* 开发时间: 2018-12-03 

* ****************************************************************************/
package com.aiqin.mgs.order.api.domain.response;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("封装-收银员交班收银情况统计")
@Data
public class LatelyResponse{
    
	@ApiModelProperty("最近消费时间")
    @JsonProperty("pay_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pay_time;

    @ApiModelProperty("最近消费金额")
    @JsonProperty("price")
    private Integer price;
    
}



