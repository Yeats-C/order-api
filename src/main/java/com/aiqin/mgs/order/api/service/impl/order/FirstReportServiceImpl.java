package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.FirstReportDao;
import com.aiqin.mgs.order.api.dao.FirstReportInfoDao;
import com.aiqin.mgs.order.api.domain.FirstReportInfo;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.FirstReportStoreAndOrderRerequest;
import com.aiqin.mgs.order.api.domain.response.FirstReportResponse;
import com.aiqin.mgs.order.api.service.FirstReportService;
import com.aiqin.mgs.order.api.util.ResultModel;
import com.github.pagehelper.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class FirstReportServiceImpl implements FirstReportService {

    @Autowired
    private FirstReportDao firstReportDao;

    @Autowired
    private FirstReportInfoDao firstReportInfoDao;

    @Override
    @Transactional
    public void reportTimedTask() {
        //获取经营区域id和名称
        List<FirstReportResponse> firstReportResponseList = firstReportDao.QueryArea();
        log.info("获取区域id以及名称,返回结果：" + firstReportResponseList);
        if (CollectionUtils.isEmpty(firstReportResponseList)) {
            log.info("获取经营区域为空");
            return;
        }
        //遍历获取的区域集合
        for (FirstReportResponse Firs : firstReportResponseList) {
            String copartnerAreaId = Firs.getCopartner_area_id();
            String copartnerAreaName = Firs.getCopartner_area_name();
            List<String> storeIds = firstReportDao.QueryStoreByAreaId(copartnerAreaId);
            log.info("区域id：" + copartnerAreaId + "获取门店id集合，返回结果：" + storeIds);
            if (CollectionUtils.isEmpty(storeIds)) {
                log.info("获取门店id集合为空");
                continue;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Date time = new Date();
            //获取Calendar对象
            Calendar rightNow = Calendar.getInstance();
            //使用给定的time设置此日历的时间
            rightNow.setTime(time);
            //日历的规则，给指定字段添加或减少时间量
            rightNow.add(Calendar.DAY_OF_MONTH, -1);//因为统计的是前一天的数据，所以日期减1
            //返回一个dete为日历时间
            Date d = rightNow.getTime();
            String countTime = sdf.format(d);
            log.info("时间：" + countTime);
            //销售金额参数对象
            FirstReportStoreAndOrderRerequest firstReportStoreAndOrderRerequest = new FirstReportStoreAndOrderRerequest();
            firstReportStoreAndOrderRerequest.setRecords(storeIds);
            firstReportStoreAndOrderRerequest.setOrder_category_code("2");
            firstReportStoreAndOrderRerequest.setOrder_type_code("1");
            firstReportStoreAndOrderRerequest.setCountTime(countTime);
            List<ErpOrderInfo> erpOrderInfos = firstReportDao.selectOrders(firstReportStoreAndOrderRerequest);
            firstReportStoreAndOrderRerequest.setOrder_category_code("2");
            firstReportStoreAndOrderRerequest.setOrder_type_code("2");
            List<ErpOrderInfo> erpOrderInfos1 = firstReportDao.selectOrders(firstReportStoreAndOrderRerequest);
            firstReportStoreAndOrderRerequest.setOrder_category_code("16");
            firstReportStoreAndOrderRerequest.setOrder_type_code("3");
            List<ErpOrderInfo> erpOrderInfos2 = firstReportDao.selectOrders(firstReportStoreAndOrderRerequest);
            Boolean a = false;
            Boolean b = false;
            Boolean c = false;
            if (CollectionUtils.isEmpty(erpOrderInfos)) {
                a = true;
            }
            if (CollectionUtils.isEmpty(erpOrderInfos1)) {
                b = true;
            }
            if (CollectionUtils.isEmpty(erpOrderInfos2)) {
                c = true;
            }
            if(a&&b&&c){//三种销售订单为空
                continue;
            }
            //首单直送销售初始金额
            BigDecimal aa=new BigDecimal(0);
            //首单配送销售初始金额
            BigDecimal bb=new BigDecimal(0);
            //首单货架销售初始金额
            BigDecimal cc=new BigDecimal(0);
            //首单直送销售金额
            if(!a){ //如果是真的
                firstReportStoreAndOrderRerequest.setOrder_category_code("2");
                firstReportStoreAndOrderRerequest.setOrder_type_code("1");
                BigDecimal bigDecimal = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
                if (bigDecimal != null) {
                    aa =bigDecimal;
                }
            }
            //首单配送销售金额
            if(!b){
                firstReportStoreAndOrderRerequest.setOrder_category_code("2");
                firstReportStoreAndOrderRerequest.setOrder_type_code("2");
                BigDecimal bigDecimal = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
                if (bigDecimal != null) {
                    bb =bigDecimal;
                }
            }
            //首单货架销售金额
            if(!c){
                firstReportStoreAndOrderRerequest.setOrder_category_code("16");
                firstReportStoreAndOrderRerequest.setOrder_type_code("3");
                BigDecimal bigDecimal = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
                if (bigDecimal != null) {
                    cc =bigDecimal;
                }
            }
            log.info("首单直送金额：" + bb+",首单货架金额:"+aa+",首单配送金额:"+cc);
            //创建首单报表对象
            FirstReportInfo firstReportInfo = new FirstReportInfo();
            firstReportInfo.setZsSalesAmount(aa);
            firstReportInfo.setPsSalesAmount(bb);
            firstReportInfo.setHjSalesAmount(cc);
            firstReportInfo.setCopartnerAreaId(copartnerAreaId);
            firstReportInfo.setCopartnerAreaName(copartnerAreaName);
            FirstReportInfo firstReportInfo1=new FirstReportInfo();
            firstReportInfo1.setCopartnerAreaId(copartnerAreaId);
            rightNow.setTime(time);
            rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
            rightNow.add(Calendar.YEAR,-1);//日期减1年
            Date dt1=rightNow.getTime();
            String reStr = sdf.format(dt1);
            firstReportInfo1.setReportTime(reStr);
            //查询同比数据
            FirstReportInfo firstReportInfo2 = firstReportInfoDao.selectReportInfo(firstReportInfo1);
            rightNow.setTime(time);
            rightNow.add(Calendar.MONTH,-1);//日期减1个月
            Date dt2=rightNow.getTime();
            String reStr2 = sdf.format(dt2);
            firstReportInfo1.setReportTime(reStr2);
            //查询环比数据
            FirstReportInfo firstReportInfo3 = firstReportInfoDao.selectReportInfo(firstReportInfo1);
            firstReportInfo.setZsSalesAmount(aa);
            firstReportInfo.setPsSalesAmount(bb);
            firstReportInfo.setHjSalesAmount(cc);
            BigDecimal aaa=new BigDecimal(1.0000);
            BigDecimal bbb=new BigDecimal(1.0000);
            BigDecimal ccc=new BigDecimal(1.0000);
            BigDecimal ddd=new BigDecimal(1.0000);
            BigDecimal eee=new BigDecimal(1.0000);
            BigDecimal fff=new BigDecimal(1.0000);
            if(firstReportInfo2!=null&&null!=firstReportInfo2.getPsSamePeriod()){
                aaa=bb.subtract(firstReportInfo2.getPsSamePeriod()).divide(firstReportInfo2.getPsSamePeriod());
            }
            if(firstReportInfo3!=null&&null!=firstReportInfo3.getPsOnPeriod()){
                ddd=bb.subtract(firstReportInfo3.getPsOnPeriod()).divide(firstReportInfo3.getPsOnPeriod());
            }
            firstReportInfo.setPsRingRatio(ddd.multiply(new BigDecimal(100)));
            firstReportInfo.setPsSameRatio(aaa.multiply(new BigDecimal(100)));
            firstReportInfo.setPsOnPeriod(firstReportInfo3.getPsOnPeriod());
            firstReportInfo.setPsSamePeriod(firstReportInfo2.getPsSamePeriod());

            if(firstReportInfo2!=null&&null!=firstReportInfo2.getHjSamePeriod()){
                bbb=cc.subtract(firstReportInfo2.getHjSamePeriod()).divide(firstReportInfo2.getHjSamePeriod());
            }
            if(firstReportInfo3!=null&&null!=firstReportInfo3.getHjOnPeriod()){
                eee=bb.subtract(firstReportInfo3.getHjOnPeriod()).divide(firstReportInfo3.getHjOnPeriod());
            }
            firstReportInfo.setHjSameRatio(bbb.multiply(new BigDecimal(100)));
            firstReportInfo.setHjOnPeriod(firstReportInfo3.getHjOnPeriod());
            firstReportInfo.setHjSamePeriod(firstReportInfo2.getHjSamePeriod());
            firstReportInfo.setHjRingRatio(eee.multiply(new BigDecimal(100)));

            if(firstReportInfo2!=null&&null!=firstReportInfo2.getZsSamePeriod()){
                ccc=aa.subtract(firstReportInfo2.getZsSamePeriod()).divide(firstReportInfo2.getZsSamePeriod());
            }
            if(firstReportInfo3!=null&&null!=firstReportInfo3.getZsOnPeriod()){
                fff=bb.subtract(firstReportInfo3.getZsOnPeriod()).divide(firstReportInfo3.getZsOnPeriod());
            }
            firstReportInfo.setZsOnPeriod(firstReportInfo3.getZsOnPeriod());
            firstReportInfo.setZsRingRatio(fff.multiply(new BigDecimal(100)));
            firstReportInfo.setZsSamePeriod(firstReportInfo2.getZsSamePeriod());
            firstReportInfo.setZsSameRatio(ccc.multiply(new BigDecimal(100)));
            firstReportInfo.setCreateTime(new Date());
            rightNow.setTime(time);
            rightNow.add(Calendar.DAY_OF_MONTH, -1);//因为统计的是前一天的数据，所以日期减1
            Date dt3=rightNow.getTime();
            String reStr3 = sdf.format(dt3);
            firstReportInfo.setReportTime(reStr3);
            firstReportInfo1.setReportTime(reStr3);
            //删除数据
            firstReportInfoDao.delete(firstReportInfo1);
            //插入数据
            firstReportInfoDao.insert(firstReportInfo);
        }
    }

    /**
     * 根据报表时间获取表格数据
     *
     * @param reportTime
     * @return
     */
    @Override
    public HttpResponse getLists(String reportTime) {
        log.info("获取首单报表表格数据入参：{}", reportTime);
        if (reportTime == null) {
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        List<FirstReportInfo> firstReportInfos = firstReportInfoDao.reportLis(reportTime);
        log.info("查询首单报表表格数据，返回结果 : " + firstReportInfos);
        if (firstReportInfos == null) {
            return HttpResponse.failure(ResultCode.FIRST_REPORT_ERROP);
        }
        ResultModel resultModel = new ResultModel();
        resultModel.setResult(firstReportInfos);
        resultModel.setTotal(((Page) firstReportInfos).getTotal());
        return HttpResponse.success(resultModel);
    }
}
