package com.aiqin.mgs.order.api.service.product;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRespVo6;
import com.aiqin.mgs.order.api.domain.request.product.SkuProductReqVO;

/**
 * 商品信息查询service
 *
 * @author: Tao.CSF
 * @version: v1.0.0
 * @date 2020/3/19 10:14c
 */
public interface ProductService {

    /**
     * erp查询商品信息列表
     * @param  skuProductReqVO
     * @return
     */
    HttpResponse<PageResData<ProductSkuRespVo6>> queryErpProductList(SkuProductReqVO skuProductReqVO);
}
