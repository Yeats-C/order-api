package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ConstantData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.StatusEnum;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayWayEnum;
import com.aiqin.mgs.order.api.dao.CopartnerAreaDao;
import com.aiqin.mgs.order.api.dao.OrderGiveApprovalDao;
import com.aiqin.mgs.order.api.dao.OrderGiveFeeDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.OrderConstant;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaList;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaVo;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewLogisticsInfo;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewLogisticsRequest;
import com.aiqin.mgs.order.api.domain.logisticsRule.NewReduceInfo;
import com.aiqin.mgs.order.api.domain.po.cart.ErpOrderCartInfo;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.CouponShareRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpCartQueryRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ErpQueryCartGroupTempRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderPayRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderProductItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderSaveRequest;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.response.LogisticsAllResponse;
import com.aiqin.mgs.order.api.domain.response.StoreMarketValueResponse;
import com.aiqin.mgs.order.api.domain.response.cart.*;
import com.aiqin.mgs.order.api.domain.wholesale.WholesaleCustomers;
import com.aiqin.mgs.order.api.service.CartOrderService;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import com.aiqin.mgs.order.api.service.LogisticsRuleService;
import com.aiqin.mgs.order.api.service.SequenceGeneratorService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.cart.ErpOrderCartService;
import com.aiqin.mgs.order.api.service.gift.GiftPoolService;
import com.aiqin.mgs.order.api.service.gift.GiftQuotasUseDetailService;
import com.aiqin.mgs.order.api.service.impl.LogisticsRuleServiceImpl;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.service.wholesale.WholesaleCustomersService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.aiqin.mgs.order.api.util.RequestReturnUtil;
import com.aiqin.platform.flows.client.constant.*;
import com.aiqin.platform.flows.client.domain.vo.ActBaseProcessEntity;
import com.aiqin.platform.flows.client.domain.vo.FormCallBackVo;
import com.aiqin.platform.flows.client.domain.vo.StartProcessParamVO;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * 创建订单service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 13:57
 */
@Slf4j
@Service
public class ErpOrderCreateServiceImpl implements ErpOrderCreateService {

    @Value("${activiti.host}")
    private String activitiHost;
    @Value("${bridge.url.slcs_api}")
    private String slcsHost;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderRequestService erpOrderRequestService;
    @Resource
    private CartOrderService cartOrderService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Resource
    private SequenceGeneratorService sequenceGeneratorService;
    @Resource
    private ErpOrderCartService erpOrderCartService;
    @Resource
    private WholesaleCustomersService wholesaleCustomersService;
    @Resource
    private CouponRuleService couponRuleService;
    @Resource
    private OrderGiveFeeDao orderGiveFeeDao;
    @Resource
    private OrderGiveApprovalDao orderGiveApprovalDao;
    @Resource
    private ErpOrderPayNoTransactionalService erpOrderPayNoTransactionalService;
    @Resource
    private BridgeProductService bridgeProductService;
    @Resource
    private GiftQuotasUseDetailService giftQuotasUseDetailService;
    @Resource
    private GiftPoolService giftPoolService;
    @Resource
    private LogisticsRuleService logisticsRuleService;
    @Resource
    private CopartnerAreaDao copartnerAreaDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo erpSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, true);

        ErpOrderTypeCategoryControlEnum controlEnum = ErpOrderTypeCategoryControlEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (controlEnum == null || !controlEnum.isErpCartCreate()) {
            throw new BusinessException("不支持创建该类型和类别的订单");
        }
        return saveOrder(erpOrderSaveRequest,ErpOrderSourceEnum.ERP, auth);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo storeSaveOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {
        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, false);

        ErpOrderTypeCategoryControlEnum controlEnum = ErpOrderTypeCategoryControlEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (controlEnum == null || !controlEnum.isStoreCartCreate()) {
            throw new BusinessException("不支持创建该类型和类别的订单");
        }
        return saveOrder(erpOrderSaveRequest,ErpOrderSourceEnum.STORE, auth);
    }

    /**
     * 从购物车创建订单
     *
     * @param erpOrderSaveRequest
     * @return com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/26 9:49
     */
    private ErpOrderInfo saveOrder(ErpOrderSaveRequest erpOrderSaveRequest, ErpOrderSourceEnum orderSourceEnum, AuthToken auth) {

        //获取订单节点进程控制枚举
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (processTypeEnum == null) {
            throw new BusinessException("不允许创建类型为" + ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()) + "，类别为" + ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()) + "的订单");
        }
        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        log.info("创建订单,获取门店信息返回结果storeInfo={}",storeInfo);
        //获取购物车商品
//        List<CartOrderInfo> cartProductList = getStoreCartProductList(erpOrderSaveRequest.getCartGroupTempKey(),erpOrderSaveRequest.getStoreId(), erpOrderSaveRequest.getOrderType(),orderSourceEnum);
        List<ErpOrderCartInfo> cartProductList = getStoreCartProductList(erpOrderSaveRequest.getCartGroupTempKey(),erpOrderSaveRequest.getStoreId(), erpOrderSaveRequest.getOrderType(),orderSourceEnum);

        //构建订单商品明细行
//        List<ErpOrderItem> erpOrderItemList = generateOrderItemList(cartProductList, storeInfo, processTypeEnum);
        List<ErpOrderItem> erpOrderItemList = generateOrderItemList(cartProductList, storeInfo, processTypeEnum);

        //数据校验
        productCheck(processTypeEnum, storeInfo, erpOrderItemList);


        ErpOrderFee orderFee = new ErpOrderFee();
        //生成订单code
        String orderCode = sequenceGeneratorService.generateOrderCode();
        //判断是否需要审批--赠送市值余额不够。进行审批 true
        Boolean flag1=false;
        //判断是首单赠送市值订单。进行审批 true
        Boolean flag2=false;
        //首单赠送--加入了市值赠送金额判断
        if(ErpOrderTypeEnum.DIRECT_SEND.getCode().equals(erpOrderSaveRequest.getOrderType())&&ErpOrderCategoryEnum.ORDER_TYPE_4.getCode().equals(erpOrderSaveRequest.getOrderCategory())){
            Map<ErpOrderFee,Boolean> res=firstGivePrice(erpOrderSaveRequest.getApplier(),erpOrderSaveRequest.getDeptCode(),orderCode,storeInfo,erpOrderItemList, erpOrderSaveRequest.getTopCouponCodeList());
            for(Map.Entry<ErpOrderFee,Boolean> r:res.entrySet()){
                orderFee = r.getKey();
                flag1=r.getValue();
            }
            flag2=true;
        }else {
            //计算A品券分摊金额
            orderFee = shareTopCouponPrice(erpOrderItemList, erpOrderSaveRequest.getTopCouponCodeList());
        }
        //生成订单主体信息
//        ErpOrderInfo order = generateOrder(erpOrderItemList, storeInfo, erpOrderSaveRequest, orderFee,auth);
        ErpOrderInfo order = generateOrder(flag1,flag2,erpOrderItemList, storeInfo, erpOrderSaveRequest, orderFee,auth);
        /*//配送 计算订单物流费用减免比例
        if(ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(erpOrderSaveRequest.getOrderType())){
            calculateLogisticsCostReductionRatio(order,erpOrderItemList);
        }*/

        log.info("创建订单,生成订单主体信息返回结果order={}",order);
        //保存订单、订单明细、订单支付、订单收货人信息、订单日志
        order.setOrderStoreCode(orderCode);
        String orderId = insertOrder(order,orderFee, storeInfo, auth, erpOrderSaveRequest);
        //删除购物车商品
