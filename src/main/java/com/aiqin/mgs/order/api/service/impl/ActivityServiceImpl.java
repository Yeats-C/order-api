package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.id.IdUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.dao.*;
import com.aiqin.mgs.order.api.dao.order.ErpOrderInfoDao;
import com.aiqin.mgs.order.api.dao.order.ErpOrderItemDao;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.constant.Global;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.util.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
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

    @Resource
    private CartOrderDao cartOrderDao;

    @Override
    public HttpResponse<Map> activityList(Activity activity) {
        LOGGER.info("查询促销活动列表activityList参数为：{}", activity);
        HttpResponse response = HttpResponse.success();
        Integer totalCount=activityDao.totalCount(activity);
        Map data=new HashMap();
        List<Activity> activities=new ArrayList<>();
        for (Activity act:activityDao.activityList(activity)){
            int finishNum=DateUtils.truncatedCompareTo(DateUtil.getNowDate(),DateUtil.StrToDate(act.getFinishTime()), Calendar.SECOND);
            int startNum=DateUtils.truncatedCompareTo(DateUtil.getNowDate(), DateUtil.StrToDate(act.getBeginTime()), Calendar.SECOND);
            if(act.getActivityStatus()==1 || finishNum>0){
                act.setActivityStatus(3);//已关闭
            }else if(act.getActivityStatus()==0
                    && startNum>=0
                    && finishNum<0){
                act.setActivityStatus(2);//进行中
            }else if(act.getActivityStatus()==0
                    && startNum<0){
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
    public HttpResponse<Map> getActivityItem(ErpOrderItem erpOrderItem) {
        LOGGER.info("查询活动详情-销售数据-活动销售列表（分页）getActivityItem参数erpOrderItem为：{}", erpOrderItem);
        HttpResponse response = HttpResponse.success();
        //只查询活动商品
        erpOrderItem.setIsActivity(1);
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
        LOGGER.info("查询活动详情-销售数据-活动销售统计");
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

    @Override
    public HttpResponse<List<ActivityRequest>> effectiveActivityList(String storeId) {
        LOGGER.info("通过门店id爱掌柜的促销活动列表（所有生效活动）effectiveActivityList参数为：{}", storeId);
        HttpResponse response = HttpResponse.success();
        List<Activity> activityList=activityDao.effectiveActivityList(storeId);
        return response;
    }

    @Override
    public HttpResponse<Integer> getSkuNum(ShoppingCartRequest shoppingCartRequest) {
        LOGGER.info("返回购物车中的sku商品的数量getSkuNum参数为：{}", shoppingCartRequest);
        HttpResponse response = HttpResponse.success();
        Integer skuNum=cartOrderDao.getSkuNum(shoppingCartRequest);
        response.setData(skuNum);
        return response;
    }

    @Override
    public Boolean checkProcuct(String activityId, String storeId, String productId) {
        LOGGER.info("校验商品活动是否过期checkProcuct参数activityId为:{},storeId:{},productId:{}", activityId,storeId,productId);
        if(null==activityId ||null==storeId ||null==productId){
            return false;
        }
        List<Activity> activityList=activityDao.checkProcuct(activityId,storeId,productId);
        if(activityList!=null){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public HttpResponse<List<ActivityProduct>> productBrandList(String productBrandName) {
        LOGGER.info("活动商品品牌列表接口参数productBrandName为:{}", productBrandName);
        HttpResponse response = HttpResponse.success();
        ActivityProduct activityProduct=new ActivityProduct();
        activityProduct.setProductBrandName(productBrandName);
        List<ActivityProduct> activityProducts=activityProductDao.productBrandList(activityProduct);
        response.setData(activityProducts);
        return response;
    }

    @Override
    public HttpResponse<List<ActivityProduct>> productCategoryList() {
        LOGGER.info("活动商品品类列表接口开始");
        HttpResponse response = HttpResponse.success();
        List<ActivityProduct> activityProducts=activityProductDao.productCategoryList();
        //所有根节点
        List<ActivityProduct> parentList = new ArrayList<>();
        //所有子节点
        List<ActivityProduct> childList = new ArrayList<>();
        activityProducts.forEach(item->{
            if ("0".equals(item.getProductCategoryCode())){
                parentList.add(item);
            } else {
                childList.add(item);
            }
        });

        parentList.forEach(item->{
            List<ActivityProduct> resultList = getChildren(String.valueOf(item.getProductCategoryCode()),childList);
            item.setActivityProductList(resultList);
        });
        response.setData(activityProducts);
        return response;
    }




    @Override
    public HttpResponse updateStatus(Activity activity) {
        LOGGER.info("编辑活动生效状态updateStatus参数为：{}", activity);
        try {
            if(null==activity.getActivityId()
                    ||null==activity.getActivityStatus()){
                return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
            }
            //保存活动主表信息start
            activity.setUpdateTime(new Date());
            int activityRecord = activityDao.updateActivity(activity);
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
    public HttpResponse excelActivityItem(ErpOrderItem erpOrderItem, HttpServletResponse response) {
        LOGGER.info("导出--活动详情-销售数据-活动销售列表excelActivityItem参数为：{}", erpOrderItem);
        HttpResponse res = HttpResponse.success();
        try {
            //只查询活动商品
            erpOrderItem.setIsActivity(1);
            List<ErpOrderItem> select = erpOrderItemDao.getActivityItem(erpOrderItem);
            HSSFWorkbook wb = exportData(select);
            String excelName = "数据列表";
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(excelName.getBytes("UTF-8"), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            return res;
        } catch (Exception ex) {
            throw new GroundRuntimeException(ex.getMessage());
        }
    }

    @Override
    public HttpResponse<List<ActivitySales>> comparisonActivitySalesStatistics(List<String> activityIdList) {
        LOGGER.info("活动列表-对比分析柱状图comparisonActivitySalesStatistics参数为：{}", activityIdList);
        HttpResponse res = HttpResponse.success();
        List<ActivitySales> activitySalesList=new ArrayList<>();
        for (String activityId: activityIdList){
            ActivitySales sales=getActivitySalesStatistics(activityId).getData();
            activitySalesList.add(sales);
        }
        res.setData(activityIdList);
        return res;
    }

    @Override
    public HttpResponse excelActivitySalesStatistics(List<String> activityIdList, HttpServletResponse response) {
        LOGGER.info("导出--活动列表-对比分析柱状图excelActivitySalesStatistics参数为：{}", activityIdList);
        HttpResponse res = HttpResponse.success();
        try {
            List<ActivitySales> activitySalesList=new ArrayList<>();
            for (String activityId: activityIdList){
                ActivitySales sales=getActivitySalesStatistics(activityId).getData();
                activitySalesList.add(sales);
            }
            HSSFWorkbook wb = exportActivitySalesData(activitySalesList);
            String excelName = "数据列表";
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.addHeader("Content-Disposition", "attachment;fileName=" + new String(excelName.getBytes("UTF-8"), "iso-8859-1"));
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
            return res;
        } catch (Exception ex) {
            throw new GroundRuntimeException(ex.getMessage());
        }
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

}