package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
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
     * 查询商品明细
     *
     * @param storeId   门店id
     * @param productId 商品id
     * @param skuCode   商品sku编码
     * @return com.aiqin.mgs.order.api.domain.ProductInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    ProductInfo getProductDetail(String storeId, String productId, String skuCode);

    /**
     * 根据订单编码获取订单支付状态
     *
     * @param orderCode 订单编码
     * @return com.aiqin.mgs.order.api.component.enums.PayStatusEnum
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    ErpOrderPayStatusResponse getOrderPayStatus(String orderCode);

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
    boolean sendPayRequest(OrderStoreOrderInfo order, OrderStoreOrderPay orderPay);

    /**
     * 获取物流券
     *
     * @param order 订单信息
     * @return java.math.BigDecimal
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:45
     */
    BigDecimal getGoodsCoupon(OrderStoreOrderInfo order);

    /**
     * 订单支付成功推送供应链
     *
     * @param order
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:48
     */
    boolean sendOrderPaySuccessToSupplyChain(OrderStoreOrderInfo order);

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
    boolean sendSplitOrderToSupplyChain(OrderStoreOrderInfo order, List<OrderStoreOrderInfo> splitOrderList);

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
