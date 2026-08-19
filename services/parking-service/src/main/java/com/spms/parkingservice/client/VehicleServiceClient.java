package com.spms.parkingservice.client;

import com.spms.parkingservice.exceptions.ServiceUnavailableException;
import com.spms.parkingservice.exceptions.VehicleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class VehicleServiceClient {

    private final WebClient.Builder webClientBuilder;

    public void validateVehicleExists(Long vehicleId) {
        try {
            webClientBuilder.build()
                .get()
                .uri("lb://vehicle-service/vehicle/{id}", vehicleId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                    response -> Mono.error(new VehicleNotFoundException("Vehicle not found with id: " + vehicleId)))
                .onStatus(status -> status.isError(),
                    response -> Mono.error(new ServiceUnavailableException("Vehicle service returned an unexpected status")))
                .bodyToMono(Void.class)
                .block();
        } catch (VehicleNotFoundException | ServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceUnavailableException("Vehicle service is currently unavailable");
        }
    }
}