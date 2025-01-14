package com.kylemall.shop.domain;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCart {
	
	private int cartId;           // 장바구니 ID
    private int quantity;         // 수량
    private Timestamp addedAt;    // 추가 시간
    private String memberId;      // 회원 ID
    private int productNo;        // 상품 번호
    private String productName;   // 상품 이름
    private int productPrice;     // 상품 가격
    private String saleOk;		  // 할인 여부
    private Integer salePrice;    // 할인 가격
    private String imageUrl;      // 상품 이미지 URL
	
}
