package com.spms.paymentservice.dto.res;

import com.spms.paymentservice.entity.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class PaymentSummaryRes {
    private Long id;
    private Long bookingId;
    private Long userId;
    private BigDecimal amount;
    private PaymentStatus status;
    private String receiptNumber;
}
