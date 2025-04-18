package com.example.myblog.domain.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileDto {
    private String username;
    private String bio;
    private String image;
    private Boolean following;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Single {
        private ProfileDto profile;
    }
}
