package com.example.myblog.service;


import com.example.myblog.domain.profile.FollowEntity;
import com.example.myblog.domain.profile.FollowRepository;
import com.example.myblog.domain.profile.ProfileDto;
import com.example.myblog.domain.user.UserEntity;
import com.example.myblog.domain.user.UserRepository;
import com.example.myblog.exception.AppException;
import com.example.myblog.security.AuthUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.myblog.exception.Error;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    @Override
    public ProfileDto getProfile(String name, AuthUserDetails authUserDetails) {
        UserEntity user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = followRepository.findByFolloweeIdAndFollowerId(user.getId(), authUserDetails.getId()).isPresent();

        return convertToProfile(user, following);
    }

    @Transactional
    @Override
    public ProfileDto followUser(String name, AuthUserDetails authUserDetails) {
        UserEntity followee = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder().id(authUserDetails.getId()).build(); // myself

        followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .ifPresent(follow -> {throw new AppException(Error.ALREADY_FOLLOWED_USER);});

        FollowEntity follow =  FollowEntity.builder().followee(followee).follower(follower).build();
        followRepository.save(follow);

        return convertToProfile(followee, true);
    }

    @Transactional
    @Override
    public ProfileDto unfollowUser(String name, AuthUserDetails authUserDetails) {
        UserEntity followee = userRepository.findByUsername(name).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        UserEntity follower = UserEntity.builder().id(authUserDetails.getId()).build(); // myself

        FollowEntity follow = followRepository.findByFolloweeIdAndFollowerId(followee.getId(), follower.getId())
                .orElseThrow(() -> new AppException(Error.FOLLOW_NOT_FOUND));
        followRepository.delete(follow);

        return convertToProfile(followee, false);
    }

    @Override
    public ProfileDto getProfileByUserId(Long userId, AuthUserDetails authUserDetails) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AppException(Error.USER_NOT_FOUND));
        Boolean following = followRepository.findByFolloweeIdAndFollowerId(user.getId(), authUserDetails.getId()).isPresent();

        return convertToProfile(user, following);
    }

    private ProfileDto convertToProfile(UserEntity user, Boolean following) {
        return ProfileDto.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(following)
                .build();
    }
}
