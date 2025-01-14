package com.kylemall.shop.DTO;

import lombok.Data;

@Data
public class PaymentVerificationRequest {
    private String impUid;
    private String merchantUid;
    private Integer totalPrice;
    private String paymentMethod;
    private String recipientName;
    private String address;
    private String phoneNumber;
    private String productTitle;
    private String orderMsg;
    private String shippingMsg;
}
