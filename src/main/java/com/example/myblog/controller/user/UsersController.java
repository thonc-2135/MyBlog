package com.example.myblog.controller.user;

import com.example.myblog.domain.user.UserDto;
import com.example.myblog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping
    public UserDto registration(@RequestBody @Valid UserDto.Registration registration) {
        return userService.registration(registration);
    }

    @PostMapping("/login")
    public UserDto login(@RequestBody @Valid UserDto.Login login) {
        return userService.login(login);
    }
}
