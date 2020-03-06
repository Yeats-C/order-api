package com.aiqin.mgs.order.api.base;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description:
 序号	订单状态	前台订单状态	后台订单状态	说明	标准描述	订单是否显示	后续状态	申请取消、终止订单状态
 1	1 	等待客户支付	待支付	客户下单后，等待支付	您提交了订单，请尽快支付		2	99
 2	2 	付款成功	已支付	收款已确认，等待拆分	您的订单已支付，请等待系统确认		3	102
 3	3 	商品出库	订单拆分中	订单拆分过程中	订单拆分过程中，请等待系统确认		4/5/6
 4	4 	商品出库	订单同步中	订单同步采购单、上级销售单、上级采购单	订单同步过程中，请等待系统确认		5/6
 5	5 	商品出库	等待配货	直送订单等待完成	您的订单等待拣货		11	97
 6	6 	商品出库	等待拣货	配送订单拆分完成，等待拣货	您的订单等待拣货		7/11	106
 7	7 	商品出库	正在拣货	打印完拣货单，正在拣货	您的订单已经开始拣货		8	107
 8	8 	商品出库	扫描完成	扫描完成，确认订单商品	您的订单已经开始拣货		11	108
 9	11 	等待收货	已全部发货	已经全部发货	您的订单已全部发货		12
 10	12 	交易完成	交易全部完成	交易全部完成，客户收货	您的订单已完成配送，欢迎您再次光临！
 11	96 	交易取消	拒收终止交易	“已全部发货”后客户拒收	您的订单已终止
 12	97 	交易取消	缺货终止交易	“等待配货”时，因为缺货终止交易	您的订单已终止
 13	98 	交易取消	交易异常终止	交易异常终止	您的订单已终止
 14	99 	交易取消	已取消	已取消订单	您的订单已取消
 15	102 	申请取消交易	申请终止交易（已支付）	“已支付”时，客户申请终止交易	您已申请终止交易，请等待系统确认		98
 18	106 	申请取消交易	申请终止交易（等待拣货）	“等待拣货”时，客户申请终止交易	您已申请终止交易，请等待系统确认		98
 19	107 	申请取消交易	申请终止交易（正在拣货）	“正在拣货”时，客户申请终止交易	您已申请终止交易，请等待系统确认		98
 20	108 	申请取消交易	申请终止交易（扫描完成）	“扫描完成”时，客户申请终止交易	您已申请终止交易，请等待系统确认		98
 21	92 		待支付订单确认支付	“待支付”订单人工确认变成“已支付”	您的订单已支付，请等待系统确认	否	2
 22	93 		已取消订单确认支付	“已取消”订单人工确认变成“已支付”	您的订单已支付，请等待系统确认	否	2
 23	94 		申请终止交易不通过		您的订单申请终止交易未通过，继续发货	否	原状态
 24	95 		收货延期		您的订单收货延期	否	原状态
 */
@Getter
public enum OrderStatus {

    TO_BE_PAID(1, "等待客户支付", "待支付", "客户下单后，等待支付", "您提交了订单，请尽快支付", "1", "2", "99"),
    PAID(2,"付款成功","已支付","收款已确认，等待拆分","您的订单已支付，请等待系统确认","1","3","102"),
    ORDER_SPLIT(3,"商品出库","订单拆分中","订单拆分过程中","订单拆分过程中，请等待系统确认","1","4/5/6",null),
    ORDER_SYNCHRONIZATION(4,"商品出库","订单同步中","订单同步采购单、上级销售单、上级采购单","订单同步过程中，请等待系统确认","1","5/6",null),
    WAITING_FOR_DISTRIBUTION(5,"商品出库","等待配货","直送订单等待完成","您的订单等待拣货","1","11","97"),
    WAITING_FOR_PICKING(6,"商品出库","等待拣货","配送订单拆分完成，等待拣货","您的订单等待拣货","1","7/11","106"),
    PICKING(7,"商品出库","正在拣货","客打印完拣货单，正在拣货","您的订单已经开始拣货","1","8","107"),
    SCAN_COMPLETED(8,"商品出库","扫描完成","扫描完成，确认订单商品","您的订单已经开始拣货","1","11","108"),
    ALL_SHIPPED(11,"等待收货","已全部发货","已经全部发货","您的订单已全部发货","1","12",null),
    THE_TRANSACTION_IS_ALL_COMPLETED(12,"已支付物流费用","已支付物流费用","已支付物流费用","","1",null,null),
    CODE_STATUS_13(13,"已签收","已签收","已签收","您的订单已签收，欢迎您再次光临！","1",null,null),

