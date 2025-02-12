package com.recovery.managementsystem.service;

import com.recovery.managementsystem.model.Employee;
import com.recovery.managementsystem.model.PasswordResetToken;
import com.recovery.managementsystem.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
@Transactional
public class PasswordResetService {
    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmployeeService employeeService;

 
    public void createPasswordResetToken(Employee employee) {
        // Check if there's already an existing token for this employee
        PasswordResetToken existingToken = tokenRepository.findByEmployeeId(employee.getId());

        // If an existing token exists, remove it from the employee entity
        if (existingToken != null) {
            // Remove the token from the employee
            employee.setPasswordResetToken(null);
            tokenRepository.delete(existingToken);
        }

        // Create a new reset token
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, employee);

        // Save the new reset token to the database
        tokenRepository.save(resetToken);

        // Generate the reset link
        String resetLink = "http://localhost:8080/reset-password?token=" + token;

        // Send email to the employee with the reset link
        employeeService.sendEmail(employee.getEmail(), "Reset Password", "Click the link to reset your password: " + resetLink);
    }

    


    public PasswordResetToken validateToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}