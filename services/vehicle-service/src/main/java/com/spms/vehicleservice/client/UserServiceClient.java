package com.spms.vehicleservice.client;

import com.spms.vehicleservice.exceptions.ServiceUnavailableException;
import com.spms.vehicleservice.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient webClient;

    @Value("${user-service.url:http://localhost:8005}")
    private String userServiceBaseUrl;

    public void validateUserExists(Long userId) {
        try {
            webClient.get()
                .uri(userServiceBaseUrl + "/user/{id}", userId)
                .retrieve()
                .onStatus(status -> status.value() == 404,
                    response -> Mono.error(new UserNotFoundException("User not found with id: " + userId)))
                .onStatus(status -> status.isError(),
                    response -> Mono.error(new ServiceUnavailableException("User service returned an unexpected status")))
                .bodyToMono(Void.class)
                .block();
        } catch (UserNotFoundException | ServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }
}