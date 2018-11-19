package com.aiqin.mgs.order.api.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * 签名
 *
 *
 */
public class SignatureHzy {


	/**
	 * 生成签名
	 * @param characterEncoding
	 * @param parameters
	 * @param key
	 * @return
	 */
	public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters,String key){
	    StringBuffer sb = new StringBuffer();
	    StringBuffer sbkey = new StringBuffer();
	    Set es = parameters.entrySet();  //所有参与传参的参数按照accsii排序（升序）
	    Iterator it = es.iterator();
	    while(it.hasNext()) {
	        Map.Entry entry = (Map.Entry)it.next();
	        String k = (String)entry.getKey();
	        Object v = entry.getValue();
	        //空值不传递，不参与签名组串
	        if(null != v && !"".equals(v)) {
	            sb.append(k + "=" + v + "&");
	            sbkey.append(k + "=" + v + "&");
	        }
	    }
	    //System.out.println("字符串:"+sb.toString());
	    sbkey=sbkey.append("key="+key);
	    System.out.println("字符串:"+sbkey.toString());
	    //MD5加密,结果转换为大写字符
	    String sign = MD5Util.MD5Encode(sbkey.toString(), characterEncoding).toUpperCase();
	    System.out.println("MD5加密值:"+sign);
	    return sb.toString()+"sign="+sign;
	}
	
	/**
     * 签名算法-TEST
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
	public static void main(String[] args) {
	       SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
	       //String mfrchant_id="fffff";
	       String merchant_id="20000001";  //爱亲商户账号
	       String business_type="1005";
	       String out_trade_no="1400000001";
	       String key="3A4BC4A4000CF1B5FFA9E351E6C1539E";
	       //parameters.put("mfrchant_id", mfrchant_id);
	       parameters.put("merchant_id", merchant_id);
	       parameters.put("business_type", business_type);
	       parameters.put("out_trade_no",out_trade_no);
	       String characterEncoding = "UTF-8";         //指定字符集UTF-8
	       String mySign = createSign(characterEncoding,parameters,key);
	       //System.out.println("我 的签名是："+mySign);
	   }

}
