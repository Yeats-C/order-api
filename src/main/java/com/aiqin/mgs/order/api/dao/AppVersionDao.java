package com.aiqin.mgs.order.api.dao;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.AppVersionInfo;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/6/8 15:26
 * Description:
 */
public interface AppVersionDao {
    int add(AppVersionInfo appVersionInfo);

    /**
     * 将所有同类型的状态修改为结束
     * @param appVersionInfo
     * @return
     */
    int updateStateAll(AppVersionInfo appVersionInfo);

    List<AppVersionInfo> selectAppList(PagesRequest pagesRequest);

    AppVersionInfo selectAppActive(AppVersionInfo appVersionInfo);

    int update(AppVersionInfo appVersionInfo);

    int selectSize(PagesRequest pagesRequest);

    void delete(AppVersionInfo appVersionInfo);
}
