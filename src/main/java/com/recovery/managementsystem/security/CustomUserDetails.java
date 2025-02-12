package com.recovery.managementsystem.security;

import com.recovery.managementsystem.model.Employee;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Employee employee;

    public CustomUserDetails(Employee employee) {
        this.employee=employee;
    }


@Override public Collection<? extends GrantedAuthority> getAuthorities() {
	return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + employee.getRole()));
}

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmployeeId(); 
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
