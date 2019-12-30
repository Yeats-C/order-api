package com.aiqin.mgs.order.api.domain;

import lombok.Data;

import java.util.Date;
@Data
public class OrderStoreDetailBatch {
    private Long id;

    private String orderStoreDetailBatchId;

    private String orderStoreCode;

    private String skuCode;

    private String skuName;

    private String batchCode;

    private Date productDate;

    private String batchRemark;

    private String unitCode;

    private String unitName;

    private Long lineCode;

    private Integer useStatus;

    private String createById;

    private String createByName;

    private String updateById;

    private String updateByName;

    private Date createTime;

    private Date updateTime;
}