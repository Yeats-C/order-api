package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.PayCallbackRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPrintQueryResponse;
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
public class ErpOrderLogisticsNoTransactionalServiceImpl implements ErpOrderLogisticsNoTransactionalService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderLogisticsNoTransactionalServiceImpl.class);

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;

    @Override
    public void orderLogisticsPayCallback(PayCallbackRequest payCallbackRequest) {

        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(payCallbackRequest.getOrderNo());
        if (orderLogistics == null) {
            throw new BusinessException("无效的物流单号");
        }
        if (ErpPayStatusEnum.PAYING.getCode().equals(orderLogistics.getPayStatus())) {
            ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderLogistics.getPayId());

            AuthToken auth = new AuthToken();
            auth.setPersonId(orderPay.getCreateById());
            auth.setPersonName(orderPay.getCreateByName());

            if (StatusEnum.YES.getValue().equals(payCallbackRequest.getReturnCode())) {
                if (StringUtils.isEmpty(payCallbackRequest.getPayNum())) {
                    throw new BusinessException("缺失支付流水号");
                }
                erpOrderLogisticsService.endOrderLogisticsPay(orderLogistics.getLogisticsCode(), payCallbackRequest.getPayNum(), ErpPayStatusEnum.SUCCESS, auth);
            } else {
                erpOrderLogisticsService.endOrderLogisticsPay(orderLogistics.getLogisticsCode(), null, ErpPayStatusEnum.FAIL, auth);
            }
        }
    }

    @Override
    public ErpOrderLogisticsPayResultResponse orderLogisticsPayResult(ErpOrderPayRequest erpOrderPayRequest) {
        if (erpOrderPayRequest == null || StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("缺失订单号");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }

        ErpOrderLogisticsPayResultResponse response = new ErpOrderLogisticsPayResultResponse();
        response.setOrderCode(order.getOrderStoreCode());
        response.setFranchiseeId(order.getFranchiseeId());
        response.setFranchiseeCode(order.getFranchiseeCode());
        response.setFranchiseeName(order.getFranchiseeName());

        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
        if (orderLogistics != null) {
            response.setPayStatus(orderLogistics.getPayStatus());
            response.setLogisticsCode(orderLogistics.getLogisticsCode());
            response.setLogisticsCentreCode(orderLogistics.getLogisticsCentreCode());
            response.setLogisticsCentreName(orderLogistics.getLogisticsCentreName());
            response.setSendRepertoryCode(orderLogistics.getSendRepertoryCode());
            response.setSendRepertoryName(orderLogistics.getSendRepertoryName());
            response.setLogisticsFee(orderLogistics.getLogisticsFee());

            if (ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
                ErpOrderPay orderPay = erpOrderPayService.getOrderPayByPayId(orderLogistics.getPayId());
                if (orderPay != null) {
                    response.setPayId(orderPay.getPayId());
                    response.setPayCode(orderPay.getPayCode());
                    response.setPayStartTime(orderPay.getPayStartTime());
                    response.setPayEndTime(orderPay.getPayEndTime());
                }
            }
        }
        return response;
    }

    @Override
    public ErpOrderLogisticsPrintQueryResponse orderLogisticsPrintQuery(ErpOrderPayRequest erpOrderPayRequest) {
        if (erpOrderPayRequest == null || StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("缺失订单号");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
        if (orderLogistics == null) {
            throw new BusinessException("未找到物流单");
        }
        ErpOrderPay logisticsPay = erpOrderPayService.getOrderPayByPayId(orderLogistics.getPayId());
        if (logisticsPay == null) {
            throw new BusinessException("未找到物流单支付信息");
        }
        if (!ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
            throw new BusinessException("物流费用未支付成功");
        }
        ErpOrderLogisticsPrintQueryResponse response = new ErpOrderLogisticsPrintQueryResponse();
        response.setStoreName(order.getStoreName());
        response.setOrderCode(order.getOrderStoreCode());
        response.setLogisticCentreCode(orderLogistics.getLogisticsCentreCode());
        response.setLogisticCentreName(orderLogistics.getLogisticsCentreName());
        response.setLogisticCode(orderLogistics.getLogisticsCode());
        response.setLogisticFee(orderLogistics.getLogisticsFee());
        response.setCouponPayFee(orderLogistics.getCouponPayFee());
        response.setBalancePayFee(orderLogistics.getBalancePayFee());
        response.setPayCode(logisticsPay.getPayCode());
        response.setPayEndTime(logisticsPay.getPayEndTime());
        response.setPayUser(logisticsPay.getCreateByName());
        return response;
    }

    @Override
    public void orderLogisticsPayPolling(String logisticsCode, AuthToken auth) {

        logger.info("开始物流单支付轮询：{}", logisticsCode);

        //轮询请求查询支付信息
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(new Runnable() {

            int pollingTimes = 0;

            @Override
            public void run() {
                pollingTimes++;
                logger.info("第{}次轮询物流单：{}", pollingTimes, logisticsCode);

                ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logisticsCode);

                if (!ErpPayStatusEnum.PAYING.getCode().equals(orderLogistics.getPayStatus())) {
                    logger.info("结束物流单支付轮询：{}", logisticsCode);
                    service.shutdown();
                } else if (pollingTimes > OrderConstant.MAX_PAY_POLLING_TIMES) {
                    //轮询次数超时
                    logger.info("结束物流单支付轮询：{}", logisticsCode);
                    service.shutdown();
                } else {
                    //调用支付中心，查看结果
                    ErpOrderPayStatusResponse payStatusResponse = erpOrderRequestService.getOrderLogisticsPayStatus(logisticsCode);
                    if (payStatusResponse.isRequestSuccess()) {
                        ErpPayStatusEnum payStatusEnum = payStatusResponse.getPayStatusEnum();
                        if (payStatusEnum == ErpPayStatusEnum.SUCCESS || payStatusEnum == ErpPayStatusEnum.FAIL) {
                            erpOrderLogisticsService.endOrderLogisticsPay(logisticsCode, payStatusResponse.getPayCode(), payStatusEnum, auth);
                            logger.info("结束物流单支付轮询：{}", logisticsCode);
                            service.shutdown();
                        }
                    }
                }

            }
            //轮询时间控制
        }, OrderConstant.MAX_PAY_POLLING_INITIALDELAY, OrderConstant.MAX_PAY_POLLING_PERIOD, TimeUnit.MILLISECONDS);
    }

}