//        deleteOrderProductFromCart(cartProductList);
        deleteOrderProductFromCart(cartProductList);

        if(null!=erpOrderSaveRequest.getCartGroupTempKey()){
            //删除兑换赠品购物车商品
            deleteGiftProductFromCart(erpOrderSaveRequest.getCartGroupTempKey());
        }


        /*//扣除订单积分兑换赠品额度并插入赠品额度明细使用记录
        integralGift(cartProductList,storeInfo,order.getOrderStoreCode(),auth);*/

        //锁库存 TODO 此处得改为调用DL接口锁库并同步 待DL提供接口
        if (processTypeEnum.isLockStock()) {
            //TODO 修改成实时获取锁库结果并新建表保存
            boolean flag = erpOrderRequestService.lockStockInSupplyChain(order, erpOrderItemList, auth);
            if (!flag) {
                throw new BusinessException("锁库存失败");
            }
            //将结果插入到新表中
            //TODO 根据供应链的返回结果定
            StoreLockDetails storeLockDetails=new StoreLockDetails();
            List<StoreLockDetails> list=new ArrayList<>();
            list.add(storeLockDetails);
//            erpStoreLockDetailsService.insertStoreLockDetails(list);
        }

        //返回订单信息
        return erpOrderQueryService.getOrderByOrderId(orderId);
    }

    private void calculateLogisticsCostReductionRatio(ErpOrderInfo order, List<ErpOrderItem> erpOrderItemList) {
        List<NewLogisticsRequest> list=(List<NewLogisticsRequest>)logisticsRuleService.selectLogisticsAll().getData();
        if(null!=list){
            BigDecimal logisticsCostReductionRatio=BigDecimal.ZERO;
            for(NewLogisticsRequest newLogisticsRequest:list){
                NewLogisticsInfo newLogisticsInfo=newLogisticsRequest.getNewLogisticsInfo();
                List<NewReduceInfo> newReduceInfoList=newLogisticsRequest.getNewReduceInfoList();
                BigDecimal totalAmount=BigDecimal.ZERO;
                switch (newLogisticsInfo.getLogisticsOrderType()){
                    case 1:
                        //1按品类
                        Map<String,String> categoryMap=new HashMap();
                        for(NewReduceInfo newReduceInfo: newReduceInfoList){
                            categoryMap.put(newReduceInfo.getCategory(),newReduceInfo.getCategory());
                        }
                        if(categoryMap.containsKey("All")){
                            for(ErpOrderItem orderItem:erpOrderItemList){
                                totalAmount=totalAmount.add(orderItem.getProductAmount().multiply(BigDecimal.valueOf(orderItem.getProductCount())));
                            }
                        }else{
                            for(ErpOrderItem orderItem:erpOrderItemList){
                                if(categoryMap.containsKey(orderItem.getProductCategoryCode())){
                                    totalAmount=totalAmount.add(orderItem.getProductAmount().multiply(BigDecimal.valueOf(orderItem.getProductCount())));
                                }
                            }
                        }
                        ;
                        break;
                    case 2:
                        //2按品牌
                        Map<String,String> brandMap=new HashMap();
                        for(NewReduceInfo newReduceInfo: newReduceInfoList){
                            brandMap.put(newReduceInfo.getBrand(),newReduceInfo.getBrand());
                        }
                        if(brandMap.containsKey("All")){
                            for(ErpOrderItem orderItem:erpOrderItemList){
                                totalAmount=totalAmount.add(orderItem.getProductAmount().multiply(BigDecimal.valueOf(orderItem.getProductCount())));
                            }
                        }else{
                            for(ErpOrderItem orderItem:erpOrderItemList){
                                if(brandMap.containsKey(orderItem.getProductBrandCode())){
                                    totalAmount=totalAmount.add(orderItem.getProductAmount().multiply(BigDecimal.valueOf(orderItem.getProductCount())));
                                }
                            }
                        }
                        ;
                    case 3:
                        //3按商品属性
                        Map<String,String> productTypeMap=new HashMap();
                        for(NewReduceInfo newReduceInfo: newReduceInfoList){
                            productTypeMap.put(newReduceInfo.getProductType(),newReduceInfo.getProductType());
                        }

                        for(ErpOrderItem orderItem:erpOrderItemList){
                            if(productTypeMap.containsKey(orderItem.getProductPropertyName())){
                                totalAmount=totalAmount.add(orderItem.getProductAmount().multiply(BigDecimal.valueOf(orderItem.getProductCount())));
                            }
                        }
                        ;
                        break;
                    default:
                        ;

                }
                if(totalAmount.compareTo(newLogisticsInfo.getAmountRequired())>0){
                    if(newLogisticsInfo.getLogisticsProportion().compareTo(logisticsCostReductionRatio)>0){
                        logisticsCostReductionRatio=newLogisticsInfo.getLogisticsProportion();
                    }
                }
            }

            order.setLogisticsCostReductionRatio(logisticsCostReductionRatio.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 删除兑换赠品购物车商品
     * @param cartGroupTempKey
     */
    private void deleteGiftProductFromCart(String cartGroupTempKey) {
        ErpQueryCartGroupTempRequest erpQueryCartGroupTempRequest=new ErpQueryCartGroupTempRequest();
        erpQueryCartGroupTempRequest.setCartGroupTempKey(cartGroupTempKey);
        log.info("创建订单[删除兑换赠品购物车商品],调用查询购物车接口入参erpQueryCartGroupTempRequest={}",erpQueryCartGroupTempRequest);
        ErpStoreCartQueryResponse erpStoreCartQueryResponse = erpOrderCartService.queryCartGroupTemp(erpQueryCartGroupTempRequest, null);
        log.info("创建订单[删除兑换赠品购物车商品],调用查询购物车接口返回结果erpStoreCartQueryResponse={}",erpStoreCartQueryResponse);
        if(null!=erpStoreCartQueryResponse.getErpCartQueryResponse()
                && null!=erpStoreCartQueryResponse.getErpCartQueryResponse().getCartInfoList()
                && erpStoreCartQueryResponse.getErpCartQueryResponse().getCartInfoList().size()>0){
            List<ErpOrderCartInfo> cartInfoList=erpStoreCartQueryResponse.getErpCartQueryResponse().getCartInfoList();
           for(ErpOrderCartInfo erpOrderCartInfo:cartInfoList){
               giftPoolService.deleteCartLine(erpOrderCartInfo.getCartId());
           }
        }


    }

    private void integralGift(List<ErpOrderCartInfo> cartProductList, StoreInfo storeInfo, String orderStoreCode, AuthToken auth) {
        log.info("扣除兑换赠品积分，并添加积分使用记录开始，参数为{}"+cartProductList,storeInfo,orderStoreCode);
        BigDecimal giftAmount=BigDecimal.ZERO;
        for(ErpOrderCartInfo erp:cartProductList){
            if(ErpProductGiftEnum.JIFEN.getCode().equals(erp.getProductGift())){
                giftAmount=giftAmount.add(erp.getPrice().multiply(new BigDecimal(erp.getAmount().toString())));
            }
        }
        if(giftAmount.compareTo(BigDecimal.ZERO)>0){
            //查詢門店赠品额度
            BigDecimal availableGiftQuota=bridgeProductService.getStoreAvailableGiftGuota(storeInfo.getStoreId());
            //计算订单使用过后的赠品额度
            BigDecimal newAvailableGiftQuota=availableGiftQuota.subtract(giftAmount);
            //更新订单使用过后的赠品额度
            bridgeProductService.updateAvailableGiftQuota(storeInfo.getStoreId(),newAvailableGiftQuota);

            //新建一个赠品额度使用明细对象
            GiftQuotasUseDetail giftQuotasUseDetail=new GiftQuotasUseDetail();
            giftQuotasUseDetail.setStoreId(storeInfo.getStoreId());
            giftQuotasUseDetail.setChangeInGiftQuota("-"+giftAmount);
            giftQuotasUseDetail.setBillCode(orderStoreCode);
            giftQuotasUseDetail.setType(2);
            giftQuotasUseDetail.setCreateBy(auth.getPersonName());
            giftQuotasUseDetail.setUpdateBy(auth.getPersonName());
            //插入一条赠品明细使用记录
            giftQuotasUseDetailService.add(giftQuotasUseDetail);
        }


    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo saveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, true);
//        if (!ErpOrderTypeEnum.ASSIST_PURCHASING.getCode().equals(erpOrderSaveRequest.getOrderType())) {
//            throw new BusinessException("只能创建" + ErpOrderTypeEnum.ASSIST_PURCHASING.getDesc() + "的订单");
//        }

        //获取门店信息
        StoreInfo storeInfo = erpOrderRequestService.getStoreInfoByStoreId(erpOrderSaveRequest.getStoreId());
        //商品参数信息
        List<ErpOrderProductItemRequest> itemList = erpOrderSaveRequest.getItemList();

        //构建货架订单商品信息行
        List<ErpOrderItem> orderItemList = generateRackOrderItemList(itemList, storeInfo);

        //获取订单节点进程控制枚举
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (processTypeEnum == null) {
            throw new BusinessException("不允许创建类型为" + ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()) + "，类别为" + ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()) + "的订单");
        }
        //数据校验
        productCheck(processTypeEnum, storeInfo, orderItemList);

        //构建和保存货架订单，返回订单编号
        String orderCode = generateAndSaveRackOrder(erpOrderSaveRequest, orderItemList, storeInfo, auth);

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);

        if (processTypeEnum.isLockStock()) {
            //锁库
            boolean flag = erpOrderRequestService.lockStockInSupplyChain(order, orderItemList, auth);
            if (!flag) {
                throw new BusinessException("锁库存失败");
            }
        }

        //更新门店营业状态
        erpOrderRequestService.updateStoreBusinessStage(storeInfo.getStoreId(), OrderConstant.OPEN_STATUS_CODE_17, OrderConstant.OPEN_STATUS_CODE_18);

        //返回订单
        return order;
    }

    @Override
    @Transactional
    public String callback(FormCallBackVo formCallBackVo) {
        String result = "success";
        log.info("首单赠送超额审批进入回调,request={}", formCallBackVo);
        OrderGiveApproval orderGiveApproval = orderGiveApprovalDao.selectByFormNo(formCallBackVo.getFormNo());
        log.info("首单赠送超额审批回调,查询原始记录结果orderGiveApproval={}", orderGiveApproval);
        if (orderGiveApproval != null) {
            if (formCallBackVo.getUpdateFormStatus().equals(Indicator.COST_FORM_STATUS_APPROVED.getCode())) {
                //审核通过，修改本地主表状态
                orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_PASS.getValue());
                orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_PASS.getDesc());
                //修改主订单状态 改为待支付
                ErpOrderInfo po=new ErpOrderInfo();
                po.setOrderStoreCode(orderGiveApproval.getOrderCode());
                //待支付
                po.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
//                AuthToken auth=new AuthToken();
//                auth.setPersonId(ConstantData.SYS_OPERTOR);
//                auth.setPersonName(ConstantData.SYS_OPERTOR);
//                AuthUtil.loginCheck();
                AuthToken auth = AuthUtil.getCurrentAuth();
                erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(po,auth);
                //发起支付 自动轮询
                ErpOrderPayRequest payRequest = new ErpOrderPayRequest();
                payRequest.setOrderCode(orderGiveApproval.getOrderCode());
                payRequest.setPayWay(ErpPayWayEnum.PAY_1.getCode());
                erpOrderPayNoTransactionalService.orderPayStartMethodGroup(payRequest, auth, true);
                log.info("首单赠送超额审批回调结束");
            } else if (TpmBpmUtils.isPass(formCallBackVo.getUpdateFormStatus(), formCallBackVo.getOptBtn())) {
                orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT.getValue());
                orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT.getDesc());
            } else {
                if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_FIRST.getCode())) {
                    //驳回发起人
                    orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_BACK.getValue());
                    orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_BACK.getDesc());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_REJECT_END.getCode())) {
                    //驳回并结束
                    orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_END.getValue());
                    orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_END.getDesc());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_CANCEL.getCode())) {
                    //撤销
                    orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_CANCEL.getValue());
                    orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_CANCEL.getDesc());
                } else if (formCallBackVo.getOptBtn().equals(IndicatorStr.PROCESS_BTN_KILL.getCode())) {
                    //终止
                    orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_END.getValue());
                    orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_END.getDesc());
                } else {
                    //终止
                    orderGiveApproval.setStatus(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_END.getValue());
                    orderGiveApproval.setStatuStr(com.aiqin.platform.flows.client.constant.StatusEnum.AUDIT_END.getDesc());
                }
            }
            //更新本地审批表数据
            orderGiveApprovalDao.updateByFormNoSelective(orderGiveApproval);
        } else {
            //result = "false";
        }
        return result;
    }


    @Override
    public OrderGiveApproval getDetailByformNo(String formNo) {
        return orderGiveApprovalDao.selectByFormNo(formNo);
    }

    /**
     * 校验保存订单参数
     *
     * @param erpOrderSaveRequest
     * @param erpOrder            erp的订单
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/25 9:40
     */
    private void validateSaveOrderRequest(ErpOrderSaveRequest erpOrderSaveRequest, boolean erpOrder) {
        if (erpOrderSaveRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderSaveRequest.getStoreId())) {
            throw new BusinessException("请传入门店id");
        }
        if (erpOrderSaveRequest.getOrderType() == null) {
            throw new BusinessException("请传入订单类型");
        } else {
            if (!ErpOrderTypeEnum.exist(erpOrderSaveRequest.getOrderType())) {
                throw new BusinessException("无效的订单类型");
            }
        }
        if (ErpOrderTypeEnum.DIRECT_SEND.getCode().equals(erpOrderSaveRequest.getOrderType()) && erpOrder) {
            erpOrderSaveRequest.setOrderCategory(ErpOrderCategoryEnum.ORDER_TYPE_2.getCode());
        } else {
            if (erpOrderSaveRequest.getOrderCategory() == null) {
                throw new BusinessException("请传入订单类别");
            } else {
                if (!ErpOrderCategoryEnum.exist(erpOrderSaveRequest.getOrderCategory())) {
                    throw new BusinessException("无效的订单类别");
                }
            }
        }
    }

    /**
     * 从购物车获取选择的门店的勾选的商品
     *
     * @param storeId 门店id
     * @return com.aiqin.mgs.order.api.domain.response.cart.OrderConfirmResponse
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:00
     */
    private OrderConfirmResponse getStoreCartProduct(String storeId,Integer orderType) {


        //调用查询购物车接口
        CartOrderInfo cartOrderInfoQuery = new CartOrderInfo();
        cartOrderInfoQuery.setStoreId(storeId);
        cartOrderInfoQuery.setProductType(orderType);
        HttpResponse listHttpResponse = cartOrderService.displayCartLineCheckProduct(cartOrderInfoQuery);
        if (!RequestReturnUtil.validateHttpResponse(listHttpResponse)) {
            throw new BusinessException("获取购物车商品失败");
        }
        StoreCartProductResponse data = (StoreCartProductResponse) listHttpResponse.getData();
        if (data == null || data.getCartGroupList() == null || data.getCartGroupList().size() == 0) {
            throw new BusinessException("购物车没有勾选的商品");
        }

        return null;
    }

    /**
     * 根据门店id和订单类型获取购物车商品信息
     *
     * @param storeId
     * @param orderType
     * @return
     */
//    private List<CartOrderInfo> getStoreCartProductList(String cartGroupTempKey,String storeId, Integer orderType, ErpOrderSourceEnum orderSourceEnum) {
    private List<ErpOrderCartInfo> getStoreCartProductList(String cartGroupTempKey,String storeId, Integer orderType, ErpOrderSourceEnum orderSourceEnum) {
        log.info("创建订单,获取购物车商品入参cartGroupTempKey={},storeId={},orderType={},orderSourceEnum={}",cartGroupTempKey,storeId,orderType,orderSourceEnum);
//        List<CartOrderInfo> list = new ArrayList<>();
        List<ErpOrderCartInfo> cartInfoList=new ArrayList<>();

        if (ErpOrderSourceEnum.STORE == orderSourceEnum) {
            //调用查询购物车接口
            CartOrderInfo cartOrderInfoQuery = new CartOrderInfo();
            cartOrderInfoQuery.setStoreId(storeId);
            cartOrderInfoQuery.setProductType(orderType);
//            HttpResponse listHttpResponse = cartOrderService.displayCartLineCheckProduct(cartOrderInfoQuery);
            ErpQueryCartGroupTempRequest erpQueryCartGroupTempRequest=new ErpQueryCartGroupTempRequest();
            erpQueryCartGroupTempRequest.setCartGroupTempKey(cartGroupTempKey);
            log.info("创建订单,调用查询购物车接口入参erpQueryCartGroupTempRequest={}",erpQueryCartGroupTempRequest);
            ErpStoreCartQueryResponse erpStoreCartQueryResponse = erpOrderCartService.queryCartGroupTemp(erpQueryCartGroupTempRequest, null);
            log.info("创建订单,调用查询购物车接口返回结果erpStoreCartQueryResponse={}",erpStoreCartQueryResponse);
            if(erpStoreCartQueryResponse==null){
                throw new BusinessException("获取购物车商品失败");
            }
            if(null==erpStoreCartQueryResponse.getCartGroupList()){
                throw new BusinessException("购物车没有勾选的商品");
            }
//            if (!RequestReturnUtil.validateHttpResponse(listHttpResponse)) {
//                throw new BusinessException("获取购物车商品失败");
//            }
//            StoreCartProductResponse data = (StoreCartProductResponse) listHttpResponse.getData();
//            if (data == null || data.getCartGroupList() == null || data.getCartGroupList().size() == 0) {
//                throw new BusinessException("购物车没有勾选的商品");
//            }

//            List<CartGroupInfo> cartGroupList = data.getCartGroupList();
            List<ErpCartGroupInfo> cartGroupList = erpStoreCartQueryResponse.getCartGroupList();
            for (ErpCartGroupInfo item : cartGroupList) {
                for (ErpOrderCartInfo productItem : item.getCartProductList()) {
                    if (!ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(orderType)) {
                        productItem.setActivityId(null);
//                        productItem.setActivityName(null);
                    }
                    if(null!=item.getActivityRule()&&null!=item.getActivityRule().getActivityType()){
                        productItem.setActivityType(item.getActivityRule().getActivityType());
                    }
                    cartInfoList.add(productItem);
                }
                if (ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(orderType)) {
                    if (item.getCartGiftList() != null && item.getCartGiftList().size() > 0) {
//                        list.addAll(item.getCartGiftList());
                        cartInfoList.addAll(item.getCartGiftList());
                    }
                }
            }
            //兑换赠品相关商品
            if (erpStoreCartQueryResponse.getErpCartQueryResponse().getCartInfoList() != null && erpStoreCartQueryResponse.getErpCartQueryResponse().getCartInfoList().size() > 0) {
                cartInfoList.addAll(erpStoreCartQueryResponse.getErpCartQueryResponse().getCartInfoList());
            }
        } else {
//            list = cartOrderService.getErpProductList(storeId, orderType);
            //修改erp查询list
            ErpCartQueryRequest erpCartQueryRequest=new ErpCartQueryRequest();
            erpCartQueryRequest.setProductType(orderType);
            erpCartQueryRequest.setStoreId(storeId);
            erpCartQueryRequest.setLineCheckStatus(YesOrNoEnum.YES.getCode());
            log.info("创建订单,调用ERP购物车接口入参erpCartQueryRequest={}",erpCartQueryRequest);
            ErpCartQueryResponse erpCartQueryResponse = erpOrderCartService.queryErpCartList(erpCartQueryRequest, null);
            log.info("创建订单,调用ERP购物车接口返回结果erpCartQueryResponse={}",erpCartQueryResponse);
            cartInfoList = erpCartQueryResponse.getCartInfoList();
            if (cartInfoList == null) {
                throw new BusinessException("购物车没有勾选的商品");
            }
        }
        return cartInfoList;
    }

    /**
     * 构建订单商品明细行数据
     *
     * @param cartProductList 购物车商品行
     * @param storeInfo       门店信息
     * @param processTypeEnum 节点控制
     * @return java.util.List<com.aiqin.mgs.order.api.domain.ErpOrderItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:02
     */
