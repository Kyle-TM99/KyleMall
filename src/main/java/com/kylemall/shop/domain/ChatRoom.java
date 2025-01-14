package com.kylemall.shop.domain;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class ChatRoom {
    private String roomId;
    private String roomName;
    private String createdBy;
    private String roomPassword;  // 비공개방 비밀번호
    private int maxUsers;         // 최대 참여 인원
    private int currentUsers;     // 현재 참여 인원
    private Timestamp createdAt;
    
    // 비밀번호 설정 여부 확인
    public boolean isPrivate() {
        return roomPassword != null && !roomPassword.isEmpty();
    }
} 