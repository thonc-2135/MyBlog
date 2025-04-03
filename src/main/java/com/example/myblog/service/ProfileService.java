package com.example.myblog.service;

import com.example.myblog.domain.profile.ProfileDto;
import com.example.myblog.security.AuthUserDetails;

public interface ProfileService {
    ProfileDto getProfile(final String username, final AuthUserDetails authUserDetails);

    ProfileDto followUser(final String name, final AuthUserDetails authUserDetails);

    ProfileDto unfollowUser(final String name, final AuthUserDetails authUserDetails);

    ProfileDto getProfileByUserId(Long userId, AuthUserDetails authUserDetails);
}
