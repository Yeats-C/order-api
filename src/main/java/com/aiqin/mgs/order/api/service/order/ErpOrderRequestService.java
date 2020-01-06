package com.aiqin.mgs.order.api.service.order;

import com.aiqin.mgs.order.api.component.enums.ErpOrderLockStockTypeEnum;
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
     * @param auth
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:38
     */
    boolean lockStockInSupplyChain(ErpOrderInfo order,List<ErpOrderItem> itemList, AuthToken auth);

    /**
     * 解锁库存（根据明细解锁）
     *
     * @param order                  订单信息
     * @param orderLockStockTypeEnum 操作类型
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 13:38
     */
    boolean unlockStockInSupplyChainByDetail(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum, AuthToken auth);

    boolean unlockStockInSupplyChainByOrderCode(ErpOrderInfo order, ErpOrderLockStockTypeEnum orderLockStockTypeEnum, AuthToken auth);

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
     * 发起支付物流费用申请
     *
     * @param order
     * @param orderLogistics
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 15:45
     */
    boolean sendLogisticsPayRequest(ErpOrderInfo order, ErpOrderLogistics orderLogistics);

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

    void updateStoreBusinessStage(String storeId, String origCode, String destCode);

    /**
     * 首单，修改门店状态
     * @param storeId
     * @param s
     */
    void updateStoreStatus(String storeId, String s);
}
