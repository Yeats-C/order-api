package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderLockStockTypeEnum;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
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
     * @param skuCode     锁库编码
     * @return com.aiqin.mgs.order.api.domain.ProductInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/23 15:06
     */
    ProductInfo getSkuDetail(String companyCode, String skuCode);

    /**
     * 锁库存
     *
     * @param order
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:38
     */
    void lockStockInSupplyChain(ErpOrderInfo order);

    /**
     * 解锁库存
     *
     * @param order                  订单信息
     * @param orderLockStockTypeEnum 操作类型
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:38
     */
    void unlockStockInSupplyChain(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum);

    /**
     * 根据支付id获取订单支付状态
     *
     * @param payId 订单编码
     * @return com.aiqin.mgs.order.api.component.enums.ErpPayStatusEnum
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    ErpOrderPayStatusResponse getOrderPayStatus(String payId);

    /**
     * 发起支付请求
     *
     * @param order    订单信息
     * @param orderPay 订单支付信息
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    boolean sendOrderPayRequest(ErpOrderInfo order, ErpOrderPay orderPay);

    /**
     * 发起支付物流费用申请
     *
     * @param orderLogistics
     * @param orderPay
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 15:45
     */
    boolean sendLogisticsPayRequest(ErpOrderLogistics orderLogistics, ErpOrderPay orderPay);

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
    ErpOrderInfo sendOrderToSupplyChainAndGetSplitGroup(ErpOrderInfo order);

    /**
     * 推送拆分后的订单到供应链
     *
     * @param order
     * @param splitOrderList
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/3 17:26
     */
    boolean sendSplitOrderToSupplyChain(ErpOrderInfo order, List<ErpOrderInfo> splitOrderList);

    /**
     * 请求供应链取消订单
     *
     * @param order
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/12 19:04
     */
    void applyToCancelOrderRequest(ErpOrderInfo order);

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
     * 商品销售价格校验
     *
     * @param storeInfo
     * @param orderProductItemList
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 16:11
     */
    boolean priceCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList);

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

}
