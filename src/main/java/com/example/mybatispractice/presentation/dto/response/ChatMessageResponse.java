package com.example.mybatispractice.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageResponse(
        Long groupId,
        Long senderId,
        String content,
        LocalDateTime timestamp
) {}
