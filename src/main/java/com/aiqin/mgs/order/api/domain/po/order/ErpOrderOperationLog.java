package com.aiqin.mgs.order.api.domain.po.order;

import lombok.Data;

import java.util.Date;

/**
 * 订单操作日志实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:58
 */
@Data
public class ErpOrderOperationLog {

    /***主键*/
    private Long id;
    /***来源编码*/
    private String operationCode;
    /***日志类型 0 .新增 1.修改 2.删除 3.下载*/
    private Integer operationType;
    /***来源类型 0.销售 1.采购 2.退货  3.退供*/
    private Integer sourceType;
    /***日志内容*/
    private String operationContent;
    /***备注*/
    private String remark;
    /***启用状态 0.启用 1.禁用*/
    private Integer useStatus;
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

}
