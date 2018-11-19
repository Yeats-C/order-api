package com.aiqin.mgs.order.api.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.aiqin.mgs.order.api.domain.PaymentInfo;
import com.aiqin.mgs.order.api.domain.PaymentReturnInfo;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;

/**
 * 统一下单接口
 */
@Controller
public class Xiadan {
	private static final long serialVersionUID = 1L;
//	private static final Logger L = Logger.getLogger(Xiadan.class);
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Xiadan() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @RequestMapping(value = "/Xiadan", method = RequestMethod.POST)
	protected void Xiadan(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			
			String openid = request.getParameter("openid");
			PaymentInfo order = new PaymentInfo();
			order.setAppid(Configure.getAppID());
			order.setMch_id(Configure.getMch_id());
			order.setNonce_str(RandomStringGenerator.getRandomStringByLength(32));
			order.setBody("abcdefg");  //我改成了DHA8管理系统,在前端调用的时候报错, 参数total-free
			order.setOut_trade_no(RandomStringGenerator.getRandomStringByLength(32));

			order.setTotal_fee(1990);
//			order.setTotal_fee(10);
			
			order.setNotify_url("https://dha8.top/PayResult");
//			order.setNotify_url("http://localhost:8448/PayResult");
			
			order.setSpbill_create_ip("116.62.18.101");  //服务器IP
			
			order.setTrade_type("JSAPI");
			order.setOpenid(openid);
			order.setSign_type("MD5");
			
			
			//生成签名
			String sign = Signature.getSign(order);
			order.setSign(sign);
			
			
			String result = HttpRequest.sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder", order);
			System.out.println("order====="+order);
			System.out.println("result===="+result);
			System.out.println("---------下单返回:"+result);
			XStream xStream = new XStream();
			xStream.alias("xml", PaymentReturnInfo.class); 

			PaymentReturnInfo returnInfo = (PaymentReturnInfo)xStream.fromXML(result);
			JSONObject json = new JSONObject();
			json.put("prepay_id", returnInfo.getPrepay_id());
			response.getWriter().append(json.toJSONString());
		} catch (Exception e) {
			e.printStackTrace();
//			L.error("-------", e);
		}
		
	}


}
