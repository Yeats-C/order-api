package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeProcessTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderNodeStatusEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayFeeTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderLogisticsDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.CouponDetail;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
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

@Service
@Transactional(rollbackFor = Exception.class)
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
    @Resource
    private ErpOrderLogisticsNoTransactionalService erpOrderLogisticsNoTransactionalService;

    @Override
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
    public void updateOrderLogisticsSelective(ErpOrderLogistics po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderLogisticsDao.updateByPrimaryKeySelective(po);
    }

    @Override
    public void updateOrderLogistics(ErpOrderLogistics po, AuthToken auth) {
        po.setUpdateById(auth.getPersonId());
        po.setUpdateByName(auth.getPersonName());
        Integer integer = erpOrderLogisticsDao.updateByPrimaryKey(po);
    }

    @Override
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
                CouponDetail logisticsCoupon = erpOrderRequestService.getCouponDetailByCode(couponIdItem);
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
            erpOrderLogisticsNoTransactionalService.orderLogisticsPayPolling(orderLogistics.getLogisticsCode(), auth);
        }

    }

    @Override
    public void endOrderLogisticsPay(String logisticsCode, String payCode, ErpPayStatusEnum payStatusEnum, AuthToken auth) {

        //查询物流费用
        ErpOrderLogistics orderLogistics = this.getOrderLogisticsByLogisticsCode(logisticsCode);
        if (orderLogistics == null) {
            throw new BusinessException("数据异常");
        }

        //更新物流单关联支付单
        ErpOrderPay logisticsPay = erpOrderPayService.getOrderPayByPayId(orderLogistics.getPayId());
        logisticsPay.setPayEndTime(new Date());
        logisticsPay.setPayCode(payCode);
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
                        erpOrderRequestService.updateCouponStatus(order.getFranchiseeId(), couponCode, orderLogistics.getLogisticsCode(), order.getStoreName());
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
