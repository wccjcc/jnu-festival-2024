package com.jnu.festival.domain.timecapsule.entity;

import com.jnu.festival.domain.user.entity.User;
import com.jnu.festival.global.util.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "timecapsule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Timecapsule extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "mail_address", nullable = false)
    private String mailAddress;

    @Column(name = "content", nullable = false, length = 500)
    private String content;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Builder
    public Timecapsule(User user, String mailAddress, String content, Boolean isPublic) {
        this.user = user;
        this.mailAddress = mailAddress;
        this.content = content;
        this.isPublic = isPublic;
    }
}
