package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDao;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDetailDao;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportStoreGoodsCountResponse;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * description: ReportStoreGoodsServiceImpl
 * date: 2020/2/19 17:25
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class ReportStoreGoodsServiceImpl implements ReportStoreGoodsService {

    @Autowired
    private ReportStoreGoodsDao reportStoreGoodsDao;
    @Autowired
    private ReportStoreGoodsDetailDao reportStoreGoodsDetailDao;

    @Override
    public Boolean insert(ReportStoreGoods entity) {
        entity.setCreateTime(new Date());
        return reportStoreGoodsDao.insertSelective(entity)>0;
    }

    @Override
    public Boolean update(ReportStoreGoods entity) {
        return reportStoreGoodsDao.updateByPrimaryKeySelective(entity)>0;
    }

    @Override
    public PageResData<ReportStoreGoods> getList(PageRequestVO<ReportStoreGoodsVo> searchVo) {
        PageResData result=new PageResData();
        if(searchVo.getSearchVO()!=null&&StringUtils.isNotBlank(searchVo.getSearchVO().getStoreCode())&&StringUtils.isNotBlank(searchVo.getSearchVO().getCountTime())){
            PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
            List<ReportStoreGoods> reportStoreGoods = reportStoreGoodsDao.selectList(searchVo.getSearchVO());
            result.setTotalCount((int)((Page)reportStoreGoods).getTotal());
            result.setDataList(reportStoreGoods);
        }
        return result;
    }

    @Override
    public List<ReportStoreGoodsDetail> getCountDetailList(ReportStoreGoodsDetailVo searchVo) {
        List<ReportStoreGoodsDetail> list=new ArrayList<>();
        if(StringUtils.isNotBlank(searchVo.getStoreCode())&&StringUtils.isNotBlank(searchVo.getBrandId())&&StringUtils.isNotBlank(searchVo.getCountTime())){
            list =reportStoreGoodsDetailDao.selectList(searchVo);
        }
        return list;
    }

    @Override
    @Transactional
    public void dealWith() {
        long start=System.currentTimeMillis();
        log.info("开始执行门店补货定时任务");
//        String url="http://slcs.api.aiqin.com/store/getAllStoreCode";
//        log.info("查询所有门店编码,请求url={}", url);
//        HttpClient httpClient = HttpClient.get(url);
//        Map<String, Object> result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
//        log.info("调用slcs系统返回结果，result={}", JSON.toJSON(result));
//        if(result==null){
//            log.info("调用门店系统--查询所有门店编码失败");
//            return;
//        }
        List<String> jsonMap=new ArrayList<>();
//        if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
//            jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
//            log.info("jsonMap:"+jsonMap);
//        } else {
//            log.info("查询所有门店编码为空");
//            return;
//        }
        jsonMap.add("60000028");
        //判断门店编码集合是否为空，不为空，遍历查询信息
        if(CollectionUtils.isNotEmpty(jsonMap)){
            for(String storeCode:jsonMap){
                //查询门店下补货品牌、数量（主表）
                List<ReportStoreGoods> reportStoreGoodsCountResponses = reportStoreGoodsDao.selectProductCount(storeCode);
                if(CollectionUtils.isEmpty(reportStoreGoodsCountResponses)){//未查询到订单数据
                    log.info("查询"+storeCode+"该门店下补货品牌、数量为空");
                    continue;
                }
                //查询门店下补货，各个品牌下商品信息
                List<ReportStoreGoodsDetail> reportStoreGoodsDetails = reportStoreGoodsDao.selectReportStoreGoodsDetail(storeCode);
                if(CollectionUtils.isEmpty(reportStoreGoodsDetails)){
                    log.info("查询"+storeCode+"该门店下补货，各个品牌下商品信息为空");
                    continue;
                }
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM");
                Date time=new Date();
                String countTime=sdf.format(time);
                reportStoreGoodsDetails.forEach(p -> p.setStoreCode(storeCode));
                reportStoreGoodsDetails.forEach(p -> p.setCreateTime(new Date()));
                reportStoreGoodsDetails.forEach(p -> p.setCountTime(countTime));
                //插入门店补货列表统计detail报表
                reportStoreGoodsDetailDao.insertBatch(reportStoreGoodsDetails);
                reportStoreGoodsCountResponses=reportStoreGoodsCountResponses.stream().map(detailVo -> {
                    ReportStoreGoods detail = new ReportStoreGoods();
                    BeanUtils.copyProperties(detailVo,detail);
                    String productBrandCode = detail.getBrandId();
                    ReportStoreGoodsDetailVo reportStoreGoodsDetailVo=new ReportStoreGoodsDetailVo();
                    reportStoreGoodsDetailVo.setStoreCode(storeCode);
                    reportStoreGoodsDetailVo.setBrandId(productBrandCode);
                    reportStoreGoodsDetailVo.setCountTime(countTime);
                    //各个品牌总金额
                    BigDecimal amount = reportStoreGoodsDetailDao.sumAmountByBrandId(reportStoreGoodsDetailVo);
                    detail.setAmount(amount);
                    detail.setCreateTime(new Date());
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.setTime(time);
                    rightNow.add(Calendar.YEAR,-1);//日期减1年
                    Date dt1=rightNow.getTime();
                    String reStr = sdf.format(dt1);
                    ReportStoreGoods r=new ReportStoreGoods();
                    r.setStoreCode(storeCode);
                    r.setCountTime(reStr);
                    r.setBrandId(productBrandCode);
                    //计算同比
                    ReportStoreGoods reportStoreGoods = reportStoreGoodsDao.selectReportStoreGoods(r);
                    BigDecimal a=new BigDecimal(100.0000);
                    BigDecimal b=new BigDecimal(100.0000);
                    if(reportStoreGoods!=null&&reportStoreGoods.getNum()>0){
                        if(null!=detail.getAmount()){
                            BigDecimal cha=detail.getAmount().subtract(reportStoreGoods.getAmount());
                            a=cha.divide(reportStoreGoods.getAmount());
                        }
                    }
                    rightNow.setTime(time);
                    rightNow.add(Calendar.MONTH,-1);//日期减1个月
                    Date dt2=rightNow.getTime();
                    String reStr1 = sdf.format(dt2);
                    r.setCountTime(reStr1);
                    //计算环比
                    reportStoreGoods = reportStoreGoodsDao.selectReportStoreGoods(r);
                    if(reportStoreGoods!=null&&reportStoreGoods.getNum()>0){
                        if(null!=detail.getNum()){
                            BigDecimal cha=detail.getAmount().subtract(reportStoreGoods.getAmount());
                            b=cha.divide(reportStoreGoods.getAmount());
                        }
                    }
                    detail.setChainRatio(b);
                    detail.setTongRatio(a);
                    detail.setCountTime(countTime);
                    detail.setStoreCode(storeCode);
                    return detail;
                }).collect(Collectors.toList());
                reportStoreGoodsDao.insertBatch(reportStoreGoodsCountResponses);
                //查询各个sku对应的总金额
//                List<BigDecimal> amounts=reportStoreGoodsDetails.stream().map(ReportStoreGoodsDetail::getAmount).collect(Collectors.toList());
//                BigDecimal totalAmount=new BigDecimal(0);
//                for(BigDecimal amount:amounts){
//                    totalAmount=totalAmount.add(amount);
//                }
            }
        }else{
            log.info("校验查询所有门店编码为空");
        }
        log.info("执行门店补货定时任务结束，消耗时间:"+(System.currentTimeMillis()-start));
    }

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("http://slcs.api.aiqin.com/store/getAllStoreCode");
        log.info("查询dl闭店审批信息,请求url为{}", sb);
        HttpClient httpClient = HttpClient.get(sb.toString());
        Map<String, Object> result;
        result = httpClient.action().result(new TypeReference<Map<String, Object>>() {
        });
        log.info("调用主控反参result{}", JSON.toJSON(result));
        if (StringUtils.isNotBlank(String.valueOf(result.get("code"))) && "0".equals(String.valueOf(result.get("code")))) {
            List<String> jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
            log.info("jsonMap:"+jsonMap);
        } else {
            log.info("33333333333333333333333");
    }

    }
}
