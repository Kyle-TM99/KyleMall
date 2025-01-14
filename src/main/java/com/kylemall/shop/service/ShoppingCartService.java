package com.kylemall.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kylemall.shop.domain.ShoppingCart;
import com.kylemall.shop.mapper.ShoppingCartMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ShoppingCartService {
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	
	public void clearCart(String memberId) {
		shoppingCartMapper.clearCart(memberId);
	}
	
	public void deleteCart(int no) {
		shoppingCartMapper.deleteCart(no);
	}
	
	public void insertCart(int count, String memberId, int productNo) {
	    List<ShoppingCart> cart = shoppingCartMapper.getCartByMemberId(memberId);
	    log.info("장바구니 상품 수: " + cart.size());

	    boolean productExists = false; // 상품 존재 여부를 확인하기 위한 플래그

	    if (cart.size() != 0) {
	        for (ShoppingCart item : cart) {
	            if (productNo == item.getProductNo()) {
	                // 상품이 이미 존재하는 경우 수량 업데이트
	                item.setQuantity(item.getQuantity() + count); // 수량 증가
	                shoppingCartMapper.updateCart(item); // 수량 업데이트 호출
	                productExists = true; // 상품 존재 플래그 설정
	                break; // 상품을 찾았으므로 반복문 종료
	            }
	        }
	    }

	    // 상품이 장바구니에 존재하지 않는 경우 삽입
	    if (!productExists) {
	        shoppingCartMapper.insertCart(count, memberId, productNo);
	    }
	}
	
	public int getCartCount(String memberId) {
		return shoppingCartMapper.getCartCount(memberId);
	}
	
	public List<ShoppingCart> getCartItemsByMemberId(String memberId) {
	    return shoppingCartMapper.getCartByMemberId(memberId);
	}

}
