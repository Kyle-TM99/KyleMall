package com.kylemall.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kylemall.shop.domain.CustomerSupport;
import com.kylemall.shop.service.CustomerSupportService;

@Controller
public class CustomerSupportController {

	@Autowired
	private CustomerSupportService customerService;
	
	@GetMapping("/faq")
	public String viewFaq(Model model) {
		
		List<CustomerSupport> csFaq = customerService.viewFaq();
		
		model.addAttribute("csFaq", csFaq);
		
		return "views/faq";
	}
	
	@GetMapping("/addFaqForm")
	public String addFaq(Model model) {
		
		
		return "views/addFaq";
	}
	
	@PostMapping("/insertFaq")
	public String insertFaq(Model model,
			@RequestParam("question") String question,
			@RequestParam("answer") String answer) {
		
		customerService.insertFaq(question, answer);
		
		return "redirect:faq";
		
	}
	
}
