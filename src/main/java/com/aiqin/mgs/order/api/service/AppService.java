package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.PageResData;
import com.aiqin.mgs.order.api.base.PagesRequest;
import com.aiqin.mgs.order.api.domain.AppVersionInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/6/6 11:52
 * Description:
 */
public interface AppService {
    HttpResponse<AppVersionInfo> appActive(AppVersionInfo appVersionInfo);

    HttpResponse appAdd(AppVersionInfo appVersionInfo);

    HttpResponse<PageResData<AppVersionInfo>> appList(PagesRequest pagesRequest);

    HttpResponse appUpload(MultipartFile file);
}
