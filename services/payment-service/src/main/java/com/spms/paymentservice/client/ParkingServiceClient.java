package com.spms.paymentservice.client;

import com.spms.paymentservice.exceptions.BookingNotFoundException;
import com.spms.paymentservice.exceptions.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ParkingServiceClient {

    private final WebClient.Builder webClientBuilder;

    public void validateParkingExists(Long parkingId) {
        try {
            webClientBuilder.build()
                .get()
                .uri("lb://parking-service/parking/{id}", parkingId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                    response -> Mono.error(new BookingNotFoundException("Booking not found with id: " + parkingId)))
                .onStatus(status -> status.isError(),
                    response -> Mono.error(new ServiceUnavailableException("Parking service returned an unexpected status")))
                .bodyToMono(Void.class)
                .block();
        } catch (BookingNotFoundException | ServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Parking service is currently unavailable");
        }
    }
}