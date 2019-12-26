package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderPayDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayCallbackRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderLogisticsPrintQueryResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayResultResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
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
    private ErpOrderLogisticsService erpOrderLogisticsService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private ErpOrderDeliverService erpOrderDeliverService;

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
    @Transactional(rollbackFor = Exception.class)
    public void orderPay(ErpOrderPayRequest erpOrderPayRequest) {

        AuthToken auth = AuthUtil.getCurrentAuth();

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
        if (orderFee == null) {
            throw new BusinessException("订单费用信息异常");
        }
        if (!ErpPayStatusEnum.UNPAID.getCode().equals(orderFee.getPayStatus())) {
            throw new BusinessException("不是" + ErpPayStatusEnum.UNPAID.getDesc() + "的订单不能发起支付");
        }

        //TODO CT 调用支付中心支付 拿到支付流水号
        String payCode = System.currentTimeMillis() + "";

        //支付单id
        String payId = OrderPublic.getUUID();

        //生成支付单
        ErpOrderPay orderPay = new ErpOrderPay();
        orderPay.setPayId(payId);
        orderPay.setPayCode(payCode);
        orderPay.setPayFee(orderFee.getPayMoney());
        orderPay.setBusinessKey(order.getOrderStoreCode());
        orderPay.setFeeType(ErpPayFeeTypeEnum.ORDER_FEE.getCode());
        orderPay.setPayStatus(ErpPayPollingBackStatusEnum.STATUS_0.getCode());
        orderPay.setPayStartTime(new Date());
        orderPay.setPayWay(erpOrderPayRequest.getPayWay());
        this.saveOrderPay(orderPay, auth);

        //修改费用单
        orderFee.setPayId(payId);
        orderFee.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
        erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);

        //开启轮询
