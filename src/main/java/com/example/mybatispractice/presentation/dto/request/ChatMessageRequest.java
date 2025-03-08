package com.example.mybatispractice.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record ChatMessageRequest(
        @NotNull(message = "그룹 ID는 필수입니다")
        Long groupId,

        @NotNull(message = "발신자 ID는 필수입니다")
        Long senderId,

        @NotNull(message = "메시지 내용은 필수입니다")
        @Size(min = 1, max = 1000, message = "메시지는 1자 이상 1000자 이하여야 합니다")
        String content
) {}
