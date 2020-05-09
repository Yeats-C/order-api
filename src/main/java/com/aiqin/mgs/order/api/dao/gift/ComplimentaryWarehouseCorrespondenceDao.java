package com.aiqin.mgs.order.api.dao.gift;

import com.aiqin.mgs.order.api.domain.po.gift.ComplimentaryWarehouseCorrespondence;

import java.util.List;

public interface ComplimentaryWarehouseCorrespondenceDao {

    /**
     * 保存兑换赠品与仓库对应关系List
     * @param complimentaryWarehouseCorrespondenceList
     * @return
     */
    Integer insertList(List<ComplimentaryWarehouseCorrespondence> complimentaryWarehouseCorrespondenceList);
}








