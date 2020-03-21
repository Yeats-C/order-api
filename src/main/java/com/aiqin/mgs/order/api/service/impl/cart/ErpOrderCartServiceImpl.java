package com.aiqin.mgs.order.api.service.impl.cart;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityRuleUnitEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityTypeEnum;
import com.aiqin.mgs.order.api.component.enums.cart.ErpCartLineStatusEnum;
import com.aiqin.mgs.order.api.component.enums.cart.ErpProductGiftGiveTypeEnum;
import com.aiqin.mgs.order.api.dao.cart.ErpOrderCartDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityParameterRequest;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.cart.*;
import com.aiqin.mgs.order.api.domain.response.cart.*;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import com.aiqin.mgs.order.api.service.RedisService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.cart.ErpOrderCartService;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class ErpOrderCartServiceImpl implements ErpOrderCartService {

    @Resource
    private ErpOrderCartDao erpOrderCartDao;

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private ErpOrderRequestService erpOrderRequestService;

    @Resource
    private ActivityService activityService;

    @Resource
    private RedisService redisService;

    @Resource
    private CouponRuleService couponRuleService;

    @Override
    public void insertCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken) {
        erpOrderCartInfo.setCreateById(authToken.getPersonId());
        erpOrderCartInfo.setCreateByName(authToken.getPersonName());
        erpOrderCartInfo.setUpdateById(authToken.getPersonId());
        erpOrderCartInfo.setUpdateByName(authToken.getPersonName());
        erpOrderCartDao.insert(erpOrderCartInfo);
    }

    @Override
    public void insertCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken) {
        for (ErpOrderCartInfo item :
                list) {
            insertCartLine(item, authToken);
        }
    }

    @Override
    public void updateCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken) {
        erpOrderCartInfo.setCreateById(authToken.getPersonId());
        erpOrderCartInfo.setCreateByName(authToken.getPersonName());
        erpOrderCartInfo.setUpdateById(authToken.getPersonId());
        erpOrderCartInfo.setUpdateByName(authToken.getPersonName());
        erpOrderCartDao.updateByPrimaryKey(erpOrderCartInfo);
    }

    @Override
    public void updateCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken) {
        for (ErpOrderCartInfo item :
                list) {
            updateCartLine(item, authToken);
        }
    }

    @Override
    public ErpOrderCartInfo getCartLineByCartId(String cartId) {
        ErpOrderCartInfo result = null;
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setCartId(cartId);
        List<ErpOrderCartInfo> select = erpOrderCartDao.select(query);
        if (select != null && select.size() > 0) {
            result = select.get(0);
        }
        return result;
    }

    @Override
    public List<ErpOrderCartInfo> selectByProperty(ErpOrderCartInfo erpOrderCartInfo) {
        return erpOrderCartDao.select(erpOrderCartInfo);
    }

    @Override
    public void deleteCartLine(String cartId) {
        ErpOrderCartInfo cartLine = getCartLineByCartId(cartId);
        if (cartLine != null) {
            erpOrderCartDao.deleteByPrimaryKey(cartLine.getId());
        }
    }

    @Override
    public void deleteAllCartLine(ErpCartQueryRequest erpCartQueryRequest) {
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartQueryRequest.getStoreId());
        query.setProductType(erpCartQueryRequest.getProductType());
        List<ErpOrderCartInfo> cartLineList = this.selectByProperty(query);
        if (cartLineList != null && cartLineList.size() > 0) {
            for (ErpOrderCartInfo item :
                    cartLineList) {
                this.deleteCartLine(item.getCartId());
            }
        }
    }

    @Override
    public ErpOrderCartAddResponse addProduct(ErpCartAddRequest erpCartAddRequest, AuthToken auth) {

        if (erpCartAddRequest == null) {
            throw new BusinessException("参数为空");
        }
        if (erpCartAddRequest.getProducts() == null || erpCartAddRequest.getProducts().size() == 0) {
            throw new BusinessException("商品不能为空");
        }
        if (erpCartAddRequest.getSpuCode() == null) {
            throw new BusinessException("productId为空");
        }
        if (erpCartAddRequest.getStoreId() == null) {
            throw new BusinessException("门店id为空");
        }
        if (erpCartAddRequest.getProductType() == null) {
            throw new BusinessException("商品类型为空");
        }
        StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(erpCartAddRequest.getStoreId());

        List<String> skuCodeList = new ArrayList<>();
        for (ErpCartAddSkuItem item :
                erpCartAddRequest.getProducts()) {
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("缺失sku编码");
            }
            if (item.getAmount() == null) {
                throw new BusinessException("缺失sku数量");
            }
            skuCodeList.add(item.getSkuCode());
        }

        //获取商品详情
        Map<String, ErpSkuDetail> skuDetailMap = getProductSkuDetailMap(store.getProvinceId(), store.getCityId(), skuCodeList);

        //获取当前门店购物车已有的商品
        Map<String, ErpOrderCartInfo> cartLineMap = new HashMap<>(16);
        ErpOrderCartInfo queryInfo = new ErpOrderCartInfo();
        queryInfo.setStoreId(store.getStoreId());
        queryInfo.setProductType(erpCartAddRequest.getProductType());
        List<ErpOrderCartInfo> select = erpOrderCartDao.select(queryInfo);
        if (select != null && select.size() > 0) {
            for (ErpOrderCartInfo item :
                    select) {
                cartLineMap.put(item.getSkuCode(), item);
            }
        }

        //新增的商品
        List<ErpOrderCartInfo> addList = new ArrayList<>();
        //修改数量的商品
        List<ErpOrderCartInfo> updateList = new ArrayList<>();
        //库存不足10个的商品
        List<ErpCartAddItemResponse> skuStockList = new ArrayList<>();
        for (ErpCartAddSkuItem item :
                erpCartAddRequest.getProducts()) {
            if (!skuDetailMap.containsKey(item.getSkuCode())) {
                throw new BusinessException("未找到商品" + item.getSkuCode() + "详情");
            }
            ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode());

            //购物车该商品当前数量
            int cartAmount = 0;

            if (cartLineMap.containsKey(item.getSkuCode())) {
                //购物车已经存在该商品
                ErpOrderCartInfo erpOrderCartInfo = cartLineMap.get(item.getSkuCode());
                cartAmount = erpOrderCartInfo.getAmount() + item.getAmount();
                erpOrderCartInfo.setLineCheckStatus(YesOrNoEnum.YES.getCode());
                erpOrderCartInfo.setAmount(cartAmount);
                if (StringUtils.isNotEmpty(item.getActivityId())) {
                    erpOrderCartInfo.setActivityId(item.getActivityId());
                }
                updateList.add(erpOrderCartInfo);
            } else {
                //购物车不存在，新添加一行

                cartAmount = item.getAmount();
                ErpOrderCartInfo erpOrderCartInfo = new ErpOrderCartInfo();
                erpOrderCartInfo.setCartId(IdUtil.uuid());
                erpOrderCartInfo.setStoreId(store.getStoreId());
                erpOrderCartInfo.setSpuCode(skuDetail.getSpuCode());
                erpOrderCartInfo.setSpuName(skuDetail.getSpuName());
                erpOrderCartInfo.setSkuCode(skuDetail.getSkuCode());
                erpOrderCartInfo.setSkuName(skuDetail.getSkuName());
                erpOrderCartInfo.setActivityId(item.getActivityId());
                erpOrderCartInfo.setAmount(item.getAmount());
                erpOrderCartInfo.setPrice(skuDetail.getPriceTax());
                erpOrderCartInfo.setLogo(skuDetail.getProductPicturePath());
                erpOrderCartInfo.setColor(skuDetail.getColorName());
                erpOrderCartInfo.setProductSize(skuDetail.getModelNumber());
                erpOrderCartInfo.setProductType(erpCartAddRequest.getProductType());
                erpOrderCartInfo.setCreateSource(erpCartAddRequest.getCreateSource());
                erpOrderCartInfo.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());
                erpOrderCartInfo.setLineCheckStatus(YesOrNoEnum.YES.getCode());
                erpOrderCartInfo.setZeroRemovalCoefficient(skuDetail.getZeroRemovalCoefficient());
                erpOrderCartInfo.setMaxOrderNum(skuDetail.getMaxOrderNum());
                erpOrderCartInfo.setSpec(skuDetail.getSpec());
                erpOrderCartInfo.setProductPropertyCode(skuDetail.getProductPropertyCode());
                erpOrderCartInfo.setProductPropertyName(skuDetail.getProductPropertyName());
                erpOrderCartInfo.setProductCategoryCode(skuDetail.getProductCategoryCode());
                erpOrderCartInfo.setProductCategoryName(skuDetail.getProductCategoryName());
                erpOrderCartInfo.setProductBrandCode(skuDetail.getProductBrandCode());
                erpOrderCartInfo.setProductBrandName(skuDetail.getProductBrandName());

                addList.add(erpOrderCartInfo);
            }

            if (cartAmount > skuDetail.getStockNum()) {
                throw new BusinessException("商品" + skuDetail.getSkuName() + "库存不足");
            }

            //校验数量范围和规则
            this.validateSkuQuantity(cartAmount, skuDetail.getMaxOrderNum(), skuDetail.getZeroRemovalCoefficient(), skuDetail.getSkuName());

            //库存不足10个的商品返回
            if (skuDetail.getStockNum() < 10) {
                ErpCartAddItemResponse cartAddItemResponse = new ErpCartAddItemResponse();
                cartAddItemResponse.setSkuCode(skuDetail.getSkuCode());
                cartAddItemResponse.setSkuName(skuDetail.getSkuName());
                cartAddItemResponse.setSpuCode(skuDetail.getSpuCode());
                cartAddItemResponse.setSpuName(skuDetail.getSpuName());
                cartAddItemResponse.setStockNum(skuDetail.getStockNum());
                skuStockList.add(cartAddItemResponse);
            }
        }

        if (addList.size() > 0) {
            insertCartLineList(addList, auth);
        }

        if (updateList.size() > 0) {
            updateCartLineList(updateList, auth);
        }

        ErpOrderCartAddResponse addResponse = new ErpOrderCartAddResponse();
        if (skuCodeList.size() > 0) {
            addResponse.setHasLowStockProduct(YesOrNoEnum.YES.getCode());
            addResponse.setLowStockProductList(skuStockList);
        } else {
            addResponse.setHasLowStockProduct(YesOrNoEnum.NO.getCode());
        }

        return addResponse;
    }

    @Override
    public void changeGroupCheckStatus(ErpCartChangeGroupCheckStatusRequest changeRequest, AuthToken auth) {

        if (changeRequest == null) {
            throw new BusinessException("空参数");
        }
        if (changeRequest.getStoreId() == null) {
            throw new BusinessException("缺失门店id");
        }
        if (changeRequest.getProductType() == null) {
            throw new BusinessException("缺失订单类型");
        }
        if (changeRequest.getLineCheckStatus() == null) {
            throw new BusinessException("缺失选中类型");
        } else {
            if (!YesOrNoEnum.exist(changeRequest.getLineCheckStatus())) {
                throw new BusinessException("不支持的选中类型");
            }
        }

        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(changeRequest.getStoreId());
        query.setProductType(changeRequest.getProductType());

        List<ErpOrderCartInfo> cartLineList = this.selectByProperty(query);
        if (cartLineList != null && cartLineList.size() > 0) {
            for (ErpOrderCartInfo item :
                    cartLineList) {
                item.setLineCheckStatus(changeRequest.getLineCheckStatus());
            }
            this.updateCartLineList(cartLineList, auth);
        }
    }

    @Override
    public void updateCartLineProduct(ErpCartUpdateRequest erpCartUpdateRequest, AuthToken auth) {
        if (erpCartUpdateRequest == null) {
            throw new BusinessException("空参数");
        }
        if (erpCartUpdateRequest.getCartId() == null) {
            throw new BusinessException("缺失行唯一标识");
        }
        if (erpCartUpdateRequest.getAmount() == null) {
            throw new BusinessException("缺失数量");
        } else {
            if (erpCartUpdateRequest.getAmount() < 0) {
                throw new BusinessException("商品数量不能小于0");
            }
        }
        if (erpCartUpdateRequest.getLineCheckStatus() == null) {
            throw new BusinessException("缺失选中状态");
        } else {
            if (!YesOrNoEnum.exist(erpCartUpdateRequest.getLineCheckStatus())) {
                throw new BusinessException("无效的选中状态");
            }
        }

        ErpOrderCartInfo cartLine = this.getCartLineByCartId(erpCartUpdateRequest.getCartId());
        if (cartLine == null) {
            throw new BusinessException("无效的行唯一标识");
        }
        if (!cartLine.getAmount().equals(erpCartUpdateRequest.getAmount())) {
            //如果修改了数量，判断数量范围规则
            this.validateSkuQuantity(erpCartUpdateRequest.getAmount(), cartLine.getMaxOrderNum(), cartLine.getZeroRemovalCoefficient(), cartLine.getSkuName());
        }

        cartLine.setAmount(erpCartUpdateRequest.getAmount());
        cartLine.setLineCheckStatus(erpCartUpdateRequest.getLineCheckStatus());
        cartLine.setActivityId(erpCartUpdateRequest.getActivityId());
        this.updateCartLine(cartLine, auth);
    }

    @Override
    public ErpCartQueryResponse queryErpCartList(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth) {
        if (erpCartQueryRequest == null) {
            throw new BusinessException("参数缺失");
        }
        if (erpCartQueryRequest.getStoreId() == null) {
            throw new BusinessException("缺失门店id");
        }
        if (erpCartQueryRequest.getProductType() == null) {
            throw new BusinessException("缺失订单类型");
        }

        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartQueryRequest.getStoreId());
        query.setProductType(erpCartQueryRequest.getProductType());
        query.setLineCheckStatus(YesOrNoEnum.YES.getCode());
        List<ErpOrderCartInfo> cartLineList = this.selectByProperty(query);

        //分销价汇总
        BigDecimal accountActualPrice = BigDecimal.ZERO;
        //总数量汇总
        Integer totalNumber = 0;
        if (cartLineList != null && cartLineList.size() > 0) {
            //获取门店
            StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(erpCartQueryRequest.getStoreId());
            //获取最新商品信息
            this.setNewestSkuDetail(store.getProvinceId(), store.getCityId(), cartLineList);
            for (ErpOrderCartInfo item :
                    cartLineList) {
                totalNumber += item.getAmount();
                accountActualPrice = accountActualPrice.add(item.getPrice().multiply(new BigDecimal(item.getAmount())));
            }
        }
        ErpCartQueryResponse queryResponse = new ErpCartQueryResponse();
        queryResponse.setCartInfoList(cartLineList);
        queryResponse.setTotalNumber(totalNumber);
        queryResponse.setAccountActualPrice(accountActualPrice);
        return queryResponse;
    }

    @Override
    public ErpStoreCartQueryResponse queryStoreCartList(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth) {
        if (erpCartQueryRequest == null) {
            throw new BusinessException("参数缺失");
        }
        if (erpCartQueryRequest.getStoreId() == null) {
            throw new BusinessException("缺失门店id");
        }
        if (erpCartQueryRequest.getProductType() == null) {
            throw new BusinessException("缺失订单类型");
        }

        //查询门店信息
        StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(erpCartQueryRequest.getStoreId());
        //查询A品卷使用规则code Map
        Map ruleMap=couponRuleService.couponRuleMap();
        //购物车数据查询参数封装
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartQueryRequest.getStoreId());
        query.setProductType(erpCartQueryRequest.getProductType());
        //查询购物车数据集
        List<ErpOrderCartInfo> cartLineList = this.selectByProperty(query);

        //组装楼层列表
        List<ErpCartGroupInfo> cartGroupList = new ArrayList<>();

        if (cartLineList != null && cartLineList.size() > 0) {
            //商品skuCode集合
            List<String> skuCodeList = new ArrayList<>();
            for (ErpOrderCartInfo item :
                    cartLineList) {
                skuCodeList.add(item.getSkuCode());
            }
            //查询商品详情集合
            Map<String, ErpSkuDetail> skuDetailMap = this.getProductSkuDetailMap(store.getProvinceId(), store.getCityId(), skuCodeList);

            //把库存不足的行变成非选中状态
            uncheckUnderStockLine(cartLineList, skuDetailMap, auth);

            //----------开始组装楼层----------

            //活动id 购物车商品行
            Map<String, List<ErpOrderCartInfo>> activityCartMap = new LinkedHashMap<>(16);
            //当前可用活动缓存
            Map<String, ActivityRequest> usefulActivityMap = new LinkedHashMap<>(16);
            //当前不可用的活动id缓存
            List<String> uselessActivityIdList = new ArrayList<>();

            for (ErpOrderCartInfo item :
                    cartLineList) {

                //该行是否归到活动楼层
                boolean activityItemFlag = false;

                //配送才能解析活动，其他类型强制不解析活动
                if (ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(erpCartQueryRequest.getProductType())) {
                    if (StringUtils.isNotEmpty(item.getActivityId())) {
                        //如果活动id不为空，判断活动是否有效
                        if (!uselessActivityIdList.contains(item.getActivityId())) {
                            if (usefulActivityMap.containsKey(item.getActivityId())) {
                                activityItemFlag = true;
                            } else {
                                //通过活动id查询活动信息
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
                }

                //如果当前活动是有效的，校验当前商品是否可用当前活动
                if (activityItemFlag) {
                    ActivityParameterRequest activityParameterRequest = new ActivityParameterRequest();
                    activityParameterRequest.setSkuCode(item.getSkuCode());
                    activityParameterRequest.setActivityId(item.getActivityId());
                    activityParameterRequest.setProductBrandCode(item.getProductBrandCode());
                    activityParameterRequest.setProductCategoryCode(item.getProductCategoryCode());
                    activityParameterRequest.setStoreId(store.getStoreId());
                    activityItemFlag = activityService.checkProcuct(activityParameterRequest);
                }

                if (activityItemFlag) {
                    //参加活动的数据
                    List<ErpOrderCartInfo> list = new ArrayList<>();
                    if (activityCartMap.containsKey(item.getActivityId())) {
                        list = activityCartMap.get(item.getActivityId());
                    }
                    list.add(item);
                    activityCartMap.put(item.getActivityId(), list);
                } else {
                    //非活动楼层
                    ErpCartGroupInfo cartGroupInfo = new ErpCartGroupInfo();
                    cartGroupInfo.setHasActivity(YesOrNoEnum.NO.getCode());
                    List<ErpOrderCartInfo> cartOrderList = new ArrayList<>();
                    item.setActivityPrice(item.getPrice());
                    BigDecimal totalAmount = item.getPrice().multiply(new BigDecimal(item.getAmount()));
                    item.setLineAmountTotal(totalAmount);
                    item.setLineAmountAfterActivity(totalAmount);
                    item.setLineActivityAmountTotal(totalAmount);
                    item.setLineActivityDiscountTotal(BigDecimal.ZERO);
                    cartOrderList.add(item);
                    cartGroupInfo.setCartProductList(cartOrderList);
                    cartGroupList.add(cartGroupInfo);
                }
            }

            //遍历有活动的行
            for (Map.Entry<String, List<ErpOrderCartInfo>> entry :
                    activityCartMap.entrySet()) {
                ErpCartGroupInfo cartGroupInfo = generateCartGroup(usefulActivityMap.get(entry.getKey()), store, entry.getValue());
                cartGroupList.add(cartGroupInfo);
            }
        }

        //勾选商品活动价汇总
        BigDecimal activityAmountTotal = BigDecimal.ZERO;
        //勾选商品活动优惠金额
        BigDecimal activityDiscountAmount = BigDecimal.ZERO;
        //勾选商品本品总数量
        int totalNumber = 0;

        for (ErpCartGroupInfo item :
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

            //遍历本品列表
            if (item.getCartProductList() != null && item.getCartProductList().size() > 0) {
                for (ErpOrderCartInfo productItem :
                        item.getCartProductList()) {
                    if (YesOrNoEnum.YES.getCode().equals(productItem.getLineCheckStatus())) {
                        groupAmount = groupAmount.add(productItem.getLineAmountTotal());
                        groupProductAmount = groupProductAmount.add(productItem.getLineAmountTotal());
                        groupActivityAmount = groupActivityAmount.add(productItem.getLineActivityAmountTotal());
                        groupActivityDiscountAmount = groupActivityDiscountAmount.add(productItem.getLineActivityDiscountTotal());
                        groupProductQuantity += productItem.getAmount();
                    }

                    //调用接口查询商品可选活动列表
                    ActivityParameterRequest activityParameterRequest = new ActivityParameterRequest();
                    activityParameterRequest.setStoreId(store.getStoreId());
                    activityParameterRequest.setSkuCode(productItem.getSkuCode());
                    activityParameterRequest.setProductBrandCode(productItem.getProductBrandCode());
                    activityParameterRequest.setProductCategoryCode(productItem.getProductCategoryCode());
                    List<Activity> activityList = activityService.activityList(activityParameterRequest);
                    productItem.setActivityList(activityList);

                    //判断是否可用A品券
                    if(ruleMap.containsKey(productItem.getProductPropertyCode())){
                        //可使用优惠券
                        productItem.setCouponRule(YesOrNoEnum.YES.getCode());
                    }
                }
            }

            //遍历赠品列表
            if (item.getCartGiftList() != null && item.getCartGiftList().size() > 0) {
                for (ErpOrderCartInfo giftItem :
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

            activityAmountTotal = activityAmountTotal.add(groupActivityAmount);
            totalNumber += groupProductQuantity;
            activityDiscountAmount = activityDiscountAmount.add(groupActivityDiscountAmount);
        }

        ErpStoreCartQueryResponse response = new ErpStoreCartQueryResponse();
        response.setActivityAmountTotal(activityAmountTotal);
        response.setTotalNumber(totalNumber);
        response.setCartGroupList(cartGroupList);
        response.setActivityDiscountAmount(activityDiscountAmount);


        return response;
    }

    @Override
    public ErpGenerateCartGroupTempResponse generateCartGroupTemp(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth) {
        if (erpCartQueryRequest == null) {
            throw new BusinessException("参数缺失");
        }
        if (erpCartQueryRequest.getStoreId() == null) {
            throw new BusinessException("缺失门店id");
        }
        if (erpCartQueryRequest.getProductType() == null) {
            throw new BusinessException("缺失订单类型");
        }

        //查询门店信息
        StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(erpCartQueryRequest.getStoreId());
        //查询A品卷使用规则code Map
        Map ruleMap=couponRuleService.couponRuleMap();
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartQueryRequest.getStoreId());
        query.setProductType(erpCartQueryRequest.getProductType());
        query.setLineCheckStatus(YesOrNoEnum.YES.getCode());
        List<ErpOrderCartInfo> cartLineList = this.selectByProperty(query);
        if (cartLineList == null || cartLineList.size() == 0) {
            throw new BusinessException("购物车中没有勾选的商品");
        }

        //组装楼层列表
        List<ErpCartGroupInfo> cartGroupList = new ArrayList<>();

        List<String> skuCodeList = new ArrayList<>();
        for (ErpOrderCartInfo item :
                cartLineList) {
            skuCodeList.add(item.getSkuCode());
        }
        //缓存商品详情信息
        Map<String, ErpSkuDetail> skuDetailMap = this.getProductSkuDetailMap(store.getProvinceId(), store.getCityId(), skuCodeList);
        //缓存当前剩余的库存数量
        Map<String, Integer> skuStockNumMap = new HashMap<>(16);
        for (ErpOrderCartInfo item :
                cartLineList) {
            ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode());
            if (skuDetail == null) {
                throw new BusinessException("未获取到商品" + item.getSkuName() + "信息");
            }
            item.setStockNum(skuDetail.getStockNum());
            if (item.getAmount() > skuDetail.getStockNum()) {
                throw new BusinessException("商品" + skuDetail.getSkuName() + "库存不足");
            }
            skuStockNumMap.put(skuDetail.getSkuCode(), skuDetail.getStockNum() - item.getAmount());
        }

        //----------开始组装楼层----------

        //活动id 购物车商品行
        Map<String, List<ErpOrderCartInfo>> activityCartMap = new LinkedHashMap<>(16);
        //当前可用活动缓存
        Map<String, ActivityRequest> usefulActivityMap = new LinkedHashMap<>(16);
        //当前不可用的活动id缓存
        List<String> uselessActivityIdList = new ArrayList<>();

        for (ErpOrderCartInfo item :
                cartLineList) {

            //该行是否归到活动楼层
            boolean activityItemFlag = false;

            //配送才能解析活动，其他类型强制不解析活动
            if (ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(erpCartQueryRequest.getProductType())) {
                if (StringUtils.isNotEmpty(item.getActivityId())) {
                    //如果活动id不为空，判断活动是否有效
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
            }

            //如果当前活动是有效的，校验当前商品是否可用当前活动
            if (activityItemFlag) {
                ActivityParameterRequest activityParameterRequest = new ActivityParameterRequest();
                activityParameterRequest.setSkuCode(item.getSkuCode());
                activityParameterRequest.setActivityId(item.getActivityId());
                activityParameterRequest.setProductBrandCode(item.getProductBrandCode());
                activityParameterRequest.setProductCategoryCode(item.getProductCategoryCode());
                activityParameterRequest.setStoreId(store.getStoreId());
                activityItemFlag = activityService.checkProcuct(activityParameterRequest);
            }

            if (activityItemFlag) {
                //参加活动的数据
                List<ErpOrderCartInfo> list = new ArrayList<>();
                if (activityCartMap.containsKey(item.getActivityId())) {
                    list = activityCartMap.get(item.getActivityId());
                }
                list.add(item);
                activityCartMap.put(item.getActivityId(), list);
            } else {
                //非活动楼层
                ErpCartGroupInfo cartGroupInfo = new ErpCartGroupInfo();
                cartGroupInfo.setHasActivity(YesOrNoEnum.NO.getCode());
                List<ErpOrderCartInfo> cartOrderList = new ArrayList<>();
                item.setActivityPrice(item.getPrice());
                BigDecimal totalAmount = item.getPrice().multiply(new BigDecimal(item.getAmount()));
                item.setLineAmountTotal(totalAmount);
                item.setLineAmountAfterActivity(totalAmount);
                item.setLineActivityAmountTotal(totalAmount);
                item.setLineActivityDiscountTotal(BigDecimal.ZERO);
                cartOrderList.add(item);
                cartGroupInfo.setCartProductList(cartOrderList);
                cartGroupList.add(cartGroupInfo);
            }
        }


        //遍历有活动的行
        for (Map.Entry<String, List<ErpOrderCartInfo>> entry :
                activityCartMap.entrySet()) {
            ErpCartGroupInfo cartGroupInfo = generateCartGroupWithinStock(skuDetailMap, skuStockNumMap, usefulActivityMap.get(entry.getKey()), store, entry.getValue());
            cartGroupList.add(cartGroupInfo);
        }

        //勾选商品活动价汇总
        BigDecimal activityAmountTotal = BigDecimal.ZERO;
        //勾选商品活动优惠金额
        BigDecimal activityDiscountAmount = BigDecimal.ZERO;
        //勾选商品本品总数量
        int totalNumber = 0;
        //所有勾选的商品中可使用A品券的商品活动均摊后金额汇总
        BigDecimal topTotalPrice = BigDecimal.ZERO;

        for (ErpCartGroupInfo item :
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
                for (ErpOrderCartInfo productItem :
                        item.getCartProductList()) {
                    if (YesOrNoEnum.YES.getCode().equals(productItem.getLineCheckStatus())) {
                        groupAmount = groupAmount.add(productItem.getLineAmountTotal());
                        groupProductAmount = groupProductAmount.add(productItem.getLineAmountTotal());
                        groupActivityAmount = groupActivityAmount.add(productItem.getLineActivityAmountTotal());
                        groupActivityDiscountAmount = groupActivityDiscountAmount.add(productItem.getLineActivityDiscountTotal());
                        groupProductQuantity += productItem.getAmount();

                        if(ruleMap.containsKey(productItem.getProductPropertyCode())){
                            //可使用优惠券
                            productItem.setCouponRule(YesOrNoEnum.YES.getCode());
                        }
                    }
                }
            }

            //遍历赠品列表
            if (item.getCartGiftList() != null && item.getCartGiftList().size() > 0) {
                for (ErpOrderCartInfo giftItem :
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
            topTotalPrice = topTotalPrice.add(groupTopCouponMaxTotal);
            activityDiscountAmount = activityDiscountAmount.add(groupActivityDiscountAmount);
        }

        ErpStoreCartQueryResponse response = new ErpStoreCartQueryResponse();
        response.setActivityAmountTotal(activityAmountTotal);
        response.setTotalNumber(totalNumber);
        response.setTopTotalPrice(topTotalPrice);
        response.setCartGroupList(cartGroupList);
        response.setActivityDiscountAmount(activityDiscountAmount);
        response.setStoreAddress(store.getAddress());
        response.setStoreContacts(store.getContacts());
        response.setStoreContactsPhone(store.getContactsPhone());
        response.setStoreId(store.getStoreId());
        response.setProductType(erpCartQueryRequest.getProductType());

        String redisKey = IdUtil.uuid();
        boolean set = redisService.setSeconds(redisKey, response, OrderConstant.REDIS_ORDER_CART_GROUP_TEMP_TIME);
        if (!set) {
            throw new BusinessException("生成订单结算数据失败");
        }
        ErpGenerateCartGroupTempResponse tempResponse = new ErpGenerateCartGroupTempResponse();
        tempResponse.setCartGroupTempKey(redisKey);
        return tempResponse;
    }

    @Override
    public ErpStoreCartQueryResponse queryCartGroupTemp(ErpQueryCartGroupTempRequest erpQueryCartGroupTempRequest, AuthToken auth) {
        Object o = redisService.get(erpQueryCartGroupTempRequest.getCartGroupTempKey());
        if (o == null) {
            throw new BusinessException("未找到结算数据或者结算数据已经过期");
        }

        String json = JSON.toJSONString(o);

        ErpStoreCartQueryResponse queryResponse = JSON.parseObject(json, new TypeReference<ErpStoreCartQueryResponse>() {
        });
        return queryResponse;
    }

    @Override
    public int getCartProductTotalNum(ErpCartNumQueryRequest erpCartNumQueryRequest, AuthToken auth) {
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartNumQueryRequest.getStoreId());
        query.setProductType(erpCartNumQueryRequest.getProductType());
        query.setLineCheckStatus(erpCartNumQueryRequest.getLineCheckStatus());
        List<ErpOrderCartInfo> select = this.selectByProperty(query);
        int cartProductTotalNum = 0;
        if (select != null && select.size() > 0) {
            for (ErpOrderCartInfo item :
                    select) {
                cartProductTotalNum += item.getAmount();
            }
        }
        return cartProductTotalNum;
    }

    @Override
    public StoreActivityAchieveResponse getStoreActivityAchieveDetail(StoreActivityAchieveRequest storeActivityAchieveRequest) {
        StoreActivityAchieveResponse storeActivityAchieveResponse = new StoreActivityAchieveResponse();

        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(storeActivityAchieveRequest.getStoreId());
        query.setActivityId(storeActivityAchieveRequest.getActivityId());
        query.setLineCheckStatus(Global.LINECHECKSTATUS_1);
        query.setProductType(ErpOrderTypeEnum.DISTRIBUTION.getCode());

        BigDecimal totalMoney = BigDecimal.ZERO;
        int totalCount = 0;
        List<ErpOrderCartInfo> list = this.selectByProperty(query);
        if (list != null && list.size() > 0) {
            StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(storeActivityAchieveRequest.getStoreId());
            this.setNewestSkuDetail(store.getProvinceId(), store.getCityId(), list);
            for (ErpOrderCartInfo item :
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

        if(null!=activityRules&&0<activityRules.size()){

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
        }


        storeActivityAchieveResponse.setActivity(activity);
        storeActivityAchieveResponse.setCurActivityRule(curRule);
        storeActivityAchieveResponse.setFirstActivityRule(firstRule);
        storeActivityAchieveResponse.setTotalCount(totalCount);
        storeActivityAchieveResponse.setTotalMoney(totalMoney);
        return storeActivityAchieveResponse;
    }

    @Override
    public void deleteMultipleCartLine(ErpCartDeleteMultipleRequest erpCartDeleteMultipleRequest) {
        if(null==erpCartDeleteMultipleRequest && null==erpCartDeleteMultipleRequest.getCartIds()){
            throw new BusinessException("参数为空");
        }
        for (String cartId:erpCartDeleteMultipleRequest.getCartIds()){
            deleteCartLine(cartId);
        }
    }

    /**
     * 根据活动id和本品列表解析活动并且返回一个楼层
     *
     * @param activityRequest 活动详情
     * @param store           门店信息
     * @param list            本品列表
     * @return
     */
    private ErpCartGroupInfo generateCartGroup(ActivityRequest activityRequest, StoreInfo store, List<ErpOrderCartInfo> list) {

        ErpCartGroupInfo cartGroupInfo = new ErpCartGroupInfo();
        cartGroupInfo.setCartProductList(list);

        //勾选的商品组商品数量
        Integer quantity = 0;
        //勾选的商品组分销价汇总
        BigDecimal amountTotal = BigDecimal.ZERO;
        //勾选的商品组活动价汇总，初始等于商品组分销价汇总
        BigDecimal activityAmountTotal = BigDecimal.ZERO;

        //用来缓存勾选状态的行
        List<ErpOrderCartInfo> tempList = new ArrayList<>();
        //商品组金额数量汇总
        for (ErpOrderCartInfo item :
                list) {
            //行分销金额
            BigDecimal lineAmountTotal = item.getPrice().multiply(new BigDecimal(item.getAmount()));

            if (YesOrNoEnum.YES.getCode().equals(item.getLineCheckStatus())) {
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
            //行减去活动优惠之后分摊的金额，初始赋值为原价
            item.setLineAmountAfterActivity(lineAmountTotal);
        }


        //获取活动详情
        Activity activity = activityRequest.getActivity();
        cartGroupInfo.setHasActivity(YesOrNoEnum.YES.getCode());
        cartGroupInfo.setActivity(activity);
        List<ActivityRule> activityRules = activityRequest.getActivityRules();

        List<ActivityProduct> activityProducts = activityRequest.getActivityProducts();
        Map productMap=new HashMap();
        for(ActivityProduct pro:activityProducts){
            productMap.put(pro.getSkuCode(),pro.getReduce());
        }


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
                        flag = true;
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
                    List<ErpOrderCartInfo> cartGiftList = new ArrayList<>();

                    //生成赠品行
                    for (ActivityGift giftItem :
                            giftList) {
                        //生成赠品行
                        ErpOrderCartInfo giftProductLine = createGiftProductLine(activity, giftItem, store);
                        cartGiftList.add(giftProductLine);
                    }
                    cartGroupInfo.setCartGiftList(cartGiftList);
                }

                ;
                break;
            case TYPE_3:
                //折扣

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
                        flag = true;
                    }

                    if (flag) {
                        curRule = ruleItem;
                    }
                }

                if (curRule != null) {

                    cartGroupInfo.setActivityRule(curRule);

                    //计算折扣均摊

                    //当前活动的折扣点数（百分比）
                    BigDecimal restPreferentialAmount = curRule.getPreferentialAmount();

                    if (activityAmountTotal.compareTo(BigDecimal.ZERO) > 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            ErpOrderCartInfo item = tempList.get(i);

                            //行减去活动优惠之后分摊的金额  =  当前活动的折扣点数（百分比） 乘以  商品原价（分销价） 乘以  商品数量
                            item.setLineAmountAfterActivity(restPreferentialAmount.multiply(item.getPrice()).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));
                            //行活动优惠的金额，行根据活动使该行减少的金额，前端不显示  =  商品原价（分销价）  -  行活动优惠的金额，行根据活动使该行减少的金额，前端不显示
                            item.setLineActivityDiscountTotal(item.getLineAmountTotal().subtract(item.getLineAmountAfterActivity()).setScale(2, RoundingMode.HALF_UP));
                            //设置行活动价  = 当前活动的折扣点数（百分比） 乘以  商品原价（分销价）
                            item.setActivityPrice(restPreferentialAmount.multiply(item.getPrice()).setScale(2, RoundingMode.HALF_UP));
                            //设置行活动价汇总  = 当前活动的折扣点数（百分比） 乘以  商品原价（分销价） 乘以  商品数量
//                            item.setLineActivityAmountTotal(restPreferentialAmount.multiply(item.getPrice()).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));
                        }
                    }

                }
                ;
                break;
                case TYPE_5:
                //特价

                if (activityAmountTotal.compareTo(BigDecimal.ZERO) > 0) {

                    for (int i = 0; i < tempList.size(); i++) {
                        ErpOrderCartInfo item = tempList.get(i);
                        Double reduce=0.00;
                        //通过sku拿到活动特价金额
                        if(null!=productMap.get(item.getSkuCode())){
                            reduce= Double.valueOf(productMap.get(item.getSkuCode()).toString());
                        }

                        //行减去活动优惠之后分摊的金额  =  活动特价金额 乘以  商品数量
                        item.setLineAmountAfterActivity(BigDecimal.valueOf(reduce).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));

                        //行活动优惠的金额，行根据活动使该行减少的金额，前端不显示  =   商品原价（分销价）- 减去活动优惠之后分摊的金额
                        item.setLineActivityDiscountTotal(item.getLineAmountTotal().subtract(item.getLineAmountAfterActivity()).setScale(2, RoundingMode.HALF_UP));

                        //设置行活动价  = 当前活动的折扣点数（百分比） 乘以  商品原价（分销价）
                        item.setActivityPrice(BigDecimal.valueOf(reduce).setScale(2, RoundingMode.HALF_UP));

                        //行活动价汇总  =  活动特价金额 乘以  商品数量
//                        item.setLineActivityAmountTotal(BigDecimal.valueOf(reduce).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));

                    }
                }

                break;
            default:
                ;
        }
        cartGroupInfo.setFirstActivityRule(firstRule);



        return cartGroupInfo;
    }

    /**
     * 根据活动id和本品列表解析活动并且返回一个楼层
     *
     * @param activityRequest 活动详情
     * @param store           门店信息
     * @param list            本品列表
     * @return
     */
    private ErpCartGroupInfo generateCartGroupWithinStock(Map<String, ErpSkuDetail> skuDetailMap, Map<String, Integer> skuStockNumMap, ActivityRequest activityRequest, StoreInfo store, List<ErpOrderCartInfo> list) {

        ErpCartGroupInfo cartGroupInfo = new ErpCartGroupInfo();
        cartGroupInfo.setCartProductList(list);

        //勾选的商品组商品数量
        Integer quantity = 0;
        //勾选的商品组分销价汇总
        BigDecimal amountTotal = BigDecimal.ZERO;
        //勾选的商品组活动价汇总，初始等于商品组分销价汇总
        BigDecimal activityAmountTotal = BigDecimal.ZERO;

        //用来缓存勾选状态的行
        List<ErpOrderCartInfo> tempList = new ArrayList<>();
        //商品组金额数量汇总
        for (ErpOrderCartInfo item :
                list) {
            //行分销金额
            BigDecimal lineAmountTotal = item.getPrice().multiply(new BigDecimal(item.getAmount()));

            if (YesOrNoEnum.YES.getCode().equals(item.getLineCheckStatus())) {
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
            //行减去活动优惠之后分摊的金额，初始赋值为原价
            item.setLineAmountAfterActivity(lineAmountTotal);
        }


        //获取活动详情
        Activity activity = activityRequest.getActivity();
        cartGroupInfo.setHasActivity(YesOrNoEnum.YES.getCode());
        cartGroupInfo.setActivity(activity);
        List<ActivityRule> activityRules = activityRequest.getActivityRules();

        List<ActivityProduct> activityProducts = activityRequest.getActivityProducts();
        Map productMap=new HashMap();
        for(ActivityProduct pro:activityProducts){
            productMap.put(pro.getSkuCode(),pro.getReduce());
        }

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
                        flag = true;
                    }

                    if (flag) {
                        curRule = ruleItem;
                    }
                }

                if (curRule != null) {

                    cartGroupInfo.setActivityRule(curRule);

                    //计算满减均摊

                    //当前剩余满减的金额
                    BigDecimal restPreferentialAmount = curRule.getPreferentialAmount();

                    if (activityAmountTotal.compareTo(BigDecimal.ZERO) > 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            ErpOrderCartInfo item = tempList.get(i);
                            if (i == tempList.size() - 1) {
                                //最后一行，用减法避免误差
                                item.setLineActivityDiscountTotal(restPreferentialAmount);
                            } else {
                                //行活动优惠的金额，行根据活动使该行减少的金额，前端不显示 = 行活动价汇总 除以  勾选的商品组活动价汇总，初始等于商品组分销价汇总  乘以   活动规则的优惠金额、优惠件数、折扣点数（百分比）
                                BigDecimal lineActivityDiscountTotal = item.getLineActivityAmountTotal().divide(activityAmountTotal, 6, RoundingMode.HALF_UP).multiply(curRule.getPreferentialAmount()).setScale(2, RoundingMode.HALF_UP);
                                item.setLineActivityDiscountTotal(lineActivityDiscountTotal);
                                restPreferentialAmount = restPreferentialAmount.subtract(lineActivityDiscountTotal);
                            }
                            item.setLineAmountAfterActivity(item.getLineActivityAmountTotal().subtract(item.getLineActivityDiscountTotal()).setScale(2, RoundingMode.HALF_UP));
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
                        flag = true;
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
                    List<ErpOrderCartInfo> cartGiftList = new ArrayList<>();

                    //生成赠品行
                    for (ActivityGift giftItem :
                            giftList) {
                        //生成赠品行
                        ErpOrderCartInfo giftProductLine = createGiftProductLineWithinStock(activity, giftItem, store, skuDetailMap, skuStockNumMap, true);
                        if (giftProductLine != null) {
                            cartGiftList.add(giftProductLine);
                            amountTotal = amountTotal.add(giftProductLine.getPrice().multiply(new BigDecimal(giftProductLine.getAmount())));
                        }
                    }

                    if (cartGiftList.size() > 0) {
                        //本品+赠品的list，用来计算活动价格均摊
                        List<ErpOrderCartInfo> productShareList = new ArrayList<>();
                        productShareList.addAll(tempList);
                        productShareList.addAll(cartGiftList);

                        BigDecimal restActivityAmountTotal = activityAmountTotal;

                        if (amountTotal.compareTo(BigDecimal.ZERO) > 0) {
                            for (int i = 0; i < productShareList.size(); i++) {
                                ErpOrderCartInfo item = productShareList.get(i);
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
                    }
                    cartGroupInfo.setCartGiftList(cartGiftList);
                }

                ;
                break;
            case TYPE_3:
                //折扣

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
                        flag = true;
                    }

                    if (flag) {
                        curRule = ruleItem;
                    }
                }

                if (curRule != null) {

                    cartGroupInfo.setActivityRule(curRule);

                    //计算折扣均摊

                    //当前活动的折扣点数（百分比）
                    BigDecimal restPreferentialAmount = curRule.getPreferentialAmount();

                    if (activityAmountTotal.compareTo(BigDecimal.ZERO) > 0) {
                        for (int i = 0; i < tempList.size(); i++) {
                            ErpOrderCartInfo item = tempList.get(i);

                            //行减去活动优惠之后分摊的金额  =  当前活动的折扣点数（百分比） 乘以  商品原价（分销价） 乘以  商品数量
                            item.setLineAmountAfterActivity(restPreferentialAmount.multiply(item.getPrice()).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));
                            //行活动优惠的金额，行根据活动使该行减少的金额，前端不显示  =  商品原价（分销价）  -  行活动优惠的金额，行根据活动使该行减少的金额，前端不显示
                            item.setLineActivityDiscountTotal(item.getLineAmountTotal().subtract(item.getLineAmountAfterActivity()).setScale(2, RoundingMode.HALF_UP));
                            //设置行活动价  = 当前活动的折扣点数（百分比） 乘以  商品原价（分销价）
                            item.setActivityPrice(restPreferentialAmount.multiply(item.getPrice()).setScale(2, RoundingMode.HALF_UP));
                            //设置行活动价汇总  = 当前活动的折扣点数（百分比） 乘以  商品原价（分销价） 乘以  商品数量
//                            item.setLineActivityAmountTotal(restPreferentialAmount.multiply(item.getPrice()).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));
                        }
                    }

                }
                ;
                break;
            case TYPE_5:
                //特价

                if (activityAmountTotal.compareTo(BigDecimal.ZERO) > 0) {

                    for (int i = 0; i < tempList.size(); i++) {
                        ErpOrderCartInfo item = tempList.get(i);
                        Double reduce=0.00;
                        //通过sku拿到活动特价金额
                        if(null!=productMap.get(item.getSkuCode())){
                            reduce= Double.valueOf(productMap.get(item.getSkuCode()).toString());
                        }

                        //行减去活动优惠之后分摊的金额  =  活动特价金额 乘以  商品数量
                        item.setLineAmountAfterActivity(BigDecimal.valueOf(reduce).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));

                        //行活动优惠的金额，行根据活动使该行减少的金额，前端不显示  =   商品原价（分销价）- 减去活动优惠之后分摊的金额
                        item.setLineActivityDiscountTotal(item.getLineAmountTotal().subtract(item.getLineAmountAfterActivity()).setScale(2, RoundingMode.HALF_UP));

                        //设置行活动价  = 当前活动的折扣点数（百分比） 乘以  商品原价（分销价）
                        item.setActivityPrice(BigDecimal.valueOf(reduce).setScale(2, RoundingMode.HALF_UP));

                        //行活动价汇总  =  活动特价金额 乘以  商品数量
//                        item.setLineActivityAmountTotal(BigDecimal.valueOf(reduce).multiply(new BigDecimal(item.getAmount())).setScale(2, RoundingMode.HALF_UP));

                    }
                }

                break;
            default:
                ;
        }
        cartGroupInfo.setFirstActivityRule(firstRule);



        return cartGroupInfo;
    }

    /**
     * 生成赠品行，购物车的查询，不考虑库存
     *
     * @param activity
     * @param rule
     * @param store
     * @return
     */
    private ErpOrderCartInfo createGiftProductLine(Activity activity, ActivityGift rule, StoreInfo store) {

        ErpSkuDetail skuDetail = bridgeProductService.getProductSkuDetail(store.getProvinceId(), store.getCityId(), OrderConstant.SELECT_PRODUCT_COMPANY_CODE, rule.getSkuCode());

        ErpOrderCartInfo erpOrderCartInfo = new ErpOrderCartInfo();
        erpOrderCartInfo.setCartId(IdUtil.uuid());
        erpOrderCartInfo.setStoreId(store.getStoreId());
        erpOrderCartInfo.setSpuCode(skuDetail.getSpuCode());
        erpOrderCartInfo.setSpuName(skuDetail.getSpuName());
        erpOrderCartInfo.setSkuCode(skuDetail.getSkuCode());
        erpOrderCartInfo.setSkuName(skuDetail.getSkuName());
        erpOrderCartInfo.setActivityId(activity.getActivityId());
        erpOrderCartInfo.setAmount(rule.getNumbers());
        erpOrderCartInfo.setPrice(skuDetail.getPriceTax());
        erpOrderCartInfo.setLogo(skuDetail.getProductPicturePath());
        erpOrderCartInfo.setColor(skuDetail.getColorName());
        erpOrderCartInfo.setProductSize(skuDetail.getModelNumber());
        erpOrderCartInfo.setProductGift(ErpProductGiftEnum.GIFT.getCode());
        erpOrderCartInfo.setLineCheckStatus(YesOrNoEnum.YES.getCode());
        erpOrderCartInfo.setZeroRemovalCoefficient(skuDetail.getZeroRemovalCoefficient());
        erpOrderCartInfo.setSpec(skuDetail.getSpec());
        erpOrderCartInfo.setProductPropertyCode(skuDetail.getProductPropertyCode());
        erpOrderCartInfo.setProductPropertyName(skuDetail.getProductPropertyName());
        erpOrderCartInfo.setProductCategoryCode(skuDetail.getProductCategoryCode());
        erpOrderCartInfo.setProductCategoryName(skuDetail.getProductCategoryName());
        erpOrderCartInfo.setProductBrandCode(skuDetail.getProductBrandCode());
        erpOrderCartInfo.setProductBrandName(skuDetail.getProductBrandName());
        erpOrderCartInfo.setTagInfoList(skuDetail.getTagInfoList());
        BigDecimal multiplyAmountTotal = erpOrderCartInfo.getPrice().multiply(new BigDecimal(erpOrderCartInfo.getAmount()));
        erpOrderCartInfo.setLineAmountTotal(multiplyAmountTotal);
        erpOrderCartInfo.setLineActivityAmountTotal(BigDecimal.ZERO);
        erpOrderCartInfo.setLineActivityDiscountTotal(BigDecimal.ZERO);
        erpOrderCartInfo.setLineAmountAfterActivity(BigDecimal.ZERO);
        erpOrderCartInfo.setGiftGiveType(ErpProductGiftGiveTypeEnum.TYPE_1.getCode());
        return erpOrderCartInfo;
    }

    /**
     * 生成赠品行，要考虑库存不足的情况
     *
     * @param activity       活动主题
     * @param rule           活动梯度里的赠品
     * @param store          门店
     * @param skuDetailMap   已有的商品详情信息
     * @param skuStockNumMap 已有的商品剩余库存数量
     * @param withinStock    是否考虑库存
     * @return
     */
    private ErpOrderCartInfo createGiftProductLineWithinStock(Activity activity, ActivityGift rule, StoreInfo store, Map<String, ErpSkuDetail> skuDetailMap, Map<String, Integer> skuStockNumMap, boolean withinStock) {

        ErpSkuDetail skuDetail = null;
        if (skuDetailMap.containsKey(rule.getSkuCode())) {
            skuDetail = skuDetailMap.get(rule.getSkuCode());
        } else {
            skuDetail = bridgeProductService.getProductSkuDetail(store.getProvinceId(), store.getCityId(), OrderConstant.SELECT_PRODUCT_COMPANY_CODE, rule.getSkuCode());
            if (skuDetail != null) {
                skuDetailMap.put(skuDetail.getSkuCode(), skuDetail);
                skuStockNumMap.put(skuDetail.getSkuCode(), skuDetail.getStockNum());
            }
        }

        ErpOrderCartInfo erpOrderCartInfo = null;
        if (skuDetail != null) {
            int amount = rule.getNumbers();
            if (withinStock) {
                int stockNum = skuStockNumMap.get(skuDetail.getSkuCode());
                if (amount > stockNum) {
                    amount = stockNum;
                }
                skuStockNumMap.put(skuDetail.getSkuCode(), stockNum - amount);
            }
            if (amount > 0) {
                erpOrderCartInfo = new ErpOrderCartInfo();
                erpOrderCartInfo.setCartId(IdUtil.uuid());
                erpOrderCartInfo.setStoreId(store.getStoreId());
                erpOrderCartInfo.setSpuCode(skuDetail.getSpuCode());
                erpOrderCartInfo.setSpuName(skuDetail.getSpuName());
                erpOrderCartInfo.setSkuCode(skuDetail.getSkuCode());
                erpOrderCartInfo.setSkuName(skuDetail.getSkuName());
                erpOrderCartInfo.setActivityId(activity.getActivityId());
                erpOrderCartInfo.setAmount(amount);
                erpOrderCartInfo.setPrice(skuDetail.getPriceTax());
                erpOrderCartInfo.setLogo(skuDetail.getProductPicturePath());
                erpOrderCartInfo.setColor(skuDetail.getColorName());
                erpOrderCartInfo.setProductSize(skuDetail.getModelNumber());
                erpOrderCartInfo.setProductGift(ErpProductGiftEnum.GIFT.getCode());
                erpOrderCartInfo.setLineCheckStatus(YesOrNoEnum.YES.getCode());
                erpOrderCartInfo.setZeroRemovalCoefficient(skuDetail.getZeroRemovalCoefficient());
                erpOrderCartInfo.setSpec(skuDetail.getSpec());
                erpOrderCartInfo.setProductPropertyCode(skuDetail.getProductPropertyCode());
                erpOrderCartInfo.setProductPropertyName(skuDetail.getProductPropertyName());
                erpOrderCartInfo.setProductCategoryCode(skuDetail.getProductCategoryCode());
                erpOrderCartInfo.setProductCategoryName(skuDetail.getProductCategoryName());
                erpOrderCartInfo.setProductBrandCode(skuDetail.getProductBrandCode());
                erpOrderCartInfo.setProductBrandName(skuDetail.getProductBrandName());
                erpOrderCartInfo.setTagInfoList(skuDetail.getTagInfoList());
                BigDecimal multiplyAmountTotal = erpOrderCartInfo.getPrice().multiply(new BigDecimal(amount));
                erpOrderCartInfo.setLineAmountTotal(multiplyAmountTotal);
                erpOrderCartInfo.setLineActivityAmountTotal(BigDecimal.ZERO);
                erpOrderCartInfo.setLineActivityDiscountTotal(BigDecimal.ZERO);
                erpOrderCartInfo.setLineAmountAfterActivity(BigDecimal.ZERO);
                erpOrderCartInfo.setGiftGiveType(ErpProductGiftGiveTypeEnum.TYPE_1.getCode());
            }
        }
        return erpOrderCartInfo;
    }

    /**
     * 把库存不足的行变成非选中状态
     * 如果没有库存不足的行，cartLineList返回原来的数据
     * 如果有库存不足的行，cartLineList返回变更后的数据
     *
     * @param cartLineList 购物车已有的行
     * @param skuDetailMap 购物车商品sku详情
     */
    private void uncheckUnderStockLine(List<ErpOrderCartInfo> cartLineList, Map<String, ErpSkuDetail> skuDetailMap, AuthToken auth) {

        List<ErpOrderCartInfo> updateList = new ArrayList<>();
        for (ErpOrderCartInfo item :
                cartLineList) {
            ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode());
            if (skuDetail == null || item.getAmount() > skuDetail.getStockNum()) {
                item.setLineCheckStatus(YesOrNoEnum.NO.getCode());
                updateList.add(item);

                if (skuDetail == null) {
                    item.setCartLineStatus(ErpCartLineStatusEnum.NOT_ALLOWED.getCode());
                } else if (item.getAmount() > skuDetail.getStockNum()) {
                    item.setCartLineStatus(ErpCartLineStatusEnum.UNDER_STOCK.getCode());
                }
            }
            if (skuDetail != null) {
                item.setStockNum(skuDetail.getStockNum());
                item.setIsSale(skuDetail.getIsSale());
            }
        }

        if (updateList.size() > 0) {
            this.updateCartLineList(updateList, auth);
        }

    }

    /**
     * 获取sku详情，返回map
     *
     * @param provinceCode 省编码
     * @param cityCode     市编码
     * @param skuCodeList  sku编码list
     * @return
     */
    private Map<String, ErpSkuDetail> getProductSkuDetailMap(String provinceCode, String cityCode, List<String> skuCodeList) {
        Map<String, ErpSkuDetail> skuDetailMap = new HashMap<>(16);
        List<ErpSkuDetail> productSkuDetailList = bridgeProductService.getProductSkuDetailList(provinceCode, cityCode, OrderConstant.SELECT_PRODUCT_COMPANY_CODE, skuCodeList);
        for (ErpSkuDetail item :
                productSkuDetailList) {
            skuDetailMap.put(item.getSkuCode(), item);
        }
        return skuDetailMap;
    }

    /**
     * 获取最新的商品价格数据
     *
     * @param provinceCode
     * @param cityCode
     * @param cartList
     */
    private void setNewestSkuDetail(String provinceCode, String cityCode, List<ErpOrderCartInfo> cartList) {
        if (cartList != null && cartList.size() > 0) {

            List<String> skuCodeList = new ArrayList<>();
            for (ErpOrderCartInfo item :
                    cartList) {
                skuCodeList.add(item.getSkuCode());
            }
            Map<String, ErpSkuDetail> skuDetailMap = this.getProductSkuDetailMap(provinceCode, cityCode, skuCodeList);

            for (ErpOrderCartInfo item :
                    cartList) {
                ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode());
                if (skuDetail != null) {
                    item.setLogo(skuDetail.getProductPicturePath());
                    item.setPrice(skuDetail.getPriceTax());
                    item.setTagInfoList(skuDetail.getTagInfoList());
                    item.setStockNum(skuDetail.getStockNum());
                } else {
                    //TODO 未查询到商品信息，需要做哪些处理
                }
            }
        }
    }

    /**
     * 校验购物车数量规则
     *
     * @param amount                 目标数量
     * @param maxOrderNum            最大订购量
     * @param zeroRemovalCoefficient 交易倍数
     * @param skuName                sku名称
     */
    private void validateSkuQuantity(int amount, Integer maxOrderNum, Integer zeroRemovalCoefficient, String skuName) {
        if (maxOrderNum != null && maxOrderNum > 0) {
            if (amount > maxOrderNum) {
                throw new BusinessException("商品" + skuName + "数量超过最大订购量" + maxOrderNum);
            }
        }
        if (zeroRemovalCoefficient != null && zeroRemovalCoefficient > 1) {
            if (amount % zeroRemovalCoefficient != 0) {
                throw new BusinessException("商品" + skuName + "商品数量必须是" + zeroRemovalCoefficient + "的倍数");
            }
        }
    }
}
