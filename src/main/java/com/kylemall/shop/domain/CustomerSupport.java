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
public class CustomerSupport {
	
	private Integer faqId;
	private String question;
	private String answer;
	private Timestamp regDate;
	
}
