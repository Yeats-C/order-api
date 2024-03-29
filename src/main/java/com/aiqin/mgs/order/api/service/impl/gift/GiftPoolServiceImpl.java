package com.aiqin.mgs.order.api.service.impl.gift;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpProductGiftEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.dao.gift.ComplimentaryWarehouseCorrespondenceDao;
import com.aiqin.mgs.order.api.dao.gift.ErpOrderGiftPoolCartDao;
import com.aiqin.mgs.order.api.dao.gift.GiftPoolDao;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.StoreInfo;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.po.gift.ComplimentaryWarehouseCorrespondence;
import com.aiqin.mgs.order.api.domain.po.gift.GiftCartQueryResponse;
import com.aiqin.mgs.order.api.domain.po.gift.GiftPool;
import com.aiqin.mgs.order.api.domain.request.cart.*;
import com.aiqin.mgs.order.api.domain.request.gift.GiftCartUpdateRequest;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.request.product.StockBatchRespVO;
import com.aiqin.mgs.order.api.domain.request.stock.ProductSkuStockRespVo;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartAddItemResponse;
import com.aiqin.mgs.order.api.domain.response.cart.ErpCartQueryResponse;
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
import java.math.BigDecimal;
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

    @Resource
    private ComplimentaryWarehouseCorrespondenceDao correspondenceDao;




    @Override
    public HttpResponse add(GiftPool giftPool) {
        LOGGER.info("添加兑换赠品池赠品 add 参数 giftPool 为：{}", giftPool);
        HttpResponse httpResponse=HttpResponse.success();
        //通过skuCode查询赠品池是否已存在此赠品信息
        List<GiftPool> giftPoolList= giftPoolDao.getGiftPoolList(giftPool);
        if(null!=giftPoolList&&giftPoolList.size()>0){
            return HttpResponse.failure(ResultCode.GIFT_POOL_ALREADY_EXISTS);
        }

        giftPoolDao.add(giftPool);

        List<String> skuCodes=new ArrayList<>();
        skuCodes.add(giftPool.getSkuCode());
        List<ProductSkuStockRespVo> stockRespVoList=bridgeProductService.findStockDetail(skuCodes);
        if(null==stockRespVoList || stockRespVoList.size()<=0){
            throw new BusinessException("查询供应链 根据skucode获取库存详情 /search/spu/findStockDetail 接口失败，参数为"+giftPool.getSkuCode());
        }
        ProductSkuStockRespVo respVo=stockRespVoList.get(0);
        if(null==respVo || null==respVo.getStockBatchRespVOList() || respVo.getStockBatchRespVOList().size()<=0){
            throw new BusinessException("查询供应链 根据skucode获取库存详情 /search/spu/findStockDetail 接口失败，该sku无库存，无法加入赠品池，参数为"+giftPool.getSkuCode());
        }
        List<ComplimentaryWarehouseCorrespondence> correspondenceList=new ArrayList<>();
        for(StockBatchRespVO stockBatchRespVO:respVo.getStockBatchRespVOList()){
            ComplimentaryWarehouseCorrespondence correspondence=new ComplimentaryWarehouseCorrespondence();
            correspondence.setSkuCode(stockBatchRespVO.getSkuCode());
            correspondence.setWarehouseCode(stockBatchRespVO.getTransportCenterCode());
            correspondenceList.add(correspondence);
        }
        correspondenceDao.insertList(correspondenceList);
        return httpResponse;
    }

    @Override
    public HttpResponse<PageResData<GiftPool>> getGiftPoolList(GiftPool giftPool) {
        LOGGER.info("查询赠品池列表 getGiftPoolList 参数 giftPool 为：{}", giftPool);
        HttpResponse httpResponse=HttpResponse.success();
        PageResData pageResData=new PageResData();
        List<GiftPool> giftPoolList=giftPoolDao.getGiftPoolList(giftPool);
        int totalNum=giftPoolDao.getTotalNum(giftPool);

        List<String> skuCodes=new ArrayList<>();
        for(GiftPool gift:giftPoolList){
            skuCodes.add(gift.getSkuCode());
        }
        List<ProductSkuStockRespVo> stockRespVoList=bridgeProductService.findStockDetail(skuCodes);

        for(ProductSkuStockRespVo stockRespVo:stockRespVoList){

            for (GiftPool gift:giftPoolList){

                if(stockRespVo.getSkuCode().equals(gift.getSkuCode())){

                    gift.setStockRespVOS(stockRespVo.getStockBatchRespVOList());
                }
            }
        }

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
        if (erpCartAddRequest.getStoreId() == null) {
            throw new BusinessException("门店id为空");
        }
        if (erpCartAddRequest.getProductType() == null) {
            throw new BusinessException("商品类型为空");
        }
        StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(erpCartAddRequest.getStoreId());

        List<ProductSkuRequest2> productSkuRequest2List=new ArrayList<>();
        for (ErpCartAddSkuItem item :
                erpCartAddRequest.getProducts()) {
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("缺失sku编码");
            }
            if (item.getAmount() == null) {
                throw new BusinessException("缺失sku数量");
            }
            ProductSkuRequest2 productSkuRequest2=new ProductSkuRequest2();
            productSkuRequest2.setSkuCode(item.getSkuCode());
            productSkuRequest2.setBatchInfoCode(item.getBatchInfoCode());
            if(null==item.getWarehouseTypeCode()){
                item.setWarehouseTypeCode("1");
            }
            productSkuRequest2.setWarehouseTypeCode(item.getWarehouseTypeCode());
            productSkuRequest2List.add(productSkuRequest2);
        }

        //获取商品详情
        Map<String, ErpSkuDetail> skuDetailMap = bridgeProductService.getProductSkuDetailMap(store.getProvinceId(), store.getCityId(), productSkuRequest2List);

        //获取当前门店购物车已有的商品
        Map<String, ErpOrderCartInfo> cartLineMap = new HashMap<>(16);
        ErpOrderCartInfo queryInfo = new ErpOrderCartInfo();
        queryInfo.setStoreId(store.getStoreId());
        queryInfo.setProductType(erpCartAddRequest.getProductType());
        List<ErpOrderCartInfo> select = erpOrderGiftPoolCartDao.select(queryInfo);
        //购物车里商品总金额
        BigDecimal allAmount=BigDecimal.ZERO;
        if (select != null && select.size() > 0) {
            for (ErpOrderCartInfo item :
                    select) {
                cartLineMap.put(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode(), item);
                allAmount=allAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getAmount())));
            }
        }
        //查詢門店赠品额度
        BigDecimal availableGiftQuota=bridgeProductService.getStoreAvailableGiftGuota(store.getStoreId());



        //新增的商品
        List<ErpOrderCartInfo> addList = new ArrayList<>();
        //修改数量的商品
        List<ErpOrderCartInfo> updateList = new ArrayList<>();
        //库存不足10个的商品
        List<ErpCartAddItemResponse> skuStockList = new ArrayList<>();
        for (ErpCartAddSkuItem item :
                erpCartAddRequest.getProducts()) {
            if (!skuDetailMap.containsKey(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode())) {
                throw new BusinessException("未找到商品" + item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode() + "详情");
            }
            ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());

            //购物车该商品当前数量
            int cartAmount = 0;

            if (cartLineMap.containsKey(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode())) {
                //购物车已经存在该商品
                ErpOrderCartInfo erpOrderCartInfo = cartLineMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());
                cartAmount = erpOrderCartInfo.getAmount() + item.getAmount();
                erpOrderCartInfo.setLineCheckStatus(YesOrNoEnum.YES.getCode());
                erpOrderCartInfo.setAmount(cartAmount);
                if (StringUtils.isNotEmpty(item.getActivityId())) {
                    erpOrderCartInfo.setActivityId(item.getActivityId());
                }

                allAmount=allAmount.add(erpOrderCartInfo.getPrice().multiply(BigDecimal.valueOf(erpOrderCartInfo.getAmount())));
                //比较当前购物车总金额与赠品额度
                if(allAmount.compareTo(availableGiftQuota)>0){
                    throw new BusinessException("超出赠品额度，不可增加");
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
                //兑换赠品池商品的分销价取熙耘中商品基本信息中的厂商指导价
                erpOrderCartInfo.setPrice(skuDetail.getManufacturerGuidePrice());
                erpOrderCartInfo.setLogo(skuDetail.getProductPicturePath());
                erpOrderCartInfo.setColor(skuDetail.getColorName());
                erpOrderCartInfo.setProductSize(skuDetail.getModelNumber());
                erpOrderCartInfo.setProductType(erpCartAddRequest.getProductType());
                erpOrderCartInfo.setCreateSource(erpCartAddRequest.getCreateSource());
                erpOrderCartInfo.setProductGift(ErpProductGiftEnum.JIFEN.getCode());
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

                //增加批次信息
                if(null!= skuDetail.getBatchList()&&skuDetail.getBatchList().size()>0&&null!=skuDetail.getBatchList().get(0).getBatchInfoCode()){
                    //增加批次信息
                    erpOrderCartInfo.setBatchCode(skuDetail.getBatchList().get(0).getBatchCode());
                    erpOrderCartInfo.setBatchInfoCode(skuDetail.getBatchList().get(0).getBatchInfoCode());
                    erpOrderCartInfo.setBatchDate(skuDetail.getBatchList().get(0).getBatchDate());
                    erpOrderCartInfo.setWarehouseTypeCode(item.getWarehouseTypeCode());
                }
                allAmount=allAmount.add(erpOrderCartInfo.getPrice().multiply(BigDecimal.valueOf(erpOrderCartInfo.getAmount())));
                //比较当前购物车总金额与赠品额度
                if(allAmount.compareTo(availableGiftQuota)>0){
                    throw new BusinessException("超出赠品额度，不可增加");
                }
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
        if (skuStockList.size() > 0) {
            addResponse.setHasLowStockProduct(YesOrNoEnum.YES.getCode());
            addResponse.setLowStockProductList(skuStockList);
        } else {
            addResponse.setHasLowStockProduct(YesOrNoEnum.NO.getCode());
        }

        return addResponse;
    }

    @Override
    public HttpResponse updateUseStatus(GiftPool giftPool) {
        LOGGER.info("修改兑换赠品池赠品状态 updateUseStatus 参数 giftPool 为：{}", giftPool);
        if(null==giftPool.getId() || null==giftPool.getUseStatus()){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        HttpResponse httpResponse=HttpResponse.success();
        giftPoolDao.updateUseStatus(giftPool);
        return httpResponse;

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

    private void  updateCartLine(ErpOrderCartInfo erpOrderCartInfo, AuthToken authToken) {
        erpOrderCartInfo.setCreateById(authToken.getPersonId());
        erpOrderCartInfo.setCreateByName(authToken.getPersonName());
        erpOrderCartInfo.setUpdateById(authToken.getPersonId());
        erpOrderCartInfo.setUpdateByName(authToken.getPersonName());
        erpOrderGiftPoolCartDao.updateByPrimaryKey(erpOrderCartInfo);
    }

    @Override
    public HttpResponse<GiftCartQueryResponse> getGiftPoolListByStoreId(GiftPool giftPool) {
        LOGGER.info("爱掌柜通过门店id及筛选项查询赠品池列表 getGiftPoolListByStoreId 参数 giftPool 为：{}", giftPool);
        if(null==giftPool.getStoreId()){
            throw new BusinessException("缺失门店id--store_id");
        }else if(null==giftPool.getProductType()){
            throw new BusinessException("缺失订单类型--product_type");
        }
        HttpResponse httpResponse=HttpResponse.success();
        GiftCartQueryResponse giftCartQueryResponse=new GiftCartQueryResponse();
        //通过门店id查询门店省市信息
        StoreInfo store = erpOrderRequestService.getStoreInfoByStoreId(giftPool.getStoreId());
        if(StringUtils.isEmpty(store.getProvinceId())||StringUtils.isEmpty(store.getCityId())){
            return HttpResponse.failure(ResultCode.PROVINCE_AND_CITY_INFORMATION_IS_EMPTY);
        }

        //总之，应该拿到一个仓库list数据，string类型
        List<String> warehouseCodeList=bridgeProductService.findTransportCenter(store.getProvinceId(),store.getCityId());
        giftPool.setWarehouseCodeList(warehouseCodeList);

        List<GiftPool> giftPoolList=giftPoolDao.getGiftPoolListByWarehouseCodeList(giftPool);
        List<ProductSkuRequest2> productSkuRequest2List =new ArrayList<>();
        for (GiftPool item : giftPoolList) {
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("缺失sku编码");
            }
            ProductSkuRequest2 productSkuRequest2=new ProductSkuRequest2();
            productSkuRequest2.setSkuCode(item.getSkuCode());

            productSkuRequest2.setWarehouseTypeCode("1");
            productSkuRequest2List.add(productSkuRequest2);
        }
        if(null!=productSkuRequest2List&& productSkuRequest2List.size()>0){
            //获取商品详情
            Map<String, ErpSkuDetail> skuDetailMap = bridgeProductService.getProductSkuDetailMap1(store.getProvinceId(), store.getCityId(), productSkuRequest2List);
            for (GiftPool item : giftPoolList) {
                ErpSkuDetail erpSkuDetail=skuDetailMap.get(item.getSkuCode());
                if(null==erpSkuDetail){
                    throw new BusinessException("供应链调用 /search/spu/sku/detail2 接口 查询有误 ,skuCode为"+item.getSkuCode()+"商品查询失败");
                }
                item.setStockNum(erpSkuDetail.getStockNum());
                item.setZeroRemovalCoefficient(erpSkuDetail.getZeroRemovalCoefficient());
                item.setMaxOrderNum(erpSkuDetail.getMaxOrderNum());
                item.setProductPicturePath(erpSkuDetail.getProductPicturePath());
                item.setColorName(erpSkuDetail.getColorName());
                item.setModelNumber(erpSkuDetail.getModelNumber());
                item.setSpec(erpSkuDetail.getSpec());
                item.setBatchList(erpSkuDetail.getBatchList());
                item.setSpuCode(erpSkuDetail.getSpuCode());
                item.setSpuName(erpSkuDetail.getSpuName());
                ShoppingCartRequest shoppingCartRequest=new ShoppingCartRequest();
                shoppingCartRequest.setStoreId(giftPool.getStoreId());
                shoppingCartRequest.setProductType(giftPool.getProductType());
                shoppingCartRequest.setProductId(item.getSkuCode());
                Integer cartNum=erpOrderGiftPoolCartDao.getSkuNum(shoppingCartRequest);
                if(null==cartNum){
                    cartNum=0;
                }
                item.setCartNum(cartNum);
            }
        }

        int totalNum=giftPoolDao.getTotalNumByWarehouseCodeList(giftPool);
        ErpCartNumQueryRequest erpCartNumQueryRequest=new ErpCartNumQueryRequest();
        erpCartNumQueryRequest.setStoreId(giftPool.getStoreId());
        erpCartNumQueryRequest.setProductType(giftPool.getProductType());
        erpCartNumQueryRequest.setLineCheckStatus(1);
        BigDecimal totalPrice= getCartProductTotalPrice(erpCartNumQueryRequest);
        giftCartQueryResponse.setGiftPoolList(giftPoolList);
        giftCartQueryResponse.setTotalNumber(totalNum);
        giftCartQueryResponse.setAccountActualPrice(totalPrice);
        httpResponse.setData(giftCartQueryResponse);
        return httpResponse;
    }

    @Override
    public ErpCartQueryResponse queryGiftCartList(ErpCartQueryRequest erpCartQueryRequest, AuthToken auth) {
        LOGGER.info("爱掌柜查询赠品购物车列表 queryGiftCartList 参数 erpCartQueryRequest 为：{}", erpCartQueryRequest);
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
        List<ErpOrderCartInfo> cartLineList = erpOrderGiftPoolCartDao.select(query);

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
                //后来新加的
                BigDecimal totalMoney = item.getPrice().multiply(new BigDecimal(item.getAmount()));
                item.setActivityPrice(item.getPrice());
                item.setLineAmountTotal(totalMoney);
                item.setLineActivityAmountTotal(totalMoney);
                item.setLineActivityDiscountTotal(BigDecimal.ZERO);
                item.setLineAmountAfterActivity(totalMoney);
                item.setActivityId(null);

            }
        }
        ErpCartQueryResponse queryResponse = new ErpCartQueryResponse();
        queryResponse.setCartInfoList(cartLineList);
        queryResponse.setTotalNumber(totalNumber);
        queryResponse.setAccountActualPrice(accountActualPrice);
        return queryResponse;
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

            List<ProductSkuRequest2> productSkuRequest2List =new ArrayList<>();
            for (ErpOrderCartInfo item :
                    cartList) {
                ProductSkuRequest2 productSkuRequest2=new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(item.getSkuCode());
                productSkuRequest2.setBatchInfoCode(item.getBatchInfoCode());
                if(null==item.getWarehouseTypeCode()){
                    item.setWarehouseTypeCode("1");
                }
                productSkuRequest2.setWarehouseTypeCode(item.getWarehouseTypeCode());
                productSkuRequest2List.add(productSkuRequest2);
            }
            Map<String, ErpSkuDetail> skuDetailMap = bridgeProductService.getProductSkuDetailMap(provinceCode, cityCode, productSkuRequest2List);

            for (ErpOrderCartInfo item :
                    cartList) {
                ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());
                if (skuDetail != null) {
                    item.setLogo(skuDetail.getProductPicturePath());
                    item.setPrice(skuDetail.getManufacturerGuidePrice());
                    item.setTagInfoList(skuDetail.getTagInfoList());
                    item.setStockNum(skuDetail.getStockNum());
                    item.setIsSale(skuDetail.getIsSale());
                } else {
                    LOGGER.info("查询商品信息 /search/spu/sku/detail2  接口 sku为"+item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode()+"查询失败");
                }
            }
        }
    }
    @Override
    public void deleteCartLine(String cartId) {
        ErpOrderCartInfo cartLine = getCartLineByCartId(cartId);
        if (cartLine != null) {
            erpOrderGiftPoolCartDao.deleteByPrimaryKey(cartLine.getId());
        }
    }

    private ErpOrderCartInfo getCartLineByCartId(String cartId) {
        ErpOrderCartInfo result = null;
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setCartId(cartId);
        List<ErpOrderCartInfo> select = erpOrderGiftPoolCartDao.select(query);
        if (select != null && select.size() > 0) {
            result = select.get(0);
        }
        return result;
    }

    @Override
    public void deleteAllCartLine(ErpCartQueryRequest erpCartQueryRequest) {
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartQueryRequest.getStoreId());
        query.setProductType(erpCartQueryRequest.getProductType());
        List<ErpOrderCartInfo> cartLineList = erpOrderGiftPoolCartDao.select(query);
        if (cartLineList != null && cartLineList.size() > 0) {
            for (ErpOrderCartInfo item :
                    cartLineList) {
                this.deleteCartLine(item.getCartId());
            }
        }
    }

    private int getCartProductTotalNum(ErpCartNumQueryRequest erpCartNumQueryRequest) {
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartNumQueryRequest.getStoreId());
        query.setProductType(erpCartNumQueryRequest.getProductType());
        query.setLineCheckStatus(erpCartNumQueryRequest.getLineCheckStatus());
        List<ErpOrderCartInfo> select = erpOrderGiftPoolCartDao.select(query);
        int cartProductTotalNum = 0;
        if (select != null && select.size() > 0) {
            for (ErpOrderCartInfo item :
                    select) {
                cartProductTotalNum += item.getAmount();
            }
        }
        return cartProductTotalNum;
    }
    private BigDecimal getCartProductTotalPrice(ErpCartNumQueryRequest erpCartNumQueryRequest) {
        ErpOrderCartInfo query = new ErpOrderCartInfo();
        query.setStoreId(erpCartNumQueryRequest.getStoreId());
        query.setProductType(erpCartNumQueryRequest.getProductType());
        query.setLineCheckStatus(erpCartNumQueryRequest.getLineCheckStatus());
        List<ErpOrderCartInfo> select = erpOrderGiftPoolCartDao.select(query);
        BigDecimal cartProductTotalPrice = BigDecimal.ZERO;
        if (select != null && select.size() > 0) {
            for (ErpOrderCartInfo item : select) {
                cartProductTotalPrice= cartProductTotalPrice.add(item.getPrice().multiply(new BigDecimal(item.getAmount().toString()))) ;
            }
        }
        return cartProductTotalPrice;
    }

    @Override
    public HttpResponse<Integer> getSkuNum(ShoppingCartRequest shoppingCartRequest) {
        LOGGER.info("返回购物车中的sku商品的数量getSkuNum参数为：{}", shoppingCartRequest);
        HttpResponse response = HttpResponse.success();
        Integer skuNum=erpOrderGiftPoolCartDao.getSkuNum(shoppingCartRequest);
        if(null==skuNum){
            skuNum=0;
        }
        response.setData(skuNum);
        return response;
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
    public void updateCartMultilineProducts(List<GiftCartUpdateRequest> giftCartUpdateRequestList, AuthToken auth) {
        LOGGER.info("修改兑换赠品购物车多行数据 updateCartMultilineProducts 参数为：{}", giftCartUpdateRequestList);
        Map map = new HashMap();
        String storeId = null;
        Integer productType = 0;
        for (GiftCartUpdateRequest req : giftCartUpdateRequestList) {
            if (null != req.getCartId()) {
                map.put(req.getCartId(), req.getCartId());
                storeId = req.getStoreId();
                productType = req.getProductType();
            }
        }

        if (null != storeId) {
            ErpOrderCartInfo query = new ErpOrderCartInfo();
            query.setStoreId(storeId);
            query.setProductType(productType);
            query.setLineCheckStatus(YesOrNoEnum.YES.getCode());
            List<ErpOrderCartInfo> cartLineList = erpOrderGiftPoolCartDao.select(query);

            for (ErpOrderCartInfo erpOrderCartInfo : cartLineList) {
                if (!map.containsKey(erpOrderCartInfo.getCartId())) {
                    erpOrderGiftPoolCartDao.deleteByPrimaryKey(erpOrderCartInfo.getId());
                }
            }
        }
        for (GiftCartUpdateRequest request : giftCartUpdateRequestList) {
            if (null == request.getCartId()) {
                ErpCartAddRequest erpCartAddRequest = new ErpCartAddRequest();
                erpCartAddRequest.setCreateSource("1");
                erpCartAddRequest.setProductType(request.getProductType());
                erpCartAddRequest.setStoreId(request.getStoreId());
                erpCartAddRequest.setProducts(request.getProducts());
                addProduct(erpCartAddRequest, auth);
            } else {
                ErpCartUpdateRequest erpCartUpdateRequest = new ErpCartUpdateRequest();
                erpCartUpdateRequest.setActivityId(request.getActivityId());
                erpCartUpdateRequest.setAmount(request.getAmount());
                erpCartUpdateRequest.setCartId(request.getCartId());
                erpCartUpdateRequest.setLineCheckStatus(request.getLineCheckStatus());
                updateCartLineProduct(erpCartUpdateRequest, auth);

            }
        }
    }
}