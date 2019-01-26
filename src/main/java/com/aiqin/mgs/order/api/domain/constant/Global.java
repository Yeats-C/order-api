package com.aiqin.mgs.order.api.domain.constant;


public interface Global {

    /**
     * 禁用状态
     */
    String USER_OFF = "0";
    /**
     * 启用状态
     */
    String USER_ON = "1";
    
    //对接通联-成功状态 
    String SUCCESS_STATUS = "OK";
    String FAIL_STATUS = "error";
    
    //会员类型（2.企业会员，3个人会员）
    Integer MEMBER_TYPE_2 = 2;
    Integer MEMBER_TYPE_3 = 3;
    
    
//    //访问终端类型(1, Mobile ，2pc)
//    String SOURCE_TYPE_1 = "1";
//    String SOURCE_TYPE_2 = "2";
    
    //审核结果 2：审核成功 3：审核失败
    Long RESULT_2 = 2L; 
    Long RESULT_3 = 3L;
    
    //是否为安全卡(1.是，2不是)
    Integer ISSAFETYBANK_1 = 1;
    Integer ISSAFETYBANK_2 = 2;
    
    //是否已设置支付密码 1:未设置 2：已设置
    Integer IS_PASSWORD_1 = 1;
    Integer IS_PASSWORD_2 = 2;
    
    //银行卡类型 1:储蓄卡 2：信用卡
    Long CARD_TYPE_1 = 1L;
    Long CARD_TYPE_2 = 2L;
    
    //通联接口类型
    String INTERFACE_TYPE_0="MemberService";
    
    //通联接口-创建会员
    String CREATE_MEMBER="createMember";
    //通联接口-个人实名认证
    String SET_REAL_NAME="setRealName";
    
    //通联接口-设置企业信息
    String SET_COMPANY_INFO="setCompanyInfo";
    
    //通联接口-发送短信验证码
    String SEND_VERIFICATION_CODE="sendVerificationCode";
    
    //通联接口-请求绑定银行卡 
    String APPLY_BIND_BAK_CARD ="applyBindBankCard";
    
    //对接通联-锁定会员
    String LOCK_MEMBER ="lockMember";
    
    //对接通联-解锁会员
    String UN_LOCK_MEMBER ="unlockMember"; 
    
    //对接通联-会员电子协议签约
    String SIGN_CONTRACT ="signContract"; 
    
    //对接通联-绑定手机
    String BIND_PHONE ="bindPhone"; 
    
    //对接通联-解绑银行卡
    String UN_BIND_CARD ="unbindBankCard"; 
    
    //对接通联-修改绑定手机
    String CHANGE_BIND_PHONE ="changeBindPhone"; 
    
    //对接通联-设置安全卡
    String SET_SAFE_CARD ="setSafeCard"; 
    
    //手机验证码类型
    Long PHONE_CHECK_TYPE = 9L;
    
    
    //证件类型
    
    //会员状态(1正常，2冻结,3待审批,4审批中,5审批失败)
    Integer MEMBER_STATUS_1 = 1;
    Integer MEMBER_STATUS_2 = 2;
    Integer MEMBER_STATUS_3 = 3;
    Integer MEMBER_STATUS_4 = 4;
    Integer MEMBER_STATUS_5 = 5;
    
    
    //反馈结果
    Integer RETRURN_RESULT_1 = 1; //审核成功
    Integer RETRURN_RESULT_2 = 2; //审核失败
    Integer RETRURN_RESULT_4 = 4; //签约成功
    Integer RETRURN_RESULT_5 = 5; //签约失败
    
    //银行卡绑定状态(1.已绑定，2已解除，3.未绑定)
    Integer BANK_STATUS_1 = 1;
    Integer BANK_STATUS_2 = 2;
    Integer BANK_STATUS_3 = 3;
    
    //用户来源类型(1.爱亲总账户,2门店,3导购)
    Integer USER_SOURCE_1 = 1;
    Integer USER_SOURCE_2 = 2;
    Integer USER_SOURCE_3 = 3;
    
    //银行卡账户类型 1.对公账户，2个人账户
    Integer BANK_TYPE_1 = 1;
    Integer BANK_TYPE_2 = 2;
    
