package com.aiqin.mgs.order.api.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("关联销售")
public class RelatedSales {

    private Long id;

    private String salseCategoryName;

    private String salseCategoryId;

    private String firstSku;

    private String secondlySku;

    private String lastSku;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}