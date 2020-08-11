package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class BatchInfo {

    @ApiModelProperty(value = "关联商品id")
    private String basicId;

    @ApiModelProperty(value = "批次编码")
    private String batchInfoCode;

    @ApiModelProperty(value = "批次号时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String batchDate;

    @ApiModelProperty(value = "批次号")
    private String batchCode;

    @ApiModelProperty("传入库房编码:1:销售库，2:特卖库")
    private String warehouseTypeCode;

    @ApiModelProperty("批次类型  0：月份批次  1：非月份批次")
    private Integer batchType;

    @ApiModelProperty(value = "商品数量")
    private Long productCount;

    @ApiModelProperty(value = "0. 启用   1.禁用")
    private Integer useStatus;

    @ApiModelProperty(value = "创建人编码")
    private String createBy;

    @ApiModelProperty(value = "修改人编码")
    private String updateBy;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "sku编号")
    private String skuCode;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

}