    CODE_STATUS_92(92,"待支付人工确认支付","待支付人工确认支付","待支付人工确认支付","待支付人工确认支付","1",null,null),
    CODE_STATUS_94(92,"终止交易失败","终止交易失败","终止交易失败","终止交易失败","1",null,null),
    REJECT_TO_TERMINATE_THE_TRANSACTION(96,"交易取消","拒收终止交易","“已全部发货”后客户拒收","您的订单已终止","1",null,null),
    OUT_OF_STOCK_TO_TERMINATE_THE_TRANSACTION(97,"交易取消","缺货终止交易","“等待配货”时，因为缺货终止交易","您的订单已终止","1",null,null),
    TRANSACTION_TERMINATED_ABNORMALLY(98,"交易取消","交易异常终止","交易异常终止","您的订单已终止","1",null,null),
    CANCELLED(99,"交易取消","已取消","已取消订单","您的订单已取消","1",null,null),
    APPLY_TO_TERMINATE_THE_TRANSACTION_PAID(102,"申请取消交易","申请终止交易（已支付）","“已支付”时，客户申请终止交易","您已申请终止交易，请等待系统确认","1","98","94"),
    APPLY_TO_TERMINATE_THE_TRANSACTION_WAITING_FOR_PICKING(106,"申请取消交易","申请终止交易（等待拣货）","“等待拣货”时，客户申请终止交易","您已申请终止交易，请等待系统确认","1","98","94"),
    APPLY_TO_TERMINATE_THE_TRANSACTION_PICKING(107,"申请取消交易","申请终止交易（正在拣货）","“正在拣货”时，客户申请终止交易","您已申请终止交易，请等待系统确认","1","98","94"),
    APPLY_TO_TERMINATE_THE_TRANSACTION_SCAN_COMPLETED(108,"申请取消交易","申请终止交易（扫描完成）","“扫描完成”时，客户申请终止交易","您已申请终止交易，请等待系统确认","1","98","94"),
    PENDING_PAYMENT_CONFIRMATION_PAYMENT(92,null,"“待支付”订单人工确认变成“已支付”",null,"您的订单已支付，请等待系统确认","0","2",null),
    CANCELLATION_OF_ORDER_CONFIRMATION_PAYMENT(93,null,"“已取消”订单人工确认变成“已支付”",null,"您的订单已支付，请等待系统确认","0","2",null),
    APPLY_TO_TERMINATE_THE_TRANSACTION_FAIL(94,null,"申请终止交易不通过",null,"您的订单申请终止交易未通过，继续发货","0",null,null),
    RECEIVING_EXTENSION(95,null,"收货延期",null,"您的订单收货延期","0",null,null),
    WAITING_FOR_PICKING_FAILED(-6,"等待生成出库单","等待生成出库单","配送订单拆分完成,生成出库单失败！","生成出库单失败","1",null,null);

    private Integer statusCode;
    private String frontOrderStatus;
    private String backgroundOrderStatus;
    private String explain;
    private String standardDescription;
    private String beDisplay;
    private String followUpStatus;
    private String finalStatus;

    OrderStatus(Integer statusCode, String frontOrderStatus, String backgroundOrderStatus, String explain, String standardDescription, String beDisplay, String followUpStatus, String finalStatus) {
        this.statusCode = statusCode;
        this.frontOrderStatus = frontOrderStatus;
        this.backgroundOrderStatus = backgroundOrderStatus;
        this.explain = explain;
        this.standardDescription = standardDescription;
        this.beDisplay = beDisplay;
        this.followUpStatus = followUpStatus;
        this.finalStatus = finalStatus;
    }
    public static Map<Integer, OrderStatus> getAllStatus(){
        return Arrays.stream(values()).collect(Collectors.toMap(OrderStatus::getStatusCode,Function.identity(),(k1,k2)->k2));
    }
}


