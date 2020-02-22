package com.aiqin.mgs.order.api.util;

/**
 * description: 分页时使用
 * date: 2019/11/22 14:09
 * author: hantao
 * version: 1.0
 */
public class ResultModel<T> {
    /**
     * 总条数
     */
    private Long total;
    /**
     * 数据
     */
    private T result;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ResultModel{" +
                "total=" + total +
                ", result=" + result +
                '}';
    }
}
