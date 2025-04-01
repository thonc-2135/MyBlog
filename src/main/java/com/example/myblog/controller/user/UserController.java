package com.example.myblog.controller.user;


import com.example.myblog.domain.user.UserDto;
import com.example.myblog.security.AuthUserDetails;
import com.example.myblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public UserDto currentUser(@AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return userService.currentUser(authUserDetails);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto.Update update, @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return userService.update(update, authUserDetails);
    }
}
