package com.cue.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private Long id;
    @Getter
    private String username;
    private String phoneNumber;
    private String profilePictureUrl;

    private String bio;
    private String firstName;
    private String lastName;
    private String email;
    private String city;

    protected User() {}

    public User (String username, String firstName, String lastName) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public User(Long id, String username, String phoneNumber,
                String profilePictureUrl, String bio, String firstName,
                String lastName, String email, String city) {
        this.id = id;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.profilePictureUrl = profilePictureUrl;
        this.bio = bio;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.city = city;
    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%d, username='%s]", id, username
        );
    }
}