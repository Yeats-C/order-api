package com.aiqin.mgs.order.api.domain.request.purchase;


import com.aiqin.mgs.order.api.base.PagesRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class PurchaseOrderProductRequest extends PagesRequest {

    @ApiModelProperty(value="采购单id")
    @JsonProperty("purchase_order_id")
    private String purchaseOrderId;

    @ApiModelProperty(value="是否分页 1不分页  0 分页")
    @JsonProperty("is_page")
    private Integer isPage;

    public PurchaseOrderProductRequest(){

    }

    public PurchaseOrderProductRequest(String purchaseOrderId, Integer isPage) {
        this.purchaseOrderId = purchaseOrderId;
        this.isPage = isPage;
    }
}

