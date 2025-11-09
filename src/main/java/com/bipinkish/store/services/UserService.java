package com.bipinkish.store.services;

import com.bipinkish.store.config.JwtConfig;
import com.bipinkish.store.dto.*;
import com.bipinkish.store.entities.Role;
import com.bipinkish.store.exceptions.UserNotFoundException;
import com.bipinkish.store.exceptions.UserServiceException;
import com.bipinkish.store.exceptions.UserUnAuthorizedExcception;
import com.bipinkish.store.mappers.UserMapper;
import com.bipinkish.store.repositories.UserRepository;
import com.bipinkish.store.utils.HashUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;



    public List<UserDto> getAllUsers(String sortBy) {
        if (!Set.of("name", "email").contains(sortBy)) {
            sortBy = "name";
        }
        return userRepository.findAll(Sort.by(sortBy))
                .stream()
                .map(userMapper::toDto).toList();
    }

    public UserDto getUserById(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserServiceException("User not found");
        }
        return userMapper.toDto(user);
    }

    public UserDto createUser(NewUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserServiceException("Email is already in use");
        }
        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserServiceException("User not found");
        }
        userMapper.mapDtoToEntity(request, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void deleteUser(Long userId) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserServiceException("User not found");
        }
        userRepository.delete(user);
    }

    public void changePassword(Long userId, ChangePasswordRequest request) {
        var user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UserServiceException("User not found");
        }
        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new UserUnAuthorizedExcception("Old password is incorrect");
        }
        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }

    public String loginUser(LoginUserRequest request, HttpServletResponse response) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        var user =  userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        var accessToken = jwtService.generateAccessToken(user).toString();
        var refreshToken = jwtService.generateRefreshToken(user).toString();
        var hashedRefreshToken = HashUtils.sha256Hex(refreshToken);
        user.setRefreshToken(hashedRefreshToken);
        user.setSessionStartTime(new Date());
        saveUser(response, user, refreshToken);
        return accessToken;
    }

    public UserDto me() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Auth"+ authentication);
        var userId =  (Long)  authentication.getPrincipal();
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDto(user);
    }

    public String fetchAccessToken(String refreshToken, HttpServletResponse response) {
        var jwt = jwtService.parse(refreshToken);
        if(jwt == null || jwt.isExpired()) {
            throw new UserUnAuthorizedExcception("Invalid Refresh Token. Please try again");
        }
        var userId = jwt.getUserId();
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        var receivedRefreshToken = HashUtils.sha256Hex(refreshToken);
        var userRefreshToken = user.getRefreshToken();
        if(!receivedRefreshToken.equals(userRefreshToken)) {
            throw new UserUnAuthorizedExcception("Invalid Refresh Token. Please login again!");
        }
        if(isAbsoluteSessionEnded(user.getSessionStartTime())) {
            user.setRefreshToken(null);
            user.setSessionStartTime(null);
            user.setRefreshTokenExpiration(null);
            userRepository.save(user);
            throw new UserUnAuthorizedExcception("You've logged in for long time. Please login again!");
        }
        var newRefreshToken = jwtService.generateRefreshToken(user).toString();
        user.setRefreshToken(HashUtils.sha256Hex(newRefreshToken));
        saveUser(response, user, newRefreshToken);
        return jwtService.generateAccessToken(user).toString();
    }

    public void logoutUser(String refreshToken, HttpServletResponse response) {
        var jwt = jwtService.parse(refreshToken);
        if(jwt == null || jwt.isExpired()) {
            throw new UserUnAuthorizedExcception("Invalid Refresh Token. Please try again");
        }
        var userId = jwt.getUserId();
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setRefreshToken(null);
        user.setSessionStartTime(null);
        user.setRefreshTokenExpiration(null);
        userRepository.save(user);

        var cookie = new Cookie("refreshToken", "");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/auth/token");
        cookie.setMaxAge(0); // deletes cookie immediately
        response.addCookie(cookie);
    }

    private boolean isAbsoluteSessionEnded(Date sessionStartedAt) {
        Instant now = Instant.now();
        Instant sessionStartTime = sessionStartedAt.toInstant();
        return now.isAfter(sessionStartTime.plus(jwtConfig.getAbsoluteSessionInSeconds(), ChronoUnit.SECONDS));
    }

    private void saveUser(HttpServletResponse response, com.bipinkish.store.entities.User user, String refreshToken) {
        userRepository.save(user);
        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/token");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        return new User(user.getEmail(), user.getPassword(), authorities);
    }
}
