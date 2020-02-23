package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.CopartnerAreaStoreDao;
import com.aiqin.mgs.order.api.dao.FirstReportDao;
import com.aiqin.mgs.order.api.dao.FirstReportInfoDao;
import com.aiqin.mgs.order.api.domain.FirstReportInfo;
import com.aiqin.mgs.order.api.domain.copartnerArea.PublicAreaStore;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.FirstReportStoreAndOrderRerequest;
import com.aiqin.mgs.order.api.domain.response.FirstReportResponse;
import com.aiqin.mgs.order.api.service.CopartnerAreaService;
import com.aiqin.mgs.order.api.service.FirstReportService;
import com.aiqin.mgs.order.api.util.ResultModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FirstReportServiceImpl implements FirstReportService {

    @Autowired
    private FirstReportDao firstReportDao;

    @Autowired
    private FirstReportInfoDao firstReportInfoDao;
    @Autowired
    private CopartnerAreaStoreDao copartnerAreaStoreDao;

    @Autowired
    private CopartnerAreaService copartnerAreaService;

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
            //查询同期数据
            FirstReportInfo firstReportInfo2 = firstReportInfoDao.selectReportInfo(firstReportInfo1);
            rightNow.setTime(time);
            rightNow.add(Calendar.MONTH,-1);//日期减1个月
            Date dt2=rightNow.getTime();
            String reStr2 = sdf.format(dt2);
            firstReportInfo1.setReportTime(reStr2);
            //查询上期数据
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
            //"首单配送同期
            BigDecimal PsSamePeriod=new BigDecimal(0);
            //首单配送上期
            BigDecimal PsOnPeriod=new BigDecimal(0);
            if(firstReportInfo2!=null&&null!=firstReportInfo2.getPsSamePeriod()){
                aaa=bb.subtract(firstReportInfo2.getPsSamePeriod()).divide(firstReportInfo2.getPsSamePeriod());
                PsSamePeriod=firstReportInfo2.getPsSamePeriod();
            }
            if(firstReportInfo3!=null&&null!=firstReportInfo3.getPsOnPeriod()){
                ddd=bb.subtract(firstReportInfo3.getPsOnPeriod()).divide(firstReportInfo3.getPsOnPeriod());
                PsOnPeriod=firstReportInfo3.getPsOnPeriod();
            }
            //首单配送环比
            firstReportInfo.setPsRingRatio(ddd.multiply(new BigDecimal(100)));
            //首单配送同比
            firstReportInfo.setPsSameRatio(aaa.multiply(new BigDecimal(100)));
            //首单配送上期
            firstReportInfo.setPsOnPeriod(PsOnPeriod);
            //首单配送同期
            firstReportInfo.setPsSamePeriod(PsSamePeriod);
            //首单货架同期
            BigDecimal HjSamePeriod=new BigDecimal(0);
            //首单货架上期
            BigDecimal HjOnPeriod=new BigDecimal(0);
            if(firstReportInfo2!=null&&null!=firstReportInfo2.getHjSamePeriod()){
                bbb=cc.subtract(firstReportInfo2.getHjSamePeriod()).divide(firstReportInfo2.getHjSamePeriod());
                HjSamePeriod=firstReportInfo2.getHjSamePeriod();
            }
            if(firstReportInfo3!=null&&null!=firstReportInfo3.getHjOnPeriod()){
                eee=bb.subtract(firstReportInfo3.getHjOnPeriod()).divide(firstReportInfo3.getHjOnPeriod());
                HjOnPeriod=firstReportInfo3.getHjOnPeriod();
            }
            //首单货架同比
            firstReportInfo.setHjSameRatio(bbb.multiply(new BigDecimal(100)));
            //首单货架上期
            firstReportInfo.setHjOnPeriod(HjOnPeriod);
            //首单货架同期
            firstReportInfo.setHjSamePeriod(HjSamePeriod);
            //首单货架环比
            firstReportInfo.setHjRingRatio(eee.multiply(new BigDecimal(100)));
            //首单直送同期
            BigDecimal ZsSamePeriod=new BigDecimal(0);
            //首单直送上期
            BigDecimal ZsOnPeriod=new BigDecimal(0);
            if(firstReportInfo2!=null&&null!=firstReportInfo2.getZsSamePeriod()){
                ccc=aa.subtract(firstReportInfo2.getZsSamePeriod()).divide(firstReportInfo2.getZsSamePeriod());
                ZsSamePeriod=firstReportInfo2.getZsSamePeriod();
            }
            if(firstReportInfo3!=null&&null!=firstReportInfo3.getZsOnPeriod()){
                fff=bb.subtract(firstReportInfo3.getZsOnPeriod()).divide(firstReportInfo3.getZsOnPeriod());
                ZsOnPeriod=firstReportInfo3.getZsOnPeriod();
            }
            //首单直送上期
            firstReportInfo.setZsOnPeriod(ZsOnPeriod);
            //首单直送环比
            firstReportInfo.setZsRingRatio(fff.multiply(new BigDecimal(100)));
            //首单直送同期
            firstReportInfo.setZsSamePeriod(ZsSamePeriod);
            //首单直送同比
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
    public HttpResponse getLists(String reportTime,Integer pageNo,Integer pageSize,String personId,String resourceCode) {
        log.info("获取首单报表表格数据入参：reportTime={},personId={},resourceCode={}", reportTime,personId,resourceCode);
        //引用合伙人区域公共接口
        HttpResponse httpResponse = copartnerAreaService.selectStoreByPerson(personId, resourceCode);
        Object obj=httpResponse.getData();
        ResultModel resultModel = new ResultModel();
        if (obj!=null) {
            List<PublicAreaStore> dataList = JSONArray.parseArray(JSON.toJSONString(obj), PublicAreaStore.class);
            List<String> storeIds=dataList.stream().map(PublicAreaStore::getStoreId).collect(Collectors.toList());
            log.info("门店id集合:"+storeIds);
            List<String> areaIds = copartnerAreaStoreDao.qryAreaByStores(storeIds);
            if(CollectionUtils.isEmpty(areaIds)){
                return HttpResponse.success(resultModel);
            }
            if (reportTime == null) {
                return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
            }
            FirstReportInfo firstReportInfo=new FirstReportInfo();
            firstReportInfo.setReportTime(reportTime);
            firstReportInfo.setAreaIds(areaIds);
            pageNo = pageNo == null ? 1 : pageNo;
            pageSize = pageSize == null ? 10 : pageSize;
            PageHelper.startPage(pageNo, pageSize);
            List<FirstReportInfo> firstReportInfos = firstReportInfoDao.reportLis(firstReportInfo);
            log.info("查询首单报表表格数据，返回结果 : " + firstReportInfos);
            if (firstReportInfos == null) {
                return HttpResponse.failure(ResultCode.FIRST_REPORT_ERROP);
            }
            resultModel.setResult(firstReportInfos);
            resultModel.setTotal(((Page) firstReportInfos).getTotal());
        }
        return HttpResponse.success(resultModel);
    }
}
