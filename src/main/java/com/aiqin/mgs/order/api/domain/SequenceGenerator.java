package com.aiqin.mgs.order.api.domain;

import lombok.Data;

/**
 * 模拟序列表
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2020/1/11 14:28
 */
@Data
public class SequenceGenerator {

    /***id*/
    private String id;
    /***序列名称*/
    private String sequenceName;
    /***序列描述*/
    private String sequenceDesc;
    /***序列当前值*/
    private Long sequenceVal;
    /***当前天yyyyMMdd*/
    private String currentDay;
    /***序列步长*/
    private Integer sequenceStep;

}
