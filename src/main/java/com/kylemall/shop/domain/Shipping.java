package com.kylemall.shop.domain;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {
	
	private Integer shippingId;          // 배송 ID
    private String merchantUid;             // 주문 ID (Foreign Key)
    private String recipientName;         // 수령인 이름
    private String address;               // 배송 주소
    private String phoneNumber;           // 수령인 전화번호
    private String deliveryStatus;        // 배송 상태
    private String trackingNumber;        // 운송장 번호
    private Date shippingDate;            // 배송 시작일
    private Date estimatedArrival;        // 예상 도착일
    private String shippingMsg;           // 배송 메세지
    private Date shippingCreatedAt;               // 생성일
    private Date shippingUpdatedAt;               // 수정일
	
}
