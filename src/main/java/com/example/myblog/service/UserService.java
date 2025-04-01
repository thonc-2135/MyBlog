package com.example.myblog.service;

import com.example.myblog.domain.user.UserDto;
import com.example.myblog.security.AuthUserDetails;

public interface UserService {
    UserDto registration(final UserDto.Registration registration);

    UserDto login(final UserDto.Login login);

    UserDto currentUser(final AuthUserDetails authUserDetails);

    UserDto update(final UserDto.Update update, final AuthUserDetails authUserDetails);
}
