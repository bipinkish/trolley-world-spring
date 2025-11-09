package com.bipinkish.store.controllers;

import com.bipinkish.store.dto.ChangePasswordRequest;
import com.bipinkish.store.dto.NewUserRequest;
import com.bipinkish.store.dto.UpdateUserRequest;
import com.bipinkish.store.dto.UserDto;
import com.bipinkish.store.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(@RequestParam(required = false, name = "sort", defaultValue = "") String sortBy) {
      return  userService.getAllUsers(sortBy);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        var user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody NewUserRequest request, UriComponentsBuilder uriBuilder) {
        var newUser = userService.createUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).body(newUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserDto> updateUser(@PathVariable(name = "userId") Long userId, @RequestBody UpdateUserRequest request){
        var updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Long userId){
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable(name = "userId") Long userId,
                                               @RequestBody ChangePasswordRequest request){
        userService.changePassword(userId, request);
        return ResponseEntity.noContent().build();
    }


}
