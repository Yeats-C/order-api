package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.*;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.dao.CouponApprovalDetailDao;
import com.aiqin.mgs.order.api.dao.CouponApprovalInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.RefundInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.bill.RejectRecordReq;
import com.aiqin.mgs.order.api.domain.request.returnorder.*;
import com.aiqin.mgs.order.api.domain.response.returnorder.ReturnOrderStatusVo;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.service.order.ErpOrderItemService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.mgs.order.api.util.URLConnectionUtil;
import com.aiqin.platform.flows.client.constant.AjaxJson;
import com.aiqin.platform.flows.client.constant.FormUpdateUrlType;
import com.aiqin.platform.flows.client.constant.StatusEnum;
import com.aiqin.platform.flows.client.domain.vo.ActBaseProcessEntity;
import com.aiqin.platform.flows.client.domain.vo.StartProcessParamVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
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

    @Value("${bridge.url.pay-api}")
    private String paymentHost;

    @Value("${bridge.url.slcs_api}")
    private String slcsHost;

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
    @Resource
    private RefundInfoDao refundInfoDao;
    @Resource
    private ErpOrderItemService erpOrderItemService;

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
        //tui
        record.setReturnMoneyType(ConstantData.returnMoneyType);
        returnOrderInfoDao.insertSelective(record);
        List<ReturnOrderDetail> details = reqVo.getDetails().stream().map(detailVo -> {
            ReturnOrderDetail detail = new ReturnOrderDetail();
            BeanUtils.copyProperties(detailVo, detail);
            detail.setCreateTime(now);
            detail.setReturnOrderDetailId(IdUtil.uuid());
            detail.setReturnOrderCode(afterSaleCode);
            detail.setCreateById(reqVo.getCreateById());
            detail.setCreateByName(reqVo.getCreateByName());
            return detail;
        }).collect(Collectors.toList());
        returnOrderDetailDao.insertBatch(details);
        return true;
    }

    @Override
    public PageResData<ReturnOrderInfo> list(ReturnOrderSearchVo searchVo) {
        List<ReturnOrderInfo> content = returnOrderInfoDao.page(searchVo);
        Integer pageCount = returnOrderInfoDao.pageCount(searchVo);
        return new PageResData<>(pageCount, content);
    }

    @Override
    @Transactional
    public Boolean updateReturnStatus(ReturnOrderReviewReqVo reqVo) {
        boolean flag = false;
        boolean flag1 = false;
        //1--通过 2--挂账 3--不通过（驳回）99-已取消"
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
            case 99:
                reqVo.setOperateStatus(99);
                break;
            default:
                return false;
        }
        //修改退货单状态
        Integer review = returnOrderInfoDao.updateReturnStatus(reqVo);
        if (flag) {
            //todo 同步到供应链
//            createRejectRecord(reqVo.getReturnOrderCode());
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
            approvalDetail.setOrderId(reqVo.getReturnOrderCode());
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
        Boolean flag=false;
        ReturnOrderReviewReqVo re=new ReturnOrderReviewReqVo();
        BeanUtils.copyProperties(reqVo,re);
        //根据供应链请求修改退货单状态
        returnOrderInfoDao.updateReturnStatus(re);
        if(null!=reqVo.getReturnOrderDetailReviewApiReqVo()&&null!=reqVo.getReturnOrderDetailReviewApiReqVo().getList()){
            //根据供应链请求修改退货单详情表数量
            returnOrderDetailDao.updateActualCountBatch(reqVo.getReturnOrderDetailReviewApiReqVo().getList());
            //根据退货单id查询详情计算金额
            List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(reqVo.getReturnOrderId());
            //查询原始订单详情
            String orderId = returnOrderInfoDao.selectOrderId(reqVo.getReturnOrderId());
            List<ErpOrderItem> erpOrderItems = erpOrderItemService.selectOrderItemListByOrderId(orderId);
            Map<String,BigDecimal> map=new HashMap<>();
            Map<String,BigDecimal> map2=new HashMap<>();
            Map<String,Long> map3=new HashMap<>();
            Map<String,Long> map4=new HashMap<>();
            for(ErpOrderItem eoi:erpOrderItems){
                //存储订单号+linecode 分摊单价
                map.put(eoi.getOrderStoreCode()+eoi.getLineCode(),eoi.getPreferentialAmount());
                //存储订单号+linecode 分摊总价
                map2.put(eoi.getOrderStoreCode()+eoi.getLineCode(),eoi.getTotalPreferentialAmount());
                //存储订单号+linecode 可退总数量
                Long actualInboundCount = eoi.getActualInboundCount();//可退总数量
                map3.put(eoi.getOrderStoreCode()+eoi.getLineCode(),actualInboundCount);
                //存储订单号+linecode 已退数量
                Long returnProductCount=eoi.getReturnProductCount();
                if(returnProductCount==null){
                    returnProductCount=0L;
                }
                //已退数量
                map4.put(eoi.getOrderStoreCode()+eoi.getLineCode(),returnProductCount);
            }
            //此退货单实际退款总金额
            BigDecimal totalMoney=BigDecimal.valueOf(0);
            String returnOrderId="";
            long totalCount=0;
            for(ReturnOrderDetail rod:returnOrderDetails){
                //商品实退数量
                long count=rod.getActualReturnProductCount();
                //已退数量
                Long returnProductCount=map4.get(orderId+rod.getLineCode());
                //分摊后单价
                BigDecimal preferentialAmount = map.get(orderId+rod.getLineCode());
                //优惠分摊总金额（分摊后金额）
                BigDecimal totalPreferentialAmount = map2.get(orderId+rod.getLineCode());
                //实收数量（门店）
                Long actualInboundCount = map3.get(orderId+rod.getLineCode());
                BigDecimal totalMoney1=new BigDecimal(0);
                if((actualInboundCount-returnProductCount)>count){//可退数量大于前端入参退货数量
                    //计算公式：此商品退货总金额=分摊后单价 X 前端入参退货数量
                    totalMoney1=preferentialAmount.multiply(BigDecimal.valueOf(count));
                }else if((actualInboundCount-returnProductCount)==count){//可退数量等于前端入参退货数量
                    //计算公式：此商品退货总金额=分摊总金额-分摊后单价 X 已退数量
                    totalMoney1=totalPreferentialAmount.subtract(preferentialAmount.multiply(BigDecimal.valueOf(returnProductCount)));
                }
                //实退商品总价
                rod.setActualTotalProductAmount(totalMoney1);
                totalMoney.add(totalMoney1);
                returnOrderId=rod.getReturnOrderCode();
                totalCount=totalCount+count;
            }
            //修改主订单实际退货数量、实际退款总金额
            returnOrderInfoDao.updateLogisticsCountAndAmount(returnOrderId,totalMoney,totalCount);
            //修改详情表实际退款金额
            returnOrderDetailDao.updateActualAmountBatch(returnOrderDetails);
            flag=true;
        }
        //退货单状态(供应链使用):4-等待退货验收，5-等待退货入库 11-退货完成
        if(reqVo.getOperateStatus().equals(ConstantData.returnOrderStatusComplete)){
            //发起退款
            flag=refund(reqVo.getReturnOrderId());
        }
        return flag;
    }

    @Override
    public Boolean check(String orderCode) {
        List<ReturnOrderInfo> returnOrderInfo = returnOrderInfoDao.selectByOrderId(orderCode);
        if(CollectionUtils.isNotEmpty(returnOrderInfo)){
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateLogistics(LogisticsVo logisticsVo) {
        int res=returnOrderInfoDao.updateLogistics(logisticsVo);
        return res>0;
    }

    @Override
    public Boolean callback(RefundReq reqVo) {
        log.info("退款回调开始，reqVo={}",reqVo);
        //查询退货单状态是否修改成功
        ReturnOrderInfo returnOrderInfo=returnOrderInfoDao.selectByReturnOrderCode(reqVo.getOrderNo());
        //退款状态，0-未退款、1-已退款
        if(returnOrderInfo!=null&&returnOrderInfo.getRefundStatus().equals(ConstantData.refundStatus)){//1-已退款
            return true;
        }
        RefundInfo record=new RefundInfo();
        record.setOrderCode(reqVo.getOrderNo());
        record.setPayNum(reqVo.getPayNum());
        record.setUpdateTime(new Date());
        record.setStatus(ConstantData.refundStatus);
        refundInfoDao.updateByOrderCode(record);
        return returnOrderInfoDao.updateRefundStatus(reqVo.getOrderNo())>0;
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
     * @param returnOrderCode 退货单编码
     */
    public void createRejectRecord(String returnOrderCode){
        log.info("供应链同步退货单开始,returnOrderId={}",returnOrderCode);
        RejectRecordReq rejectRecordReq=new RejectRecordReq();
        //根据退货单编码查询退货信息
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
        //根据退货单编码查询退货详细信息
        List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
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

    /**
     * 封装--发起退款
     * @param returnOrderCode
     */
    public boolean refund(String returnOrderCode){
        ReturnOrderInfo returnOrderInfo=returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
        log.info("发起退款单申请，returnOrderInfo={}",returnOrderInfo);
        if(returnOrderInfo!=null){
            //同步至支付流水表
            insertRefundInfo(returnOrderInfo.getActualReturnOrderAmount(),returnOrderInfo.getReturnOrderCode(),ConstantData.payTypeRefund);
            String url=paymentHost+"/payment/pay/payTobAll";
            JSONObject json=new JSONObject();
            json.put("order_no",returnOrderInfo.getReturnOrderCode());
            json.put("order_amount",returnOrderInfo.getActualReturnOrderAmount());
            json.put("fee",0);
            json.put("order_time",returnOrderInfo.getCreateTime());
            json.put("pay_type",returnOrderInfo.getPaymentCode());
            json.put("order_source",returnOrderInfo.getSourceType());
            json.put("create_by",returnOrderInfo.getCityId());
            json.put("update_by",returnOrderInfo.getCreateByName());
            json.put("order_type",4);
            //根据门店id获取加盟商id
            String franchiseeId=getFranchiseeId(returnOrderInfo.getStoreId());
            json.put("franchisee_id",franchiseeId);
            json.put("store_name",returnOrderInfo.getStoreName());
            json.put("store_id",returnOrderInfo.getStoreCode());
            Integer method=returnOrderInfo.getTreatmentMethod();
            //处理办法 0退货退款  1仅退款
            if(null!=method&&method.equals(0)){//RETURN_REFUND 退货退款
                json.put("transactionType","RETURN_REFUND");
            }else if(null!=method&&method.equals(1)){//"REFUND_ONLY 仅退款
                json.put("transactionType","REFUND_ONLY");
            }
            //订单类型 0直送、1配送、2辅采
            Integer type=returnOrderInfo.getOrderType();
            if(null!=type&&type.equals(0)){//订单类型 14配送tob 2直送tob
                json.put("pay_order_type",2);
                json.put("pay_origin_type",10);
            }else if(null!=type&&type.equals(1)){
                json.put("pay_order_type",14);
                json.put("pay_origin_type",6);
            }
            json.put("pay_order_type",returnOrderInfo.getOrderType());
            json.put("back_url","http://order.api.aiqin.com/returnOrder/callback");
            String request= URLConnectionUtil.doPost(url,null,json);
            log.info("发起退款单申请，request={}",request);
            if(StringUtils.isNotBlank(request)){
                JSONObject jsonObject= JSON.parseObject(request);
                if(jsonObject.containsKey("code")&&"0".equals(jsonObject.getString("code"))){
//                    log.info("退款完成，修改退货单状态");
//                    //查询退货单状态是否修改成功
//                    ReturnOrderInfo returnOrderInfo1=returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
//                    //退款状态，0-未退款、1-已退款
//                    if(returnOrderInfo1!=null&&returnOrderInfo1.getRefundStatus().equals(ConstantData.refundStatus)){//1-已退款
//                        return true;
//                    }
//                    returnOrderInfoDao.updateRefundStatus(returnOrderCode);
                    log.info("退款完成");
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 根据门店id获取加盟商id
     * @param storeId
     * @return
     */
    public String getFranchiseeId(String storeId){
        log.info("根据门店id获取加盟商id,storeId={}",storeId);
        String franchiseeId=null;
        String url=slcsHost+"/store/getFranchiseeId?store_id="+storeId;
        String request= URLConnectionUtil.doGet(url,null,null);
        log.info("根据门店id获取加盟商id,request={}",request);
        if(StringUtils.isNotBlank(request)){
            JSONObject json=JSON.parseObject(request);
            if(json.containsKey("code")&&"0".equals(json.get("code"))){
                franchiseeId=json.getString("data");
            }
        }
        return franchiseeId;
    }

    /**
     * 同步至支付流水表
     * @param amount
     * @param orderCode
     * @param payType
     * @return
     */
    public Boolean insertRefundInfo(BigDecimal amount,String orderCode,Integer payType){
        RefundInfo record=new RefundInfo();
        record.setOrderAmount(amount);
        record.setOrderCode(orderCode);
//        record.setPayNum(payNum);
        record.setPayType(payType);
        int res=refundInfoDao.insertSelective(record);
        return res>0;
    }

    @Override
    public ReturnOrderDetailVO detail(String returnOrderCode) {
        //通过退货单号校验退货单是否存在
        Map queryData = new HashMap();
        queryData.put("returnOrderCode",returnOrderCode);
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByParameter(queryData);
        Assert.notNull(returnOrderInfo, "退货单不存在");

        //查询退货单详情
        List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(returnOrderCode);
        ReturnOrderDetailVO respVo = new ReturnOrderDetailVO();
        respVo.setReturnOrderInfo(returnOrderInfo);
        respVo.setDetails(returnOrderDetails);

        return respVo;
    }

    @Override
    public PageResData<ReturnOrderInfo> getlist(PageRequestVO<AfterReturnOrderSearchVo> searchVo) {
        if(searchVo.getSearchVO()!=null&&null!=searchVo.getSearchVO().getAreaReq()){
            String url=slcsHost+"/store/getStoreAllId";
            JSONObject json=new JSONObject();
            if(StringUtils.isNotBlank(searchVo.getSearchVO().getAreaReq().getCityId())){
                json.put("cityId",searchVo.getSearchVO().getAreaReq().getCityId());
            }
            if(StringUtils.isNotBlank(searchVo.getSearchVO().getAreaReq().getDistrictId())){
                json.put("districtId",searchVo.getSearchVO().getAreaReq().getDistrictId());
            }
            if(StringUtils.isNotBlank(searchVo.getSearchVO().getAreaReq().getProvinceId())){
                json.put("provinceId",searchVo.getSearchVO().getAreaReq().getProvinceId());
            }
            if(!json.isEmpty()){
                String request= URLConnectionUtil.doPost(url,null,json);
                log.info("根据省市区id查询门店请求结果，request={}",request);
                if(StringUtils.isNotBlank(request)){
                    JSONObject jsonObject=JSON.parseObject(request);
                    if(jsonObject.containsKey("code")&&jsonObject.get("code").equals("0")&&jsonObject.containsKey("data")){
                        List<String> ids=JSON.parseArray(jsonObject.getString("data"),String.class);
                        if(CollectionUtils.isNotEmpty(ids)){
                            searchVo.getSearchVO().setStoreIds(ids);
                        }
                    }
                }
            }
        }
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        log.info("erp售后管理--退货单列表入参，searchVo={}",searchVo);
        List<ReturnOrderInfo> content = returnOrderInfoDao.selectAll(searchVo.getSearchVO());
        return new PageResData(Integer.valueOf((int)((Page) content).getTotal()) , content);
    }

    @Override
    public HttpResponse getAmount(String orderCode, Long lineCode,Long number) {
        ErpOrderItem erpOrderItem = erpOrderItemService.getItemByOrderCodeAndLine(orderCode, lineCode);
        //此商品退货总金额
        BigDecimal totalMoney=new BigDecimal(0);
        if(erpOrderItem!=null){
            //已退数量
            Long returnProductCount=erpOrderItem.getReturnProductCount();
            if(returnProductCount==null){
                returnProductCount=0L;
            }
            //分摊后单价
            BigDecimal preferentialAmount = erpOrderItem.getPreferentialAmount();
            if(preferentialAmount==null){
                return HttpResponse.failure(ResultCode.RETURN_PRE_AMOUNT_ERROR);
            }
            //优惠分摊总金额（分摊后金额）
            BigDecimal totalPreferentialAmount = erpOrderItem.getTotalPreferentialAmount();
            if(totalPreferentialAmount==null){
                return HttpResponse.failure(ResultCode.RETURN_TOTAL_AMOUNT_ERROR);
            }
            //实收数量（门店）
            Long actualInboundCount = erpOrderItem.getActualInboundCount();
            if(actualInboundCount==null){
                return HttpResponse.failure(ResultCode.RETURN_ACUNUM_WRONG_ERROR);
            }
            if((actualInboundCount-returnProductCount)>number){//可退数量大于前端入参退货数量
                //计算公式：此商品退货总金额=分摊后单价 X 前端入参退货数量
                totalMoney=preferentialAmount.multiply(BigDecimal.valueOf(number));
                return HttpResponse.success(totalMoney);
            }else if((actualInboundCount-returnProductCount)==number){//可退数量等于前端入参退货数量
                //计算公式：此商品退货总金额=分摊总金额-分摊后单价 X 已退数量
                totalMoney=totalPreferentialAmount.subtract(preferentialAmount.multiply(BigDecimal.valueOf(returnProductCount)));
                return HttpResponse.success(totalMoney);
            }
            return HttpResponse.failure(ResultCode.RETURN_NUM_WRONG_ERROR);
        }
        return HttpResponse.failure(ResultCode.RETURN_AMOUNT_ERROR);
    }

    @Override
    public HttpResponse getReturnStatus() {
        List<ReturnOrderStatusVo> list=new ArrayList<>();
        for (ReturnOrderStatusEnum s : ReturnOrderStatusEnum.values()){
            ReturnOrderStatusVo returnOrderStatusVo=new ReturnOrderStatusVo();
            returnOrderStatusVo.setStatus(s.getKey());
            returnOrderStatusVo.setName(s.getMsg());
            list.add(returnOrderStatusVo);
        }
        return HttpResponse.success(list);
    }

}
