package com.bipinkish.store.controllers;

import com.bipinkish.store.dto.JwtResponse;
import com.bipinkish.store.dto.LoginUserRequest;
import com.bipinkish.store.dto.UserDto;
import com.bipinkish.store.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginUserRequest request, HttpServletResponse response) {
        var accessToken = userService.loginUser(request, response);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }

    @PostMapping("/token/logout")
    public ResponseEntity<Void>  logoutUser(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        System.out.println("Logout Refresh : "+ refreshToken);
        userService.logoutUser(refreshToken, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        System.out.println("Refresh Refresh : "+ refreshToken);
        var accessToken = userService.fetchAccessToken(refreshToken, response);
        return ResponseEntity.ok(new JwtResponse(accessToken));
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        return ResponseEntity.ok(userService.me());
    }

}
