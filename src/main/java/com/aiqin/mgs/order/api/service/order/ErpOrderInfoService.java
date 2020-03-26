package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderReturnRequestEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderCarryOutNextStepRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSignRequest;

import java.util.List;

/**
 * 订单操作service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/21 11:00
 */
public interface ErpOrderInfoService {

    /**
     * 保存订单数据
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:38
     */
    void saveOrder(ErpOrderInfo po, AuthToken auth);

    /**
     * 保存订单数据（不保存日志）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:38
     */
    void saveOrderNoLog(ErpOrderInfo po, AuthToken auth);

    /**
     * 根据主键更新订单数据
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:38
     */
    void updateOrderByPrimaryKeySelective(ErpOrderInfo po, AuthToken auth);

    /**
     * 根据主键更新订单数据（不保存日志）
     *
     * @param po
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/11 10:19
     */
    void updateOrderByPrimaryKeySelectiveNoLog(ErpOrderInfo po, AuthToken auth);

    /**
     * 订单拆单逻辑
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:50
     */
    void orderSplit(String orderCode, AuthToken auth);

    /**
     * 同步订单到供应链
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/30 15:26
     */
    void orderSendToSupply(String orderCode, AuthToken auth);

    /**
     * 订单签收
     *
     * @param erpOrderSignRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 9:39
     */
    void orderSign(ErpOrderSignRequest erpOrderSignRequest);

    /**
     * 订单流程校正
     *
     * @param erpOrderCarryOutNextStepRequest
     * @param auth
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/4 14:10
     */
    void orderCarryOutNextStep(ErpOrderCarryOutNextStepRequest erpOrderCarryOutNextStepRequest, AuthToken auth);

    /**
     * 订单发起冲减单
     *
     * @param orderCode
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/11 10:02
     */
    void orderScourSheet(String orderCode);

    /**
     * 修改订单退货状态
     *
     * @param orderCode              订单号
     * @param orderReturnRequestEnum 订单退货请求类型
     * @param returnQuantityList     退货数量（行号lineCode + 行退货数量returnProductCount） 只有状态是退货成功才传这个参数
     * @param personId               操作人id
     * @param personName             操作人名称
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/14 16:08
     */
    void updateOrderReturnStatus(String orderCode, ErpOrderReturnRequestEnum orderReturnRequestEnum, List<ErpOrderItem> returnQuantityList, String personId, String personName);

}
