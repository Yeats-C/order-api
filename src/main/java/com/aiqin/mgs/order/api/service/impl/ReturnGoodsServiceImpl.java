package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.mgs.order.api.base.BasePage;
import com.aiqin.mgs.order.api.base.PageUtil;
import com.aiqin.mgs.order.api.dao.OperationLogDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderDetailDao;
import com.aiqin.mgs.order.api.dao.returnorder.ReturnOrderInfoDao;
import com.aiqin.mgs.order.api.domain.OperationLog;
import com.aiqin.mgs.order.api.domain.OperationOrderLog;
import com.aiqin.mgs.order.api.domain.ReturnOrderDetail;
import com.aiqin.mgs.order.api.domain.request.returngoods.QueryReturnOrderManagementReqVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.QueryReturnOrderManagementRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderDetailRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoApplyInboundDetailRespVO;
import com.aiqin.mgs.order.api.domain.response.returngoods.ReturnOrderInfoItemRespVO;
import com.aiqin.mgs.order.api.service.ReturnGoodsService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
    @Resource
    private OperationLogDao operationLogDao;
    @Resource
    private ReturnOrderDetailDao returnOrderDetailDao;

    @Override
    public BasePage<QueryReturnOrderManagementRespVO> returnOrderManagement(QueryReturnOrderManagementReqVO reqVO) {
        PageHelper.startPage(reqVO.getPageNo(), reqVO.getPageSize());
        List<QueryReturnOrderManagementRespVO> list = returnOrderInfoMapper.selectReturnOrderManagementList(reqVO);
        return PageUtil.getPageList(reqVO.getPageNo(), list);
    }

    @Override
    public ReturnOrderDetailRespVO returnOrderDetail(String code) {
        // 查询退货单的详情
        ReturnOrderDetailRespVO respVO = returnOrderInfoMapper.selectReturnOrderInfo(code);
        // 查询退货单商品信息
        List<ReturnOrderInfoItemRespVO> itemList = returnOrderDetailDao.selectReturnOrderList(code);
        respVO.setItemList(itemList);

        // 查询退货单日志信息
        List<OperationOrderLog> logList = operationLogDao.searchOrderLog(code, 3);
        respVO.setLogList(logList);
        return respVO;
    }

    @Override
    public List<ReturnOrderInfoApplyInboundDetailRespVO> inboundInfo(String code) {
        return returnOrderInfoMapper.selectInbound(code);
    }
}
