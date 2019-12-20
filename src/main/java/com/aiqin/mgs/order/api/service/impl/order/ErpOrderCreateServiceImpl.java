package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.order.*;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 创建订单service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 13:57
 */
@Service
public class ErpOrderCreateServiceImpl implements ErpOrderCreateService {

    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private CartOrderService cartOrderService;
    @Resource
    private ErpOrderPayService erpOrderPayService;
    @Resource
    private ErpOrderConsigneeService erpOrderConsigneeService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo saveOrder(ErpOrderSaveRequest erpOrderSaveRequest) {

        //操作人信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);
        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //获取购物车商品
        OrderConfirmResponse storeCartProduct = getStoreCartProduct(erpOrderSaveRequest.getStoreId());
        //构建订单商品明细行
        List<ErpOrderItem> erpOrderItemList = generateOrderItemList(storeCartProduct.getCartOrderInfos(), storeInfo);
        //购物车数据校验
        productCheck(erpOrderSaveRequest.getOrderType(), storeInfo, erpOrderItemList);
        //生成订单主体信息
        ErpOrderInfo erpOrderInfo = generateOrder(erpOrderItemList, storeInfo, erpOrderSaveRequest, auth);
        //计算均摊金额
        sharePrice(erpOrderInfo);
        //保存订单、订单明细、订单支付、订单收货人信息、订单日志
        String orderId = insertOrder(erpOrderInfo, storeInfo, auth, erpOrderSaveRequest);
        //删除购物车商品
        deleteOrderProductFromCart(erpOrderSaveRequest.getStoreId(), storeCartProduct);
        //锁库存
        erpOrderRequestService.lockStockInSupplyChain(erpOrderInfo);
        //返回订单信息
        return erpOrderQueryService.getOrderByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest) {
        //操作人信息
        AuthToken auth = AuthUtil.getCurrentAuth();
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest);
        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //商品参数信息
        List<ErpOrderItem> itemList = erpOrderSaveRequest.getItemList();

        //构建货架订单商品信息行
        List<ErpOrderItem> orderItemList = generateRackOrderItemList(itemList, storeInfo);
        //构建和保存货架订单，返回订单编号
        String orderId = generateAndSaveRackOrder(erpOrderSaveRequest, orderItemList, storeInfo, auth);

