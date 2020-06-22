package com.aiqin.mgs.order.api.domain.wholesale;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel
@Data
public class MerchantAccount {
    @ApiModelProperty(value="")
    @JsonProperty("id")
    private Long id;

    @ApiModelProperty(value="迅联id")
    @JsonProperty("merchant_id")
    private String merchantId;

    @ApiModelProperty(value="迅联商户号")
    @JsonProperty("merchant_code")
    private String merchantCode;

    @ApiModelProperty(value="商户名称")
    @JsonProperty("merchant_name")
    private String merchantName;

    @ApiModelProperty(value="商户密钥")
    @JsonProperty("merchant_sign")
    private String merchantSign;

    @ApiModelProperty(value="商户mcc")
    @JsonProperty("merchant_mcc")
    private String merchantMcc;

    @ApiModelProperty(value="商户类型:0个人 1企业")
    @JsonProperty("merchant_type")
    private Integer merchantType;

    @ApiModelProperty(value="开通时间")
    @JsonProperty("register_time")
    private String registerTime;

    @ApiModelProperty(value="营业执照注册地址")
    @JsonProperty("register_address")
    private String registerAddress;

    @ApiModelProperty(value="营业地址")
    @JsonProperty("business_address")
    private String businessAddress;

    @ApiModelProperty(value="营业执照")
    @JsonProperty("business_license")
    private String businessLicense;

    @ApiModelProperty(value="证件类型:0 身份证 1护照 2中国香港居民 3中国澳门 4 中国台湾")
    @JsonProperty("card_type")
    private Integer cardType;

    @ApiModelProperty(value="证件持有人类型 : 0法人")
    @JsonProperty("card_person_type")
    private Integer cardPersonType;

    @ApiModelProperty(value="证件持有人名称")
    @JsonProperty("card_person_name")
    private String cardPersonName;

    @ApiModelProperty(value="证件号")
    @JsonProperty("card_no")
    private String cardNo;

    @ApiModelProperty(value="证件有效期")
    @JsonProperty("card_finish_date")
    private String cardFinishDate;

    @ApiModelProperty(value="")
    @JsonProperty("provinces_name")
    private String provincesName;

    @ApiModelProperty(value="")
    @JsonProperty("provinces_id")
    private String provincesId;

    @ApiModelProperty(value="")
    @JsonProperty("city_name")
    private String cityName;

    @ApiModelProperty(value="")
    @JsonProperty("city_id")
    private String cityId;

    @ApiModelProperty(value="")
    @JsonProperty("district_name")
    private String districtName;

    @ApiModelProperty(value="")
    @JsonProperty("district_id")
    private String districtId;

    @ApiModelProperty(value="账户类型")
    @JsonProperty("account_type")
    private Integer accountType;

    @ApiModelProperty(value="开户银行城市")
    @JsonProperty("bank_city")
    private String bankCity;

    @ApiModelProperty(value="开户行支行")
    @JsonProperty("bank_name")
    private String bankName;

    @ApiModelProperty(value="开户行行号")
    @JsonProperty("union_bank_code")
    private String unionBankCode;

    @ApiModelProperty(value="收款人")
    @JsonProperty("payee_name")
    private String payeeName;

    @ApiModelProperty(value="银行账号")
    @JsonProperty("bank_code")
    private String bankCode;

    @ApiModelProperty(value="联系人")
    @JsonProperty("contacts_person")
    private String contactsPerson;

    @ApiModelProperty(value="客服电话")
    @JsonProperty("contacts_phone")
    private String contactsPhone;

    @ApiModelProperty(value="联系人邮箱")
    @JsonProperty("contacts_email")
    private String contactsEmail;

    @ApiModelProperty(value="")
    @JsonProperty("customer_phone")
    private String customerPhone;

    @ApiModelProperty(value="微信一级商户号")
    @JsonProperty("wx_merchant_id")
    private String wxMerchantId;

    @ApiModelProperty(value="阿里一级商户号")
    @JsonProperty("ali_merchant_id")
    private String aliMerchantId;

    @ApiModelProperty(value="与加盟商关联状态:0禁用 1启用")
    @JsonProperty("relation_status")
    private Integer relationStatus;

    @ApiModelProperty(value="加盟商id")
    @JsonProperty("franchisee_id")
    private String franchiseeId;

    @ApiModelProperty(value="加盟商code/客户编码")
    @JsonProperty("franchisee_code")
    private String franchiseeCode;

    @ApiModelProperty(value="加盟商名称")
    @JsonProperty("franchisee_name")
    private String franchiseeName;

    @ApiModelProperty(value="公司id")
    @JsonProperty("company_id")
    private String companyId;

    @ApiModelProperty(value="公司code")
    @JsonProperty("company_code")
    private String companyCode;

    @ApiModelProperty(value="公司名称")
    @JsonProperty("company_name")
    private String companyName;

    @ApiModelProperty(value="机构号")
    @JsonProperty("inscd")
    private String inscd;

    @ApiModelProperty(value="")
    @JsonProperty("create_by_id")
    private String createById;

    @ApiModelProperty(value="")
    @JsonProperty("create_by_name")
    private String createByName;

    @ApiModelProperty(value="")
    @JsonProperty("update_by_id")
    private String updateById;

    @ApiModelProperty(value="")
    @JsonProperty("update_by_name")
    private String updateByName;

    @ApiModelProperty(value="")
    @JsonProperty("create_time")
    private Date createTime;

    @ApiModelProperty(value="")
    @JsonProperty("update_time")
    private Date updateTime;



    @ApiModelProperty(value="加盟商类型:0爱亲客户， 1批发商")
    @JsonProperty("relation_type")
    private Integer relationType;




    /**
     * 可用余额
     */
    @ApiModelProperty(value="可用余额")
    @JsonProperty("available_balance")
    private BigDecimal availableBalance;

}