package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayFeeTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.LogisticsCouponDetail;
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
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ErpOrderLogisticsServiceImpl implements ErpOrderLogisticsService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderLogisticsServiceImpl.class);

    @Resource
    private ErpOrderLogisticsDao erpOrderLogisticsDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderDeliverService erpOrderDeliverService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderLogistics(ErpOrderLogistics po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderLogisticsDao.insert(po);
    }

    @Override
    public ErpOrderLogistics getOrderLogisticsByPayId(String payId) {
        ErpOrderLogistics orderLogistics = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderLogistics query = new ErpOrderLogistics();
            query.setPayId(payId);
            List<ErpOrderLogistics> select = erpOrderLogisticsDao.select(query);
            if (select != null && select.size() > 0) {
                orderLogistics = select.get(0);
            }
        }
        return orderLogistics;
    }

    @Override
    public ErpOrderLogistics getOrderLogisticsByLogisticsId(String logisticsId) {
        ErpOrderLogistics orderLogistics = null;
        if (StringUtils.isNotEmpty(logisticsId)) {
            ErpOrderLogistics query = new ErpOrderLogistics();
            query.setLogisticsId(logisticsId);
            List<ErpOrderLogistics> select = erpOrderLogisticsDao.select(query);
            if (select != null && select.size() > 0) {
                orderLogistics = select.get(0);
            }
        }
        return orderLogistics;
    }

    @Override
    public ErpOrderLogistics getOrderLogisticsByLogisticsCode(String logisticsCode) {
        ErpOrderLogistics orderLogistics = null;
        if (StringUtils.isNotEmpty(logisticsCode)) {
            ErpOrderLogistics query = new ErpOrderLogistics();
            query.setLogisticsCode(logisticsCode);
            List<ErpOrderLogistics> select = erpOrderLogisticsDao.select(query);
            if (select != null && select.size() > 0) {
                orderLogistics = select.get(0);
            }
        }
        return orderLogistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderLogisticsSelective(ErpOrderLogistics po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderLogisticsDao.updateByPrimaryKeySelective(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderLogistics(ErpOrderLogistics po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderLogisticsDao.updateByPrimaryKey(po);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderLogisticsPay(ErpOrderPayRequest erpOrderPayRequest) {
        if (erpOrderPayRequest == null || StringUtils.isEmpty(erpOrderPayRequest.getPersonId())) {
            throw new BusinessException("缺失用户id");
        }
        if (StringUtils.isEmpty(erpOrderPayRequest.getPersonName())) {
            throw new BusinessException("缺失用户名称");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderPayRequest.getPersonId());
        auth.setPersonName(erpOrderPayRequest.getPersonName());
        if (StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("缺失订单号");
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编号");
        }
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
        if (!processTypeEnum.isHasLogisticsFee()) {
            throw new BusinessException("该类型的订单不需要支付物流费用");
        }

        if (!ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("不是" + ErpOrderStatusEnum.ORDER_STATUS_11.getDesc() + "的订单不能支付物流费用");
        }
        ErpOrderLogistics orderLogistics = this.getOrderLogisticsByLogisticsId(order.getLogisticsId());
        if (orderLogistics == null) {
            throw new BusinessException("订单物流数据异常");
        }
        if (ErpPayStatusEnum.PAYING.getCode().equals(orderLogistics.getPayStatus())) {
            throw new BusinessException("物流单正在支付中");
        }
        if (ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
            throw new BusinessException("物流单已经支付成功，不能重复支付");
        }

        List<String> couponIdList = erpOrderPayRequest.getCouponIds();
        String couponIds = null;

        //物流券抵扣金额
        BigDecimal couponPayFee = BigDecimal.ZERO;
        if (couponIdList != null && couponIdList.size() > 0) {
            couponIds = String.join(",", couponIdList);
            for (String couponIdItem :
                    couponIdList) {
                //调用物流券接口获取物流券信息
                LogisticsCouponDetail logisticsCoupon = erpOrderRequestService.getLogisticsCouponByCode(couponIdItem);
                if (logisticsCoupon == null) {
                    throw new BusinessException("无效的物流券编码");
                }
                if (logisticsCoupon.getActiveCondition() == 1) {
                    throw new BusinessException("优惠券已经被使用");
                }
                if (logisticsCoupon.getActiveCondition() == 2) {
                    throw new BusinessException("优惠券已经过期");
                }
                couponPayFee = couponPayFee.add(logisticsCoupon.getNominalValue() == null ? BigDecimal.ZERO : logisticsCoupon.getNominalValue());
            }
        }

        //需要余额支付的金额
        BigDecimal balancePayFee = BigDecimal.ZERO;
        if (couponPayFee.compareTo(orderLogistics.getLogisticsFee()) > 0) {
            couponPayFee = orderLogistics.getLogisticsFee();
        } else {
            balancePayFee = orderLogistics.getLogisticsFee().subtract(couponPayFee);
        }

        orderLogistics.setCouponPayFee(couponPayFee);
        orderLogistics.setBalancePayFee(balancePayFee);
        //物流单关联的订单
        List<ErpOrderInfo> logisticsOrderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());

        //调用支付中心接口发起支付
        boolean flag = erpOrderRequestService.sendLogisticsPayRequest(order, logisticsOrderList, orderLogistics, auth);
        if (flag) {
            String payId = OrderPublic.getUUID();

            //生成支付单
            ErpOrderPay logisticsPay = new ErpOrderPay();
            logisticsPay.setPayId(payId);
            logisticsPay.setPayCode(null);
            logisticsPay.setPayFee(balancePayFee);
            logisticsPay.setBusinessKey(orderLogistics.getLogisticsCode());
            logisticsPay.setFeeType(ErpPayFeeTypeEnum.LOGISTICS_FEE.getCode());
            logisticsPay.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
            logisticsPay.setPayStartTime(new Date());
            logisticsPay.setPayWay(erpOrderPayRequest.getPayWay());
            erpOrderPayService.saveOrderPay(logisticsPay, auth);

            orderLogistics.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
            orderLogistics.setCouponIds(couponIds);
            orderLogistics.setPayId(payId);
            this.updateOrderLogisticsSelective(orderLogistics, auth);

            //开启轮询
            this.orderLogisticsPayPolling(orderLogistics.getLogisticsCode(), auth);
        }

    }

    @Override
    public void orderLogisticsPayCallback(PayCallbackRequest payCallbackRequest) {

        ErpOrderLogistics orderLogistics = this.getOrderLogisticsByLogisticsCode(payCallbackRequest.getOrderNo());
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
                this.endOrderLogisticsPay(orderLogistics.getLogisticsCode(), payCallbackRequest.getPayNum(), ErpPayStatusEnum.SUCCESS, auth);
            } else {
                this.endOrderLogisticsPay(orderLogistics.getLogisticsCode(), null, ErpPayStatusEnum.FAIL, auth);
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

        ErpOrderLogistics orderLogistics = this.getOrderLogisticsByLogisticsId(order.getLogisticsId());
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
        ErpOrderLogistics orderLogistics = this.getOrderLogisticsByLogisticsId(order.getLogisticsId());
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

                ErpOrderLogistics orderLogistics = getOrderLogisticsByLogisticsCode(logisticsCode);

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
                            endOrderLogisticsPay(logisticsCode, payStatusResponse.getPayCode(), payStatusEnum, auth);
                            logger.info("结束物流单支付轮询：{}", logisticsCode);
                            service.shutdown();
                        }
                    }
                }

            }
            //轮询时间控制
        }, OrderConstant.MAX_PAY_POLLING_INITIALDELAY, OrderConstant.MAX_PAY_POLLING_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void endOrderLogisticsPay(String logisticsCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth) {

        //查询物流费用
        ErpOrderLogistics orderLogistics = this.getOrderLogisticsByLogisticsCode(logisticsCode);
        if (orderLogistics == null) {
            throw new BusinessException("数据异常");
        }

        //更新物流单关联支付单
        ErpOrderPay logisticsPay = erpOrderPayService.getOrderPayByPayId(orderLogistics.getPayId());
        logisticsPay.setPayEndTime(new Date());
        logisticsPay.setPayStatus(payStatusEnum.getCode());
        erpOrderPayService.updateOrderPaySelective(logisticsPay, auth);

        if (payStatusEnum == ErpPayStatusEnum.SUCCESS) {
            //物流费用支付成功

            orderLogistics.setPayStatus(payStatusEnum.getCode());
            this.updateOrderLogisticsSelective(orderLogistics, auth);

            //订单修改为已支付物流费用
            List<ErpOrderInfo> orderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());
            if (orderList != null && orderList.size() > 0) {
                for (ErpOrderInfo item :
                        orderList) {
                    if (ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(item.getOrderStatus())) {
                        if (ErpOrderNodeStatusEnum.STATUS_10.getCode().equals(item.getOrderNodeStatus())) {
                            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(item.getOrderTypeCode(), item.getOrderCategoryCode());
                            if (processTypeEnum != null && processTypeEnum.isHasLogisticsFee()) {
                                item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                                item.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                                erpOrderInfoService.updateOrderByPrimaryKeySelective(item, auth);
                            }
                        }
                    }
                }
                //均摊物流费用
                erpOrderDeliverService.distributeLogisticsFee(orderLogistics.getLogisticsCode());
                String couponIds = orderLogistics.getCouponIds();
                if (StringUtils.isNotEmpty(couponIds)) {
                    String[] split = couponIds.split(",");
                    ErpOrderInfo order = orderList.get(0);
                    for (String couponCode :
                            split) {
                        //优惠券消券变成已使用
                        erpOrderRequestService.updateCouponStatus(order.getFranchiseeId(), couponCode, orderLogistics.getLogisticsCode(), order.getStoreName(), payCode, null);
                    }
                }
            }
        } else if (payStatusEnum == ErpPayStatusEnum.FAIL) {
            //物流费用支付失败

            orderLogistics.setPayStatus(payStatusEnum.getCode());
            orderLogistics.setCouponPayFee(null);
            orderLogistics.setBalancePayFee(null);
            orderLogistics.setCouponIds(null);
            this.updateOrderLogisticsSelective(orderLogistics, auth);
        } else {

        }
    }

}
