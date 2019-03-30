package com.aiqin.mgs.order.api.domain;

import com.aiqin.mgs.order.api.base.PagesRequest;

/**
 * 支付---1
 * 
 * @author 黄祉壹
 *
 */
public class PaymentInfo extends PagesRequest{
	private String appid;// 小程序ID	
	private String mch_id;// 商户号
	private String nonce_str;// 随机字符串
	private String sign_type;//签名类型
	private String sign;// 签名
	private String body;// 商品描述
	private String out_trade_no;// 商户订单号
	private int total_fee;// 标价金额 ,单位为分
	private String spbill_create_ip;// 终端IP
	private String notify_url;// 通知地址
	private String trade_type;// 交易类型
	private String openid;//用户标识	
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}
	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}
	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}
	public String getTrade_type() {
		return trade_type;
	}
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	
//	private static long   mchId     = 20000001 ; //商户ID         
//	private String appId     ; //应用ID         
//	private int    passageId ; //通道ID         
//	private String mchOrderNo; //商户订单号     
//	private static String channelId = "wxpay_jsapi"; //渠道ID         
//	private static String currency  = "cny"  ; //币种           
//	private int    amount    ; //支付金额       
//	private String clientIp  ; //客户端IP       
//	private String device    ; //设备           
//	private String notifyUrl ="http://localhost:9021/settlement/payresult"; //支付结果回调URL
//	private String subject   ; //商品主题       
//	private String body      ; //商品描述信息   
//	private String param1    ; //扩展参数1      
//	private String param2    ; //扩展参数2      
//	private String extra     ; //附加参数       
//	private String sign      ; //签名 
//	private String apikey    ; //密钥
//	
//	
//	public long getMchId() {
//		return mchId;
//	}
//	public void setMchId(long mchId) {
//		this.mchId = mchId;
//	}
//	public String getAppId() {
//		return appId;
//	}
//	public void setAppId(String appId) {
//		this.appId = appId;
//	}
//	public int getPassageId() {
//		return passageId;
//	}
//	public void setPassageId(int passageId) {
//		this.passageId = passageId;
//	}
//	public String getMchOrderNo() {
//		return mchOrderNo;
//	}
//	public void setMchOrderNo(String mchOrderNo) {
//		this.mchOrderNo = mchOrderNo;
//	}
//	public String getChannelId() {
//		return channelId;
//	}
//	public void setChannelId(String channelId) {
//		this.channelId = channelId;
//	}
//	public String getCurrency() {
//		return currency;
//	}
//	public void setCurrency(String currency) {
//		this.currency = currency;
//	}
//	public int getAmount() {
//		return amount;
//	}
//	public void setAmount(int amount) {
//		this.amount = amount;
//	}
//	public String getClientIp() {
//		return clientIp;
//	}
//	public void setClientIp(String clientIp) {
//		this.clientIp = clientIp;
//	}
//	public String getDevice() {
//		return device;
//	}
//	public void setDevice(String device) {
//		this.device = device;
//	}
//	public String getNotifyUrl() {
//		return notifyUrl;
//	}
//	public void setNotifyUrl(String notifyUrl) {
//		this.notifyUrl = notifyUrl;
//	}
//	public String getSubject() {
//		return subject;
//	}
//	public void setSubject(String subject) {
//		this.subject = subject;
//	}
//	public String getBody() {
//		return body;
//	}
//	public void setBody(String body) {
//		this.body = body;
//	}
//	public String getParam1() {
//		return param1;
//	}
//	public void setParam1(String param1) {
//		this.param1 = param1;
//	}
//	public String getParam2() {
//		return param2;
//	}
//	public void setParam2(String param2) {
//		this.param2 = param2;
//	}
//	public String getExtra() {
//		return extra;
//	}
//	public void setExtra(String extra) {
//		this.extra = extra;
//	}
//	public String getSign() {
//		return sign;
//	}
//	public void setSign(String sign) {
//		this.sign = sign;
//	}
//	public String getApikey() {
//		return apikey;
//	}
//	public void setApikey(String apikey) {
//		this.apikey = apikey;
//	}
	
//	public static void main(String[] args) {
//		PaymentInfo info = new PaymentInfo();
//	}
}
