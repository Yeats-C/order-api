package com.aiqin.mgs.order.api.domain.request;

import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Api("封装参数-商品总库菜单-统计门店商品在各个渠道的订单数..")
@Data
public class ProductStoreRequest extends PagesRequest {

    @ApiModelProperty("skuidlist")
    @JsonProperty("sku_list")
    private List<String> skuList;

    @ApiModelProperty("来源类型list")
    @JsonProperty("origin_type_list")
    private List<Integer> originTypeList;

    @ApiModelProperty(value = "升降序标识，asc  升序（低）  desc 降序（高）")
    @JsonProperty("asc_or_desc")
    private String ascOrDesc;

    public List<String> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<String> skuList) {
        this.skuList = skuList;
    }

    public List<Integer> getOriginTypeList() {
        return originTypeList;
    }

    public void setOriginTypeList(List<Integer> originTypeList) {
        this.originTypeList = originTypeList;
    }

    public ProductStoreRequest() {

    }

    public ProductStoreRequest(@NotNull List<String> skuList, List<Integer> originTypeList) {
        this.skuList = skuList;
        this.originTypeList = originTypeList;
    }

    @Override
    public String toString() {
        return "ProductStoreRequest{" +
                "skuList=" + skuList +
                ", originTypeList=" + originTypeList +
                '}';
    }
}
