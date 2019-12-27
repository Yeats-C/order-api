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

    MessageId OVER_LIMIT = MessageId.create(Project.PRODUCT_API,20,"订购商品不能超过999");
    MessageId STORE_SHORT = MessageId.create(Project.PRODUCT_API,21,"库存不足");
    MessageId STOCK_SHORT1 = MessageId.create(Project.PRODUCT_API,22,"库存紧张");
    MessageId GETTOTAL = MessageId.create(Project.PRODUCT_API,23,"查询总数量异常");
    MessageId INSERT_SOURCE_EXCEPTION = MessageId.create(Project.OMS_API, 30, "添加用户服务项目资产信息异常");
    MessageId INSERT_SOURCE_LIST_EXCEPTION = MessageId.create(Project.OMS_API, 31, "添加用户服务项目资产信息异常");
    MessageId UPDATE_SOURCE_EXCEPTION = MessageId.create(Project.OMS_API, 32, "更新用户服务项目资产信息异常");
    MessageId SELECT_SOURCE_BY_ID_EXCEPTION = MessageId.create(Project.OMS_API, 33, "根据资产id查询用户服务项目资产信息");

    MessageId SELECT_SOURCE_BY_PHONE_EXCEPTION = MessageId.create(Project.OMS_API, 34, "根据用户手机号查询用户服务项目资产信息");

    MessageId INSERT_SOURCE_FAIL = MessageId.create(Project.OMS_API, 35, "添加用户服务项目资产失败");
    MessageId INSERT_SOURCE_LIST_FAIL = MessageId.create(Project.OMS_API, 36, "批量添加用户服务项目资产失败");
    MessageId UPDATE_SOURCE_FAIL = MessageId.create(Project.OMS_API, 37, "更新用户服务项目资产信息失败");
    MessageId SERVICE_PROJECT_TRANSFORM_STATISTICS_EXCEPTION = MessageId.create(Project.OMS_API, 39, "统计服务项目转化情况异常");
    MessageId SERVICE_PROJECT_TYPE_TRANSFORM_STATISTICS_EXCEPTION = MessageId.create(Project.OMS_API, 40, "统计服务项目转化情况异常");
    MessageId SELECT_REDUCE_DETAIL_EXCEPTION = MessageId.create(Project.OMS_API, 41, "通过门店编号、名称、用户手机号和时间查询订单的信息异常");
    MessageId SELECT_ORDER_INFO_EXCEPTION = MessageId.create(Project.OMS_API, 42, "根据订单id和订单类型查询订单信息异常");
    MessageId INSERT_REDUCE_DETAIL_EXCEPTION = MessageId.create(Project.OMS_API, 43, "添加服务项目扣减信息异常");

    MessageId INSERT_REDUCE_DETAIL_FAIL = MessageId.create(Project.OMS_API, 44, "添加服务项目扣减信息失败");
    MessageId SELECT_STORE_PROJECT_TREE_EXCEPTION = MessageId.create(Project.OMS_API, 45, "获取门店服务商品树异常");

    MessageId INSERT_REDUCE_DETAIL_LIST_EXCEPTION = MessageId.create(Project.OMS_API, 50, "批量添加服务项目扣减信息异常");
    MessageId INSERT_ASSET_AND_REDUCE_DETAIL_LIST_EXCEPTION = MessageId.create(Project.OMS_API, 51, "批量添加服务项目扣减信息和服务商品资产信息异常");
    MessageId SELECT_ASSET_BY_ORDER_CODE_EXCEPTION = MessageId.create(Project.OMS_API, 52, "根据订单编号查询服务商品资产信息异常");

    MessageId UPDATE_SOURCE_LIST_FAIL = MessageId.create(Project.OMS_API, 53, "批量更新用户服务项目资产失败");

    MessageId DELETE_SOURCE_LIST_FAIL = MessageId.create(Project.OMS_API, 55, "批量删除用户服务项目资产失败");
    MessageId SELECT_ASSET_BY_ASSET_ID_EXCEPTION = MessageId.create(Project.OMS_API, 56, "根据资产id查询用户服务项目消费记录list异常");
    MessageId INSERT_REDUCE_DETAIL_UPDATE_ASSET_EXCEPTION = MessageId.create(Project.OMS_API, 57, "添加服务项目扣减信息和更新资产信息异常");

    MessageId SELECT_ASSET_BY_ORDER_Id_FAIL = MessageId.create(Project.OMS_API, 58, "根据订单id查询服务商品资产信息失败，未查询到对应订单信息");
    MessageId SELECT_PROJECT_TREE_STORE_EXCEPTION = MessageId.create(Project.OMS_API, 59, "获取某个门店服务商品树异常");

    MessageId SERVICE_PROJECT_PROJECT_TRANSFORM_STATISTICS_EXCEPTION = MessageId.create(Project.OMS_API, 60, "统计各服务项目的转化情况异常");
    MessageId SERVICE_PROJECT_TOP_TRANSFORM_STATISTICS_EXCEPTION = MessageId.create(Project.OMS_API, 61, "统计各服务项目销量top10异常");

    MessageId CASHIER_SHIFT_SCHEDULE = MessageId.create(Project.OMS_API, 61, "交接班退出异常");

    String CART = "购物车";
    String ORDER = "订单";

    MessageId OPT_ERROR = MessageId.create(Project.ZERO, 28, "操作失败");

    MessageId PAY_ERROR = MessageId.create(Project.PAYMENT_API, 1, "回调失败");

    
    /** 黄祉壹   2018-11-05 结束     */
}
