package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.component.returnenums.OrderTypeEnum;
import com.aiqin.mgs.order.api.component.returnenums.ReturnReasonEnum;
import com.aiqin.mgs.order.api.dao.ReportAreaReturnSituationDao;
import com.aiqin.mgs.order.api.dao.ReportCategoryGoodsDao;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDao;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.domain.ReportAreaReturnSituation;
import com.aiqin.mgs.order.api.domain.ReportCategoryGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.copartnerArea.NewStoreTreeResponse;
import com.aiqin.mgs.order.api.domain.request.ReportAreaReturnSituationVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ProvinceAreaResponse;
import com.aiqin.mgs.order.api.domain.response.report.ReportCategoryResponse;
import com.aiqin.mgs.order.api.domain.response.report.ReportOrderAndStoreListResponse;
import com.aiqin.mgs.order.api.domain.response.report.ReportOrderAndStoreResponse;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Map.Entry;

/**
 * description: ReportStoreGoodsServiceImpl
 * date: 2020/2/19 17:25
 * author: hantao
 * version: 1.0
 */
@Slf4j
@Service
public class ReportStoreGoodsServiceImpl implements ReportStoreGoodsService {

    @Value("${bridge.url.slcs_api}")
    private String slcsiHost;
    @Value("${bridge.url.product-api}")
    private String productHost;
    @Autowired
    private ReportStoreGoodsDao reportStoreGoodsDao;
    @Autowired
    private ReportStoreGoodsDetailDao reportStoreGoodsDetailDao;
    @Autowired
    private ReportAreaReturnSituationDao reportAreaReturnSituationDao;
    @Autowired
    private ReturnOrderDetailDao returnOrderDetailDao;
    @Autowired
    private ReportCategoryGoodsDao reportCategoryGoodsDao;

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
    public ReportOrderAndStoreListResponse<PageResData<ReportStoreGoods>> getList(PageRequestVO<ReportStoreGoodsVo> searchVo) {
        ReportOrderAndStoreListResponse res=new ReportOrderAndStoreListResponse();
        PageResData result=new PageResData();
        if(searchVo.getSearchVO()!=null&&StringUtils.isNotBlank(searchVo.getSearchVO().getStoreCode())&&StringUtils.isNotBlank(searchVo.getSearchVO().getCountTime())){
            PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
            List<ReportStoreGoods> reportStoreGoods = reportStoreGoodsDao.selectList(searchVo.getSearchVO());
            if(CollectionUtils.isNotEmpty(reportStoreGoods)){
                ReportStoreGoods reportStoreGoods1= reportStoreGoods.get(0);
                res.setTotalAmount(reportStoreGoods1.getTotalAmount());
                res.setTotalChainRatio(reportStoreGoods1.getTotalChainRatio());
                res.setTotalNum(reportStoreGoods1.getTotalNum());
                res.setTotalTongRatio(reportStoreGoods1.getTotalTongRatio());
            }
            result.setTotalCount((int)((Page)reportStoreGoods).getTotal());
            result.setDataList(reportStoreGoods);
            res.setPageResData(result);
        }
        return res;
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
    public void reportTimingTask() {
        long start=System.currentTimeMillis();
        String url=slcsiHost+"/store/getAllStoreCode";
        log.info("调用slcs系统,查询所有门店编码,请求url={}", url);
        HttpClient httpClient = HttpClient.get(url);
        Map<String, Object> result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        log.info("调用slcs系统,查询所有门店编码返回结果，result={}", JSON.toJSON(result));
        if(result==null){
            log.info("调用slcs系统,--查询所有门店编码失败");
            return;
        }
        List<String> jsonMap=new ArrayList<>();
        if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
            jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
            log.info("调用slcs系统,查询所有门店编码数组为:"+jsonMap);
        } else {
            log.info("调用slcs系统,查询所有门店编码为空");
            return;
        }
//        jsonMap.add("60000681");
//        jsonMap.add("1147621");
        //根据门店编码批量查询是否在本月下过订单
        List<ReportOrderAndStoreResponse> reportOrderAndStoreResponses = reportStoreGoodsDao.selectOrderByStoreCodes(jsonMap);
        if(CollectionUtils.isEmpty(reportOrderAndStoreResponses)){
            log.info("所有门店在本月均未补过货");
            return;
        }
        log.info("根据门店编码批量查询是否在本月下过订单，一共下单:"+reportOrderAndStoreResponses.size()+"次，消耗时间:"+(System.currentTimeMillis()-start));
        log.info("根据门店编码批量查询是否在本月下过订单，返回结果:"+reportOrderAndStoreResponses);
        Map<String,String> map=new HashMap<>();
        for(ReportOrderAndStoreResponse re:reportOrderAndStoreResponses){
            if(re!=null&&null!=re.getStoreCode()&&null!=re.getOrderStoreCode()){
                map.put(re.getStoreCode(),re.getOrderStoreCode());
            }
        }
        log.info("查询所有下过单的门店编码集合:"+map);
        //判断门店编码集合是否为空，不为空，遍历查询信息
        if(map!=null && map.size()>0){
            for(Entry<String, String> entry:map.entrySet()){
                //统计总的销售金额
                BigDecimal totalAmount=new BigDecimal(0);
                //统计总的销售数量
                Long totalNum=0L;
                String storeCode=entry.getKey();
                //查询门店下补货品牌、数量（主表）
                List<ReportStoreGoods> reportStoreGoodsCountResponses = reportStoreGoodsDao.selectProductCount(storeCode);
                if(CollectionUtils.isEmpty(reportStoreGoodsCountResponses)&&null!=reportStoreGoodsCountResponses&&reportStoreGoodsCountResponses.size()>0){//未查询到订单数据
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
                Calendar rightNow = Calendar.getInstance();
                rightNow.setTime(time);
                rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                Date d=rightNow.getTime();
                String countTime = sdf.format(d);
                reportStoreGoodsDetails.forEach(p -> p.setStoreCode(storeCode));
                reportStoreGoodsDetails.forEach(p -> p.setCreateTime(new Date()));
                reportStoreGoodsDetails.forEach(p -> p.setCountTime(countTime));
                //根据门店编码、统计时间清空数据
                ReportStoreGoodsDetailVo vo=new ReportStoreGoodsDetailVo();
                vo.setStoreCode(storeCode);
                vo.setCountTime(countTime);
                List<ReportStoreGoodsDetail> reportStoreGoodsDetails1 = reportStoreGoodsDetailDao.selectList(vo);
                if(CollectionUtils.isNotEmpty(reportStoreGoodsDetails1)){
                    reportStoreGoodsDetailDao.deleteByStoreCodeAndCountTime(vo);
                }
                //插入门店补货列表统计detail报表
                reportStoreGoodsDetailDao.insertBatch(reportStoreGoodsDetails);
                log.info("查询"+storeCode+"该门店下补货品牌、数量结果为reportStoreGoodsCountResponses={}",reportStoreGoodsCountResponses);
                for(ReportStoreGoods rsg:reportStoreGoodsCountResponses){
                    String productBrandCode ="";
                    if(StringUtils.isNotBlank(rsg.getBrandId())){
                        productBrandCode = rsg.getBrandId();
                    }
                    Long num = rsg.getNum();
                    if(null!=rsg.getNum()){
                        totalNum=totalNum+num;
                    }
                    ReportStoreGoodsDetailVo reportStoreGoodsDetailVo=new ReportStoreGoodsDetailVo();
                    reportStoreGoodsDetailVo.setStoreCode(storeCode);
                    reportStoreGoodsDetailVo.setBrandId(productBrandCode);
                    reportStoreGoodsDetailVo.setCountTime(countTime);
                    //各个品牌总金额
                    BigDecimal amount = reportStoreGoodsDetailDao.sumAmountByBrandId(reportStoreGoodsDetailVo);
                    if(amount==null){
                        amount=new BigDecimal(0);
                    }
                    totalAmount=totalAmount.add(amount);
                    rsg.setAmount(amount);
                    rsg.setCreateTime(new Date());
                    rightNow.setTime(time);
                    rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                    rightNow.add(Calendar.YEAR,-1);//日期减1年
                    Date dt1=rightNow.getTime();
                    String reStr = sdf.format(dt1);
                    ReportStoreGoods r=new ReportStoreGoods();
                    r.setStoreCode(storeCode);
                    r.setCountTime(reStr);
                    r.setBrandId(productBrandCode);
                    //计算同比
                    ReportStoreGoods reportStoreGoods = reportStoreGoodsDao.selectReportStoreGoods(r);
                    BigDecimal a=new BigDecimal(1.0000);
                    BigDecimal b=new BigDecimal(1.0000);
                    if(reportStoreGoods!=null&&reportStoreGoods.getNum()>0){
                        if(null!=rsg.getAmount()){
                            BigDecimal cha=rsg.getAmount().subtract(reportStoreGoods.getAmount());
                            a=cha.divide(reportStoreGoods.getAmount());
                        }
                    }
                    rightNow.setTime(time);
                    rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                    rightNow.add(Calendar.MONTH,-1);//日期减1个月
                    Date dt2=rightNow.getTime();
                    String reStr1 = sdf.format(dt2);
                    r.setCountTime(reStr1);
                    //计算环比
                    reportStoreGoods = reportStoreGoodsDao.selectReportStoreGoods(r);
                    if(reportStoreGoods!=null&&reportStoreGoods.getNum()>0){
                        if(null!=rsg.getNum()){
                            BigDecimal cha=rsg.getAmount().subtract(reportStoreGoods.getAmount());
                            b=cha.divide(reportStoreGoods.getAmount());
                        }
                    }
                    rsg.setChainRatio(b.multiply(new BigDecimal(100)));
                    rsg.setTongRatio(a.multiply(new BigDecimal(100)));
                    rsg.setCountTime(countTime);
                    rsg.setStoreCode(storeCode);
                }
                //根据门店编码、统计时间清空数据
                ReportStoreGoodsVo vo1=new ReportStoreGoodsVo();
                BeanUtils.copyProperties(vo,vo1);
                List<ReportStoreGoods> reportStoreGoods = reportStoreGoodsDao.selectList(vo1);
                if(CollectionUtils.isNotEmpty(reportStoreGoods)){
                    reportStoreGoodsDao.deleteByStoreCodeAndCountTime(vo);
                }
                //插入数据
                reportStoreGoodsDao.insertBatch(reportStoreGoodsCountResponses);
                //更新总的同比环比
                ReportStoreGoodsVo vo2=new ReportStoreGoodsVo();
                rightNow.setTime(time);
                rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                rightNow.add(Calendar.YEAR,-1);//日期减1年
                Date dt1=rightNow.getTime();
                String reStr = sdf.format(dt1);
                vo2.setStoreCode(storeCode);
                vo2.setCountTime(reStr);
                //计算总同比
                List<ReportStoreGoods> reportStoreGoods1 = reportStoreGoodsDao.selectList(vo2);
                BigDecimal a=new BigDecimal(1.0000);
                BigDecimal b=new BigDecimal(1.0000);
                if(CollectionUtils.isNotEmpty(reportStoreGoods1)){
                    ReportStoreGoods rsg2=reportStoreGoods1.get(0);
                    if(null!=rsg2.getTotalAmount()){
                        BigDecimal cha=totalAmount.subtract(rsg2.getTotalAmount());
                        a=cha.divide(rsg2.getAmount());
                    }
                }
                rightNow.setTime(time);
                rightNow.add(Calendar.DAY_OF_MONTH,-1);//因为统计的是前一天的数据，所以日期减1
                rightNow.add(Calendar.MONTH,-1);//日期减1个月
                Date dt2=rightNow.getTime();
                String reStr1 = sdf.format(dt2);
                vo2.setStoreCode(storeCode);
                vo2.setCountTime(reStr1);
                //计算总环比
                List<ReportStoreGoods> reportStoreGoods2 = reportStoreGoodsDao.selectList(vo2);
                if(CollectionUtils.isNotEmpty(reportStoreGoods2)){
                    ReportStoreGoods rsg2=reportStoreGoods2.get(0);
                    if(null!=rsg2.getTotalAmount()){
                        BigDecimal cha=totalAmount.subtract(rsg2.getTotalAmount());
                        b=cha.divide(rsg2.getAmount());
                    }
                }
                ReportStoreGoods reportStoreGood=new ReportStoreGoods();
                reportStoreGood.setStoreCode(storeCode);
                reportStoreGood.setCountTime(countTime);
                reportStoreGood.setTotalAmount(totalAmount);
                reportStoreGood.setTotalNum(totalNum);
                reportStoreGood.setTotalChainRatio(b.multiply(new BigDecimal(100)));
                reportStoreGood.setTotalTongRatio(a.multiply(new BigDecimal(100)));
                //更新总的销售数量、总金额、总的环比和同比
                reportStoreGoodsDao.updateByStoreCodeAndTime(reportStoreGood);
            }
        }else{
            log.info("校验查询所有门店编码为空");
        }
    }

    @Override
    public void areaReturnSituation(ReportAreaReturnSituationVo vo) {
        Integer category=1;
        if(vo.getType().equals(OrderTypeEnum.ORDER_TYPE_PS.getCode())&&vo.getReasonCode().equals(ReturnReasonEnum.ORDER_TYPE_ZS.getCode())){//配送质量
            category=2;
        }else if(vo.getType().equals(OrderTypeEnum.ORDER_TYPE_PS.getCode())&&vo.getReasonCode().equals(ReturnReasonEnum.ORDER_TYPE_PS.getCode())){//配送一般
            category=3;
        }
        String productUrl=productHost+"/area/province";
        log.info("调用product系统,查询所有省,请求url={}",productUrl);
        HttpClient httpClient1 = HttpClient.get(productUrl);
        Map<String,Object> res=null;
        res= httpClient1.action().result(new TypeReference<Map<String,Object>>() {});
        log.info("调用product系统,查询所有省返回结果，result={}", JSON.toJSON(res));
        List<ProvinceAreaResponse> proList =new ArrayList<>();
        if (StringUtils.isNotBlank(res.get("code").toString()) && "0".equals(String.valueOf(res.get("code")))) {
            proList = JSONArray.parseArray(JSON.toJSONString(res.get("data")), ProvinceAreaResponse.class);
            log.info("调用product系统,查询所有省集合为list={}",proList);
        } else {
            log.info("调用product系统,查询所有省异常");
            return;
        }
        List<ReportAreaReturnSituation> records=new ArrayList<>();
        List<String> provinceIds=new ArrayList<>();
        if(proList!=null&&proList.size()>0){
            for(ProvinceAreaResponse par:proList){
                String provinceId=par.getAreaId();
                provinceIds.add(provinceId);
                String provinceName=par.getAreaName();
                String url=slcsiHost+"/store/getStoresByAreaCode";
                JSONObject body=new JSONObject();
                body.put("province_id",provinceId);
                log.info("调用slcs系统,根据省查询所有门店编码,请求url={},body={}", url,body);
                HttpClient httpClient = HttpClient.post(url).json(body);
                Map<String,Object> result=null;
                result= httpClient.action().result(new TypeReference<Map<String,Object>>() {});
                log.info("调用slcs系统,根据省查询所有门店编码返回结果，result={}", JSON.toJSON(result));
                if(result==null){
                    log.info("调用slcs系统,根据省查询所有门店编码失败");
                    return;
                }
                List<String> storeCodes=new ArrayList<>();
                if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
                    List<NewStoreTreeResponse> list = JSONArray.parseArray(JSON.toJSONString(result.get("data")), NewStoreTreeResponse.class);
                    log.info("调用slcs系统,根据省查询所有门店为list={}",list);
                    storeCodes = list.stream().map(NewStoreTreeResponse::getStoreCode).collect(Collectors.toList());
                    log.info("调用slcs系统,根据省查询所有门店编码数组为storeCodes={}",storeCodes);
                } else {
                    log.info("调用slcs系统,根据省查询所有门店编码异常");
                    return;
                }
                BigDecimal amount=BigDecimal.ZERO;
                Long count=0L;
                if(storeCodes!=null&&storeCodes.size()>0){
                    vo.setStoreCodes(storeCodes);
                    ReportAreaReturnSituation rars = reportAreaReturnSituationDao.selectOrderAmountByStoreCodes(vo);
                    ReportAreaReturnSituation rars2 =reportAreaReturnSituationDao.selectOrderCountByStoreCodes(vo);
                    if(rars!=null&&rars.getReturnAmount()!=null){
                        amount=rars.getReturnAmount();
                    }
                    if(rars2!=null&&rars2.getReturnCount()!=null){
                        count=rars2.getReturnCount();
                    }
                }
                ReportAreaReturnSituation rars3=new ReportAreaReturnSituation();
                rars3.setCreateTime(new Date());
                rars3.setProvinceId(provinceId);
                rars3.setProvinceName(provinceName);
                rars3.setReasonCode(vo.getReasonCode());
                rars3.setReturnAmount(amount);
                rars3.setReturnCount(count);
                rars3.setType(category);
                records.add(rars3);
            }
            if(records!=null&&records.size()>0){
                ReportAreaReturnSituationVo vo1=new ReportAreaReturnSituationVo();
                vo1.setType(category);
                //清空数据
                reportAreaReturnSituationDao.deleteByType(vo1);
                //插入数据
                reportAreaReturnSituationDao.insertBatch(records);
            }
        } else {
            log.info("调用product系统,查询所有省数据为空");
            return;
        }
    }

