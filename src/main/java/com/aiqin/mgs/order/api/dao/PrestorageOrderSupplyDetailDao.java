package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.PrestorageOrderSupplyDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2019/11/6 11:01
 * Description:
 */
public interface PrestorageOrderSupplyDetailDao {
    /**
     * 通过prestorageOrderSupplyDetailId 查询
     * @param prestorageOrderSupplyDetailId
     * @return
     */
    PrestorageOrderSupplyDetail selectprestorageorderDetailsById(@Param("prestorageOrderSupplyDetailId") String prestorageOrderSupplyDetailId);

    void updateById(PrestorageOrderSupplyDetail prestorageOrderSupplyDetail);

    /**
     * 通过预存主订单ID查询详情列表
     * @param prestorageOrderSupplyId
     * @return
     */
    List<PrestorageOrderSupplyDetail> selectprestorageorderDetailsListBySupplyId(@Param("prestorageOrderSupplyId") String prestorageOrderSupplyId);
}
