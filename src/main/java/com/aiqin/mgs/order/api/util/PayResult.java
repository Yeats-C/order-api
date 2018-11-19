package com.aiqin.mgs.order.api.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * 接收支付结果
 */
@Controller
public class PayResult {
	private static final long serialVersionUID = 1L;
//	private static final Logger L = Logger.getLogger(PayResult.class);
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PayResult() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    @RequestMapping(value = "/PayResult", method = RequestMethod.POST)
	protected void PayResult(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String reqParams = StreamUtil.read(request.getInputStream());
		System.out.println("-------支付结果:"+reqParams);
		StringBuffer sb = new StringBuffer("<xml><return_code>SUCCESS</return_code><return_msg>OK</return_msg></xml>");
		response.getWriter().append(sb.toString());
	}

}
