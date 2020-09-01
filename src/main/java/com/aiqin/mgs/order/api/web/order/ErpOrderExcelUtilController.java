package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.component.enums.ErpOrderStatusEnum;
import com.aiqin.mgs.order.api.domain.po.order.DownloadOrderInfoVo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderQueryRequest;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.service.order.ErpOrderQueryService;
import com.aiqin.mgs.order.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/erpOrderExcelUtilController")
@Api(tags = "ERP订单Excel工具类")
public class ErpOrderExcelUtilController {

    private static final Logger log = LoggerFactory.getLogger(ErpOrderExcelUtilController.class);

    @Resource
    private BridgeProductService bridgeProductService;

    @Resource
    private ErpOrderQueryService erpOrderQueryService;


    @GetMapping("/downloadRackOrderProductTempExcel")
    @ApiOperation(value = "下载货架订单商品模板")
    public HttpResponse downloadRackOrderProductTempExcel(HttpServletRequest request, HttpServletResponse response) {
        HttpResponse httpResponse = HttpResponse.success();
        try {
            createRackOrderProductTempExcel(request, response);
        } catch (BusinessException e) {
            httpResponse = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            httpResponse = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return httpResponse;
    }

    @PostMapping("/transformRackOrderProductExcel")
    @ApiOperation(value = "解析货架订单商品模板数据")
    public HttpResponse transformRackOrderProductExcel(@RequestParam("file") MultipartFile file,
                                                       @RequestParam("provinceId") String provinceId,
                                                       @RequestParam("cityId") String cityId
    ) {
        HttpResponse httpResponse = HttpResponse.success();
        try {
            List<String[]> excelData = ExcelUtil.getExcelDataList(file);
            Map<String, String> map = new LinkedHashMap<>(16);
            List<ProductSkuRequest2> productSkuRequest2List=new ArrayList<>();
            for (String[] item :
                    excelData) {

                map.put(item[2], item[6]);
                ProductSkuRequest2 request2=new ProductSkuRequest2();
                request2.setSkuCode(item[2]);
                request2.setWarehouseTypeCode("1");
                productSkuRequest2List.add(request2);
            }
            List<ErpSkuDetail> details=bridgeProductService.getProductSkuDetailList(provinceId,cityId,"14",productSkuRequest2List);
            for(ErpSkuDetail detail:details){
                if(map.containsKey(detail.getSkuCode())){
                    detail.setQuantity(Integer.valueOf(map.get(detail.getSkuCode())));
                }
            }

            httpResponse.setData(details);
        } catch (BusinessException e) {
            httpResponse = HttpResponse.failure(MessageId.create(Project.ORDER_API, 99, e.getMessage()));
        } catch (Exception e) {
            httpResponse = HttpResponse.failure(ResultCode.SELECT_EXCEPTION);
        }
        return httpResponse;
    }

    /**
     * 生成和下载货架订单商品列表xecel
     *
     * @param request
     * @param response
     * @return void
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/16 17:20
     */
    private void createRackOrderProductTempExcel(HttpServletRequest request, HttpServletResponse response) {
        //采用POI导出excel
        try {
            Workbook workbook = new HSSFWorkbook();
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/x-download");

            String filedisplay = "货架订单商品模板.xls";
            filedisplay = URLEncoder.encode(filedisplay, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);

            // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
            Sheet sheet = workbook.createSheet("sheet1");
            // 第三步，在sheet中添加表头第0行
            Row row = sheet.createRow(0);
            // 第四步，创建单元格，并设置值表头 设置表头居中
            CellStyle style = workbook.createCellStyle();
            // 创建一个居中格式
            style.setAlignment(CellStyle.ALIGN_CENTER);
            style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);

            Cell cell = row.createCell(0);
            cell.setCellValue("spu编码");
            cell.setCellStyle(style);
            //设置列宽，50个字符宽
            sheet.setColumnWidth(0, (15 * 256));

            cell = row.createCell(1);
            cell.setCellValue("spu名称");
            cell.setCellStyle(style);
            sheet.setColumnWidth(1, (30 * 256));

            cell = row.createCell(2);
            cell.setCellValue("sku编码");
            cell.setCellStyle(style);
            sheet.setColumnWidth(2, (15 * 256));

            cell = row.createCell(3);
            cell.setCellValue("sku名称");
            cell.setCellStyle(style);
            sheet.setColumnWidth(3, (30 * 256));

            cell = row.createCell(4);
            cell.setCellValue("采购单价");
            cell.setCellStyle(style);
            sheet.setColumnWidth(4, (15 * 256));

            cell = row.createCell(5);
            cell.setCellValue("销售单价");
            cell.setCellStyle(style);
            sheet.setColumnWidth(5, (15 * 256));

            cell = row.createCell(6);
            cell.setCellValue("数量");
            cell.setCellStyle(style);
            sheet.setColumnWidth(6, (15 * 256));


            // 第五步，写入实体数据
//            row = sheet.createRow(1);
//            row.createCell(0, Cell.CELL_TYPE_STRING).setCellValue("123456");
//            row.createCell(1, Cell.CELL_TYPE_STRING).setCellValue("123456");
//            row.createCell(2, Cell.CELL_TYPE_STRING).setCellValue("123456");
//            row.createCell(3, Cell.CELL_TYPE_STRING).setCellValue("123456");
//            row.createCell(4, Cell.CELL_TYPE_STRING).setCellValue("123456");

            // 第六步，将文件存到指定位置
            try {
                OutputStream out = response.getOutputStream();
                workbook.write(out);
                out.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 订单导出
     */
    @PostMapping("/downloadOrderList")
    @ApiOperation("订单导出")
    public void DownloadTemplate(HttpServletResponse response,@RequestBody ErpOrderQueryRequest erpOrderQueryRequest){
        try {

            List<DownloadOrderInfoVo> list = erpOrderQueryService.findDownloadOrderList(erpOrderQueryRequest);


            //创建新的Excel工作薄
            Workbook workbook = new XSSFWorkbook();
            //创建工作单元
            Sheet sheet = workbook.createSheet("订单");
            //创建第一行+
            Row row = sheet.createRow(0);
            //创建格式
            CellStyle cellStyle = workbook.createCellStyle();
            //为字体设置格式
            Font font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short)12);
            cellStyle.setFont(font);
            String[] titles = {"门店编码","门店名称","所属合伙人公司","零售主管","销售属性","品牌","一级品类"
                    ,"商品属性","商品编码","商品名称","分销价","活动价","订货数量","实发数量","分摊后金额","A品券抵扣"
                    ,"活动优惠","使用赠品额度","关联订单号（子订单）","订单类型","订单类别","订单状态"};
            for (int i = 0; i < titles.length; i++) {
                Cell cell4 = row.createCell(i);
                cell4.setCellStyle(cellStyle);
                cell4.setCellValue(titles[i]);
            }
            // 添加单元格数据
            for (int i = 0; i < list.size(); i++) {
                row = sheet.createRow(i + 1);
                DownloadOrderInfoVo item = list.get(i);
                if(null!=item.getStoreCode()) {
                    row.createCell(0).setCellValue(item.getStoreCode());
                }else {
                    row.createCell(0).setCellValue("");
                }
                if(null!=item.getStoreName()) {
                    row.createCell(1).setCellValue(item.getStoreName());
                }else {
                    row.createCell(1).setCellValue("");
                }
                if(null!=item.getCopartnerAreaName()) {
                    row.createCell(2).setCellValue(item.getCopartnerAreaName());
                }else {
                    row.createCell(2).setCellValue("");
                }
//                if(null!=item.getAverageUnitPrice()) {
//                    row.createCell(3).setCellValue(item.getAverageUnitPrice().doubleValue());
//                }else {
//                    row.createCell(3).setCellValue(0);
//                }
                if(null!=item.getProductType()) {
                    if(item.getProductType()==0){
                        row.createCell(4).setCellValue("商品");
                    }else if(item.getProductType()==1){
                        row.createCell(4).setCellValue("赠品");
                    }else if(item.getProductType()==2){
                        row.createCell(4).setCellValue("兑换赠品");
                    }else{
                        row.createCell(4).setCellValue("");
                    }
                }else {
                    row.createCell(4).setCellValue("");
                }
                if(null!=item.getProductBrandName()) {
                    row.createCell(5).setCellValue(item.getProductBrandName());
                }else {
                    row.createCell(5).setCellValue("");
                }
                if(null!=item.getProductCategoryName()) {
                    row.createCell(6).setCellValue(item.getProductCategoryName());
                }else {
                    row.createCell(6).setCellValue("");
                }
                if(null!=item.getProductPropertyName()) {
                    row.createCell(7).setCellValue(item.getProductPropertyName());
                }else {
                    row.createCell(7).setCellValue("");
                }
                if(null!=item.getSkuCode()) {
                    row.createCell(8).setCellValue(item.getSkuCode());
                }else {
                    row.createCell(8).setCellValue("");
                }
                if(null!=item.getSkuName()) {
                    row.createCell(9).setCellValue(item.getSkuName());
                }else {
                    row.createCell(9).setCellValue("");
                }
                if(null!=item.getProductAmount()) {
                    row.createCell(10).setCellValue(item.getProductAmount().doubleValue());
                }else {
                    row.createCell(10).setCellValue("");
                }
                if(null!=item.getActivityPrice()) {
                    row.createCell(11).setCellValue(item.getActivityPrice().doubleValue());
                }else {
                    row.createCell(11).setCellValue("");
                }
                if(null!=item.getProductCount()) {
                    row.createCell(12).setCellValue(item.getProductCount());
                }else {
                    row.createCell(12).setCellValue("");
                }
                if(null!=item.getActualProductCount()) {
                    row.createCell(13).setCellValue(item.getActualProductCount());
                }else {
                    row.createCell(13).setCellValue("");
                }
                if(null!=item.getPreferentialAmount()) {
                    row.createCell(14).setCellValue(item.getPreferentialAmount().doubleValue());
                }else {
                    row.createCell(14).setCellValue("");
                }
                if(null!=item.getTopCouponDiscountAmount()) {
                    row.createCell(15).setCellValue(item.getTopCouponDiscountAmount().doubleValue());
                }else {
                    row.createCell(15).setCellValue("");
                }
                if(null!=item.getTotalAcivityAmount()) {
                    row.createCell(16).setCellValue(item.getTotalAcivityAmount().doubleValue());
                }else {
                    row.createCell(16).setCellValue("");
                }
                if(null!=item.getUsedGiftQuota()) {
                    row.createCell(17).setCellValue(item.getUsedGiftQuota().doubleValue());
                }else {
                    row.createCell(17).setCellValue("");
                }
                if(null!=item.getMainOrderCode()) {
                    row.createCell(18).setCellValue(item.getMainOrderCode());
                }else {
                    row.createCell(18).setCellValue("");
                }
                if(null!=item.getOrderTypeName()) {
                    row.createCell(19).setCellValue(item.getOrderTypeName());
                }else {
                    row.createCell(19).setCellValue("");
                }
                if(null!=item.getOrderCategoryName()) {
                    row.createCell(20).setCellValue(item.getOrderCategoryName());
                }else {
                    row.createCell(20).setCellValue("");
                }
                if(null!=item.getOrderStatus()) {
                    row.createCell(21).setCellValue(ErpOrderStatusEnum.getEnumDesc(item.getOrderStatus()));
                }else {
                    row.createCell(21).setCellValue("");
                }
            }

            //使用输出流
            String fileName = "订单导出";
            //文件名称乱码设置
            fileName = URLEncoder.encode(fileName, "UTF8");
            response.setContentType("application/vnd.ms-excel;chartset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xlsx");
            ServletOutputStream out = response.getOutputStream();
            //workbook内容写入文件中
            workbook.write(out);
            out.close();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

}
