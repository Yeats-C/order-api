package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderRefundTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayFeeTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderRefundDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderRefund;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ErpOrderRefundServiceImpl implements ErpOrderRefundService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRefundServiceImpl.class);

    @Resource
    private ErpOrderRefundDao erpOrderRefundDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ReturnOrderInfoService returnOrderInfoService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;

    @Override
    public ErpOrderRefund getOrderRefundByRefundId(String payId) {
        ErpOrderRefund orderRefund = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderRefund query = new ErpOrderRefund();
            query.setPayId(payId);
            List<ErpOrderRefund> select = erpOrderRefundDao.select(query);
            if (select != null && select.size() > 0) {
                orderRefund = select.get(0);
            }
        }
        return orderRefund;
    }

    @Override
    public ErpOrderRefund getOrderRefundByRefundCode(String refundCode) {
        ErpOrderRefund orderRefund = null;
        if (StringUtils.isNotEmpty(refundCode)) {
            ErpOrderRefund query = new ErpOrderRefund();
            query.setRefundCode(refundCode);
            List<ErpOrderRefund> select = erpOrderRefundDao.select(query);
            if (select != null && select.size() > 0) {
                orderRefund = select.get(0);
            }
        }
        return orderRefund;
    }

    @Override
    public ErpOrderRefund getOrderRefundByOrderIdAndRefundType(String orderId, ErpOrderRefundTypeEnum refundTypeEnum) {
        ErpOrderRefund orderRefund = null;
        if (StringUtils.isNotEmpty(orderId) && refundTypeEnum != null) {
            ErpOrderRefund query = new ErpOrderRefund();
            query.setOrderId(orderId);
            query.setRefundType(refundTypeEnum.getCode());
            List<ErpOrderRefund> select = erpOrderRefundDao.select(query);
            if (select != null && select.size() > 0) {
                orderRefund = select.get(0);
            }
        }
        return orderRefund;
    }

    @Override
    public void saveOrderRefund(ErpOrderRefund po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderRefundDao.insert(po);
    }

    @Override
    public void updateOrderRefundSelective(ErpOrderRefund po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer update = erpOrderRefundDao.updateByPrimaryKeySelective(po);
    }

    @Override
    public void generateOrderRefund(String orderCode, AuthToken auth) {
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_34.getCode().equals(order.getOrderNodeStatus())) {
            String refundCode = "";
            try {
                HttpResponse response = returnOrderInfoService.saveCancelOrder(order.getOrderStoreCode());
                refundCode = (String) response.getData();
            } catch (Exception e) {
                throw new BusinessException("生成退款单号失败");
            }

            ErpOrderRefund orderRefund = new ErpOrderRefund();
            orderRefund.setOrderId(order.getOrderStoreId());
            orderRefund.setRefundId(OrderPublic.getUUID());
            orderRefund.setRefundCode(refundCode);
            orderRefund.setRefundFee(order.getOrderAmount());
            orderRefund.setRefundType(ErpOrderRefundTypeEnum.ORDER_CANCEL.getCode());
            orderRefund.setRefundStatus(ErpPayStatusEnum.UNPAID.getCode());
            orderRefund.setPayId(null);
            this.saveOrderRefund(orderRefund, auth);

            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_35.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }

    @Override
    public void orderRefundPay(String orderCode, ErpRequestPayTransactionTypeEnum payTransactionTypeEnum, AuthToken auth) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_35.getCode().equals(order.getOrderNodeStatus())) {

            ErpOrderRefund orderRefund = this.getOrderRefundByOrderIdAndRefundType(order.getOrderStoreId(), ErpOrderRefundTypeEnum.ORDER_CANCEL);

            //调用支付中心接口发起退款
            boolean flag = erpOrderRequestService.sendOrderRefundRequest(order, orderRefund, payTransactionTypeEnum, auth);

            if (flag) {
                String payId = OrderPublic.getUUID();

                //生成支付单
                ErpOrderPay logisticsPay = new ErpOrderPay();
                logisticsPay.setPayId(payId);
                logisticsPay.setPayCode(null);
                logisticsPay.setPayFee(order.getOrderAmount());
                logisticsPay.setBusinessKey(orderRefund.getRefundCode());
                logisticsPay.setFeeType(ErpPayFeeTypeEnum.REFUND_FEE.getCode());
                logisticsPay.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
                logisticsPay.setPayStartTime(new Date());
                logisticsPay.setPayWay(null);
                erpOrderPayService.saveOrderPay(logisticsPay, auth);

                orderRefund.setRefundStatus(ErpPayStatusEnum.PAYING.getCode());
                orderRefund.setPayId(payId);
                this.updateOrderRefundSelective(orderRefund, auth);

                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_36.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);

            } else {
                throw new BusinessException("发起退款申请失败");
            }
        }


    }

    @Override
    public void endOrderRefund(String refundCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth) {

        //查询退款信息
        ErpOrderRefund orderRefund = this.getOrderRefundByRefundCode(refundCode);
        if (orderRefund != null && ErpPayStatusEnum.PAYING.getCode().equals(orderRefund.getRefundStatus())) {

            //更新支付单
            ErpOrderPay logisticsPay = erpOrderPayService.getOrderPayByPayId(orderRefund.getPayId());
            logisticsPay.setPayEndTime(new Date());
            logisticsPay.setPayCode(payCode);
            logisticsPay.setPayStatus(payStatusEnum.getCode());
            erpOrderPayService.updateOrderPaySelective(logisticsPay, auth);

            //更新退款单
            orderRefund.setRefundStatus(payStatusEnum.getCode());
            this.updateOrderRefundSelective(orderRefund, auth);

            //更新订单
            ErpOrderInfo order = erpOrderQueryService.getOrderByOrderId(orderRefund.getOrderId());
            order.setOrderNodeStatus(ErpPayStatusEnum.SUCCESS == payStatusEnum ? ErpOrderNodeStatusEnum.STATUS_37.getCode() : ErpOrderNodeStatusEnum.STATUS_38.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);

        }

    }


}
