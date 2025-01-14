package com.kylemall.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kylemall.shop.domain.Order;
import com.kylemall.shop.domain.OrderDetail;
import com.kylemall.shop.domain.OrderSummary;
import com.kylemall.shop.domain.Payment;
import com.kylemall.shop.domain.Shipping;
import com.kylemall.shop.mapper.PaymentMapper;

@Service
public class PaymentService {

	@Autowired
	private PaymentMapper paymentMapper;

	private static final String PORTONE_API_URL = "https://api.iamport.kr";
	private static final String API_KEY = "5441763443418651";
	private static final String API_SECRET = "vxP1CkKUQo4bZAOwjWMj9ec5jpQeJc4VkeN5mUWApucbZMKdRPYxc70pi4wfd3Adfi9sLEqjJnv7afu0";

	public boolean verifyPayment(String impUid, String merchantUid, int expectedAmount) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String paymentInfoUrl = PORTONE_API_URL + "/payments/" + impUid;
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", getAccessToken());
			HttpEntity<String> entity = new HttpEntity<>(headers);

			// API 호출
			ResponseEntity<String> response = restTemplate.exchange(paymentInfoUrl, HttpMethod.GET, entity,
					String.class);

			// 응답 디버깅
			System.out.println("Payment Info Response: " + response.getBody());

			ObjectMapper mapper = new ObjectMapper();
			JsonNode paymentInfo = mapper.readTree(response.getBody()).get("response");

			if (paymentInfo == null)
				return false;

			String retrievedMerchantUid = paymentInfo.get("merchant_uid").asText();
			int amount = paymentInfo.get("amount").asInt();
			String status = paymentInfo.get("status").asText();

			// 조건 검증 디버깅
			System.out.println("Retrieved Merchant UID: " + retrievedMerchantUid);
			System.out.println("Expected Amount: " + expectedAmount + ", Actual Amount: " + amount);
			System.out.println("Payment Status: " + status);

			if (!"paid".equals(status))
				return false;
			if (!merchantUid.equals(retrievedMerchantUid))
				return false;
			if (amount != expectedAmount)
				return false;
			if (checkIfMerchantUidExists(merchantUid))
				return false;

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private String getAccessToken() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			String url = PORTONE_API_URL + "/users/getToken";
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			String body = "{ \"imp_key\": \"" + API_KEY + "\", \"imp_secret\": \"" + API_SECRET + "\" }";
			HttpEntity<String> entity = new HttpEntity<>(body, headers);

			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jsonNode = mapper.readTree(response.getBody());

			return jsonNode.get("response").get("access_token").asText();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("AccessToken 생성 실패");
		}
	}

	private boolean checkIfMerchantUidExists(String impUid) {

		return paymentMapper.orderCountCheck(impUid) > 0;

	}

	List<OrderDetail> getOrderDetail(String merchantUid) {
		return paymentMapper.getOrderDetail(merchantUid);
	}

	public List<OrderSummary> getAllOrderSummary(String memberId) {
		return paymentMapper.getAllOrderSummary(memberId);
	}

	public Integer memberTotalSpent(String memberId) {
		Integer totalSpent = paymentMapper.memberTotalSpent(memberId);
		return totalSpent != null ? totalSpent : 0;
	}

	public Payment getPaymentList(String merchantUid) {
		return paymentMapper.getPaymentList(merchantUid);
	}

	public Shipping getShippingList(String merchantUid) {
		return paymentMapper.getShippingList(merchantUid);
	}

	public Order getOrderList(String merchantUid) {
		return paymentMapper.getOrderList(merchantUid);
	}

	public void insertOrder(Order order) {
		paymentMapper.insertOrder(order);
	}

	public void insertOrderDetail(int detailQuantity, String merchantUid, int productNo) {
		paymentMapper.insertOrderDetail(detailQuantity, merchantUid, productNo);
	}

	public void insertPayment(Payment payment) {
		paymentMapper.insertPayment(payment);
	}

	public void insertShipping(Shipping shipping) {
		paymentMapper.insertShipping(shipping);
	}

}