//    private List<ErpOrderItem> generateOrderItemList(List<CartOrderInfo> cartProductList, StoreInfo storeInfo, ErpOrderNodeProcessTypeEnum processTypeEnum) {
    private List<ErpOrderItem> generateOrderItemList(List<ErpOrderCartInfo> cartProductList, StoreInfo storeInfo, ErpOrderNodeProcessTypeEnum processTypeEnum) {
        log.info("构建订单商品明细行数据入参cartProductList={},storeInfo={},processTypeEnum={}",cartProductList,storeInfo,processTypeEnum);
        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //遍历参数商品列表，获取商品详情，校验数据
        for (ErpOrderCartInfo item :
                cartProductList) {
            if (!productMap.containsKey(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode())) {
                // 此处查询单个sku详情增加批次信息【购物车已经选择了批次】和销售库特卖库标识
                //获取商品详情
                ProductInfo product = erpOrderRequestService.getSkuDetail(OrderConstant.SELECT_PRODUCT_COMPANY_CODE, item.getSkuCode(),item.getWarehouseTypeCode(),item.getBatchInfoCode(),storeInfo.getProvinceId(),storeInfo.getCityId());
                if (product == null) {
                    throw new BusinessException("未获取到商品" + item.getSpuName() + "的信息");
                }
                product.setBatchCode(item.getBatchCode());
                product.setBatchDate(item.getBatchDate());
                product.setBatchInfoCode(item.getBatchInfoCode());
                product.setWarehouseTypeCode(item.getWarehouseTypeCode());
                productMap.put(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode(), product);
            }
        }

        long lineCode = 1;
        //订单商品明细行
        List<ErpOrderItem> orderItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
//        for (CartOrderInfo item : cartProductList) {
        for (ErpOrderCartInfo item : cartProductList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());

            ErpOrderItem orderItem = new ErpOrderItem();
            orderItem.setLineCode(lineCode++);
            //spu编码
            orderItem.setSpuCode(productInfo.getSpuCode());
            //spu名称
            orderItem.setSpuName(productInfo.getSpuName());
            //sku编码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //sku名称
            orderItem.setSkuName(productInfo.getSkuName());
            //条形码
            orderItem.setBarCode(productInfo.getBarCode());
            //图片地址
            orderItem.setPictureUrl(productInfo.getPictureUrl());
            //规格
            orderItem.setProductSpec(productInfo.getProductSpec());
            //颜色编码
            orderItem.setColorCode(productInfo.getColorCode());
            //颜色名称
            orderItem.setColorName(productInfo.getColorName());
            //型号
            orderItem.setModelCode(productInfo.getModelCode());
            //单位编码
            orderItem.setUnitCode(productInfo.getUnitCode());
            //单位名称
            orderItem.setUnitName(productInfo.getUnitName());
            //折零系数 不填
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(item.getProductGift());
            //商品属性编码
            orderItem.setProductPropertyCode(productInfo.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(productInfo.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(productInfo.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(productInfo.getSupplierName());
            //商品数量
            orderItem.setProductCount((long) item.getAmount());
            //商品单价
            orderItem.setProductAmount(item.getPrice());
            //商品单价
            orderItem.setActivityPrice(item.getActivityPrice());
            //商品总价
            orderItem.setTotalProductAmount(item.getLineActivityAmountTotal());
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(item.getLineAmountAfterActivity());
            //分摊后单价
            if(null!=item.getLineAmountAfterActivity()){
                orderItem.setPreferentialAmount(item.getLineAmountAfterActivity().divide(new BigDecimal(item.getAmount()),2, RoundingMode.DOWN));
            }else{
                orderItem.setPreferentialAmount(BigDecimal.ZERO);
            }
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(item.getLineActivityDiscountTotal());
            //税率
            orderItem.setTaxRate(productInfo.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(storeInfo.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(storeInfo.getCompanyName());
            //单个商品毛重(kg)
            orderItem.setBoxGrossWeight(productInfo.getBoxGrossWeight());
            //单个商品包装体积(mm³)
            orderItem.setBoxVolume(productInfo.getBoxVolume());
            orderItem.setProductBrandCode(productInfo.getProductBrandCode());
            orderItem.setProductBrandName(productInfo.getProductBrandName());
            orderItem.setProductCategoryCode(productInfo.getProductCategoryCode());
            orderItem.setProductCategoryName(productInfo.getProductCategoryName());
            orderItem.setActivityDiscountAmount(item.getLineActivityDiscountTotal());
            if (processTypeEnum.getOrderTypeEnum() == ErpOrderTypeEnum.DISTRIBUTION) {
                orderItem.setActivityId(item.getActivityId());
            }
            orderItem.setIsActivity(StringUtils.isNotEmpty(orderItem.getActivityId()) ? YesOrNoEnum.YES.getCode() : YesOrNoEnum.NO.getCode());

            if (ErpOrderNodeProcessTypeEnum.PROCESS_2 == processTypeEnum) {
                //配送-首单赠送 把价格都置为0

                //商品单价
//                orderItem.setProductAmount(BigDecimal.ZERO);
                //商品总价
                orderItem.setTotalProductAmount(BigDecimal.ZERO);
                //实际商品总价
                orderItem.setActualTotalProductAmount(BigDecimal.ZERO);
                //优惠分摊总金额
                orderItem.setTotalPreferentialAmount(BigDecimal.ZERO);
                //分摊后单价
                orderItem.setTotalPreferentialAmount(BigDecimal.ZERO);
                //活动优惠总金额
                orderItem.setTotalAcivityAmount(BigDecimal.ZERO);
            }

            //增加批次信息
            orderItem.setBatchCode(productInfo.getBatchCode());
            orderItem.setBatchDate(productInfo.getBatchDate());
            orderItem.setBatchInfoCode(productInfo.getBatchInfoCode());
            orderItem.setWarehouseTypeCode(productInfo.getWarehouseTypeCode());
            orderItem.setBatchType(productInfo.getBatchType());

            //活动类型
            orderItem.setActivityType(item.getActivityType());
            //采购价
            orderItem.setPurchaseAmount(productInfo.getPurchaseAmount());
            orderItemList.add(orderItem);
        }
        log.info("构建订单商品明细行数据返回结果orderItemList={}",orderItemList);
        return orderItemList;
    }

    /**
     * 商品校验
     *
     * @param processTypeEnum      订单节点进程控制枚举
     * @param storeInfo            门店信息
     * @param orderProductItemList 订单商品明细行
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:03
     */
    private void productCheck(ErpOrderNodeProcessTypeEnum processTypeEnum, StoreInfo storeInfo, List<ErpOrderItem> orderProductItemList) {

        if (processTypeEnum.isActivityCheck()) {
            //活动校验
            boolean flag = erpOrderRequestService.activityCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("活动校验失败");
            }
        }
        if (processTypeEnum.isAreaCheck()) {
            //商品销售区域配置校验 TODO 供应链接口不通，暂时去掉
//            boolean flag = erpOrderRequestService.areaCheck(storeInfo, orderProductItemList);
//            if (!flag) {
//                throw new BusinessException("商品销售区域校验失败");
//            }
        }
        if (processTypeEnum.isRepertoryCheck()) {
            //商品库存校验
            boolean flag = erpOrderRequestService.repertoryCheck(storeInfo, orderProductItemList);
            if (!flag) {
                throw new BusinessException("商品库存校验失败");
            }
        }

    }

    /**
     * 构建订单信息
     *
     * @param flag       是否是首单赠送超额审批 true：需要审批
     * @param flag1      是否是首单赠送市值订单 true：是
     * @param orderItemList       订单商品明细行
     * @param storeInfo           门店信息
     * @param erpOrderSaveRequest 保存订单参数
     * @param auth                操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 19:04
     */
//    private ErpOrderInfo generateOrder(List<ErpOrderItem> orderItemList, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest,ErpOrderFee orderFee, AuthToken auth) {
    private ErpOrderInfo generateOrder(boolean flag,boolean flag1,List<ErpOrderItem> orderItemList, StoreInfo storeInfo, ErpOrderSaveRequest erpOrderSaveRequest,ErpOrderFee orderFee, AuthToken auth) {
        log.info("构建订单信息--入参,orderItemList={}",orderItemList);
        log.info("构建订单信息--入参,storeInfo={}",storeInfo);
        log.info("构建订单信息--入参,erpOrderSaveRequest={}",erpOrderSaveRequest);
        log.info("构建订单信息--入参,orderFee={}",orderFee);
        log.info("构建订单信息--入参,auth={}",auth);
        //商品毛重(kg)
        BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
        //商品包装体积(mm³)
        BigDecimal boxVolumeTotal = BigDecimal.ZERO;

        Integer isActivity = YesOrNoEnum.NO.getCode();
        for (ErpOrderItem item :
                orderItemList) {
            //商品毛重汇总
            boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(item.getProductCount())));
            //商品体积汇总
            boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(item.getProductCount())));
            if (YesOrNoEnum.YES.getCode().equals(item.getIsActivity())) {
                isActivity = YesOrNoEnum.YES.getCode();
            }
        }

        //订单信息
        ErpOrderInfo order = new ErpOrderInfo();
        order.setItemList(orderItemList);
        //公司编码
        order.setCompanyCode(storeInfo.getCompanyCode());
        //公司名称
        order.setCompanyName(storeInfo.getCompanyName());
        //订单类型编码 0直送、1配送、2辅采直送
        order.setOrderTypeCode(erpOrderSaveRequest.getOrderType().toString());
        //订单类型名称
        order.setOrderTypeName(ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()));
        //订单类别编码
        order.setOrderCategoryCode(erpOrderSaveRequest.getOrderCategory().toString());
        //订单类别名称
        order.setOrderCategoryName(ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()));
        //客户编码 存门店编码
        order.setCustomerCode(storeInfo.getStoreCode());
        //客户名称 存门店名称
        order.setCustomerName(storeInfo.getStoreName());
        if(flag){
            //订单状态
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_7.getCode());
            //订单流程节点状态
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        }else {
            //订单状态
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
            //订单流程节点状态
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        }
//        //订单状态
//        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
//        //订单流程节点状态
//        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        //是否锁定(0是1否）
        order.setOrderLock(StatusEnum.NO.getCode());
        //是否是异常订单(0是1否)
        order.setOrderException(StatusEnum.NO.getCode());
        //是否删除(0是1否)
        order.setOrderDelete(StatusEnum.NO.getCode());
        //支付状态0已支付  1未支付
        order.setPaymentStatus(StatusEnum.NO.getCode());

        //收货区域 :省编码
        order.setProvinceId(storeInfo.getProvinceId());
        //收货区域 :省
        order.setProvinceName(storeInfo.getProvinceName());
        //收货区域 :市编码
        order.setCityId(storeInfo.getCityId());
        //收货区域 :市
        order.setCityName(storeInfo.getCityName());
        //收货区域 :区县编码
        order.setDistrictId(storeInfo.getDistrictId());
        //收货区域 :区县
        order.setDistrictName(storeInfo.getDistrictName());

        //收货地址
        order.setReceiveAddress(storeInfo.getAddress());
        //收货人
        order.setReceivePerson(storeInfo.getContacts());
        //收货人电话
        order.setReceiveMobile(storeInfo.getContactsPhone());
        //商品总价
        order.setTotalProductAmount(orderFee.getTotalMoney());
        //优惠额度
        order.setDiscountAmount(orderFee.getActivityMoney().add(orderFee.getTopCouponMoney()));
        //实际支付金额 没有活动优惠券的情况下等于总价
        order.setOrderAmount(orderFee.getPayMoney());
        //发运状态
        order.setTransportStatus(StatusEnum.NO.getCode());
        //发票类型 默认不开发票
        order.setInvoiceType(ErpInvoiceTypeEnum.NO_INVOICE.getCode());
        //体积
        order.setTotalVolume(boxVolumeTotal);
        //重量
        order.setTotalWeight(boxGrossWeightTotal);
        //订单级别(0主1子订单)
        order.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 (0是 1否)
        order.setSplitStatus(StatusEnum.NO.getCode());
        //门店id
        order.setStoreId(storeInfo.getStoreId());
        //门店编码
        order.setStoreCode(storeInfo.getStoreCode());
        //门店名称
        order.setStoreName(storeInfo.getStoreName());
        //退货状态
        order.setOrderReturn(ErpOrderReturnStatusEnum.NONE.getCode());
        //退货流程状态
        order.setOrderReturnProcess(StatusEnum.NO.getCode());
        //加盟商id
        order.setFranchiseeId(storeInfo.getFranchiseeId());
        //加盟商编码
        order.setFranchiseeCode(storeInfo.getFranchiseeCode());
        //加盟商名称
        order.setFranchiseeName(storeInfo.getFranchiseeName());
        //同步供应链状态
        order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
        //生成冲减单状态
        order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.NOT_NEED.getCode());
        //是否活动商品0否1是
        order.setIsActivity(isActivity);
        //配送方式编码
        order.setDistributionModeCode(erpOrderSaveRequest.getDistributionModeCode());
        //配送方式名称
        order.setDistributionModeName(erpOrderSaveRequest.getDistributionModeName());
        //是否为首单市值赠送订单 0:不是 1:是
        if(flag1){
            order.setFirstMarketValueGift(1);
        }else {
            order.setFirstMarketValueGift(0);
        }
        CopartnerAreaUp copartnerAreaUp=copartnerAreaDao.getCopartnerAreaById(storeInfo.getStoreId());
        if(null!=copartnerAreaUp){
            order.setCopartnerAreaId(copartnerAreaUp.getCopartnerAreaId());
            order.setCopartnerAreaName(copartnerAreaUp.getCopartnerAreaName());
            order.setCopartnerAreaCode(copartnerAreaUp.getId());
        }
        //业务形式 0  门店
        order.setBusinessForm(0);
        log.info("构建订单信息--封装结果,order={}",order);
        return order;
    }

    /**
     * 计算均摊金额
     *
     * @param processTypeEnum
     * @param order
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 15:35
     */
    private void sharePrice(ErpOrderNodeProcessTypeEnum processTypeEnum, ErpOrderInfo order, List<String> topCouponCodeList) {

        ErpOrderFee orderFee = order.getOrderFee();
        //A品券编码，逗号隔开
        String topCouponCodes = null;
        //A品券优惠金额
        BigDecimal topCouponMoney = BigDecimal.ZERO;
        List<String> topCouponCodeUniqueCheckList = new ArrayList<>();
        if (topCouponCodeList != null && topCouponCodeList.size() > 0) {
            for (String item :
                    topCouponCodeList) {
                if (topCouponCodeUniqueCheckList.contains(item)) {
                    throw new BusinessException("A品券重复提交");
                } else {
                    topCouponCodeUniqueCheckList.add(item);
                }
                CouponDetail couponDetail = erpOrderRequestService.getCouponDetailByCode(item);
                if (couponDetail.getActiveCondition() == 1) {
                    throw new BusinessException("优惠券已经被使用");
                }
                if (couponDetail.getActiveCondition() == 2) {
                    throw new BusinessException("优惠券已经过期");
                }
                topCouponMoney = topCouponMoney.add(couponDetail.getNominalValue());
            }
            topCouponCodes = String.join(",", topCouponCodeList);
        }


        //计算A品券金额
        List<ErpOrderItem> topProductList = new ArrayList<>();
        for (ErpOrderItem item :
                order.getItemList()) {
            ErpProductPropertyTypeEnum propertyTypeEnum = ErpProductPropertyTypeEnum.getEnum(item.getProductPropertyCode());
            if (processTypeEnum != null && propertyTypeEnum.isUseTopCoupon()) {
                topProductList.add(item);
            }
        }

        //订单总金额
        BigDecimal totalMoney = orderFee.getTotalMoney();
        //已经分摊的金额临时缓存
        BigDecimal usedTopCouponAmount = BigDecimal.ZERO;
        if (topProductList.size() > 0) {
            for (int i = 0; i < topProductList.size(); i++) {
                ErpOrderItem item = topProductList.get(i);
                //行均摊后金额
                BigDecimal totalPreferentialAmount = item.getTotalPreferentialAmount();
                //行均摊后单价
                BigDecimal preferentialAmount = item.getPreferentialAmount();
                //行A品券金额
                BigDecimal lineTopCouponMoney = BigDecimal.ZERO;

                if (topCouponMoney.compareTo(BigDecimal.ZERO) > 0) {
                    if (i < topProductList.size() - 1) {
                        //非最后一行，根据比例计算
                        lineTopCouponMoney = topCouponMoney.multiply(item.getTotalProductAmount()).divide(totalMoney, 2, RoundingMode.HALF_UP);
                    } else {
                        //最后一行，用减法防止误差
                        lineTopCouponMoney = topCouponMoney.subtract(usedTopCouponAmount);
                    }
                    if (lineTopCouponMoney.compareTo(item.getTotalProductAmount()) > 0) {
                        //防止金额减成负数
                        lineTopCouponMoney = item.getTotalProductAmount();
                    }
                    //已使用A品优惠金额
                    usedTopCouponAmount = usedTopCouponAmount.add(lineTopCouponMoney);
                    //行均摊金额
                    totalPreferentialAmount = totalPreferentialAmount.subtract(lineTopCouponMoney);
                    //行均摊单价
                    preferentialAmount = totalPreferentialAmount.divide(new BigDecimal(item.getProductCount()), 2, RoundingMode.HALF_DOWN);
                }

                //优惠分摊总金额（分摊后金额）
                item.setTotalPreferentialAmount(totalPreferentialAmount);
                //分摊后单价
                item.setPreferentialAmount(preferentialAmount);
                //活动优惠总金额
                item.setTotalAcivityAmount(item.getTotalAcivityAmount().add(lineTopCouponMoney));
            }
        }

        orderFee.setSuitCouponMoney(BigDecimal.ZERO);
        orderFee.setActivityMoney(BigDecimal.ZERO);
        orderFee.setTopCouponMoney(usedTopCouponAmount);
        orderFee.setTopCouponCodes(topCouponCodes);
        orderFee.setPayMoney(orderFee.getTotalMoney().subtract(orderFee.getActivityMoney()).subtract(orderFee.getSuitCouponMoney()).subtract(orderFee.getTopCouponMoney()));
    }

    private ErpOrderFee shareTopCouponPrice(List<ErpOrderItem> itemList,List<String> topCouponCodeList) {

        //查询A品卷使用规则code Map
        Map<String,BigDecimal> ruleMap=couponRuleService.couponRuleMap();
        //A品卷规则额度
        BigDecimal ruleTop=BigDecimal.ZERO;

        List<CouponShareRequest> detailList = new ArrayList<>();
        for (ErpOrderItem item :
                itemList) {
            CouponShareRequest itemRequest = new CouponShareRequest();
            itemRequest.setLineCode(item.getLineCode());
            itemRequest.setSkuCode(item.getSkuCode());
            itemRequest.setProductCount(item.getProductCount());
            itemRequest.setTotalProductAmount(item.getTotalProductAmount());
            itemRequest.setProductPropertyCode(item.getProductPropertyCode());
            itemRequest.setTotalPreferentialAmount(item.getTotalPreferentialAmount());
            itemRequest.setPreferentialAmount(item.getTotalPreferentialAmount().divide(new BigDecimal(item.getProductCount()),2, RoundingMode.DOWN));
            itemRequest.setApinCouponAmount(BigDecimal.ZERO);
            itemRequest.setProductGift(item.getProductType());
            detailList.add(itemRequest);
        }
        //A品券计算均摊金额
//        couponSharePrice(detailList, topCouponCodeList);
        //A品券总金额
        BigDecimal totalCouponSharePrice = getTotalCouponSharePrice(topCouponCodeList);

        Map<Long, CouponShareRequest> map = new HashMap<>(16);
        for (CouponShareRequest item :
                detailList) {
            map.put(item.getLineCode(), item);
        }

        ErpOrderFee orderFee = new ErpOrderFee();


        //订单总额（元）
        BigDecimal totalMoneyTotal = BigDecimal.ZERO;
        //活动优惠金额（元）
        BigDecimal activityMoneyTotal = BigDecimal.ZERO;
        //服纺券优惠金额（元）
        BigDecimal suitCouponMoneyTotal = BigDecimal.ZERO;
        //A品券优惠金额（元）
        BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
        //实付金额（元）
        BigDecimal payMoneyTotal = BigDecimal.ZERO;
        //使用赠品额度（元）
        BigDecimal usedGiftQuota = BigDecimal.ZERO;
        //服纺券作废金额（元）
        BigDecimal nullifySuitCouponMoneyTotal = BigDecimal.ZERO;
        //A品券作废金额（元）
        BigDecimal nullifyTopCouponMoneyTotal = BigDecimal.ZERO;
        //18A商品总金额（元）
        BigDecimal groupTopProductTotal = BigDecimal.ZERO;

        for (ErpOrderItem item :
                itemList) {
            CouponShareRequest couponShareRequest = map.get(item.getLineCode());
            item.setTotalPreferentialAmount(couponShareRequest.getTotalPreferentialAmount());
            item.setPreferentialAmount(couponShareRequest.getPreferentialAmount());
            item.setTopCouponDiscountAmount(couponShareRequest.getApinCouponAmount());
            item.setTotalAcivityAmount(item.getActivityDiscountAmount().add(item.getTopCouponDiscountAmount()));

            totalMoneyTotal = totalMoneyTotal.add(item.getTotalProductAmount());
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityDiscountAmount());
            topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponDiscountAmount());
            if(ErpProductGiftEnum.JIFEN.getCode().equals(item.getProductType())){
                usedGiftQuota=usedGiftQuota.add(item.getProductAmount().multiply(new BigDecimal(item.getProductCount().toString())));
            }else if(ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductType())){
                //商品判断商品类型，是否属于18A
                if(ruleMap.containsKey(item.getProductPropertyCode())){
                    //18A规则系数
                    ruleTop=ruleMap.get(item.getProductPropertyCode());
                    //计算18A商品总金额
                    if(null!=item.getActivityType()&&item.getActivityType()==2){
                        groupTopProductTotal=groupTopProductTotal.add(item.getTotalProductAmount());
                    }else{
                        groupTopProductTotal=groupTopProductTotal.add(item.getTotalPreferentialAmount());
                    }
                }
            }

        }
        //计算出的18A商品总金额得乘以设置的规则系数
        groupTopProductTotal=groupTopProductTotal.multiply(ruleTop).setScale(2, RoundingMode.DOWN);
        nullifyTopCouponMoneyTotal=nullifyTopCouponMoneyTotal.add(totalCouponSharePrice.subtract(groupTopProductTotal));
        if(nullifyTopCouponMoneyTotal.compareTo(BigDecimal.ZERO)<0){
            //A品卷金额小于18A商品总额
            nullifyTopCouponMoneyTotal=BigDecimal.ZERO;
            orderFee.setTopCouponMoney(totalCouponSharePrice);
            orderFee.setPayMoney(totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(totalCouponSharePrice).subtract(usedGiftQuota));
        }else{
            //A品卷金额大于18A商品总额
            orderFee.setTopCouponMoney(groupTopProductTotal);
            orderFee.setPayMoney(totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(groupTopProductTotal).subtract(usedGiftQuota));
        }
        log.info("创建订单 ---- 生成支付信息-- 自选赠品金额 usedGiftQuota={}"+usedGiftQuota+
                "|===| 活动优惠金额 activityMoneyTotal={}"+activityMoneyTotal+
                "|===| 服纺券优惠金额 suitCouponMoneyTotal={}"+suitCouponMoneyTotal+
                "|===| A品券优惠金额 topCouponMoneyTotal={}"+topCouponMoneyTotal);
        log.info("创建订单 --计算均摊金额-- 生成支付信息-- 订单总额 totalMoneyTotal={}"+totalMoneyTotal);
        orderFee.setTotalMoney(totalMoneyTotal);
        orderFee.setActivityMoney(activityMoneyTotal);
        orderFee.setSuitCouponMoney(suitCouponMoneyTotal);
        orderFee.setNullifySuitCouponMoney(nullifySuitCouponMoneyTotal);
        orderFee.setNullifyTopCouponMoney(nullifyTopCouponMoneyTotal);
