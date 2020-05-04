package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.*;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityRuleUnitEnum;
import com.aiqin.mgs.order.api.component.enums.pay.ErpPayStatusEnum;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityRule;
import com.aiqin.mgs.order.api.domain.AuthToken;
import com.aiqin.mgs.order.api.domain.po.gift.GiftQuotasUseDetail;
import com.aiqin.mgs.order.api.domain.po.gift.NewStoreGradient;
import com.aiqin.mgs.order.api.domain.po.gift.StoreGradient;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderFee;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderLogistics;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverItemRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderDeliverRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportLogisticsRequest;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderTransportRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.gift.GiftQuotasUseDetailService;
import com.aiqin.mgs.order.api.service.order.*;
import com.aiqin.mgs.order.api.util.OrderPublic;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ErpOrderDeliverServiceImpl implements ErpOrderDeliverService {

    @Resource
    private ErpOrderInfoService erpOrderInfoService;
    @Resource
    private ErpOrderQueryService erpOrderQueryService;
    @Resource
    private ErpOrderItemService erpOrderItemService;
    @Resource
    private ErpOrderLogisticsService erpOrderLogisticsService;
    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;
    @Resource
    private CouponRuleService couponRuleService;
    @Resource
    private ActivityService activityService;
    @Resource
    private ErpOrderFeeService erpOrderFeeService;
    @Autowired
    private ReturnOrderInfoDao returnOrderInfoDao;
    @Autowired
    private BridgeProductService bridgeProductService;
    @Resource
    private GiftQuotasUseDetailService giftQuotasUseDetailService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderDeliver(ErpOrderDeliverRequest erpOrderDeliverRequest) {

        if (erpOrderDeliverRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderDeliverRequest.getOrderCode())) {
            throw new BusinessException("缺少订单号");
        }
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderDeliverRequest.getPersonId());
        auth.setPersonName(erpOrderDeliverRequest.getPersonName());

        Map<Long, Long> paramLineQuantityMap = new HashMap<>(16);
        List<ErpOrderDeliverItemRequest> paramItemList = erpOrderDeliverRequest.getItemList();
        if (paramItemList == null || paramItemList.size() == 0) {
            throw new BusinessException("缺少发货数量明细");
        }
        for (ErpOrderDeliverItemRequest item :
                paramItemList) {
            paramLineQuantityMap.put(item.getLineCode(), item.getActualProductCount());
        }


        ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(erpOrderDeliverRequest.getOrderCode());
        if (order == null) {
            throw new BusinessException("无效的订单号");
        }

        boolean flag = false;
        if (order.getOrderTypeCode().equals(ErpOrderTypeEnum.DIRECT_SEND.getValue())) {
            flag = true;
        }
        if (ErpOrderStatusEnum.ORDER_STATUS_6.getCode().equals(order.getOrderStatus())) {
            if (ErpOrderNodeStatusEnum.STATUS_8.getCode().equals(order.getOrderNodeStatus())) {
                    flag = true;
            }
        }
        if (flag) {
            List<ErpOrderItem> itemList = erpOrderItemService.selectOrderItemListByOrderId(order.getOrderStoreId());
            //实发数量
            Long totalActualProductCount = 0L;
            //实发均摊金额
            BigDecimal actualTotalProductAmount = BigDecimal.ZERO;

            //商品毛重(kg)
            BigDecimal boxGrossWeightTotal = BigDecimal.ZERO;
            //商品包装体积(mm³)
            BigDecimal boxVolumeTotal = BigDecimal.ZERO;

            //是否需要标记生成冲减单
            boolean scourSheetFlag = false;

            for (ErpOrderItem item :
                    itemList) {
                //行订货数量
                Long productCount = item.getProductCount();
                //行发货数量
                Long actualProductCount = paramLineQuantityMap.get(item.getLineCode());
                if (actualProductCount == null) {
                    throw new BusinessException("缺少行号为" + item.getLineCode() + "的发货数量");
                }
                if (actualProductCount < 0) {
                    throw new BusinessException("发货数量不能小于0");
                }
                if (actualProductCount > productCount) {
                    throw new BusinessException("发货数量不能超过订货数量");
                }
                if (actualProductCount < productCount) {
                    scourSheetFlag = true;
                }
                item.setActualProductCount(actualProductCount);
                totalActualProductCount += actualProductCount;

                //行实际发货商品均摊金额
                BigDecimal actualProductAmount = BigDecimal.ZERO;
                if (productCount.equals(actualProductCount)) {
                    actualProductAmount = item.getTotalPreferentialAmount();
                } else {
                    actualProductAmount = new BigDecimal(actualProductCount).multiply(item.getPreferentialAmount());
                }
                actualTotalProductAmount = actualTotalProductAmount.add(actualProductAmount);

                //设置单个商品实际发货金额
                item.setActualTotalProductAmount(actualProductAmount);

                //商品毛重汇总
                boxGrossWeightTotal = boxGrossWeightTotal.add((item.getBoxGrossWeight() == null ? BigDecimal.ZERO : item.getBoxGrossWeight()).multiply(new BigDecimal(actualProductCount)));
                //商品体积汇总
                boxVolumeTotal = boxVolumeTotal.add((item.getBoxVolume() == null ? BigDecimal.ZERO : item.getBoxVolume()).multiply(new BigDecimal(actualProductCount)));

            }
            erpOrderItemService.updateOrderItemList(itemList, auth);

            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_9.getCode());
            order.setActualProductCount(totalActualProductCount);
            order.setActualTotalProductAmount(actualTotalProductAmount);
            order.setDeliveryTime(erpOrderDeliverRequest.getDeliveryTime());
            order.setActualTotalVolume(boxVolumeTotal);
            order.setActualTotalWeight(boxGrossWeightTotal);
            if (scourSheetFlag) {
                order.setScourSheetStatus(ErpOrderScourSheetStatusEnum.WAIT.getCode());
            }
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
            //todo 更新主单明细赠品的实际发货数量
            updateGiftGoodsAutCount(order.getMainOrderCode(),itemList,auth);

        } else {
            throw new BusinessException("只有等待拣货状态且没有出货的订单才能执行该操作");
        }


    }

    /**
     * 根据主订单号修改赠品实发数量
     * @param mainOrderCode
     * @param itemList
     */
    private void updateGiftGoodsAutCount(String mainOrderCode,List<ErpOrderItem> itemList ,AuthToken auth){
        log.info("根据主订单号修改赠品实发数量,mainOrderCode={}",mainOrderCode);
        log.info("根据主订单号修改赠品实发数量,itemList={}", JSON.toJSONString(itemList));
        log.info("根据主订单号修改赠品实发数量,auth={}",auth);
        List<ErpOrderItem> items=new ArrayList<>();
        Map<String,Long> map=new HashMap<>();
        for(ErpOrderItem eoi:itemList){
            //挑选出赠品明细行
            if(null!=eoi.getProductType()&&!eoi.getProductType().equals(ErpProductGiftEnum.PRODUCT.getCode())){
                map.put(eoi.getSkuCode(),eoi.getActualProductCount());
            }
        }
        log.info("根据主订单号修改赠品实发数量,赠品 map={}",map);
        //判断是否有赠品，如果有则进行后面的数量修改
        if(null!=map&&map.size()>0){
            //根据主订单编码，查询主订单明细，进行赠品数量修改
            List<ErpOrderItem> erpOrderItemList = erpOrderItemService.selectOrderItemListByOrderCode(mainOrderCode);
            for(ErpOrderItem erpOrderItem:erpOrderItemList){
                //挑选出赠品明细行
                if(null!=erpOrderItem.getProductType()&&!erpOrderItem.getProductType().equals(ErpProductGiftEnum.PRODUCT.getCode())&&map.containsKey(erpOrderItem.getSkuCode())){
                    //根据sku拿到赠品实发数量
                    Long count=map.get(erpOrderItem.getSkuCode());
                    if(null!=erpOrderItem.getActualProductCount()){
                        count=count+erpOrderItem.getActualProductCount();
                    }
                    erpOrderItem.setActualProductCount(count);
                    items.add(erpOrderItem);
                }
            }
            log.info("根据主订单号修改赠品实发数量,需修改的明细集合 items={}",JSON.toJSONString(items));
            //进行赠品实发数量修改
            erpOrderItemService.updateOrderItemList(itemList, auth);
            log.info("根据主订单号修改赠品实发数量,成功");
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderTransport(ErpOrderTransportRequest erpOrderTransportRequest) {

        if (erpOrderTransportRequest == null) {
            throw new BusinessException("空参数");
        }
        if (StringUtils.isEmpty(erpOrderTransportRequest.getPersonId())) {
            throw new BusinessException("缺失操作人id");
        }
        if (StringUtils.isEmpty(erpOrderTransportRequest.getPersonName())) {
            throw new BusinessException("缺失操作人名称");
        }
        //操作人
        AuthToken auth = new AuthToken();
        auth.setPersonId(erpOrderTransportRequest.getPersonId());
        auth.setPersonName(erpOrderTransportRequest.getPersonName());

        //物流信息参数
        ErpOrderTransportLogisticsRequest logistics = erpOrderTransportRequest.getLogistics();

        //物流信息
        ErpOrderLogistics orderLogistics = null;
        if (logistics != null && StringUtils.isNotEmpty(logistics.getLogisticsCode())) {
            if (StringUtils.isEmpty(logistics.getLogisticsCentreCode())) {
                throw new BusinessException("缺失物流公司编号");
            }
            if (StringUtils.isEmpty(logistics.getLogisticsCentreName())) {
                throw new BusinessException("缺失物流公司名称");
            }
            if (StringUtils.isEmpty(logistics.getSendRepertoryCode())) {
                throw new BusinessException("缺失发货仓库编码");
            }
            if (StringUtils.isEmpty(logistics.getSendRepertoryName())) {
                throw new BusinessException("缺失发货仓库名称");
            }
            if (logistics.getLogisticsFee() == null) {
                throw new BusinessException("缺失物流费用");
            }
            orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logistics.getLogisticsCode());
            if (orderLogistics == null) {
                //新的物流信息，先保存物流信息

                ErpOrderLogistics newOrderLogistics = new ErpOrderLogistics();
                newOrderLogistics.setLogisticsId(OrderPublic.getUUID());
                newOrderLogistics.setLogisticsCode(logistics.getLogisticsCode());
                newOrderLogistics.setLogisticsCentreCode(logistics.getLogisticsCentreCode());
                newOrderLogistics.setLogisticsCentreName(logistics.getLogisticsCentreName());
                newOrderLogistics.setSendRepertoryCode(logistics.getSendRepertoryCode());
                newOrderLogistics.setSendRepertoryName(logistics.getSendRepertoryName());
                newOrderLogistics.setLogisticsFee(logistics.getLogisticsFee());
                newOrderLogistics.setPayStatus(ErpPayStatusEnum.UNPAID.getCode());
                erpOrderLogisticsService.saveOrderLogistics(newOrderLogistics, auth);
                orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logistics.getLogisticsCode());
            } else {
                if (orderLogistics.getLogisticsFee().compareTo(logistics.getLogisticsFee()) != 0) {
                    throw new BusinessException("物流单费用与已有物流单费用不相等");
                }
                if (!orderLogistics.getLogisticsCentreCode().equals(logistics.getLogisticsCentreCode())) {
                    throw new BusinessException("物流公司编码与已有物流单物流公司编码不相同");
                }
                if (!orderLogistics.getLogisticsCentreName().equals(logistics.getLogisticsCentreName())) {
                    throw new BusinessException("物流公司名称与已有物流单物流公司名称不相同");
                }
                if (!orderLogistics.getSendRepertoryCode().equals(logistics.getSendRepertoryCode())) {
                    throw new BusinessException("发货仓库编码与已有物流单发货仓库编码不相同");
                }
                if (!orderLogistics.getSendRepertoryName().equals(logistics.getSendRepertoryName())) {
                    throw new BusinessException("发货仓库名称与已有物流单发货仓库名称不相同");
                }
            }
        }

        List<String> orderCodeList = erpOrderTransportRequest.getOrderCodeList();
        if (orderCodeList == null || orderCodeList.size() <= 0) {
            throw new BusinessException("缺失发运订单号");
        }

        //订单类型
        ErpOrderTypeEnum orderTypeEnum = null;
        //加盟商id
        String franchiseeId = null;

        for (String orderCode :
                orderCodeList) {

            if (StringUtils.isEmpty(orderCode)) {
                throw new BusinessException("空订单号");
            }
            ErpOrderInfo order = erpOrderQueryService.getOrderByOrderCode(orderCode);
            if (order == null) {
                throw new BusinessException("无效的订单号" + orderCode);
            }
            if (StringUtils.isEmpty(franchiseeId)) {
                franchiseeId = order.getFranchiseeId();
            } else {
                if (!franchiseeId.equals(order.getFranchiseeId())) {
                    throw new BusinessException("每次发运只能同时发运同一个加盟商的订单");
                }
            }
            if (orderTypeEnum == null) {
                orderTypeEnum = ErpOrderTypeEnum.getEnum(order.getOrderTypeCode());
            } else {
                if (!orderTypeEnum.getValue().equals(order.getOrderTypeCode())) {
                    throw new BusinessException("每次发运只能同时发运相同类型的订单");
                }
            }
            if (!ErpOrderNodeStatusEnum.STATUS_9.getCode().equals(order.getOrderNodeStatus())) {
                throw new BusinessException("不是待发运的订单不能发运");
            }

            ErpOrderNodeProcessTypeEnum processTypeEnum = ErpOrderNodeProcessTypeEnum.getEnum(order.getOrderTypeCode(), order.getOrderCategoryCode());
            if (processTypeEnum == null) {
                throw new BusinessException("订单类型数据异常");
            }
            if (orderLogistics != null) {
                order.setLogisticsId(orderLogistics.getLogisticsId());
                order.setTransportCompanyCode(orderLogistics.getLogisticsCentreCode());
                order.setTransportCompanyName(orderLogistics.getLogisticsCentreName());
                order.setTransportCode(orderLogistics.getLogisticsCode());
            } else {
                if (processTypeEnum.isHasLogisticsFee()) {
                    //如果物流单信息为空但是订单类型必须要物流单信息
                    throw new BusinessException("订单" + orderCode + "必须关联物流信息");
                }
            }

            order.setTransportStatus(erpOrderTransportRequest.getTransportStatus());
            order.setTransportTime(erpOrderTransportRequest.getTransportTime());
            order.setDistributionModeCode(erpOrderTransportRequest.getDistributionModeCode());
            order.setDistributionModeName(erpOrderTransportRequest.getDistributionModeName());
            order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_11.getCode());
            order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_10.getCode());
            erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
            //todo 判断是否全部发货，然后做均摊
            //查询子单是否全部发货完成
            Boolean aBoolean = checkSendOk(order.getMainOrderCode());
            log.info("判断子单是否全部发货完成,返回结果 aBoolean={}",aBoolean);
            if(aBoolean){//全部发货完成
                //三步均摊
                shareEqually(order.getMainOrderCode());
                //遍历退货单，查看是否有退单
                ErpOrderInfo e=new ErpOrderInfo();
                e.setMainOrderCode(order.getMainOrderCode());
                List<ErpOrderInfo> orderList = erpOrderQueryService.select(e);
                //如果子订单中有退货情况，现在更想预生成的退货单的 是否发起退货状态
                if(orderList!=null&&orderList.size()>0){
                    //遍历出所有的子订单号
                    List<String> orderCodes=orderList.stream().map(ErpOrderInfo::getOrderStoreCode).collect(Collectors.toList());
                    log.info("修改订单为发货状态---全部发货完成,修改退货单是否发起退货状态，入参orderCodes={}",JSON.toJSONString(orderCodes));
                    if(null!=orderCodes&&orderCodes.size()>0){
                        returnOrderInfoDao.updateReallyReturn(orderCodes);
                        log.info("修改订单为发货状态---全部发货完成,修改退货单是否发起退货状态，成功");
                    }
                }
            }

            boolean flag=false;
            //这两个都走线下支付物流费用
            if(ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(order.getOrderTypeCode())){//配送
                //首单或首单赠送
                if(ErpOrderCategoryEnum.ORDER_TYPE_2.getValue().equals(order.getOrderCategoryCode())||ErpOrderCategoryEnum.ORDER_TYPE_4.getValue().equals(order.getOrderCategoryCode())){
                    flag=true;
                }
            }

            if (!processTypeEnum.isHasLogisticsFee()) {
                //不需要支付物流费用的订单
                order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
            } else {
                if ((orderLogistics != null && ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus()))||flag) {
                    order.setOrderStatus(ErpOrderStatusEnum.ORDER_STATUS_12.getCode());
                    order.setOrderNodeStatus(ErpOrderNodeStatusEnum.STATUS_11.getCode());
                    erpOrderInfoService.updateOrderByPrimaryKeySelective(order, auth);
                }
            }
        }

        if (orderLogistics != null) {
            //均摊物流费用
            this.distributeLogisticsFee(orderLogistics.getLogisticsCode());
        }
    }

    /**
     * 子单全部发货完后进行均摊
     * @param mainOrderCode
     */
    private void shareEqually(String mainOrderCode){
        log.info("子单全部发货完成进行均摊--入参 mainOrderCode={}",mainOrderCode);
        ErpOrderInfo orderAndItemByOrderCode = erpOrderQueryService.getOrderAndItemByOrderCode(mainOrderCode);
        log.info("子单全部发货完成进行均摊--查询原始主订单及详情返回结果 orderAndItemByOrderCode={}",JSON.toJSONString(orderAndItemByOrderCode));
        /******************挑选活动和可用A品券商品明细**********************/
        List<ErpOrderItem> itemList = orderAndItemByOrderCode.getItemList();

        //活动id:商品明细行
        Map<String, List<ErpOrderItem>> activityCartMap = new HashMap<>();
        //活动id:商品总金额
        Map<String, BigDecimal> activityMoneyMap = new HashMap<>();
        //活动id:商品总价值
        Map<String, BigDecimal> activityPriceMoneyMap = new HashMap<>();
        //活动id:商品数量 用于活动
        Map<String, Integer> quantityMap = new HashMap<>();
        //筛选出活动明细行
        for(ErpOrderItem item:itemList){
            //商品类型  0商品 1赠品
            Integer productType = item.getProductType();
            //商品总金额=活动价X订货数量（赠品的话：活动价X发货数量）
            BigDecimal totalMoney=BigDecimal.ZERO;
            //商品总价值=分销单价X订货数量
            BigDecimal totalPriceMoney=BigDecimal.ZERO;
            //商品数量
            Integer quantity = 0;
            //活动id:商品明细行 添加数据
                if (null!=item.getActivityId()&&activityCartMap.containsKey(item.getActivityId())) {
                List<ErpOrderItem> items = activityCartMap.get(item.getActivityId());
                items.add(item);
                activityCartMap.put(item.getActivityId(),items);
                if(null!=activityMoneyMap.get(item.getActivityId())){
                    totalMoney=activityMoneyMap.get(item.getActivityId());
                }
                //判断是否为赠品
                if(ErpProductGiftEnum.GIFT.getCode().equals(productType)){
                    //赠品使用实发数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getActualProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }else{
                    //商品用订货数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }
                activityMoneyMap.put(item.getActivityId(),totalMoney);

                if(null!=activityPriceMoneyMap.get(item.getActivityId())){
                    totalPriceMoney=activityPriceMoneyMap.get(item.getActivityId());
                }
                BigDecimal multiply1 = item.getProductAmount().multiply(new BigDecimal(item.getProductCount()));
                totalPriceMoney=totalPriceMoney.add(multiply1);
                activityPriceMoneyMap.put(item.getActivityId(),totalPriceMoney);

                if(null!=item.getActivityId()){
                    quantity=quantityMap.get(item.getActivityId());
                    if(null==quantity){
                        quantity=0;
                    }
                }
                quantity=quantity+item.getProductCount().intValue();
                quantityMap.put(item.getActivityId(),quantity);
            }else {
                List<ErpOrderItem> items1 =new ArrayList<>();
                items1.add(item);
                activityCartMap.put(item.getActivityId(),items1);
                if(null!=activityMoneyMap.get(item.getActivityId())){
                    totalMoney=activityMoneyMap.get(item.getActivityId());
                }
                if(ErpProductGiftEnum.GIFT.getCode().equals(productType)){
                    //赠品使用实发数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getActualProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }else{
                    //商品用订货数量
                    BigDecimal multiply = item.getActivityPrice().multiply(new BigDecimal(item.getProductCount()));
                    totalMoney=totalMoney.add(multiply);
                }
                activityMoneyMap.put(item.getActivityId(),totalMoney);

                BigDecimal multiply1 = item.getProductAmount().multiply(new BigDecimal(item.getProductCount()));
                if(null!=activityPriceMoneyMap.get(item.getActivityId())){
                    totalPriceMoney=activityPriceMoneyMap.get(item.getActivityId());
                }
                totalPriceMoney=totalPriceMoney.add(multiply1);
                activityPriceMoneyMap.put(item.getActivityId(),totalPriceMoney);
            }
        }
        log.info("子单全部发货完成进行均摊--活动id:商品明细行 activityCartMap={}",activityCartMap);
        log.info("子单全部发货完成进行均摊--活动id:商品金额 activityMoneyMap={}",activityMoneyMap);
        log.info("子单全部发货完成进行均摊--活动id:商品价值 activityPriceMoneyMap={}",activityPriceMoneyMap);
        log.info("子单全部发货完成进行均摊--活动id:商品数量 quantityMap={}",quantityMap);
        /******************商品挑选完成进行活动均摊**********************/
        //记录活动分摊后的数据
        Map<String,ErpOrderItem> activityAfterMap=new HashMap<>();
        //活动均摊
        for(Map.Entry<String, List<ErpOrderItem>> m:activityCartMap.entrySet()){
            HttpResponse<ActivityRequest> activityDetailResponse = activityService.getActivityDetail(m.getKey());
            ActivityRequest activityRequest=activityDetailResponse.getData();
            Activity activity = activityRequest.getActivity();
            List<ActivityRule> activityRules = activityRequest.getActivityRules();
            /**活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单*/
            Integer activityType = activity.getActivityType();
            String activityId= m.getKey();
            List<ErpOrderItem> li=m.getValue();
            //商品总金额
            BigDecimal productAmount=activityMoneyMap.get(activityId);
            //商品总价值
            BigDecimal productPriceAmount=activityPriceMoneyMap.get(activityId);
            //缓存当前命中的规则
            ActivityRule curRule = null;
            //最小梯度
            ActivityRule firstRule = null;
            //商品数量
            Integer quantity = quantityMap.get(activityId);
            //优惠金额
            BigDecimal youHuiAmount=BigDecimal.ZERO;
            switch (activityType){
                case 1:
                    for(ActivityRule ruleItem:activityRules){

                        //筛选最小梯度
                        if (firstRule == null || ruleItem.getMeetingConditions().compareTo(firstRule.getMeetingConditions()) < 0) {
                            firstRule = ruleItem;
                        }

                        //是否把当前梯度作为命中梯度
                        boolean flag = false;

                        if (ActivityRuleUnitEnum.BY_NUM.getCode().equals(ruleItem.getRuleUnit())) {
                            //按照数量

                            if (ruleItem.getMeetingConditions().compareTo(new BigDecimal(quantity)) <= 0) {
                                if (curRule == null) {
                                    flag = true;
                                } else {
                                    if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                        flag = true;
                                    }
                                }
                            }

                        } else if (ActivityRuleUnitEnum.BY_MONEY.getCode().equals(ruleItem.getRuleUnit())) {
                            //按照金额

                            if (ruleItem.getMeetingConditions().compareTo(productPriceAmount) <= 0) {
                                if (curRule == null) {
                                    flag = true;
                                } else {
                                    if (ruleItem.getMeetingConditions().compareTo(curRule.getMeetingConditions()) > 0) {
                                        flag = true;
                                    }
                                }
                            }

                        } else {

                        }

                        if (flag) {
                            curRule = ruleItem;
                        }

                    }
                    if (curRule != null) {//命中规则
                        youHuiAmount=curRule.getPreferentialAmount();
                        //满减活动商品总金额=商品总金额-优惠金额
                        productAmount=productAmount.subtract(youHuiAmount);
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;

            }
            for(ErpOrderItem eo:li){
                //分销总价=商品价值
                BigDecimal fenxiaozongjia=eo.getProductAmount().multiply(new BigDecimal(eo.getProductCount()));
                //分摊总额=商品价值X商品总金额/商品总价值
                BigDecimal totalPreferentialAmount=fenxiaozongjia.multiply(productAmount).divide(productPriceAmount,2,BigDecimal.ROUND_HALF_UP);
                //分摊单价
                BigDecimal preferentialAmount=totalPreferentialAmount.divide(new BigDecimal(eo.getProductCount()),2,BigDecimal.ROUND_HALF_UP);
                eo.setTotalPreferentialAmount(totalPreferentialAmount);
                eo.setPreferentialAmount(preferentialAmount);
                activityAfterMap.put(eo.getOrderInfoDetailId(),eo);
            }
        }
        log.info("子单全部发货完成进行均摊--活动分摊--活动分摊后的数据 activityAfterMap={}",activityAfterMap);
        //遍历原始明细行，将活动分摊后的数据填充进去
        for(ErpOrderItem e:itemList){
            String orderInfoDetailId = e.getOrderInfoDetailId();
            if(null!=activityAfterMap.get(orderInfoDetailId)){
                ErpOrderItem o= activityAfterMap.get(orderInfoDetailId);
                e.setTotalPreferentialAmount(o.getTotalPreferentialAmount());
                e.setPreferentialAmount(o.getPreferentialAmount());
            }
        }
        log.info("子单全部发货完成进行均摊--活动分摊--分摊后 分摊后原始明细行集合 itemList={}",JSON.toJSONString(itemList));
        /************************************A品券均摊**********************************/
        //A品券商品明细行
        List<ErpOrderItem> list = new ArrayList<>();
        //符合分摊A品卷规则的商品
        Map map = couponRuleService.couponRuleMap();
        //商品总价值
        BigDecimal priceAmount=BigDecimal.ZERO;
        //商品总金额
        BigDecimal amount=BigDecimal.ZERO;
        String orderId="";
        for(ErpOrderItem o:itemList){
            //商品属性编码
            String productPropertyCode = o.getProductPropertyCode();
            //商品类型  0商品 1赠品
            Integer productType = o.getProductType();
            //判断是否是A品券商品
            if(null!=productPropertyCode&&map.containsKey(productPropertyCode)&&null!=productType&&productType.equals(ErpProductGiftEnum.PRODUCT.getCode())){
                list.add(o);
                priceAmount=priceAmount.add(o.getTotalPreferentialAmount());
            }
            orderId=o.getOrderStoreId();
        }
        log.info("子单全部发货完成进行均摊--A品券分摊--商品价值 priceAmount={}",priceAmount);
        //查询A品券抵扣金额
        ErpOrderFee erpOrderFee=erpOrderFeeService.getOrderFeeByOrderId(orderId);
        log.info("子单全部发货完成进行均摊--A品券分摊--费用信息 erpOrderFee={}",erpOrderFee);
        BigDecimal topCouponMoney = erpOrderFee.getTopCouponMoney();
        log.info("子单全部发货完成进行均摊--A品券分摊--A品券抵扣金额 topCouponMoney={}",topCouponMoney);
        amount=priceAmount.subtract(topCouponMoney);
        log.info("子单全部发货完成进行均摊--A品券分摊--商品金额 amount={}",amount);
        //记录A品券分摊后的数据
        Map<String,ErpOrderItem> couponAfterMap=new HashMap<>();
        for(ErpOrderItem o:list){
            //本行商品价值
            BigDecimal totalPreferentialAmount = o.getTotalPreferentialAmount();
            //分摊总金额=本行商品价值X商品总金额/商品总价值
            BigDecimal to=totalPreferentialAmount.multiply(amount).divide(priceAmount,2,BigDecimal.ROUND_HALF_UP);
            //分摊单价
            BigDecimal per=to.divide(new BigDecimal(o.getProductCount()),2,BigDecimal.ROUND_HALF_UP);
            //A品券单行抵扣总金额=A品券抵扣金额X本行商品价值/商品总价值
            BigDecimal at = topCouponMoney.multiply(totalPreferentialAmount).divide(priceAmount, 2, BigDecimal.ROUND_HALF_UP);
            //A品券单行每个商品抵扣金额
            BigDecimal ap=at.divide(new BigDecimal(o.getProductCount()));
            o.setTotalPreferentialAmount(to);
            o.setPreferentialAmount(per);
            o.setTopCouponDiscountAmount(at);
            o.setTopCouponAmount(ap);
            couponAfterMap.put(o.getOrderInfoDetailId(),o);
        }
        log.info("子单全部发货完成进行均摊--A品券分摊--分摊结果map couponAfterMap={}",couponAfterMap);
        for(ErpOrderItem f:itemList){
            String orderInfoDetailId = f.getOrderInfoDetailId();
            if(null!=couponAfterMap.get(orderInfoDetailId)){
                ErpOrderItem o= couponAfterMap.get(orderInfoDetailId);
                f.setTotalPreferentialAmount(o.getTotalPreferentialAmount());
                f.setPreferentialAmount(o.getPreferentialAmount());
            }
        }
        log.info("子单全部发货完成进行均摊--A品券分摊--分摊后原始明细行集合 itemList={}",JSON.toJSONString(itemList));
        /************************************赠品均摊**********************************/
        //实付金额
        BigDecimal payMoney = erpOrderFee.getPayMoney();
        log.info("子单全部发货完成进行均摊--赠品均摊--实付金额 payMoney={}",payMoney);
        //商品总价值=（商品或活动赠品分摊总额相加）+（自选赠品实发数量X分销价）的和
        BigDecimal totalMoney = erpOrderFee.getPayMoney();
        //遍历明细行，计算商品总价值
        for(ErpOrderItem p:itemList){
            Integer productType = p.getProductType();
            if(ErpProductGiftEnum.JIFEN.getCode().equals(productType)){
                //兑换赠品本行商品价值=分销价X订货数量
                BigDecimal multiply = p.getProductAmount().multiply(new BigDecimal(p.getActualProductCount()));
                p.setTotalPreferentialAmount(multiply);
                totalMoney=totalMoney.add(multiply);
            }else{
                //其他商品按照分摊总金额累加
                totalMoney=totalMoney.add(p.getTotalPreferentialAmount());
            }
        }
        log.info("子单全部发货完成进行均摊--赠品均摊--商品总价值 totalMoney={}",totalMoney);
        //最后分摊完的商品明细，用于明细更新使用
        List<ErpOrderItem> resList=new ArrayList<>();
        //遍历明细行，进行分摊
        for(ErpOrderItem k:itemList){
            //分摊总价=商品原始分摊总价X实付金额/商品总价值
            BigDecimal tper = k.getTotalPreferentialAmount().multiply(payMoney).divide(totalMoney, 2, BigDecimal.ROUND_HALF_UP);
            //商品类型
            Integer productType = k.getProductType();
            BigDecimal s=BigDecimal.ZERO;
            if(ErpProductGiftEnum.PRODUCT.getCode().equals(productType)){
                //商品分摊单价=分摊总金额/订货数量
                s=tper.divide(new BigDecimal(k.getProductCount()));
            }else {
                //赠品分摊单价=分摊总金额/实发数量
                s=tper.divide(new BigDecimal(k.getActualProductCount()));
            }
            k.setTotalPreferentialAmount(tper);
            k.setPreferentialAmount(s);
            ErpOrderItem er=new ErpOrderItem();
            er.setTotalPreferentialAmount(k.getTotalPreferentialAmount());
            er.setPreferentialAmount(k.getPreferentialAmount());
            er.setTopCouponDiscountAmount(k.getTopCouponDiscountAmount());
            er.setOrderInfoDetailId(k.getOrderInfoDetailId());
            er.setId(k.getId());
            resList.add(er);
        }
        log.info("子单全部发货完成进行均摊--赠品均摊--分摊完后原始订单明细集合 itemList={}",JSON.toJSONString(itemList));
        /*****************************************分摊计算结束，更新明细表*****************************************/
        log.info("子单全部发货完成进行均摊--所有分摊结束--更新原始订单明细集合 resList={}",JSON.toJSONString(resList));
        AuthToken auth=new AuthToken();

        /*****************************************分摊计算结束，发放赠品额度start*****************************************/
        log.info("分摊计算结束--判断是否发放赠品额度--主订单信息为 orderAndItemByOrderCode={}",JSON.toJSONString(orderAndItemByOrderCode));
        //订单类型为配送    订单类别为普通补货  【只有这个组合类型调物流卷和发放赠品额度】
        if(ErpOrderTypeEnum.DISTRIBUTION.getCode().equals(orderAndItemByOrderCode.getOrderTypeCode())
                && ErpOrderCategoryEnum.ORDER_TYPE_1.getCode().equals(orderAndItemByOrderCode.getOrderCategoryCode())){
            //只有18A商品会发放赠品额度
            BigDecimal commodityAmountOfTop=BigDecimal.ZERO;
            //赠品返回额度比例
            Double rebatesProportion=0.00;
            for (ErpOrderItem item:resList){
                ErpProductPropertyTypeEnum propertyTypeEnum = ErpProductPropertyTypeEnum.getEnum(item.getProductPropertyCode());
                //判断是18A商品  并且是主商品（赠品不发放额度）
                if (propertyTypeEnum.isUseTopCoupon() && ErpProductGiftEnum.PRODUCT.getCode().equals(item.getProductType())) {
                    commodityAmountOfTop=commodityAmountOfTop.add(item.getTotalPreferentialAmount());
                }
            }
            log.info("子单全部发货完成进行均摊--赠品均摊--分摊完后--主订单商品18A类型总金额为commodityAmountOfTop----",commodityAmountOfTop);
            //判断18A商品金额总和大于0
            if(commodityAmountOfTop.compareTo(BigDecimal.ZERO)==1){
                NewStoreGradient gradient=bridgeProductService.selectStoreGiveawayByStoreCode(orderAndItemByOrderCode.getStoreCode());

                if(null!=gradient&&null!=gradient.getStoreGradientList()&&gradient.getStoreGradientList().size()>0){
                    for(StoreGradient storeGradient:gradient.getStoreGradientList()){
                        if(commodityAmountOfTop.compareTo(BigDecimal.valueOf(storeGradient.getTiDuMaxValue()))==1){
                            rebatesProportion=storeGradient.getRebatesProportion();
                        }
                    }
                    log.info("子单全部发货完成进行均摊--赠品均摊--分摊完后--赠品比例为 rebatesProportion={}",rebatesProportion);
                    if(rebatesProportion>0){
                        //18A主商品金额总和乘以赠品比例
                        commodityAmountOfTop=commodityAmountOfTop.multiply(BigDecimal.valueOf(rebatesProportion)).divide(new BigDecimal(100)).setScale(2, RoundingMode.DOWN);

                        //查詢門店员赠品额度
                        BigDecimal availableGiftQuota=bridgeProductService.getStoreAvailableGiftGuota(orderAndItemByOrderCode.getStoreId());
                        //计算订单赠品额度发放后的赠品额度
                        BigDecimal newAvailableGiftQuota=availableGiftQuota.add(commodityAmountOfTop);
                        //更新订单使用过后的赠品额度
                        bridgeProductService.updateAvailableGiftQuota(orderAndItemByOrderCode.getStoreId(),newAvailableGiftQuota);
                        log.info("子单全部发货完成进行均摊--赠品均摊--分摊完后--赠品额度更新信息为：应发放赠品额度 commodityAmountOfTop={}",commodityAmountOfTop
                                +"查詢門店员赠品额度 availableGiftQuota={}"+availableGiftQuota+"计算订单赠品额度发放后的赠品额度 newAvailableGiftQuota={}"+newAvailableGiftQuota);
                        //新建一个赠品额度使用明细对象
                        GiftQuotasUseDetail giftQuotasUseDetail=new GiftQuotasUseDetail();
                        giftQuotasUseDetail.setStoreId(orderAndItemByOrderCode.getStoreId());
                        giftQuotasUseDetail.setChangeInGiftQuota("+"+commodityAmountOfTop);
                        giftQuotasUseDetail.setBillCode(orderAndItemByOrderCode.getOrderStoreCode());
                        giftQuotasUseDetail.setType(3);
                        giftQuotasUseDetail.setCreateBy(auth.getPersonName());
                        giftQuotasUseDetail.setUpdateBy(auth.getPersonName());
                        //插入一条赠品明细使用记录
                        giftQuotasUseDetailService.add(giftQuotasUseDetail);

                        //更新订单费用信息表-发放赠品额度字段
                        ErpOrderFee orderFee = erpOrderFeeService.getOrderFeeByOrderId(orderAndItemByOrderCode.getOrderStoreId());
                        if(null!=orderFee){
                            orderFee.setComplimentaryAmount(commodityAmountOfTop);
                            erpOrderFeeService.updateOrderFeeByPrimaryKeySelective(orderFee,auth);
                        }else{
                            log.info("分摊计算结束，发放赠品额度-查询支付信息异常，查询订单号为"+orderAndItemByOrderCode.getOrderStoreId());
                        }

                    }

                }
            }

        }
        /*****************************************分摊计算结束，发放赠品额度end*****************************************/


        auth.setPersonId("系统操作");
        auth.setPersonName("系统操作");
        erpOrderItemService.updateOrderItemList(resList,auth);
        log.info("子单全部发货完成进行均摊--更新明细表结束");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void distributeLogisticsFee(String logisticsCode) {

        AuthToken auth = new AuthToken();
        //物流单
        ErpOrderLogistics orderLogistics = erpOrderLogisticsService.getOrderLogisticsByLogisticsCode(logisticsCode);
        if (orderLogistics == null) {
            throw new BusinessException("无效的物流单");
        }
        auth.setPersonId(orderLogistics.getCreateById());
        auth.setPersonName(orderLogistics.getCreateByName());

        if (!ErpPayStatusEnum.SUCCESS.getCode().equals(orderLogistics.getPayStatus())) {
            //物流费用没有支付成功，不做计算
            return;
        }

        List<ErpOrderInfo> orderList = erpOrderQueryService.getOrderByLogisticsId(orderLogistics.getLogisticsId());
        if (orderList == null || orderList.size() == 0) {
            //没有订单
            return;
        }

        //所有订单发货商品总数量
        Long totalCount = 0L;
        for (ErpOrderInfo item :
                orderList) {
            totalCount += item.getActualProductCount();
        }

        //物流单实际支付费用
        BigDecimal fee = orderLogistics.getBalancePayFee();
        //占用费用
        BigDecimal useFee = BigDecimal.ZERO;
        for (int i = 0; i < orderList.size(); i++) {
            ErpOrderInfo order = orderList.get(i);
            BigDecimal thisFee = BigDecimal.ZERO;
            if (i < orderList.size() - 1) {
                //该订单占用运费
                thisFee = new BigDecimal(order.getActualProductCount()).divide(new BigDecimal(totalCount), 2, RoundingMode.HALF_UP);
                useFee = useFee.add(thisFee);
            } else {
                thisFee = fee.subtract(useFee);
            }

            order.setDeliverAmount(thisFee);
            erpOrderInfoService.updateOrderByPrimaryKeySelectiveNoLog(order, auth);
        }
    }

    /**
     * 判断子单是否全部发货完成
     * @param mainOrderCode
     * @return true:全部发货完成 false:未全部发货
     */
    private Boolean checkSendOk(String mainOrderCode){
        log.info("判断子单是否全部发货完成,入参mainOrderCode={}",mainOrderCode);
        ErpOrderInfo po=new ErpOrderInfo();
        po.setMainOrderCode(mainOrderCode);
        List<ErpOrderInfo> list=erpOrderInfoDao.select(po);
        if(list!=null&&list.size()>0){
            log.info("判断子单是否全部发货完成,原始订单集合为 list={}",JSON.toJSONString(list));
            for(ErpOrderInfo eoi:list){
                Integer orderStatus = eoi.getOrderStatus();
                //判断订单状态是否是 11:发货完成或者 97:缺货终止
                if(orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_11.getCode())||orderStatus.equals(ErpOrderStatusEnum.ORDER_STATUS_97.getCode())){
                }else{
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        ErpOrderDeliverServiceImpl service=new ErpOrderDeliverServiceImpl();
        service.shareEqually("20200428000001");
    }
}
