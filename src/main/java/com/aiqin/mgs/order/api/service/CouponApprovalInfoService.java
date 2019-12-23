package com.aiqin.mgs.order.api.service;

import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;
import com.aiqin.mgs.order.api.domain.CouponApprovalInfo;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;

import java.util.List;

/**
 * description: ApprovalInfoService
 * date: 2019/12/18 15:58
 * author: hantao
 * version: 1.0
 */
public interface CouponApprovalInfoService {

    Boolean insert(CouponApprovalInfo entity);

    Boolean update(CouponApprovalInfo entity);

    Boolean updateStatus(CouponApprovalInfo entity);

    PageResData<List<CouponApprovalInfo>> getList(PageRequestVO<CouponApprovalInfo> entity);

    Boolean insertDetail(CouponApprovalDetail entity);

    CouponApprovalDetail getDetailByformNo(String formNo);

    String callback(FormCallBackVo formCallBackVo);

}