//        payPolling(payId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void autoPay(String orderId) {

        //订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderId(orderId);

        ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
        payRequest.setOrderCode(order.getOrderStoreCode());
        payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
        //发起支付
        orderPay(payRequest);

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

        ErpOrderPay orderPay = this.getOrderPayByPayId(orderFee.getPayId());
        result.setOrderCode(order.getOrderStoreCode());
        result.setOrderId(order.getOrderStoreId());
        result.setOrderStatus(order.getOrderStatus());
        result.setGoodsCoupon(orderFee.getGoodsCoupon());
        result.setPayStatus(orderFee.getPayStatus());
        if (orderPay != null) {
            result.setPayId(orderPay.getPayId());
            result.setPayCode(orderPay.getPayCode());
            result.setPayStartTime(orderPay.getPayStartTime());
            result.setPayEndTime(orderPay.getPayEndTime());
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payPolling(String payId) {

        //登录信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        logger.info("开始支付轮询：{}", payId);

        //轮询请求查询支付信息
        ScheduledExecutorService service = new ScheduledThreadPoolExecutor(1);
        service.scheduleAtFixedRate(new Runnable() {

            int pollingTimes = 0;

            @Override
            public void run() {
                logger.info("第{}次轮询", pollingTimes);
                ErpOrderPay orderPay = getOrderPayByPayId(payId);
                if (orderPay == null) {
                    throw new BusinessException("无效的支付id");
                }

                //TODO CT 根据支付单状态转换支付状态
                ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.PAYING;

                if (ErpPayStatusEnum.PAYING != payStatusEnum) {
                    service.shutdown();
                    logger.info("结束支付轮询：{}", payId);
                }
                pollingTimes++;
                logger.info("第{}次轮询支付单：{}", pollingTimes, payId);
                if (pollingTimes > OrderConstant.MAX_PAY_POLLING_TIMES) {
                    //支付超时
                    orderPay.setPayStatus(ErpPayPollingBackStatusEnum.STATUS_7.getCode());
                    orderPay.setPayEndTime(new Date());
                    updateOrderPaySelective(orderPay, auth);
                    endPay(payId, ErpPayStatusEnum.FAIL, false);
                    service.shutdown();
                    logger.info("结束支付轮询：{}", payId);
                }

                //TODO CT 轮询支付中心，查看结果
                ErpOrderPayStatusResponse payStatusResponse = erpOrderRequestService.getOrderPayStatus(payId);

                if (payStatusResponse.isRequestSuccess()) {
                    if (ErpPayStatusEnum.FAIL == payStatusResponse.getPayStatusEnum() || ErpPayStatusEnum.SUCCESS == payStatusResponse.getPayStatusEnum()) {

                        orderPay.setPayStatus(payStatusResponse.getPayPollingBackStatusEnum().getCode());
                        orderPay.setPayCode(payStatusResponse.getPayCode());
                        orderPay.setPayEndTime(new Date());
                        updateOrderPaySelective(orderPay, auth);
                        endPay(payId, payStatusResponse.getPayStatusEnum(), false);
                        service.shutdown();
                        logger.info("结束支付轮询：{}", payId);
                    }
                }
            }
            //轮询时间控制
        }, OrderConstant.MAX_PAY_POLLING_INITIALDELAY, OrderConstant.MAX_PAY_POLLING_PERIOD, TimeUnit.MILLISECONDS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayCallback(ErpOrderPayCallbackRequest erpOrderPayCallbackRequest) {

        //TODO CT 校验参数

        //支付单状态
        ErpPayPollingBackStatusEnum payPollingBackStatusEnum = ErpPayPollingBackStatusEnum.STATUS_1;
        //支付状态（根据支付单状态判断）
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.SUCCESS;
        //订单号
        String payId = erpOrderPayCallbackRequest.getPayId();
//        //支付中心支付单号
//        String payCode = erpOrderPayCallbackRequest.getPayCode();

        AuthToken auth = AuthUtil.getSystemAuth();
        if (auth.getPersonId() == null || "".equals(auth.getPersonId())) {
            auth = AuthUtil.getSystemAuth();
        }

        ErpOrderPay orderPay = getOrderPayByPayId(payId);
        if (orderPay == null) {
            throw new BusinessException("无效的payId");
        }

        //判断如果支付单已经是完成的状态
//        if (ErpPayStatusEnum.UNPAID.getCode().equals(orderPay.getPayStatus())) {
//            throw new BusinessException("单据未发起支付");
//        }
//

        orderPay.setPayStatus(payPollingBackStatusEnum.getCode());
        orderPay.setPayEndTime(new Date());
        this.updateOrderPaySelective(orderPay, auth);

        //支付单支付后续逻辑
        this.endPay(payId, payStatusEnum, false);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String endPay(String payId, ErpPayStatusEnum payStatusEnum, boolean manual) {

        AuthToken auth = AuthUtil.getSystemAuth();
        ErpOrderPay orderPay = this.getOrderPayByPayId(payId);
        //支付单类型
        ErpPayFeeTypeEnum payFeeTypeEnum = ErpPayFeeTypeEnum.getEnum(orderPay.getFeeType());

        switch (payFeeTypeEnum) {
            //订单费用
            case ORDER_FEE:
                ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByPayId(payId);
                if (orderFee == null) {
                    throw new BusinessException("订单费用数据异常");
                }
                ErpOrderInfo order = erpOrderQueryService.getOrderByOrderId(orderFee.getOrderId());
                if (order == null) {
                    throw new BusinessException("订单数据异常");
                }
                order.setItemList(erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId()));
                if (payStatusEnum == ErpPayStatusEnum.SUCCESS) {
                    //支付成功

                    //订单节点流程控制类型
                    ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
                    BigDecimal goodsCoupon = BigDecimal.ZERO;
                    if (processTypeEnum != null && processTypeEnum.isHasGoodsCoupon()) {
                        //获取返还物流券
                        goodsCoupon = erpOrderRequestService.getGoodsCoupon(order);
                    }
                    orderFee.setGoodsCoupon(goodsCoupon);
                    orderFee.setPayStatus(payStatusEnum.getCode());
                    erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);

                    if (manual) {
                        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus()) ? ErpOrderStatusEnum.ORDER_STATUS_92.getCode() : ErpOrderStatusEnum.ORDER_STATUS_93.getCode());
                    } else {
                        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_2.getCode());
                    }
                    order.setPaymentStatus(StatusEnum.YES.getCode());
                    order.setPaymentTime(orderPay.getPayEndTime());
                    order.setPaymentCode(orderPay.getPayWay().toString());
                    order.setPaymentName(ErpPayWayEnum.getEnumDesc(orderPay.getPayWay()));
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

                    //请求供应链，获取库存分组，进行拆单
                    ScheduledExecutorService splitService = new ScheduledThreadPoolExecutor(1);
                    splitService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {

                            if (ErpOrderStatusEnum.ORDER_STATUS_93.getCode().equals(order.getOrderStatus())) {
                                //TODO CT 如果是已取消人工确认，重新锁库存
                                erpOrderRequestService.lockStockInSupplyChain(order);
                            }

                            //拆单
                            erpOrderInfoService.orderSplit(order);

                            splitService.shutdown();
                        }
                        //轮询时间控制
                    }, 0, 1000L, TimeUnit.MILLISECONDS);

                } else if (payStatusEnum == ErpPayStatusEnum.FAIL) {
                    //支付失败

                    //修改费用单信息
                    orderFee.setPayStatus(payStatusEnum.getCode());
                    erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee, auth);

                    //修改订单状态
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_99.getCode());
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);

                    //解锁库存
                    ScheduledExecutorService unlockStockService = new ScheduledThreadPoolExecutor(1);
                    unlockStockService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {

                            //请求供应链解锁库存
                            erpOrderRequestService.unlockStockInSupplyChain(order, ErpOrderLockStockTypeEnum.UNLOCK);

                            unlockStockService.shutdown();
                        }
                        //轮询时间控制
                    }, 0, 1000L, TimeUnit.MILLISECONDS);

                } else {

                }
                break;

            //物流费用
            case LOGISTICS_FEE:
                //查询物流费用
                ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByPayId(orderPay.getPayId());
                if (orderLogistics == null) {
                    throw new BusinessException("数据异常");
                }
                if (payStatusEnum == ErpPayStatusEnum.SUCCESS) {
                    //物流费用支付成功

                    orderLogistics.setPayStatus(payStatusEnum.getCode());
                    erpOrderLogisticsService.updateOrderLogisticsSelective(orderLogistics, auth);
                    //TODO CT 调用结算中心接口
                    //TODO CT 优惠券消券

                    //订单修改为已支付物流费用
                    List<ErpOrderInfo> orderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());
                    if (orderList != null && orderList.size() > 0) {
                        for (ErpOrderInfo item :
                                orderList) {
                            if (ErpOrderStatusEnum.ORDER_STATUS_11.getCode().equals(item.getOrderStatus())) {
                                item.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                                erpOrderInfoService.updateOrderByPrimaryKeySelective(item, auth);

                                //均摊物流费用
                                erpOrderDeliverService.distributeLogisticsFee(orderLogistics.getLogisticsCode());
                            }
                        }
                    }
                } else if (payStatusEnum == ErpPayStatusEnum.FAIL) {
                    //物流费用支付失败

                    orderLogistics.setPayStatus(payStatusEnum.getCode());
                    orderLogistics.setCouponPayFee(null);
                    orderLogistics.setBalancePayFee(null);
                    orderLogistics.setCouponIds(null);
                    erpOrderLogisticsService.updateOrderLogistics(orderLogistics, auth);
                } else {

                }
                ;
                break;
            default:
                ;
        }


        return null;
    }

    @Override
    public ErpOrderPay getOrderPayRepayInfo(ErpOrderPayRequest erpOrderPayRequest) {

        if (erpOrderPayRequest == null || StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("空订单编码");
        }
        //订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        if (orderFee == null) {
            throw new BusinessException("订单费用信息异常");
        }
        ErpOrderPay orderPay = this.getOrderPayByPayId(orderFee.getPayId());
        if (orderPay == null) {
            throw new BusinessException("订单费用支付信息异常");
        }

        //请求支付中心获取支付结果
        ErpOrderPayStatusResponse orderPayStatus = erpOrderRequestService.getOrderPayStatus(orderPay.getPayId());
        if (!orderPayStatus.isRequestSuccess()) {
            throw new BusinessException("查询支付状态异常");
        }
        ErpOrderPay result = new ErpOrderPay();
        result.setPayFee(orderPay.getPayFee());
        result.setPayStatus(orderPayStatus.getPayStatusEnum().getCode());
        result.setPayStartTime(orderPay.getPayStartTime());
        result.setPayCode(orderPay.getPayCode());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPayRepay(ErpOrderPayRequest erpOrderPayRequest) {
        AuthToken auth = AuthUtil.getCurrentAuth();

        if (erpOrderPayRequest == null || StringUtils.isEmpty(erpOrderPayRequest.getOrderCode())) {
            throw new BusinessException("空订单编码");
        }
        //订单
        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderPayRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单编码");
        }
        if (!ErpOrderStatusEnum.ORDER_STATUS_1.getCode().equals(order.getOrderStatus()) && !ErpOrderStatusEnum.ORDER_STATUS_99.getCode().equals(order.getOrderStatus())) {
            throw new BusinessException("只有" + ErpOrderStatusEnum.ORDER_STATUS_1.getDesc() + "和" + ErpOrderStatusEnum.ORDER_STATUS_99.getDesc() + "的订单才支持该操作");
        }
        //订单费用
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByFeeId(order.getFeeId());
        if (orderFee == null) {
            throw new BusinessException("订单费用信息异常");
        }
        //订单费用支付单
        ErpOrderPay orderPay = this.getOrderPayByPayId(orderFee.getPayId());
        if (orderPay == null) {
            throw new BusinessException("订单费用支付信息异常");
        }

        //调用支付中心接口 查询支付状态是否成功
        ErpOrderPayStatusResponse orderPayStatusResponse = erpOrderRequestService.getOrderPayStatus(orderPay.getPayId());
        if (orderPayStatusResponse.isRequestSuccess()) {
            if (orderPayStatusResponse.getPayStatusEnum() == ErpPayStatusEnum.SUCCESS) {

                orderPay.setPayStatus(orderPayStatusResponse.getPayPollingBackStatusEnum().getCode());
                orderPay.setPayEndTime(new Date());
                this.updateOrderPaySelective(orderPay, auth);

                //更新订单状态和支付状态
                this.endPay(orderPay.getPayId(), ErpPayStatusEnum.SUCCESS, true);

            } else {
                throw new BusinessException("订单未支付成功");
            }
        } else {
            throw new BusinessException("查询支付中心订单支付状态失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTimeoutUnpaid(ErpOrderPayRequest erpOrderPayRequest) {
        //TODO CT 超时未支付取消订单
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
        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsId(order.getLogisticsId());
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
        if (couponIdList != null && couponIdList.size() > 0) {
            couponIds = String.join(",", couponIdList);
        }

        if (1==1) {
            //TODO CT 临时测试接口返回值
            return;
        }

        //TODO CT 获取物流券信息
        //TODO CT 计算物流费用
        BigDecimal couponPayFee = BigDecimal.ZERO;
        BigDecimal balancePayFee = orderLogistics.getLogisticsFee().subtract(couponPayFee);

        //TODO CT 调用支付中心接口发起支付

        String payId = OrderPublic.getUUID();
        String payCode = System.currentTimeMillis() + "";

        //生成支付单
        ErpOrderPay logisticsPay = new ErpOrderPay();
        logisticsPay.setPayId(payId);
        logisticsPay.setPayCode(payCode);
        logisticsPay.setPayFee(balancePayFee);
        logisticsPay.setBusinessKey(orderLogistics.getLogisticsCode());
        logisticsPay.setFeeType(ErpPayFeeTypeEnum.LOGISTICS_FEE.getCode());
        logisticsPay.setPayStatus(ErpPayPollingBackStatusEnum.STATUS_0.getCode());
        logisticsPay.setPayStartTime(new Date());
        logisticsPay.setPayWay(erpOrderPayRequest.getPayWay());
        this.saveOrderPay(logisticsPay, auth);

        orderLogistics.setPayStatus(ErpPayStatusEnum.PAYING.getCode());
        orderLogistics.setCouponPayFee(couponPayFee);
        orderLogistics.setBalancePayFee(balancePayFee);
        orderLogistics.setCouponIds(couponIds);
        orderLogistics.setPayId(payId);
        erpOrderLogisticsService.updateOrderLogisticsSelective(orderLogistics, auth);

        //开启轮询
        this.payPolling(payId);
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

            if (StringUtils.isNotEmpty(orderLogistics.getPayId())) {
                ErpOrderPay orderPay = this.getOrderPayByPayId(orderLogistics.getPayId());
                if (orderPay != null) {
                    response.setPayId(orderPay.getPayId());
                    response.setPayCode(orderPay.getPayCode());
//                    response.setPayStatus(orderPay.getPayStatus());
                    response.setPayStartTime(orderPay.getPayStartTime());
                    response.setPayEndTime(orderPay.getPayEndTime());
                }
            }
        } else {
            throw new BusinessException("未找到关联物流单");
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
        ErpOrderPay logisticsPay = getOrderPayByPayId(orderLogistics.getPayId());
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

}
