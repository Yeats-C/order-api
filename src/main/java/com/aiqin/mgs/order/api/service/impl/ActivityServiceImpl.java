package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.component.enums.YesOrNoEnum;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.cart.ErpOrderCartDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.copartnerArea.CopartnerAreaUp;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.*;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.product.BatchRespVo;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.CouponRuleService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.util.AuthUtil;
import com.aiqin.mgs.order.api.util.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private ErpOrderCartDao erpOrderCartDao;

    @Resource
    private ActivityGiftDao activityGiftDao;

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private CopartnerAreaRoleDao copartnerAreaRoleDao;

    @Autowired
    private CopartnerAreaService copartnerAreaService;

    @Autowired
    private CouponRuleService couponRuleService;

    @Override
    public HttpResponse<Map> activityList(Activity activity) {
        HttpResponse response = HttpResponse.success();
        activity.setStoresIds(storeIds("ERP007003"));
        LOGGER.info("查询促销活动列表activityList参数为：{}", activity);

        Integer totalCount=activityDao.totalCount(activity);
        Map data=new HashMap();
        List<Activity> activities=new ArrayList<>();
        for (Activity act:activityDao.activityList(activity)){
            int finishNum=DateUtils.truncatedCompareTo(DateUtil.getNowDate(),DateUtil.StrToDate(act.getFinishTime()), Calendar.SECOND);
            int startNum=DateUtils.truncatedCompareTo(DateUtil.getNowDate(), DateUtil.StrToDate(act.getBeginTime()), Calendar.SECOND);
            if(finishNum>0){
                act.setActivityStatus(3);//已关闭
            }else if(act.getActivityStatus()==0
                    && startNum>=0
                    && finishNum<0){
                act.setActivityStatus(2);//进行中
            }else if(act.getActivityStatus()==1
                    && startNum>=0
                    && finishNum<0){
                act.setActivityStatus(1);//未开始
            }else if(startNum<0){
                act.setActivityStatus(1);//未开始
            }
            activities.add(act);
        }
        data.put("activityList",activities);
        data.put("totalCount",totalCount);
        response.setData(data);
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
            ||null==activityRequest.getActivityProducts()){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        AuthToken authToken= AuthUtil.getCurrentAuth();

        //生成活动id
        String activityId = IdUtil.activityId();

        Activity activity=activityRequest.getActivity();
        activity.setCreateTime(new Date());
        activity.setUpdateTime(new Date());
        activity.setActivityId(activityId);
        activity.setActiveStoreRange(2);
        activity.setActivityScope(activityRequest.getActivityProducts().get(0).getActivityScope());

        //查询人员的所有的区域列表
        List<CopartnerAreaUp> roleList = copartnerAreaRoleDao.qryCopartnerAreaListBypersonId(authToken.getPersonId());
        if(null!=roleList&& 0!=roleList.size()){
            activity.setPublishingOrganization(roleList.get(0).getCopartnerAreaName());
        }

        //保存活动对应门店信息start
        List<ActivityStore> activityStoreList = activityRequest.getActivityStores();
        // 去重
        Set<ActivityStore> activityStoreSet = new HashSet<>(activityStoreList);
        activityStoreList.clear();
        activityStoreList.addAll(activityStoreSet);
        for (ActivityStore activityStore : activityStoreList) {
            if("all".equals(activityStore.getStoreId())){
                activity.setActiveStoreRange(1);
            }
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

        //保存活动主表信息start
        int activityRecord = activityDao.insertActivity(activity);
        if (activityRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
        }
        LOGGER.info("保存活动主表信息成功");
        //保存活动主表信息end

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
        if(null!=activityRequest.getActivityRules()&&0!=activityRequest.getActivityRules().size()){
            List<ActivityRule> activityRuleList = activityRequest.getActivityRules();
            // 去重
            Set<ActivityRule> activityRuleSet = new HashSet<>(activityRuleList);
            activityRuleList.clear();
            activityRuleList.addAll(activityRuleSet);
            for (ActivityRule activityRule : activityRuleList) {
                activityRule.setActivityId(activityId);
                activityRule.setCreateTime(new Date());
                activityRule.setUpdateTime(new Date());
                String ruleId=IdUtil.activityId();
                activityRule.setRuleId(ruleId);
                //如果规则为满赠，插入赠品信息
                if(activityRule.getActivityType()==2 && null!=activityRule.getGiftList()){
                    List<ActivityGift> activityGiftList=new ArrayList<>();
                    if(null!=activityRule.getGiftList() && 0!=activityRule.getGiftList().size()){
                        for(ActivityGift gift:activityRule.getGiftList()){
                            gift.setRuleId(ruleId);
                            activityGiftList.add(gift);
                        }
                        int activityGiftRecord = activityGiftDao.insertList(activityGiftList);
                        if (activityGiftRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
                        }
                    }
                }
            }
            int activityRuleRecord = activityRuleDao.insertList(activityRuleList);
            if (activityRuleRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("保存活动对应规则信息成功");
        }
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
    public HttpResponse<Map> getActivityItem(ErpOrderItem erpOrderItem) {
        LOGGER.info("查询活动详情-销售数据-活动销售列表（分页）getActivityItem参数erpOrderItem为：{}", erpOrderItem);
        HttpResponse response = HttpResponse.success();
        //只查询活动商品
        erpOrderItem.setIsActivity(YesOrNoEnum.YES.getCode());
        List<ErpOrderItem> select = erpOrderItemDao.getActivityItem(erpOrderItem);
        Integer totalCount = erpOrderItemDao.totalCount(erpOrderItem);
        Map data=new HashMap();

        if(null!=select){
            data.put("erpOrderItemList",select);
            data.put("totalCount",totalCount);
            response.setData(data);
        }else{
            return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
        }
        return response;
    }

    @Override
    public HttpResponse<ActivitySales> getActivitySalesStatistics(String activityId) {
        LOGGER.info("查询活动详情-销售数据-活动销售统计，参数activityId为: {}",activityId);
        try {
            HttpResponse response = HttpResponse.success();
            Activity activity=new Activity();
            activity.setActivityId(activityId);
            //活动相关订单销售额及活动订单数(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。)
            ActivitySales activitySales=erpOrderInfoDao.getActivitySales(activity);
            //活动商品销售额
            BigDecimal  productSales=erpOrderItemDao.getProductSales(activity);
            //活动补货门店数
            Integer storeNum=erpOrderInfoDao.getStoreNum(activity);
            //平均单价
            BigDecimal averageUnitPrice=BigDecimal.ZERO;
            if(activitySales.getActivitySalesNum().compareTo(BigDecimal.ZERO)!=0){
                averageUnitPrice=activitySales.getActivitySales().divide(activitySales.getActivitySalesNum(),2, RoundingMode.HALF_UP);
            }
            activitySales.setProductSales(productSales);
            activitySales.setStoreNum(storeNum);
            activitySales.setAverageUnitPrice(averageUnitPrice);
            response.setData(activitySales);
        return response;
        } catch (Exception e) {
            LOGGER.error("查询活动详情-销售数据-活动销售统计失败", e.getStackTrace());
            throw new RuntimeException(ResultCode.SELECT_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }

    @Override
    public HttpResponse<ActivityRequest> getActivityDetail(String activityId) {
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
            if(list!=null&& 0!=list.size()&& list.get(0)!=null){
                activityRequest.setActivity(list.get(0));
            }else{
                return HttpResponse.failure(ResultCode.NOT_HAVE_PARAM);
            }

            List<ActivityStore> activityStoreList=activityStoreDao.selectByActivityId(activityId);
            List<ActivityProduct> activityProductList=activityProductDao.activityProductList(activity);
            List<ActivityRule> activityRuleList=activityRuleDao.selectByActivityId(activityId);
            if(null!=activityRuleList){
                for (ActivityRule rule:activityRuleList){
                    if(2==rule.getActivityType()){
                        List<ActivityGift> giftList=activityGiftDao.selectByRuleId(rule.getRuleId());
                        rule.setGiftList(giftList);
                    }
                }
            }

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
                    ||null==activityRequest.getActivityProducts()){
                return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
            }
            AuthToken authToken=AuthUtil.getCurrentAuth();

            Activity activity=activityRequest.getActivity();
            activity.setUpdateTime(new Date());
            activity.setActiveStoreRange(2);
            activity.setActivityScope(activityRequest.getActivityProducts().get(0).getActivityScope());
            //查询人员的所有的区域列表
            List<CopartnerAreaUp> roleList = copartnerAreaRoleDao.qryCopartnerAreaListBypersonId(authToken.getPersonId());
            if(null!=roleList&& 0!=roleList.size()){
                activity.setPublishingOrganization(roleList.get(0).getCopartnerAreaName());
            }

            //保存活动对应门店信息start
            activityStoreDao.deleteStoreByActivityId(activity.getActivityId());
            List<ActivityStore> activityStoreList = activityRequest.getActivityStores();
            // 去重
            Set<ActivityStore> activityStoreSet = new HashSet<>(activityStoreList);
            activityStoreList.clear();
            activityStoreList.addAll(activityStoreSet);
            for (ActivityStore activityStore : activityStoreList) {
                if("all".equals(activityStore.getStoreId())){
                    activity.setActiveStoreRange(YesOrNoEnum.YES.getCode());
                }
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

            //保存活动主表信息start
            int activityRecord = activityDao.updateActivity(activity);
            if (activityRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                LOGGER.error("更新活动主表信息失败");
                return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("更新活动主表信息成功");
            //保存活动主表信息end

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
            if(null!=activityRequest.getActivityRules()&&0!=activityRequest.getActivityRules().size()) {
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
                    String ruleId = IdUtil.activityId();
                    activityRule.setRuleId(ruleId);
                    //如果规则为满赠，插入赠品信息
                    if (activityRule.getActivityType() == 2 && null != activityRule.getGiftList()) {
                        List<ActivityGift> activityGiftList = new ArrayList<>();
                        for (ActivityGift gift : activityRule.getGiftList()) {
                            gift.setRuleId(ruleId);
                            activityGiftList.add(gift);
                        }
                        int activityGiftRecord = activityGiftDao.insertList(activityGiftList);
                        if (activityGiftRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                            return HttpResponse.failure(ResultCode.ADD_ACTIVITY_INFO_EXCEPTION);
                        }
                    }
                }
                int activityRuleRecord = activityRuleDao.insertList(activityRuleList);
                if (activityRuleRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                    LOGGER.error("更新活动-规则信息失败");
                    return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
                }
                LOGGER.info("保存活动对应规则信息成功");
            }
            //保存活动对应规则信息end

            LOGGER.info("活动更新成功，活动id为{}，活动名称为{}", activity.getActivityId(), activity.getActivityName());
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("更新活动失败", e);
            throw new RuntimeException(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }

    @Override
    public HttpResponse<List<Activity>> effectiveActivityList(String storeId) {
        LOGGER.info("通过门店id爱掌柜的促销活动列表（所有生效活动）effectiveActivityList参数为：{}", storeId);
        HttpResponse response = HttpResponse.success();
        List<Activity> activityList=activityDao.effectiveActivityList(storeId);
        SpuProductReqVO spuProductReqVO=new SpuProductReqVO();
        if(null!=activityList){
            for (Activity activity:activityList){
                spuProductReqVO.setActivityId(activity.getActivityId());
                spuProductReqVO.setStoreId(storeId);
                spuProductReqVO.setPageSize(5);
                HttpResponse<PageResData<ProductSkuRespVo5>> resDataHttpResponse=skuPage(spuProductReqVO);
                activity.setPageResData(resDataHttpResponse.getData());
            }

        }
        response.setData(activityList);
        return response;
    }

    @Override
    public HttpResponse<Integer> getSkuNum(ShoppingCartRequest shoppingCartRequest) {
        LOGGER.info("返回购物车中的sku商品的数量getSkuNum参数为：{}", shoppingCartRequest);
        HttpResponse response = HttpResponse.success();
        Integer skuNum=erpOrderCartDao.getSkuNum(shoppingCartRequest);
        response.setData(skuNum);
        return response;
    }

    @Override
    public Boolean checkProcuct(ActivityParameterRequest activityParameterRequest) {
        LOGGER.info("校验商品活动是否过期checkProcuct参数为:{}", activityParameterRequest);
        if(null==activityParameterRequest.getActivityId() ||null==activityParameterRequest.getStoreId()){
            return false;
        }
        Activity act=new Activity();
        act.setActivityId(activityParameterRequest.getActivityId());
        List<ActivityProduct> activityProductList=activityProductDao.activityProductList(act);
        if(null!=activityProductList){
            List<Activity> activityList=new ArrayList<>();
            Integer activityScope=activityProductList.get(0).getActivityScope();
            if(activityScope==1){//按单品设置
                activityList=activityDao.checkProcuct(activityParameterRequest.getActivityId(),activityParameterRequest.getStoreId(),activityParameterRequest.getSkuCode(),null,null);
            }else if(activityScope==3){//按品牌设置
                activityList=activityDao.checkProcuct(activityParameterRequest.getActivityId(),activityParameterRequest.getStoreId(),null,activityParameterRequest.getProductBrandCode(),null);
            }else if(activityScope==2){//按品类设置
                List<String> categoryCodes=new ArrayList<>();
                if(StringUtils.isNotEmpty(activityParameterRequest.getProductCategoryCode())){
                    for(int k=0;k<4;k++) {
                        String str = activityParameterRequest.getProductCategoryCode().substring(0, activityParameterRequest.getProductCategoryCode().length() - 2 * k);
                        categoryCodes.add(str);
                    }
                    LOGGER.info("一二三级品类编码,"+categoryCodes);
                }
                activityList=activityDao.checkProcuct(activityParameterRequest.getActivityId(),activityParameterRequest.getStoreId(),null,null,categoryCodes);
            }else if(activityScope==4){//按单品排除
                activityList=activityDao.singleProductElimination(activityParameterRequest.getActivityId(),activityParameterRequest.getStoreId(),activityParameterRequest.getSkuCode());
            }

            if(activityList!=null && 0!=activityList.size()){
                return true;
            }else{
                return false;
            }
        }else{return false;}


    }

    @Override
    public HttpResponse<List<QueryProductBrandRespVO>> productBrandList(String productBrandName, String activityId) {
        LOGGER.info("活动商品品牌列表接口参数productBrandName为:{}", productBrandName);
        List<QueryProductBrandRespVO> queryProductBrandRespVO=new ArrayList<>();
        ActivityBrandCategoryRequest categoryRequest=new ActivityBrandCategoryRequest();
        HttpResponse response = HttpResponse.success();
        ActivityProduct activityProduct=new ActivityProduct();
        if(StringUtils.isNotEmpty(productBrandName)){
            activityProduct.setProductBrandName(productBrandName);
        }
        if(StringUtils.isNotEmpty(activityId)){
            activityProduct.setActivityId(activityId);
        }
        List<ActivityProduct> activityProducts=activityProductDao.productBrandList(activityProduct);
        if(null!=activityProducts&& activityProducts.get(0).getActivityScope()==2){
            for (ActivityProduct product:activityProducts){
                ProductCategoryAndBrandResponse2 response2= (ProductCategoryAndBrandResponse2) bridgeProductService.selectCategoryByBrandCode(product.getProductCategoryCode(),"1").getData();
                queryProductBrandRespVO.addAll(response2.getQueryProductBrandRespVO());
            }
            Set<QueryProductBrandRespVO> activitySet = new HashSet<>(queryProductBrandRespVO);
            queryProductBrandRespVO.clear();
            queryProductBrandRespVO.addAll(activitySet);
            response.setData(queryProductBrandRespVO);
            return  response;
        }if(null!=activityProducts&& activityProducts.get(0).getActivityScope()==4){
            List<String> excludeBrandIds=new ArrayList<>();
            for (ActivityProduct product:activityProducts){
                if(StringUtils.isNotBlank(product.getProductBrandCode())){
                    excludeBrandIds.add(product.getProductBrandCode());
                }
            }
            categoryRequest.setExcludeBrandIds(excludeBrandIds);
            response=bridgeProductService.productBrandList(categoryRequest);
            return response;
        }else{
            List<String> brandIds=new ArrayList<>();

            if(null!=activityProducts && 0!=activityProducts.size()){
                for (ActivityProduct product:activityProducts){
                    if(StringUtils.isNotBlank(product.getProductBrandCode())){
                         brandIds.add(product.getProductBrandCode());
                    }
                }
            }
            categoryRequest.setBrandIds(brandIds);
            response=bridgeProductService.productBrandList(categoryRequest);
            return response;
        }
    }

    @Override
    public HttpResponse<List<ProductCategoryRespVO>> productCategoryList(String activityId) {
        LOGGER.info("活动商品品类列表接口开始");
        HttpResponse response = HttpResponse.success();
        List<ProductCategoryRespVO> queryProductBrandRespVO=new ArrayList<>();
        ActivityBrandCategoryRequest categoryRequest=new ActivityBrandCategoryRequest();
        Activity activity=new Activity();
        activity.setActivityId(activityId);
        List<ActivityProduct> activityProducts=activityProductDao.productCategoryList(activity);
        if(null!=activityProducts&& activityProducts.get(0).getActivityScope()==3){
            for (ActivityProduct product:activityProducts){
                ProductCategoryAndBrandResponse2 response2= (ProductCategoryAndBrandResponse2) bridgeProductService.selectCategoryByBrandCode(product.getProductBrandCode(),"2").getData();
                queryProductBrandRespVO.addAll(response2.getProductCategoryRespVOList());
            }
            Set<ProductCategoryRespVO> activitySet = new HashSet<>(queryProductBrandRespVO);
            queryProductBrandRespVO.clear();
            queryProductBrandRespVO.addAll(activitySet);
            response.setData(queryProductBrandRespVO);
            return  response;
        }else if(null!=activityProducts&& activityProducts.get(0).getActivityScope()==4){
            List<String> excludeCategoryCodes=new ArrayList<>();
            for (ActivityProduct product:activityProducts){
                if(StringUtils.isNotBlank(product.getProductCategoryCode())){
                    excludeCategoryCodes.add(product.getProductCategoryCode());
                }
            }
            categoryRequest.setExcludeCategoryCodes(excludeCategoryCodes);
            response=bridgeProductService.excludeCategoryCodes(categoryRequest);
            return response;
        }else{
            List<String> categoryCodes=new ArrayList<>();

            if(null!=activityProducts && 0!=activityProducts.size()){
                for (ActivityProduct product:activityProducts){
                    if(StringUtils.isNotBlank(product.getProductCategoryCode())){
                        categoryCodes.add(product.getProductCategoryCode());
                    }
                }
                categoryRequest.setCategoryCodes(categoryCodes);
            }

            response=bridgeProductService.productCategoryList(categoryRequest);
            return response;
        }
    }




    @Override
    @Transactional
    public HttpResponse updateStatus(Activity activity) {
        LOGGER.info("编辑活动生效状态updateStatus参数为：{}", activity);
        try {
            if(null==activity.getActivityId()
                    ||null==activity.getActivityStatus()){
                return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
            }
            Activity act=new Activity();
            //保存活动主表信息start
            act.setUpdateTime(new Date());
            act.setActivityId(activity.getActivityId());
            Activity activity1=(Activity) getActivityInformation(activity.getActivityId()).getData();
            if(null!=activity.getActivityStatus() && activity.getActivityStatus()==1){
                act.setActivityStatus(1);
            }else if(null!=activity.getActivityStatus() && activity.getActivityStatus()==2){
                act.setActivityStatus(0);
                if(DateUtil.formatDate(activity1.getBeginTime()).after(new Date())){
                    act.setBeginTime(DateUtil.sysDate());
                }
            }
            int activityRecord = activityDao.updateActivity(act);
            if (activityRecord <= Global.CHECK_INSERT_UPDATE_DELETE_SUCCESS) {
                LOGGER.error("更新活动主表信息失败");
                return HttpResponse.failure(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION);
            }
            LOGGER.info("更新活动主表信息成功");
            //保存活动主表信息end
            LOGGER.info("活动更新成功，活动id为{}，活动名称为{}", activity.getActivityId(), activity.getActivityName());
            return HttpResponse.success();
        } catch (Exception e) {
            LOGGER.error("更新活动失败", e);
            throw new RuntimeException(ResultCode.UPDATE_ACTIVITY_INFO_EXCEPTION.getMessage());
        }
    }

    @Override
    public void excelActivityItem(ErpOrderItem erpOrderItem, HttpServletResponse response) {
        LOGGER.info("导出--活动详情-销售数据-活动销售列表excelActivityItem参数为：{}", erpOrderItem);
        try {
            //只查询活动商品
            erpOrderItem.setIsActivity(1);
            List<ErpOrderItem> select = erpOrderItemDao.getActivityItem(erpOrderItem);
            HSSFWorkbook wb = exportData(select);
            String excelName = "数据列表.xls";
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(excelName.getBytes("UTF-8"), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            throw new GroundRuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public HttpResponse<List<ActivitySales>> comparisonActivitySalesStatistics(List<String> activityIdList) {
        LOGGER.info("活动列表-对比分析柱状图comparisonActivitySalesStatistics参数为：{}", activityIdList);
        HttpResponse res = HttpResponse.success();
        List<ActivitySales> activitySalesList=new ArrayList<>();
        for (String activityId: activityIdList){
            ActivitySales sales=getActivitySalesStatistics(activityId).getData();
            Activity activity=(Activity) getActivityInformation(activityId).getData();
            sales.setActivityName(activity.getActivityName());
            activitySalesList.add(sales);
        }
        res.setData(activitySalesList);
        return res;
    }

    @Override
    public void excelActivitySalesStatistics(List<String> activityIdList, HttpServletResponse response) {
        LOGGER.info("导出--活动列表-对比分析柱状图excelActivitySalesStatistics参数为：{}", activityIdList);
        HttpResponse res = HttpResponse.success();
        try {
            List<ActivitySales> activitySalesList=new ArrayList<>();
            for (String activityId: activityIdList){
                ActivitySales sales=getActivitySalesStatistics(activityId).getData();
                activitySalesList.add(sales);
            }
            HSSFWorkbook wb = exportActivitySalesData(activitySalesList);
            String excelName = "数据列表.xls";
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(excelName.getBytes("UTF-8"), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception ex) {
            throw new GroundRuntimeException(ex.getMessage());
        }
    }

    @Override
    @Transactional
    public HttpResponse<PageResData<ProductSkuRespVo5>> skuPage(SpuProductReqVO spuProductReqVO) {
        LOGGER.info("活动商品查询（筛选+分页）skuPage参数为：{}", spuProductReqVO);
        HttpResponse res = HttpResponse.success();
        if(null==spuProductReqVO.getActivityId() ||null==spuProductReqVO.getStoreId()){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        ShoppingCartRequest shoppingCartRequest=new ShoppingCartRequest();
        shoppingCartRequest.setStoreId(spuProductReqVO.getStoreId());
        //通过门店id返回门店省市公司信息
        StoreInfo store = bridgeProductService.getStoreInfoByStoreId(shoppingCartRequest.getStoreId());
        if(store==null){
            return HttpResponse.failure(ResultCode.NO_HAVE_STORE_ERROR);
        }
        spuProductReqVO.setCompanyCode("14");
        spuProductReqVO.setProvinceCode(store.getProvinceId());
        spuProductReqVO.setCityCode(store.getCityId());
        //设置查询库房 1销售库  2特卖库
        spuProductReqVO.setWarehouseTypeCode("1");
        Activity activity=new Activity();
        activity.setActivityId(spuProductReqVO.getActivityId());
        List<ActivityProduct> activityProducts=activityProductDao.activityProductList(activity);
        Activity act=getActivityInformation(spuProductReqVO.getActivityId()).getData();
        if(null!=activityProducts && 0!=activityProducts.size()){
            Integer activityScope=activityProducts.get(0).getActivityScope();
            List<String> parameterList=new ArrayList<>();
            List<ActivityCategoryRequest> categoryRequestList=new ArrayList<>();
            for(ActivityProduct product:activityProducts){
                if(activityScope==1){
                    //按单品设置
                    parameterList.add(product.getSkuCode());
                }else if(activityScope==2){
                    //按品类设置
                    ActivityCategoryRequest categoryRequest=new ActivityCategoryRequest();
                    categoryRequest.setCategoryCode(product.getProductCategoryCode());
                    categoryRequest.setLevel(product.getLevel());
                    categoryRequestList.add(categoryRequest);
                }else if(activityScope==3){
                    //按品牌设置
                    parameterList.add(product.getProductBrandCode());
                }else if(activityScope==4){
                    //按单品排除
                    parameterList.add(product.getSkuCode());
                }
            }
            if(activityScope==1){
                spuProductReqVO.setIncludeSkuCodes(parameterList);
            }else if(activityScope==2){
                spuProductReqVO.setCategoryCodeList(categoryRequestList);
            }else if(activityScope==3){
                spuProductReqVO.setBrandCodeList(parameterList);
            }else if(activityScope==4){
                spuProductReqVO.setExcludeSkuCodes(parameterList);
            }
            res = bridgeProductService.getSkuPage(spuProductReqVO);
            ActivityParameterRequest activityParameterRequest=new ActivityParameterRequest();
            activityParameterRequest.setActivityId(spuProductReqVO.getActivityId());
            activityParameterRequest.setStoreId(spuProductReqVO.getStoreId());
            Map dataMap=couponRuleService.couponRuleMap();
            if(null!=res.getData()){
                PageResData<ProductSkuRespVo5> pageResData= (PageResData<ProductSkuRespVo5>) res.getData();
                List<ProductSkuRespVo5> productSkuRespVo=pageResData.getDataList();

                for (ProductSkuRespVo5 vo:productSkuRespVo){
                    activityParameterRequest.setSkuCode(vo.getSkuCode());
                    activityParameterRequest.setProductBrandCode(vo.getProductBrandCode());
                    activityParameterRequest.setProductCategoryCode(vo.getProductCategoryCode());
                    vo.setActivityList(activityList(activityParameterRequest));

                    shoppingCartRequest.setProductId(vo.getSkuCode());
                    Integer cartNum=getSkuNum(shoppingCartRequest).getData();
                    if (null == cartNum) {
                        cartNum=0;
                    }
                    vo.setCartNum(cartNum);
                    vo.setStoreStockSkuNum(bridgeProductService.getStoreStockSkuNum(shoppingCartRequest));

                    if(null!=vo.getSkuStock()){
                        if(vo.getSkuStock()>10){
                            vo.setStoreStockExplain("有货");
                        }else if(vo.getSkuStock()<=0){
                            vo.setStoreStockExplain("缺货");
                        }else if(vo.getSkuStock()>0 && vo.getSkuStock()<=10){
                            vo.setStoreStockExplain("库存紧张");
                        }
                    }else{
                        vo.setStoreStockExplain("缺货");
                    }
                    if(StringUtils.isEmpty(vo.getItroImages())){
                        vo.setItroImages("无");
                    }
                    if(dataMap.containsKey(vo.getProductPropertyCode())){
                        //可使用优惠券
                        vo.setCouponRule(YesOrNoEnum.YES.getCode());
                    }

                    if(null!=vo.getBatchList()&&vo.getBatchList().size()>0){
                        for(BatchRespVo batchRespVo:vo.getBatchList()){
                            batchRespVo.setActivityType(act.getActivityType());
                        }
                    }
                }
            }else{
                return HttpResponse.failure(ResultCode.SELECT_ACTIVITY_INFO_EXCEPTION_BY_PRODUCT);
            }

        }else{
            return HttpResponse.failure(ResultCode.SELECT_ACTIVITY_INFO_EXCEPTION);
        }
        return res;
    }

    /***********************************************工具方法**************************************************/
    public static HSSFWorkbook exportData(List<ErpOrderItem> list) {
        // 创建工作空间
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建表
        HSSFSheet sheet = wb.createSheet("数据空间");
        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeightInPoints(20);

        // 创建行
        HSSFRow row = sheet.createRow(0);

        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

        // 背景色
        style.setFillForegroundColor(HSSFColor.TAN.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillBackgroundColor(HSSFColor.DARK_RED.index);

        // 设置边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        // 生成一个字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontName("宋体");


        // 把字体 应用到当前样式
        style.setFont(font);
        // 添加表头数据
        String[] excelHeader = { "门店编码", "门店名称", "订单号", "商品名称", "是否活动商品","订货数量","订货金额","订单状态","订单时间"};
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        // 添加单元格数据
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            ErpOrderItem item = list.get(i);
            if(StringUtils.isNotBlank(item.getStoreCode())) {
                row.createCell(0).setCellValue(item.getStoreCode());
            }else {
                row.createCell(0).setCellValue("");
            }
            if(StringUtils.isNotBlank(item.getStoreName())) {
                row.createCell(1).setCellValue(item.getStoreName());
            }else {
                row.createCell(1).setCellValue("");
            }
            if(StringUtils.isNotBlank(item.getOrderStoreCode())) {
                row.createCell(2).setCellValue(item.getOrderStoreCode());
            }else {
                row.createCell(2).setCellValue("");
            }
            if(StringUtils.isNotBlank(item.getSkuName())) {
                row.createCell(3).setCellValue(item.getSkuName());
            }else {
                row.createCell(3).setCellValue("");
            }

            row.createCell(4).setCellValue("是");

            if(item.getProductCount()!=null) {
                row.createCell(5).setCellValue(item.getProductCount());
            }else {
                row.createCell(5).setCellValue(0);
            }
            if(item.getActualTotalProductAmount()!=null) {
                row.createCell(6).setCellValue(item.getActualTotalProductAmount().intValue());
            }else {
                row.createCell(6).setCellValue(0);
            }
            if(item.getOrderStatus()!=null) {
                row.createCell(7).setCellValue(ErpOrderStatusEnum.getEnumDesc(item.getOrderStatus()));
            }else {
                row.createCell(7).setCellValue("");
            }
            if(item.getCreateTime()!=null) {
                row.createCell(8).setCellValue(DateUtil.formatDate(item.getCreateTime()));
            }else {
                row.createCell(8).setCellValue("");
            }
        }
        return wb;
    }

    /**
     * 根据父节点和所有子节点集合获取父节点下得子节点集合
     * @param parentId
     * @param children
     * @return
     */
    public List<ActivityProduct> getChildren(String parentId,List<ActivityProduct> children){
        List<ActivityProduct> list = new ArrayList<>();
        children.forEach(item->{
            if (parentId.equals(item.getProductCategoryCode())){
                item.setActivityProductList(getChildren(String.valueOf(item.getProductCategoryCode()),children));
                list.add(item);
            }
        });
        return list;
    }


    public static HSSFWorkbook exportActivitySalesData(List<ActivitySales> list) {
        // 创建工作空间
        HSSFWorkbook wb = new HSSFWorkbook();
        // 创建表
        HSSFSheet sheet = wb.createSheet("数据空间");
        sheet.setDefaultColumnWidth(20);
        sheet.setDefaultRowHeightInPoints(20);

        // 创建行
        HSSFRow row = sheet.createRow(0);

        // 生成一个样式
        HSSFCellStyle style = wb.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平居中
        style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直居中

        // 背景色
        style.setFillForegroundColor(HSSFColor.TAN.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillBackgroundColor(HSSFColor.DARK_RED.index);

        // 设置边框
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        // 生成一个字体
        HSSFFont font = wb.createFont();
        font.setFontHeightInPoints((short) 20);
        font.setColor(HSSFColor.VIOLET.index);
        font.setFontName("宋体");


        // 把字体 应用到当前样式
        style.setFont(font);
        // 添加表头数据
        String[] excelHeader = { "活动相关订单销售额", "活动商品销售额", "补货门店数", "平均客单价"};
        for (int i = 0; i < excelHeader.length; i++) {
            HSSFCell cell = row.createCell(i);
            cell.setCellValue(excelHeader[i]);
            cell.setCellStyle(style);
        }
        // 添加单元格数据
        for (int i = 0; i < list.size(); i++) {
            row = sheet.createRow(i + 1);
            ActivitySales item = list.get(i);
            if(null!=item.getActivitySales()) {
                row.createCell(0).setCellValue(item.getActivitySales().doubleValue());
            }else {
                row.createCell(0).setCellValue(0);
            }
            if(null!=item.getProductSales()) {
                row.createCell(1).setCellValue(item.getProductSales().doubleValue());
            }else {
                row.createCell(1).setCellValue(0);
            }
            if(null!=item.getStoreNum()) {
                row.createCell(2).setCellValue(item.getStoreNum());
            }else {
                row.createCell(2).setCellValue(0);
            }
            if(null!=item.getAverageUnitPrice()) {
                row.createCell(3).setCellValue(item.getAverageUnitPrice().doubleValue());
            }else {
                row.createCell(3).setCellValue(0);
            }

        }
        return wb;
    }

    /**
     * 作用:遍历树形菜单得到所有的id
     * @param root
     * @param result
     * @return
     */
    //前序遍历得到所有的id List
    private static List<String> ergodicList(List<ProductCategoryRespVO> root, List<String> result){
        for (int i = 0; i < root.size(); i++) {
            // 查询某节点的子节点（获取的是list）
            result.add(root.get(i).getCategoryId());//前序遍历
            if (null != root.get(i).getProductCategoryRespVOS()) {
                List<ProductCategoryRespVO> children = root.get(i).getProductCategoryRespVOS();
                ergodicList(children,result);
            }
        }
        return result;
    }

    /**
     * 通过条件查询一个商品有多少个进行中的活动
     * @return
     */
    @Override
    public List<Activity> activityList(ActivityParameterRequest activityParameterRequest){
        LOGGER.info("通过条件查询一个商品有多少个进行中的活动activityList参数activityParameterRequest为：{}", activityParameterRequest);
        List<String> categoryCodes=new ArrayList<>();
        if(StringUtils.isNotEmpty(activityParameterRequest.getProductCategoryCode())){

            for(int k=0;k<4;k++) {
                String str = activityParameterRequest.getProductCategoryCode().substring(0, activityParameterRequest.getProductCategoryCode().length() - 2 * k);
                categoryCodes.add(str);
            }
            LOGGER.info("一二三级品类编码,"+categoryCodes);
        }


        List<Activity> activityList=activityDao.checkProcuct(null,activityParameterRequest.getStoreId(),activityParameterRequest.getSkuCode(),null,null);
        List<Activity> activityListByBrand=activityDao.checkProcuct(null,activityParameterRequest.getStoreId(),null,activityParameterRequest.getProductBrandCode(),null);
        List<Activity> activityListByCategory=activityDao.checkProcuct(null,activityParameterRequest.getStoreId(),null,null,categoryCodes);
        List<Activity> singleProductElimination=activityDao.singleProductElimination(null,activityParameterRequest.getStoreId(),activityParameterRequest.getSkuCode());
        if(null==activityList&&null==activityListByBrand&&null==activityListByCategory &&null==singleProductElimination){
            return new ArrayList<Activity>();
        }
        if(null==activityList){
            activityList=new ArrayList<>();
        }
        if(null!=activityListByBrand){
            for (Activity act:activityListByBrand){
                activityList.add(act);
            }
        }
        if(null!=activityListByCategory){
            for (Activity act:activityListByCategory){
                activityList.add(act);
            }
        }
        if(null!=singleProductElimination){
            for (Activity act:singleProductElimination){
                activityList.add(act);
            }
        }
        // 去重
        Set<Activity> activitySet = new HashSet<>(activityList);
        activityList.clear();
        activityList.addAll(activitySet);
        return activityList;
    }

    @Override
    public List<String> storeIds(String code){
        AuthToken authToken= AuthUtil.getCurrentAuth();
        LOGGER.info("调用合伙人数据权限控制公共接口入参,personId={},resourceCode={}",authToken.getPersonId(),code);
        HttpResponse httpResponse = copartnerAreaService.selectStoreByPerson(authToken.getPersonId(), code);
        List<PublicAreaStore> dataList = JSONArray.parseArray(JSON.toJSONString(httpResponse.getData()), PublicAreaStore.class);
        LOGGER.info("调用合伙人数据权限控制公共接口返回结果,dataList={}",dataList);
        if (dataList == null || dataList.size() == 0) {
            return null;
        }
        //遍历门店id
        List<String> storesIds = dataList.stream().map(PublicAreaStore::getStoreId).collect(Collectors.toList());
        LOGGER.info("门店ids={}",storesIds);
        return storesIds;
    }

    @Override
    public ProductCategoryAndBrandResponse2 ProductCategoryAndBrandResponse(String conditionCode, String type, String activityId) {
        LOGGER.info("品牌品类对应关系查询接口ProductCategoryAndBrandResponse参数conditionCode为={},参数type为={},参数activityId为={}",conditionCode,type,activityId);
        HttpResponse res=  bridgeProductService.selectCategoryByBrandCode(conditionCode,type);
        ProductCategoryAndBrandResponse2 response2=new ProductCategoryAndBrandResponse2();
        if(null!=res.getData()){
             response2= (ProductCategoryAndBrandResponse2)res.getData();
        }

        Activity activity=new Activity();
        activity.setActivityId(activityId);
        List<ActivityProduct> activityProducts=activityProductDao.activityProductList(activity);
        if(null!=activityProducts&&0!=activityProducts.size()){
            if ("1".equals(type) &&2!=activityProducts.get(0).getActivityScope()&&4!=activityProducts.get(0).getActivityScope()){
                //type=1 通过品类查品牌
                List<QueryProductBrandRespVO> queryProductBrandRespVO=new ArrayList<>();
                Iterator<ActivityProduct> it = activityProducts.iterator();
                while(it.hasNext()){
                    ActivityProduct str = it.next();
                    for(QueryProductBrandRespVO brand:response2.getQueryProductBrandRespVO()){
                        if(null==str.getProductBrandCode()){
                            queryProductBrandRespVO.add(brand);
                        }else if (null!=str.getProductBrandCode()&&str.getProductBrandCode().equals(brand.getBrandId())){
                            queryProductBrandRespVO.add(brand);
                        }
                    }
                }
                //去重
                Set<QueryProductBrandRespVO> activitySet = new HashSet<>(queryProductBrandRespVO);
                queryProductBrandRespVO.clear();
                queryProductBrandRespVO.addAll(activitySet);
                response2.setQueryProductBrandRespVO(queryProductBrandRespVO);
            }else if("2".equals(type)&&3!=activityProducts.get(0).getActivityScope()){
                //type=2 通过品牌查品类
                List<ProductCategoryRespVO> productCategoryRespVOList=new ArrayList<>();
                Iterator<ActivityProduct> it = activityProducts.iterator();
                while(it.hasNext()){
                    ActivityProduct str = it.next();
                    for(ProductCategoryRespVO cate:response2.getProductCategoryRespVOList()){
                        if(null==str.getProductCategoryCode()){
                            productCategoryRespVOList.add(cate);
                        }else if(null!=str.getProductCategoryCode()&&str.getProductCategoryCode().substring(0,2).equals(cate.getCategoryId())){
                            productCategoryRespVOList.add(cate);
                        }
                    }
                }
                //去重
                Set<ProductCategoryRespVO> activitySet = new HashSet<>(productCategoryRespVOList);
                productCategoryRespVOList.clear();
                productCategoryRespVOList.addAll(activitySet);
                response2.setProductCategoryRespVOList(productCategoryRespVOList);
            }
        }
        return response2;
    }



}