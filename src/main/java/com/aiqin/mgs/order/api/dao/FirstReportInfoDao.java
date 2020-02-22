package com.aiqin.mgs.order.api.dao;

import com.aiqin.mgs.order.api.domain.FirstReportInfo;
import java.util.List;

public interface FirstReportInfoDao {

    //根据报表时间获取报表表格数据
    List<FirstReportInfo> reportLis(String reportTime);
    //新增首单报表数据
    int insert(FirstReportInfo firstReportInfo);
    //删除首单报表数据
    int delete(String reportTime);
}
