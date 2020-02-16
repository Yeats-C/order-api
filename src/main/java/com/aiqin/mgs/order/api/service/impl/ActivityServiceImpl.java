package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.ActivityDao;
import com.aiqin.mgs.order.api.dao.ActivityProductDao;
import com.aiqin.mgs.order.api.dao.ActivityRuleDao;
import com.aiqin.mgs.order.api.dao.ActivityStoreDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author csf
 */
@Service
public class ActivityServiceImpl implements ActivityService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityServiceImpl.class);

    @Resource
    private ActivityDao activityDao;

    @Resource
    private ActivityStoreDao activityStoreDao;

    @Resource
    private ActivityRuleDao activityRuleDao;

    @Resource
    private ActivityProductDao activityProductDao;

    @Resource
    private ErpOrderItemDao erpOrderItemDao;

    @Resource
    private ErpOrderInfoDao erpOrderInfoDao;

    @Override
    public HttpResponse<List<Activity>> activityList(Activity activity) {
        LOGGER.info("查询促销活动列表activityList参数为：{}", activity);
        HttpResponse response = HttpResponse.success();
        response.setData(activityDao.activityList(activity));
        return response;
    }

    @Override
    @Transactional
    public HttpResponse<Activity> getActivityInformation(String activityId) {
        LOGGER.info("查询单个促销活动getActivityInformation参数activityId为：{}", activityId);
        HttpResponse response = HttpResponse.success();
        if(null==activityId){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        Activity activity=new Activity();
        activity.setActivityId(activityId);
        List<Activity> list=activityDao.activityList(activity);
        if(list!=null && list.get(0)!=null){
            Activity activityData=list.get(0);
            response.setData(activityData);
        }else{
            return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
        }
        return response;
    }

    @Override
    @Transactional
    public HttpResponse addActivity(ActivityRequest activityRequest) {
        try {
        LOGGER.info("新增活动addActivity参数为：{}", activityRequest);
        if(null==activityRequest
            ||null==activityRequest.getActivity()
            ||null==activityRequest.getActivityStores()
            ||null==activityRequest.getActivityProducts()
            ||null==activityRequest.getActivityRules()){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        //生成活动id
        String activityId = IdUtil.activityId();
        //保存活动主表信息start
        Activity activity=activityRequest.getActivity();
        activity.setCreateTime(new Date());
        activity.setUpdateTime(new Date());
        activity.setActivityId(activityId);
        int activityRecord = activityDao.insertActivity(activity);
        if (activityRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
        }
        LOGGER.info("保存活动主表信息成功");
        //保存活动主表信息end

        //保存活动对应门店信息start
        List<ActivityStore> activityStoreList = activityRequest.getActivityStores();
        // 去重
        Set<ActivityStore> activityStoreSet = new HashSet<>(activityStoreList);
        activityStoreList.clear();
        activityStoreList.addAll(activityStoreSet);
        for (ActivityStore activityStore : activityStoreList) {
            activityStore.setActivityId(activityId);
            activityStore.setCreateTime(new Date());
            activityStore.setUpdateTime(new Date());
        }
        int activityStoreRecord = activityStoreDao.insertList(activityStoreList);
        if (activityStoreRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
        }
        LOGGER.info("保存活动对应门店信息成功");
        //保存活动对应门店信息end

        //保存活动对应商品信息start
        List<ActivityProduct> activityProductList = activityRequest.getActivityProducts();
        // 去重
        Set<ActivityProduct> activityProductSet = new HashSet<>(activityProductList);
        activityProductList.clear();
        activityProductList.addAll(activityProductSet);
        for (ActivityProduct activityProduct : activityProductList) {
            activityProduct.setActivityId(activityId);
            activityProduct.setCreateTime(new Date());
            activityProduct.setUpdateTime(new Date());
        }
        int activityProductRecord = activityProductDao.insertList(activityProductList);
        if (activityProductRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
        }
        LOGGER.info("保存活动对应商品信息成功");
        //保存活动对应商品信息end

        //保存活动对应规则信息start
        List<ActivityRule> activityRuleList = activityRequest.getActivityRules();
        // 去重
        Set<ActivityRule> activityRuleSet = new HashSet<>(activityRuleList);
        activityRuleList.clear();
        activityRuleList.addAll(activityRuleSet);
        for (ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityId);
            activityRule.setCreateTime(new Date());
            activityRule.setUpdateTime(new Date());
        }
        int activityRuleRecord = activityRuleDao.insertList(activityRuleList);
        if (activityRuleRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
        }
        LOGGER.info("保存活动对应规则信息成功");
        //保存活动对应规则信息end
        LOGGER.info("活动添加成功，活动id为{}，活动名称为{}", activityId, activity.getActivityName());
        return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("添加活动失败", e);
            throw new RuntimeException(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }

    @Override
    public HttpResponse<List<ActivityProduct>> activityProductList(Activity activity) {
        LOGGER.info("查询单个促销活动的商品列表（分页）activityProductList参数activity为：{}", activity);
        HttpResponse response = HttpResponse.success();

        if(null==activity.getActivityId()){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        List<ActivityProduct> list=activityProductDao.activityProductList(activity);
        if(null!=list){
            response.setData(list);
        }else{
            return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
        }
        return response;
    }

    @Override
    public HttpResponse<List<Activity>> getActivityItem(ErpOrderItem erpOrderItem) {
        LOGGER.info("查询活动详情-销售数据-活动销售列表（分页）getActivityItem参数erpOrderItem为：{}", erpOrderItem);
        //只查询活动商品
        erpOrderItem.setIsActivity(1);
        List<ErpOrderItem> select = erpOrderItemDao.select(erpOrderItem);
        HttpResponse response = HttpResponse.success();
        if(null!=select){
            response.setData(select);
        }else{
            return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
        }
        return response;
    }

    @Override
    public HttpResponse<Map> getActivitySalesStatistics() {
        LOGGER.info("查询活动详情-销售数据-活动销售统计");
        try {
            HttpResponse response = HttpResponse.success();
            //活动相关订单销售额及活动订单数(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。)
            ActivitySales activitySales=erpOrderInfoDao.getActivitySales();
            //活动商品销售额
            BigDecimal  productSales=erpOrderItemDao.getProductSales();
            //活动补货门店数
            Integer storeNum=erpOrderInfoDao.getStoreNum();
            //平均单价
            BigDecimal averageUnitPrice=activitySales.getActivitySales().divide(new BigDecimal(activitySales.getActivitySalesNum()));
            activitySales.setProductSales(productSales);
            activitySales.setStoreNum(storeNum);
            activitySales.setAverageUnitPrice(averageUnitPrice);
            response.setData(activitySales);
        return response;
        } catch (Exception e) {
            LOGGER.error("查询活动详情-销售数据-活动销售统计失败", e);
            throw new RuntimeException(ResultCode.SELECT_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }

    @Override
    public HttpResponse<Activity> getActivityDetail(String activityId) {
        LOGGER.info("查询单个促销活动详情getActivityDetail参数activityId为：{}", activityId);
        try {
            HttpResponse response = HttpResponse.success();
            if(null==activityId){
                return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
            }
            ActivityRequest activityRequest=new ActivityRequest();
            Activity activity=new Activity();
            activity.setActivityId(activityId);
            List<Activity> list=activityDao.activityList(activity);
            if(list!=null && list.get(0)!=null){
                Activity activityData=list.get(0);
            }else{
                return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
            }

            List<ActivityStore> activityStoreList=activityStoreDao.selectByActivityId(activityId);
            List<ActivityProduct> activityProductList=activityProductDao.activityProductList(activity);
            List<ActivityRule> activityRuleList=activityRuleDao.selectByActivityId(activityId);
            activityRequest.setActivity(activity);
            activityRequest.setActivityStores(activityStoreList);
            activityRequest.setActivityProducts(activityProductList);
            activityRequest.setActivityRules(activityRuleList);
            response.setData(activityRequest);
            return response;
        } catch (Exception e) {
            LOGGER.error("查询单个促销活动详情失败", e);
            throw new RuntimeException(ResultCode.SELECT_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }

    @Override
    @Transactional
    public HttpResponse updateActivity(ActivityRequest activityRequest) {
        LOGGER.info("编辑活动updateActivity参数为：{}", activityRequest);
        try {
            if(null==activityRequest
                    ||null==activityRequest.getActivity()
                    ||null==activityRequest.getActivityStores()
                    ||null==activityRequest.getActivityProducts()
                    ||null==activityRequest.getActivityRules()){
                return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
            }
            //保存活动主表信息start
            Activity activity=activityRequest.getActivity();
            activity.setUpdateTime(new Date());
            int activityRecord = activityDao.updateActivity(activity);
            if (activityRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                LOGGER.error("更新活动主表信息失败");
                return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("更新活动主表信息成功");
            //保存活动主表信息end

            //保存活动对应门店信息start
            activityStoreDao.deleteStoreByActivityId(activity.getActivityId());
            List<ActivityStore> activityStoreList = activityRequest.getActivityStores();
            // 去重
            Set<ActivityStore> activityStoreSet = new HashSet<>(activityStoreList);
            activityStoreList.clear();
            activityStoreList.addAll(activityStoreSet);
            for (ActivityStore activityStore : activityStoreList) {
                activityStore.setActivityId(activity.getActivityId());
                activityStore.setCreateTime(new Date());
                activityStore.setUpdateTime(new Date());
            }
            int activityStoreRecord = activityStoreDao.insertList(activityStoreList);
            if (activityStoreRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                LOGGER.error("更新活动-门店信息失败");
                return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("保存活动对应门店信息成功");
            //保存活动对应门店信息end

            //保存活动对应商品信息start
            activityProductDao.deleteProductByActivityId(activity.getActivityId());
            List<ActivityProduct> activityProductList = activityRequest.getActivityProducts();
            // 去重
            Set<ActivityProduct> activityProductSet = new HashSet<>(activityProductList);
            activityProductList.clear();
            activityProductList.addAll(activityProductSet);
            for (ActivityProduct activityProduct : activityProductList) {
                activityProduct.setActivityId(activity.getActivityId());
                activityProduct.setCreateTime(new Date());
                activityProduct.setUpdateTime(new Date());
            }
            int activityProductRecord = activityProductDao.insertList(activityProductList);
            if (activityProductRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                LOGGER.error("更新活动-商品信息失败");
                return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("保存活动对应商品信息成功");
            //保存活动对应商品信息end

            //保存活动对应规则信息start
            activityRuleDao.deleteRuleByActivityId(activity.getActivityId());
            List<ActivityRule> activityRuleList = activityRequest.getActivityRules();
            // 去重
            Set<ActivityRule> activityRuleSet = new HashSet<>(activityRuleList);
            activityRuleList.clear();
            activityRuleList.addAll(activityRuleSet);
            for (ActivityRule activityRule : activityRuleList) {
                activityRule.setActivityId(activity.getActivityId());
                activityRule.setCreateTime(new Date());
                activityRule.setUpdateTime(new Date());
            }
            int activityRuleRecord = activityRuleDao.insertList(activityRuleList);
            if (activityRuleRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                LOGGER.error("更新活动-规则信息失败");
                return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("保存活动对应规则信息成功");
            //保存活动对应规则信息end
            LOGGER.info("活动更新成功，活动id为{}，活动名称为{}", activity.getActivityId(), activity.getActivityName());
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("更新活动失败", e);
            throw new RuntimeException(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }
}