//        orderFee.setTopCouponMoney(topCouponMoneyTotal);

//        orderFee.setPayMoney(totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(topCouponMoneyTotal));

        orderFee.setUsedGiftQuota(usedGiftQuota);
        if(null!=topCouponCodeList&&topCouponCodeList.size()>0){
            orderFee.setTopCouponCodes(String.join(",", topCouponCodeList));
        }else {
            orderFee.setTopCouponCodes("");
        }
        return orderFee;
    }

    /**
     * A品券优惠总金额
     *
     * @param couponCodeList
     */
    private BigDecimal getTotalCouponSharePrice(List<String> couponCodeList) {
        log.info("A品券计算均摊金额入参,couponCodeList={}", couponCodeList);
        //A品券总金额
        BigDecimal topCouponMoney = BigDecimal.ZERO;
        List<String> topCouponCodeUniqueCheckList = new ArrayList<>();
        if (couponCodeList != null && couponCodeList.size() > 0) {
            for (String couponCode : couponCodeList) {
                if (topCouponCodeUniqueCheckList.contains(couponCode)) {
                    throw new BusinessException("A品券重复提交");
                } else {
                    topCouponCodeUniqueCheckList.add(couponCode);
                }
                CouponDetail couponDetail = erpOrderRequestService.getCouponDetailByCode(couponCode);
                if (couponDetail.getActiveCondition() == 1) {
                    throw new BusinessException("优惠券已经被使用");
                }
                if (couponDetail.getActiveCondition() == 2) {
                    throw new BusinessException("优惠券已经过期");
                }
                topCouponMoney = topCouponMoney.add(couponDetail.getNominalValue());
            }
        }
        log.info("A品券计算均摊金额--A品券总金额,topCouponMoney={}", topCouponMoney);
        return topCouponMoney;
    }

    /**
     * A品券计算均摊金额
     *
     * @param details
     * @param couponCodeList
     */
    private void couponSharePrice(List<CouponShareRequest> details, List<String> couponCodeList) {
        log.info("A品券计算均摊金额入参,details={},couponCodeList={}", details, couponCodeList);
        //A品券总金额
        BigDecimal topCouponMoney = BigDecimal.ZERO;
        List<String> topCouponCodeUniqueCheckList = new ArrayList<>();
        if (couponCodeList != null && couponCodeList.size() > 0) {
            for (String couponCode : couponCodeList) {
                if (topCouponCodeUniqueCheckList.contains(couponCode)) {
                    throw new BusinessException("A品券重复提交");
                } else {
                    topCouponCodeUniqueCheckList.add(couponCode);
                }
                CouponDetail couponDetail = erpOrderRequestService.getCouponDetailByCode(couponCode);
                if (couponDetail.getActiveCondition() == 1) {
                    throw new BusinessException("优惠券已经被使用");
                }
                if (couponDetail.getActiveCondition() == 2) {
                    throw new BusinessException("优惠券已经过期");
                }
                topCouponMoney = topCouponMoney.add(couponDetail.getNominalValue());
            }
        }
        log.info("A品券计算均摊金额--A品券总金额,topCouponMoney={}", topCouponMoney);
        //计算A品券金额
        List<CouponShareRequest> topProductList = new ArrayList<>();
        //存储符合A品卷均摊的商品的总分销价(上一次分摊总价)
        BigDecimal totalFirstFenAmount = BigDecimal.ZERO;
        //总分销价(商品组价值)
        BigDecimal totalProAmount = BigDecimal.ZERO;
        //活动均摊后金额汇总
        BigDecimal totalAmountAfterActivity = BigDecimal.ZERO;
        for (CouponShareRequest item : details) {
            Map erpProductPropertyType = couponRuleService.couponRuleMap();
            log.info("获取A品卷属性返回结果,erpProductPropertyType={}", erpProductPropertyType);
            log.info(item.getSkuCode()+"此商品属性编码,productPropertyCode={}", item.getProductPropertyCode());
            if(null!=erpProductPropertyType&&null!=erpProductPropertyType.get(item.getProductPropertyCode())){
                if (ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductGift())) {
                    topProductList.add(item);
                    //分销总价=从活动的分摊总价取
                    totalFirstFenAmount = totalFirstFenAmount.add(item.getTotalPreferentialAmount());
                    totalProAmount = totalProAmount.add(item.getTotalProductAmount());
                    totalAmountAfterActivity = totalAmountAfterActivity.add(item.getTotalPreferentialAmount());
                }
            }
//            ErpProductPropertyTypeEnum propertyTypeEnum = ErpProductPropertyTypeEnum.getEnum(item.getProductPropertyCode());
//            log.info("判断是否是A品卷,propertyTypeEnum={}", propertyTypeEnum);
//            log.info("判断是否是A品卷,ProductGiftEnumCode={}", ErpProductGiftEnum.PRODUCT.getCode());
//            log.info("判断是否是A品卷,ProductGift={}", item.getProductGift());
//            if(propertyTypeEnum!=null){
//                log.info("判断是否是A品卷,isUseTopCoupon={}", propertyTypeEnum.isUseTopCoupon());
//                if (propertyTypeEnum.isUseTopCoupon() && ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductGift())) {
//                    topProductList.add(item);
//                    //分销总价=从活动的分摊总价取
//                    totalFirstFenAmount = totalFirstFenAmount.add(item.getTotalPreferentialAmount());
//                    totalProAmount = totalProAmount.add(item.getTotalProductAmount());
//                    totalAmountAfterActivity = totalAmountAfterActivity.add(item.getTotalPreferentialAmount());
//                }
//            }
        }
        log.info("A品券计算均摊金额,符合A品卷均摊的商品topProductList={}", topProductList);
        //判断优惠券总金额和从活动的分摊总价取，如果A品卷总金额大于活动分摊总价，则A品券总金额=活动分摊总价
        if (topCouponMoney.subtract(totalFirstFenAmount).compareTo(BigDecimal.ZERO) >0) {
            topCouponMoney = totalFirstFenAmount;
        }
        //商品组实收(商品组价值-A品卷)
        BigDecimal auGroupAmount = totalFirstFenAmount.subtract(topCouponMoney);
        log.info("A品券计算均摊金额--总分销价:totalProAmount={},上一次分摊总价:totalFirstFenAmount={},商品组实收:auGroupAmount={}", totalProAmount, totalFirstFenAmount, auGroupAmount);
        //计算累加各行A品券优惠金额（最后一行使用）
        BigDecimal totalApinAmount = BigDecimal.ZERO;
        if (topProductList != null && topProductList.size() > 0) {
            for (int i = 0; i < topProductList.size(); i++) {
                CouponShareRequest csr = topProductList.get(i);
                //A品卷分摊后总价值
                BigDecimal totalPreferentialAmount = csr.getTotalPreferentialAmount();
                //本行A品卷优惠金额
                BigDecimal apinCouponAmount = BigDecimal.ZERO;

                //本行活动均摊后金额
                BigDecimal lineAmountAfterActivity = csr.getTotalPreferentialAmount();
                if (i < topProductList.size() - 1) {
                    //非最后一行，根据比例计算

                    //计算本行占用了A品券的金额
                    apinCouponAmount = topCouponMoney.multiply(lineAmountAfterActivity).divide(totalAmountAfterActivity, 2, RoundingMode.HALF_UP);
                    //A品券剩余金额
                    totalApinAmount = totalApinAmount.add(apinCouponAmount);
                } else {
                    //最后一行，用减法防止误差
                    apinCouponAmount = topCouponMoney.subtract(totalApinAmount);
                }

                //计算本行A品券均摊之后的金额
                totalPreferentialAmount = totalPreferentialAmount.subtract(apinCouponAmount);
                //分摊单价
                BigDecimal preferentialAmount = totalPreferentialAmount.divide(new BigDecimal(csr.getProductCount()), 2, RoundingMode.DOWN);
                csr.setTotalPreferentialAmount(totalPreferentialAmount);
                csr.setPreferentialAmount(preferentialAmount);
                csr.setApinCouponAmount(apinCouponAmount);
            }
        }
        log.info("A品券计算均摊金额处理结果,details={}", details);
    }


    /**
     * //保存订单、订单明细、订单支付、订单收货人信息、订单日志
     *
     * @param order               订单信息
     * @param storeInfo           门店信息
     * @param auth                操作人信息
     * @param erpOrderSaveRequest 请求参数
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 15:34
     */
    private String insertOrder(ErpOrderInfo order, ErpOrderFee orderFee, StoreInfo storeInfo, AuthToken auth, ErpOrderSaveRequest erpOrderSaveRequest) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成费用id
        String feeId = OrderPublic.getUUID();
