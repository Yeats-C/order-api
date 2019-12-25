package com.aiqin.mgs.order.api.domain.po.order;

import com.aiqin.mgs.order.api.domain.response.order.ErpOrderOperationControlResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 订单实体
 *
 * @author: Tao.Chen
 * @version: v1.0.0
 * @date 2019/12/9 9:49
 */
@Data
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ErpOrderInfo {

    /***主键*/
    private Long id;
    /***业务id*/
    private String orderStoreId;
    /***订单编码*/
    @ApiModelProperty(value="订单编号")
    private String orderStoreCode;
    /***公司编码*/
    private String companyCode;
    /***公司名称*/
    private String companyName;
    /***订单类型编码 ErpOrderTypeEnum */
    private String orderTypeCode;
    /***订单类型名称*/
    private String orderTypeName;
    /***订单类别编码 ErpOrderCategoryEnum*/
    private String orderCategoryCode;
    /***订单类别名称*/
    private String orderCategoryName;
    /***供应商编码*/
    private String supplierCode;
    /***供应商名称*/
    private String supplierName;
    /***仓库编码*/
    @ApiModelProperty(value="仓库编码")
    private String transportCenterCode;
    /***仓库名称*/
    @ApiModelProperty(value="仓库名称")
    private String transportCenterName;
    /***库房编码*/
    @ApiModelProperty(value="库房编码")
    private String warehouseCode;
    /***库房名称*/
    @ApiModelProperty(value="库房名称")
    private String warehouseName;
    /***客户编码*/
    private String customerCode;
    /***客户名称*/
    private String customerName;
    /***订单状态*/
    @ApiModelProperty(value="订单状态")
    private Integer orderStatus;
    /***是否锁定(0是1否）*/
    private Integer orderLock;
    /***锁定原因*/
    private String lockReason;
    /***是否是异常订单(0是1否)*/
    private Integer orderException;
    /***异常原因*/
    private String exceptionReason;
    /***是否删除(0是1否)*/
    private Integer orderDelete;
    /***支付状态0已支付  1未支付*/
    private Integer paymentStatus;

    /***收货区域 :省编码*/
    private String provinceId;
    /***收货区域 :省*/
    private String provinceName;
    /***收货区域 :市编码*/
    private String cityId;
    /***收货区域 :市*/
    private String cityName;
    /***收货区域 :区县编码*/
    private String districtId;
    /***收货区域 :区县*/
    private String districtName;

    /***收货地址*/
    private String receiveAddress;
    /***配送方式编码*/
    private String distributionModeCode;
    /***配送方式名称*/
    private String distributionModeName;
    /***收货人*/
    private String receivePerson;
    /***收货人电话*/
    private String receiveMobile;
    /***邮编*/
    private String zipCode;
    /***支付方式编码*/
    private String paymentCode;
    /***支付方式名称*/
    private String paymentName;
    /***运费*/
    private BigDecimal deliverAmount;
    /***商品总价*/
    private BigDecimal totalProductAmount;
    /***实际商品总价 （发货商品的总价格） */
    private BigDecimal actualTotalProductAmount;
    /***实际发货数量*/
    private Long actualProductCount;
    /***优惠额度*/
    private BigDecimal discountAmount;
    /***实际支付金额 */
    private BigDecimal orderAmount;
    /***付款日期*/
    private Date paymentTime;
    /***发货时间*/
    private Date deliveryTime;
    /***发运时间*/
    private Date transportTime;
    /***发运状态*/
    private Integer transportStatus;
    /***发运时间*/
    private Date receiveTime;
    /***发票类型 1不开 2增普 3增专*/
    private Integer invoiceType;
    /***发票抬头*/
    private String invoiceTitle;
    /***体积*/
    private Long totalVolume;
    /***实际体积*/
    private Long actualTotalVolume;
    /***重量*/
    private Long totalWeight;
    /***实际重量*/
    private Long actualTotalWeight;
    /***主订单号  如果非子订单 此处存order_code*/
    private String mainOrderCode;
    /***订单级别(0主1子订单)*/
    private Integer orderLevel;
    /***是否被拆分 (0是 1否)*/
    private Integer splitStatus;
    /***申请取消时的状态*/
    private Integer beforeCancelStatus;
    /***备注*/
    private String remake;
    /***门店类型*/
    private Integer storeType;
    /***门店id*/
    private String storeId;
    /***门店编码*/
    private String storeCode;
    /***门店名称*/
    private String storeName;
    /***运输公司编码*/
    private String transportCompanyCode;
    /***运输公司名称*/
    private String transportCompanyName;
    /***运输单号*/
    private String transportCode;
    /***物流id*/
    private String logisticsId;
    /***费用id*/
    private String feeId;
    /***是否发生退货  0 是  1.否*/
    private Integer orderReturn;
    /***加盟商id*/
    private String franchiseeId;
    /***加盟商编码*/
    private String franchiseeCode;
    /***加盟商名称*/
    private String franchiseeName;
    /***来源单号*/
    private String sourceCode;
    /***来源名称*/
    private String sourceName;
    /***来源类型*/
    private Integer sourceType;

    /***0. 启用   1.禁用*/
    private Integer useStatus;
    /***创建人编码*/
    private String createById;
    /***创建人名称*/
    private String createByName;
    /***更新人编码*/
    private String updateById;
    /***更新人名称*/
    private String updateByName;
    /***创建时间*/
    private Date createTime;
    /***更新时间*/
    private Date updateTime;

    /***订单费用*/
    private ErpOrderFee orderFee;
    @ApiModelProperty(value="订单按钮控制")
    private ErpOrderOperationControlResponse operation;
    /***订单物流信息*/
    private ErpOrderLogistics orderLogistics;
    /***订单商品明细*/
    @ApiModelProperty(value="订单商品明细行")
    private List<ErpOrderItem> itemList;
    @ApiModelProperty(value="订单关联子订单列表")
    private List<ErpOrderInfo> secondaryOrderList;
    /***订单日志*/
    private List<ErpOrderOperationLog> operationLogList;

}
