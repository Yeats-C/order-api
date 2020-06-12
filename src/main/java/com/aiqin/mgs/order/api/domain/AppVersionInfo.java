package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author jinghaibo
 * Date: 2020/6/5 10:45
 * Description: app版本信息
 */
@Data
public class AppVersionInfo {
    //id 类型。平台 版本号。下载地址 更新描述 更新人 更新时间 状态
    private Integer id;
    /**
     * 系统类型，1 安卓，2 苹果
     */
    @NotNull
    @ApiModelProperty(value = "系统类型，1 安卓，2 苹果")
    @JsonProperty("system_type")
    private Integer systemType;

    @NotNull
    @ApiModelProperty(value = "系统版本号")
    @JsonProperty("app_version")
    private String appVersion;


    @ApiModelProperty(value = "类型，1 POS收银App，2 爱掌柜app")
    @JsonProperty("app_type")
    private Integer appType;

    @NotNull
    @ApiModelProperty(value = "内部系统版本号")
    @JsonProperty("app_build")
    private String appBuild;

    @NotNull
    @ApiModelProperty(value = "下载地址")
    @JsonProperty("update_url")
    private String updateUrl;

    @ApiModelProperty(value = "更新信息")
    @JsonProperty("update_desc")
    private String updateDesc;

    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value = "更新人ID")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value = "更新")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "状态：0：已删除，1：上架中，2：已下架")
    @JsonProperty("state")
    private Integer state;
}
