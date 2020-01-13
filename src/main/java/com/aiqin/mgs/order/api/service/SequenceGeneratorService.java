package com.aiqin.mgs.order.api.service;

/**
 * 模拟序列service
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/9 19:54
 */
public interface SequenceGeneratorService {

    /**
     * 生成订单号
     *
     * @param
     * @return java.lang.String
     * @author: Tao.Chen
     * @version: v1.0.0
     * @date 2020/1/9 19:54
     */
    String generateOrderCode();
}
