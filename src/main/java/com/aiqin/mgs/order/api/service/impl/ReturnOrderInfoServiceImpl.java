package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.*;
import com.aiqin.mgs.order.api.component.SequenceService;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderReturnRequestEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderReturnStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayOrderSourceEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpRequestPayTypeEnum;
import com.aiqin.mgs.order.api.component.returnenums.*;
import com.aiqin.mgs.order.api.dao.CouponApprovalDetailDao;
import com.aiqin.mgs.order.api.dao.CouponApprovalInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderOperationLogDao;
import com.aiqin.mgs.order.api.dao.returnorder.RefundInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;
import com.aiqin.mgs.order.api.domain.request.returnorder.*;
import com.aiqin.mgs.order.api.domain.response.returnorder.ReturnOrderStatusVo;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.bill.RejectRecordService;
import com.aiqin.mgs.order.api.service.order.ErpOrderInfoService;
import com.aiqin.mgs.order.api.service.order.ErpOrderItemService;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import com.aiqin.mgs.order.api.service.returnorder.ReturnOrderInfoService;
import com.aiqin.platform.flows.client.constant.AjaxJson;
import com.aiqin.platform.flows.client.constant.FormUpdateUrlType;
import com.aiqin.platform.flows.client.constant.StatusEnum;
import com.aiqin.platform.flows.client.domain.vo.ActBaseProcessEntity;
import com.aiqin.platform.flows.client.domain.vo.StartProcessParamVO;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
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

    @Value("${bridge.url.product-api}")
    private String productHost;

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
    @Resource
    private ErpOrderOperationLogDao erpOrderOperationLogDao;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderItemDao erpOrderItemDao;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private CopartnerAreaService copartnerAreaService;


    @Override
    @Transactional
    public HttpResponse save(ReturnOrderReqVo reqVo) {
        log.info("发起退货--入参，reqVo={}",reqVo);
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
            case 0:
                record.setReturnOrderStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_REFUND.getKey());
                record.setActualProductCount(record.getProductCount());
                record.setActualReturnOrderAmount(record.getReturnOrderAmount());
                break;
            case 1:

            case 2:
                record.setReturnOrderStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_WAIT.getKey());
                break;
            default:
                record.setReturnOrderStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_WAIT.getKey());
        }
        //退货单--退款方式 1:现金 2:微信 3:支付宝 4:银联 5:退到加盟商账户
        record.setReturnMoneyType(ConstantData.RETURN_MONEY_TYPE);
        record.setOrderSuccess(1);
        log.info("发起退货--插入退货信息，record={}",record);
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
        log.info("发起退货--插入退货详情，details={}",details);
        returnOrderDetailDao.insertBatch(details);
        //添加日志
        log.info("发起退货--插入日志，details={}",details);
        insertLog(afterSaleCode,reqVo.getCreateById(),reqVo.getCreateByName(),ErpLogOperationTypeEnum.ADD.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_WAIT.getKey(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_WAIT.getMsg());
        //修改原始订单数据
        log.info("发起退货--修改原始订单数据开始,入参orderStoreCode={},orderReturnStatusEnum={},returnQuantityList={},personId={},personName={}",record.getOrderStoreCode(), ErpOrderReturnStatusEnum.WAIT,null,record.getCreateById(),record.getCreateByName());
        erpOrderInfoService.updateOrderReturnStatus(record.getOrderStoreCode(), ErpOrderReturnRequestEnum.WAIT,null,record.getCreateById(),record.getCreateByName());
        log.info("发起退货--修改原始订单数据结束");
        //如果是配送质量退货，请求时调用门店退货申请
        if(!("15".equals(reqVo.getReturnReasonCode())&&reqVo.getOrderType().equals(2))){
            //门店退货申请-完成(门店)（erp回调）--修改商品库存
            String url=productHost+"/order/return/insert";
            JSONObject body=new JSONObject();
            body.put("create_by_id",reqVo.getCreateById());
            body.put("create_by_name",reqVo.getCreateByName());
            body.put("order_code",record.getOrderStoreCode());
            body.put("order_return_code", afterSaleCode);
            body.put("store_id",record.getStoreId());
            List<Map<String, Object>> list = new ArrayList<>();
            for(ReturnOrderDetail rod:details){
                String skuCode=rod.getSkuCode();
                Long returnQuantity=rod.getReturnProductCount();
                Map<String,Object> map=new HashMap<>();
                map.put("sku_code",skuCode);
                map.put("return_quantity",returnQuantity);
                map.put("create_by_id",reqVo.getCreateById());
                map.put("create_by_name",reqVo.getCreateByName());
                list.add(map);
            }
            body.put("order_return_product_reqs",list);
            log.info("发起门店退货申请-完成(门店)（erp回调）--修改商品库存入参，url={},json={}",url,body);
            HttpClient httpClient = HttpClient.post(url).json(body);
            Map<String ,Object> result=null;
            try{
                result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
                log.info("发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存结果，request={}",result);
                if(result!=null&&"0".equals(result.get("code"))){
                    log.info("发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存完成");
                    return HttpResponse.success();
                }else {
                    log.info("发起发起门店退货申请-完成(门店)（erp回调）--第三方修改商品库存失败");
                    throw new RuntimeException();
                }
            }catch (Exception e){
                log.info("发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存失败");
                throw e;
            }
        }
        return HttpResponse.success();
    }

    @Override
    public PageResData<ReturnOrderInfo> list(ReturnOrderSearchVo searchVo) {
        log.info("后台销售退货单管理列表入参searchVo={}",searchVo);
        PageResData pageResData=new PageResData();
        if(StringUtils.isBlank(searchVo.getPersonId())||StringUtils.isBlank(searchVo.getResourceCode())){
            return pageResData;
        }
        log.info("调用合伙人数据权限控制公共接口入参,personId={},resourceCode={}",searchVo.getPersonId(),searchVo.getResourceCode());
        HttpResponse httpResponse = copartnerAreaService.selectStoreByPerson(searchVo.getPersonId(), searchVo.getResourceCode());
        List<PublicAreaStore> dataList = JSONArray.parseArray(JSON.toJSONString(httpResponse.getData()), PublicAreaStore.class);
        log.info("调用合伙人数据权限控制公共接口返回结果,dataList={}",dataList);
        if (dataList == null || dataList.size() == 0) {
            return pageResData;
        }
        //遍历门店id
        List<String>  storesIds = dataList.stream().map(PublicAreaStore::getStoreId).collect(Collectors.toList());
        log.info("门店ids={}",storesIds);
        if(storesIds!=null&&storesIds.size()>0){
            searchVo.setStoreIds(storesIds);
        }else{
            return pageResData;
        }
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        List<ReturnOrderInfo> content = returnOrderInfoDao.page(searchVo);
//        Integer pageCount = returnOrderInfoDao.pageCount(searchVo);
        return new PageResData<>(Integer.valueOf((int)((Page) content).getTotal()), content);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse updateReturnStatus(ReturnOrderReviewReqVo reqVo) {
        log.info("退货单审核--入参,reqVo={}",reqVo);
        boolean sysFlag = false;
        boolean couponFlag = false;
        boolean cancelFlag = false;
        boolean erplFlag = false;
        //处理办法 1--退货退款(通过) 2--挂账 3--不通过(驳回) 4--仅退款 99--已取消
        String content="";
        String isPass="";
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(reqVo.getReturnOrderCode());
        switch (reqVo.getOperateStatus()) {
            case 1:
                reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_COM.getKey());
                //处理办法 1--退货退款(通过)
                reqVo.setTreatmentMethod(TreatmentMethodEnum.RETURN_AMOUNT_AND_GOODS_TYPE.getCode());
                content=ReturnOrderStatusEnum.RETURN_ORDER_STATUS_COM.getMsg();
                //同步数据到供应链
                sysFlag = true;
                break;
            case 2:
                reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_APPLY.getKey());
                //处理办法 2--挂账
                reqVo.setTreatmentMethod(TreatmentMethodEnum.BOOK_TYPE.getCode());
                content=ReturnOrderStatusEnum.RETURN_ORDER_STATUS_APPLY.getMsg();
                //调用A品卷审批
                couponFlag = true;
                break;
            case 3:
                reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_FALL.getKey());
                reqVo.setTreatmentMethod(TreatmentMethodEnum.FALL_TYPE.getCode());
                content=ReturnOrderStatusEnum.RETURN_ORDER_STATUS_FALL.getMsg();
                isPass=StoreStatusEnum.PAY_ORDER_TYPE_PEI.getKey().toString();
                erplFlag=true;
                break;
            case 99://撤销
                reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_REMOVE.getKey());
                content=ReturnOrderStatusEnum.RETURN_ORDER_STATUS_FALL.getMsg();
                //如果退货状态为11，则不进行撤销，继续向下走流程
                if(returnOrderInfo.getReturnOrderStatus().equals(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_RETURN.getKey())){
                    log.info("退货单审核--取消撤销操作");
                    return HttpResponse.failure(ResultCode.RETURN_ORDER_CANCEL_FALL);
                }
                isPass=StoreStatusEnum.PAY_ORDER_TYPE_PEI.getKey().toString();
                cancelFlag=true;
                erplFlag=true;
                break;
            default:
                return HttpResponse.failure(ResultCode.RETURN_ORDER_STATUS_NOT_FOUND);
        }
        //修改退货单状态
        reqVo.setReviewTime(new Date());
        Integer review = returnOrderInfoDao.updateReturnStatus(reqVo);
        //添加日志
        insertLog(reqVo.getReturnOrderCode(),reqVo.getOperator(),reqVo.getOperator(),ErpLogOperationTypeEnum.UPDATE.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(),reqVo.getOperateStatus(),content);
        if (couponFlag) {
            log.info("驳回--进入A品券发放审批");
            //生成审批编码
            String approvalCode=createFormNo();
            //同步审批信息到本地表中（新增）
            CouponApprovalInfo record=approvalInfoInsert(reqVo.getOperatorId(),reqVo.getOperator(),approvalCode);
            log.info("同步审批信息入参: reqVo={},approvalCode={}", reqVo,approvalCode);
            couponApprovalInfoDao.insertSelective(record);
            CouponApprovalDetail approvalDetail=reqVo.getApprovalDetail();
            approvalDetail.setFormNo(approvalCode);
            approvalDetail.setCouponType(ConstantData.COUPON_TYPE);
            approvalDetail.setCreator(reqVo.getOperator());
            approvalDetail.setRemark(reqVo.getReviewNote());
            //根据门店查询加盟商id
            String franchiseeId=getFranchiseeId(reqVo.getApprovalDetail().getStoreId());
            approvalDetail.setFranchiseeId(franchiseeId);
            approvalDetail.setOrderId(reqVo.getReturnOrderCode());
            couponApprovalDetailDao.insertSelective(approvalDetail);
            log.info("同步审批信息到本地完成");
            //A品券发放审批申请提交
            submitActBaseProcess(approvalCode,reqVo.getOperatorId(),reqVo.getDeptCode());
        }
        if (sysFlag) {
            // 同步到供应链，生成退供单
            log.info("erp同步供应链，生成退供单开始,returnOrderCode={}",reqVo.getReturnOrderCode());
            HttpResponse httpResponse=rejectRecordService.createRejectRecord(reqVo.getReturnOrderCode());
            log.info("erp同步供应链，生成退供单结束,httpResponse={}",httpResponse);
            if(!"0".equals(httpResponse.getCode())){
                //erp同步供应链，生成退供单失败
//                return HttpResponse.failure(ResultCode.RETURN_ORDER_SYNCHRONIZATION_FALL);
                throw new RuntimeException("erp同步供应链，生成退供单失败");
            }
        }
        if(cancelFlag){
            //通知退供单-撤销
            log.info("通知退供单-撤销开始，rejectRecordCode={}",reqVo.getReturnOrderCode());
            rejectRecordService.removeRejectRecordStatus(reqVo.getReturnOrderCode());
            log.info("通知退供单-撤销结束");
        }
        //配送且一般退货调用且审核通过，调用门店退货申请-完成(门店)
        if("15".equals(returnOrderInfo.getReturnReasonCode())&&returnOrderInfo.getOrderType().equals(2)&&sysFlag){
            //门店退货申请-完成(门店)（erp回调）--修改商品库存
            String url=productHost+"/order/return/insert";
            JSONObject body=new JSONObject();
            body.put("create_by_id",returnOrderInfo.getCreateById());
            body.put("create_by_name",returnOrderInfo.getCreateByName());
            body.put("order_code",returnOrderInfo.getOrderStoreCode());
            body.put("order_return_code", returnOrderInfo.getReturnOrderCode());
            body.put("store_id",returnOrderInfo.getStoreId());
            List<ReturnOrderDetail> details= returnOrderDetailDao.selectListByReturnOrderCode(returnOrderInfo.getReturnOrderCode());
            List<Map<String, Object>> list = new ArrayList<>();
            for(ReturnOrderDetail rod:details){
                String skuCode=rod.getSkuCode();
                Long returnQuantity=rod.getReturnProductCount();
                Map<String,Object> map=new HashMap<>();
                map.put("sku_code",skuCode);
                map.put("return_quantity",returnQuantity);
                map.put("create_by_id",returnOrderInfo.getCreateById());
                map.put("create_by_name",returnOrderInfo.getCreateByName());
                list.add(map);
            }
            body.put("order_return_product_reqs",list);
            log.info("配送一般退货--发起门店退货申请-完成(门店)（erp回调）--修改商品库存入参，url={},json={}",url,body);
            HttpClient httpClient = HttpClient.post(url).json(body);
            Map<String ,Object> result=null;
            try{
                result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
                log.info("配送一般退货--发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存结果，request={}",result);
                if(result!=null&&"0".equals(result.get("code"))){
                    log.info("配送一般退货--发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存完成");
//                    return HttpResponse.success();
                }else {
                    log.info("配送一般退货--发起发起门店退货申请-完成(门店)（erp回调）--第三方修改商品库存失败");
                    throw new RuntimeException();
                }
            }catch (Exception e){
                log.info("配送一般退货--发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存失败");
                throw e;
            }
        }
        //调用门店退货申请-完成(门店)（erp回调）---订货管理-修改退货申请单
        if(StringUtils.isNotBlank(isPass)){
            updateStoreStatus(reqVo.getReturnOrderCode(),isPass,returnOrderInfo.getStoreId(),reqVo.getOperatorId(),reqVo.getOperator(),"0");
        }
        //修改原始订单数据
        if(erplFlag){
            log.info("退货单审核--修改原始订单数据开始,入参orderStoreCode={},orderReturnStatusEnum={},returnQuantityList={},personId={},personName={}",returnOrderInfo.getOrderStoreCode(), ErpOrderReturnStatusEnum.SUCCESS,null,reqVo.getOperatorId(),reqVo.getOperator());
            erpOrderInfoService.updateOrderReturnStatus(returnOrderInfo.getOrderStoreCode(), ErpOrderReturnRequestEnum.CANCEL,null,reqVo.getOperatorId(),reqVo.getOperator());
        }
        return HttpResponse.success();
    }

    @Override
    @Transactional
    public HttpResponse updateReturnOrderDetail(ReturnOrderDetailVO records) {
        log.info("退货单详情修改开始,入参records={}",records);
        List<ReturnOrderDetail> details=records.getDetails();
        String returnOrderCode = records.getReturnOrderCode();
        if(StringUtils.isNotBlank(returnOrderCode)&&CollectionUtils.isNotEmpty(details)){
            //根据退货单id，删除详情记录
            returnOrderDetailDao.deleteByReturnOrderCode(returnOrderCode);
            details = details.stream().map(detailVo -> {
                ReturnOrderDetail detail = new ReturnOrderDetail();
                BeanUtils.copyProperties(detailVo, detail);
                detail.setCreateTime(new Date());
                detail.setReturnOrderDetailId(IdUtil.uuid());
                detail.setReturnOrderCode(returnOrderCode);
                detail.setCreateById(records.getCreateId());
                detail.setCreateByName(records.getCreator());
                detail.setRemark("");
                detail.setEvidenceUrl("");
                return detail;
            }).collect(Collectors.toList());
            log.info("退货单详情修改,details={}",details);
            returnOrderDetailDao.insertBatch(details);
            //添加日志
            insertLog(returnOrderCode,records.getCreateId(),records.getCreator(),ErpLogOperationTypeEnum.UPDATE.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_WAIT.getKey(),ConstantData.RETURN_ORDER_DETAIL);
            //修改主表退货金额和数量
            BigDecimal actualReturnOrderAmount=records.getReturnOrderInfo().getReturnOrderAmount();
            Long actualProductCount=records.getReturnOrderInfo().getProductCount();
            returnOrderInfoDao.updateCountAndAmount(returnOrderCode,actualReturnOrderAmount,actualProductCount);
            return HttpResponse.success();
        }
        return HttpResponse.failure(ResultCode.RETURN_ORDER_PARAMETER_FALL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateReturnStatusApi(ReturnOrderReviewApiReqVo reqVo) {
        log.info("供应链入库完成--回调退货单，修改退货单状态及详情数量开始，reqVo={}",reqVo);
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(reqVo.getReturnOrderCode());
        RefundInfo refundInfo = refundInfoDao.selectByOrderCode(reqVo.getReturnOrderCode());
        if(returnOrderInfo.getRefundStatus().equals(1)||refundInfo!=null){
            log.info("供应链入库完成--回调退货单，此退货单已退款");
            return true;
        }
        Boolean flag=false;
        ReturnOrderReviewReqVo re=new ReturnOrderReviewReqVo();
        //状态为11-退货完成
        reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_RETURN.getKey());
        BeanUtils.copyProperties(reqVo,re);
        //根据供应链请求修改退货单状态
        log.info("供应链入库完成--回调退货单，修改退货单状态，re={}",re);
        returnOrderInfoDao.updateReturnStatus(re);
        if(CollectionUtils.isNotEmpty(reqVo.getDetails())){
            //根据供应链请求修改退货单详情表数量
            reqVo.getDetails().forEach(p -> p.setReturnOrderCode(reqVo.getReturnOrderCode()));
            log.info("供应链入库完成--回调退货单，修改退货单详情表数量，details()={}",reqVo.getDetails());
            returnOrderDetailDao.updateActualCountBatch(reqVo.getDetails());
            //根据退货单id查询详情计算金额
            List<ReturnOrderDetail> returnOrderDetails = returnOrderDetailDao.selectListByReturnOrderCode(reqVo.getReturnOrderCode());
            //查询原始订单详情
            String orderId = returnOrderInfoDao.selectOrderId(reqVo.getReturnOrderCode());
            ErpOrderItem query = new ErpOrderItem();
            query.setOrderStoreCode(orderId);
            log.info("供应链入库完成--回调退货单，查询原始订单详情,入参，query={}",query);
            List<ErpOrderItem> erpOrderItems = erpOrderItemDao.select(query);
            log.info("供应链入库完成--回调退货单，查询原始订单详情,结果，erpOrderItems={}",erpOrderItems);
            if(CollectionUtils.isEmpty(erpOrderItems)){
                throw new NullPointerException("查询原始订单详情为空");
            }
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
            log.info("供应链入库完成--回调退货单，分摊单价:map={},分摊总价:map2={},可退总数量:map3={},已退数量:map4={}",map,map2,map3,map4);
            //此退货单实际退款总金额
            BigDecimal totalMoneyAll=BigDecimal.valueOf(0);
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
                BigDecimal totalMoney=new BigDecimal(1);
                log.info("供应链入库完成--回调退货单，商品实退数量:count={},已退数量:returnProductCount={},分摊后单价:preferentialAmount={},优惠分摊总金额（分摊后金额）:totalPreferentialAmount={},实收数量（门店）actualInboundCount={}",count,returnProductCount,preferentialAmount,totalPreferentialAmount,actualInboundCount);
                if((actualInboundCount-returnProductCount)>count){//可退数量大于前端入参退货数量
                    //计算公式：此商品退货总金额=分摊后单价 X 前端入参退货数量
                    totalMoney=preferentialAmount.multiply(BigDecimal.valueOf(count));
                }else if((actualInboundCount-returnProductCount)==count){//可退数量等于前端入参退货数量
                    //计算公式：此商品退货总金额=分摊总金额-分摊后单价 X 已退数量
                    totalMoney=totalPreferentialAmount.subtract(preferentialAmount.multiply(BigDecimal.valueOf(returnProductCount)));
                }
                //实退商品总价
                rod.setActualTotalProductAmount(totalMoney);
                totalMoneyAll=totalMoneyAll.add(totalMoney);
                returnOrderId=rod.getReturnOrderCode();
                totalCount=totalCount+count;
            }
            //修改主订单实际退货数量、实际退款总金额
            log.info("供应链入库完成--回调退货单,修改主订单实际退货数量、实际退款总金额入参,returnOrderId={},totalMoneyAll={},totalCount={}",returnOrderId,totalMoneyAll,totalCount);
            returnOrderInfoDao.updateLogisticsCountAndAmount(returnOrderId,totalMoneyAll,totalCount);
            //修改主订单退货数量、退款总金额
            returnOrderInfoDao.updateCountAndAmount(returnOrderId,totalMoneyAll,totalCount);
            //修改详情表实际退款金额
            log.info("供应链入库完成--回调退货单，修改详情表实际退款金额入参,returnOrderDetails={}",returnOrderDetails);
            returnOrderDetailDao.updateActualAmountBatch(returnOrderDetails);
            flag=true;
        }
        //退货单状态(供应链使用):4-等待退货验收，5-等待退货入库 11-退货完成
        log.info("供应链入库完成--回调退货单，退货单状态,operateStatus={}",reqVo.getOperateStatus());
        if(reqVo.getOperateStatus().equals(ConstantData.RETURN_ORDER_STATUS_COMPLETE)){
            log.info("供应链入库完成--回调退货单，发起退款开始");
            //发起退款
            flag=refund(reqVo.getReturnOrderCode());
        }
        return flag;
    }

    @Override
    public Boolean updateOrderSuccessApi(String returnOrderCode) {
        //return returnOrderInfoDao.updateOrderSuccess(returnOrderCode)>0;
        return false;
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
    @Transactional(rollbackFor=Exception.class)
    public Boolean updateLogistics(LogisticsVo logisticsVo) {
        int res=returnOrderInfoDao.updateLogistics(logisticsVo);
        return res>0;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public Boolean callback(RefundReq reqVo) {
        log.info("退款回调开始，reqVo={}",reqVo);
        //查询退货单状态是否修改成功
        ReturnOrderInfo returnOrderInfo=returnOrderInfoDao.selectByReturnOrderCode(reqVo.getOrderNo());
        log.info("退款回调--查询退货单,返回结果returnOrderInfo={}",returnOrderInfo);
        //退款状态，0-未退款、1-已退款
        if(returnOrderInfo!=null&&returnOrderInfo.getRefundStatus().equals(ConstantData.REFUND_STATUS)){//1-已退款
            return true;
        }
        RefundInfo record=new RefundInfo();
        record.setOrderCode(reqVo.getOrderNo());
        record.setPayNum(reqVo.getPayNum());
        record.setUpdateTime(new Date());
        record.setStatus(ConstantData.REFUND_STATUS);
        log.info("退款回调--修改退款流水,record={}",record);
        refundInfoDao.updateByOrderCode(record);
        log.info("退款回调--修改退款流水完成");
        //添加日志
        insertLog(reqVo.getOrderNo(),ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR,ErpLogOperationTypeEnum.UPDATE.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_REFUND.getKey(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_REFUND.getMsg());
        log.info("退款回调--修改退货单退款状态开始,入参returnOrderCode={}",reqVo.getOrderNo());
        returnOrderInfoDao.updateRefundStatus(reqVo.getOrderNo());
        log.info("退款回调--修改退货单退款状态结束");
        // 调用门店退货申请-完成(门店)（erp回调）---订货管理-修改退货申请单（减库存）
        ReturnOrderInfo roi=returnOrderInfoDao.selectByReturnOrderCode(reqVo.getOrderNo());
        //除了冲减单，都要回调门店和修改原始订单
        if(!roi.getReturnOrderType().equals(ReturnOrderTypeEnum.WRITE_DOWN_ORDER_TYPE.getCode())){
            updateStoreStatus(reqVo.getOrderNo(),StoreStatusEnum.PAY_ORDER_TYPE_ZHI.getKey().toString(),roi.getStoreId(),ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR,roi.getActualProductCount().toString());
            //修改原始订单数据
            List<ReturnOrderDetail> details = returnOrderDetailDao.selectListByReturnOrderCode(reqVo.getOrderNo());
            List<ErpOrderItem> returnQuantityList=new ArrayList<>();
            for(ReturnOrderDetail rod:details){
                ErpOrderItem eoi=new ErpOrderItem();
                eoi.setLineCode(rod.getLineCode());
                eoi.setReturnProductCount(rod.getActualReturnProductCount());
                returnQuantityList.add(eoi);
            }
            log.info("退款回调--修改原始订单数据开始,入参orderStoreCode={},orderReturnStatusEnum={},returnQuantityList={},personId={},personName={}",roi.getOrderStoreCode(), ErpOrderReturnStatusEnum.SUCCESS,returnQuantityList,ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR);
            erpOrderInfoService.updateOrderReturnStatus(roi.getOrderStoreCode(), ErpOrderReturnRequestEnum.SUCCESS,returnQuantityList,ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR);
        }
        log.info("退款回调结束");
        return true;
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
        paramVO.setCostType(ConstantData.APPLY_COUPON01_KEY);
        paramVO.setFormType(ConstantData.APPLY_COUPON01_KEY);
        paramVO.setFormUrl("http://order.api.aiqin.com/approval/formDetail/" + approvalCode);//表单详情页面地址 必传
        paramVO.setCurrentUser(applier);
        paramVO.setFormNo(approvalCode);
        paramVO.setTitle("A品卷发放审批");
        paramVO.setRemark("A品卷发放审批");
        paramVO.setFormUpdateUrl("http://order.api.aiqin.com/approval/callback");//回调地址
        paramVO.setFormUpdateUrlType(FormUpdateUrlType.HTTP);
        paramVO.setSignTicket(IdUtil.uuid());
        paramVO.setReceiptType("1");
        paramVO.setPositionCode(positionCode);
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
     * 封装--发起退款
     * @param returnOrderCode
     */
    public boolean refund(String returnOrderCode){
        ReturnOrderInfo returnOrderInfo=returnOrderInfoDao.selectByReturnOrderCode(returnOrderCode);
        log.info("发起退款单申请，returnOrderInfo={}",returnOrderInfo);
        if(returnOrderInfo!=null){
            //同步至支付流水表
            insertRefundInfo(returnOrderInfo.getActualReturnOrderAmount(),returnOrderInfo.getReturnOrderCode(),ConstantData.PAY_TYPE_REFUND);
            String url=paymentHost+"/payment/pay/payTobAll";
            JSONObject json=new JSONObject();
            json.put("order_no",returnOrderInfo.getReturnOrderCode());
            BigDecimal amount=returnOrderInfo.getActualReturnOrderAmount().multiply(BigDecimal.valueOf(100));
            long amountFen=amount.longValue();
            json.put("order_amount",amountFen);
            json.put("fee",0);
            json.put("order_time",returnOrderInfo.getCreateTime());
            //在线支付
            json.put("pay_type", ErpRequestPayTypeEnum.PAY_10.getCode());
            json.put("order_source", ErpRequestPayOrderSourceEnum.WEB.getCode());
            json.put("create_by",returnOrderInfo.getCreateById());
            json.put("update_by",returnOrderInfo.getCreateByName());
            //4-退款
            json.put("order_type", ErpRequestPayOperationTypeEnum.TYPE_4.getCode());
            //根据门店id获取加盟商id
            String franchiseeId=getFranchiseeId(returnOrderInfo.getStoreId());
            json.put("franchisee_id",franchiseeId);
            json.put("store_name",returnOrderInfo.getStoreName());
            json.put("store_id",returnOrderInfo.getStoreId());
            Integer method=returnOrderInfo.getTreatmentMethod();
            //处理办法 1--退货退款(通过) 2--挂账 3--不通过(驳回) 4--仅退款
            if(null!=method&&method.equals(TreatmentMethodEnum.RETURN_AMOUNT_AND_GOODS_TYPE.getCode())){//RETURN_REFUND 退货退款
//                json.put("transactionType","RETURN_REFUND");
                json.put("transactionType","AFTER_SALE_RETURNS");
            }else if(null!=method&&method.equals(TreatmentMethodEnum.RETURN_AMOUNT_TYPE.getCode())){//"DELIVER_GOODS_DEDUCT 仅退款--发货冲减
                json.put("transactionType","DELIVER_GOODS_DEDUCT");
            }
            //订单类型 0直送、1配送、2辅采
            //1直送 2配送 3货架
            Integer type=returnOrderInfo.getOrderType();
            if(null!=type&&type.equals(1)){//订单类型 14配送tob 2直送tob
                json.put("pay_order_type", PayOrderTypeEnum.PAY_ORDER_TYPE_PEI.getKey());
                json.put("pay_origin_type",PayOriginTypeEnum.DIRECT_SEND_TOB_RETURN.getKey());
            }else if(null!=type&&type.equals(2)){
                json.put("pay_order_type",PayOrderTypeEnum.PAY_ORDER_TYPE_ZHI.getKey());
                json.put("pay_origin_type",PayOriginTypeEnum.TOB_RETURN.getKey());
            }
            json.put("back_url","http://order.api.aiqin.com/returnOrder/callback");
            log.info("发起退款单申请入参，url={},json={}",url,json);
            HttpClient httpClient = HttpClient.post(url).json(json);
            Map<String ,Object> result=null;
            result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
            log.info("发起退款单申请结果，request={}",result);
            if(result!=null&&"0".equals(result.get("code"))){
                log.info("退款完成");
                return true;
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
        HttpClient httpClient = HttpClient.get(url);
        Map<String ,Object> result=null;
        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("根据门店id获取加盟商id，request={}",result);
        if(result!=null&&"0".equals(result.get("code"))){
            franchiseeId=result.get("data").toString();
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
        //查询日志详情
        List<ErpOrderOperationLog> erpOrderOperationLogs = getOrderOperationLogList(returnOrderCode);
        ReturnOrderDetailVO respVo = new ReturnOrderDetailVO();
        respVo.setReturnOrderInfo(returnOrderInfo);
        respVo.setDetails(returnOrderDetails);
        respVo.setLogDetails(erpOrderOperationLogs);
        return respVo;
    }

    @Override
    public PageResData<ReturnOrderInfo> getlist(PageRequestVO<AfterReturnOrderSearchVo> searchVo) {
        log.info("售后管理--退货单列表入参searchVo={}",searchVo);
        PageResData pageResData=new PageResData();
        //根据地区查询出的门店
        List<String> ids=new ArrayList<>();
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
                log.info("根据省市区id查询门店请求入参，url={},body={}",url,json);
                HttpClient httpClient = HttpClient.post(url).json(json);
                Map<String ,Object> result=null;
                result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
                log.info("根据省市区id查询门店请求结果，request={}",result);
                if(result!=null&&"0".equals(result.get("code"))){
                    ids=(List<String>)result.get("data");
                }
            }
        }
        //保存数据权限查询出的数据
        List<String> storesIds=new ArrayList<>();
        List<String> newIds=new ArrayList<>();
        if(searchVo.getSearchVO().getOrderType().equals(2)&&"15".equals(searchVo.getSearchVO().getReturnReasonCode())){//一般退货需要加数据权限
            if(StringUtils.isBlank(searchVo.getSearchVO().getPersonId())||StringUtils.isBlank(searchVo.getSearchVO().getResourceCode())){
                return pageResData;
            }
            log.info("调用合伙人数据权限控制公共接口入参,personId={},resourceCode={}",searchVo.getSearchVO().getPersonId(),searchVo.getSearchVO().getResourceCode());
            HttpResponse httpResponse = copartnerAreaService.selectStoreByPerson(searchVo.getSearchVO().getPersonId(), searchVo.getSearchVO().getResourceCode());
            List<PublicAreaStore> dataList = JSONArray.parseArray(JSON.toJSONString(httpResponse.getData()), PublicAreaStore.class);
            log.info("调用合伙人数据权限控制公共接口返回结果,dataList={}",dataList);
            if (dataList == null || dataList.size() == 0) {
                return pageResData;
            }
            //遍历门店id
            storesIds = dataList.stream().map(PublicAreaStore::getStoreId).collect(Collectors.toList());
            log.info("门店ids={}",storesIds);
            if(ids!=null&&ids.size()>0){
                for(String id:ids){
                    if(storesIds.contains(id)){
                        newIds.add(id);
                    }
                }
            }else{
                newIds.addAll(storesIds);
            }
        }else{//质量退货、直送退货
            if(ids!=null&&ids.size()>0){
                newIds.addAll(ids);
            }
        }
        if(newIds!=null&&newIds.size()>0){
            searchVo.getSearchVO().setStoreIds(newIds);
        }
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        log.info("erp售后管理--退货单列表入参，searchVo={}",searchVo);
        ReturnOrderQueryVo queryVo=new ReturnOrderQueryVo();
        BeanUtils.copyProperties(searchVo.getSearchVO(),queryVo);
        List<ReturnOrderInfo> content = returnOrderInfoDao.selectAll(queryVo);
        pageResData.setTotalCount(Integer.valueOf((int)((Page) content).getTotal()));
        pageResData.setDataList(content);
        return pageResData;
    }

    @Override
    public HttpResponse getAmount(String orderCode, Long lineCode,Long number) {
        ErpOrderItem erpOrderItem = erpOrderItemService.getItemByOrderCodeAndLine(orderCode, lineCode);
        //此商品退货总金额
        BigDecimal totalMoney=new BigDecimal(1);
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

    @Override
    public HttpResponse getEvidenceUrl(String returnOrderDetailId) {
        String url = returnOrderDetailDao.selectUrlsByReturnOrderDetailId(returnOrderDetailId);
        String[] urls=new String[]{};
        if(StringUtils.isNotBlank(url)){
            urls=url.split(",");
        }
        return HttpResponse.success(urls);
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public HttpResponse saveWriteDownOrder(String orderCode) {
        log.info("发起冲减单开始,原始订单编码,orderCode={}",orderCode);
        //根据订单编码查询原始订单数据及详情数据
        ErpOrderInfo erpOrderInfo=erpOrderQueryService.getOrderAndItemByOrderCode(orderCode);
        if(null==erpOrderInfo){
            //此单号有误，未查到订单数据
            return HttpResponse.failure(ResultCode.NOT_FOUND_ORDER_DATA);
        }
        List<ErpOrderItem> itemList=erpOrderInfo.getItemList();
        log.info("发起冲减单,原始订单详情,itemList={}",itemList);
        //冲减单总金额
        BigDecimal totalAmount=new BigDecimal(0);
        //发起冲减单所有商品总数量
        Long totalCount=0L;
        List<ReturnOrderDetail> detailsList=new ArrayList<>();
        if(CollectionUtils.isNotEmpty(itemList)){
            for(ErpOrderItem eoi:itemList){
                ReturnOrderDetail returnOrderDetail=new ReturnOrderDetail();
                //优惠分摊总金额（分摊后金额）
                BigDecimal totalPreferentialAmount=eoi.getTotalPreferentialAmount();
                //分摊后单价
                BigDecimal preferentialAmount=eoi.getPreferentialAmount();
                //数量
                Long productCount=eoi.getProductCount();
                //实发数量
                Long actualProductCount=eoi.getActualProductCount();
                if(null==actualProductCount){
                    actualProductCount=0L;
                }
                //发起冲减单数量
                Long differenceCount=productCount-actualProductCount;
                if(differenceCount.equals(0L)){//无需退款
                    continue;
                }else if(differenceCount>0&&differenceCount<productCount){//部分退
                    //计算公式：此商品退货总金额=分摊后单价 X 发起冲减单数量
                    BigDecimal amount=preferentialAmount.multiply(BigDecimal.valueOf(differenceCount));
                    totalAmount=totalAmount.add(amount);
                    totalCount=totalCount+differenceCount;
                    //todo 少参数
                    BeanUtils.copyProperties(eoi,returnOrderDetail);
                    returnOrderDetail.setActualReturnProductCount(differenceCount);
                    returnOrderDetail.setActualTotalProductAmount(amount);
                    returnOrderDetail.setProductAmount(eoi.getProductAmount());
                    returnOrderDetail.setReturnProductCount(eoi.getProductCount());
                    returnOrderDetail.setProductCategoryCode(eoi.getProductCategoryCode());
                    returnOrderDetail.setProductCategoryName(eoi.getProductCategoryName());
                    detailsList.add(returnOrderDetail);
                }else if(differenceCount.equals(productCount)){//全退
                    //计算公式：优惠分摊总金额（分摊后金额）
                    totalAmount=totalAmount.add(totalPreferentialAmount);
                    totalCount=totalCount+differenceCount;
                    BeanUtils.copyProperties(eoi,returnOrderDetail);
                    returnOrderDetail.setActualReturnProductCount(differenceCount);
                    returnOrderDetail.setActualTotalProductAmount(totalPreferentialAmount);
                    returnOrderDetail.setProductAmount(eoi.getProductAmount());
                    returnOrderDetail.setReturnProductCount(eoi.getProductCount());
                    returnOrderDetail.setProductCategoryCode(eoi.getProductCategoryCode());
                    returnOrderDetail.setProductCategoryName(eoi.getProductCategoryName());
                    detailsList.add(returnOrderDetail);
                }
            }
            log.info("发起冲减单,退货单详情,detailsList={}",detailsList);
            if(CollectionUtils.isNotEmpty(detailsList)){
                ReturnOrderInfo returnOrderInfo=new ReturnOrderInfo();
                BeanUtils.copyProperties(erpOrderInfo,returnOrderInfo);
                //插入数据库
                String returnOrderId = IdUtil.uuid();
                String returnOrderCode = sequenceService.generateOrderAfterSaleCode(erpOrderInfo.getCompanyCode(), ReturnOrderTypeEnum.WRITE_DOWN_ORDER_TYPE.getCode());
                returnOrderInfo.setReturnOrderId(returnOrderId);
                returnOrderInfo.setReturnOrderCode(returnOrderCode);
                returnOrderInfo.setCreateTime(new Date());
                //退货状态 改为：11-退货完成
                returnOrderInfo.setReturnOrderStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_RETURN.getKey());
                returnOrderInfo.setActualProductCount(totalCount);
                returnOrderInfo.setProductCount(totalCount);
                returnOrderInfo.setActualReturnOrderAmount(totalAmount);
                returnOrderInfo.setReturnOrderAmount(totalAmount);
                //退款方式 5:退到加盟商账户
                returnOrderInfo.setReturnMoneyType(ConstantData.RETURN_MONEY_TYPE);
                //退货类型 3冲减单
                returnOrderInfo.setReturnOrderType(ReturnOrderTypeEnum.WRITE_DOWN_ORDER_TYPE.getCode());
                //处理办法 4--仅退款
                returnOrderInfo.setTreatmentMethod(TreatmentMethodEnum.RETURN_AMOUNT_TYPE.getCode());
                //生成退货单
                returnOrderInfo.setId(null);
                returnOrderInfo.setOrderType(Integer.valueOf(erpOrderInfo.getOrderTypeCode()));
                log.info("发起冲减单,生成退货单,returnOrderInfo={}",returnOrderInfo);
                returnOrderInfoDao.insertSelective(returnOrderInfo);
                List<ReturnOrderDetail> details = detailsList.stream().map(detailVo -> {
                    ReturnOrderDetail detail = new ReturnOrderDetail();
                    BeanUtils.copyProperties(detailVo, detail);
                    detail.setCreateTime(new Date());
                    detail.setReturnOrderDetailId(IdUtil.uuid());
                    detail.setReturnOrderCode(returnOrderCode);
                    detail.setCreateById(ConstantData.SYS_OPERTOR);
                    detail.setCreateByName(ConstantData.SYS_OPERTOR);
                    return detail;
                }).collect(Collectors.toList());
                //生成退货单详情
                log.info("发起冲减单,生成退货单详情,details={}",details);
                returnOrderDetailDao.insertWriteDownOrderBatch(details);
                //添加日志
                insertLog(returnOrderCode,ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR,ErpLogOperationTypeEnum.ADD.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(), WriteDownOrderStatusEnum.CREATE_ORDER_STATUS.getCode(),WriteDownOrderStatusEnum.CREATE_ORDER_STATUS.getName());
                //发起退款
                refund(returnOrderCode);
                return HttpResponse.success();
            }

        }
        return HttpResponse.failure(ResultCode.NOT_FOUND_ORDER_DATA);
    }

    @Override
    public PageResData<ReturnOrderInfo> getWriteDownOrderList(PageRequestVO<WriteDownOrderSearchVo> searchVo) {
        log.info("erp售后管理--冲减单列表入参searchVo={}",searchVo);
        PageResData pageResData=new PageResData();
        if(StringUtils.isBlank(searchVo.getSearchVO().getPersonId())||StringUtils.isBlank(searchVo.getSearchVO().getResourceCode())){
            return pageResData;
        }
        log.info("调用合伙人数据权限控制公共接口入参,personId={},resourceCode={}",searchVo.getSearchVO().getPersonId(),searchVo.getSearchVO().getResourceCode());
        HttpResponse httpResponse = copartnerAreaService.selectStoreByPerson(searchVo.getSearchVO().getPersonId(), searchVo.getSearchVO().getResourceCode());
        List<PublicAreaStore> dataList = JSONArray.parseArray(JSON.toJSONString(httpResponse.getData()), PublicAreaStore.class);
        log.info("调用合伙人数据权限控制公共接口返回结果,dataList={}",dataList);
        if (dataList == null || dataList.size() == 0) {
            return pageResData;
        }
        //遍历门店id
        List<String> storesIds = dataList.stream().map(PublicAreaStore::getStoreId).collect(Collectors.toList());
        log.info("门店ids={}",storesIds);
        if (storesIds == null || storesIds.size() == 0) {
            return pageResData;
        }
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        ReturnOrderQueryVo afterReturnOrderSearchVo=new ReturnOrderQueryVo();
        BeanUtils.copyProperties(searchVo.getSearchVO(),afterReturnOrderSearchVo);
        //退货类型 3冲减单
        afterReturnOrderSearchVo.setReturnOrderType(ReturnOrderTypeEnum.WRITE_DOWN_ORDER_TYPE.getCode());
        afterReturnOrderSearchVo.setStoreIds(storesIds);
        log.info("erp售后管理--冲减单列表，searchVo={}",searchVo);
        List<ReturnOrderInfo> content = returnOrderInfoDao.selectAll(afterReturnOrderSearchVo);
        pageResData.setTotalCount(Integer.valueOf((int)((Page) content).getTotal()));
        pageResData.setDataList(content);
        return pageResData;
    }

    //@Override
    @Transactional
    public boolean searchPayOrder(String orderCode) {
        String url=paymentHost+"/payment/pay/searchPayOrder?orderNo="+orderCode;
        log.info("根据退货单号查询支付状态入参，url={}",url);
        HttpClient httpClient = HttpClient.get(url);
        Map<String ,Object> result=null;
        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("根据退货单号查询支付状态结果，request={}",result);
        if(result!=null&&"0".equals(result.get("code"))){
            log.info("根据退货单号查询支付状态结果---退款成功");
            //更新退货单表状态
            returnOrderInfoDao.updateRefundStatus(orderCode);
            //更新流水表状态
            Map map=(Map)result.get("data");
            RefundInfo refundInfo=new RefundInfo();
            //1-已退款
            refundInfo.setStatus(ConstantData.REFUND_STATUS);
            refundInfo.setUpdateTime(new Date());
            refundInfo.setPayNum(map.get("payNum").toString());
            refundInfo.setOrderCode(orderCode);
            refundInfoDao.updateByOrderCode(refundInfo);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public HttpResponse saveCancelOrder(String orderCode) {
        log.info("客户取消订单---订单使用接口--入参orderCode={}",orderCode);
        //根据订单编码查询原始订单数据及详情数据
        ErpOrderInfo erpOrderInfo=erpOrderQueryService.getOrderAndItemByOrderCode(orderCode);
        log.info("客户取消订单---订单使用接口--根据订单编码查询原始订单数据及详情数据erpOrderInfo={}",erpOrderInfo);
        if(null==erpOrderInfo){
            //此单号有误，未查到订单数据
            return HttpResponse.failure(ResultCode.NOT_FOUND_ORDER_DATA);
        }
        List<ErpOrderItem> itemList=erpOrderInfo.getItemList();
        ReturnOrderInfo returnOrderInfo=new ReturnOrderInfo();
        BeanUtils.copyProperties(erpOrderInfo,returnOrderInfo);
        //插入数据库
        String returnOrderId = IdUtil.uuid();
        String returnOrderCode = sequenceService.generateOrderAfterSaleCode(erpOrderInfo.getCompanyCode(), ReturnOrderTypeEnum.WRITE_DOWN_ORDER_TYPE.getCode());
        returnOrderInfo.setReturnOrderId(returnOrderId);
        returnOrderInfo.setReturnOrderCode(returnOrderCode);
        returnOrderInfo.setCreateTime(new Date());
        //退货状态 改为：11-退货完成
        returnOrderInfo.setReturnOrderStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_RETURN.getKey());
        //退款方式 5:退到加盟商账户
        returnOrderInfo.setReturnMoneyType(ConstantData.RETURN_MONEY_TYPE);
        //退货类型 4客户取消
        returnOrderInfo.setReturnOrderType(ReturnOrderTypeEnum.CANCEL_ORDER.getCode());
        //处理办法 4--仅退款
        returnOrderInfo.setTreatmentMethod(TreatmentMethodEnum.RETURN_AMOUNT_TYPE.getCode());
        //生成退货单
        returnOrderInfo.setId(null);
        log.info("客户取消订单---订单使用接口--插入退货单主表入参returnOrderInfo={}",returnOrderInfo);
        returnOrderInfoDao.insertSelective(returnOrderInfo);
        log.info("客户取消订单---订单使用接口--插入退货单主表成功");
        List<ReturnOrderDetail> details = itemList.stream().map(detailVo -> {
            ReturnOrderDetail detail = new ReturnOrderDetail();
            BeanUtils.copyProperties(detailVo, detail);
            detail.setCreateTime(new Date());
            detail.setReturnOrderDetailId(IdUtil.uuid());
            detail.setReturnOrderCode(returnOrderCode);
            detail.setCreateById(ConstantData.SYS_OPERTOR);
            detail.setCreateByName(ConstantData.SYS_OPERTOR);
            detail.setProductCategoryCode(detailVo.getProductCategoryCode());
            detail.setProductCategoryName(detailVo.getProductCategoryName());
            return detail;
        }).collect(Collectors.toList());
        //生成退货单详情
        log.info("客户取消订单---订单使用接口--插入退货单详情表入参details={}",details);
        returnOrderDetailDao.insertWriteDownOrderBatch(details);
        log.info("客户取消订单---订单使用接口--插入退货单详情表成功");
        //添加日志
        insertLog(returnOrderCode,ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR,ErpLogOperationTypeEnum.ADD.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(), WriteDownOrderStatusEnum.CANCEL_ORDER.getCode(),WriteDownOrderStatusEnum.CANCEL_ORDER.getName());
        return HttpResponse.success(returnOrderCode);
    }

    @Override
    public HttpResponse apply(String approvalCode, String operatorId, String deptCode) {
        submitActBaseProcess(approvalCode,operatorId,deptCode);
        return HttpResponse.success();
    }

    @Override
    public HttpResponse directDelivery(ReturnOrderReviewReqVo reqVo) {
        log.info("直送退货处理--入参,reqVo={}",reqVo);
        String content="";
        int synFlag = 0;
        String isPass="";
        int erpFlag = 0;
        ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(reqVo.getReturnOrderCode());
        switch (reqVo.getOperateStatus()) {//处理办法 1--退款并退货 2--驳回
            case 1:
                reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_COM.getKey());
                //处理办法 1--退货退款(通过)
                reqVo.setTreatmentMethod(TreatmentMethodEnum.RETURN_AMOUNT_AND_GOODS_TYPE.getCode());
                content=ReturnOrderStatusEnum.RETURN_ORDER_STATUS_COM.getMsg();
                //同步数据到供应链
                synFlag = 1;
                break;
            case 2:
                reqVo.setOperateStatus(ReturnOrderStatusEnum.RETURN_ORDER_STATUS_FALL.getKey());
                reqVo.setTreatmentMethod(TreatmentMethodEnum.FALL_TYPE.getCode());
                content=ReturnOrderStatusEnum.RETURN_ORDER_STATUS_FALL.getMsg();
                //调用门店退货申请-完成(门店)（erp回调）---订货管理-修改退货申请单
                isPass=StoreStatusEnum.PAY_ORDER_TYPE_PEI.getKey().toString();
                erpFlag=1;
                break;
            default:
                return HttpResponse.failure(ResultCode.RETURN_ORDER_STATUS_NOT_FOUND);
        }
        reqVo.setReviewTime(new Date());
        //修改退货单状态
        returnOrderInfoDao.updateReturnStatus(reqVo);
        //添加日志
        insertLog(reqVo.getReturnOrderCode(),reqVo.getOperator(),reqVo.getOperator(),ErpLogOperationTypeEnum.UPDATE.getCode(),ErpLogSourceTypeEnum.RETURN.getCode(),reqVo.getOperateStatus(),content);
        if (synFlag==1) {
            // 同步到供应链，生成退供单
            log.info("erp同步供应链，生成退供单开始,returnOrderCode={}",reqVo.getReturnOrderCode());
            HttpResponse httpResponse=rejectRecordService.createRejectRecord(reqVo.getReturnOrderCode());
            log.info("erp同步供应链，生成退供单结束,httpResponse={}",httpResponse);
            if(!"0".equals(httpResponse.getCode())){
                throw new RuntimeException("erp同步供应链，生成退供单失败");
            }
        }
        if(StringUtils.isNotBlank(isPass)){
            updateStoreStatus(reqVo.getReturnOrderCode(),isPass,returnOrderInfo.getStoreId(),reqVo.getOperatorId(),reqVo.getOperator(),"0");
        }
        if(erpFlag==1){//修改原始订单数据
            log.info("退货单审核--修改原始订单数据开始,入参orderStoreCode={},orderReturnStatusEnum={},returnQuantityList={},personId={},personName={}",returnOrderInfo.getOrderStoreCode(), ErpOrderReturnStatusEnum.SUCCESS,null,reqVo.getOperatorId(),reqVo.getOperator());
            erpOrderInfoService.updateOrderReturnStatus(returnOrderInfo.getOrderStoreCode(), ErpOrderReturnRequestEnum.CANCEL,null,reqVo.getOperatorId(),reqVo.getOperator());
        }
        return HttpResponse.success();
    }

    @Override
    public HttpResponse getOrderDetail(String orderCode) {
        ErpOrderItem po=new ErpOrderItem();
        po.setOrderStoreCode(orderCode);
        po.setProductType(0);//商品类型  0商品 1赠品
        List<ErpOrderItem> select = erpOrderItemDao.select(po);
        return HttpResponse.success(select);
    }

    /**
     * 插入日志表
     * @param orderCode
     * @param persionId
     * @param persionName
     * @param operationType
     * @param sourceType
     * @param status
     * @param content
     */
    public void insertLog(String orderCode,String persionId,String persionName,Integer operationType,Integer sourceType,Integer status,String content){
        ErpOrderOperationLog operationLog=new ErpOrderOperationLog();
        operationLog.setOperationCode(orderCode);
        operationLog.setOperationType(operationType);
        operationLog.setSourceType(sourceType);
        operationLog.setOperationStatus(status);
        operationLog.setOperationContent(content);
        operationLog.setRemark("");
        operationLog.setCreateTime(new Date());
        operationLog.setCreateById(persionId);
        operationLog.setCreateByName(persionName);
        operationLog.setUpdateTime(new Date());
        operationLog.setUpdateById(persionId);
        operationLog.setUpdateByName(persionName);
        operationLog.setUseStatus(0);
        erpOrderOperationLogDao.insert(operationLog);
    }

    /**
     * 根据单号查询操作日志
     * @param orderCode
     * @return
     */
    public List<ErpOrderOperationLog> getOrderOperationLogList(String orderCode){
        List<ErpOrderOperationLog> list = new ArrayList<>();
        if (StringUtils.isNotEmpty(orderCode)) {
            ErpOrderOperationLog query = new ErpOrderOperationLog();
            query.setOperationCode(orderCode);
            query.setSourceType(ErpLogSourceTypeEnum.RETURN.getCode());
            List<ErpOrderOperationLog> select = erpOrderOperationLogDao.select(query);
            if (select != null && select.size() > 0) {
                for (ErpOrderOperationLog item :
                        select) {
                    if (!ErpLogOperationTypeEnum.DOWNLOAD.getCode().equals(item.getOperationType())) {
                        list.add(item);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 门店退货申请，完成门店（erp回调）--修改退货单申请
     * @param orderReturnCode
     * @param orderReturnStatus
     * @param storeId
     * @param updateById
     * @param updateByName
     */
    public void updateStoreStatus(String orderReturnCode,String orderReturnStatus,String storeId,String updateById,String updateByName,String actualAuantity){
        String url=productHost+"/order/return/update/status";
        StringBuilder sb=new StringBuilder();
        sb.append("?order_return_code="+orderReturnCode);
        sb.append("&order_return_status="+orderReturnStatus);
        sb.append("&store_id="+storeId);
        sb.append("&update_by_id="+updateById);
        sb.append("&update_by_name="+updateByName);
        if(StringUtils.isNotBlank(actualAuantity)){
            sb.append("&actual_quantity="+actualAuantity);
        }
        log.info("发起订货管理-修改退货申请单入参，url={},json={}",url+sb);
        HttpClient httpClient = HttpClient.get(url+sb);
        Map<String ,Object> result=null;
        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("发起订货管理-修改退货申请单结果，request={}",result);
        if(result!=null&&"0".equals(result.get("code").toString())){
            log.info("发起订货管理-修改退货申请单结果--成功");
        }
    }

    public static void main(String[] args) {
        String url="http://product.api.aiqin.com"+"/order/return/insert";
//        OrderReturnReq orr=new OrderReturnReq();
//        orr.setCreateById("reqVo");
//        orr.setCreateByName("reqVo");
//        orr.setCreateTime(new Date());
//        orr.setOrderCode("reqVo");
//        orr.setOrderReturnCode("reqVo");
//        orr.setOrderReturnId("reqVo");
//        orr.setStoreId("reqVo");
//        List<OrderReturnProductReq> orderReturnProductReqList=new ArrayList<>();
//        String orderReturnId="1111";
//        String orderReturnCode="225";
//        String skuCode="5588";
//        Long returnQuantity=2L;
//        OrderReturnProductReq orpr=new OrderReturnProductReq();
//        orpr.setOrderReturnId(orderReturnId);
//        orpr.setOrderReturnCode(orderReturnCode);
//        orpr.setSkuCode(skuCode);
//        orpr.setReturnQuantity(returnQuantity);
//        orpr.setCreateById("ID");
//        orpr.setCreateByName("HAN");
//        orpr.setCreateTime(new Date());
//        orderReturnProductReqList.add(orpr);
//        orr.setOrderReturnProductReqs(orderReturnProductReqList);
//        log.info("发起门店退货申请-完成(门店)（erp回调）--修改商品库存入参，url={},json={}",url,orr);
//        HttpClient httpClient = HttpClient.post(url).json(orr);
//        Map<String ,Object> result=null;
//        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
//        log.info("发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存结果，request={}",result);

        String str="{\n" +
                "  \"create_by_id\": \"12272\",\n" +
                "  \"create_by_name\": \"张苗苗（CTO助理）\",\n" +
                "  \"order_code\": \"20200107000017\",\n" +
                "  \"order_return_code\": \"20200108045100004\",\n" +
                "  \"order_return_product_reqs\": [\n" +
                "    {\n" +
                "      \"create_by_id\": \"12272\",\n" +
                "      \"create_by_name\": \"张苗苗（CTO助理）\",\n" +
                "      \"create_time\": \"2020-01-08T05:38:42.801Z\",\n" +
                "      \"return_quantity\": 10,\n" +
                "      \"sku_code\": \"0000109\"\n" +
                "    }\n" +
                "  ],\n" +
//                "  \"store_id\": \"AB957AE69917B34B35BEFFF8A23573F01E\"\n" +
                "  \"store_id\": \"ABEC8D65036E5A45DBABCBA413FA56AEA2\"\n" +
                "}";
//        JSONObject json= JSON.parseObject(str);
//        log.info("发起门店退货申请-完成(门店)（erp回调）--修改商品库存入参，url={},json={}",url,json);
//        HttpClient httpClient = HttpClient.post(url).json(json);
//        Map<String ,Object> result=null;
//        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
//        log.info("发起发起门店退货申请-完成(门店)（erp回调）--修改商品库存结果，request={}",result);
    }
}
