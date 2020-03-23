package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.BasePage;
import com.aiqin.mgs.order.api.base.PageUtil;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.base.exception.BizException;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.request.returngoods.QueryReturnOrderManagementReqVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.QueryReturnOrderManagementRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderDetailRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoApplyInboundDetailRespVO;
import com.aiqin.mgs.order.api.service.ReturnGoodsService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @Description
 * @Author xieq
 * @Date 2020/2/29
 */
@Service
public class ReturnGoodsServiceImpl implements ReturnGoodsService {

    @Autowired
    private ReturnOrderInfoDao returnOrderInfoMapper;

    @Override
    public BasePage<QueryReturnOrderManagementRespVO> returnOrderManagement(QueryReturnOrderManagementReqVO reqVO) {
        PageHelper.startPage(reqVO.getPageNo(), reqVO.getPageSize());
        List<QueryReturnOrderManagementRespVO> list = returnOrderInfoMapper.selectReturnOrderManagementList(reqVO);
        return PageUtil.getPageList(reqVO.getPageNo(), list);
    }

    @Override
    public ReturnOrderDetailRespVO returnOrderDetail(String code) {
        ReturnOrderDetailRespVO respVO =  returnOrderInfoMapper.selectReturnOrderDetail(code);
        if(Objects.isNull(respVO)){
            throw new BizException(ResultCode.GET_RETURN_GOODS_DETAIL_FAILED);
        }
        respVO.setDetailList(inboundInfo(code));
        return respVO;
    }

    @Override
    public List<ReturnOrderInfoApplyInboundDetailRespVO> inboundInfo(String code) {
        return returnOrderInfoMapper.selectInbound(code);
    }
}
