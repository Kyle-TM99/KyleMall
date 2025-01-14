package com.kylemall.shop.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.service.CustomerSupportService;
import com.kylemall.shop.service.PaymentService;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@RestController
public class CustomerSupportAjaxController {
	
	@Autowired
	private CustomerSupportService customerService;
	
	@PostMapping("/deleteFaq.ajax")
	public Map<String, Object> deleteCart(
			HttpSession session, @RequestParam("faqId") Integer faqId){
		customerService.deleteFaq(faqId);
		
		Map<String, Object> result = new HashMap<>();
		
		result.put("faqList", customerService.viewFaq());
		return result;
	}
	
}