        //返回订单
        return erpOrderQueryService.getOrderByOrderId(orderId);
    }

    /**
     * 校验保存订单参数
     *
     * @param erpOrderSaveRequest
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 9:40
     */
    private void validateSaveOrderRequest(ErpOrderSaveRequest erpOrderSaveRequest) {
        AuthToken auth = AuthUtil.getCurrentAuth();
        if (auth.getPersonId() == null) {
            throw new BusinessException("请先登录");
        }
        if (erpOrderSaveRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderSaveRequest.getStoreId())) {
            throw new BusinessException("请传入门店id");
        }
        if (erpOrderSaveRequest.getOrderType() == null) {
            throw new BusinessException("请传入订单类型");
        } else {
            if (!ErpOrderTypeEnum.exist(erpOrderSaveRequest.getOrderType())) {
                throw new BusinessException("无效的订单类型");
            }
        }
        if (erpOrderSaveRequest.getOrderOriginType() == null) {
            throw new BusinessException("请选择订单来源");
        } else {
            if (!ErpOrderOriginTypeEnum.exist(erpOrderSaveRequest.getOrderOriginType())) {
                throw new BusinessException("无效的订单来源");
            }
        }
        if (erpOrderSaveRequest.getOrderChannel() == null) {
            throw new BusinessException("请选择销售渠道");
        } else {
            if (!ErpOrderChannelTypeEnum.exist(erpOrderSaveRequest.getOrderChannel())) {
                throw new BusinessException("无效的销售渠道");
            }
        }
    }

    /**
     * 从购物车获取选择的门店的勾选的商品
     *
     * @param storeId 门店id
     * @return com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:00
     */
    private OrderConfirmResponse getStoreCartProduct(String storeId) {
//        CartOrderInfo cartOrderInfoQuery = new CartOrderInfo();
//        cartOrderInfoQuery.setStoreId(storeId);
//        cartOrderInfoQuery.setLineCheckStatus(YesOrNoEnum.YES.getCode());
//        HttpResponse listHttpResponse = cartOrderService.displayCartLineCheckProduct(cartOrderInfoQuery);
//        if (!RequestReturnUtil.validateHttpResponse(listHttpResponse)) {
//            throw new BusinessException("获取购物车商品失败");
//        }
//        OrderConfirmResponse data = (OrderConfirmResponse) listHttpResponse.getData();
//        if (data == null || data.getCartOrderInfos() == null || data.getCartOrderInfos().size() == 0) {
//            throw new BusinessException("购物车没有勾选的商品");
//        }

        OrderConfirmResponse data = new OrderConfirmResponse();
        List<CartOrderInfo> list = new ArrayList<>();
        CartOrderInfo info1 = new CartOrderInfo();
        info1.setProductId("1000001");
        info1.setProductName("奶粉");
        info1.setSkuCode("9001");
        info1.setPrice(new BigDecimal(5));
        info1.setAmount(11);
        list.add(info1);

        CartOrderInfo info2 = new CartOrderInfo();
        info2.setProductId("1000002");
        info2.setProductName("玩具");
        info2.setSkuCode("9002");
        info2.setPrice(new BigDecimal(10));
        info2.setAmount(22);
        list.add(info2);

        CartOrderInfo info3 = new CartOrderInfo();
        info3.setProductId("1000003");
        info3.setProductName("衣服");
        info3.setSkuCode("9003");
        info3.setPrice(new BigDecimal(15));
        info3.setAmount(8);
        list.add(info3);
        data.setCartOrderInfos(list);

        return data;
    }

    /**
     * 构建订单商品明细行数据
     *
     * @param cartProductList 购物车商品行
     * @param storeInfo       门店信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.ErpOrderItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:02
     */
    private List<ErpOrderItem> generateOrderItemList(List<CartOrderInfo> cartProductList, StoreInfo storeInfo) {

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //遍历参数商品列表，获取商品详情，校验数据
        for (CartOrderInfo item :
                cartProductList) {
            //获取商品详情
            ProductInfo product = erpOrderRequestService.getProductDetail(storeInfo.getStoreId(), item.getProductId(), item.getSkuCode());
            if (product == null) {
                throw new BusinessException("未获取到商品" + item.getProductName() + "的信息");
            }
            productMap.put(item.getSkuCode(), product);
        }

        //订单商品明细行
        List<ErpOrderItem> orderItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (CartOrderInfo item :
                cartProductList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode());

            ErpOrderItem orderItem = new ErpOrderItem();

            //商品ID
            orderItem.setProductId(productInfo.getProductId());
            //商品编码
            orderItem.setProductCode(productInfo.getProductCode());
            //商品名称
            orderItem.setProductName(productInfo.getProductName());
            //商品sku码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //商品名称
            orderItem.setSkuName(productInfo.getSkuName());
            //单位
            orderItem.setUnit(productInfo.getUnit());
            //本品赠品标记 ErpProductGiftEnum
            orderItem.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());

            //活动id
            orderItem.setActivityId(null);

            //订货数量
            orderItem.setQuantity(item.getAmount());
            //订货价
            orderItem.setPrice(item.getPrice());
            //订货金额
            orderItem.setMoney(productInfo.getPrice().multiply(new BigDecimal(item.getAmount())).setScale(4, RoundingMode.UP));
            //活动优惠金额
            orderItem.setActivityMoney(BigDecimal.ZERO);
            //实际支付金额
            orderItem.setActualMoney(orderItem.getMoney().subtract(orderItem.getActivityMoney()));

            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    /**
     * 商品校验
     *
     * @param orderType            订单类型
     * @param storeInfo            门店信息
     * @param orderProductItemList 订单商品明细行
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:03
     */
    private void productCheck(Integer orderType, StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {

        //订单类型
        ErpOrderTypeEnum erpOrderTypeEnum = ErpOrderTypeEnum.getEnum(orderType);
        if (erpOrderTypeEnum == null) {
            throw new BusinessException("无效的订单类型");
        }

        if (erpOrderTypeEnum.isActivityCheck()) {
            //活动校验
            boolean flag = erpOrderRequestService.activityCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("活动校验失败");
            }
        }
        if (erpOrderTypeEnum.isAreaCheck()) {
            //商品销售区域配置校验
            boolean flag = erpOrderRequestService.areaCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品销售区域配置校验失败");
            }
        }
        if (erpOrderTypeEnum.isPriceCheck()) {
            //商品销售价格校验
            boolean flag = erpOrderRequestService.priceCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品销售价格校验失败");
            }
        }
        if (erpOrderTypeEnum.isRepertoryCheck()) {
            //商品库存校验
            boolean flag = erpOrderRequestService.repertoryCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品库存校验失败");
            }
        }

    }

    /**
     * 构建订单信息
     *
     * @param orderItemList       订单商品明细行
     * @param storeInfo           门店信息
     * @param erpOrderSaveRequest 保存订单参数
     * @param auth                操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:04
     */
    private ErpOrderInfo generateOrder(List<ErpOrderItem> orderItemList, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

        //订货金额汇总
        BigDecimal moneyTotal = BigDecimal.ZERO;
        //实际支付金额汇总
        BigDecimal realMoneyTotal = BigDecimal.ZERO;
        //活动优惠金额汇总
        BigDecimal activityMoneyTotal = BigDecimal.ZERO;

        for (ErpOrderItem item :
                orderItemList) {

            //订货金额汇总
            moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
            //实际支付金额汇总
            realMoneyTotal = realMoneyTotal.add(item.getActualMoney() == null ? BigDecimal.ZERO : item.getActualMoney());
            //活动优惠金额汇总
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityMoney() == null ? BigDecimal.ZERO : item.getActivityMoney());
        }

        //保存订单费用信息
        ErpOrderFee orderFee = new ErpOrderFee();
        //订单总额
        orderFee.setTotalMoney(moneyTotal);
        //活动优惠金额
        orderFee.setActivityMoney(activityMoneyTotal);
        //服纺券优惠金额
        orderFee.setSuitCouponMoney(BigDecimal.ZERO);
        //A品券优惠金额
        orderFee.setTopCouponMoney(BigDecimal.ZERO);
        //实付金额
        orderFee.setPayMoney(orderFee.getTotalMoney().subtract(orderFee.getActivityMoney()).subtract(orderFee.getSuitCouponMoney()).subtract(orderFee.getTopCouponMoney()));

        //保存订单信息
        ErpOrderInfo orderInfo = new ErpOrderInfo();
        //订单费用信息
        orderInfo.setOrderFee(orderFee);
        //订单状态 枚举 ErpOrderStatusEnum
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        //订单类型 枚举 ErpOrderTypeEnum
        orderInfo.setOrderType(erpOrderSaveRequest.getOrderType());
        //订单来源 ErpOrderOriginTypeEnum
        orderInfo.setOrderOriginType(erpOrderSaveRequest.getOrderOriginType());
        //订单销售渠道标识 ErpOrderChannelTypeEnum
        orderInfo.setOrderChannelType(erpOrderSaveRequest.getOrderChannel());
        //订单级别 枚举 ErpOrderLevelEnum
        orderInfo.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 YesOrNoEnum
        orderInfo.setSplitStatus(YesOrNoEnum.NO.getCode());
        //是否发生退货 YesOrNoEnum
        orderInfo.setReturnStatus(YesOrNoEnum.NO.getCode());
        //加盟商id
        orderInfo.setFranchiseeId(storeInfo.getFranchiseeId());
        //加盟商编码
        orderInfo.setFranchiseeCode(storeInfo.getFranchiseeCode());
        //加盟商名称
        orderInfo.setFranchiseeName(storeInfo.getFranchiseeName());
        //门店id
        orderInfo.setStoreId(storeInfo.getStoreId());
        //门店编码
        orderInfo.setStoreCode(storeInfo.getStoreCode());
        //门店名称
        orderInfo.setStoreName(storeInfo.getStoreName());

        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }

    /**
     * 计算均摊金额
     *
     * @param erpOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 15:35
     */
    private void sharePrice(ErpOrderInfo erpOrderInfo) {
        //TODO CT 计算均摊金额
        for (ErpOrderItem item :
                erpOrderInfo.getOrderItemList()) {
            item.setShareMoney(item.getActualMoney());
        }
    }

    /**
     * //保存订单、订单明细、订单支付、订单收货人信息、订单日志
     *
     * @param orderInfo           订单信息
     * @param storeInfo           门店信息
     * @param auth                操作人信息
     * @param erpOrderSaveRequest 请求参数
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 15:34
     */
    private String insertOrder(ErpOrderInfo orderInfo, StoreInfo storeInfo, AuthToken auth, ErpOrderSaveRequest erpOrderSaveRequest) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成费用id
        String feeId = OrderPublic.getUUID();
        //生成支付id
        String payId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = OrderPublic.generateOrderCode(erpOrderSaveRequest.getOrderOriginType(), erpOrderSaveRequest.getOrderChannel());
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;
        int orderItemNum = 1;
        for (ErpOrderItem item :
                orderInfo.getOrderItemList()) {
            //订单id
            item.setOrderId(orderId);
            //订单明细id
            item.setOrderItemId(OrderPublic.getUUID());
            //订单明细行编号
            item.setOrderItemCode(orderCode + String.format("%03d", orderItemNum++));
        }
        //保存订单
        orderInfo.setOrderId(orderId);
        orderInfo.setOrderCode(orderCode);
        orderInfo.setFeeId(feeId);
        erpOrderInfoService.saveOrder(orderInfo, auth);

        //保存订单明细行
        erpOrderItemService.saveOrderItemList(orderInfo.getOrderItemList(), auth);

        ErpOrderFee orderFee = orderInfo.getOrderFee();
        orderFee.setOrderId(orderId);
        orderFee.setPayId(payId);
        orderFee.setFeeId(feeId);
        orderFee.setPayStatus(payStatusEnum.getCode());
        erpOrderFeeService.saveOrderFee(orderFee, auth);

        //保存订单支付信息
        ErpOrderPay orderPay = new ErpOrderPay();
        orderPay.setPayId(payId);
        orderPay.setBusinessKey(orderCode);
        orderPay.setPayFee(orderFee.getPayMoney());
        orderPay.setPayStatus(payStatusEnum.getCode());
        orderPay.setPayWay(null);
        orderPay.setFeeType(ErpPayFeeTypeEnum.ORDER_FEE.getCode());
        erpOrderPayService.saveOrderPay(orderPay, auth);

        //保存订单收货信息
        ErpOrderConsignee orderConsignee = new ErpOrderConsignee();
        orderConsignee.setOrderId(orderId);
        orderConsignee.setReceiveId(OrderPublic.getUUID());
        orderConsignee.setReceiveName(storeInfo.getContacts());
        orderConsignee.setReceiveAddress(storeInfo.getAddress());
        orderConsignee.setReceivePhone(storeInfo.getContactsPhone());
        orderConsignee.setBillStatus(erpOrderSaveRequest.getBillStatus());
        erpOrderConsigneeService.saveOrderConsignee(orderConsignee, auth);

        return orderId;
    }

    /**
     * 从购物车删除已经生成订单的商品
     *
     * @param orderConfirmResponse 购物车商品
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:02
     */
    private void deleteOrderProductFromCart(String storeId, OrderConfirmResponse orderConfirmResponse) {
        for (CartOrderInfo item :
                orderConfirmResponse.getCartOrderInfos()) {
//            cartOrderService.deleteCartInfo(storeId, item.getSkuId(), YesOrNoEnum.YES.getCode());
        }
    }

    /**
     * 构建货架订单商品信息行数据
     *
     * @param paramItemList 货架商品参数
     * @param storeInfo     门店信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:43
     */
    private List<ErpOrderItem> generateRackOrderItemList(List<ErpOrderItem> paramItemList, StoreInfo storeInfo) {

        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少商品数据");
        }

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //参数商品行标识
        int lineIndex = 0;

        //遍历参数商品列表，获取商品详情，校验数据
        for (ErpOrderItem item :
                paramItemList) {
            lineIndex++;
            if (StringUtils.isEmpty(item.getProductId())) {
                throw new BusinessException("第" + lineIndex + "行商品id为空");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品sku为空");
            }
            if (item.getTaxPurchasePrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品含税采购价为空");
            }
            if (item.getPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品销售价为空");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("第" + lineIndex + "行商品数量为空");
            }

            //获取商品详情
            ProductInfo product = erpOrderRequestService.getProductDetail(storeInfo.getStoreId(), item.getProductId(), item.getSkuCode());
            if (product == null) {
                throw new BusinessException("第" + lineIndex + "行商品不存在");
            }

            productMap.put(item.getSkuCode(), product);
        }

        //订单商品明细行
        List<ErpOrderItem> orderItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (ErpOrderItem item :
                paramItemList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode());

            ErpOrderItem orderItem = new ErpOrderItem();

            //商品ID
            orderItem.setProductId(productInfo.getProductId());
            //商品编码
            orderItem.setProductCode(productInfo.getProductCode());
            //商品名称
            orderItem.setProductName(productInfo.getProductName());
            //商品sku码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //商品名称
            orderItem.setSkuName(productInfo.getSkuName());
            //单位
            orderItem.setUnit(productInfo.getUnit());
            //本品赠品标记 ErpProductGiftEnum
            orderItem.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());

            //订货数量
            orderItem.setQuantity(item.getQuantity());
            //订货价
            orderItem.setPrice(item.getPrice());
            //含税采购价
            orderItem.setTaxPurchasePrice(item.getTaxPurchasePrice());

            //订货金额
            orderItem.setMoney(item.getPrice().multiply(new BigDecimal(item.getQuantity())).setScale(4, RoundingMode.UP));
            //活动优惠金额
            orderItem.setActivityMoney(BigDecimal.ZERO);
            //实际支付金额
            orderItem.setActualMoney(orderItem.getMoney().subtract(orderItem.getActivityMoney()));
            //货架订单分摊金额等于实际金额
            orderItem.setShareMoney(orderItem.getActualMoney());

            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    /**
     * 构建和保存货架订单数据
     *
     * @param erpOrderSaveRequest 入口参数
     * @param orderItemList       订单商品明细行
     * @param storeInfo           门店信息
     * @param auth                操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:50
     */
    private String generateAndSaveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest, List<ErpOrderItem> orderItemList, StoreInfo storeInfo, AuthToken auth) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成费用id
        String feeId = OrderPublic.getUUID();
        //生成支付id
        String payId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = OrderPublic.generateOrderCode(erpOrderSaveRequest.getOrderOriginType(), erpOrderSaveRequest.getOrderChannel());
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;

        //订货金额汇总
        BigDecimal moneyTotal = BigDecimal.ZERO;

        int orderItemNum = 1;

        //遍历商品订单行，汇总价格
        for (ErpOrderItem item :
                orderItemList) {

            //订单id
            item.setOrderId(orderId);
            //订单明细id
            item.setOrderItemId(OrderPublic.getUUID());
            //订单明细行编号
            item.setOrderItemCode(orderCode + String.format("%03d", orderItemNum++));

            //订货金额汇总
            moneyTotal = moneyTotal.add(item.getMoney() == null ? BigDecimal.ZERO : item.getMoney());
        }

        erpOrderItemService.saveOrderItemList(orderItemList, auth);

        ErpOrderInfo orderInfo = new ErpOrderInfo();
        //订单id
        orderInfo.setOrderId(orderId);
        //订单编号
        orderInfo.setOrderCode(orderCode);
        //订单状态 枚举 ErpOrderStatusEnum
        orderInfo.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        //订单类型 枚举 ErpOrderTypeEnum
        orderInfo.setOrderType(erpOrderSaveRequest.getOrderType());
        //订单来源 ErpOrderOriginTypeEnum
        orderInfo.setOrderOriginType(erpOrderSaveRequest.getOrderOriginType());
        //订单销售渠道标识 ErpOrderChannelTypeEnum
        orderInfo.setOrderChannelType(erpOrderSaveRequest.getOrderChannel());
        //订单级别 枚举 ErpOrderLevelEnum
        orderInfo.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 YesOrNoEnum
        orderInfo.setSplitStatus(YesOrNoEnum.NO.getCode());
        //是否发生退货 YesOrNoEnum
        orderInfo.setReturnStatus(YesOrNoEnum.NO.getCode());

        //加盟商id
        orderInfo.setFranchiseeId(storeInfo.getFranchiseeId());
        //加盟商编码
        orderInfo.setFranchiseeCode(storeInfo.getFranchiseeCode());
        //加盟商名称
        orderInfo.setFranchiseeName(storeInfo.getFranchiseeName());
        //门店id
        orderInfo.setStoreId(storeInfo.getStoreId());
        //门店编码
        orderInfo.setStoreCode(storeInfo.getStoreCode());
        //门店名称
        orderInfo.setStoreName(storeInfo.getStoreName());
        //支付id
        orderInfo.setFeeId(feeId);
        erpOrderInfoService.saveOrder(orderInfo, auth);

        //保存订单费用信息
        ErpOrderFee orderFee = new ErpOrderFee();
        //费用id
        orderFee.setFeeId(feeId);
        //订单id
        orderFee.setOrderId(orderId);
        //支付单id
        orderFee.setPayId(payId);
        //支付状态
        orderFee.setPayStatus(payStatusEnum.getCode());
        //订单总额
        orderFee.setTotalMoney(moneyTotal);
        //活动优惠金额
        orderFee.setActivityMoney(BigDecimal.ZERO);
        //服纺券优惠金额
        orderFee.setSuitCouponMoney(BigDecimal.ZERO);
        //A品券优惠金额
        orderFee.setTopCouponMoney(BigDecimal.ZERO);
        //实付金额
        orderFee.setPayMoney(orderFee.getTotalMoney().subtract(orderFee.getActivityMoney()).subtract(orderFee.getSuitCouponMoney()).subtract(orderFee.getTopCouponMoney()));
        erpOrderFeeService.saveOrderFee(orderFee, auth);

        //保存订单支付信息
        ErpOrderPay orderPay = new ErpOrderPay();
        orderPay.setPayId(payId);
        orderPay.setBusinessKey(orderCode);
        orderPay.setPayFee(orderFee.getPayMoney());
        orderPay.setPayStatus(payStatusEnum.getCode());
        orderPay.setPayWay(null);
        orderPay.setFeeType(ErpPayFeeTypeEnum.ORDER_FEE.getCode());
        erpOrderPayService.saveOrderPay(orderPay, auth);

        //保存订单收货人信息
        ErpOrderConsignee orderConsignee = new ErpOrderConsignee();
        orderConsignee.setOrderId(orderId);
        orderConsignee.setReceiveId(OrderPublic.getUUID());
        orderConsignee.setReceiveName(storeInfo.getContacts());
        orderConsignee.setReceiveAddress(storeInfo.getAddress());
        orderConsignee.setReceivePhone(storeInfo.getContactsPhone());
        orderConsignee.setBillStatus(erpOrderSaveRequest.getBillStatus());
        erpOrderConsigneeService.saveOrderConsignee(orderConsignee, auth);

        return orderId;
    }

}
