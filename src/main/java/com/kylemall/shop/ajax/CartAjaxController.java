package com.kylemall.shop.ajax;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;

@RestController
public class CartAjaxController {
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@PostMapping("/clearCart.ajax")
	public Map<String, Integer> clearCart(
			HttpSession session) {
		Member member = (Member) session.getAttribute("member");
		shoppingCartService.clearCart(member.getId());
		
		Map<String, Integer> result = new HashMap<>();
		int headerCount = shoppingCartService.getCartCount(member.getId());
		
		result.put("cnt", headerCount);
		return result;
		
	}
	
	
	@PostMapping("/deleteCart.ajax")
	public Map<String, Object> deleteCart(
			HttpSession session, @RequestParam("no") int no){
		shoppingCartService.deleteCart(no);
		
		Map<String, Object> result = new HashMap<>();
		Member member = (Member) session.getAttribute("member");
		
		result.put("cartList", shoppingCartService.getCartItemsByMemberId(member.getId()));
		result.put("cnt", shoppingCartService.getCartCount(member.getId()));
		return result;
	}
	
	@PostMapping("/addCart.ajax")
	public Map<String, Integer> addCart(
			@RequestParam("count") int count,
			@RequestParam("memberId") String id,
			@RequestParam("productNo") int No){
		
		shoppingCartService.insertCart(count, id, No);
		
		int cartCnt = shoppingCartService.getCartCount(id);
		Map<String, Integer> map = new HashMap<>();
		map.put("count", cartCnt);
		
		return map;
	}
	
}
