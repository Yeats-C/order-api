package com.aiqin.mgs.order.api.domain.copartnerArea;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author boyd
 * @since 2018-09-05
 */
@ApiModel(value = "系统方法")
public class SystemMethod implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    private Long id;
    /**
     * 
     */
    @ApiModelProperty(value = "方法编码")
    private String methodCode;
    /**
     * 
     */
    @ApiModelProperty(value = "方法名称")
    private String methodName;
    /**
     * 
     */
    @ApiModelProperty(value = "系统编码")
    private String systemCode;
    /**
     * 
     */
    @ApiModelProperty(value = "菜单编码")
    private String resourceCode;
    /**
     * 
     */
    @ApiModelProperty(value = "菜单名称")
    private String resourceName;
    /**
     * 方法描述
     */
    @ApiModelProperty(value = "方法描述")
    private String methodDesciption;
    /**
     * 同步批次标识
     */
    @ApiModelProperty(value = "同步批次标志")
    private String planMark;
    /**
     * 
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 
     */
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private boolean checkout;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isCheckout() {
        return checkout;
    }

    public void setCheckout(boolean checkout) {
        this.checkout = checkout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMethodCode() {
        return methodCode;
    }

    public void setMethodCode(String methodCode) {
        this.methodCode = methodCode;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getResourceCode() {
        return resourceCode;
    }

    public void setResourceCode(String resourceCode) {
        this.resourceCode = resourceCode;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getMethodDesciption() {
        return methodDesciption;
    }

    public void setMethodDesciption(String methodDesciption) {
        this.methodDesciption = methodDesciption;
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
