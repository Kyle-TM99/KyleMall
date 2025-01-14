package com.kylemall.shop.domain;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

@Getter
@Setter
public class ChatRoomParticipant {
    private String roomId;
    private String memberId;
    private Timestamp joinedAt;
} 