package com.cue.demo.entities;

import com.cue.demo.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.*;


@Builder @AllArgsConstructor
@Entity @Table(name= "app_user")
@SQLDelete(sql="UPDATE users SET deleted = true WHERE id=?")
@SQLRestriction("deleted=false")
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.USER;

    private String username;

    private String passwordHash;

    @Column(name="profile_picture_url")
    private String profilePictureUrl;

    private String bio;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    private String email;
    @Builder.Default
    private String city = "Constanța";

    @OneToMany(mappedBy="user")
    @Builder.Default
    private Set<Review> reviews = new HashSet<>();

    @Builder.Default
    private boolean deleted = false;

    @CreationTimestamp
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdDateTime;

    protected User() {}

    public boolean isAdmin() {
        return this.role == UserRole.ADMIN;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, username='%s]", id, username
        );
    }
}