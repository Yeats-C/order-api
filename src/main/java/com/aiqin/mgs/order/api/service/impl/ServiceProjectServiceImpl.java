package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.ServiceProjectAssetDao;
import com.aiqin.mgs.order.api.dao.ServiceProjectReduceDetailDao;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.request.service.ReduceAndAssetRequest;
import com.aiqin.mgs.order.api.domain.request.service.ReduceDetailRequest;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectAsset;
import com.aiqin.mgs.order.api.domain.request.service.ServiceProjectReduceDetail;
import com.aiqin.mgs.order.api.domain.response.service.ServiceProjectTransformResponse;
import com.aiqin.mgs.order.api.service.BridgeOmsService;
import com.aiqin.mgs.order.api.service.ServiceProjectService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author jinghaibo
 * Date: 2019/12/5 16:17
 * Description:
 */
@Service
public class ServiceProjectServiceImpl  implements ServiceProjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceProjectServiceImpl.class);
    @Value("${bridge.url.store-api}")
    private String storeApiUrl;
    @Resource
    private ServiceProjectAssetDao serviceProjectAssetDao;
    @Resource
    private ServiceProjectReduceDetailDao serviceProjectReduceDetailDao;
    @Resource
    private BridgeOmsService bridgeOmsService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse insertServiceProjectAsset(ServiceProjectAsset serviceProjectAsset) {
        try {
            LOGGER.info("添加用户服务项目资产信息，请求参数serviceProjectAsset为{}", serviceProjectAsset);
            String assetId = IdUtil.assetId();
            serviceProjectAsset.setAssetId(assetId);
            Integer record = serviceProjectAssetDao.insertServiceProjectAsset(serviceProjectAsset);
            if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                LOGGER.info("添加用户服务项目资产信息失败，请求参数serviceProjectAsset为{}", serviceProjectAsset);
                return HttpResponse.failure(ResultCode.INSERT_SOURCE_FAIL);
            }
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.error("添加用户服务项目资产信息异常，请求参数serviceProjectAsset为{}", serviceProjectAsset, e);
            throw new RuntimeException(ResultCode.INSERT_SOURCE_EXCEPTION.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse insertList(List<ServiceProjectAsset> serviceProjectAssetList) {
        try {
            LOGGER.info("批量添加用户的服务项目资产信息，请求参数serviceProjectAssetList为{}", serviceProjectAssetList);
            if (CollectionUtils.isNotEmpty(serviceProjectAssetList)) {
                serviceProjectAssetList.stream().forEach(serviceProjectAsset -> {
                    serviceProjectAsset.setAssetId(IdUtil.assetId());
                });
                Integer record = serviceProjectAssetDao.insertList(serviceProjectAssetList);
                if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                    LOGGER.info("批量添加用户的服务项目资产信息失败，请求参数serviceProjectAssetList为{}", serviceProjectAssetList);
                    return HttpResponse.failure(ResultCode.INSERT_SOURCE_LIST_FAIL);
                }
            }
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.error("批量添加用户的服务项目资产信息异常，请求参数serviceProjectAssetList为{}", serviceProjectAssetList, e);
            throw new RuntimeException(ResultCode.INSERT_SOURCE_LIST_EXCEPTION.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse updateAsset(ServiceProjectAsset serviceProjectAsset) {
        try {
            LOGGER.info("更新用户服务商品资产信息，请求参数serviceProjectAsset为{}", serviceProjectAsset);
            if (serviceProjectAsset.getConsumptionPattern().equals(0) && serviceProjectAsset.getRemainCount() == 0) {
                LOGGER.info("服务商品次数用完，资产状态变为否");
                serviceProjectAsset.setUseStatus(1);
            }
            Integer record = serviceProjectAssetDao.updateSource(serviceProjectAsset);
            if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                LOGGER.info("更新用户服务商品资产信息失败，请求参数serviceProjectAsset为{}", serviceProjectAsset);
                return HttpResponse.failure(ResultCode.UPDATE_SOURCE_FAIL);
            }
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.error("更新用户服务商品资产信息异常，serviceProjectAsset为" + serviceProjectAsset, e);
            throw new RuntimeException(ResultCode.UPDATE_SOURCE_EXCEPTION.getMessage());
        }
    }

    @Override
    public HttpResponse selectServiceProjectAssetByPhone(String customerPhone, String storeId) {
        try {
            LOGGER.info("根据用户手机号查询用户服务项目资产list，请求参数customerPhone为{},storeId为{}", customerPhone, storeId);
            List<ServiceProjectAsset> serviceProjectAssetList = serviceProjectAssetDao.selectServiceProjectAssetByPhone(customerPhone, storeId, null, null);
            LOGGER.info("根据用户手机号查询用户服务项目资产list，请求参数customerPhone为{},storeId为{},查询结果为{}", customerPhone, storeId, serviceProjectAssetList);
            return HttpResponse.success(serviceProjectAssetList);
        } catch (Exception e) {
            LOGGER.error("根据用户手机号查询用户服务项目资产list异常，customerPhone为{},storeId为{}", customerPhone, storeId, e);
            return HttpResponse.failure(ResultCode.SELECT_SOURCE_BY_PHONE_EXCEPTION);
        }
    }

    @Override
    public HttpResponse selectServiceProjectAssetByAssetId(String sourceId) {
        try {
            LOGGER.info("根据资产id查询用户服务项目资产信息，请求参数sourceId为{}", sourceId);
            ServiceProjectAsset serviceProjectAsset = serviceProjectAssetDao.selectServiceProjectAssetBySourceId(sourceId);
            LOGGER.info("根据资产id查询用户服务项目资产信息，请求参数sourceId为{},查询结果为{}", sourceId, serviceProjectAsset);
            return HttpResponse.success(serviceProjectAsset);
        } catch (Exception e) {
            LOGGER.error("根据资产id查询用户服务项目资产信息异常，sourceId为" + sourceId, e);
            return HttpResponse.failure(ResultCode.SELECT_SOURCE_BY_ID_EXCEPTION);
        }
    }

    @Override
    public HttpResponse serviceProjectTransformStatistics(String storeId) {
        try {
            LOGGER.info("开始统计服务项目的转化情况");
            ServiceProjectTransformResponse serviceProjectTransformResponse = new ServiceProjectTransformResponse();
            //
            ReduceDetailRequest reduceDetailRequest = new ReduceDetailRequest();
            reduceDetailRequest.setStoreId(storeId);
            serviceProjectTransformResponse.setTotalAmount(serviceProjectAssetDao.TotalAmount(reduceDetailRequest)-serviceProjectReduceDetailDao.TotalReturnAmount(reduceDetailRequest));
            serviceProjectTransformResponse.setYesterdayAmount(serviceProjectAssetDao.yesTodayTotalAmount(reduceDetailRequest)-serviceProjectReduceDetailDao.yesTodayTotalReturnAmount(reduceDetailRequest));
            serviceProjectTransformResponse.setTotalCount(serviceProjectAssetDao.TotalCount(reduceDetailRequest)-serviceProjectReduceDetailDao.TotalReturnCount(reduceDetailRequest));
            serviceProjectTransformResponse.setYesterdayCount(serviceProjectAssetDao.yesTodayTotalCount(reduceDetailRequest)-serviceProjectReduceDetailDao.yesTodayTotalReturnCount(reduceDetailRequest));
            serviceProjectTransformResponse.setUseProjectCount(bridgeOmsService.countUseStoreProjectByStoreId(reduceDetailRequest.getStoreId()));
            List<String> totalCustomerList = serviceProjectAssetDao.TotalConsumer(reduceDetailRequest);
            List<String> totalCustomers = serviceProjectReduceDetailDao.TotalConsumer(reduceDetailRequest);
            totalCustomerList.addAll(totalCustomers);
            Set<String> totalCustomerSet = new HashSet<>(totalCustomerList);
            totalCustomerList.clear();
            totalCustomerList.addAll(totalCustomerSet);
            serviceProjectTransformResponse.setTotalCustomerCount(totalCustomerList.size());
            List<String> yesTotalCustomerList = serviceProjectAssetDao.yesTodayTotalConsumer(reduceDetailRequest);
            List<String> yesTotalCustomers = serviceProjectReduceDetailDao.yesTodayTotalConsumer(reduceDetailRequest);
            yesTotalCustomerList.addAll(yesTotalCustomers);
            Set<String> yesTotalCustomerSet = new HashSet<>(yesTotalCustomerList);
            yesTotalCustomerList.clear();
            yesTotalCustomerList.addAll(yesTotalCustomerSet);
            serviceProjectTransformResponse.setYesterdayCustomerCount(yesTotalCustomerList.size());
            LOGGER.info("统计服务项目的转化情况完成,查询结果为{}", serviceProjectTransformResponse);
            return HttpResponse.success(serviceProjectTransformResponse);
        } catch (Exception e) {
            LOGGER.error("统计服务项目的转化情况异常", e);
            return HttpResponse.failure(ResultCode.SERVICE_PROJECT_TRANSFORM_STATISTICS_EXCEPTION);
        }
    }

    @Override
    public HttpResponse serviceProjectTypeTransformStatistics(ReduceDetailRequest reduceDetailRequest) {
        try {
            LOGGER.info("统计各类别服务项目的转化情况，请求参数reduceDetailRequest为{}",reduceDetailRequest);
            List<ServiceProjectTransformResponse> serviceProjectTypeTransformResponseList = serviceProjectAssetDao.totalCountAndAmountByType(reduceDetailRequest);
            List<ServiceProjectTransformResponse> serviceProjectTransformResponses = serviceProjectReduceDetailDao.totalReturnCountAndAmountByType(reduceDetailRequest);
            for(ServiceProjectTransformResponse serviceProjectTransformResponse : serviceProjectTypeTransformResponseList) {
                for(ServiceProjectTransformResponse serviceProjectTransformResponse1 : serviceProjectTransformResponses){
                    if(serviceProjectTransformResponse.getTypeId().equals(serviceProjectTransformResponse1.getTypeId())){
                        serviceProjectTransformResponse.setTotalCount(serviceProjectTransformResponse.getTotalCount()-serviceProjectTransformResponse1.getTotalReturnCount());
                        serviceProjectTransformResponse.setTotalAmount(serviceProjectTransformResponse.getTotalAmount()-serviceProjectTransformResponse1.getTotalReturnAmount());
                        break;
                    }
                }
                reduceDetailRequest.setTypeId(serviceProjectTransformResponse.getTypeId());
                List<String> totalCustomerList = serviceProjectAssetDao.buyCount(reduceDetailRequest);
                List<String> totalCustomers = serviceProjectReduceDetailDao.consumerCount(reduceDetailRequest);
                totalCustomerList.addAll(totalCustomers);
                Set<String> totalCustomerSet = new HashSet<>(totalCustomerList);
                totalCustomerList.clear();
                totalCustomerList.addAll(totalCustomerSet);
                serviceProjectTransformResponse.setTotalCustomerCount(totalCustomerList.size());
            }
            Collections.sort(serviceProjectTypeTransformResponseList, new Comparator<ServiceProjectTransformResponse>() {
                @Override
                public int compare(ServiceProjectTransformResponse o1, ServiceProjectTransformResponse o2) {
                    return (int)(o2.getTotalAmount()-o1.getTotalAmount());
                }
            });
            LOGGER.info("统计各类别服务项目的转化情况完成,查询结果为{}", serviceProjectTypeTransformResponseList);
            return HttpResponse.success(serviceProjectTypeTransformResponseList);
        } catch (Exception e) {
            LOGGER.error("统计各类别服务项目的转化情况异常", e);
            return HttpResponse.failure(ResultCode.SERVICE_PROJECT_TYPE_TRANSFORM_STATISTICS_EXCEPTION);
        }
    }

    @Override
    public HttpResponse serviceProjectTransformStatistics(ReduceDetailRequest reduceDetailRequest) {
        try {
            LOGGER.info("统计各服务项目的转化情况，请求参数reduceDetailRequest为{}",reduceDetailRequest);
            List<ServiceProjectTransformResponse> serviceProjectTypeTransformResponseList = serviceProjectAssetDao.totalCountAndAmountByProject(reduceDetailRequest);
            List<ServiceProjectTransformResponse> serviceProjectTransformResponses = serviceProjectReduceDetailDao.totalReturnCountAndAmountByProject(reduceDetailRequest);
            for(ServiceProjectTransformResponse serviceProjectTransformResponse : serviceProjectTypeTransformResponseList) {
                for(ServiceProjectTransformResponse serviceProjectTransformResponse1 : serviceProjectTransformResponses){
                    if(serviceProjectTransformResponse.getTypeId().equals(serviceProjectTransformResponse1.getTypeId())){
                        serviceProjectTransformResponse.setTotalCount(serviceProjectTransformResponse.getTotalCount()-serviceProjectTransformResponse1.getTotalReturnCount());
                        serviceProjectTransformResponse.setTotalAmount(serviceProjectTransformResponse.getTotalAmount()-serviceProjectTransformResponse1.getTotalReturnAmount());
                        break;
                    }
                }
                reduceDetailRequest.setProjectId(serviceProjectTransformResponse.getProjectId());
                List<String> totalCustomerList = serviceProjectAssetDao.buyCount(reduceDetailRequest);
                List<String> totalCustomers = serviceProjectReduceDetailDao.consumerCount(reduceDetailRequest);
                totalCustomerList.addAll(totalCustomers);
                Set<String> totalCustomerSet = new HashSet<>(totalCustomerList);
                totalCustomerList.clear();
                totalCustomerList.addAll(totalCustomerSet);
                serviceProjectTransformResponse.setTotalCustomerCount(totalCustomerList.size());
            }
            Collections.sort(serviceProjectTypeTransformResponseList, (o1, o2) -> {
                return (int) (o2.getTotalAmount() - o1.getTotalAmount());
            });
            LOGGER.info("统计各服务项目的转化情况完成,查询结果为{}", serviceProjectTypeTransformResponseList);
            return HttpResponse.success(serviceProjectTypeTransformResponseList);
        } catch (Exception e) {
            LOGGER.error("统计各服务项目的转化情况异常", e);
            return HttpResponse.failure(ResultCode.SERVICE_PROJECT_PROJECT_TRANSFORM_STATISTICS_EXCEPTION);
        }
    }

    @Override
    public HttpResponse serviceProjectTopTransformStatistics(ReduceDetailRequest reduceDetailRequest) {
        try {
            LOGGER.info("统计各服务项目销量top10，请求参数reduceDetailRequest为{}",reduceDetailRequest);
            List<ServiceProjectTransformResponse> serviceProjectTypeTransformResponseList = serviceProjectAssetDao.totalCountAndAmountByProject(reduceDetailRequest);
            List<ServiceProjectTransformResponse> serviceProjectTransformResponses = serviceProjectReduceDetailDao.totalReturnCountAndAmountByProject(reduceDetailRequest);
            for(ServiceProjectTransformResponse serviceProjectTransformResponse : serviceProjectTypeTransformResponseList) {
                for(ServiceProjectTransformResponse serviceProjectTransformResponse1 : serviceProjectTransformResponses){
                    if(serviceProjectTransformResponse.getTypeId().equals(serviceProjectTransformResponse1.getTypeId())){
                        serviceProjectTransformResponse.setTotalCount(serviceProjectTransformResponse.getTotalCount()-serviceProjectTransformResponse1.getTotalReturnCount());
                        break;
                    }
                }
            }
            Collections.sort(serviceProjectTypeTransformResponseList, (o1, o2) -> o2.getTotalCount()-o1.getTotalCount());
            if(serviceProjectTypeTransformResponseList.size()>10){
                serviceProjectTypeTransformResponseList = serviceProjectTypeTransformResponseList.subList(0,10);
            }
            LOGGER.info("统计各服务项目销量top10完成,查询结果为{}", serviceProjectTypeTransformResponseList);
            return HttpResponse.success(serviceProjectTypeTransformResponseList);
        } catch (Exception e) {
            LOGGER.error("统计各服务项目销量top10异常", e);
            return HttpResponse.failure(ResultCode.SERVICE_PROJECT_TOP_TRANSFORM_STATISTICS_EXCEPTION);
        }
    }

    @Override
    public HttpResponse selectServiceProjectOrder(ReduceDetailRequest reduceDetailRequest) {
        try {
            LOGGER.info("通过门店编号、名称、用户手机号和时间查询订单的信息，请求参数为{}", reduceDetailRequest);
            PageResData<ServiceProjectReduceDetail> pageResData = new PageResData<>();
            reduceDetailRequest.setBeginIndex((reduceDetailRequest.getPageNo()-1)*reduceDetailRequest.getPageSize());
            List<ServiceProjectReduceDetail> serviceProjectReduceDetailList = serviceProjectReduceDetailDao.selectReduceDetailByCondition(reduceDetailRequest);
           // serviceProjectReduceDetailList.forEach(serviceProjectReduceDetail -> serviceProjectReduceDetail.setOrderType(reduceDetailRequest.getOrderType()));
            Integer totalCount = serviceProjectReduceDetailDao.countReduceDetailByCondition(reduceDetailRequest);
            pageResData.setDataList(serviceProjectReduceDetailList);
            pageResData.setTotalCount(totalCount);

/*
            if (reduceDetailRequest.getOrderType()!=null && !Global.ORDER_TYPE_BUY.equals(reduceDetailRequest.getOrderType())) {
                // 扣减单和退次单查询
                List<ServiceProjectReduceDetail> serviceProjectReduceDetailList = serviceProjectReduceDetailDao.selectReduceDetailByCondition(reduceDetailRequest);
                serviceProjectReduceDetailList.forEach(serviceProjectReduceDetail -> serviceProjectReduceDetail.setOrderType(reduceDetailRequest.getOrderType()));
                Integer totalCount = serviceProjectReduceDetailDao.countReduceDetailByCondition(reduceDetailRequest);
                pageResData.setDataList(serviceProjectReduceDetailList);
                pageResData.setTotalCount(totalCount);
            } else if (Global.ORDER_TYPE_BUY.equals(reduceDetailRequest.getOrderType())) {
                // 购买订单查询，通过
                List<ServiceProjectReduceDetail> serviceProjectReduceDetailList = new ArrayList<>();
                List<ServiceProjectAsset> serviceProjectAssetList = serviceProjectAssetDao.selectAssetByCondition(reduceDetailRequest);
                Integer totalCount = serviceProjectAssetDao.countAssetByCondition(reduceDetailRequest);
                ServiceProjectReduceDetail serviceProjectReduceDetail;
                for (ServiceProjectAsset serviceProjectAsset : serviceProjectAssetList) {
                    serviceProjectReduceDetail = new ServiceProjectReduceDetail();
                    serviceProjectReduceDetail.setAssetId(serviceProjectAsset.getAssetId());
                    serviceProjectReduceDetail.setOrderCode(serviceProjectAsset.getOrderCode());
                    serviceProjectReduceDetail.setOrderId(serviceProjectAsset.getOrderId());
                    serviceProjectReduceDetail.setOrderType(2);
                    serviceProjectReduceDetail.setCashierId(serviceProjectAsset.getCashierId());
                    serviceProjectReduceDetail.setCashierName(serviceProjectAsset.getCashierName());
                    serviceProjectReduceDetail.setCustomerId(serviceProjectAsset.getCustomerId());
                    serviceProjectReduceDetail.setCustomerName(serviceProjectAsset.getCustomerName());
                    serviceProjectReduceDetail.setCustomerPhone(serviceProjectAsset.getCustomerPhone());
                    serviceProjectReduceDetail.setCustomerType(serviceProjectAsset.getCustomerType());
                    serviceProjectReduceDetail.setStoreCode(serviceProjectAsset.getStoreCode());
                    serviceProjectReduceDetail.setStoreId(serviceProjectAsset.getStoreId());
                    serviceProjectReduceDetail.setStoreName(serviceProjectAsset.getStoreName());
                    serviceProjectReduceDetail.setGuideId(serviceProjectAsset.getGuideId());
                    serviceProjectReduceDetail.setGuideName(serviceProjectAsset.getGuideName());
                    serviceProjectReduceDetail.setCreateTime(serviceProjectAsset.getCreateTime());
                    serviceProjectReduceDetail.setReturnAmount(serviceProjectAssetDao.countAmount(serviceProjectAsset.getOrderId()));
                    serviceProjectReduceDetailList.add(serviceProjectReduceDetail);
                }
                pageResData.setDataList(serviceProjectReduceDetailList);
                pageResData.setTotalCount(totalCount);
            } else {
                //全部查询
                List<ServiceProjectReduceDetail> serviceProjectReduceDetailList = serviceProjectReduceDetailDao.selectReduceDetailByConditions(reduceDetailRequest);
                Integer totalCount = serviceProjectReduceDetailDao.countReduceDetailByCondition(reduceDetailRequest);
                List<ServiceProjectReduceDetail> serviceProjectReduceDetails = new ArrayList<>();
                List<ServiceProjectAsset> serviceProjectAssetList = serviceProjectAssetDao.selectAssetByConditions(reduceDetailRequest);
                Integer totalCounts = serviceProjectAssetDao.countAssetByConditions(reduceDetailRequest);
                ServiceProjectReduceDetail serviceProjectReduceDetail;
                for (ServiceProjectAsset serviceProjectAsset : serviceProjectAssetList) {
                    serviceProjectReduceDetail = new ServiceProjectReduceDetail();
                    serviceProjectReduceDetail.setAssetId(serviceProjectAsset.getAssetId());
                    serviceProjectReduceDetail.setOrderCode(serviceProjectAsset.getOrderCode());
                    serviceProjectReduceDetail.setOrderId(serviceProjectAsset.getOrderId());
                    serviceProjectReduceDetail.setOrderType(2);
                    serviceProjectReduceDetail.setCashierId(serviceProjectAsset.getCashierId());
                    serviceProjectReduceDetail.setCashierName(serviceProjectAsset.getCashierName());
                    serviceProjectReduceDetail.setCustomerId(serviceProjectAsset.getCustomerId());
                    serviceProjectReduceDetail.setCustomerName(serviceProjectAsset.getCustomerName());
                    serviceProjectReduceDetail.setCustomerPhone(serviceProjectAsset.getCustomerPhone());
                    serviceProjectReduceDetail.setCustomerType(serviceProjectAsset.getCustomerType());
                    serviceProjectReduceDetail.setStoreCode(serviceProjectAsset.getStoreCode());
                    serviceProjectReduceDetail.setCreateTime(serviceProjectAsset.getCreateTime());
                    serviceProjectReduceDetail.setStoreId(serviceProjectAsset.getStoreId());
                    serviceProjectReduceDetail.setStoreName(serviceProjectAsset.getStoreName());
                    serviceProjectReduceDetail.setGuideId(serviceProjectAsset.getGuideId());
                    serviceProjectReduceDetail.setGuideName(serviceProjectAsset.getGuideName());
                    serviceProjectReduceDetailList.add(serviceProjectReduceDetail);
                }
                serviceProjectReduceDetailList.sort(new Comparator<ServiceProjectReduceDetail>() {
                    @Override
                    public int compare(ServiceProjectReduceDetail o1, ServiceProjectReduceDetail o2) {
                        if (o1.getCreateTime().before(o2.getCreateTime())) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }
                });
                for (int i = 0; i < serviceProjectReduceDetailList.size(); i++) {
                    if (i >= reduceDetailRequest.getBeginIndex() && i < reduceDetailRequest.getPageSize() + reduceDetailRequest.getBeginIndex()) {
                        serviceProjectReduceDetails.add(serviceProjectReduceDetailList.get(i));
                    }
                }
                if(CollectionUtils.isNotEmpty(serviceProjectReduceDetails)){
                    for(ServiceProjectReduceDetail serviceProjectReduceDetail1 : serviceProjectReduceDetails){
                        if(Global.ORDER_TYPE_BUY.equals(serviceProjectReduceDetail1.getOrderType())){
                            serviceProjectReduceDetail1.setReturnAmount(serviceProjectAssetDao.countAmount(serviceProjectReduceDetail1.getOrderId()));
                        }
                    }
                }
                totalCount = totalCounts + totalCount;
                pageResData.setDataList(serviceProjectReduceDetails);
                pageResData.setTotalCount(totalCount);
            }*/
            LOGGER.info("通过门店编号、名称、用户手机号和时间查询订单的信息完成,查询结果为{}", pageResData);
            return HttpResponse.success(pageResData);
        } catch (Exception e) {
            LOGGER.error("通过门店编号、名称、用户手机号和时间查询订单的信息异常", e);
            return HttpResponse.failure(ResultCode.SELECT_REDUCE_DETAIL_EXCEPTION);
        }
    }

    @Override
    public HttpResponse selectOrderInfo(String orderId, Integer orderType, String storeId) {
        try {
            LOGGER.info("根据订单id和订单类型查询订单信息，请求参数orderId为{},orderType为{}", orderId, orderType);
            ServiceProjectReduceDetail serviceProjectReduceDetail = new ServiceProjectReduceDetail();
            if (Global.ORDER_TYPE_BUY.equals(orderType)) {
                //购买订单查询
                List<ServiceProjectAsset> serviceProjectAssetList = serviceProjectAssetDao.selectServiceProjectAssetByPhone(null, storeId, null, orderId);
                if (CollectionUtils.isEmpty(serviceProjectAssetList)) {
                    LOGGER.info("未查询到对应的订单信息");
                    return HttpResponse.failure(ResultCode.SELECT_ASSET_BY_ORDER_Id_FAIL);
                }
                ServiceProjectAsset serviceProjectAsset = serviceProjectAssetList.get(0);
                serviceProjectReduceDetail.setOrderCode(serviceProjectAsset.getOrderCode());
                serviceProjectReduceDetail.setOrderId(serviceProjectAsset.getOrderId());
                serviceProjectReduceDetail.setOrderType(Global.ORDER_TYPE_BUY);
                serviceProjectReduceDetail.setCashierId(serviceProjectAsset.getCashierId());
                serviceProjectReduceDetail.setCashierName(serviceProjectAsset.getCashierName());
                serviceProjectReduceDetail.setCustomerId(serviceProjectAsset.getCustomerId());
                serviceProjectReduceDetail.setCustomerName(serviceProjectAsset.getCustomerName());
                serviceProjectReduceDetail.setCustomerPhone(serviceProjectAsset.getCustomerPhone());
                serviceProjectReduceDetail.setCustomerType(serviceProjectAsset.getCustomerType());
                serviceProjectReduceDetail.setStoreCode(serviceProjectAsset.getStoreCode());
                serviceProjectReduceDetail.setStoreId(serviceProjectAsset.getStoreId());
                serviceProjectReduceDetail.setStoreName(serviceProjectAsset.getStoreName());
                serviceProjectReduceDetail.setGuideId(serviceProjectAsset.getGuideId());
                serviceProjectReduceDetail.setGuideName(serviceProjectAsset.getGuideName());
                serviceProjectReduceDetail.setCreateTime(serviceProjectAsset.getCreateTime());
                for (ServiceProjectAsset serviceProjectAsset1 : serviceProjectAssetList) {
                    if (serviceProjectAsset.getConsumptionPattern().equals(0) && serviceProjectAsset.getIsDirectCustom().equals(0)) {
                        serviceProjectAsset1.setRemainCount(serviceProjectAsset1.getLimitCount() - 1);
                    } else if (serviceProjectAsset.getConsumptionPattern().equals(0)) {
                        serviceProjectAsset1.setRemainCount(serviceProjectAsset1.getLimitCount());
                    }
                }
                serviceProjectReduceDetail.setServiceProjectAssetList(serviceProjectAssetList);
            } else {
                // 扣减单和退次单查询查询
                ReduceDetailRequest reduceDetailRequest = new ReduceDetailRequest();
                reduceDetailRequest.setStoreId(storeId);
                reduceDetailRequest.setOrderId(orderId);
                reduceDetailRequest.setOrderType(orderType);
                serviceProjectReduceDetail = serviceProjectReduceDetailDao.selectReduceDetailByReduceId(reduceDetailRequest);
                if(serviceProjectReduceDetail==null){
                    LOGGER.info("未查询到对应的订单信息");
                    return HttpResponse.failure(ResultCode.SELECT_ASSET_BY_ORDER_Id_FAIL);
                }
                LOGGER.info("根据订单id和订单类型查询订单信息完成,查询结果为{}", serviceProjectReduceDetail);
            }
            return HttpResponse.success(serviceProjectReduceDetail);
        } catch (Exception e) {
            LOGGER.error("根据订单id和订单类型查询订单信息异常", e);
            return HttpResponse.failure(ResultCode.SELECT_ORDER_INFO_EXCEPTION);
        }
    }

    @Override
    public HttpResponse insertReduceDetail(ServiceProjectReduceDetail serviceProjectReduceDetail) {
        try {
            LOGGER.info("添加服务项目扣减信息，请求参数serviceProjectReduceDetail为{}", serviceProjectReduceDetail);
            Integer record = serviceProjectReduceDetailDao.insertReduceDetail(serviceProjectReduceDetail);
            if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                LOGGER.info("添加服务项目扣减信息失败，请求参数serviceProjectReduceDetail为{}", serviceProjectReduceDetail);
                return HttpResponse.failure(ResultCode.INSERT_REDUCE_DETAIL_FAIL);
            }
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.error("添加服务项目扣减信息异常，请求参数serviceProjectReduceDetail为{}", serviceProjectReduceDetail, e);
            throw new RuntimeException(ResultCode.INSERT_REDUCE_DETAIL_EXCEPTION.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse insertReduceDetailList(List<ServiceProjectReduceDetail> serviceProjectReduceDetailList) {
        try {
            LOGGER.info("批量添加服务项目扣减信息，请求参数serviceProjectReduceDetailList为{}", serviceProjectReduceDetailList);
            if (CollectionUtils.isEmpty(serviceProjectReduceDetailList)) {
                LOGGER.info("批量添加服务项目扣减信息，参数为空");
                return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
            }
            serviceProjectReduceDetailList.forEach(serviceProjectReduceDetail -> {
                serviceProjectReduceDetail.setOrderId(IdUtil.orderId());
            });
            Integer record = serviceProjectReduceDetailDao.insertReduceDetailList(serviceProjectReduceDetailList);
            if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                LOGGER.info("批量添加服务项目扣减信息，请求参数serviceProjectReduceDetailList为{}", serviceProjectReduceDetailList);
                return HttpResponse.failure(ResultCode.INSERT_REDUCE_DETAIL_FAIL);
            }
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.error("批量添加服务项目扣减信息异常，请求参数serviceProjectReduceDetailList为{}", serviceProjectReduceDetailList, e);
            throw new RuntimeException(ResultCode.INSERT_REDUCE_DETAIL_LIST_EXCEPTION.getMessage());
        }
    }






    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse insertAssetAndReduceDetailList(ReduceAndAssetRequest reduceAndAssetRequest) {
        try {
            LOGGER.info("批量添加服务项目扣减信息和服务商品资产信息，请求参数reduceAndAssetRequest为{}", reduceAndAssetRequest);
            if (reduceAndAssetRequest == null) {
                LOGGER.info("批量添加服务项目扣减信息和服务商品资产信息，参数为空");
                return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
            }
            List<ServiceProjectReduceDetail> serviceProjectReduceDetailList = reduceAndAssetRequest.getServiceProjectReduceDetailList();
            List<ServiceProjectAsset> serviceProjectAssetList = reduceAndAssetRequest.getServiceProjectAssetList();
            if (CollectionUtils.isNotEmpty(serviceProjectReduceDetailList)) {
                Integer record = serviceProjectReduceDetailDao.insertReduceDetailList(serviceProjectReduceDetailList);
                if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                    LOGGER.info("批量添加服务项目扣减信息失败，请求参数serviceProjectReduceDetailList为{}", serviceProjectReduceDetailList);
                    throw new RuntimeException(ResultCode.INSERT_REDUCE_DETAIL_FAIL.getMessage());
                }
            }
            if (CollectionUtils.isNotEmpty(serviceProjectAssetList)) {
                //先删除，在重新插入
                Integer deleteRecode = serviceProjectAssetDao.deleteList(serviceProjectAssetList);
                if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(deleteRecode)) {
                    LOGGER.info("批量删除服务项目资产信息失败，请求参数serviceProjectAssetList为{}", serviceProjectAssetList);
                    throw new RuntimeException(ResultCode.DELETE_SOURCE_LIST_FAIL.getMessage());
                }
                Integer insertRecord = serviceProjectAssetDao.insertList(serviceProjectAssetList);
                if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(deleteRecode) || !(insertRecord.equals(deleteRecode))) {
                    LOGGER.info("批量更新服务项目资产信息失败，请求参数serviceProjectAssetList为{}", serviceProjectAssetList);
                    throw new RuntimeException(ResultCode.UPDATE_SOURCE_LIST_FAIL.getMessage());
                }
            }
            return HttpResponse.success(true);
        } catch (Exception e) {
            LOGGER.error("批量添加服务项目扣减信息和服务商品资产信息异常，请求参数reduceAndAssetRequest为{}", reduceAndAssetRequest, e);
            throw new RuntimeException(ResultCode.INSERT_ASSET_AND_REDUCE_DETAIL_LIST_EXCEPTION.getMessage());
        }
    }



    @Override
    public HttpResponse selectAssetByOrderCode(String orderCode) {
        try {
            LOGGER.info("根据订单编号查询用户服务项目资产list，请求参数orderCode为{}", orderCode);
            List<ServiceProjectAsset> serviceProjectAssetList = serviceProjectAssetDao.selectServiceProjectAssetByPhone(null, null, orderCode, null);
            LOGGER.info("根据订单编号查询用户服务项目资产list，请求参数orderCode为{},查询结果为{}", orderCode, serviceProjectAssetList);
            return HttpResponse.success(serviceProjectAssetList);
        } catch (Exception e) {
            LOGGER.error("根据订单编号查询用户服务项目资产list异常，请求参数orderCode为{}", orderCode, e);
            return HttpResponse.failure(ResultCode.SELECT_ASSET_BY_ORDER_CODE_EXCEPTION);
        }
    }

    @Override
    public HttpResponse selectServiceProjectReduceDetail(String assetId) {
        try {
            LOGGER.info("根据资产id查询用户服务项目消费记录list，请求参数assetId为{}", assetId);
            List<ServiceProjectReduceDetail> serviceProjectReduceDetailList = serviceProjectReduceDetailDao.selectReduceDetailByAssetId(assetId);
            LOGGER.info("根据资产id查询用户服务项目消费记录list，请求参数assetId为{},查询结果为{}", assetId, serviceProjectReduceDetailList);
            return HttpResponse.success(serviceProjectReduceDetailList);
        } catch (Exception e) {
            LOGGER.error("根据资产id查询用户服务项目消费记录list异常，请求参数assetId为{}", assetId, e);
            return HttpResponse.failure(ResultCode.SELECT_ASSET_BY_ASSET_ID_EXCEPTION);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HttpResponse insertReduceAndUpdateAsset(ReduceAndAssetRequest reduceDetailAsset) {
        try {
            LOGGER.info("添加服务项目扣减信息和更新资产信息，请求参数reduceDetailAsset为{}", reduceDetailAsset);
            if (reduceDetailAsset.getServiceProjectReduceDetail() != null) {
                Integer record = serviceProjectReduceDetailDao.insertReduceDetail(reduceDetailAsset.getServiceProjectReduceDetail());
                if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                    LOGGER.info("添加服务项目扣减信息失败，请求参数serviceProjectReduceDetail为{}", reduceDetailAsset.getServiceProjectReduceDetail());
                    return HttpResponse.failure(ResultCode.INSERT_REDUCE_DETAIL_FAIL);
                }
            }
            ServiceProjectAsset serviceProjectAsset = reduceDetailAsset.getServiceProjectAsset();
            if (serviceProjectAsset != null) {
                if (serviceProjectAsset.getConsumptionPattern().equals(0) && serviceProjectAsset.getRemainCount() == 0) {
                    LOGGER.info("服务商品次数用完，资产状态变为否");
                    serviceProjectAsset.setUseStatus(1);
                }
                Integer record = serviceProjectAssetDao.updateSource(serviceProjectAsset);
                if (Global.CHERC_INSERT_DELETE_UPDATE_SUCCESS.equals(record)) {
                    LOGGER.info("更新用户服务商品资产信息失败，请求参数serviceProjectAsset为{}", serviceProjectAsset);
                    return HttpResponse.failure(ResultCode.UPDATE_SOURCE_FAIL);
                }
            }
            LOGGER.info("添加服务项目扣减信息和更新资产信息成功，请求参数reduceDetailAsset为{}", reduceDetailAsset);
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("添加服务项目扣减信息和更新资产信息异常，请求参数reduceDetailAsset为{}", reduceDetailAsset, e);
            throw new RuntimeException(ResultCode.INSERT_REDUCE_DETAIL_UPDATE_ASSET_EXCEPTION.getMessage());
        }
    }


}
