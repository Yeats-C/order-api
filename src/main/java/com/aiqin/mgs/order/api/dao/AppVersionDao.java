package com.aiqin.mgs.order.api.dao;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.AppVersionInfo;

/**
 * @author jinghaibo
 * Date: 2020/6/8 15:26
 * Description:
 */
public interface AppVersionDao {
    int add(AppVersionInfo appVersionInfo);
}
