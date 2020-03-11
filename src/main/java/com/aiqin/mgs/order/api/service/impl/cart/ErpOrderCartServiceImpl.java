package com.aiqin.mgs.order.api.service.impl.cart;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.dao.cart.ErpOrderCartDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.request.cart.*;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartAddItemResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartQueryResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpOrderCartAddResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.cart.ErpOrderCartService;
import com.aiqin.mgs.order.api.service.order.ErpOrderRequestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class ErpOrderCartServiceImpl implements ErpOrderCartService {

    @Resource
    private ErpOrderCartDao erpOrderCartDao;

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private ErpOrderRequestService erpOrderRequestService;

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
        Map<String, ErpSkuDetail> skuDetailMap = getProductSkuDetailList(store.getProvinceId(), store.getCityId(), skuCodeList);

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
                erpOrderCartInfo.setColor(skuDetail.getColor());
                erpOrderCartInfo.setProductSize(skuDetail.getProductSize());
                erpOrderCartInfo.setProductType(erpCartAddRequest.getProductType());
                erpOrderCartInfo.setCreateSource(erpCartAddRequest.getCreateSource());
                erpOrderCartInfo.setProductGift(ErpProductGiftEnum.PRODUCT.getCode());
                erpOrderCartInfo.setLineCheckStatus(YesOrNoEnum.YES.getCode());
                erpOrderCartInfo.setZeroRemovalCoefficient(skuDetail.getZeroRemovalCoefficient());
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

        cartLine.setAmount(erpCartUpdateRequest.getAmount());
        cartLine.setLineCheckStatus(erpCartUpdateRequest.getLineCheckStatus());
        cartLine.setActivityId(erpCartUpdateRequest.getActivityId());
        this.updateCartLine(cartLine, auth);
    }

    @Override
    public ErpCartQueryResponse queryErpCartList(ErpCartQueryRequest erpCartQueryRequest) {
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

        return null;
    }

    private Map<String, ErpSkuDetail> getProductSkuDetailList(String provinceCode, String cityCode, List<String> skuCodeList) {
        Map<String, ErpSkuDetail> skuDetailMap = new HashMap<>(16);
        List<ErpSkuDetail> productSkuDetailList = bridgeProductService.getProductSkuDetailList(provinceCode, cityCode, OrderConstant.SELECT_PRODUCT_COMPANY_CODE, skuCodeList);
        for (ErpSkuDetail item :
                productSkuDetailList) {
            skuDetailMap.put(item.getSkuCode(), item);
        }
        return skuDetailMap;
    }

}
