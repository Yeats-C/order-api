package com.aiqin.mgs.order.api.service.impl;

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
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
        if(StringUtils.isNotBlank(searchVo.getStoreCode())&&StringUtils.isNotBlank(searchVo.getBrandId())&&StringUtils.isNotBlank(searchVo.getCountTime())){
            return reportStoreGoodsDetailDao.selectList(searchVo);
        }
        return null;
    }
}
