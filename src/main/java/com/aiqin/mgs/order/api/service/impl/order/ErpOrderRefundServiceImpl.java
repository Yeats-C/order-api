package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderRefundTypeEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayFeeTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderRefundDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderRefund;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
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
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    public void orderRefundCallback(PayCallbackRequest payCallbackRequest) {
        ErpOrderRefund orderRefund = this.getOrderRefundByRefundCode(payCallbackRequest.getOrderNo());
        if (orderRefund == null) {
            throw new BusinessException("无效的退款单号");
        }
        if (ErpPayStatusEnum.PAYING.getCode().equals(orderRefund.getRefundStatus())) {
            ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderRefund.getPayId());

            AuthToken auth = new AuthToken();
            auth.setPersonId(orderPay.getCreateById());
            auth.setPersonName(orderPay.getCreateByName());

            if (StatusEnum.YES.getValue().equals(payCallbackRequest.getReturnCode())) {
                if (StringUtils.isEmpty(payCallbackRequest.getPayNum())) {
                    throw new BusinessException("缺失支付流水号");
                }
                this.endOrderRefund(orderRefund.getRefundCode(), payCallbackRequest.getPayNum(), ErpPayStatusEnum.SUCCESS, auth);
            } else {
                this.endOrderRefund(orderRefund.getRefundCode(), null, ErpPayStatusEnum.FAIL, auth);
            }
        }
    }

    @Override
    public void orderRefundPolling(String orderCode, AuthToken auth) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_36.getCode().equals(order.getOrderNodeStatus())) {
            ErpOrderRefund refund = this.getOrderRefundByOrderIdAndRefundType(order.getOrderStoreId(), ErpOrderRefundTypeEnum.ORDER_CANCEL);
            String refundCode = refund.getRefundCode();

            //轮询请求查询支付信息
            logger.info("开始订单退款结果轮询：{}", refundCode);
            ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
            service.scheduleAtFixedRate(new Runnable() {

                int pollingTimes = 0;

                @Override
                public void run() {
                    pollingTimes++;
                    logger.info("第{}次订单退款结果轮询：{}", pollingTimes, refundCode);

                    ErpOrderRefund orderRefund = getOrderRefundByRefundCode(refundCode);

                    if (!ErpPayStatusEnum.PAYING.getCode().equals(orderRefund.getRefundStatus())) {
                        logger.info("第{}次订单退款结果轮询：{}", pollingTimes, refundCode);
                        service.shutdown();
                    } else if (pollingTimes > OrderConstant.MAX_PAY_POLLING_TIMES) {
                        //轮询次数超时
                        logger.info("第{}次订单退款结果轮询：{}", pollingTimes, refundCode);
                        service.shutdown();
                    } else {
                        //调用支付中心，查看结果
                        ErpOrderPayStatusResponse payStatusResponse = erpOrderRequestService.getOrderRefundStatus(refundCode);
                        if (payStatusResponse.isRequestSuccess()) {
                            ErpPayStatusEnum payStatusEnum = payStatusResponse.getPayStatusEnum();
                            if (payStatusEnum == ErpPayStatusEnum.SUCCESS || payStatusEnum == ErpPayStatusEnum.FAIL) {
                                endOrderRefund(refundCode, payStatusResponse.getPayCode(), payStatusEnum, auth);
                                logger.info("结束订单退款结果轮询：{}", refundCode);
                                service.shutdown();
                            }
                        }
                    }
                }
                //轮询时间控制
            }, OrderConstant.MAX_PAY_POLLING_INITIALDELAY, OrderConstant.MAX_PAY_POLLING_PERIOD, TimeUnit.MILLISECONDS);
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
