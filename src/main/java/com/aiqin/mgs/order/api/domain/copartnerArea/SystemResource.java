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
    @ApiModelProperty(value = "菜单编码")
    private String resourceCode;
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
    
    @ApiModelProperty(value = "勾选标识: 1:已勾选")
    @JsonProperty("check_flag")
    private Integer checkFlag;
    
    /**
     * 
     */
    @ApiModelProperty(value = "父级菜单编码")
    private String parentCode;

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


    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
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

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

	public Integer getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Integer checkFlag) {
		this.checkFlag = checkFlag;
	}
    
    


}
