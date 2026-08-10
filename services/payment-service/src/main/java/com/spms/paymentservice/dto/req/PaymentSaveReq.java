package com.spms.paymentservice.dto.req;

import com.spms.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor @NoArgsConstructor
@Data @Builder
public class PaymentSaveReq {

    @NotNull(message = "Booking id is required")
    @Positive(message = "Booking id must be a positive number")
    private Long bookingId;

    @NotNull(message = "User id is required")
    @Positive(message = "User id must be a positive number")
    private Long userId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than zero")
    private BigDecimal amount;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "Card number is required")
    @Pattern(regexp = "^\\d{13,19}$", message = "Card number must contain only digits and be between 13 and 19 characters")
    private String cardNumber;

    @NotBlank(message = "Card holder is required")
    @Size(min = 2, max = 50, message = "Card holder must be between 2 and 50 characters")
    @Pattern(regexp = "^[A-Za-z][A-Za-z\\s.'-]*$", message = "Card holder must contain only letters, spaces, dots, apostrophes and hyphens")
    private String cardHolder;

    @NotBlank(message = "Card expiry is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])/\\d{2}$", message = "Card expiry must be in MM/YY format")
    private String expiry;

    @NotBlank(message = "Card cvv is required")
    @Pattern(regexp = "^\\d{3,4}$", message = "Card cvv must be 3 or 4 digits")
    private String cvv;
}
