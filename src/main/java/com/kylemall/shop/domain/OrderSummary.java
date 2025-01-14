package com.kylemall.shop.domain;

import java.sql.Timestamp;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;

@Data
public class OrderSummary {
	
    // payment 테이블 필드
    private String impUid;                // 상점 고유 결제 ID
    private String merchantUid;           // 연관된 주문 ID
    private String paymentMethod;         // 결제 수단
    private String paymentStatus;         // 결제 상태
    private int amount;                   // 결제 금액
    private Timestamp paidAt;             // 결제 완료 시간
    private Timestamp cancelledAt;        // 결제 취소 시간
    private Timestamp paymentCreatedAt;   // 결제 생성 시간
    private Timestamp paymentUpdatedAt;   // 결제 업데이트 시간

    // orders 테이블 필드
    private int totalAmount;              // 총 금액
    private String orderStatus;           // 주문 상태
    private String productTitle;          // 상품명
    private String orderMsg;              // 주문 메시지
    private Timestamp orderCreatedAt;     // 주문 생성 시간
    private Timestamp orderUpdatedAt;     // 주문 업데이트 시간
    private String memberId;              // 회원 ID

    // shipping 테이블 필드
    private int shippingId;               // 배송 ID
    private String recipientName;         // 수령인 이름
    private String address;                // 배송 주소
    private String phoneNumber;           // 수령인 전화번호
    private String deliveryStatus;        // 배송 상태
    private String trackingNumber;        // 운송장 번호
    private Timestamp shippingDate;       // 배송 시작일
    private Timestamp estimatedArrival;   // 예상 도착일
    private String shippingMsg;           // 배송 메시지
    private Timestamp shippingCreatedAt;  // 배송 생성일
    private Timestamp shippingUpdatedAt;  // 배송 업데이트일

    // Additional fields
    private String recipientPhone;    // 받는사람 연락처
    private String recipientAddress;  // 받는사람 주소
}
