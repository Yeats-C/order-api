package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.CartOrderInfo;
import com.aiqin.mgs.order.api.domain.ProductInfo;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.constant.ErpOrderCodeSequence;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderProductItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private ErpOrderFeeService erpOrderFeeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo erpSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, true);

        ErpOrderTypeCategoryControlEnum controlEnum = ErpOrderTypeCategoryControlEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (controlEnum == null || !controlEnum.isErpCartCreate()) {
            throw new BusinessException("不支持创建该类型和类别的订单");
        }
        return saveOrder(erpOrderSaveRequest, auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo storeSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, false);

        ErpOrderTypeCategoryControlEnum controlEnum = ErpOrderTypeCategoryControlEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (controlEnum == null || !controlEnum.isStoreCartCreate()) {
            throw new BusinessException("不支持创建该类型和类别的订单");
        }
        return saveOrder(erpOrderSaveRequest, auth);
    }

    /**
     * 从购物车创建订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/26 9:49
     */
    private ErpOrderInfo saveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //获取购物车商品
        OrderConfirmResponse storeCartProduct = getStoreCartProduct(erpOrderSaveRequest.getStoreId());
        //构建订单商品明细行
        List<ErpOrderItem> erpOrderItemList = generateOrderItemList(storeCartProduct.getCartOrderInfos(), storeInfo);

        //获取订单节点进程控制枚举
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (processTypeEnum == null) {
            throw new BusinessException("不允许创建类型为" + ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()) + "，类别为" + ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()) + "的订单");
        }
        //数据校验
        productCheck(processTypeEnum, storeInfo, erpOrderItemList);
        //生成订单主体信息
        ErpOrderInfo order = generateOrder(erpOrderItemList, storeInfo, erpOrderSaveRequest, auth);
        //计算均摊金额
        sharePrice(processTypeEnum, order);
        //保存订单、订单明细、订单支付、订单收货人信息、订单日志
        String orderId = insertOrder(order, storeInfo, auth, erpOrderSaveRequest);
        //删除购物车商品
        deleteOrderProductFromCart(erpOrderSaveRequest.getStoreId(), storeCartProduct);

        //锁库存
        if (processTypeEnum.isLockStock()) {
            boolean flag = erpOrderRequestService.lockStockInSupplyChain(order, erpOrderItemList, auth);
            if (!flag) {
                throw new BusinessException("锁库存失败");
            }
        }

        //返回订单信息
        return erpOrderQueryService.getOrderByOrderId(orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, true);
        if (!ErpOrderTypeEnum.ASSIST_PURCHASING.getCode().equals(erpOrderSaveRequest.getOrderType())) {
            throw new BusinessException("只能创建" + ErpOrderTypeEnum.ASSIST_PURCHASING.getDesc() + "的订单");
        }

        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //商品参数信息
        List<ErpOrderProductItemRequest> itemList = erpOrderSaveRequest.getItemList();

        //构建货架订单商品信息行
        List<ErpOrderItem> orderItemList = generateRackOrderItemList(itemList, storeInfo);

        //获取订单节点进程控制枚举
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (processTypeEnum == null) {
            throw new BusinessException("不允许创建类型为" + ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()) + "，类别为" + ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()) + "的订单");
        }
        //数据校验
        productCheck(processTypeEnum, storeInfo, orderItemList);

        //构建和保存货架订单，返回订单编号
        String orderCode = generateAndSaveRackOrder(erpOrderSaveRequest, orderItemList, storeInfo, auth);

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);

        if (processTypeEnum.isLockStock()) {
            //锁库
            boolean flag = erpOrderRequestService.lockStockInSupplyChain(order, orderItemList, auth);
            if (!flag) {
                throw new BusinessException("锁库存失败");
            }
        }

        //更新门店营业状态
        erpOrderRequestService.updateStoreBusinessStage(storeInfo.getStoreId(), OrderConstant.OPEN_STATUS_CODE_17, OrderConstant.OPEN_STATUS_CODE_18);

        //返回订单
        return order;
    }

    /**
     * 校验保存订单参数
     *
     * @param erpOrderSaveRequest
     * @param erpOrder            erp的订单
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 9:40
     */
    private void validateSaveOrderRequest(ErpOrderSaveRequest erpOrderSaveRequest, boolean erpOrder) {
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
        if (ErpOrderTypeEnum.DIRECT_SEND.getCode().equals(erpOrderSaveRequest.getOrderType()) && erpOrder) {
            erpOrderSaveRequest.setOrderCategory(ErpOrderCategoryEnum.ORDER_TYPE_1.getCode());
        } else {
            if (erpOrderSaveRequest.getOrderCategory() == null) {
                throw new BusinessException("请传入订单类别");
            } else {
                if (!ErpOrderCategoryEnum.exist(erpOrderSaveRequest.getOrderCategory())) {
                    throw new BusinessException("无效的订单类别");
                }
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

        //TODO 调用查询购物车接口
        CartOrderInfo cartOrderInfoQuery = new CartOrderInfo();
        cartOrderInfoQuery.setStoreId(storeId);
        cartOrderInfoQuery.setLineCheckStatus(YesOrNoEnum.YES.getCode());
        HttpResponse listHttpResponse = cartOrderService.displayCartLineCheckProduct(cartOrderInfoQuery);
        if (!RequestReturnUtil.validateHttpResponse(listHttpResponse)) {
            throw new BusinessException("获取购物车商品失败");
        }
        OrderConfirmResponse data = (OrderConfirmResponse) listHttpResponse.getData();
        if (data == null || data.getCartOrderInfos() == null || data.getCartOrderInfos().size() == 0) {
            throw new BusinessException("购物车没有勾选的商品");
        }

//        OrderConfirmResponse data = new OrderConfirmResponse();
//        List<CartOrderInfo> list = new ArrayList<>();
//        CartOrderInfo info1 = new CartOrderInfo();
//        info1.setProductId("1000001");
//        info1.setSpuId("1000001");
//        info1.setProductName("奶粉");
//        info1.setSkuCode("9001");
//        info1.setPrice(new BigDecimal(5));
//        info1.setAmount(11);
//        list.add(info1);
//
//        CartOrderInfo info2 = new CartOrderInfo();
//        info2.setProductId("1000002");
//        info2.setSpuId("1000002");
//        info2.setProductName("玩具");
//        info2.setSkuCode("9002");
//        info2.setPrice(new BigDecimal(10));
//        info2.setAmount(22);
//        list.add(info2);
//
//        CartOrderInfo info3 = new CartOrderInfo();
//        info3.setProductId("1000003");
//        info3.setSpuId("1000003");
//        info3.setProductName("衣服");
//        info3.setSkuCode("9003");
//        info3.setPrice(new BigDecimal(15));
//        info3.setAmount(8);
//        list.add(info3);
//        data.setCartOrderInfos(list);

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
            ProductInfo product = erpOrderRequestService.getSkuDetail(storeInfo.getCompanyCode(), item.getSkuCode());
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
            //spu编码
            orderItem.setSpuCode(productInfo.getSpuCode());
            //spu名称
            orderItem.setSpuName(productInfo.getSpuName());
            //sku编码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //sku名称
            orderItem.setSkuName(productInfo.getSkuName());
            //条形码
            orderItem.setBarCode(productInfo.getBarCode());
            //图片地址
            orderItem.setPictureUrl(productInfo.getPictureUrl());
            //规格
            orderItem.setProductSpec(productInfo.getProductSpec());
            //颜色编码
            orderItem.setColorCode(productInfo.getColorCode());
            //颜色名称
            orderItem.setColorName(productInfo.getColorName());
            //型号
            orderItem.setModelCode(productInfo.getModelCode());
            //单位编码
            orderItem.setUnitCode(productInfo.getUnitCode());
            //单位名称
            orderItem.setUnitName(productInfo.getUnitName());
            //折零系数 不填
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(ErpProductGiftEnum.PRODUCT.getCode());
            //商品属性编码
            orderItem.setProductPropertyCode(productInfo.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(productInfo.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(productInfo.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(productInfo.getSupplierName());
            //商品数量
            orderItem.setProductCount((long) item.getAmount());
            //商品单价
            orderItem.setProductAmount(item.getPrice());
            //商品总价
            orderItem.setTotalProductAmount(item.getPrice().multiply(new BigDecimal(item.getAmount())));
            //实际商品总价（发货商品总价）
            orderItem.setActualTotalProductAmount(null);
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(orderItem.getTotalProductAmount());
            //分摊后单价
            orderItem.setPreferentialAmount(orderItem.getProductAmount());
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(BigDecimal.ZERO);
            //实发商品数量
            orderItem.setActualProductCount(null);
            //税率
            orderItem.setTaxRate(productInfo.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(storeInfo.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(storeInfo.getCompanyName());
            //单个商品毛重(kg)
            orderItem.setBoxGrossWeight(productInfo.getBoxGrossWeight());
            //单个商品包装体积(mm³)
            orderItem.setBoxVolume(productInfo.getBoxVolume());

            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    /**
     * 商品校验
     *
     * @param processTypeEnum      订单节点进程控制枚举
     * @param storeInfo            门店信息
     * @param orderProductItemList 订单商品明细行
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:03
     */
    private void productCheck(ErpOrderNodeProcessTypeEnum processTypeEnum, StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {

        if (processTypeEnum.isActivityCheck()) {
            //活动校验
            boolean flag = erpOrderRequestService.activityCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("活动校验失败");
            }
        }
        if (processTypeEnum.isAreaCheck()) {
            //商品销售区域配置校验
            boolean flag = erpOrderRequestService.areaCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品销售区域配置校验失败");
            }
        }
        if (processTypeEnum.isPriceCheck()) {
            //商品销售价格校验
            boolean flag = erpOrderRequestService.priceCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品销售价格校验失败");
            }
        }
        if (processTypeEnum.isRepertoryCheck()) {
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
        //商品毛重(kg)
        BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
        //商品包装体积(mm³)
        BigDecimal boxVolumeTotal = BigDecimal.ZERO;

        for (ErpOrderItem item :
                orderItemList) {

            //订货金额汇总
            moneyTotal = moneyTotal.add(item.getTotalProductAmount() == null ? BigDecimal.ZERO : item.getTotalProductAmount());
            //活动优惠金额汇总
            activityMoneyTotal = activityMoneyTotal.add(item.getTotalAcivityAmount() == null ? BigDecimal.ZERO : item.getTotalAcivityAmount());
            //商品毛重汇总
            boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(item.getProductCount())));
            //商品体积汇总
            boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(item.getProductCount())));

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
        ErpOrderInfo order = new ErpOrderInfo();
        order.setOrderFee(orderFee);
        order.setItemList(orderItemList);
        //公司编码
        order.setCompanyCode(storeInfo.getCompanyCode());
        //公司名称
        order.setCompanyName(storeInfo.getCompanyName());
        //订单类型编码 0直送、1配送、2辅采直送
        order.setOrderTypeCode(erpOrderSaveRequest.getOrderType().toString());
        //订单类型名称
        order.setOrderTypeName(ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()));
        //订单类别编码
        order.setOrderCategoryCode(erpOrderSaveRequest.getOrderCategory().toString());
        //订单类别名称
        order.setOrderCategoryName(ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()));
        //供应商编码
        order.setSupplierCode(null);
        //供应商名称
        order.setSupplierName(null);
        //仓库编码
        order.setTransportCenterCode(null);
        //仓库名称
        order.setTransportCenterName(null);
        //库房编码
        order.setWarehouseCode(null);
        //库房名称
        order.setWarehouseName(null);
        //客户编码 存门店编码
        order.setCustomerCode(storeInfo.getStoreCode());
        //客户名称 存门店名称
        order.setCustomerName(storeInfo.getStoreName());
        //订单状态
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        //订单流程节点状态
        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        //是否锁定(0是1否）
        order.setOrderLock(StatusEnum.NO.getCode());
        //锁定原因
        order.setLockReason(null);
        //是否是异常订单(0是1否)
        order.setOrderException(StatusEnum.NO.getCode());
        //是否删除(0是1否)
        order.setOrderDelete(StatusEnum.NO.getCode());
        //支付状态0已支付  1未支付
        order.setPaymentStatus(StatusEnum.NO.getCode());

        //收货区域 :省编码
        order.setProvinceId(storeInfo.getProvinceId());
        //收货区域 :省
        order.setProvinceName(storeInfo.getProvinceName());
        //收货区域 :市编码
        order.setCityId(storeInfo.getCityId());
        //收货区域 :市
        order.setCityName(storeInfo.getCityName());
        //收货区域 :区县编码
        order.setDistrictId(storeInfo.getDistrictId());
        //收货区域 :区县
        order.setDistrictName(storeInfo.getDistrictName());

        //收货地址
        order.setReceiveAddress(storeInfo.getAddress());
        //配送方式编码
        order.setDistributionModeCode(null);
        //配送方式名称
        order.setDistributionModeName(null);
        //收货人
        order.setReceivePerson(storeInfo.getContacts());
        //收货人电话
        order.setReceiveMobile(storeInfo.getContactsPhone());
        //邮编
        order.setZipCode(null);
        //支付方式编码
        order.setPaymentCode(null);
        //支付方式名称
        order.setPaymentName(null);
        //运费
        order.setDeliverAmount(null);
        //商品总价
        order.setTotalProductAmount(moneyTotal);
        //实际商品总价(发货商品总价)
        order.setActualTotalProductAmount(null);
        //实际发货数量
        order.setActualProductCount(null);
        //优惠额度
        order.setDiscountAmount(BigDecimal.ZERO);
        //实际支付金额 没有活动优惠券的情况下等于总价
        order.setOrderAmount(moneyTotal);
        //付款日期
        order.setPaymentTime(null);
        //发货时间
        order.setDeliveryTime(null);
        //发运时间
        order.setTransportTime(null);
        //发运状态
        order.setTransportStatus(StatusEnum.NO.getCode());
        //签收时间
        order.setReceiveTime(null);
        //发票类型 默认不开发票
        order.setInvoiceType(ErpInvoiceTypeEnum.NO_INVOICE.getCode());
        //发票抬头
        order.setInvoiceTitle(null);
        //体积
        order.setTotalVolume(boxVolumeTotal);
        //实际体积
        order.setActualTotalVolume(null);
        //重量
        order.setTotalWeight(boxGrossWeightTotal);
        //实际重量
        order.setActualTotalWeight(null);
        //主订单号  如果非子订单 此处存order_code
        order.setMainOrderCode(null);
        //订单级别(0主1子订单)
        order.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 (0是 1否)
        order.setSplitStatus(StatusEnum.NO.getCode());
        //申请取消时的状态
        order.setBeforeCancelStatus(null);
        //备注
        order.setRemake(null);
        //门店类型
        order.setStoreType(null);
        //门店id
        order.setStoreId(storeInfo.getStoreId());
        //门店编码
        order.setStoreCode(storeInfo.getStoreCode());
        //门店名称
        order.setStoreName(storeInfo.getStoreName());
        //运输公司编码
        order.setTransportCenterCode(null);
        //运输公司名称
        order.setTransportCenterName(null);
        //运输单号
        order.setTransportCode(null);
        //物流id
        order.setLogisticsId(null);
        //费用id
        order.setFeeId(null);
        //是否发生退货  0 是  1.否
        order.setOrderReturn(StatusEnum.NO.getCode());
        //加盟商id
        order.setFranchiseeId(storeInfo.getFranchiseeId());
        //加盟商编码
        order.setFranchiseeCode(storeInfo.getFranchiseeCode());
        //加盟商名称
        order.setFranchiseeName(storeInfo.getFranchiseeName());
        //来源单号
        order.setSourceCode(null);
        //来源名称
        order.setSourceName(null);
        //来源类型
        order.setSourceType(null);
        //同步供应链状态
        order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
        //生成冲减单状态
        order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.NOT_NEED.getCode());

        return order;
    }

    /**
     * 计算均摊金额
     *
     * @param processTypeEnum
     * @param erpOrderInfo
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 15:35
     */
    private void sharePrice(ErpOrderNodeProcessTypeEnum processTypeEnum, ErpOrderInfo erpOrderInfo) {
        //TODO 计算均摊金额
        for (ErpOrderItem item :
                erpOrderInfo.getItemList()) {
            item.setTotalPreferentialAmount(item.getTotalProductAmount());
            item.setPreferentialAmount(item.getProductAmount());
        }
    }

    /**
     * //保存订单、订单明细、订单支付、订单收货人信息、订单日志
     *
     * @param order               订单信息
     * @param storeInfo           门店信息
     * @param auth                操作人信息
     * @param erpOrderSaveRequest 请求参数
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 15:34
     */
    private String insertOrder(ErpOrderInfo order, StoreInfo storeInfo, AuthToken auth, ErpOrderSaveRequest erpOrderSaveRequest) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成费用id
        String feeId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = getOrderCode();
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;
        long lineCode = 1;
        for (ErpOrderItem item :
                order.getItemList()) {
            //订单id
            item.setOrderStoreId(orderId);
            //订单编码
            item.setOrderStoreCode(orderCode);
            //订单明细id
            item.setOrderInfoDetailId(OrderPublic.getUUID());
            //订单明细行编号
            item.setLineCode(lineCode++);
            item.setUseStatus(StatusEnum.YES.getValue());
        }
        //保存订单
        order.setOrderStoreId(orderId);
        order.setOrderStoreCode(orderCode);
        order.setMainOrderCode(orderCode);
        order.setFeeId(feeId);
        erpOrderInfoService.saveOrder(order, auth);

        //保存订单明细行
        erpOrderItemService.saveOrderItemList(order.getItemList(), auth);

        ErpOrderFee orderFee = order.getOrderFee();
        orderFee.setOrderId(orderId);
        orderFee.setPayId(null);
        orderFee.setFeeId(feeId);
        orderFee.setPayStatus(payStatusEnum.getCode());
        erpOrderFeeService.saveOrderFee(orderFee, auth);

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
            cartOrderService.deleteCartInfo(storeId, item.getSkuCode(), YesOrNoEnum.YES.getCode(),item.getProductType());
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
    private List<ErpOrderItem> generateRackOrderItemList(List<ErpOrderProductItemRequest> paramItemList, StoreInfo storeInfo) {

        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少商品数据");
        }

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //参数商品行标识
        int lineIndex = 0;

        //遍历参数商品列表，获取商品详情，校验数据
        for (ErpOrderProductItemRequest item :
                paramItemList) {
            lineIndex++;
            if (StringUtils.isEmpty(item.getSpuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品spu编码为空");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品sku编码为空");
            }
            if (item.getTaxPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品采购价为空");
            }
            if (item.getPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品销售价为空");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("第" + lineIndex + "行商品数量为空");
            }

            //获取商品详情
            ProductInfo product = erpOrderRequestService.getSkuDetail(storeInfo.getCompanyCode(), item.getSkuCode());
            if (product == null) {
                throw new BusinessException("第" + lineIndex + "行商品不存在");
            }
            productMap.put(item.getSkuCode(), product);
        }

        //订单商品明细行
        List<ErpOrderItem> orderItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (ErpOrderProductItemRequest item :
                paramItemList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode());

            ErpOrderItem orderItem = new ErpOrderItem();
            //spu编码
            orderItem.setSpuCode(productInfo.getSpuCode());
            //spu名称
            orderItem.setSpuName(productInfo.getSpuName());
            //sku编码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //sku名称
            orderItem.setSkuName(productInfo.getSkuName());
            //条形码
            orderItem.setBarCode(productInfo.getBarCode());
            //图片地址
            orderItem.setPictureUrl(productInfo.getPictureUrl());
            //规格
            orderItem.setProductSpec(productInfo.getProductSpec());
            //颜色编码
            orderItem.setColorCode(productInfo.getColorCode());
            //颜色名称
            orderItem.setColorName(productInfo.getColorName());
            //型号
            orderItem.setModelCode(productInfo.getModelCode());
            //单位编码
            orderItem.setUnitCode(productInfo.getUnitCode());
            //单位名称
            orderItem.setUnitName(productInfo.getUnitName());
            //折零系数 不存
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(ErpProductGiftEnum.PRODUCT.getCode());
            //商品属性编码
            orderItem.setProductPropertyCode(productInfo.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(productInfo.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(productInfo.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(productInfo.getSupplierName());
            //商品数量
            orderItem.setProductCount(Long.valueOf(item.getQuantity()));
            //商品单价
            orderItem.setProductAmount(item.getPrice());
            //商品含税采购价
            orderItem.setPurchaseAmount(item.getTaxPrice());
            //商品总价
            orderItem.setTotalProductAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            //实际商品总价 发货商品价格
            orderItem.setActualTotalProductAmount(null);
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(orderItem.getTotalProductAmount());
            //分摊后单价
            orderItem.setPreferentialAmount(orderItem.getProductAmount());
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(BigDecimal.ZERO);
            //实发商品数量
            orderItem.setActualProductCount(null);
            //税率
            orderItem.setTaxRate(productInfo.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(storeInfo.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(storeInfo.getCompanyName());
            //单个商品毛重(kg)
            orderItem.setBoxGrossWeight(productInfo.getBoxGrossWeight());
            //单个商品包装体积(mm³)
            orderItem.setBoxVolume(productInfo.getBoxVolume());

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
        //生成订单code
        String orderCode = getOrderCode();
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;

        //订货金额汇总
        BigDecimal moneyTotal = BigDecimal.ZERO;
        //商品毛重(kg)
        BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
        //商品包装体积(mm³)
        BigDecimal boxVolumeTotal = BigDecimal.ZERO;

        long lineCode = 1L;

        //遍历商品订单行，汇总价格
        for (ErpOrderItem item :
                orderItemList) {

            //订单id
            item.setOrderStoreId(orderId);
            //订单编码
            item.setOrderStoreCode(orderCode);
            //订单明细id
            item.setOrderInfoDetailId(OrderPublic.getUUID());
            //订单明细行编号
            item.setLineCode(lineCode++);
            item.setUseStatus(StatusEnum.YES.getValue());

            //金额汇总
            moneyTotal = moneyTotal.add(item.getTotalProductAmount() == null ? BigDecimal.ZERO : item.getTotalProductAmount());
            //商品毛重汇总
            boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(item.getProductCount())));
            //商品体积汇总
            boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(item.getProductCount())));
        }

        erpOrderItemService.saveOrderItemList(orderItemList, auth);

        ErpOrderInfo order = new ErpOrderInfo();
        //业务id
        order.setOrderStoreId(orderId);
        //订单编码
        order.setOrderStoreCode(orderCode);
        //公司编码
        order.setCompanyCode(storeInfo.getCompanyCode());
        //公司名称
        order.setCompanyName(storeInfo.getCompanyName());
        //订单类型编码 0直送、1配送、2辅采直送
        order.setOrderTypeCode(erpOrderSaveRequest.getOrderType().toString());
        //订单类型名称
        order.setOrderTypeName(ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()));
        //订单类别编码
        order.setOrderCategoryCode(erpOrderSaveRequest.getOrderCategory().toString());
        //订单类别名称
        order.setOrderCategoryName(ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()));
        //供应商编码
        order.setSupplierCode(null);
        //供应商名称
        order.setSupplierName(null);
        //仓库编码
        order.setTransportCenterCode(null);
        //仓库名称
        order.setTransportCenterName(null);
        //库房编码
        order.setWarehouseCode(null);
        //库房名称
        order.setWarehouseName(null);
        //客户编码
        order.setCustomerCode(storeInfo.getStoreCode());
        //客户名称
        order.setCustomerName(storeInfo.getStoreName());
        //订单状态
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        //订单节点流程状态
        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        //是否锁定(0是1否）
        order.setOrderLock(StatusEnum.NO.getCode());
        //锁定原因
        order.setLockReason(null);
        //是否是异常订单(0是1否)
        order.setOrderException(StatusEnum.NO.getCode());
        //是否删除(0是1否)
        order.setOrderDelete(null);
        //支付状态0已支付  1未支付
        order.setPaymentStatus(StatusEnum.NO.getCode());

        //收货区域 :省编码
        order.setProvinceId(storeInfo.getProvinceId());
        //收货区域 :省
        order.setProvinceName(storeInfo.getProvinceName());
        //收货区域 :市编码
        order.setCityId(storeInfo.getCityId());
        //收货区域 :市
        order.setCityName(storeInfo.getCityName());
        //收货区域 :区县编码
        order.setDistrictId(storeInfo.getDistrictId());
        //收货区域 :区县
        order.setDistrictName(storeInfo.getDistrictName());

        //收货地址
        order.setReceiveAddress(storeInfo.getAddress());
        //配送方式编码
        order.setDistributionModeCode(null);
        //配送方式名称
        order.setDistributionModeName(null);
        //收货人
        order.setReceivePerson(storeInfo.getContacts());
        //收货人电话
        order.setReceiveMobile(storeInfo.getContactsPhone());
        //邮编
        order.setZipCode(null);
        //支付方式编码
        order.setPaymentCode(null);
        //支付方式名称
        order.setPaymentName(null);
        //运费
        order.setDeliverAmount(null);
        //商品总价
        order.setTotalProductAmount(moneyTotal);
        //实际商品总价
        order.setActualTotalProductAmount(moneyTotal);
        //实际发货数量
        order.setActualProductCount(null);
        //优惠额度
        order.setDiscountAmount(BigDecimal.ZERO);
        //实付金额
        order.setOrderAmount(null);
        //付款日期
        order.setPaymentTime(null);
        //发货时间
        order.setDeliveryTime(null);
        //发运时间
        order.setTransportTime(null);
        //发运状态
        order.setTransportStatus(StatusEnum.NO.getCode());
        //签收时间
        order.setReceiveTime(null);
        //发票类型
        order.setInvoiceType(ErpInvoiceTypeEnum.NO_INVOICE.getCode());
        //发票抬头
        order.setInvoiceTitle(null);
        //体积
        order.setTotalVolume(boxVolumeTotal);
        //实际体积
        order.setActualTotalVolume(null);
        //重量
        order.setTotalWeight(boxGrossWeightTotal);
        //实际重量
        order.setActualTotalWeight(null);
        //主订单号  如果非子订单 此处存order_code
        order.setMainOrderCode(orderCode);
        //订单级别(0主1子订单)
        order.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 (0是 1否)
        order.setSplitStatus(StatusEnum.NO.getCode());
        //申请取消时的状态
        order.setBeforeCancelStatus(null);
        //备注
        order.setRemake(null);
        //门店类型 类型不对，应该是字符串
        order.setStoreType(null);
        //门店id
        order.setStoreId(storeInfo.getStoreId());
        //门店编码
        order.setStoreCode(storeInfo.getStoreCode());
        //门店名称
        order.setStoreName(storeInfo.getStoreName());
        //运输公司编码
        order.setTransportCenterCode(null);
        //运输公司名称
        order.setTransportCenterName(null);
        //运输单号
        order.setTransportCode(null);
        //物流id
        order.setLogisticsId(null);
        //费用id
        order.setFeeId(feeId);
        //是否发生退货  0 是  1.否
        order.setOrderReturn(StatusEnum.NO.getCode());
        //加盟商id
        order.setFranchiseeId(storeInfo.getFranchiseeId());
        //加盟商编码
        order.setFranchiseeCode(storeInfo.getFranchiseeCode());
        //加盟商名称
        order.setFranchiseeName(storeInfo.getFranchiseeName());
        //来源单号是什么
        order.setSourceCode(null);
        //来源名称
        order.setSourceName(null);
        //来源类型
        order.setSourceType(null);
        //同步供应链状态
        order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
        //生成冲减单状态
        order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.NOT_NEED.getCode());
        erpOrderInfoService.saveOrder(order, auth);

        //保存订单费用信息
        ErpOrderFee orderFee = new ErpOrderFee();
        //费用id
        orderFee.setFeeId(feeId);
        //订单id
        orderFee.setOrderId(orderId);
        //支付单id
        orderFee.setPayId(null);
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

        return orderCode;
    }

    @Override
    public String getOrderCode() {
        ErpOrderCodeSequence.num = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String curDay = sdf.format(new Date());
        if (StringUtils.isEmpty(ErpOrderCodeSequence.currentDay) || !curDay.equals(ErpOrderCodeSequence.currentDay) || ErpOrderCodeSequence.num == null) {
            //如果是
            //查询数据库，获取最新序列
            String maxOrderCode = erpOrderQueryService.getMaxOrderCodeByCurrentDay(curDay);
            if (StringUtils.isNotEmpty(maxOrderCode)) {
                ErpOrderCodeSequence.num = Long.valueOf(maxOrderCode.substring(maxOrderCode.length() - 6));
            } else {
                ErpOrderCodeSequence.num = 0L;
            }
            ErpOrderCodeSequence.currentDay = curDay;
        }
        return curDay + String.format("%06d", ++ErpOrderCodeSequence.num);
    }

}
