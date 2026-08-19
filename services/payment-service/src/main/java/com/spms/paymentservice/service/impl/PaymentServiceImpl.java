package com.spms.paymentservice.service.impl;

import com.spms.paymentservice.client.ParkingServiceClient;
import com.spms.paymentservice.client.UserServiceClient;
import com.spms.paymentservice.dto.req.PaymentSaveReq;
import com.spms.paymentservice.dto.req.PaymentUpdateReq;
import com.spms.paymentservice.dto.res.PaymentDetailRes;
import com.spms.paymentservice.dto.res.PaymentReceiptRes;
import com.spms.paymentservice.dto.res.PaymentSummaryRes;
import com.spms.paymentservice.entity.Payment;
import com.spms.paymentservice.entity.PaymentStatus;
import com.spms.paymentservice.exceptions.InvalidCardException;
import com.spms.paymentservice.exceptions.PaymentAlreadyPaidException;
import com.spms.paymentservice.exceptions.PaymentNotFoundException;
import com.spms.paymentservice.exceptions.PaymentNotProcessedException;
import com.spms.paymentservice.exceptions.PaymentNotRefundableException;
import com.spms.paymentservice.repo.PaymentRepo;
import com.spms.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepo paymentRepo;
    private final ModelMapper modelMapper;
    private final ParkingServiceClient parkingServiceClient;
    private final UserServiceClient userServiceClient;

    @Override
    public List<PaymentSummaryRes> getAll() {
        return paymentRepo.findAll().stream()
            .map(payment -> modelMapper.map(payment, PaymentSummaryRes.class))
            .toList();
    }

    @Override
    public PaymentDetailRes getById(Long id) {
        Payment payment = paymentRepo.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        return modelMapper.map(payment, PaymentDetailRes.class);
    }

    @Override
    public List<PaymentSummaryRes> getPaymentsByBookingId(Long bookingId) {
        return paymentRepo.getAllByBookingId(bookingId).stream()
            .map(payment -> modelMapper.map(payment, PaymentSummaryRes.class))
            .toList();
    }

    @Override
    public List<PaymentSummaryRes> getPaymentsByUserId(Long userId) {
        return paymentRepo.getAllByUserId(userId).stream()
            .map(payment -> modelMapper.map(payment, PaymentSummaryRes.class))
            .toList();
    }

    @Override
    public PaymentDetailRes save(PaymentSaveReq req) {
        parkingServiceClient.validateParkingExists(req.getBookingId());
        userServiceClient.validateUserExists(req.getUserId());
        validateMockCard(req);
        Payment payment = new Payment();
        payment.setBookingId(req.getBookingId());
        payment.setUserId(req.getUserId());
        payment.setAmount(req.getAmount());
        payment.setPaymentMethod(req.getPaymentMethod());
        payment.setCardLast4(req.getCardNumber().substring(req.getCardNumber().length() - 4));
        return modelMapper.map(paymentRepo.save(payment), PaymentDetailRes.class);
    }

    @Override
    public PaymentDetailRes update(Long id, PaymentUpdateReq req) {
        Payment payment = paymentRepo.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        payment.setAmount(req.getAmount());
        payment.setPaymentMethod(req.getPaymentMethod());
        payment.setStatus(req.getStatus());
        return modelMapper.map(paymentRepo.save(payment), PaymentDetailRes.class);
    }

    @Override
    public void delete(Long id) {
        Payment payment = paymentRepo.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        paymentRepo.delete(payment);
    }

    @Transactional
    @Override
    public PaymentReceiptRes pay(Long id) {
        Payment payment = paymentRepo.findByIdForUpdate(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        if (payment.getStatus() == PaymentStatus.PAID) {
            throw new PaymentAlreadyPaidException("Payment is already paid");
        }
        if (payment.getStatus() == PaymentStatus.REFUNDED) {
            throw new PaymentNotRefundableException("Payment is already refunded, cannot pay again");
        }
        simulateGateway(payment);
        return buildReceiptRes(paymentRepo.save(payment));
    }

    @Transactional
    @Override
    public PaymentReceiptRes refund(Long id) {
        Payment payment = paymentRepo.findByIdForUpdate(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new PaymentNotRefundableException("Payment is not paid, nothing to refund");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        return buildReceiptRes(paymentRepo.save(payment));
    }

    @Override
    public PaymentReceiptRes getReceipt(Long id) {
        Payment payment = paymentRepo.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        if (payment.getStatus() == PaymentStatus.PENDING) {
            throw new PaymentNotProcessedException("Payment is not processed yet, no receipt available");
        }
        return buildReceiptRes(payment);
    }

    private void validateMockCard(PaymentSaveReq req) {
        if (!isLuhnValid(req.getCardNumber())) {
            throw new InvalidCardException("Invalid card number");
        }
        if (!isExpiryValid(req.getExpiry())) {
            throw new InvalidCardException("Card has expired");
        }
    }

    private void simulateGateway(Payment payment) {
        if ("0000".equals(payment.getCardLast4())) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setReceiptNumber(generateReceiptNumber());
            return;
        }
        payment.setStatus(PaymentStatus.PAID);
        payment.setPaidAt(LocalDateTime.now());
        payment.setReceiptNumber(generateReceiptNumber());
    }

    private PaymentReceiptRes buildReceiptRes(Payment payment) {
        return PaymentReceiptRes.builder()
            .paymentId(payment.getId())
            .receiptNumber(payment.getReceiptNumber())
            .bookingId(payment.getBookingId())
            .userId(payment.getUserId())
            .amount(payment.getAmount())
            .paymentMethod(payment.getPaymentMethod())
            .cardLast4(payment.getCardLast4())
            .status(payment.getStatus())
            .paidAt(payment.getPaidAt())
            .build();
    }

    private String generateReceiptNumber() {
        return "RCP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean isLuhnValid(String cardNumber) {
        int sum = 0;
        boolean doubleDigit = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = cardNumber.charAt(i) - '0';
            if (doubleDigit) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            doubleDigit = !doubleDigit;
        }
        return sum % 10 == 0;
    }

    private boolean isExpiryValid(String expiry) {
        try {
            String[] parts = expiry.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = 2000 + Integer.parseInt(parts[1]);
            YearMonth exp = YearMonth.of(year, month);
            return !exp.isBefore(YearMonth.now());
        } catch (Exception e) {
            return false;
        }
    }
}
