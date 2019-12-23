package com.aiqin.mgs.order.api.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Createed by sunx on 2018/9/4.<br/>
 */
@ApiModel(value = "分页请求实体", description = "分页请求实体")
public class PageRequestVO<T> {
    @ApiModelProperty("查询对象")
    private T searchVO;
    @ApiModelProperty("当前页数")
    @JsonProperty("page_no")
    private Integer pageNo;
    @ApiModelProperty("页面大小")
    @JsonProperty("page_size")
    private Integer pageSize;

    @ApiModelProperty(value = "不用传这个", hidden = true)
    @JsonProperty("page_index")
    private Integer beginIndex;

    public PageRequestVO() {
    }

    public PageRequestVO(T searchVO, Integer pageNo, Integer pageSize) {
        this.searchVO = searchVO;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public Integer getBeginIndex() {
        beginIndex = (getPageNo() - 1) * getPageSize();
        return beginIndex;
    }

    public void setBeginIndex(Integer beginIndex) {
        this.beginIndex = beginIndex;
    }

    public T getSearchVO() {
        return searchVO;
    }

    public void setSearchVO(T searchVO) {
        this.searchVO = searchVO;
    }

    public Integer getPageSize() {
        pageSize = (pageSize == null || pageSize < 1) ? 10 : pageSize;
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        pageNo = (pageNo == null || pageNo < 1) ? 1 : pageNo;
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    @Override
    public String toString() {
        return "PageRequestVO{" +
                "searchVO=" + searchVO +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", beginIndex=" + beginIndex +
                '}';
    }
}
