package com.aiqin.mgs.order.api.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@ApiModel("促销活动bean")
@Data
public class Activities {

    /**活动id*/
    @ApiModelProperty(value = "活动id")
    @JsonProperty("activity_id")
    private String activityId="";

    /**活动名称*/
    @ApiModelProperty(value = "活动名称")
    @JsonProperty("activity_name")
    private String activityName="";

    /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单*/
    @ApiModelProperty(value = "活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单")
    @JsonProperty("activity_type")
    private Integer activityType;

    /**活动状态1.未开始2.进行中3.已结束*/
    @ApiModelProperty(value = "活动状态1.未开始2.进行中3.已结束")
    @JsonProperty("activity_status")
    private Integer activityStatus;

    /***活动开始时间*/
    @ApiModelProperty(value = "活动开始时间")
    @JsonProperty("begin_time")
    private Date beginTime;

    /***活动终止时间*/
    @ApiModelProperty(value = "活动终止时间")
    @JsonProperty("finish_time")
    private Date finishTime;

    /**发布组织*/
    @ApiModelProperty(value = "发布组织")
    @JsonProperty("publishing_organization")
    private String publishingOrganization="";

    /**活动简介*/
    @ApiModelProperty(value = "活动简介")
    @JsonProperty("activity_brief")
    private String activityBrief="";

    /**PC端展示图片*/
    @ApiModelProperty(value = "PC端展示图片")
    @JsonProperty("activity_pic_pc")
    private String activityPicPc="";

    /**手机端展示图片*/
    @ApiModelProperty(value = "手机端展示图片")
    @JsonProperty("activity_pic_app")
    private String activityPicApp="";


    /***创建人*/
    @ApiModelProperty(value = "创建人")
    @JsonProperty("create_by")
    private String createBy;

    /***修改人*/
    @ApiModelProperty(value = "修改人")
    @JsonProperty("update_by")
    private String updateBy;

    /***创建时间*/
    @ApiModelProperty(value = "创建时间")
    @JsonProperty("create_time")
    private Date createTime;

    /***更新时间*/
    @ApiModelProperty(value = "更新时间")
    @JsonProperty("update_time")
    private Date updateTime;
}
