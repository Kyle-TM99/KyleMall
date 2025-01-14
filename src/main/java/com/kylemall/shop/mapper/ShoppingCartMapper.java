package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kylemall.shop.domain.ShoppingCart;

@Mapper
public interface ShoppingCartMapper {

	void updateCart(ShoppingCart item);
	
	void clearCart(String memberId);
	
	void deleteCart(int no);
	
	void insertCart(@Param("count") int count, @Param("memberId") String memberId, @Param("productNo") int productNo);
	
	int getCartCount(String memberId);
	
	List<ShoppingCart> getCartByMemberId(String memberId);
	
}
