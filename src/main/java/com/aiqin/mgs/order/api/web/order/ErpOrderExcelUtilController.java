package com.aiqin.mgs.order.api.web.order;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.aiqin.mgs.order.api.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    @Resource
    private BridgeProductService bridgeProductService;

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
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
