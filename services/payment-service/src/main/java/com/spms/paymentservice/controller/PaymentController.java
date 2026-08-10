package com.spms.paymentservice.controller;

import com.spms.paymentservice.dto.req.PaymentSaveReq;
import com.spms.paymentservice.dto.req.PaymentUpdateReq;
import com.spms.paymentservice.dto.res.PaymentDetailRes;
import com.spms.paymentservice.dto.res.PaymentReceiptRes;
import com.spms.paymentservice.dto.res.PaymentSummaryRes;
import com.spms.paymentservice.service.PaymentService;
import com.spms.paymentservice.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PaymentSummaryRes>>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved All Payment Details Successfully",
                paymentService.getAll()
            )
        );
    }

    @GetMapping("/receipt/{id}")
    public ResponseEntity<ApiResponse<PaymentReceiptRes>> getReceipt(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Payment Receipt Successfully",
                paymentService.getReceipt(id)
            )
        );
    }

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<PaymentSummaryRes>>> getPaymentsByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Payment Details by Booking Successfully",
                paymentService.getPaymentsByBookingId(bookingId)
            )
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaymentSummaryRes>>> getPaymentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Payment Details by User Successfully",
                paymentService.getPaymentsByUserId(userId)
            )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDetailRes>> getById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Retrieved Payment Details Successfully",
                paymentService.getById(id)
            )
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentDetailRes>> save(@Valid @RequestBody PaymentSaveReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "Payment Created Successfully",
                paymentService.save(req)
            )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentDetailRes>> update(@PathVariable Long id, @Valid @RequestBody PaymentUpdateReq req) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Payment Updated Successfully",
                paymentService.update(id, req)
            )
        );
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<ApiResponse<PaymentReceiptRes>> pay(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Payment Processed Successfully",
                paymentService.pay(id)
            )
        );
    }

    @PostMapping("/{id}/refund")
    public ResponseEntity<ApiResponse<PaymentReceiptRes>> refund(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Payment Refunded Successfully",
                paymentService.refund(id)
            )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        paymentService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
            new ApiResponse<>(
                HttpStatus.OK.value(),
                "Payment Deleted Successfully",
                null
            )
        );
    }
}
