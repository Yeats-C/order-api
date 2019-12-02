package com.aiqin.mgs.order.api.base;

import com.aiqin.ground.util.protocol.MessageId;
import com.aiqin.ground.util.protocol.Project;

public interface ResultCode {
    MessageId SYSTEM_ERROR = MessageId.create(Project.STORE_API, 500, "系统异常");
    MessageId REQUIRED_PARAMETER = MessageId.create(Project.STORE_API, 1, "参数缺失");
    MessageId NOT_HAVE_PARAM = MessageId.create(Project.STORE_API, 2, "未查询到参数信息");
    MessageId UNIQUE_CITY_CODE = MessageId.create(Project.STORE_API, 3, "已存在城市编码");
    MessageId NOT_HAVE_CITY = MessageId.create(Project.STORE_API, 4, "未查询到对应城市");
    MessageId NOT_HAVE_COMP = MessageId.create(Project.STORE_API, 5, "未查询到竞品门店信息");
    MessageId NOT_HAVE_OPPORTUNITY = MessageId.create(Project.STORE_API, 6, "未查询到机会点信息");
    MessageId NOT_LOGIN_INFO = MessageId.create(Project.STORE_API, 7, "登录人账号或密码错误");
    MessageId SOSO_CONNECT_ERROR = MessageId.create(Project.STORE_API, 8, "调用腾讯地图异常");
    MessageId NO_HAVE_STORE_ERROR = MessageId.create(Project.STORE_API, 9, "未查询到对应门店信息");
    MessageId CHANGE_STORE_ERROR=MessageId.create(Project.ORDER_API, -1, "修改库存失败");
    MessageId STATUS_CHANGE_ERROR=MessageId.create(Project.ORDER_API, -1, "订单状态修改失败");

    /** 黄祉壹   2018-11-05 开始     */
    
    MessageId ADD_EXCEPTION = MessageId.create(Project.ORDER_API, 10, "新增异常");
    MessageId UPDATE_EXCEPTION = MessageId.create(Project.ORDER_API, 11, "更新异常");
    MessageId DELETE_EXCEPTION = MessageId.create(Project.ORDER_API, 12, "删除异常");
    MessageId SELECT_EXCEPTION = MessageId.create(Project.ORDER_API, 13, "查询异常");
    MessageId PARAMETER_EXCEPTION = MessageId.create(Project.ORDER_API, 14, "参数异常");
    
    MessageId NET_EXCEPTION = MessageId.create(Project.ORDER_API, 15, "网络异常");
    
    String CART = "购物车";
    String ORDER = "订单";
    
    
    
    /** 黄祉壹   2018-11-05 结束     */
}