//        //生成订单code
//        String orderCode = sequenceGeneratorService.generateOrderCode();
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;

        for (ErpOrderItem item :
                order.getItemList()) {
            //订单id
            item.setOrderStoreId(orderId);
            //订单编码
//            item.setOrderStoreCode(orderCode);
            item.setOrderStoreCode(order.getOrderStoreCode());
            //订单明细id
            item.setOrderInfoDetailId(OrderPublic.getUUID());
            item.setUseStatus(StatusEnum.YES.getValue());
        }
        //保存订单
        order.setOrderStoreId(orderId);
//        order.setOrderStoreCode(orderCode);
        order.setOrderStoreCode(order.getOrderStoreCode());
//        order.setMainOrderCode(orderCode);
        order.setMainOrderCode(order.getOrderStoreCode());
        order.setFeeId(feeId);
        erpOrderInfoService.saveOrder(order, auth);

        //保存订单明细行
        erpOrderItemService.saveOrderItemList(order.getItemList(), auth);

        orderFee.setOrderId(orderId);
        orderFee.setPayId(null);
        orderFee.setFeeId(feeId);
        orderFee.setPayStatus(payStatusEnum.getCode());
        erpOrderFeeService.saveOrderFee(orderFee, auth);

        return orderId;
    }

    /**
     * 从购物车删除已经生成订单的商品
     *
     * @param cartProductList 购物车商品
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/21 16:02
     */
