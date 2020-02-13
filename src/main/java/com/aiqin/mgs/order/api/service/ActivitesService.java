package com.aiqin.mgs.order.api.service;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.domain.Activities;

import java.util.List;

public interface ActivitesService {

    /**
     * 促销活动管理--活动列表
     * @param activities
     * @return
     */
    HttpResponse<List<Activities>> activityList(Activities activities);
}
