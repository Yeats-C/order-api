/**
 * 
 */
package com.aiqin.mgs.order.api.base;


import com.fasterxml.jackson.annotation.JsonProperty;

public class PageResData {
    @JsonProperty("totalCount")
    private Integer totalCount;
    @JsonProperty("dataList")
    private Object dataList;

    public PageResData() {
    }

    public PageResData(Integer totalCount, Object dataList) {
        this.totalCount = totalCount;
        this.dataList = dataList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getDataList() {
        return dataList;
    }

    public void setDataList(Object dataList) {
        this.dataList = dataList;
    }

}
