package com.aiqin.mgs.order.api.domain.po.order;


import lombok.Data;

import java.util.Date;

/**
 * 订单相关表通用字段
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 10:13
 */
@Data
public class ErpOrderBase {

    /***主键*/
    private Long id;
    /***创建时间*/
    private Date createTime;
    /***创建人id*/
    private String createById;
    /***创建人姓名*/
    private String createByName;
    /***更新时间*/
    private Date updateTime;
    /***修改人id*/
    private String updateById;
    /***修改人姓名*/
    private String updateByName;
    /***数据状态 1有效 0删除*/
    private Integer status;

}