    //手机绑定状态 1:未绑定,2:已绑定
    Integer BIND_PHONE_STATUS_1 = 1;
    Integer BIND_PHONE_STATUS_2 = 2;
    
    //签约状态(1.未签约，2.已签约)
    Integer SIGNING_STATUS_1 = 1;
    Integer SIGNING_STATUS_2 = 2;
    
    
    //爱亲总账户
    String AIQIN_USER_CODE ="ZH0001";
    String AIQIN_USER_NAME ="爱亲总账户";
    
    //门店接口地址
    String STORE_URL = "store.api.aiqin.com";
    
    //门店接口地址
    String CONTROL_URL = "control.api.aiqin.com";
    
    
    //接口目录-查询门店数据
    String STORE_LIST = "/backstage/store/lists?"; 
    
    //接口目录-查询门店数据
    String CONTROL_LIST = "/backstage/store/all/person?"; 
    
    /**
	 * 企业信息审核结果通知
	 */
	public static final String COMPANY_CHECK_RESULT_URL = "http://39.96.90.159:9001//member/comgz?";
	
	/**
	 * 签约审核结果通知
	 */
	public static final String SINGNING_STATUS_URL = "http://39.96.90.159:9001//member/touqy?";
	
	
	//财务类型：
	//1）pos收银消费转入：to c 订单，渠道来源为“pos收银台的”订单，对方信息为付款者的手机号和姓名，如未填则“-” 表示；
	//2）微商城消费转入：to c 订单, 渠道来源为“微商城”订单，对方信息为付款者的手机号和姓名，如未填则“-” 表示；
	//3）web收银：to c 订单, 渠道来源为“web收银台”的订单，对方信息为付款者的手机号和姓名，如未填则“-” 表示；
	//4）线上提现：每一次提现记录都需要记录，提现列表里没有“订单号”和“对方信息”，用-表示；
	//5）手续费：提现会产生手续费，手续费到爱亲的手续费账户里，没有订单号，对方信息为入账的账户商户名。如：爱亲科技股份有限公司。
	//6）充值到配送账户：客户每一次充值要生成一个订单，取订单类型为“配送充值”的订单支付信息，充值订单号生成xxxx，对方信息为充值方的账户信息，比如支付宝账号+姓名
	//7）客户取消退货：to B 退货单，取退单类型为“客户取消”的订单支付信息；
	//8）缺货取消退货：to B 退货单，取退单类型为“缺货取消”的订单支付信息；
	//9）售后退货：to B 退货单，取退单类型为“售后退货”的订单支付信息；
	//10）配送订单：to B 订单，取配送订货单里，订单类型为“配送订货”的订单支付信息。
	//11）直送订单：to B 订单，取直送订货单里，订单类型为“直送订货”的订单支付信息。
	Integer TRANS_TYPE_1 =1; //pos收银消费转入
	Integer TRANS_TYPE_2 =2; //微商城消费转入
	Integer TRANS_TYPE_3 =3; //web收银
	Integer TRANS_TYPE_4 =4; //线上提现
	Integer TRANS_TYPE_5 =5; //手续费
	Integer TRANS_TYPE_6 =6; //充值到配送账户
	Integer TRANS_TYPE_7 =7; //客户取消退货
	Integer TRANS_TYPE_8 =8; //缺货取消退货
	Integer TRANS_TYPE_9 =9; //售后退货
	Integer TRANS_TYPE_10 =10; //配送订单
	Integer TRANS_TYPE_11 =11; //直送订单
	
	//交易类型 1:付款账户,2:普通账户,3：手续费账户
	Integer TRANS_1 = 1;
	Integer TRANS_2 = 2;
	Integer TRANS_3 = 3;
	
	//账户类型 1：无 2:配送账户,3:直送账户
	Integer ACCOUNT_TYPE_1=1;
	Integer ACCOUNT_TYPE_2=2;
	Integer ACCOUNT_TYPE_3=3;
	//业务场景: 0 TO C,1 TO B
    Integer SERVICESCENE_TOB=1;
    Integer SERVICESCENE_TOC=0;
}