//    private void deleteOrderProductFromCart(List<CartOrderInfo> cartProductList) {
    private void deleteOrderProductFromCart(List<ErpOrderCartInfo> cartProductList) {
//        for (CartOrderInfo item : cartProductList) {
        for (ErpOrderCartInfo item : cartProductList) {
            if (ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductGift())) {
//                cartOrderService.deleteByCartId(item.getCartId());
                //新接口删除购物车
                erpOrderCartService.deleteCartLine(item.getCartId());
            }
        }
    }

    /**
     * 构建货架订单商品信息行数据
     *
     * @param paramItemList 货架商品参数
     * @param storeInfo     门店信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:43
     */
    private List<ErpOrderItem> generateRackOrderItemList(List<ErpOrderProductItemRequest> paramItemList, StoreInfo storeInfo) {

        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少商品数据");
        }

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        //参数商品行标识
        int lineIndex = 0;

        //遍历参数商品列表，获取商品详情，校验数据
        for (ErpOrderProductItemRequest item :
                paramItemList) {
            lineIndex++;
            if (StringUtils.isEmpty(item.getSpuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品spu编码为空");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品sku编码为空");
            }
            if (item.getTaxPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品采购价为空");
            }
            if (item.getPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品销售价为空");
            }
            if (item.getQuantity() == null) {
                throw new BusinessException("第" + lineIndex + "行商品数量为空");
            }

            //获取商品详情
            ProductInfo product = erpOrderRequestService.getSkuDetail(OrderConstant.SELECT_PRODUCT_COMPANY_CODE, item.getSkuCode(), null,null,storeInfo.getProvinceId(),storeInfo.getCityId());
            if (product == null) {
                throw new BusinessException("第" + lineIndex + "行商品不存在");
            }
            productMap.put(item.getSkuCode(), product);
        }

        //订单商品明细行
        List<ErpOrderItem> orderItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (ErpOrderProductItemRequest item :
                paramItemList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode());

            ErpOrderItem orderItem = new ErpOrderItem();
            //spu编码
            orderItem.setSpuCode(productInfo.getSpuCode());
            //spu名称
            orderItem.setSpuName(productInfo.getSpuName());
            //sku编码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //sku名称
            orderItem.setSkuName(productInfo.getSkuName());
            //条形码
            orderItem.setBarCode(productInfo.getBarCode());
            //图片地址
            orderItem.setPictureUrl(productInfo.getPictureUrl());
            //规格
            orderItem.setProductSpec(productInfo.getProductSpec());
            //颜色编码
            orderItem.setColorCode(productInfo.getColorCode());
            //颜色名称
            orderItem.setColorName(productInfo.getColorName());
            //型号
            orderItem.setModelCode(productInfo.getModelCode());
            //单位编码
            orderItem.setUnitCode(productInfo.getUnitCode());
            //单位名称
            orderItem.setUnitName(productInfo.getUnitName());
            //折零系数 不存
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(ErpProductGiftEnum.PRODUCT.getCode());
            //商品属性编码
            orderItem.setProductPropertyCode(productInfo.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(productInfo.getProductPropertyName());
            orderItem.setProductBrandCode(productInfo.getProductBrandCode());
            orderItem.setProductBrandName(productInfo.getProductBrandName());
            orderItem.setProductCategoryCode(productInfo.getProductCategoryCode());
            orderItem.setProductCategoryName(productInfo.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(productInfo.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(productInfo.getSupplierName());
            //商品数量
            orderItem.setProductCount(Long.valueOf(item.getQuantity()));
            //商品单价
            orderItem.setProductAmount(item.getPrice());
            orderItem.setActivityPrice(item.getPrice());
            //商品含税采购价
            orderItem.setPurchaseAmount(item.getTaxPrice());
            //商品总价
            orderItem.setTotalProductAmount(item.getPrice().multiply(new BigDecimal(item.getQuantity())));
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(orderItem.getTotalProductAmount());
            //分摊后单价
            orderItem.setPreferentialAmount(orderItem.getProductAmount());
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(BigDecimal.ZERO);
            //税率
            orderItem.setTaxRate(productInfo.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(storeInfo.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(storeInfo.getCompanyName());
            //单个商品毛重(kg)
            orderItem.setBoxGrossWeight(productInfo.getBoxGrossWeight());
            //单个商品包装体积(mm³)
            orderItem.setBoxVolume(productInfo.getBoxVolume());
            orderItem.setIsActivity(YesOrNoEnum.NO.getCode());
            orderItem.setActivityDiscountAmount(BigDecimal.ZERO);
            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    /**
     * 构建和保存货架订单数据
     *
     * @param erpOrderSaveRequest 入口参数
     * @param orderItemList       订单商品明细行
     * @param storeInfo           门店信息
     * @param auth                操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:50
     */
    private String generateAndSaveRackOrder(ErpOrderSaveRequest erpOrderSaveRequest, List<ErpOrderItem> orderItemList, StoreInfo storeInfo, AuthToken auth) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成费用id
        String feeId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = sequenceGeneratorService.generateOrderCode();
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;

        //订货金额汇总
        BigDecimal moneyTotal = BigDecimal.ZERO;
        //商品毛重(kg)
        BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
        //商品包装体积(mm³)
        BigDecimal boxVolumeTotal = BigDecimal.ZERO;

        long lineCode = 1L;

        //遍历商品订单行，汇总价格
        for (ErpOrderItem item :
                orderItemList) {

            //订单id
            item.setOrderStoreId(orderId);
            //订单编码
            item.setOrderStoreCode(orderCode);
            //订单明细id
            item.setOrderInfoDetailId(OrderPublic.getUUID());
            //订单明细行编号
            item.setLineCode(lineCode++);
            item.setUseStatus(StatusEnum.YES.getValue());

            //金额汇总
            moneyTotal = moneyTotal.add(item.getTotalProductAmount() == null ? BigDecimal.ZERO : item.getTotalProductAmount());
            //商品毛重汇总
            boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(item.getProductCount())));
            //商品体积汇总
            boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(item.getProductCount())));
        }

        erpOrderItemService.saveOrderItemList(orderItemList, auth);

        ErpOrderInfo order = new ErpOrderInfo();
        ErpOrderFee orderFee = new ErpOrderFee();

        Boolean flag1=false;
        Boolean flag2=false;
        //首单赠送--加入了市值赠送金额判断
        if(ErpOrderTypeEnum.DIRECT_PURCHASE.getCode().equals(erpOrderSaveRequest.getOrderType())&&ErpOrderCategoryEnum.ORDER_TYPE_4.getCode().equals(erpOrderSaveRequest.getOrderCategory())){
            Map<ErpOrderFee,Boolean> res=firstGivePrice(erpOrderSaveRequest.getApplier(),erpOrderSaveRequest.getDeptCode(),orderCode,storeInfo,orderItemList, erpOrderSaveRequest.getTopCouponCodeList());
            for(Map.Entry<ErpOrderFee,Boolean> r:res.entrySet()){
                orderFee = r.getKey();
                flag1=r.getValue();
            }
            flag2=true;
        }else if(ErpOrderTypeEnum.DIRECT_PURCHASE.getCode().equals(erpOrderSaveRequest.getOrderType())&&ErpOrderCategoryEnum.ORDER_TYPE_147.getCode().equals(erpOrderSaveRequest.getOrderCategory())){
            //计算A品券分摊金额
            orderFee = shareTopCouponPrice(orderItemList, erpOrderSaveRequest.getTopCouponCodeList());
        }
        //业务id
        order.setOrderStoreId(orderId);
        //订单编码
        order.setOrderStoreCode(orderCode);
        //公司编码
        order.setCompanyCode(storeInfo.getCompanyCode());
        //公司名称
        order.setCompanyName(storeInfo.getCompanyName());
        //订单类型编码 0直送、1配送、2辅采直送
        order.setOrderTypeCode(erpOrderSaveRequest.getOrderType().toString());
        //订单类型名称
        order.setOrderTypeName(ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()));
        //订单类别编码
        order.setOrderCategoryCode(erpOrderSaveRequest.getOrderCategory().toString());
        //订单类别名称
        order.setOrderCategoryName(ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()));
        //客户编码
        order.setCustomerCode(storeInfo.getStoreCode());
        //客户名称
        order.setCustomerName(storeInfo.getStoreName());
        if(flag1){
            //订单状态
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_7.getCode());
            //订单流程节点状态
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        }else {
            //订单状态
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
            //订单流程节点状态
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        }
        //是否锁定(0是1否）
        order.setOrderLock(StatusEnum.NO.getCode());
        //是否是异常订单(0是1否)
        order.setOrderException(StatusEnum.NO.getCode());
        //是否删除(0是1否)
        order.setOrderDelete(StatusEnum.NO.getCode());
        //支付状态0已支付  1未支付
        order.setPaymentStatus(StatusEnum.NO.getCode());

        //收货区域 :省编码
        order.setProvinceId(storeInfo.getProvinceId());
        //收货区域 :省
        order.setProvinceName(storeInfo.getProvinceName());
        //收货区域 :市编码
        order.setCityId(storeInfo.getCityId());
        //收货区域 :市
        order.setCityName(storeInfo.getCityName());
        //收货区域 :区县编码
        order.setDistrictId(storeInfo.getDistrictId());
        //收货区域 :区县
        order.setDistrictName(storeInfo.getDistrictName());

        //收货地址
        order.setReceiveAddress(storeInfo.getAddress());
        //收货人
        order.setReceivePerson(storeInfo.getContacts());
        //收货人电话
        order.setReceiveMobile(storeInfo.getContactsPhone());
        //商品总价
        order.setTotalProductAmount(moneyTotal);
        //实际商品总价
        order.setActualTotalProductAmount(moneyTotal);
        //优惠额度
        order.setDiscountAmount(BigDecimal.ZERO);
        //实付金额
        order.setOrderAmount(moneyTotal);
        //发运状态
        order.setTransportStatus(StatusEnum.NO.getCode());
        //发票类型
        order.setInvoiceType(ErpInvoiceTypeEnum.NO_INVOICE.getCode());
        //体积
        order.setTotalVolume(boxVolumeTotal);
        //重量
        order.setTotalWeight(boxGrossWeightTotal);
        //主订单号  如果非子订单 此处存order_code
        order.setMainOrderCode(orderCode);
        //订单级别(0主1子订单)
        order.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 (0是 1否)
        order.setSplitStatus(StatusEnum.NO.getCode());
        //门店id
        order.setStoreId(storeInfo.getStoreId());
        //门店编码
        order.setStoreCode(storeInfo.getStoreCode());
        //门店名称
        order.setStoreName(storeInfo.getStoreName());
        //费用id
        order.setFeeId(feeId);
        //退货状态
        order.setOrderReturn(ErpOrderReturnStatusEnum.NONE.getCode());
        //退货流程状态
        order.setOrderReturnProcess(StatusEnum.NO.getCode());
        //加盟商id
        order.setFranchiseeId(storeInfo.getFranchiseeId());
        //加盟商编码
        order.setFranchiseeCode(storeInfo.getFranchiseeCode());
        //加盟商名称
        order.setFranchiseeName(storeInfo.getFranchiseeName());
        //同步供应链状态
        order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
        //生成冲减单状态
        order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.NOT_NEED.getCode());
        order.setIsActivity(YesOrNoEnum.NO.getCode());
        //是否为首单市值赠送订单 0:不是 1:是
        if(flag1){
            order.setFirstMarketValueGift(1);
        }else {
            order.setFirstMarketValueGift(0);
        }
        //销售单增加合伙人公司字段
        CopartnerAreaUp copartnerAreaUp=copartnerAreaDao.getCopartnerAreaById(storeInfo.getStoreId());
        if(null!=copartnerAreaUp){
            order.setCopartnerAreaId(copartnerAreaUp.getCopartnerAreaId());
            order.setCopartnerAreaName(copartnerAreaUp.getCopartnerAreaName());
            order.setCopartnerAreaCode(copartnerAreaUp.getId());
        }

        //配送方式编码
        order.setDistributionModeCode(erpOrderSaveRequest.getDistributionModeCode());
        //配送方式名称
        order.setDistributionModeName(erpOrderSaveRequest.getDistributionModeName());

        //业务形式 0  门店
        order.setBusinessForm(0);

        erpOrderInfoService.saveOrder(order, auth);

        //费用id
        orderFee.setFeeId(feeId);
        //订单id
        orderFee.setOrderId(orderId);
        //支付状态
        orderFee.setPayStatus(payStatusEnum.getCode());
        //保存订单费用信息 如果非首单，再生成费用信息
        if(!flag2){
            //支付单id
            orderFee.setPayId(null);
            //订单总额
            orderFee.setTotalMoney(moneyTotal);
            //活动优惠金额
            orderFee.setActivityMoney(BigDecimal.ZERO);
            //服纺券优惠金额
            orderFee.setSuitCouponMoney(BigDecimal.ZERO);
            //A品券优惠金额
            orderFee.setTopCouponMoney(BigDecimal.ZERO);
            //实付金额
            orderFee.setPayMoney(orderFee.getTotalMoney().subtract(orderFee.getActivityMoney()).subtract(orderFee.getSuitCouponMoney()).subtract(orderFee.getTopCouponMoney()));
        }

        erpOrderFeeService.saveOrderFee(orderFee, auth);

        return orderCode;
    }

    /**
     * 首单赠送金额计算
     * @param itemList
     * @param topCouponCodeList
     */
    private Map<ErpOrderFee,Boolean> firstGivePrice(String applier,String positionCode,String orderCode,StoreInfo storeInfo,List<ErpOrderItem> itemList,List<String> topCouponCodeList) {
        Map<ErpOrderFee,Boolean> resMap=new HashMap<>();
        //true:赠送市值余额不够发起审批 false:不用审批
        Boolean flag=false;
        //赠送市值
        Double marketValue = storeInfo.getMarketValue();
        //赠送市值余额
        Double marketValueBalance = storeInfo.getMarketValueBalance();
        if(null==marketValueBalance){
            marketValueBalance=0.00;
        }
        //赠送费用
        Double freeCost = storeInfo.getFreeCost();
        //赠送费用余额
        Double freeCostBalance1=storeInfo.getFreeCostBalance();
        List<CouponShareRequest> detailList = new ArrayList<>();
        for (ErpOrderItem item :
                itemList) {
            CouponShareRequest itemRequest = new CouponShareRequest();
            itemRequest.setLineCode(item.getLineCode());
            itemRequest.setSkuCode(item.getSkuCode());
            itemRequest.setProductCount(item.getProductCount());
            itemRequest.setTotalProductAmount(item.getTotalProductAmount());
            itemRequest.setProductPropertyCode(item.getProductPropertyCode());
            itemRequest.setTotalPreferentialAmount(item.getTotalPreferentialAmount());
            itemRequest.setPreferentialAmount(item.getTotalPreferentialAmount().divide(new BigDecimal(item.getProductCount()),2, RoundingMode.DOWN));
            itemRequest.setApinCouponAmount(BigDecimal.ZERO);
            itemRequest.setProductGift(item.getProductType());
            detailList.add(itemRequest);
        }
        //A品券计算均摊金额 没有A品卷跳过计算
//        couponSharePrice(detailList, topCouponCodeList);

        Map<Long, CouponShareRequest> map = new HashMap<>(16);
        for (CouponShareRequest item :
                detailList) {
            map.put(item.getLineCode(), item);
        }

        ErpOrderFee orderFee = new ErpOrderFee();


        //订单总额（元）
        BigDecimal totalMoneyTotal = BigDecimal.ZERO;
        //活动优惠金额（元）
        BigDecimal activityMoneyTotal = BigDecimal.ZERO;
        //服纺券优惠金额（元）
        BigDecimal suitCouponMoneyTotal = BigDecimal.ZERO;
        //A品券优惠金额（元）
        BigDecimal topCouponMoneyTotal = BigDecimal.ZERO;
        //实付金额（元）
        BigDecimal payMoneyTotal = BigDecimal.ZERO;
        //自选赠品金额（元）
        BigDecimal usedGiftQuota = BigDecimal.ZERO;

        for (ErpOrderItem item :
                itemList) {
            CouponShareRequest couponShareRequest = map.get(item.getLineCode());
            item.setTotalPreferentialAmount(couponShareRequest.getTotalPreferentialAmount());
            item.setPreferentialAmount(couponShareRequest.getPreferentialAmount());
            item.setTopCouponDiscountAmount(couponShareRequest.getApinCouponAmount());
            item.setTotalAcivityAmount(item.getActivityDiscountAmount().add(item.getTopCouponDiscountAmount()));

            totalMoneyTotal = totalMoneyTotal.add(item.getTotalProductAmount());
            activityMoneyTotal = activityMoneyTotal.add(item.getActivityDiscountAmount());
            topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponDiscountAmount());
        }
        //判断订单总金额是否大于赠送市值
        if(totalMoneyTotal.compareTo(new BigDecimal(marketValueBalance))>0){//大于赠送市值，走审批
            //TODO 走审批
            flag=true;
            //orderCode 订单编码
            String approvalCode=createFormNo();
            OrderGiveApproval record=new OrderGiveApproval();
            record.setActualMarketValue(totalMoneyTotal);
            record.setFanchiseeId(storeInfo.getFranchiseeId());
            record.setFanchiseeName(storeInfo.getFranchiseeName());
            record.setMarketValue(new BigDecimal(storeInfo.getMarketValue()));
            record.setOrderCode(orderCode);
            //赠送市值余额--用于修改
            marketValueBalance = 0.0;
            //赠送费用余额--用于修改
            freeCostBalance1=0.0;
            //发起审批
            HttpResponse httpResponse = submitActBaseProcess(approvalCode, applier, positionCode, record);
            if(!"0".equals(httpResponse.getCode())){
                throw new BusinessException("订单金额超过市值赠送总金额审批申请失败");
            }
        }else{//计算分摊
            //计算比例系数=总金额/赠送总市值
            BigDecimal pro=totalMoneyTotal.divide(new BigDecimal(marketValue));
            //根据系数，算实付金额=赠送费用X系数
            BigDecimal shiFuAmount=pro.multiply(pro);
            //统计分摊总金额和，用于最后一行做减法
            BigDecimal fenTanAmount=BigDecimal.ZERO;
            //遍历详情从新算金额
            for(int i=0;i<itemList.size();i++){
                ErpOrderItem item=itemList.get(i);
                BigDecimal productAmount = item.getProductAmount();
                BigDecimal totalPreferentialAmount=productAmount.divide(totalMoneyTotal,10,ROUND_HALF_DOWN).multiply(shiFuAmount);
                fenTanAmount=fenTanAmount.add(totalPreferentialAmount);
                item.setTotalPreferentialAmount(totalPreferentialAmount);
                if(i==itemList.size()){
                    //最后一行做减法
                    item.setPreferentialAmount(shiFuAmount.subtract(fenTanAmount));
                }
                item.setPreferentialAmount(totalPreferentialAmount.divide(new BigDecimal(item.getProductCount())));
//                item.setTopCouponDiscountAmount(couponShareRequest.getApinCouponAmount());
                item.setTotalAcivityAmount(item.getActivityDiscountAmount().add(item.getTopCouponDiscountAmount()));

//                totalMoneyTotal = totalMoneyTotal.add(item.getTotalProductAmount());
//                activityMoneyTotal = activityMoneyTotal.add(item.getActivityDiscountAmount());
//                topCouponMoneyTotal = topCouponMoneyTotal.add(item.getTopCouponDiscountAmount());
                if(ErpProductGiftEnum.JIFEN.getCode().equals(item.getProductType())){
                    usedGiftQuota=usedGiftQuota.add(item.getTotalProductAmount());
                }
            }
            //支付金额
            BigDecimal zhi=totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(topCouponMoneyTotal).subtract(usedGiftQuota);
            //赠送市值余额--用于修改
            marketValueBalance = marketValueBalance-totalMoneyTotal.doubleValue();
            //赠送费用余额--用于修改
            freeCostBalance1=freeCostBalance1-zhi.doubleValue();
        }

        log.info("创建订单 --首单赠送金额计算-- 生成支付信息-- 订单总额 totalMoneyTotal={}"+totalMoneyTotal);
        orderFee.setTotalMoney(totalMoneyTotal);
        orderFee.setActivityMoney(activityMoneyTotal);
        orderFee.setSuitCouponMoney(suitCouponMoneyTotal);
        orderFee.setTopCouponMoney(topCouponMoneyTotal);
        log.info("创建订单 --首单赠送金额计算-- 生成支付信息-- 自选赠品金额 usedGiftQuota={}"+usedGiftQuota+
                "|===| 活动优惠金额 activityMoneyTotal={}"+activityMoneyTotal+
                "|===| 服纺券优惠金额 suitCouponMoneyTotal={}"+suitCouponMoneyTotal+
                "|===| A品券优惠金额 topCouponMoneyTotal={}"+topCouponMoneyTotal);
        orderFee.setPayMoney(totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(topCouponMoneyTotal).subtract(usedGiftQuota));
        if(null!=topCouponCodeList&&topCouponCodeList.size()>0){
            orderFee.setTopCouponCodes(String.join(",", topCouponCodeList));
        }else {
            orderFee.setTopCouponCodes("");
        }
        //存储赠送市值到本地数据库
        OrderGiveFee record=new OrderGiveFee();
        record.setAmount(totalMoneyTotal.subtract(activityMoneyTotal).subtract(suitCouponMoneyTotal).subtract(topCouponMoneyTotal));
        record.setCreateTime(new Date());
        record.setOrderCode(orderCode);
        record.setStoreId(storeInfo.getStoreId());
        orderGiveFeeDao.insertSelective(record);
        //修改门店系统的市值余额
        upGive(storeInfo.getStoreCode(),marketValueBalance,freeCostBalance1);
        resMap.put(orderFee,flag);
        return resMap;
    }

    /**
     * 封装 订单金额超过市值赠送总金额调用审批
     *
     * @param approvalCode 审批号
     * @param applier      申请人
     * @param positionCode 部门编码
     */
    public HttpResponse submitActBaseProcess(String approvalCode, String applier, String positionCode,OrderGiveApproval record) {
        StartProcessParamVO paramVO = new StartProcessParamVO();
        // 订单金额超过市值赠送总金额审批申请
        paramVO.setCostType(ConstantData.FIRST_SINGLE_EXCESS_KEY);
        paramVO.setFormType(ConstantData.FIRST_SINGLE_EXCESS_KEY);
        paramVO.setFormUrl("http://order.api.aiqin.com/erpOrderCreateController/formDetail/" + approvalCode);//表单详情页面地址 必传
        paramVO.setCurrentUser(applier);
        paramVO.setFormNo(approvalCode);
        paramVO.setTitle("订单金额超过市值赠送总金额审批");
        paramVO.setRemark("订单金额超过市值赠送总金额审批");
        paramVO.setFormUpdateUrl("http://order.api.aiqin.com/erpOrderCreateController/callback");//回调地址
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
            //将审批信息添加到本地审批表中
            record.setProcessTitle("订单金额超过市值赠送总金额审批");
            //todo
//            record.setProcessType();
            record.setApplierId(applier);
            record.setApplierName(applier);
            record.setFormNo(approvalCode);
            record.setCreateTime(new Date());
            orderGiveApprovalDao.insertSelective(record);
            return HttpResponse.success(actBaseProcessEntity);
        }
        log.error("发起申请失败,request={}", paramVO);
        return HttpResponse.failure(ResultCode.MARKET_VALUE_APPLY_FALL);
    }

    /**
     * 修改slcs门店市值赠余额
     */
    private void upGive(String storeCode,Double marketValueBalance,Double freeCostBalance){
        log.info("修改slcs门店市值赠余额,入参storeCode={},marketValueBalance={},freeCostBalance={}",storeCode,marketValueBalance,freeCostBalance);
        String url=slcsHost+"/store/updateValue";
        JSONObject json=new JSONObject();
        StoreMarketValueResponse storeMarketValueResponse=new StoreMarketValueResponse();
        storeMarketValueResponse.setFreeCostBalance(freeCostBalance);
        storeMarketValueResponse.setMarketValueBalance(marketValueBalance);
        storeMarketValueResponse.setStoreCode(storeCode);
        json.put("storeMarketValueResponse",storeMarketValueResponse);
        log.info("修改slcs门店市值赠余额,调用修改赠送市值余额接口,url={},parm={}",url,json);
        HttpClient httpClient = HttpClient.post(url).json(json);
        Map<String ,Object> res=null;
        res = httpClient.action().result(new TypeReference<Map<String ,Object>>() {});
        log.info("同步到虚拟资产:"+res);
        if(res!=null&&"0".equals(res.get("code"))){
            log.info("修改slcs门店市值赠余额,调用修改赠送市值余额接口,成功");
        }

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
        String value = "MV"+valueOf + String.format("%015d", hashCode);
        return value;
    }

    /**
     * 计算均摊金额--新修改
     *
     * @param orderId 主订单号
     */
    private void couponSharePrice(List<CouponShareRequest> details,String orderId) {
        log.info("计算均摊金额--新修改,orderId={}", orderId);
        //查询主订单费用信息
        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByOrderId(orderId);
        log.info("计算均摊金额--主订单费用信息结果,orderFee={}", orderFee);
        if(orderFee==null){
            throw new BusinessException("计算均摊金额--主订单费用信息结果为空");
        }
        //A品券总金额
        BigDecimal topCouponMoney = orderFee.getTopCouponMoney();
        log.info("计算均摊金额--A品券总金额,topCouponMoney={}", topCouponMoney);
        //计算A品券金额
        List<CouponShareRequest> topProductList = new ArrayList<>();
        //存储符合A品卷均摊的商品的总分销价(上一次分摊总价)
        BigDecimal totalFirstFenAmount = BigDecimal.ZERO;
        //总分销价(商品组价值)
        BigDecimal totalProAmount = BigDecimal.ZERO;
        //活动均摊后金额汇总
        BigDecimal totalAmountAfterActivity = BigDecimal.ZERO;
        for (CouponShareRequest item : details) {
            Map erpProductPropertyType = couponRuleService.couponRuleMap();
            log.info("获取A品卷属性返回结果,erpProductPropertyType={}", erpProductPropertyType);
            log.info(item.getSkuCode()+"此商品属性编码,productPropertyCode={}", item.getProductPropertyCode());
            if(null!=erpProductPropertyType&&null!=erpProductPropertyType.get(item.getProductPropertyCode())){
                if (ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductGift())) {
                    topProductList.add(item);
                    //活动分摊后金额
                    BigDecimal totalpPreferentialAmount = BigDecimal.ZERO;
                    if(item.getTotalProductAmount()!=null){
                        totalpPreferentialAmount=item.getTotalProductAmount();
                    }
                    //分销总价=从活动的分摊总价取
                    totalFirstFenAmount = totalFirstFenAmount.add(totalpPreferentialAmount);
                    totalProAmount = totalProAmount.add(item.getTotalProductAmount());
                    totalAmountAfterActivity = totalAmountAfterActivity.add(item.getTotalPreferentialAmount());
                }
            }
        }
        log.info("A品券计算均摊金额,符合A品卷均摊的商品topProductList={}", topProductList);
        //判断优惠券总金额和从活动的分摊总价取，如果A品卷总金额大于活动分摊总价，则A品券总金额=活动分摊总价
        if (topCouponMoney.subtract(totalFirstFenAmount).compareTo(BigDecimal.ZERO) >0) {
            topCouponMoney = totalFirstFenAmount;
        }
        //商品组实收(商品组价值-A品卷)
        BigDecimal auGroupAmount = totalFirstFenAmount.subtract(topCouponMoney);
        log.info("A品券计算均摊金额--总分销价:totalProAmount={},上一次分摊总价:totalFirstFenAmount={},商品组实收:auGroupAmount={}", totalProAmount, totalFirstFenAmount, auGroupAmount);
        //计算累加各行A品券优惠金额（最后一行使用）
        BigDecimal totalApinAmount = BigDecimal.ZERO;
        if (topProductList != null && topProductList.size() > 0) {
            for (int i = 0; i < topProductList.size(); i++) {
                CouponShareRequest csr = topProductList.get(i);
                //A品卷分摊后总价值
                BigDecimal totalPreferentialAmount = csr.getTotalPreferentialAmount();
                //本行A品卷优惠金额
                BigDecimal apinCouponAmount = BigDecimal.ZERO;

                //本行活动均摊后金额
                BigDecimal lineAmountAfterActivity = csr.getTotalPreferentialAmount();
                if (i < topProductList.size() - 1) {
                    //非最后一行，根据比例计算

                    //计算本行占用了A品券的金额
                    apinCouponAmount = topCouponMoney.multiply(lineAmountAfterActivity).divide(totalAmountAfterActivity, 2, RoundingMode.HALF_UP);
                    //A品券剩余金额
                    totalApinAmount = totalApinAmount.add(apinCouponAmount);
                } else {
                    //最后一行，用减法防止误差
                    apinCouponAmount = topCouponMoney.subtract(totalApinAmount);
                }

                //计算本行A品券均摊之后的金额
                totalPreferentialAmount = totalPreferentialAmount.subtract(apinCouponAmount);
                //分摊单价
                BigDecimal preferentialAmount = totalPreferentialAmount.divide(new BigDecimal(csr.getProductCount()), 2, RoundingMode.DOWN);
                csr.setTotalPreferentialAmount(totalPreferentialAmount);
                csr.setPreferentialAmount(preferentialAmount);
                csr.setApinCouponAmount(apinCouponAmount);
            }
        }
        log.info("A品券计算均摊金额处理结果,details={}", details);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ErpOrderInfo saveWholesaleOrder(ErpOrderSaveRequest erpOrderSaveRequest, AuthToken auth) {

        //校验参数
        validateSaveOrderRequest(erpOrderSaveRequest, false);
        //获取订单节点进程控制枚举
        ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(erpOrderSaveRequest.getOrderType(), erpOrderSaveRequest.getOrderCategory());
        if (processTypeEnum == null) {
            throw new BusinessException("不允许创建类型为" + ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()) + "，类别为" + ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()) + "的订单");
        }
        if (!ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(erpOrderSaveRequest.getOrderType())) {
            throw new BusinessException("只能创建" + ErpOrderTypeEnum.DISTRIBUTION.getDesc() + "的订单");
        }

        //获取批发客户信息
        WholesaleCustomers wholesaleCustomer=wholesaleCustomersService.getCustomerByCode(erpOrderSaveRequest.getStoreId()).getData();
        log.info("创建批发订单,获取批发客户信息返回结果 wholesaleCustomer={}",JsonUtil.toJson(wholesaleCustomer));

        List<ErpOrderCartInfo> cartProductList = getStoreCartProductList(erpOrderSaveRequest.getCartGroupTempKey(),erpOrderSaveRequest.getStoreId(), erpOrderSaveRequest.getOrderType(),ErpOrderSourceEnum.STORE);

        //构建批发订单商品信息行
        List<ErpOrderItem> orderItemList = generateWholesaleOrderItemList(cartProductList, wholesaleCustomer);



        //构建和保存批发订单，返回订单编号
        String orderCode = generateAndSaveWholesaleOrder(erpOrderSaveRequest, orderItemList, wholesaleCustomer, auth);

        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);

        if (processTypeEnum.isLockStock()) {
            //锁库
            boolean flag = erpOrderRequestService.lockStockInSupplyChain(order, orderItemList, auth);
            if (!flag) {
                throw new BusinessException("锁库存失败");
            }
        }
        //删除购物车商品
