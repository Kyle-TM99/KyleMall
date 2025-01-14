package com.kylemall.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import lombok.extern.slf4j.Slf4j;
import com.kylemall.shop.service.MemberService;

import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.kylemall.shop.domain.Member;
import java.util.UUID;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequestMapping("/google")
public class GoogleController {

    @Autowired
    private MemberService memberService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/callback")
    public String handleGoogleCallback(HttpServletRequest request, HttpSession session, Model model) {
        log.info("GoogleController.handleGoogleCallback() 실행");
        
        OAuth2User oauth2User = (OAuth2User) request.getAttribute("oauth2User");
        if (oauth2User == null) {
            log.error("OAuth2User is null");
            return "redirect:/loginForm?error=auth";
        }
        
        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");
            
            log.info("Google OAuth2 User 정보: email={}, name={}", email, name);
            
            // 기존 회원인지 확인
            Member member = memberService.getMember(email);
            
            if (member == null) {
                // 신규 회원인 경우 추가 정보 입력 페이지로 이동
                model.addAttribute("googleEmail", email);
                model.addAttribute("googleName", name);
                model.addAttribute("googlePicture", picture);
                return "/member/googleAdditionalInfo";
            } else {
                // 기존 회원인 경우 로그인 처리
                session.setAttribute("member", member);
                session.setAttribute("isLogin", true);
                return "redirect:/mainList";
            }
            
        } catch (Exception e) {
            log.error("Google 로그인 처리 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/loginForm?error=google";
        }
    }

    @GetMapping("/logout")
public String logout(HttpServletRequest request, HttpServletResponse response) {
    // Spring Security의 로그아웃 처리
    SecurityContextHolder.clearContext();
    
    // 세션 무효화
    HttpSession session = request.getSession(false);
    if (session != null) {
        session.invalidate();
    }
    
    return "redirect:/loginForm";
}

    @PostMapping("/register")
    public String registerGoogleMember(
            @RequestParam("id") String googleId,
            @RequestParam("name") String name,
            @RequestParam("nickname") String nickname,
            @RequestParam(name = "emailId") String emailId,
            @RequestParam(name = "emailDomain") String emailDomain,
            @RequestParam("mobile1") String mobile1,
            @RequestParam("mobile2") String mobile2,
            @RequestParam("mobile3") String mobile3,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address1") String address1,
            @RequestParam("address2") String address2,
            @RequestParam(value = "emailGet", defaultValue = "false") boolean emailGet,
            @RequestParam(name = "profileImage", required = false) String profileImage,
            HttpSession session) {
        
        try {
            log.info("Registering new Google member: {}", googleId);
            
            // 1. 비밀번호 암호화
            String temporaryPassword = googleId + UUID.randomUUID().toString().substring(0, 8);
            String encodedPassword = passwordEncoder.encode(temporaryPassword);

            Member newMember = new Member();
            newMember.setId(googleId);
            newMember.setPass(encodedPassword);
            newMember.setName(name);
            newMember.setNickname(nickname);
            newMember.setEmail(emailId + "@" + emailDomain);
            newMember.setMobile(mobile1 + "-" + mobile2 + "-" + mobile3);
            newMember.setZipcode(zipcode);
            newMember.setAddress1(address1);
            newMember.setAddress2(address2);
            newMember.setEmailGet(emailGet);
            newMember.setProfileImage(profileImage);
            newMember.setSocial(true);
            newMember.setSocialType("google");
            
            memberService.addMember(newMember);
            
            session.setAttribute("member", newMember);
            session.setAttribute("isLogin", true);
            
            return "redirect:/mainList";
            
        } catch (Exception e) {
            log.error("Google 회원가입 중 오류 발생: {}", e.getMessage(), e);
            return "redirect:/loginForm?error=register";
        }
    }
} 