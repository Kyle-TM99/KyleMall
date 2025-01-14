package com.kylemall.shop.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.kylemall.shop.domain.Member;

@Mapper
public interface MemberMapper {

	// 회원 id에 해당하는 회원 정보를 삭제하는 메서드
	public void deleteMember(String memberId);
	
	// 회원 ID에 해당하는 회원 정보를 member 테이블에서 읽어와 반환하는 메서드
	public Member getMember(String id);	
	  
	// 회원 정보를 회원 테이블에 저장하는 메서드
	public void addMember(Member member);
	
	// 회원 정보 수정 시에 기존 비밀번호가 맞는지 체크하는 메서드
	public String memberPassCheck(String id);
	
	// 회원 정보를 회원 테이블에서 수정하는 메서드
	public void updateMember(Member member);
	
}
