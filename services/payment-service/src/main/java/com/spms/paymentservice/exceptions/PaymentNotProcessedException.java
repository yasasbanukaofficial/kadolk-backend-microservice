package com.spms.paymentservice.exceptions;

public class PaymentNotProcessedException extends RuntimeException {
    public PaymentNotProcessedException(String message) {
        super(message);
    }
}
