package com.spms.apigateway.controller;

import com.spms.apigateway.dto.req.LoginReq;
import com.spms.apigateway.dto.res.LoginRes;
import com.spms.apigateway.service.AuthService;
import com.spms.apigateway.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginRes>> login(@Valid @RequestBody LoginReq req) {
        return ResponseEntity.ok(new ApiResponse<>(200, "User Authenticated Successfully", authService.login(req)));
    }
}