package com.kylemall.shop.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.domain.OrderSummary;
import com.kylemall.shop.service.MemberService;
import com.kylemall.shop.service.PaymentService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Controller
@SessionAttributes("member")
@Slf4j
public class MemberController {
	
	// 회원 관련 Business 로직을 담당하는 객체를 의존성 주입하도록 설정
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping("/joinForm")
	public String memberJoinForm(Model model) {
		return "member/memberJoinForm";
	}

	@GetMapping("/deleteMember")
	public String deleteMember(Model model, HttpSession session) {
		
		Member member = (Member) session.getAttribute("member");
		session.setAttribute("isLogin", false);
		memberService.deleteMember(member.getId());
		
		return "redirect:/mainList";
	}
	
	@GetMapping("/myPage")
	public String myPage(Model model, HttpSession session) {
		
		Member member = (Member) session.getAttribute("member");
		
		List<OrderSummary> osList = paymentService.getAllOrderSummary(member.getId());
		
		
		model.addAttribute(member);
		model.addAttribute("totalSpent" ,paymentService.memberTotalSpent(member.getId()));
		model.addAttribute("orderSummary", paymentService.getAllOrderSummary(member.getId()));
		
		return "member/myPage";
	}
	 
	@PostMapping("/login")	
	public String login(Model model, @RequestParam("userId") String id, 
			@RequestParam("userPass") String pass, 
			HttpSession session, HttpServletResponse response) 
					throws ServletException, IOException {
		log.info(id);
		log.info(pass);
		log.info("MemberController.login()");
		
		// MemberService 클래스를 사용해 로그인 성공여부 확인
		int result = memberService.login(id, pass);
		
		if(result == -1) { // 회원 아이디가 존재하지 않으면
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("	alert('존재하지 않는 아이디 입니다.');");
			out.println("	history.back();");
			out.println("</script>");
			return null;
			
		} else if(result == 0) { // 비밀번호가 틀리면
			response.setContentType("text/html; charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("	alert('비밀번호가 다릅니다.');");
			out.println("	location.href='loginForm'");
			out.println("</script>");
			
			return null;
		}		
		
		Member member = memberService.getMember(id);
		session.setAttribute("isLogin", true);
	
		model.addAttribute("member", member);
		
		return "redirect:/mainList";
	}
	
	@GetMapping("/memberLogout")
	public String logout(HttpSession session) {
		session.invalidate();  // 세션 완전히 제거
		return "redirect:/loginForm";
	}
	
	@RequestMapping("/overlapIdCheck")
	//@GetMapping("/overlapIdCheck")
	public String overlapIdCheck(Model model, @RequestParam("id") String id) {		
		
		// 회원 아이디 중복 여부를 받아 온다.
		boolean overlap = memberService.overlapIdCheck(id);
		
		// model에 회원 ID와 회원 ID 중복 여부를 저장한다. 
		model.addAttribute("id", id);
		model.addAttribute("overlap", overlap);
		
		// 뷰 페이지만 먼저 작성해 새 창으로 잘 표시되는지 확인해 보자. 
		return "member/overlapIdCheck";
	}
	
	// 회원가입 폼에서 들어오는 회원가입 요청을 처리하는 메서드	
	@PostMapping("/joinResult")
	public String joinResult(Model model, Member member,
			@RequestParam("pass1") String pass1, 
			@RequestParam("emailId") String emailId, 
			@RequestParam("emailDomain") String emailDomain,
			@RequestParam("mobile1") String mobile1, 
			@RequestParam("mobile2") String mobile2, 
			@RequestParam("mobile3") String mobile3,
			@RequestParam(value="emailGet", required=false, 
				defaultValue="false")boolean emailGet) {	
		
		member.setPass(pass1);
		member.setEmail(emailId + "@" + emailDomain);
		member.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
		member.setEmailGet(Boolean.valueOf(emailGet));
		member.setSocialType("none");
		member.setSocial(false);
		member.setProfileImage("/static/images/defaultProfile.png");
		
		// MemberService를 통해서 회원 가입 폼에서 들어온 데이터를 DB에 저장한다.
		memberService.addMember(member);
		
		// 로그인 폼으로 리다이렉트 시킨다.
		return "redirect:loginForm";
	}

	// 회원 정보 수정 폼 요청을 처리하는 메서드
	@GetMapping("/memberUpdateForm")
	public String updateForm() {
		return "member/memberUpdateForm";
	}	
	
	// 회원 수정 폼에서 들어오는 요청을 처리하는 메서드
	@PostMapping("/memberUpdateResult")
	public String memberUpdateInfo(Model model, Member member,
			@RequestParam("pass1") String pass1, 
			@RequestParam("emailId") String emailId, 
			@RequestParam("emailDomain") String emailDomain,
			@RequestParam("mobile1") String mobile1, 
			@RequestParam("mobile2") String mobile2,
			@RequestParam("mobile3") String mobile3,
			@RequestParam(value="emailGet", required=false, 
				defaultValue="false")boolean emailGet) {
		
		member.setPass(pass1);
		member.setEmail(emailId + "@" + emailDomain);
		member.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
		member.setEmailGet(Boolean.valueOf(emailGet));	
			
		// MemberService를 통해서 회원 수정 폼에서 들어온 데이터를 DB에서 수정한다.
		memberService.updateMember(member);		

		model.addAttribute("member", member);
		
		// 게시글 리스트로 리다이렉트 시킨다.
		return "redirect:mainList";
	}

	@GetMapping("/loginForm")
	public String loginForm(HttpSession session) {
		// 이미 로그인된 상태면 메인으로 리다이렉트
		Boolean isLogin = (Boolean) session.getAttribute("isLogin");
		if (isLogin != null && isLogin) {
			return "redirect:/mainList";
		}
		return "member/loginForm";
	}
}
