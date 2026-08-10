package com.spms.apigateway.service;

import com.spms.apigateway.client.UserServiceClient;
import com.spms.apigateway.dto.req.LoginReq;
import com.spms.apigateway.dto.res.LoginRes;
import com.spms.apigateway.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;

    public LoginRes login(LoginReq req) {
        UserServiceClient.UserLoginInfo info = userServiceClient.login(req.getEmail(), req.getPassword());
        String token = jwtService.generateToken(info.getUserId(), info.getEmail(), info.getRole());
        return new LoginRes(token, info.getUserId(), info.getName(), info.getEmail(), info.getRole());
    }
}