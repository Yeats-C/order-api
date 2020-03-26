package com.aiqin.mgs.order.api.domain.response.purchase;

import com.aiqin.mgs.order.api.domain.response.OrderOperationLog;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author: zhao shuai
 * @create: 2020-03-16
 **/
@Data
public class PurchaseDetailResponse extends PurchaseOrder{

    @ApiModelProperty(value="日志文件")
    @JsonProperty("log_list")
    List<OrderOperationLog> logList;

    @ApiModelProperty(value="商品数量")
    @JsonProperty("piece_count")
    private Integer productCount;

    @ApiModelProperty(value="实际商品数量")
    @JsonProperty("actual_piece_count")
    private Integer actualProductCount;

}
