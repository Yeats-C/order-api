package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.CouponApprovalDetailDao;
import com.aiqin.mgs.order.api.dao.CouponApprovalInfoDao;
import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;
import com.aiqin.mgs.order.api.domain.CouponApprovalInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.FranchiseeAsset;
import com.aiqin.mgs.order.api.service.CouponApprovalInfoService;
import com.aiqin.mgs.order.api.util.URLConnectionUtil;
import com.aiqin.platform.flows.client.constant.Indicator;
import com.aiqin.platform.flows.client.constant.IndicatorStr;
import com.aiqin.platform.flows.client.constant.StatusEnum;
import com.aiqin.platform.flows.client.constant.TpmBpmUtils;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * description: ApprovalInfoServiceImpl
 * date: 2019/12/18 16:04
 * author: hantao
 * version: 1.0
 */
@Service
@Slf4j
public class CouponApprovalInfoServiceImpl implements CouponApprovalInfoService {

    @Value("${bridge.url.slcs_api}")
    private String slcsHost;

    @Autowired
    private CouponApprovalInfoDao couponApprovalInfoDao;
    @Autowired
    private CouponApprovalDetailDao couponApprovalDetailDao;


    @Override
    public Boolean insert(CouponApprovalInfo entity) {
        return couponApprovalInfoDao.insertSelective(entity)>0;
    }

    @Override
    public Boolean update(CouponApprovalInfo entity) {
        return couponApprovalInfoDao.updateByPrimaryKeySelective(entity)>0;
    }

    @Override
    public Boolean updateStatus(CouponApprovalInfo entity) {
        return couponApprovalInfoDao.updateByFormNoSelective(entity)>0;
    }

    @Override
    public PageResData<List<CouponApprovalInfo>> getList(PageRequestVO<CouponApprovalInfo> entity) {
        PageHelper.startPage(entity.getPageNo(),entity.getPageSize());
        List<CouponApprovalInfo> approvalInfos = couponApprovalInfoDao.selectList(entity.getSearchVO());
        log.info("The approvalInfos for approvalInfoList is:"+approvalInfos);
        PageResData result=new PageResData();
        result.setTotalCount((int)((Page)approvalInfos).getTotal());
        result.setDataList(approvalInfos);
        return result;
    }

    @Override
    public Boolean insertDetail(CouponApprovalDetail entity) {
        return null;
    }

    @Override
    public CouponApprovalDetail getDetailByformNo(String formNo) {
        return null;
    }

    @Override
    @Transactional
    public String callback(FormCallBackVo formCallBackVo) {
        String result = "success";
        log.info("A品券发放审批进入回调,request={}", formCallBackVo);
        CouponApprovalInfo couponApprovalInfo = couponApprovalInfoDao.selectByFormNo(formCallBackVo.getFormNo());
        CouponApprovalDetail couponApprovalDetail = couponApprovalDetailDao.selectByFormNo(formCallBackVo.getFormNo());
        log.info("A品券发放审批回调,couponApprovalInfo={},couponApprovalDetail={}", couponApprovalInfo,couponApprovalDetail);
        if (couponApprovalInfo != null) {
            if (formCallBackVo.getUpdateFormStatus().equals(Indicator.COST_FORM_STATUS_APPROVED.getCode())) {
                //审核通过，修改本地主表状态
                couponApprovalInfo.setStatus(StatusEnum.AUDIT_PASS.getValue());
                couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_PASS.getDesc());
                //计算A品券数量，同步到虚拟资产
                List<FranchiseeAsset> list=new ArrayList();
                Double totalMoney=couponApprovalDetail.getTotalMoney();
                if(couponApprovalDetail!=null&&totalMoney!=null){
                    int num=(int)(totalMoney/100);
                    double balance=totalMoney%100;

                    for(int i=0;i<num;i++){

                    }

                }
                String url=slcsHost+"/franchiseeVirtual/VirtualA";
                JSONObject json=new JSONObject();
                json.put("list",null);
                String request= URLConnectionUtil.doPost(url,null,json);
//                logger.info("the getByBrandName request is:"+request);


            } else if (TpmBpmUtils.isPass(formCallBackVo.getUpdateFormStatus(), formCallBackVo.getOptBtn())) {
                couponApprovalInfo.setStatus(StatusEnum.AUDIT.getValue());
                couponApprovalInfo.setStatuStr(StatusEnum.AUDIT.getDesc());
            } else {
                if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_FIRST.getCode())) {
                    //驳回发起人
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_BACK.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_BACK.getDesc());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_END.getCode())) {
                    //驳回并结束
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_CANCEL.getCode())) {
                    //撤销
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_CANCEL.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_CANCEL.getDesc());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_KILL.getCode())) {
                    //终止
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                } else {
                    //终止
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                }
            }
            //更新本地审批表数据
            couponApprovalInfoDao.updateByFormNoSelective(couponApprovalInfo);
        } else {
            //result = "false";
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(IdUtil.activityId());
    }

}
