package com.kylemall.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String merchantUid; // 주문 번호 (Primary Key)
    private String memberId;    // 회원 ID (Foreign Key)
    private Integer totalAmount;    // 총 결제 금액
    private String orderStatus; // 주문 상태
    private String productTitle; // 상품명
    private String orderMsg;      // 주문 메세지
    private Timestamp orderCreatedAt; // 생성 일자
    private Timestamp orderUpdatedAt; // 수정 일자
}
