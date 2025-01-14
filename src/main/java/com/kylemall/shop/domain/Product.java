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
public class Product {

	private Integer productNo;
	private String productName;
	private String productDescription;
	private Integer productPrice;
	private Integer stockQuantity;
	private String imageUrl;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	private String categoryName;
	private String saleOk;
	private Integer salePrice;
	
}
