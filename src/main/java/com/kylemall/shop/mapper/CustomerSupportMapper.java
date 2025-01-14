package com.kylemall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.kylemall.shop.domain.CustomerSupport;

@Mapper
public interface CustomerSupportMapper {

	public List<CustomerSupport> viewFaq ();
	
	public void deleteFaq(Integer faqId);
	
	public void insertFaq(@Param("question") String question, @Param("answer") String answer);
}

