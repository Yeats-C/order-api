package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.request.RejectRequest;
import com.aiqin.mgs.order.api.domain.response.RejectResponse;
import com.aiqin.mgs.order.api.domain.response.RejectVoResponse;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "退供单查询")
@RestController
@RequestMapping("/reject")
@Slf4j
public class RejectController {

    @Autowired
    private RejectRecordService rejectRecordService;

    @PostMapping("/search")
    @ApiOperation("根据搜索条件获取退供单列表")
    public HttpResponse<List<RejectResponse>> searchReject(@RequestBody RejectRequest rejectRequest){
        ;
        return rejectRecordService.selectByRejectRequest(rejectRequest);
    }

    @GetMapping("/view")
    @ApiOperation("查看退供单详情")
    public HttpResponse<RejectVoResponse> searchRejectDetailByRejectCode(@ApiParam("退供单号")@RequestParam(value = "rejectRecordCode") String rejectRecordCode){
        return rejectRecordService.searchRejectDetailByRejectCode(rejectRecordCode);
    }
}
