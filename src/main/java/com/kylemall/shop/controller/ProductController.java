package com.kylemall.shop.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kylemall.shop.domain.Product;
import com.kylemall.shop.service.PaymentService;
import com.kylemall.shop.service.ProductService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProductController {
	
	private static final String DEFAULT_PATH = "src/main/resources/static/kylemallproducts/";
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/productManagement")
	public String productManagement(Model model,
			@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
			@RequestParam(value = "type", required = false, defaultValue = "null") String type,
			@RequestParam(value = "keyword", required = false, defaultValue = "null") String keyword,
			@RequestParam(value = "category", required = false, defaultValue = "0") int category,
			@RequestParam(value = "sortBy", required = false, defaultValue = "0") String sortBy) {
		
		Map<String, Object> modelMap = productService.productList(pageNum, type, keyword, category, sortBy);
		
		model.addAllAttributes(modelMap);
		
		return "views/productManagement";
	}
	
	@PostMapping("/addProduct")
	public String addProduct(Product product, HttpSession session, RedirectAttributes reAttrs,
			@RequestParam(value="fileData1", required = false) MultipartFile imageUrl) throws IllegalStateException, IOException {
		
		if(product.getSaleOk() == null) {
			product.setSaleOk("0");
		} else {
			product.setSaleOk("1");
		}
		
		if(imageUrl != null && !imageUrl.isEmpty()) {
			File parent = new File(DEFAULT_PATH);
			
			if (!parent.isDirectory() && !parent.exists()) {
				parent.mkdir();
			}
			
			UUID uid = UUID.randomUUID();
			String extension = StringUtils.getFilenameExtension(imageUrl.getOriginalFilename());
			String saveName = uid.toString() + "." + extension;
			
			File file = new File(parent.getAbsolutePath(), saveName);
			
			imageUrl.transferTo(file);
			
			product.setImageUrl(saveName);
		} else {
			log.info("No file uploaded - 파일이 업로드 되지 않음");
		}
		
		productService.addProduct(product);
		
		return "redirect:mainList";
	}
	
	@GetMapping("/addProductForm")
	public String addProductForm(Model model) {
		return "views/addProduct";
	}
	
	@GetMapping("/orderComplete")
	public String orderComplete(Model model,
			@RequestParam(value = "merchantUid", required = true) String merchantUid) {
		
		model.addAttribute("payment", paymentService.getPaymentList(merchantUid));
		model.addAttribute("order", paymentService.getOrderList(merchantUid));
		model.addAttribute("shipping", paymentService.getShippingList(merchantUid));
		
		return "views/orderComplete";
	}
	
	@GetMapping("/productList")
	public String productList(Model model,
		@RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
		@RequestParam(value = "type", required = false, defaultValue = "null") String type,
		@RequestParam(value = "keyword", required = false, defaultValue = "null") String keyword,
		@RequestParam(value = "category", required = false, defaultValue = "0") int category,
		@RequestParam(value = "sortBy", required = false, defaultValue = "0") String sortBy) {
		
		Map<String, Object> modelMap = productService.productList(pageNum, type, keyword, category, sortBy);
		
		model.addAllAttributes(modelMap);
		
		return "views/productList";
	}
	
	@GetMapping({"/", "/mainList"})
	public String mainList(Model model) {
		 List<Product> newList = productService.mainList("default");
		 List<Product> bestList = productService.mainList("name");
		
		model.addAttribute("nList", newList);
		model.addAttribute("bList", bestList);
		return "views/mainList";
	}
	
	@GetMapping("/productDetail")
	public String getProduct(Model model,
			@RequestParam("no") int no,
			@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
			@RequestParam(value = "type", defaultValue = "null") String type,
			@RequestParam(value = "keyword", defaultValue = "null") String keyword,
			@RequestParam(value = "category", defaultValue = "0") int category) {
		
		boolean searchOption = (type.equals("null") || keyword.equals("null")) ? false : true;
		Product product = productService.getProduct(no);
		List<Product> pList = productService.categoryList(category, "default");
		
		model.addAttribute("product", product);
		model.addAttribute("pList", pList);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("searchOption", searchOption);
		model.addAttribute("category", category);
		
		if (searchOption) {
			model.addAttribute("type", type);
			model.addAttribute("keyword", keyword);
		}
		return "views/productDetail";
	}
	
}
