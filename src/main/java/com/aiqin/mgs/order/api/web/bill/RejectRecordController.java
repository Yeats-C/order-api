package com.aiqin.mgs.order.api.web.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.RejectRecordInfo;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 爱亲退供单 控制器
 */
@RestController
@RequestMapping("/reject")
@Api(tags = "爱亲退供单")
public class RejectRecordController {
    @Resource
    private RejectRecordService rejectRecordService;

    /**
     * 同步退供单
     * @param returnOrderCode
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "根据退货单，生成爱亲采购单")
    public HttpResponse synchronizationRejectRecord(String returnOrderCode){
        return rejectRecordService.createRejectRecord(returnOrderCode);
    }

    /**
     * 耘链销售单回传
     */
    @PostMapping("info")
    @ApiOperation(value = "耘链销售单回传")
    public HttpResponse<List<RejectRecordInfo>> selectPurchaseInfo() {
        return rejectRecordService.selectPurchaseInfo();
    }
}
