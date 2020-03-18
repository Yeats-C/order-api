package com.aiqin.mgs.order.api.web.purchase;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.RejectRecord;
import com.aiqin.mgs.order.api.domain.request.purchase.RejectQueryRequest;
import com.aiqin.mgs.order.api.domain.response.RejectResponse;
import com.aiqin.mgs.order.api.domain.response.purchase.RejectRecordInfo;
import com.aiqin.mgs.order.api.domain.response.purchase.RejectResponseInfo;
import com.aiqin.mgs.order.api.service.GoodsRejectService;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "退供相关接口")
@RequestMapping("/reject")
@RestController
public class GoodsRejectController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodsRejectController.class);

    @Resource
    private GoodsRejectService goodsRejectService;

    @GetMapping("/record/list")
    @ApiOperation(value = "查询退供单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "reject_record_code", value = "退供单号", type = "String"),
            @ApiImplicitParam(name = "supplier_code", value = "供应商code", type = "String"),
            @ApiImplicitParam(name = "transport_center_code", value = "仓库", type = "String"),
            @ApiImplicitParam(name = "warehouse_code", value = "库房", type = "String"),
            @ApiImplicitParam(name = "purchase_group_code", value = "采购组 code", type = "String"),
            @ApiImplicitParam(name = "reject_status", value = "退供单状态: 1进行中 2 完成", type = "Integer"),
            @ApiImplicitParam(name = "begin_time", value = "开始时间", type = "String"),
            @ApiImplicitParam(name = "finish_time", value = "结束时间", type = "String"),
            @ApiImplicitParam(name = "page_no", value = "当前页", type = "Integer"),
            @ApiImplicitParam(name = "page_size", value = "每页条数", type = "Integer"),
    })
    public HttpResponse<PageResData<RejectRecordInfo>> rejectList(@RequestParam(value = "page_no", required = false) Integer page_no,
                                                                  @RequestParam(value = "page_size", required = false) Integer page_size,
                                                                  @RequestParam(value = "begin_time", required = false) String beginTime,
                                                                  @RequestParam(value = "purchase_group_code", required = false) String purchase_group_code,
                                                                  @RequestParam(value = "reject_record_code", required = false) String reject_record_code,
                                                                  @RequestParam(value = "reject_status", required = false) Integer reject_status,
                                                                  @RequestParam(value = "transport_center_code", required = false) String transport_center_code,
                                                                  @RequestParam(value = "supplier_code", required = false) String supplier_code,
                                                                  @RequestParam(value = "warehouse_code", required = false) String warehouse_code,
                                                                  @RequestParam(value = "finish_time", required = false) String finishTime) {
        RejectQueryRequest rejectQueryRequest = new RejectQueryRequest(beginTime, finishTime, reject_record_code, purchase_group_code, supplier_code, transport_center_code, warehouse_code, reject_status);
        rejectQueryRequest.setPageNo(page_no);
        rejectQueryRequest.setPageSize(page_size);
        LOGGER.info("查询退供单列表请求,rejectRecord:{}", rejectQueryRequest.toString());
        return goodsRejectService.rejectList(rejectQueryRequest);
    }

    @GetMapping("/record/{reject_record_code}")
    @ApiOperation(value = "查询退供单详情")
    @ApiImplicitParam(name = "reject_record_code", value = "退供单code", type = "String")
    public HttpResponse<RejectResponseInfo> rejectInfo(@PathVariable String reject_record_code) {
        LOGGER.info("查询退供单详情请求,reject_record_code:{}", reject_record_code);
        return goodsRejectService.rejectInfo(reject_record_code);
    }
}
