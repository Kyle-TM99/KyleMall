package com.kylemall.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalController {

	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@ModelAttribute("cartCount")
	public int getCartCount(HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		
		// 로그인 여부 확인
	    if (member == null) {
	        return 0; // 로그인되지 않았으면 기본값 0 반환
	    }
		
		return shoppingCartService.getCartCount(member.getId());
	}
	
}
