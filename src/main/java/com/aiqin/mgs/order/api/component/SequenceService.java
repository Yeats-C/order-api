package com.aiqin.mgs.order.api.component;

import org.apache.http.client.utils.DateUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.util.Date;

/**
 * 各类code 生成
 *
 * @reviewer
 * @see
 */
@Component
public class SequenceService {

    private static final String FMT_PATTERN_DAY = "yyyyMMdd";
    private static final int EXPIRED_TIME_DAY = 25 * 60 * 60;
    private static final String FMT_PATTERN_MONTH = "yyyyMM";
    private static final int EXPIRED_TIME_MONTH = 24 * 60 * 60 * 32;

    @Resource
    private SequenceRedisDao sequenceRedisDao;

    /**
     * 优惠额类型
     *
     * @return
     */
    public String generateDiscountTypeCode() {
        Integer sequence = sequenceRedisDao.nextSequence(SequenceType.PROMOTION);
        return formatNumber(sequence, 3);
    }
    /**
     * 订货退货原因编号
     */
    public String generateReturnReasonCode() {
        Integer sequence = sequenceRedisDao.nextSequence(SequenceType.RETURN_REASON);
        return formatNumber(sequence, 3);
    }

    /**
     * 物流减免序号
     */
    public String generateLogisticsReduction() {
        Integer sequence = sequenceRedisDao.nextSequence(SequenceType.LOGISTICS_REDUCTION);
        return formatNumber(sequence, 3);
    }

    /**
     * 生成优惠额编号
     * @return
     */
    public String generateDiscountAmountCode(String companyCode) {
        SequenceType type = SequenceType.DISCOUNT_AMOUNT;
        String dateStr = DateUtils.formatDate(new Date(), FMT_PATTERN_MONTH);
        Integer sequence = sequenceRedisDao.nextSequence(type, companyCode, dateStr, EXPIRED_TIME_MONTH);
        return String.format("%s%s%s", type.getPrefix(), dateStr, formatNumber(sequence,5));
    }

    /**
     * 生成活动编号
     * @return
     */
    public String generatePromotionCode() {
        String dateStr = DateUtils.formatDate(new Date(), FMT_PATTERN_MONTH);
        Integer sequence = sequenceRedisDao.nextSequence(SequenceType.PROMOTION, dateStr, EXPIRED_TIME_MONTH);
        return String.format("%s%s", dateStr, formatNumber(sequence, 5));
    }


    /**
     * 生成订单编号
     * @return
     */
    public String generateOrderCode(String companyCode, Integer orderType) {
        String dateStr = DateUtils.formatDate(new Date(), FMT_PATTERN_DAY);
        Integer sequence = sequenceRedisDao.nextSequence(SequenceType.ORDER, dateStr, EXPIRED_TIME_DAY);
        return String.format("%s%s%s%s", dateStr, companyCode, formatNumber(orderType, 2), formatNumber(sequence, 5));
    }

    /**
     * 生成
     * @return
     */
    public String getOrderCode() {
        return null;
    }

    private String formatNumber(Number source, int len) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append("0");
        }
        return new DecimalFormat(builder.toString()).format(source);
    }

    /**
     * 生成退货单编号
     *
     * @return
     */
    public String generateOrderAfterSaleCode(String companyCode, Integer afterSaleType) {
        String dateStr = DateUtils.formatDate(new Date(), FMT_PATTERN_DAY);
        Integer sequence = sequenceRedisDao.nextSequence(SequenceType.ORDER_AFTER_SALE, dateStr, EXPIRED_TIME_MONTH);
//        Integer sequence = 1;
        String type = "";
        //0-售后退货 1-缺货取消(发货冲减) 2-客户取消
        switch (afterSaleType) {
            case 0:
                type = "53";
                break;
            case 1:
                type = "52";
                break;
            case 2:
                type = "51";
                break;
        }
        return String.format("%s%s%s%s", dateStr, companyCode, type, formatNumber(sequence, 5));
    }

}
