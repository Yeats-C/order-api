package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderLockStockTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTransactionTypeEnum;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderItemSplitGroupResponse;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单调用其他系统service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/2 15:01
 */
public interface ErpOrderRequestService {

    /**
     * 根据门店id查询门店信息
     *
     * @param storeId 门店id
     * @return com.aiqin.mgs.order.api.domain.StoreInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:36
     */
    StoreInfo getStoreInfoByStoreId(String storeId);

    /**
     * 查询sku详情
     *
     * @param companyCode 公司编码
     * @param skuCode     sku编码
     * @return com.aiqin.mgs.order.api.domain.ProductInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/23 15:06
     */
    ProductInfo getSkuDetail(String companyCode, String skuCode);

    /**
     * 根据物流券编码获取物流券信息
     *
     * @param logisticsCode
     * @return com.aiqin.mgs.order.api.domain.LogisticsCouponDetail
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/7 20:48
     */
    LogisticsCouponDetail getLogisticsCouponByCode(String logisticsCode);

    /**
     * 申请取消订单
     *
     * @param orderCode 订单号
     * @param auth      操作人
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/8 19:34
     */
    boolean applyToCancelOrderRequest(String orderCode, AuthToken auth);

    /**
     * 把订单对应的物流券全部注销
     *
     * @param orderCode 订单编号
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/7 17:34
     */
    void turnOffCouponsByOrderId(String orderCode);

    /**
     * 修改优惠券状态
     *
     * @param franchiseeId 加盟商id
     * @param couponCode   优惠券编码
     * @param businessCode 业务编码（使用优惠券的单据号）
     * @param storeName    门店名称
     * @param payCode      支付流水号
     * @param balancePay   支付方式编码
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/7 17:42
     */
    void updateCouponStatus(String franchiseeId, String couponCode, String businessCode, String storeName, String payCode, String balancePay);

    /**
     * 锁库存
     *
     * @param order
     * @param auth
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:38
     */
    boolean lockStockInSupplyChain(ErpOrderInfo order, List<ErpOrderItem> itemList, AuthToken auth);

    /**
     * 解锁库存（根据明细解锁）
     *
     * @param order                  订单信息
     * @param orderLockStockTypeEnum 操作类型
     * @param auth                   操作人
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:38
     */
    boolean unlockStockInSupplyChainByDetail(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum, AuthToken auth);

    /**
     * 解锁库存（按照订单号解锁）
     *
     * @param order
     * @param auth
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/6 13:58
     */
    boolean unlockStockInSupplyChainByOrderCode(ErpOrderInfo order, AuthToken auth);

    /**
     * 根据订单编码获取订单支付状态
     *
     * @param orderCode 订单编码
     * @return com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    ErpOrderPayStatusResponse getOrderPayStatus(String orderCode);

    /**
     * 根据物流单号查询物流单支付状态
     *
     * @param logisticsCode 物流单号
     * @return com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/1 11:17
     */
    ErpOrderPayStatusResponse getOrderLogisticsPayStatus(String logisticsCode);

    /**
     * 根据退款单号查询退款单状态
     *
     * @param refundCode 退款单号
     * @return com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/8 19:35
     */
    ErpOrderPayStatusResponse getOrderRefundStatus(String refundCode);

    /**
     * 发起支付请求
     *
     * @param order    订单信息
     * @param orderFee 订单费用信息
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    boolean sendOrderPayRequest(ErpOrderInfo order, ErpOrderFee orderFee);

    /**
     * 发起物流费支付请求
     *
     * @param order          基数据订单
     * @param orderList      物流单关联的全部订单
     * @param orderLogistics 物流单
     * @param auth           操作人
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/7 10:13
     */
    boolean sendLogisticsPayRequest(ErpOrderInfo order, List<ErpOrderInfo> orderList, ErpOrderLogistics orderLogistics, AuthToken auth);

    /**
     * 发起退款请求
     *
     * @param order                  订单信息
     * @param orderRefund            退款单信息
     * @param payTransactionTypeEnum 交易业务类型
     * @param auth                   操作人
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/8 19:35
     */
    boolean sendOrderRefundRequest(ErpOrderInfo order, ErpOrderRefund orderRefund, ErpRequestPayTransactionTypeEnum payTransactionTypeEnum, AuthToken auth);

    /**
     * 支付成功获取返还物流券金额
     *
     * @param order 订单信息
     * @return java.math.BigDecimal
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:45
     */
    BigDecimal getGoodsCoupon(ErpOrderInfo order);

    /**
     * 订单支付成功推送供应链,返回商品库存分组数据
     *
     * @param order
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:48
     */
    List<ErpOrderItemSplitGroupResponse> getRepositorySplitGroup(ErpOrderInfo order);

    /**
     * 商品销售区域配置校验
     *
     * @param storeInfo
     * @param orderProductItemList
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 16:11
     */
    boolean areaCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList);

    /**
     * 商品库存校验
     *
     * @param storeInfo
     * @param orderProductItemList
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 16:11
     */
    boolean repertoryCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList);

    /**
     * 促销活动校验
     *
     * @param storeInfo
     * @param orderProductItemList
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 16:11
     */
    boolean activityCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList);

    /**
     * 修改门店营业状态，从A到B,如果不是A状态则不修改
     *
     * @param storeId  门店id
     * @param origCode 原状态
     * @param destCode 目标状态
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/6 13:56
     */
    void updateStoreBusinessStage(String storeId, String origCode, String destCode);

    /**
     * 首单，修改门店状态
     *
     * @param storeId
     * @param s
     */
    void updateStoreStatus(String storeId, String s);
}
