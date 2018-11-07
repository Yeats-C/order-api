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
}
