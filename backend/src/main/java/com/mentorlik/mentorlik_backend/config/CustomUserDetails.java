package com.mentorlik.mentorlik_backend.config;

import com.mentorlik.mentorlik_backend.model.AdminProfile;
import com.mentorlik.mentorlik_backend.model.MentorProfile;
import com.mentorlik.mentorlik_backend.model.StudentProfile;
import com.mentorlik.mentorlik_backend.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;
    private final String role;

    public CustomUserDetails(AdminProfile admin) {
        this.user = admin;
        this.role = "ROLE_ADMIN";
    }

    public CustomUserDetails(MentorProfile mentor) {
        this.user = mentor;
        this.role = "ROLE_MENTOR";
    }

    public CustomUserDetails(StudentProfile student) {
        this.user = student;
        this.role = "ROLE_STUDENT";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
} 