package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ConstantData;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.enums.ErpLogOperationTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpLogSourceTypeEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderReturnRequestEnum;
import com.aiqin.mgs.order.api.component.enums.ErpOrderReturnStatusEnum;
import com.aiqin.mgs.order.api.component.returnenums.ReturnOrderStatusEnum;
import com.aiqin.mgs.order.api.component.returnenums.StoreStatusEnum;
import com.aiqin.mgs.order.api.config.properties.UrlProperties;
import com.aiqin.mgs.order.api.dao.CouponApprovalDetailDao;
import com.aiqin.mgs.order.api.dao.CouponApprovalInfoDao;
import com.aiqin.mgs.order.api.dao.CouponInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderOperationLogDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderOperationLog;
import com.aiqin.mgs.order.api.domain.request.returnorder.FranchiseeAssetVo;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderReviewReqVo;
import com.aiqin.mgs.order.api.service.CouponApprovalInfoService;
import com.aiqin.mgs.order.api.service.order.ErpOrderInfoService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.platform.flows.client.constant.Indicator;
import com.aiqin.platform.flows.client.constant.IndicatorStr;
import com.aiqin.platform.flows.client.constant.StatusEnum;
import com.aiqin.platform.flows.client.constant.TpmBpmUtils;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;
import com.aiqin.platform.flows.client.service.FormDetailService;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @Value("${bridge.url.product-api}")
    private String productHost;

    @Autowired
    private CouponApprovalInfoDao couponApprovalInfoDao;
    @Autowired
    private CouponApprovalDetailDao couponApprovalDetailDao;
    @Autowired
    private CouponInfoDao couponInfoDao;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Autowired
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Autowired
    private ErpOrderOperationLogDao erpOrderOperationLogDao;
    @Resource
    private FormDetailService  formDetailService;


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
        return couponApprovalDetailDao.selectByFormNo(formNo);
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
                Double totalMoney=couponApprovalDetail.getTotalMoney().doubleValue();
                //计算A品券数量，同步到虚拟资产
                List<FranchiseeAssetVo> franchiseeAssets=new ArrayList<>();
                if(couponApprovalDetail!=null&&totalMoney!=null){
                    //计算面值为100的A品券数量
                    int num=(int)(totalMoney/100);
                    //计算剩余钱数
                    double balance=totalMoney%100;
                    //存储A品卷信息
                    List<CouponInfo> couponInfoList=new ArrayList<>();
                    for(int i=0;i<num;i++){
                        CouponInfo couponInfo=new CouponInfo();
                        couponInfo.setCouponName(ConstantData.COUPON_NAME_A);
                        couponInfo.setCouponType(ConstantData.COUPON_TYPE);
                        couponInfo.setFranchiseeId(couponApprovalDetail.getFranchiseeId());
                        couponInfo.setOrderId(couponApprovalDetail.getOrderId());
                        couponInfo.setValidityStartTime(couponApprovalDetail.getStartTime());
                        couponInfo.setValidityEndTime(couponApprovalDetail.getEndTime());
                        couponInfo.setCouponCode(couponCode());
                        couponInfo.setNominalValue(ConstantData.NOMINAL_VALUE);
                        couponInfoList.add(couponInfo);
                        FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
                        BeanUtils.copyProperties(couponInfo,franchiseeAsset);
                        franchiseeAssets.add(franchiseeAsset);
                    }
                    if(balance>0){
                        CouponInfo couponInfo=new CouponInfo();
                        couponInfo.setCouponName(ConstantData.COUPON_NAME_A);
                        couponInfo.setCouponType(ConstantData.COUPON_TYPE);
                        couponInfo.setFranchiseeId(couponApprovalDetail.getFranchiseeId());
                        couponInfo.setOrderId(couponApprovalDetail.getOrderId());
                        couponInfo.setValidityStartTime(couponApprovalDetail.getStartTime());
                        couponInfo.setValidityEndTime(couponApprovalDetail.getEndTime());
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

                ReturnOrderInfo returnOrderInfo = returnOrderInfoDao.selectByReturnOrderCode(couponApprovalDetail.getOrderId());
                // 调用门店退货申请-完成(门店)（erp回调）---订货管理-修改退货申请单
                updateStoreStatus(couponApprovalDetail.getOrderId(),StoreStatusEnum.PAY_ORDER_TYPE_PEI.getKey().toString(),couponApprovalDetail.getStoreId(),ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR,returnOrderInfo.getProductCount().toString());
                //修改原始订单数据
                List<ReturnOrderDetail> details = returnOrderDetailDao.selectListByReturnOrderCode(couponApprovalDetail.getOrderId());
                List<ErpOrderItem> returnQuantityList=new ArrayList<>();
                for(ReturnOrderDetail rod:details){
                    ErpOrderItem eoi=new ErpOrderItem();
                    eoi.setLineCode(rod.getLineCode());
                    eoi.setReturnProductCount(rod.getReturnProductCount());
                    returnQuantityList.add(eoi);
                }
                log.info("A品券发放审批回调--修改原始订单数据开始,入参orderStoreCode={},orderReturnStatusEnum={},returnQuantityList={},personId={},personName={}",returnOrderInfo.getOrderStoreCode(), ErpOrderReturnStatusEnum.SUCCESS,returnQuantityList,ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR);
                erpOrderInfoService.updateOrderReturnStatus(returnOrderInfo.getOrderStoreCode(), ErpOrderReturnRequestEnum.SUCCESS,returnQuantityList,ConstantData.SYS_OPERTOR,ConstantData.SYS_OPERTOR);
                //产生的A品券不为空，同步到虚拟资产
                if(CollectionUtils.isNotEmpty(franchiseeAssets)){
                    log.info("A品券同步到虚拟资产开始，franchiseeAssets={}",franchiseeAssets);
                    franchiseeAssets.forEach(p -> p.setFranchiseeId(couponApprovalDetail.getFranchiseeId()));
                    franchiseeAssets.forEach(p -> p.setCreateTime(new Date()));
                    String url=slcsHost+"/franchiseeVirtual/VirtualA";
                    JSONObject json=new JSONObject();
                    json.put("list",franchiseeAssets);
                    HttpClient httpClient = HttpClient.post(url).json(json);
                    Map<String ,Object> res=null;
                    res = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
                    log.info("同步到虚拟资产:"+res);
                    if(res!=null&&"0".equals(res.get("code"))){
                        log.info("A品券虚拟资产同步成功，修改退货单状态");
                        ReturnOrderReviewReqVo reqVo=new ReturnOrderReviewReqVo();
                        reqVo.setReturnOrderCode(couponApprovalDetail.getOrderId());
                        reqVo.setOperateStatus(ConstantData.RETURN_ORDER_SUCCESS);
                        returnOrderInfoDao.updateReturnStatus(reqVo);
                        couponInfoDao.updateByOrderId(couponApprovalDetail.getOrderId());
                        log.info("退款完成");
                    }
                }
                log.info("A品券发放审批回调结束");
            } else if (TpmBpmUtils.isPass(formCallBackVo.getUpdateFormStatus(), formCallBackVo.getOptBtn())) {
                couponApprovalInfo.setStatus(StatusEnum.AUDIT.getValue());
                couponApprovalInfo.setStatuStr(StatusEnum.AUDIT.getDesc());
            } else {
                if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_FIRST.getCode())) {
                    //驳回发起人
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_BACK.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_BACK.getDesc());
                    updateReturnOrderStatus(ConstantData.RETURN_ORDER_STATUS_WAIT,couponApprovalDetail.getOrderId());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_END.getCode())) {
                    //驳回并结束
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                    updateReturnOrderStatus(ConstantData.RETURN_ORDER_STATUS_WAIT,couponApprovalDetail.getOrderId());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_CANCEL.getCode())) {
                    //撤销
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_CANCEL.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_CANCEL.getDesc());
                    updateReturnOrderStatus(ConstantData.RETURN_ORDER_STATUS_WAIT,couponApprovalDetail.getOrderId());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_KILL.getCode())) {
                    //终止
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                    updateReturnOrderStatus(ConstantData.RETURN_ORDER_STATUS_WAIT,couponApprovalDetail.getOrderId());
                } else {
                    //终止
                    couponApprovalInfo.setStatus(StatusEnum.AUDIT_END.getValue());
                    couponApprovalInfo.setStatuStr(StatusEnum.AUDIT_END.getDesc());
                    updateReturnOrderStatus(ConstantData.RETURN_ORDER_STATUS_WAIT,couponApprovalDetail.getOrderId());
                }
            }

            //操作人
            String opertor="";
            String opertorName = "";
            HttpResponse httpResponse=formDetailService.goTaskOperateFormScmp(formCallBackVo.getFormNo(), null);
            Map<String,Object> map=(Map<String, Object>) httpResponse.getData();
            log.info("门店新增增品比例返还审批回调查询任务节点，返回结果,httpResponse={}", map);
            map=(Map<String, Object>) map.get("process");
            if(null!=map.get("applierCode")){
                opertor=map.get("applierCode").toString();
                opertorName=map.get("applierName").toString();
            }
            //订单日志
            insertLog(couponApprovalDetail.getOrderId(),opertor,opertorName, ErpLogOperationTypeEnum.UPDATE.getCode(), ErpLogSourceTypeEnum.RETURN.getCode(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_COM.getKey(),ReturnOrderStatusEnum.RETURN_ORDER_STATUS_COM.getMsg());
            //更新本地审批表数据
            couponApprovalInfoDao.updateByFormNoSelective(couponApprovalInfo);
        } else {
            //result = "false";
        }
        return result;
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
        log.info("插入日志表-方法入参： " + operationLog);
        erpOrderOperationLogDao.insert(operationLog);
    }

    /**
     * 生成A品卷
     * @param
     * @return
     */
    public HttpResponse  AGoodsVolumeGenerate(BigDecimal topCouponDiscountAmount,String franchiseeid,String returnCode){
        log.info("生成A品卷入参：{},{},{}",topCouponDiscountAmount,franchiseeid,returnCode);
        List<FranchiseeAssetVo> franchiseeAssets=new ArrayList<>();
        Double totalMoney = topCouponDiscountAmount.doubleValue();
        if(totalMoney!=null){
            //计算面值为100的A品券数量
            int num=(int)(totalMoney/100);
            //计算剩余钱数
            double balance=totalMoney%100;
            //存储A品卷信息
            List<CouponInfo> couponInfoList=new ArrayList<>();
            for(int i=0;i<num;i++){
                CouponInfo couponInfo=new CouponInfo();
                couponInfo.setCouponName(ConstantData.COUPON_NAME_A);
                couponInfo.setCouponType(ConstantData.COUPON_TYPE);
                couponInfo.setFranchiseeId(franchiseeid);
                couponInfo.setOrderId(returnCode);
                //开始时间
                Date date = new Date();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String format = simpleDateFormat.format(date);
                couponInfo.setValidityStartTime(date);
                couponInfo.setValidityEndTime(obtainDate());
                couponInfo.setCouponCode(couponCode());
                couponInfo.setNominalValue(ConstantData.NOMINAL_VALUE);
                couponInfoList.add(couponInfo);
                FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
                BeanUtils.copyProperties(couponInfo,franchiseeAsset);
                franchiseeAssets.add(franchiseeAsset);
            }
            if(balance>0){
                CouponInfo couponInfo=new CouponInfo();
                couponInfo.setCouponName(ConstantData.COUPON_NAME_A);
                couponInfo.setCouponType(ConstantData.COUPON_TYPE);
                couponInfo.setFranchiseeId(franchiseeid);
                couponInfo.setOrderId(returnCode);
                //开始时间
                Date date = new Date();
                couponInfo.setValidityStartTime(date);
                couponInfo.setValidityEndTime(obtainDate());
                couponInfo.setCouponCode(couponCode());
                couponInfo.setNominalValue(BigDecimal.valueOf(balance));
                couponInfoList.add(couponInfo);
                FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
                BeanUtils.copyProperties(couponInfo,franchiseeAsset);
                franchiseeAssets.add(franchiseeAsset);
            }
            if(CollectionUtils.isNotEmpty(franchiseeAssets)){
                log.info("A品券同步到虚拟资产开始，franchiseeAssets={}",franchiseeAssets);
                franchiseeAssets.forEach(p -> p.setFranchiseeId(franchiseeid));
                franchiseeAssets.forEach(p -> p.setCreateTime(new Date()));
                String url=slcsHost+"/franchiseeVirtual/VirtualA";
                JSONObject json=new JSONObject();
                json.put("list",franchiseeAssets);
                HttpClient httpClient = HttpClient.post(url).json(json);
                Map<String ,Object> res=null;
                res = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
                log.info("同步到虚拟资产:"+res);
                if(res!=null&&"0".equals(res.get("code"))){
                    log.info("A品卷完成");
                    return HttpResponse.success();
                }
                return HttpResponse.failure(ResultCode.FRANCHISEE_VIRTUAL_ERROP);
            }
        }
        return HttpResponse.failure(ResultCode.A_GOODS_ERROP);
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
     * 生成当前月份时间+3
     */
    public static Date obtainDate(){
        Calendar instance = Calendar.getInstance();
        Date date = new Date();
        instance.setTime(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        instance.add(instance.MONTH,3);
        Date time1 = instance.getTime();
        String format = simpleDateFormat.format(time1);
        Date parse = null;
        try {
             parse = simpleDateFormat.parse(format);
            return parse;
        } catch (ParseException e) {
            log.info("context",e);
            return null;
        }

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
        reqVo.setReturnOrderCode(returnOrderId);
        Integer res=returnOrderInfoDao.updateReturnStatus(reqVo);
        return res>0;
    }


    public static void main(String[] args) {
//        System.out.println(IdUtil.activityId());
//        for (int i=0;i<1000;i++){
//            System.out.println(couponCode());
//        }

        String url="http://slcs.api.aiqin.com/franchiseeVirtual/VirtualA";
//        String url="http://192.168.200.127:9011/franchiseeVirtual/VirtualA";
//        String url="http://127.0.0.1:9011/franchiseeVirtual/VirtualA";
        List<FranchiseeAssetVo> franchiseeAssets=new ArrayList<>();
        CouponInfo couponInfo=new CouponInfo();
        couponInfo.setCouponName(ConstantData.COUPON_NAME_A);
        couponInfo.setCouponType(ConstantData.COUPON_TYPE);
        couponInfo.setFranchiseeId("1001");
        couponInfo.setOrderId("1002");
        couponInfo.setValidityStartTime(new Date());
        couponInfo.setValidityEndTime(new Date());
        couponInfo.setCouponCode(couponCode());
        couponInfo.setNominalValue(ConstantData.NOMINAL_VALUE);
        couponInfo.setCreateTime(new Date());
        FranchiseeAssetVo franchiseeAsset=new FranchiseeAssetVo();
        BeanUtils.copyProperties(couponInfo,franchiseeAsset);
        franchiseeAssets.add(franchiseeAsset);
        JSONObject json=new JSONObject();
        json.put("list",franchiseeAssets);
        HttpClient httpClient = HttpClient.post(url).json(json);
        Map<String ,Object> result=null;
        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("同步到虚拟资产结果，request={}",result);
        if(result!=null&&"0".equals(result.get("code"))){
            log.info("同步到虚拟资产完成");
        }

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
     * 门店退货申请，完成门店（erp回调）--修改退货单申请
     * @param orderReturnCode
     * @param orderReturnStatus
     * @param storeId
     * @param updateById
     * @param updateByName
     */
    public void updateStoreStatus(String orderReturnCode,String orderReturnStatus,String storeId,String updateById,String updateByName,String actualAuantity){
        //修改商品库存
        String url=productHost+"/order/return/update/status";
        StringBuilder sb=new StringBuilder();
        sb.append("?order_return_code="+orderReturnCode);
        sb.append("&order_return_status="+orderReturnStatus);
        sb.append("&store_id="+storeId);
        sb.append("&update_by_id="+updateById);
        sb.append("&update_by_name="+updateByName);
        sb.append("&actual_quantity="+actualAuantity);
        log.info("发起订货管理-修改退货申请单入参，url={},json={}",url+sb);
        HttpClient httpClient = HttpClient.get(url+sb);
        Map<String ,Object> result=null;
        result = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("发起订货管理-修改退货申请单结果，request={}",result);
        if(result!=null&&"0".equals(result.get("code").toString())){
            log.info("发起订货管理-修改退货申请单结果--成功");
        }else {
            throw new RuntimeException("发起订货管理-修改退货申请单结果--失败");
        }
    }

}
