package com.kylemall.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kylemall.shop.domain.CustomerSupport;
import com.kylemall.shop.mapper.CustomerSupportMapper;

@Service
public class CustomerSupportService {
	
	@Autowired
	private CustomerSupportMapper customerMapper;
	
	public void deleteFaq(Integer faqId) {
		customerMapper.deleteFaq(faqId);
	}
	
	public List<CustomerSupport> viewFaq () {
		return customerMapper.viewFaq();
	};
	
	public void insertFaq(String question, String answer) {
		customerMapper.insertFaq(question, answer);
	}
	
}
