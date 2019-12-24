package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ConstantData;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.dao.CouponApprovalDetailDao;
import com.aiqin.mgs.order.api.dao.CouponApprovalInfoDao;
import com.aiqin.mgs.order.api.dao.returnOrder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnOrder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderDetailVO;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewApiReqVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.platform.flows.client.constant.AjaxJson;
import com.aiqin.platform.flows.client.constant.FormUpdateUrlType;
import com.aiqin.platform.flows.client.constant.StatusEnum;
import com.aiqin.platform.flows.client.domain.vo.ActBaseProcessEntity;
import com.aiqin.platform.flows.client.domain.vo.StartProcessParamVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * description: ReturnOrderInfoServiceImpl
 * date: 2019/12/19 19:16
 * author: hantao
 * version: 1.0
 */
@Service
@Slf4j
public class ReturnOrderInfoServiceImpl implements ReturnOrderInfoService {

    @Value("${activiti.host}")
    private String activitiHost;

    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Autowired
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Autowired
    private SequenceService sequenceService;
    @Resource
    private CouponApprovalInfoDao couponApprovalInfoDao;
    @Resource
    private CouponApprovalDetailDao couponApprovalDetailDao;
    @Resource
    private RejectRecordService rejectRecordService;

    @Override
    @Transactional
    public Boolean save(ReturnOrderReqVo reqVo) {
        //查询订单是否存在未处理售后单
        List<ReturnOrderInfo> returnOrderInfo = returnOrderInfoDao.selectByOrderCodeAndStatus(reqVo.getOrderStoreCode(), 1);
        Assert.isTrue(CollectionUtils.isEmpty(returnOrderInfo), "该订单还有未审核售后单，请稍后提交");
        ReturnOrderInfo record = new ReturnOrderInfo();
        Date now = new Date();
        BeanUtils.copyProperties(reqVo, record);
        String returnOrderId = IdUtil.uuid();
        String afterSaleCode = sequenceService.generateOrderAfterSaleCode(reqVo.getCompanyCode(), reqVo.getReturnOrderType());
        record.setReturnOrderId(returnOrderId);
        record.setReturnOrderCode(afterSaleCode);
        record.setCreateTime(now);
        switch (reqVo.getReturnOrderType()) {
            case 2:
                record.setReturnOrderStatus(1);
                break;
            case 1:
            case 0:
                record.setReturnOrderStatus(12);
                record.setActualProductCount(record.getProductCount());
                record.setActualReturnOrderAmount(record.getReturnOrderAmount());
//                refund(record, reqVo.getDiscountAmountInfos(), reqVo.getCreateBy());
                break;
            default:
                record.setReturnOrderStatus(1);
        }
//        if (reqVo.getReturnDiscountAmount() > 0) {//退回优惠额度
//            record.setDiscountAmountInfoStr(JSON.toJSONString(reqVo.getDiscountAmountInfos()));
//        }
        returnOrderInfoDao.insertSelective(record);
        List<ReturnOrderDetail> details = reqVo.getDetails().stream().map(detailVo -> {
            ReturnOrderDetail detail = new ReturnOrderDetail();
            BeanUtils.copyProperties(detailVo, detail);
            detail.setCreateTime(now);
            detail.setReturnOrderDetailId(IdUtil.uuid());
            detail.setReturnOrderCode(returnOrderId);
            detail.setCreateById(reqVo.getCreateById());
            detail.setCreateByName(reqVo.getCreateByName());
            return detail;
        }).collect(Collectors.toList());
        returnOrderDetailDao.insertBatch(details);
        return true;
    }

//    @Override
//    public PageResData<ReturnOrderListVo> list(OrderAfterSaleSearchVo searchVo) {
//        return null;
//    }

