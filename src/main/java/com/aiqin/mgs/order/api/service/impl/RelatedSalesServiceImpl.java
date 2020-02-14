package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageRequestVO;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.dao.RelatedSalesDao;
import com.aiqin.mgs.order.api.domain.RelatedSales;
import com.aiqin.mgs.order.api.domain.request.RelatedSalesVo;
import com.aiqin.mgs.order.api.service.RelatedSalesService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * description: RelatedSalesServiceImpl
 * date: 2020/2/14 11:30
 * author: hantao
 * version: 1.0
 */
@Service
public class RelatedSalesServiceImpl implements RelatedSalesService {

    @Autowired
    private RelatedSalesDao relatedSalesDao;

    @Override
    public HttpResponse insert(RelatedSales entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        relatedSalesDao.insertSelective(entity);
        return HttpResponse.success();
    }

    @Override
    public PageResData<RelatedSales> getList(PageRequestVO<RelatedSalesVo> searchVo) {
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        List<RelatedSales> relatedSales = relatedSalesDao.selectList(searchVo.getSearchVO());
        return new PageResData(Integer.valueOf((int)((Page) relatedSales).getTotal()) , relatedSales);
    }
}
