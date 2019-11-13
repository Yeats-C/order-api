package com.aiqin.mgs.order.api.base.exception;

/**
 * 业务异常类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/11/12 15:41
 */
public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}