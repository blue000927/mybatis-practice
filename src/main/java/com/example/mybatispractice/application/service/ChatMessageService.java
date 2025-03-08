package com.example.mybatispractice.application.service;

import com.example.mybatispractice.presentation.dto.request.ChatMessageRequest;
import com.example.mybatispractice.presentation.dto.response.ChatMessageResponse;
import com.example.mybatispractice.presentation.dto.response.ChatMessageErrorResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageService {

    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(ChatMessageRequest request, String destination) {
        try {
            ChatMessageResponse response = ChatMessageResponse.builder()
                    .groupId(request.groupId())
                    .senderId(request.senderId())
                    .content(request.content())
                    .timestamp(LocalDateTime.now())
                    .build();
            messagingTemplate.convertAndSend(destination, response);
            log.info("Message sent to destination {}: {}", destination, request.content());
        } catch (Exception e) {
            log.error("예상치 못한 메시지 처리 오류 - 대상: {}, 원인: {}", destination, e.getMessage(), e);

            ChatMessageErrorResponse errorResponse = ChatMessageErrorResponse.builder()
                    .errorMessage("메시지 처리 중 오류가 발생했습니다.")
                    .destination(destination)
                    .build();

            messagingTemplate.convertAndSendToUser(
                String.valueOf(request.senderId()),
                "/queue/errors",
                errorResponse
            );
        }
    }
}