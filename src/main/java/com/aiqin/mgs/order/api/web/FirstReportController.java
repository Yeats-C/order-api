package com.aiqin.mgs.order.api.web;


import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.dao.FirstReportInfoDao;
import com.aiqin.mgs.order.api.domain.FirstReportInfo;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.FirstReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

@RestController
@Api(tags = "首单报表")
@RequestMapping("/firstReport")
public class FirstReportController {

    private static final Logger logger = LoggerFactory.getLogger(FirstReportController.class);

    @Autowired
    private FirstReportService firstReportService;

    @Autowired
    private FirstReportInfoDao firstReportInfoDao;

    @Autowired
    private CopartnerAreaService copartnerAreaService;



    /**
     * 首单报表定时任务
     * @return
     */
    @ApiOperation("首单报表定时任务")
    @PostMapping("/first")
    public HttpResponse reportTimedTask(){
        firstReportService.reportTimedTask();
        return HttpResponse.success();
    }

    /**
     * 首单报表定时任务
     * @return
     */
    @ApiOperation("首单年报表定时任务")
    @PostMapping("/firstYear")
    public HttpResponse reportTimedTaskYear(){
        firstReportService.reportTimedTaskYear();
        return HttpResponse.success();
    }

    /**
     * 报表时间获取首单报表数据
     * @param reportTime
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation("获取首单报表表格数据")
    @GetMapping("/gitList")
    public HttpResponse<List<FirstReportInfo>> getList(@RequestParam(value = "report_time") String reportTime,
                                                       @RequestParam(value = "person_id") String personId,
                                                       @RequestParam(value = "resource_code") String resourceCode,
                                                       @RequestParam(value = "page_no", required = false) Integer pageNo,
                                                       @RequestParam(value = "page_size", required = false) Integer pageSize){
        return firstReportService.getLists(reportTime,pageNo,pageSize, personId,resourceCode);
    }

    /**
     * 导出报表
     * @return
     * @throws Exception
     */
    @ApiOperation("导出月份报表")
    @GetMapping("/exportReport")
    public void exportReportData(HttpServletResponse resp,String reportTime) {
        try {
            //获取报表每月数据集合
            List<FirstReportInfo> firstReportInfos = firstReportInfoDao.selcetReportInfoAll(reportTime);
            //创建新的Excel工作薄
            Workbook workbook = new XSSFWorkbook();
            //创建合并单元格对象(开始行，结束行，开始列，结束列)  表头标题
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 15);
            CellRangeAddress cellRangeAddress1 = new CellRangeAddress(1, 1, 1, 5);
            CellRangeAddress cellRangeAddress2 = new CellRangeAddress(1, 1, 6, 10);
            CellRangeAddress cellRangeAddress3 = new CellRangeAddress(1, 1, 11, 15);
            //设置表头居中
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            //设置字体
            Font font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short)16);
            cellStyle.setFont(font);
            //创建工作表
            Sheet sheet = workbook.createSheet("首单月份销售报表");
            //加载合并单元格对象
            sheet.addMergedRegion(cellRangeAddress);
            sheet.addMergedRegion(cellRangeAddress1);
            sheet.addMergedRegion(cellRangeAddress2);
            sheet.addMergedRegion(cellRangeAddress3);
            //创建行
            //创建标题行，并设置头标题
            Row row = sheet.createRow(0);
            row.setHeight((short)(500));
            Cell cell = row.createCell(0);
            //设置头标题
            cell.setCellValue("首单月份销售报表");
            cell.setCellStyle(cellStyle);
            //第二行
            Row row1 = sheet.createRow(1);
            Cell cell1 = row1.createCell(1);
            cell1.setCellValue("首单配送销售金额");
            cell1.setCellStyle(cellStyle);
            Cell cell2 = row1.createCell(6);
            cell2.setCellValue("首单直送销售金额");
            cell2.setCellStyle(cellStyle);
            Cell cell3 = row1.createCell(11);
            cell3.setCellValue("首单货架销售金额");
            cell3.setCellStyle(cellStyle);
            //第三行
            Row row2 = sheet.createRow(2);
            String[] titles = {"经营区域", "销售金额", "同期", "上期", "同比", "环比", "本期",
                    "同期", "上期", "同比", "环比", "本期", "同期", "上期", "同比", "环比"};
            for (int i = 0; i < titles.length; i++) {
                Cell cell4 = row2.createCell(i);
                cell4.setCellValue(titles[i]);
            }
            //将报表信息写入Excel
            if (firstReportInfos != null) {
                for (int i = 0; i < firstReportInfos.size(); i++) {
                    //创建第4行
                    Row row3 = sheet.createRow(i + 3);
                    //获取出来每一个对象
                    FirstReportInfo firstReportInfo = firstReportInfos.get(i);
                    //从第一列开始写入值
                    //经营区域
                    Cell cell4 = row3.createCell(0);
                    cell4.setCellValue(firstReportInfo.getCopartnerAreaName());
                    //首单配送销售金额
                    Cell cell5 = row3.createCell(1);
                    cell5.setCellValue((firstReportInfo.getPsSalesAmount()).toString());
                    Cell cell6 = row3.createCell(2);
                    cell6.setCellValue((firstReportInfo.getPsSamePeriod()).toString());
                    Cell cell7 = row3.createCell(3);
                    cell7.setCellValue((firstReportInfo.getPsOnPeriod()).toString());
                    Cell cell8 = row3.createCell(4);
                    cell8.setCellValue((firstReportInfo.getPsSameRatio()).toString());
                    Cell cell9 = row3.createCell(5);
                    cell9.setCellValue((firstReportInfo.getPsRingRatio()).toString());
                    //首单直送
                    Cell cell10 = row3.createCell(6);
                    cell10.setCellValue((firstReportInfo.getZsSalesAmount()).toString());
                    Cell cell11 = row3.createCell(7);
                    cell11.setCellValue((firstReportInfo.getZsSamePeriod()).toString());
                    Cell cell12 = row3.createCell(8);
                    cell12.setCellValue((firstReportInfo.getZsOnPeriod()).toString());
                    Cell cell13 = row3.createCell(9);
                    cell13.setCellValue((firstReportInfo.getZsSameRatio()).toString());
                    Cell cell14 = row3.createCell(10);
                    cell14.setCellValue((firstReportInfo.getZsRingRatio()).toString());
                    //首单货架
                    Cell cell15 = row3.createCell(11);
                    cell15.setCellValue((firstReportInfo.getHjSalesAmount()).toString());
                    Cell cell16 = row3.createCell(12);
                    cell16.setCellValue((firstReportInfo.getHjSamePeriod()).toString());
                    Cell cell17 = row3.createCell(13);
                    cell17.setCellValue((firstReportInfo.getHjOnPeriod()).toString());
                    Cell cell18 = row3.createCell(14);
                    cell18.setCellValue((firstReportInfo.getHjSameRatio()).toString());
                    Cell cell19 = row3.createCell(15);
                    cell19.setCellValue((firstReportInfo.getHjRingRatio()).toString());
                }
            }
            //使用输出流
            String fileName = "首单销售报表";
            fileName = URLEncoder.encode(fileName, "UTF8");
            resp.setContentType("application/vnd.ms-excel;chartset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xls");
            ServletOutputStream out = resp.getOutputStream();
            //workbook内容写入文件中
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }



    /**
     * 导出年份报表
     * @return
     * @throws Exception
     */
    @ApiOperation("导出年份报表")
    @GetMapping("/exportReportYear")
    public void exportReportDatas(HttpServletResponse resp,String reportTime) {
        try {
            //获取报表每月数据集合
            List<FirstReportInfo> firstReportInfos = firstReportInfoDao.selcetReportInfoAll(reportTime);
            //创建新的Excel工作薄
            Workbook workbook = new XSSFWorkbook();
            //创建合并单元格对象(开始行，结束行，开始列，结束列)  表头标题
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 16);
            CellRangeAddress cellRangeAddress1 = new CellRangeAddress(1, 1, 1, 5);
            CellRangeAddress cellRangeAddress2 = new CellRangeAddress(1, 1, 6, 10);
            CellRangeAddress cellRangeAddress3 = new CellRangeAddress(1, 1, 11, 15);
            //设置表头居中
            CellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            //设置字体
            Font font = workbook.createFont();
            font.setFontName("黑体");
            font.setFontHeightInPoints((short)16);
            cellStyle.setFont(font);
            //创建工作表
            Sheet sheet = workbook.createSheet("首单年份销售报表");
            //加载合并单元格对象
            sheet.addMergedRegion(cellRangeAddress);
            sheet.addMergedRegion(cellRangeAddress1);
            sheet.addMergedRegion(cellRangeAddress2);
            sheet.addMergedRegion(cellRangeAddress3);
            //创建行
            //创建标题行，并设置头标题
            Row row = sheet.createRow(0);
            row.setHeight((short)(500));
            Cell cell = row.createCell(0);
            //设置头标题
            cell.setCellValue("首单年份销售报表");
            cell.setCellStyle(cellStyle);
            //第二行
            Row row1 = sheet.createRow(1);
            Cell cell1 = row1.createCell(1);
            cell1.setCellValue("首单配送销售金额");
            cell1.setCellStyle(cellStyle);
            Cell cell2 = row1.createCell(6);
            cell2.setCellValue("首单直送销售金额");
            cell2.setCellStyle(cellStyle);
            Cell cell3 = row1.createCell(11);
            cell3.setCellValue("首单货架销售金额");
            cell3.setCellStyle(cellStyle);
            //第三行
            Row row2 = sheet.createRow(2);
            String[] titles = {"月份","经营区域", "销售金额", "同期", "上期", "同比", "环比", "本期",
                    "同期", "上期", "同比", "环比", "本期", "同期", "上期", "同比", "环比"};
            for (int i = 0; i < titles.length; i++) {
                Cell cell4 = row2.createCell(i);
                cell4.setCellValue(titles[i]);
            }
            //将报表信息写入Excel
            if (firstReportInfos != null) {
                for (int i = 0; i < firstReportInfos.size(); i++) {
                    //创建第4行
                    Row row3 = sheet.createRow(i + 3);
                    //获取出来每一个对象
                    FirstReportInfo firstReportInfo = firstReportInfos.get(i);
                    //从第一列开始写入值
                    Cell cell4 = row3.createCell(0);
                    cell4.setCellValue(firstReportInfo.getReportTime());
                    //经营区域
                    Cell cell5 = row3.createCell(1);
                    cell5.setCellValue(firstReportInfo.getCopartnerAreaName());
                    //首单配送销售金额
                    Cell cell6 = row3.createCell(2);
                    cell6.setCellValue((firstReportInfo.getPsSalesAmount()).toString());
                    Cell cell7 = row3.createCell(3);
                    cell7.setCellValue((firstReportInfo.getPsSamePeriod()).toString());
                    Cell cell8 = row3.createCell(4);
                    cell8.setCellValue((firstReportInfo.getPsOnPeriod()).toString());
                    Cell cell9 = row3.createCell(5);
                    cell9.setCellValue((firstReportInfo.getPsSameRatio()).toString());
                    Cell cell10 = row3.createCell(6);
                    cell10.setCellValue((firstReportInfo.getPsRingRatio()).toString());
                    //首单直送
                    Cell cell11 = row3.createCell(7);
                    cell11.setCellValue((firstReportInfo.getZsSalesAmount()).toString());
                    Cell cell12 = row3.createCell(8);
                    cell12.setCellValue((firstReportInfo.getZsSamePeriod()).toString());
                    Cell cell13 = row3.createCell(9);
                    cell13.setCellValue((firstReportInfo.getZsOnPeriod()).toString());
                    Cell cell14 = row3.createCell(10);
                    cell14.setCellValue((firstReportInfo.getZsSameRatio()).toString());
                    Cell cell15 = row3.createCell(11);
                    cell15.setCellValue((firstReportInfo.getZsRingRatio()).toString());
                    //首单货架
                    Cell cell16 = row3.createCell(12);
                    cell16.setCellValue((firstReportInfo.getHjSalesAmount()).toString());
                    Cell cell17 = row3.createCell(13);
                    cell17.setCellValue((firstReportInfo.getHjSamePeriod()).toString());
                    Cell cell18 = row3.createCell(14);
                    cell18.setCellValue((firstReportInfo.getHjOnPeriod()).toString());
                    Cell cell19 = row3.createCell(15);
                    cell19.setCellValue((firstReportInfo.getHjSameRatio()).toString());
                    Cell cell20 = row3.createCell(16);
                    cell20.setCellValue((firstReportInfo.getHjRingRatio()).toString());
                }
            }
            //使用输出流
            String fileName = "首单销售报表";
            fileName = URLEncoder.encode(fileName, "UTF8");
            resp.setContentType("application/vnd.ms-excel;chartset=utf-8");
            resp.setHeader("Content-Disposition", "attachment;filename="+fileName + ".xls");
            ServletOutputStream out = resp.getOutputStream();
            //workbook内容写入文件中
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
