package com.aiqin.mgs.order.api.util;

import com.aiqin.mgs.order.api.component.enums.activity.ActivityRuleUnitEnum;
import com.aiqin.mgs.order.api.component.enums.activity.ActivityTypeEnum;
import com.aiqin.mgs.order.api.domain.Activity;
import com.aiqin.mgs.order.api.domain.ActivityGift;
import com.aiqin.mgs.order.api.domain.ActivityRule;
import com.aiqin.mgs.order.api.domain.request.activity.ActivityRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 用于测试使用，用完之后删除
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/2/22 10:53
 */
public class TestUtil {

    public static List<Activity> getTestActivityList() {
        List<Activity> activityList = new ArrayList<>();
        Activity activity1 = new Activity();
        activity1.setActivityId("999999");
        activity1.setActivityName("临时测试满减活动假数据");
        activity1.setActivityType(ActivityTypeEnum.TYPE_1.getCode());
        activityList.add(activity1);

        Activity activity2 = new Activity();
        activity2.setActivityId("888888");
        activity2.setActivityName("临时测试满赠活动假数据");
        activity2.setActivityType(ActivityTypeEnum.TYPE_2.getCode());
        activityList.add(activity2);

        return activityList;
    }


    public static ActivityRequest getTestActivity(String activityId) {
        ActivityRequest activityRequest = new ActivityRequest();
        if ("999999".equals(activityId)) {
            //满减假数据
            Activity activity1 = new Activity();
            activity1.setActivityId("999999");
            activity1.setActivityName("临时测试满减活动假数据");
            activity1.setActivityType(ActivityTypeEnum.TYPE_1.getCode());
            activityRequest.setActivity(activity1);

            List<ActivityRule> activityRuleList = new ArrayList<>();
            ActivityRule rule1 = new ActivityRule();
            rule1.setActivityType(ActivityTypeEnum.TYPE_1.getCode());
            rule1.setRuleUnit(ActivityRuleUnitEnum.BY_NUM.getCode());
            rule1.setActivityId(activity1.getActivityId());
            rule1.setMeetingConditions(BigDecimal.ONE);
            rule1.setPreferentialAmount(new BigDecimal(5));
            activityRuleList.add(rule1);

            ActivityRule rule2 = new ActivityRule();
            rule2.setActivityType(ActivityTypeEnum.TYPE_1.getCode());
            rule2.setRuleUnit(ActivityRuleUnitEnum.BY_NUM.getCode());
            rule2.setActivityId(activity1.getActivityId());
            rule2.setMeetingConditions(BigDecimal.TEN);
            rule2.setPreferentialAmount(new BigDecimal(60));
            activityRuleList.add(rule2);
            activityRequest.setActivityRules(activityRuleList);


        } else if ("888888".equals(activityId)) {
            //满赠假数据

            Activity activity2 = new Activity();
            activity2.setActivityId("888888");
            activity2.setActivityName("临时测试满赠活动假数据");
            activity2.setActivityType(ActivityTypeEnum.TYPE_2.getCode());
            activityRequest.setActivity(activity2);

            List<ActivityRule> activityRuleList = new ArrayList<>();
            ActivityRule rule1 = new ActivityRule();
            rule1.setActivityType(ActivityTypeEnum.TYPE_2.getCode());
            rule1.setRuleUnit(ActivityRuleUnitEnum.BY_NUM.getCode());
            rule1.setActivityId(activity2.getActivityId());
            rule1.setMeetingConditions(BigDecimal.ONE);
            List<ActivityGift> giftList1 = new ArrayList<>();
            ActivityGift gift1 = new ActivityGift();
            gift1.setSkuCode("991191");
            gift1.setNumbers(1);
            giftList1.add(gift1);
            rule1.setGiftList(giftList1);

            activityRuleList.add(rule1);

            ActivityRule rule2 = new ActivityRule();
            rule2.setActivityType(ActivityTypeEnum.TYPE_2.getCode());
            rule2.setRuleUnit(ActivityRuleUnitEnum.BY_NUM.getCode());
            rule2.setActivityId(activity2.getActivityId());
            rule2.setMeetingConditions(BigDecimal.TEN);

            List<ActivityGift> giftList2 = new ArrayList<>();
            ActivityGift gift2 = new ActivityGift();
            gift2.setSkuCode("991191");
            gift2.setNumbers(10);
            giftList2.add(gift2);
            rule2.setGiftList(giftList2);

            activityRuleList.add(rule2);
            activityRequest.setActivityRules(activityRuleList);

        } else {

        }
        return activityRequest;
    }

}
