package com.aiqin.mgs.order.api.domain.po.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by 爱亲 on 2019/7/23.
 */
@Data
@ApiModel("DL返回实体")
public class DLResponse<T> {

    /**
     * 全局返回码说明如下：
     * <p>
     * 返回码	说明
     * 0	成功
     * 1	程序处理逻辑异常
     * -1	服务器异常
     * 4002	未经过授权
     * 10001	不合法的参数
     * 10002	不合法的请求格式
     */
    @ApiModelProperty("全局返回码")
    private int status;

    @ApiModelProperty("消息")
    private String message;

    /**
     * 只有在状态码为1的情况才会有data返回
     * 返回信息里的data 通常情况下是没有data 字段返回过来的  只有在特定可预知的情况下才会有data字段返回。不需要data的时候解析data会解析失败
     */
    @ApiModelProperty("数据")
    private T data;

    public DLResponse() {
    }

    public DLResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public DLResponse(T data) {
        this.status = 0;
        this.message = "SUCCESS";
        this.data = data;
    }

    public static DLResponse<Object> success(Object data) {
        return new DLResponse<>(data);
    }

    public static DLResponse<Object> success(int status,String message) {
        return new DLResponse<>(status,message);
    }

    public static DLResponse success() {
        return new DLResponse(0, "SUCCESS");
    }


}