package com.spms.paymentservice.dto.req;

import com.spms.paymentservice.entity.PaymentMethod;
import com.spms.paymentservice.entity.PaymentStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class PaymentUpdateReq {

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Status is required")
    private PaymentStatus status;
}
