package com.aiqin.mgs.order.api.domain.response;

import lombok.Data;

import java.util.List;

/**
 * @author jinghaibo
 * Date: 2020/8/6 20:37
 * Description:
 */
@Data
public class OrderSumResponse extends OrderSumCountResponse {
    List<OrderbyReceiptSumResponse> orderbyReceiptSumResponses;

}
