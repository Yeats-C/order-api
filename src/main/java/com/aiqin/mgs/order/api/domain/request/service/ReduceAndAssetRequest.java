package com.aiqin.mgs.order.api.domain.request.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("扣减和添加资产list请求")
public class ReduceAndAssetRequest {

    @ApiModelProperty(value = "服务商品资产list")
    @JsonProperty("service_project_asset_list")
    private List<ServiceProjectAsset> serviceProjectAssetList;

    @ApiModelProperty(value = "服务商品扣减信息list")
    @JsonProperty("service_project_reduce_detail_list")
    private List<ServiceProjectReduceDetail> serviceProjectReduceDetailList;

    private ServiceProjectReduceDetail serviceProjectReduceDetail;

    private ServiceProjectAsset serviceProjectAsset;
}
