package com.aiqin.mgs.order.api.dao.order;

import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivitySales;
import com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo;
import com.aiqin.mgs.order.api.domain.request.order.ErpOrderQueryRequest;
import com.aiqin.mgs.order.api.domain.request.returnorder.ReturnOrderFranchisee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单信息
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 14:10
 */
public interface ErpOrderInfoDao {

    /**
     * 根据字段精确查询符合的数据列表
     *
     * @param po
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    List<ErpOrderInfo> select(ErpOrderInfo po);

    /**
     * 根据主键更新非空字段（部分字段除外）
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:03
     */
    Integer updateByPrimaryKeySelective(ErpOrderInfo po);

    /**
     * 插入数据
     *
     * @param po
     * @return java.lang.Integer
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/9 14:04
     */
    Integer insert(ErpOrderInfo po);

    /**
     * 查询订单列表
     *
     * @param erpOrderQueryRequest
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 20:05
     */
    List<ErpOrderInfo> findOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * 查询子订单列表
     *
     * @param erpOrderQueryRequest
     * @return java.util.List<com.aiqin.mgs.order.api.domain.po.order.ErpOrderInfo>
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/10 20:05
     */
    List<ErpOrderInfo> findSecondaryOrderList(ErpOrderQueryRequest erpOrderQueryRequest);

    /**
     * 获取当前天最大的订单号
     *
     * @param currentDay 当前天字符串yyyyMMdd
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2019/12/24 18:52
     */
    String getMaxOrderCodeByCurrentDay(@Param("currentDay") String currentDay);

    /**
     * 查询同步时失败的订单
     *
     * @param orderSuccess
     */
    List<ErpOrderInfo> selectByOrderSucess(Integer orderSuccess);

    /**
     * 修改订单同步状态
     * 
     * @param orderSucess
     * @return
     */
    Integer updateOrderSuccess(@Param("orderSucess") Integer orderSucess, @Param("orderStoreCode") String orderStoreCode);

    /**
     * 活动相关订单销售额及活动订单数(当订单中的商品命中了这个促销活动时，这个订单纳入统计，统计主订单。
     * @return
     * @param activity
     */
    ActivitySales getActivitySales(Activity activity);

    /**
     * 活动补货门店数
     * @return
     * @param activity
     */
    Integer getStoreNum(Activity activity);

    /**
     * 修改订单中的冲减单状态
     * @param orderCode
     */
    int updateScourSheetStatus(String orderCode);

    /**
     * 获取加盟商编码
     * @param orderStoreCode
     */
     ReturnOrderFranchisee selectFranchisee(String orderStoreCode);

    /**
     * 退货后修改订单状态
     * @param orderStoreCode
     */
    int updateOrderStatus(String orderStoreCode);

    /**
     * 修改订单退货流程节点
     * @param orderStoreCode
     * @return
     */
    int updateOrderReturnProcess(String orderStoreCode);
}
