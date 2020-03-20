package com.aiqin.mgs.order.api.service.impl.product;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.request.product.AreaReq;
import com.aiqin.mgs.order.api.domain.request.product.NewStoreCategory;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRespVo6;
import com.aiqin.mgs.order.api.domain.request.product.SkuProductReqVO;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.product.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private ActivityService activityService;


    @Override
    public HttpResponse<PageResData<ProductSkuRespVo6>> queryErpProductList(SkuProductReqVO skuProductReqVO) {
        LOGGER.info("erp查询商品信息列表 queryErpProductList 参数为：{}", skuProductReqVO);
        //权限控制代码-----------------------------开始

        //通过商品查询菜单code查出当前拥有权限的门店数据
        List<String> storeIdList=activityService.storeIds("ERP002003");
        if(null!=storeIdList&&0<storeIdList.size()){
            List<AreaReq>  areaReqs=null;
            //通过门店id集合查询出省市集合
            List<NewStoreCategory> provinceList=bridgeProductService.selectProvincesByStoreList(storeIdList);
            if(null!=provinceList&&0<provinceList.size()){
                for (NewStoreCategory province:provinceList){
                    AreaReq areaReq=new AreaReq();
                    areaReq.setCityCode(province.getCityId());
                    areaReq.setProvinceCode(province.getProvinceId());
                    areaReqs.add(areaReq);
                }
                skuProductReqVO.setAreaReqs(areaReqs);
            }

        }
        //权限控制代码-----------------------------结束
        return bridgeProductService.getSkuPage2(skuProductReqVO);
    }
}
