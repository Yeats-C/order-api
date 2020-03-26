package com.aiqin.mgs.order.api.domain.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Joker
 * @date 2018/11/7 下午2:31
 * DTO基类
 */
@Data
public class BaseDTO implements Serializable {


    private static final long serialVersionUID = 9155697475645649642L;
    @ApiModelProperty("自增主键Id")
    public Long id;

    @JsonProperty("create_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    public Date createTime;

    @JsonProperty("update_time")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("更新时间")
    public Date updateTime;

    @JsonProperty("create_by")
    @ApiModelProperty("创建人")
    public String createBy;

    @JsonProperty("create_by_name")
    @ApiModelProperty("创建人名称")
    public String createByName;

    @JsonProperty("update_by")
    @ApiModelProperty("更新人")
    public String updateBy;

    @JsonProperty("update_by_name")
    @ApiModelProperty("更新人名称")
    public String updateByName;

    public BaseDTO() {
    }
}
