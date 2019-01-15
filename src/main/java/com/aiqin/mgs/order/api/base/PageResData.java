package com.aiqin.mgs.order.api.base;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel("分页数据")
public class PageResData<T> {
    @ApiModelProperty("总条数")
    private Integer totalCount;
    @ApiModelProperty("返回数据")
    private List<T> dataList;

    public PageResData() {
    }

    public PageResData(Integer totalCount, List<T> dataList) {
        this.totalCount = totalCount;
        this.dataList = dataList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

}
