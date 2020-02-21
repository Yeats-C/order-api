package com.aiqin.mgs.order.api.service.impl.order;

import com.aiqin.mgs.order.api.dao.FirstReportDao;
import com.aiqin.mgs.order.api.domain.request.FirstReportStoreAndOrderRerequest;
import com.aiqin.mgs.order.api.domain.response.FirstReportResponse;
import com.aiqin.mgs.order.api.service.FirstReportService;
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

@Slf4j
@Service
public class FirstReportServiceImpl implements FirstReportService {

    @Autowired
    private FirstReportDao firstReportDao;

    @Override
    @Transactional
    public void reportTimedTask() {
        //获取经营区域id和名称
        List<FirstReportResponse> firstReportResponseList = firstReportDao.QueryArea();
        log.info("获取区域id以及名称,返回结果：" + firstReportResponseList);
           if(CollectionUtils.isEmpty(firstReportResponseList)){
            log.info("获取经营区域为空");
            return;
           }
        //遍历获取的区域集合
        List<String> AraeId = new ArrayList<>();
          for (FirstReportResponse Firs : firstReportResponseList) {
                  String copartnerAreaId = Firs.getCopartner_area_id();
                  AraeId.add(copartnerAreaId);
          }
        //根据经营区域id去查询门店
        log.info("区域id集合：" + AraeId);
        List<String> StoreIdList = firstReportDao.QueryStore(AraeId);
        log.info("获取门店id集合，返回结果：" + StoreIdList);
          if(CollectionUtils.isEmpty(StoreIdList)){
              log.info("获取门店id集合为空");
              return;
          }
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
        Date time=new Date();
        //获取Calendar对象
        Calendar rightNow = Calendar.getInstance();
        //使用给定的time设置此日历的时间
        rightNow.setTime(time);
        //日历的规则，给指定字段添加或减少时间量
        rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
        //返回一个dete为日历时间
        Date d=rightNow.getTime();
        String countTime = sdf.format(d);
        log.info("时间：" + countTime);
        //销售金额参数对象
        FirstReportStoreAndOrderRerequest firstReportStoreAndOrderRerequest = new FirstReportStoreAndOrderRerequest();
        firstReportStoreAndOrderRerequest.setRecords(StoreIdList);

        firstReportStoreAndOrderRerequest.setOrder_category_code("2");
        firstReportStoreAndOrderRerequest.setOrder_type_code("1");
        firstReportStoreAndOrderRerequest.setCountTime(countTime);
        //首单直送销售金额
        BigDecimal bigDecimal = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
//        if(bigDecimal == null){
//            bigDecimal = new BigDecimal(0);
//        }
        firstReportStoreAndOrderRerequest.setOrder_category_code("2");
        firstReportStoreAndOrderRerequest.setOrder_type_code("2");
        //首单配送销售金额
        BigDecimal bigDecimal1 = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
//        if(bigDecimal1 == null){
//            bigDecimal1 = new BigDecimal(0);
//        }
        firstReportStoreAndOrderRerequest.setOrder_category_code("16");
        firstReportStoreAndOrderRerequest.setOrder_type_code("3");
        //首单货架销售金额
        BigDecimal bigDecimal2 = firstReportDao.selectOrder(firstReportStoreAndOrderRerequest);
//        if(bigDecimal2 == null){
//            bigDecimal2 = new BigDecimal(0);
//        }
        log.info("首单货架金额：" + bigDecimal2);
        log.info("首单直送金额：" + bigDecimal);
        log.info("首单配送金额：" + bigDecimal1);
    }
}
