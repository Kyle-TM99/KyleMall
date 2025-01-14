package com.kylemall.shop.mapper;

import com.kylemall.shop.domain.ChatMessage;
import com.kylemall.shop.domain.ChatRoom;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface ChatMapper {
    void insertMessage(ChatMessage message);
    List<ChatMessage> getRecentMessages(String roomId, int limit);
    void createChatRoom(ChatRoom room);
    List<ChatRoom> getAllChatRooms();
    ChatRoom getChatRoomById(String roomId);
    void updateParticipantCount(String roomId, int count);
    String getRoomPassword(String roomId);
    void addParticipant(String roomId, String memberId);
    void removeParticipant(String roomId, String memberId);
    boolean isParticipant(String roomId, String memberId);
    int getParticipantCount(String roomId);
} 