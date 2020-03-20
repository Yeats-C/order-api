package com.aiqin.mgs.order.api.web.product;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRespVo6;
import com.aiqin.mgs.order.api.domain.request.product.SkuProductReqVO;
import com.aiqin.mgs.order.api.service.product.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
@Api(tags = "商品查询相关接口")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Resource
    private ProductService productService;

    @PostMapping("/queryErpProductList")
    @ApiOperation(value = "erp查询商品信息列表")
    public HttpResponse<PageResData<ProductSkuRespVo6>> queryErpProductList(@RequestBody SkuProductReqVO skuProductReqVO) {
        HttpResponse<PageResData<ProductSkuRespVo6>> response = HttpResponse.success();
        try {
            response = productService.queryErpProductList(skuProductReqVO);
        } catch (BusinessException e) {
            logger.error("erp查询商品信息列表：{}", e);
            response = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            logger.error("erp查询商品信息列表：{}", e);
            response = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return response;
    }


}
