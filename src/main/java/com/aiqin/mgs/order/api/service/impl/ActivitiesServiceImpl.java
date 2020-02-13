package com.aiqin.mgs.order.api.service.impl;

import com.aiqin.ground.util.protocol.http.HttpResponse;
import com.aiqin.mgs.order.api.base.ResultCode;
import com.aiqin.mgs.order.api.dao.ActivitiesDao;
import com.aiqin.mgs.order.api.domain.Activities;
import com.aiqin.mgs.order.api.service.ActivitesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dell
 */
@Service
public class ActivitiesServiceImpl implements ActivitesService {


    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiesServiceImpl.class);

    @Resource
    private ActivitiesDao activitiesDao;

    @Override
    public HttpResponse<List<Activities>> activityList(Activities activities) {
        HttpResponse response = HttpResponse.success();
        response.setData(activitiesDao.activityList(activities));
        return response;
    }

    @Override
    public HttpResponse<Activities> getActivityInformation(String activityId) {
        HttpResponse response = HttpResponse.success();
        if(null==activityId){
            return HttpResponse.failure(ResultCode.REQUIRED_PARAMETER);
        }
        Activities activity=new Activities();
        activity.setActivityId(activityId);
        Activities activityData=activitiesDao.getActivityInformation(activity);
        response.setData(activityData);
        return response;
    }
}