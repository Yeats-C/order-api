package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.ConstantData;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.CouponApprovalDetailDao;
import com.aiqin.mgs.order.api.dao.CouponApprovalInfoDao;
import com.aiqin.mgs.order.api.dao.CouponInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.CouponApprovalDetail;
import com.aiqin.mgs.order.api.domain.CouponApprovalInfo;
import com.aiqin.mgs.order.api.domain.CouponInfo;
import com.aiqin.mgs.order.api.domain.request.returnorder.FranchiseeAssetVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.service.CouponApprovalInfoService;
import com.aiqin.mgs.order.api.util.URLConnectionUtil;
import com.aiqin.platform.flows.client.constant.Indicator;
import com.aiqin.platform.flows.client.constant.IndicatorStr;
import com.aiqin.platform.flows.client.constant.StatusEnum;
import com.aiqin.platform.flows.client.constant.TpmBpmUtils;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    @Autowired
    private CouponInfoDao couponInfoDao;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;


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
                Double totalMoney=couponApprovalDetail.getTotalMoney();
                //计算A品券数量，同步到虚拟资产
                List<FranchiseeAssetVo> franchiseeAssets=new ArrayList<>();
                if(couponApprovalDetail!=null&&totalMoney!=null){
                    //计算面值为100的A品券数量
                    int num=(int)(totalMoney/100);
                    //计算剩余钱数
                    double balance=totalMoney%100;
                    //存储A品卷信息
                    List<CouponInfo> couponInfoList=new ArrayList<>();
                    CouponInfo couponInfo=new CouponInfo();
                    couponInfo.setCouponName(ConstantData.aCouponName);
                    couponInfo.setCouponType(ConstantData.couponType);
                    couponInfo.setFranchiseeId(couponApprovalDetail.getFranchiseeId());
                    couponInfo.setOrderId(couponApprovalDetail.getOrderId());
                    couponInfo.setValidityStartTime(couponApprovalDetail.getStartTime());
                    couponInfo.setValidityEndTime(couponApprovalDetail.getEndTime());
                    for(int i=0;i<num;i++){
                        couponInfo.setCouponCode(couponCode());
                        couponInfo.setNominalValue(ConstantData.nominalValue);
                        couponInfoList.add(couponInfo);
                        FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
                        BeanUtils.copyProperties(couponInfo,franchiseeAsset);
                        franchiseeAssets.add(franchiseeAsset);
                    }
                    if(balance>0){
                        couponInfo.setCouponCode(couponCode());
                        couponInfo.setNominalValue(BigDecimal.valueOf(balance));
                        couponInfoList.add(couponInfo);
                        FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
                        BeanUtils.copyProperties(couponInfo,franchiseeAsset);
                        franchiseeAssets.add(franchiseeAsset);
                    }
                    log.info("A品券明细本地同步开始,couponInfoList={}",couponInfoList);
                    couponInfoDao.insertBatch(couponInfoList);
                    log.info("A品券明细本地同步完成");
                }
                //产生的A品券不为空，同步到虚拟资产
                if(CollectionUtils.isNotEmpty(franchiseeAssets)){
                    log.info("A品券同步到虚拟资产开始，franchiseeAssets={}",franchiseeAssets);
                    String url=slcsHost+"/franchiseeVirtual/VirtualA";
                    JSONObject json=new JSONObject();
                    json.put("list",franchiseeAssets);
                    String request= URLConnectionUtil.doPost(url,null,json);
                    log.info("同步到虚拟资产:"+request);
                    if(StringUtils.isNotBlank(request)){
                        JSONObject jsonObject= JSON.parseObject(request);
                        if(jsonObject.containsKey("code")&&"0".equals(jsonObject.getString("code"))){
                            log.info("A品券虚拟资产同步成功，修改退货单状态");
                            ReturnOrderReviewReqVo reqVo=new ReturnOrderReviewReqVo();
                            reqVo.setReturnOrderId(couponApprovalDetail.getOrderId());
                            reqVo.setOperateStatus(ConstantData.returnOrderSuccess);
                            returnOrderInfoDao.updateReturnStatus(reqVo);
                            log.info("退款完成");
                        }
                    }
                }
            } else if (TpmBpmUtils.isPass(formCallBackVo.getUpdateFormStatus(), formCallBackVo.getOptBtn())) {
                couponApprovalInfo.setStatus(StatusEnum.AUDIT.getValue());
                couponApprovalInfo.setStatuStr(StatusEnum.AUDIT.getDesc());
            } else {
                if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_FIRST.getCode())) {
                    //驳回发起人
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_BACK.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_BACK.getDesc());
                    updateReturnOrderStatus(ConstantData.returnOrderStatusWait,couponApprovalDetail.getOrderId());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_END.getCode())) {
                    //驳回并结束
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                    updateReturnOrderStatus(ConstantData.returnOrderStatusWait,couponApprovalDetail.getOrderId());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_CANCEL.getCode())) {
                    //撤销
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_CANCEL.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_CANCEL.getDesc());
                    updateReturnOrderStatus(ConstantData.returnOrderStatusWait,couponApprovalDetail.getOrderId());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_KILL.getCode())) {
                    //终止
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                    updateReturnOrderStatus(ConstantData.returnOrderStatusWait,couponApprovalDetail.getOrderId());
                } else {
                    //终止
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                    updateReturnOrderStatus(ConstantData.returnOrderStatusWait,couponApprovalDetail.getOrderId());
                }
            }
            //更新本地审批表数据
            couponApprovalInfoDao.updateByFormNoSelective(couponApprovalInfo);
        } else {
            //result = "false";
        }
        return result;
    }

    /**
     * 生成A品券编码
     * @return
     */
    public static String couponCode(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String str=sdf.format(new Date());
        str="AC"+str+String.format("%04d",new Random().nextInt(9999));
        return str;
    }

    /**
     * 修改退货单状态--A品卷审批为非“同意”状态时，全部改为01待审核状态
     * @param status
     * @param returnOrderId
     * @return
     */
    public Boolean updateReturnOrderStatus(Integer status,String returnOrderId){
        ReturnOrderReviewReqVo reqVo=new ReturnOrderReviewReqVo();
        reqVo.setOperateStatus(status);
        reqVo.setReturnOrderId(returnOrderId);
        Integer res=returnOrderInfoDao.updateReturnStatus(reqVo);
        return res>0;
    }


    public static void main(String[] args) {
//        System.out.println(IdUtil.activityId());
//        for (int i=0;i<1000;i++){
//            System.out.println(couponCode());
//        }

//        String url="http://slcs.api.aiqin.com/franchiseeVirtual/VirtualA";
//        String url="http://192.168.200.127:9011/franchiseeVirtual/VirtualA";
        String url="http://127.0.0.1:9011/franchiseeVirtual/VirtualA";
        List<FranchiseeAssetVo> franchiseeAssets=new ArrayList<>();
        CouponInfo couponInfo=new CouponInfo();
        couponInfo.setCouponName(ConstantData.aCouponName);
        couponInfo.setCouponType(ConstantData.couponType);
        couponInfo.setFranchiseeId("1001");
        couponInfo.setOrderId("1002");
        couponInfo.setValidityStartTime(new Date());
        couponInfo.setValidityEndTime(new Date());
        couponInfo.setCouponCode(couponCode());
        couponInfo.setNominalValue(ConstantData.nominalValue);
        couponInfo.setCreateTime(new Date());
        FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
        BeanUtils.copyProperties(couponInfo,franchiseeAsset);
        franchiseeAssets.add(franchiseeAsset);
        JSONObject json=new JSONObject();
        json.put("list",franchiseeAssets);
        String request= URLConnectionUtil.doPost(url,null,json);
        log.info("同步到虚拟资产:"+request);


    }

}
