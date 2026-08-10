package com.spms.paymentservice.exceptions;

public class PaymentAlreadyPaidException extends RuntimeException {
    public PaymentAlreadyPaidException(String message) {
        super(message);
    }
}
