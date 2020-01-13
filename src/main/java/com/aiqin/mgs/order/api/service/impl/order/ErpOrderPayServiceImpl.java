package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayFeeTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayWayEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderPayDao;
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
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class ErpOrderPayServiceImpl implements ErpOrderPayService {

    private static final Logger logger = LoggerFactory.getLogger(ErpOrderPayServiceImpl.class);

    @Resource
    private ErpOrderPayDao erpOrderPayDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;

    @Override
    public ErpOrderPay getOrderPayByPayId(String payId) {
        ErpOrderPay orderPay = null;
        if (StringUtils.isNotEmpty(payId)) {
            ErpOrderPay query = new ErpOrderPay();
            query.setPayId(payId);
            List<ErpOrderPay> select = erpOrderPayDao.select(query);
            if (select != null && select.size() > 0) {
                orderPay = select.get(0);
            }
        }
        return orderPay;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrderPay(ErpOrderPay po, AuthToken auth) {
        po.setCreateById(auth.getPersonId());
        po.setCreateByName(auth.getPersonName());
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer insert = erpOrderPayDao.insert(po);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrderPaySelective(ErpOrderPay po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderPayDao.updateByPrimaryKeySelective(po);
    }

    @Override
    public void orderPayStartMethodGroup(ErpOrderPayRequest erpOrderPayRequest, AuthToken auth, boolean autoCheck) {

        //订单支付
        this.orderPay(erpOrderPayRequest, auth, autoCheck);

        //开启轮询
        this.orderPayPolling(erpOrderPayRequest.getOrderCode(), auth);

    }

    @Override
    public void orderPaySuccessMethodGroup(String orderCode, AuthToken auth) {

        //获取物流券
        this.getGoodsCoupon(orderCode, auth);

        //拆单
        erpOrderInfoService.orderSplit(orderCode, auth);

        //同步
        erpOrderInfoService.orderSendToSupply(orderCode, auth);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPay(ErpOrderPayRequest erpOrderPayRequest, AuthToken auth, boolean autoCheck) {

        if (erpOrderPayRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("缺少订单编码");
        }
        if (erpOrderPayRequest.getPayWay() == null) {
            throw new BusinessException("缺少支付方式");
        } else {
            if (!ErpPayWayEnum.exist(erpOrderPayRequest.getPayWay())) {
                throw new BusinessException("无效的支付方式");
            }
        }
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());


        //是否要做发起支付操作
        boolean payFlag = false;

        //只有待支付状态或者支付失败的订单才需要订单支付
        if (ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_1.getCode().equals(order.getOrderNodeStatus()) || ErpOrderNodeStatusEnum.STATUS_4.getCode().equals(order.getOrderNodeStatus())) {
                ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
                if (autoCheck) {
                    if (processTypeEnum != null && processTypeEnum.isAutoPay()) {
                        payFlag = true;
                    }
                } else {
                    payFlag = true;
                }
            }
        }

        if (payFlag) {
            //调用支付中心支付发起支付
            boolean payRequestSuccess = erpOrderRequestService.sendOrderPayRequest(order, orderFee);
            if (payRequestSuccess) {

                //支付单id
                String payId = OrderPublic.getUUID();

                //生成支付单
                ErpOrderPay orderPay = new ErpOrderPay();
                orderPay.setPayId(payId);
                orderPay.setPayCode(null);
                orderPay.setPayFee(orderFee.getPayMoney().setScale(2, RoundingMode.DOWN));
                orderPay.setBusinessKey(order.getOrderStoreCode());
                orderPay.setFeeType(ErpPayFeeTypeEnum.ORDER_FEE.getCode());
                orderPay.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
                orderPay.setPayStartTime(new Date());
                orderPay.setPayWay(erpOrderPayRequest.getPayWay());
                this.saveOrderPay(orderPay, auth);

                //修改费用单
                orderFee.setPayId(payId);
                orderFee.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
                erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);

                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_2.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);

            } else {
                //发起支付失败
                throw new BusinessException("发起订单支付失败");
            }
        }

    }

    @Override
    public ErpOrderPayResultResponse orderPayResult(ErpOrderPayRequest erpOrderPayRequest) {
        ErpOrderPayResultResponse result = new ErpOrderPayResultResponse();
        String orderCode = erpOrderPayRequest.getOrderCode();
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单编码");
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
            ErpOrderPay orderPay = this.getOrderPayByPayId(orderFee.getPayId());
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
                                endOrderPay(orderCode, payStatusResponse.getPayCode(), payStatusEnum, auth, false);
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
    @Transactional(rollbackFor = Exception.class)
    public void endOrderPay(String orderCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth, boolean manual) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        ErpOrderPay orderPay = this.getOrderPayByPayId(orderFee.getPayId());

        if (payStatusEnum == ErpPayStatusEnum.SUCCESS) {
            //支付成功
            orderFee.setGoodsCoupon(BigDecimal.ZERO);
            orderFee.setPayStatus(payStatusEnum.getCode());
            erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);

            orderPay.setPayStatus(payStatusEnum.getCode());
            orderPay.setPayEndTime(new Date());
            orderPay.setPayCode(payCode);
            this.updateOrderPaySelective(orderPay, auth);

            order.setPaymentStatus(StatusEnum.YES.getCode());
            order.setPaymentTime(orderPay.getPayEndTime());
            order.setPaymentCode(orderPay.getPayWay().toString());
            order.setPaymentName(ErpPayWayEnum.getEnumDesc(orderPay.getPayWay()));
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_3.getCode());

            if (manual) {
                order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_92.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
            }
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_2.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

            String topCouponCodes = orderFee.getTopCouponCodes();
            if (StringUtils.isNotEmpty(topCouponCodes)) {
                String[] topCouponCodeArray = topCouponCodes.split(",");
                for (String topCouponCode :
                        topCouponCodeArray) {
                    erpOrderRequestService.updateCouponStatus(order.getFranchiseeId(), topCouponCode, order.getOrderStoreCode(), order.getStoreName());
                }
            }

        } else if (payStatusEnum == ErpPayStatusEnum.FAIL) {
            //支付失败

            //修改支付单状态
            orderPay.setPayStatus(payStatusEnum.getCode());
            orderPay.setPayEndTime(new Date());
            this.updateOrderPaySelective(orderPay, auth);

            //修改费用单信息
            orderFee.setPayStatus(payStatusEnum.getCode());
            erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);

            //修改订单节点状态
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_4.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);

        } else {

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void getGoodsCoupon(String orderCode, AuthToken auth) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        //是否要做获取物流券操作
        boolean flag = false;
        if (ErpOrderStatusEnum.ORDER_STATUS_2.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_3.getCode().equals(order.getOrderNodeStatus())) {
                flag = true;
            }
        }

        if (flag) {
            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            if (processTypeEnum != null && processTypeEnum.isHasGoodsCoupon()) {
                //获取物流券

                order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));
                BigDecimal goodsCoupon = erpOrderRequestService.getGoodsCoupon(order);

                ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
                orderFee.setGoodsCoupon(goodsCoupon);
                erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);
            }
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_5.getCode());
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_3.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
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
            ErpOrderPay orderPay = this.getOrderPayByPayId(orderFee.getPayId());

            AuthToken auth = new AuthToken();
            auth.setPersonId(orderPay.getCreateById());
            auth.setPersonName(orderPay.getCreateByName());

            if (StatusEnum.YES.getValue().equals(payCallbackRequest.getReturnCode())) {
                if (StringUtils.isEmpty(payCallbackRequest.getPayNum())) {
                    throw new BusinessException("缺失支付流水号");
                }
                endOrderPay(order.getOrderStoreCode(), payCallbackRequest.getPayNum(), ErpPayStatusEnum.SUCCESS, auth, false);
                //支付成功后续操作
                this.orderPaySuccessMethodGroup(order.getOrderStoreCode(), auth);
            } else {
                endOrderPay(order.getOrderStoreCode(), null, ErpPayStatusEnum.FAIL, auth, false);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTimeoutUnpaid(String orderCode) {

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);

        AuthToken auth = new AuthToken();
        auth.setPersonId(order.getCreateById());
        auth.setPersonName(order.getCreateByName());

        if (ErpOrderNodeStatusEnum.STATUS_1.getCode().equals(order.getOrderNodeStatus()) || ErpOrderNodeStatusEnum.STATUS_4.getCode().equals(order.getOrderNodeStatus())) {
            //未发起支付，直接取消
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_21.getCode());
            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            if (processTypeEnum.isLockStock()) {
                boolean flag = erpOrderRequestService.unlockStockInSupplyChainByOrderCode(order, auth);
                if (!flag) {
                    throw new BusinessException("解锁库存失败");
                }
            }
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
        } else if (ErpOrderNodeStatusEnum.STATUS_2.getCode().equals(order.getOrderNodeStatus())) {
            //支付中，再查询一次结果
            ErpOrderPayStatusResponse payStatusResponse = erpOrderRequestService.getOrderPayStatus(order.getOrderStoreCode());
            if (payStatusResponse.isRequestSuccess()) {
                ErpPayStatusEnum payStatusEnum = payStatusResponse.getPayStatusEnum();
                if (payStatusEnum == ErpPayStatusEnum.SUCCESS || payStatusEnum == ErpPayStatusEnum.FAIL) {
                    endOrderPay(orderCode, payStatusResponse.getPayCode(), payStatusEnum, auth, false);
                    //支付成功后续操作
                    this.orderPaySuccessMethodGroup(orderCode, auth);
                }
            }
        } else {

        }
    }

}
