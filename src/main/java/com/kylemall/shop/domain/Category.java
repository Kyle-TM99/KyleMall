package com.kylemall.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

	private Integer categoryNo;
	private String categoryName;
	private Integer parentNo;
	
}
