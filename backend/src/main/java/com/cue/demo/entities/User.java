package com.cue.demo.entities;

import com.cue.demo.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.Set;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE) @Getter
    private Long id;

    private UserRole role;
    private String username;
    @Column(name="phone_number")
    private String phoneNumber;
    @Column(name="profile_picture_url")
    private String profilePictureUrl;

    private String bio;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    private String email;
    private String city;

    @OneToMany(mappedBy="user")
    private Set<Review> reviews;

    protected User() {}

    /**
     * Automatically sets the role member to user
     * @param username
     * @param firstName
     * @param lastName
     */
    public User (String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = UserRole.USER;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, username='%s]", id, username
        );
    }
}