    @Override
    public List<ReportAreaReturnSituation> topProvinceAmount(Integer type) {
        return reportAreaReturnSituationDao.topProvinceAmount(type);
    }

    @Override
    public void reportCategoryGoods(ReportAreaReturnSituationVo vo) {
        Integer insertType=1;
        if(vo.getType().equals(2)&&"14".equals(vo.getReasonCode())){
            insertType=2;
        }else if(vo.getType().equals(2)&&"15".equals(vo.getReasonCode())){
            insertType=3;
        }
        //查询所有一级品类的编码集合，然后遍历去查询金额
        String url=productHost+"/search/spu/sku/category";
        JSONObject body=new JSONObject();
        body.put("level",1);
        log.info("退货商品分类统计--调用product系统,查询所有一级品类,请求url={},body={}", url,body);
        HttpClient httpClient = HttpClient.post(url).json(body);
        Map<String,Object> result=null;
        result= httpClient.action().result(new TypeReference<Map<String,Object>>() {});
        log.info("退货商品分类统计--调用product系统,查询所有一级品类返回结果，result={}", JSON.toJSON(result));
        if(result==null){
            log.info("退货商品分类统计--调用product系统,查询所有一级品类失败");
            return;
        }
        List<ReportCategoryResponse> list=new ArrayList<>();
        if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
            list = JSONArray.parseArray(JSON.toJSONString(result.get("data")), ReportCategoryResponse.class);
            log.info("退货商品分类统计--调用product系统,查询所有一级品类为list={}",list);
        } else {
            log.info("退货商品分类统计--调用product系统,查询所有一级品类异常");
            return;
        }
        if(list!=null&&list.size()>0){
            Date time=new Date();
            List<ReportCategoryGoods> list1=new ArrayList();
            BigDecimal totalAmount=BigDecimal.ZERO;
            Map<String,BigDecimal> map=new HashMap<>();
            //第一次循环查询金额
            for(ReportCategoryResponse rcr:list){
                vo.setCategoryCode(rcr.getCategoryCode());
                //查出某个一级品类的总金额
                BigDecimal amount = returnOrderDetailDao.countAmountByCategoryCode(vo);
                totalAmount=totalAmount.add(amount);
                map.put(rcr.getCategoryCode(),amount);
            }
            log.info("退货商品分类统计--所有品类及各自金额map={}",map);
            log.info("退货商品分类统计--所有品类总金额totalAmount={}",totalAmount);
            //第二次循环计算比例
            for(ReportCategoryResponse rcr:list){
                vo.setCategoryCode(rcr.getCategoryCode());
                //从map中取出某个一级品类的总金额
                BigDecimal am = map.get(rcr.getCategoryCode());
                ReportCategoryGoods rcg=new ReportCategoryGoods();
                BigDecimal proportion=BigDecimal.ZERO;
                if(totalAmount.compareTo(BigDecimal.ZERO)==1){
                    proportion=am.divide(totalAmount);
                }
                rcg.setAmount(am);
                rcg.setCategoryCode(rcr.getCategoryCode());
                rcg.setCategoryName(rcr.getCategoryName());
                rcg.setType(insertType);
                rcg.setCreateTime(time);
                rcg.setProportion(proportion);
                list1.add(rcg);
            }
            log.info("退货商品分类统计--插入退货商品分类统计表,入参list={}",list1);
            if(list1!=null&&list1.size()>0){
                //删除数据
                reportCategoryGoodsDao.deleteByType(insertType);
                log.info("退货商品分类统计--清除"+insertType+"退货商品分类统计数据成功");
                //插入数据
                reportCategoryGoodsDao.insertBatch(list1);
                log.info("退货商品分类统计--插入退货商品分类统计表成功");
            }
        }
    }

    @Override
    public List<ReportCategoryGoods> getReportCategoryGoods(Integer type) {
        ReportAreaReturnSituationVo vo=new ReportAreaReturnSituationVo();
        vo.setType(type);
        return reportCategoryGoodsDao.selectList(vo);
    }

}
