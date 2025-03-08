package com.example.mybatispractice.presentation.dto.response;

import lombok.Builder;

@Builder
public record ChatMessageErrorResponse(
    String errorMessage,
    String destination
) {} 