package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.http.HttpClient;
import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDao;
import com.aiqin.mgs.order.api.dao.ReportStoreGoodsDetailDao;
import com.aiqin.mgs.order.api.domain.ReportStoreGoods;
import com.aiqin.mgs.order.api.domain.ReportStoreGoodsDetail;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsDetailVo;
import com.aiqin.mgs.order.api.domain.request.ReportStoreGoodsVo;
import com.aiqin.mgs.order.api.domain.response.report.ReportStoreGoodsCountDetailResponse;
import com.aiqin.mgs.order.api.service.ReportStoreGoodsService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        List<ReportStoreGoods> reportStoreGoods = reportStoreGoodsDao.selectList(searchVo.getSearchVO());
        PageResData result=new PageResData();
        result.setTotalCount((int)((Page)reportStoreGoods).getTotal());
        result.setDataList(reportStoreGoods);
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
    public void dealWith() {
        String url="http://slcs.api.aiqin.com/store/getAllStoreCode";
        log.info("查询所有门店编码,请求url={}", url);
        HttpClient httpClient = HttpClient.get(url);
        Map<String, Object> result = httpClient.action().result(new TypeReference<Map<String, Object>>() {});
        log.info("调用slcs系统返回结果，result={}", JSON.toJSON(result));
        if(result==null){
            return;
        }
        List<String> jsonMap=new ArrayList<>();
        if (StringUtils.isNotBlank(result.get("code").toString()) && "0".equals(String.valueOf(result.get("code")))) {
            jsonMap = JSONArray.parseArray(JSON.toJSONString(result.get("data")), String.class);
            log.info("jsonMap:"+jsonMap);
        } else {
            log.info("查询所有门店编码为空");
            return;
        }
        //判断门店编码集合是否为空，不为空，遍历查询信息
        if(CollectionUtils.isNotEmpty(jsonMap)){

        }

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
