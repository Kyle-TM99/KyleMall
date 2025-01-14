package com.kylemall.shop.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.service.ShoppingCartService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ShoppingCartController {
	
	private final int deliveryPrice = 3000;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
	@PostMapping("/productPayment")
	public String getPaymentCart(HttpSession session, Model model) {
		Member member = (Member) session.getAttribute("member");
		
		List<ShoppingCart> cart = shoppingCartService.getCartItemsByMemberId(member.getId());
		
		int productPrice = 0;
		
		if (cart == null) {
	        cart = new ArrayList<>();
	    } else {
	    	for(int i = 0; i < cart.size(); i++) {
	    		
	    		if(cart.get(i).getSaleOk().equals("1")) { 
	    			productPrice += cart.get(i).getSalePrice() * cart.get(i).getQuantity();
	    		} else {
	    			productPrice += cart.get(i).getProductPrice() * cart.get(i).getQuantity();
	    		}
	    	}
	    }
		
		int totalPrice = productPrice + deliveryPrice;
		
	    model.addAttribute("sCart", cart);
	    model.addAttribute("productPrice", productPrice);
	    model.addAttribute("totalPrice", totalPrice);
	    model.addAttribute("delivery", deliveryPrice);
		return "views/productPayment";
	}
	
	@GetMapping("/shoppingCart")
	public String getCartItems(HttpSession session, Model model) {
	    Member member = (Member) session.getAttribute("member");
	    if (member == null) {
	        log.warn("Unauthenticated access to shopping cart");
	        return "redirect:/loginForm";
	    }

	    List<ShoppingCart> cart = shoppingCartService.getCartItemsByMemberId(member.getId());
	    log.info("Retrieved cart items: {}", cart);
	    
	    if (cart == null) {
	        cart = new ArrayList<>();
	    }
	    model.addAttribute("sCart", cart);
	    return "views/productCart";
	}
	
}