package com.aiqin.mgs.order.api.web;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.json.JsonUtil;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.exception.BusinessException;
import com.aiqin.mgs.order.api.domain.StoreBackInfoResponse;
import com.aiqin.mgs.order.api.domain.request.product.ProductSkuRequest2;
import com.aiqin.mgs.order.api.domain.response.cart.ErpSkuDetail;
import com.aiqin.mgs.order.api.service.bridge.BridgeProductService;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.apache.bcel.generic.LOOKUPSWITCH;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 物资申领
 */
@RestController
@RequestMapping("/material/claim")
@Api(tags = "订单批量加入")
public class MaterialClaimController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MaterialClaimController.class);


    @Value("${bridge.url.slcs_api}")
    private String slcsHost;
    @Autowired
    private BridgeProductService bridgeProductService;

    /**
     * 下载导入模板
     */
    @GetMapping("/download/template")
    @ApiOperation("下载物资申领导入模板")
    public void Download(HttpServletResponse response, String type){
        try {
            //创建新的Excel工作薄
            Workbook workbook = new XSSFWorkbook();
            //创建工作单元
            Sheet sheet = workbook.createSheet("sheet1");
            //创建第一行
            Row row = sheet.createRow(0);
            //创建格式
            CellStyle cellStyle = workbook.createCellStyle();
            //为字体设置格式
            Font font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short)12);
            cellStyle.setFont(font);
            if ("1".equals(type)){  //1是普通订单的模板
                String[] titles = {"商品编码","商品名称","销售库/特卖库(全部0销售库1特卖库2)","是否赠品(赠品请录入1)"};
                for (int i = 0; i < titles.length; i++) {
                    Cell cell4 = row.createCell(i);
                    cell4.setCellStyle(cellStyle);
                    cell4.setCellValue(titles[i]);
                }
            }else { //其他是首单
                String[] titles = {"商品编码","商品名称","销售库/特卖库(全部0销售库1特卖库2)"};
                for (int i = 0; i < titles.length; i++) {
                    Cell cell4 = row.createCell(i);
                    cell4.setCellStyle(cellStyle);
                    cell4.setCellValue(titles[i]);
                }
            }
            //使用输出流
            String fileName = "订单批量导入模板";
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
     * 上传Excel表解析
     */
    @PostMapping("/upload")
    @ApiOperation(value = "导入Excel表数据")
    public HttpResponse<ErpSkuDetail> getExcel(MultipartFile file, String storeId) throws IOException {
            List<ErpSkuDetail> erpSkuDetailList = new ArrayList<>();
            checkFile(file);
            //获得Workbook工作薄对象
            Workbook workbook = getWorkBook(file);
            //创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
            List<ProductSkuRequest2> objectList = new ArrayList<>();
            List<String[]> list = new ArrayList<>();
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
                        int firstCellNum = row.getFirstCellNum();
                        //获得当前行的列数
                        int lastCellNum = row.getLastCellNum();
                        //创建列数大小的数组，存放每一行的每列的值
                        String[] cells = new String[row.getLastCellNum()];
                        //循环当前行
                        for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                            Cell cell = row.getCell(cellNum);
                            //将列值放到对应的位置
                            cells[cellNum] = getCellValue(cell);
                        }
                        list.add(cells);
                    }
                }
            }
            for (String[] s : list){
                ProductSkuRequest2 productSkuRequest2 = new ProductSkuRequest2();
                productSkuRequest2.setSkuCode(s[0]);
                productSkuRequest2.setWarehouseTypeCode(s[2]);
                objectList.add(productSkuRequest2);
            }
        LOGGER.info("解析后的数据集合： " + objectList);
        StoreBackInfoResponse data = null;
        Map<String, Object> result;
        StringBuilder sb = new StringBuilder(slcsHost + "/store/getStoreInfo?store_id=" + storeId);
        LOGGER.info("通过门店id查询门店信息,请求url为{}", sb);
        HttpClient httpClient = HttpClient.get(sb.toString());
        try{
             result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
             LOGGER.info("调用门店系统返回结果result:{}", JSON.toJSON(result));
             data = JSON.parseObject(JSON.toJSONString(result.get("data")), StoreBackInfoResponse.class);
             LOGGER.info("获取门店信息：{}", data);
        }catch (Exception e) {
            LOGGER.info("查询门店信息失败");
            throw e;
        }
        LOGGER.info(JsonUtil.toJson(objectList));
        erpSkuDetailList = bridgeProductService.getProductSkuDetailList(data.getProvinceId(), data.getCityId(), data.getCompanyCode(), objectList);
        LOGGER.info("供应链查询数据返回结果： " + erpSkuDetailList);
        return HttpResponse.success(erpSkuDetailList);
    }


    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    public static void checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            LOGGER.error("文件不存在！");
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            LOGGER.error(fileName + "不是excel文件");
        }
    }


    public static Workbook getWorkBook(MultipartFile file) {
        //获得文件名
        String fileName = file.getOriginalFilename();
        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            InputStream is = file.getInputStream();
            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return workbook;
    }

    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        //判断数据的类型
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: //数字
                cellValue = stringDateProcess(cell);
                break;
            case Cell.CELL_TYPE_STRING: //字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case Cell.CELL_TYPE_BOOLEAN: //Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA: //公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case Cell.CELL_TYPE_BLANK: //空值
                cellValue = "";
                break;
            case Cell.CELL_TYPE_ERROR: //故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }

    public static String stringDateProcess(Cell cell) {
        String result = new String();
        if (HSSFDateUtil.isCellDateFormatted(cell)) {
            // 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {
                // 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
            // CHECKSTYLE:OFF
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            double value = cell.getNumericCellValue();
            Date date = org.apache.poi.ss.usermodel.DateUtil
                    .getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }

        return result;
    }
}

