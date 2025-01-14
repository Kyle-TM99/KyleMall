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
public class OrderDetail {
    // orderdetail 테이블 필드
    private int detailId;                  // 주문 상세 ID
    private int detailQuantity;            // 주문 수량
    private String merchantUid;            // 연관된 주문 ID
    private int productNo;                 // 상품 번호

    // product 테이블 필드
    private String productName;            // 상품 이름
    private String productDescription;     // 상품 설명
    private int productPrice;              // 상품 가격
    private int stockQuantity;             // 재고 수량
    private String imageUrl;               // 이미지 URL
    private Timestamp createdAt;           // 생성일
    private Timestamp updatedAt;           // 수정일
    private int categoryNo;                // 카테고리 번호
    private boolean saleOk;                // 판매 가능 여부
    private Integer salePrice;             // 세일 가격 (null일 수 있음)

    // 추가적인 비즈니스 로직이나 메소드를 필요에 따라 추가할 수 있습니다.
}
