package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderRefundTypeEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderRefund;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.service.order.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ErpOrderRefundNoTransactionalServiceImpl implements ErpOrderRefundNoTransactionalService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderRefundNoTransactionalServiceImpl.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderRefundService erpOrderRefundService;

    @Override
    public void orderRefundCallback(PayCallbackRequest payCallbackRequest) {
        ErpOrderRefund orderRefund = erpOrderRefundService.getOrderRefundByRefundCode(payCallbackRequest.getOrderNo());
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
                erpOrderRefundService.endOrderRefund(orderRefund.getRefundCode(), payCallbackRequest.getPayNum(), ErpPayStatusEnum.SUCCESS, auth);
            } else {
                erpOrderRefundService.endOrderRefund(orderRefund.getRefundCode(), null, ErpPayStatusEnum.FAIL, auth);
            }
        }
    }

    @Override
    public void orderRefundPolling(String orderCode, AuthToken auth) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (ErpOrderNodeStatusEnum.STATUS_36.getCode().equals(order.getOrderNodeStatus())) {
            ErpOrderRefund refund = erpOrderRefundService.getOrderRefundByOrderIdAndRefundType(order.getOrderStoreId(), ErpOrderRefundTypeEnum.ORDER_CANCEL);
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

                    ErpOrderRefund orderRefund = erpOrderRefundService.getOrderRefundByRefundCode(refundCode);

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
                                erpOrderRefundService.endOrderRefund(refundCode, payStatusResponse.getPayCode(), payStatusEnum, auth);
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

}
