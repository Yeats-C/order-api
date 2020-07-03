package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.BasePage;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.request.returngoods.QueryReturnOrderManagementReqVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.QueryReturnOrderManagementRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderDetailRespVO;
import com.aiqin.mgs.order.api.service.ReturnGoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author xieq
 * @Date 2020/2/29
 */
@RestController
@Slf4j
@Api(tags = "退货api")
@RequestMapping("/returnGoods")
public class ReturnGoodsController {
    @Autowired
    private ReturnGoodsService returnGoodsService;

    @ApiOperation("退货单管理")
    @PostMapping("/returnOrderManagement")
    public HttpResponse<BasePage<QueryReturnOrderManagementRespVO>> returnOrderManagement(@RequestBody QueryReturnOrderManagementReqVO reqVO){
        log.info("爱亲供应链退货单列表入参：[{}]", JsonUtil.toJson(reqVO));
        try {
            return HttpResponse.success(returnGoodsService.returnOrderManagement(reqVO));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponse.failure(ResultCode.SYSTEM_ERROR);
        }
    }

    @ApiOperation("退货详情")
    @GetMapping("/returnOrderDetail")
    public HttpResponse<ReturnOrderDetailRespVO> returnOrderDetail(@RequestParam String code){
        log.info("爱亲供应链退货单详情入参：[{}]", code);
        try {
            return HttpResponse.success(returnGoodsService.returnOrderDetail(code));
        }catch (Exception e) {
            log.error(e.getMessage(),e);
            return HttpResponse.failure(ResultCode.SYSTEM_ERROR);
        }
    }

}
