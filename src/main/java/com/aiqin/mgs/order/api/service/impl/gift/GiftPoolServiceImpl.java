package com.aiqin.mgs.order.api.service.impl.gift;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.dao.gift.ErpOrderGiftPoolCartDao;
import com.aiqin.mgs.order.api.dao.gift.GiftPoolDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartAddSkuItem;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartAddItemResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpOrderCartAddResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author csf
 */
@Service
public class GiftPoolServiceImpl implements GiftPoolService {


    private static final Logger LOGGER = LoggerFactory.getLogger(GiftPoolServiceImpl.class);

    @Resource
    private GiftPoolDao giftPoolDao;

    @Resource
    private ErpOrderGiftPoolCartDao erpOrderGiftPoolCartDao;

    @Resource
    private ErpOrderRequestService erpOrderRequestService;

    @Resource
    private BridgeProductService bridgeProductService;




    @Override
    public HttpResponse add(GiftPool giftPool) {
        LOGGER.info("添加兑换赠品池赠品 add 参数 giftPool 为：{}", giftPool);
        HttpResponse httpResponse=HttpResponse.success();
        giftPoolDao.add(giftPool);
        //TODO  此处需调用供应链接口，通过skuCode查询仓库信息，并记录表
        return httpResponse;
    }

    @Override
    public HttpResponse<PageResData<GiftPool>> getGiftPoolList(GiftPool giftPool) {
        LOGGER.info("查询赠品池列表 getGiftPoolList 参数 giftPool 为：{}", giftPool);
        HttpResponse httpResponse=HttpResponse.success();
        PageResData pageResData=new PageResData();
        List<GiftPool> giftPoolList=giftPoolDao.getGiftPoolList(giftPool);
        int totalNum=giftPoolDao.getTotalNum(giftPool);
        pageResData.setDataList(giftPoolList);
        pageResData.setTotalCount(totalNum);
        httpResponse.setData(pageResData);
        return httpResponse;
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
        List<ErpOrderCartInfo> select = erpOrderGiftPoolCartDao.select(queryInfo);
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

    private void insertCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken) {
        for (ErpOrderCartInfo item :
                list) {
            insertCartLine(item, authToken);
        }
    }

    private void insertCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken) {
        erpOrderCartInfo.setCreateById(authToken.getPersonId());
        erpOrderCartInfo.setCreateByName(authToken.getPersonName());
        erpOrderCartInfo.setUpdateById(authToken.getPersonId());
        erpOrderCartInfo.setUpdateByName(authToken.getPersonName());
        erpOrderGiftPoolCartDao.insert(erpOrderCartInfo);
    }

    private void updateCartLineList(List<ErpOrderCartInfo> list, AuthToken authToken) {
        for (ErpOrderCartInfo item :
                list) {
            updateCartLine(item, authToken);
        }
    }

    private void updateCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken) {
        erpOrderCartInfo.setCreateById(authToken.getPersonId());
        erpOrderCartInfo.setCreateByName(authToken.getPersonName());
        erpOrderCartInfo.setUpdateById(authToken.getPersonId());
        erpOrderCartInfo.setUpdateByName(authToken.getPersonName());
        erpOrderGiftPoolCartDao.updateByPrimaryKey(erpOrderCartInfo);
    }

}