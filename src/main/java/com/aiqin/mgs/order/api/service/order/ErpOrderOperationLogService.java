package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
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

    void saveOrderOperationLog(String operationCode, ErpLogSourceTypeEnum sourceTypeEnum, ErpLogOperationTypeEnum operationTypeEnum, String operationContent, String remark, AuthToken auth);

    List<ErpOrderOperationLog> selectOrderOperationLogList(String orderCode);

    void copySplitOrderLog(String orderCode, List<ErpOrderOperationLog> list);
}
