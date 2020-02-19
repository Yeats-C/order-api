package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/2/12 17:47
 * Description:
 */
@ApiModel("记录出入库流水(爱掌柜)")
@Data
@AllArgsConstructor
public class InventoryDetailRequest {
    @ApiModelProperty(value = "门店ID")
    @JsonProperty("store_id")
    private String storeId;

    @ApiModelProperty(value = "门店编号")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty(value = "记录类型")
    @JsonProperty("record_type")
    private Integer recordType;

    @ApiModelProperty(value = "出/入库单号")
    @JsonProperty("master_number")
    private String masterNumber;

    @ApiModelProperty(value = "关联单号")
    @JsonProperty("relate_number")
    private String relateNumber;

    @ApiModelProperty(value = "单据类型")
    @JsonProperty("bill_type")
    private Integer billType;

    @ApiModelProperty(value = "库存类别")
    @JsonProperty("storage_type")
    private Integer storageType;

    @ApiModelProperty(value = "仓位")
    @JsonProperty("storage_position")
    private Integer storagePosition;

    @ApiModelProperty(value = "操作人id")
    @JsonProperty("operator")
    private String operator;

    @JsonProperty("create_by_name")
    @ApiModelProperty("操作人名称")
    public String createByName;

    @JsonProperty("inventory_record_requests")
    @ApiModelProperty("出入库流水明细")
    public List<OperateStockVo> inventoryRecordRequests;

    public InventoryDetailRequest(){

    }
}
