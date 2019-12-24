package com.aiqin.mgs.order.api.web.returnOrder;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.service.returnOrder.ReturnOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * description: ReturnOrderInfoController
 * date: 2019/12/19 17:44
 * author: hantao
 * version: 1.0
 */
@RestController
@Api(tags = "订货单退货")
@RequestMapping("/returnOrder")
public class ReturnOrderInfoController {

    @Resource
    private ReturnOrderInfoService returnOrderInfoService;

    @ApiOperation("新增退货信息")
    @PostMapping("/add")
    public HttpResponse<Boolean> save(@RequestBody ReturnOrderReqVo reqVo) {
        return new HttpResponse<>(returnOrderInfoService.save(reqVo));
    }



    @ApiOperation("操作审核退货单")
    @PostMapping("/updateStatus")
    public HttpResponse<Boolean> updateStatus(@RequestBody ReturnOrderReviewReqVo reqVo) {
        Boolean review = returnOrderInfoService.updateReturnStatus(reqVo);
        return new HttpResponse<>(review);
    }

    /**
     * 修改退货单详情--这里逻辑是根据需求，先根据退货单id删除所有详情记录，然后重新添加
     * @param reqVo
     * @return
     */
    @ApiOperation("修改退货单详情")
    @PostMapping("/updateDetail")
    public HttpResponse<Boolean> updateDetail(@RequestBody ReturnOrderDetailVO reqVo) {
        Boolean review = returnOrderInfoService.updateOrderAfterSaleDetail(reqVo);
        return new HttpResponse<>(review);
    }


}
