package com.finpay.demo.dto.response;

public record PaymentResponse(
    String status,
    String instanceId,
    long durationMs,
    int totalObjCreatedAtHeap,
    String note
) {}