//        deleteOrderProductFromCart(cartProductList);
        deleteOrderProductFromCart(cartProductList);
        //返回订单
        return order;
    }

    /**
     * 构建批发订单商品信息行数据
     *
     * @param paramItemList 批发商品参数
     * @param wholesaleCustomer     批发客户信息
     * @return java.util.List<com.aiqin.mgs.order.api.domain.OrderStoreOrderProductItem>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:43
     */
    private List<ErpOrderItem> generateWholesaleOrderItemList(List<ErpOrderCartInfo> paramItemList,WholesaleCustomers wholesaleCustomer) {

        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少商品数据");
        }

        //商品详情Map
        Map<String, ProductInfo> productMap = new HashMap<>(16);

        List<ProductSkuRequest2> productSkuRequest2List =new ArrayList<>();

        //参数商品行标识
        int lineIndex = 0;

        //遍历参数商品列表，获取商品详情，校验数据
        for (ErpOrderCartInfo item :
                paramItemList) {
            lineIndex++;
            if (StringUtils.isEmpty(item.getSpuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品spu编码为空");
            }
            if (StringUtils.isEmpty(item.getSkuCode())) {
                throw new BusinessException("第" + lineIndex + "行商品sku编码为空");
            }
            if (item.getPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品价格为空");
            }
            if (item.getPrice() == null) {
                throw new BusinessException("第" + lineIndex + "行商品销售价为空");
            }
            if (item.getAmount() == null) {
                throw new BusinessException("第" + lineIndex + "行商品数量为空");
            }

            //获取商品详情
            ProductInfo product = erpOrderRequestService.getSkuDetail(OrderConstant.SELECT_PRODUCT_COMPANY_CODE, item.getSkuCode(),item.getWarehouseTypeCode(),item.getBatchInfoCode(),wholesaleCustomer.getProvinceId(),wholesaleCustomer.getCityId());
            if (product == null) {
                throw new BusinessException("第" + lineIndex + "行商品不存在");
            }
            productMap.put(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode(), product);

            ProductSkuRequest2 productSkuRequest2=new ProductSkuRequest2();
            productSkuRequest2.setSkuCode(item.getSkuCode());
            productSkuRequest2.setBatchInfoCode(item.getBatchInfoCode());
            if(null==item.getWarehouseTypeCode()){
                item.setWarehouseTypeCode("1");
            }
            productSkuRequest2.setWarehouseTypeCode(item.getWarehouseTypeCode());

            List<String> warehouseCodes=new ArrayList<>();
            warehouseCodes.add(item.getWarehouseCode());
            productSkuRequest2.setWarehouseCodes(warehouseCodes);
            productSkuRequest2List.add(productSkuRequest2);

    }

        //缓存商品详情信息
        Map<String, ErpSkuDetail> skuDetailMap = bridgeProductService.getProductSkuDetailMap(wholesaleCustomer.getProvinceId(),wholesaleCustomer.getCityId(), productSkuRequest2List);

        //订单商品明细行
        List<ErpOrderItem> orderItemList = new ArrayList<>();
        //遍历参数商品列表，构建订单商品明细行
        for (ErpOrderCartInfo item :
                paramItemList) {
            ProductInfo productInfo = productMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());

            ErpSkuDetail skuDetail = skuDetailMap.get(item.getSkuCode()+"WAREHOUSE_TYPE_CODE"+item.getWarehouseTypeCode()+"BATCH_INFO_CODE"+item.getBatchInfoCode());
            if (item.getAmount() > skuDetail.getStockNum()) {
                throw new BusinessException("商品" + skuDetail.getSkuName() + "库存不足");
            }

            ErpOrderItem orderItem = new ErpOrderItem();
            //spu编码
            orderItem.setSpuCode(productInfo.getSpuCode());
            //spu名称
            orderItem.setSpuName(productInfo.getSpuName());
            //sku编码
            orderItem.setSkuCode(productInfo.getSkuCode());
            //sku名称
            orderItem.setSkuName(productInfo.getSkuName());
            //条形码
            orderItem.setBarCode(productInfo.getBarCode());
            //图片地址
            orderItem.setPictureUrl(productInfo.getPictureUrl());
            //规格
            orderItem.setProductSpec(productInfo.getProductSpec());
            //颜色编码
            orderItem.setColorCode(productInfo.getColorCode());
            //颜色名称
            orderItem.setColorName(productInfo.getColorName());
            //型号
            orderItem.setModelCode(productInfo.getModelCode());
            //单位编码
            orderItem.setUnitCode(productInfo.getUnitCode());
            //单位名称
            orderItem.setUnitName(productInfo.getUnitName());
            //折零系数 不存
            orderItem.setZeroDisassemblyCoefficient(null);
            //商品类型  0商品 1赠品
            orderItem.setProductType(ErpProductGiftEnum.PRODUCT.getCode());
            //商品属性编码
            orderItem.setProductPropertyCode(productInfo.getProductPropertyCode());
            //商品属性名称
            orderItem.setProductPropertyName(productInfo.getProductPropertyName());
            orderItem.setProductBrandCode(productInfo.getProductBrandCode());
            orderItem.setProductBrandName(productInfo.getProductBrandName());
            orderItem.setProductCategoryCode(productInfo.getProductCategoryCode());
            orderItem.setProductCategoryName(productInfo.getProductPropertyName());
            //供应商编码
            orderItem.setSupplierCode(productInfo.getSupplierCode());
            //供应商名称
            orderItem.setSupplierName(productInfo.getSupplierName());
            //商品数量
            orderItem.setProductCount((long)item.getAmount());
            //商品单价
            orderItem.setProductAmount(item.getPrice());
            //商品单价
            orderItem.setActivityPrice(item.getActivityPrice());
            //商品总价
            orderItem.setTotalProductAmount(item.getLineActivityAmountTotal());
            //优惠分摊总金额
            orderItem.setTotalPreferentialAmount(item.getLineAmountAfterActivity());
            //分摊后单价
            if(null!=item.getLineAmountAfterActivity()){
                orderItem.setPreferentialAmount(item.getLineAmountAfterActivity().divide(new BigDecimal(item.getAmount()),2, RoundingMode.DOWN));
            }else{
                orderItem.setPreferentialAmount(BigDecimal.ZERO);
            }
            //活动优惠总金额
            orderItem.setTotalAcivityAmount(item.getLineActivityDiscountTotal());
            //税率
            orderItem.setTaxRate(productInfo.getTaxRate());
            //公司编码
            orderItem.setCompanyCode(wholesaleCustomer.getCompanyCode());
            //公司名称
            orderItem.setCompanyName(wholesaleCustomer.getCompanyName());
            //单个商品毛重(kg)
            orderItem.setBoxGrossWeight(productInfo.getBoxGrossWeight());
            //单个商品包装体积(mm³)
            orderItem.setBoxVolume(productInfo.getBoxVolume());
            orderItem.setIsActivity(YesOrNoEnum.NO.getCode());

            //采购价
            orderItem.setPurchaseAmount(productInfo.getPurchaseAmount());

            orderItemList.add(orderItem);
        }

        return orderItemList;
    }

    /**
     * 构建和保存批发订单数据
     *
     * @param erpOrderSaveRequest 入口参数
     * @param orderItemList       订单商品明细行
     * @param wholesaleCustomers           批发客户信息
     * @param auth                操作人信息
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/11/27 17:50
     */
    private String generateAndSaveWholesaleOrder(ErpOrderSaveRequest erpOrderSaveRequest, List<ErpOrderItem> orderItemList, WholesaleCustomers wholesaleCustomers, AuthToken auth) {

        //生成订单id
        String orderId = OrderPublic.getUUID();
        //生成费用id
        String feeId = OrderPublic.getUUID();
        //生成订单code
        String orderCode = sequenceGeneratorService.generateOrderCode();
        //初始支付状态
        ErpPayStatusEnum payStatusEnum = ErpPayStatusEnum.UNPAID;

        //订货金额汇总
        BigDecimal moneyTotal = BigDecimal.ZERO;
        //商品毛重(kg)
        BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
        //商品包装体积(mm³)
        BigDecimal boxVolumeTotal = BigDecimal.ZERO;

        long lineCode = 1L;

        //遍历商品订单行，汇总价格
        for (ErpOrderItem item :
                orderItemList) {

            //订单id
            item.setOrderStoreId(orderId);
            //订单编码
            item.setOrderStoreCode(orderCode);
            //订单明细id
            item.setOrderInfoDetailId(OrderPublic.getUUID());
            //订单明细行编号
            item.setLineCode(lineCode++);
            item.setUseStatus(StatusEnum.YES.getValue());

            //金额汇总
            moneyTotal = moneyTotal.add(item.getTotalProductAmount() == null ? BigDecimal.ZERO : item.getTotalProductAmount());
            //商品毛重汇总
            boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(item.getProductCount())));
            //商品体积汇总
            boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(item.getProductCount())));
        }

        erpOrderItemService.saveOrderItemList(orderItemList, auth);

        ErpOrderInfo order = new ErpOrderInfo();
        //业务id
        order.setOrderStoreId(orderId);
        //订单编码
        order.setOrderStoreCode(orderCode);
        //公司编码
        order.setCompanyCode(wholesaleCustomers.getCompanyCode());
        //公司名称
        order.setCompanyName(wholesaleCustomers.getCompanyName());
        //订单类型编码 0直送、1配送、2辅采直送
        order.setOrderTypeCode(erpOrderSaveRequest.getOrderType().toString());
        //订单类型名称
        order.setOrderTypeName(ErpOrderTypeEnum.getEnumDesc(erpOrderSaveRequest.getOrderType()));
        //订单类别编码
        order.setOrderCategoryCode(erpOrderSaveRequest.getOrderCategory().toString());
        //订单类别名称
        order.setOrderCategoryName(ErpOrderCategoryEnum.getEnumDesc(erpOrderSaveRequest.getOrderCategory()));
        //客户编码
        order.setCustomerCode(wholesaleCustomers.getCustomerCode());
        //客户名称
        order.setCustomerName(wholesaleCustomers.getCustomerName());
        //订单状态
        order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_1.getCode());
        //订单节点流程状态
        order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_1.getCode());
        //是否锁定(0是1否）
        order.setOrderLock(StatusEnum.NO.getCode());
        //是否是异常订单(0是1否)
        order.setOrderException(StatusEnum.NO.getCode());
        //是否删除(0是1否)
        order.setOrderDelete(StatusEnum.NO.getCode());
        //支付状态0已支付  1未支付
        order.setPaymentStatus(StatusEnum.NO.getCode());

        //收货区域 :省编码
        order.setProvinceId(wholesaleCustomers.getProvinceId());
        //收货区域 :省
        order.setProvinceName(wholesaleCustomers.getProvinceName());
        //收货区域 :市编码
        order.setCityId(wholesaleCustomers.getCityId());
        //收货区域 :市
        order.setCityName(wholesaleCustomers.getCityName());
        //收货区域 :区县编码
        order.setDistrictId(wholesaleCustomers.getDistrictId());
        //收货区域 :区县
        order.setDistrictName(wholesaleCustomers.getDistrictName());

        //收货地址
        if(StringUtils.isNotBlank(erpOrderSaveRequest.getReceiveAddress())){
            order.setReceiveAddress(erpOrderSaveRequest.getReceiveAddress());
        }else{
            order.setReceiveAddress(wholesaleCustomers.getStreetAddress());
        }
        //收货人
        if(StringUtils.isNotBlank(erpOrderSaveRequest.getReceivePerson())){
            order.setReceivePerson(erpOrderSaveRequest.getReceivePerson());
        }else{
            order.setReceivePerson(wholesaleCustomers.getCustomerName());
        }
        //收货人电话
        if(StringUtils.isNotBlank(erpOrderSaveRequest.getReceiveMobile())){
            order.setReceiveMobile(erpOrderSaveRequest.getReceiveMobile());
        }else{
            order.setReceiveMobile(wholesaleCustomers.getPhoneNumber());
        }
        //商品总价
        order.setTotalProductAmount(moneyTotal);
        //实际商品总价
        order.setActualTotalProductAmount(moneyTotal);
        //优惠额度
        order.setDiscountAmount(BigDecimal.ZERO);
        //实付金额
        order.setOrderAmount(moneyTotal);
        //发运状态
        order.setTransportStatus(StatusEnum.NO.getCode());
        //发票类型
        order.setInvoiceType(ErpInvoiceTypeEnum.NO_INVOICE.getCode());
        //体积
        order.setTotalVolume(boxVolumeTotal);
        //重量
        order.setTotalWeight(boxGrossWeightTotal);
        //主订单号  如果非子订单 此处存order_code
        order.setMainOrderCode(orderCode);
        //订单级别(0主1子订单)
        order.setOrderLevel(ErpOrderLevelEnum.PRIMARY.getCode());
        //是否被拆分 (0是 1否)
        order.setSplitStatus(StatusEnum.NO.getCode());
        //费用id
        order.setFeeId(feeId);
        //退货状态
        order.setOrderReturn(ErpOrderReturnStatusEnum.NONE.getCode());
        //退货流程状态
        order.setOrderReturnProcess(StatusEnum.NO.getCode());
        //同步供应链状态
        order.setOrderSuccess(OrderSucessEnum.ORDER_SYNCHRO_NO.getCode());
        //生成冲减单状态
        order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.NOT_NEED.getCode());
        //门店id
        order.setStoreId(wholesaleCustomers.getCustomerCode());
        //门店编码
        order.setStoreCode(wholesaleCustomers.getCustomerAccount());
        //门店名称
        order.setStoreName(wholesaleCustomers.getCustomerName());
        //加盟商id
        order.setFranchiseeId(wholesaleCustomers.getCustomerCode());
        //加盟商编码
        order.setFranchiseeCode(wholesaleCustomers.getCustomerAccount());
        //加盟商名称
        order.setFranchiseeName(wholesaleCustomers.getCustomerName());
        //是否活动商品0否1是
        order.setIsActivity(YesOrNoEnum.NO.getCode());

        //判断是否减免物流费用
        int logisticsFeeStatus=getLogisticsFeeStatus(orderItemList);
        order.setLogisticsFeeStatus(logisticsFeeStatus);

        //销售单增加合伙人公司字段
        CopartnerAreaVo copartnerAreaVo=new CopartnerAreaVo();
        copartnerAreaVo.setCopartnerAreaCompany(wholesaleCustomers.getAffiliatedCompany());
        List<CopartnerAreaList> copartnerAreaList=copartnerAreaDao.copartnerAreaList(copartnerAreaVo);
        if(null!=copartnerAreaList &&copartnerAreaList.size()>0){
            order.setCopartnerAreaId(copartnerAreaList.get(0).getCopartnerAreaId());
            order.setCopartnerAreaName(copartnerAreaList.get(0).getCopartnerAreaName());
            order.setCopartnerAreaCode(copartnerAreaList.get(0).getId());
        }
        //业务形式 1 批发
        order.setBusinessForm(1);
        erpOrderInfoService.saveOrder(order, auth);

        //保存订单费用信息
        ErpOrderFee orderFee = new ErpOrderFee();
        //费用id
        orderFee.setFeeId(feeId);
        //订单id
        orderFee.setOrderId(orderId);
        //支付单id
        orderFee.setPayId(null);
        //支付状态
        orderFee.setPayStatus(payStatusEnum.getCode());
        //订单总额
        orderFee.setTotalMoney(moneyTotal);
        //活动优惠金额
        orderFee.setActivityMoney(BigDecimal.ZERO);
        //服纺券优惠金额
        orderFee.setSuitCouponMoney(BigDecimal.ZERO);
        //A品券优惠金额
        orderFee.setTopCouponMoney(BigDecimal.ZERO);
        //实付金额
        orderFee.setPayMoney(orderFee.getTotalMoney().subtract(orderFee.getActivityMoney()).subtract(orderFee.getSuitCouponMoney()).subtract(orderFee.getTopCouponMoney()));
        erpOrderFeeService.saveOrderFee(orderFee, auth);

        return orderCode;
    }

    private int getLogisticsFeeStatus(List<ErpOrderItem> orderItemList) {
        LogisticsRuleRequest logisticsRuleRequest=new LogisticsRuleRequest();
        List<LogisticsAllResponse> logisticsAllResponseList =(List<LogisticsAllResponse>)logisticsRuleService.selectLogisticsList(logisticsRuleRequest).getData().getResult();
        if(null!=orderItemList&&orderItemList.size()>0&&null!=logisticsAllResponseList && logisticsAllResponseList.size()>0){
            for(LogisticsAllResponse logisticsAllResponse:logisticsAllResponseList){
                //根据活动类型解析活动
                switch (logisticsAllResponse.getRultType()) {
                    case 0:
                        //单品购买数量

                        for (ErpOrderItem item : orderItemList) {
                            //判断订单里是否存在单品
                            if(logisticsAllResponse.getProductCode().equals(item.getSkuCode())){
                                //判断是否满足条件
                                if(new BigDecimal(item.getProductCount()).compareTo(logisticsAllResponse.getFareSill())>-1){
                                    return 1;
                                }
                            }
                        }
                        break;
                    case 1:
                        //单品购买金额

                        for (ErpOrderItem item : orderItemList) {
                            //判断订单里是否存在单品
                            if(logisticsAllResponse.getProductCode().equals(item.getSkuCode())){
                                //计算单品总金额
                                BigDecimal totalProductAmount=new BigDecimal(item.getProductCount()).multiply(item.getProductAmount());
                                //判断是否满足条件
                                if(totalProductAmount.compareTo(logisticsAllResponse.getFareSill())>-1){
                                    return 1;
                                }
                            }
                        }
                        break;
                    case 2:
                        //累计购买数量

                        Long count=0L;
                        String[] skuList= logisticsAllResponse.getProductCode().split(",");
                        Map map =new HashMap();
                        for(String skuCode:skuList){
                            map.put(skuCode,skuCode);
                        }

                        for (ErpOrderItem item : orderItemList) {
                            //判断订单里是否存在规则配置里的sku
                            if(map.containsKey(item.getSkuCode())){
                                //计算满足条件sku总数量
                                count+=item.getProductCount();
                            }
                        }

                        //判断是否满足条件
                        if(new BigDecimal(count).compareTo(logisticsAllResponse.getFareSill())>-1){
                            return 1;
                        }

                        break;
                    case 3:
                        //累计购买金额

                        BigDecimal price=BigDecimal.ZERO;
                        String[] skuCodes= logisticsAllResponse.getProductCode().split(",");
                        Map dataMap =new HashMap();
                        for(String skuCode:skuCodes){
                            dataMap.put(skuCode,skuCode);
                        }

                        for (ErpOrderItem item : orderItemList) {
                            //判断订单里是否存在规则配置里的sku
                            if(dataMap.containsKey(item.getSkuCode())){
                                //计算满足条件sku总金额
                                price=price.add(new BigDecimal(item.getProductCount()).multiply(item.getProductAmount()));
                            }
                        }

                        //判断是否满足条件
                        if(price.compareTo(logisticsAllResponse.getFareSill())>-1){
                            return 1;
                        }
                        break;
                    default:
                        ;
                }

            }
        }
        return 0;
    }

    public static void main(String[] args) {
        LogisticsRuleServiceImpl logisticsRuleService=new LogisticsRuleServiceImpl();
        LogisticsRuleRequest logisticsRuleRequest=new LogisticsRuleRequest();
        List<LogisticsAllResponse> logisticsAllResponseList =(List<LogisticsAllResponse>)logisticsRuleService.selectLogisticsList(logisticsRuleRequest).getData();
        System.out.println(JsonUtil.toJson(logisticsAllResponseList));
        System.out.println(JsonUtil.toJson(logisticsRuleService.selectLogisticsList(logisticsRuleRequest).getData()));
    }


}
