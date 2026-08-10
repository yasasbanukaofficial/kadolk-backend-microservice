package com.spms.apigateway.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRes {
    private String token;
    private String userId;
    private String name;
    private String email;
    private String role;
}