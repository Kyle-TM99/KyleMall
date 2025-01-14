package com.kylemall.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kylemall.shop.domain.Member;
import com.kylemall.shop.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberService {
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;	
	
		public void deleteMember(String memberId) {

			memberMapper.deleteMember(memberId);
			
		}
	
	// 회원 로그인 요청을 처리하고 결과를 반환하는 메서드	
		public int login(String id, String pass) {
			int result = -1;
			log.info(id);
			
			Member m = memberMapper.getMember(id);
			// id가 존재하지 않으면 : -1
			
			if(m == null) {
				return result;
			}
			
			if(passwordEncoder.matches(pass, m.getPass())) {
				result = 1;
				
			} else { // 비밀번호가 틀리면 : 0
				result = 0;
			}
			
			return result;
		}
		
		// 회원 ID에 해당하는 회원 정보를 읽어와 반환하는 메서드	
		public Member getMember(String id) {
			return memberMapper.getMember(id);
		}
		
		// 회원 가입시 아이디 중복을 체크하는 메서드	
		public boolean overlapIdCheck(String id) {
			Member member = memberMapper.getMember(id);
			log.info("overlapIdCheck - member : " + member);
			if(member == null) {
				return false;
			}
			return true;
		}
		
		// 회원 정보를 MemberMapper를 이용해 회원 테이블에 저장하는 메서드	
		public void addMember(Member member) {
			
			// BCryptPasswordEncoder 객체를 이용해 비밀번호를 암호화한 후 저장
			member.setPass(passwordEncoder.encode(member.getPass()));
			
			log.debug("전화번호 저장 전: {}", member.getMobile());
			memberMapper.addMember(member);
			log.debug("전화번호 저장 후: {}", member.getMobile());
		}
		
		// 회원 정보 수정 시에 기존 비밀번호가 맞는지 체크하는 메서드
		public boolean memberPassCheck(String id, String pass) {		
			
			String dbPass = memberMapper.memberPassCheck(id);		
			boolean result = false;		
			
			if(passwordEncoder.matches(pass, dbPass)) {
				result = true;	
			}
			return result;	
		}
		
		// 회원 정보를 MemberMapper를 이용해 회원 테이블에서 수정하는 메서드
		public void updateMember(Member member) {
			
			// BCryptPasswordEncoder 객체를 이용해 비밀번호를 암호화한 후 저장
			member.setPass(passwordEncoder.encode(member.getPass()));
			
			memberMapper.updateMember(member);
		}
}
