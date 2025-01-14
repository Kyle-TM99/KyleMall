package com.kylemall.shop.controller;

import com.kylemall.shop.domain.ChatMessage;
import com.kylemall.shop.domain.ChatRoom;
import com.kylemall.shop.domain.Member;
import com.kylemall.shop.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.HashMap;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;

    // 채팅방 목록 페이지
    @GetMapping("/chat")
    public String chatRooms(Model model, HttpSession session) {
        if(session.getAttribute("member") == null) {
            return "redirect:/loginForm";
        }
        
        List<ChatRoom> chatRooms = chatMapper.getAllChatRooms();
        model.addAttribute("chatRooms", chatRooms);
        return "views/chatRooms";
    }
    
    // 특정 채팅방 입장
    @GetMapping("/chat/{roomId}")
    public String chatRoom(@PathVariable String roomId, Model model, HttpSession session) {
        if(session.getAttribute("member") == null) {
            return "redirect:/loginForm";
        }
        
        ChatRoom room = chatMapper.getChatRoomById(roomId);
        if(room == null) {
            return "redirect:/chat";
        }
        
        // 참여자 수 제한 확인
        if(room.getCurrentUsers() >= room.getMaxUsers()) {
            model.addAttribute("error", "채팅방이 가득 찼습니다.");
            return "redirect:/chat";
        }
        
        Member member = (Member) session.getAttribute("member");
        
        // 이미 참여 중인지 확인
        if(!chatMapper.isParticipant(roomId, member.getId())) {
            chatMapper.addParticipant(roomId, member.getId());
            chatMapper.updateParticipantCount(roomId, room.getCurrentUsers() + 1);
        }
        
        List<ChatMessage> recentMessages = chatMapper.getRecentMessages(roomId, 50);
        model.addAttribute("room", room);
        model.addAttribute("recentMessages", recentMessages);
        return "views/chat";
    }
    
    // 채팅방 생성 API
    @PostMapping("/api/chat/rooms")
    @ResponseBody
    public Map<String, Object> createRoom(@RequestBody ChatRoom chatRoom, HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        chatRoom.setRoomId(UUID.randomUUID().toString());
        chatRoom.setCreatedBy(member.getId());
        chatRoom.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        chatRoom.setCurrentUsers(1);
        
        // 채팅방 생성
        chatMapper.createChatRoom(chatRoom);
        // 채팅방 생성 후 참여자 추가
        chatMapper.addParticipant(chatRoom.getRoomId(), member.getId());
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("roomId", chatRoom.getRoomId());
        return response;
    }
    
    // 비밀번호 확인 API
    @PostMapping("/api/chat/rooms/{roomId}/verify")
    @ResponseBody
    public Map<String, Object> verifyPassword(@PathVariable String roomId, 
                                            @RequestBody Map<String, String> request) {
        String password = request.get("password");
        String storedPassword = chatMapper.getRoomPassword(roomId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", password != null && password.equals(storedPassword));
        return response;
    }
    
    // WebSocket 메시지 처리
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        chatMessage.setSentAt(new Timestamp(System.currentTimeMillis()));
        chatMapper.insertMessage(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                             SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        chatMessage.setSentAt(new Timestamp(System.currentTimeMillis()));
        chatMapper.insertMessage(chatMessage);
        return chatMessage;
    }
} 