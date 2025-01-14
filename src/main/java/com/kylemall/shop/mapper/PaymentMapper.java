package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kylemall.shop.domain.Order;
import com.kylemall.shop.domain.OrderDetail;
import com.kylemall.shop.domain.OrderSummary;
import com.kylemall.shop.domain.Payment;
import com.kylemall.shop.domain.Shipping;

@Mapper
public interface PaymentMapper {
	
	List<OrderDetail> getOrderDetail(String merchantUid);
	
	List<OrderSummary> getAllOrderSummary(String memberId);
	
	Payment getPaymentList(String merchantUid);
	
	Shipping getShippingList(String merchantUid);
	
	Order getOrderList(String merchantUid);
	
	Integer memberTotalSpent(String memberId);
	
	int orderCountCheck(String impUid);
	
	void insertOrder(Order order);
	
	void insertOrderDetail(@Param("detailQuantity") int detailQuantity, @Param("merchantUid") String merchantUid, 
			@Param("productNo") int productNo);
	
	void insertPayment(Payment payment);
	
	void insertShipping(Shipping shipping);
	
}