    @Override
    @Transactional
    public Boolean updateReturnStatus(ReturnOrderReviewReqVo reqVo) {
        boolean flag = false;
        boolean flag1 = false;
        //1--通过 2--挂账 3--不通过（驳回）"
        switch (reqVo.getOperateStatus()) {
            case 1:
                reqVo.setOperateStatus(2);
                //同步数据到供应链
                flag = true;
                break;
            case 2:
                reqVo.setOperateStatus(6);
                //调用A品卷审批
                flag1 = true;
                break;
            case 3:
                reqVo.setOperateStatus(98);
                break;
            default:
                return false;
        }
        //修改退货单状态
        Integer review = returnOrderInfoDao.updateReturnStatus(reqVo);
        if (flag) {
            //todo 同步到供应链
            createRejectRecord(reqVo.getReturnOrderId());
        }
        if (flag1) {
            log.info("驳回--进入A品券发放审批");
            //生成审批编码
            String approvalCode=createFormNo();
            //同步审批信息到本地表中（新增）
            CouponApprovalInfo record=approvalInfoInsert(reqVo.getApplier(),reqVo.getUserName(),approvalCode);
            log.info("同步审批信息入参: reqVo={},approvalCode={}", reqVo,approvalCode);
            couponApprovalInfoDao.insertSelective(record);
            CouponApprovalDetail approvalDetail=reqVo.getApprovalDetail();
            approvalDetail.setFormNo(approvalCode);
            approvalDetail.setCouponType(ConstantData.couponType);
            approvalDetail.setCreator(reqVo.getUserName());
            approvalDetail.setRemark(reqVo.getReviewNote());
            approvalDetail.setFranchiseeId(reqVo.getFranchiseeId());
            approvalDetail.setOrderId(reqVo.getReturnOrderId());
            couponApprovalDetailDao.insertSelective(approvalDetail);
            log.info("同步审批信息到本地完成");
            //A品券发放审批申请提交
            submitActBaseProcess(approvalCode,reqVo.getApplier(),reqVo.getDeptCode());
        }
        return review > 0;
    }

