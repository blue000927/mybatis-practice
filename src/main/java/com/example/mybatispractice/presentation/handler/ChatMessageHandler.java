package com.example.mybatispractice.presentation.handler;

import com.example.mybatispractice.application.service.ChatMessageService;
import com.example.mybatispractice.presentation.dto.request.ChatMessageRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageHandler {

    private static final String GROUP_DESTINATION = "/topic/group/%s";
    private final ChatMessageService chatMessageService;

    @MessageMapping("/chat/message")
    public void handle(@Valid ChatMessageRequest message) {
        String destination = String.format(GROUP_DESTINATION, message.groupId());
        chatMessageService.sendMessage(message, destination);
    }
}
