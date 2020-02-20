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
@ApiModel("保存经营区域信息")
@Data
public class CopartnerAreaSave{
	
	@ApiModelProperty(value = "基本信息")
    @JsonProperty("copartner_area")
    private CopartnerAreaDetail copartnerAreaDetail;
	
	@ApiModelProperty(value = "门店列表")
    @JsonProperty("store_list")
    private List<CopartnerAreaStoreList> storeList;
	
	@ApiModelProperty(value = "公司人员及权限")
    @JsonProperty("role_list")
    private List<CopartnerAreaRoleList> roleList;
}
