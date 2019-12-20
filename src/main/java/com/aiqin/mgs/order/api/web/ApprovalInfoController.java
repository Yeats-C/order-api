package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;
import com.aiqin.mgs.order.api.domain.CouponApprovalInfo;
import com.aiqin.mgs.order.api.service.CouponApprovalInfoService;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * description: ApprovalInfoController
 * date: 2019/12/18 16:41
 * author: hantao
 * version: 1.0
 */

@RestController
@Api(tags = "审批相关接口")
@RequestMapping("/approval")
public class ApprovalInfoController {

    @Autowired
    private CouponApprovalInfoService approvalInfoService;

    @ApiOperation("审批列表")
    @PostMapping("/getList")
    public HttpResponse<PageResData<List<CouponApprovalInfo>>> getList(@RequestBody PageRequestVO<CouponApprovalInfo> entity) {
        return HttpResponse.success(approvalInfoService.getList(entity));
    }

    @ApiOperation("新增审批")
    @PostMapping("/insert")
    public HttpResponse<Boolean> insert(@RequestBody CouponApprovalInfo entity) {
        return HttpResponse.success(approvalInfoService.insert(entity));
    }

    @ApiOperation("修改审批状态")
    @PostMapping("/updateStatus")
    public HttpResponse<Boolean> updateStatus(@RequestBody CouponApprovalInfo entity) {
        return HttpResponse.success(approvalInfoService.updateStatus(entity));
    }

    @ApiOperation("新增审批详情")
    @PostMapping("/insertDetail")
    public HttpResponse<Boolean> insertDetail(@RequestBody CouponApprovalDetail entity) {
        return HttpResponse.success(approvalInfoService.insertDetail(entity));
    }

    @ApiOperation("根据formNo查询审批详情")
    @GetMapping("/formDetail/{form_no}")
    public HttpResponse<CouponApprovalDetail> getDetailByformNo(@PathVariable(value = "form_no") String formNo) {
        return HttpResponse.success(approvalInfoService.getDetailByformNo(formNo));
    }

    @PostMapping("/callback")
    @ApiOperation("审批流回调地址")
    public String getByBrandName(@RequestBody FormCallBackVo formCallBackVo) {
        return approvalInfoService.callback(formCallBackVo);
    }

}
