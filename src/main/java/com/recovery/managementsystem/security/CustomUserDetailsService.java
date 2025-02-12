package com.recovery.managementsystem.security;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
       Employee value = employeeRepository.findByEmployeeId(id);
        logger.info("User : {}", value);
        if(value == null ){
        	logger.info("User not found");
            throw new UsernameNotFoundException("User not found!");
        }
        return new CustomUserDetails(value);
    }
}