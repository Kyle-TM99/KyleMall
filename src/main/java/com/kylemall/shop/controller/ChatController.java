package com.kylemall.shop.controller;

import com.kylemall.shop.domain.ChatMessage;
import com.kylemall.shop.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
import java.util.List;
import jakarta.servlet.http.HttpSession;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatMapper chatMapper;

    @GetMapping("/chat")
    public String chatPage(Model model, HttpSession session) {
        // 로그인 체크
        if(session.getAttribute("member") == null) { // 세션에 회원 정보가 없으면 로그인 페이지로 이동
            return "redirect:/loginForm";   // 로그인 페이지로 이동
        }

        try {
            // 최근 메시지 50개 불러오기
            List<ChatMessage> recentMessages = chatMapper.getRecentMessages("public", 50); // 최근 메시지 50개 불러오기
            model.addAttribute("recentMessages", recentMessages); // 모델에 최근 메시지 추가
        } catch (Exception e) {
            log.error("Error loading chat messages", e); // 오류 로깅
        }
        return "views/chat"; // 채팅 페이지로 이동
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        try {
            log.info("Received message: {}", chatMessage); // 메시지 로깅   
            chatMessage.setType("CHAT"); // 메시지 유형 설정
            chatMessage.setSentAt(new Timestamp(System.currentTimeMillis())); // 메시지 전송 시간 설정
            chatMapper.insertMessage(chatMessage); // 메시지 저장
        } catch (Exception e) {
            log.error("Error saving chat message", e); // 오류 로깅
        }
        return chatMessage; // 메시지 반환
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, 
                             SimpMessageHeaderAccessor headerAccessor) {
        try {
            log.info("User joined: {}", chatMessage.getSender()); // 사용자 추가 로깅
            headerAccessor.getSessionAttributes().put("username", chatMessage.getSender()); // 세션에 사용자 이름 추가
            chatMessage.setType("JOIN"); // 메시지 유형 설정
            chatMessage.setSentAt(new Timestamp(System.currentTimeMillis())); // 메시지 전송 시간 설정
            chatMapper.insertMessage(chatMessage); // 메시지 저장
        } catch (Exception e) {
            log.error("Error saving join message", e); // 오류 로깅
        }
        return chatMessage; // 메시지 반환
    }
} 