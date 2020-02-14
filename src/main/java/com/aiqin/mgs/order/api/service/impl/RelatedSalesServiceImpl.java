package com.aiqin.mgs.order.api.service.impl;

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
    public Boolean insert(RelatedSales entity) {
        //判断此销售分类是否已存在记录，如果存在做修改，不存在做新增操作
        RelatedSales relatedSales = relatedSalesDao.selectBySalseCategoryId(entity.getSalseCategoryId());
        if(relatedSales!=null){
            entity.setId(relatedSales.getId());
            return relatedSalesDao.updateByPrimaryKeySelective(entity)>0;
        }
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        return relatedSalesDao.insertSelective(entity)>0;
    }

    @Override
    public Boolean update(RelatedSales entity) {
        entity.setUpdateTime(new Date());
        return relatedSalesDao.updateByPrimaryKeySelective(entity)>0;
    }

    @Override
    public Boolean updateStatus(Long id, Integer status) {
        RelatedSales entity=new RelatedSales();
        entity.setId(id);
        entity.setStatus(status);
        return relatedSalesDao.updateStatus(entity)>0;
    }

    @Override
    public PageResData<RelatedSales> getList(PageRequestVO<RelatedSalesVo> searchVo) {
        PageHelper.startPage(searchVo.getPageNo(),searchVo.getPageSize());
        List<RelatedSales> relatedSales = relatedSalesDao.selectList(searchVo.getSearchVO());
        return new PageResData(Integer.valueOf((int)((Page) relatedSales).getTotal()) , relatedSales);
    }

    @Override
    public RelatedSales selectBySalseCategoryId(String salseCategoryId) {
        return relatedSalesDao.selectBySalseCategoryId(salseCategoryId);
    }
}
