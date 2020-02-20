package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityRuleUnitEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityTypeEnum;
import com.aiqin.mgs.order.api.dao.CartOrderDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.cart.Product;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartProductRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.response.cart.CartResponse;
import com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse;
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
    public HttpResponse addCart(ShoppingCartRequest shoppingCartRequest , AuthToken authToken) {
        List<String> skuCodeList=new ArrayList();
        //入参校验
        checkParam(shoppingCartRequest);

        //商品数量不能大于999
        List<Product> products = shoppingCartRequest.getProducts();
        for (Product product : products) {
            if (product.getAmount() > 999) {
                return HttpResponse.failure(ResultCode.OVER_LIMIT);
            }
            skuCodeList.add(product.getSkuId());
        }
        //通过门店id返回门店省市公司信息
        HttpResponse<StoreInfo> storeInfo = bridgeProductService.getStoreInfo(shoppingCartRequest);
        if(storeInfo==null||storeInfo.getData()==null){
            return HttpResponse.failure(ResultCode.NO_HAVE_STORE_ERROR);
        }
        ShoppingCartProductRequest shoppingCartProductRequest=new ShoppingCartProductRequest();
        shoppingCartProductRequest.setCityCode(storeInfo.getData().getCityId());
        shoppingCartProductRequest.setProvinceCode(storeInfo.getData().getProvinceId());
        shoppingCartProductRequest.setCompanyCode("14");
        shoppingCartProductRequest.setSkuCodes(skuCodeList);
        if(shoppingCartProductRequest.getCompanyCode()==null
                || shoppingCartProductRequest.getCityCode()==null
                || shoppingCartProductRequest.getProvinceCode()==null
                || shoppingCartProductRequest.getSkuCodes()==null){
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
                if(skuId.equals(product.getSkuId())) {
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
                    try {
                        if (cartOrderInfo != null) {
                            //判断sku是否在购物车里面存在
                            LOGGER.info("判断SKU商品是否已存在购物车中:{}", cartOrderInfo.getSkuCode());
                            String oldAount = cartOrderDao.isYesCart(cartOrderInfo);
                            if (oldAount != null && ! "".equals(oldAount)) {
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
            //默认标志为0
            lineCheckStatus = lineCheckStatus == null ? 0 : lineCheckStatus;
            //检查商品是否被勾选，勾选后，更新数据库标识
            LOGGER.info("商品勾选后，更新勾选状态：{}", lineCheckStatus);
            if (null != skuId && lineCheckStatus.equals(Global.LINECHECKSTATUS_1) &&null !=productType) {
                cartOrderInfo.setSkuCode(skuId);
                cartOrderInfo.setLineCheckStatus(lineCheckStatus);
                cartOrderInfo.setStoreId(storeId);
                cartOrderInfo.setProductType(productType);
                if (null != number){
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);
                CartResponse cartResponse = getProductList(cartOrderInfo);
                LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
                response.setData(cartResponse);
                //如果是取消勾选，通过门店id所有标记
            }else if (null != skuId && lineCheckStatus.equals(Global.LINECHECKSTATUS_0) &&null !=productType) {
                cartOrderInfo.setSkuCode(skuId);
                cartOrderInfo.setLineCheckStatus(lineCheckStatus);
                cartOrderInfo.setProductType(productType);
                cartOrderInfo.setStoreId(storeId);
                if (null != number){
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);
                CartResponse cartResponse = getProductList(cartOrderInfo);
                LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
                response.setData(cartResponse);
                //如果是全选，通过门店id更新所有标记
            }else if(null != storeId && lineCheckStatus.equals(Global.LINECHECKSTATUS_2) && null !=productType) {
                cartOrderInfo.setStoreId(storeId);
                cartOrderInfo.setProductType(productType);
                cartOrderInfo.setLineCheckStatus(Global.LINECHECKSTATUS_1);
                if (null != number){
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);
                //返回商品列表并结算价格
                CartResponse cartResponse = getProductList(cartOrderInfo);
                LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
                response.setData(cartResponse);
                //如果是全部取消，通过门店id更新所有标记
            } else if(null != storeId && lineCheckStatus.equals(Global.LINECHECKSTATUS_3) && null !=productType) {
                cartOrderInfo.setStoreId(storeId);
                cartOrderInfo.setProductType(productType);
                cartOrderInfo.setLineCheckStatus(Global.LINECHECKSTATUS_0);
                if (null != number){
                    cartOrderInfo.setAmount(number);
                }
                cartOrderDao.updateProductList(cartOrderInfo);
                //返回商品列表并结算价格
                CartResponse cartResponse = getProductList(cartOrderInfo);
                LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
                response.setData(cartResponse);
            } else {
                //返回商品列表并结算价格
                CartResponse cartResponse = getProductList(cartOrderInfo);
                LOGGER.info("返回购物车中的数据给前端：{}", cartResponse);
                response.setData(cartResponse);
            }
            return response;
        } catch (Exception e) {
            LOGGER.error("根据门店ID查询购物车数据异常：{}", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
    }

    /**
     * 返回购物车中的商品列表，并计算出商品总价和总数量
     * @param cartOrderInfo
     * @return
     * @throws Exception
     */
    private CartResponse getProductList(CartOrderInfo cartOrderInfo) throws Exception {
        //购物车数据
        List<CartOrderInfo> cartInfoList = cartOrderDao.selectCartByStoreId(cartOrderInfo);
        //计算商品总价格
        BigDecimal acountActualprice = new BigDecimal(0);
        for (CartOrderInfo cartOrderInfo1 : cartInfoList) {
            if(cartOrderInfo1.getLineCheckStatus()==1){
                BigDecimal total = cartOrderInfo1.getPrice().multiply(new BigDecimal(cartOrderInfo1.getAmount()));
                acountActualprice = acountActualprice.add(total);
            }

        }
        //计算商品总数量
        int totalNumber = 0;
        for (CartOrderInfo cartOrderInfo2 : cartInfoList) {
            if(cartOrderInfo2.getLineCheckStatus()==1){
                totalNumber += cartOrderInfo2.getAmount();
            }
        }
        CartResponse cartResponse = new CartResponse();
        cartResponse.setCartInfoList(cartInfoList);
        cartResponse.setAccountActualPrice(acountActualprice);
        cartResponse.setTotalNumber(totalNumber);
        return cartResponse;
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
            //清空购物车
            if (storeId != null) {
                LOGGER.info("删除购物车中的商品：{}", storeId);
                //可以清空，可以删除单条，删除勾选数据。
                cartOrderDao.deleteCart(storeId, skuId, lineCheckStatus,productType);
            } else {
                LOGGER.error("删除购物车中的商品失败：{}", storeId);
                return HttpResponse.failure(ResultCode.DELETE_EXCEPTION);
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
        BigDecimal priceProductA=BigDecimal.ZERO;
        HttpResponse response = HttpResponse.success();
        //调用门店接口，返回门店的基本信息
        ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest();
        shoppingCartRequest.setStoreId(cartOrderInfo.getStoreId());
        HttpResponse<StoreInfo> storeInfo = bridgeProductService.getStoreInfo(shoppingCartRequest);
        OrderConfirmResponse orderConfirmResponse = new OrderConfirmResponse();
        //封装门店信息
        orderConfirmResponse.setStoreAddress(storeInfo.getData().getAddress());
        orderConfirmResponse.setStoreContacts(storeInfo.getData().getContacts());
        orderConfirmResponse.setStoreContactsPhone(storeInfo.getData().getContactsPhone());
        try {
            if (cartOrderInfo != null) {
                List<CartOrderInfo> cartOrderInfos = cartOrderDao.selectCartByLineCheckStatus(cartOrderInfo);
                //封装返回的勾选的商品信息
                orderConfirmResponse.setCartOrderInfos(cartOrderInfos);
                orderConfirmResponse.setHaveProductA(0);

                //计算订货金额合计
                BigDecimal orderTotalPrice = new BigDecimal(0);
                for (CartOrderInfo cartOrderInfo1 : cartOrderInfos) {
                    BigDecimal total = cartOrderInfo1.getPrice().multiply(new BigDecimal(cartOrderInfo1.getAmount()));
                    orderTotalPrice = orderTotalPrice.add(total);
                    //TODO 判断商品为A类以上商品 此处最好是按照字典表接口比对，防止供应链更改类型code
                    if(cartOrderInfo1.getProductPropertyCode().equals("1") ||cartOrderInfo1.getProductPropertyCode().equals("6")){//A品与A+品
                        orderConfirmResponse.setHaveProductA(1);
                        priceProductA=priceProductA.add(cartOrderInfo1.getPrice());
                    }

                }
                orderConfirmResponse.setPriceProductA(priceProductA);
                //封装订货金额合计
                orderConfirmResponse.setAcountActualprice(orderTotalPrice);
                response.setData(orderConfirmResponse);
            }
        } catch (Exception e) {
            LOGGER.error("返回订单确认数据失败", e);
            return HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }

    @Override
    public void analysisActivityInCart(String cartId) {
        CartOrderInfo cart = cartOrderDao.getCartByCartId(cartId);
        if (cart == null) {
            //异常
            return;
        }

        //如果不是本品行，跳过
        if (ErpProductGiftEnum.GIFT.getCode().equals(cart.getProductGift())) {
            return;
        }

        //复位，把活动减去的数量和金额、赠品都归位
        List<CartOrderInfo> list = cartOrderDao.findByGiftParentCartId(cartId);
        if (list != null && list.size() > 0) {
            for (CartOrderInfo item :
                    list) {
                cartOrderDao.deleteByCartId(item.getCartId());
            }
        }
        //把实际支付金额置为原价
        cart.setAccountActualPrice(cart.getPrice().multiply(new BigDecimal(cart.getAmount())));
        //活动价等于原价
        cart.setActivityPrice(cart.getPrice());

        if (StringUtils.isNotEmpty(cart.getActivityId())) {
            //校验活动，如果过期，删除活动
            Boolean aBoolean = activityService.checkProcuct(cart.getActivityId(), cart.getStoreId(), cart.getProductId());
            if (aBoolean) {
                //解析活动

                //获取活动详情
                HttpResponse<ActivityRequest> activityDetail = activityService.getActivityDetail(cart.getActivityId());
                if (!RequestReturnUtil.validateHttpResponse(activityDetail)) {
                    throw new BusinessException("活动异常");
                }
                ActivityRequest activityRequest = activityDetail.getData();
                Activity activity = activityRequest.getActivity();
                cart.setActivityName(activity.getActivityName());
                List<ActivityRule> activityRules = activityRequest.getActivityRules();

                //活动类型
                ActivityTypeEnum activityTypeEnum = ActivityTypeEnum.getEnum(activity.getActivityType());
                //行原始金额
                BigDecimal cartMoney = cart.getPrice().multiply(new BigDecimal(cart.getAmount()));
                switch (activityTypeEnum) {
                    case TYPE_1:
                        //满减

                        //缓存当前命中的规则
                        ActivityRule curRule = null;

                        for (ActivityRule ruleItem :
                                activityRules) {

                            //是否把当前梯度作为命中梯度
                            boolean flag = false;

                            if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(ruleItem.getRuleUnit())) {
                                //按照数量

                                if (ruleItem.getMeetingConditions().compareTo(new BigDecimal(cart.getAmount())) <= 0) {
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

                                if (ruleItem.getMeetingConditions().compareTo(cartMoney) <= 0) {
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
                            cart.setAccountActualPrice(cart.getAccountActualPrice().compareTo(curRule.getPreferentialAmount()) > 0 ? cart.getAccountActualPrice().subtract(curRule.getPreferentialAmount()) : BigDecimal.ZERO);
                        }
                        ;
                        break;
                    case TYPE_2:
                        //满赠

                        Map<Integer, List<ActivityRule>> ruleNumGroupMap = new HashMap<>(16);
                        Integer curRuleNum = null;
                        BigDecimal curMeetingConditions = null;


                        for (ActivityRule ruleItem :
                                activityRules) {

                            //记录分组情况
                            List<ActivityRule> ruleNumGroupList = new ArrayList<>();
                            if (ruleNumGroupMap.containsKey(ruleItem.getRuleNum())) {
                                ruleNumGroupList = ruleNumGroupMap.get(ruleItem.getRuleNum());
                            }
                            ruleNumGroupList.add(ruleItem);
                            ruleNumGroupMap.put(ruleItem.getRuleNum(), ruleNumGroupList);

                            //是否把当前梯度作为命中梯度
                            boolean flag = false;


                            if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(ruleItem.getRuleUnit())) {
                                //按照数量

                                if (ruleItem.getMeetingConditions().compareTo(new BigDecimal(cart.getAmount())) <= 0) {
                                    if (curRuleNum == null) {
                                        flag = true;
                                    } else {
                                        if (ruleItem.getMeetingConditions().compareTo(curMeetingConditions) > 0) {
                                            flag = true;
                                        }
                                    }
                                }

                            } else if (ActivityRuleUnitEnum.BY_MONEY.getCode().equals(ruleItem.getRuleUnit())) {
                                //按照金额

                                if (ruleItem.getMeetingConditions().compareTo(cartMoney) <= 0) {
                                    if (curRuleNum == null) {
                                        flag = true;
                                    } else {
                                        if (ruleItem.getMeetingConditions().compareTo(curMeetingConditions) > 0) {
                                            flag = true;
                                        }
                                    }
                                }

                            } else {

                            }

                            if (flag) {
                                curRuleNum = ruleItem.getRuleNum();
                                curMeetingConditions = ruleItem.getMeetingConditions();
                            }
                        }

                        if (curRuleNum != null) {
                            //满赠规则组
                            List<ActivityRule> activityRuleList = ruleNumGroupMap.get(curRuleNum);

                            //生成赠品行
                            for (ActivityRule ruleItem :
                                    activityRuleList) {
                                CartOrderInfo giftProductLine = createGiftProductLine(cart, ruleItem);
                                try {
                                    cartOrderDao.insertCart(giftProductLine);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        ;
                        break;
                    default:
                        ;
                }
            } else {
                cart.setActivityId(null);
            }

        }

        try {
            cartOrderDao.updateCartByCartId(cart);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 根据满赠活动生成赠品行
     *
     * @param cart 本品行
     * @param rule 满赠梯度规则中的一个赠品规则
     * @return
     */
    private CartOrderInfo createGiftProductLine(CartOrderInfo cart, ActivityRule rule) {

        ProductInfo skuDetail = erpOrderRequestService.getSkuDetail(OrderConstant.SELECT_PRODUCT_COMPANY_CODE, "rule.getSkuCode()");

        CartOrderInfo cartOrderInfo = new CartOrderInfo();
        cartOrderInfo.setCartId(IdUtil.uuid());
        cartOrderInfo.setSkuCode(skuDetail.getSkuCode());//skuId
        cartOrderInfo.setSpuId(skuDetail.getSpuCode());//spuId
        cartOrderInfo.setProductId(skuDetail.getSpuCode());//商品Code
        cartOrderInfo.setStoreId(cart.getStoreId());//门店id
        cartOrderInfo.setProductName(skuDetail.getSpuName());//商品名称
        cartOrderInfo.setColor(skuDetail.getColorName());//商品颜色
        cartOrderInfo.setProductSize(skuDetail.getModelCode());//商品型号
        cartOrderInfo.setCreateSource(cart.getCreateSource());//插入商品来源
//        cartOrderInfo.setAmount(rule.getNumbers());//获取商品数量
        cartOrderInfo.setPrice(BigDecimal.ZERO);//商品价格
        cartOrderInfo.setProductType(cart.getProductType());//商品类型 0直送、1配送、2辅采
        cartOrderInfo.setCreateById(cart.getCreateById());//创建者id
        cartOrderInfo.setCreateByName(cart.getCreateByName());//创建者名称
//        cartOrderInfo.setStockNum(skuDetail.getStockNum());//库存数量
//        cartOrderInfo.setZeroRemovalCoefficient(skuDetail.getZeroRemovalCoefficient());//交易倍数
        cartOrderInfo.setSpec(skuDetail.getProductSpec());//规格
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
        cartOrderInfo.setAccountActualPrice(BigDecimal.ZERO);
        cartOrderInfo.setCreateTime(new Date());

        return cartOrderInfo;
    }
}