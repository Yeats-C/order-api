package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author boyd
 * @since 2018-09-05
 */
@ApiModel(value = "系统菜单")
public class SystemResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;
    /**
     * 
     */
    @ApiModelProperty(value = "菜单编码")
    private String resourceCode;
    /**
     * 菜单链接
     */
    @ApiModelProperty(value = "菜单链接")
    private String resourceLink;
    /**
     * 菜单名称
     */
    @ApiModelProperty(value = "菜单名称")
    private String resourceName;
    /**
     * 显示名称
     */
    @ApiModelProperty(value = "显示名称")
    private String resourceShowName;
    /**
     * 菜单等级
     */
    @ApiModelProperty(value = "菜单等级")
    private Integer resourceLevel;
    /**
     * 排序
     */
    @ApiModelProperty(value = "排序")
    private Integer resourceOrder;
    /**
     * 
     */
    @ApiModelProperty(value = "菜单图标")
    private String resourceIcon;
    /**
     * 0禁用1启用
     */
    @ApiModelProperty(value = "项目编码")
    private Integer resourceStatus;
    /**
     * 
     */
    @ApiModelProperty(value = "父级菜单编码")
    private String parentCode;
    /**
     * 所属系统
     */
    @ApiModelProperty(value = "所属系统")
    private String systemCode;
    /**
     * 同步批次标识
     */
    @ApiModelProperty(value = "同步批次标识")
    private String planMark;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private List<SystemResource> resources;
    
    @JsonProperty(value="resource_mark")
    private List<String> resourceMark;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<String> getResourceMark() {
        return resourceMark;
    }

    public void setResourceMark(List<String> resourceMark) {
        this.resourceMark = resourceMark;
    }

    public String getResourceShowName() {
        return resourceShowName;
    }

    public void setResourceShowName(String resourceShowName) {
        this.resourceShowName = resourceShowName;
    }

    public List<SystemResource> getResources() {
        return resources;
    }

    public void setResources(List<SystemResource> resources) {
        this.resources = resources;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceLink() {
        return resourceLink;
    }

    public void setResourceLink(String resourceLink) {
        this.resourceLink = resourceLink;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Integer getResourceLevel() {
        return resourceLevel;
    }

    public void setResourceLevel(Integer resourceLevel) {
        this.resourceLevel = resourceLevel;
    }

    public Integer getResourceOrder() {
        return resourceOrder;
    }

    public void setResourceOrder(Integer resourceOrder) {
        this.resourceOrder = resourceOrder;
    }

    public String getResourceIcon() {
        return resourceIcon;
    }

    public void setResourceIcon(String resourceIcon) {
        this.resourceIcon = resourceIcon;
    }

    public Integer getResourceStatus() {
        return resourceStatus;
    }

    public void setResourceStatus(Integer resourceStatus) {
        this.resourceStatus = resourceStatus;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getPlanMark() {
        return planMark;
    }

    public void setPlanMark(String planMark) {
        this.planMark = planMark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

}
