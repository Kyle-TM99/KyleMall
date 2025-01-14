package com.kylemall.shop.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ChatMessage {
    private Long messageId;      // 메시지 ID
    private String type;         // 메시지 타입 (CHAT, JOIN, LEAVE)
    private String roomId;       // 채팅방 ID
    private String sender;       // 보내는 사람
    private String nickname;     // 닉네임 필드 추가
    private String message;      // 메시지
    private Timestamp sentAt;    // 전송 시간

    public ChatMessage() {
        this.roomId = "public";  // 기본값으로 public 채팅방 설정
    }

    public ChatMessage(String type, String sender, String message) {
        this();
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.sentAt = new Timestamp(System.currentTimeMillis());
    }
} 