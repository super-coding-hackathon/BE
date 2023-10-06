package com.supercoding.hackathon01.entity;

import com.supercoding.hackathon01.dto.user.request.SignupRequest;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 45)
    @NotNull
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @NotNull
    @Lob
    @Column(name = "password", nullable = false)
    private String password;

    @Size(max = 45)
    @NotNull
    @Column(name = "nickname", nullable = false, length = 45)
    private String nickname;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", insertable = false)
    private Instant updatedAt;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    public static User from(SignupRequest signupRequest, String passwordEnc) {
        return User.builder()
                .email(signupRequest.getEmail())
                .password(passwordEnc)
                .nickname(signupRequest.getNickname())
                .isDeleted(false)
                .build();
    }

}