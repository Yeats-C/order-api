package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.exception.GroundRuntimeException;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.domain.*;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderItem;
import com.aiqin.mgs.order.api.domain.request.activity.*;
import com.aiqin.mgs.order.api.domain.request.cart.ShoppingCartRequest;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.response.order.ProductSkuRespVo2;
import com.aiqin.mgs.order.api.service.ActivityService;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

import static com.aiqin.mgs.order.api.web.MaterialClaimController.*;

@RestController
@RequestMapping("/activity")
@Api(tags = "促销活动相关接口")
public class ActivityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityController.class);
    @Resource
    private ActivityService activitesService;
    @Value("${bridge.url.slcs_api}")
    private String slcsHost;
    @Autowired
    private BridgeProductService bridgeProductService;


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
    public Boolean checkProcuct(ActivityParameterRequest activityParameterRequest){
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
     * @param activityId
     * @param response
     * @return
     */
    @GetMapping("/excelActivityItem")
    @ApiOperation("导出--活动详情-销售数据-活动销售列表")
    public void excelActivityItem(@RequestParam(name = "activityId", required = false) String activityId, HttpServletResponse response){
        activitesService.excelActivityItem(activityId,response);
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

    /**
     * 通过列表id查询门店权限集合
     */
    @PostMapping("/storeIds")
    @ApiOperation("通过列表id查询门店权限集合")
    public HttpResponse<List<String>> storeIds(String menuCode){
        HttpResponse response = HttpResponse.success();
        response.setData(activitesService.storeIds(menuCode));
        return response;
    };


    /**
     * 下载SKU导入用excel模板
     */
    @GetMapping("/download/template")
    @ApiOperation("SKU导入用excel")
    public void DownloadTemplate(HttpServletResponse response){
        try {
            //创建新的Excel工作薄
            Workbook workbook = new XSSFWorkbook();
            //创建工作单元
            Sheet sheet = workbook.createSheet("SKU导入用excel");
            //创建第一行
            Row row = sheet.createRow(0);
            //创建格式
            CellStyle cellStyle = workbook.createCellStyle();
            //为字体设置格式
            Font font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short)12);
            cellStyle.setFont(font);
            String[] titles = {"sku编码","金额门槛","条件类型","满足条件","赠品1sku编码","赠送数量","赠品2sku编码"
                    ,"赠送数量","赠品3sku编码","赠送数量","赠品4sku编码","赠送数量","赠品5sku编码","赠送数量"};
            for (int i = 0; i < titles.length; i++) {
                Cell cell4 = row.createCell(i);
                cell4.setCellStyle(cellStyle);
                cell4.setCellValue(titles[i]);
            }

            //使用输出流
            String fileName = "SKU导入用excel";
            //文件名称乱码设置
            fileName = URLEncoder.encode(fileName, "UTF8");
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
            ServletOutputStream out = response.getOutputStream();
            //workbook内容写入文件中
            workbook.write(out);
            out.close();
        }catch (Exception e){
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * 【买赠】解析SkuExcel表数据
     */
    @PostMapping("/uploadSkuExcel")
    @ApiOperation(value = "【买赠】解析SkuExcel表数据")
    public HttpResponse<ActivityRequest> getExcel(MultipartFile file) throws IOException {
        checkFile(file);
        //获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        Set<ProductSkuRequest2> objectList = new HashSet<>();
        List<ActivityProduct> activityProductList = new ArrayList<>();
        List<ActivityRule> activityRuleList = new ArrayList<>();
        List<ActivityGift> activityGiftList = new ArrayList<>();
        ActivityRequest activityRequest=new ActivityRequest();
        Map<String,String> productMap=new HashMap();
        List<String[]> list = new ArrayList<>();
        List<Integer> errorList = new ArrayList<>();
        if (workbook != null) {
            //getNumberOfSheets() 获取工作薄中的sheet个数
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                //获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                //获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                //获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                //循环除了所有行,如果要循环除第一行以外的就firstRowNum+1
                for (int rowNum = firstRowNum+1; rowNum <= lastRowNum; rowNum++) {
                    //获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    //获得当前行的开始列
//                        int firstCellNum = row.getFirstCellNum();
                    //获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    //创建列数大小的数组，存放每一行的每列的值
                    String[] cells = new String[row.getLastCellNum()];
                    //循环当前行
                    for (int cellNum = 0; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        String cellValue = getCellValue(cell);
                        if (cellNum == 0 && "".equals(cellValue)){
                            errorList.add(rowNum);
                            break;
                        }
                        cells[cellNum] = cellValue;
                    }
                    list.add(cells);
                }
            }
        }
        if (!errorList.isEmpty()){
            return HttpResponse.success("导入模板部分商品Sku缺失,请填写完成再次导入,行号： " + errorList);
        }
        for (String[] s : list){
            //调用供应链商品查询接口参数组装
            ProductSkuRequest2 productSkuRequest2 = new ProductSkuRequest2();
            productSkuRequest2.setSkuCode(s[0]);
            objectList.add(productSkuRequest2);
            //解析商品集合
            if(!productMap.containsKey(s[0])){
                ActivityProduct activityProduct =new ActivityProduct();
                activityProduct.setSkuCode(s[0]);
                activityProductList.add(activityProduct);
            }
            //解析活动规则集合
            ActivityRule activityRule=new ActivityRule();
            //活动类型1.满减2.满赠3.折扣4.返点5.特价6.整单7.买赠
            activityRule.setActivityType(7);
            //商品编码
            activityRule.setSkuCode(s[0]);
            //买赠活动sku门槛
            activityRule.setThreshold(BigDecimal.valueOf(Double.valueOf(s[1])));
            //优惠单位：0.无条件 1.按数量（件）2.按金额（元）
            activityRule.setRuleUnit(Integer.valueOf(s[2]));
            //满足条件(满多少参加活动)
            activityRule.setMeetingConditions(BigDecimal.valueOf(Double.valueOf(s[3])));
            activityGiftList.clear();
            if(null!=s[4]){
                ActivityGift activityGift=new ActivityGift();
                activityGift.setSkuCode(s[4]);
                activityGift.setNumbers(Integer.valueOf(s[5]));
                activityGiftList.add(activityGift);
                productSkuRequest2 = new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(s[4]);
                objectList.add(productSkuRequest2);
            }

            if(null!= s[6]){
                ActivityGift activityGift=new ActivityGift();
                activityGift.setSkuCode(s[6]);
                activityGift.setNumbers(Integer.valueOf(s[7]));
                activityGiftList.add(activityGift);
                productSkuRequest2 = new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(s[6]);
                objectList.add(productSkuRequest2);
            }

            if(null!= s[8]){
                ActivityGift activityGift=new ActivityGift();
                activityGift.setSkuCode(s[8]);
                activityGift.setNumbers(Integer.valueOf(s[9]));
                activityGiftList.add(activityGift);
                productSkuRequest2 = new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(s[8]);
                objectList.add(productSkuRequest2);
            }

            if(null!= s[10]){
                ActivityGift activityGift=new ActivityGift();
                activityGift.setSkuCode(s[10]);
                activityGift.setNumbers(Integer.valueOf(s[11]));
                activityGiftList.add(activityGift);
                productSkuRequest2 = new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(s[10]);
                objectList.add(productSkuRequest2);
            }

            if(null!= s[12]){
                ActivityGift activityGift=new ActivityGift();
                activityGift.setSkuCode(s[12]);
                activityGift.setNumbers(Integer.valueOf(s[13]));
                activityGiftList.add(activityGift);
                productSkuRequest2 = new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(s[12]);
                objectList.add(productSkuRequest2);
            }
            activityRule.setGiftList(activityGiftList);
            activityRuleList.add(activityRule);
        }

        LOGGER.info("解析后的数据集合： " + JsonUtil.toJson(activityRequest));

        LOGGER.info(JsonUtil.toJson(objectList));
        List<ProductSkuRespVo2> erpSkuDetailList = bridgeProductService.detail4(null, null, "14", new ArrayList<>(objectList));
        LOGGER.info("供应链查询数据返回结果： " + JsonUtil.toJson(erpSkuDetailList));
        Map<String, ProductSkuRespVo2> skuDetailMap = new HashMap<>(16);
        for (ProductSkuRespVo2 item : erpSkuDetailList) {
            skuDetailMap.put(item.getSkuCode(), item);
        }
        for(ActivityProduct activityProduct: activityProductList){
            if(skuDetailMap.containsKey(activityProduct.getSkuCode())){
                ProductSkuRespVo2 erpSkuDetail=skuDetailMap.get(activityProduct.getSkuCode());
                activityProduct.setProductCode(erpSkuDetail.getSkuCode());
                activityProduct.setProductName(erpSkuDetail.getSkuName());
                activityProduct.setProductBrandCode(erpSkuDetail.getProductBrandCode());
                activityProduct.setProductBrandName(erpSkuDetail.getProductBrandName());
                activityProduct.setProductCategoryCode(erpSkuDetail.getProductCategoryCode());
                activityProduct.setProductCategoryName(erpSkuDetail.getProductCategoryName());
                activityProduct.setPriceTax(erpSkuDetail.getPriceTax());
                activityProduct.setStatus(0);
                activityProduct.setActivityScope(1);
            }

        }
        //赠品需要商品编码 商品名称
        for(ActivityRule rule:activityRuleList){
            for(ActivityGift gift:rule.getGiftList()){
                if(skuDetailMap.containsKey(gift.getSkuCode())){
                    ProductSkuRespVo2 erpSkuDetail=skuDetailMap.get(gift.getSkuCode());
                    gift.setProductCode(erpSkuDetail.getSkuCode());
                    gift.setProductName(erpSkuDetail.getSkuName());
                }
            }
        }
        activityRequest.setActivityProducts(activityProductList);
        activityRequest.setActivityRules(activityRuleList);
        return HttpResponse.success(activityRequest);
    }

}
