package com.aiqin.mgs.order.api.component.enums;

import lombok.Getter;

/**
 * 退货请求类型枚举类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/14 16:28
 */
@Getter
public enum ErpOrderReturnRequestEnum {

    /***取消或终止进行中的退货状态，失败的情况下终止*/
    CANCEL,
    /***发起退货*/
    WAIT,
    /***退货完成，成功的情况下终止*/
    SUCCESS,
    ;

}
