package com.aiqin.mgs.order.api.web.bill;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/reject")
@Api(tags = "退货相关操作接口")
public class RejectRecordController {
    @Resource
    private RejectRecordService rejectRecordService;


    /**
     * 创建销售
     * @param RejectRecordReq
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加退货单")
    public HttpResponse createSaleOrder(@Valid @RequestBody RejectRecordReq RejectRecordReq){
        return rejectRecordService.createRejectRecord(RejectRecordReq);
    }
}
