package com.spms.apigateway.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.spms.apigateway.exceptions.InvalidCredentialsException;
import com.spms.apigateway.exceptions.ServiceUnavailableException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final WebClient webClient;

    @Value("${user-service.url:http://localhost:8083}")
    private String userServiceBaseUrl;

    public UserLoginInfo login(String email, String password) {
        try {
            UserServiceResponse response = webClient.post()
                .uri(userServiceBaseUrl + "/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("email", email, "password", password))
                .retrieve()
                .onStatus(status -> status.value() == 401,
                    res -> Mono.error(new InvalidCredentialsException("Invalid email or password")))
                .onStatus(HttpStatusCode::isError,
                    res -> Mono.error(new ServiceUnavailableException("User service returned an unexpected status")))
                .bodyToMono(UserServiceResponse.class)
                .block();

            if (response == null || response.getData() == null || response.getData().getUser() == null) {
                throw new ServiceUnavailableException("User service returned an empty response");
            }

            UserServiceUser user = response.getData().getUser();
            return new UserLoginInfo(user.getId(), user.getName(), user.getEmail(), user.getRole());
        } catch (InvalidCredentialsException | ServiceUnavailableException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }

    @Data
    @NoArgsConstructor
    private static class UserServiceResponse {
        private int statusCode;
        private String message;
        private UserServiceData data;
    }

    @Data
    @NoArgsConstructor
    private static class UserServiceData {
        private String token;
        private UserServiceUser user;
    }

    @Data
    @NoArgsConstructor
    private static class UserServiceUser {
        @JsonProperty("_id")
        private String id;
        private String name;
        private String email;
        private String role;
    }

    @Data
    @AllArgsConstructor
    public static class UserLoginInfo {
        private String userId;
        private String name;
        private String email;
        private String role;
    }
}