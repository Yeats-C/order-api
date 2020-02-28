package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayResultResponse;
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
public class ErpOrderPayNoTransactionalServiceImpl implements ErpOrderPayNoTransactionalService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayNoTransactionalServiceImpl.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderPayService erpOrderPayService;

    @Override
    public void orderPayStartMethodGroup(ErpOrderPayRequest erpOrderPayRequest, AuthToken auth, boolean autoCheck) {

        //订单支付
        erpOrderPayService.orderPay(erpOrderPayRequest, auth, autoCheck);

        //开启轮询
        this.orderPayPolling(erpOrderPayRequest.getOrderCode(), auth);

    }

    @Override
    public void orderPaySuccessMethodGroup(String orderCode, AuthToken auth) {

        //获取物流券
        erpOrderPayService.getGoodsCoupon(orderCode, auth);

        //拆单
        erpOrderInfoService.orderSplit(orderCode, auth);

        //同步
        erpOrderInfoService.orderSendToSupply(orderCode, auth);

    }

    @Override
    public ErpOrderPayResultResponse orderPayResult(ErpOrderPayRequest erpOrderPayRequest) {
        ErpOrderPayResultResponse result = new ErpOrderPayResultResponse();
        if (erpOrderPayRequest == null || StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("缺失订单号");
        }
        String orderCode = erpOrderPayRequest.getOrderCode();
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());

        result.setOrderInfo(order);
        result.setOrderCode(order.getOrderStoreCode());
        result.setOrderId(order.getOrderStoreId());
        result.setReceivePerson(order.getReceivePerson());
        result.setReceiveAddress(order.getReceiveMobile());
        result.setReceiveAddress(order.getReceiveAddress());
        result.setGoodsCoupon(orderFee.getGoodsCoupon());
        result.setPayStatus(orderFee.getPayStatus());
        result.setPayMoney(orderFee.getPayMoney());
        if (ErpPayStatusEnum.SUCCESS.getCode().equals(orderFee.getPayStatus())) {
            ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderFee.getPayId());
            if (orderPay != null) {
                result.setPayCode(orderPay.getPayCode());
                result.setPayStartTime(orderPay.getPayStartTime());
                result.setPayEndTime(orderPay.getPayEndTime());
            }
        }
        if (ErpOrderStatusEnum.ORDER_STATUS_2.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_3.getCode().equals(order.getOrderNodeStatus())) {
                //如果是支付成功，但是还没获取到物流券时，就给前端返回支付中
                result.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
            }
        }
        return result;
    }

    @Override
    public void orderPayPolling(String orderCode, AuthToken auth) {

        ErpOrderInfo orderInfo = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (orderInfo == null) {
            throw new BusinessException("无效的订单号");
        }

        //是否需要发起轮询
        boolean pollingFlag = false;
        if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(orderInfo.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_2.getCode().equals(orderInfo.getOrderNodeStatus())) {
                pollingFlag = true;
            }
        }

        if (pollingFlag) {
            logger.info("开始支付轮询：{}", orderCode);

            //轮询请求查询支付信息
            ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
            service.scheduleAtFixedRate(new Runnable() {

                int pollingTimes = 0;

                @Override
                public void run() {
                    pollingTimes++;
                    logger.info("第{}次轮询支付单：{}", pollingTimes, orderCode);

                    ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
                    ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
                    if (!ErpPayStatusEnum.PAYING.getCode().equals(orderFee.getPayStatus())) {
                        logger.info("结束支付轮询：{}", orderCode);
                        service.shutdown();
                    } else if (pollingTimes > OrderConstant.MAX_PAY_POLLING_TIMES) {
                        service.shutdown();
                        logger.info("结束支付轮询：{}", orderCode);
                    } else {
                        //调用支付中心，查看结果
                        ErpOrderPayStatusResponse payStatusResponse = erpOrderRequestService.getOrderPayStatus(orderCode);

                        if (payStatusResponse.isRequestSuccess()) {
                            ErpPayStatusEnum payStatusEnum = payStatusResponse.getPayStatusEnum();
                            if (payStatusEnum == ErpPayStatusEnum.SUCCESS || payStatusEnum == ErpPayStatusEnum.FAIL) {
                                erpOrderPayService.endOrderPay(orderCode, payStatusResponse.getPayCode(), payStatusEnum, auth, false);
                                //支付成功后续操作
                                orderPaySuccessMethodGroup(orderCode, auth);
                                logger.info("结束支付轮询：{}", orderCode);
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
    public void orderPayCallback(PayCallbackRequest payCallbackRequest) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(payCallbackRequest.getOrderNo());
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        if (ErpPayStatusEnum.PAYING.getCode().equals(orderFee.getPayStatus())) {
            ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderFee.getPayId());

            AuthToken auth = new AuthToken();
            auth.setPersonId(orderPay.getCreateById());
            auth.setPersonName(orderPay.getCreateByName());

            if (StatusEnum.YES.getValue().equals(payCallbackRequest.getReturnCode())) {
                if (StringUtils.isEmpty(payCallbackRequest.getPayNum())) {
                    throw new BusinessException("缺失支付流水号");
                }
                erpOrderPayService.endOrderPay(order.getOrderStoreCode(), payCallbackRequest.getPayNum(), ErpPayStatusEnum.SUCCESS, auth, false);
                //支付成功后续操作
                this.orderPaySuccessMethodGroup(order.getOrderStoreCode(), auth);
            } else {
                erpOrderPayService.endOrderPay(order.getOrderStoreCode(), null, ErpPayStatusEnum.FAIL, auth, false);
            }
        }
    }

}
