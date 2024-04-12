package com.volkan.controller;


import com.volkan.dto.request.LoginRequestDto;
import com.volkan.dto.request.RegisterRequestDto;
import com.volkan.dto.request.ResetPasswordRequestDto;
import com.volkan.dto.request.UpdateRoleRequestDto;
import com.volkan.exception.AuthServiceException;
import com.volkan.exception.EErrorType;
import com.volkan.repository.entity.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.volkan.constants.EndPoints.*;
import com.volkan.service.AuthService;

import java.util.List;


@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(REGISTER)
    public ResponseEntity<Auth> register(@RequestBody @Valid RegisterRequestDto dto) {
        if(!dto.getPassword().equals(dto.getRepassword()))
            throw new AuthServiceException(EErrorType.REGISTER_ERROR_PASSWORD_UNMATCHED);
        return ResponseEntity.ok(authService.register(dto));
    }

    @PostMapping(LOGIN)
    public ResponseEntity<String> login(@RequestBody LoginRequestDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    @GetMapping(FIND_ALL)
    public ResponseEntity<List<Auth>> findAll() {
        return ResponseEntity.ok(authService.findAll());
    }

    @GetMapping(FORGOT_PASSWORD)
    public ResponseEntity<String>forgotMyPassword(@RequestParam String email){
        return ResponseEntity.ok(authService.forgotMyPassword(email));
    }

    @PatchMapping(RESET_PASSWORD)
    public ResponseEntity<Boolean>resetPassword(@RequestBody @Valid ResetPasswordRequestDto dto) {
        return ResponseEntity.ok(authService.resetPassword(dto));
    }

    @PatchMapping(DELETE)
    public ResponseEntity<Boolean> deleteAuth(@RequestParam Long id){
        return ResponseEntity.ok(authService.deleteAuth(id));
    }

    @PutMapping(UPDATE)
    public ResponseEntity<Boolean> updateAuth(@RequestBody @Valid UpdateRoleRequestDto dto){
        return ResponseEntity.ok(authService.updateAuth(dto));
    }

}
