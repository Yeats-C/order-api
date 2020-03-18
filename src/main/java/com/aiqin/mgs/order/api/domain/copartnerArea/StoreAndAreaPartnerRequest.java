package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.aiqin.mgs.order.api.base.PagesRequest;
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
@ApiModel("同步门店查询")
@Data
public class StoreAndAreaPartnerRequest{
	

    
	@ApiModelProperty(value = "门店id")
    @JsonProperty("store_id")
    private String storeId;
	
	@ApiModelProperty(value = "所属合伙人公司名称")
    @JsonProperty("partner")
    private String partner;
	
	@ApiModelProperty(value = "所属合伙人公司id")
    @JsonProperty("partner_code")
    private String partnerCode;
	
}
