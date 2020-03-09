package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityProduct;
import com.aiqin.mgs.order.api.domain.ActivitySales;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.*;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/activity")
@Api(tags = "促销活动相关接口")
public class ActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);
    @Resource
    private ActivityService activitesService;

    /**
     * 促销活动管理--活动列表
     * @param activity
     * @return
     */
    @GetMapping("/activityList")
    @ApiOperation(value = "促销活动管理--活动列表")
    public HttpResponse<Map> activityList(Activity activity){
        return activitesService.activityList(activity);
    }

    /**
     * 通过活动id获取单个活动信息
     * @param activityId
     * @return
     */
    @GetMapping("/getActivityInformation")
    @ApiOperation(value = "通过活动id获取单个活动信息")
    public HttpResponse<Activity> getActivityInformation(String activityId){
        return activitesService.getActivityInformation(activityId);
    }

    /**
     * 添加活动
     *
     * @param
     * @return
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加活动")
    public HttpResponse add(@RequestBody ActivityRequest activityRequest) {
        //将商品添加到购物车
        return activitesService.addActivity(activityRequest);
    }

    /**
     * 活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数
     * @param activity
     * @return
     */
    @GetMapping("/activityProductList")
    @ApiOperation(value = "活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数")
    public HttpResponse<List<ActivityProduct>> activityProductList(Activity activity){
        return activitesService.activityProductList(activity);
    }

    /**
     * 活动详情-销售数据-活动销售列表（分页）-只传分页参数
     * @param erpOrderItem
     * @return
     */
    @GetMapping("/getActivityItem")
    @ApiOperation(value = "活动详情-销售数据-活动销售列表（分页）-只传分页参数")
    public HttpResponse<Map> getActivityItem(ErpOrderItem erpOrderItem){
        return activitesService.getActivityItem(erpOrderItem);
    }

    /**
     * 活动详情-销售数据-活动销售统计
     * @param activityId
     * @return
     */
    @GetMapping("/getActivitySalesStatistics")
    @ApiOperation(value = "活动详情-销售数据-活动销售统计")
    public HttpResponse<ActivitySales> getActivitySalesStatistics(@RequestParam(name = "activity_id", required = false) String activityId){
        return activitesService.getActivitySalesStatistics(activityId);
    }

    /**
     * 通过活动id获取单个活动详情（活动+门店+商品+规则）
     * @param activityId
     * @return
     */
    @GetMapping("/getActivityDetail")
    @ApiOperation(value = "通过活动id获取单个活动详情（活动+门店+商品+规则）")
    public HttpResponse<ActivityRequest> getActivityDetail(String activityId){
        return activitesService.getActivityDetail(activityId);
    }

    /**
     * 编辑活动
     *
     * @param
     * @return
     */
    @PostMapping("/update")
    @ApiOperation(value = "编辑活动")
    public HttpResponse update(@RequestBody ActivityRequest activityRequest) {
        //编辑活动
        return activitesService.updateActivity(activityRequest);
    }

    /**
     * 通过门店id爱掌柜的促销活动列表（所有生效活动）
     * @param storeId
     * @return
     */
    @GetMapping("/effectiveActivityList")
    @ApiOperation(value = "通过门店id爱掌柜的促销活动列表（所有生效活动）")
    public HttpResponse<List<Activity>> effectiveActivityList(String storeId){
        return activitesService.effectiveActivityList(storeId);
    }

    /**
     * 爱掌柜-活动商品列表查询（分页），只传activityId与分页参数（未完，商品需处理。每个商品得确定有什么活动，这个最好是在service层写成公用方法）
     * @param activity
     * @return
     */
    @GetMapping("/productList")
    @ApiOperation(value = "活动详情-促销规则-活动商品列表查询（分页），只传activityId与分页参数")
    public HttpResponse<List<ActivityProduct>> productList(Activity activity){
        return activitesService.activityProductList(activity);
    }
    /**
     * 编辑活动生效状态
     *
     * @param
     * @return
     */
    @PostMapping("/updateStatus")
    @ApiOperation(value = "编辑活动生效状态")
    public HttpResponse updateStatus(@RequestBody Activity activity) {
        //编辑活动生效状态
        return activitesService.updateStatus(activity);
    }




    /**
     * 返回购物车中的sku商品的数量
     * @param shoppingCartRequest
     * @return
     */
    @GetMapping("/getSkuNum")
    @ApiOperation(value = "返回购物车中的sku商品的数量")
    public HttpResponse<Integer> getSkuNum(@Valid ShoppingCartRequest shoppingCartRequest) {
        return activitesService.getSkuNum(shoppingCartRequest);
    }

    /**
     * 校验商品活动是否过期
     * @param activityParameterRequest
     * @return
     */
    @GetMapping("/checkProcuct")
    @ApiOperation(value = "校验商品活动是否过期")
    Boolean checkProcuct(ActivityParameterRequest activityParameterRequest){
        return activitesService.checkProcuct(activityParameterRequest);
    }

    /**
     * 活动商品品牌列表接口
     * @param productBrandName
     * @return
     */
    @GetMapping("/product/brand/list")
    @ApiOperation(value = "活动商品品牌列表接口")
    public HttpResponse<List<QueryProductBrandRespVO>> productBrandList(@RequestParam(name = "product_brand_name", required = false) String productBrandName, @RequestParam(name = "activity_id", required = false) String activityId) {
        return activitesService.productBrandList(productBrandName,activityId);
    }

    /**
     * 活动商品品类接口
     * @param
     * @return
     */
    @GetMapping("/product/category/list")
    @ApiOperation(value = "活动商品品类接口")
    public HttpResponse<List<ProductCategoryRespVO>> productCategoryList(@RequestParam(name = "activity_id", required = false) String activityId) {
        return activitesService.productCategoryList(activityId);
    }

    /**
     * 导出--活动详情-销售数据-活动销售列表
     * @param erpOrderItem
     * @param response
     * @return
     */
    @PostMapping("/excelActivityItem")
    @ApiOperation("导出--活动详情-销售数据-活动销售列表")
    public void excelActivityItem(ErpOrderItem erpOrderItem, HttpServletResponse response){
        activitesService.excelActivityItem(erpOrderItem,response);
    }

    /**
     * 活动列表-对比分析柱状图
     * @param activityIdList
     * @return
     */
    @PostMapping("/comparisonActivitySalesStatistics")
    @ApiOperation(value = "活动列表-对比分析柱状图")
    public HttpResponse<List<ActivitySales>> comparisonActivitySalesStatistics(@RequestBody  List<String> activityIdList){
        return activitesService.comparisonActivitySalesStatistics(activityIdList);
    }

    /**
     * 导出--活动列表-对比分析柱状图
     * @param activityIdList
     * @param response
     * @return
     */
    @GetMapping("/excelActivitySalesStatistics")
    @ApiOperation("导出--活动列表-对比分析柱状图")
    public void excelActivitySalesStatistics(@RequestParam(name = "activityIdList") List<String> activityIdList, HttpServletResponse response){
         activitesService.excelActivitySalesStatistics(activityIdList,response);
    }


    /**
     * 活动商品查询（筛选+分页）
     * @param spuProductReqVO
     */
    @PostMapping("/skuPage")
    @ApiOperation("活动商品查询（筛选+分页）")
        public HttpResponse<PageResData<ProductSkuRespVo5>> skuPage(@Valid @RequestBody SpuProductReqVO spuProductReqVO){
        return activitesService.skuPage(spuProductReqVO);
    }

    /**
     * 通过条件查询一个商品有多少个进行中的活动
     * @param parameterRequest
     */
    @PostMapping("/productActivityList")
    @ApiOperation("通过条件查询一个商品有多少个进行中的活动")
    public HttpResponse<List<Activity>> productActivityList(@Valid @RequestBody ActivityParameterRequest parameterRequest){
        HttpResponse response = HttpResponse.success();
        response.setData(activitesService.activityList(parameterRequest));
        return response;
    }

    @GetMapping("/selectCategoryByBrandCode")
    @ApiOperation(value = "品牌和品类关系,condition_code为查询条件，type=2 通过品牌查品类,type=1 通过品类查品牌")
    public HttpResponse<ProductCategoryAndBrandResponse2> selectCategoryByBrandCode(@RequestParam(value = "condition_code") String conditionCode,
                                                                                    @RequestParam(value = "type") String type,@RequestParam(value = "activity_id") String activityId
    ) {
        if (StringUtils.isBlank(conditionCode) && StringUtils.isBlank(type) && StringUtils.isBlank(activityId)) {
            return HttpResponse.failure(MessageId.create(Project.PRODUCT_API, 500, "必传项为空"));
        }
        try {
            ProductCategoryAndBrandResponse2 responses= activitesService.ProductCategoryAndBrandResponse(conditionCode, type,activityId);
            return HttpResponse.successGenerics(responses);
        }catch (Exception e){
            if(e instanceof GroundRuntimeException){
                return HttpResponse.failure(MessageId.create(Project.SUPPLIER_API, -1, e.getMessage()));
            }
            return HttpResponse.failure(ResultCode.SELECT_ACTIVITY_INFO_EXCEPTION);
        }
    }


}
