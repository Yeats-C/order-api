package com.aiqin.mgs.order.api.domain.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 操作库存vo
 *
 * @author 杨俊
 * @create 2018-11-012 10:14
 */
@Data
@ApiModel(value = "操作库存vo")
public class OperateStockVo implements Serializable{
    @ApiModelProperty("门店id")
    @JsonProperty("store_id")
    private String storeId;
    @ApiModelProperty("门店编码")
    @JsonProperty("store_code")
    private String storeCode;

    @ApiModelProperty("库存类别  1代表门店自有仓，2代表配送中心大仓")
    @JsonProperty("storage_type")
    private Integer storageType;

    @ApiModelProperty("仓位：1代表陈列仓位，2代表退货仓位，3代表存储仓位")
    @JsonProperty("storage_position")
    private Integer storagePosition;

    @ApiModelProperty("释放状态 0为未释放，1为释放")
    @JsonProperty("release_status")
    private Integer releaseStatus;

    @ApiModelProperty("关联单号")
    @JsonProperty("relate_number")
    private String relateNumber;

    @ApiModelProperty("记录类型 0入库，1出库")
    @JsonProperty("record_type")
    private Integer recordType;

    @ApiModelProperty("记录数量")
    @JsonProperty("record_number")
    private Integer recordNumber;

    @ApiModelProperty("商品SKU码")
    @JsonProperty("product_sku")
    private String productSku;

    @ApiModelProperty("操作人")
    @JsonProperty("operator")
    private String operator;

    @ApiModelProperty("出/入库单号")
    @JsonProperty("master_number")
    private String masterNumber;

    @ApiModelProperty("单据类型 1初始化入库 2门店销售退货 3网店销售退货 4门店销售 5网店销售")
    @JsonProperty("bill_type")
    private Integer billType;

    @JsonProperty("create_by_name")
    @ApiModelProperty("创建人名称")
    public String createByName;

    /*@ApiModelProperty("订单编号")
    @JsonProperty("order_code")
    private String orderCode;
    @ApiModelProperty("sku_code")
    @JsonProperty("sku_code")
    private String skuCode;
    @ApiModelProperty("变更类型，0为入库，1为出库----不传")
    @JsonProperty("stock_change_type")
    private Integer stockChangeType;
    @ApiModelProperty("变更数量")
    @JsonProperty("change_count")
    private Integer changeCount;
    @ApiModelProperty("入库、出库原因")
    @JsonProperty("stock_change_reason")
    private String stockChangeReason;
    @ApiModelProperty("库存是否释放----不传")
    @JsonProperty("release_status")
    private String releaseStatus;*/
}
