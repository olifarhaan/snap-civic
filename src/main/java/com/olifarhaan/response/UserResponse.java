package com.olifarhaan.response;

import java.time.LocalDate;

import com.olifarhaan.model.Address;
import com.olifarhaan.model.User;
import com.olifarhaan.model.User.Gender;
import com.olifarhaan.model.User.Role;

import lombok.Getter;

@Getter
public class UserResponse {
    private String id;
    private String fullName;
    private String email;
    private Address address;
    private Role role;
    private Gender gender;
    private String phoneNumber;
    private String profilePictureUrl;
    private LocalDate dateOfBirth;

    public UserResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.role = user.getRole();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.profilePictureUrl = user.getProfilePictureUrl();
        this.dateOfBirth = user.getDateOfBirth();
    }
}
