package com.example.myblog.domain.profile;

import com.example.myblog.domain.BaseEntity;
import com.example.myblog.domain.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "follows", uniqueConstraints = {
        @UniqueConstraint(name = "u_follow_followee_pair_must_be_unique", columnNames = {"followee", "follower"})
})
public class FollowEntity extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "followee")
    private UserEntity followee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "follower")
    private UserEntity follower;
}
