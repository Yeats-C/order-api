package com.aiqin.mgs.order.api.domain.po.gift;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("门店梯度信息")
@JsonInclude(JsonInclude.Include.ALWAYS)
public class StoreGradient {

    @ApiModelProperty("门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "序号")
    @JsonProperty("serial_number")
    private Long serialNumber;

    @ApiModelProperty(value = "梯度名称")
    @JsonProperty("serial_number_name")
    private String serialNumberName;

    @ApiModelProperty(value = "梯度最小值")
    @JsonProperty("minimum_value")
    private Long minimumValue;

    @ApiModelProperty(value = "梯度最大值")
    @JsonProperty("max_value")
    private Long tiDuMaxValue;

    @ApiModelProperty(value = "返赠品比列")
    @JsonProperty("rebates_proportion")
    private Double rebatesProportion;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人id")
    @JsonProperty("create_person_id")
    private String createPersonId;

    @ApiModelProperty(value = "创建人名称")
    @JsonProperty("create_person_name")
    private String createPersonName;

    @ApiModelProperty(value = "审批编码")
    @JsonProperty("form_no")
    private String formNo;

    @ApiModelProperty(value = "集合")
    @JsonProperty("store_gradients")
    private List<StoreGradient> storeGradients;

    @ApiModelProperty(value="区别formNo")
    @JsonProperty("index_dic")
    private String indexDic;

}
