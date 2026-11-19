package org.example.authservice.controlller;

import com.example.api.AuthApi;
import com.example.dto.ApiError;
import com.example.dto.LoginRequestDto;
import com.example.dto.LoginResponseDto;
import com.example.dto.RegisterRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import org.example.authservice.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AuthController implements AuthApi {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public ResponseEntity<String> register( RegisterRequestDto request) {
        authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @Override
    public ResponseEntity<LoginResponseDto> login( LoginRequestDto request) {
        LoginResponseDto response = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(response);
    }

}
