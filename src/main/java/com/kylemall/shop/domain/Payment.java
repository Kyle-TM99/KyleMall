package com.kylemall.shop.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

	private String impUid;          // 상점 고유 결제 ID
    private String merchantUid;              // 연관된 주문 ID
    private String paymentMethod;         // 결제 수단
    private String paymentStatus;         // 결제 상태
    private Integer amount;            // 결제 금액
    private Timestamp paidAt;             // 결제 완료 시간
    private Timestamp cancelledAt;        // 결제 취소 시간
    private Timestamp paymentCreatedAt;          // 레코드 생성 시간
    private Timestamp paymentUpdatedAt;          // 레코드 업데이트 시간
	
}
