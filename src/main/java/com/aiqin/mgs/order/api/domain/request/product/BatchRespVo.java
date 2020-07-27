package com.aiqin.mgs.order.api.domain.request.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: mamingze
 * @Date: 2020-05-14 10:36
 * @Description:
 */
@Data
public class BatchRespVo {
    @ApiModelProperty("批次号")
    @JsonProperty(value = "batch_code")
    private String batchCode;


    @ApiModelProperty("批次唯一标识")
    @JsonProperty(value = "batch_info_code")
    private String batchInfoCode;


    @ApiModelProperty("批次日期")
    @JsonProperty(value = "batch_date")
    private String batchDate;


    @ApiModelProperty("批次对应库房编号")
    @JsonProperty(value = "warehouse_code")
    private String warehouseCode;


    @ApiModelProperty("批次库存")
    @JsonProperty(value = "batch_num")
    private Long batchNum;


    @ApiModelProperty("批次价格")
    @JsonProperty("batch_price")
    private BigDecimal batchPrice=new BigDecimal("0");

    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单*/
    @ApiModelProperty(value = "活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单【爱掌柜使用字段】")
    @JsonProperty("activity_type")
    private Integer activityType;
}
