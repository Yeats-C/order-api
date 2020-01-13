package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;

import java.util.List;

/**
 * 订单操作日志service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/10 9:43
 */
public interface ErpOrderOperationLogService {

    /**
     * 保存销售单日志
     *
     * @param operationCode     销售单编码
     * @param operationTypeEnum 操作类型
     * @param orderStatus       销售单状态
     * @param remark            备注
     * @param auth              操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 14:39
     */
    void saveOrderOperationLog(String operationCode, ErpLogOperationTypeEnum operationTypeEnum, Integer orderStatus, String remark, AuthToken auth);

    /**
     * 查询销售单日志列表
     *
     * @param orderCode 销售单编码
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 14:40
     */
    List<ErpOrderOperationLog> selectOrderOperationLogList(String orderCode);

    /**
     * 复制日志
     *
     * @param orderCode 单据号
     * @param list      日志列表
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 14:41
     */
    void copySplitOrderLog(String orderCode, List<ErpOrderOperationLog> list);
}
