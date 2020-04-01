package com.aiqin.mgs.order.api.base;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 序号	订单状态	前台订单状态	后台订单状态	说明	标准描述	订单是否显示	后续状态	申请取消、终止订单状态
 1	1 	待审核	待审核	客户申请退货后，等待审核	您的服务单已申请成功，待审核中		2	99，98
 2	2 	审核通过	审核通过	退单审核通过	您的服务单已审核		3	99，97
 3	3 	等待退货	退单同步中	退货单同步给订单、供应链系统	服务单同步过程中，请等待系统确认		4	99，97
 4	4 	等待退货	等待退货入库	等待客户把退货寄回来，然后入库	您的服务单等待寄回入库		11	99，97
 5	11 	等待退款	退货完成	退货入库完成	您的服务单已收货完成		12
 6	12 	退款完成	退款完成	退款完成	您的服务单已退款完成
 7	97 	已取消	退货异常终止	退货终止，长时间不寄回退货	您的服务单已终止
 8	98 	审核不通过	审核不通过	退单审核不通过	   您的服务单审核不通过
 9	99 	已取消	已取消	已取消退单	您的服务单已取消
 */
@Getter
public enum ReturnOrderStatus {

    PENDING_REVIEW(1, "待审核", "待审核", "客户申请退货后，等待审核", "您的服务单已申请成功，待审核中", "1", "2", "99，98"),
    EXAMINATION_PASSED(2, "审核通过", "审核通过", "退单审核通过", "您的服务单已审核", "1", "3", "99，97"),
    RETURN_ORDER_SYNCHRONIZATION(3, "等待退货", "退单同步中", "货单同步给订单、供应链系统", "服务单同步过程中，请等待系统确认", "1", "4", "99，97"),
    WAITING_FOR_RETURN_TO_INSPECTION(4, "等待退货", "等待退货验货", "等待客户把退货寄回来，然后验货", "您的服务单等待寄回入库", "1", "11", "99，97"),
    WAITING_FOR_RETURN_TO_THE_WAREHOUSE(5, "等待退货", "等待退货入库", "验货完成，然后入库", "您的服务单等待寄回入库", "1", "11", "99，97"),
    WAITING_FOR_RETURN_TO_THE_WAREHOUSE1(6, "等待审批", "运营中台挂账", "需要给客户返A品券", "", "", "", ""),
    RETURN_COMPLETED(11, "等待退货", "退货完成", "退货入库完成", "您的服务单已收货完成", "1", "12", null),
    REFUND_COMPLETED(12, "退款完成", "退款完成", "退款完成", "您的服务单已退款完成", "1", null, null),
    RETURN_ABNORMALLY_TERMINATED(97, "已取消", "退货异常终止", "退货终止，长时间不寄回退货", "您的服务单已终止", "1", null, null),
    AUDIT_NOT_PASSED(98, "审核不通过", "审核不通过", "退单审核不通过", "您的服务单审核不通过", "1", null, null),
    cancelled(99, "已取消", "已取消", "已取消退单", "您的服务单已取消", "1", null, null);


    private Integer statusCode;
    private String frontOrderStatus;
    private String backgroundOrderStatus;
    private String explain;
    private String standardDescription;
    private String beDisplay;
    private String followUpStatus;
    private String finalStatus;

    ReturnOrderStatus(Integer statusCode, String frontOrderStatus, String backgroundOrderStatus, String explain, String standardDescription, String beDisplay, String followUpStatus, String finalStatus) {
        this.statusCode = statusCode;
        this.frontOrderStatus = frontOrderStatus;
        this.backgroundOrderStatus = backgroundOrderStatus;
        this.explain = explain;
        this.standardDescription = standardDescription;
        this.beDisplay = beDisplay;
        this.followUpStatus = followUpStatus;
        this.finalStatus = finalStatus;
    }
    public static Map<Integer, ReturnOrderStatus> getAllStatus(){
        return Arrays.stream(values()).collect(Collectors.toMap(ReturnOrderStatus::getStatusCode, Function.identity(),(k1, k2)->k2));
    }
}


