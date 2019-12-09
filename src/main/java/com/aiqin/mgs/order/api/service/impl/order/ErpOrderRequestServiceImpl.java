package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderPay;
import com.aiqin.mgs.order.api.domain.response.order.ErpOrderPayStatusResponse;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ErpOrderRequestServiceImpl implements ErpOrderRequestService {

    /**
     * 根据门店id查询门店信息
     *
     * @param storeId 门店id
     * @return com.aiqin.mgs.order.api.domain.StoreInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:36
     */
    @Override
    public StoreInfo getStoreInfoByStoreId(String storeId) {
        return null;
    }

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
    @Override
    public ProductInfo getProductDetail(String storeId, String productId, String skuCode) {
        return null;
    }

    /**
     * 根据订单编码获取订单支付状态
     *
     * @param orderCode 订单编码
     * @return com.aiqin.mgs.order.api.component.enums.PayStatusEnum
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:37
     */
    @Override
    public ErpOrderPayStatusResponse getOrderPayStatus(String orderCode) {
        return null;
    }

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
    @Override
    public boolean sendPayRequest(ErpOrderInfo order, ErpOrderPay orderPay) {
        return false;
    }

    /**
     * 获取物流券
     *
     * @param order 订单信息
     * @return java.math.BigDecimal
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:45
     */
    @Override
    public BigDecimal getGoodsCoupon(ErpOrderInfo order) {
        return null;
    }

    /**
     * 订单支付成功推送供应链
     *
     * @param order
     * @return boolean
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/2 15:48
     */
    @Override
    public boolean sendOrderPaySuccessToSupplyChain(ErpOrderInfo order) {
        return false;
    }

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
    @Override
    public boolean sendSplitOrderToSupplyChain(ErpOrderInfo order, List<ErpOrderInfo> splitOrderList) {
        return false;
    }

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
    @Override
    public boolean areaCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        return false;
    }

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
    @Override
    public boolean repertoryCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        return false;
    }

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
    @Override
    public boolean priceCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        return false;
    }

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
    @Override
    public boolean activityCheck(StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {
        return false;
    }
}
