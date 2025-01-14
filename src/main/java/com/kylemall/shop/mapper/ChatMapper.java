package com.kylemall.shop.mapper;

import com.kylemall.shop.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChatMapper {
    void insertMessage(ChatMessage message);
    List<ChatMessage> getRecentMessages(String roomId, int limit);
} 