    @Override
    @Transactional
    public Boolean updateOrderAfterSaleDetail(ReturnOrderDetailVO records) {
        List<ReturnOrderDetail> details=records.getDetails();
        String returnOrderCode = records.getReturnOrderCode();
        if(StringUtils.isNotBlank(returnOrderCode)&&CollectionUtils.isNotEmpty(details)){
            //根据退货单id，删除详情记录
            int res=returnOrderDetailDao.deleteByReturnOrderCode(returnOrderCode);
            if(res>0){
                details = details.stream().map(detailVo -> {
                    ReturnOrderDetail detail = new ReturnOrderDetail();
                    BeanUtils.copyProperties(detailVo, detail);
                    detail.setCreateTime(new Date());
                    detail.setReturnOrderDetailId(IdUtil.uuid());
                    detail.setReturnOrderCode(returnOrderCode);
                    detail.setCreateById(records.getCreateId());
                    detail.setCreateByName(records.getCreator());
                    return detail;
                }).collect(Collectors.toList());
                returnOrderDetailDao.insertBatch(details);
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public Boolean updateReturnStatusApi(ReturnOrderReviewApiReqVo reqVo) {
        ReturnOrderReviewReqVo re=new ReturnOrderReviewReqVo();
        BeanUtils.copyProperties(reqVo,re);
        if(null!=reqVo.getReturnOrderDetail()){
            // todo 修改实退数量
        }
        return returnOrderInfoDao.updateReturnStatus(re)>0;
    }

    @Override
    public Boolean check(String orderCode) {
        List<ReturnOrderInfo> returnOrderInfo = returnOrderInfoDao.selectByOrderId(orderCode);
        if(CollectionUtils.isNotEmpty(returnOrderInfo)){
            return true;
        }
        return false;
    }

    /**
     * 生成16位唯一性的审批编码
     * @return
     */
    public static String createFormNo(){
        //随机生成一位整数
        int random = (int) (Math.random()*9+1);
        String valueOf = String.valueOf(random);
        //生成uuid的hashCode值
        int hashCode = UUID.randomUUID().toString().hashCode();
        //可能为负数
        if(hashCode<0){
            hashCode = -hashCode;
        }
        String value = "AC"+valueOf + String.format("%015d", hashCode);
        return value;
    }

    /**
     * 同步审批信息--新增
     * @param applier
     * @param username
     * @param approvalCode
     * @return
     */
    public static CouponApprovalInfo approvalInfoInsert(String applier, String username, String approvalCode){
        CouponApprovalInfo record=new CouponApprovalInfo();
        record.setStatus(StatusEnum.AUDIT.getValue());
        record.setStatuStr(StatusEnum.AUDIT.getDesc());
        record.setApplierId(applier);
        record.setApplierName(username);
        record.setCreateTimeStr(new Date());
        record.setFormNo(approvalCode);
        record.setProcessTitle("A品券发放审批");
        record.setProcessType("A品券发放审批");
        return record;
    }

    /**
     * 封装 A品券发放审批
     *
     * @param approvalCode 审批号
     * @param applier      申请人
     * @param positionCode 部门编码
     */
    public HttpResponse submitActBaseProcess(String approvalCode, String applier, String positionCode) {
        StartProcessParamVO paramVO = new StartProcessParamVO();
        // A品券发放申请
        paramVO.setCostType(ConstantData.applyCoupon01);
        paramVO.setFormType(ConstantData.applyCoupon01);
        paramVO.setFormUrl("http://order.api.aiqin.com/approval/formDetail/" + approvalCode);//表单详情页面地址 必传
        paramVO.setCurrentUser(applier);
        paramVO.setFormNo(approvalCode);
        paramVO.setTitle("A品卷发放审批");
        paramVO.setRemark("A品卷发放审批");
        paramVO.setFormUpdateUrl("http://order.api.aiqin.com/approval/callback");//回调地址
        paramVO.setFormUpdateUrlType(FormUpdateUrlType.HTTP);
        paramVO.setSignTicket(IdUtil.uuid());
        paramVO.setReceiptType(1);
        paramVO.setPositionCode(positionCode);
//        if (StringUtils.isNotBlank(request.getAuditPersonId())) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("auditPersonId", request.getAuditPersonId());
//            paramVO.setVariables(map);
//        }
        log.info("调用审批流发起申请,request={}", paramVO);
        String url = activitiHost + "/activiti/common/submit";
        HttpClient httpClient = HttpClient.post(url).json(paramVO).timeout(10000);
        AjaxJson result = httpClient.action().result(AjaxJson.class);
        log.info("调用审批流发起申请返回结果,result={}", result);
        if (result.getSuccess()) {
            ActBaseProcessEntity actBaseProcessEntity = JsonUtil.fromJson(JsonUtil.toJson(result.getObj()), ActBaseProcessEntity.class);
            log.info("发起申请成功,request={}", paramVO);
//            formKeyDao.insert(new FormKey(paramVO.getFormNo(),paramVO.getFormType()));
            return HttpResponse.success(actBaseProcessEntity);
        }
        log.error("发起申请失败,request={}", paramVO);
        return HttpResponse.success();
    }

    /**
     * 调用供应链封装
     * @param returnOrderId 退货单id
     */
    public void createRejectRecord(String returnOrderId){
        log.info("供应链同步退货单开始,returnOrderId={}",returnOrderId);
        RejectRecordReq rejectRecordReq=new RejectRecordReq();
        //根据退货单id查询退货信息
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderId(returnOrderId);
        //根据退货单id查询退货详细信息
        List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderId);
        RejectRecord rejectRecord=new RejectRecord();
        List<RejectRecordDetail> rejectRecordDetail=new ArrayList<>();
        BeanUtils.copyProperties(returnOrderInfo,rejectRecord);
        rejectRecord.setSettlementMethodCode(returnOrderInfo.getReturnMoneyType().toString());
        if(null!=returnOrderInfo.getReturnMoneyType()){
            //退款方式 1:现金 2:微信 3:支付宝 4:银联
            switch (returnOrderInfo.getReturnMoneyType()) {
                case 1:
                    rejectRecord.setSettlementMethodName("现金");
                    break;
                case 2:
                    rejectRecord.setSettlementMethodName("微信");
                    break;
                case 3:
                    rejectRecord.setSettlementMethodName("支付宝");
                    break;
                case 4:
                    rejectRecord.setSettlementMethodName("银联");
                    break;
            }
        }
        //todo 退供单状态
//        rejectRecord.setRejectRecordStatus(1);
        for(ReturnOrderDetail rod:returnOrderDetails){
            RejectRecordDetail rrd=new RejectRecordDetail();
            BeanUtils.copyProperties(rod,rrd);
            rejectRecordDetail.add(rrd);
        }
        rejectRecordReq.setRejectRecord(rejectRecord);
        rejectRecordReq.setRejectRecordDetail(rejectRecordDetail);
        HttpResponse httpResponse=rejectRecordService.createRejectRecord(rejectRecordReq);
        log.info("供应链同步退货单结束,httpResponse={}",httpResponse);
    }

}
