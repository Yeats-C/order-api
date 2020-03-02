package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.ErpProductPropertyTypeEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityRuleUnitEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityTypeEnum;
import com.aiqin.mgs.order.api.dao.CartOrderDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityParameterRequest;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.cart.Product;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.cart.StoreActivityAchieveRequest;
import com.aiqin.mgs.order.api.domain.response.cart.CartResponse;
import com.aiqin.mgs.order.api.domain.response.cart.StoreActivityAchieveResponse;
import com.aiqin.mgs.order.api.domain.response.cart.StoreCartProductResponse;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class CartOrderServiceImpl implements CartOrderService {


    private static final Logger LOGGER = LoggerFactory.getLogger(CartServiceImpl.class);

    @Resource
    private CartOrderDao cartOrderDao;

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private ErpOrderRequestService erpOrderRequestService;

    @Resource
    private ActivityService activityService;

    /**
     * 将商品加入购物车
     *
     * @param shoppingCartRequest
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse addCart(ShoppingCartRequest shoppingCartRequest, AuthToken authToken) {
        List<String> skuCodeList = new ArrayList();
        //入参校验
        checkParam(shoppingCartRequest);

        //记录各个sku附带的活动
        Map<String, String> skuActivityMap = new HashMap<>(16);

        //商品数量不能大于999
        List<Product> products = shoppingCartRequest.getProducts();
        for (Product product : products) {
            if (product.getAmount() > 999) {
                return HttpResponse.failure(ResultCode.OVER_LIMIT);
            }
            skuCodeList.add(product.getSkuId());
            skuActivityMap.put(product.getSkuId(), product.getActivityId());
        }
        //通过门店id返回门店省市公司信息
        HttpResponse<StoreInfo> storeInfo = bridgeProductService.getStoreInfo(shoppingCartRequest);
        if (storeInfo == null || storeInfo.getData() == null) {
            return HttpResponse.failure(ResultCode.NO_HAVE_STORE_ERROR);
        }
        ShoppingCartProductRequest shoppingCartProductRequest = new ShoppingCartProductRequest();
        shoppingCartProductRequest.setCityCode(storeInfo.getData().getCityId());
        shoppingCartProductRequest.setProvinceCode(storeInfo.getData().getProvinceId());
        shoppingCartProductRequest.setCompanyCode("14");
        shoppingCartProductRequest.setSkuCodes(skuCodeList);
        if (shoppingCartProductRequest.getCompanyCode() == null
                || shoppingCartProductRequest.getCityCode() == null
                || shoppingCartProductRequest.getProvinceCode() == null
                || shoppingCartProductRequest.getSkuCodes() == null) {
            return HttpResponse.failure(ResultCode.PARAMETER_EXCEPTION);
        }
        //通过省市公司ID、skuid调用商品模块，返回商品信息
        HttpResponse<List<CartOrderInfo>> productInfo = bridgeProductService.getProduct(shoppingCartProductRequest);
        List<CartOrderInfo> cartOrderInfoList = productInfo.getData();

        for (CartOrderInfo cartOrderInfo1 : cartOrderInfoList) {
            //获取库房的商品数量和sku码
            int stockProductAmount = cartOrderInfo1.getStockNum();
            String skuId = cartOrderInfo1.getSkuCode();
            LOGGER.info("库存的skuId编码和数量：{},{}", skuId, stockProductAmount);
            //获取前端的商品数量，与库房数量进行比对
            for (Product product : products) {
                if (skuId != null && skuId.equals(product.getSkuId()) && stockProductAmount < product.getAmount()) {
                    return HttpResponse.failure(ResultCode.STORE_SHORT);
                }
                if (stockProductAmount < 10) {
                    return HttpResponse.failure(ResultCode.STOCK_SHORT1);
                }
                if (skuId.equals(product.getSkuId())) {
                    CartOrderInfo cartOrderInfo = new CartOrderInfo();
                    cartOrderInfo.setSkuCode(cartOrderInfo1.getSkuCode());//skuId
                    cartOrderInfo.setSpuId(shoppingCartRequest.getProductId());//spuId
                    cartOrderInfo.setProductId(cartOrderInfo1.getSkuCode());//商品Code
                    cartOrderInfo.setStoreId(cartOrderInfo1.getStoreId());//门店id
                    cartOrderInfo.setProductType(shoppingCartRequest.getProductType());//商品类型
                    cartOrderInfo.setProductName(cartOrderInfo1.getSkuName());//商品名称
                    cartOrderInfo.setColor(cartOrderInfo1.getColorName());//商品颜色
                    cartOrderInfo.setProductSize(cartOrderInfo1.getModelNumber());//商品型号
                    cartOrderInfo.setCreateSource(shoppingCartRequest.getCreateSource());//插入商品来源
                    cartOrderInfo.setAmount(product.getAmount());//获取商品数量
                    cartOrderInfo.setPrice(cartOrderInfo1.getPriceTax());//商品价格
                    cartOrderInfo.setProductType(shoppingCartRequest.getProductType());//商品类型 0直送、1配送、2辅采
                    cartOrderInfo.setStoreId(shoppingCartRequest.getStoreId());//门店ID
                    cartOrderInfo.setCreateById(authToken.getPersonId());//创建者id
                    cartOrderInfo.setCreateByName(authToken.getPersonName());//创建者名称
                    cartOrderInfo.setStockNum(cartOrderInfo1.getStockNum());//库存数量
                    cartOrderInfo.setZeroRemovalCoefficient(cartOrderInfo1.getZeroRemovalCoefficient());//交易倍数
                    cartOrderInfo.setSpec(cartOrderInfo1.getSpec());//规格
                    cartOrderInfo.setProductPropertyCode(cartOrderInfo1.getProductPropertyCode());//商品属性码
                    cartOrderInfo.setProductPropertyName(cartOrderInfo1.getProductPropertyName());//商品属性名称、
                    cartOrderInfo.setLineCheckStatus(1);//选中状态
                    cartOrderInfo.setProductBrandCode(cartOrderInfo1.getProductBrandCode());//品牌编码
                    cartOrderInfo.setProductBrandName(cartOrderInfo1.getProductBrandName());//品牌名称
                    cartOrderInfo.setProductCategoryCode(cartOrderInfo1.getProductCategoryCode());//品类编码
                    cartOrderInfo.setProductCategoryName(cartOrderInfo1.getProductCategoryName());//品类编码
                    cartOrderInfo.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());
                    cartOrderInfo.setActivityId(skuActivityMap.containsKey(cartOrderInfo1.getSkuCode()) ? skuActivityMap.get(cartOrderInfo1.getSkuCode()) : null);
                    try {
                        if (cartOrderInfo != null) {
                            //判断sku是否在购物车里面存在
                            LOGGER.info("判断SKU商品是否已存在购物车中:{}", cartOrderInfo.getSkuCode());
                            String oldAount = cartOrderDao.isYesCart(cartOrderInfo);
                            if (oldAount != null && !"".equals(oldAount)) {
                                //已存在购物车的、新添加+已存在购物车的数量=真实数量
                                LOGGER.info("更新购物车:{}", cartOrderInfo);
                                int newAount = Integer.valueOf(oldAount) + cartOrderInfo.getAmount();
                                cartOrderInfo.setAmount(newAount);
                                //更新购物车
                                cartOrderDao.updateCartById(cartOrderInfo);
                            } else {
                                if (cartOrderInfo.getCartId() != null) {
                                    cartOrderDao.insertCart(cartOrderInfo);
                                } else {
                                    //生成购物车id
                                    String cartId = IdUtil.uuid();
                                    cartOrderInfo.setCartId(cartId);
                                    //生成商品加入购物车的时间
                                    cartOrderInfo.setCreateTime(new Date());
                                    //添加购物车
                                    LOGGER.info("添加购物车:{}", cartOrderInfo);
                                    cartOrderDao.insertCart(cartOrderInfo);
                                }
                            }

                        } else {
                            LOGGER.warn("购物车信息为空!");
                            return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
                        }

                    } catch (Exception e) {
                        LOGGER.error("添加购物车异常：{}", e);
                        return HttpResponse.failure(ResultCode.ADD_EXCEPTION);
                    }
                }
            }

        }
        return HttpResponse.success();
    }

    private void checkParam(ShoppingCartRequest shoppingCartRequest) {
        if (shoppingCartRequest == null) {
            throw new BusinessException("参数为空");
        }
        if (shoppingCartRequest.getProducts() == null) {
            throw new BusinessException("商品不能为空");
        }
        if (shoppingCartRequest.getProductId() == null) {
            throw new BusinessException("productId为空");
        }
        if (shoppingCartRequest.getStoreId() == null) {
            throw new BusinessException("门店id为空");
        }
        if (shoppingCartRequest.getProductId() == null) {
            throw new BusinessException("商品类型为空");
        }
    }

    /**
     * 根据门店ID显示购物车中商品信息,如果勾选商品，就更新标记和数量
     *
     * @param storeId
     * @param productType
     * @return
     */
    @Override
    public HttpResponse<CartResponse> selectCartByStoreId(String storeId, Integer productType, String skuId, Integer lineCheckStatus, Integer number) {
        HttpResponse<CartResponse> response = HttpResponse.success();
        try {
            CartOrderInfo cartOrderInfo = new CartOrderInfo();
            cartOrderInfo.setStoreId(storeId);
            cartOrderInfo.setProductType(productType);
            cartOrderInfo.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());
            //默认标志为0
            lineCheckStatus = lineCheckStatus == null ? 0 : lineCheckStatus;
            //检查商品是否被勾选，勾选后，更新数据库标识
            LOGGER.info("商品勾选后，更新勾选状态：{}", lineCheckStatus);
            if (null != skuId && lineCheckStatus.equals(Global.LINECHECKSTATUS_1) && null != productType) {
                cartOrderInfo.setSkuCode(skuId);
                cartOrderInfo.setLineCheckStatus(lineCheckStatus);
                cartOrderInfo.setStoreId(storeId);
                cartOrderInfo.setProductType(productType);
                if (null != number) {
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);

            } else if (null != skuId && lineCheckStatus.equals(Global.LINECHECKSTATUS_0) && null != productType) {
                //如果是取消勾选，通过门店id所有标记
                cartOrderInfo.setSkuCode(skuId);
                cartOrderInfo.setLineCheckStatus(lineCheckStatus);
                cartOrderInfo.setProductType(productType);
                cartOrderInfo.setStoreId(storeId);
                if (null != number) {
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);

            } else if (null != storeId && lineCheckStatus.equals(Global.LINECHECKSTATUS_2) && null != productType) {
                //如果是全选，通过门店id更新所有标记
                cartOrderInfo.setStoreId(storeId);
                cartOrderInfo.setProductType(productType);
                cartOrderInfo.setLineCheckStatus(Global.LINECHECKSTATUS_1);
                if (null != number) {
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);
            } else if (null != storeId && lineCheckStatus.equals(Global.LINECHECKSTATUS_3) && null != productType) {
                //如果是全部取消，通过门店id更新所有标记
                cartOrderInfo.setStoreId(storeId);
                cartOrderInfo.setProductType(productType);
                cartOrderInfo.setLineCheckStatus(Global.LINECHECKSTATUS_0);
                if (null != number) {
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);

            } else {
            }

            //返回商品列表并结算价格
            CartResponse cartResponse = getProductList(cartOrderInfo);
            LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
            response.setData(cartResponse);
            return response;
        } catch (Exception e) {
            LOGGER.error("根据门店ID查询购物车数据异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse queryCartByStoreId(String storeId, Integer productType, String skuId, Integer lineCheckStatus, Integer number, String activityId) {
        HttpResponse<StoreCartProductResponse> response = HttpResponse.success();
        try {
            CartOrderInfo query = new CartOrderInfo();
            query.setStoreId(storeId);
            query.setProductType(productType);
            query.setSkuCode(skuId);
            query.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());

            List<CartOrderInfo> queryList = cartOrderDao.selectByProperty(query);
            if (queryList != null && queryList.size() > 0) {
                for (CartOrderInfo item :
                        queryList) {

                    if (Global.LINECHECKSTATUS_0.equals(lineCheckStatus) || Global.LINECHECKSTATUS_1.equals(lineCheckStatus)) {
                        //勾选这一行 或者 取消勾选这一行
                        if (StringUtils.isEmpty(skuId)) {
                            throw new BusinessException("参数缺失");
                        }
                        item.setLineCheckStatus(lineCheckStatus);
                        if (number != null) {
                            item.setAmount(number);
                        }
                        item.setActivityId(activityId);

                    } else if (Global.LINECHECKSTATUS_2.equals(lineCheckStatus)) {
                        //全选
                        item.setLineCheckStatus(Global.LINECHECKSTATUS_1);
                    } else if (Global.LINECHECKSTATUS_3.equals(lineCheckStatus)) {
                        //取消全选
                        item.setLineCheckStatus(Global.LINECHECKSTATUS_0);
                    } else {
                    }
                    //更新这一行
                    cartOrderDao.updateCartByCartId(item);

                }
            }

            //返回商品列表并结算价格
            StoreCartProductResponse cartResponse = getStoreCartProductGroup(storeId, productType, true);
            LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
            response.setData(cartResponse);
            return response;
        } catch (Exception e) {
            LOGGER.error("根据门店ID查询购物车数据异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    /**
     * 返回购物车中的商品列表，并计算出商品总价和总数量
     *
     * @param cartOrderInfo
     * @return
     * @throws Exception
     */
    private CartResponse getProductList(CartOrderInfo cartOrderInfo) throws Exception {
        CartOrderInfo query = new CartOrderInfo();
        query.setStoreId(cartOrderInfo.getStoreId());
        query.setProductType(cartOrderInfo.getProductType());
        query.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());

        //购物车数据
        List<CartOrderInfo> cartInfoList = cartOrderDao.selectCartByStoreId(query);

        //计算商品总价格
        BigDecimal acountActualprice = BigDecimal.ZERO;

        //计算商品总数量
        int totalNumber = 0;

        for (CartOrderInfo item : cartInfoList) {
            if (Global.LINECHECKSTATUS_1.equals(item.getLineCheckStatus())) {
                BigDecimal total = item.getPrice().multiply(new BigDecimal(item.getAmount()));
                acountActualprice = acountActualprice.add(total);
                totalNumber += item.getAmount();
            }
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartInfoList(cartInfoList);
        cartResponse.setAccountActualPrice(acountActualprice);
        cartResponse.setTotalNumber(totalNumber);
        return cartResponse;
    }

    /**
     * 获取购物车商品楼层信息
     *
     * @param storeId     门店id
     * @param productType 商品类型
     * @param cartRequest 是否是购物车请求，购物车请求的返回值包含未勾选的行
     * @return
     * @throws Exception
     */
    private StoreCartProductResponse getStoreCartProductGroup(String storeId, Integer productType, boolean cartRequest) throws Exception {
        CartOrderInfo query = new CartOrderInfo();
        query.setStoreId(storeId);
        query.setProductType(productType);
        query.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());
        if (!cartRequest) {
            query.setLineCheckStatus(Global.LINECHECKSTATUS_1);
        }

        //购物车数据-本品行
        List<CartOrderInfo> cartInfoList = cartOrderDao.selectCartByStoreId(query);
        //门店信息
        StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(storeId);
        //获取最新价格
        getCartProductListDetail(store.getProvinceId(), store.getCityId(), cartInfoList);

        //活动id 购物车商品行
        Map<String, List<CartOrderInfo>> activityCartMap = new HashMap<>(16);

        //楼层列表
        List<CartGroupInfo> cartGroupList = new ArrayList<>();

        Map<String, ActivityRequest> usefulActivityMap = new HashMap<>(16);
        List<String> uselessActivityIdList = new ArrayList<>();
        for (CartOrderInfo item :
                cartInfoList) {

            //该行是否归到活动楼层
            boolean activityItemFlag = false;
            if (StringUtils.isNotEmpty(item.getActivityId())) {
                if (!uselessActivityIdList.contains(item.getActivityId())) {
                    if (usefulActivityMap.containsKey(item.getActivityId())) {
                        activityItemFlag = true;
                    } else {
                        HttpResponse<ActivityRequest> activityDetailResponse = activityService.getActivityDetail(item.getActivityId());
                        if (RequestReturnUtil.validateHttpResponse(activityDetailResponse)) {
                            usefulActivityMap.put(item.getActivityId(), activityDetailResponse.getData());
                            activityItemFlag = true;
                        } else {
                            uselessActivityIdList.add(item.getActivityId());
                        }
                    }
                }
            }

            //校验当前商品是否可用当前活动
            if (activityItemFlag) {
                ActivityParameterRequest activityParameterRequest = new ActivityParameterRequest();
                activityParameterRequest.setSkuCode(item.getSkuCode());
                activityParameterRequest.setActivityId(item.getActivityId());
                activityParameterRequest.setProductBrandCode(item.getProductBrandCode());
                activityParameterRequest.setProductCategoryCode(item.getProductCategoryCode());
                activityParameterRequest.setStoreId(storeId);
                activityItemFlag = activityService.checkProcuct(activityParameterRequest);
            }

            if (activityItemFlag) {
                //参加活动的数据
                List<CartOrderInfo> list = new ArrayList<>();
                if (activityCartMap.containsKey(item.getActivityId())) {
                    list = activityCartMap.get(item.getActivityId());
                }
                list.add(item);
                activityCartMap.put(item.getActivityId(), list);
            } else {
                //非活动楼层
                CartGroupInfo cartGroupInfo = new CartGroupInfo();
                cartGroupInfo.setHasActivity(YesOrNoEnum.NO.getCode());
                List<CartOrderInfo> cartOrderList = new ArrayList<>();
                item.setActivityPrice(item.getPrice());
                BigDecimal totalAmount = item.getPrice().multiply(new BigDecimal(item.getAmount()));
                item.setLineAmountTotal(totalAmount);
                item.setLineAmountAfterActivity(totalAmount);
                item.setLineActivityAmountTotal(totalAmount);
                item.setLineActivityDiscountTotal(BigDecimal.ZERO);
                item.setActivityId(null);
                item.setActivityName(null);
                cartOrderList.add(item);
                cartGroupInfo.setCartProductList(cartOrderList);
                cartGroupList.add(cartGroupInfo);
            }
        }

        //有活动id的商品行
        for (Map.Entry<String, List<CartOrderInfo>> entry :
                activityCartMap.entrySet()) {
            CartGroupInfo cartGroupInfo = generateCartGroupAndShareActivityPrice(usefulActivityMap.get(entry.getKey()), store, entry.getValue());
            cartGroupList.add(cartGroupInfo);
        }

        //勾选商品活动价汇总
        BigDecimal activityAmountTotal = BigDecimal.ZERO;
        //勾选商品活动优惠金额
        BigDecimal activityDiscountAmount = BigDecimal.ZERO;
        //商品总数量
        int totalNumber = 0;
        //所有勾选的商品中可使用A品券的商品活动均摊后金额汇总
        BigDecimal topTotalPrice = BigDecimal.ZERO;

        //遍历楼层，汇总数据
        for (CartGroupInfo item :
                cartGroupList) {
            //楼层总的分销金额汇总
            BigDecimal groupAmount = BigDecimal.ZERO;
            //楼层总的活动价金额汇总
            BigDecimal groupActivityAmount = BigDecimal.ZERO;
            //楼层总的活动优惠减少的金额汇总
            BigDecimal groupActivityDiscountAmount = BigDecimal.ZERO;
            //楼层本品数量汇总
            int groupProductQuantity = 0;
            //楼层赠品数量汇总
            int groupGiftQuantity = 0;
            //楼层本品分销价汇总
            BigDecimal groupProductAmount = BigDecimal.ZERO;
            //楼层A品本品均摊后金额汇总
            BigDecimal groupTopCouponMaxTotal = BigDecimal.ZERO;

            //遍历本品列表
            if (item.getCartProductList() != null && item.getCartProductList().size() > 0) {
                for (CartOrderInfo productItem :
                        item.getCartProductList()) {
                    if (Global.LINECHECKSTATUS_1.equals(productItem.getLineCheckStatus())) {
                        groupAmount = groupAmount.add(productItem.getLineAmountTotal());
                        groupProductAmount = groupProductAmount.add(productItem.getLineAmountTotal());
                        groupActivityAmount = groupActivityAmount.add(productItem.getLineActivityAmountTotal());
                        groupActivityDiscountAmount = groupActivityDiscountAmount.add(productItem.getLineActivityDiscountTotal());
                        groupProductQuantity += productItem.getAmount();

                        ErpProductPropertyTypeEnum productPropertyTypeEnum = ErpProductPropertyTypeEnum.getEnum(productItem.getProductPropertyCode());
                        if (productPropertyTypeEnum != null && productPropertyTypeEnum.isUseTopCoupon()) {
                            groupTopCouponMaxTotal = groupTopCouponMaxTotal.add(productItem.getLineAmountAfterActivity());
                        }
                    }

                    if (cartRequest) {
                        //调用接口查询商品可选活动列表
                        ActivityParameterRequest activityParameterRequest = new ActivityParameterRequest();
                        activityParameterRequest.setStoreId(storeId);
                        activityParameterRequest.setSkuCode(productItem.getSkuCode());
                        activityParameterRequest.setProductBrandCode(productItem.getProductBrandCode());
                        activityParameterRequest.setProductCategoryCode(productItem.getProductCategoryCode());
                        List<Activity> activityList = activityService.activityList(activityParameterRequest);
                        productItem.setActivityList(activityList);
                    }
                }
            }

            //遍历赠品列表
            if (item.getCartGiftList() != null && item.getCartGiftList().size() > 0) {
                for (CartOrderInfo giftItem :
                        item.getCartGiftList()) {
                    groupAmount = groupAmount.add(giftItem.getLineAmountTotal());
                    groupActivityAmount = groupActivityAmount.add(giftItem.getLineActivityAmountTotal());
                    groupActivityDiscountAmount = groupActivityDiscountAmount.add(giftItem.getLineActivityDiscountTotal());
                    groupGiftQuantity += giftItem.getAmount();
                }
            }


            //楼层总的分销金额汇总
            item.setGroupAmount(groupAmount);
            //楼层总的活动价金额汇总
            item.setGroupActivityAmount(groupActivityAmount);
            //楼层总的活动优惠减少的金额汇总
            item.setGroupActivityDiscountAmount(groupActivityDiscountAmount);
            //楼层本品数量汇总
            item.setGroupProductQuantity(groupProductQuantity);
            //楼层赠品数量汇总
            item.setGroupGiftQuantity(groupGiftQuantity);
            //楼层本品分销价汇总
            item.setGroupProductAmount(groupProductAmount);
            //楼层A品本品均摊后金额汇总
            item.setGroupTopCouponMaxTotal(groupTopCouponMaxTotal);

            activityAmountTotal = activityAmountTotal.add(groupActivityAmount);
            totalNumber += groupProductQuantity;
            totalNumber += groupGiftQuantity;
            topTotalPrice = topTotalPrice.add(groupTopCouponMaxTotal);
            activityDiscountAmount = activityDiscountAmount.add(groupActivityDiscountAmount);
        }

        StoreCartProductResponse response = new StoreCartProductResponse();
        response.setActivityAmountTotal(activityAmountTotal);
        response.setTotalNumber(totalNumber);
        response.setTopTotalPrice(topTotalPrice);
        response.setCartGroupList(cartGroupList);
        response.setActivityDiscountAmount(activityDiscountAmount);
        response.setStoreAddress(store.getAddress());
        response.setStoreContacts(store.getContacts());
        response.setStoreContactsPhone(store.getContactsPhone());

        return response;
    }

    /**
     * 根据门店id返回商品的总数量
     *
     * @param storeId
     * @return
     */
    @Override
    public HttpResponse getTotal(String storeId) {
        HttpResponse<Integer> response = HttpResponse.success();
        try {
            if (storeId != null) {
                Integer total = cartOrderDao.getTotal(storeId);
                LOGGER.info("返回总数量给购物车：{}", total);
                return response.setData(total);
            }
        } catch (Exception e) {
            LOGGER.error("查询商品总量异常：{}", e);
            return HttpResponse.failure(ResultCode.GETTOTAL);
        }
        return response;
    }

    /**
     * 删除单条商品，清空整个购物车，删除勾选商品
     *
     * @param storeId
     * @param skuId
     * @param lineCheckStatus
     * @param productType
     * @return
     */
    @Override
    public HttpResponse deleteCartInfo(String storeId, String skuId, Integer lineCheckStatus, Integer productType) {
        try {
            if (StringUtils.isEmpty(storeId)) {
                LOGGER.error("删除购物车中的商品失败：{}", storeId);
                return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
            }
            CartOrderInfo query = new CartOrderInfo();
            query.setStoreId(storeId);
            query.setSkuCode(skuId);
            query.setLineCheckStatus(lineCheckStatus);
            query.setProductType(productType);
            query.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());

            List<CartOrderInfo> queryList = cartOrderDao.selectByProperty(query);
            if (queryList != null && queryList.size() > 0) {
                for (CartOrderInfo item :
                        queryList) {
                    cartOrderDao.deleteByCartId(item.getCartId());
                }
            }
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("清空购物车失败", e);
            return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
    }

    /**
     * 显示购物车中的勾选商品
     *
     * @param cartOrderInfo
     * @return
     */
    @Override
    public HttpResponse displayCartLineCheckProduct(CartOrderInfo cartOrderInfo) {
        HttpResponse httpResponse = HttpResponse.success();
        try {
            StoreCartProductResponse storeCartProductGroup = getStoreCartProductGroup(cartOrderInfo.getStoreId(), cartOrderInfo.getProductType(), false);
            httpResponse.setData(storeCartProductGroup);
        } catch (Exception e) {
            LOGGER.error("查询订单确认信息异常", e);
            httpResponse = HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
        }
        return httpResponse;
    }

    @Override
    public List<CartOrderInfo> getErpProductList(String storeId, Integer productType) {
        CartOrderInfo query = new CartOrderInfo();
        query.setStoreId(storeId);
        query.setProductType(productType);
        query.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());
        query.setLineCheckStatus(Global.LINECHECKSTATUS_1);

        //购物车数据
        List<CartOrderInfo> cartInfoList = null;
        try {
            cartInfoList = cartOrderDao.selectCartByStoreId(query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cartInfoList != null && cartInfoList.size() > 0) {
            for (CartOrderInfo item :
                    cartInfoList) {
                BigDecimal totalMoney = item.getPrice().multiply(new BigDecimal(item.getAmount()));
                item.setActivityPrice(item.getPrice());
                item.setLineAmountTotal(totalMoney);
                item.setLineActivityAmountTotal(totalMoney);
                item.setLineActivityDiscountTotal(BigDecimal.ZERO);
                item.setLineAmountAfterActivity(totalMoney);
            }
        }

        return cartInfoList;
    }

    @Override
    public void deleteByCartId(String cartId) {
        cartOrderDao.deleteByCartId(cartId);
    }

    @Override
    public StoreActivityAchieveResponse getStoreActivityAchieveDetail(StoreActivityAchieveRequest storeActivityAchieveRequest) {
        StoreActivityAchieveResponse storeActivityAchieveResponse = new StoreActivityAchieveResponse();

        CartOrderInfo query = new CartOrderInfo();
        query.setStoreId(storeActivityAchieveRequest.getStoreId());
        query.setActivityId(storeActivityAchieveRequest.getActivityId());
        query.setLineCheckStatus(Global.LINECHECKSTATUS_1);
        query.setProductType(ErpOrderTypeEnum.DISTRIBUTION.getCode());

        BigDecimal totalMoney = BigDecimal.ZERO;
        int totalCount = 0;
        List<CartOrderInfo> list = cartOrderDao.selectByProperty(query);
        if (list != null && list.size() > 0) {
            StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(storeActivityAchieveRequest.getStoreId());
            getCartProductListDetail(store.getProvinceId(), store.getCityId(), list);
            for (CartOrderInfo item :
                    list) {
                ActivityParameterRequest activityParameterRequest = new ActivityParameterRequest();
                activityParameterRequest.setSkuCode(item.getSkuCode());
                activityParameterRequest.setActivityId(item.getActivityId());
                activityParameterRequest.setProductBrandCode(item.getProductBrandCode());
                activityParameterRequest.setProductCategoryCode(item.getProductCategoryCode());
                activityParameterRequest.setStoreId(store.getStoreId());
                Boolean aBoolean = activityService.checkProcuct(activityParameterRequest);
                if (aBoolean) {
                    totalMoney = totalMoney.add(item.getPrice().multiply(new BigDecimal(item.getAmount())));
                    totalCount += item.getAmount();
                }
            }
        }


        HttpResponse<ActivityRequest> activityDetail = activityService.getActivityDetail(storeActivityAchieveRequest.getActivityId());
        if (!RequestReturnUtil.validateHttpResponse(activityDetail)) {
            throw new BusinessException("获取活动详情失败");
        }
        ActivityRequest activityRequest = activityDetail.getData();
        Activity activity = activityRequest.getActivity();
        List<ActivityRule> activityRules = activityRequest.getActivityRules();

        //最低梯度
        ActivityRule firstRule = null;
        //当前满足梯度
        ActivityRule curRule = null;

        for (ActivityRule item :
                activityRules) {

            //筛选出最低梯度
            if (firstRule == null) {
                firstRule = item;
            }
            if (item.getMeetingConditions().compareTo(firstRule.getMeetingConditions()) < 0) {
                firstRule = item;
            }

            //筛选出当前满足的最大梯度
            if (ActivityRuleUnitEnum.BY_MONEY.getCode().equals(item.getRuleUnit())) {
                //按照金额
                if (item.getMeetingConditions().compareTo(totalMoney) <= 0) {
                    if (curRule == null || item.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                        curRule = item;
                    }
                }
            }

            if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(item.getRuleUnit())) {
                //按照金额
                if (item.getMeetingConditions().compareTo(new BigDecimal(totalCount)) <= 0) {
                    if (curRule == null || item.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                        curRule = item;
                    }
                }
            }

        }

        storeActivityAchieveResponse.setActivity(activity);
        storeActivityAchieveResponse.setCurActivityRule(curRule);
        storeActivityAchieveResponse.setFirstActivityRule(firstRule);
        storeActivityAchieveResponse.setTotalCount(totalCount);
        storeActivityAchieveResponse.setTotalMoney(totalMoney);
        return storeActivityAchieveResponse;
    }

    /**
     * 根据满赠活动生成赠品行
     *
     * @param cart  本品行
     * @param rule  满赠梯度规则中的一个赠品规则
     * @param store 门店信息
     * @return
     */
    private CartOrderInfo createGiftProductLine(CartOrderInfo cart, ActivityGift rule, StoreInfo store) {

        CartOrderInfo skuDetail = bridgeProductService.getCartProductDetail(store.getProvinceId(), store.getCityId(), rule.getSkuCode());

        CartOrderInfo cartOrderInfo = new CartOrderInfo();
        cartOrderInfo.setCartId(IdUtil.uuid());
        cartOrderInfo.setSkuCode(skuDetail.getSkuCode());//skuId
        cartOrderInfo.setSpuId(skuDetail.getSpuId());//spuId
        cartOrderInfo.setProductId(skuDetail.getSkuCode());//商品Code
        cartOrderInfo.setStoreId(cart.getStoreId());//门店id
        cartOrderInfo.setProductName(skuDetail.getSkuName());//商品名称
        cartOrderInfo.setColor(skuDetail.getColorName());//商品颜色
        cartOrderInfo.setProductSize(skuDetail.getProductSize());//商品型号
        cartOrderInfo.setCreateSource(cart.getCreateSource());//插入商品来源
        cartOrderInfo.setAmount(rule.getNumbers());//获取商品数量
        cartOrderInfo.setPrice(skuDetail.getPriceTax());//商品价格
        cartOrderInfo.setProductType(cart.getProductType());//商品类型 0直送、1配送、2辅采
        cartOrderInfo.setCreateById(cart.getCreateById());//创建者id
        cartOrderInfo.setCreateByName(cart.getCreateByName());//创建者名称
        cartOrderInfo.setStockNum(skuDetail.getStockNum());//库存数量
        cartOrderInfo.setZeroRemovalCoefficient(skuDetail.getZeroRemovalCoefficient());//交易倍数
        cartOrderInfo.setSpec(skuDetail.getSpec());//规格
        cartOrderInfo.setProductPropertyCode(skuDetail.getProductPropertyCode());//商品属性码
        cartOrderInfo.setProductPropertyName(skuDetail.getProductPropertyName());//商品属性名称
        cartOrderInfo.setLineCheckStatus(1);//选中状态
        cartOrderInfo.setProductBrandCode(skuDetail.getProductBrandCode());//品牌编码
        cartOrderInfo.setProductBrandName(skuDetail.getProductBrandName());//品牌名称
        cartOrderInfo.setProductCategoryCode(skuDetail.getProductCategoryCode());//品类编码
        cartOrderInfo.setProductCategoryName(skuDetail.getProductCategoryName());//品类编码
        cartOrderInfo.setProductGift(ErpProductGiftEnum.GIFT.getCode());
        cartOrderInfo.setGiftParentCartId(cart.getCartId());
        cartOrderInfo.setActivityPrice(BigDecimal.ZERO);
        cartOrderInfo.setCreateTime(new Date());
        cartOrderInfo.setActivityId(cart.getActivityId());
        cartOrderInfo.setActivityName(cart.getActivityName());
        cartOrderInfo.setTagInfoList(skuDetail.getTagInfoList());
        BigDecimal multiplyAmountTotal = cartOrderInfo.getPrice().multiply(new BigDecimal(cartOrderInfo.getAmount()));
        cartOrderInfo.setLineAmountTotal(multiplyAmountTotal);
        cartOrderInfo.setLineActivityAmountTotal(BigDecimal.ZERO);
        cartOrderInfo.setLineActivityDiscountTotal(BigDecimal.ZERO);
        cartOrderInfo.setLineAmountAfterActivity(BigDecimal.ZERO);


        return cartOrderInfo;
    }

    /**
     * 根据活动id和本品列表解析活动并且返回一个楼层
     *
     * @param activityRequest 活动详情
     * @param store           门店信息
     * @param list            本品列表
     * @return
     */
    private CartGroupInfo generateCartGroupAndShareActivityPrice(ActivityRequest activityRequest, StoreInfo store, List<CartOrderInfo> list) {

        CartGroupInfo cartGroupInfo = new CartGroupInfo();
        cartGroupInfo.setCartProductList(list);

        //勾选的商品组商品数量
        Integer quantity = 0;
        //勾选的商品组分销价汇总
        BigDecimal amountTotal = BigDecimal.ZERO;
        //勾选的商品组活动价汇总，初始等于商品组分销价汇总
        BigDecimal activityAmountTotal = BigDecimal.ZERO;

        //用来缓存勾选状态的行
        List<CartOrderInfo> tempList = new ArrayList<>();
        //商品组金额数量汇总
        for (CartOrderInfo item :
                list) {
            //行分销金额
            BigDecimal lineAmountTotal = item.getPrice().multiply(new BigDecimal(item.getAmount()));

            if (Global.LINECHECKSTATUS_1.equals(item.getLineCheckStatus())) {
                //勾选的行

                quantity += item.getAmount();
                amountTotal = amountTotal.add(lineAmountTotal);
                activityAmountTotal = activityAmountTotal.add(lineAmountTotal);
                tempList.add(item);
            }

            item.setActivityPrice(item.getPrice());
            //行分销金额
            item.setLineAmountTotal(lineAmountTotal);
            //行活动金额暂且赋值为分销价汇总
            item.setLineActivityAmountTotal(lineAmountTotal);
            //行活动优惠金额暂且赋值为0
            item.setLineActivityDiscountTotal(BigDecimal.ZERO);
            //行减去活动优惠之后分摊的金额
            item.setLineAmountAfterActivity(lineAmountTotal);
        }


        //获取活动详情
        Activity activity = activityRequest.getActivity();
        cartGroupInfo.setHasActivity(YesOrNoEnum.YES.getCode());
        cartGroupInfo.setActivity(activity);
        List<ActivityRule> activityRules = activityRequest.getActivityRules();

        //活动类型
        ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.getEnum(activity.getActivityType());

        //缓存当前命中的规则
        ActivityRule curRule = null;
        //最小梯度
        ActivityRule firstRule = null;

        //根据活动类型解析活动
        switch (activityTypeEnum) {
            case TYPE_1:
                //满减

                for (ActivityRule ruleItem :
                        activityRules) {

                    //筛选最小梯度
                    if (firstRule == null || ruleItem.getMeetingConditions().compareTo(firstRule.getMeetingConditions()) < 0) {
                        firstRule = ruleItem;
                    }

                    //是否把当前梯度作为命中梯度
                    boolean flag = false;

                    if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(ruleItem.getRuleUnit())) {
                        //按照数量

                        if (ruleItem.getMeetingConditions().compareTo(new BigDecimal(quantity)) <= 0) {
                            if (curRule == null) {
                                flag = true;
                            } else {
                                if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                    flag = true;
                                }
                            }
                        }

                    } else if (ActivityRuleUnitEnum.BY_MONEY.getCode().equals(ruleItem.getRuleUnit())) {
                        //按照金额

                        if (ruleItem.getMeetingConditions().compareTo(amountTotal) <= 0) {
                            if (curRule == null) {
                                flag = true;
                            } else {
                                if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                    flag = true;
                                }
                            }
                        }

                    } else {

                    }

                    if (flag) {
                        curRule = ruleItem;
                    }
                }

                if (curRule != null) {

                    cartGroupInfo.setActivityRule(curRule);

                    //当前剩余满减的金额
                    BigDecimal restPreferentialAmount = curRule.getPreferentialAmount();


                    if (activityAmountTotal.compareTo(BigDecimal.ZERO) > 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            CartOrderInfo item = tempList.get(i);
                            if (i == tempList.size() - 1) {
                                //最后一行，用减法避免误差
                                item.setLineActivityDiscountTotal(restPreferentialAmount);
                            } else {
                                BigDecimal lineActivityDiscountTotal = item.getLineActivityAmountTotal().divide(activityAmountTotal, 6, RoundingMode.HALF_UP).multiply(curRule.getPreferentialAmount()).setScale(2,RoundingMode.HALF_UP);
                                item.setLineActivityDiscountTotal(lineActivityDiscountTotal);
                                restPreferentialAmount = restPreferentialAmount.subtract(lineActivityDiscountTotal);
                            }
                            item.setLineAmountAfterActivity(item.getLineActivityAmountTotal().subtract(item.getLineActivityDiscountTotal()));
                        }
                    }
                }
                ;
                break;
            case TYPE_2:
                //满赠

                for (ActivityRule ruleItem :
                        activityRules) {

                    //筛选最小梯度
                    if (firstRule == null || ruleItem.getMeetingConditions().compareTo(firstRule.getMeetingConditions()) < 0) {
                        firstRule = ruleItem;
                    }

                    //是否把当前梯度作为命中梯度
                    boolean flag = false;


                    if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(ruleItem.getRuleUnit())) {
                        //按照数量

                        if (ruleItem.getMeetingConditions().compareTo(new BigDecimal(quantity)) <= 0) {
                            if (curRule == null) {
                                flag = true;
                            } else {
                                if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                    flag = true;
                                }
                            }
                        }

                    } else if (ActivityRuleUnitEnum.BY_MONEY.getCode().equals(ruleItem.getRuleUnit())) {
                        //按照金额

                        if (ruleItem.getMeetingConditions().compareTo(amountTotal) <= 0) {
                            if (curRule == null) {
                                flag = true;
                            } else {
                                if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                    flag = true;
                                }
                            }
                        }

                    } else {

                    }

                    if (flag) {
                        curRule = ruleItem;
                    }
                }

                if (curRule != null) {

                    cartGroupInfo.setActivityRule(curRule);

                    //满赠规则组
                    List<ActivityGift> giftList = curRule.getGiftList();

                    //存放生成的赠品行
                    List<CartOrderInfo> cartGiftList = new ArrayList<>();

                    //生成赠品行
                    for (ActivityGift giftItem :
                            giftList) {
                        //生成赠品行
                        CartOrderInfo giftProductLine = createGiftProductLine(list.get(0), giftItem, store);
                        cartGiftList.add(giftProductLine);
                        amountTotal = amountTotal.add(giftProductLine.getPrice().multiply(new BigDecimal(giftProductLine.getAmount())));
                    }

                    //本品+赠品的list，用来计算活动价格均摊
                    List<CartOrderInfo> productShareList = new ArrayList<>();
                    productShareList.addAll(tempList);
                    productShareList.addAll(cartGiftList);

                    BigDecimal restActivityAmountTotal = activityAmountTotal;

                    if (amountTotal.compareTo(BigDecimal.ZERO) > 0) {
                        for (int i = 0; i < productShareList.size(); i++) {
                            CartOrderInfo item = productShareList.get(i);
                            if (i == productShareList.size() - 1) {
                                //最后一行
                                item.setLineAmountAfterActivity(restActivityAmountTotal);
                            } else {
                                BigDecimal lineAmountAfterActivity = item.getLineAmountTotal().divide(amountTotal, 6, RoundingMode.HALF_UP).multiply(activityAmountTotal).setScale(2, RoundingMode.HALF_UP);
                                item.setLineAmountAfterActivity(lineAmountAfterActivity);
                                restActivityAmountTotal = restActivityAmountTotal.subtract(lineAmountAfterActivity);
                            }
                        }
                    }
                    cartGroupInfo.setCartGiftList(cartGiftList);
                }

                ;
                break;
            default:
                ;
        }
        cartGroupInfo.setFirstActivityRule(firstRule);

        return cartGroupInfo;
    }

    /**
     * 获取最新的商品价格数据
     *
     * @param provinceId
     * @param cityId
     * @param cartList
     */
    private void getCartProductListDetail(String provinceId, String cityId, List<CartOrderInfo> cartList) {
        if (cartList != null && cartList.size() > 0) {
            ShoppingCartProductRequest shoppingCartProductRequest = new ShoppingCartProductRequest();
            shoppingCartProductRequest.setProvinceCode(provinceId);
            shoppingCartProductRequest.setCityCode(cityId);
            shoppingCartProductRequest.setCompanyCode(OrderConstant.SELECT_PRODUCT_COMPANY_CODE);
            List<String> skuCodeList = new ArrayList<>();
            for (CartOrderInfo item :
                    cartList) {
                skuCodeList.add(item.getSkuCode());
            }
            shoppingCartProductRequest.setSkuCodes(skuCodeList);
            HttpResponse<List<CartOrderInfo>> response = bridgeProductService.getProduct(shoppingCartProductRequest);
            if (RequestReturnUtil.validateHttpResponse(response)) {
                List<CartOrderInfo> data = response.getData();
                if (data != null && data.size() > 0) {
                    for (CartOrderInfo item :
                            cartList) {
                        for (CartOrderInfo dataItem :
                                data) {
                            if (item.getSkuCode().equals(dataItem.getSkuCode())) {
                                item.setPrice(dataItem.getPriceTax());
                                item.setTagInfoList(dataItem.getTagInfoList());
                                item.setStockNum(dataItem.getStockNum());
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}