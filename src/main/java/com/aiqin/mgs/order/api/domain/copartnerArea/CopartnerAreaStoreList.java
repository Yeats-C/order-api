package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 描述:
 *
 * @author huangzy
 * @create 2020-02-13
 */
@ApiModel("经营区域详情所辖门店列表")
@Data
public class CopartnerAreaStoreList{
	
	@ApiModelProperty(value = "自动生成-经营区域ID")
    @JsonProperty("copartner_area_id")
    private String copartnerAreaId;
    
	@ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;
	
	@ApiModelProperty(value = "门店编码")
    @JsonProperty("store_code")
    private String storeCode;
	
	@ApiModelProperty(value = "门店名称")
    @JsonProperty("store_name")
    private String storeName;
	
}
