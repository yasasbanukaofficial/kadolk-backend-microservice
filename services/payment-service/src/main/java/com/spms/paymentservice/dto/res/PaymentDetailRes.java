package com.spms.paymentservice.dto.res;

import com.spms.paymentservice.entity.PaymentMethod;
import com.spms.paymentservice.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class PaymentDetailRes {
    private Long id;
    private Long bookingId;
    private Long userId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private String cardLast4;
    private PaymentStatus status;
    private String receiptNumber;
    private LocalDateTime paidAt;
    private LocalDateTime updatedAt;
}
