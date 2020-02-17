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
@ApiModel("经营区域详情所辖门店")
@Data
public class CopartnerAreaStoreVo extends PagesRequest{
	

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
	
	@ApiModelProperty(value = "备注")
    @JsonProperty("memo")
    private String memo;
	
	@ApiModelProperty("创建人编码")
    @JsonProperty("create_by")
    private String createBy;
	
	@ApiModelProperty("修改人编码")
	@JsonProperty("update_by")
	private String updateBy;
		
    @ApiModelProperty("创建日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("create_time")
	private Date createTime;
	  
	@ApiModelProperty("修改日期")
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonProperty("update_time")
	private Date updateTime;
	
	
	public CopartnerAreaStoreVo(String copartnerAreaId) {
		this.copartnerAreaId = copartnerAreaId;
	}
	
	public CopartnerAreaStoreVo() {
	}
	
}
