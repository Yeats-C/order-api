package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.CashierReqVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import javax.validation.Valid;

public interface CashierShiftScheduleDao {
    Integer cashierQuery(CashierReqVo cashierReqVo);

    //  获取收银员上次退出时间
    @Select("select end_time from cashier_shift_schedule where cashier_id = #{cashierId} and store_id = #{distributorId} order by end_time desc limit 1")
    String selectTimeByCashierId(@Valid @Param("cashierId") String cashierId, @Param(value = "distributorId") String distributorId);

}
