package com.aiqin.mgs.order.api.util;

import com.aiqin.mgs.order.api.base.exception.BusinessException;
import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对象复制异常类
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/28 14:36
 */
public class CopyBeanUtil {

    private static final Logger logger = LoggerFactory.getLogger(CopyBeanUtil.class);

    /**
     * 复制对象
     *
     * @param dest 空对象
     * @param orig 被复制对象
     */
    public static void copySameBean(Object dest, Object orig) {
        try {
            PropertyUtils.copyProperties(dest, orig);
        } catch (Exception e) {
            logger.error("对象复制异常：{}", e);
            throw new BusinessException("对象复制异常");
        }
    }

}
