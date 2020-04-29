package com.aiqin.mgs.order.api.domain.po.gift;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("门店赠品比例信息")
public class NewStoreGradient {

    @ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;

    @ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "序号")
    @JsonProperty("serial_number")
    private Long serialNumber;

    @ApiModelProperty(value = "梯度最小值")
    @JsonProperty("minimum_value")
    private Long minimumValue;

    @ApiModelProperty(value = "梯度最大值")
    @JsonProperty("max_value")
    private Long tiDuMaxValue;

    @ApiModelProperty(value = "返赠品比列")
    @JsonProperty("rebates_proportion")
    private Double rebatesProportion;

    @ApiModelProperty(value = "审批编码")
    @JsonProperty("form_no")
    private String formNo;

    @ApiModelProperty(value = "审批状态(0审批中 1通过 2不通过)")
    @JsonProperty("approval_status")
    private Long approvalStatus;

    @ApiModelProperty(value = "门店梯度集合")
    @JsonProperty("store_gradient_list")
    private List<StoreGradient> storeGradientList;

    @ApiModelProperty(value="区别formNo")
    @JsonProperty("index_dic")
    private String indexDic;

    @ApiModelProperty(value = "上月补货")
    @JsonProperty("last_month_purchase")
    private String lastMonthPurchase;
}
